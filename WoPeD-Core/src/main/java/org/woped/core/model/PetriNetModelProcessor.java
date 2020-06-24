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
package org.woped.core.model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.jgraph.graph.DefaultPort;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.Toolspecific;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         <p>
 *         Represents a Petri-Net. It is the main access point to the Model.
 *         Contains the ModelElementFactory and the ModelElementContainer <br>
 *         <br>
 *         Created on 29.04.2003
 */

public class PetriNetModelProcessor implements Serializable {

    static final int PNMLFILE = 1;
    private static final long serialVersionUID = 1L;
    private String type = "http://www.informatik.hu-berlin.de/top/pntd/ptNetb";
    private Dimension netWindowSize = null;
    private int placeCouter = 0;
    private int transitionCounter = 0;
    private int subprocessCounter = 0;
    private int arcCounter = 0;
    private int simulationCounter = 0;
    private Vector<Object> unknownToolSpecs = new Vector<Object>();
    private Vector<ResourceClassModel> roles = null;
    private Vector<ResourceModel> resources = null;
    private Vector<ResourceClassModel> organizationUnits = null;
    private Vector<SimulationModel> simulations = null;
    private HashMap<String, Vector<String>> resourceMapping = new HashMap<String, Vector<String>>();
    private String id = null;
    private String name = null;
    private ModelProcessorContext modelProcessorContext = null;
    private ModelElementContainer elementContainer = null;

    /**
     * Creates a new {@code PetriNetModelProcessor}.
     * <p>
     * The new instance is going to have the id "noID".
     */
    public PetriNetModelProcessor() {
        this("noID");
    }

    /**
     * Creates a new {@code PetriNetModelProcessor} with the given id.
     *
     * @param id the id of the {@code PetriNetModelProcessor}.
     */
    public PetriNetModelProcessor(String id) {
        elementContainer = new ModelElementContainer();
        modelProcessorContext = new ModelProcessorContext();

		/* First thing for all: creating a ModelElementContainer */
        setId(id);

    }

    /**
     * Creates a new {@link AbstractPetriNetElementModel}.
     *
     * @param map the map containing the parameters of the new element.
     * @return the new created element or null, if the map is not valid.
     */
    public AbstractPetriNetElementModel createElement(CreationMap map) {
        // creating a new ModelElement through Factory
        if ( map.isValid() ) {
            if ( map.getId() == null ) {
                map.setId(getNewElementId(map.getType()));
            }
            AbstractPetriNetElementModel anElement = ModelElementFactory.createModelElement(map);
            getElementContainer().addElement(anElement);

            if ( map.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE || map.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE || map.getType() == AbstractPetriNetElementModel.SUBP_TYPE ) {
                // Trigger
                if ( map.getTriggerType() != -1 ) {
                    ((TransitionModel) anElement).getToolSpecific().setTrigger(newTrigger(map));


                    if ( map.getTriggerPosition() != null ) {
                        ((TransitionModel) anElement).getToolSpecific().getTrigger().setPosition(map.getTriggerPosition().x, map.getTriggerPosition().y);
                    }
                }
                if ( map.getResourceOrgUnit() != null && map.getResourceRole() != null ) {
                    newTransResource(map);
                    if ( map.getResourcePosition() != null ) {
                        ((TransitionModel) anElement).getToolSpecific().getTransResource().setPosition(map.getResourcePosition().x, map.getResourcePosition().y);
                    }
                }
                if ( map.getTransitionTime() != -1 ) {
                    ((TransitionModel) anElement).getToolSpecific().setTime(map.getTransitionTime());
                }
                if ( map.getTransitionTimeUnit() != -1 ) {
                    ((TransitionModel) anElement).getToolSpecific().setTimeUnit(map.getTransitionTimeUnit());
                }
            }
            return anElement;
        }
        return null;
    }

