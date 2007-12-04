package org.woped.gui.controller.vc;
import javax.swing.*;

import org.woped.editor.utilities.Messages;

import java.awt.*;

/**
 * This class specifies the remote control UI of the Tokengame-simulator
 * Currently no Actions can be performed by the Buttons except the close-"x"
 * Standard-Constructor is available
 * 
 * @author Tilmann Glaser
 * 
 */
public class TokengameBarVC extends JInternalFrame {
	
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
	
	//Other Variables
	private int    stXsize            = 50;
	private int    stYsize            = 25;
	private int    xtXsize            = 30;
	private int    xtYsize            = 25;
	private GridBagConstraints hgbc   = null;
	private GridBagConstraints gbc    = null;
	
	//Constructor(s)
	public TokengameBarVC()
	{
		super(Messages.getTitle("Tokengame.RemoteControl"), false, true);
		this.setFrameIcon(Messages.getImageIcon("Tokengame.RemoteControl"));
	    this.setToolTipText(Messages.getTitle("Tokengame.RemoteControl"));//setAlignmentY(300);
		this.setSize(910,140);
		this.reshape(910, 140, 0, 400);
		this.setVisible(true);
		//RC will disappear
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);  
		
		this.getContentPane();
		this.setLayout(new FlowLayout(1,5,10)); //1 = Align Center
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
		acoChoice = new JList();
		acoChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acoChoice.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.ChoiceList"));
		
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
		ahxChoice = new JList();
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
	
}



