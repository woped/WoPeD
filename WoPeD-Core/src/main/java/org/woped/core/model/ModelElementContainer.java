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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jgraph.graph.DefaultPort;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLinks;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariables;
import org.woped.core.Constants;
import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.bpel.BpelVariableList;
import org.woped.core.model.bpel.PartnerlinkList;
import org.woped.core.model.bpel.UddiVariable;
import org.woped.core.model.bpel.UddiVariableList;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.ParaphrasingModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.ShortLexStringComparator;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ModelElementContainer</code> stores the whole Model of an
 *         Petri-Net. In the frist view it is an <code>HashMap</code>,
 *         containing for each <code>PetriNetModelElement</code> an Entry with
 *         its ID as key. This Entry is again an <code>HashMap</code>
 *         containing the Element itselfs with the ID
 *         <code>ModelElementContainer.SELF_ID</code>. Additionally it
 *         contains all Elements of which this <code>PetriNetModelElement</code>
 *         is Source. The Methods of the <code>ModelElementContainer</code>
 *         offer a secure and comfort handling with the Model. Therefore the
 *         <code>getIdMap()</code> Method should not be used. <br>
 *         <br>
 *         Created on 29.04.2003
 */

@SuppressWarnings("serial")
public class ModelElementContainer implements Serializable {
    public static final String SELF_ID = "_#_";
    // ! If !=null, stores editor layout info for the editor
    // ! that is to be used to edit the sub-process
    private EditorLayoutInfo editorLayoutInfo = null;
    // ! Just as we own elements, elements own us
    // ! if we're a simple transition element container
    // ! Again, it is important for navigation to know these things
    // ! The owningElement member may be null (which is in fact the default)
    // ! if we're not owned by an AbstractElementModel instance at all
    private AbstractPetriNetElementModel owningElement = null;
    private BpelVariableList variablesList = new BpelVariableList();
    private PartnerlinkList partnerLinkList = new PartnerlinkList();
    private UddiVariableList uddiVariableList = new UddiVariableList();
    private ParaphrasingModel paraphrasingModel = new ParaphrasingModel();
    private Map<String, Map<String, Object>> idMap = null;
    private Map<String, ArcModel> arcs = null;

    /**
     * Creates an new instance of an {@code ModelElementContainer}
     */
    public ModelElementContainer() {
        idMap = new HashMap<>();
        arcs = new HashMap<>();
    }

    public EditorLayoutInfo getEditorLayoutInfo() {
        return editorLayoutInfo;
    }

    public void setEditorLayoutInfo(EditorLayoutInfo editorLayoutInfo) {
        this.editorLayoutInfo = editorLayoutInfo;
    }

    public AbstractPetriNetElementModel getOwningElement() {
        return owningElement;
    }

    public void setOwningElement(AbstractPetriNetElementModel element) {
        owningElement = element;
    }

    /**
     * Returns the idMapper. This is the Main Hashmap containing the whole
     * Petri-Net. Is mainly used by the Class itselfs. Should not be necessary
     * to use this Method outside of the Container!
     *
     * @return Map
     */
    public Map<String, Map<String, Object>> getIdMap() {
        return idMap;
    }

    /**
     * Adds an <code>PetriNetModelElement</code> to the container.
     * <p>
     * If an element with the id of the new element already exists in the container
     * it will be kept and the new element will not be added to the container.
     *
     * @param element the element to add
     * @return the element provided as parameter {@code element}
     */
    public AbstractPetriNetElementModel addElement(AbstractPetriNetElementModel element) {

        // Check if an element with this id already exists in the container
        if (getIdMap().get(element.getId()) == null) {

            // create a new reference map
            Map<String, Object> referenceMap = new HashMap<String, Object>();

            // add element as self reference
            referenceMap.put(SELF_ID, element);

            // add element to the container
            getIdMap().put(element.getId(), referenceMap);

            // set the new owning container of the element
            element.addOwningContainer(this);

            LoggerManager.debug(Constants.CORE_LOGGER, String.format("Element: %s added", element.getId()));
        } else {

            // an element with the id of the new element already exists
            LoggerManager.debug(Constants.CORE_LOGGER, "The Element already exists, did nothing!");
        }

        return element;
    }

