package org.woped.qualanalysis.simulation;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;

import org.woped.qualanalysis.simulation.controller.ReferenceProvider;
import org.woped.qualanalysis.simulation.controller.TokenGameBarController;
import org.woped.qualanalysis.simulation.controller.TokenGameBarListener;
import org.woped.translations.Messages;
import java.io.File;

/**
 * Currently, this Views (SlimView and EyeView do only look well under Windows while you get the best
 * results on WinXP. But will be improved later
 * 
 * @author Tilmann Glaser
 *
 */

public class SlimGameBarVC extends JPanel{
	
	//Image
	private Image  background     = null;
	
	//Declaration of all Buttons
	private SlimToggleButton            ppbSteps                      = null;
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
	
	//Dimension
	private Dimension                d                             = null;
	
	//ViewMode
	private int                      ViewMode                      = 0;
	
	public SlimGameBarVC(TokenGameBarController tgbController, DefaultListModel Choice, int ViewMode)
	{
	    super();
	    this.ViewMode = ViewMode;
	    ChoiceContent = Choice;
	    this.tgbController = tgbController;
	    if(ViewMode == tgbController.SLIM_VIEW)
	    {
	    	d = new Dimension(580,110);
	    }
	    else
	    {
	    	d = new Dimension(140, 150);
	    }
	    this.setPreferredSize(d);
	    addPlaybackNavigation(ViewMode);
	}   
	 
