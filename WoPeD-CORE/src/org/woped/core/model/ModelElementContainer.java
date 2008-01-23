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
package org.woped.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jgraph.graph.DefaultPort;
import org.oasisOpen.docs.wsbpel.x20.process.executable.QNames;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLinks;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariables;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.utilities.LoggerManager;

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
public class ModelElementContainer implements Serializable
{
    //! If !=null, stores editor layout info for the editor
    //! that is to be used to edit the sub-process
    private EditorLayoutInfo      editorLayoutInfo = null;

	public EditorLayoutInfo getEditorLayoutInfo() {
		return editorLayoutInfo;
	}

	public void setEditorLayoutInfo(EditorLayoutInfo editorLayoutInfo) {
		this.editorLayoutInfo = editorLayoutInfo;
	}  
	
	//! Just as we own elements, elements own us
	//! if we're a simple transition element container
	//! Again, it is important for navigation to know these things
	//! The owningElement member may be null (which is in fact the default)
	//! if we're not owned by an AbstractElementModel instance at all
	private AbstractElementModel owningElement = null;
	
	private TVariables variablesList;
	private TPartnerLinks partnerLinkList;
	
	public void setOwningElement(AbstractElementModel element)
	{
		owningElement = element;
	}
	public AbstractElementModel getOwningElement()
	{
		return owningElement;
	}
	
    private Map<String, Map<String, Object>> idMap = null;
    private Map<String, ArcModel> arcs = null;
    public static final String SELF_ID = "_#_";

    /**
     * Constructor for ModelElementContainer.
     */
    public ModelElementContainer()
    {
        idMap = new HashMap<String, Map<String, Object>>();
        arcs = new HashMap<String, ArcModel>();
        this.variablesList = this.genVariableList();
    }

    /**
     * Returns the idMapper. This is the Main Hashmap containing the whole
     * Petri-Net. Is mainly used by the Class itselfs. Should not be necessary
     * to use this Method outside of the Container!
     * 
     * @return Map
     */
    public Map<String, Map<String, Object>> getIdMap()
    {
        return idMap;
    }
    
    public TPartnerLinks getTPartnerLinkList()
    {
    	return this.partnerLinkList;
    }
    
    /*Returns a list of TVariables. This method is used by the bpel-generator to produce BPEL-Code.*/
    public TVariables getTVariablesList()
    {
    	return this.variablesList;
    }
    
    private TPartnerLinks genPartnerLinkList()
    {
    	return TPartnerLinks.Factory.newInstance();
    }
    
    /*Returns a list of PartnerLinkTypes. This method is used by the BPEL-generator to pruduce the global
     * datamodel of partnerlinks*/
    public String[] getPartnerLinkList()
    {
    	TPartnerLink[] tpl = this.partnerLinkList.getPartnerLinkArray();
    	String[] partnerLinklist = new String[tpl.length];
    	for(int i=0;i<tpl.length;i++)
    	{
    		partnerLinklist[i] = tpl[i].getName();
    	}
    	return partnerLinklist;
    }
    
    /*Insert a partnerlink to a consisting list of partnerlinks*/
    public void addPartnerLink(String name, String namespace, String partnerLinkType, String partnerRole)
    {
    	TPartnerLink pl = this.partnerLinkList.addNewPartnerLink();
    	pl.setName(name);
    	pl.setPartnerLinkType(new QName(namespace, partnerLinkType));
    	pl.setPartnerRole(partnerRole);
    }
    
    /*Insert a partnerlink to a consisting list of partnerlinks
     * Attention: Parameters: name, namespace, partnerLinkType, partnerRole, myRole*/
    public void addPartnerLink(String name, String namespace, String partnerLinkType, String partnerRole, String myRole)
    {
    	TPartnerLink pl = this.partnerLinkList.addNewPartnerLink();
    	pl.setName(name);    	
    	pl.setPartnerLinkType(new QName(namespace, partnerLinkType));
    	pl.setPartnerRole(partnerRole);
    	pl.setMyRole(myRole);
    }
    
    /*Insert a partnerlink to a consisting list of partnerlinks
     * Attention: Parameters: name, namespace, partnerLinkType, myRole (without partnerRole*/
    public void addPartnerLinkWithoutPartnerRole(String name, String namespace, String partnerLinkType, String myRole)
    {
    	TPartnerLink pl = this.partnerLinkList.addNewPartnerLink();
    	pl.setName(name);
    	pl.setPartnerLinkType(new QName(namespace, partnerLinkType));
    	pl.setMyRole(myRole);
    }
    
    public void removePartnerLink()
    {
    	//zu erledigen
    }
    
    private TVariables genVariableList()
    {    	
        return TVariables.Factory.newInstance();
    }
    
