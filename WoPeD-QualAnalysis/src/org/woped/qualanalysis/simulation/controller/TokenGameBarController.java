package org.woped.qualanalysis.simulation.controller;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Dimension2D;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.TransitionModel;
import  org.woped.qualanalysis.simulation.*;
import org.woped.qualanalysis.test.ReferenceProvider;

import com.sun.java.swing.plaf.windows.WindowsBorders.InternalFrameLineBorder;

public class TokenGameBarController implements Runnable {
	
	private TokenGameBarVC           ExpertView                    = null;
    private ReferenceProvider        desktop                       = null;
	private TokenGameController      tgController                  = null;
	private PetriNetModelProcessor   PetriNet                      = null;
	//Linked Lists 
	private LinkedList               followingActivatedTransitions = null;
	private LinkedList               ProcessEditors                = null;
	
	//Occurring Transitions
	private TransitionModel          TransitionToOccur             = null;
	private TransitionModel          BackwardTransitionToOccur     = null;
	private TransitionModel          helpTransition                = null;
	
	//SimulationModels
	private SimulationModel          SaveableSimulation            = null;
	
	//DefaultListModels
	private DefaultListModel         acoChoiceItems                = null;
	private DefaultListModel         ahxHistoryContent             = null;
	
	//Vectors
	private Vector <TransitionModel> HistoryVector                 = null;
	
	//Booleans
	private boolean                  autoPlayBack                  = false;
	private boolean                  backward                      = false;
	private boolean                  newHistory                    = false;
	private boolean                  endofnet                      = false;
	
	//Playback Properties
	private int						 occurtimes					   = 3;
	private int						 delaytime					   = 1;
	private int					     viewmode					   = 0;
	
	//Integers
	private int                      HistoryIndex                  = 0; 

	//SlimFrames
	private SlimInternalFrame        SlimView                      = null;
	
	public TokenGameBarController(TokenGameController tgController, PetriNetModelProcessor PetriNet)
	{
		this.PetriNet     = PetriNet;
		this.tgController = tgController;
		
		acoChoiceItems = new DefaultListModel();
		ahxHistoryContent = new DefaultListModel();		
		
		addControlElements();
	}
	
	/**
	 * The ExpertView and SlimView Objects will either been created or shown, depending on the case 
	 * if they already exist or not
	 */
	public void addControlElements()
	{
	  if (ExpertView != null)
	  {
        //Add Default Mediator Pattern here instead of the DesktopReference-thing
		desktop.getDesktopReference().add(ExpertView);
        ExpertView.setVisible(true);
	  }
      else
      {
    	ExpertView = new TokenGameBarVC(this, acoChoiceItems, ahxHistoryContent);
        desktop    = new ReferenceProvider();
        Point p    = desktop.getDesktopReference().getLocation();
        p.setLocation(desktop.getDesktopReference().getLocation().getX(), (int)desktop.getDesktopReference().getHeight()-ExpertView.getHeight());
        desktop.getDesktopReference().add(ExpertView).setLocation(p);
        ExpertView.moveToFront();
        
        SlimView = new SlimInternalFrame(this, acoChoiceItems, ahxHistoryContent);
        desktop.getDesktopReference().add(SlimView);
       /*
        * Not everybody should see the new view right now, therefore commented it out...
        */
        SlimView.moveToFront();
        
        switch(viewmode)
		{
	    	case 0:
	    		ExpertView.setVisible(true);
	        	SlimView.setVisible(false);
	    		break;
	    	case 1:
	    		ExpertView.setVisible(false);
	        	SlimView.setVisible(true);
	    		break;
	    	case 3:
	    		break;
	    	default:
	    		break;
		}
      }
	}
	
	/**
	 * The TokenGameBar will be removed from the desktop 
	 */
	public void removeControlElements()
	{
	  if((ExpertView != null) && (desktop != null))
      {
		ExpertView.setVisible(false);
		SlimView.setVisible(false);
       	desktop.getDesktopReference().remove(ExpertView);
       	desktop.getDesktopReference().remove(SlimView);
       	ExpertView = null;
       	SlimView = null;
      }
	}
	
