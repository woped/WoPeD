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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.editor.simulation;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.border.LineBorder;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.editor.controller.vep.ViewEvent;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The <code>TokenGameController</code> provides the visual and/or model sided
 * TokenGame of a Petrinet. <br>
 * <br>
 * 1) Visual (and Model) sided TokenGame: <br>
 * You have to set a <code>PetriNet</code> and a <code>WoPeDJGraph</code>.
 * <br>
 * If it's done via the Contructor you have nothing to worry about. <br>
 * If you only set the petrinet via Constructor and the Graph with the setter
 * method, you'll have to enable the visualTokenGame explicit with the method
 * <code>enableVisualTokenGame()</code>.<br>
 * <br>
 * 2) Only model sided TokenGame: <br>
 * If you have set the the <code>PetriNet</code> and the
 * <code>WoPeDJGraph</code> with the Constructor you have to call
 * <code>disableVisualTokenGame()</code>.<br>
 * 
 * Created on 21.09.2004 Last Change on 13.10.2004
 */
public class TokenGameController
{

    private PetriNetModelProcessor petrinet              = null;
    private AbstractGraph          graph                 = null;
    private Map<String, AbstractElementModel>                    allTransitions        = null;
    //! Stores a set containing all sink places of the simulated net
    //! Used to manage return handling for sub-processes
    //! (Visual token game only!)
    private Set<PlaceModel>        sinkPlaces = null;
    private MouseHandler           tokenGameMouseHandler = null;
    private boolean                visualTokenGame       = false;
    private EditorVC			   thisEditor 			 = null;
    
    /**
     * Constructor for the model and visual sided TokenGame.
     * 
     * @param petrinet
     * @param graph
     */
    public TokenGameController(EditorVC thisEditor)
    {
        this.petrinet = (PetriNetModelProcessor)thisEditor.getModelProcessor();
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
    public TokenGameController(PetriNetModelProcessor petrinet)
    {
        this.petrinet = petrinet;
        this.graph = null;
        this.thisEditor = null;
        setVisualTokenGame(false);
        tokenGameMouseHandler = new MouseHandler();
    }

    /* ###################### Controller Methods ###################### */

    /**
     * Starts the TokenGame.
     */
    public void start()
    {
    	
        if (isVisualTokenGame())
        {
            enableVisualTokenGame();
        }
        // Storing Transition Reference (simple and operator)
        allTransitions = getPetriNet().getElementContainer().getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
        allTransitions.putAll(getPetriNet().getElementContainer().getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE));
        allTransitions.putAll(getPetriNet().getElementContainer().getElementsByType(PetriNetModelElement.SUBP_TYPE));
        // Find and show active Transitions/Arcs
        checkNet();
        //animator.start();
    }

    /**
     * Stops the TokenGame. Disables the visual TokenGame if set.
     */
    public void stop()
    {
        // set all transistions/arcs inactive & not fireing
        resetTransitionStatus();
        resetArcStatus();
        // restore origin tokencount
        resetVirtualTokensInElementContainer(getPetriNet().getElementContainer());
        // disable visualTokenGame
        if (isVisualTokenGame())
        {
            disableVisualTokenGame();
        }
        //animator.stop();
    }

    
    //! Reset the virtual token count for the specified element container
    //! If the element container contains subprocess elements,
    //! this method is called recursively to reset token counts in sub-processes
    //! @param container specifies the container that should be processed
    private void resetVirtualTokensInElementContainer(ModelElementContainer container)
    {
        // restore origin tokencount
        Iterator placeIter = container.getElementsByType(PetriNetModelElement.PLACE_TYPE).keySet().iterator();
        while (placeIter.hasNext())
        {
            ((PlaceModel) container.getElementById(placeIter.next())).resetVirtualTokens();
        }    	
        Iterator subpIter = container.getElementsByType(PetriNetModelElement.SUBP_TYPE).keySet().iterator();
        while (subpIter.hasNext())
        {
        	// Now we call ourselves recursively for all sub-processes
        	ModelElementContainer innerContainer =
        		((SubProcessModel) container.getElementById(subpIter.next())).getSimpleTransContainer();
        	resetVirtualTokensInElementContainer(innerContainer);
        }    	        
    }

