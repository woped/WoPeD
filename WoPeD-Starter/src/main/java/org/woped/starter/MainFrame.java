package org.woped.starter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizeSequencingPolicies;
import org.woped.config.general.WoPeDRecentFile;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.VEPController;
import org.woped.core.controller.ViewEvent;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.editor.action.ActionButtonListener;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.gui.images.svg.F_icon;
import org.woped.gui.images.svg.Twitter_icon;
import org.woped.gui.images.svg.analyze_capacityplanning;
import org.woped.gui.images.svg.analyze_coloring;
import org.woped.gui.images.svg.analyze_metric;
import org.woped.gui.images.svg.analyze_metric_builder;
import org.woped.gui.images.svg.analyze_metric_mass_import;
import org.woped.gui.images.svg.analyze_quantitative_simulation;
import org.woped.gui.images.svg.analyze_reachability_graph;
import org.woped.gui.images.svg.analyze_semanticalanalysis;
import org.woped.gui.images.svg.analyze_tokengame;
import org.woped.gui.images.svg.apromore_export;
import org.woped.gui.images.svg.apromore_import;
import org.woped.gui.images.svg.document_new;
import org.woped.gui.images.svg.document_save;
import org.woped.gui.images.svg.edit_copy;
import org.woped.gui.images.svg.edit_paste;
import org.woped.gui.images.svg.editor_copy;
import org.woped.gui.images.svg.editor_cut;
import org.woped.gui.images.svg.editor_group;
import org.woped.gui.images.svg.editor_paste;
import org.woped.gui.images.svg.editor_redo;
import org.woped.gui.images.svg.editor_undo;
import org.woped.gui.images.svg.editor_ungroup;
import org.woped.gui.images.svg.file_close;
import org.woped.gui.images.svg.file_exportas;
import org.woped.gui.images.svg.file_new;
import org.woped.gui.images.svg.file_open;
import org.woped.gui.images.svg.file_print;
import org.woped.gui.images.svg.file_recent;
import org.woped.gui.images.svg.file_save;
import org.woped.gui.images.svg.file_saveas;
import org.woped.gui.images.svg.format_justify_left;
import org.woped.gui.images.svg.forms_and_join;
import org.woped.gui.images.svg.forms_and_join_xor_split;
import org.woped.gui.images.svg.forms_and_split;
import org.woped.gui.images.svg.forms_and_split_join;
import org.woped.gui.images.svg.forms_place;
import org.woped.gui.images.svg.forms_subprocess;
import org.woped.gui.images.svg.forms_transition;
import org.woped.gui.images.svg.forms_xor_join;
import org.woped.gui.images.svg.forms_xor_join_and_split;
import org.woped.gui.images.svg.forms_xor_split;
import org.woped.gui.images.svg.forms_xor_split_join;
import org.woped.gui.images.svg.help_about;
import org.woped.gui.images.svg.help_configuration;
import org.woped.gui.images.svg.help_contents;
import org.woped.gui.images.svg.help_manual;
import org.woped.gui.images.svg.help_reportbug;
import org.woped.gui.images.svg.help_smaplenets;
import org.woped.gui.images.svg.process_to_text;
import org.woped.gui.images.svg.register;
import org.woped.gui.images.svg.text_to_process;
import org.woped.gui.images.svg.tokengame_edit_autoPlay;
import org.woped.gui.images.svg.tokengame_edit_step_by_step;
import org.woped.gui.images.svg.tokengame_play_jump_into_subprocess;
import org.woped.gui.images.svg.tokengame_play_jump_out_of_subprocess;
import org.woped.gui.images.svg.tokengame_play_pause;
import org.woped.gui.images.svg.tokengame_play_seek_backward;
import org.woped.gui.images.svg.tokengame_play_seek_forward;
import org.woped.gui.images.svg.tokengame_play_start;
import org.woped.gui.images.svg.tokengame_play_stop;
import org.woped.gui.images.svg.tokengame_tokengame_exit;
import org.woped.gui.images.svg.view_change_modelling_direction;
import org.woped.gui.images.svg.view_optimize_layout;
import org.woped.gui.images.svg.window_arrange;
import org.woped.gui.images.svg.window_cascadewindows;
import org.woped.gui.images.svg.woped_community;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphRibbonMenu;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewEvents;
import org.woped.starter.osxMenu.OSXFullscreen;
import org.woped.starter.osxMenu.OSXMenu;
import org.woped.starter.osxMenu.OSXMenuAdapter;
import org.woped.starter.osxMenu.OSXMenuItem;

public class MainFrame extends JRibbonFrame implements IUserInterface {
	//

    private static final long serialVersionUID = 1L;

    private RibbonContextualTaskGroup tokengameGroup = null;
    private CoverabilityGraphRibbonMenu coverabilityGraphExtension = null;

    private JCommandButton taskbarButtonNew = null;
    private JCommandButton taskbarButtonSave = null;
    private JCommandButton taskbarButtonClose = null;
    private JCommandButton taskbarButtonPaste = null;
    private JCommandButton taskbarButtonCopy = null;
    private JCommandButton taskbarButtonUndo = null;
    private JCommandButton taskbarButtonRedo = null;
    private JCommandButton taskbarButtonTokengame = null;
    private JCommandButton taskbarButtonAnalyze = null;
    private JCommandButton taskbarButtonConfig = null;


    private JRibbonBand saveBand = null;
    private JRibbonBand documentBand = null;
    private JRibbonBand outputBand = null;
    private JRibbonBand apromoreBand = null;
    private JRibbonBand editBand = null;
    private JRibbonBand formsBand = null;
    private JRibbonBand layoutBand = null;
    private JRibbonBand windowsBand = null;
    private JRibbonBand sidebarBand = null;
    private JRibbonBand analyzeBand = null;
    private JRibbonBand metricsBand = null;
    private JRibbonBand p2tBand = null;
    private JRibbonBand optionsAndHelpBand = null;
    private JRibbonBand tokengameCloseBand = null;
    private JRibbonBand tokengameStepBand = null;
    private JRibbonBand tokengameAutoBand = null;
    private JRibbonBand registrationBand = null;
    private JRibbonBand socialMediaBand = null;

    private RibbonTask fileTask = null;
    private RibbonTask editTask = null;
    private RibbonTask viewTask = null;
    private RibbonTask analyzeTask = null;
    private RibbonTask optionsHelpTask = null;
    private RibbonTask tokengameTask = null;
    private RibbonTask communityTask = null;

    private JCommandButton newButton = null;
    private JCommandButton openButton = null;
    private JCommandButton recentButton = null;
    private JCommandButton closeButton = null;

    private JCommandButton saveButton = null;
    private JCommandButton saveAsButton = null;

    private JCommandButton printButton = null;
    private JCommandButton exportAsButton = null;

    private JCommandButton importApromoreButton = null;
    private JCommandButton exportApromoreButton = null;

    private JCommandButton undoButton = null;
    private JCommandButton redoButton = null;
    private JCommandButton cutButton = null;
    private JCommandButton copyButton = null;
    private JCommandButton pasteButton = null;
    private JCommandButton groupButton = null;
    private JCommandButton ungroupButton = null;

    private JCommandButton placeButton = null;
    private JCommandButton transitionButton = null;
    private JCommandButton xorSplitButton = null;
    private JCommandButton xorJoinButton = null;
    private JCommandButton andSplitButton = null;
    private JCommandButton andJoinButton = null;
    private JCommandButton xorSplitJoinButton = null;
    private JCommandButton andJoinXorSplitButton = null;
    private JCommandButton xorJoinAndSplitButton = null;
    private JCommandButton andSplitJoinButton = null;
    private JCommandButton subprocessButton = null;

    private JCommandButton changeOrientationButton = null;
    private JCommandButton optimizeLayoutButton = null;

    private JCommandButton arrangeButton = null;
    private JCommandButton cascadeButton = null;

    private JRibbonComponent overviewComponent = null;
    private JRibbonComponent treeviewComponent = null;
    private JCheckBox overviewCheckbox = null;
    private JCheckBox treeviewCheckbox = null;
    private JCommandButton tokengameStartButton = null;
    private JCommandButton coverabilityGraphButton = null;
    private JCommandButton coloringButton = null;
    private JCommandButton semanticalAnalysisButton = null;
    private JCommandButton capacityPlanningButton = null;
    private JCommandButton quantitativeSimulationButton = null;

    private JCommandButton processMetricsButton = null;
    private JCommandButton processMassAnalyzeButton = null;
    private JCommandButton processMetricsBuilderButton = null;

    private JCommandButton p2tButton = null;
    private JCommandButton t2pButton = null;
    
    private JCommandButton configurationButton = null;
    private JCommandButton manualButton = null;
    private JCommandButton contentsButton = null;
    private JCommandButton sampleNetsButton = null;
    private JCommandButton reportBugButton = null;
    private JCommandButton aboutButton = null;

    private JCommandButton tokengameCloseButton = null;

    private JCommandButton stepWiseButton = null;
    private JCommandButton backwardButton = null;
    private JCommandButton forwardButton = null;
    private JCommandButton stopButton = null;
    private JCommandButton jumpOutOfSubprocessButton = null;
    private JCommandButton jumpIntoSubProcessButton = null;

    private JCommandButton autoPlayButton = null;
    private JCommandButton startButton = null;
    private JCommandButton pauseButton = null;

    private JCommandButton facebookButton = null;
//    private JCommandButton googleplusButton = null;
    private JCommandButton twitterButton = null;
    private JCommandButton signUpButton = null;
    private JCommandButton communityButton = null;

    private JCommandPopupMenu m_sampleMenu = null;
    private JCommandPopupMenu m_recentMenu = null;

    private AbstractApplicationMediator m_mediator = null;

    private OSXMenuAdapter menuAdapter = null;
    private OSXMenu osxFileMenu = null;
    private OSXMenu recentFiles = null;
    private OSXMenu osxEditMenu = null;
    private OSXMenu osxAnalyzeMenu = null;
    private OSXMenu tokengameMenu = null;
    private OSXMenu osxViewMenu = null;
    private OSXMenu osxCommunityMenu = null;
    private OSXMenu osxHelpMenu = null;