	/*
	 * Actions performed "in" the ExpertView / SlimView
	 */

	/*
	 * ********************************************************************************************
	 * Occurence and Depending Actions
	 * ********************************************************************************************
	 */
	
	/**
	 * This method let the active transition occur (currently only for sequences, as soon
	 * as two transitions are active, the method cannot occur so far)
	 */
	public void occurTransition(boolean BackWard)
	{

		/*
		 * Backward is done with the 
		 */
	  if( BackWard )
	  {
		 previousItem();
		if(BackwardTransitionToOccur != null)
		{
		  while(BackwardTransitionToOccur.getId().contains("sub") && (BackwardTransitionToOccur.getId().length() > 6))
		  {
			  previousItem();
			  while(BackwardTransitionToOccur.getId().contains("a"))
			  {
				previousItem();  
			  }
		  }
		  TransitionToOccur = BackwardTransitionToOccur;
		  tgController.occurTransitionbyTokenGameBarVC(TransitionToOccur, BackWard);
		}
	  } 
	  else
	  {
		  //AFAIK needed to make automatic backward stepping
		  BackwardTransitionToOccur = TransitionToOccur;
		  if(playbackRunning())
		  {
			  if((HistoryIndex < HistoryVector.size()) && (HistoryIndex >= 0))
			  {
			    TransitionToOccur = (TransitionModel)HistoryVector.get(HistoryIndex++);
			    
			    /*
			     * If History steps into a SubProcess, do so as well 
			     */
			    if((!TransitionToOccur.getId().contains("t")) && (!TransitionToOccur.getId().contains("a")))
			    {
			    	if(HistoryIndex < HistoryVector.size())
			    	{
			    	  helpTransition = (TransitionModel)HistoryVector.get(HistoryIndex);
			    	  if(helpTransition.getId().contains(TransitionToOccur.getId()) && (helpTransition.getId().contains("a") || helpTransition.getId().contains("t")))
			    	  {
			    		 tgController.setStepIntoSubProcess(true);
			    		 
			    	  }
			    	}
			    }
			    /*
			     * If History is in a Subprocess and it is finished, step out automatically
			     */
			    if(HistoryIndex < HistoryVector.size())
			    {
			      helpTransition = (TransitionModel)HistoryVector.get(HistoryIndex);
                  if(TransitionToOccur.getId().contains("t") || TransitionToOccur.getId().contains("a"))
                  {
                    if(TransitionToOccur.getId().length() >= (helpTransition.getId().length()+3))
                    {
                  	  changeTokenGameReference(null, true);
                    }
                  }
                  else
                  {
                   if(TransitionToOccur.getId().length() >= (helpTransition.getId().length()+5))
               	   {
                     changeTokenGameReference(null, true);
                   }
                  }			    			
			    }	   
			    
			  //Secure the net, so that no playback further than the last Simulation point can be done
			  }
			  else
			  {
				  HistoryIndex = HistoryVector.size();
			  }
		  }
		  //If end of net is not reached yet or there is still something to occur
		  if(followingActivatedTransitions.size() > 0)
		  {
		    tgController.occurTransitionbyTokenGameBarVC(TransitionToOccur, BackWard);
		  }
	  } 
	}

	
      	

	
	/**
	 * This method let the multiple transition occur (now 3 times) (only for sequences,
	 * as two transitions are active, the methode will stop)
	 * TODO: 
	 * 1) replace occurtimes 3 with a parameter. On default 3 but User can
	 * define this parameter, but this will be
	 */
	public void occurTransitionMulti(boolean BackWard)
	{
		int i = 0;
		if(BackWard)
		{
			while (i != occurtimes)
			{
				if(BackwardTransitionToOccur != null)
				{
					occurTransition(BackWard);
				}
				i++;
			}
		}
		else
		{
			while (i != occurtimes)
			{
				if((followingActivatedTransitions.size() < 2) && (!playbackRunning()))
				{
					occurTransition(BackWard);
				}
				else if (playbackRunning())
				{
					occurTransition(BackWard);
				}
				i++;
			}
		}		
	}
	
