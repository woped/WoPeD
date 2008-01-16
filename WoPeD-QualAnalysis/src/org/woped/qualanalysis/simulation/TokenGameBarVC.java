package org.woped.qualanalysis.simulation;
import javax.swing.*;
import java.util.LinkedList;

import org.woped.core.model.petrinet.*;
import org.woped.translations.Messages;
import org.woped.qualanalysis.simulation.controller.*;
import java.awt.*;
import java.util.Vector;
import org.woped.qualanalysis.test.*;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.PetriNetModelProcessor;
/**
 * This class specifies the remote control UI of the Tokengame-simulator
 * Currently no Actions can be performed by the Buttons except the close-"x"
 * Standard-Constructor is available
 * 
 * @author Tilmann Glaser
 * 
 */
public class TokenGameBarVC extends JInternalFrame {
	
	
	//Declaration of all JPanels
	private JPanel PropertiesPanel    = null;
	private JPanel NavigationPlayback = null;
	private JPanel AutoChoice         = null;
	private JPanel History            = null;
	
	//Declaration of all Buttons
	private JButton ppbSteps          = null;
	private JButton ppbPlay           = null;
	private JButton ppbDelay          = null;
	private JButton pbnUp             = null;
	private JButton pbnDown           = null;
    private JButton pbnFastBW         = null;
    private JButton pbnBW             = null;
	private JButton pbnStop           = null;
	private JButton pbnPlay           = null;
	private JButton pbnPause          = null;
	private JButton pbnFW             = null;
	private JButton pbnFastFW         = null;
	private JButton acoAuto           = null;
	private JButton ahyJump           = null;
	private JButton ahySave           = null;
	private JButton ahyDelete         = null;
	private JToggleButton pbnRecord   = null;
	
	//Declaration of the Lists
	private JList       acoChoice     = null; 
	private JScrollPane acoScroll     = null;
	private JList       ahxChoice     = null;
	private JScrollPane ahxScroll     = null;
	private DefaultListModel ahxHistoryContent = null;
	private DefaultListModel acoChoiceItems = null;
	
	//Other Variables
	private int    stXsize            = 50;
	private int    stYsize            = 25;
	private int    xtXsize            = 30;
	private int    xtYsize            = 25;
	private GridBagConstraints hgbc   = null;
	private GridBagConstraints gbc    = null;
	private boolean newHistory        = false;
	
	//private ReferenceProvider MainWindowReference = null;
	//private TokenGameHistoryManagerVC HistoryDialog = null;
	private TransitionModel   TransitionToOccur         = null;
	private TransitionModel   BackwardTransitionToOccur = null;
	private TransitionModel   helpTransition    = null;
    private boolean			  autoplayback      = false;
	private PetriNetModelProcessor           PetriNet = null;
    
	//Linked Lists 
	private LinkedList       followingActivatedTransitions           = null;
	private LinkedList       previousActivatedTransitions            = null;
	
	//History Variables
	private Vector <TransitionModel>  HistoryVector           = null;
	private SimulationModel   SaveableSimulation              = null;
	private static int HistoryID         = 0; //Help Variable as long as no Name is available for Histories
	private static int HistoryIndex      = 0; //Will guide through the History when playbacking
	private boolean backward = false;
	// TokenGame
	private TokenGameController m_tokenGameController = null;
	
	//Constructor(s)
	public TokenGameBarVC(TokenGameController tgcontroller, PetriNetModelProcessor PetriNet)
	{
		super(Messages.getTitle("Tokengame.RemoteControl"), false, true);
		this.setFrameIcon(Messages.getImageIcon("Tokengame.RemoteControl"));
	    this.setToolTipText(Messages.getTitle("Tokengame.RemoteControl"));//setAlignmentY(300);
		this.setSize(910,140);
		this.setVisible(true);
		//RC will disappear
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		
		this.getContentPane();
		this.setLayout(new FlowLayout(1,5,10)); //1 = Align Center
		this.add(addPropertiesPanel());
		this.add(addPlaybackNavigation());
		this.add(addAutoChoice());
		this.add(addHistory());
		this.PetriNet = PetriNet;
		

		
		m_tokenGameController = tgcontroller;

	}
	
	

	/**
	 * this is the Left Bar of Buttons in the Remote Control
	 * @return
	 */
	
