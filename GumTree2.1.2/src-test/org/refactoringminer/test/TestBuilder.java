package org.refactoringminer.test;

import com.models.GumTreeResultsOuterClass.GumTreeResults;
import com.models.GumTreeResultsOuterClass.GumTreeResults.RefactoringReported;
import gr.uom.java.xmi.diff.CodeRange;
import org.junit.Assert;
import org.refactoringminer.api.*;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.rm1.GumTreeDiff.RefactoringInfo;
import org.refactoringminer.test.RefactoringPopulator.Refactorings;
import org.refactoringminer.util.GitServiceImpl;
import refdiff.core.ResultModels.ResultsOuterClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class TestBuilder {


    private final String tempDir;
    private final Map<String, ProjectMatcher> map;
    private List<RefactoringReported> refactoringReported = new ArrayList<>();
    private final GitHistoryRefactoringMiner refactoringDetector;
    private boolean verbose;
    private boolean aggregate;
    private int commitsCount;
    private int errorCommitsCount;
    private Counter c;// = new Counter();
    private Map<RefactoringType, Counter> cMap;
    private static final int TP = 0;
    private static final int FP = 1;
    private static final int FN = 2;
    private static final int TN = 3;
    private static final int UNK = 4;

    private BigInteger refactoringFilter;

    public TestBuilder(GitHistoryRefactoringMiner detector, String tempDir) {
        this.map = new HashMap<String, ProjectMatcher>();
        this.refactoringDetector = detector;
        this.tempDir = tempDir;
        this.verbose = false;
        this.aggregate = false;
    }

    public TestBuilder(GitHistoryRefactoringMiner detector, String tempDir, BigInteger refactorings) {
        this(detector, tempDir);

        this.refactoringFilter = refactorings;
    }

    public static RefactoringReported getAsRefactoringReported(RefactoringInfo ri, String sha, String cloneUrl) {
        return RefactoringReported.newBuilder()
                .setDescription(ri.getDescription())
                .setLeft(getProtoCodeRange(ri.getLeft()))
                .setRight(getProtoCodeRange(ri.getRight()))
                .setSha(sha)
                .setCloneUrl(cloneUrl)
                .build();
    }

    private static RefactoringReported.ProtoCodeRange getProtoCodeRange(CodeRange cr) {
        return RefactoringReported.ProtoCodeRange.newBuilder()
                .setCodeElementType(cr.getCodeElementType().name())
                .setStartColumn(cr.getStartColumn())
                .setEndColumn(cr.getEndColumn())
                .setStartLine(cr.getStartLine())
                .setEndLine(cr.getEndLine())
                .setFilePath(cr.getFilePath())
                .setDescription(cr.getDescription())
                .setCodeElement(cr.getCodeElement())
                .build();
    }

    public TestBuilder verbose() {
        this.verbose = true;
        return this;
    }

    public TestBuilder withAggregation() {
        this.aggregate = true;
        return this;
    }

    private static class Counter {
        int[] c = new int[5];
    }

    private void count(int type, String refactoring) {
        c.c[type]++;
        RefactoringType refType = RefactoringType.extractFromDescription(refactoring);
        Counter refTypeCounter = cMap.get(refType);
        if (refTypeCounter == null) {
            refTypeCounter = new Counter();
            cMap.put(refType, refTypeCounter);
        }
        refTypeCounter.c[type]++;
    }

    private int get(int type) {
        return c.c[type];
    }

    private int get(int type, Counter counter) {
        return counter.c[type];
    }

    public TestBuilder() {
        this(new GitHistoryRefactoringMinerImpl(), "tmp");
    }

    public final ProjectMatcher project(String cloneUrl, String branch) {
        ProjectMatcher projectMatcher = this.map.get(cloneUrl);
        if (projectMatcher == null) {
            projectMatcher = new ProjectMatcher(cloneUrl, branch);
            this.map.put(cloneUrl, projectMatcher);
        }
        return projectMatcher;
    }

    public void assertExpectations() throws Exception {
        c = new Counter();
        cMap = new HashMap<RefactoringType, Counter>();
        commitsCount = 0;
        errorCommitsCount = 0;
        GitService gitService = new GitServiceImpl();

        Set<String> alreadyParsed = TestBuilder.readAllResults().stream()
                .map(x -> x.getSha()).collect(toSet());

        for (ProjectMatcher m : map.values()) {
            String projectName = m.cloneUrl.substring(m.cloneUrl.lastIndexOf('/') + 1, m.cloneUrl.lastIndexOf('.'));
            String folder = tempDir + "/" + projectName;
//			try (Repository rep = gitService.cloneIfNotExists(folder,
//					m.cloneUrl/*, m.branch */)) {
            if (m.ignoreNonSpecifiedCommits) {
                // It is faster to only look at particular commits
                for (String commitId : m.getCommits()) {

                    if (
                            !alreadyParsed.contains(commitId)
//								&& !commitId.equals("cfc54e8afa7ee7d5376525a84559e90b21487ccf")
//								&& !commitId.equals("e2de877a29217a50afbd142454a330e423d86045")
//								&& !commitId.equals("e9efc045fbc6fa893c66a03b72b7eedb388cf96c")
                    ) {
                        System.out.println("Commit: " + commitId + " " + folder);
                        refactoringDetector.detectAtCommit(projectName, commitId, m);
                    }
                }
            } else {
                // Iterate over each commit
                //	refactoringDetector.detectAll(rep, m.branch, m);
            }
//			}
        }
        write(GumTreeResults.newBuilder().addAllRefactoringsReported(refactoringReported).build());
        System.out.println(String.format("Commits: %d  Errors: %d", commitsCount, errorCommitsCount));

        String mainResultMessage = buildResultMessage(c);
        System.out.println("Total  " + mainResultMessage);
        for (RefactoringType refType : RefactoringType.values()) {
            Counter refTypeCounter = cMap.get(refType);
            if (refTypeCounter != null) {
                System.out
                        .println(String.format("%-7s", refType.getAbbreviation()) + buildResultMessage(refTypeCounter));
            }
        }

        boolean success = get(FP) == 0 && get(FN) == 0 && get(TP) > 0;
        if (!success || verbose) {
            for (ProjectMatcher m : map.values()) {
                m.printResults();
            }
        }
        Assert.assertTrue(mainResultMessage, success);
    }

    private String buildResultMessage(Counter c) {
        double precision = ((double) get(TP, c) / (get(TP, c) + get(FP, c)));
        double recall = ((double) get(TP, c)) / (get(TP, c) + get(FN, c));
        String mainResultMessage = String.format(
                "TP: %2d  FP: %2d  FN: %2d  TN: %2d  Unk.: %2d  Prec.: %.3f  Recall: %.3f", get(TP, c), get(FP, c),
                get(FN, c), get(TN, c), get(UNK, c), precision, recall);
        return mainResultMessage;
    }

    private List<String> normalize(String refactoring) {
        RefactoringType refType = RefactoringType.extractFromDescription(refactoring);
        refactoring = normalizeSingle(refactoring);
        if (aggregate) {
            refactoring = refType.aggregate(refactoring);
        } else {
            int begin = refactoring.indexOf("from classes [");
            if (begin != -1) {
                int end = refactoring.lastIndexOf(']');
                String types = refactoring.substring(begin + "from classes [".length(), end);
                String[] typesArray = types.split(", ");
                List<String> refactorings = new ArrayList<String>();
                for (String type : typesArray) {
                    refactorings.add(refactoring.substring(0, begin) + "from class " + type);
                }
                return refactorings;
            }
        }
        return Collections.singletonList(refactoring);
    }

    /**
     * Remove generics type information.
     */
    private static String normalizeSingle(String refactoring) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < refactoring.length(); i++) {
            char c = refactoring.charAt(i);
            if (c == '\t') {
                c = ' ';
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public class ProjectMatcher extends RefactoringHandler {

        private final String cloneUrl;
        private final String branch;
        private Map<String, CommitMatcher> expected = new HashMap<>();
        private boolean ignoreNonSpecifiedCommits = true;
        private int truePositiveCount = 0;
        private int falsePositiveCount = 0;
        private int falseNegativeCount = 0;
        private int trueNegativeCount = 0;
        private int unknownCount = 0;
        // private int errorsCount = 0;

        private ProjectMatcher(String cloneUrl, String branch) {
            this.cloneUrl = cloneUrl;
            this.branch = branch;
        }

        public ProjectMatcher atNonSpecifiedCommitsContainsNothing() {
            this.ignoreNonSpecifiedCommits = false;
            return this;
        }

        public CommitMatcher atCommit(String commitId) {
            CommitMatcher m = expected.get(commitId);
            if (m == null) {
                m = new CommitMatcher();
                expected.put(commitId, m);
            }
            return m;
        }

        public Set<String> getCommits() {
            return expected.keySet();
        }

        @Override
        public boolean skipCommit(String commitId) {
            if (this.ignoreNonSpecifiedCommits) {
                return !this.expected.containsKey(commitId);
            }
            return false;
        }


        @Override
        public void handle(String commitId, Set<RefactoringInfo> refactorings) {
            ResultsOuterClass.Results.Builder result = ResultsOuterClass.Results.newBuilder();
//			refactorings= filterRefactoring(refactorings);

			refactoringReported.addAll(refactorings.stream()
                    .filter(x -> x.getDescription().startsWith("Rename Method"))
                    .map(x->getAsRefactoringReported(x,commitId, cloneUrl))
                    .collect(Collectors.toList()));
//					.forEach(x -> {
//						write(getAsRefactoringReported(x,commitId));
//					});

            CommitMatcher commitMatcher;
            commitsCount++;
            //String commitId = curRevision.getId().getName();
            if (expected.containsKey(commitId)) {
                commitMatcher = expected.get(commitId);
            } else if (!this.ignoreNonSpecifiedCommits) {
                commitMatcher = this.atCommit(commitId);
                commitMatcher.containsOnly();
            } else {
                // ignore this commitMatcher
                commitMatcher = null;
            }
            if (commitMatcher != null) {
                commitMatcher.analyzed = true;
                Set<String> refactoringsFound = new HashSet<String>();
                for (RefactoringInfo refactoring : refactorings) {
                    refactoringsFound.addAll(normalize(refactoring.getDescription()));
                }
                // count true positives
                for (Iterator<String> iter = commitMatcher.expected.iterator(); iter.hasNext(); ) {
                    String expectedRefactoring = iter.next();
                    if (refactoringsFound.contains(expectedRefactoring)) {
                        iter.remove();
                        refactoringsFound.remove(expectedRefactoring);
                        this.truePositiveCount++;
                        count(TP, expectedRefactoring);
                        commitMatcher.truePositive.add(expectedRefactoring);
                        result.addTruePositives(expectedRefactoring);
                    }
                }

                // count false positives
                for (Iterator<String> iter = commitMatcher.unknown.iterator(); iter.hasNext(); ) {
                    String notExpectedRefactoring = iter.next();
                    if (refactoringsFound.contains(notExpectedRefactoring)) {
                        refactoringsFound.remove(notExpectedRefactoring);
                        this.falsePositiveCount++;
                        count(FP, notExpectedRefactoring);
                        result.addFalsePositives(notExpectedRefactoring);
                    } else {
                        this.trueNegativeCount++;
                        count(TN, notExpectedRefactoring);
                        result.addTrueNegatives(notExpectedRefactoring);
                        iter.remove();
                    }
                }
                // count true positives
                for (Iterator<String> iter = commitMatcher.expected.iterator(); iter.hasNext(); ) {
                    String expectedRefactoring = iter.next();
                    if (refactoringsFound.contains(expectedRefactoring)) {
                        iter.remove();
                        refactoringsFound.remove(expectedRefactoring);
                        this.truePositiveCount++;
                        count(TP, expectedRefactoring);
                        commitMatcher.truePositive.add(expectedRefactoring);
                        result.addTruePositives(expectedRefactoring);
                    }
                }

                // count false positives
                for (Iterator<String> iter = commitMatcher.falsePositives.iterator(); iter.hasNext(); ) {
                    String notExpectedRefactoring = iter.next();
                    if (refactoringsFound.contains(notExpectedRefactoring)) {
                        refactoringsFound.remove(notExpectedRefactoring);
                        this.falsePositiveCount++;
                        count(FP, notExpectedRefactoring);
                        result.addFalsePositives(notExpectedRefactoring);
                    } else {
                        this.trueNegativeCount++;
                        count(TN, notExpectedRefactoring);
                        result.addTrueNegatives(notExpectedRefactoring);
                        iter.remove();
                    }
                }
                // count false positives when using containsOnly
                if (commitMatcher.ignoreNonSpecified) {
                    for (String refactoring : refactoringsFound) {
                        result.addTrueNegatives(refactoring);
                        commitMatcher.unknown.add(refactoring);
                        this.unknownCount++;
                        count(UNK, refactoring);
                    }
                } else {
                    for (String refactoring : refactoringsFound) {
                        commitMatcher.falsePositives.add(refactoring);
                        result.addFalsePositives(refactoring);
                        this.falsePositiveCount++;
                        count(FP, refactoring);
                    }
                }

                // count false negatives
                for (String expectedButNotFound : commitMatcher.expected) {
                    this.falseNegativeCount++;
                    result.addFalseNegatives(expectedButNotFound);
                    count(FN, expectedButNotFound);
                }
            }
            write(result.setSha(commitId).build());
        }

        private List<Refactoring> filterRefactoring(List<Refactoring> refactorings) {
            List<Refactoring> filteredRefactorings = new ArrayList<>();

            for (Refactoring refactoring : refactorings) {
                BigInteger value = Enum.valueOf(Refactorings.class, refactoring.getName().replace(" ", "")).getValue();
                if (value.and(refactoringFilter).compareTo(BigInteger.ZERO) == 1) {
                    filteredRefactorings.add(refactoring);
                }
            }

            return filteredRefactorings;
        }

        @Override
        public void handleException(String commitId, Exception e) {
            if (expected.containsKey(commitId)) {
                CommitMatcher matcher = expected.get(commitId);
                matcher.error = e.toString();
            }
            errorCommitsCount++;
            e.printStackTrace();
            System.err.println(" error at commit " + commitId + ": ");
            // e.getMessage());
        }

        private void printResults() {
            // if (verbose || this.falsePositiveCount > 0 ||
            // this.falseNegativeCount > 0 || this.errorsCount > 0) {
            // System.out.println(this.cloneUrl);
            // }
            String baseUrl = this.cloneUrl.substring(0, this.cloneUrl.length() - 4) + "/commit/";
            for (Map.Entry<String, CommitMatcher> entry : this.expected.entrySet()) {
                String commitUrl = baseUrl + entry.getKey();
                CommitMatcher matcher = entry.getValue();
                if (matcher.error != null) {
                    System.out.println("error at " + commitUrl + ": " + matcher.error);
                } else {
                    if (verbose || !matcher.expected.isEmpty() || !matcher.unknown.isEmpty()
                            || !matcher.unknown.isEmpty()) {
                        if (!matcher.analyzed) {
                            System.out.println("at not analyzed " + commitUrl);
                        } else {
                            System.out.println("at " + commitUrl);
                        }
                    }
                    if (verbose && !matcher.truePositive.isEmpty()) {
                        System.out.println(" true positives");
                        for (String ref : matcher.truePositive) {
                            System.out.println("  " + ref);
                        }
                    }
                    if (!matcher.unknown.isEmpty()) {
                        System.out.println(" false positives");
                        for (String ref : matcher.unknown) {
                            System.out.println("  " + ref);
                        }
                    }
                    if (!matcher.expected.isEmpty()) {
                        System.out.println(" false negatives");
                        for (String ref : matcher.expected) {
                            System.out.println("  " + ref);
                        }
                    }
                    if (!matcher.unknown.isEmpty()) {
                        System.out.println(" unknown");
                        for (String ref : matcher.unknown) {
                            System.out.println("  " + ref);
                        }
                    }
                }
            }
        }

        // private void countFalseNegatives() {
        // for (Map.Entry<String, CommitMatcher> entry :
        // this.expected.entrySet()) {
        // CommitMatcher matcher = entry.getValue();
        // if (matcher.error == null) {
        // this.falseNegativeCount += matcher.expected.size();
        // }
        // }
        // }

        public class CommitMatcher {
            private Set<String> expected = new HashSet<>();
            private Set<String> falsePositives = new HashSet<>();
            private Set<String> truePositive = new HashSet<>();
            private Set<String> unknown = new HashSet<>();
            private boolean ignoreNonSpecified = true;
            private boolean analyzed = false;
            private String error = null;

            private CommitMatcher() {
            }

            public ProjectMatcher contains(String... refactorings) {
                for (String refactoring : refactorings) {
                    expected.addAll(normalize(refactoring));
                }
                return ProjectMatcher.this;
            }

            public ProjectMatcher containsOnly(String... refactorings) {
                this.ignoreNonSpecified = false;
                this.expected = new HashSet<String>();
                this.falsePositives = new HashSet<String>();
                for (String refactoring : refactorings) {
                    expected.addAll(normalize(refactoring));
                }
                return ProjectMatcher.this;
            }

            public ProjectMatcher containsNothing() {
                return containsOnly();
            }

            public ProjectMatcher notContains(String... refactorings) {
                for (String refactoring : refactorings) {
                    falsePositives.addAll(normalize(refactoring));
                }
                return ProjectMatcher.this;
            }
        }
    }

    public static void write(ResultsOuterClass.Results msg) {
        String s = System.getProperty("user.dir") + "/Output/" + msg.getSha() + ".txt";
        try {
            FileOutputStream output1 = new FileOutputStream(s, true);
            output1.write(msg.toByteArray());
        } catch (FileNotFoundException e) {
            System.out.println("File not found.  Creating a new file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void write(GumTreeResults msg) {
		String s = System.getProperty("user.dir") + "/Output/GumTreeResult";
		try {
			FileOutputStream output1 = new FileOutputStream(s, true);
			output1.write(msg.toByteArray());
		} catch (FileNotFoundException e) {
			System.out.println("File not found.  Creating a new file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    private static Optional<ResultsOuterClass.Results> getResults(Path file) {
        try {
            return Optional.of(ResultsOuterClass.Results.parseFrom(Files.readAllBytes(file)));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static List<ResultsOuterClass.Results> readAllResults() {
        try {
            String s = System.getProperty("user.dir") + "/Output";

            return Files.walk(Paths.get(s))
                    .map(p -> getResults(p))
                    .filter(x -> x.isPresent())
                    .map(x -> x.get())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("TFG protos could not be deserialised");
            return new ArrayList<>();
        }
    }


}
