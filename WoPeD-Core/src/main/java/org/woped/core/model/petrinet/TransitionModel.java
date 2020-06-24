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
package org.woped.core.model.petrinet;

import java.awt.Point;
import java.util.Collection;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;

/**
 * This class represents a classic transition in a petrinet.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Petri_net">Petri net</a>
 */
@SuppressWarnings("serial")
public class TransitionModel extends AbstractPetriNetElementModel {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private Toolspecific toolSpecific;
    // it is important only in Subprocess
    private boolean incomingTarget;
    private boolean outgoingSource;

    public TransitionModel(CreationMap map) {
        super(map);
        toolSpecific = new Toolspecific(getId());
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        setAttributes(attributes);
    }

    public CreationMap getCreationMap() {
        CreationMap map = super.getCreationMap();
        if (hasTrigger()) {
            map.setTriggerType(getToolSpecific().getTrigger().getTriggertype());
            map.setTriggerPosition(new Point(getToolSpecific().getTrigger().getPosition()));
        } else
            // If no trigger exists, our creation map must reflect this
            // (there might have been a trigger before which was removed
            // during editing)
            map.setTriggerType(-1);
        if (hasResource()) {
            map.setResourceOrgUnit(getToolSpecific().getTransResource().getTransOrgUnitName());
            map.setResourceRole(getToolSpecific().getTransResource().getTransRoleName());
            map.setResourcePosition(new Point(getToolSpecific().getTransResource().getPosition()));
        } else {
            // If no resource org unit exists, our creation map must reflect this
            // (there might have been a resource unit before which was removed
            // during editing)
            map.setResourceOrgUnit(null);
            map.setResourceRole(null);
            map.setResourcePosition(null);
        }
        // Extract transition service time and transition service
        // time unit
        map.setTransitionTime(getToolSpecific().getTime());
        map.setTransitionTimeUnit(getToolSpecific().getTimeUnit());

        return map;
    }

    /**
     * Gets the default height of the transition.
     *
     * @return the default height of the transition.
     */
    public int getDefaultHeight() {
        return HEIGHT;
    }

    /**
     * Gets the default width of the transition.
     *
     * @return the default width of the transition.
     */
    public int getDefaultWidth() {
        return WIDTH;
    }

    /**
     * Gets the number of active places which have an outgoing arc to this transition.
     * <p>
     * Active means, that their token count is greater or equal to the weight of the arc.
     *
     * @return the number of incoming places which are active
     */
    public int getNumIncomingActivePlaces() {
        // TODO: This is an error condition, triggered by a broken model (e.g.
        // TransitionModel has not been created through its factory).
        // Unfortunately, this error condition currently occurs due to the
        // code in TStar.java which generates model elements and arcs that
        // are part of the JGraph model and view, but intentionally excluded
        // from the ModelElementContainer to not disturb semantic analysis.
        // This is broken and should be fixed.
        if (getRootOwningContainer() == null) return 0;

        Collection<ArcModel> arcs = getRootOwningContainer().getIncomingArcs(this.getId()).values();

        int activePlaces = 0;
        for (ArcModel arc : arcs) {
            PlaceModel source = (PlaceModel) getRootOwningContainer().getElementById(arc.getSourceId());
            if (source.getVirtualTokenCount() >= arc.getInscriptionValue()) {
                activePlaces++;
            }
        }

        return activePlaces;
    }

    /**
     * Get the number of places, which have an outgoing arc to this transition.
     * <p>
     * Because of the fact, that an arc must have a source and a target, this number is equal
     * to the number of incoming arcs.
     *
     * @return the number of incoming arcs.
     */
    int getNumInputPlaces() {

        //TODO: This is an error condition, triggered by a broken model (e.g.
        //TransitionModel has not been created through its factory).
        //Unfortunately, this error condition currently occurs due to the
        //code in TStar.java which generates model elements and arcs that
        //are part of the JGraph model and view, but intentionally excluded
        //from the ModelElementContainer to not disturb semantic analysis.
        //This is broken and should be fixed.
        if (getRootOwningContainer() == null) return 0;

        Map<String, ArcModel> incomingArcs = getRootOwningContainer().getIncomingArcs(getId());
        return incomingArcs.size();
    }

    public Point getResourcePosition() {
        if (hasResource()) return getToolSpecific().getTransResource().getPosition();
        else if (toolSpecific.getOperatorPosition() == OperatorPosition.NORTH || toolSpecific.getOperatorPosition() == OperatorPosition.SOUTH) {
            return new Point(getX() - 65, getY() - TransitionResourceModel.DEFAULT_HEIGHT + 5);
        } else {
            return new Point(getX() + 10, getY() - TransitionResourceModel.DEFAULT_HEIGHT - 25);
        }
    }

    /**
     * Gets the WoPeD specific parameter object.
     *
     * @return the WoPeD specific parameter object
     * @see Toolspecific
     */
    public Toolspecific getToolSpecific() {
        return toolSpecific;
    }

    /**
     * Sets the WoPeD specific parameter object.
     *
     * @param toolSpecific the new parameter object to set.
     * @see Toolspecific
     */
    public void setToolSpecific(Toolspecific toolSpecific) {
        this.toolSpecific = toolSpecific;
    }

    public String getToolTipText() {
        return "Transition\nID: " + getId() + "\nName: " + getNameValue();
    }

    public Point getTriggerPosition() {
        if (hasTrigger()) return getToolSpecific().getTrigger().getPosition();
        else if (toolSpecific.getOperatorPosition() == OperatorPosition.NORTH || toolSpecific.getOperatorPosition() == OperatorPosition.SOUTH) {
            return new Point(getX() - 25, getY() + 10);
        } else {
            return new Point(getX() + 10, getY() - 20);
        }
    }

    public int getTriggerType() {
        return (getToolSpecific().getTrigger().getTriggertype());
    }

    public int getType() {
        return AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE;
    }

    public boolean hasResource() {
        return (getToolSpecific().getTransResource() != null);
    }

    public boolean hasTrigger() {
        return (getToolSpecific().getTrigger() != null);
    }

    public boolean isActivated() {
        return (getNumInputPlaces() == getNumIncomingActivePlaces());
    }

    public boolean isIncomingTarget() {
        return incomingTarget;
    }

    void setIncomingTarget(boolean incomingTarget) {
        this.incomingTarget = incomingTarget;
    }

    public boolean isOutgoingSource() {
        return outgoingSource;
    }

    void setOutgoingSource(boolean outgoingSource) {
        this.outgoingSource = outgoingSource;
    }
}