	/**
	 * will fill the "BackwardTransitionToOccur" with Data
	 */
	public void previousItem()
	{		  
	  if(HistoryVector != null)
	  {  
		if(!playbackRunning())
		{
			 if (HistoryVector.size() > 0)
			 {
			   BackwardTransitionToOccur = (TransitionModel)HistoryVector.remove(HistoryVector.size()-1);		
			   if (isRecordSelected())
			   {
			   	ahxHistoryContent.remove(ahxHistoryContent.size()-1);
			   }
			 }
			 else
			 {
				 BackwardTransitionToOccur = null;
			 }
		}
		//If playback running
		else
		{
			if(HistoryIndex > 0)
			{
				HistoryIndex--;
				BackwardTransitionToOccur = (TransitionModel)HistoryVector.get(HistoryIndex);
				return;
			}
			else  //if(HistoryIndex < 0)
			{
				BackwardTransitionToOccur = null;
				return;
			}
		}
	  }			
	}

	/**
	 * Will automatically run the TokenGame
	 * @param BackWard
	 */
	public void autoOccurAllTransitions(boolean BackWard)
	{
		backward = BackWard;
		new Thread(this).start();
	}
	
	/**
	 * This method will be called by the EasyChoice-Event and will handover the
	 * chosen transition to the occurTransition() method 
	 */
	public void proceedTransitionChoice(int index)
	{
		if((followingActivatedTransitions != null) && (index < followingActivatedTransitions.size()))
		{
			TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(index);
		    occurTransition(false);
		}	
	}
	
	
	
	/*
	 * ChoiceBox Handling section
	 */
	
	/**
	 * Adds a choice-item to followingActivatedTransitions
	 */
	public void addFollowingItem(TransitionModel transition)
	{
		if(followingActivatedTransitions == null)
		{
			followingActivatedTransitions = new LinkedList();
		}
		followingActivatedTransitions.addLast(transition);
	}
	
	/**
	 * Fills multi-choice box with content from followingActivatedTransitions
	 */
	public void fillChoiceBox()
	{
	  if(!playbackRunning())	
	  {
		clearChoiceBox(); //To ensure that the box is empty
		if(followingActivatedTransitions.size() == 1)
		{
			TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(0);
		}
		if(followingActivatedTransitions.size() > 1)
		{
			for(int i = 0; i < followingActivatedTransitions.size(); i++)				
			{
				helpTransition = (TransitionModel)followingActivatedTransitions.get(i);
     			acoChoiceItems.addElement(helpTransition.getNameValue());
			}
			if(!autoPlayBack)
			{
				ExpertView.disableForwardButtons();
			}
		}
	  }
	}

	/**
	 * clears the multi-Choice box
	 */
	public void clearChoiceBox()
	{
		acoChoiceItems.clear();
		if(!autoPlayBack)
		{
			ExpertView.enableForwardButtons();
		}
	}
	

	
	/*
	 * History Handling Section
	 */
	
	/**
	 * Starts the HistoryPlayback
	 */
    public void startHistoryPlayback()
    {
    	HistoryIndex = 0;
    	TransitionToOccur = HistoryVector.get(HistoryIndex);   	
    }
	
	
	/**
	 * Adds one more Item to the History List, to track the walked way
	 */
    public void addHistoryItem(TransitionModel transition)
    {	
    	if (HistoryVector == null)
    	{
    		HistoryVector = new Vector<TransitionModel>(1);
    		ahxHistoryContent.clear();
    	}
    	if(!playbackRunning())
    	{
    	  HistoryVector.add(transition);
    	}
    }
    
    /**
     * If History-tracking ("Recording") has been chosen by the user,
     * Every occurred transition is added to this List
     *  
     * @param transition
     */
    public void addHistoryListItem(TransitionModel transition)
    {
    	ahxHistoryContent.addElement(transition.getNameModel());
    }
    