    /**
     * Creates an new arc from the source to the target.
     * <p>
     * This Method is called, when Elements get connected. If the source or the
     * target is an Operator, the internal transformation to the classic
     * petrinet will be done.
     *
     * @param sourceId the id of the source of the arc, not null
     * @param targetId the id of the target of the arc, not null
     * @return ArcModel the new arc or null if one of the parameters is invalid.
     */
    public ArcModel createArc(Object sourceId, Object targetId) {
        return createArc(null, sourceId, targetId, new Point2D[0], true);
    }

    /**
     * Creates an new arc from the source to the target via the given points.
     * <p>
     * This Method is called, when Elements get connected. It only checks the
     * connection for AalstElements and guarantees the building of a classical
     * Petrinet in The Model, if transformOperators is true.
     *
     * @param id                 the id of the arc
     * @param sourceId           the id of the source, not null
     * @param targetId           the id of the target, not null
     * @param points             the points describing the route of the arc, may be null
     * @param transformOperators indicator if the inner connection of an operator type should be processed
     * @return ArcModel the new arc or null, if one of the parameters is invalid
     */
    public ArcModel createArc(String id, Object sourceId, Object targetId, Point2D[] points, boolean transformOperators) {

        AbstractPetriNetElementModel sourceModel = getElementContainer().getElementById(sourceId);
        AbstractPetriNetElementModel targetModel = getElementContainer().getElementById(targetId);

        // Ensures that the connection is valid
        if ( (sourceModel == null) || (targetModel == null) ) {
            return null;
        }

        // Ensures that the id is fresh
        if ( id == null || this.getElementContainer().getArcById(id) != null ) {
            id = getNexArcId();
        }


        ArcModel displayedArc = ModelElementFactory.createArcModel(id, (DefaultPort) sourceModel.getChildAt(0), (DefaultPort) targetModel.getChildAt(0));
        displayedArc.setPoints(points);

        insertArc(displayedArc, transformOperators);
        return displayedArc;
    }

    public void insertArc(ArcModel arc, boolean transformOperators) {

        // add arc to net
        AbstractPetriNetElementModel sourceModel = getElementContainer().getElementById(arc.getSourceId());
        AbstractPetriNetElementModel targetModel = getElementContainer().getElementById(arc.getTargetId());
        getElementContainer().addReference(arc);

        if ( transformOperators ) {
            resolveInnerConnections(sourceModel, targetModel);
        }
    }

    private void resolveInnerConnections(AbstractPetriNetElementModel sourceModel, AbstractPetriNetElementModel targetModel) {
        OperatorTransitionModel operatorModel;
            /* IF THE SOURCE IS AN OPERATOR */
        if ( sourceModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE ) {

            // storing the source as operator
            operatorModel = (OperatorTransitionModel) sourceModel;
            // put the Target in the SimpleContainer of the operator
            operatorModel.addElement(getElementContainer().getElementById(targetModel.getId()));

            LoggerManager.debug(Constants.CORE_LOGGER, "Connection from Aalst Model detected... resolve Inner-Transitions ...");

            // Register new outgoing connection with the operator.
            // This will generate all the necessary inner arcs
            operatorModel.registerOutgoingConnection(this, targetModel);

            LoggerManager.debug(Constants.CORE_LOGGER, "... Inner-Transition resolving completed");
        }
        /* IF OPERATOR IS TARGET */
        else if ( targetModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE ) {

            LoggerManager.debug(Constants.CORE_LOGGER, "Connection to Aalst Model detected... resolve Inner-Transitions ...");
            // store the target as operatorModel
            operatorModel = (OperatorTransitionModel) getElementContainer().getElementById(targetModel.getId());

            // add the source to the SimpleTransContainer
            operatorModel.addElement(getElementContainer().getElementById(sourceModel.getId()));

            // Register new incoming connection with the operator.
            // This will generate all the necessary inner arcs
            operatorModel.registerIncomingConnection(this, sourceModel);

            LoggerManager.debug(Constants.CORE_LOGGER, "... Inner-Transition resolving completed");
        }
    }

