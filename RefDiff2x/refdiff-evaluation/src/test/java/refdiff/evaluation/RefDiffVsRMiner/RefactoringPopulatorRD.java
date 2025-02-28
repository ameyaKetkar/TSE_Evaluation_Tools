package refdiff.evaluation.RefDiffVsRMiner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import refdiff.evaluation.RefactoringType;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class RefactoringPopulatorRD {

	public enum Systems {
		FSE(1), All(2);
		private int value;

		Systems(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum Refactorings {
		MoveMethod(new BigInteger("1")),
		MoveAttribute(new BigInteger("2")),
		InlineMethod(new BigInteger("4")),
		ExtractMethod(new BigInteger("8")),
		PushDownMethod(new BigInteger("16")),
		PushDownAttribute(new BigInteger("32")),
		PullUpMethod(new BigInteger("64")),
		PullUpAttribute(new BigInteger("128")),
		ExtractInterface(new BigInteger("256")),
		ExtractSuperclass(new BigInteger("512")),
		MoveClass(new BigInteger("1024")),
		//ChangePackage(new BigInteger("2048")),
		RenameMethod(new BigInteger("4096")),
		ExtractAndMoveMethod(new BigInteger("8192")),
		RenameClass(new BigInteger("16384")),
		//MoveSourceFolder(new BigInteger("32768")),
		MoveAndRenameClass(new BigInteger("65536")),
		MoveAndRenameMethod(new BigInteger("131072")),
		//ExtractVariable(new BigInteger("131072")),
		//RenameVariable(new BigInteger("262144")),
		//RenameParameter(new BigInteger("524288")),
		//RenameAttribute(new BigInteger("1048576")),
		//ParameterizeVariable(new BigInteger("2097152")),
		//ReplaceVariableWithAttribute(new BigInteger("4194304")),
		//MoveAndRenameAttribute(new BigInteger("8388608")),
		//ReplaceAttribute(new BigInteger("16777216")),
		//InlineVariable(new BigInteger("33554432")),
		//ExtractClass(new BigInteger("67108864")),
		//ExtractSubclass(new BigInteger("134217728")),
		//MergeVariable(new BigInteger("268435456")),
		//MergeParameter(new BigInteger("536870912")),
		//MergeAttribute(new BigInteger("1073741824")),
//		SplitVariable(new BigInteger("2147483648")),
//		SplitParameter(new BigInteger("4294967296")),
//		SplitAttribute(new BigInteger("8589934592")),
//		ChangeReturnType(new BigInteger("17179869184")),
//		ChangeVariableType(new BigInteger("34359738368")),
//		ChangeParameterType(new BigInteger("68719476736")),
//		ChangeAttributeType(new BigInteger("137438953472")),
	//	ExtractAttribute(new BigInteger("274877906944")),
		All(new BigInteger("549755813887"));

		private BigInteger value;

		Refactorings(BigInteger value) {
			this.value = value;
		}

		public BigInteger getValue() {
			return value;
		}
	}

	public static void feedRefactoringsInstances(BigInteger refactoringsFlag, int systemsFlag, TestBuilderRD test, String outputPath)
			throws IOException {

		if ((systemsFlag & Systems.FSE.getValue()) > 0) {
			prepareFSERefactorings(test, refactoringsFlag, outputPath);
		}
	}

	private static void prepareFSERefactorings(TestBuilderRD test, BigInteger flag, String outputPath)
			throws IOException {
		List<Root> roots = getFSERefactorings(flag, outputPath);

		for (Root root : roots) {
			test.project(root.repository, "master").atCommit(root.sha1)
					.containsOnly(extractRefactorings(root.refactorings));
		}
	}

	static Set<String> refTypes = Arrays.stream(RefactoringType.values()).map(x -> x.getDisplayName())
			.filter(x->!x.contains("Attribute"))
			.collect(Collectors.toSet());

	public static String[] extractRefactorings(List<Refactoring> refactoring) {
		int count = 0;
		for (Refactoring ref : refactoring) {
			if (ref.validation.contains("TP") && refTypes.stream().anyMatch(x -> ref.description.startsWith(x)))
				count++;
		}
		String[] refactorings = new String[count];
		int counter = 0;
		for (Refactoring ref : refactoring) {
			if (ref.validation.contains("TP") && refTypes.stream().anyMatch(x -> ref.description.startsWith(x))) {
				refactorings[counter++] = ref.description;
			}
		}
		return refactorings;
	}

	private static List<String> getDeletedCommits() {
		List<String> deletedCommits = new ArrayList<String>();
		String file = System.getProperty("user.dir") + "/data/icse/deleted_commits.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String sha1 = line.substring(line.lastIndexOf("/")+1);
				deletedCommits.add(sha1);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return deletedCommits;
	}

	public static List<Root> getFSERefactorings(BigInteger flag, String outputPath) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		String jsonFile = System.getProperty("user.dir")  + "/data/icse/data.json";
		List<Root> roots = mapper.readValue(new File(jsonFile),
				mapper.getTypeFactory().constructCollectionType(List.class, Root.class));

		List<Root> filtered = new ArrayList<>();

		Set<String> commits = Stream.concat(TestBuilderRD.readAllResults(outputPath)
				.stream(), TestBuilderRD.readAllResults(outputPath).stream()).map(x -> x.getSha()).collect(toSet());

		List<String> deletedCommits = getDeletedCommits();
		roots = roots.stream().filter(x-> !deletedCommits.contains(x.sha1))
				.filter(x -> !commits.contains(x.sha1))
				.collect(Collectors.toList());

		System.out.println(roots.size());

		for (Root root : roots) {
			List<Refactoring> refactorings = new ArrayList<>();
			root.refactorings.forEach((refactoring) -> {
				if (isAdded(refactoring, flag))
					refactorings.add(refactoring);
			});
			if (refactorings.size() > 0) {
				Root tmp = root;
				tmp.refactorings = refactorings;
				filtered.add(tmp);
			}

		}
		return filtered;
	}

	private static boolean isAdded(Refactoring refactoring, BigInteger flag) {
		try {
			BigInteger value = Enum.valueOf(Refactorings.class, refactoring.type.replace(" ", "")).getValue();
			return value.and(flag).compareTo(BigInteger.ZERO) == 1;

		} catch (Exception e) {
			return false;
		}
	}

	public static void printRefDiffResults(BigInteger flag, String outputPath) {
		Hashtable<String, Tuple> result = new Hashtable<>();
		try {
			List<Root> roots = getFSERefactorings(flag, outputPath);
			for (Refactorings ref : Refactorings.values()) {
				if (ref == Refactorings.All)
					continue;
				result.put(ref.toString(), new Tuple());
			}
			for (Root root : roots) {
				for (Refactoring ref : root.refactorings) {
					Tuple tuple = result.get(ref.type.replace(" ", ""));
					tuple.totalTruePositives += ref.validation.contains("TP") ? 1 : 0;
					tuple.unknown += ref.validation.equals("UKN") ? 1 : 0;

					if (ref.detectionTools.contains("RefDiff")) {
						tuple.refDiffTruePositives += ref.validation.contains("TP") ? 1 : 0;
						tuple.refDiffFalsePositives += ref.validation.equals("FP") ? 1 : 0;
					}

				}
			}
			Tuple[] tmp = {};
			System.out.println("Total\t" + buildResultMessage(result.values().toArray(tmp)));
			for (String key : result.keySet()) {
				System.out.println(getInitials(key) + "\t" + buildResultMessage(result.get(key)));
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getInitials(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String character = str.substring(i, i + 1);
			if (character == character.toUpperCase())
				sb.append(character);
		}
		return sb.toString();
	}

	private static String buildResultMessage(Tuple... result) {
		int trueP = 0;
		int total = 0;
		int ukn = 0;
		int falseP = 0;
		for (Tuple res : result) {
			trueP += res.refDiffTruePositives;
			total += res.totalTruePositives;
			ukn += res.unknown;
			falseP += res.refDiffFalsePositives;
		}
		double precision = trueP / (double) (trueP + falseP);
		double recall = trueP / (double) (total);
		try {
			String mainResultMessage = String.format("TP: %2d  FP: %2d  FN: %2d  Unk.: %2d  Prec.: %.3f  Recall: %.3f",
					trueP, falseP, total - trueP, ukn, precision, recall);
			return mainResultMessage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static class Tuple {
		public int totalTruePositives;
		public int refDiffTruePositives;
		public int falseNegatives;
		public int unknown;
		public int refDiffFalsePositives;
	}

	public static class Root {
		public int id;
		public String repository;
		public String sha1;
		public String url;
		public String author;
		public String time;
		public List<Refactoring> refactorings;
		public long refDiffExecutionTime;

	}

	public static class Refactoring {
		public String type;
		public String description;
		public String comment;
		public String validation;
		public String detectionTools;
		public String validators;

	}

	public static class Comment {
		public String refactored;
		public String link;
		public String message;
		public String type;
		public String reportedCase;
	}
 
}
