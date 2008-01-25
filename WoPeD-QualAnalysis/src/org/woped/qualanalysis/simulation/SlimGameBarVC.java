package org.woped.qualanalysis.simulation;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;

import org.woped.qualanalysis.simulation.controller.TokenGameBarController;
import org.woped.qualanalysis.simulation.controller.TokenGameBarListener;
import org.woped.qualanalysis.test.ReferenceProvider;
import org.woped.translations.Messages;
import java.io.File;



public class SlimGameBarVC extends JPanel{
	
	//Image
	private Image  background     = null;
	
	//Declaration of all Buttons
	private SlimButton                  ppbSteps                      = null;
	private SlimButton                  ppbPlay                       = null;
    private SlimButton                  pbnFastBW                     = null;
    private SlimButton                  pbnBW                         = null;
	private SlimButton                  pbnStop                       = null;
	private SlimButton                  pbnPlay                       = null;
	private SlimButton                  pbnPause                      = null;
	private SlimButton                  pbnFW                         = null;
	private SlimButton                  pbnFastFW                     = null;
	private SlimButton                  sbnExpertView                 = null;
	
	//Size-Ints
	private int                      xtXsize                       = 40;
	private int                      xtYsize                       = 40;
	
	//GridbagLayout
	private GridBagConstraints       gbc                           = null;
	
	//TokenGameBarController
	private TokenGameBarController   tgbController                 = null;
	
	//SlimChoice-Box
	private JDialog                  SlimChoiceBox                 = null;
	private JList                    SlimChoiceList                = null;
	private int                      ListSizeX                     = 170;
	private int                      ListSizeY                     = 200;
	private JPanel                   SlimChoicePanel               = null;
	private DefaultListModel         ChoiceContent                 = null;
	
	public SlimGameBarVC(TokenGameBarController tgbController, DefaultListModel Choice)
	{
	    super();
	    ChoiceContent = Choice;
	    this.tgbController = tgbController;
	    Dimension d = new Dimension(580,110);
	    this.setPreferredSize(d);
	    addPlaybackNavigation();
	}   
	 
	private JPanel addPlaybackNavigation()
	{
		//Define Playback-Buttons
		pbnFastBW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.FastBackward"));
	    pbnBW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Backward"));
		pbnStop = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Stop"));
		pbnPlay = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Play"));
		pbnPause = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Pause"));
		pbnFW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Forward"));
		pbnFastFW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.FastForward"));
		
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
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,3,0,3);
		this.add(pbnFastBW, gbc);


		gbc.gridx = 6;
		gbc.gridy = 1;
		this.add(pbnFastFW, gbc);

		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets (5,3,0,3);
		this.add(pbnBW, gbc);

		gbc.gridx = 5;
		gbc.gridy = 1;
		this.add(pbnFW, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.insets = new Insets (10,3,0,3);
		this.add(pbnStop, gbc);

		gbc.gridx = 4;
		gbc.gridy = 1;
		this.add(pbnPause, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.insets = new Insets (15,3,0,3);
		this.add(pbnPlay, gbc);
		
		return this;
	}
	
	public Point getButtonCoords(boolean BackWard)
	{
		if(!BackWard)
		{
			return pbnFW.getLocationOnScreen();
		}
		else
		{
			return pbnBW.getLocationOnScreen();
		}
	}
	
	public void showChoice()
	{
		if(SlimChoiceBox == null)
		{
			SlimChoiceBox = new JDialog();
			SlimChoiceBox.setUndecorated(true);

			
			SlimChoiceList   = new JList(ChoiceContent);
		    SlimChoiceList.setPreferredSize(new Dimension(ListSizeX, ListSizeY));		
		  
			SlimChoiceList.addMouseListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_TRANSITION, tgbController));
		    
			//Define Panel
			SlimChoicePanel     = new JPanel();
		    SlimChoicePanel.add(SlimChoiceList);
		    
			SlimChoiceBox.add(SlimChoicePanel);
			SlimChoiceBox.setAlwaysOnTop(true);
			SlimChoiceBox.setVisible(true);	
		}
		else
		{
			SlimChoiceBox.setVisible(true);
		}
		updateList(ChoiceContent, getButtonCoords(false));
	}

	public void setChoiceListInvisible()
	{
		SlimChoiceBox.setVisible(false);
	}
	
	public void updateList(DefaultListModel ChoiceContent, Point buttonlocation)
	{
		if(ChoiceContent.size() > 0)
		{
			ListSizeY = ChoiceContent.size() * 15; 
		}
		SlimChoiceList.setSize(ListSizeX, ListSizeY);
		SlimChoiceBox.setSize(ListSizeX+4,ListSizeY+8);
		SlimChoiceBox.setLocation(buttonlocation.x,buttonlocation.y-(ListSizeY+18));
	}
	
	
	
	public int getSelectedChoiceID()
	{
		return SlimChoiceList.getSelectedIndex();
	
	}
	
	protected void paintComponent(Graphics g) 
	{
	    background = Messages.getImageSource("Tokengame.SlimView.Panel");
		g.drawImage (background, 0, 0, this);
		this.ui.paint(g, this);
	}
 
	
	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g)
	{
	  return; 
	}
}