    /*public TVariables getVariablesList()
    {
    	return this.variablesList;
    }*/
    
    /*Inserts a variable to a consisting list of TVariables*/
    public void addVariable(TVariable arg)
    {
    	TVariable var = this.variablesList.addNewVariable();
    	var.set(arg);
    }
    
    /*Inserts a variable to a consisting list of TVariables*/
    public void addVariable(String name, String type)
    {
    	TVariable var = this.variablesList.addNewVariable();
    	var.setName(name);    	
    	var.setType(new QName("http://www.w3.org/2001/XMLSchema", type));
    }
    
    /*Inserts a variable from WSDL-File to a consisting list of TVariables
     * method is used to create variables in WSDL-tab*/
    public void addWSDLVariable(String name, String namespace, String type)
    {
    	TVariable var = this.variablesList.addNewVariable();
    	var.setName(name);
    	var.setMessageType(new QName(namespace, type));
    }    
    
    /*Returns a array (type=String) of created variables
     * method is used to create drop-down list in GUI*/
    public String[] getVariableList()
    {
    	TVariable[] tva = this.variablesList.getVariableArray();
    	String[] namelist = new String[tva.length];
    	for(int i=0;i<tva.length;i++)
    	{
    		namelist[i] = tva[i].getName();
    	}
    	return namelist;
    }
    
    /*Returns a array (type=String) of possible basictypes for BPEL-variables
     * method is used to create drop-down list in GUI*/
    public String[] getTypes()
    {
    	String[] list = {"String", "normalizedString", "token", "byte", "unsignedByte", "base64Binary", "hexBinary", "integer", "positiveInteger", "negativeInteger", "nonNegativeInteger", "nonPositiveInteger", "int", "unsignedInt", "long", "unsignedLong", "short", "unsignedShort", "decimal", "float", "double", "boolean", "time", "dateTime", "duration", "date", "gMonth", "gYear", "gYearMonth", "gDay", "gMonthDay", "Name", "QName", "NCName", "anyURI", "language"};
    	return list;
    }
    
    public void removeVariable(TVariable arg)
    {
    	TVariable[] list = this.variablesList.getVariableArray();
    	for(int i = 0; i < list.length; i++)
    	{
    		//noch machen
    			
    	}
    }

    /**
     * Method addElement. Adds an <code>PetriNetModelElement</code> to the
     * Container.
     * 
     * @param theElement
     * @throws ElementException
     */
    public AbstractElementModel addElement(AbstractElementModel theElement)
    {
        if (getIdMap().get(theElement.getId()) == null)
        {
            // if referenceMap does not exits, create it
            Map<String, Object> referenceMap = new HashMap<String, Object>();
            // =>frist time adding element, first add Element itself with
            // SELF_ID to the referenceMap
            referenceMap.put(SELF_ID, theElement);
            // ... and to the idMap
            getIdMap().put(theElement.getId(), referenceMap);
            
            // Tell the element that it is now owned
            theElement.addOwningContainer(this);
            
            LoggerManager.debug(Constants.CORE_LOGGER, "Element: " + theElement.getId() + " added");
        } else
        {
            LoggerManager.debug(Constants.CORE_LOGGER, "The Element already exists, did nothing!");
        }
        return theElement;
    }

    /**
     * Method addReference. Adds an Reference from the
     * <code>PetriNetModelElement</code> with id <code>sourceId</code> to
     * the Element with id <code>targetId</code>.
     * 
     * @param sourceId
     * @param targetId
     */
    public void addReference(ArcModel arc)
    {

        if (getElementById(arc.getSourceId()) == null)
        {
            // if referenceMap is not setup, then the Element itself was not set
            LoggerManager.warn(Constants.CORE_LOGGER, "Source (ID:" + arc.getSourceId() + ") does not exist");
        } else if (getElementById(arc.getTargetId()) == null)
        {
            LoggerManager.warn(Constants.CORE_LOGGER, "Target (ID:" + arc.getTargetId() + ") does not exist");
        } else if (containsArc(arc))
        {
            LoggerManager.debug(Constants.CORE_LOGGER, "Arc already exists!");
        } else
        {
            getIdMap().get(arc.getSourceId()).put(arc.getId(), arc);
            arcs.put(arc.getId(), arc);
            LoggerManager.debug(Constants.CORE_LOGGER, "Reference: " + arc.getId() + " (" + arc.getSourceId() + " -> " + arc.getTargetId() + ") added.");
        }

    }

    /**
     * Check whether a reference from sourceID to targetID exists.
     * Note that this will check for top-level references, not low-level
     * components of van der Aalst operators.
     * This means that only actual, visible arcs as present in the graphical
     * Petri-Net representation will be found
     * @param sourceId
     * @param targetId
     * @return
     */
    public boolean hasReference(Object sourceId, Object targetId)
    {
    	return (findArc(sourceId.toString(),targetId.toString())!=null);
    }

