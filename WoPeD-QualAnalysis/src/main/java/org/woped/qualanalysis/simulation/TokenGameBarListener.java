package org.woped.qualanalysis.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class TokenGameBarListener implements ActionListener, ChangeListener, ListSelectionListener,
        CaretListener {

    // Constants
    // ======================
    // Playback configuration
    public final static int CHOOSE_STEPWISE = 1;
    public final static int CHOOSE_PLAYBACK = 2;

    // Navigation Button
    public final static int CLICK_BACKWARD = 5;
    public final static int CLICK_STOP = 6;
    public final static int CLICK_PLAY = 7;
    public final static int CLICK_PAUSE = 8;
    public final static int CLICK_FORWARD = 9;

    // Process stepping
    public final static int CLICK_STEP_UP = 11;
    public final static int CLICK_STEP_DOWN = 12;

    // Use analysis functions from simulatorBar
    public final static int ANALYSIS = 39;
    public final static int QUANTCAP = 40;
    public final static int QUANTSIM = 41;
    public final static int REACHABILITY_GRAPH = 42;

    // AutoChoice List
    public final static int CHOOSE_REMOTECONTROLL = 43;

    // Variables
    private int ID = 0;
    private TokenGameSession RemoteControl = null;

    // Needed for RemoteControlElements
    public TokenGameBarListener(int ElementID, TokenGameSession RC) {
        ID = ElementID;
        RemoteControl = RC;
    }

    private void actionRouter() {
        switch (ID) {
        case CHOOSE_STEPWISE:
            RemoteControl.setEndOfAutoPlay(true);        	
            RemoteControl.setAutoPlayback(false);
            break;
        case CHOOSE_PLAYBACK:
            RemoteControl.setAutoPlayback(true);
            break;
        case CLICK_BACKWARD:
        	RemoteControl.occurTransition(true, false);
            break;
        case CLICK_STOP:
        	// Stop and reset token game to initial state
            RemoteControl.resetTokenGameSession();
            break;
        case CLICK_PLAY:
            RemoteControl.setEndOfAutoPlay(false);
            RemoteControl.autoOccurAllTransitions(false);
            break;
        case CLICK_PAUSE:
            RemoteControl.setEndOfAutoPlay(true);
            break;
        case CLICK_FORWARD:
            RemoteControl.occurTransition(false, false);
            break;
        case CLICK_STEP_UP:
        	RemoteControl.changeTokenGameReference(null, true);
            break;
        case CLICK_STEP_DOWN:
        	// Make sure that if there is a sub process transition
        	// it is the one selected to occur
        	RemoteControl.setSubProcessTransition();
        	RemoteControl.occurTransition(false, true);
            break;
        default:
            break;
        }
    }

    /*
     * Action Events
     */
    public void actionPerformed(ActionEvent e) {
        // Calls the method for the centralized Action-Handling
        actionRouter();
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