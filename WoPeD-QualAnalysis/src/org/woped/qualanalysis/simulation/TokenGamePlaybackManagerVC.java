package org.woped.qualanalysis.simulation;

import javax.swing.*;

import org.woped.core.gui.*;
import java.awt.*;

import org.woped.qualanalysis.simulation.controller.TokenGameBarController;
import org.woped.qualanalysis.simulation.controller.TokenGameBarListener;
import org.woped.translations.Messages; 

/**
 * This class specifies the Playback Manager Properties Dialog
 * User can modify Delaytime for Tokengame Autoplayback, set Occurtime for
 * Fast Forward and Fast Backward and activate Expertview
 * 
 */

public class TokenGamePlaybackManagerVC extends JDialog
{
	//Declaration of the Panel(s)
	private JPanel       jp_PlayBackManager     = null;
	private JPanel		 jp_occuretime			= null;
	private JPanel		 jp_delaytime			= null;
	
	//Declaration of the Buttons
	private JButton      jb_SaveProperties     = null;
	
	//Declaration of the Checkbox
	private JCheckBox    jcb_SwitchViewMode     = null;
	
	//Declaration of the Sliders
	private JSlider		 jsl_fastfwbwoption		= null;
	private JSlider	     jsl_delaytime			= null;
	
	//Declare Layout Items
	private GridBagConstraints hmgbc        = null;
	private int          ButtonSizeX        = 50;
	private int          ButtonSizeY        = 25;
	private int          ListSizeX          = 200;
	private int          ListSizeY          = 200;
	
	//Declare Reference-Variables
	private TokenGameBarController           RemoteControl    = null;     
	
	//Standard-Constructor
	public TokenGamePlaybackManagerVC(IUserInterface mediator, TokenGameBarController RC)
	{
		super((JFrame)mediator, Messages.getTitle("Tokengame.PlaybackManager"), true);
		RemoteControl = RC;
		this.add(addPropertyPanel());
		this.setLocationRelativeTo(null);
		this.setSize(300,300);
		this.setResizable(false);
	}
	
	//Panel Definition and Creation
	private JPanel addPropertyPanel()
	{
		//Build Buttons
		jb_SaveProperties    = new JButton(Messages.getTitle("Tokengame.PlaybackManager.SaveProperties"));
	    
	    //Set Button-Size
	    //jb_SaveProperties.setPreferredSize(new Dimension(ButtonSizeX, ButtonSizeY));
	    jb_SaveProperties.setSize(50,20);
	    
	    //Set ButtonActions
	    jb_SaveProperties.addActionListener(new TokenGameBarListener(TokenGameBarListener.PM_SAVE_PROPERTIES,RemoteControl,this));
	    
	    //Build Checkbox
		jcb_SwitchViewMode    = new JCheckBox(Messages.getTitle("Tokengame.PlaybackManager.ExpertMode"));
		jcb_SwitchViewMode.setSelected(RemoteControl.getExpertViewMode());
		
	    //Build Sliders
	    jsl_fastfwbwoption 	= new JSlider();
		jsl_delaytime		= new JSlider();
		
		//Set Slidersproperties
		jsl_fastfwbwoption.setMinimum(1);
		jsl_fastfwbwoption.setMaximum(10);
		jsl_fastfwbwoption.setLabelTable(jsl_fastfwbwoption.createStandardLabels(1));
		jsl_fastfwbwoption.setPaintLabels(true);
		jsl_fastfwbwoption.setPaintTicks(true);
		jsl_fastfwbwoption.setSnapToTicks(true);
		jsl_fastfwbwoption.setMajorTickSpacing(1);
		jsl_fastfwbwoption.setValue(RemoteControl.getOccurTimes());

		jsl_delaytime.setMinimum(1);
		jsl_delaytime.setMaximum(10);
		jsl_delaytime.setLabelTable(jsl_delaytime.createStandardLabels(1));
		jsl_delaytime.setPaintLabels(true);
		jsl_delaytime.setPaintTicks(true);
		jsl_delaytime.setSnapToTicks(true);
		jsl_delaytime.setMajorTickSpacing(1);
		jsl_delaytime.setValue(RemoteControl.getDelaytime());

		//Define Panel
	    jp_PlayBackManager     = new JPanel();
	    jp_PlayBackManager.setLayout(new GridBagLayout());
	    
	    jp_occuretime 		   = new JPanel();
	    jp_occuretime.setLayout(new GridLayout());
	    
	    jp_delaytime		  = new JPanel();
	    jp_delaytime.setLayout(new GridLayout());
	    
	    //Set Layout
        hmgbc              = new GridBagConstraints();
		
		//Add to Panels
        hmgbc.weightx = 0;
        hmgbc.weighty = 0;
        hmgbc.gridx = 0;
        hmgbc.gridy = 0;
        hmgbc.insets = new Insets(0, 0, 0, 0);
	    jp_PlayBackManager.add(jb_SaveProperties,hmgbc);
	    
	    hmgbc.weightx = 0;
        hmgbc.weighty = 1;
        hmgbc.gridx = 0;
        hmgbc.gridy = 1;
        hmgbc.anchor = GridBagConstraints.WEST;
        hmgbc.insets = new Insets(0, 0, 0, 0);
		jp_PlayBackManager.add(jcb_SwitchViewMode,hmgbc);
		
        hmgbc.weightx = 0;
        hmgbc.weighty = 0;
        hmgbc.gridx = 0;
        hmgbc.gridy = 2;
        hmgbc.fill = GridBagConstraints.BOTH;
        hmgbc.insets = new Insets(0, 0, 0, 0);
		jp_occuretime.setBorder(BorderFactory
                .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getTitle("Tokengame.PlaybackManager.Occurtimes")), BorderFactory.createEmptyBorder()));
		jp_occuretime.add(jsl_fastfwbwoption);
		jp_PlayBackManager.add(jp_occuretime,hmgbc);
		
		hmgbc.weightx = 1;
        hmgbc.weighty = 0;
        hmgbc.gridx = 0;
        hmgbc.gridy = 3;
        hmgbc.fill = GridBagConstraints.BOTH;
        hmgbc.insets = new Insets(0, 0, 0, 0);
        jp_delaytime.setBorder(BorderFactory
                .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getTitle("Tokengame.PlaybackManager.Delaytime")), BorderFactory.createEmptyBorder()));
		jp_delaytime.add(jsl_delaytime);
		jp_PlayBackManager.add(jp_delaytime,hmgbc);
		
		return jp_PlayBackManager;
	}
	
	public void saveProperties()
	{
		RemoteControl.setExpertViewMode(jcb_SwitchViewMode.isSelected());
		RemoteControl.setOccurTimes(jsl_fastfwbwoption.getValue());
		RemoteControl.setDelaytime(jsl_delaytime.getValue());
	}
	
	public TokenGameBarController getRemoteControl()
	{
		return RemoteControl;
	}
}