    /**
     * Deletes the arc with the given id from the petrinet.
     *
     * @param arcId the id of the arc to remove.
     */
    public void removeArc(Object arcId) {

        ArcModel arcToDelete = getElementContainer().getArcById(arcId);

        // Ensure arc exists
        if ( null == arcToDelete ) {
            return;
        }

        AbstractPetriNetElementModel sourceElement = getElementContainer().getElementById(arcToDelete.getSourceId());
        AbstractPetriNetElementModel targetElement = getElementContainer().getElementById(arcToDelete.getTargetId());

        if ( sourceElement.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE ) {
            OperatorTransitionModel currentOperator = (OperatorTransitionModel) sourceElement;
            // Register the removal of an outgoing arc
            currentOperator.registerOutgoingConnectionRemoval(this, targetElement);

            currentOperator.getSimpleTransContainer().removeIncomingArcsFromElement(arcToDelete.getTargetId());

            // Each inner transition container contains a local copy of each
            // element connecting to an operator
            // We have to remove this local copy
            currentOperator.getSimpleTransContainer().removeElement(targetElement.getId());

        } else if ( targetElement.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE ) {
            OperatorTransitionModel currentOperator = (OperatorTransitionModel) targetElement;
            // Register the removal of an incoming arc
            currentOperator.registerIncomingConnectionRemoval(this, sourceElement);

            currentOperator.getSimpleTransContainer().removeOutgoingArcsFromElement(arcToDelete.getSourceId());

            // Each inner transition container contains a local copy of each
            // element connecting to an operator
            // We have to remove this local copy
            currentOperator.getSimpleTransContainer().removeElement(sourceElement.getId());

            LoggerManager.debug(Constants.CORE_LOGGER, "INNER ARC TO SOURCE deleted");
        }

        // remove element from the subprocess model
        if ( targetElement.getType() == AbstractPetriNetElementModel.SUBP_TYPE ) {
            ((SubProcessModel) targetElement).getSimpleTransContainer().removeElement(sourceElement.getId());
        }

        if ( sourceElement.getType() == AbstractPetriNetElementModel.SUBP_TYPE ) {
            ((SubProcessModel) sourceElement).getSimpleTransContainer().removeElement(targetElement.getId());
        }

        getElementContainer().removeArc(arcToDelete);

    }

    /**
     * Creates a new Trigger and adds it to the Transtition
     */
    public TriggerModel newTrigger(CreationMap map) {
        TransitionModel transition = (TransitionModel) getElementContainer().getElementById(map.getId());
        if ( transition.getToolSpecific() == null ) {
            transition.setToolSpecific(new Toolspecific(transition.getId()));
        }
        return transition.getToolSpecific().setTrigger(map);

    }

    // TODO: DOCUMentation
    public TransitionResourceModel newTransResource(CreationMap map) {
        TransitionModel transition = (TransitionModel) getElementContainer().getElementById(map.getId());
        if ( transition.getToolSpecific() == null ) {
            transition.setToolSpecific(new Toolspecific(transition.getId()));
        }
        return transition.getToolSpecific().setTransResource(map);
    }

    public String getNewElementId(int elementType) {
        String id = null;

        String prefix = "";
        AbstractPetriNetElementModel owningElement = getElementContainer().getOwningElement();
        if ( owningElement != null ) {
            prefix = owningElement.getId() + OperatorTransitionModel.INNERID_SEPERATOR;
        }

        switch (elementType) {
            case AbstractPetriNetElementModel.SUBP_TYPE:
                id = "sub" + ++subprocessCounter;
                break;
            case AbstractPetriNetElementModel.PLACE_TYPE:
                id = "p" + ++placeCouter;
                break;
            case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
            case AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE:
                id = "t" + ++transitionCounter;
                break;
            case AbstractPetriNetElementModel.SIMULATION_TYPE:
                id = "s" + ++simulationCounter;
                break;
        }
        if ( id != null ) {
            // Prepend the prefix (used for sub-processes to make identifiers
            // unique)
            id = prefix + id;
            // Check whether an element with the same ID already exists.
            // If so, recursively call ourselves to obtain a new one
            if ( getElementContainer().getElementById(id) != null ) {
                id = getNewElementId(elementType);
            }
        }
        return id;
    }