    /**
     * Will create a Simulation-Object that can be added to the Simulations-Vector
     */
    public void createSaveableHistory()
    {
    	SaveableSimulation = new SimulationModel(PetriNet.getNewElementId(AbstractPetriNetModelElement.SIMULATION_TYPE), "Default", (Vector<TransitionModel>)HistoryVector.clone(), PetriNet.getLogicalFingerprint());
    	newHistory = true;
    }
	
    /**
     * Adds the History (SimulationModel) Objekt to the Simulations Vector
     */
    public void saveHistory()
    {
    	PetriNet.addSimulation(SaveableSimulation);
    }
    
    /**
     * Loads the History into the HistoryContent
     */
    public void loadHistory(int index)
    {
    	ahxHistoryContent.clear();
    	SaveableSimulation = (SimulationModel)PetriNet.getSimulations().get(index);
    	//needs a clone, otherwise, the saved history might be erased when the user just wants to clean the history-box
    	HistoryVector = (Vector<TransitionModel>)SaveableSimulation.getFiredTransitions().clone();
    	for (int i = 0; i < HistoryVector.size(); i++)
    	{
    		ahxHistoryContent.addElement(HistoryVector.get(i).getNameValue());
    	}
    }
    
    
    /**
     * Overwrites an existing history with the current one. 
     * @param index
     */
    public void overwriteHistory(int index)
    {
    	SaveableSimulation = (SimulationModel)PetriNet.getSimulations().get(index);
    	SaveableSimulation.setFiredTransitions((Vector<TransitionModel>)HistoryVector.clone());
    }
    
    
    /**
     * Deletes the a saved simulation 
     * @param index
     */
    public void deleteHistory(int index)
    {
    	SaveableSimulation = (SimulationModel)PetriNet.getSimulations().remove(index);
    }
    
	/**
	 * Will clear the History-Vector, the HistoryContent and will set the newHistory-variable to "true"
	 */
    public void clearHistoryData()
	{
		ahxHistoryContent.clear();
		HistoryVector = null; //Set reference to null, so that a new history-Vector will be created!
		newHistory = true;
	}
	
    
   /*
    * Threading Methods
    */ 
	/**
	 * Automatic TokenGame
	 * This Method occur automatic all transition if autoplayback is true. Choice transition will be
	 * occured by random choice
	 * 
	 * ToDo: 
	 * 	1) refreshNet after occurence - done
	 *  2) random choice only if no probabilities are set
	 */
	public void run() {
		
		auto();
	}
	
