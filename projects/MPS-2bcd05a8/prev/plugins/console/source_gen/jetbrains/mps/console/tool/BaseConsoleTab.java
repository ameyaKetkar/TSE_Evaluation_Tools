package jetbrains.mps.console.tool;

/*Generated by MPS */

import javax.swing.JPanel;
import com.intellij.openapi.Disposable;
import org.jetbrains.mps.openapi.model.SModel;
import org.jetbrains.mps.openapi.model.SNode;
import com.intellij.openapi.fileEditor.FileEditor;
import jetbrains.mps.nodeEditor.UIEditorComponent;
import jetbrains.mps.nodeEditor.Highlighter;
import jetbrains.mps.nodeEditor.EditorComponent;
import org.jetbrains.mps.openapi.language.SLanguage;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.smodel.adapter.ids.MetaIdFactory;
import java.util.Collection;
import jetbrains.mps.smodel.SLanguageHierarchy;
import java.util.Collections;
import jetbrains.mps.smodel.SModelInternal;
import jetbrains.mps.internal.collections.runtime.CollectionSequence;
import jetbrains.mps.smodel.Language;
import jetbrains.mps.project.AbstractModule;
import org.jetbrains.mps.openapi.persistence.PersistenceFacade;
import org.jetbrains.mps.openapi.module.SModuleReference;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import java.util.ArrayList;
import jetbrains.mps.smodel.tempmodel.TemporaryModels;
import jetbrains.mps.workbench.action.BaseAction;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import com.intellij.openapi.actionSystem.ShortcutSet;
import jetbrains.mps.ide.project.ProjectHelper;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ide.PasteProvider;
import jetbrains.mps.smodel.tempmodel.TempModuleOptions;
import org.apache.log4j.Level;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.Map;
import jetbrains.mps.workbench.action.ActionUtils;
import com.intellij.openapi.actionSystem.ActionManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.mps.openapi.model.SNodeReference;
import java.awt.datatransfer.Transferable;
import com.intellij.ide.CopyPasteManagerEx;
import jetbrains.mps.ide.datatransfer.SModelDataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import jetbrains.mps.openapi.editor.cells.EditorCell;
import org.jetbrains.mps.openapi.module.SRepository;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SConceptOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SNodeOperations;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;
import jetbrains.mps.nodeEditor.datatransfer.NodePaster;
import jetbrains.mps.baseLanguage.closures.runtime.Wrappers;
import jetbrains.mps.smodel.ModelAccess;
import com.intellij.util.Base64Converter;
import jetbrains.mps.persistence.PersistenceUtil;
import jetbrains.mps.project.Project;
import org.jetbrains.mps.openapi.module.SearchScope;
import jetbrains.mps.ide.findusages.model.scopes.ProjectScope;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import org.jetbrains.mps.openapi.model.SReference;
import jetbrains.mps.internal.collections.runtime.Sequence;
import java.util.Scanner;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.MouseShortcut;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.ActionPlaces;
import jetbrains.mps.smodel.behaviour.BehaviorReflection;
import com.intellij.openapi.wm.IdeFocusManager;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SModelOperations;
import jetbrains.mps.internal.collections.runtime.IWhereFilter;
import jetbrains.mps.internal.collections.runtime.IVisitor;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.jetbrains.mps.openapi.model.SModelReference;
import jetbrains.mps.smodel.SModelUtil_new;

public abstract class BaseConsoleTab extends JPanel implements Disposable {
  protected ConsoleTool myTool;
  protected SModel myModel;
  protected SNode myRoot;
  protected FileEditor myFileEditor;
  protected UIEditorComponent myEditor;
  protected Highlighter myHighlighter;
  protected String myTabTitle;

  public String getTitle() {
    return myTabTitle;
  }

  public SModel getConsoleModel() {
    return myModel;
  }

  public EditorComponent getEditorComponent() {
    return myEditor;
  }

  public SNode getRoot() {
    return myRoot;
  }