	private JPanel addPlaybackNavigation(int ViewMode)
	{
	  if(ViewMode == tgbController.SLIM_VIEW)
	  {
		//Define Playback-Buttons
		pbnFastBW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.FastBackward"));
	    pbnBW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Backward"));
		pbnStop = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Stop"));
		pbnPlay = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Play"));
		pbnPause = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Pause"));
		pbnFW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Forward"));
		pbnFastFW = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.FastForward"));
		sbnExpertView = new SlimButton(Messages.getImageIcon("Tokengame.RemoteControl.Enlarge"));
		ppbSteps = new SlimToggleButton(Messages.getImageIcon("Tokengame.RemoteControl.Stepwise"));

		
		//Define Button-Size
		pbnFastBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnBW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnStop.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPlay.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnPause.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		pbnFastFW.setPreferredSize(new Dimension(xtXsize, xtYsize));
		sbnExpertView.setPreferredSize(new Dimension(xtXsize, xtYsize));
		ppbSteps.setPreferredSize(new Dimension(xtXsize, xtYsize));
		
		//Define Playback's ToolTips
		pbnFastBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastBackward")); 
		pbnBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Backward")); 
		pbnStop.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stop")); 
		pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Play")); 
		pbnPause.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Pause")); 
		pbnFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Forward")); 
		pbnFastFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.FastForward")); 
		sbnExpertView.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Enlarge")); 
		ppbSteps.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stepwise")); 
		
		//Define Button-Actions
		pbnBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_BACKWARD, tgbController));
		pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, tgbController));
		pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, tgbController));
		pbnPause.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PAUSE, tgbController));		
		pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, tgbController));
		pbnFastFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_FORWARD, tgbController));
		pbnFastBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_BACKWARD, tgbController));
		sbnExpertView.addActionListener(new TokenGameBarListener(TokenGameBarListener.PM_SAVE_VIEW, tgbController));
		ppbSteps.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHANGE_PLAYMODE, tgbController));
		
		//Create Playback&Navigation-Panel and add Buttons
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();	

		gbc.gridx = 8;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,30,0,0);
		this.add(sbnExpertView, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,0,0,0);
		this.add(ppbSteps, gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,30,0,3);
		this.add(pbnFastBW, gbc);


		gbc.gridx = 7;
		gbc.gridy = 1;
		gbc.insets = new Insets (0,3,0,3);
		this.add(pbnFastFW, gbc);

		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.insets = new Insets (5,3,0,3);
		this.add(pbnBW, gbc);

		gbc.gridx = 6;
		gbc.gridy = 1;
		this.add(pbnFW, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.insets = new Insets (10,3,0,3);
		this.add(pbnStop, gbc);

		gbc.gridx = 5;
		gbc.gridy = 1;
		this.add(pbnPause, gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.insets = new Insets (15,3,0,3);
		this.add(pbnPlay, gbc);
	  }
	  else //if EyeView-Mode
	  {
		//Define Playback-Buttons
		    pbnBW = new SlimButton(SlimButton.EYE_BACK);
			pbnStop = new SlimButton(SlimButton.EYE_STOP);
			pbnPlay = new SlimButton(SlimButton.EYE_PLAY);
			pbnFW = new SlimButton(SlimButton.EYE_FORWARD);
			sbnExpertView = new SlimButton(SlimButton.EYE_EXPERT);

			
			//Define Button-Size
			pbnBW.setPreferredSize(new Dimension(32, 68));
			pbnStop.setPreferredSize(new Dimension(68, 29));
			pbnPlay.setPreferredSize(new Dimension(62,62));
			pbnFW.setPreferredSize(new Dimension(27, 68));
			sbnExpertView.setPreferredSize(new Dimension(68, 28));
			
			//Define Playback's ToolTips
			pbnBW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Backward")); 
			pbnStop.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stop")); 
			pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Play")); 
			pbnFW.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Forward")); 
			sbnExpertView.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Enlarge")); 
			
			//Define Button-Actions
			pbnBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_BACKWARD, tgbController));
			pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, tgbController));
			pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, tgbController));
			pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, tgbController));
			sbnExpertView.addActionListener(new TokenGameBarListener(TokenGameBarListener.PM_SAVE_VIEW, tgbController));
			
			this.setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();	

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 3;
			gbc.insets = new Insets (9,5,0,0);
			gbc.ipadx = 0;
			gbc.ipady = 0;			
			this.add(sbnExpertView, gbc);
						
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.insets = new Insets (0,-2,0,0);
			this.add(pbnBW, gbc);

			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.insets = new Insets (0,-1,0,0);
			this.add(pbnFW, gbc);
			
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 3;
			gbc.insets = new Insets (-1,4,0,0);
			this.add(pbnStop, gbc);

	
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.ipadx = 0;
			gbc.ipady = 0;
			gbc.insets = new Insets (-5,0,0,2);
			this.add(pbnPlay, gbc);

	  }
		return this;
	  
	}
	
	public Point getButtonCoords(boolean BackWard)
	{
	  if(tgbController.getViewMode() != tgbController.EXPERT_VIEW)
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
	  return null;
	}
	
	public void showChoice()
	{
		if((ppbSteps != null) && (ppbSteps.isSelected()))
		{
			return;
		}
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
			SlimChoiceBox.setAlwaysOnTop(true);
		}
		updateList(ChoiceContent, getButtonCoords(false));
	}

	public void setChoiceListInvisible()
	{
		if(SlimChoiceBox != null)
		{
			SlimChoiceBox.setVisible(false);	
		}
	}
	
	public void updateList(DefaultListModel ChoiceContent, Point buttonlocation)
	{
	 if(buttonlocation != null)
	 {
		if(ChoiceContent.size() > 0)
		{
			ListSizeY = ChoiceContent.size() * 15; 
		}
		SlimChoiceList.setSize(ListSizeX, ListSizeY);
		SlimChoiceBox.setSize(ListSizeX+4,ListSizeY+8);
		SlimChoiceBox.setLocation(buttonlocation.x,buttonlocation.y-(ListSizeY+18));
	 }
	}
	
	
	
	public int getSelectedChoiceID()
	{
		if(SlimChoiceList != null)
		{
			return SlimChoiceList.getSelectedIndex();
		}
		else
		{
			return -1;
		}
	}

	public void setPlaymodeSelected(boolean selected)
	{
		ppbSteps.setSelected(selected);
		if(selected)
		{
		    ppbSteps.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.Playback"));
		}
		else
		{
		    ppbSteps.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.Stepwise"));
		}
	}
	
	protected void paintComponent(Graphics g) 
	{
	  if(ViewMode == tgbController.SLIM_VIEW)
	  {
	    background = Messages.getImageSource("Tokengame.SlimView.Panel");
		g.drawImage (background, 0, 0, this);
		this.ui.paint(g, this);
	  }  
	}
 
	
	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g)
	{
	  return; 
	}
}


