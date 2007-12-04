package org.woped.gui.controller.vc;
import javax.swing.*;

import org.woped.editor.utilities.Messages;



import java.awt.*;
/**
 * 
 * @author Tilmann Glaser
 * This class specifies the remote control of the tokengame-simulator
 * Standard-Constructor is available
 * Currently there are still some UI-TODOs.
 * E.g. Alignment, Icons, Wordbook instead of Hardcoded Strings, Tooltips
 * It is just a version to allow other teammembers to call the remote-control 
 */
public class TokengameBarVC extends JInternalFrame {
	
	//Declaration of all JPanels
	private JPanel PropertiesPanel    = null;
	private JPanel Playback           = null;
	private JPanel NavigationPlayback = null;
	private JPanel AutoChoice         = null;
	private JPanel HistoryButtons     = null;
	private JPanel HistoryBox         = null;
	
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
	private String RCCaption          = "Tokengame RemoteControl";
	
	//Constructor(s)
	public TokengameBarVC()
	{
		super("Tokengame RemoteControl", false, true);
		this.setFrameIcon(Messages.getImageIcon("Document"));
	    this.setToolTipText("RemoteControl");//setAlignmentY(300);
		this.setSize(900,150);
		this.setVisible(true);
		//RC will disappear
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);  
		
		
		this.getContentPane();
		this.setLayout(new FlowLayout(1,5,10)); //1 = Align Center
		this.add(addPropertiesPanel());
		this.add(addPlaybackNavigation());
		this.add(addAutoChoice());
		this.add(addHistoryButtons());
		this.add(addHistoryBox());
	}
	
	

	/**
	 * this is the Left Bar of Buttons in the Remote Control
	 * @return
	 */
	
	private JPanel addPropertiesPanel()
	{
		//Define Buttons
	    ppbSteps = new JButton("Stp");
		ppbPlay  = new JButton("Ply");
		ppbDelay = new JButton("Dly");
		
		//Define Panel and Layout
		PropertiesPanel = new JPanel();
		PropertiesPanel.setLayout(new GridLayout(3,1,0,5));
		
		//add Buttons to the Panel
		PropertiesPanel.add(ppbSteps);
		PropertiesPanel.add(ppbPlay);
		PropertiesPanel.add(ppbDelay);
		
		return PropertiesPanel;	
	}
	
	private JPanel addPlaybackNavigation()
	{
		//Define Navigation-Buttons
		pbnUp = new JButton("Up");
		pbnDown = new JButton("Dn");
		
		//Define Playback-Buttons
		pbnFastBW = new JButton("FB");
	    pbnBW = new JButton("BW");
		pbnStop = new JButton("ST");
		pbnPlay = new JButton("PY");
		pbnPause = new JButton("PA");
		pbnFW = new JButton("FW");
		pbnFastFW = new JButton("FF");
		
		//Add Playback-Buttons to own Panel
		Playback = new JPanel();
		Playback.setLayout(new FlowLayout(1,5,5));
		Playback.add(pbnFastBW);
		Playback.add(pbnBW);
		Playback.add(pbnStop);
		Playback.add(pbnPlay);
		Playback.add(pbnPause);
		Playback.add(pbnFW);
		Playback.add(pbnFastFW);
		
		//Create Playback&Navigation-Panel
		NavigationPlayback = new JPanel();
		NavigationPlayback.setLayout(new GridLayout(3,1,0,5));
		NavigationPlayback.add(pbnUp);
		NavigationPlayback.add(Playback);
		NavigationPlayback.add(pbnDown);

		return NavigationPlayback;
	}
	
	private JPanel addAutoChoice()
	{
		//Define Elements
		acoAuto = new JButton("AT");
		acoChoice = new JList();
		acoChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acoScroll = new JScrollPane(acoChoice);
		acoScroll.setPreferredSize(new Dimension(50,60));
		acoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		acoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Add Elements to Panel
		AutoChoice = new JPanel();
		AutoChoice.add(acoAuto);
		AutoChoice.add(acoScroll);
		
		return AutoChoice;
	}
	
	private JPanel addHistoryButtons()
	{
		
		//Define Elements
		ahyJump = new JButton("JP");
		ahySave = new JButton("SV");
		ahyDelete = new JButton("DL");
		
		HistoryButtons = new JPanel();
		HistoryButtons.setLayout(new GridLayout(3,1,0,5));
		HistoryButtons.add(ahyJump);
		HistoryButtons.add(ahySave);
		HistoryButtons.add(ahyDelete);
		
		return HistoryButtons;
	}
	
	private JPanel addHistoryBox()
	{
		//Define List
		ahxChoice = new JList();
		ahxChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ahxScroll = new JScrollPane(ahxChoice);
		ahxScroll.setPreferredSize(new Dimension(50,60));
		ahxScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ahxScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    
		HistoryBox = new JPanel();
		HistoryBox.add(ahxScroll);
		return HistoryBox;
		
	}
}



