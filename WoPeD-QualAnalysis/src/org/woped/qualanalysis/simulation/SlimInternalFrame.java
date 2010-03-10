package org.woped.qualanalysis.simulation;
import javax.swing.*;

import org.woped.translations.Messages;
import org.woped.qualanalysis.simulation.controller.*;

/**
 * This class specifies the remote control UI of the Tokengame-simulator
 * This SlimController looks nice on Microsoft OS usage, but on Apple, currently there is still a GUI prolem
 * TODO: Fix GUI-Problems on Apple
 * 
 * @author Tilmann Glaser
 * 
 */
public class SlimInternalFrame extends NewInternalFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TokenGameBarController   tgbController                 = null;
	private SlimGameBarVC            SlimPanel                     = null;       
	
	//Constructor(s)
	public SlimInternalFrame(TokenGameBarController tgbController, DefaultListModel acoChoiceItems, DefaultListModel ahxHistoryContent, int ViewMode)
	{
		super(ViewMode);
	    this.setToolTipText(Messages.getTitle("Tokengame.RemoteControl"));
		
		this.tgbController = tgbController;
        
		SlimPanel = new SlimGameBarVC(this.tgbController, acoChoiceItems, ViewMode);
		this.getContentPane().add(SlimPanel);	
		
	}	
	
	public SlimGameBarVC getSlimPanel()
	{
		return SlimPanel;
	}
}
