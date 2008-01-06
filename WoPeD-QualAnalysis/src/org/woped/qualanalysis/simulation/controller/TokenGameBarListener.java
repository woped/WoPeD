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


		
	//Action-Variables
	private ReferenceProvider         MainWindowReference     = null;
	private TokenGameHistoryManagerVC HistoryDialog           = null;
	
		
	//Variables
	private int                       ID            = 0;
	private TokenGameBarVC            RemoteControl = null;
	private String[]                  TestItems     = null;
	private static boolean            HistoryChanged= false;


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
	
	//Needed e.g. for HistoryManager Elements
	public TokenGameBarListener(int ButtonID)
	{
	  	ID = ButtonID;
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
		 case 1:
			 break;
		 case 2:
			 break;
		 case 3:
			 break;
		 case CLICK_FAST_BACKWARD:
			 RemoteControl.occurTransitionMulti(true);
			 break;
		 case CLICK_BACKWARD:
			 /*
			  * Will make a step back
			  */
			 RemoteControl.occurTransition(true);
			 break;
		 case CLICK_STOP:
			 /*
			  *  Reset TokenGame to Startposition and Enable PlayButton
			  */
		       stopAction();
		       RemoteControl.enablePlayButton();
			 break;
		 case CLICK_PLAY:
			 /*
			  *  Start "TokenGame" and disable Editor. Disable Play Button to prevent multiple TokenGame instances
			  */
			 playbackActions();
			 RemoteControl.disablePlayButton();
			 
			 //Cleanup needed to avoid double ENtries in the ChoiceBox
			 RemoteControl.cleanupTransition();
			 break;
		 case 8:
			 break;
		 case CLICK_FORWARD:
			 RemoteControl.occurTransition(false);
			 /*
			  *  (Not Now, but later)
			  *  TODO: 2.) For Automatic Playback just enable direction <forward> or <backward>
			  *            so that the net will be played in that direction
			  */
			 break;
		 case CLICK_FAST_FORWARD:
			 RemoteControl.occurTransitionMulti(false);
			 /*
			  * TODO: look at Method definition!
			  */
			 break;
		 case 11:
			 break;
		 case 12:
			 break;
		 case 13:
			 break;
		 case 14:
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
			 break;
		 case HM_OVERWRITE_SELECTED:
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
		    if (HistoryChanged)
			{
				HistoryDialog.enableSaveButton();
			}
			else
			{
				HistoryDialog.disableSaveButton();
			}
		    HistoryDialog.setVisible(true);
		}
		else
		{
			HistoryDialog.resetStates();
			if (HistoryChanged)
			{
				HistoryDialog.enableSaveButton();
			}
			else
			{
				HistoryDialog.disableSaveButton();
			}
			HistoryDialog.setVisible(true);
			HistoryChanged = false;
			
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
			HistoryChanged = false;
		}
	}
	
	private void loadExistingHistory()
	{
		HistoryDialog.openSelected();
	}
	
	private void playbackActions()
	{
		HistoryChanged = true;
		TestItems = TokenGameTest.createTestdata();
		RemoteControl.addHistoryData(TestItems);
		
		//Active TokenGame, disable DrawMode, checkNet and activate transition
		if(RemoteControl.getTokenGameController().isVisualTokenGame())
		{
			RemoteControl.getTokenGameController().enableVisualTokenGame();
			RemoteControl.getTokenGameController().TokenGameCheckNet();
		}
	}

	private void stopAction()
	{
		RemoteControl.getTokenGameController().TokenGameRetore();
		RemoteControl.clearChoiceBox();
	}

}