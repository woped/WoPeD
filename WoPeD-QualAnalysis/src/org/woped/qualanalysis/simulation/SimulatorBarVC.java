/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.qualanalysis.simulation;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JMenuItem;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.qualanalysis.reachabilitygraph.gui.DropDownButton;
import org.woped.qualanalysis.simulation.controller.TokenGameBarController;
import org.woped.qualanalysis.simulation.controller.TokenGameBarListener;
import org.woped.translations.Messages;


/**
 * This toolbar makes use of JToolBar. Creates the main
 * not-dockable toolbar that is supposed to look vaguely like one that might come
 * with a Web browser and is able to cover the whole simulation of the TokenGame.
 * Makes use of ToolBarButton, a small extension of JButton that shrinks the margins
 * around the icon and puts text label, if any, below the icon. 
 * 
 * @author M. Beiser
 */

@SuppressWarnings("serial")
public class SimulatorBarVC extends JToolBar implements IViewController, MouseListener
{
    // ViewControll
    private Vector<IViewListener>             viewListener         = new Vector<IViewListener>(1, 3);
    private String             id                   = null;
    public static final String ID_PREFIX            = "SIMULATORBAR_VC_";

    static final Insets        margins              = new Insets(0, 0, 0, 0);

     
  //Other Variables
	private int                      stXsize                       = 20;
	private int                      stYsize                       = 20;
	private boolean                  tokenGameButtonOnStage        = false;
	
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
	private JToggleButton            pbnRecord                     = null;
	private JToggleButton            acoAuto                       = null;
	private AbstractButton     		 tokenGameButton    		   = null;
	
	//ViewChoice-Box
	private JButton                  viewChooser                   = null;
	
	// Listener for the Buttons
	private JMenuItem jmiExpertView, jmiSlimView, jmiEyeView       = null;
	private TB_Listener				listener			           = null;
	
	//SlimChoice-Box
	private JDialog                  SlimChoiceBox                 = null;
	private JList                    SlimChoiceList                = null;
	private int                      ListSizeX                     = 170;
	private int                      ListSizeY                     = 200;
	private JPanel                   SlimChoicePanel               = null;
	private boolean                  isMouseOver                   = false;
	
	private DefaultListModel         acoChoiceItems                = null;
	private TokenGameBarController   tgbController                 = null;    
	public SimulatorBarVC(TokenGameBarController tgbController, DefaultListModel acoChoiceItems, DefaultListModel ahxHistoryContent)
    {
        this.addMouseListener(this);
        this.setFloatable(false);
        this.acoChoiceItems = acoChoiceItems;
        setBorder(BorderFactory.createEtchedBorder());
        setRollover(true);
        this.tgbController = tgbController;
        addPlaybackNavigation();
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        addPropertiesPanel();
        add(getAutoChoiceButton());
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        addView();
        addSeparator();
        addSeparator(new Dimension(360, 0));
        addSeparator();
        addAnalysisButtons();
        
        this.disablePlayButtons();
    }        

