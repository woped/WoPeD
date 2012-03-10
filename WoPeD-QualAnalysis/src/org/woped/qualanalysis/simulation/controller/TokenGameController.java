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
package org.woped.qualanalysis.simulation.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.border.LineBorder;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.ITokenGameController;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 * 
 *         The <code>TokenGameController</code> provides the visual and/or model sided TokenGame of a Petrinet. <br>
 * <br>
 *         1) Visual (and Model) sided TokenGame: <br>
 *         You have to set a <code>PetriNet</code> and a <code>WoPeDJGraph</code>. <br>
 *         If it's done via the Contructor you have nothing to worry about. <br>
 *         If you only set the petrinet via Constructor and the Graph with the setter method, you'll have to enable the visualTokenGame explicit with the method
 *         <code>enableVisualTokenGame()</code>.<br>
 * <br>
 *         2) Only model sided TokenGame: <br>
 *         If you have set the the <code>PetriNet</code> and the <code>WoPeDJGraph</code> with the Constructor you have to call
 *         <code>disableVisualTokenGame()</code>.<br>
 * 
 *         Created on 21.09.2004 Last Change on 13.10.2004
 */
public class TokenGameController implements ITokenGameController {

    private PetriNetModelProcessor petrinet = null;
    private AbstractGraph graph = null;
    private Map<String, AbstractPetriNetElementModel> allTransitions = null;
    // ! Stores a set containing all sink places of the simulated net
    // ! Used to manage return handling for sub-processes
    // ! (Visual token game only!)
    private Set<PlaceModel> sinkPlaces = null;
    private MouseHandler tokenGameMouseHandler = null;
    private boolean visualTokenGame = false;
    private IEditor thisEditor = null;
    private ReferenceProvider ParentControl = null;
    private TokenGameBarController RemoteControl = null;
    private boolean stepIntoSubProcess = false;

    /**
     * Constructor for the model and visual sided TokenGame.
     * 
     * @param petrinet
     * @param graph
     */
    public TokenGameController(IEditor thisEditor) {
        this.petrinet = (PetriNetModelProcessor) thisEditor.getModelProcessor();
        this.graph = thisEditor.getGraph();
        this.thisEditor = thisEditor;
        setVisualTokenGame(graph != null);
        tokenGameMouseHandler = new MouseHandler();

    }

    /**
     * Constructor for the model sided TokenGame.
     * 
     * @param petrinet
     */
    public TokenGameController(PetriNetModelProcessor petrinet) {
        this.petrinet = petrinet;
        this.graph = null;
        this.thisEditor = null;
        setVisualTokenGame(false);
        tokenGameMouseHandler = new MouseHandler();
    }

    /* ###################### Controller Methods ###################### */

    /**
     * Starts the TokenGame. TokenGame Start method changed. Now it will only open the RemoteController No "checkNet()" this will be done later.
     */
    public void start() {
        // remove Highlighting in RG
        deHighlightRG();
        enableVisualTokenGame();

        // if you are stepping into a subprocess right now
        if (thisEditor.isSubprocessEditor()) {
            ParentControl = new ReferenceProvider();
            RemoteControl = ParentControl.getRemoteControlReference();
            RemoteControl.changeTokenGameReference(this, false);

            // Storing Transition Reference (simple and operator)
            allTransitions = getPetriNet().getElementContainer().getElementsByType(
                    AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
            allTransitions.putAll(getPetriNet().getElementContainer().getElementsByType(
                    AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));
            allTransitions
                    .putAll(getPetriNet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE));
            // Find and show active Transitions/Arcs
            RemoteControl.cleanupTransition();
            checkNet();
        } else {
            // remove highlighting from RG in Editor
            petrinet.resetRGHighlightAndVTokens();
            // displays the TokenGame Remote-Control if it already exist, if not create
            if (RemoteControl != null) {
                RemoteControl.addControlElements();
            } else {
                RemoteControl = new TokenGameBarController(this, petrinet);
            }

            // Storing Transition Reference (simple and operator)
            allTransitions = getPetriNet().getElementContainer().getElementsByType(
                    AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
            allTransitions.putAll(getPetriNet().getElementContainer().getElementsByType(
                    AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));
            allTransitions
                    .putAll(getPetriNet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE));

        }
        ReferenceProvider refer = new ReferenceProvider();
        refer.getUIReference().setFirstTransitionActive();        
        // set first transition active
        RemoteControl.startPlayback();
        
    }