	private JPanel addPropertiesPanel()
	{
		//Define Buttons
	    ppbSteps = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Stepwise"));
	    ppbPlay  = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Playback"));
		ppbDelay = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Delay"));
		
		//Define Button-Size
		ppbSteps.setPreferredSize(new Dimension(stXsize, stYsize));
		ppbPlay.setPreferredSize(new Dimension(stXsize, stYsize));
		ppbDelay.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define Button's ToolTips
		ppbSteps.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stepwise"));
		ppbPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Playback"));
		ppbDelay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Delay"));
		
		//Define Button's Actions
		ppbPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_PLAYBACK, this));
		ppbSteps.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_STEPWISE, this));
		
		//Define Panel and Layout
		PropertiesPanel = new JPanel();
		PropertiesPanel.setLayout(new GridLayout(3,1,0,5));
		
		//add Buttons to the Panel
		PropertiesPanel.add(ppbSteps);
		PropertiesPanel.add(ppbPlay);
		PropertiesPanel.add(ppbDelay);
		
		return PropertiesPanel;	
	}
	
	/**
	 * The Playback-Buttons Panel
	 * @return
	 */
	private JPanel addPlaybackNavigation()
	{
		//Define Navigation-Buttons
		pbnUp = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.NaviUp"));
		pbnDown = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.NaviDown"));
		
		//Define Button-Size
		pbnUp.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnDown.setPreferredSize(new Dimension(xtXsize, xtYsize));
		
		//Define Navigation's ToolTips
		pbnUp.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.NaviUp")); 
		pbnDown.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.NaviDown")); 
		
		//Define Navigation-Actions
		pbnUp.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STEP_UP, this));
		pbnDown.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STEP_DOWN, this));
		
		//Define Playback-Buttons
		pbnFastBW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.FastBackward"));
	    pbnBW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Backward"));
		pbnStop = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Stop"));
		pbnPlay = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Play"));
		pbnPause = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Pause"));
		pbnFW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Forward"));
		pbnFastFW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.FastForward"));
		pbnRecord = new JToggleButton(Messages.getImageIcon("Tokengame.RemoteControl.Record"));
		
		//Define Button-Size
		pbnFastBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnStop.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPlay.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPause.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFastFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnRecord.setPreferredSize(new Dimension(xtXsize, xtYsize));
		
		//Define Playback's ToolTips
		pbnFastBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastBackward")); 
		pbnBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Backward")); 
		pbnStop.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stop")); 
		pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Play")); 
		pbnPause.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Pause")); 
		pbnFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Forward")); 
		pbnFastFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastForward")); 
		pbnRecord.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Record"));
		
		//Define Button-Actions
		pbnBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_BACKWARD, this));
		pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, this));
		pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, this));
		pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, this));
		pbnFastFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_FORWARD, this));
		pbnFastBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_BACKWARD, this));
		pbnRecord.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_RECORD, this));
				
		//Create Playback&Navigation-Panel and add Buttons
		NavigationPlayback = new JPanel();
		NavigationPlayback.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
        
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.insets = new Insets (0,5,5,0);
		NavigationPlayback.add(pbnUp, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,5,0,0);
		NavigationPlayback.add(pbnFastBW, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnBW, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnStop, gbc);

		gbc.gridx = 3;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnPlay, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnPause, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnFW, gbc);
		
		gbc.gridx = 6;
		gbc.gridy = 1;
		NavigationPlayback.add(pbnFastFW, gbc);

		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.insets = new Insets (5,5,0,0);
		NavigationPlayback.add(pbnDown, gbc);

		gbc.gridx = 6;
		gbc.gridy = 0;
		gbc.insets = new Insets (0,5,0,0);
		NavigationPlayback.add(pbnRecord, gbc);
		
		return NavigationPlayback;
	}
	
	/**
	 * The AutoChoice Panel
	 * @return
	 */
	private JPanel addAutoChoice()
	{
		//Define Elements
		//... the autochoice Button
		acoAuto = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.AutoChoice"));
		acoAuto.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.AutoChoice"));
		
		//Define Button-Size
		acoAuto.setPreferredSize(new Dimension(stXsize, stYsize));
				
		//... the easychoice List
		acoChoiceItems = new DefaultListModel();
		acoChoice = new JList(acoChoiceItems);
		acoChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acoChoice.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.ChoiceList"));
		
		//add Listener
		acoChoice.addMouseListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_TRANSITION, this));
		
		//... the easychoice Scroll-Bars and Listbox Sizedefinition
		acoScroll = new JScrollPane(acoChoice);
		acoScroll.setPreferredSize(new Dimension(200,85));
		acoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		acoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Add Elements to Panel
		AutoChoice = new JPanel();
		AutoChoice.add(acoAuto);
		AutoChoice.add(acoScroll);
		
		return AutoChoice;
	}
	
	/**
	 * The History Panel
	 * @return
	 */
	private JPanel addHistory()
	{
		//Define Buttons
		ahyJump = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.JumpHere"));
		ahySave = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.SaveHistory"));
		ahyDelete = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.DeleteHistory"));
		
		//Define Button-Size
		ahyJump.setPreferredSize(new Dimension(stXsize, stYsize));
		ahySave.setPreferredSize(new Dimension(stXsize, stYsize));
		ahyDelete.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define Button's ToolTips
		ahyJump.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.JumpHere"));
		ahySave.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.SaveHistory"));
		ahyDelete.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.DeleteHistory"));
		
		//Define ActionListeners
		ahyJump.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_JUMP_HERE, this));
		ahySave.addActionListener(new TokenGameBarListener(TokenGameBarListener.OPEN_HISTORY_MANAGER, this));
		ahyDelete.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_DELETE_CURRENT, this));
		
		//Define Panel and add Buttons
		History = new JPanel();
		History.setLayout(new GridBagLayout());
		hgbc = new GridBagConstraints();
		
		hgbc.gridx = 0;
		hgbc.gridy = 0;
		hgbc.insets = new Insets(0,0,5,0);
		History.add(ahyJump, hgbc);
		
		hgbc.gridx = 0;
		hgbc.gridy = 1;
		hgbc.insets = new Insets(0,0,0,0);
		History.add(ahySave, hgbc);
		
		hgbc.gridx = 0;
		hgbc.gridy = 2;
		hgbc.insets = new Insets(5,0,0,0);
		History.add(ahyDelete, hgbc);

		//Define Scrollbars, Listbox-Size and add to Panel
		ahxHistoryContent = new DefaultListModel();
		ahxChoice = new JList(ahxHistoryContent);
		ahxChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ahxChoice.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.HistoryList"));
		
		ahxScroll = new JScrollPane(ahxChoice);
		ahxScroll.setPreferredSize(new Dimension(250,85));
		ahxScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ahxScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		hgbc.gridx = 1;
		hgbc.gridy = 0;
		hgbc.gridheight = 3;
		hgbc.insets = new Insets(0,5,0,0);
		hgbc.fill = GridBagConstraints.VERTICAL;
		History.add(ahxScroll, hgbc);	
		return History;
	}
	
	//All Actions regarding the TokenGame-Remote-Control
	
	/*
	 * List-Boxes' section
	 */
	
	//Choice Listbox
	//get Active Transitions
	public void addFollowingItem(TransitionModel transition)
	{
		if(followingActivatedTransitions == null)
		{
			followingActivatedTransitions = new LinkedList();
		}
		followingActivatedTransitions.addLast(transition);
	}
	
	
	//Get previous "BackwardActive" Transitions
	/**
	 * previousActivated Transition is taken out of the HistoryVector
	 */
	public void addPreviousItem(TransitionModel transition)
	{;
	}
	
	/**
	 * will set the "BackwardTransitionToOccur" with Data
	 */
	public void previousItem()
	{		  
	  if(HistoryVector != null)
	  {  
		if(!playbackRunning())
		{
			 if (HistoryVector.size() > 0)
			 {
			   BackwardTransitionToOccur = (TransitionModel)HistoryVector.remove(HistoryVector.size()-1);		
			   if (isRecordSelected())
			   {
			   	ahxHistoryContent.remove(ahxHistoryContent.size()-1);
			   }
			 }  
		}
		else
		{
			BackwardTransitionToOccur = (TransitionModel)HistoryVector.get(HistoryIndex--);
		}
	  }			
	}
	

	public int getSelectedChoiceID()
	{
		return acoChoice.getSelectedIndex();
	
	}
	
	/**
	 * Adds multi-choice entries to the multi-choice box
	 */
	public void fillChoiceBox()
	{
	  if(!playbackRunning())	
	  {
		clearChoiceBox(); //To ensure that the box is empty
		if(followingActivatedTransitions.size() == 1)
		{
			TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(0);
		}
		if(followingActivatedTransitions.size() > 1)
		{
			for(int i = 0; i < followingActivatedTransitions.size(); i++)				
			{
				helpTransition = (TransitionModel)followingActivatedTransitions.get(i);
     			acoChoiceItems.addElement(helpTransition.getNameValue());
			}
     		disableForwardButtons();
		}
	  }
	  else
	  {
	
    	if(HistoryIndex < ahxHistoryContent.size())
		{
			if(HistoryIndex < 0)
			{
				HistoryIndex = 0;
				backward = false;
			}
	    	TransitionToOccur = HistoryVector.get(HistoryIndex);
			if(!backward)
			{	
	    	  HistoryIndex++;
	    	  if(HistoryIndex >= ahxHistoryContent.size())
	    	  {
	    		  HistoryIndex = ahxHistoryContent.size()-1;
	    	  }
			}
			else
			{
				backward = false;
			}
		}
	  }
	}
	
	public void clearChoiceBox()
	{
		acoChoiceItems.clear();
		enableForwardButtons();
	}
	
	//HistoryListbox
		
	public SimulationModel getHistoryData()
	{
		return SaveableSimulation;
	}
	
	public void clearHistoryData()
	{
		ahxHistoryContent.clear();
		HistoryVector = null; //Set reference to null, so that a new history-Vector will be created!
	}
	

	/*
	 * History Recording
	 */
    
	public boolean isRecordSelected()
	{
		return pbnRecord.isSelected();
	}
    	
	
    public void addHistoryItem(TransitionModel transition)
    {	
    	if (HistoryVector == null)
    	{
    		HistoryVector = new Vector<TransitionModel>(1);
    		ahxHistoryContent.clear();
    	}
    	HistoryVector.add(transition);
    }

    
    public void addHistoryListItem(TransitionModel transition)
    {
    	ahxHistoryContent.addElement(transition.getNameModel());
    }
    
    public void createSaveableHistory()
    {
    	SaveableSimulation = new SimulationModel(PetriNet.getNewElementId(AbstractPetriNetModelElement.SIMULATION_TYPE), "Name"+HistoryID, (Vector<TransitionModel>)HistoryVector.clone());
    	newHistory = true;
    	HistoryID++;
    }
	
    /**
     * Adds the History (SimulationModel) Objekt to the Simulations Vector
     */
    public void saveHistory()
    {
    	PetriNet.addSimulation(SaveableSimulation);
    }
    
    
    public void loadHistory(int index)
    {
    	//HistoryIndex = 0;
    	ahxHistoryContent.clear();
    	SaveableSimulation = PetriNet.getSimulations().elementAt(index);
    	//needs a clone, otherwise, the saved history might be erased when the user just wants to clean the history-box
    	HistoryVector = (Vector<TransitionModel>)SaveableSimulation.getFiredTransitions().clone();
    	for (int i = 0; i < HistoryVector.size(); i++)
    	{
    		ahxHistoryContent.addElement(HistoryVector.get(i).getNameValue());
    	}
    	//TransitionToOccur = HistoryVector.get(HistoryIndex);
    }
    
    public boolean isNewHistory()
    {
    	return newHistory;
    }
    
    public void setToOldHistory()
    {
    	newHistory = false;
    }
    
    public void startHistoryPlayback()
    {
    	HistoryIndex = 0;
    	TransitionToOccur = HistoryVector.get(HistoryIndex);
    	HistoryIndex++;   	
    }
    
	/*
	 * disable / enable Buttons - Sektion
	 * 
	 */
	
	public void disableForwardButtons()
	{
		pbnFW.setEnabled(false);
		pbnFastFW.setEnabled(false);
	}
	
	public void enableForwardButtons()
	{
		pbnFW.setEnabled(true);
		pbnFastFW.setEnabled(true);
	}
	
	public void enablePlayButton()
	{
		pbnPlay.setEnabled(true);
	}
	
	public boolean isPlayButtonEnabled()
	{
		return pbnPlay.isEnabled();
	}
	
	public void disablePlayButton()
	{
		pbnPlay.setEnabled(false);
	}
	
	public void setAutoPlayback(boolean onoff)
	{
		autoplayback = onoff;
	}
	
	public boolean getAutoPlayBack()
	{
		return autoplayback;
	}
	
	public void enableStepDown()
	{
		pbnDown.setEnabled(true);
	}
	
	public void disableStepDown()
	{
		pbnDown.setEnabled(false);
	}
	
	public void enableRecordButton()
	{
		pbnRecord.setEnabled(true);
	}
	
	public void disableRecordButton()
	{
		pbnRecord.setEnabled(false);
	}
		

	
	/**
	 * This method is to avoid problems when stepping in any direction without having pressed playback in advance
	 * @return
	 */
	public boolean tokengameRunning()
	{
		if(!pbnPlay.isEnabled())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * this method is to check whether a real playback is running, a walkthrough without recording or a record session.
	 * If it is a real playback, the application will not give the user any choice-possibility but it will just follow
	 * the history in the history-box
	 * @return true / false 
	 */
	public boolean playbackRunning()
	{
		if((ahxHistoryContent.size() != 0) && (!isRecordSelected()))
		{
		  return true;
		}
		else
		{
			return false;	
		}
	}
	
	/*
	 * Transition-Actions 
	 */
	
	
	/**
	 * This method will be called by the EasyChoice-Event and will handover the
	 * chosen transition to the occurTransition() method 
	 */
	public void proceedTransitionChoice(int index)
	{
		if((followingActivatedTransitions != null) && (index < followingActivatedTransitions.size()))
		{
			TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(index);
		    occurTransition(false);
		}	
	}
	
	
	/**
	 * This method let the active transition occur (currently only for sequences, as soon
	 * as two transitions are active, the method cannot occur so far)
	 */
	public void occurTransition(boolean BackWard)
	{
	  if( BackWard )
	  {
		previousItem();
		backward = true;
		if(BackwardTransitionToOccur != null)
		{
			TransitionToOccur = BackwardTransitionToOccur;
		}
  	  }
	  m_tokenGameController.occurTransitionbyTokenGameBarVC(TransitionToOccur, BackWard);
	}
	
	/**
	 * This method let the multiple transition occur (now 3 times) (only for sequences,
	 * as two transitions are active, the methode will stop)
	 * TODO: 
	 * 1) replace occurtimes 3 with a parameter. On default 3 but User can
	 * define this parameter, but this will be
	 */
	public void occurTransitionMulti(boolean BackWard)
	{
		int i = 0;
		if(BackWard)
		{
			while (i != 3)
			{
				if(previousActivatedTransitions.size() < 2)
				{
					occurTransition(BackWard);
				}
				i++;
			}
		}
		else
		{
			while (i != 3)
			{
				if(followingActivatedTransitions.size() < 2)
				{
					occurTransition(BackWard);
				}
				i++;
			}
		}
		
	}
	
	/**
	 * Automatic TokenGame
	 * This Method occur automatic all transition if autoplayback is true. Choice transition will be
	 * occured by random choice
	 * 
	 * ToDo: 
	 * 	1) refreshNet after occurence
	 *  2) random choice only if no probabilities are set
	 */
	public void autoOccurAllTransitions(boolean BackWard)
	{
		boolean ende = false;
		int index;
		while(!ende)
		{
			if(BackWard)
			{
				if(previousActivatedTransitions.size() == 0)
				{
					ende = true;
				}
				else
				{
					occurTransition(BackWard);
				}
			}
			else
			{
				if(followingActivatedTransitions.size() == 0)
				{
					ende = true;
				}
				else
				{
					if(followingActivatedTransitions.size() >= 2)
					{
						index = (int) Math.round(Math.random() * followingActivatedTransitions.size()-1);
						TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(index);
						occurTransition(BackWard);
					}
					else
					{
					occurTransition(BackWard);
					}
				}
			}
		}
	}
	
	/**
	 * Cleans up the ChoiceBox and the ChoiceArray.
	 * Is called by the TokenGameController.transitionClicked method and therefore
	 * makes it possible to step through the net with in-Editor-clicks or Remote-clicks
	 */
	public void cleanupTransition()
	{
		clearChoiceBox();
		if(followingActivatedTransitions != null)
		{
			followingActivatedTransitions.clear();
		}
		if(previousActivatedTransitions != null)
		{
			previousActivatedTransitions.clear();
		}
	}
	
	/**
	 * 
	 * @return Reference to TokenGameController
	 */
	public TokenGameController getTokenGameController()
	{
		return m_tokenGameController;
	}
	
	
}