    public String getNexArcId() {
        String arcId = "a" + ++arcCounter;
        return getElementContainer().getArcById(arcId) != null ? getNexArcId() : arcId;
    }

    public void addResourceMapping(String resourceClass, String resourceId) {
        Vector<String> resourcesVector;
        if ( containsRole(resourceClass) != -1 || containsOrgunit(resourceClass) != -1 ) {
            if ( containsResource(resourceId) != -1 ) {
                if ( getResourceMapping().get(resourceClass) == null ) {
                    resourcesVector = new Vector<String>();
                    resourcesVector.add(resourceId);
                    getResourceMapping().put(resourceClass, resourcesVector);
                } else {
                    getResourceMapping().get(resourceClass).add(resourceId);
                }
            } else {
                LoggerManager.warn(Constants.CORE_LOGGER, "resource doesn't exist");
            }

        } else {
            LoggerManager.warn(Constants.CORE_LOGGER, "role or orgUnit doesn't exist");
        }

    }

    public void removeResourceMapping(String resourceClass, String resourceId) {
        Vector<String> tempResourceVector;
        tempResourceVector = getResourceMapping().get(resourceClass);
        tempResourceVector.remove(resourceId);
    }

    /**
     * @param resourceId
     * @return
     */
    public Vector<String> getResourceClassesResourceIsAssignedTo(String resourceId) {
        Vector<String> assignedVector = new Vector<String>();
        String resourceClassIdTemp;
        for ( Iterator<String> iter = getResourceMapping().keySet().iterator(); iter.hasNext(); ) {
            resourceClassIdTemp = iter.next().toString();
            Vector<String> resourceVector = getResourceMapping().get(resourceClassIdTemp);
            if ( containsResource(resourceVector, resourceId) != -1 ) {
                assignedVector.add(resourceClassIdTemp);
            }
        }
        return assignedVector;

    }

    /**
     * @return
     */
    public Dimension getNetWindowSize() {
        return netWindowSize;
    }