    public void initialize(AbstractApplicationMediator mediator) {

        setMediator(mediator);
        /**
         * Creation of a new MacOSX MenuBar if on specific platform
         *
         * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
         */
        if (Platform.isMac()) {
            // Get the frame
            getOSXMenu();
        }

        getRibbon().addTaskbarComponent(getTaskbarButtonNew());
        getRibbon().addTaskbarComponent(getTaskbarButtonSave());
        getRibbon().addTaskbarComponent(getTaskbarButtonClose());
        getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
        getRibbon().addTaskbarComponent(getTaskbarButtonPaste());
        getRibbon().addTaskbarComponent(getTaskbarButtonCopy());
        getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
        getRibbon().addTaskbarComponent(getTaskbarButtonUndo());
        getRibbon().addTaskbarComponent(getTaskbarButtonRedo());
        getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
        getRibbon().addTaskbarComponent(getTaskbarButtonTokengame());
        getRibbon().addTaskbarComponent(getTaskbarButtonAnalyze());
        getRibbon().addTaskbarComponent(new JSeparator(JSeparator.VERTICAL));
        getRibbon().addTaskbarComponent(getTaskbarButtonConfig());

        getRibbon().addTask(getFileTask());
        getRibbon().addTask(getEditTask());
        getRibbon().addTask(getAnalyzeTask());
        getRibbon().addTask(getViewTask());
        getRibbon().addTask(getOptionsHelpTask());
        getRibbon().addTask(getCommunityTask());
        getRibbon().addContextualTaskGroup(getTokengameGroup());

        //ribbon controls for coverability graph are outsourced in module CoverabilityGraph
        coverabilityGraphExtension = new CoverabilityGraphRibbonMenu(mediator);
        getRibbon().addContextualTaskGroup(coverabilityGraphExtension.getContextGroup());

        registerEventProcessor(mediator);
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(mediator, "InternalFrameCount", null, null));
    }

    private void registerEventProcessor(AbstractApplicationMediator mediator) {
        MenuViewEventProcessor menuViewEventProcessor = new MenuViewEventProcessor(mediator);
        VEPController vepController = mediator.getVepController();
        vepController.register(AbstractViewEvent.VIEWEVENTTYPE_GUI, menuViewEventProcessor);
        vepController.register(AbstractViewEvent.VIEWEVENTTYPE_EDIT, menuViewEventProcessor);
        vepController.register(AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, menuViewEventProcessor);
    }

    private void setPopupTooltip(JCommandButton button, String prefix) {
        button.setPopupRichTooltip(new RichTooltip(Messages.getString(prefix + ".text"), Messages.getString(prefix + ".tooltip")));
    }

    /**
     * ******.
     * <p>
     * Creates a new menuAdapter
     * Adds menuitems for taskgroups
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXMenu() {

        menuAdapter = new OSXMenuAdapter((JFrame) SwingUtilities.getRoot(this));

        getOSXFileMenu();
        getOSXEditMenu();
        getOSXAnalyzeMenu();
        getOSXViewMenu();
        getOSXCommunityMenu();
        getOSXHelpMenu();

    }

    /**
     * Creates the OSX file menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXFileMenu() {
        AbstractApplicationMediator mediator = m_mediator;
        //new File Menu
        osxFileMenu = new OSXMenu(Messages.getTitle("Menu.File"));

        osxFileMenu.addMenuItem(Messages.getTitle("Action.NewEditor"), "Action.NewEditor").addAction(mediator, ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW);
        osxFileMenu.addMenuItem(Messages.getTitle("Action.OpenEditor"), "Action.OpenEditor").addAction(mediator, ActionFactory.ACTIONID_OPEN, AbstractViewEvent.OPEN);

        recentFiles = new OSXMenu(Messages.getTitle("Menu.File.RecentMenu"));
        recentFiles.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent arg0) {
                recentFiles.removeAll();
                getRecentMenu();
                for (Component temp : m_recentMenu.getMenuComponents()) {
                    final JCommandMenuButton a = (JCommandMenuButton) temp;
                    OSXMenuItem recentFile = recentFiles.addMenuItem(a.getText());
                    recentFile.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            a.doActionClick();
                        }
                    });
                }
            }

            @Override
            public void menuDeselected(MenuEvent arg0) {
            }

            @Override
            public void menuCanceled(MenuEvent arg0) {
            }

        });
        osxFileMenu.addSubMenu(recentFiles);

        osxFileMenu.addMenuItem(Messages.getTitle("Action.CloseEditor"), "Action.CloseEditor").addAction(mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE);
        osxFileMenu.addSeparator();
        osxFileMenu.addMenuItem(Messages.getTitle("Action.SaveEditor"), "Action.SaveEditor").addAction(mediator, ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE);
        osxFileMenu.addMenuItem(Messages.getTitle("Action.EditorSaveAs"), "Action.EditorSaveAs").addAction(mediator, ActionFactory.ACTIONID_SAVEAS, AbstractViewEvent.SAVEAS);
        osxFileMenu.addSeparator();
        osxFileMenu.addMenuItem(Messages.getTitle("Action.PrintEditor"), "Action.PrintEditor").addAction(mediator, ActionFactory.ACTIONID_PRINT, AbstractViewEvent.PRINT);
        osxFileMenu.addMenuItem(Messages.getTitle("Action.Export"), "Action.Export").addAction(mediator, ActionFactory.ACTIONID_EXPORT, AbstractViewEvent.EXPORT);

        osxFileMenu.addSeparator();
        OSXMenu apromoreMenu = new OSXMenu(Messages.getString("Apromore.UI.TextBand.Title"));
        apromoreMenu.addMenuItem(Messages.getString("Apromore.UI.TextBand.Import.Text"), "Action.ImportApromore").addAction(mediator, ActionFactory.ACTIONID_IMPORTAPRO, AbstractViewEvent.IMPORTAPRO);
        apromoreMenu.addMenuItem(Messages.getString("Apromore.UI.TextBand.Export.Text"), "Action.ExportApromore").addAction(mediator, ActionFactory.ACTIONID_EXPORTAPRO, AbstractViewEvent.EXPORTAPRO);
        osxFileMenu.addSubMenu(apromoreMenu);
        menuAdapter.addMenu(osxFileMenu);

    }

    /**
     * Creates the OSX edit menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXEditMenu() {
        AbstractApplicationMediator mediator = m_mediator;
        osxEditMenu = new OSXMenu(Messages.getTitle("Menu.Edit"));

        osxEditMenu.addMenuItem(Messages.getTitle("Action.Undo"), "Action.Undo").addAction(mediator, ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO);
        osxEditMenu.addMenuItem(Messages.getTitle("Action.Redo"), "Action.Redo").addAction(mediator, ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO);
        osxEditMenu.addSeparator();
        osxEditMenu.addMenuItem(Messages.getTitle("Action.CutSelection"), "Action.CutSelection").addAction(mediator, ActionFactory.ACTIONID_CUT, AbstractViewEvent.CUT);
        osxEditMenu.addMenuItem(Messages.getTitle("Action.CopySelection"), "Action.CopySelection").addAction(mediator, ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY);
        osxEditMenu.addMenuItem(Messages.getTitle("Action.PasteElements"), "Action.PasteElements").addAction(mediator, ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE);
        osxEditMenu.addSeparator();
        osxEditMenu.addMenuItem(Messages.getTitle("Action.GroupSelection"), "Action.GroupSelection").addAction(mediator, ActionFactory.ACTIONID_GROUP, AbstractViewEvent.GROUP);
        osxEditMenu.addMenuItem(Messages.getTitle("Action.UngroupSelection"), "Action.UngroupSelection").addAction(mediator, ActionFactory.ACTIONID_UNGROUP, AbstractViewEvent.UNGROUP);

        menuAdapter.addMenu(osxEditMenu);
    }


	/* OSX MENU */
    /*********/

    /**
     * Creates the OSX analyze menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXAnalyzeMenu() {
        //AbstractApplicationMediator mediator = m_mediator;
        osxAnalyzeMenu = new OSXMenu(Messages.getTitle("Task.Analyze"));
        //osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.tokengame.text"));
        //Submenu

        tokengameMenu = new OSXMenu(Messages.getString("Tools.tokengame.text"));
        OSXMenuItem tokenGameStart = tokengameMenu.addMenuItem(Messages.getTitle("TokenGame.Start"), "TokenGame.Start");
        tokenGameStart.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_OPEN_TOKENGAME, AbstractViewEvent.OPEN_TOKENGAME, tokenGameStart));

        //tokengameMenu.addMenuItem(Messages.getString("Tokengame.CloseBand.CloseButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_CLOSE_TOKENGAME, AbstractViewEvent.CLOSE_TOKENGAME);
        //OSXMenuItem tokenGameEnd = tokengameMenu.addMenuItem(Messages.getTitle("ToolBar.TokenGame.Leave"),"ToolBar.TokenGame.Leave");
        OSXMenuItem tokenGameEnd = tokengameMenu.addMenuItem(Messages.getString("Tokengame.CloseBand.CloseButton.text"), "Tokengame.CloseBand.CloseButton");
        tokenGameEnd.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CLOSE_TOKENGAME, AbstractViewEvent.CLOSE_TOKENGAME, tokenGameEnd));

        tokengameMenu.addSeparator();
        //Submenu
        OSXMenu tokengameStepModeMenu = new OSXMenu(Messages.getString("Tokengame.StepBand.title"));
        OSXMenuItem tokenGameStepByStepButton = tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.StepByStepButton.text"));
        tokenGameStepByStepButton.addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_STEP, AbstractViewEvent.TOKENGAME_STEP);

        tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.BackwardButton.text"), "Tokengame.StepBand.BackwardButton").addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_BACKWARD, AbstractViewEvent.TOKENGAME_BACKWARD);
        tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.StopButton.text"), "Tokengame.StepBand.StopButton").addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_STOP, AbstractViewEvent.TOKENGAME_STOP);

        tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.ForwardButton.text"), "Tokengame.StepBand.ForwardButton").addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_FORWARD, AbstractViewEvent.TOKENGAME_FORWARD);
        tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.JumpIntoSubProcessButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_JUMPINTO, AbstractViewEvent.TOKENGAME_JUMPINTO);
        tokengameStepModeMenu.addMenuItem(Messages.getString("Tokengame.StepBand.JumpOutOfSubprocessButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_LEAVE, AbstractViewEvent.TOKENGAME_LEAVE);
        tokengameMenu.addSubMenu(tokengameStepModeMenu);
        tokengameMenu.addSeparator();
        //Submenu
        OSXMenu tokengameAutomaticModeMenu = new OSXMenu(Messages.getString("Tokengame.AutoBand.title"));
        tokengameAutomaticModeMenu.addMenuItem(Messages.getString("Tokengame.AutoBand.AutoPlayButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_AUTO, AbstractViewEvent.TOKENGAME_AUTO);
        tokengameAutomaticModeMenu.addMenuItem(Messages.getString("Tokengame.AutoBand.StartButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_START, AbstractViewEvent.TOKENGAME_START);
        tokengameAutomaticModeMenu.addMenuItem(Messages.getString("Tokengame.AutoBand.PauseButton.text")).addAction(m_mediator, ActionFactory.ACTIONID_TOKENGAME_PAUSE, AbstractViewEvent.TOKENGAME_PAUSE);
        tokengameMenu.addSubMenu(tokengameAutomaticModeMenu);

        osxAnalyzeMenu.addSubMenu(tokengameMenu);

        osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.coloring.text"), "Action.Coloring").addAction(m_mediator, ActionFactory.ACTIONID_COLORING, AbstractViewEvent.COLORING);
        osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.semanticalAnalysis.text"), "Tools.semanticalAnalysis").addAction(m_mediator, ActionFactory.ACTIONID_SEMANTICAL_ANALYSIS, AbstractViewEvent.ANALYSIS_WOPED);
        osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.capacityPlanning.text"), "Tools.capacityPlanning").addAction(m_mediator, ActionFactory.ACTIONID_QUANTCAP, AbstractViewEvent.QUANTCAP);
        osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.quantitativeSimulation.text"), "Tools.quantitativeSimulation").addAction(m_mediator, ActionFactory.ACTIONID_QUANTSIM, AbstractViewEvent.QUANTSIM);
        osxAnalyzeMenu.addMenuItem(Messages.getString("Tools.reachabilityGraph.text"), "Tools.reachabilityGraph").addAction(m_mediator, ActionFactory.ACTIONID_REACHGRAPH_START, AbstractViewEvent.REACHGRAPH);
        osxAnalyzeMenu.addSeparator();
        //Submenu
        OSXMenu processMetricsMenu = new OSXMenu(Messages.getString("Metrics.textBandTitle"));
        processMetricsMenu.addMenuItem(Messages.getString("Metrics.processmetricsmassanalysis.text")).addAction(m_mediator, ActionFactory.ACTIONID_MASSMETRICANALYSE, AbstractViewEvent.ANALYSIS_MASSMETRICANALYSE);
        processMetricsMenu.addMenuItem(Messages.getString("Metrics.processmetrics.text"), "Metrics.processmetrics").addAction(m_mediator, ActionFactory.ACTIONID_METRIC, AbstractViewEvent.ANALYSIS_METRIC);
        processMetricsMenu.addMenuItem(Messages.getString("Metrics.processmetricsbuilder.text")).addAction(m_mediator, ActionFactory.ACTIONID_METRICSBUILDER, AbstractViewEvent.ANALYSIS_METRICSBUILDER);
        osxAnalyzeMenu.addSubMenu(processMetricsMenu);
        //Submenu
        OSXMenu p2tMenu = new OSXMenu(Messages.getString("P2T.openP2T.text"));
        p2tMenu.addMenuItem(Messages.getString("P2T.text")).addAction(m_mediator, ActionFactory.ACTIONID_P2T, AbstractViewEvent.P2T);
        osxAnalyzeMenu.addSubMenu(p2tMenu);
        
        OSXMenu t2pMenu = new OSXMenu(Messages.getString("T2P.openT2P.text"));
		t2pMenu.addMenuItem(Messages.getString("T2P.text")).addAction(m_mediator, ActionFactory.ACTIONID_T2P, AbstractViewEvent.T2P);
		osxAnalyzeMenu.addSubMenu(t2pMenu);
        
        
        menuAdapter.addMenu(osxAnalyzeMenu);
    }

    /**
     * Creates the OSX view menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXViewMenu() {
        AbstractApplicationMediator mediator = m_mediator;

        final Window currentWindow = (Window) SwingUtilities.getRoot(this);

        osxViewMenu = new OSXMenu(Messages.getTitle("Menu.View"));

        osxViewMenu.addMenuItem(Messages.getTitle("Action.Minimize"), "Action.Minimize").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Frame) currentWindow).setState(Frame.ICONIFIED);
            }
        });
//Toolbar
//		osxViewMenu.addMenuItem("Toolbar switcher").addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	getRibbon().setMinimized(!getRibbon().isMinimized());
//            }
//        });

        osxViewMenu.addMenuItem(Messages.getString("View.changeModellingDirection.text"), "View.changeModellingDirection").addAction(m_mediator, ActionFactory.ACTIONID_ROTATEVIEW, AbstractViewEvent.ROTATEVIEW);
        osxViewMenu.addMenuItem(Messages.getString("View.optimizeLayout.text"), "View.optimizeLayout").addAction(m_mediator, ActionFactory.ACTIONID_GRAPHBEAUTIFIER_DEFAULT, AbstractViewEvent.GRAPHBEAUTIFIER);
        osxViewMenu.addSeparator();
        osxViewMenu.addMenuItem(Messages.getTitle("Action.Frames.Cascade")).addAction(m_mediator, ActionFactory.ACTIONID_CASCADE, AbstractViewEvent.CASCADE);
        osxViewMenu.addMenuItem(Messages.getTitle("Action.Frames.Arrange")).addAction(m_mediator, ActionFactory.ACTIONID_ARRANGE, AbstractViewEvent.ARRANGE);
        osxViewMenu.addSeparator();

        //synchronized checkbox menus
        getOverviewComponent();
        osxViewMenu.addCheckboxMenuItem(Messages.getString("Sidebar.Overview.Title"), overviewCheckbox).addAction(mediator, ActionFactory.ACTIONID_SHOWOVERVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI);
        getTreeviewComponent();
        osxViewMenu.addCheckboxMenuItem(Messages.getString("Sidebar.Treeview.Title"), treeviewCheckbox).addAction(mediator, ActionFactory.ACTIONID_SHOWTREEVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI);
        osxViewMenu.addSeparator();
        //Fullscreen support


        osxViewMenu.addMenuItem(Messages.getTitle("Action.Fullscreen"), "Action.Fullscreen").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                OSXFullscreen.toggleOSXFullscreen(currentWindow);
            }
        });

        menuAdapter.addMenu(osxViewMenu);
    }

    /**
     * Creates the OSX community menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXCommunityMenu() {
        //AbstractApplicationMediator mediator = m_mediator;
        osxCommunityMenu = new OSXMenu(Messages.getTitle("Task.Community"));
        osxCommunityMenu.addMenuItem(Messages.getString("Community.Facebook.text")).addAction(m_mediator, ActionFactory.ACTIONID_FACEBOOK, AbstractViewEvent.FACEBOOK);
 //       osxCommunityMenu.addMenuItem(Messages.getString("Community.Googleplus.text")).addAction(m_mediator, ActionFactory.ACTIONID_GOOGLEPLUS, AbstractViewEvent.GOOGLEPLUS);
        osxCommunityMenu.addMenuItem(Messages.getString("Community.Twitter.text")).addAction(m_mediator, ActionFactory.ACTIONID_TWITTER, AbstractViewEvent.TWITTER);
        osxCommunityMenu.addSeparator();
        osxCommunityMenu.addMenuItem(Messages.getString("Community.Register.text")).addAction(m_mediator, ActionFactory.ACTIONID_REGISTER, AbstractViewEvent.REGISTER);
        osxCommunityMenu.addMenuItem(Messages.getString("Community.Community.text")).addAction(m_mediator, ActionFactory.ACTIONID_COMMUNITY, AbstractViewEvent.COMMUNITY);

        menuAdapter.addMenu(osxCommunityMenu);
    }

    /**
     * Creates the OSX help menu.
     *
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void getOSXHelpMenu() {
        //AbstractApplicationMediator mediator = m_mediator;
        osxHelpMenu = new OSXMenu(Messages.getTitle("Menu.Help"));

        osxHelpMenu.addMenuItem(Messages.getTitle("Menu.Help.Index"), "Menu.Help.Index").addAction(m_mediator, ActionFactory.ACTIONID_SHOWHELPINDEX, AbstractViewEvent.HELP);
        osxHelpMenu.addMenuItem(Messages.getTitle("Menu.Help.Contents"), "Menu.Help.Contents").addAction(m_mediator, ActionFactory.ACTIONID_SHOWHELPCONTENTS, AbstractViewEvent.HELP_CONTENTS);
        osxHelpMenu.addMenuItem(Messages.getString("OptionsAndHelp.ReportBug.text")).addAction(m_mediator, ActionFactory.ACTIONID_SHOWBUGREPORT, AbstractViewEvent.BUGREPORT);

        OSXMenu sampleNets = new OSXMenu(Messages.getString("OptionsAndHelp.SampleNets.text"));
        getSampleMenu();
        //clone each entry in samplenets and perform action if selected
        for (Component temp : m_sampleMenu.getMenuComponents()) {
            final JCommandMenuButton a = (JCommandMenuButton) temp;
            OSXMenuItem sampleFile = sampleNets.addMenuItem(a.getText());
            sampleFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    a.doActionClick();
                }
            });
        }
        osxHelpMenu.addSubMenu(sampleNets);

        menuAdapter.addMenu(osxHelpMenu);
    }

    /*************/
    private RibbonContextualTaskGroup getTokengameGroup() {

        if (tokengameGroup == null) {
            tokengameGroup = new RibbonContextualTaskGroup("", Color.green, getTokengameTask());
        }

        return tokengameGroup;
    }

    private void setTooltip(JCommandButton button, String prefix) {
        button.setActionRichTooltip(getTooltip(prefix));
    }

    private RichTooltip getTooltip(String prefix) {
        String text, tooltip;

        // First attempt to read the matching string from the message file
        // with the "legacy" lowercase property name. If that fails, attempt to
        // read with a capital letter as well.

        text = Messages.getString(prefix + ".text");
        if(text.startsWith("!")) {
            text = Messages.getString(prefix + ".Text");
        }

        tooltip = Messages.getString(prefix + ".tooltip");
        if(tooltip.startsWith("!")) {
            tooltip = Messages.getString(prefix + ".Tooltip");
        }

        return new RichTooltip(text, tooltip);
    }

    /**
     * Sets the tooltip.
     *
     * @param button       the button
     * @param prefix       the prefix in Messages_xx.properties
     * @param usesShortcut set to true if a shortcut shoud be added
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void setTooltip(JCommandButton button, String prefix, Boolean usesShortcut) {

        if (!usesShortcut) {
            setTooltip(button, prefix);
        } else {
            String shortcut = "";
            KeyStroke shortcutKS;
            int modifier;
            try {
                shortcutKS = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).allKeys()[0];
                modifier = shortcutKS.getModifiers();
                if ((modifier & (InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK)) != 0) {
                    shortcut += "Ctrl+";
                }
                if ((modifier & (InputEvent.META_DOWN_MASK | InputEvent.META_MASK)) != 0) {
                    if (Platform.isMac()) {
                        shortcut += ("Cmd+");
                    } else {
                        shortcut += ("Ctrl+");
                    }
                }
                if ((modifier & (InputEvent.ALT_DOWN_MASK | InputEvent.ALT_MASK)) != 0) {
                    shortcut += ("Alt+");
                }
                if ((modifier & (InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK)) != 0) {
                    shortcut += "Shift+";
                }

                if (shortcutKS.getKeyCode() == 0) {
                    setTooltip(button, prefix);
                } else {
                    shortcut += KeyEvent.getKeyText(shortcutKS.getKeyCode());
                    RichTooltip rt = getTooltip(prefix);
                    //rt.addFooterSection(shortcut); //Alternative display of Shortcut
                    rt.setTitle(rt.getTitle() + " (" + shortcut + ")");
                    button.setActionRichTooltip(rt);
                }

            } catch (NullPointerException nex) {
                RichTooltip rt = getTooltip(prefix);
                //rt.addFooterSection(shortcut); //Alternative display of Shortcut
                button.setActionRichTooltip(rt);
            } catch (Exception ex) {
            }
        }
    }

    /* ************/
    /* TASKGROUP */
    /* **********/
    private JCommandButton getTaskbarButtonNew() {

        if (taskbarButtonNew == null) {
            taskbarButtonNew = new JCommandButton("", new document_new());
            setTooltip(taskbarButtonNew, "Document.new");
            taskbarButtonNew.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW, taskbarButtonNew));
        }
        return taskbarButtonNew;
    }

    private JCommandButton getTaskbarButtonSave() {

        if (taskbarButtonSave == null) {
            taskbarButtonSave = new JCommandButton("", new document_save());
            setTooltip(taskbarButtonSave, "Save.save");
            taskbarButtonSave.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE, taskbarButtonSave));
        }
        return taskbarButtonSave;
    }

    private JCommandButton getTaskbarButtonClose() {

        if (taskbarButtonClose == null) {
            taskbarButtonClose = new JCommandButton("", new file_close());
            setTooltip(taskbarButtonClose, "Document.close");
            taskbarButtonClose.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE, taskbarButtonClose));
        }
        return taskbarButtonClose;
    }


    /***********/
	/* TASKBAR */

    private JCommandButton getTaskbarButtonPaste() {

        if (taskbarButtonPaste == null) {
            taskbarButtonPaste = new JCommandButton("", new edit_paste());
            setTooltip(taskbarButtonPaste, "Edit.paste");
            taskbarButtonPaste.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE, taskbarButtonPaste));
        }
        return taskbarButtonPaste;
    }

    private JCommandButton getTaskbarButtonCopy() {

        if (taskbarButtonCopy == null) {
            taskbarButtonCopy = new JCommandButton("", new edit_copy());
            setTooltip(taskbarButtonCopy, "Edit.copy");
            taskbarButtonCopy.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY, taskbarButtonCopy));
        }
        return taskbarButtonCopy;
    }

    private JCommandButton getTaskbarButtonUndo() {

        if (taskbarButtonUndo == null) {
            taskbarButtonUndo = new JCommandButton("", new editor_undo());
            setTooltip(taskbarButtonUndo, "Edit.undo");
            taskbarButtonUndo.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO, taskbarButtonUndo));
        }
        return taskbarButtonUndo;
    }

    private JCommandButton getTaskbarButtonRedo() {

        if (taskbarButtonRedo == null) {
            taskbarButtonRedo = new JCommandButton("", new editor_redo());
            setTooltip(taskbarButtonRedo, "Edit.redo");
            taskbarButtonRedo.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO, taskbarButtonRedo));
        }
        return taskbarButtonRedo;
    }

    private JCommandButton getTaskbarButtonTokengame() {
        if (taskbarButtonTokengame == null) {
            taskbarButtonTokengame = new JCommandButton(Messages.getString("Tools.tokengame.text"), new analyze_tokengame());
            taskbarButtonTokengame.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_OPEN_TOKENGAME, AbstractViewEvent.OPEN_TOKENGAME, taskbarButtonTokengame));
            setTooltip(taskbarButtonTokengame, "Tools.tokengame");
        }
        return taskbarButtonTokengame;
    }

    private JCommandButton getTaskbarButtonAnalyze() {

        if (taskbarButtonAnalyze == null) {
            taskbarButtonAnalyze = new JCommandButton(Messages.getString("Tools.semanticalAnalysis.text"), new analyze_semanticalanalysis());
            setTooltip(taskbarButtonAnalyze, "Tools.semanticalAnalysis");
            taskbarButtonAnalyze.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SEMANTICAL_ANALYSIS, AbstractViewEvent.ANALYSIS_WOPED, taskbarButtonAnalyze));
        }
        return taskbarButtonAnalyze;
    }

    private JCommandButton getTaskbarButtonConfig() {

        if (taskbarButtonConfig == null) {
            taskbarButtonConfig = new JCommandButton(Messages.getString("OptionsAndHelp.Configuration.text"), new help_configuration());
            setTooltip(taskbarButtonConfig, "OptionsAndHelp.Configuration");
            taskbarButtonConfig.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWCONFIG, AbstractViewEvent.CONFIG, taskbarButtonConfig));
        }
        return taskbarButtonConfig;
    }

    /*********/
    private RibbonTask getFileTask() {

        if (fileTask == null) {
            if (ConfigurationManager.getConfiguration().getApromoreUse())
                fileTask = new RibbonTask(Messages.getTitle("Task.File"), getDocumentBand(), getSaveBand(), getOutputBand(), getApromoreBand());
            else
                fileTask = new RibbonTask(Messages.getTitle("Task.File"), getDocumentBand(), getSaveBand(), getOutputBand());
            fileTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(fileTask));
        }

        return fileTask;
    }

    private RibbonTask getEditTask() {

        if (editTask == null) {
            editTask = new RibbonTask(Messages.getTitle("Task.Edit"), getEditBand(), getFormsBand());
            editTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(editTask));
        }

        return editTask;
    }

    private RibbonTask getAnalyzeTask() {

        if (analyzeTask == null) {
            if (ConfigurationManager.getConfiguration().isUseMetrics()) {
                if (ConfigurationManager.getConfiguration().getProcess2TextUse()) {
                    analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getMetricsBand(), getP2TBand());
                } else {
                    analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getMetricsBand());
                }
            } else {
                if (ConfigurationManager.getConfiguration().getProcess2TextUse()) {
                    analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getP2TBand());
                } else {
                    analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand());
                }
            }

