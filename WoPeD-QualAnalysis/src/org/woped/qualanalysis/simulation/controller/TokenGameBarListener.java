package org.woped.qualanalysis.simulation.controller;
import java.awt.event.*;

import org.woped.qualanalysis.simulation.*;


import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;




public class TokenGameBarListener implements ActionListener, MouseListener, ChangeListener, ListSelectionListener, CaretListener {
	
	//Constants
	//======================  
	//Playback configuration
	public final static int           CHOOSE_STEPWISE        = 1;
	public final static int           CHOOSE_PLAYBACK        = 2;
	public final static int           OPEN_PLAYBACK_MANAGER  = 3;
	public final static int           CHANGE_PLAYMODE        = 14; //This is used for the SlimView, only
	
	//Navigation Button
	public final static int           CLICK_FAST_BACKWARD    = 4;
	public final static int           CLICK_BACKWARD         = 5;
	public final static int           CLICK_STOP             = 6;
	public final static int           CLICK_PLAY             = 7;
	public final static int           CLICK_PAUSE            = 8;
	public final static int           CLICK_FORWARD          = 9;
	public final static int           CLICK_FAST_FORWARD     = 10;
	
	//Process stepping
	public final static int           CLICK_STEP_UP          = 11;
	public final static int           CLICK_STEP_DOWN        = 12;
	
	//Auto Choices
	public final static int           CHOOSE_AUTO_CHOICE     = 13;

	//History Management
	public final static int           OPEN_HISTORY_MANAGER   = 15;
	public final static int           CHOOSE_DELETE_CURRENT  = 16;
	
	//History Manager Buttons
	public final static int           HM_SAVE_HISTORY        = 17;
	public final static int           HM_DELETE_SELECTED     = 18;
	public final static int           HM_OVERWRITE_SELECTED  = 19;
	public final static int           HM_OPEN_SELECTED       = 20;
	
	//History Manager Table
	public final static int			  HM_ELEMENT_SELECTED	= 21;
	
	//History Manager NameField
	public final static int			  HM_NAME_CHANGED		= 22;
	
	//AutoChoice List
	public final static int           CHOOSE_TRANSITION      = 30;
	
	//Record Simulation
	public final static int           CHOOSE_RECORD          = 31;

	//Playback Manager Buttons
	public final static int			  PM_SAVE_VIEW     		 = 32;
	public final static int			  PM_FASTFWBW			 = 33;
	public final static int			  PM_DELAYTIME			 = 34;
		
	//Action-Variables
	private ReferenceProvider         MainWindowReference    = null;
	private TokenGameHistoryManagerVC HistoryDialog          = null;
	
		
	//Variables
	private int                       ID                     = 0;
	private TokenGameBarController    RemoteControl          = null;
	private TokenGamePlaybackManagerVC	  PlaybackDialog		  = null;


	public TokenGameBarListener(int ElementID, TokenGameBarController RC, TokenGameHistoryManagerVC ToGaHiMan)
	{
	  	ID = ElementID;
	  	RemoteControl = RC;
	  	HistoryDialog = ToGaHiMan;
	}
	
	public TokenGameBarListener(int ElementID, TokenGameBarController RC, TokenGamePlaybackManagerVC ToGaPM)
	{
	  	ID = ElementID;
	  	RemoteControl = RC;
	  	PlaybackDialog = ToGaPM;
	}
	
