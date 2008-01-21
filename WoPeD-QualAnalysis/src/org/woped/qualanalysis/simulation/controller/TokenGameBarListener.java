package org.woped.qualanalysis.simulation.controller;
import java.awt.event.*;


import org.woped.core.model.petrinet.PlaceModel;
import org.woped.qualanalysis.test.*;
import org.woped.qualanalysis.simulation.*;

import javax.swing.*;


import org.woped.translations.Messages;

import java.awt.*;

public class TokenGameBarListener implements ActionListener, MouseListener {
	
	//Constants
	//======================  
	//Playback configuration
	public final static int CHOOSE_STEPWISE        = 1;
	public final static int CHOOSE_PLAYBACK        = 2;
	public final static int OPEN_PLAYBACK_MANAGER  = 3;
	
	//Navigation Button
	public final static int CLICK_FAST_BACKWARD    = 4;
	public final static int CLICK_BACKWARD         = 5;
	public final static int CLICK_STOP             = 6;
	public final static int CLICK_PLAY             = 7;
	public final static int CLICK_PAUSE            = 8;
	public final static int CLICK_FORWARD          = 9;
	public final static int CLICK_FAST_FORWARD     = 10;
	
	//Process stepping
	public final static int CLICK_STEP_UP          = 11;
	public final static int CLICK_STEP_DOWN        = 12;
	
	//Auto Choices
	public final static int CHOOSE_AUTO_CHOICE     = 13;

	//History Management
	public final static int CHOOSE_JUMP_HERE       = 14;
	public final static int OPEN_HISTORY_MANAGER   = 15;
	public final static int CHOOSE_DELETE_CURRENT  = 16;
	
	//History Manager Buttons
	public final static int HM_SAVE_HISTORY        = 17;
	public final static int HM_DELETE_SELECTED     = 18;
	public final static int HM_OVERWRITE_SELECTED  = 19;
	public final static int HM_OPEN_SELECTED       = 20;
	
	//AutoChoice List
	public final static int CHOOSE_TRANSITION      = 21;
	
	//Record Simulation
	public final static int CHOOSE_RECORD          = 22;


		
	//Action-Variables
	private ReferenceProvider         MainWindowReference     = null;
	private TokenGameHistoryManagerVC HistoryDialog           = null;
	
		
	//Variables
	private int                       ID            = 0;
	private TokenGameBarVC            RemoteControl = null;


	public TokenGameBarListener(int ButtonID, TokenGameBarVC RC, TokenGameHistoryManagerVC ToGaHiMan)
	{
	  	ID = ButtonID;
	  	RemoteControl = RC;
	  	HistoryDialog = ToGaHiMan;
	}
	
	//Needed for RemoteControlElements
	public TokenGameBarListener(int ButtonID, TokenGameBarVC RC)
	{
	  	ID = ButtonID;
	  	RemoteControl = RC;
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
	    //Calls the method for the centralized Action-Handling
		actionRouter();	
	}

	public void mouseClicked(MouseEvent e)
	{
		//Calls the method for the centralized Action-Handling
        actionRouter();	
	}
	
	public void mouseEntered(MouseEvent e)
	{
	}
	
	public void mousePressed(MouseEvent e)
	{
	}
	
	public void mouseReleased(MouseEvent e)
	{
	}
	
	public void mouseExited(MouseEvent e)
	{
	}
	