//		if (ConfigurationManager.getConfiguration().isUseMetrics()){
//				analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getMetricsBand(), getP2TBand());
//
//		} else {
//				analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getP2TBand());
//
//		}

            analyzeTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(analyzeTask));
        }

        return analyzeTask;
    }
    /*********/
	/* TASKS */

    private RibbonTask getViewTask() {

        if (viewTask == null) {
            viewTask = new RibbonTask(Messages.getTitle("Task.View"), getLayoutBand(), getWindowsBand(), getSidebarBand());
            viewTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(viewTask));
        }

        return viewTask;
    }

    private RibbonTask getOptionsHelpTask() {

        if (optionsHelpTask == null) {
            optionsHelpTask = new RibbonTask(Messages.getTitle("Task.OptionsHelp"), getOptionsHelpBand());
            optionsHelpTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(optionsHelpTask));
        }

        return optionsHelpTask;
    }

    private RibbonTask getTokengameTask() {

        if (tokengameTask == null) {
            tokengameTask = new RibbonTask(Messages.getTitle("Task.Tokengame"), getTokengameCloseBand(), getTokengameStepBand(), getTokengameAutoBand());
            tokengameTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(tokengameTask));
        }

        return tokengameTask;
    }

    private RibbonTask getCommunityTask() {

        if (communityTask == null) {
            communityTask = new RibbonTask(Messages.getTitle("Task.Community"), getSocialMediaBand(), getRegistrationBand());
            communityTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(communityTask));

        }
        return communityTask;
    }

    /*********/
    private JRibbonBand getDocumentBand() {

        if (documentBand == null) {
            documentBand = new JRibbonBand(Messages.getString("Document.textBandTitle"), null);
            documentBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(documentBand));

            documentBand.addCommandButton(getNewButton(), RibbonElementPriority.TOP);
            documentBand.addCommandButton(getOpenButton(), RibbonElementPriority.TOP);
            documentBand.addCommandButton(getRecentButton(), RibbonElementPriority.TOP);
            documentBand.addCommandButton(getCloseButton(), RibbonElementPriority.TOP);
        }

        return documentBand;
    }

    private JRibbonBand getSaveBand() {

        if (saveBand == null) {
            saveBand = new JRibbonBand(Messages.getString("Save.textBandTitle"), null);
            saveBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(saveBand));
            saveBand.addCommandButton(getSaveButton(), RibbonElementPriority.TOP);
            saveBand.addCommandButton(getSaveAsButton(), RibbonElementPriority.TOP);
        }

        return saveBand;
    }

    private JRibbonBand getOutputBand() {

        if (outputBand == null) {
            outputBand = new JRibbonBand(Messages.getString("DataOutput.textBandTitle"), null);
            outputBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(outputBand));
            outputBand.addCommandButton(getPrintButton(), RibbonElementPriority.TOP);
            outputBand.addCommandButton(getExportButton(), RibbonElementPriority.TOP);
        }

        return outputBand;
    }

    /*********/
	/* BANDS */

    private JRibbonBand getApromoreBand() {

        if (apromoreBand == null) {
            apromoreBand = new JRibbonBand(Messages.getString("Apromore.UI.TextBand.Title"), null);
            apromoreBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(apromoreBand));
            apromoreBand.addCommandButton(getImportApromoreButton(), RibbonElementPriority.TOP);
            apromoreBand.addCommandButton(getExportApromoreButton(), RibbonElementPriority.TOP);
        }

        return apromoreBand;
    }

    private JRibbonBand getEditBand() {

        if (editBand == null) {
            editBand = new JRibbonBand(Messages.getString("Edit.textBandTitle"), null);
            editBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(editBand));
            editBand.addCommandButton(getUndoButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getRedoButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getCutButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getCopyButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getPasteButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getGroupButton(), RibbonElementPriority.TOP);
            editBand.addCommandButton(getUngroupButton(), RibbonElementPriority.TOP);
        }

        return editBand;
    }

    private JRibbonBand getFormsBand() {

        if (formsBand == null) {
            formsBand = new JRibbonBand(Messages.getString("Forms.textBandTitle"), new forms_place());
            formsBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(formsBand));

            formsBand.addCommandButton(getPlaceButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getTransitionButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getAndSplitButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getXorSplitButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getAndJoinButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getXorJoinButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getXorSplitJoinButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getAndSplitJoinButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getAndJoinXorSplitButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getXorJoinAndSplitButton(), RibbonElementPriority.MEDIUM);
            formsBand.addCommandButton(getSubprocessButton(), RibbonElementPriority.MEDIUM);
        }

        return formsBand;
    }

    private JRibbonBand getLayoutBand() {

        if (layoutBand == null) {
            layoutBand = new JRibbonBand(Messages.getString("View.textBandTitle"), new editor_group());
            layoutBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(layoutBand));
            layoutBand.startGroup();

            layoutBand.addCommandButton(getChangeOrientationButton(), RibbonElementPriority.TOP);
            layoutBand.addCommandButton(getOptimizeLayoutButton(), RibbonElementPriority.TOP);
        }

        return layoutBand;
    }

    private JRibbonBand getAnalyzeBand() {

        if (analyzeBand == null) {
            analyzeBand = new JRibbonBand(Messages.getString("Tools.textBandTitle"), new analyze_semanticalanalysis());
            analyzeBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(analyzeBand));
            analyzeBand.startGroup();

            analyzeBand.addCommandButton(getTokengameStartButton(), RibbonElementPriority.TOP);
            analyzeBand.addCommandButton(getColoringButton(), RibbonElementPriority.TOP);
            analyzeBand.addCommandButton(getSemanticalAnalysisButton(), RibbonElementPriority.TOP);
            analyzeBand.addCommandButton(getCapacityPlanningButton(), RibbonElementPriority.TOP);
            analyzeBand.addCommandButton(getQuantitativeSimulationButton(), RibbonElementPriority.TOP);
            analyzeBand.addCommandButton(getCoverabilityGraphButton(), RibbonElementPriority.TOP);
        }

        return analyzeBand;
    }

    private JRibbonBand getMetricsBand() {

        if (metricsBand == null) {
            metricsBand = new JRibbonBand(Messages.getString("Metrics.textBandTitle"), new analyze_semanticalanalysis());
            metricsBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(metricsBand));
            metricsBand.startGroup();

            metricsBand.addCommandButton(getProcessMetricsButton(), RibbonElementPriority.TOP);
            metricsBand.addCommandButton(getProcessMassAnalyzeButton(), RibbonElementPriority.TOP);
            metricsBand.addCommandButton(getProcessMetricsBuilderButton(), RibbonElementPriority.TOP);
        }

        return metricsBand;
    }

    private JRibbonBand getP2TBand() {
        if (p2tBand == null) {
            p2tBand = new JRibbonBand(Messages.getString("P2T.textBandTitle"), new process_to_text());
            p2tBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(p2tBand));
            p2tBand.startGroup();

            p2tBand.addCommandButton(getP2TButton(), RibbonElementPriority.TOP);
            p2tBand.addCommandButton(getT2PButton(), RibbonElementPriority.TOP);
        }

        return p2tBand;
    }

    private JRibbonBand getWindowsBand() {

        if (windowsBand == null) {
            windowsBand = new JRibbonBand(Messages.getString("WindowPreferences.textBandTitle"), null);
            windowsBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(windowsBand));

            windowsBand.addCommandButton(getCascadeButton(), RibbonElementPriority.TOP);
            windowsBand.addCommandButton(getArrangeButton(), RibbonElementPriority.TOP);
        }

        return windowsBand;
    }

    private JRibbonBand getSidebarBand() {

        if (sidebarBand == null) {
            sidebarBand = new JRibbonBand(Messages.getString("ShowHide.textBandTitle"), new format_justify_left(), null);
            sidebarBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(sidebarBand));

            sidebarBand.addRibbonComponent(getOverviewComponent());
            sidebarBand.addRibbonComponent(getTreeviewComponent());
        }

        return sidebarBand;
    }

    private JRibbonBand getOptionsHelpBand() {

        if (optionsAndHelpBand == null) {
            optionsAndHelpBand = new JRibbonBand(
                    Messages.getString("OptionsAndHelp.textBandTitle"), null);
            optionsAndHelpBand.setResizePolicies(CoreRibbonResizePolicies
                    .getCorePoliciesNone(optionsAndHelpBand));
            optionsAndHelpBand.startGroup();

            optionsAndHelpBand.addCommandButton(getConfigurationButton(),
                    RibbonElementPriority.TOP);
            optionsAndHelpBand.addCommandButton(getManualButton(),
                    RibbonElementPriority.TOP);
            optionsAndHelpBand.addCommandButton(getContentsButton(),
                    RibbonElementPriority.TOP);
            optionsAndHelpBand.addCommandButton(getSampleNetsButton(),
                    RibbonElementPriority.TOP);
            optionsAndHelpBand.addCommandButton(getReportBugButton(),
                    RibbonElementPriority.TOP);
            optionsAndHelpBand.addCommandButton(getAboutButton(),
                    RibbonElementPriority.TOP);
        }
        return optionsAndHelpBand;
    }

    private JRibbonBand getTokengameCloseBand() {

        if (tokengameCloseBand == null) {
            tokengameCloseBand = new JRibbonBand(Messages.getString("Tokengame.CloseBand.title"), new tokengame_play_start());
            tokengameCloseBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(tokengameCloseBand));
            tokengameCloseBand.addCommandButton(getTokengameCloseButton(), RibbonElementPriority.TOP);
        }

        return tokengameCloseBand;
    }

    private JRibbonBand getTokengameStepBand() {

        if (tokengameStepBand == null) {
            tokengameStepBand = new JRibbonBand(Messages.getString("Tokengame.StepBand.title"), new tokengame_play_start());
            tokengameStepBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(tokengameStepBand));

            tokengameStepBand.addCommandButton(getStepWiseButton(), RibbonElementPriority.TOP);
            tokengameStepBand.addCommandButton(getBackwardButton(), RibbonElementPriority.TOP);
            tokengameStepBand.addCommandButton(getStopButton(), RibbonElementPriority.TOP);
            tokengameStepBand.addCommandButton(getForwardButton(), RibbonElementPriority.TOP);
            tokengameStepBand.addCommandButton(getJumpIntoSubProcessButton(), RibbonElementPriority.TOP);
            tokengameStepBand.addCommandButton(getJumpOutOfSubprocessButton(), RibbonElementPriority.TOP);
        }

        return tokengameStepBand;
    }

    private JRibbonBand getTokengameAutoBand() {

        if (tokengameAutoBand == null) {
            tokengameAutoBand = new JRibbonBand(Messages.getString("Tokengame.AutoBand.title"), new tokengame_edit_step_by_step());
            tokengameAutoBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(tokengameAutoBand));

            tokengameAutoBand.addCommandButton(getAutoPlayButton(), RibbonElementPriority.TOP);
            tokengameAutoBand.addCommandButton(getStartButton(), RibbonElementPriority.TOP);
            tokengameAutoBand.addCommandButton(getPauseButton(), RibbonElementPriority.TOP);
        }

        return tokengameAutoBand;
    }

    private JRibbonBand getSocialMediaBand() {

        if (socialMediaBand == null) {
            socialMediaBand = new JRibbonBand(Messages.getString("Community.socialmediaBandTitle"), null);
            socialMediaBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(socialMediaBand));
            socialMediaBand.addCommandButton(getFacebookButton(), RibbonElementPriority.TOP);
//            socialMediaBand.addCommandButton(getGoogleplusButton(), RibbonElementPriority.TOP);
            socialMediaBand.addCommandButton(getTwitterButton(), RibbonElementPriority.TOP);


        }
        return socialMediaBand;
    }

    private JRibbonBand getRegistrationBand() {

        if (registrationBand == null) {
            registrationBand = new JRibbonBand(Messages.getString("Community.registerBandTitle"), null);
            registrationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(registrationBand));
            registrationBand.addCommandButton(getSignUpButton(), RibbonElementPriority.TOP);
            registrationBand.addCommandButton(getCommunityButton(), RibbonElementPriority.TOP);

        }
        return registrationBand;
    }

    /***********/
    private JCommandButton getNewButton() {

        if (newButton == null) {
            newButton = new JCommandButton(Messages.getString("Document.new.text"), new file_new());
            newButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW, newButton));
            addShortcutToJCommandButton("Action.NewEditor", newButton, ActionFactory.ACTIONID_NEW);
            setTooltip(newButton, "Document.new", true);
        }

        return newButton;
    }

    private JCommandButton getOpenButton() {

        if (openButton == null) {
            openButton = new JCommandButton(Messages.getString("Document.open.text"), new file_open());
            openButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_OPEN, AbstractViewEvent.OPEN, openButton));
            addShortcutToJCommandButton("Action.OpenEditor", openButton, ActionFactory.ACTIONID_OPEN);
            setTooltip(openButton, "Document.open", true);
        }

        return openButton;
    }

    private JCommandButton getRecentButton() {

        if (recentButton == null) {
            recentButton = new JCommandButton(Messages.getString("Document.recent.text"), new file_recent());
            recentButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);

            recentButton.setPopupCallback(new PopupPanelCallback() {
                @Override
                public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                    return getRecentMenu();
                }
            });
            setPopupTooltip(recentButton, "Document.recent");
        }

        return recentButton;
    }
    /***********/
	/* BUTTONS */

    private JCommandPopupMenu getRecentMenu() {
        m_recentMenu = new JCommandPopupMenu();
        Vector<?> v = ConfigurationManager.getConfiguration().getRecentFiles();
        if (v.size() != 0) {
            for (int idx = 0; idx < v.size(); idx++) {
                String name = ((WoPeDRecentFile) v.get(idx)).getName();
                String path = ((WoPeDRecentFile) v.get(idx)).getPath();

                JCommandMenuButton recentMenuItem = new JCommandMenuButton(
                        name, new file_recent());
                recentMenuItem.addActionListener(new recentFileListener(path));

                m_recentMenu.addMenuButton(recentMenuItem);
            }
        } else {
            JCommandMenuButton emptyItem = new JCommandMenuButton(
                    Messages.getString("Menu.File.RecentMenu.empty"),
                    new file_recent());
            m_recentMenu.addMenuButton(emptyItem);
            emptyItem.setEnabled(false);
            m_recentMenu.addMenuButton(emptyItem);
        }

        return m_recentMenu;
    }

    private JCommandButton getCloseButton() {

        if (closeButton == null) {
            closeButton = new JCommandButton(Messages.getString("Document.close.text"), new file_close());
            closeButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE, closeButton));
            addShortcutToJCommandButton("Action.CloseEditor", closeButton, ActionFactory.ACTIONID_CLOSE);
            setTooltip(closeButton, "Document.close", true);
        }

        return closeButton;
    }

    private JCommandButton getSaveButton() {

        if (saveButton == null) {
            saveButton = new JCommandButton(Messages.getString("Save.save.text"), new file_save());
            saveButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE, saveButton));
            addShortcutToJCommandButton("Action.SaveEditor", saveButton, ActionFactory.ACTIONID_SAVE);
            setTooltip(saveButton, "Save.save", true);
        }

        return saveButton;
    }

    private JCommandButton getSaveAsButton() {

        if (saveAsButton == null) {
            saveAsButton = new JCommandButton(Messages.getString("Save.saveAs.text"), new file_saveas());
            saveAsButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SAVEAS, AbstractViewEvent.SAVEAS, saveAsButton));
            addShortcutToJCommandButton("Action.EditorSaveAs", saveAsButton, ActionFactory.ACTIONID_SAVEAS);
            setTooltip(saveAsButton, "Save.saveAs", true);
        }

        return saveAsButton;
    }

    private JCommandButton getPrintButton() {

        if (printButton == null) {
            printButton = new JCommandButton(Messages.getString("DataOutput.print.text"), new file_print());
            printButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_PRINT, AbstractViewEvent.PRINT, printButton));
            addShortcutToJCommandButton("Action.PrintEditor", printButton, ActionFactory.ACTIONID_PRINT);
            setTooltip(printButton, "DataOutput.print", true);
        }

        return printButton;
    }

    private JCommandButton getExportButton() {

        if (exportAsButton == null) {
            exportAsButton = new JCommandButton(Messages.getString("DataOutput.exportAs.text"), new file_exportas());
            exportAsButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_EXPORT, AbstractViewEvent.EXPORT, exportAsButton));
            addShortcutToJCommandButton("Action.Export", exportAsButton, ActionFactory.ACTIONID_EXPORT);
            setTooltip(exportAsButton, "DataOutput.exportAs", true);
        }

        return exportAsButton;
    }

    private JCommandButton getImportApromoreButton() {

        if (importApromoreButton == null) {
            importApromoreButton = new JCommandButton(Messages.getString("Apromore.UI.TextBand.Import.Text"), new apromore_import());
            importApromoreButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_IMPORTAPRO, AbstractViewEvent.IMPORTAPRO, importApromoreButton));
            addShortcutToJCommandButton("Action.ImportApromore", importApromoreButton, ActionFactory.ACTIONID_IMPORTAPRO);
            setTooltip(importApromoreButton, "Apromore.UI.TextBand.Import", true);
        }

        return importApromoreButton;
    }

    private JCommandButton getExportApromoreButton() {

        if (exportApromoreButton == null) {
            exportApromoreButton = new JCommandButton(Messages.getString("Apromore.UI.TextBand.Export.Text"), new apromore_export());
            exportApromoreButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_EXPORTAPRO, AbstractViewEvent.EXPORTAPRO, exportApromoreButton));
            addShortcutToJCommandButton("Action.ExportApromore", exportApromoreButton, ActionFactory.ACTIONID_EXPORTAPRO);
            setTooltip(exportApromoreButton, "Apromore.UI.TextBand.Export", true);
        }

        return exportApromoreButton;
    }

    private JCommandButton getUndoButton() {

        if (undoButton == null) {
            undoButton = new JCommandButton(Messages.getString("Edit.undo.text"), new editor_undo());
            undoButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO, undoButton));
            addShortcutToJCommandButton("Action.Undo", undoButton, ActionFactory.ACTIONID_UNDO);
            setTooltip(undoButton, "Edit.undo", true);
        }

        return undoButton;
    }

    private JCommandButton getRedoButton() {

        if (redoButton == null) {
            redoButton = new JCommandButton(Messages.getString("Edit.redo.text"), new editor_redo());
            redoButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO, redoButton));
            addShortcutToJCommandButton("Action.Redo", redoButton, ActionFactory.ACTIONID_REDO);
            setTooltip(redoButton, "Edit.redo", true);
        }

        return redoButton;
    }

    private JCommandButton getCutButton() {

        if (cutButton == null) {
            cutButton = new JCommandButton(Messages.getString("Edit.cut.text"), new editor_cut());
            cutButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CUT, AbstractViewEvent.CUT, cutButton));
            addShortcutToJCommandButton("Action.CutSelection", cutButton, ActionFactory.ACTIONID_CUT);
            setTooltip(cutButton, "Edit.cut", true);
        }

        return cutButton;
    }

    private JCommandButton getCopyButton() {

        if (copyButton == null) {
            copyButton = new JCommandButton(Messages.getString("Edit.copy.text"), new editor_copy());
            copyButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY, copyButton));
            addShortcutToJCommandButton("Action.CopySelection", copyButton, ActionFactory.ACTIONID_COPY);
            setTooltip(copyButton, "Edit.copy", true);
        }

        return copyButton;
    }

    private JCommandButton getPasteButton() {

        if (pasteButton == null) {
            pasteButton = new JCommandButton(Messages.getString("Edit.paste.text"), new editor_paste());
            pasteButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE, pasteButton));
            addShortcutToJCommandButton("Action.PasteElements", pasteButton, ActionFactory.ACTIONID_PASTE);
            setTooltip(pasteButton, "Edit.paste", true);
        }

        return pasteButton;
    }

    private JCommandButton getGroupButton() {

        if (groupButton == null) {
            groupButton = new JCommandButton(Messages.getString("Edit.group.text"), new editor_group());
            groupButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_GROUP, AbstractViewEvent.GROUP, groupButton));
            addShortcutToJCommandButton("Action.GroupSelection", groupButton, ActionFactory.ACTIONID_GROUP);
            setTooltip(groupButton, "Edit.group", true);

        }

        return groupButton;
    }

    private JCommandButton getUngroupButton() {

        if (ungroupButton == null) {
            ungroupButton = new JCommandButton(Messages.getString("Edit.ungroup.text"), new editor_ungroup());
            ungroupButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_UNGROUP, AbstractViewEvent.UNGROUP, ungroupButton));
            addShortcutToJCommandButton("Action.UngroupSelection", ungroupButton, ActionFactory.ACTIONID_UNGROUP);
            setTooltip(ungroupButton, "Edit.ungroup", true);
        }

        return ungroupButton;
    }

    private JCommandButton getPlaceButton() {

        if (placeButton == null) {
            placeButton = new JCommandButton(Messages.getString("Forms.place.text"), new forms_place());
            placeButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_PLACE, AbstractViewEvent.DRAWMODE_PLACE, placeButton));
            addShortcutToMouseButton("Forms.place", placeButton, ActionFactory.ACTIONID_DRAWMODE_PLACE, false);
            setTooltip(placeButton, "Forms.place", true);

        }

        return placeButton;
    }

    private JCommandButton getTransitionButton() {

        if (transitionButton == null) {
            transitionButton = new JCommandButton(Messages.getString("Forms.transition.text"), new forms_transition());
            transitionButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_TRANSITION, AbstractViewEvent.DRAWMODE_TRANSITION, transitionButton));
            addShortcutToMouseButton("Forms.transition", transitionButton, ActionFactory.ACTIONID_DRAWMODE_TRANSITION, false);
            setTooltip(transitionButton, "Forms.transition", true);
        }

        return transitionButton;
    }

    private JCommandButton getXorSplitButton() {

        if (xorSplitButton == null) {
            xorSplitButton = new JCommandButton(Messages.getString("Forms.XORSplit.text"), new forms_xor_split());
            xorSplitButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_XORSPLIT, AbstractViewEvent.DRAWMODE_XORSPLIT, xorSplitButton));
            addShortcutToMouseButton("Forms.XORSplit", xorSplitButton, ActionFactory.ACTIONID_DRAWMODE_XORSPLIT, false);
            setTooltip(xorSplitButton, "Forms.XORSplit", true);
        }

        return xorSplitButton;
    }

    private JCommandButton getXorJoinButton() {

        if (xorJoinButton == null) {
            xorJoinButton = new JCommandButton(Messages.getString("Forms.XORJoin.text"), new forms_xor_join());
            xorJoinButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_XORJOIN, AbstractViewEvent.DRAWMODE_XORJOIN, xorJoinButton));
            addShortcutToMouseButton("Forms.XORJoin", xorJoinButton, ActionFactory.ACTIONID_DRAWMODE_XORJOIN, true);
            setTooltip(xorJoinButton, "Forms.XORJoin", true);
        }

        return xorJoinButton;
    }

    private JCommandButton getAndSplitButton() {
        if (andSplitButton == null) {
            andSplitButton = new JCommandButton(Messages.getString("Forms.ANDSplit.text"), new forms_and_split());
            andSplitButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_ANDSPLIT, AbstractViewEvent.DRAWMODE_ANDSPLIT, andSplitButton));
            addShortcutToMouseButton("Forms.ANDSplit", andSplitButton, ActionFactory.ACTIONID_DRAWMODE_ANDSPLIT, false);
            setTooltip(andSplitButton, "Forms.ANDSplit", true);
        }

        return andSplitButton;
    }

    private JCommandButton getAndJoinButton() {

        if (andJoinButton == null) {
            andJoinButton = new JCommandButton(Messages.getString("Forms.ANDJoin.text"), new forms_and_join());
            andJoinButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_ANDJOIN, AbstractViewEvent.DRAWMODE_ANDJOIN, andJoinButton));
            addShortcutToMouseButton("Forms.ANDJoin", andJoinButton, ActionFactory.ACTIONID_DRAWMODE_ANDJOIN, true);
            setTooltip(andJoinButton, "Forms.ANDJoin", true);
        }

        return andJoinButton;
    }

    private JCommandButton getXorSplitJoinButton() {
        if (xorSplitJoinButton == null) {
            xorSplitJoinButton = new JCommandButton(Messages.getString("Forms.XORSplitJoin.text"), new forms_xor_split_join());
            xorSplitJoinButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_XORSPLITJOIN, AbstractViewEvent.DRAWMODE_XORSPLITJOIN, xorSplitJoinButton));
            addShortcutToMouseButton("Forms.XORSplitJoin", xorSplitJoinButton, ActionFactory.ACTIONID_DRAWMODE_XORSPLITJOIN, true);
            setTooltip(xorSplitJoinButton, "Forms.XORSplitJoin", true);
        }

        return xorSplitJoinButton;
    }

    private JCommandButton getAndSplitJoinButton() {

        if (andSplitJoinButton == null) {
            andSplitJoinButton = new JCommandButton(Messages.getString("Forms.ANDSplitJoin.text"), new forms_and_split_join());
            andSplitJoinButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_ANDSPLITJOIN, AbstractViewEvent.DRAWMODE_ANDSPLITJOIN, andSplitJoinButton));
            addShortcutToMouseButton("Forms.ANDSplitJoin", andSplitJoinButton, ActionFactory.ACTIONID_DRAWMODE_ANDSPLITJOIN, true);
            setTooltip(andSplitJoinButton, "Forms.ANDSplitJoin", true);
        }

        return andSplitJoinButton;
    }

    private JCommandButton getAndJoinXorSplitButton() {

        if (andJoinXorSplitButton == null) {
            andJoinXorSplitButton = new JCommandButton(Messages.getString("Forms.ANDJoinXORSplit.text"), new forms_and_join_xor_split());
            andJoinXorSplitButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_ANDJOINXORSPLIT, AbstractViewEvent.DRAWMODE_ANDJOIN_XORSPLIT, andJoinXorSplitButton));
            addShortcutToMouseButton("Forms.ANDJoinXORSplit", andJoinXorSplitButton, ActionFactory.ACTIONID_DRAWMODE_ANDJOINXORSPLIT, false);
            setTooltip(andJoinXorSplitButton, "Forms.ANDJoinXORSplit", true);
        }

        return andJoinXorSplitButton;
    }

    private JCommandButton getXorJoinAndSplitButton() {

        if (xorJoinAndSplitButton == null) {
            xorJoinAndSplitButton = new JCommandButton(Messages.getString("Forms.XORJoinANDSplit.text"), new forms_xor_join_and_split());
            xorJoinAndSplitButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_XORJOINANDSPLIT, AbstractViewEvent.DRAWMODE_XORJOIN_ANDSPLIT, xorJoinAndSplitButton));
            addShortcutToMouseButton("Forms.XORJoinANDSplit", xorJoinAndSplitButton, ActionFactory.ACTIONID_DRAWMODE_XORJOINANDSPLIT, false);
            setTooltip(xorJoinAndSplitButton, "Forms.XORJoinANDSplit", true);
        }

        return xorJoinAndSplitButton;
    }

    private JCommandButton getSubprocessButton() {

        if (subprocessButton == null) {
            subprocessButton = new JCommandButton(Messages.getString("Forms.subprocess.text"), new forms_subprocess());
            subprocessButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_DRAWMODE_SUB, AbstractViewEvent.DRAWMODE_SUB, subprocessButton));
            addShortcutToMouseButton("Forms.subprocess", subprocessButton, ActionFactory.ACTIONID_DRAWMODE_SUB, false);
            setTooltip(subprocessButton, "Forms.subprocess", true);
        }

        return subprocessButton;
    }

    public JCommandButton getCoverabilityGraphButton() {

        if (coverabilityGraphButton == null) {
            coverabilityGraphButton = new JCommandMenuButton(Messages.getString("Tools.reachabilityGraph.text"), new analyze_reachability_graph());
            coverabilityGraphButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_REACHGRAPH_START, AbstractViewEvent.REACHGRAPH, coverabilityGraphButton));
            addShortcutToJCommandButton("Tools.reachabilityGraph", coverabilityGraphButton, ActionFactory.ACTIONID_REACHGRAPH_START);
            setTooltip(coverabilityGraphButton, "Tools.reachabilityGraph", true);
        }

        return coverabilityGraphButton;
    }

    private JCommandButton getColoringButton() {

        if (coloringButton == null) {
            coloringButton = new JCommandButton(Messages.getString("Tools.coloring.text"), new analyze_coloring());
            coloringButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_COLORING, AbstractViewEvent.COLORING, coloringButton));
            addShortcutToJCommandButton("Action.Coloring", coloringButton, ActionFactory.ACTIONID_COLORING);
            setTooltip(coloringButton, "Tools.coloring", true);
        }

        return coloringButton;
    }

    private JCommandButton getSemanticalAnalysisButton() {

        if (semanticalAnalysisButton == null) {
            semanticalAnalysisButton = new JCommandButton(Messages.getString("Tools.semanticalAnalysis.text"), new analyze_semanticalanalysis());
            semanticalAnalysisButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SEMANTICAL_ANALYSIS, AbstractViewEvent.ANALYSIS_WOPED, semanticalAnalysisButton));
            addShortcutToJCommandButton("Tools.semanticalAnalysis", semanticalAnalysisButton, ActionFactory.ACTIONID_SEMANTICAL_ANALYSIS);
            setTooltip(semanticalAnalysisButton, "Tools.semanticalAnalysis", true);
        }

        return semanticalAnalysisButton;
    }

    private JCommandButton getCapacityPlanningButton() {

        if (capacityPlanningButton == null) {
            capacityPlanningButton = new JCommandButton(Messages.getString("Tools.capacityPlanning.text"), new analyze_capacityplanning());
            capacityPlanningButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_QUANTCAP, AbstractViewEvent.QUANTCAP, capacityPlanningButton));
            addShortcutToJCommandButton("Tools.capacityPlanning", capacityPlanningButton, ActionFactory.ACTIONID_QUANTCAP);
            setTooltip(capacityPlanningButton, "Tools.capacityPlanning", true);
        }

        return capacityPlanningButton;
    }

    private JCommandButton getQuantitativeSimulationButton() {

        if (quantitativeSimulationButton == null) {
            quantitativeSimulationButton = new JCommandButton(Messages.getString("Tools.quantitativeSimulation.text"), new analyze_quantitative_simulation());
            quantitativeSimulationButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_QUANTSIM, AbstractViewEvent.QUANTSIM, quantitativeSimulationButton));
            addShortcutToJCommandButton("Tools.quantitativeSimulation", quantitativeSimulationButton, ActionFactory.ACTIONID_QUANTSIM);
            setTooltip(quantitativeSimulationButton, "Tools.quantitativeSimulation", true);
        }

        return quantitativeSimulationButton;
    }

    private JCommandButton getProcessMetricsButton() {

        if (processMetricsButton == null) {
            processMetricsButton = new JCommandButton(Messages.getString("Metrics.processmetrics.text"), new analyze_metric());
            processMetricsButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_METRIC, AbstractViewEvent.ANALYSIS_METRIC, processMetricsButton));
            addShortcutToJCommandButton("Metrics.processmetrics", processMetricsButton, ActionFactory.ACTIONID_METRIC);
            setTooltip(processMetricsButton, "Metrics.processmetrics", true);
        }

        return processMetricsButton;
    }

    private JCommandButton getProcessMassAnalyzeButton() {

        if (processMassAnalyzeButton == null) {
            processMassAnalyzeButton = new JCommandButton(Messages.getString("Metrics.processmetricsmassanalysis.text"), new analyze_metric_mass_import());
            processMassAnalyzeButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_MASSMETRICANALYSE, AbstractViewEvent.ANALYSIS_MASSMETRICANALYSE, processMassAnalyzeButton));
            setTooltip(processMassAnalyzeButton, "Metrics.processmetricsmassanalysis");
        }

        return processMassAnalyzeButton;
    }

    private JCommandButton getProcessMetricsBuilderButton() {

        if (processMetricsBuilderButton == null) {
            processMetricsBuilderButton = new JCommandButton(Messages.getString("Metrics.processmetricsbuilder.text"), new analyze_metric_builder());
            processMetricsBuilderButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_METRICSBUILDER, AbstractViewEvent.ANALYSIS_METRICSBUILDER, processMetricsBuilderButton));
            setTooltip(processMetricsBuilderButton, "Metrics.processmetricsbuilder");
        }

        return processMetricsBuilderButton;
    }

    private JCommandButton getP2TButton() {
        if (p2tButton == null) {
            p2tButton = new JCommandButton(Messages.getString("P2T.text"), new process_to_text());
            p2tButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_P2T, AbstractViewEvent.P2T, p2tButton));