    /**
     * @param dimension
     */
    public void setNetWindowSize(Dimension dimension) {
        netWindowSize = dimension;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Gets a string representing the logical structure of the petrinet.
     * <p>
     * The fingerprint is represented as a String with the following components:
     * <ul>
     * <li>number of places</li>
     * <li>place details</li>
     * <li>a part delimiter (#)</li>
     * <li>number of arcs</li>
     * <li>arc details</li>
     * </ul>
     * <p>
     * The place details consist of the place id and the amount of tokens for each place.
     * For example place p0 with 2 tokens: "p02".
     * <p>
     * The arc details consists of the source id and the target id of each arc. The arc id is not
     * appropriate for this purpose, because it is possible they could be different after saving/loading a net.
     * Each arc is preceded with a single {@code *}.
     * For example, an arc form p1 to t1 is listed as "*p1t1"
     * <p>
     * This is an example of an whole fingerprint: <code>2p10p20#2*t1p2*p2t1</code>
     *
     * @return a string representing the logical structure of the petrinet
     */
    public String getLogicalFingerprint() {
        StringBuilder sb = new StringBuilder();

        // Add places
        Collection<AbstractPetriNetElementModel> places = getElementContainer().getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE).values();

        // Add number of places
        sb.append(places.size());

        // Add place details
        for ( AbstractPetriNetElementModel place : places ) {
            PlaceModel p = (PlaceModel) place;
            sb.append(String.format("%s%d", p.getId(), p.getTokenCount()));
        }

        // Add part delimiter
        sb.append("#");

        // Add arcs
        Collection<ArcModel> arcs = this.getElementContainer().getArcMap().values();

        // Add number of arcs
        sb.append(arcs.size());

        // Add arc details
        for ( ArcModel arc : arcs ) {
            String weight = arc.getInscriptionValue() == 1 ? "" : String.valueOf(arc.getInscriptionValue());
            sb.append(String.format("*%s%s%s", weight, arc.getSourceId(), arc.getTargetId()));
        }

        return sb.toString();
    }

    /**
     * Checks if a fingerprint is logically equal with the fingerprint of the current net.
     * <p>
     * The difference to the {@code String.equals()}-method is, that fingerprints are considered equal
     * if the have a different order of the contained arcs. But the id's of the places has to be consistent.
     *
     * @param fingerprintToCheck The fingerprint to compare
     * @return <code>true</code> if the fingerprint is equal, otherwise <code>false</code>
     */
    public boolean isLogicalFingerprintEqual(String fingerprintToCheck) {
        String currFingerprint = this.getLogicalFingerprint();

        // Equal fingerprints are always logically equal
        if ( currFingerprint.equals(fingerprintToCheck) ) {
            return true;
        }

        String currLeadingPart = currFingerprint.substring(0, currFingerprint.indexOf('*'));
        String otherLeadingPart = fingerprintToCheck.substring(0, fingerprintToCheck.indexOf('*'));

        // fingerprints are always different, if they have a different length.
        if ( currFingerprint.length() != fingerprintToCheck.length() ) {
            return false;
        }

        // fingerprints are not equal if the have a different leading part. (place ids + tokens)
        if ( !currLeadingPart.equals(otherLeadingPart) ) {
            return false;
        }

        // extract the fingerprint specific arc ids from the current net
        Set<String> currentArcs = new HashSet<>();
        for ( ArcModel arc : this.getElementContainer().getArcMap().values() ) {
            currentArcs.add(String.format("%s%s", arc.getSourceId(), arc.getTargetId()));
        }

        // Ensure that the arc sets are equal.
        String arcString = fingerprintToCheck.substring(fingerprintToCheck.indexOf('*') + 1);
        String[] arcs = arcString.split("\\*");

        // Check each arc
        for ( String arc : arcs ) {
            boolean arcContained = currentArcs.remove(arc);
            if ( !arcContained ) {
                return false;
            }
        }

        // Ensure that no arcs remain.
        return currentArcs.size() == 0;
    }

    public Vector<Object> getUnknownToolSpecs() {
        return unknownToolSpecs;
    }

    public void addUnknownToolSpecs(Object unknownToolSpecs) {
        getUnknownToolSpecs().add(unknownToolSpecs);
    }

    /**
     * @return Returns the organizationUnits.
     */
    public Vector<ResourceClassModel> getOrganizationUnits() {
        if ( organizationUnits == null ) {
            organizationUnits = new Vector<ResourceClassModel>();
        }
        return organizationUnits;
    }

    /**
     * @param organizationUnits The organizationUnits to set.
     */
    public void setOrganizationUnits(Vector<ResourceClassModel> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    /**
     * @return Returns the resourceMap.
     */
    public HashMap<String, Vector<String>> getResourceMapping() {
        if ( resourceMapping == null ) {
            resourceMapping = new HashMap<String, Vector<String>>();
        }
        return resourceMapping;
    }

    public void setResourceMapping(HashMap<String, Vector<String>> resourceMapping) {
        this.resourceMapping = resourceMapping;
    }

    /**
     * @return Returns the roles.
     */
    public Vector<ResourceClassModel> getRoles() {
        if ( roles == null ) {
            roles = new Vector<ResourceClassModel>();
        }
        return roles;
    }

    /**
     * @param roles The roles to set.
     */
    public void setRoles(Vector<ResourceClassModel> roles) {
        this.roles = roles;
    }

    /**
     * @return Returns the resources.
     */
    public Vector<ResourceModel> getResources() {
        if ( resources == null ) {
            resources = new Vector<ResourceModel>();
        }
        return resources;
    }

    public void setResources(Vector<ResourceModel> resources) {
        this.resources = resources;
    }

    /**
     * @return Returns the simulations.
     */
    public Vector<SimulationModel> getSimulations() {
        if ( simulations == null ) {
            simulations = new Vector<SimulationModel>();
        }
        return simulations;
    }

    public void addRole(ResourceClassModel role) {
        getRoles().add(role);
    }

    public void addOrgUnit(ResourceClassModel orgUnit) {
        getOrganizationUnits().add(orgUnit);
    }

    public void addResource(ResourceModel resource) {
        getResources().add(resource);
    }

    public void addSimulation(SimulationModel simulation) {
        getSimulations().add(simulation);
    }

    public int containsRole(String name) {
        for ( int i = 0; i < getRoles().size(); i++ ) {
            if ( (getRoles().get(i)).getName().equals(name) ) {
                return i;
            }

        }
        return -1;
    }

    public int containsOrgunit(String name) {
        for ( int i = 0; i < getOrganizationUnits().size(); i++ ) {
            if ( (getOrganizationUnits().get(i)).getName().equals(name) ) {
                return i;
            }

        }
        return -1;
    }

    public int containsResource(String name) {
        for ( int i = 0; i < getResources().size(); i++ ) {
            if ( (getResources().get(i)).getName().equals(name) ) {
                return i;
            }

        }
        return -1;
    }

    public int containsResource(Vector<String> resourceVector, String name) {
        for ( int i = 0; i < resourceVector.size(); i++ ) {
            if ( resourceVector.get(i).equals(name) ) {
                return i;
            }

        }
        return -1;
    }

    public void replaceResourceMapping(String oldName, String newName) {
        for ( Iterator<String> iter = getResourceMapping().keySet().iterator(); iter.hasNext(); ) {
            Vector<String> resourcevalues = getResourceMapping().get(iter.next());
            for ( int i = 0; i < resourcevalues.size(); i++ ) {
                if ( resourcevalues.get(i).equals(oldName) ) {
                    resourcevalues.set(i, newName);
                }
            }
        }
    }

    public void replaceResourceClassMapping(String oldName, String newName) {
        if ( getResourceMapping().containsKey(oldName) ) {
            getResourceMapping().put(newName, getResourceMapping().get(oldName));
            getResourceMapping().remove(oldName);
        }
    }

    public void removeElement(Object id) {
        getElementContainer().removeElement(id);
    }

    public void setSimulationCounter(int simulationCounter) {
        this.simulationCounter = simulationCounter;
    }

    /**
     * Resets the virtual tokens in all places.
     */
    public void resetVirtualTokens() {
        Collection<PlaceModel> places = getElementContainer().getPlaces();
        for (PlaceModel place: places){
            place.resetVirtualTokens();
        }
    }

    public ModelElementContainer getElementContainer() {
        return elementContainer;
    }

    public void setElementContainer(ModelElementContainer elementContainer) {
        this.elementContainer = elementContainer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns if the petri net contains arc weight larger than 1.
     *
     * @return true if the net contains arc weights, otherwise false
     */
    public boolean usesArcWeights(){
        Collection<ArcModel> arcs = getElementContainer().getArcMap().values();
        for(ArcModel arc : arcs){
            if(arc.getInscriptionValue() > 1) return true;
        }

        return false;
    }

    /**
     * Removes the highlighting from all elements and all arcs.
     */
    public void removeHighlighting(){
        removeHighlightingFromElements();
        removeHighlightingFromArcs();
    }

    private void removeHighlightingFromElements() {
        for(AbstractPetriNetElementModel element : elementContainer.getRootElements()){
            element.setHighlighted(false);
        }
    }

    private void removeHighlightingFromArcs() {
        for(ArcModel arc : elementContainer.getArcMap().values()){
            arc.setHighlighted(false);
        }
    }
}