  public ConsoleTool getConsoleTool() {
    return myTool;
  }

  protected void addBuiltInImports() {
    SLanguage base = MetaAdapterFactory.getLanguage(MetaIdFactory.langId(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L), "jetbrains.mps.console.base", -1);
    Collection<SLanguage> baseAndExtensions = new SLanguageHierarchy(Collections.singleton(base)).getExtending();
    SModelInternal modelInternal = ((SModelInternal) myModel);
    for (SLanguage l : CollectionSequence.fromCollection(baseAndExtensions)) {
      modelInternal.addLanguage(l);
      Language sourceLangModule = (Language) l.getSourceModule();
      if (sourceLangModule == null) {
        continue;
      }
      modelInternal.addModelImport(sourceLangModule.getStructureModelDescriptor().getReference(), false);
      ((AbstractModule) myModel.getModule()).addDependency(sourceLangModule.getModuleReference(), false);
    }
    modelInternal.addDevKit(PersistenceFacade.getInstance().createModuleReference("fbc25dd2-5da4-483a-8b19-70928e1b62d7(jetbrains.mps.devkit.general-purpose)"));
  }

  protected void validateImports() {
    SModelInternal modelInternal = (SModelInternal) myModel;
    for (SModuleReference devKit : ListSequence.fromListWithValues(new ArrayList<SModuleReference>(), modelInternal.importedDevkits())) {
      modelInternal.deleteDevKit(devKit);
    }
    for (SLanguage language : ListSequence.fromListWithValues(new ArrayList<SLanguage>(), modelInternal.importedLanguageIds())) {
      modelInternal.deleteLanguageId(language);
    }
    for (jetbrains.mps.smodel.SModel.ImportElement model : ListSequence.fromListWithValues(new ArrayList<jetbrains.mps.smodel.SModel.ImportElement>(), modelInternal.importedModels())) {
      modelInternal.deleteModelImport(model.getModelReference());
    }
    addBuiltInImports();
    TemporaryModels.getInstance().addMissingImports(myModel);
  }

  protected BaseAction registerKeyShortcut(BaseAction a, int key) {
    return registerShortcutSet(a, new CustomShortcutSet(KeyStroke.getKeyStroke(key, KeyEvent.CTRL_MASK)));
  }

  protected BaseAction registerShortcutSet(BaseAction a, ShortcutSet shortcutSet) {
    a.registerCustomShortcutSet(shortcutSet, myEditor);
    return a;
  }

  protected void createEditor() {
    this.myEditor = new UIEditorComponent(check_6q36mf_a0a0a0a62(ProjectHelper.toMPSProject(myTool.getProject())), null) {
      @Nullable
      @Override
      public Object getData(@NonNls String key) {
        if (PlatformDataKeys.FILE_EDITOR.is(key)) {
          return myFileEditor;
        }
        if (PlatformDataKeys.PASTE_PROVIDER.is(key)) {
          PasteProvider parentPasteProvider = as_6q36mf_a0a0a1a0a0a0a0ab(super.getData(key), PasteProvider.class);
          return (myTool.getPasteAsRef() ? new BaseConsoleTab.MyPasteProvider(parentPasteProvider) : parentPasteProvider);
        }
        return super.getData(key);
      }
    };
    myEditor.editNode(myRoot);
  }


  protected void createConsoleModel() {
    this.myModel = TemporaryModels.getInstance().create(false, TempModuleOptions.forDefaultModuleWithSourceAndClassesGen());
    if (myModel == null) {
      if (LOG.isEnabledFor(Level.ERROR)) {
        LOG.error("Error: could not create console model");
      }
      return;
    }
  }

  public void dispose() {
    disposeConsoleTab();
  }

  public void disposeConsoleTab() {
    getProject().getModelAccess().executeCommand(new Runnable() {
      public void run() {
        if (myEditor != null) {
          myEditor.dispose();
        }
        TemporaryModels.getInstance().dispose(myModel);
      }
    });
    myHighlighter.removeAdditionalEditorComponent(myEditor);
  }