    /**
     * Method removeElement. Removes a <code>PetriNetModelElement</code>
     * including all its References.
     * 
     * @param id
     */
    public void removeElement(Object id)
    {

        // AT FIRST delete element's connections
        removeArcsFromElement(id);
        // AND THEN remove the element, and all its target information
        removeOnlyElement(id);

    }

    public void removeOnlyElement(Object id)
    {
    	AbstractElementModel element = getElementById(id);
    	// The element is no longer owned by anybody
    	element.removeOwningContainer(this); 
        getIdMap().remove(id);        
        LoggerManager.debug(Constants.CORE_LOGGER, "Element (ID:" + id + ") deleted.");
    }

    public void removeTargetArcsFromElement(Object id) {
        // remove all Target Arcs
        Iterator arcsToRemove2 = getOutgoingArcs(id).keySet().iterator();
        // arcsToRemove2.next();
        while (arcsToRemove2.hasNext())
        {
            removeArc(arcsToRemove2.next());
        }
    }
    
    public void removeSourceArcsFromElement(Object id) {
        // remove all Source Arcs
        Iterator arcsToRemove = getIncomingArcs(id).keySet().iterator();
        while (arcsToRemove.hasNext())
        {
            removeArc(arcsToRemove.next());
        }
    }
    
    /**
     * Method removeRefElements. Removes only all Arcs from a
     * <code>PetriNetModelElement</code>, not the Element itselfs.
     * 
     * @param id
     */
    public void removeArcsFromElement(Object id)
    {
        removeSourceArcsFromElement(id);
        removeTargetArcsFromElement(id);

        LoggerManager.debug(Constants.CORE_LOGGER, "All References from/to (ID:" + id + ") deleted");
    }

    public void removeArc(Object id)
    {
        if (getArcById(id) != null)
        {
            // remove the Arc-Model
            removeArc(getArcById(id));
        } else LoggerManager.warn(Constants.CORE_LOGGER, "Arc with ID: " + id + " does not exists");
    }

    public void removeArc(ArcModel arc)
    {
        if (arc != null)
        {
            LoggerManager.debug(Constants.CORE_LOGGER, "Reference (ID:" + arc.getId() + ") deleted");
            // remove in arc Map
            arcs.remove(arc.getId());
            // remove Target Entry, (in Source Element's reference Map)
            ((Map) getIdMap().get(arc.getSourceId())).remove(arc.getId());
        }

    }

    public void removeAllSourceElements(Object targetId)
    {
        Iterator transIter = getSourceElements(targetId).keySet().iterator();
        while (transIter.hasNext())
        {
            removeElement(transIter.next());
        }
    }

    public void removeAllTargetElements(Object sourceId)
    {
        Iterator transIter = getTargetElements(sourceId).keySet().iterator();
        while (transIter.hasNext())
        {
            removeElement(transIter.next());
        }
    }

    /**
     * Method getReferenceElements. Returns the all
     * <code>AbstractElementModel</code>, of which an Element with a special
     * id is source.
     * 
     * @param id
     * @return Map
     */
    public Map<String, AbstractElementModel> getTargetElements(Object id)
    {

        if ((Map) getIdMap().get(id) != null)
        {

            Iterator refIter = ((Map) getIdMap().get(id)).keySet().iterator();
            Map<String, AbstractElementModel> targetMap = new HashMap<String, AbstractElementModel>();
            while (refIter.hasNext())
            {
                Object arc = ((Map) getIdMap().get(id)).get(refIter.next());
                if (arc instanceof ArcModel)
                {
                    AbstractElementModel aCell = (AbstractElementModel) ((DefaultPort) ((ArcModel) arc).getTarget()).getParent();
                    targetMap.put(aCell.getId(), aCell);
                }
            }
            return targetMap;
        } else
        {
            return null;
        }
    }

    public Map getOutgoingArcs(Object id)
    {

        if ((Map) getIdMap().get(id) != null)
        {
            Map<String, Object> arcOut = new HashMap<String, Object>(getIdMap().get(id));
            arcOut.remove("_#_");
            return arcOut;
        } else return new HashMap();
    }

    public Map getIncomingArcs(Object id)
    {
        return findSourceArcs(id);
    }

    /**
     * Method getSourceElements. Returns the all
     * <code>PetriNetModelElement</code>, of which an Element with a special
     * id is target.
     * 
     * @param id
     * @return Map
     */
    public Map<String, AbstractElementModel> getSourceElements(Object targetId)
    {

        return findSourceElements(targetId);

    }