	/**
	 * Threading
	 */
	public void auto()
	{
		disableButtonforAutoPlayback();
		while(!isEndOfNet())
		{
			try {
		    	javax.swing.SwingUtilities.invokeLater(new TokenGameRunnableObject(this));
		    	Thread.sleep(getDelaytime()*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clearChoiceBox();
		}
		setEndOfNet(false);
		enableButtonforAutoPlayback();
	}
	
	public void moveForward()
	{
		if(followingActivatedTransitions.size() == 0)
		{
			setEndOfNet(true);
		}
		else
		{
			if(followingActivatedTransitions.size() >= 2)
			{
				int index = (int) Math.round(Math.random() * (followingActivatedTransitions.size()-1));
				TransitionToOccur = (TransitionModel)followingActivatedTransitions.get(index);
				occurTransition(false);
			}
			else
			{
				occurTransition(false);
				
			}
		}
	}
	
	public void moveBackward()
	{
		if(BackwardTransitionToOccur == null)
		{
			setEndOfNet(true);
		}
		else
		{
			occurTransition(true);
		}
	}
	

    
    
    /*
     * Setters and Getters
     */
	public LinkedList getFollowingActivatedTransitions()
	{
		return followingActivatedTransitions;
	}
	
	public SimulationModel getHistoryData()
	{
		return SaveableSimulation;
	}
	
	public DefaultListModel getHistoryContent()
	{
		return ahxHistoryContent;
	}

	public DefaultListModel getChoiceItems()
	{
		return acoChoiceItems;
	}
	
    public void setToOldHistory()
    {
    	newHistory = false;
    }
	
	/**
	 * Switches the AutoPlayback on or off
	 * @param onoff
	 */
	public void setAutoPlayback(boolean onoff)
	{
		autoPlayBack = onoff;
		if(autoPlayBack)
		{
			enableStepwiseButton();
			ExpertView.enableForwardButtons();
			disableAutoPlaybackButton();
			clearChoiceBox();
		}
		else
		{
			enableAutoPlaybackButton();
			disableStepwiseButton();
			fillChoiceBox();
		}
	}
	
	/**
	 * sets the EndOfNet flag
	 * @param end
	 */
	public void setEndOfNet(boolean end)
	{
		endofnet = end;
	}
	
	/**
	 * Determines witch View is switched on
	 * @return
	 */
	public int getViewMode()
	{
		return viewmode;
	}
	
	/**
	 * Set Viewmode Variable
	 */
	public void setViewMode(int view)
	{
		viewmode = view;
		if(viewmode == 0)
		{
			ExpertView.setVisible(true);
			SlimView.setVisible(false);
		}
		if(viewmode == 1)
		{
			ExpertView.setVisible(false);
			SlimView.setVisible(true);
		}
	}
	
	/**
	 * Determines how often fast fw / bw occure
	 */
	public int getOccurTimes()
	{
		return occurtimes;
	}
	
	/**
	 * Determines how often fast fw / bw occure
	 */
	public void setOccurTimes(int oc)
	{
		occurtimes = oc;
	}
	
	/**
	 * Determines the delay time for autoplayback
	 */
	public int getDelaytime()
	{
		return delaytime;
	}
	
	/**
	 * Set DelayTime for Autoplayback
	 */
	public void setDelaytime(int dt)
	{
		delaytime = dt;
	}
	
	/*
	 * Special Getters: Reference Providers
	 * IndexNumbers and so on
	 */
	
	/**
	 * 
	 * @return Reference to TokenGameController
	 */
	public TokenGameController getTokenGameController()
	{
		return tgController;
	}
	
	/**
	 * 
	 * @return Reference to TokenGameBarVC
	 */
	public TokenGameBarVC getExpertView()
	{
		return ExpertView;
	}
	
	/**
	 * 
	 * @return Reference to the PetriNet
	 */
	public PetriNetModelProcessor getPetriNet()
	{
		return PetriNet;
	}
	
	/**
	 * returns the ID of the transition which has to occur next
	 * @return
	 */
	public int getSelectedChoiceID()
	{
		//Add "if(ExpertView_enabled =true)" and else case to return the really selected list
		return ExpertView.getSelectedChoiceID();
	}
	
	/**
	 * Determines if the AutoPlayback is switched on
	 * @return
	 */
	public boolean getAutoPlayBack()
	{
		return autoPlayBack;
	}
	
	
    /*
     * boolean checks ("is"-checks)
     */
    
	/**
	 * Returns the value for the endofnet variable
	 * 
	 */
	public boolean isEndOfNet()
	{
		return endofnet;
	}
	
	/**
	 * Returns the value for the backward vasriable
	 * @return
	 */
	public boolean isBackward()
	{
		return backward;
	}
	
	/**
	 * Checks if the RecordButton is selected
	 */
    public boolean isRecordSelected()
	{
    	return ExpertView.isRecordSelected();
	}
    
    public boolean isNewHistory()
    {
    	return newHistory;
    }
    
	/**
	 * this method is to check whether a real playback is running, a walkthrough without recording or a record session.
	 * If it is a real playback, the application will not give the user any choice-possibility but it will just follow
	 * the history in the history-box
	 * @return true / false 
	 */
	public boolean playbackRunning()
	{
		if((ahxHistoryContent.size() != 0) && (!isRecordSelected()))
		{
		  return true;
		}
		else
		{
			return false;	
		}
	}
	
	/**
	 * checks if the TokenGame is currently running
	 * @return true if it is running
	 */
	public boolean tokengameRunning()
	{
		if(!ExpertView.isPlayButtonEnabled())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * Disable / Enable Buttons from ExpertView and SlimView 
	 * 
	 */
	
	/**
	 * Enables the StepDown-Button in the ExpertView
	 * will show the StepDown-Option on SlimView-PopUp
	 */
	public void enableStepDown()
	{
		ExpertView.enableStepDown();
	}
	
	/**
	 * Disables the StepDown-Button in the ExpertView
	 * and will hide the StepDown-Option on SlimView-PopUp
	 */
	public void disableStepDown()
	{
		ExpertView.disableStepDown();
	}
	
	/**
	 * Enables the StepWiseButton on ExpertView
	 * Toggles the Step/Run Button on SlimView
	 */
	public void enableStepwiseButton()
	{
		ExpertView.enableStepwiseButton();
	}

	/**
	 * Disables the StepWiseButton on ExpertView
	 * Un-toggles the Step/Run Button on SlimView
	 */	
	public void disableStepwiseButton()
	{
		ExpertView.disableStepwiseButton();
	}

	/**
	 * Enables the AutoPlayButton on ExpertView
	 * Un-toggles the Step/Run Button on SlimView
	 */
	public void enableAutoPlaybackButton()
	{
		ExpertView.enableAutoPlaybackButton();
	}
	
	/**
	 * Disables the AutoPlayButton on ExpertView
	 * Toggles the Step/Run Button on SlimView
	 */
	public void disableAutoPlaybackButton()
	{
		ExpertView.disableAutoPlaybackButton();
	}

	/**
	 * enables the PlayButton on ExpertView and on SlimView
	 */
	public void enablePlayButton()
	{
		ExpertView.enablePlayButton();
	}
	
	/**
	 * disables the PlayButton on ExpertView and on SlimView
	 */
	public void disablePlayButton()
	{
		ExpertView.disablePlayButton();
	}
	
	/**
	 * Enables the RecordButton.
	 * This Button is only available in ExpertView
	 */
	public void enableRecordButton()
	{
		ExpertView.enableRecordButton();
	}
	
	/**
	 * Disables the RecordButton.
	 * This Button is only available in ExpertView
	 */
	public void disableRecordButton()
	{
		ExpertView.disableRecordButton();
	}
		
	/**
	 * Calls the ExpertView enableButtonforAutoPlayback
	 */
	public void enableButtonforAutoPlayback()
	{
		ExpertView.enableButtonforAutoPlayback();
	}
	
	/**
	 * Calls the ExpertView disableButtonforAutoPlayback
	 */
	public void disableButtonforAutoPlayback()
	{
		ExpertView.disableButtonforAutoPlayback();
	}
	
	/**
	 * Calls the ExpertView enableBackWardButtons
	 */
	public void enableBackWardButtons()
	{
		ExpertView.enableBackWardButtons();
	}
	
	/**
	 * Calls the ExpertView disableBackWardButtons
	 */
	public void disableBackWardButtons()
	{
		ExpertView.disableBackWardButtons();
	}

	
	
	
	/*
	 * MISC
	 */
	
	/**
	 * Cleans up the ChoiceBox and the ChoiceArray.
	 * Is called by the TokenGameController.transitionClicked method and therefore
	 * makes it possible to step through the net with in-Editor-clicks or Remote-clicks
	 */
	public void cleanupTransition()
	{
		clearChoiceBox();
		if(followingActivatedTransitions != null)
		{
			followingActivatedTransitions.clear();
		}
	}
	
	public void changeTokenGameReference(TokenGameController newReference, boolean up)
	{
		if(up)
		{
			if((ProcessEditors != null) && (ProcessEditors.size() > 0))
			{              
			  //Remove all remaining tokens in the Editor
			  tgController.TokenGameRetore();
			  tgController.closeSubProcess();
			  tgController = (TokenGameController)ProcessEditors.removeLast();
			  if(!tgController.getThisEditor().isSubprocessEditor())
			  {
				  enableBackWardButtons();
			  }
			  cleanupTransition();
			  tgController.TokenGameCheckNet();
              
			}
		}
		else
		{
			if(ProcessEditors == null)
			{
				ProcessEditors = new LinkedList();
			}
		    ProcessEditors.add(tgController);
			tgController = newReference;
			if(tgController.getThisEditor().isSubprocessEditor())
			{
				disableBackWardButtons();
			}
		}
	}
}
