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
package org.woped.simulation;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.woped.config.ConfigurationManager;
import org.woped.controller.AbstractGraph;
import org.woped.controller.WoPeDJGraph;
import org.woped.model.ArcModel;
import org.woped.model.PetriNetModelProcessor;
import org.woped.model.petrinet.GroupModel;
import org.woped.model.petrinet.OperatorTransitionModel;
import org.woped.model.petrinet.PetriNetModelElement;
import org.woped.model.petrinet.PlaceModel;
import org.woped.model.petrinet.TransitionModel;
import org.woped.utilities.WoPeDLogger;

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
public class TokenGameController implements WoPeDLogger
{

    private PetriNetModelProcessor petrinet              = null;
    private AbstractGraph          graph                 = null;
    private Map                    allTransitions        = null;
    private MouseHandler           tokenGameMouseHandler = null;
    private boolean                visualTokenGame       = false;

    /**
     * Constructor for the model and visual sided TokenGame.
     * 
     * @param petrinet
     * @param graph
     */
    public TokenGameController(PetriNetModelProcessor petrinet, AbstractGraph graph)
    {
        this.petrinet = petrinet;
        this.graph = graph;
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
        this(petrinet, null);
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
        Iterator placeIter = getPetriNet().getElementContainer().getElementsByType(PetriNetModelElement.PLACE_TYPE).keySet().iterator();
        while (placeIter.hasNext())
        {
            ((PlaceModel) getPetriNet().getElementContainer().getElementById(placeIter.next())).resetVirtualTokens();
        }
        // disable visualTokenGame
        if (isVisualTokenGame())
        {
            disableVisualTokenGame();
        }
        //animator.stop();
    }

    /**
     * Enables the visual TokenGame.
     */
    public void enableVisualTokenGame()
    {
        if (getGraph() != null)
        {
            this.visualTokenGame = true;
            // disable editor access
            getGraph().enableMarqueehandler(false);
            getGraph().clearSelection();
            getGraph().setEnabled(false);
            getGraph().setPortsVisible(false);
            getGraph().setGridVisible(false);
            getGraph().setBackground(new Color(245, 245, 230));
            // register own MouseHandler
            getGraph().addMouseListener(tokenGameMouseHandler);
            getGraph().validate();
            getGraph().updateUI();
        } else
        {
            logger.error("You cannot enable the visual TokenGame without a JGraph Object.");
        }
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
        //getGraph().setSelectNewCells(true);
        getGraph().setBackground(Color.WHITE);
        // remove own MouseHandler
        getGraph().removeMouseListener(tokenGameMouseHandler);
        getGraph().updateUI();
    }