  protected class ExecuteClosureAction extends BaseAction {
    public ExecuteClosureAction() {
      super("Execute Closure");
    }
    protected void doExecute(AnActionEvent event, Map<String, Object> map) {
      ActionUtils.updateAndPerformAction(((BaseAction) ActionManager.getInstance().getAction("jetbrains.mps.console.actions.ExecuteActionAttachedToCurrentNode_Action")), event);
    }
  }

  public class MyPasteProvider implements PasteProvider {

    private PasteProvider myDefaultPasteProvider;

    public MyPasteProvider(PasteProvider defaultPasteProvider) {
      myDefaultPasteProvider = defaultPasteProvider;
    }

    public void performPaste(@NotNull final DataContext context) {
      getProject().getModelAccess().executeCommand(new Runnable() {
        public void run() {
          SNodeReference pastingNodeReference = null;
          try {
            for (Transferable trf : CopyPasteManagerEx.getInstanceEx().getAllContents()) {
              if (trf != null && trf.isDataFlavorSupported(SModelDataFlavor.sNodeReference)) {
                pastingNodeReference = (SNodeReference) trf.getTransferData(SModelDataFlavor.sNodeReference);
              }
              break;
            }
          } catch (UnsupportedFlavorException ignored) {
          } catch (IOException ignored) {
          }
          EditorCell currentCell = myEditor.getSelectedCell();
          SRepository repository = getProject().getRepository();
          SNode referenceTarget = check_6q36mf_a0e0a0a0a5lb(pastingNodeReference, repository);
          if (referenceTarget != null && currentCell != null && !(check_6q36mf_a0a5a0a0a0f73(check_6q36mf_a0a0f0a0a0a5lb(pastingNodeReference), myModel))) {
            SNode refContainer = SConceptOperations.createNewNode(SNodeOperations.asInstanceConcept(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x51132a123c89fa7eL, "jetbrains.mps.console.base.structure.PastedNodeReference")));
            SLinkOperations.setTarget(refContainer, MetaAdapterFactory.getReferenceLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x36ac6f29ae8c1fb5L, 0x4904fd89e74fc6fL, "target"), referenceTarget);
            NodePaster paster = new NodePaster(ListSequence.fromListAndArray(new ArrayList<SNode>(), refContainer));
            if (paster.canPaste(currentCell)) {
              paster.paste(currentCell);
            } else if (paster.canPasteWithRemove(myEditor.getSelectedNodes())) {
              paster.pasteWithRemove(myEditor.getSelectedNodes());
            }
            TemporaryModels.getInstance().addMissingImports(myModel);
          } else {
            check_6q36mf_a0a0f0a0a0a5lb_0(myDefaultPasteProvider, context);
          }
        }
      });
    }
    public boolean isPastePossible(@NotNull DataContext context) {
      return true;
    }
    public boolean isPasteEnabled(@NotNull DataContext context) {
      return true;
    }
  }

  protected abstract void loadHistory(String state);

  @Nullable
  public String saveHistory() {
    final Wrappers._T<String> result = new Wrappers._T<String>(null);
    ModelAccess.instance().runReadAction(new Runnable() {
      public void run() {
        try {
          result.value = (myModel == null ? null : Base64Converter.encode(PersistenceUtil.saveBinaryModel(myModel)));
        } catch (Exception e) {
          if (LOG.isEnabledFor(Level.WARN)) {
            LOG.warn("Error on console model saving", e);
          }
        }
      }
    });
    return result.value;
  }

  protected ConsoleContext getConsoleContext() {
    return new ConsoleContext() {
      public Project getProject() {
        return ProjectHelper.toMPSProject(myTool.getProject());
      }
      public SearchScope getDefaultSearchscope() {
        return new ProjectScope(getProject());
      }
      public BaseConsoleTab getConsoleTab() {
        return BaseConsoleTab.this;
      }
    };
  }

  protected SNode getLastReponse() {
    SNode last = SNodeOperations.as(ListSequence.fromList(SLinkOperations.getChildren(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bafL, "history")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0xa835f28c1aa02beL, 0x63da33792b5df49aL, "item"))).last(), MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171a5ba02L, "jetbrains.mps.console.base.structure.Response"));
    if (last != null) {
      return last;
    }
    return SLinkOperations.addNewChild(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bafL, "history")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0xa835f28c1aa02beL, 0x63da33792b5df49aL, "item"), SNodeOperations.asInstanceConcept(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171a5ba02L, "jetbrains.mps.console.base.structure.Response")));
  }

  protected void addNodeImports(SNode node) {
    final SModelInternal modelInternal = (SModelInternal) myModel;
    final AbstractModule module = ((AbstractModule) myModel.getModule());
    final Collection<SLanguage> importedLanguages = modelInternal.importedLanguageIds();
    for (SNode subNode : ListSequence.fromList(SNodeOperations.getNodeDescendants(node, null, true, new SAbstractConcept[]{}))) {
      SLanguage usedLanguage = subNode.getConcept().getLanguage();
      if (!(importedLanguages.contains(usedLanguage))) {
        modelInternal.addLanguage(usedLanguage);
      }
      for (SReference ref : Sequence.fromIterable(SNodeOperations.getReferences(subNode))) {
        SModel usedModel = SNodeOperations.getModel(SLinkOperations.getTargetNode(ref));
        if (usedModel != null && !(modelInternal.importedModels().contains(usedModel))) {
          modelInternal.addModelImport(usedModel.getReference(), false);
          module.addDependency(SNodeOperations.getModel(SLinkOperations.getTargetNode(ref)).getModule().getModuleReference(), false);
        }
      }
    }
  }

  public ConsoleStream getConsoleStream() {
    return new ConsoleStream() {
      public void addText(String text) {
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          if ((line != null && line.length() > 0)) {
            ListSequence.fromList(SLinkOperations.getChildren(getLastReponse(), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171a5ba02L, 0x4e3b035171b356edL, "item"))).addElement(createTextResponseItem_6q36mf_a0a0a1a1a0a0a0a94(line));
          }
          if (scanner.hasNextLine() || text.charAt(text.length() - 1) == '\n') {
            SLinkOperations.addNewChild(getLastReponse(), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171a5ba02L, 0x4e3b035171b356edL, "item"), SNodeOperations.asInstanceConcept(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171b35d30L, "jetbrains.mps.console.base.structure.NewLineResponseItem")));
          }
        }
      }
      public void addNode(SNode node) {
        addNodeImports(node);
        SLinkOperations.setTarget(SLinkOperations.addNewChild(getLastReponse(), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171a5ba02L, 0x4e3b035171b356edL, "item"), SNodeOperations.asInstanceConcept(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171b35c14L, "jetbrains.mps.console.base.structure.NodeResponseItem"))), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171b35c14L, 0x4e3b035171b35c15L, "node"), node);
      }
    };
  }


  public BaseConsoleTab(ConsoleTool tool, String title, @Nullable String history) {
    myTool = tool;
    myTabTitle = title;
    initConsoleTab(history);
  }

  protected void registerActions(DefaultActionGroup group) {
    registerShortcutSet(new BaseConsoleTab.ExecuteClosureAction(), new CustomShortcutSet(new MouseShortcut(MouseEvent.BUTTON1, 0, 1)));
  }

  @NotNull
  private Project getProject() {
    Project mpsProject = ProjectHelper.toMPSProject(this.getConsoleTool().getProject());
    if (mpsProject == null) {
      throw new IllegalStateException("Cannot convert idea project to the mps project");
    }
    return mpsProject;
  }

  protected void initConsoleTab(@Nullable final String history) {
    getProject().getModelAccess().executeCommand(new Runnable() {
      public void run() {
        createConsoleModel();
        addBuiltInImports();
        loadHistory(history);
        createEditor();
        myFileEditor = new ConsoleFileEditor(myEditor);
      }
    });

    this.setLayout(new BorderLayout());


    DefaultActionGroup group = new DefaultActionGroup();
    registerActions(group);
    ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, false);
    JPanel toolbarComponent = new JPanel(new BorderLayout());
    toolbarComponent.add(toolbar.getComponent(), BorderLayout.CENTER);

    this.add(toolbarComponent, BorderLayout.WEST);
    this.add(myEditor.getExternalComponent(), BorderLayout.CENTER);

    myHighlighter = check_6q36mf_a0o0gc(myTool.getProject());
    myHighlighter.addAdditionalEditorComponent(myEditor);
  }

  public void execute(@Nullable final SNode command, @Nullable final Runnable executeBefore, @Nullable final Runnable executeAfter) {
    myTool.selectTab(this);
    final SNode[] typedCommand = new SNode[1];
    getProject().getModelAccess().executeCommand(new Runnable() {
      public void run() {
        typedCommand[0] = SConceptOperations.createNewNode(SNodeOperations.asInstanceConcept(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, "jetbrains.mps.console.base.structure.CommandHolder")));
        if (command != null) {
          addNodeImports(command);
          SLinkOperations.setTarget(typedCommand[0], MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command"), SNodeOperations.copyNode(SLinkOperations.getTarget(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command"))));
          SLinkOperations.setTarget(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command"), SNodeOperations.copyNode(command));
        }
      }
    });
    BehaviorReflection.invokeVirtual(Void.class, SLinkOperations.getTarget(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command")), "virtual_execute_6854397602732226506", new Object[]{getConsoleContext(), getConsoleStream(), new Runnable() {
      public void run() {
        getProject().getModelAccess().executeCommand(new Runnable() {
          public void run() {
            ListSequence.fromList(SLinkOperations.getChildren(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bafL, "history")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0xa835f28c1aa02beL, 0x63da33792b5df49aL, "item"))).addElement(SNodeOperations.copyNode(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder"))));
            SNodeOperations.deleteNode(SLinkOperations.getTarget(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command")));
            check_6q36mf_a2a0a0a0a0a2a0d0ic(executeBefore);
          }
        });
      }
    }, new Runnable() {
      public void run() {
        getProject().getModelAccess().executeCommand(new Runnable() {
          public void run() {
            SLinkOperations.setTarget(SLinkOperations.getTarget(myRoot, MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x15fb34051f725a2cL, 0x15fb34051f725bb1L, "commandHolder")), MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command"), SLinkOperations.getTarget(typedCommand[0], MetaAdapterFactory.getContainmentLink(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e27160acb4484bL, 0x4e27160acb44924L, "command")));
            check_6q36mf_a1a0a0a0a0a3a0d0ic(executeAfter);
          }
        });
      }
    }});
  }

  public void selectNode(final SNode nodeToSelect) {
    myTool.getToolWindow().activate(new Runnable() {
      public void run() {
        ModelAccess.instance().runReadAction(new Runnable() {
          public void run() {
            myEditor.selectNode(nodeToSelect);
            getEditorComponent().ensureSelectionVisible();
            IdeFocusManager.getInstance(myTool.getProject()).requestFocus(myEditor, false);
          }
        });
      }
    });
    myTool.selectTab(this);
  }

  protected SModel loadHistoryModel(String state) {
    if (state != null) {
      try {
        final Wrappers._T<SModel> loadedModel = new Wrappers._T<SModel>(PersistenceUtil.loadBinaryModel(Base64Converter.decode(state.getBytes())));
        ListSequence.fromList(SModelOperations.nodes(loadedModel.value, null)).where(new IWhereFilter<SNode>() {
          public boolean accept(SNode it) {
            return !(it.getConcept().isValid());
          }
        }).visitAll(new IVisitor<SNode>() {
          public void visit(SNode it) {
            if ((SNodeOperations.getNodeAncestor(it, MetaAdapterFactory.getInterfaceConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x5f195a051bd47defL, "jetbrains.mps.console.base.structure.HistoryItem"), false, false) != null)) {
              SNodeOperations.deleteNode(SNodeOperations.getNodeAncestor(it, MetaAdapterFactory.getInterfaceConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x5f195a051bd47defL, "jetbrains.mps.console.base.structure.HistoryItem"), false, false));
              if (LOG.isEnabledFor(Level.ERROR)) {
                LOG.error("Unknown concept on loading console history: removing enclosing history item");
              }
            } else {
              loadedModel.value = null;
              if (LOG.isEnabledFor(Level.ERROR)) {
                LOG.error("Unknown concept on loading console history: not loading history");
              }
            }
          }
        });
        return loadedModel.value;
      } catch (RuntimeException e) {
        if (LOG.isEnabledFor(Level.ERROR)) {
          LOG.error("Console history was not loaded. Maybe you are opening project from previous MPS versions?");
        }
      } catch (Throwable e) {
        if (LOG.isEnabledFor(Level.ERROR)) {
          LOG.error("Error on loading console history.", e);
        }
      }
    }
    return null;
  }

  protected static Logger LOG = LogManager.getLogger(BaseConsoleTab.class);
  private static SRepository check_6q36mf_a0a0a0a62(Project checkedDotOperand) {
    if (null != checkedDotOperand) {
      return checkedDotOperand.getRepository();
    }
    return null;
  }
  private static SNode check_6q36mf_a0e0a0a0a5lb(SNodeReference checkedDotOperand, SRepository repository) {
    if (null != checkedDotOperand) {
      return checkedDotOperand.resolve(repository);
    }
    return null;
  }
  private static boolean check_6q36mf_a0a5a0a0a0f73(SModelReference checkedDotOperand, SModel myModel) {
    if (null != checkedDotOperand) {
      return checkedDotOperand.equals(myModel.getReference());
    }
    return false;
  }
  private static SModelReference check_6q36mf_a0a0f0a0a0a5lb(SNodeReference checkedDotOperand) {
    if (null != checkedDotOperand) {
      return checkedDotOperand.getModelReference();
    }
    return null;
  }
  private static void check_6q36mf_a0a0f0a0a0a5lb_0(PasteProvider checkedDotOperand, DataContext context) {
    if (null != checkedDotOperand) {
      checkedDotOperand.performPaste(context);
    }

  }
  private static SNode createTextResponseItem_6q36mf_a0a0a1a1a0a0a0a94(Object p0) {
    PersistenceFacade facade = PersistenceFacade.getInstance();
    SNode n1 = SModelUtil_new.instantiateConceptDeclaration(MetaAdapterFactory.getConcept(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171b35c38L, "jetbrains.mps.console.base.structure.TextResponseItem"), null, null, false);
    n1.setProperty(MetaAdapterFactory.getProperty(0xde1ad86d6e504a02L, 0xb306d4d17f64c375L, 0x4e3b035171b35c38L, 0x4e3b035171b35d11L, "text"), String.valueOf(p0));
    return n1;
  }
  private static Highlighter check_6q36mf_a0o0gc(com.intellij.openapi.project.Project checkedDotOperand) {
    if (null != checkedDotOperand) {
      return checkedDotOperand.getComponent(Highlighter.class);
    }
    return null;
  }
  private static void check_6q36mf_a2a0a0a0a0a2a0d0ic(Runnable checkedDotOperand) {
    if (null != checkedDotOperand) {
      checkedDotOperand.run();
    }

  }
  private static void check_6q36mf_a1a0a0a0a0a3a0d0ic(Runnable checkedDotOperand) {
    if (null != checkedDotOperand) {
      checkedDotOperand.run();
    }

  }
  private static <T> T as_6q36mf_a0a0a1a0a0a0a0ab(Object o, Class<T> type) {
    return (type.isInstance(o) ? (T) o : null);
  }
}