//			TODO(optional):
//			addShortcutToJCommandButton("Metrics.processmetrics", processMetricsButton, ActionFactory.ACTIONID_METRIC);
            setTooltip(p2tButton, "P2T");
        }

        return p2tButton;
    }
    
    private JCommandButton getT2PButton() {
		if (t2pButton == null) {
			t2pButton = new JCommandButton(Messages.getString("T2P.text"), new text_to_process()); //T2P
			t2pButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_T2P, AbstractViewEvent.T2P, t2pButton));
//			TODO(optional):
//			addShortcutToJCommandButton("Metrics.processmetrics", processMetricsButton, ActionFactory.ACTIONID_METRIC);
			setTooltip(t2pButton, "T2P");
		}

		return t2pButton;
	}

    private JCommandButton getChangeOrientationButton() {

        if (changeOrientationButton == null) {
            changeOrientationButton = new JCommandButton(Messages.getString("View.changeModellingDirection.text"), new view_change_modelling_direction());
            changeOrientationButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_ROTATEVIEW, AbstractViewEvent.ROTATEVIEW, changeOrientationButton));
            addShortcutToJCommandButton("View.changeModellingDirection", changeOrientationButton, ActionFactory.ACTIONID_ROTATEVIEW);
            setTooltip(changeOrientationButton, "View.changeModellingDirection", true);
        }

        return changeOrientationButton;
    }

    private JCommandButton getOptimizeLayoutButton() {

        if (optimizeLayoutButton == null) {
            optimizeLayoutButton = new JCommandButton(Messages.getString("View.optimizeLayout.text"), new view_optimize_layout());
            optimizeLayoutButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_GRAPHBEAUTIFIER_DEFAULT, AbstractViewEvent.GRAPHBEAUTIFIER, optimizeLayoutButton));
            addShortcutToJCommandButton("View.optimizeLayout", optimizeLayoutButton, ActionFactory.ACTIONID_GRAPHBEAUTIFIER_DEFAULT);
            setTooltip(optimizeLayoutButton, "View.optimizeLayout", true);
        }

        return optimizeLayoutButton;
    }

    private JCommandButton getArrangeButton() {

        if (arrangeButton == null) {
            arrangeButton = new JCommandButton(Messages.getString("WindowPreferences.arrange.text"), new window_arrange());
            arrangeButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_ARRANGE, AbstractViewEvent.ARRANGE, arrangeButton));
            //addShortcutToJCommandButton("Action.Frames.Arrange", arrangeButton, ActionFactory.ACTIONID_ARRANGE);
            setTooltip(arrangeButton, "WindowPreferences.arrange");
        }

        return arrangeButton;
    }

    private JCommandButton getCascadeButton() {

        if (cascadeButton == null) {
            cascadeButton = new JCommandButton(Messages.getString("WindowPreferences.cascade.text"), new window_cascadewindows());
            cascadeButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CASCADE, AbstractViewEvent.CASCADE, cascadeButton));
            //addShortcutToJCommandButton("Action.Frames.Cascade", cascadeButton, ActionFactory.ACTIONID_CASCADE);
            setTooltip(cascadeButton, "WindowPreferences.arrange");
        }

        return cascadeButton;
    }

    private JRibbonComponent getOverviewComponent() {

        if (overviewComponent == null) {
            overviewCheckbox = new JCheckBox(Messages.getString("Sidebar.Overview.Title"));
            overviewComponent = new JRibbonComponent(overviewCheckbox);
            overviewCheckbox.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWOVERVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI, overviewCheckbox));
            overviewCheckbox.setToolTipText(Messages.getString("Sidebar.Overview.Tooltip"));
        }

        return overviewComponent;
    }

    private JRibbonComponent getTreeviewComponent() {

        if (treeviewComponent == null) {
            treeviewCheckbox = new JCheckBox(Messages.getString("Sidebar.Treeview.Title"));
            treeviewComponent = new JRibbonComponent(treeviewCheckbox);
            treeviewCheckbox.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWTREEVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI, treeviewCheckbox));
            treeviewCheckbox.setToolTipText(Messages.getString("Sidebar.Treeview.Tooltip"));
        }

        return treeviewComponent;
    }

    private JCommandButton getConfigurationButton() {

        if (configurationButton == null) {
            configurationButton = new JCommandButton(Messages.getString("OptionsAndHelp.Configuration.text"), new help_configuration());
            configurationButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWCONFIG, AbstractViewEvent.CONFIG, configurationButton));
            setTooltip(configurationButton, "OptionsAndHelp.Configuration");
        }

        return configurationButton;
    }

    private JCommandButton getManualButton() {

        if (manualButton == null) {
            manualButton = new JCommandButton(Messages.getString("OptionsAndHelp.Manual.text"), new help_manual());
            manualButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWHELPINDEX, AbstractViewEvent.HELP, manualButton));
            addShortcutToJCommandButton("Menu.Help.Index", manualButton, ActionFactory.ACTIONID_SHOWHELPINDEX);
            setTooltip(manualButton, "OptionsAndHelp.Manual", true);
        }

        return manualButton;
    }

    private JCommandButton getContentsButton() {

        if (contentsButton == null) {
            contentsButton = new JCommandButton(Messages.getString("OptionsAndHelp.Contents.text"), new help_contents());
            contentsButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWHELPCONTENTS, AbstractViewEvent.HELP_CONTENTS, contentsButton));
            addShortcutToJCommandButton("Menu.Help.Contents", contentsButton, ActionFactory.ACTIONID_SHOWHELPCONTENTS);
            setTooltip(contentsButton, "OptionsAndHelp.Contents", true);
        }

        return contentsButton;
    }

    private JCommandButton getSampleNetsButton() {

        if (sampleNetsButton == null) {
            sampleNetsButton = new JCommandButton(Messages.getString("OptionsAndHelp.SampleNets.text"), new help_smaplenets());
            sampleNetsButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
            sampleNetsButton.setPopupCallback(new PopupPanelCallback() {
                @Override
                public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                    return getSampleMenu();
                }
            });
            setPopupTooltip(sampleNetsButton, "OptionsAndHelp.SampleNets");
        }

        return sampleNetsButton;
    }

    private JCommandButton getReportBugButton() {

        if (reportBugButton == null) {
            reportBugButton = new JCommandButton(Messages.getString("OptionsAndHelp.ReportBug.text"), new help_reportbug());
            reportBugButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWBUGREPORT, AbstractViewEvent.BUGREPORT, reportBugButton));
            setTooltip(reportBugButton, "OptionsAndHelp.ReportBug");
        }

        return reportBugButton;
    }

    private JCommandButton getAboutButton() {

        if (aboutButton == null) {
            aboutButton = new JCommandButton(Messages.getString("OptionsAndHelp.About.text"), new help_about());
            aboutButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SHOWABOUT, AbstractViewEvent.ABOUT, aboutButton));
            setTooltip(aboutButton, "OptionsAndHelp.About");
        }

        return aboutButton;
    }

    private JCommandButton getTokengameStartButton() {

        if (tokengameStartButton == null) {
            tokengameStartButton = new JCommandButton(Messages.getString("Tools.tokengame.text"), new analyze_tokengame());
            tokengameStartButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory
                    .ACTIONID_SELECT, AbstractViewEvent.SELECT_EDITOR, tokengameStartButton));
            tokengameStartButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_OPEN_TOKENGAME, AbstractViewEvent.OPEN_TOKENGAME, tokengameStartButton));

            setTooltip(tokengameStartButton, "Tools.tokengame");
        }

        return tokengameStartButton;
    }

    private JCommandPopupMenu getSampleMenu() {

        if (m_sampleMenu == null) {
            m_sampleMenu = new JCommandPopupMenu();
            try {
                String innerPath = "org/woped/file/samples/";
                String path = this.getClass().getResource("/" + innerPath)
                        .toExternalForm();
                // Jar file access
                if (path.indexOf("jar:file:") != -1) {
                    String fn = path.replaceAll("jar:file:", "");
                    // find jar start
                    int n = fn.indexOf("!");
                    if (n == -1)
                        n = fn.length();
                    // read Jar File Name
                    fn = fn.substring(0, n);
                    // Replace all whitespaces in filename
                    fn = fn.replaceAll("%20", " ");

                    JarFile jf = new JarFile(fn);
                    Enumeration<JarEntry> e = jf.entries();
                    ZipEntry ze;
                    // process entries
                    while (e.hasMoreElements()) {
                        ze = (ZipEntry) e.nextElement();
                        String name;
                        String samplepath;
                        if (ze.getName().indexOf(innerPath) == 0
                                && ze.getName().length() > innerPath.length()) {
                            samplepath = "/" + ze.getName();
                            name = ze.getName().substring(
                                    ze.getName().lastIndexOf("/") + 1);
                            JCommandMenuButton sampleItem = new JCommandMenuButton(
                                    name, new help_smaplenets());
                            sampleItem
                                    .addActionListener(new sampleFileListener(
                                            samplepath));

                            m_sampleMenu.addMenuButton(sampleItem);
                        }
                    }
                    jf.close();
                }
                // Normal dir access
                else {
                    path = this.getClass().getResource("/" + innerPath).toExternalForm().replaceAll("file:", "");
                    File sampleDir = new File(path);
                    if (sampleDir.isDirectory()) {
                        for (int idx = 0; idx < sampleDir.listFiles().length; idx++) {
                            if (!sampleDir.listFiles()[idx].isDirectory()) {
                                JCommandMenuButton sampleItem = new JCommandMenuButton(
                                        sampleDir.listFiles()[idx].getName(),
                                        new help_smaplenets());
                                String name = sampleDir.listFiles()[idx]
                                        .getAbsolutePath();
                                sampleItem
                                        .addActionListener(new sampleFileListener(
                                                name));

                                m_sampleMenu.addMenuButton(sampleItem);
                            }
                        }
                    } else {
                        LoggerManager.error(Constants.GUI_LOGGER,
                                "No sample nets found in directory " + path);
                    }
                }
            } catch (Exception ex) {
                LoggerManager.error(Constants.GUI_LOGGER,
                        "Cannot find sample files");
                ex.printStackTrace();
            }
        }
        return m_sampleMenu;
    }

    private JCommandButton getBackwardButton() {

        if (backwardButton == null) {
            backwardButton = new JCommandButton(Messages.getString("Tokengame.StepBand.BackwardButton.text"), new tokengame_play_seek_backward());
            backwardButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_BACKWARD, AbstractViewEvent.TOKENGAME_BACKWARD, backwardButton));
            addShortcutToJCommandButton("Tokengame.StepBand.BackwardButton", backwardButton, ActionFactory.ACTIONID_TOKENGAME_BACKWARD);
            setTooltip(backwardButton, "Tokengame.StepBand.BackwardButton", true);
        }

        return backwardButton;
    }

    private JCommandButton getJumpIntoSubProcessButton() {

        if (jumpIntoSubProcessButton == null) {
            jumpIntoSubProcessButton = new JCommandButton(Messages.getString("Tokengame.StepBand.JumpIntoSubProcessButton.text"), new tokengame_play_jump_into_subprocess());
            jumpIntoSubProcessButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_JUMPINTO, AbstractViewEvent.TOKENGAME_JUMPINTO, jumpIntoSubProcessButton));
            setTooltip(jumpIntoSubProcessButton, "Tokengame.StepBand.JumpIntoSubProcessButton");
        }

        return jumpIntoSubProcessButton;
    }

    private JCommandButton getForwardButton() {

        if (forwardButton == null) {
            forwardButton = new JCommandButton(Messages.getString("Tokengame.StepBand.ForwardButton.text"), new tokengame_play_seek_forward());
            forwardButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_FORWARD, AbstractViewEvent.TOKENGAME_FORWARD, forwardButton));
            addShortcutToJCommandButton("Tokengame.StepBand.ForwardButton", forwardButton, ActionFactory.ACTIONID_TOKENGAME_FORWARD);
            setTooltip(forwardButton, "Tokengame.StepBand.ForwardButton", true);
        }

        return forwardButton;
    }

    private JCommandButton getPauseButton() {

        if (pauseButton == null) {
            pauseButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.PauseButton.text"), new tokengame_play_pause());
            pauseButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_PAUSE, AbstractViewEvent.TOKENGAME_PAUSE, pauseButton));
            setTooltip(pauseButton, "Tokengame.AutoBand.PauseButton");
        }

        return pauseButton;
    }

    private JCommandButton getStartButton() {

        if (startButton == null) {
            startButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.StartButton.text"), new tokengame_play_start());
            startButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_START, AbstractViewEvent.TOKENGAME_START, startButton));
            setTooltip(startButton, "Tokengame.AutoBand.StartButton");
        }

        return startButton;
    }

    private JCommandButton getStopButton() {

        if (stopButton == null) {
            stopButton = new JCommandButton(Messages.getString("Tokengame.StepBand.StopButton.text"), new tokengame_play_stop());
            stopButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_STOP, AbstractViewEvent.TOKENGAME_STOP, stopButton));
            addShortcutToJCommandButton("Tokengame.StepBand.StopButton", stopButton, ActionFactory.ACTIONID_TOKENGAME_STOP);
            setTooltip(stopButton, "Tokengame.StepBand.StopButton", true);
        }

        return stopButton;
    }

    private JCommandButton getJumpOutOfSubprocessButton() {

        if (jumpOutOfSubprocessButton == null) {
            jumpOutOfSubprocessButton = new JCommandButton(Messages.getString("Tokengame.StepBand.JumpOutOfSubprocessButton.text"), new tokengame_play_jump_out_of_subprocess());
            jumpOutOfSubprocessButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_LEAVE, AbstractViewEvent.TOKENGAME_LEAVE, jumpOutOfSubprocessButton));
            setTooltip(jumpOutOfSubprocessButton, "Tokengame.StepBand.JumpOutOfSubprocessButton");
        }

        return jumpOutOfSubprocessButton;
    }

    private JCommandButton getStepWiseButton() {

        if (stepWiseButton == null) {
            stepWiseButton = new JCommandButton(Messages.getString("Tokengame.StepBand.StepByStepButton.text"), new tokengame_edit_step_by_step());
            stepWiseButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_STEP, AbstractViewEvent.TOKENGAME_STEP, stepWiseButton));
            setTooltip(stepWiseButton, "Tokengame.StepBand.StepByStepButton");
        }

        return stepWiseButton;
    }

    private JCommandButton getAutoPlayButton() {

        if (autoPlayButton == null) {
            autoPlayButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.AutoPlayButton.text"), new tokengame_edit_autoPlay());
            autoPlayButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TOKENGAME_AUTO, AbstractViewEvent.TOKENGAME_AUTO, autoPlayButton));
            setTooltip(autoPlayButton, "Tokengame.AutoBand.AutoPlayButton");
        }

        return autoPlayButton;
    }

    private JCommandButton getTokengameCloseButton() {

        if (tokengameCloseButton == null) {
            tokengameCloseButton = new JCommandButton(Messages.getString("Tokengame.CloseBand.CloseButton.text"), new tokengame_tokengame_exit());
            tokengameCloseButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CLOSE_TOKENGAME, AbstractViewEvent.CLOSE_TOKENGAME, tokengameCloseButton));
            setTooltip(tokengameCloseButton, "Tokengame.CloseBand.CloseButton");
        }

        return tokengameCloseButton;
    }

    private JCommandButton getFacebookButton() {

        if (facebookButton == null) {
            facebookButton = new JCommandButton(Messages.getString("Community.Facebook.text"), new F_icon());
            facebookButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_FACEBOOK, AbstractViewEvent.FACEBOOK, facebookButton));
            setTooltip(facebookButton, "Community.Facebook");
        }

        return facebookButton;
    }