    /**
     * Adds a reference between the source of the arc and the target of the arc {@code PetriNetModelElement} to the net
     * Method addReference. Adds an Reference from the
     * <code>PetriNetModelElement</code> with id <code>sourceId</code> to
     * the Element with id <code>targetId</code>.
     *
     * @param arc the arc representing the reference
     */
    public void addReference(ArcModel arc) {

        // validate source
        if (getElementById(arc.getSourceId()) == null) {
            LoggerManager.warn(Constants.CORE_LOGGER, String.format("Source (ID: %s) doesn't exist", arc.getSourceId()));
            return;
        }

        // validate target
        if (getElementById(arc.getTargetId()) == null) {
            LoggerManager.warn(Constants.CORE_LOGGER, "Target (ID:" + arc.getTargetId() + ") does not exist");
            return;
        }

        // Check if reference already exists
        if (hasReference(arc.getSourceId(), arc.getTargetId())) {
            LoggerManager.debug(Constants.CORE_LOGGER, "Arc already exists!");
            return;
        }

        // Add reference
        getIdMap().get(arc.getSourceId()).put(arc.getId(), arc);
        arcs.put(arc.getId(), arc);
        LoggerManager.debug(Constants.CORE_LOGGER, "Reference: " + arc.getId() + " (" + arc.getSourceId() + " -> " + arc.getTargetId() + ") added.");
    }

    /**
     * Checks if there exists a reference from the {@code PetriNetModelElement} with the sourceID
     * to the {@code PetriNetModelElement} with the targetID.
     * <br>
     * Note that this will check for top-level references, not low-level components of van
     * der Aalst operators. This means that only actual, visible arcs as present
     * in the graphical Petri-Net representation will be found
     *
     * @param sourceId The id of the source element of the reference
     * @param targetId The id of the target element of the reference
     * @return true if an reference exits, otherwise false
     */
    public boolean hasReference(Object sourceId, Object targetId) {
        return (findArc(sourceId.toString(), targetId.toString()) != null);
    }

    /**
     * Removes an {@code PetriNetModelElement} from the net
     * including all its References.
     *
     * @param id the id of the element to remove
     */
    public void removeElement(Object id) {
        // AT FIRST delete element's connections
        removeArcsFromElement(id);

        // AND THEN remove the element, and all its target information
        removeOnlyElement(id);
    }

    /**
     * Removes an element from the net.
     * Caution! The references from the element will consist in the net!
     *
     * @param id the id of the element to remove
     */
    public void removeOnlyElement(Object id) {
        AbstractPetriNetElementModel element = getElementById(id);
        // The element is no longer owned by anybody
        if (element != null) element.removeOwningContainer(this);
        getIdMap().remove(id);
        LoggerManager.debug(Constants.CORE_LOGGER, "Element (ID:" + id + ") removed.");
    }

    /**
     * Removes only all references from or to an {@code PetriNetModelElement}.
     * The element itself will not be removed
     *
     * @param id the id of the element from which the references should be removed
     */
    public void removeArcsFromElement(Object id) {
        removeIncomingArcsFromElement(id);
        removeOutgoingArcsFromElement(id);

        LoggerManager.debug(Constants.CORE_LOGGER, "All References from/to (ID:" + id + ") deleted");
    }

    /**
     * Removes all outgoing arcs from the element.
     * The element is the source of this arcs.
     *
     * @param elementId the id of the element from which the outgoing arcs should be removed
     */
    protected void removeOutgoingArcsFromElement(Object elementId) {

        for (String arcId : getOutgoingArcs(elementId).keySet()) {
            removeArc(arcId);
        }
    }

    /**
     * Removes all incoming arcs from an element.
     * The element is the target of this arcs.
     *
     * @param elementId the id of the element from which the incoming arcs should be removed
     */
    protected void removeIncomingArcsFromElement(Object elementId) {

        for (String arcId : getIncomingArcs(elementId).keySet()) {
            removeArc(arcId);
        }
    }

    /**
     * Removes the arc with the given id from the petrinet
     *
     * @param id the id of the arc to remove
     */
    public void removeArc(Object id) {

        ArcModel arc = getArcById(id);

        if (arc == null) {
            LoggerManager.warn(Constants.CORE_LOGGER, "Arc with ID: " + id + " does not exists");
        }

        removeArc(arc);
    }

    /**
     * Removes the given arc from the petrinet.
     *
     * @param arc the arc to remove
     */
    public void removeArc(ArcModel arc) {
        if (arc != null) {
            LoggerManager.debug(Constants.CORE_LOGGER, "Reference (ID:" + arc.getId() + ") deleted");
            // remove in arc Map
            arcs.remove(arc.getId());
            // remove Target Entry, (in Source Element's reference Map)
            getIdMap().get(arc.getSourceId()).remove(arc.getId());
        }
    }

    public void removeAllSourceElements(Object targetId) {
        Iterator<String> transIter = getSourceElements(targetId).keySet().iterator();
        while (transIter.hasNext()) {
            removeElement(transIter.next());
        }
    }

