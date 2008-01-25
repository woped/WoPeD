package org.woped.qualanalysis.simulation;
import javax.swing.*;

import org.woped.translations.Messages;
import org.woped.qualanalysis.simulation.controller.*;

/**
 * This class specifies the remote control UI of the Tokengame-simulator
 * Currently no Actions can be performed by the Buttons except the close-"x"
 * Standard-Constructor is available
 * 
 * @author Tilmann Glaser
 * 
 */
public class SlimInternalFrame extends NewInternalFrame {
	


	
	private TokenGameBarController   tgbController                 = null;
	
	private SlimGameBarVC         SlimPanel                     = null;       
	
	//Constructor(s)
	public SlimInternalFrame(TokenGameBarController tgbController, DefaultListModel acoChoiceItems, DefaultListModel ahxHistoryContent)
	{
		super();
	    this.setToolTipText(Messages.getTitle("Tokengame.RemoteControl"));
		
		
		this.tgbController = tgbController;
        SlimPanel = new SlimGameBarVC(this.tgbController, acoChoiceItems);
		this.getContentPane().add(SlimPanel);	
		
	}	
	
	public SlimGameBarVC getSlimPanel()
	{
		return SlimPanel;
	}
}
