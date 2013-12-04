package org.woped.starter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

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
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.qualanalysis.IReachabilityGraph;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.gui.images.svg.*;
import org.woped.gui.translations.Messages;
import org.woped.starter.osxMenu.OSXFullscreen;
import org.woped.starter.osxMenu.OSXMenu;
import org.woped.starter.osxMenu.OSXMenuAdapter;
import org.woped.starter.osxMenu.OSXMenuItem;
import org.woped.config.general.WoPeDRecentFile;

public class MainFrame extends JRibbonFrame implements IUserInterface {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RibbonContextualTaskGroup		tokengameGroup				= null;
	
	private JCommandButton 					taskbarButtonNew			= null;
	private JCommandButton 					taskbarButtonSave			= null;
	private JCommandButton 					taskbarButtonClose			= null;
	private JCommandButton 					taskbarButtonPaste			= null;
	private JCommandButton 					taskbarButtonCopy			= null;
	private JCommandButton 					taskbarButtonUndo			= null;
	private JCommandButton 					taskbarButtonRedo			= null;
	private JCommandButton 					taskbarButtonTokengame		= null;
	private JCommandButton 					taskbarButtonAnalyze		= null;
	private JCommandButton 					taskbarButtonConfig			= null;
	
	
	private	JRibbonBand 					saveBand					= null;
	private	JRibbonBand 					documentBand				= null;
	private	JRibbonBand 					outputBand					= null;
	private	JRibbonBand 					apromoreBand				= null;
	private	JRibbonBand 					editBand					= null;
	private	JRibbonBand 					formsBand					= null;
	private	JRibbonBand 					layoutBand					= null;
	private	JRibbonBand 					windowsBand					= null;
	private	JRibbonBand 					sidebarBand					= null;
	private	JRibbonBand 					analyzeBand					= null;
	private	JRibbonBand 					metricsBand					= null;
	private JRibbonBand 					optionsAndHelpBand			= null;
	private	JRibbonBand 					tokengameCloseBand			= null;
	private	JRibbonBand 					tokengameStepBand			= null;
	private	JRibbonBand 					tokengameAutoBand			= null;
	private JRibbonBand						registrationBand			= null;
	private JRibbonBand						socialMediaBand				= null;
	
	private RibbonTask						fileTask					= null;
	private RibbonTask						editTask					= null;
	private RibbonTask						viewTask					= null;
	private RibbonTask						analyzeTask					= null;
	private RibbonTask						optionsHelpTask				= null;
	private RibbonTask						tokengameTask				= null;
	private RibbonTask						communityTask				= null;

	private	JCommandButton 					newButton					= null; 
	private	JCommandButton 					openButton					= null; 
	private	JCommandButton 					recentButton				= null; 
	private	JCommandButton 					closeButton					= null; 
	
	private	JCommandButton 					saveButton					= null; 
	private	JCommandButton 					saveAsButton				= null; 
	
	private	JCommandButton 					printButton					= null; 
	private	JCommandButton 					exportAsButton				= null; 

	private	JCommandButton 					importApromoreButton		= null; 
	private	JCommandButton 					exportApromoreButton		= null; 
	
	private	JCommandButton 					undoButton					= null; 
	private	JCommandButton 					redoButton					= null; 
	private	JCommandButton 					cutButton					= null; 
	private	JCommandButton 					copyButton					= null; 
	private	JCommandButton 					pasteButton					= null; 
	private	JCommandButton 					groupButton					= null; 
	private	JCommandButton 					ungroupButton				= null; 
	
	private	JCommandButton 					placeButton					= null; 
	private	JCommandButton 					transitionButton			= null; 
	private	JCommandButton 					xorSplitButton				= null; 
	private	JCommandButton 					xorJoinButton				= null; 
	private	JCommandButton 					andSplitButton				= null; 
	private	JCommandButton 					andJoinButton				= null; 
	private	JCommandButton 					xorSplitJoinButton			= null; 
	private	JCommandButton 					andJoinXorSplitButton		= null; 
	private	JCommandButton 					xorJoinAndSplitButton		= null; 
	private	JCommandButton 					andSplitJoinButton			= null; 
	private	JCommandButton 					subprocessButton			= null; 
	
	private	JCommandButton 					changeOrientationButton 	= null;
	private	JCommandButton 					optimizeLayoutButton 		= null;

	private	JCommandButton 					arrangeButton 				= null;
	private	JCommandButton 					cascadeButton 				= null;
	
	private JRibbonComponent				overviewComponent			= null;
	private JRibbonComponent				treeviewComponent			= null;
	private JCheckBox						overviewCheckbox			= null;
	private JCheckBox						treeviewCheckbox			= null;

	private	JCommandButton 					tokengameStartButton		= null;
	private	JCommandButton 					coverabilityGraphButton 	= null;
	private	JCommandButton 					coloringButton 				= null;
	private	JCommandButton 					semanticalAnalysisButton 	= null;
	private	JCommandButton 					capacityPlanningButton 		= null;
	private	JCommandButton 					quantitativeSimulationButton= null;

	private	JCommandButton 					processMetricsButton 		= null;
	private	JCommandButton 					processMassAnalyzeButton 	= null;
	private	JCommandButton 					processMetricsBuilderButton = null;

	private	JCommandButton 					configurationButton 		= null;
	private	JCommandButton 					manualButton 				= null;
	private	JCommandButton 					contentsButton 				= null;
	private	JCommandButton 					sampleNetsButton 			= null;
	private	JCommandButton 					reportBugButton 			= null;
	private	JCommandButton 					aboutButton 				= null;

	private	JCommandButton 					tokengameCloseButton 		= null;

	private	JCommandButton 					stepWiseButton 				= null;
	private	JCommandButton 					backwardButton 				= null;
	private	JCommandButton 					forwardButton 				= null;
	private	JCommandButton 					stopButton 					= null;
	private	JCommandButton 					jumpOutOfSubprocessButton 	= null;
	private	JCommandButton 					jumpIntoSubProcessButton 	= null;

	private	JCommandButton 					autoPlayButton 				= null;
	private JCommandButton					startButton					= null;
	private	JCommandButton 					pauseButton 				= null;
	