    public void removeAllTargetElements(Object sourceId) {
        Iterator<String> transIter = getTargetElements(sourceId).keySet().iterator();
        while (transIter.hasNext()) {
            removeElement(transIter.next());
        }
    }

    /**
     * Gets all {@code PetriNetModelElement} which has an outgoing arc to the {@code PetriNetModelElement}
     * with the given id.
     *
     * @param targetId The id of the {@code PetriNetModelElement} to detect the source elements from.
     * @return A map containing all source elements. The map may be empty.
     */
    public Map<String, AbstractPetriNetElementModel> getSourceElements(Object targetId) {

        return findSourceElements(targetId);
    }

    /**
     * Gets all {@code AbstractPetriNetElementModel} which have a incoming reference
     * from the {@code AbstractPetriNetElementModel} with the given id.
     *
     * @param sourceId the id of the {@code AbstractPetriNetElementModel} to get the targets from.
     * @return a Map containing all existing targets or {@code null} if no element with the provided id exists.
     */
    public Map<String, AbstractPetriNetElementModel> getTargetElements(Object sourceId) {

        Map<String, Object> sourceMap = getIdMap().get(sourceId);
        Map<String, AbstractPetriNetElementModel> targetMap = new HashMap<>();

        if (sourceMap == null) {
            return null;
        }

        for (String key : sourceMap.keySet()) {

            Object value = sourceMap.get(key);

            if (!(value instanceof ArcModel)) continue;

            AbstractPetriNetElementModel target = (AbstractPetriNetElementModel) ((DefaultPort) ((ArcModel) value).getTarget()).getParent();
            targetMap.put(target.getId(), target);
        }

        return targetMap;
    }

    /**
     * Gets all outgoing arcs from the {@code AbstractPetriNetElement} with the given id.
     * This element is source of all that arcs.
     *
     * @param elementId the id of the {@code AbstractPetriNetElement} to get the outgoing arcs from.
     * @return A map containing all outgoing arcs. The map may be empty if no such arcs exist.
     */
    public Map<String, Object> getOutgoingArcs(Object elementId) {

        if (null == getIdMap().get(elementId)) {
            return new HashMap<>();
        }

        Map<String, Object> arcOut = new HashMap<>(getIdMap().get(elementId));
        arcOut.remove(SELF_ID);

        return arcOut;
    }

    /**
     * Gets all incoming arcs from the {@code AbstractPetriNetElement} with the given id.
     * This element is the target of that arcs
     *
     * @param elementId The id of the {@code AbstractPetriNetElement} to get the incoming arcs from.
     * @return A map containing all incoming arcs. The map may be empty if no such arcs exist.
     */
    public Map<String, ArcModel> getIncomingArcs(Object elementId) {

        Map<String, ArcModel> result = new HashMap<>();

        for (ArcModel arc : arcs.values()) {
            if (arc.getTargetId().equals(elementId)) {
                result.put(arc.getId(), arc);
            }
        }

        return result;
    }

    /**
     * Gets all {@code PetriNetModelElement} without any Reference information.
     *
     * @return A List containing all {@code PetriNetModelElement}.
     */
    public List<AbstractPetriNetElementModel> getRootElements() {

        List<AbstractPetriNetElementModel> rootElements = new ArrayList<AbstractPetriNetElementModel>();
        Iterator<String> allIter = getIdMap().keySet().iterator();
        while (allIter.hasNext()) {
            AbstractPetriNetElementModel element = getElementById(allIter.next());
            rootElements.add(element);
        }

        return rootElements;

    }

    /**
     * Searches for all elements, which have an outgoing arc to the {@code PetriNetModelElement}
     * with the given id.
     *
     * @param targetId The id of the {@code PetriNetModelElement} to search for.
     * @return A map containing all {@code PetriNetModelElement}. The map may be empty.
     */
    protected Map<String, AbstractPetriNetElementModel> findSourceElements(Object targetId) {

        Map<String, AbstractPetriNetElementModel> result = new HashMap<>();
        AbstractPetriNetElementModel source;

        for (ArcModel arc : getIncomingArcs(targetId).values()) {
            source = getElementById(arc.getSourceId());
            result.put(source.getId(), source);
        }

        return result;
    }