    private void addPropertiesPanel()
	{
		//Define Buttons
	    ppbSteps = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Stepwise"));
	    ppbPlay  = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Playback"));
		ppbDelay = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.Properties"));
		
		//Define Button-Size
		ppbSteps.setPreferredSize(new Dimension(stXsize, stYsize));
		ppbPlay.setPreferredSize(new Dimension(stXsize, stYsize));
		ppbDelay.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define Button's ToolTips
		ppbSteps.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Stepwise"));
		ppbPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Playback"));
		ppbDelay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Properties"));
		
		//Define Button's Actions
		ppbPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_PLAYBACK, tgbController));
		ppbSteps.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_STEPWISE, tgbController));
		ppbDelay.addActionListener(new TokenGameBarListener(TokenGameBarListener.OPEN_PLAYBACK_MANAGER, tgbController));
		
	    //Disbale StepButton on Default
		disableStepwiseButton();
		
		add(ppbSteps);
		add(ppbPlay);
		add(ppbDelay);
	}
    
  // Buttons for remoteControl --> AutomaticSteps
    
    /**
	 * The Playback-Buttons 
	 */
	private void addPlaybackNavigation()
	{
		//Define Navigation-Buttons
		pbnUp = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.SubprocessStepOut"));
		pbnDown = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.SubprocessStepIn"));
		
		//Define Button-Size
		pbnUp.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnDown.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define Navigation's ToolTips
		pbnUp.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.SubprocessStepOut")); 
		pbnDown.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.SubprocessStepIn")); 
		pbnDown.addMouseListener(this);
		
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
		pbnFastBW.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnBW.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnStop.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnPlay.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnPause.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnFW.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnFastFW.setPreferredSize(new Dimension(stXsize, stYsize));
		
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
		pbnBW.addMouseListener(this);
		pbnStop.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_STOP, tgbController));
		pbnStop.addMouseListener(this);
		pbnPlay.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PLAY, tgbController));
		pbnFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FORWARD, tgbController));
		pbnFW.addMouseListener(this);
		pbnFastFW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_FORWARD, tgbController));
		pbnFastFW.addMouseListener(this);
		pbnFastBW.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_FAST_BACKWARD, tgbController));
		pbnFastBW.addMouseListener(this);
		pbnPause.addActionListener(new TokenGameBarListener(TokenGameBarListener.CLICK_PAUSE, tgbController));			
	
		add(pbnFastBW);
		add(pbnBW);
		add(pbnDown);
		add(pbnPlay);
		add(pbnUp);
		add(pbnPause);
		add(pbnStop);
		add(pbnFW);
		add(pbnFastFW);
		
	}

	/**
	 * The AnalysisButtons for RG,Woflan, etc.. 
	 */
	public void addAnalysisButtons()
	{
		tokenGameButton = new JToggleButton(Messages.getImageIcon("Tokengame.RemoteControl.stopTG"));
		tokenGameButton.setPreferredSize(new Dimension(stXsize, stYsize));
		tokenGameButton.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.stopTG"));
		tokenGameButton.addActionListener(new TokenGameBarListener(TokenGameBarListener.STOP_TG, tgbController));
		
		//Add Buttons
		if(!tokenGameButtonOnStage){
			add(tokenGameButton);
		}
		// control-variable for tokenGameButton
		tokenGameButtonOnStage = true;
		
//		add(refer.getUIReference().getToolBar().getWoflanButton());
//		add(refer.getUIReference().getToolBar().getAnalysisButton());
//		add(refer.getUIReference().getToolBar().getMetricButton());
//		add(refer.getUIReference().getToolBar().getQuantCapButton());
//		add(refer.getUIReference().getToolBar().getQuantSimButton());
//		add(refer.getUIReference().getToolBar().getReachabilityGraphButton());
//		add(refer.getUIReference().getToolBar().getColoringButton());
		//Remove them from standard ToolBar
//		refer.getUIReference().getToolBar().removeAnalysisButtons();
	}

    public JToggleButton getAutoChoiceButton()
    {
    	if (acoAuto == null)
    	{
    		acoAuto = new JToggleButton(Messages.getImageIcon("Tokengame.RemoteControl.AutoChoice"));
    		acoAuto.setPreferredSize(new Dimension(stXsize, stYsize));
    		acoAuto.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.AutoChoice"));
    		acoAuto.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_AUTO_CHOICE, tgbController));
    	}
    	return acoAuto;
    }
    
    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
    	return 0; 
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    @SuppressWarnings("unchecked")
	public final void fireViewEvent(AbstractViewEvent viewevent)
    {
        if (viewevent == null) return;
        Vector<IViewListener> vector;
        synchronized (viewListener)
        {
            vector = (Vector<IViewListener>) viewListener.clone();
        }
        if (vector == null) return;
        int i = vector.size();
        for (int j = 0; !viewevent.isConsumed() && j < i; j++)
        {
            IViewListener viewlistener = (IViewListener) vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    public void addView(){
        DropDownButton dp = new DropDownButton(Messages.getImageIcon("Tokengame.RemoteControl.chooseView"));
        dp.setFocusable(false);
        dp.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.chooseView"));
        
        listener = new TB_Listener();
        
        jmiExpertView = dp.getMenu().add(Messages.getTitle("Tokengame.RemoteControl.ExpertView"));
        jmiExpertView.addActionListener(listener);
        jmiExpertView.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.ExpertView"));
        jmiSlimView = dp.getMenu().add(Messages.getTitle("Tokengame.RemoteControl.SlimView"));
        jmiSlimView.addActionListener(listener);
        jmiSlimView.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.SlimView"));   
        jmiEyeView = dp.getMenu().add(Messages.getTitle("Tokengame.RemoteControl.EyeView"));
        jmiEyeView.addActionListener(listener);
        jmiEyeView.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.EyeView"));  
        
        add(dp);      
    }
    
    class TB_Listener implements ActionListener {

		public TB_Listener(){			
		}

		public void actionPerformed(ActionEvent e) {
			if		(e.getSource() == jmiExpertView){
				tgbController.getSelectedViewChoiceID(0);
			}else if	(e.getSource() == jmiSlimView){
				if(tgbController.getSelectedViewChoiceID(1) > -1)
				 {
					tgbController.getSelectedViewChoiceID(1);
				 }
			}else if	(e.getSource() == jmiEyeView){
				if(tgbController.getSelectedViewChoiceID(2) > -1)
				 {
					tgbController.getSelectedViewChoiceID(2);
				 }
			}	
		}
    }
    
 	/**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @return
     */
    public JButton getViewChooser()
    {
    	if (viewChooser == null)
    	{
    		viewChooser = new JButton(Messages.getImageIcon("Tokengame.RemoteControl.chooseView"));
    		viewChooser.setPreferredSize(new Dimension(stXsize, stYsize));
    		viewChooser.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.chooseView"));
    		viewChooser.addActionListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_VIEW, tgbController));
    	}
        return viewChooser;
    }
    
      
    /**
     * The next methods are going to create a dialog to select the next 
     * transition which has to be triggered. 
     * 
     * @return
     */
    public Point getButtonCoords(boolean BackWard) {
			if (!BackWard) {
				return pbnFW.getLocationOnScreen();
			} else {
				return pbnBW.getLocationOnScreen();
			}
	}
    

	private void createChoiceList()
	{
		SlimChoiceBox = new JDialog();
		SlimChoiceBox.setUndecorated(true);
		SlimChoiceList   = new JList(acoChoiceItems);
	    SlimChoiceList.setPreferredSize(new Dimension(ListSizeX, ListSizeY));			  
		SlimChoiceList.addMouseListener(new TokenGameBarListener(TokenGameBarListener.CHOOSE_TRANSITION, tgbController));
		
		//Define Panel
		SlimChoicePanel     = new JPanel();
	    SlimChoicePanel.add(SlimChoiceList);
		SlimChoiceBox.add(SlimChoicePanel);			
	}
	
	public void showChoice() {
		if ((ppbSteps != null) && (ppbSteps.isSelected())) {
			return;
		}
		if (getIsMouseOver()) {
			if (SlimChoiceBox == null) {
				createChoiceList();
				showChoice();
			}
			if (acoChoiceItems.size() > 0) {
				SlimChoiceList.setVisible(true);
				SlimChoicePanel.setVisible(true);
				SlimChoiceBox.setVisible(true);
				SlimChoiceBox.setAlwaysOnTop(true);
				updateList(acoChoiceItems, getButtonCoords(false));
			}
		}
	}

	public void setChoiceListInvisible()
	{
		if(SlimChoiceBox != null)
		{
			SlimChoiceBox.setVisible(false);
			SlimChoiceList.removeAll();
		}
	}
	
	public void updateList(DefaultListModel acoChoiceItems, Point buttonlocation)
	{
	 if(buttonlocation != null)
	 { 
		 if(acoChoiceItems.size() > 0)
		{
			ListSizeY = acoChoiceItems.size() * 15; 
		}
		SlimChoiceList.setSize(ListSizeX, ListSizeY);
		SlimChoiceBox.setSize(ListSizeX+4,ListSizeY+8);
		SlimChoiceBox.setLocation(buttonlocation.x,buttonlocation.y+20);
	 }
	}

    /*
     * getter and setter
    */ 
 	
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
	
	public boolean getIsMouseOver(){
		return isMouseOver;
	}
	
	public void setIsMouseOver(boolean state){
		isMouseOver = state;
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

     public boolean isAutoChoiceSelected()
     {
     	return acoAuto.isSelected();
     }
     
     public void setAutoChoiceSelected(boolean selected)
     {
     	acoAuto.setSelected(selected);
     }
     
     public void setRecordSelected(boolean isSelected)
     {
     	pbnRecord.setSelected(isSelected);
     }
     public void doRecordClick()
     {
     	pbnRecord.doClick();
     }
     
     public void doPlayClick(){
    	 pbnPlay.doClick();
     }
     
     public void setPlayIcon(boolean record)
     {
     	if(record)
     	{
     		pbnPlay.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.PlayRecord"));
     		pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.PlayRecord"));
 	
     	}
     	else
     	{
     		pbnPlay.setIcon(Messages.getImageIcon("Tokengame.RemoteControl.Play"));
     		pbnPlay.setToolTipText(Messages.getTitle("Tokengame.RemoteControl.Play"));
     	}
     }

     
 	/*
 	 * disable / enable Buttons - section 
 	 */
 	
     public void enablePlayButtons()
     {
     	pbnFW.setEnabled(true);
 		pbnFastFW.setEnabled(true);
 		pbnBW.setEnabled(true);
 		pbnFastBW.setEnabled(true);
     }
     
     public void disablePlayButtons()
     {
     	pbnFW.setEnabled(false);
 		pbnFastFW.setEnabled(false);
 		pbnBW.setEnabled(false);
 		pbnFastBW.setEnabled(false);
     }
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
 		ppbSteps.setEnabled(false);
 		
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
 		ppbSteps.setEnabled(true);
 		
 		ppbDelay.setEnabled(true);
 		pbnUp.setEnabled(true);
 		pbnDown.setEnabled(true);
 		
 		pbnBW.setEnabled(true);
 		pbnFastBW.setEnabled(true);
 		pbnFW.setEnabled(true);
 		pbnFastFW.setEnabled(true);

 		acoAuto.setEnabled(true);
 	}

	public void mouseClicked(MouseEvent e) {
		
		if((e.getSource() == pbnBW)||(e.getSource() == pbnFastBW)) {
			if(acoChoiceItems.size() == 0){
				setChoiceListInvisible();
			}
		}
		if(e.getSource() == pbnStop) {
			setChoiceListInvisible();
		}

	}

	public void mouseEntered(MouseEvent e) {

		if (acoChoiceItems.size() == 0) {
			setIsMouseOver(true);
		}
		// Enables the ForwardButtons, if there is no further element to choose in SlimChoiceBox
		/*if((!pbnFW.isEnabled())&&(acoChoiceItems.size() < 0)){
			enableForwardButtons();
		}*/
	}

	public void mouseExited(MouseEvent e) {

		if (acoChoiceItems.size() == 0) {
			setIsMouseOver(false);
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}