    /**
     * Enables the visual TokenGame.
     */
    public void enableVisualTokenGame()
    {
        this.visualTokenGame = true;
               
        // disable editor access
        getGraph().enableMarqueehandler(false);
        getGraph().clearSelection();
        getGraph().setEnabled(false);
        getGraph().setPortsVisible(false);
        getGraph().setGridVisible(false);
        getGraph().setBackground(new Color(250, 250, 250));
        getGraph().setBorder(new LineBorder(
		new Color(0, 175, 20), 3, false));
        
        // register own MouseHandler
        getGraph().addMouseListener(tokenGameMouseHandler);
        
        sinkPlaces = new HashSet<PlaceModel>();
        StructuralAnalysis analysis = new StructuralAnalysis(thisEditor);
        Iterator i = analysis.getSinkPlacesIterator();
        while (i.hasNext())
        	sinkPlaces.add((PlaceModel)i.next());               
        
        getGraph().refreshNet();
    }

    /**
     * Disables the visual TokenGame.
     */
    public void disableVisualTokenGame()
    {
        // enalbe editor access
        getGraph().setEnabled(true);
        getGraph().enableMarqueehandler(true);
        getGraph().setPortsVisible(true);
        getGraph().setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());
        getGraph().setBackground(Color.WHITE);
        getGraph().setBorder(null);
        //getGraph().setSelectNewCells(true);
        // remove own MouseHandler
        getGraph().removeMouseListener(tokenGameMouseHandler);
        getGraph().refreshNet();
    }

    /*
     *  
     */
    private void checkNet()
    {
        long begin = System.currentTimeMillis();
        LoggerManager.debug(Constants.EDITOR_LOGGER, "TokenGame: CHECK NET");
        Iterator transIter = allTransitions.keySet().iterator();

        resetArcStatus();
        // Iterate over all Transitions
        while (transIter.hasNext())
        {
            checkTransition((TransitionModel) allTransitions.get(transIter.next()));
        }
        // Have a look at sink places
        // and see whether we need to activate them
        // Do so only inside of sub-processes
        if ((petrinet.getElementContainer().getOwningElement()!=null)&&(sinkPlaces!=null))
        {
        	Iterator<PlaceModel> i = sinkPlaces.iterator();
        	while (i.hasNext())
        	{
        		PlaceModel currentSink = i.next();
        		currentSink.setActivated(currentSink.getVirtualTokenCount()>0);
        	}        	
        }              
        getGraph().updateUI();

        LoggerManager.debug(Constants.EDITOR_LOGGER, "           ... DONE (" + (System.currentTimeMillis() - begin) + " ms)");
    }

    /*
     *  
     */
    private void checkTransition(TransitionModel transition)
    {
        transition.setActivated(false);
        transition.setFireing(false);
        Map incomingArcs = getPetriNet().getElementContainer().getIncomingArcs(transition.getId());
        // temporary variables

        if (transition.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE || transition.getType() == PetriNetModelElement.SUBP_TYPE)
        {
            if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size()) transition.setActivated(true);
        } else if (transition.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
        {
            OperatorTransitionModel operator = (OperatorTransitionModel) transition;
            if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE 
            		|| operator.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
            {
                if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size()) transition.setActivated(true);

            } else if ((operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)||
            		(operator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE))
            {
                if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size())
                {
                    setOutgoingArcsActive(transition.getId(), true);
                    operator.setFireing(true);
                }
            } else if ((operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)||
            		(operator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE))
            {
                if (countIncomingActivePlaces(incomingArcs) > 0)
                {
                    setIncomingArcsActive(transition.getId(), true);
                    operator.setFireing(true);
                }
            } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
            {
                // This is the XOR split-join combination type.
                // Check whether the center place already contains (a) token(s)
                if ((operator.getCenterPlace() != null) && (operator.getCenterPlace().getVirtualTokenCount() > 0))
                {
                    // The center place does in fact have at least one token.
                    // We have to make the outgoing arcs active
                    setOutgoingArcsActive(transition.getId(), true);
                    // Set this transition active
                    operator.setFireing(true);
                } else
                {
                    // There must at least be one token at the input side for
                    // the transition to be
                    // activated
                    if (countIncomingActivePlaces(incomingArcs) > 0)
                    {
                        // Activate all incoming arcs. This will allow the user
                        // to click them
                        // and choose where the token will come from
                        setIncomingArcsActive(transition.getId(), true);
                        // Set this transition active.
                        operator.setFireing(true);
                    }
                }
            }
        }
    }

    /*
     * Handles a click on any Transition in any state
     */
    private void transitionClicked(TransitionModel transition, MouseEvent e)
    {
        if (transition.isActivated())
        {
            // Rememeber whether we actually did something here
            // and only deactivate the transition after a *successful* click
            boolean actionPerformed = false;
            if (transition.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE || transition.getType() == PetriNetModelElement.SUBP_TYPE)
            {
                //LoggerManager.debug(Constants.EDITOR_LOGGER, "TokenGame: FIRE
                // simple Transition:
                // "+transition.getId());
                receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
                if (transition.getType() == PetriNetModelElement.SUBP_TYPE)
                {
                	
                	int relativeX = e.getX() - transition.getX();
                	int relativeY = e.getY() - transition.getY();
                	// the lower left half of the transition will trigger 'step into'
                	if (relativeY>=relativeX)
                		// Step into sub-process and process it in a new modal editor
                		// dialog in token-game mode
                		thisEditor.openTokenGameSubProcess((SubProcessModel)transition);
                }
                actionPerformed = true;
            } else if (transition.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                OperatorTransitionModel operator = (OperatorTransitionModel) transition;
                if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
                		|| operator.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
                {
                    //LoggerManager.debug(Constants.EDITOR_LOGGER, "TokenGame:
                    // FIRE AND-Transition:
                    // "+transition.getId());
                    receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                    sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
                    actionPerformed = true;

                } else if ((operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)||
                		(operator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE))
                {
                    // Do nothing: Only controlled by Arc Clicking
                } else if ((operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)||
                		(operator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE))
                {
                    // Do nothing: Only controlled by Arc Clicking
                } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
                {
                    // Do nothing: Only controlled by Arc Clicking as the user
                    // has to select the
                    // token source
                }
            }
            if (actionPerformed == true)
            {
                // Now update the status of the petri net by checking all
                // transitions and activating them
                // if their input conditions are fulfilled
                // This will also trigger a redraw
                checkNet();
            }
        }
    }

    /*
     * Handles a click on any arc in any state
     */
    private void arcClicked(ArcModel arc)
    {
        if (arc.isActivated())
        {
            PetriNetModelElement source = (PetriNetModelElement) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
            PetriNetModelElement target = (PetriNetModelElement) getPetriNet().getElementContainer().getElementById(arc.getTargetId());

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
            if (source.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                tempOperator = (OperatorTransitionModel) source;
                if (tempOperator.isFireing())
                {
                    if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE || tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE ||
                    		tempOperator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)
                    {
                        receiveTokens(arc);
                        if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) sendTokens(getPetriNet().getElementContainer().getIncomingArcs(tempOperator.getId()));
                        else
                        {
                            // Special code for splitjoin. We have to take the
                            // token from the center place
                            if (tempOperator.getCenterPlace() != null)
                            // FIXME: Once implemented, this place will also
                            // have to remove weighted tokens
                            tempOperator.getCenterPlace().sendToken();
                        }
                    }
                }
            } else if (target.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                tempOperator = (OperatorTransitionModel) target;
                if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE ||
                		tempOperator.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE ||
                		tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
                {
                    sendTokens(arc);
                    if (tempOperator.getOperatorType() != OperatorTransitionModel.XOR_SPLITJOIN_TYPE) receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(tempOperator.getId()));
                    else
                    {
                        // Special code for splitjoin. We have to send the token
                        // to the center place
                        if (tempOperator.getCenterPlace() != null)
                        // FIXME: Once implemented, this place will also have to
                        // receive weighted tokens
                        tempOperator.getCenterPlace().receiveToken();
                    }
                }
            }
            // Update net status
            // and trigger redraw
            checkNet();

        }
    }

    /*
     * Counts the token-filled Places which are the source of the Map filled
     * Arcs. ATTENTION: use only for Arcs with Places as source!
     */
    private int countIncomingActivePlaces(Map arcsFromPlaces)
    {
        Iterator incomingArcsIter = arcsFromPlaces.keySet().iterator();
        int activePlaces = 0;
        while (incomingArcsIter.hasNext())
        {
            ArcModel arc = getPetriNet().getElementContainer().getArcById(incomingArcsIter.next());
            try
            {
                PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
                if (place != null && place.getVirtualTokenCount() > 0)
                {
                    // 	TODO: when ARC WEIGTH implemented check tokens >= weigth
                    activePlaces++;
                }
            } catch (ClassCastException cce)
            {
                LoggerManager.warn(Constants.EDITOR_LOGGER, "TokenGame: Source not a Place. Ignore arc: " + arc.getId());
            }
        }
        return activePlaces;
    }

    /*
     * Sets incoming Arcs of an Transition active, if the Place before has
     * Tokens. Sets incoming Arcs of an Transition inactive, without any
     * condition.
     */
    private void setIncomingArcsActive(Object transitionId, boolean active)
    {
        Iterator incomingArcsIter = getPetriNet().getElementContainer().getIncomingArcs(transitionId).keySet().iterator();
        while (incomingArcsIter.hasNext())
        {
            ArcModel arc = getPetriNet().getElementContainer().getArcById(incomingArcsIter.next());
            if (active)
            {
                PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
                if (place.getVirtualTokenCount() > 0)
                {
                    arc.setActivated(true);
                    // 	TODO: check weight
                }
            } else
            {
                arc.setActivated(false);
            }
        }
    }

    /*
     * Sets all outgoing arcs of an Transition active/inactive, without any
     * condition.
     */
    private void setOutgoingArcsActive(Object transitionId, boolean active)
    {
        Iterator outgoingIter = getPetriNet().getElementContainer().getOutgoingArcs(transitionId).keySet().iterator();
        while (outgoingIter.hasNext())
        {
            getPetriNet().getElementContainer().getArcById(outgoingIter.next()).setActivated(active);
        }
    }

    /*
     * Send (subtract) Tokens from the places that are the source of the
     * Map-filled arcs. ATTENTION: source ot the arcs must be always a place
     */
    private void sendTokens(Map arcsToFire)
    {
        Iterator arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext())
        {
            sendTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));

        }
    }

    /*
     * Send (subtract) Tokens from the arc's source. ATTENTION: source must be a
     * Place.
     */
    private void sendTokens(ArcModel arc)
    {
        try
        {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getSourceId());
            if (place != null)
            {
                place.sendToken();
                // TODO: when ARC WEIGTH implemented send tokens weigth times
            }
        } catch (ClassCastException cce)
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "TokenGame: Cannot send token. Source is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Receive (add) Tokens to the places that are the target or the Map-filled
     * arcs. ATTENTION: targets must be places.
     */
    private void receiveTokens(Map arcsToFire)
    {
        Iterator arcIter = arcsToFire.keySet().iterator();
        while (arcIter.hasNext())
        {
            receiveTokens(getPetriNet().getElementContainer().getArcById(arcIter.next()));
        }
    }

    /*
     * Receive (add) Tokens to the place that is the target of the arc.
     * ATTENTION: Target must be a place.
     */
    private void receiveTokens(ArcModel arc)
    {
        try
        {
            PlaceModel place = (PlaceModel) getPetriNet().getElementContainer().getElementById(arc.getTargetId());
            if (place != null)
            {
                place.receiveToken();
                // TODO: when ARC WEIGTH implemented receive tokens weigth times
            }
        } catch (ClassCastException cce)
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "TokenGame: Cannot receive token. Target is not a place. Ignore arc: " + arc.getId());
        }
    }

    /*
     * Disables all Transitions. Set each transition inactive and not firing.
     * Used on stop.
     */
    private void resetTransitionStatus()
    {
        Iterator eleIter = allTransitions.keySet().iterator();
        TransitionModel transition;
        // Iterate over all Transitions
        while (eleIter.hasNext())
        {
            transition = (TransitionModel) allTransitions.get(eleIter.next());
            transition.setActivated(false);
            transition.setFireing(false);
            if (transition.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                // When starting a new token game we have to reset all center
                // places that
                // may contain tokens
                OperatorTransitionModel tempOperator = (OperatorTransitionModel) transition;
                if (tempOperator.getCenterPlace() != null) tempOperator.getCenterPlace().resetVirtualTokens();
            }
        }
    }

    /*
     * Inactivate all arcs.
     */
    private void resetArcStatus()
    {
        Iterator arcIter = getPetriNet().getElementContainer().getArcMap().keySet().iterator();
        while (arcIter.hasNext())
        {
            getPetriNet().getElementContainer().getArcById(arcIter.next()).setActivated(false);
        }
    }

    /*
     * Returns a Map containing the fireing Transitions.
     */