    /**
     * Gets the {@code AbstractPetriNetElementModel} with the given id.
     *
     * @param id The id of the requested {@code AbstractPetriNetElementModel}.
     * @return The requested {@code AbstractPetriNetElementModel} or null, if non such element exists.
     */
    public AbstractPetriNetElementModel getElementById(Object id) {

        if (getIdMap().get(id) != null) {
            return (AbstractPetriNetElementModel) getIdMap().get(id).get(ModelElementContainer.SELF_ID);


        } else {
            LoggerManager.debug(Constants.CORE_LOGGER, "Requested Element (ID:" + id + ") does not exists");
            return null;
        }

    }

    /**
     * Removes the highlighting from all {@code AbstractPetriNetElementModel} and {@code ArcModel}.
     */
    public void removeAllHighlighting() {
        Map<String, Map<String, Object>> map = getIdMap();
        for (String id : map.keySet())
            ((AbstractPetriNetElementModel) map.get(id).get(ModelElementContainer.SELF_ID)).setHighlighted(false);
        for (String arc : arcs.keySet())
            arcs.get(arc).setHighlighted(false);
    }

    /**
     * Gets the arc with the given id.
     *
     * @param id the id of the requested arc.
     * @return the requested arc or null if no such arc exists.
     */
    public ArcModel getArcById(Object id) {

        if (arcs.get(id) != null) {
            return arcs.get(id);
        } else {
            LoggerManager.debug(Constants.CORE_LOGGER, " Requested Arc (ID:" + id + ") does not exists");
            return null;
        }
    }

    /**
     * Checks if an arc with the given source and target exists in the net.
     *
     * @param sourceId the id of the arcs source
     * @param targetId the id of the arcs target
     * @return the arc or null, if no such arc exists
     */
    public ArcModel findArc(String sourceId, String targetId) {

        for (ArcModel arc : arcs.values()) {
            if (arc.getSourceId().equals(sourceId) && arc.getTargetId().equals(targetId))
                return arc;
        }

        return null;
    }

    /**
     * Checks if the {@code AbstractPetriNetElementModel} with the given id exists in the net.
     *
     * @param elementId the id of the {@code AbstractPetriNetElementModel} to check.
     * @return true if the element exists, otherwise false
     */
    public boolean containsElement(Object elementId) {
        return getIdMap().containsKey(elementId);
    }

    /**
     * Gets a map containing all arcs from the net.
     *
     * @return A Map containing all arcs.
     */
    public Map<String, ArcModel> getArcMap() {
        return arcs;
    }

    /**
     * Gets all {@code AbstractPetriNetElementModel} with the given type.
     * <br>
     * The types are defined in the {@link AbstractPetriNetElementModel} class.
     *
     * @param type the requested type
     * @return A map containing the {@code AbstractPetriNetElementModel} with their id as key. The map may be empty.
     */
    public Map<String, AbstractPetriNetElementModel> getElementsByType(int type) {
        Map<String, AbstractPetriNetElementModel> elements = new HashMap<>();
        Iterator<String> elementsIter = getIdMap().keySet().iterator();
        AbstractPetriNetElementModel element;

        while (elementsIter.hasNext()) {
            element = getElementById(elementsIter.next());
            if (element != null && element.getType() == type) {
                elements.put(element.getId(), element);
            }
        }

        return elements;
    }


    /**
     * Gets all places of the net except places from sub processes or operator transitions
     *
     * @return all places of the net
     */
    public Collection<PlaceModel> getPlaces(){
        TreeSet<PlaceModel> places =  new TreeSet<>(new Comparator<PlaceModel>() {
            Comparator<String> comparator = new ShortLexStringComparator();

            @Override
            public int compare(PlaceModel o1, PlaceModel o2) {

                int result = comparator.compare(o1.getNameValue(), o2.getNameValue());

                if(result == 0){
                    result = comparator.compare(o1.getId(), o2.getId());
                }

                return result;
            }
        });

        for(String elementId:this.idMap.keySet()){
            AbstractPetriNetElementModel element = getElementById(elementId);

            if(element instanceof PlaceModel){
                places.add((PlaceModel) element);
            }
        }

        return places;
    }

	/* Bpel extension */

    /**
     * @return
     */
    public BpelVariableList getVariableList() {
        return this.variablesList;
    }

    /**
     * @return
     */
    public PartnerlinkList getPartnerlinkList() {
        return this.partnerLinkList;
    }

    /**
     * @return
     */
    public TPartnerLinks getTPartnerLinkList() {
        return this.partnerLinkList.getBpelCode();
    }

    /**
     * Returns a list of TVariables. This method is used by the bpel-generator
     * to produce BPEL-Code.
     *
     * @return
     */
    public TVariables getTVariablesList() {
        return this.variablesList.getBpelCode();
    }

