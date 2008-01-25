package org.woped.qualanalysis.simulation;
import javax.swing.*;

import org.woped.translations.Messages;
import org.woped.qualanalysis.simulation.controller.*;
import java.awt.*;

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
	private JPanel                   PropertiesPanel               = null;
	private JPanel                   NavigationPlayback            = null;
	private JPanel                   AutoChoice                    = null;
	private JPanel                   History                       = null;
	
	//Declaration of all Buttons
	private JButton                  ppbSteps                      = null;
	private JButton                  ppbPlay                       = null;
	private JButton                  ppbDelay                      = null;
	private JButton                  pbnUp                         = null;
	private JButton                  pbnDown                       = null;
    private JButton                  pbnFastBW                     = null;
    private JButton                  pbnBW                         = null;
	private JButton                  pbnStop                       = null;
	private JButton                  pbnPlay                       = null;
	private JButton                  pbnPause                      = null;
	private JButton                  pbnFW                         = null;
	private JButton                  pbnFastFW                     = null;
	private JButton                  acoAuto                       = null;
//	private JButton                  ahyJump                       = null;
	private JButton                  ahySave                       = null;
	private JButton                  ahyDelete                     = null;
	private JToggleButton            pbnRecord                     = null;
	
	//Declaration of the Lists
	private JList                    acoChoice                     = null; 
	private JScrollPane              acoScroll                     = null;
	private JList                    ahxChoice                     = null;
	private JScrollPane              ahxScroll                     = null;
	private DefaultListModel         acoChoiceItems                = null;
	private DefaultListModel         ahxHistoryContent             = null;
	
	//Other Variables
	private int                      stXsize                       = 50;
	private int                      stYsize                       = 25;
	private int                      xtXsize                       = 30;
	private int                      xtYsize                       = 25;
	private GridBagConstraints       hgbc                          = null;
	private GridBagConstraints       gbc                           = null;

	private TokenGameBarController   tgbController                 = null;
	
	//Constructor(s)
	public TokenGameBarVC(TokenGameBarController tgbController, DefaultListModel acoChoiceItems, DefaultListModel ahxHistoryContent)
	{
		super(Messages.getTitle("Tokengame.RemoteControl"), false, false);
		this.setFrameIcon(Messages.getImageIcon("Tokengame.RemoteControl"));
	    this.setToolTipText(Messages.getTitle("Tokengame.RemoteControl"));//setAlignmentY(300);
		this.setSize(910,140);
		this.setVisible(true);
		//RC will disappear
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		
		this.acoChoiceItems = acoChoiceItems;
		this.ahxHistoryContent = ahxHistoryContent;
		this.tgbController = tgbController;
		
		this.getContentPane();
		this.setLayout(new FlowLayout(FlowLayout.CENTER,5,10));
		this.add(addPropertiesPanel());
		this.add(addPlaybackNavigation());
		this.add(addAutoChoice());
		this.add(addHistory());
		
		
		
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
		ppbPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_PLAYBACK, tgbController));
		ppbSteps.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_STEPWISE, tgbController));
		ppbDelay.addActionListener(new TokenGameBarListener(TokenGameBarListener.OPEN_PLAYBACK_MANAGER, tgbController));
		
		//Define Panel and Layout
		PropertiesPanel = new JPanel();
		PropertiesPanel.setLayout(new GridLayout(3,1,0,5));
		
		//add Buttons to the Panel
		PropertiesPanel.add(ppbSteps);
		PropertiesPanel.add(ppbPlay);
		PropertiesPanel.add(ppbDelay);
		
		//Disbale StepButton on Default
		disableStepwiseButton();
		
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
		pbnUp.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STEP_UP, tgbController));
		pbnDown.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STEP_DOWN, tgbController));
		
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
		pbnBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_BACKWARD, tgbController));
		pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, tgbController));
		pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, tgbController));
		pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, tgbController));
		pbnFastFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_FORWARD, tgbController));
		pbnFastBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_BACKWARD, tgbController));
				
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
		acoChoice = new JList(acoChoiceItems);
		acoChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acoChoice.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.ChoiceList"));
		
		//add Listener
		acoChoice.addMouseListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_TRANSITION, tgbController));
		
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
		pbnRecord = new JToggleButton(Messages.getImageIcon("Tokengame.RemoteControl.Record"));
		ahySave = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.SaveHistory"));
		ahyDelete = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.DeleteHistory"));
		
		//Define Button-Size
		pbnRecord.setPreferredSize(new Dimension(stXsize, stYsize));
		ahySave.setPreferredSize(new Dimension(stXsize, stYsize));
		ahyDelete.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define Button's ToolTips
		pbnRecord.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Record"));
		ahySave.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.SaveHistory"));
		ahyDelete.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.DeleteHistory"));
		
		//Define ActionListeners
		pbnRecord.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_RECORD, tgbController));
		ahySave.addActionListener(new TokenGameBarListener(TokenGameBarListener.OPEN_HISTORY_MANAGER, tgbController));
		ahyDelete.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_DELETE_CURRENT, tgbController));
		
		//Define Panel and add Buttons
		History = new JPanel();
		History.setLayout(new GridBagLayout());
		hgbc = new GridBagConstraints();
		
		hgbc.gridx = 0;
		hgbc.gridy = 0;
		hgbc.insets = new Insets(0,0,5,0);
		History.add(pbnRecord, hgbc);
		
		hgbc.gridx = 0;
		hgbc.gridy = 1;
		hgbc.insets = new Insets(0,0,0,0);
		History.add(ahySave, hgbc);
		
		hgbc.gridx = 0;
		hgbc.gridy = 2;
		hgbc.insets = new Insets(5,0,0,0);
		History.add(ahyDelete, hgbc);

		//Define Scrollbars, Listbox-Size and add to Panel
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
	
	
	
   /*
    * getter and setter
    */

	public int getSelectedChoiceID()
	{
		return acoChoice.getSelectedIndex();
	
	}
	


	
	/*
	 * "is"-checks
	 */

	public boolean isRecordSelected()
	{
		return pbnRecord.isSelected();
	}
    
	public boolean isPlayButtonEnabled()
	{
		return pbnPlay.isEnabled();
	}

    
    

    
	/*
	 * disable / enable Buttons - Sektion 
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
	
	public void disableBackWardButtons()
	{
		pbnBW.setEnabled(false);
		pbnFastBW.setEnabled(false);
	}
	
	public void enableBackWardButtons()
	{
		pbnBW.setEnabled(true);
		pbnFastBW.setEnabled(true);
	}
	
	public void enablePlayButton()
	{
		pbnPlay.setEnabled(true);
	}
	
	public void disablePlayButton()
	{
		pbnPlay.setEnabled(false);
	}
	
	public void disableStepwiseButton()
	{
		ppbSteps.setEnabled(false);
	}
	
	public void enableStepwiseButton()
	{
		ppbSteps.setEnabled(true);
	}
	
	public void disableAutoPlaybackButton()
	{
		ppbPlay.setEnabled(false);
	}
	
	public void enableAutoPlaybackButton()
	{
		ppbPlay.setEnabled(true);
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
			
	public void disableButtonforAutoPlayback()
	{
		ppbDelay.setEnabled(false);
		pbnUp.setEnabled(false);
		pbnDown.setEnabled(false);
		
		pbnBW.setEnabled(false);
		pbnFastBW.setEnabled(false);
		pbnFW.setEnabled(false);
		pbnFastFW.setEnabled(false);

		acoAuto.setEnabled(false);
	}
	
	public void enableButtonforAutoPlayback()
	{
		ppbDelay.setEnabled(true);
		pbnUp.setEnabled(true);
		pbnDown.setEnabled(true);
		
		pbnBW.setEnabled(true);
		pbnFastBW.setEnabled(true);
		pbnFW.setEnabled(true);
		pbnFastFW.setEnabled(true);

		acoAuto.setEnabled(true);
	}
	
}