/*    private Map getFireingTransitions()
    {
        Iterator transIter = allTransitions.keySet().iterator();
        TransitionModel transition;
        Map fireingTrans = new HashMap();

        while (transIter.hasNext())
        {
            transition = (TransitionModel) getPetriNet().getElementContainer().getElementById(transIter.next());
            // save if the transition is fireing
            if (transition.isFireing())
            {
                fireingTrans.put(transition.getId(), transition);
            }
        }
        return fireingTrans;
    }
*/
    /*
     * ################################### Getter & Setter
     * ########################################
     */

    /**
     * Returns if the visual TokenGame is enabled.
     * 
     * @return Returns the visualTokenGame.
     */
    public boolean isVisualTokenGame()
    {
        return visualTokenGame;
    }

    /*
     * use with caution. does not affect the TokenGame Controlling directly.
     * Just for setting the initial variable value. @param visualTokenGame The
     * visualTokenGame to set.
     */
    private void setVisualTokenGame(boolean visualTokenGame)
    {
        this.visualTokenGame = visualTokenGame;
    }

    /*
     * @return Returns the graph.
     */
    private AbstractGraph getGraph()
    {
        return graph;
    }

    /*
     * @param graph The graph to set.
     */
/*    private void setGraph(WoPeDJGraph graph)
    {
        this.graph = graph;
    }
*/
    /*
     * @return Returns the petrinet.
     */
    private PetriNetModelProcessor getPetriNet()
    {
        return petrinet;
    }

    /*
     * @param petrinet The petrinet to set.
     */