	private JCommandButton					facebookButton				= null;
	private JCommandButton					googleplusButton			= null;
	private JCommandButton					twitterButton				= null;
	private JCommandButton					signUpButton				= null;
	private JCommandButton					communityButton				= null;
		
	private JCommandPopupMenu              	m_sampleMenu             	= null;
    private JCommandPopupMenu			   	m_recentMenu				= null;
    
    private AbstractApplicationMediator	   	m_mediator 					= null;   

     	
     // ActionListener for Ribbon Components
    class ActionButtonListener implements ActionListener {
		
		private int			event_id;
		private String      action_id;
		private WoPeDAction	action;
				
		public ActionButtonListener(AbstractApplicationMediator mediator, String action_id, int event_id, JComponent target) {
			
			action = ActionFactory.getStaticAction(action_id);
			ActionFactory.addTarget(mediator, action_id, target);
			this.event_id = event_id;
			this.action_id = action_id;
			target.setName(action_id);
		}
		
		public void actionPerformed(ActionEvent e) {	
			
			action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, event_id));
			if (action_id.equals(ActionFactory.ACTIONID_CLOSE_TOKENGAME)) {
				getRibbon().setVisible(getTokengameGroup(), false);
				getRibbon().setSelectedTask(getAnalyzeTask());
			}
		}
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
	
   	
	public void initialize(AbstractApplicationMediator mediator) {
			
		setMediator(mediator);
		
		 /**
         * Instantiates a new MacOS X MenuBar.
         *
         * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
         */
		if (Platform.isMac()) {
	        // Get the frame
		      OSXMenuAdapter menuAdapter = new OSXMenuAdapter((JFrame) SwingUtilities.getRoot((Component) this));
		      //new File Menu
		      OSXMenu osxFileMenu = new OSXMenu(Messages.getTitle("Menu.File"));
		      
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.NewEditor"), KeyEvent.VK_N).addAction(mediator, ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW);  
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.OpenEditor"), KeyEvent.VK_O).addAction(mediator, ActionFactory.ACTIONID_OPEN, AbstractViewEvent.OPEN);  
		      //recent files
		      //JMenu recentFiles = osxMenu.addSubMenu(Messages.getTitle("Menu.File.RecentMenu"))
				JMenu recentFiles = new JMenu(Messages.getTitle("Menu.File.RecentMenu"));
				Vector<?> v = ConfigurationManager.getConfiguration().getRecentFiles();
				if (v.size() != 0) {
					for (int idx = 0; idx < v.size(); idx++) {
						String name = ((WoPeDRecentFile) v.get(idx)).getName();
						String path = ((WoPeDRecentFile) v.get(idx)).getPath();
						OSXMenuItem recentFile = new OSXMenuItem(name);
						recentFile.addActionListener(new recentFileListener(path));
						recentFiles.add(recentFile);
					}
				} else {
					OSXMenuItem emptyItem = new OSXMenuItem(Messages.getString("Menu.File.RecentMenu.empty"));
					emptyItem.setEnabled(false);
					recentFiles.add(emptyItem);
				}
				osxFileMenu.add(recentFiles);

		      //osxMenu.addMenuItem("Close").addAction(mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE);
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.CloseEditor"), KeyEvent.VK_W).addAction(mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE);  
		      osxFileMenu.addSeparator();
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.SaveEditor"), KeyEvent.VK_S).addAction(mediator, ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE);  
		      osxFileMenu.addMenuItem(Messages.getTitle("Action.EditorSaveAs")).addAction(mediator, ActionFactory.ACTIONID_SAVEAS, AbstractViewEvent.SAVEAS); 

		      osxFileMenu.addSeparator();
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.PrintEditor"), KeyEvent.VK_P).addAction(mediator, ActionFactory.ACTIONID_PRINT, AbstractViewEvent.PRINT);  
		      osxFileMenu.addMenuItemWithShortcut(Messages.getTitle("Action.Export"), KeyEvent.VK_E).addAction(mediator, ActionFactory.ACTIONID_EXPORT, AbstractViewEvent.EXPORT);  

		      menuAdapter.addMenu(osxFileMenu);
		     
		      OSXMenu osxEditMenu = new OSXMenu(Messages.getTitle("Menu.Edit"));
		      
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.Undo"), KeyEvent.VK_Z).addAction(mediator, ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO);  
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.Redo"), KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_DOWN_MASK).addAction(mediator, ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO);  
		      //osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.Redo"), Messages.getShortcutKey("Action.Redo"), Messages.getShortcutModifier("Action.Redo")).addAction(mediator, ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO);  
		      
		      osxEditMenu.addSeparator();
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.CutSelection"), KeyEvent.VK_X).addAction(mediator, ActionFactory.ACTIONID_CUT, AbstractViewEvent.CUT);  
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.CopySelection"), KeyEvent.VK_C).addAction(mediator, ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY);  
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.PasteElements"), KeyEvent.VK_V).addAction(mediator, ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE);   
		      osxEditMenu.addSeparator();
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.GroupSelection"), KeyEvent.VK_G).addAction(mediator, ActionFactory.ACTIONID_GROUP, AbstractViewEvent.GROUP);  
		      osxEditMenu.addMenuItemWithShortcut(Messages.getTitle("Action.UngroupSelection"), KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_DOWN_MASK).addAction(mediator, ActionFactory.ACTIONID_UNGROUP, AbstractViewEvent.UNGROUP);  
		      
		      menuAdapter.addMenu(osxEditMenu);
		      
		      OSXMenu osxViewMenu = new OSXMenu(Messages.getTitle("Menu.View"));
		      osxViewMenu.addMenuItem(Messages.getString("View.changeModellingDirection.text"));
		      osxViewMenu.addMenuItem(Messages.getString("View.optimizeLayout.text"));
		      osxViewMenu.addSeparator();
		      osxViewMenu.addMenuItem(Messages.getTitle("Action.Frames.Cascade"));
		      osxViewMenu.addMenuItem(Messages.getTitle("Action.Frames.Arrange"));
		      osxViewMenu.addSeparator();
		      final Window currentWindow = (Window) SwingUtilities.getRoot((Component) this);
		      OSXFullscreen.enableOSXFullscreen(currentWindow);
		     
		      osxViewMenu.addMenuItemWithShortcut("Toggle Fullscreen",KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK).addActionListener(new ActionListener() {
		    	  @Override  
		    	  public void actionPerformed(ActionEvent evt) {
		    		  OSXFullscreen.toggleOSXFullscreen(currentWindow);
		            }
		        });
		      
		      menuAdapter.addMenu(osxViewMenu);
		      
		      OSXMenu osxHelpMenu = new OSXMenu(Messages.getTitle("Menu.Help"));
		      
		      osxHelpMenu.addMenuItem(Messages.getTitle("Menu.Help.Index"));
		      osxHelpMenu.addMenuItem(Messages.getTitle("Menu.Help.Contents"));
		      osxHelpMenu.addMenuItem(Messages.getString("OptionsAndHelp.ReportBug.text"));
		      osxHelpMenu.addMenuItem("TEST").setEnabled(false);
		      osxHelpMenu.addMenuItem("TEST2").setVisible(false);
		      
		      menuAdapter.addMenu(osxHelpMenu);

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

		VisualController.getInstance().propertyChange(new PropertyChangeEvent(mediator, "InternalFrameCount", null, null));
	}

	
	private void setTooltip(JCommandButton button, String prefix) {
		button.setActionRichTooltip(new RichTooltip(Messages.getString(prefix + ".text"), Messages.getString(prefix + ".tooltip")));
	}

	
	private void setPopupTooltip(JCommandButton button, String prefix) {
		button.setPopupRichTooltip(new RichTooltip(Messages.getString(prefix + ".text"), Messages.getString(prefix + ".tooltip")));
	}

	
	/*************/
	/* TASKGROUP */
	/*************/
	private RibbonContextualTaskGroup getTokengameGroup() {
		
		if (tokengameGroup == null) {
			tokengameGroup = new RibbonContextualTaskGroup("", Color.green, getTokengameTask());
		}
		
		return tokengameGroup;
	}

	/***********/
	/* TASKBAR */
	/***********/
	private JCommandButton getTaskbarButtonNew() {
		
		if (taskbarButtonNew == null) {
			taskbarButtonNew = new JCommandButton("",new document_new());
			setTooltip(taskbarButtonNew, "Document.new");
			taskbarButtonNew.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW, taskbarButtonNew));
		}
		return taskbarButtonNew;
	}

	private JCommandButton getTaskbarButtonSave() {
		
		if (taskbarButtonSave == null) {
			taskbarButtonSave = new JCommandButton("",new document_save());
			setTooltip(taskbarButtonSave, "Save.save");
			taskbarButtonSave.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE, taskbarButtonSave));
		}
		return taskbarButtonSave;
	}
	
	private JCommandButton getTaskbarButtonClose() {
		
		if (taskbarButtonClose == null) {
			taskbarButtonClose = new JCommandButton("",new file_close());
			setTooltip(taskbarButtonClose, "Document.close");
			taskbarButtonClose.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE, taskbarButtonClose));
		}
		return taskbarButtonClose;
	}
	
	private JCommandButton getTaskbarButtonPaste() {
		
		if (taskbarButtonPaste == null) {
			taskbarButtonPaste = new JCommandButton("",new edit_paste());
			setTooltip(taskbarButtonPaste, "Edit.paste");
			taskbarButtonPaste.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE, taskbarButtonPaste));
		}
		return taskbarButtonPaste;
	}
	
	private JCommandButton getTaskbarButtonCopy() {
		
		if (taskbarButtonCopy == null) {
			taskbarButtonCopy = new JCommandButton("",new edit_copy());
			setTooltip(taskbarButtonCopy, "Edit.copy");
			taskbarButtonCopy.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY, taskbarButtonCopy));	
		}
		return taskbarButtonCopy;
	}
	
	private JCommandButton getTaskbarButtonUndo() {
		
		if (taskbarButtonUndo == null) {
			taskbarButtonUndo = new JCommandButton("",new editor_undo());
			setTooltip(taskbarButtonUndo, "Edit.undo");
			taskbarButtonUndo.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO, taskbarButtonUndo));
		}
		return taskbarButtonUndo;
	}

	private JCommandButton getTaskbarButtonRedo() {
		
		if (taskbarButtonRedo == null) {
			taskbarButtonRedo = new JCommandButton("",new editor_redo());
			setTooltip(taskbarButtonRedo, "Edit.redo");
			taskbarButtonRedo.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO, taskbarButtonRedo));
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
			taskbarButtonAnalyze.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_WOPED, AbstractViewEvent.ANALYSIS_WOPED, taskbarButtonAnalyze));
		}
		return taskbarButtonAnalyze;
	}

	private JCommandButton getTaskbarButtonConfig() {
		
		if (taskbarButtonConfig == null) {
			taskbarButtonConfig = new JCommandButton(Messages.getString("OptionsAndHelp.Configuration.text"), new help_configuration());
			setTooltip(taskbarButtonConfig, "OptionsAndHelp.Configuration");
			taskbarButtonConfig.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWCONFIG, AbstractViewEvent.CONFIG, taskbarButtonConfig));		
		}
		return taskbarButtonConfig;
	}		
	/*********/
	/* TASKS */
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
			if (ConfigurationManager.getConfiguration().isUseMetrics())
				analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand(), getMetricsBand());	
			else
				analyzeTask = new RibbonTask(Messages.getTitle("Task.Analyze"), getAnalyzeBand());					
			analyzeTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(analyzeTask));
		}
		
		return analyzeTask;
	}
	
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
		
		if(communityTask == null){
			communityTask = new RibbonTask(Messages.getTitle("Task.Community"), getSocialMediaBand(), getRegistrationBand());	
			communityTask.setResizeSequencingPolicy(new CoreRibbonResizeSequencingPolicies.CollapseFromLast(communityTask));
			
		}
		return communityTask;
	}
	
	/*********/
	/* BANDS */
	/*********/
	private JRibbonBand getDocumentBand () {
		
		if (documentBand == null) {	
			documentBand = new JRibbonBand(Messages.getString("Document.textBandTitle"), null);
			documentBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(documentBand));
					
			documentBand.addCommandButton(getNewButton(),  RibbonElementPriority.TOP);
			documentBand.addCommandButton(getOpenButton(),  RibbonElementPriority.TOP);
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
	
	private JRibbonBand getApromoreBand() {
		
		if (apromoreBand == null) {
			apromoreBand = new JRibbonBand(Messages.getString("Apromore.textBandTitle"), null);
			apromoreBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(apromoreBand));
			apromoreBand.addCommandButton(getImportApromoreButton(),  RibbonElementPriority.TOP);
			apromoreBand.addCommandButton(getExportApromoreButton(),  RibbonElementPriority.TOP);
		}
		
		return apromoreBand;
	}
			
	private JRibbonBand getEditBand() {
		
		if (editBand == null) {
			editBand = new JRibbonBand(Messages.getString("Edit.textBandTitle"), null);
			editBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(editBand));
			editBand.addCommandButton(getUndoButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getRedoButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getCutButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getCopyButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getPasteButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getGroupButton(),  RibbonElementPriority.TOP);
			editBand.addCommandButton(getUngroupButton(),  RibbonElementPriority.TOP);
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

			layoutBand.addCommandButton(getChangeOrientationButton(),RibbonElementPriority.TOP);
			layoutBand.addCommandButton(getOptimizeLayoutButton(),RibbonElementPriority.TOP);
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
			analyzeBand.addCommandButton(getQuantitativeSimulationButton(),RibbonElementPriority.TOP);
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
	
	private JRibbonBand getWindowsBand (){
			
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
			sidebarBand = new JRibbonBand(Messages.getString("ShowHide.textBandTitle"),new format_justify_left(), null);
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
	
	private JRibbonBand getTokengameCloseBand(){
		
		if (tokengameCloseBand == null) {	
			tokengameCloseBand = new JRibbonBand(Messages.getString("Tokengame.CloseBand.title"),new tokengame_play_start());
			tokengameCloseBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(tokengameCloseBand));
			tokengameCloseBand.addCommandButton(getTokengameCloseButton(), RibbonElementPriority.TOP);		
		}
		
		return tokengameCloseBand;
	}

	private JRibbonBand getTokengameStepBand(){
		
		if (tokengameStepBand == null) {	
			tokengameStepBand = new JRibbonBand(Messages.getString("Tokengame.StepBand.title"),new tokengame_play_start());
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

	private JRibbonBand getTokengameAutoBand(){
		
		if (tokengameAutoBand == null) {	
			tokengameAutoBand = new JRibbonBand(Messages.getString("Tokengame.AutoBand.title"),new tokengame_edit_step_by_step());
			tokengameAutoBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(tokengameAutoBand));
		
			tokengameAutoBand.addCommandButton(getAutoPlayButton(), RibbonElementPriority.TOP);
			tokengameAutoBand.addCommandButton(getStartButton(), RibbonElementPriority.TOP);
			tokengameAutoBand.addCommandButton(getPauseButton(), RibbonElementPriority.TOP);
		}	
		
		return tokengameAutoBand;
	}	
	private JRibbonBand getSocialMediaBand(){
		
		if(socialMediaBand == null){
			socialMediaBand = new JRibbonBand(Messages.getString("Community.socialmediaBandTitle"), null);
			socialMediaBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(socialMediaBand));			
			socialMediaBand.addCommandButton(getFacebookButton(), RibbonElementPriority.TOP);
			socialMediaBand.addCommandButton(getGoogleplusButton(), RibbonElementPriority.TOP);
			socialMediaBand.addCommandButton(getTwitterButton(), RibbonElementPriority.TOP);
		
			
		}
		return socialMediaBand;
	}
	
	private JRibbonBand getRegistrationBand(){
		
		if(registrationBand == null){
			registrationBand = new JRibbonBand(Messages.getString("Community.registerBandTitle"), null);
			registrationBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesNone(registrationBand));			
			registrationBand.addCommandButton(getSignUpButton(), RibbonElementPriority.TOP);
			registrationBand.addCommandButton(getCommunityButton(), RibbonElementPriority.TOP);
			
		}
		return registrationBand;
	}
	/***********/
	/* BUTTONS */
	/***********/
	private JCommandButton getNewButton() {
		
		if (newButton == null) {
			newButton = new JCommandButton(Messages.getString("Document.new.text"), new file_new());
			newButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_NEW, AbstractViewEvent.NEW, newButton));
			setTooltip(newButton, "Document.new");
		}
		
		return newButton;
	}

	private JCommandButton getOpenButton() {
		
		if (openButton == null) {
			openButton = new JCommandButton(Messages.getString("Document.open.text"), new file_open());
			openButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_OPEN, AbstractViewEvent.OPEN, openButton));			
			setTooltip(openButton, "Document.open");
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
			closeButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_CLOSE, AbstractViewEvent.CLOSE, closeButton));			
			setTooltip(closeButton, "Document.close");
		}
		
		return closeButton;
	}
	
	private JCommandButton getSaveButton() {
		
		if (saveButton == null) {
			saveButton = new JCommandButton(Messages.getString("Save.save.text"), new file_save());
			saveButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SAVE, AbstractViewEvent.SAVE, saveButton));			
			setTooltip(saveButton, "Save.save");
		}
		
		return saveButton;
	}
	
	private JCommandButton getSaveAsButton() {
		
		if (saveAsButton == null) {
			saveAsButton = new JCommandButton(Messages.getString("Save.saveAs.text"), new file_saveas());
			saveAsButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SAVEAS, AbstractViewEvent.SAVEAS, saveAsButton));			
			setTooltip(saveAsButton, "Save.saveAs");
		}
		
		return saveAsButton;
	}
	
	private JCommandButton getPrintButton() {
		
		if (printButton == null) {
			printButton = new JCommandButton(Messages.getString("DataOutput.print.text"), new file_print());
			printButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_PRINT, AbstractViewEvent.PRINT, printButton));			
			setTooltip(printButton, "DataOutput.print");
		}
		
		return printButton;
	}
		
	private JCommandButton getExportButton() {
		
		if (exportAsButton == null) {
			exportAsButton = new JCommandButton(Messages.getString("DataOutput.exportAs.text"), new file_exportas());
			exportAsButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_EXPORT, AbstractViewEvent.EXPORT, exportAsButton));			
			setTooltip(exportAsButton, "DataOutput.exportAs");
		}
		
		return exportAsButton;
	}
		
	private JCommandButton getImportApromoreButton() {
		
		if (importApromoreButton == null) {		
			importApromoreButton = new JCommandButton(Messages.getString("Apromore.aproImport.text"), new apromore_import());
			importApromoreButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_IMPORTAPRO, AbstractViewEvent.IMPORTAPRO, importApromoreButton));
			setTooltip(importApromoreButton, "Apromore.aproImport");
		}
		
		return importApromoreButton;
	}
				
	private JCommandButton getExportApromoreButton() {
		
		if (exportApromoreButton == null) {		
			exportApromoreButton = new JCommandButton(Messages.getString("Apromore.aproExport.text"), new apromore_export());
			exportApromoreButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_EXPORTAPRO, AbstractViewEvent.EXPORTAPRO, exportApromoreButton));
			setTooltip(exportApromoreButton, "Apromore.aproExport");
		}
		
		return exportApromoreButton;
	}
			

	private JCommandButton getUndoButton() {
		
		if (undoButton == null) {
			undoButton = new JCommandButton(Messages.getString("Edit.undo.text"), new editor_undo());
			undoButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_UNDO, AbstractViewEvent.UNDO, undoButton));			
			setTooltip(undoButton, "Edit.undo");
		}
		
		return undoButton;
	}

	private JCommandButton getRedoButton() {
		
		if (redoButton == null) {
			redoButton = new JCommandButton(Messages.getString("Edit.redo.text"), new editor_redo());
			redoButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_REDO, AbstractViewEvent.REDO, redoButton));			
			setTooltip(redoButton, "Edit.redo");
		}
		
		return redoButton;
	}

	private JCommandButton getCutButton() {
		
		if (cutButton == null) {
			cutButton = new JCommandButton(Messages.getString("Edit.cut.text"), new editor_cut());
			cutButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_CUT, AbstractViewEvent.CUT, cutButton));			
			setTooltip(cutButton, "Edit.cut");
		}
		
		return cutButton;
	}

	private JCommandButton getCopyButton() {
		
		if (copyButton == null) {
			copyButton = new JCommandButton(Messages.getString("Edit.copy.text"), new editor_copy());
			copyButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_COPY, AbstractViewEvent.COPY, copyButton));			
			setTooltip(copyButton, "Edit.copy");
		}
		
		return copyButton;
	}

	private JCommandButton getPasteButton() {
		
		if (pasteButton == null) {
			pasteButton = new JCommandButton(Messages.getString("Edit.paste.text"), new editor_paste());
			pasteButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_PASTE, AbstractViewEvent.PASTE, pasteButton));			
			setTooltip(pasteButton, "Edit.paste");
		}
		
		return pasteButton;
	}

	private JCommandButton getGroupButton() {
		
		if (groupButton == null) {
			groupButton = new JCommandButton(Messages.getString("Edit.group.text"), new editor_group());
			groupButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_GROUP, AbstractViewEvent.GROUP, groupButton));			
			setTooltip(groupButton, "Edit.group");
		}
		
		return groupButton;
	}

	private JCommandButton getUngroupButton() {
		
		if (ungroupButton == null) {
			ungroupButton = new JCommandButton(Messages.getString("Edit.ungroup.text"), new editor_ungroup());
			ungroupButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_UNGROUP, AbstractViewEvent.UNGROUP, ungroupButton));			
			setTooltip(ungroupButton, "Edit.ungroup");
		}
		
		return ungroupButton;
	}

	private JCommandButton getPlaceButton() {
		
		if (placeButton == null) {
			placeButton = new JCommandButton(Messages.getString("Forms.place.text"), new forms_place());
			placeButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_PLACE, AbstractViewEvent.DRAWMODE_PLACE, placeButton));
			setTooltip(placeButton, "Forms.place");
		}
		
		return placeButton;
	}

	private JCommandButton getTransitionButton() {
		
		if (transitionButton == null) {
			 transitionButton = new JCommandButton(Messages.getString("Forms.transition.text"), new forms_transition());
			transitionButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_TRANSITION, AbstractViewEvent.DRAWMODE_TRANSITION, transitionButton));
			setTooltip(transitionButton, "Forms.transition");
		}
		
		return transitionButton;
	}

	private JCommandButton getXorSplitButton() {
		
		if (xorSplitButton == null) {
			xorSplitButton = new JCommandButton(Messages.getString("Forms.XORSplit.text"), new forms_xor_split());
			xorSplitButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_XORSPLIT, AbstractViewEvent.DRAWMODE_XORSPLIT, xorSplitButton));
			setTooltip(xorSplitButton, "Forms.XORSplit");
		}
		
		return xorSplitButton;
	}

	private JCommandButton getXorJoinButton() {
		
		if (xorJoinButton == null) {
			xorJoinButton = new JCommandButton(Messages.getString("Forms.XORJoin.text"), new forms_xor_join());
			xorJoinButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_XORJOIN, AbstractViewEvent.DRAWMODE_XORJOIN, xorJoinButton));
			setTooltip(xorJoinButton, "Forms.XORJoin");
		}
		
		return xorJoinButton;
	}

	
	private JCommandButton getAndSplitButton() {
		if (andSplitButton == null) {
			andSplitButton = new JCommandButton(Messages.getString("Forms.ANDSplit.text"),new forms_and_split());
			andSplitButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_ANDSPLIT, AbstractViewEvent.DRAWMODE_ANDSPLIT, andSplitButton));
			setTooltip(andSplitButton, "Forms.ANDSplit");
		}
		
		return andSplitButton;
	}

	private JCommandButton getAndJoinButton() {
		
		if (andJoinButton == null) {
			andJoinButton = new JCommandButton(Messages.getString("Forms.ANDJoin.text"), new forms_and_join());
			andJoinButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_ANDJOIN, AbstractViewEvent.DRAWMODE_ANDJOIN, andJoinButton));
			setTooltip(andJoinButton, "Forms.ANDJoin");
		}
		
		return andJoinButton;
	}

	private JCommandButton getXorSplitJoinButton() {
		if (xorSplitJoinButton == null) {
			xorSplitJoinButton = new JCommandButton(Messages.getString("Forms.XORSplitJoin.text"), new forms_xor_split_join());
			xorSplitJoinButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_XORSPLITJOIN, AbstractViewEvent.DRAWMODE_XORSPLITJOIN, xorSplitJoinButton));
			setTooltip(xorSplitJoinButton, "Forms.XORSplitJoin");
		}
		
		return xorSplitJoinButton;
	}

	private JCommandButton getAndSplitJoinButton() {
		
		if (andSplitJoinButton == null) {
			andSplitJoinButton = new JCommandButton(Messages.getString("Forms.ANDSplitJoin.text"), new forms_and_split_join());
			andSplitJoinButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_ANDSPLITJOIN, AbstractViewEvent.DRAWMODE_ANDSPLITJOIN, andSplitJoinButton));
			setTooltip(andSplitJoinButton, "Forms.ANDSplitJoin");
		}
		
		return andSplitJoinButton;
	}

	private JCommandButton getAndJoinXorSplitButton() {
		
		if (andJoinXorSplitButton == null) {
			andJoinXorSplitButton = new JCommandButton(Messages.getString("Forms.ANDJoinXORSplit.text"), new forms_and_join_xor_split());
			andJoinXorSplitButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_ANDJOINXORSPLIT, AbstractViewEvent.DRAWMODE_ANDJOIN_XORSPLIT, andJoinXorSplitButton));
			setTooltip(andJoinXorSplitButton, "Forms.ANDJoinXORSplit");
		}
		
		return andJoinXorSplitButton;
	}

	private JCommandButton getXorJoinAndSplitButton() {
		
		if (xorJoinAndSplitButton == null) {
			xorJoinAndSplitButton = new JCommandButton(Messages.getString("Forms.XORJoinANDSplit.text"), new forms_xor_join_and_split());
			xorJoinAndSplitButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_XORJOINANDSPLIT, AbstractViewEvent.DRAWMODE_XORJOIN_ANDSPLIT, xorJoinAndSplitButton));
			setTooltip(xorJoinAndSplitButton, "Forms.XORJoinANDSplit");
		}
		
		return xorJoinAndSplitButton;
	}

	private JCommandButton getSubprocessButton() {
		
		if (subprocessButton == null) {
			subprocessButton = new JCommandButton(Messages.getString("Forms.subprocess.text"), new forms_subprocess());
			subprocessButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_DRAWMODE_SUB, AbstractViewEvent.DRAWMODE_SUB, subprocessButton));
			setTooltip(subprocessButton, "Forms.subprocess");
		}
		
		return subprocessButton;
	}
		
	public JCommandButton getCoverabilityGraphButton() {
		
		if (coverabilityGraphButton == null) {
			coverabilityGraphButton = new JCommandMenuButton(Messages.getString("Tools.reachabilityGraph.text"), new analyze_reachability_graph());
			coverabilityGraphButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_REACHGRAPH_START, 																			 AbstractViewEvent.REACHGRAPH, coverabilityGraphButton));
			setTooltip(coverabilityGraphButton, "Tools.reachabilityGraph");
		}
		
		return coverabilityGraphButton;
	}

	private JCommandButton getColoringButton() {
		
		if (coloringButton == null) {
			coloringButton = new JCommandButton(Messages.getString("Tools.coloring.text"), new analyze_coloring());
			coloringButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_COLORING, AbstractViewEvent.COLORING, coloringButton));
			setTooltip(coloringButton, "Tools.coloring");
		}
		
		return coloringButton;
	}

	private JCommandButton getSemanticalAnalysisButton() {
		
		if (semanticalAnalysisButton == null) {
			semanticalAnalysisButton = new JCommandButton(Messages.getString("Tools.semanticalAnalysis.text"), new analyze_semanticalanalysis());
			semanticalAnalysisButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_WOPED, AbstractViewEvent.ANALYSIS_WOPED, semanticalAnalysisButton));
			setTooltip(semanticalAnalysisButton, "Tools.semanticalAnalysis");
		}
		
		return semanticalAnalysisButton;
	}

	private JCommandButton getCapacityPlanningButton() {
		
		if (capacityPlanningButton == null) {
			capacityPlanningButton = new JCommandButton(Messages.getString("Tools.capacityPlanning.text"), new analyze_capacityplanning());
			capacityPlanningButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_QUANTCAP, AbstractViewEvent.QUANTCAP, capacityPlanningButton));
			setTooltip(capacityPlanningButton, "Tools.capacityPlanning");
		}
		
		return capacityPlanningButton;
	}

	private JCommandButton getQuantitativeSimulationButton() {
		
		if (quantitativeSimulationButton == null) {
			quantitativeSimulationButton = new JCommandButton(Messages.getString("Tools.quantitativeSimulation.text"),new analyze_quantitative_simulation());
			quantitativeSimulationButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_QUANTSIM, AbstractViewEvent.QUANTSIM, quantitativeSimulationButton));
			setTooltip(quantitativeSimulationButton, "Tools.quantitativeSimulation");
		}
		
		return quantitativeSimulationButton;
	}

	private JCommandButton getProcessMetricsButton() {
		
		if (processMetricsButton == null) {
			processMetricsButton = new JCommandButton(Messages.getString("Metrics.processmetrics.text"), new analyze_metric());
			processMetricsButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_METRIC, AbstractViewEvent.ANALYSIS_METRIC, processMetricsButton));
			setTooltip(processMetricsButton, "Metrics.processmetrics");
		}
		
		return processMetricsButton;
	}

	private JCommandButton getProcessMassAnalyzeButton() {
		
		if (processMassAnalyzeButton == null) {
			processMassAnalyzeButton = new JCommandButton(Messages.getString("Metrics.processmetricsmassanalysis.text"), new analyze_metric_mass_import());
			processMassAnalyzeButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_MASSMETRICANALYSE, AbstractViewEvent.ANALYSIS_MASSMETRICANALYSE, processMassAnalyzeButton));
			setTooltip(processMassAnalyzeButton, "Metrics.processmetricsmassanalysis");
		}
		
		return processMassAnalyzeButton;
	}

	private JCommandButton getProcessMetricsBuilderButton() {
		
		if (processMetricsBuilderButton == null) {
			processMetricsBuilderButton = new JCommandButton(Messages.getString("Metrics.processmetricsbuilder.text"), new analyze_metric_builder());
			processMetricsBuilderButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_METRICSBUILDER, AbstractViewEvent.ANALYSIS_METRICSBUILDER, processMetricsBuilderButton));
			setTooltip(processMetricsBuilderButton, "Metrics.processmetricsbuilder");
		}
		
		return processMetricsBuilderButton;
	}

	private JCommandButton getChangeOrientationButton() {
		
		if (changeOrientationButton == null) {
			changeOrientationButton = new JCommandButton(Messages.getString("View.changeModellingDirection.text"),new view_change_modelling_direction());
			changeOrientationButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_ROTATEVIEW, AbstractViewEvent.ROTATEVIEW, changeOrientationButton));
			setTooltip(changeOrientationButton, "View.changeModellingDirection");
		}
		
		return changeOrientationButton;
	}

	private JCommandButton getOptimizeLayoutButton() {
		
		if (optimizeLayoutButton == null) {
			optimizeLayoutButton = new JCommandButton(Messages.getString("View.optimizeLayout.text"),new view_optimize_layout());
			optimizeLayoutButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_GRAPHBEAUTIFIER_DEFAULT, AbstractViewEvent.GRAPHBEAUTIFIER, optimizeLayoutButton));
			setTooltip(optimizeLayoutButton, "View.optimizeLayout");
		}
		
		return optimizeLayoutButton;
	}

	private JCommandButton getArrangeButton() {
		
		if (arrangeButton == null) {
			arrangeButton = new JCommandButton(Messages.getString("WindowPreferences.arrange.text"), new window_arrange());
			arrangeButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_ARRANGE, AbstractViewEvent.ARRANGE, arrangeButton));
			setTooltip(arrangeButton, "WindowPreferences.arrange");
		}
		
		return arrangeButton;
	}

	private JCommandButton getCascadeButton() {
		
		if (cascadeButton == null) {
			cascadeButton = new JCommandButton(Messages.getString("WindowPreferences.cascade.text"), new window_cascadewindows());
			cascadeButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_CASCADE, AbstractViewEvent.CASCADE, cascadeButton));
			setTooltip(cascadeButton, "WindowPreferences.arrange");
		}
		
		return cascadeButton;
	}
	
	private JRibbonComponent getOverviewComponent() {
		
		if (overviewComponent == null) {
			overviewCheckbox = new JCheckBox(Messages.getString("Sidebar.Overview.Title"));
			overviewComponent = new JRibbonComponent(overviewCheckbox);
			overviewCheckbox.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWOVERVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI, overviewCheckbox));
			overviewCheckbox.setToolTipText(Messages.getString("Sidebar.Overview.Tooltip"));
		}
		
		return overviewComponent;
	}

	private JRibbonComponent getTreeviewComponent() {
		
		if (treeviewComponent == null) {
			treeviewCheckbox = new JCheckBox(Messages.getString("Sidebar.Treeview.Title"));
			treeviewComponent = new JRibbonComponent(treeviewCheckbox);
			treeviewCheckbox.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWTREEVIEW, AbstractViewEvent.VIEWEVENTTYPE_GUI, treeviewCheckbox));
			treeviewCheckbox.setToolTipText(Messages.getString("Sidebar.Treeview.Tooltip"));
		}
		
		return treeviewComponent;
	}

	private JCommandButton getConfigurationButton() {
		
		if (configurationButton == null) {
			configurationButton = new JCommandButton(Messages.getString("OptionsAndHelp.Configuration.text"), new help_configuration());
			configurationButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWCONFIG, AbstractViewEvent.CONFIG, configurationButton));		
			setTooltip(configurationButton, "OptionsAndHelp.Configuration");
		}
		
		return configurationButton;
	}

	private JCommandButton getManualButton() {
		
		if (manualButton == null) {
			manualButton = new JCommandButton(Messages.getString("OptionsAndHelp.Manual.text"), new help_manual());
			manualButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWHELPINDEX, AbstractViewEvent.HELP, manualButton));
			setTooltip(manualButton, "OptionsAndHelp.Manual");
		}
		
		return manualButton;
	}

	private JCommandButton getContentsButton() {
		
		if (contentsButton == null) {
			contentsButton = new JCommandButton(Messages.getString("OptionsAndHelp.Contents.text"),new help_contents());
			contentsButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWHELPCONTENTS, AbstractViewEvent.HELP_CONTENTS, contentsButton));
			setTooltip(contentsButton, "OptionsAndHelp.Contents");
		}
		
		return contentsButton;
	}

	private JCommandButton getSampleNetsButton() {
		
		if (sampleNetsButton == null) {
			sampleNetsButton = new JCommandButton(Messages.getString("OptionsAndHelp.SampleNets.text"),new help_smaplenets());
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
			reportBugButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWBUGREPORT, AbstractViewEvent.BUGREPORT, reportBugButton));
			setTooltip(reportBugButton, "OptionsAndHelp.ReportBug");
		}
		
		return reportBugButton;
	}

	private JCommandButton getAboutButton() {
		
		if (aboutButton == null) {
			aboutButton = new JCommandButton(Messages.getString("OptionsAndHelp.About.text"), new help_about());
			aboutButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_SHOWABOUT, AbstractViewEvent.ABOUT, aboutButton));
			setTooltip(aboutButton, "OptionsAndHelp.About");
		}
		
		return aboutButton;
	}

	private JCommandButton getTokengameStartButton() {
		
		if (tokengameStartButton == null) {
			tokengameStartButton = new JCommandButton(Messages.getString("Tools.tokengame.text"), new analyze_tokengame());
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
				}
				// Normal dir access
				else {
					path = "../WoPeD-FileInterface/bin/" + innerPath;
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
			backwardButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_BACKWARD, AbstractViewEvent.TOKENGAME_BACKWARD, backwardButton));			
			setTooltip(backwardButton, "Tokengame.StepBand.BackwardButton");
		}
		
		return backwardButton;
	}

	private JCommandButton getJumpIntoSubProcessButton() {
		
		if (jumpIntoSubProcessButton == null) {
			jumpIntoSubProcessButton = new JCommandButton(Messages.getString("Tokengame.StepBand.JumpIntoSubProcessButton.text"), new tokengame_play_jump_into_subprocess());
			jumpIntoSubProcessButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_JUMPINTO, AbstractViewEvent.TOKENGAME_JUMPINTO, jumpIntoSubProcessButton));			
			setTooltip(jumpIntoSubProcessButton, "Tokengame.StepBand.JumpIntoSubProcessButton");
		}
		
		return jumpIntoSubProcessButton;
	}

	private JCommandButton getForwardButton() {
		
		if (forwardButton == null) {
			forwardButton = new JCommandButton(Messages.getString("Tokengame.StepBand.ForwardButton.text"), new tokengame_play_seek_forward());
			forwardButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_FORWARD, AbstractViewEvent.TOKENGAME_FORWARD, forwardButton));			
			setTooltip(forwardButton, "Tokengame.StepBand.ForwardButton");
		}
		
		return forwardButton;
	}

	private JCommandButton getPauseButton() {
		
		if (pauseButton == null) {
			pauseButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.PauseButton.text"), new tokengame_play_pause());
			pauseButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_PAUSE, AbstractViewEvent.TOKENGAME_PAUSE, pauseButton));			
			setTooltip(pauseButton, "Tokengame.AutoBand.PauseButton");
		}
		
		return pauseButton;
	}

	private JCommandButton getStartButton() {
		
		if (startButton == null) {
			startButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.StartButton.text"), new tokengame_play_start());
			startButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_START, AbstractViewEvent.TOKENGAME_START, startButton));			
			setTooltip(startButton, "Tokengame.AutoBand.StartButton");
		}
		
		return startButton;
	}

	private JCommandButton getStopButton() {
		
		if (stopButton == null) {
			stopButton = new JCommandButton(Messages.getString("Tokengame.StepBand.StopButton.text"), new tokengame_play_stop());
			stopButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_STOP, AbstractViewEvent.TOKENGAME_STOP, stopButton));			
			setTooltip(stopButton, "Tokengame.StepBand.StopButton");
		}
		
		return stopButton;
	}

	private JCommandButton getJumpOutOfSubprocessButton() {
		
		if (jumpOutOfSubprocessButton == null) {
			jumpOutOfSubprocessButton = new JCommandButton(Messages.getString("Tokengame.StepBand.JumpOutOfSubprocessButton.text"), new tokengame_play_jump_out_of_subprocess());
			jumpOutOfSubprocessButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_LEAVE, AbstractViewEvent.TOKENGAME_LEAVE, jumpOutOfSubprocessButton));			
			setTooltip(jumpOutOfSubprocessButton, "Tokengame.StepBand.JumpOutOfSubprocessButton");
		}
		
		return jumpOutOfSubprocessButton;
	}

	private JCommandButton getStepWiseButton() {
		
		if (stepWiseButton == null) {
			stepWiseButton = new JCommandButton(Messages.getString("Tokengame.StepBand.StepByStepButton.text"), new tokengame_edit_step_by_step());
			stepWiseButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_STEP, AbstractViewEvent.TOKENGAME_STEP, stepWiseButton));			
			setTooltip(stepWiseButton, "Tokengame.StepBand.StepByStepButton");
		}
		
		return stepWiseButton;
	}
		
	private JCommandButton getAutoPlayButton() {
		
		if (autoPlayButton == null) {
			autoPlayButton = new JCommandButton(Messages.getString("Tokengame.AutoBand.AutoPlayButton.text"), new tokengame_edit_autoPlay());
			autoPlayButton.addActionListener(new ActionButtonListener(m_mediator,ActionFactory.ACTIONID_TOKENGAME_AUTO, AbstractViewEvent.TOKENGAME_AUTO, autoPlayButton));			
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

	private JCommandButton getGoogleplusButton() {
		
		if (googleplusButton == null) {
			googleplusButton = new JCommandButton(Messages.getString("Community.Googleplus.text"), new Google_plus_icon());
			googleplusButton.addActionListener(new ActionButtonListener(m_mediator, ActionFactory.ACTIONID_GOOGLEPLUS, AbstractViewEvent.GOOGLEPLUS, googleplusButton));
			setTooltip(googleplusButton, "Community.Googleplus");
		}
		
		return googleplusButton;
	}

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

	
		
	public void fireViewEvent(AbstractViewEvent viewevent) {
		this.m_mediator.fireViewEvent(viewevent);
	}
	
	/**
	 * Gets the mediator for fireing events with shortcuts
	 *
	 * @return the mediator
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
	public AbstractApplicationMediator getMediator(){
		return this.m_mediator;
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
	public IReachabilityGraph getReachGraphFocus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hideEditor(IEditor editor) {
		// TODO Auto-generated method stub
		
	}

	public void setMediator(AbstractApplicationMediator mediator) {
		m_mediator = mediator;		
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
	    if (editor.isTokenGameEnabled()) {
	    	getRibbon().setVisible(getTokengameGroup(), true);
	    	getRibbon().setSelectedTask(getTokengameTask());
		}
		else {
			if (getRibbon().isVisible(getTokengameGroup())) {
				getRibbon().setVisible(getTokengameGroup(), false);
				getRibbon().setSelectedTask(getAnalyzeTask());
			}
	    }
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
}