/*    private JCommandButton getGoogleplusButton() {

        if (googleplusButton == null) {
            googleplusButton = new JCommandButton(Messages.getString("Community.Googleplus.text"), new Google_plus_icon());
            googleplusButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_GOOGLEPLUS, AbstractViewEvent.GOOGLEPLUS, googleplusButton));
            setTooltip(googleplusButton, "Community.Googleplus");
        }

        return googleplusButton;
    }*/

    private JCommandButton getTwitterButton() {

        if (twitterButton == null) {
            twitterButton = new JCommandButton(Messages.getString("Community.Twitter.text"), new Twitter_icon());
            twitterButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_TWITTER, AbstractViewEvent.TWITTER, twitterButton));
            setTooltip(twitterButton, "Community.Twitter");
        }

        return twitterButton;
    }

    private JCommandButton getSignUpButton() {

        if (signUpButton == null) {
            signUpButton = new JCommandButton(Messages.getString("Community.Register.text"), new register());
            signUpButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_REGISTER, AbstractViewEvent.REGISTER, signUpButton));
            setTooltip(signUpButton, "Community.Register");
        }

        return signUpButton;
    }

    private JCommandButton getCommunityButton() {

        if (communityButton == null) {
            communityButton = new JCommandButton(Messages.getString("Community.Community.text"), new woped_community());
            communityButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_COMMUNITY, AbstractViewEvent.COMMUNITY, communityButton));
            setTooltip(communityButton, "Community.Community");
        }

        return communityButton;
    }

    /**
     * Adds the shortcut to a button.
     *
     * @param propertiesPrefixForShortcuts the properties prefix for shortcuts
     * @param button                       the button
     * @param action_id                    the action_id
     * @param ignoreDefaultKeyMask         the ignore default key mask
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void addShortcutToJCommandButton(final String propertiesPrefixForShortcuts, final JCommandButton button, String action_id, final Boolean ignoreDefaultKeyMask) {
        if (!ignoreDefaultKeyMask) {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts) | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())), action_id);
        } else {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), Messages.getShortcutModifier(propertiesPrefixForShortcuts)), action_id);
        }
        if (!Platform.isMac()) {
            GlobalShortcutEventDispatcher.addShortcutButtonListener(button, propertiesPrefixForShortcuts, ignoreDefaultKeyMask);
        }
    }

    /**
     * Adds the shortcut to a button.
     *
     * @param propertiesPrefixForShortcuts the properties prefix for shortcuts
     * @param button                       the button
     * @param action_id                    the action_id
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void addShortcutToJCommandButton(String propertiesPrefixForShortcuts, JCommandButton button, String action_id) {
        addShortcutToJCommandButton(propertiesPrefixForShortcuts, button, action_id, false);
    }

    /**
     * Adds the shortcut to a button - requires mouseclick.
     * Action will be triggered by click
     * required for Petri-Net-Elements - DrawingMode
     *
     * @param propertiesPrefixForShortcuts the properties prefix for shortcuts
     * @param button                       the button
     * @param action_id                    the action_id
     * @param ignoreDefaultKeyMask         the ignore default key mask
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private void addShortcutToMouseButton(final String propertiesPrefixForShortcuts, final JCommandButton button, String action_id, final Boolean ignoreDefaultKeyMask) {
        if (!ignoreDefaultKeyMask) {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts) | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())), "shortcutAction");
        } else {
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), Messages.getShortcutModifier(propertiesPrefixForShortcuts)), "shortcutAction");
        }
        GlobalShortcutEventDispatcher.addShortcutClickListener(button, propertiesPrefixForShortcuts, ignoreDefaultKeyMask);
    }

    public void fireViewEvent(AbstractViewEvent viewevent) {
        this.m_mediator.fireViewEvent(viewevent);
    }

    /**
     * Gets the mediator for firing events with shortcuts
     *
     * @return the mediator
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    public AbstractApplicationMediator getMediator() {
        return this.m_mediator;
    }

    public void setMediator(AbstractApplicationMediator mediator) {
        m_mediator = mediator;
    }

    @Override
    public void arrangeFrames() {
        // TODO Auto-generated method stub
    }

    @Override
    public void cascadeFrames() {
        // TODO Auto-generated method stub
    }

    @Override
    public List<IEditor> getAllEditors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Component getComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IEditor getEditorFocus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Component getPropertyChangeSupportBean() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void hideEditor(IEditor editor) {
        // TODO Auto-generated method stub
    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub
    }

    @Override
    public void refreshFocusOnFrames() {
        // TODO Auto-generated method stub
    }

    @Override
    public void addEditor(IEditor editor) {
        getRibbon().setSelectedTask(getEditTask());
    }
    
    @Override
    public void addTextEditor(IEditor editor) {
       // getRibbon().setSelectedTask(getEditTask());
    }
    
    
    

    @Override
    public void removeEditor(IEditor editor) {
        if (getAllEditors().size() == 0) {
            getRibbon().setSelectedTask(getFileTask());
        }
    }

    @Override
    public void renameEditor(IEditor editor) {
        // TODO Auto-generated method stub
    }

    @Override
    public void selectEditor(IEditor editor) {

    }

    @Override
    public boolean isMaximized() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateRecentMenu() {
        // TODO Auto-generated method stub
    }

    class recentFileListener implements ActionListener {

        private int numEditors = m_mediator.getUi().getAllEditors().size();
        private String path;

        public recentFileListener(String path) {
            this.path = path;
        }

        public void actionPerformed(ActionEvent e) {

            File f = new File(path);
            fireViewEvent(new ViewEvent(m_mediator.getUi(), AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, f));
            if (m_mediator.getUi().getAllEditors().size() > numEditors)
                getRibbon().setSelectedTask(getEditTask());
        }
    }

    class sampleFileListener implements ActionListener {

        private String path;

        public sampleFileListener(String path) {
            this.path = path;
        }

        public void actionPerformed(ActionEvent e) {
            File f = new File(path);
            fireViewEvent(new ViewEvent(m_mediator.getUi(), AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN_SAMPLE, f));
            getRibbon().setSelectedTask(getEditTask());
        }
    }

    private class MenuViewEventProcessor extends AbstractEventProcessor {

        /**
         * Constructor for the <code>AbstractVEP</code> stores the
         * <code>AtelosApplicationMediator</code>.
         *
         * @param mediator
         */
        public MenuViewEventProcessor(AbstractApplicationMediator mediator) {
            super(mediator);
        }

        @Override
        public void processViewEvent(AbstractViewEvent event) {

            switch (event.getOrder()) {
                case AbstractViewEvent.OPEN_TOKENGAME:
                    // Always show on opening
                    setTokenGameTaskVisibility(true);
                    break;
                case AbstractViewEvent.CLOSE_TOKENGAME:
                    // Always hide on closing
                    setTokenGameTaskVisibility(false);
                    break;
                case AbstractViewEvent.SELECT_EDITOR:
                    // Check state when editor window is selected
                    checkTokenGameTaskVisibility(event.getSource());
                    break;
                case CoverabilityGraphViewEvents.FRAME_ACTIVATED:
                    setCoverabilityGraphTaskVisible(true);
                    break;
                case CoverabilityGraphViewEvents.FRAME_DEACTIVATED:
                    setCoverabilityGraphTaskVisible(false);
                    break;
            }
        }

        private void checkTokenGameTaskVisibility(Object sender) {
            if (!(sender instanceof IEditor)) return;

            IEditor editor = (IEditor) sender;
            boolean enable = editor.isTokenGameEnabled();

            setTokenGameTaskVisibility(enable);
        }

        private void setTokenGameTaskVisibility(boolean visible) {
            if (visible) {
                getRibbon().setVisible(getTokengameGroup(), true);
                getRibbon().setSelectedTask(getTokengameTask());
            } else {
                if (getRibbon().isVisible(getTokengameGroup())) {
                    getRibbon().setVisible(getTokengameGroup(), false);
                    getRibbon().setSelectedTask(getAnalyzeTask());
                }
            }
        }

        private void setCoverabilityGraphTaskVisible(boolean visible) {
            getRibbon().setVisible(coverabilityGraphExtension.getContextGroup(), visible);

            if (visible) {
                getRibbon().setSelectedTask(coverabilityGraphExtension.getDefaultTask());
            }
        }
    }
}