    /**
     * Stops the TokenGame. Disables the visual TokenGame if set.
     */
    public void stop() {
        // remove Highlighting in RG
        deHighlightRG();
        // set all transistions/arcs inactive & not fireing
        resetTransitionStatus();
        resetArcStatus();
        // restore origin tokencount
        resetVirtualTokensInElementContainer(getPetriNet().getElementContainer());
        // disable visualTokenGame
        if (isVisualTokenGame()) {
            disableVisualTokenGame();
        }
        // animator.stop();

        // Hide and remove Tokengame Remote-Control
        RemoteControl.removeControlElements();
    }

    private void deHighlightRG() {
        ReferenceProvider refer = new ReferenceProvider();
        Object[] a = refer.getDesktopReference().getComponents();
        for (int i = 0; i < refer.getDesktopReference().getComponentCount(); i++) {
            if (a[i] instanceof ReachabilityGraphVC) {
                ReachabilityGraphVC rvc = (ReachabilityGraphVC) a[i];

                if (rvc.hasEditor(thisEditor)) {
                    rvc.setUnselectButtonEnabled(thisEditor, false);
                    ((ReachabilityJGraph) rvc.getJGraph(thisEditor)).deHighlight();
                }
                break;
            }
        }
    }

    // ! Reset the virtual token count for the specified element container
    // ! If the element container contains subprocess elements,
    // ! this method is called recursively to reset token counts in sub-processes
    // ! @param container specifies the container that should be processed
    private void resetVirtualTokensInElementContainer(ModelElementContainer container) {
        // restore origin tokencount
        Iterator<String> placeIter = container.getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE).keySet().iterator();
        while (placeIter.hasNext()) {
            ((PlaceModel) container.getElementById(placeIter.next())).resetVirtualTokens();
        }
        Iterator<String> subpIter = container.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE).keySet().iterator();
        while (subpIter.hasNext()) {
            // Now we call ourselves recursively for all sub-processes
            ModelElementContainer innerContainer = ((SubProcessModel) container.getElementById(subpIter.next()))
                    .getSimpleTransContainer();
            resetVirtualTokensInElementContainer(innerContainer);
        }
    }

    /**
     * Enables the visual TokenGame.
     */
    public void enableVisualTokenGame() {
        this.visualTokenGame = true;

        // disable editor access
        thisEditor.setReadOnly(false);
        getGraph().enableMarqueehandler(false);
        getGraph().clearSelection();
        getGraph().setEnabled(false);
        getGraph().setPortsVisible(false);
        getGraph().setGridVisible(false);
        getGraph().setBackground(new Color(250, 250, 250));
        getGraph().setBorder(new LineBorder(new Color(0, 175, 20), 3, false));

        // register own MouseHandler
        getGraph().addMouseListener(tokenGameMouseHandler);

        sinkPlaces = new HashSet<PlaceModel>();
        IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(thisEditor);
        Iterator<AbstractPetriNetElementModel> i = qualanService.getSinkPlaces().iterator();
        while (i.hasNext()) {
            sinkPlaces.add((PlaceModel) i.next());
        }

        getGraph().refreshNet();
    }

    /**
     * Disables the visual TokenGame.
     */
    public void disableVisualTokenGame() {
        // enable editor access
        getGraph().setEnabled(true);
        getGraph().enableMarqueehandler(true);
        // getGraph().setPortsVisible(true);
        getGraph().setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());
        getGraph().setBackground(Color.WHITE);
        getGraph().setBorder(null);
        // getGraph().setSelectNewCells(true);
        // remove own MouseHandler
        getGraph().removeMouseListener(tokenGameMouseHandler);
        getGraph().refreshNet();
    }

    /*
	 *  
	 */
    private void checkNet() {
        long begin = System.currentTimeMillis();
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "TokenGame: CHECK NET");
        Iterator<String> transIter = allTransitions.keySet().iterator();
        RemoteControl.disableStepDown(); // Disables the stepDown-Navigation button
        resetArcStatus();
        // Iterate over all Transitions
        while (transIter.hasNext()) {
            checkTransition((TransitionModel) allTransitions.get(transIter.next()));
        }

        getGraph().updateUI();
        RemoteControl.fillChoiceBox(); // Fills the Choicebox with the active Transitions that have been encountered through checkTransition()
        // Check if there is a transition to choose in SlimChoiceBox
        RemoteControl.checkSlimChoiceBox();
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "           ... DONE ("
                + (System.currentTimeMillis() - begin) + " ms)");
        setMarkingInRG((BuilderFactory.createCurrentMarking(BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(
                thisEditor).getLowLevelPetriNet(), true)));
        // FIXME highlighting does not work properly for markings with unbounded places
    }

    private void setMarkingInRG(IMarking mark) {
        if (ParentControl == null) {
            ParentControl = new ReferenceProvider();
        }
        Object[] a = ParentControl.getDesktopReference().getComponents();
        for (int i = 0; i < ParentControl.getDesktopReference().getComponentCount(); i++) {
            if (a[i] instanceof ReachabilityGraphVC) {
                ReachabilityGraphVC rvc = (ReachabilityGraphVC) a[i];
                if (rvc.hasEditor(thisEditor)) {
                    ((ReachabilityJGraph) rvc.getJGraph(thisEditor)).highlightMarking(mark);
                }
                break;
            }
        }
    }

    /*
     * Will check transitions if they have to be activated or not
     */
    private void checkTransition(TransitionModel transition) {
        Map<String, ArcModel> incomingArcs = getPetriNet().getElementContainer().getIncomingArcs(transition.getId());

        Map<String, Object> outgoingArcs = getPetriNet().getElementContainer().getOutgoingArcs(transition.getId());
        // temporary variables

        if (transition.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE
                || transition.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {

            if (transition.isActivated()) {
                // This will add all currently active postSet Transitions to the TokenGameBarVC-Autochoice-List

                RemoteControl.addFollowingItem(transition);

                if (transition.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
                    RemoteControl.enableStepDown(transition); // Enables Step-Down Navigation Button
                }
            }
        } else
            if (transition.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                OperatorTransitionModel operator = (OperatorTransitionModel) transition;
                if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE
                        || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
                        || operator.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE) {
                    if (transition.isActivated()) {
                        // This will add the AND-X-Transition to the OccurenceList
                        RemoteControl.addFollowingItem(transition);
                    }

                } else
                    if ((operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
                            || (operator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)) {
                        if (transition.isActivated()) {
                            String XorName, ID;
                            Iterator<String> outArcs = outgoingArcs.keySet().iterator();
                            TransitionModel virtualTransition; // needed to build virtual Transitions.
                            AbstractPetriNetElementModel helpPlace;

                            /*
                             * In this while-loop, Virtual Transitions will be build which represent the Arcs in the OccurenceList. If a virtual Transition is
                             * chosen by the user, it will be identified by its ID as Arc and the depending Arc will be taken instead to be occured
                             */
                            while (outArcs.hasNext()) {
                                ID = outArcs.next(); // get the Arc's ID
                                // Use a new CreationMap with the arcs id to create the virtual transition
                                CreationMap map = CreationMap.createMap();
                                map.setId(ID);
                                virtualTransition = new TransitionModel(map);
                                helpPlace = getPetriNet().getElementContainer().getElementById(
                                        getPetriNet().getElementContainer().getArcById(ID).getTargetId());
                                XorName = transition.getNameValue() + " -> (" + helpPlace.getNameValue() + ")";
                                virtualTransition.setNameValue(XorName);
                                RemoteControl.addFollowingItem(virtualTransition);
                                virtualTransition = null;
                                XorName = "";
                            }

                            setOutgoingArcsActive(transition.getId(), true);
                        }
                    } else
                        if ((operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
                                || (operator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)) {
                            if (transition.getNumIncomingActivePlaces() > 0) {
                                String XorName, ID;
                                Iterator<String> inArcs = incomingArcs.keySet().iterator();
                                TransitionModel virtualTransition; // needed to build virtual Transitions.
                                AbstractPetriNetElementModel helpPlace;
                                // ArcModel activeArc;

                                /*
                                 * In this while-loop, Virtual Transitions will be build which represent the Arcs in the OccurenceList. If a virtual Transition
                                 * is chosen by the user, it will be identified by its ID as Arc and the depending Arc will be taken instead to be occured
                                 */
                                setIncomingArcsActive(transition.getId(), true);
                                while (inArcs.hasNext()) {
                                    ID = inArcs.next(); // Get Arcs ID
                                    // List all activated Arcs in the occur List
                                    if (getPetriNet().getElementContainer().getArcById(ID).isActivated()) {
                                        // Use a new CreationMap with the arcs id to create the virtual transition
                                        CreationMap map = CreationMap.createMap();
                                        map.setId(ID);
                                        virtualTransition = new TransitionModel(map);
                                        helpPlace = getPetriNet().getElementContainer().getElementById(
                                                getPetriNet().getElementContainer().getArcById(ID).getSourceId());
                                        XorName = "(" + helpPlace.getNameValue() + ")-> " + transition.getNameValue();
                                        virtualTransition.setNameValue(XorName);
                                        RemoteControl.addFollowingItem(virtualTransition);
                                        virtualTransition = null;
                                        XorName = "";
                                    }
                                }
                            }
                        } else
                            if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                                // This is the XOR split-join combination type.
                                // Check whether the center place already contains (a) token(s)
                                if ((operator.getCenterPlace() != null)
                                        && (operator.getCenterPlace().getVirtualTokenCount() > 0)) {

                                    String XorName, ID;
                                    Iterator<String> outArcs = outgoingArcs.keySet().iterator();
                                    TransitionModel virtualTransition; // needed to build virtual Transitions.
                                    AbstractPetriNetElementModel helpPlace;

                                    /*
                                     * In this while-loop, Virtual Transitions will be build which represent the Arcs in the OccurenceList. If a virtual
                                     * Transition is chosen by the user, it will be identified by its ID as Arc and the depending Arc will be taken instead to
                                     * be occured
                                     */
                                    while (outArcs.hasNext()) {
                                        ID = outArcs.next(); // get the Arc's ID
                                        // Use a new CreationMap with the arcs id to create the virtual transition
                                        CreationMap map = CreationMap.createMap();
                                        map.setId(ID);
                                        virtualTransition = new TransitionModel(map);

                                        virtualTransition.setId(ID); // set HelpTransition's ID to Arc's ID
                                        helpPlace = getPetriNet().getElementContainer().getElementById(
                                                getPetriNet().getElementContainer().getArcById(ID).getTargetId());
                                        XorName = transition.getNameValue() + " -> (" + helpPlace.getNameValue() + ")";
                                        virtualTransition.setNameValue(XorName);
                                        RemoteControl.addFollowingItem(virtualTransition);
                                        virtualTransition = null;
                                        XorName = "";
                                    }

                                    setOutgoingArcsActive(transition.getId(), true);
                                }
                                // There must at least be one token at the input side for
                                // the transition to be
                                // activated
                                if (transition.getNumIncomingActivePlaces() > 0) {
                                    String XorName, ID;
                                    Iterator<String> inArcs = incomingArcs.keySet().iterator();
                                    TransitionModel virtualTransition; // needed to build virtual Transitions.
                                    AbstractPetriNetElementModel helpPlace;
                                    // ArcModel activeArc;

                                    /*
                                     * In this while-loop, Virtual Transitions will be build which represent the Arcs in the OccurenceList. If a virtual
                                     * Transition is chosen by the user, it will be identified by its ID as Arc and the depending Arc will be taken instead to
                                     * be occured
                                     */
                                    setIncomingArcsActive(transition.getId(), true);
                                    while (inArcs.hasNext()) {
                                        ID = inArcs.next(); // Get Arcs ID
                                        // List all activated Arcs in the occur List
                                        if (getPetriNet().getElementContainer().getArcById(ID).isActivated()) {
                                            // Use a new CreationMap with the arcs id to create the virtual transition
                                            CreationMap map = CreationMap.createMap();
                                            map.setId(ID);
                                            virtualTransition = new TransitionModel(map);
                                            helpPlace = getPetriNet().getElementContainer().getElementById(
                                                    getPetriNet().getElementContainer().getArcById(ID).getSourceId());
                                            XorName = "(" + helpPlace.getNameValue() + ")-> "
                                                    + transition.getNameValue();
                                            virtualTransition.setNameValue(XorName);
                                            RemoteControl.addFollowingItem(virtualTransition);
                                            virtualTransition = null;
                                            XorName = "";
                                        }
                                    }
                                }
                            }
            }
    }

    /*
     * Handles a click on any Transition in any state
     */
    private void transitionClicked(TransitionModel transition, MouseEvent e) {
        if (transition.isActivated()) {
            // Remember whether we actually did something here
            // and only deactivate the transition after a *successful* click
            boolean actionPerformed = false;
            if (transition.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE
                    || transition.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
                // LoggerManager.debug(Constants.EDITOR_LOGGER, "TokenGame: FIRE
                // simple Transition:
                // "+transition.getId());
                receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
                if (transition.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
                    if ((e != null) || (stepIntoSubProcess)) {
                        int relativeX = 0;
                        int relativeY = 0;
                        if (e != null) {
                            relativeX = e.getX() - transition.getX();
                            relativeY = e.getY() - transition.getY();
                        }
                        // the lower left half of the transition will trigger 'step into'
                        if ((relativeY >= relativeX) || (stepIntoSubProcess)) {
                            // Step into sub-process and process it in a new modal editor
                            // dialog in token-game mode
                            ParentControl = new ReferenceProvider();
                            ParentControl.setRemoteControlReference(RemoteControl);
                            thisEditor.openTokenGameSubProcess((SubProcessModel) transition);
                        }
                    }
                }
                actionPerformed = true;

            } else
                if (transition.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                    OperatorTransitionModel operator = (OperatorTransitionModel) transition;
                    if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE
                            || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
                            || operator.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE) {
                        // LoggerManager.debug(Constants.EDITOR_LOGGER, "TokenGame:
                        // FIRE AND-Transition:
                        // "+transition.getId());
                        receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                        sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
                        actionPerformed = true;

                    } else
                        if ((operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
                                || (operator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)) {
                            // Do nothing: Only controlled by Arc Clicking
                        } else
                            if ((operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
                                    || (operator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)) {
                                // Do nothing: Only controlled by Arc Clicking
                            } else
                                if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                                    // Do nothing: Only controlled by Arc Clicking as the user
                                    // has to select the
                                    // token source
                                }
                }
            if (actionPerformed == true) {
                // Now update the status of the petri net by checking all
                // transitions and activating them
                // if their input conditions are fulfilled
                // This will also trigger a redraw

                // Track the "walked way"
                RemoteControl.addHistoryItem(transition);
                if (RemoteControl.isRecordSelected()) {
                    // Track the way in the history-box
                    RemoteControl.addHistoryListItem(transition);
                }
                if (stepIntoSubProcess) {
                    setStepIntoSubProcess(false);
                } else {
                    RemoteControl.cleanupTransition();
                    checkNet();
                }

            }
        }
    }

    /*
     * Handles a click on any arc in any state
     */
    private void arcClicked(ArcModel arc) {

        if (arc.isActivated()) {
            AbstractPetriNetElementModel source = (AbstractPetriNetElementModel) getPetriNet().getElementContainer().getElementById(
                    arc.getSourceId());
            AbstractPetriNetElementModel target = (AbstractPetriNetElementModel) getPetriNet().getElementContainer().getElementById(
                    arc.getTargetId());

            OperatorTransitionModel tempOperator;

            // As a reminder, an arc is generally going from a place to a
            // transition or from a
            // transition to a place.
            // When pointing to a transition it is referencing a potential
            // provider of a token.
            // When pointing to a place that place is potential receiver for a
            // token.
            // First, we check if the origin of our clicked arc is a transition
            // (Note that we check for the operator type only as ordinary
            // transitions are not triggered
            // by clicking the arrow but by clicking the transition itself which
            // is handled in transitionClicked())
            if (source.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                tempOperator = (OperatorTransitionModel) source;
                if (tempOperator.isActivated()) {
                    if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE
                            || tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE
                            || tempOperator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE) {
                        receiveTokens(arc);
                        if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                            sendTokens(getPetriNet().getElementContainer().getIncomingArcs(tempOperator.getId()));
                        } else {
                            // Special code for splitjoin. We have to take the
                            // token from the center place
                            if (tempOperator.getCenterPlace() != null) {
                                // FIXME: Once implemented, this place will also
                                // have to remove weighted tokens
                                tempOperator.getCenterPlace().sendToken();
                            }
                        }
                    }
                }
            } else
                if (target.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                    tempOperator = (OperatorTransitionModel) target;
                    if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE
                            || tempOperator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE
                            || tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                        sendTokens(arc);
                        if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                            receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(tempOperator.getId()));
                        } else {
                            // Special code for splitjoin. We have to send the token
                            // to the center place
                            if (tempOperator.getCenterPlace() != null) {
                                // FIXME: Once implemented, this place will also have to
                                // receive weighted tokens
                                tempOperator.getCenterPlace().receiveToken();
                            }
                        }
                    }
                }
            // Update net status
            // and trigger redraw

            // Track the "walked way" by get out the transition's ID of the Choice-Box
            TransitionModel helpTransitionReference;
            for (int i = 0; i < RemoteControl.getFollowingActivatedTransitions().size(); i++) {
                helpTransitionReference = RemoteControl.getFollowingActivatedTransitions().get(i);
                if (arc.getId().equals(helpTransitionReference.getId())) {
                    RemoteControl.addHistoryItem(helpTransitionReference);
                    if (RemoteControl.isRecordSelected()) {
                        // Track the way in the history
                        RemoteControl.addHistoryListItem(helpTransitionReference);
                    }
                }
            }
            RemoteControl.cleanupTransition();
            checkNet();

        }
    }

    /**
     * This method has been implemented to allow backward-stepping through the net. It cares about stepping back transitions as well as arcs. This method is
     * called by the TokenGameController.occurTransitionbyTokenGameBarVC
     * 
     * @param transition
     * @param arc
     */
    private void backwardItem(TransitionModel transition, ArcModel arc) {
        // Either transition or arc has to be null
        // Remember whether we actually did something here
        // and only deactivate the transition after a *successful* click
        boolean actionPerformed = false;
        if (transition != null) {
            receiveBackwardTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
            sendBackwardTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
            actionPerformed = true;
        } else // if it is an arc ==> it is some kind of XOR-Operation
        {
            AbstractPetriNetElementModel source = (AbstractPetriNetElementModel) getPetriNet().getElementContainer().getElementById(
                    arc.getSourceId());
            AbstractPetriNetElementModel target = (AbstractPetriNetElementModel) getPetriNet().getElementContainer().getElementById(
                    arc.getTargetId());
            OperatorTransitionModel tempOperator;
            if (target.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                tempOperator = (OperatorTransitionModel) target;
                receiveBackwardTokens(arc);
                if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                    sendBackwardTokens(getPetriNet().getElementContainer().getOutgoingArcs(tempOperator.getId()));
                    actionPerformed = true;
                } else {
                    // Special code for splitjoin. We have to take the
                    // token from the center place
                    if (tempOperator.getCenterPlace() != null) {
                        tempOperator.getCenterPlace().sendToken();
                        actionPerformed = true;
                    }
                }
            }
            if (source.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                tempOperator = (OperatorTransitionModel) source;
                sendBackwardTokens(arc);
                if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) {
                    receiveBackwardTokens(getPetriNet().getElementContainer().getIncomingArcs(tempOperator.getId()));
                    actionPerformed = true;
                } else {
                    // Special code for splitjoin. We have to send the token
                    // to the center place
                    if (tempOperator.getCenterPlace() != null) {
                        tempOperator.getCenterPlace().receiveToken();
                    }
                    actionPerformed = true;
                }
            }
        }

        if (actionPerformed == true) {
            // Now update the status of the petri net by checking all
            // transitions and activating them
            // if their input conditions are fulfilled
            // This will also trigger a redraw
            // Cleans up the RemoteControl. Needed to make sure that in-Editor-click and Remote-click work properly
            RemoteControl.cleanupTransition();
            RemoteControl.clearChoiceBox();
            checkNet();
        }
    }

    /*
     * Sets incoming Arcs of an Transition active, if the Place before has Tokens. Sets incoming Arcs of an Transition inactive, without any condition.
     */
    private void setIncomingArcsActive(Object transitionId, boolean active) {
        Iterator<String> incomingArcsIter = getPetriNet().getElementContainer().getIncomingArcs(transitionId).keySet()
                .iterator();
        while (incomingArcsIter.hasNext()) {
            ArcModel arc = getPetriNet().getElementContainer().getArcById(incomingArcsIter.next());
            if (active) {
                PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
                if (place.getVirtualTokenCount() > 0) {
                    arc.setActivated(true);
                    // TODO: check weight
                }
            } else {
                arc.setActivated(false);
            }
        }
    }

    /*
     * Sets all outgoing arcs of an Transition active/inactive, without any condition.
     */
    private void setOutgoingArcsActive(Object transitionId, boolean active) {
        Iterator<String> outgoingIter = getPetriNet().getElementContainer().getOutgoingArcs(transitionId).keySet()
                .iterator();
        while (outgoingIter.hasNext()) {
            getPetriNet().getElementContainer().getArcById(outgoingIter.next()).setActivated(active);
        }
    }

    /*
     * Send (subtract) Tokens from the places that are the source of the Map-filled arcs. ATTENTION: source ot the arcs must be always a place
     */
    private void sendTokens(Map<String, ArcModel> arcsToFire) {
        Iterator<String> arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext()) {
            sendTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));

        }
    }

    /*
     * Send (subtract) Tokens from the arc's source. ATTENTION: source must be a Place.
     */
    private void sendTokens(ArcModel arc) {
        try {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
            if (place != null) {
                place.sendToken();
                // TODO: when ARC WEIGTH implemented send tokens weigth times
            }
        } catch (ClassCastException cce) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
                    "TokenGame: Cannot send token. Source is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Receive (add) Tokens to the places that are the target or the Map-filled arcs. ATTENTION: targets must be places.
     */
    private void receiveTokens(Map<String, Object> arcsToFire) {
        Iterator<String> arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext()) {
            receiveTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));

        }
    }

    /*
     * Receive (add) Tokens to the place that is the target of the arc. ATTENTION: Target must be a place.
     */
    private void receiveTokens(ArcModel arc) {
        try {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getTargetId());
            if (place != null) {
                place.receiveToken();
                // TODO: when ARC WEIGTH implemented receive tokens weigth times
            }
        } catch (ClassCastException cce) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
                    "TokenGame: Cannot receive token. Target is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Send (subtract) Tokens from the places that are the target of the arcs. ATTENTION: target of the arcs must be always a place
     */

    private void sendBackwardTokens(Map<String, Object> arcsToFire) {
        Iterator<String> arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext()) {
            sendBackwardTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));

        }
    }

    /*
     * Send (subtract) Tokens from the arc's target on Backward stepping. ATTENTION: target must be a Place.
     */
    private void sendBackwardTokens(ArcModel arc) {
        try {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getTargetId());
            if (place != null) {
                place.sendToken();
                // TODO: when ARC WEIGTH implemented send tokens weigth times
            }
        } catch (ClassCastException cce) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
                    "TokenGame: Cannot send token. Source is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Receive Tokens when doing backward steps
     */
    private void receiveBackwardTokens(Map<String, ArcModel> arcsToFire) {
        Iterator<String> arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext()) {
            receiveBackwardTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));

        }
    }

    /*
     * Receive (add) Tokens to the place that is the source of the arc. ATTENTION: Target must be a place.
     */
    private void receiveBackwardTokens(ArcModel arc) {
        try {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
            if (place != null) {
                place.receiveToken();
                // TODO: when ARC WEIGTH implemented receive tokens weigth times
            }
        } catch (ClassCastException cce) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
                    "TokenGame: Cannot receive token. Target is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Disables all Transitions. Set each transition inactive and not firing. Used on stop.
     */
    private void resetTransitionStatus() {
        Iterator<String> eleIter = allTransitions.keySet().iterator();
        TransitionModel transition;
        // Iterate over all Transitions
        while (eleIter.hasNext()) {
            transition = (TransitionModel) allTransitions.get(eleIter.next());
            if (transition.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                // When starting a new token game we have to reset all center
                // places that
                // may contain tokens
                OperatorTransitionModel tempOperator = (OperatorTransitionModel) transition;
                if (tempOperator.getCenterPlace() != null) {
                    tempOperator.getCenterPlace().resetVirtualTokens();
                }
            }
        }
    }

    /*
     * Inactivate all arcs.
     */
    private void resetArcStatus() {
        Iterator<String> arcIter = getPetriNet().getElementContainer().getArcMap().keySet().iterator();
        while (arcIter.hasNext()) {
            getPetriNet().getElementContainer().getArcById(arcIter.next()).setActivated(false);
        }
    }

    /*
     * Returns a Map containing the fireing Transitions.
     */
    /*
     * private Map getFireingTransitions() { Iterator transIter = allTransitions.keySet().iterator(); TransitionModel transition; Map fireingTrans = new
     * HashMap(); while (transIter.hasNext()) { transition = (TransitionModel) getPetriNet().getElementContainer().getElementById(transIter.next()); // save if
     * the transition is fireing if (transition.isFireing()) { fireingTrans.put(transition.getId(), transition); } } return fireingTrans; }
     */
    /*
     * ################################### Getter & Setter ########################################
     */

    /**
     * Returns if the visual TokenGame is enabled.
     * 
     * @return Returns the visualTokenGame.
     */
    public boolean isVisualTokenGame() {
        return visualTokenGame;
    }

    /*
     * use with caution. does not affect the TokenGame Controlling directly. Just for setting the initial variable value. @param visualTokenGame The
     * visualTokenGame to set.
     */
    private void setVisualTokenGame(boolean visualTokenGame) {
        this.visualTokenGame = visualTokenGame;
    }

    /*
     * @return Returns the graph.
     */
    private AbstractGraph getGraph() {
        return graph;
    }

    /*
     * @param graph The graph to set.
     */
    /*
     * private void setGraph(WoPeDJGraph graph) { this.graph = graph; }
     */
    /*
     * @return Returns the petrinet.
     */
    private PetriNetModelProcessor getPetriNet() {
        return petrinet;
    }

    /*
     * @param petrinet The petrinet to set.
     */
    /*
     * private void setPetriNet(PetriNetModelProcessor petrinet) { this.petrinet = petrinet; }
     */
    /*
     * ################################### Mouse Handler ########################################
     */

    /*
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br><br> MouseHandle for the TokenGame. USE ONLY if visual TokenGame is ENABLED.
     */
    private class MouseHandler implements MouseListener {

        /**
         * Ignore mouse pressed, all handling happens in mouse released
         */
        public void mousePressed(final MouseEvent e) {
            e.consume();
        }

        /**
		 *  
		 */
        public void mouseReleased(MouseEvent e) {
            if (RemoteControl.playbackRunning()) {
                return;
            }
            Vector<Object> allCells = getGraph().getAllCellsForLocation(e.getPoint().x, e.getPoint().y);
            TransitionModel transition = findTransitionInCell(allCells);
            PlaceModel place = findPlaceInCell(allCells);
            ArcModel arc = findArcInCell(allCells);
            if (transition != null && transition.isActivated()) {
                transitionClicked(transition, e);
            } else
                if (arc != null && arc.isActivated()) {
                    arcClicked(arc);
                } else
                    if (place != null && place.isActivated()) {
                        // If an active place has been clicked there is only one reasonable explanation for this:
                        // It is a sink place of a sub-process 
                        RemoteControl = ParentControl.getRemoteControlReference();
                        // De-register the sub-process 
                        RemoteControl.changeTokenGameReference(null, true);
                        // and close the editor
                        thisEditor.closeEditor();

                    }
            e.consume();
        }

        @SuppressWarnings("unused")
        public void mouseMoved(MouseEvent arg0) {}

        public void mouseClicked(MouseEvent arg0) {}

        public void mouseEntered(MouseEvent arg0) {}

        public void mouseExited(MouseEvent arg0) {}

        /*
         * Checks if cell is a Place or if a Place is nested in a Group.
         */
        private PlaceModel findPlaceInCell(Vector<Object> cells) {
            for (Object cell : cells) {
                if (cell instanceof PlaceModel) {
                    return (PlaceModel) cell;
                }
            }
            return null;
        }

        /*
         * Checks if cell is a Transition or if a Transition is nested in a Group.
         */
        private TransitionModel findTransitionInCell(Vector<Object> cells) {
            for (Object cell : cells) {
                if (cell instanceof TransitionModel) {
                    return (TransitionModel) cell;
                }
            }
            return null;
        }

        /*
         * Checks if cell is a ArcModel or if a ArcModel is nested in a Group.
         */
        private ArcModel findArcInCell(Vector<Object> cells) {
            for (Object cell : cells) {
                if (cell instanceof ArcModel) {
                    return (ArcModel) cell;
                }
            }
            return null;
        }
    }

    /**
     * will be called by TokenGameBarVC to let active transitions occur
     * 
     * @param transition
     */
    public void occurTransitionbyTokenGameBarVC(TransitionModel transition, boolean BackWard) {
        char checkA = transition.getId().charAt(0);
        if (checkA == 'a') {
            if (BackWard) {
                backwardItem(null, getPetriNet().getElementContainer().getArcById(transition.getId()));
            } else {
                arcClicked(getPetriNet().getElementContainer().getArcById(transition.getId()));
            }
        } else
            if (checkA == 'S') {
                thisEditor.openTokenGameSubProcess((SubProcessModel) transition);
            } else {
                if (BackWard) {
                    backwardItem(transition, null);
                } else {
                    transitionClicked(transition, null);
                }
            }
    }

    /**
     * method to call private checkNet() method from TokenGameBar
     */
    public void tokenGameCheckNet() {
        checkNet();
    }

    /**
     * if the current Editor is a SubProcess, it will be closed.
     */
    public void closeSubProcess() {
        if (thisEditor.isSubprocessEditor()) {
            thisEditor.closeEditor();
        }
    }

    /**
     * this method is similiar to stop(). it will reset all tokens and playactions but it will not activate editor. through play button you could simulate again
     */
    public void tokenGameRestore() {
        deHighlightRG();
        resetTransitionStatus();
        resetArcStatus();
        resetVirtualTokensInElementContainer(getPetriNet().getElementContainer());
        // getGraph().setPortsVisible(true);
        getGraph().refreshNet();
        getGraph().updateUI();
    }

    // If tokengamebar-user chooses to step into a subprocess
    public void setStepIntoSubProcess(boolean step) {
        stepIntoSubProcess = step;

    }

    public IEditor getThisEditor() {
        return thisEditor;
    }

}