	//Needed for RemoteControlElements
	public TokenGameBarListener(int ElementID, TokenGameBarController RC)
	{
	  	ID = ElementID;
	  	RemoteControl = RC;
	}
	
	
	
	
	private void actionRouter()
	{
		switch(ID)
		{
		 case CHOOSE_STEPWISE:
			 RemoteControl.setAutoPlayback(false);
			 break;
		 case CHOOSE_PLAYBACK:
			 RemoteControl.setAutoPlayback(true);
			 break;
		 case OPEN_PLAYBACK_MANAGER:
			 showPlaybackManager();
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
			 if (RemoteControl.getViewMode() > 0)
			 {
				 if(RemoteControl.getTokenGameController().getThisEditor().isSubprocessEditor())
				 {
					 RemoteControl.changeTokenGameReference(null, true); 
				 }
			 }
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
		       RemoteControl.enableStepDown(null);
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
		 case CLICK_PAUSE:
			 RemoteControl.setEndOfAutoPlay(true);
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
			 break;
		 case CLICK_FAST_FORWARD:
			 if (RemoteControl.tokengameRunning())
			 {
			   RemoteControl.occurTransitionMulti(false);
			 }
			 break;
		 case CLICK_STEP_UP:
			 if (RemoteControl.tokengameRunning())
			 {
				RemoteControl.changeTokenGameReference(null, true);
			 }
			 break;
		 case CLICK_STEP_DOWN:
			 if (RemoteControl.tokengameRunning())
			 {
				RemoteControl.getTokenGameController().setStepIntoSubProcess(true);
				RemoteControl.occurTransition(false);
			 }
			 break;
		 case CHOOSE_AUTO_CHOICE:
			 if (RemoteControl.tokengameRunning())
			 {
				RemoteControl.switchAutoChoice();
			 }
			 break;
		 case CHANGE_PLAYMODE:
			 /*
			  * Switch Playmode to stepwise
			  */
			 if(RemoteControl.getAutoPlayBack())
			 {
				RemoteControl.setAutoPlayback(false); 
				break; 
			 }
			 /*
			  * Switch playmode to autoplay
			  */
			 else
			 {
			    RemoteControl.setAutoPlayback(true);
			 }
			 break;
		 case OPEN_HISTORY_MANAGER:
			 showHistoryManager();
			 break;
		 case CHOOSE_DELETE_CURRENT:
			 deleteCurrentHistory();
			 break;
		 case HM_ELEMENT_SELECTED:
			 HistoryDialog.initializeElementStatus();
			 break;
		 case HM_NAME_CHANGED:
			 HistoryDialog.initializeElementStatus();
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
			   RemoteControl.setPlayIcon(true);	 
			   RemoteControl.clearHistoryData();
			 }
			 else
			 {
				RemoteControl.setPlayIcon(false); 
			 }
		     break;
		 case PM_SAVE_VIEW:
			 if(RemoteControl.getViewMode() == RemoteControl.EXPERT_VIEW)
			 {
			   PlaybackDialog.savePMView();
			   break;
			 }
			 if((RemoteControl.getViewMode() == RemoteControl.SLIM_VIEW) || (RemoteControl.getViewMode() == RemoteControl.EYE_VIEW))
			 {
			   if(RemoteControl.tokengameRunning())
			   {
				   RemoteControl.getExpertView().disableRecordButton();
			   }
			   RemoteControl.setViewMode(RemoteControl.EXPERT_VIEW);
			   break;
			 }
			 break;
		 case PM_FASTFWBW:
			 PlaybackDialog.savePropertyOccurtime();
			 break;
		 case PM_DELAYTIME:
			 PlaybackDialog.savePropertyDelaytime();
			 break;
		 default:
			 break;
		}

	}
	
	private void showPlaybackManager()
	{
		 MainWindowReference = new ReferenceProvider();
		 PlaybackDialog= new TokenGamePlaybackManagerVC(MainWindowReference.getUIReference(), RemoteControl);
		 PlaybackDialog.setVisible(true);
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
		    	Object[] item = {RemoteControl.getPetriNet().getSimulations().get(i).getName(),RemoteControl.getPetriNet().getSimulations().get(i).getSavedDate()};
		    	HistoryDialog.addLoadListItem(item);
		    }
		    	
		    HistoryDialog.initializeElementStatus();
		    HistoryDialog.setVisible(true);
		    
		}
		else
		{
			HistoryDialog.resetStates();
			HistoryDialog.initializeElementStatus();
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
			//RemoteControl.getTokenGameController().enableVisualTokenGame();
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
		RemoteControl.getTokenGameController().getThisEditor().setTokenGameEnabled(true);
	}

	private void stopAction()
	{
		RemoteControl.setEndOfAutoPlay(true);
		while(RemoteControl.getTokenGameController().getThisEditor().isSubprocessEditor())
		{
			RemoteControl.changeTokenGameReference(null, true);
		}
		RemoteControl.getTokenGameController().TokenGameRetore();
		RemoteControl.clearChoiceBox();
		RemoteControl.setStepIn(false);
		RemoteControl.getSlimView().getSlimPanel().setChoiceListInvisible();
		RemoteControl.getTokenGameController().getThisEditor().setTokenGameEnabled(false);
	}

	
	/*
	 * Action Events
	 */
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

	public void stateChanged(ChangeEvent arg0) {
		actionRouter();
		
	}

	public void valueChanged(ListSelectionEvent e) {
		actionRouter();
	}

	public void caretUpdate(CaretEvent e) {
		actionRouter();
	}
}