    /**
     * Returns a list of PartnerLinkTypes. This method is used by the
     * BPEL-generator to pruduce the global datamodel of partnerlinks
     *
     * @return
     */
    public String[] getPartnerLinkList() {
        return this.partnerLinkList.getPartnerlinkNameArray();
    }

    /**
     * Insert a partnerlink to a consisting list of partnerlinks
     *
     * @param name
     * @param namespace
     * @param partnerLinkType
     * @param partnerRole
     * @param WsdlUrl
     */
    public void setPartnerLink(String name, String namespace, String partnerLinkType, String partnerRole, String WsdlUrl) {
        this.partnerLinkList.setPartnerLink(name, namespace, partnerLinkType, partnerRole, WsdlUrl);
    }

    /**
     * Insert a partnerlink to a consisting list of partnerlinks Attention:
     * Parameters: name, namespace, partnerLinkType, partnerRole, myRole
     *
     * @param name
     * @param namespace
     * @param partnerLinkType
     * @param partnerRole
     * @param myRole
     * @param WsdlUrl
     */
    public void addPartnerLink(String name, String namespace, String partnerLinkType, String partnerRole, String myRole, String WsdlUrl) {
        this.partnerLinkList.addPartnerLink(name, namespace, partnerLinkType, partnerRole, myRole, WsdlUrl);
    }

    /**
     * Insert a partnerlink to a consisting list of partnerlinks Attention:
     * Parameters: name, namespace, partnerLinkType, myRole (without
     * partnerRole)
     *
     * @param name
     * @param namespace
     * @param partnerLinkType
     * @param myRole
     * @param WsdlUrl
     */
    public void addPartnerLinkWithoutPartnerRole(String name, String namespace, String partnerLinkType, String myRole, String WsdlUrl) {
        this.partnerLinkList.addPartnerLinkWithoutPartnerRole(name, namespace, partnerLinkType, myRole, WsdlUrl);
    }

    /**
     * @param name
     * @param namespace
     * @param partnerLinkType
     * @param partnerRole
     * @param WsdlUrl
     * @edit by Alexander Roßwog
     * <p>
     * Insert a partnerlink to a consisting list of partnerlinks
     * Attention: Parameters: name, namespace, partnerLinkType, partnerRole
     */
    public void addPartnerLinkWithoutMyRole(String name, String namespace, String partnerLinkType, String partnerRole, String WsdlUrl) {
        this.partnerLinkList.addPartnerLinkWithoutMyRole(name, namespace, partnerLinkType, partnerRole, WsdlUrl);
    }

    public boolean existWsdlUrl(String WsdlUrl) {

        String[] urls = this.partnerLinkList.getWsdlUrls();
        if (urls == null) return false;
        for (int i = 0; i < urls.length - 1; i++) {
            if (urls[i].equals(WsdlUrl)) return true;
        }

        return false;
    }

    public boolean existPLName(String newname) {
        String[] names = this.partnerLinkList.getPartnerlinkNameArray();
        if (names == null) return false;
        for (int i = 0; i < names.length - 1; i++) {
            if (names[i].equals(newname)) return true;
        }
        return false;
    }

    /**
     * @param arg
     */
    public void addVariable(TVariable arg) {
        this.variablesList.addVariable(arg);
    }

    /**
     * @param name
     * @param type
     */
    public void addVariable(String name, String type) {
        this.variablesList.addVariable(name, type);
    }

    /**
     * @param name
     * @param namespace
     * @param type
     */
    public void addWSDLVariable(String name, String namespace, String type) {
        this.variablesList.addWSDLVariable(name, namespace, type);
    }

    /**
     * @param Name
     * @return
     */
    public BpelVariable findBpelVariableByName(String Name) {
        return this.variablesList.findBpelVaraibleByName(Name);
    }

    /**
     * @return
     */
    public String[] getBpelVariableNameList() {
        return this.variablesList.getVariableNameArray();
    }

    /**
     * @return
     */
    public HashSet<BpelVariable> getBpelVariableList() {
        return this.variablesList.getBpelVariableList();
    }

    /**
     * @Param: Name, URL
     */

    public void addUddiVariable(String name, String url) {
        this.uddiVariableList.addVariable(name, url);
    }

    /**
     * @return
     */
    public String[] getUddiVariableNameList() {
        return this.uddiVariableList.getVariableNameArray();
    }

    /**
     * @return
     * @param: Name
     */

    public UddiVariable findUddiVariableByName(String name) {
        return this.uddiVariableList.findUddiVariableByName(name);
    }

    public ParaphrasingModel getParaphrasingModel() {
        return this.paraphrasingModel;
    }
}