    /*
     *  
     */
    private void checkNet()
    {
        long begin = System.currentTimeMillis();
        logger.debug("TokenGame: CHECK NET");
        Iterator transIter = allTransitions.keySet().iterator();
        TransitionModel transition;
        resetArcStatus();
        // Iterate over all Transitions
        while (transIter.hasNext())
        {
            checkTransition((TransitionModel) allTransitions.get(transIter.next()));
        }
        getGraph().updateUI();
        logger.debug("           ... DONE (" + (System.currentTimeMillis() - begin) + " ms)");
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
        int activePlaces = 0;
        if (transition.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE || transition.getType() == PetriNetModelElement.SUBP_TYPE)
        {
            if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size()) transition.setActivated(true);
        } else if (transition.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
        {
            OperatorTransitionModel operator = (OperatorTransitionModel) transition;
            if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE)
            {
                if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size()) transition.setActivated(true);

            } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE || operator.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
            {
                if (countIncomingActivePlaces(incomingArcs) == incomingArcs.size())
                {
                    setOutgoingArcsActive(transition.getId(), true);
                    operator.setFireing(true);
                }
            } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
            {
                if (countIncomingActivePlaces(incomingArcs) > 0)
                {
                    setIncomingArcsActive(transition.getId(), true);
                    operator.setFireing(true);
                }
            }
        }
    }

    /*
     * Handels a click on any Transition in any state
     */
    private void transitionClicked(TransitionModel transition)
    {
        if (transition.isActivated())
        {
            if (transition.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE || transition.getType() == PetriNetModelElement.SUBP_TYPE)
            {
                //logger.debug("TokenGame: FIRE simple Transition:
                // "+transition.getId());
                receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));
            } else if (transition.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                OperatorTransitionModel operator = (OperatorTransitionModel) transition;
                if (operator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE || operator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE)
                {
                    //logger.debug("TokenGame: FIRE AND-Transition:
                    // "+transition.getId());
                    receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(transition.getId()));
                    sendTokens(getPetriNet().getElementContainer().getIncomingArcs(transition.getId()));

                } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
                {
                    // Do nothing: Only controlled by Arc Clicking
                } else if (operator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
                {
                    // Do nothing: Only controlled by Arc Clicking
                } else if (operator.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
                {
                    //logger.debug("TokenGame: FIRE ORSPLIT-Transition:
                    // "+transition.getId());
                    sendTokens(getPetriNet().getElementContainer().getIncomingArcs(operator.getId()));
                }
            }
            transition.setActivated(false);
            transition.setFireing(false);
            checkNet();
            getGraph().updateUI();
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

            if (source.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                tempOperator = (OperatorTransitionModel) source;
                if (tempOperator.isFireing())
                {
                    if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE || tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
                    {
                        receiveTokens(arc);
                        sendTokens(getPetriNet().getElementContainer().getIncomingArcs(tempOperator.getId()));
                        setOutgoingArcsActive(tempOperator.getId(), false);
                        arc.setActivated(false);
                        checkNet();
                    } else if (tempOperator.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
                    {
                        // memorize attribute of transtiton
                        Vector activeArcIds = new Vector();
                        Iterator iter = getPetriNet().getElementContainer().getOutgoingArcs(tempOperator.getId()).keySet().iterator();
                        while (iter.hasNext())
                        {
                            Object arcId = iter.next();
                            if (getPetriNet().getElementContainer().getArcById(arcId).isActivated())
                            {
                                activeArcIds.add(arcId);
                            }
                        }
                        // lock net
                        resetArcStatus();
                        resetTransitionStatus();
                        // set status before lock
                        int activeArcCouter = 0;
                        for (int i = 0; i < activeArcIds.size(); i++)
                        {
                            if (activeArcIds.get(i) != arc.getId())
                            {
                                getPetriNet().getElementContainer().getArcById(activeArcIds.get(i)).setActivated(true);
                                tempOperator.setFireing(true);
                            }
                        }
                        // if no active arcs left => finish
                        tempOperator.setActivated(true);
                        if (!tempOperator.isFireing()) transitionClicked(tempOperator);
                        // receive Token(s) for the target place
                        receiveTokens(arc);
                    }
                }
            } else if (target.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                tempOperator = (OperatorTransitionModel) target;
                if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE || tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
                {
                    sendTokens(arc);
                    receiveTokens(getPetriNet().getElementContainer().getOutgoingArcs(tempOperator.getId()));
                    arc.setActivated(false);
                    checkNet();
                }
            }
            getGraph().updateUI();
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
                logger.warn("TokenGame: Source not a Place. Ignore arc: " + arc.getId());
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
            logger.warn("TokenGame: Cannot send token. Source is not a place. Ignore arc: " + arc.getId());
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
            logger.warn("TokenGame: Cannot receive token. Target is not a place. Ignore arc: " + arc.getId());
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
    private Map getFireingTransitions()
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
    private void setGraph(WoPeDJGraph graph)
    {
        this.graph = graph;
    }

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
    private void setPetriNet(PetriNetModelProcessor petrinet)
    {
        this.petrinet = petrinet;
    }

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
            TransitionModel transition = findTransitionInCell(getGraph().getFirstCellForLocation(e.getPoint().x, e.getPoint().y));
            ArcModel arc = findArcInCell(getGraph().getFirstCellForLocation(e.getPoint().x, e.getPoint().y));
            if (transition != null && transition.isActivated() && transition.isFireing())
            {
                transitionClicked(transition);
            } else if (arc != null && arc.isActivated())
            {
                arcClicked(arc);
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