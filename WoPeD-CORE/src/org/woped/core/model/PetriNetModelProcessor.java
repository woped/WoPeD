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

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.jgraph.graph.DefaultPort;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.Toolspecific;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Represents a Petri-Net. It is the main access point to the Model. Contains
 * the ModelElementFactory and the ModelElementContainer <br>
 * <br>
 * Created on 29.04.2003
 */

@SuppressWarnings("serial")
public class PetriNetModelProcessor extends AbstractModelProcessor implements
		Serializable
{

	static final int PNMLFILE = 1;
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

	/**
	 * Empty Constructor creates a new PetriNet without ID (ID "noID").s
	 */
	public PetriNetModelProcessor()
	{
		this("noID");
	}

	/**
	 * Method PetriNet. Creates a PetriNet with a spiecial ID.
	 * 
	 * @param name
	 */
	public PetriNetModelProcessor(String id)
	{
		super(AbstractModelProcessor.MODEL_PROCESSOR_PETRINET);
		/* First thing for all: creating a ModelElementContainer */
		setId(id);

	}
	
	/**
	 * Method getNewPetriNetElement. Creates a new PetriNetElement with a
	 * special type (@see PetriNetElementModel) and a Aalst type respectivley
	 * 
	 * @param type
	 * @param aalsttype
	 * @return PetriNetElementModel
	 */
	public AbstractElementModel createElement(CreationMap map)
	{
		// creating a new ModelElement through Factory
		if (map.isValid())
		{
			if (map.getId() == null)
			{
				map.setId(getNewElementId(map.getType()));
			}
			AbstractElementModel anElement = ModelElementFactory
					.createModelElement(map);
			getElementContainer().addElement(anElement);
			
            if (map.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE
					|| map.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE
					|| map.getType() == PetriNetModelElement.SUBP_TYPE)
			{
				// Trigger
				if (map.getTriggerType() != -1) {
					newTrigger(map);
					if (map.getTriggerPosition() != null)
						((TransitionModel) anElement).getToolSpecific()
								.getTrigger().setPosition(
									map.getTriggerPosition().x,
									map.getTriggerPosition().y);
				}
				if (map.getResourceOrgUnit() != null
						&& map.getResourceRole() != null) {
					newTransResource(map);
					if (map.getResourcePosition() != null)
						((TransitionModel) anElement).getToolSpecific()
							.getTransResource().setPosition(
									map.getResourcePosition().x,
									map.getResourcePosition().y);
				}
				if (map.getTransitionTime()!=-1)
					((TransitionModel)anElement).getToolSpecific()
							.setTime(map.getTransitionTime());
				if (map.getTransitionTimeUnit()!=-1)
					((TransitionModel)anElement).getToolSpecific()
							.setTimeUnit(map.getTransitionTimeUnit());
			}
			
			return anElement;
		}
		return null;
	}

	/**
	 * This Method is called, when Elements get connected. If the source or the
	 * target is an Operator, the internal transformation to the classic
	 * petrinet will be done.
	 * 
	 * @param sourceId
	 * @param targetId
	 * @return ArcModel
	 */
	public ArcModel createArc(Object sourceId, Object targetId)
	{
		return createArc(null, sourceId, targetId, new Point2D[0], true);
	}

	/**
	 * This Method is called, when Elements get connected. It only checks the
	 * connection for AalstElements and guarantees the building of a classical
	 * Petrinet in The Model, if transformOperators is true.
	 * 
	 * @param id
	 * @param sourceId
	 * @param targetId
	 * @param transformOperators
	 * @return ArcModel
	 */
	public ArcModel createArc(String id, Object sourceId, Object targetId,
			Point2D[] points, boolean transformOperators)
	{

		AbstractElementModel sourceModel = getElementContainer()
				.getElementById(sourceId);
		AbstractElementModel targetModel = getElementContainer()
				.getElementById(targetId);
		
		if ((sourceModel == null)||(targetModel == null))
			return null;
		
		// if the id isn't set or the id set already belongs to an existing arc then fetch a new one
		if(id==null|this.getElementContainer().getArcById(id)!=null)
		{
			id = getNexArcId();
		}

		// if aalst source or target -> update Model
		ArcModel displayedArc = ModelElementFactory.createArcModel(
				id, (DefaultPort) sourceModel.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));
		displayedArc.setPoints(points);
		insertArc(displayedArc, transformOperators);
		return displayedArc;
	}
    
    public void insertArc(ArcModel arc, boolean transformOperators){
        AbstractElementModel sourceModel = getElementContainer()
        .getElementById(arc.getSourceId());
AbstractElementModel targetModel = getElementContainer()
        .getElementById(arc.getTargetId());
        getElementContainer().addReference(arc);

        /* IF transform Operators into classic petrinet */
        if (transformOperators)
        {
            OperatorTransitionModel operatorModel;
            /* IF THE SOURCE IS AN OPERATOR */
            if (sourceModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {

                // storing the source as operator
                operatorModel = (OperatorTransitionModel) sourceModel;
                // put the Target in the SimpleContainer of the operator
                operatorModel.addElement(getElementContainer().getElementById(
                        targetModel.getId()));

                LoggerManager
                        .debug(Constants.CORE_LOGGER,
                                "Connection from Aalst Model detected... resolve Inner-Transitions ...");

                // Register new outgoing connection with the operator.
                // This will generate all the necessary inner arcs
                operatorModel.registerOutgoingConnection(this, targetModel);

                LoggerManager.debug(Constants.CORE_LOGGER,
                        "... Inner-Transition resolving completed");
            }
            /* IF OPERATOR IS TARGET */
            else if (targetModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {

                LoggerManager
                        .debug(Constants.CORE_LOGGER,
                                "Connection to Aalst Model detected... resolve Inner-Transitions ...");
                // stor the target as operatorModel
                operatorModel = (OperatorTransitionModel) getElementContainer()
                        .getElementById(targetModel.getId());

                // add the source to the SimpleTransContainer
                operatorModel.addElement(getElementContainer().getElementById(
                        sourceModel.getId()));
                
                // Register new incoming connection with the operator.
                // This will generate all the necessary inner arcs
                operatorModel.registerIncomingConnection(this, sourceModel);

                LoggerManager.debug(Constants.CORE_LOGGER,
                        "... Inner-Transition resolving completed");

            }
        }
    }

	public void removeArc(Object id)
	{

		ArcModel arcToDelete = getElementContainer().getArcById(id);
		if (arcToDelete != null)
		{
			AbstractElementModel sourceElement = getElementContainer().getElementById(arcToDelete.getSourceId());
			AbstractElementModel targetElement = getElementContainer().getElementById(arcToDelete.getTargetId());
			
			if (sourceElement.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
			{
				OperatorTransitionModel currentOperator = (OperatorTransitionModel) sourceElement;
				// Register the removal of an outgoing arc
				currentOperator.registerOutgoingConnectionRemoval(this, targetElement);
								
				currentOperator.getSimpleTransContainer()
						.removeSourceArcsFromElement(arcToDelete.getTargetId());
				
				// Each inner transition container contains a local copy of each
				// element connecting to an operator
				// We have to remove this local copy
				currentOperator.getSimpleTransContainer()
						.removeElement(targetElement.getId());					
				
				// System.out.println("INNER ARC TO TARGET deleted");
			} else if (targetElement.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
			{
				OperatorTransitionModel currentOperator = (OperatorTransitionModel) targetElement;
				// Register the removal of an incoming arc
				currentOperator.registerIncomingConnectionRemoval(this, sourceElement);
				
				currentOperator.getSimpleTransContainer()
						.removeTargetArcsFromElement(arcToDelete.getSourceId());

				// Each inner transition container contains a local copy of each
				// element connecting to an operator
				// We have to remove this local copy
				currentOperator.getSimpleTransContainer()
						.removeElement(sourceElement.getId());
				
				
				LoggerManager.debug(Constants.CORE_LOGGER,
						"INNER ARC TO SOURCE deleted");
			}
			getElementContainer().removeArc(arcToDelete);
		}

	}

	/**
	 * Creates a new Trigger and adds it to the Transtition
	 * 
	 */
	public TriggerModel newTrigger(CreationMap map)
	{
		TransitionModel transition = (TransitionModel) getElementContainer()
				.getElementById(map.getId());
		if (transition.getToolSpecific() == null)
		{
			transition.setToolSpecific(new Toolspecific(transition.getId()));
		}
		return transition.getToolSpecific().setTrigger(map);

	}

	// TODO: DOCUMentation
	public TransitionResourceModel newTransResource(CreationMap map)
	{
		TransitionModel transition = (TransitionModel) getElementContainer()
				.getElementById(map.getId());
		if (transition.getToolSpecific() == null)
		{
			transition.setToolSpecific(new Toolspecific(transition.getId()));
		}
		return transition.getToolSpecific().setTransResource(map);
	}

	public String getNewElementId(int elementType)
	{
		String id = null;
		
		String prefix = "";
		AbstractElementModel owningElement = getElementContainer().getOwningElement();
		if (owningElement!=null)
		{
			prefix = owningElement.getId() + OperatorTransitionModel.INNERID_SEPERATOR;			
		}
		
		switch (elementType)
		{
		case PetriNetModelElement.SUBP_TYPE:
			id = "sub" + ++subprocessCounter;
			break;
		case PetriNetModelElement.PLACE_TYPE:
			id = "p" + ++placeCouter;
			break;
		case PetriNetModelElement.TRANS_SIMPLE_TYPE:
		case PetriNetModelElement.TRANS_OPERATOR_TYPE:
			id = "t" + ++transitionCounter;
			break;
		case PetriNetModelElement.SIMULATION_TYPE:
			id = "s" + ++simulationCounter;
			break;
		}
		if (id!=null)
		{
			// Prepend the prefix (used for sub-processes to make identifiers unique)
			id = prefix + id;
			// Check whether an element with the same ID already exists. 
			// If so, recursively call ourselves to obtain a new one
			if (getElementContainer().getElementById(id)!=null)
				id = getNewElementId(elementType);
		}
		return id;
	}

	public String getNexArcId()
	{
		String arcId = "a" + ++arcCounter;
		return getElementContainer().getArcById(arcId) != null ? getNexArcId()
				: arcId;
	}

	public void addResourceMapping(String resourceClass, String resourceId)
	{
		Vector<String> resourcesVector;
		if (containsRole(resourceClass) != -1
				|| containsOrgunit(resourceClass) != -1)
		{
			if (containsResource(resourceId) != -1)
			{
				if (getResourceMapping().get(resourceClass) == null)
				{
					resourcesVector = new Vector<String>();
					resourcesVector.add(resourceId);
					getResourceMapping().put(resourceClass, resourcesVector);
				} else
				{
					getResourceMapping().get(resourceClass).add(resourceId);
				}
			} else
			{
				LoggerManager.warn(Constants.CORE_LOGGER,
						"resource doesn't exist");
			}

		} else
		{
			LoggerManager.warn(Constants.CORE_LOGGER,
					"role or orgUnit doesn't exist");
		}

	}

	public void removeResourceMapping(String resourceClass, String resourceId)
	{
		Vector<String> tempResourceVector;
		tempResourceVector = (Vector<String>) getResourceMapping().get(resourceClass);
		tempResourceVector.remove(resourceId);
	}

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	public Vector<String> getResourceClassesResourceIsAssignedTo(String resourceId)
	{
		Vector<String> assignedVector = new Vector<String>();
		String resourceClassIdTemp;
		for (Iterator<String> iter = getResourceMapping().keySet().iterator(); iter
				.hasNext();)
		{
			resourceClassIdTemp = iter.next().toString();
			Vector<String> resourceVector = getResourceMapping().get(
					resourceClassIdTemp);
			if (containsResource(resourceVector, resourceId) != -1)
			{
				assignedVector.add(resourceClassIdTemp);
			}
		}
		return assignedVector;

	}

	// returns a vector with notAssigned resourceClasses, the vector contains
	// the ResourceClassModels!!
	// public Vector getResourceClassesResourceIsNotAssignedTo(String
	// resourceId)
	// {
	// Vector notAssignedVector = new Vector();
	// Vector assignedVector;
	// System.out.println("getResourceClassesResourceIsNotAssignedTo");
	// assignedVector = getResourceClassesResourceIsAssignedTo(resourceId);
	// Vector tempRoleVector = new Vector();
	// tempRoleVector =(Vector) getRoles().clone();
	// Vector tempOrgUnitVector = new Vector();
	// tempOrgUnitVector = (Vector) getOrganizationUnits().clone();
	// if (assignedVector.size()>0)
	// {
	// for (int i =0;i<assignedVector.size();i++)
	// {
	// for (int j=0;j<tempRoleVector.size();j++)
	// {
	// if((assignedVector.get(i)).equals(((ResourceClassModel)tempRoleVector.get(j)).getName()))
	// {
	// System.out.println("unassigned role" +tempRoleVector.get(j).toString());
	// tempRoleVector.remove(j);
	//                        		
	// }
	// }
	// for (int k=0;k<tempOrgUnitVector.size();k++)
	// {
	// if
	// ((assignedVector.get(i)).equals(((ResourceClassModel)tempOrgUnitVector.get(k)).getName()));
	// {
	// System.out.println("unassigned orgUnit" +
	// tempOrgUnitVector.get(k).toString());
	// tempOrgUnitVector.remove(k);
	// }
	// }
	// }
	// }
	// notAssignedVector.addAll(tempOrgUnitVector);
	// notAssignedVector.addAll(tempRoleVector);
	//        
	//        
	//
	//        
	//
	// return notAssignedVector;
	// }

	/**
	 * @return
	 */
	public Dimension getNetWindowSize()
	{
		return netWindowSize;
	}

	/**
	 * @param dimension
	 */
	public void setNetWindowSize(Dimension dimension)
	{
		netWindowSize = dimension;
	}

	/**
	 * 
	 * @return
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * The fingerprint is represented as a String with the following components concatenated:
	 * - the number of places
	 * - all place-IDs directly followed by their individual TokenCount
	 * - the '#'-sign as an seperator
	 * - the number of arcs
	 * - for each arc a concatenation of '*', the arc's sourceID and the arc's targetID
	 * Example: p11p20p30p4#6*p1t1*t1p2*p2t2*t2p3*p3t3*t3p4
	 * 
	 * @return Returns a String witch logically identifys the petrinet
	 */
	public String getLogicalFingerprint()
	{
		String fingerprint = "";
		// fingerprint of places & markings
		Iterator<AbstractElementModel> aemIter = getElementContainer().getElementsByType(PetriNetModelElement.PLACE_TYPE).values().iterator();
		int count = 0;
		while(aemIter.hasNext())
		{
			count++;
			PlaceModel currPlace = (PlaceModel) aemIter.next();
			fingerprint += currPlace.getId() + currPlace.getTokenCount();
		}
		fingerprint = count + fingerprint;
		fingerprint += "#";
		// fingerprint of arcs
		Iterator<ArcModel> amIter = this.getElementContainer().getArcMap().values().iterator();
		count = 0;
		String arcFingerprint = "";
		while(amIter.hasNext())
		{
			count++;
			ArcModel currArc = (ArcModel) amIter.next();
			// don't use ArcId in fingerprint because some ArcId's change on save/load pnml
			// but the arcs are distinctly described by arc-source and arc-target
			/*arcFingerprint += currArc.getId() + currArc.getSourceId() + currArc.getTargetId();*/
			arcFingerprint += "*" + currArc.getSourceId() + currArc.getTargetId();
		}
		fingerprint += count + arcFingerprint;
		// fingerprint of subprocesses
		//iter = this.getElementContainer().getElementsByType(PetriNetModelElement.SUBP_TYPE).values().iterator();
		
		return fingerprint;
	}
	
	/**
	 * Compares an fingerprint to the fingerprint of the current net on a logical basis
	 * and not absolute like the equals-method of the String would do
	 * 
	 * @param fingerprintToCheck The fingerprintrepresentation which get's compared to the current fingerprint
	 * @return <code>true</code> if equal, otherwise <code>false</code>
	 */
	public boolean isLogicalFingerprintEqual(String fingerprintToCheck)
	{
		boolean equal = true;
		String currFingerprint, currLeadingPart;
		currFingerprint = this.getLogicalFingerprint();
		currLeadingPart = currFingerprint.substring(0, currFingerprint.indexOf('*'));
		// if the strings have a different length or the leading part (places, tokens, number of Arcs)
		// is different, then the fingerprint is always different
		if(currFingerprint.length()==fingerprintToCheck.length()&&currLeadingPart.equals(fingerprintToCheck.substring(0,fingerprintToCheck.indexOf('*'))))
		{
			//creating a Hashmap with all arcsource-arctarget values from the current net
			HashSet<String> currentArcs = new HashSet<String>();
			Iterator<ArcModel> amIter = this.getElementContainer().getArcMap().values().iterator();
			while(amIter.hasNext())
			{
				ArcModel currArc = (ArcModel) amIter.next();
				currentArcs.add(currArc.getSourceId() + currArc.getTargetId());
			}
			
			//check if the arcs in the fingerprintToCheck are present in the HashSet of the current net's arcs
			String arcsToCheck = fingerprintToCheck.substring(fingerprintToCheck.indexOf('*'));
			String currArc;
			boolean deleted=true;
			while(deleted && arcsToCheck!="")
			{
				// delete the leading '*'
				arcsToCheck = arcsToCheck.substring(arcsToCheck.indexOf('*')+1); 
				// Get the next arc
				// - String till the next occurance of '*'
				// - or till the end of the String if there is no more '*'
				if(arcsToCheck.indexOf('*')!=-1)
				{
					currArc = arcsToCheck.substring(0, arcsToCheck.indexOf('*'));
					arcsToCheck.substring(arcsToCheck.indexOf('*'));
				}
				else
				{
					currArc = arcsToCheck;
					arcsToCheck = "";
				}
				deleted = currentArcs.remove(currArc);
			}
			
			if(currentArcs.size()!=0|!deleted)
			{
				equal = false;
			}
		}
		else
			equal = false;
		return equal;
	}

	public Vector<Object> getUnknownToolSpecs()
	{
		return unknownToolSpecs;
	}

	public void setUnknownToolSpecs(Vector<Object> unknownToolSpecs)
	{
		this.unknownToolSpecs = unknownToolSpecs;
	}

	public void addUnknownToolSpecs(Object unknownToolSpecs)
	{
		getUnknownToolSpecs().add(unknownToolSpecs);
	}

	/**
	 * @return Returns the organizationUnits.
	 */
	public Vector<ResourceClassModel> getOrganizationUnits()
	{
		if (organizationUnits == null)
		{
			organizationUnits = new Vector<ResourceClassModel>();
		}
		return organizationUnits;
	}

	/**
	 * @param organizationUnits
	 *            The organizationUnits to set.
	 */
	public void setOrganizationUnits(Vector<ResourceClassModel> organizationUnits)
	{
		this.organizationUnits = organizationUnits;
	}

	/**
	 * @return Returns the resourceMap.
	 */
	public HashMap<String, Vector<String>> getResourceMapping()
	{
		if (resourceMapping == null)
		{
			resourceMapping = new HashMap<String, Vector<String>>();
		}
		return resourceMapping;
	}

	/**
	 * @param resourceMap
	 *            The resourceMap to set.
	 */
	public void setResourceMapping(LinkedHashMap<String, Vector<String>> resourceMapping)
	{
		this.resourceMapping = resourceMapping;
	}

	/**
	 * @return Returns the roles.
	 */
	public Vector<ResourceClassModel> getRoles()
	{
		if (roles == null)
		{
			roles = new Vector<ResourceClassModel>();
		}
		return roles;
	}

	/**
	 * @param roles
	 *            The roles to set.
	 */
	public void setRoles(Vector<ResourceClassModel> roles)
	{
		this.roles = roles;
	}

	/**
	 * @return Returns the resources.
	 */
	public Vector<ResourceModel> getResources()
	{
		if (resources == null)
		{
			resources = new Vector<ResourceModel>();
		}
		return resources;
	}

	/**
	 * @return Returns the simulations.
	 */
	public Vector<SimulationModel> getSimulations()
	{
		if (simulations == null)
		{
			simulations = new Vector<SimulationModel>();
		}
		return simulations;
	}
	
	public void addRole(ResourceClassModel role)
	{
		getRoles().add(role);
	}

	public void addOrgUnit(ResourceClassModel orgUnit)
	{
		getOrganizationUnits().add(orgUnit);
	}

	public void addResource(ResourceModel resource)
	{
		getResources().add(resource);
	}
	
	public void addSimulation(SimulationModel simulation)
	{
		getSimulations().add(simulation);
	}

	public int containsRole(String name)
	{
		for (int i = 0; i < getRoles().size(); i++)
		{
			if (((ResourceClassModel) getRoles().get(i)).getName().equals(name))
				return i;

		}
		return -1;
	}

	public int containsOrgunit(String name)
	{
		for (int i = 0; i < getOrganizationUnits().size(); i++)
		{
			if (((ResourceClassModel) getOrganizationUnits().get(i)).getName()
					.equals(name))
				return i;

		}
		return -1;
	}

	public int containsResource(String name)
	{
		for (int i = 0; i < getResources().size(); i++)
		{
			if (((ResourceModel) getResources().get(i)).getName().equals(name))
				return i;

		}
		return -1;
	}

	public int containsResource(Vector<String> resourceVector, String name)
	{
		for (int i = 0; i < resourceVector.size(); i++)
		{
			if (resourceVector.get(i).equals(name))
				return i;

		}
		return -1;
	}

	public void replaceResourceMapping(String oldName, String newName)
	{
		for (Iterator<String> iter = getResourceMapping().keySet().iterator(); iter
				.hasNext();)
		{
			Vector<String> resourcevalues = getResourceMapping().get(
					iter.next());
			for (int i = 0; i < resourcevalues.size(); i++)
			{
				if (resourcevalues.get(i).equals(oldName))
					resourcevalues.set(i, newName);
			}
		}
	}

	public void replaceResourceClassMapping(String oldName, String newName)
	{
		if (getResourceMapping().containsKey(oldName))
		{
			getResourceMapping()
					.put(newName, getResourceMapping().get(oldName));
			getResourceMapping().remove(oldName);
		}
	}

	public void removeElement(AbstractElementModel element)
	{
		removeElement(element.getId());
	}

	public void removeElement(Object id)
	{
		getElementContainer().removeElement(id);
	}

	public int getArcCounter()
	{
		return arcCounter;
	}

	public void setArcCounter(int arcCounter)
	{
		this.arcCounter = arcCounter;
	}

	public int getPlaceCouter()
	{
		return placeCouter;
	}

	public void setPlaceCouter(int placeCouter)
	{
		this.placeCouter = placeCouter;
	}

	public int getSubprocessCounter()
	{
		return subprocessCounter;
	}

	public void setSubprocessCounter(int subprocessCounter)
	{
		this.subprocessCounter = subprocessCounter;
	}

	public int getTransitionCounter()
	{
		return transitionCounter;
	}

	public void setTransitionCounter(int transitionCounter)
	{
		this.transitionCounter = transitionCounter;
	}
	
	public int getSimulationCounter()
	{
		return simulationCounter;
	}
	
	public void setSimulationCounter(int simulationCounter)
	{
		this.simulationCounter = simulationCounter;
	}

	public void setResources(Vector<ResourceModel> resources) {
		this.resources = resources;
	}

	public void setResourceMapping(HashMap<String, Vector<String>> resourceMapping) {
		this.resourceMapping = resourceMapping;
	}

}