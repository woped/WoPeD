package org.woped.qualanalysis.simulation;
import javax.swing.*;
import java.util.LinkedList;

import org.woped.core.model.petrinet.*;

import org.woped.translations.Messages;
import org.woped.qualanalysis.simulation.controller.*;
import java.awt.*;

import org.woped.qualanalysis.test.*;

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
	private ReferenceProvider MainWindowReference = null;
	private TokenGameHistoryManagerVC HistoryDialog = null;
	private TransitionModel   TransitionToOccur         = null;
	private TransitionModel   BackwardTransitionToOccur = null;
	private TransitionModel   helpTransition    = null;

	
	//Linked Lists 
	private LinkedList       followingActivatedTransitions           = null;
	private LinkedList       previousActivatedTransitions            = null;
		
	// TokenGame
	private TokenGameController m_tokenGameController = null;
	
	//Constructor(s)
	public TokenGameBarVC(TokenGameController tgcontroller)
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
		
		//Define Playback-Buttons
		pbnFastBW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.FastBackward"));
	    pbnBW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Backward"));
		pbnStop = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Stop"));
		pbnPlay = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Play"));
		pbnPause = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Pause"));
		pbnFW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Forward"));
		pbnFastFW = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.FastForward"));
		
		//Define Button-Size
		pbnFastBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnStop.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPlay.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPause.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFastFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		
		//Define Playback's ToolTips
		pbnFastBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastBackward")); 
		pbnBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Backward")); 
		pbnStop.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stop")); 
		pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Play")); 
		pbnPause.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Pause")); 
		pbnFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Forward")); 
		pbnFastFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastForward")); 
		
		
		//Define Button-Actions
		pbnBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_BACKWARD, this));
		pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, this));
		pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, this));
		pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, this));
		pbnFastFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_FORWARD, this));
		pbnFastBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_BACKWARD, this));
		
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
		
		//List will be displayed
		if(followingActivatedTransitions.size() == 2)
		{
			helpTransition = (TransitionModel)followingActivatedTransitions.get(0);
			acoChoiceItems.addElement(helpTransition.getNameValue());
			helpTransition = (TransitionModel)followingActivatedTransitions.get(1);
			acoChoiceItems.addElement(helpTransition.getNameValue());
    		disableForwardButtons();
		}
		if(followingActivatedTransitions.size() > 2)
		{
			helpTransition = (TransitionModel)followingActivatedTransitions.get(followingActivatedTransitions.size()-1);
			acoChoiceItems.addElement(helpTransition.getNameValue());
		    disableForwardButtons();
		}
		if(followingActivatedTransitions.size() == 1)
		{
			TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(0);
		}
	}
	
	//Get previous "BackwardActive" Transitions
	/**
	 *  List of previousActivated Transitions is built up here
	 */
	public void addPreviousItem(TransitionModel transition)
	{
		if(previousActivatedTransitions == null)
		{
			previousActivatedTransitions = new LinkedList();
		}
		previousActivatedTransitions.addLast(transition);
			
			BackwardTransitionToOccur = (TransitionModel)previousActivatedTransitions.get(previousActivatedTransitions.size()-1);
		
	}
	
	
	public int getSelectedChoiceID()
	{
		return acoChoice.getSelectedIndex();
	
	}
	
	public void clearChoiceBox()
	{
		acoChoiceItems.clear();
		enableForwardButtons();
	}
	
	
	//HistoryListbox
	public void addHistoryData(String[] Data)
	{
		ahxHistoryContent.clear();
		for(int i = 0; i < Data.length; i++)
		{
	      ahxHistoryContent.addElement(Data[i]);
		}
	}
	
	
	public DefaultListModel getHistoryData()
	{
		return ahxHistoryContent;
	}
	
	public void clearHistoryData()
	{
		ahxHistoryContent.clear();
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
	
	public void disablePlayButton()
	{
		pbnPlay.setEnabled(false);
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
		if((BackWard) && (BackwardTransitionToOccur != null))
		{
			TransitionToOccur = BackwardTransitionToOccur;
			// = Let the transition occur backward.
			/*
			 * Currently always the last added Transition in the "previousActivatedTransition"-List is taken
			 * TODO: @Anthy: 
			 * Two lists exist:
			 * 1.) followingActivatedTransitions:
			 *     All possible transitions for a Forward-Step are listed in there
			 * 2.) previousActivatedTransitions:
			 *     All possible transitions for a Backward-Step are listed in there
			 *
			 * For the first List, the choice-structure has already been established
			 * For the second List (backward) a choice algorithm and appearing rules are needed
			 * The chosen Backward-Transition should be Stored in "BackwardTransitionToOccur" and
			 * the occurTransition - Method should be called with "BackWard == true" 
			 * 
			 */
		}
		m_tokenGameController.occurTransitionbyTokenGameBarVC(TransitionToOccur, BackWard);
		
	}
	
	/**
	 * This method let the multiple transition occur (now 3 times) (only for sequences,
	 * as two transitions are active, the methode will stop)
	 * TODO: 
	 * 1) replace occurtimes 3 with a parameter. On default 3 but User can
	 * define this parameter, but this will be
	 * 2) intruduce parameter to define if it is fast forward or fast rewind
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