    /**
     * Method getRootElements. Returns a <code>List</code> containing all
     * <code>PetriNetModelElement</code> without any Reference information.
     * 
     * @return List
     */
    public List getRootElements()
    {

        List<AbstractElementModel> rootElements = new ArrayList<AbstractElementModel>();
        Iterator allIter = getIdMap().keySet().iterator();
        while (allIter.hasNext())
        {
            AbstractElementModel element = getElementById(allIter.next());
            rootElements.add(element);
        }

        return rootElements;

    }

    /**
     * Method findSourceElements. Returns a Map with the Elements that contains
     * a reference to the Object with a special id.
     * 
     * @param id
     * @return List
     */
    protected Map<String, AbstractElementModel> findSourceElements(Object targetId)
    {

        Map<String, AbstractElementModel> sourceMap = new HashMap<String, AbstractElementModel>();
        Iterator sourceArcIter = findSourceArcs(targetId).keySet().iterator();
        ArcModel tempArc;
        while (sourceArcIter.hasNext())
        {
            tempArc = (ArcModel) arcs.get(sourceArcIter.next());
            sourceMap.put(tempArc.getSourceId(), getElementById(tempArc.getSourceId()));
        }
        return sourceMap;
    }

    protected Map findSourceArcs(Object id)
    {

        Iterator arcIter = arcs.keySet().iterator();
        Map<String, ArcModel> sourceArcs = new HashMap<String, ArcModel>();
        ArcModel tempArc;
        while (arcIter.hasNext())
        {
            tempArc = (ArcModel) arcs.get(arcIter.next());
            if (tempArc.getTargetId() != null) {
            	if (tempArc.getTargetId().equals(id)) {
            		sourceArcs.put(tempArc.getId(), tempArc);
            	}
            }
        }
        return sourceArcs;

    }

    /**
     * Method getElementById. Returns the a ModelElement with a special id
     * itselfs
     * 
     * @param id
     * @return ModelElement
     */
    public AbstractElementModel getElementById(Object id)
    {

        if (getIdMap().get(id) != null)
        {
            return (AbstractElementModel) ((Map) getIdMap().get(id)).get(ModelElementContainer.SELF_ID);
        } else
        {
            LoggerManager.debug(Constants.CORE_LOGGER, "Requested Element (ID:" + id + ") does not exists");
            return null;
        }

    }

    public ArcModel getArcById(Object id)
    {

        if (arcs.get(id) != null)
        {
            return (ArcModel) arcs.get(id);
        } else
        {
            LoggerManager.debug(Constants.CORE_LOGGER, " Requested Arc (ID:" + id + ") does not exists");
            return null;
        }
    }

    /* only for debugging use */
/*    private void printContent()
    {

        Iterator rootTier = getRootElements().iterator();
        System.out.println("#######################################");
        System.out.println("The Content: ");
        while (rootTier.hasNext())
        {
            Object id = ((AbstractElementModel) rootTier.next()).getId();
            System.out.println("Element: (" + ") " + id);
            if (getTargetElements(id) != null)
            {
                Iterator targetIter = getTargetElements(id).keySet().iterator();
                while (targetIter.hasNext())
                {
                    System.out.println("  -> " + targetIter.next());
                }
            } else System.out.println("uhm, fehler bei selbsteintrag _#_");
        }
        System.out.println("#######################################");

    }
*/
    public boolean containsArc(ArcModel arc)
    {

        Iterator arcIter = getSourceElements(arc.getTargetId()).keySet().iterator();
        while (arcIter.hasNext())
        {
            if (arcIter.next().equals(arc.getSourceId()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean containsElement(Object id)
    {
        return getIdMap().containsKey(id);
    }

    /**
     * Returns the arcs.
     * 
     * @return Map
     */
    public Map<String, ArcModel> getArcMap()
    {
        return arcs;
    }

    /**
     * Sets the arcs.
     * 
     * @param arcs
     *            The arcs to set
     */
    public void setArcMap(Map<String, ArcModel> arcs)
    {
        this.arcs = arcs;
    }

    public Map<String, AbstractElementModel> getElementsByType(int type)
    {
        Map<String, AbstractElementModel> elements = new HashMap<String, AbstractElementModel>();
        Iterator elementsIter = getIdMap().keySet().iterator();
        AbstractElementModel element;
        // try {
        while (elementsIter.hasNext())
        {
            element = getElementById(elementsIter.next());
            if (element != null 
            		&& element.getType() == type) {
            	elements.put(element.getId(), element);
            }
        }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return elements;
    }

    public ArcModel findArc(String sourceId, String targetId)
    {
        Iterator iter = arcs.keySet().iterator();
        while (iter.hasNext())
        {
            ArcModel arc = (ArcModel) arcs.get(iter.next());
            if (arc.getSourceId().equals(sourceId) && arc.getTargetId().equals(targetId))
            {
                return arc;
            }
        }
        return null;
    }
}