	private void actionRouter()
	{
		switch(ID)
		{
		 case CHOOSE_STEPWISE:
			 if (RemoteControl.tokengameRunning())
			 {
				 RemoteControl.setAutoPlayback(false);
			 }
			 break;
		 case CHOOSE_PLAYBACK:
			 if (RemoteControl.tokengameRunning())
			 {
				RemoteControl.setAutoPlayback(true);
			 }
			 break;
		 case 3:
			 break;
		 case CLICK_FAST_BACKWARD:
			 if (RemoteControl.tokengameRunning())
			 {
			   RemoteControl.occurTransitionMulti(true);
			 }
			 break;
		 case CLICK_BACKWARD:
			 /*
			  * Will make a step back
			  */
			 if (RemoteControl.tokengameRunning())
			 {
				 if(RemoteControl.getAutoPlayBack())
				 {
					 RemoteControl.autoOccurAllTransitions(true);
				 }
				 else
				 {
					 RemoteControl.occurTransition(true);
				 }
			 }
			 break;
		 case CLICK_STOP:
			 /*
			  *  Reset TokenGame to Startposition and Enable PlayButton
			  */
			 if(RemoteControl.tokengameRunning())
			 {
		       stopAction();
		       if(RemoteControl.isRecordSelected())
		       {   
		         RemoteControl.createSaveableHistory();
		       }
		       RemoteControl.enablePlayButton();
		       RemoteControl.enableStepDown();
		       RemoteControl.enableRecordButton();
			 }
			 break;
		 case CLICK_PLAY:
			 /*
			  *  Start "TokenGame" and disable Editor. Disable Play Button to prevent multiple TokenGame instances
			  */
			 //Cleanup needed to avoid double ENtries in the ChoiceBox
			 RemoteControl.disableStepDown();
			 RemoteControl.disableRecordButton();
			 RemoteControl.disablePlayButton();
			 RemoteControl.cleanupTransition();
			 playbackActions();
			 break;
		 case 8:
			 break;
		 case CLICK_FORWARD:
			 if (RemoteControl.tokengameRunning())
			 {
				 if(RemoteControl.getAutoPlayBack())
				 {
					 RemoteControl.autoOccurAllTransitions(false);
				 }
				 else
				 {
					 RemoteControl.occurTransition(false);
				 }
			 }
			 /*
			  *  (Not Now, but later)
			  *  TODO: 2.) For Automatic Playback just enable direction <forward> or <backward>
			  *            so that the net will be played in that direction
			  */
			 break;
		 case CLICK_FAST_FORWARD:
			 if (RemoteControl.tokengameRunning())
			 {
			   RemoteControl.occurTransitionMulti(false);
			 }
			 /*
			  * TODO: look at Method definition!
			  */
			 break;
		 case 11:
			 break;
		 case CLICK_STEP_DOWN:
			 if (RemoteControl.tokengameRunning())
			 {
				RemoteControl.getTokenGameController().setStepIntoSubProcess(true);
				RemoteControl.occurTransition(false);
			 }
			  
			 break;
		 case 13:
			 break;
		 case CHOOSE_JUMP_HERE:
			 break;
		 case OPEN_HISTORY_MANAGER:
			 showHistoryManager();
			 break;
		 case CHOOSE_DELETE_CURRENT:
			 deleteCurrentHistory();
			 break;
		 case HM_SAVE_HISTORY:
			 saveRecordedHistory();
			 break;
		 case HM_DELETE_SELECTED:
			 if(HistoryDialog.getSelection() > -1)
			 {
				 RemoteControl.deleteHistory(HistoryDialog.getSelection());
				 HistoryDialog.removeLoadListItem(HistoryDialog.getSelection());
			 }
			 break;
		 case HM_OVERWRITE_SELECTED:
			 if(HistoryDialog.getSelection() == -1)
			 {
				 saveRecordedHistory();
			 }
			 else
			 {
				 RemoteControl.overwriteHistory(HistoryDialog.getSelection());
				 HistoryDialog.setVisible(false);
			 }
			 break;
		 case HM_OPEN_SELECTED:
			 loadExistingHistory();
			 break;
		
		 case CHOOSE_TRANSITION:
			 if(RemoteControl.getSelectedChoiceID() > -1)
			 {
				 RemoteControl.proceedTransitionChoice(RemoteControl.getSelectedChoiceID());
			 }
			 break;
		 case CHOOSE_RECORD:
			 if(RemoteControl.isRecordSelected())
			 {
			   RemoteControl.clearHistoryData();
			 }
		     break;
		 default:
			 break;
		}

	}
	
	
	private void showHistoryManager()
	{
		//Calling the Dialog-Box of the HistoryManager
		//Gets Reference out of Help-Class: ReferenceProvider
		if(HistoryDialog == null)
		{	
		    MainWindowReference = new ReferenceProvider();
		    HistoryDialog = new TokenGameHistoryManagerVC(MainWindowReference.getUIReference(), RemoteControl);
		    for(int i = 0; i < RemoteControl.getPetriNet().getSimulations().size(); i++)
		    {
		    	HistoryDialog.addLoadListItem(RemoteControl.getPetriNet().getSimulations().get(i).getName());
		    }
		    	
		    HistoryDialog.setVisible(true);
		    
		}
		else
		{
			HistoryDialog.resetStates();
			HistoryDialog.setVisible(true);
		}
	}
	
	private void deleteCurrentHistory()
	{
		RemoteControl.clearHistoryData();
	}
	
	private void saveRecordedHistory()
	{
		if(HistoryDialog.checkSaveName())
		{
			HistoryDialog.saveNewHistory();
		}
	}
	
	private void loadExistingHistory()
	{
		HistoryDialog.openSelected();
	}
	
	private void playbackActions()
	{
		//Active TokenGame, disable DrawMode, checkNet and activate transition
		if(RemoteControl.getTokenGameController().isVisualTokenGame())
		{
			RemoteControl.getTokenGameController().enableVisualTokenGame();
			RemoteControl.getTokenGameController().TokenGameCheckNet();
			if (RemoteControl.playbackRunning())
			{
				  RemoteControl.startHistoryPlayback();
			}
			else
			{
				RemoteControl.clearHistoryData();
			}
		}
	}

	private void stopAction()
	{
		RemoteControl.setEndOfNet(true);
		RemoteControl.getTokenGameController().TokenGameRetore();
		RemoteControl.clearChoiceBox();
	}

}