/*    private void setPetriNet(PetriNetModelProcessor petrinet)
    {
        this.petrinet = petrinet;
    }
*/
    /*
     * ################################### Mouse Handler
     * ########################################
     */

    /*
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br><br>
     * 
     * MouseHandle for the TokenGame. USE ONLY if visual TokenGame is ENABLED.
     */
    private class MouseHandler implements MouseListener
    {

        /**
         * if clicked on a transition that is active, it will be set in firesate
         */
        public void mousePressed(final MouseEvent e)
        {
            TransitionModel transition = findTransitionInCell(getGraph().getFirstCellForLocation(e.getPoint().x, e.getPoint().y));
            if (transition != null && transition.isActivated())
            {
                transition.setFireing(true);
            }
            e.consume();
        }

        /**
         *  
         */
        public void mouseReleased(MouseEvent e)
        {
        	Object cell = getGraph().getFirstCellForLocation(e.getPoint().x, e.getPoint().y);
            TransitionModel transition = findTransitionInCell(cell);
            PlaceModel place = findPlaceInCell(cell);
            ArcModel arc = findArcInCell(cell);
            if (transition != null && transition.isActivated() && transition.isFireing())
            {
                transitionClicked(transition, e);
            } else if (arc != null && arc.isActivated())
            {
                arcClicked(arc);
            } else if (place != null && place.isActivated())
            {            	
            	// If an active place has been clicked there is only one reasonable explanation for this:
            	// It is a sink place of a sub-process and we need to close the sub-process editing window
                thisEditor.fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE, null));

            }
            e.consume();
        }

        public void mouseMoved(MouseEvent arg0)
        {}

        public void mouseClicked(MouseEvent arg0)
        {}

        public void mouseEntered(MouseEvent arg0)
        {}

        public void mouseExited(MouseEvent arg0)
        {}


        /*
         * Checks if cell is a Place or if a Place is nested in a
         * Group.
         */
        private PlaceModel findPlaceInCell(Object cell)
        {
            if (cell instanceof GroupModel)
            {
                cell = ((GroupModel) cell).getMainElement();
            }
            if (cell instanceof PlaceModel)
            {
                return (PlaceModel) cell;
            } else
            {
                return null;
            }
        }
        
        /*
         * Checks if cell is a Transition or if a Transition is nested in a
         * Group.
         */
        private TransitionModel findTransitionInCell(Object cell)
        {
            if (cell instanceof GroupModel)
            {
                cell = ((GroupModel) cell).getMainElement();
            }
            if (cell instanceof TransitionModel || cell instanceof OperatorTransitionModel)
            {
                return (TransitionModel) cell;
            } else
            {
                return null;
            }
        }

        /*
         * Checks if cell is a ArcModel or if a ArcModel is nested in a Group.
         */
        private ArcModel findArcInCell(Object cell)
        {
            if (cell instanceof GroupModel)
            {
                cell = ((GroupModel) cell).getMainElement();
            }
            if (cell instanceof ArcModel)
            {
                return (ArcModel) cell;
            } else
            {
                return null;
            }
        }
    }

}