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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.ParentMap;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
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

	private Vector unknownToolSpecs = new Vector();

	private Vector roles = null;

	private Vector resources = null;

	private Vector organizationUnits = null;

	private HashMap resourceMapping = new HashMap();

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
	public AbstractElementModel createElement(CreationMap map, String idPreFix)
	{
		// creating a new ModelElement through Factory
		if (map.isValid())
		{
			if (map.getId() == null)
			{
				map.setId(idPreFix + getNewElementId(map.getType()));
			}
			AbstractElementModel anElement = ModelElementFactory
					.createModelElement(map);
			getElementContainer().addElement(anElement);
			
            if (map.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE
					|| map.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE
					|| map.getType() == PetriNetModelElement.SUBP_TYPE)
			{
				// Trigger
				if (map.getTriggerType() != -1)
					newTrigger(map);
				if (map.getTriggerPosition() != null)
					((TransitionModel) anElement).getToolSpecific()
							.getTrigger().setPosition(
									map.getTriggerPosition().getX1(),
									map.getTriggerPosition().getX2());
				if (map.getResourceOrgUnit() != null
						&& map.getResourceRole() != null)
					newTransResource(map);
				if (map.getResourcePosition() != null)
					((TransitionModel) anElement).getToolSpecific()
							.getTransResource().setPosition(
									map.getResourcePosition().getX1(),
									map.getResourcePosition().getX2());
				if (map.getTransitionTime()!=-1)
					((TransitionModel)anElement).getToolSpecific().setTime(map.getTransitionTime());
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
		OperatorTransitionModel operatorModel;

		// falls aalst source oder target -> update Model
		ArcModel displayedArc = ModelElementFactory.createArcModel(
				getNexArcId(), (DefaultPort) sourceModel.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));
		displayedArc.setPoints(points);
		getElementContainer().addReference(displayedArc);

		/* IF transform Operators into classic petrinet */
		if (transformOperators)
		{
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

				/* IF SOURCE IS XOR SPLIT */
				if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
				{

					// Wenn die Referenztabelle von dem Operator nur diesen
					// neuen Eintrag hat
					if (getElementContainer().getTargetElements(
							operatorModel.getId()).size() == 1
							&& operatorModel
									.getSimpleTransContainer()
									.getElementsByType(
											PetriNetModelElement.TRANS_SIMPLE_TYPE)
									.size() == 1)
					{
						// get simple trans
						TransitionModel simpleTrans = (TransitionModel) operatorModel
								.getSimpleTransContainer().getElementsByType(
										PetriNetModelElement.TRANS_SIMPLE_TYPE)
								.values().iterator().next();
						// create an reference entry
						operatorModel.addReference(getNexArcId(),
								(DefaultPort) simpleTrans.getChildAt(0),
								(DefaultPort) targetModel.getChildAt(0));

					} else
					{

						// create simple trans
						TransitionModel simpleTrans = operatorModel
								.addNewSimpleTrans();
						// create an reference entry for the existing
						// simpleTrans
						operatorModel.addReference(getNexArcId(),
								(DefaultPort) simpleTrans.getChildAt(0),
								(DefaultPort) targetModel.getChildAt(0));

						// create reference from each source to each inner
						// transition
						Iterator sourceIter = getElementContainer()
								.getSourceElements(operatorModel.getId())
								.keySet().iterator();
						while (sourceIter.hasNext())
						{
							operatorModel.addReference(getNexArcId(),
									(DefaultPort) getElementContainer()
											.getElementById(sourceIter.next())
											.getChildAt(0),
									(DefaultPort) simpleTrans.getChildAt(0));
						}
					}

				}
				/* IF SOURCE IS XOR JOIN */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
				{

					// für jede simpletrans ne connection auf das target
					Iterator simpleRootIter = operatorModel
							.getSimpleTransContainer().getRootElements()
							.iterator();
					while (simpleRootIter.hasNext())
					{
						PetriNetModelElement pec = (PetriNetModelElement) simpleRootIter
								.next();
						if (pec.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
							operatorModel.addReference(getNexArcId(), pec
									.getPort(), targetModel.getPort());
					}
				}
				/* IF SOURCE IS XOR SPLITJOIN */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
				{
					// For EACH outgoing connection we need a new simpletrans as
					// target for centerplace
					TransitionModel simpleTrans;
					if (operatorModel.getSimpleTransContainer().getArcMap()
							.size() > 0)
					{
						simpleTrans = operatorModel.addNewSimpleTrans();
					} else
					{
						simpleTrans = (TransitionModel) operatorModel
								.getSimpleTransContainer().getElementsByType(
										PetriNetModelElement.TRANS_SIMPLE_TYPE)
								.values().iterator().next();
					}
					PlaceModel centerPlace = operatorModel.getCenterPlace();
					// centerplace with simpletrans
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) centerPlace.getChildAt(0),
							(DefaultPort) simpleTrans.getChildAt(0));
					// simpletrans with target
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) simpleTrans.getChildAt(0),
							(DefaultPort) targetModel.getChildAt(0));
				}
				/* IF SOURCE IS OR SPLIT */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
				{
					int z = getElementContainer().getTargetElements(
							operatorModel.getId()).size();
					// Wenn die Referenztabelle von dem Aalst Element nur diesen
					// neuen Eintrag hat
					if (z == 1
							&& operatorModel
									.getSimpleTransContainer()
									.getElementsByType(
											PetriNetModelElement.TRANS_SIMPLE_TYPE)
									.size() == 1)
					{
						// get simple trans
						Object simpleTransId = operatorModel
								.getSimpleTransContainer().getElementsByType(
										PetriNetModelElement.TRANS_SIMPLE_TYPE)
								.keySet().iterator().next();
						TransitionModel simpleTrans = (TransitionModel) operatorModel
								.getElement(simpleTransId);
						// create an reference entry
						operatorModel.addReference(getNexArcId(),
								(DefaultPort) simpleTrans.getChildAt(0),
								(DefaultPort) targetModel.getChildAt(0));

					} else if (z > 5 | z == 0)
					{
						LoggerManager
								.error(Constants.CORE_LOGGER,
										"The Maximum for OR Split is 5 following places.");
					} else
					{

						Iterator targetIter = getElementContainer()
								.getTargetElements(operatorModel.getId())
								.keySet().iterator();
						Object[] targets = new Object[OperatorTransitionModel.OR_SPLIT_TABLE[z].length];
						int j = z - 2;
						// new target must be first entry
						targets[z - 1] = targetId;
						while (targetIter.hasNext())
						{
							// fill array (from back to front) with old targets
							Object tempId = targetIter.next();
							if (!tempId.equals(targetId))
							{
								targets[j] = tempId;
								j--;
							}
						}

						int trans = (int) Math.pow(2, z) - 1;
						int newTrans = trans - ((int) Math.pow(2, z - 1) - 1);
						// create all inner transitions
						TransitionModel simpleTrans;
						Iterator sourceIter;
						for (int k = trans - newTrans; k < trans; k++)
						{
							// create simple trans
							simpleTrans = operatorModel.addNewSimpleTrans();
							// create outgoing references for simple trans
							for (short l = 0; l < z; l++)
							{
								if (OperatorTransitionModel.OR_SPLIT_TABLE[z][k][l] == 1)
								{
									operatorModel.addReference(getNexArcId(),
											(DefaultPort) simpleTrans
													.getChildAt(0),
											(DefaultPort) operatorModel
													.getSimpleTransContainer()
													.getElementById(targets[l])
													.getChildAt(0));
								}
							}
							sourceIter = getElementContainer()
									.getSourceElements(operatorModel.getId())
									.keySet().iterator();
							// Connect each Source with each Inner transition
							while (sourceIter.hasNext())
							{
								operatorModel
										.addReference(
												getNexArcId(),
												(DefaultPort) getElementContainer()
														.getElementById(
																sourceIter
																		.next())
														.getChildAt(0),
												(DefaultPort) simpleTrans
														.getChildAt(0));

							}
						}
					}

				} else if (operatorModel.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE
						|| operatorModel.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
						|| operatorModel.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
				{
					// get simple trans
					Object simpleId = operatorModel.getSimpleTransContainer()
							.getElementsByType(
									PetriNetModelElement.TRANS_SIMPLE_TYPE)
							.keySet().iterator().next();
					TransitionModel simpleTrans;
					if ((simpleTrans = (TransitionModel) operatorModel
							.getElement(simpleId)) == null)
					{
						simpleTrans = operatorModel.addNewSimpleTrans();
					}
					// create an reference entry
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) simpleTrans.getChildAt(0),
							(DefaultPort) targetModel.getChildAt(0));
				}
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

				/* IF target is XOR JOIN */
				if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
				{
					// GENAU WIE XOR SPILT BEI SOURCE !!
					/*
					 * If operator have only one Source AND container does only
					 * have one simple trans
					 */
					if (getElementContainer().getSourceElements(
							operatorModel.getId()).size() == 1
							&& operatorModel
									.getSimpleTransContainer()
									.getElementsByType(
											PetriNetModelElement.TRANS_SIMPLE_TYPE)
									.size() == 1)
					{
						// get simple trans
						TransitionModel simpleTrans = (TransitionModel) operatorModel
								.getSimpleTransContainer().getElementsByType(
										PetriNetModelElement.TRANS_SIMPLE_TYPE)
								.values().iterator().next();
						// dann füge nur die Reference hinzu
						operatorModel.addReference(getNexArcId(),
								(DefaultPort) sourceModel.getChildAt(0),
								(DefaultPort) simpleTrans.getChildAt(0));
					} else
					{
						// create new SimpleTrans
						// add simpletrans to SimpleTransContainer
						TransitionModel simpleTrans = operatorModel
								.addNewSimpleTrans();

						// add new arc from new source to cew simpleTrans in
						// SimpleTransConatainer
						operatorModel.addReference(getNexArcId(),
								(DefaultPort) sourceModel.getChildAt(0),
								(DefaultPort) simpleTrans.getChildAt(0));

						// create a new arc from EACH SimpleTrans to EACH Target
						// of the Operator
						Iterator targetIter = getElementContainer()
								.getTargetElements(operatorModel.getId())
								.keySet().iterator();
						while (targetIter.hasNext())
						{
							operatorModel.addReference(getNexArcId(),
									(DefaultPort) simpleTrans.getChildAt(0),
									(DefaultPort) getElementContainer()
											.getElementById(targetIter.next())
											.getChildAt(0));
						}
					}
				}
				/* IF target IS XOR SPLITJOIN */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
				{
					// For EACH incomming connection we need a new simpletrans
					// as source for centerplace
					TransitionModel simpleTrans;
					if (operatorModel.getSimpleTransContainer().getArcMap()
							.size() > 0)
					{
						simpleTrans = operatorModel.addNewSimpleTrans();
					} else
					{
						simpleTrans = (TransitionModel) operatorModel
								.getSimpleTransContainer().getElementsByType(
										PetriNetModelElement.TRANS_SIMPLE_TYPE)
								.values().iterator().next();
					}
					PlaceModel centerPlace = operatorModel.getCenterPlace();
					// simpletrans with centerplace
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) simpleTrans.getChildAt(0),
							(DefaultPort) centerPlace.getChildAt(0));
					// source with simpletrans
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) sourceModel.getChildAt(0),
							(DefaultPort) simpleTrans.getChildAt(0));
				}
				/* IF target ist XOR SPLIT OR OR SPLIT */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE
						|| operatorModel.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
				{

					// create & add new arc From EACH Source to EACH SimpleTrans
					// of Operator
					Iterator sourceIter = getElementContainer()
							.getSourceElements(operatorModel.getId()).keySet()
							.iterator();
					while (sourceIter.hasNext())
					{
						Iterator simpleRootIter = operatorModel
								.getSimpleTransContainer().getRootElements()
								.iterator();
						AbstractElementModel tempSource = getElementContainer()
								.getElementById(sourceIter.next());
						while (simpleRootIter.hasNext())
						{
							// wenn simpletrans dann reference
							PetriNetModelElement tempTarget = (PetriNetModelElement) simpleRootIter
									.next();
							if (tempTarget.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
								operatorModel.addReference(getNexArcId(),
										(DefaultPort) tempSource.getChildAt(0),
										(DefaultPort) tempTarget.getChildAt(0));
						}
					}
				}
				/* IF target is AND SPLIT / JOIN OR COMBINED AND SPLIT JOIN */
				else if (operatorModel.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE
						|| operatorModel.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
						|| operatorModel.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
				{
					// get simple trans
					Object simpleTransId = operatorModel
							.getSimpleTransContainer().getElementsByType(
									PetriNetModelElement.TRANS_SIMPLE_TYPE)
							.keySet().iterator().next();
					TransitionModel simpleTrans;
					if ((simpleTrans = (TransitionModel) operatorModel
							.getElement(simpleTransId)) == null)
					{
						simpleTrans = operatorModel.addNewSimpleTrans();
					}
					// dann füge nur die Reference hinzu
					operatorModel.addReference(getNexArcId(),
							(DefaultPort) sourceModel.getChildAt(0),
							(DefaultPort) simpleTrans.getChildAt(0));
				}
				LoggerManager.debug(Constants.CORE_LOGGER,
						"... Inner-Transition resolving completed");

			}
		}

		return displayedArc;
	}

	public void removeArc(Object id)
	{

		ArcModel arcToDelete = getElementContainer().getArcById(id);
		if (arcToDelete != null)
		{
			if (getElementContainer().getElementById(arcToDelete.getSourceId())
					.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
			{
				OperatorTransitionModel currentOperator = (OperatorTransitionModel) getElementContainer()
						.getElementById(arcToDelete.getSourceId());
				// SOURCE IS XOR-SPLIT OPERATOR => delete inner Transition that
				// is source to place IF more than 1 inner transition

				if (currentOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
				{
					if (currentOperator.getSimpleTransContainer()
							.getElementsByType(
									PetriNetModelElement.TRANS_SIMPLE_TYPE)
							.size() != 1)
					{
						currentOperator.getSimpleTransContainer()
								.removeAllSourceElements(
										arcToDelete.getTargetId());
						// System.out.println("INNER Source Elements deleted");
					}
				}
				/* IF SOURCE IS XOR SPLITJOIN */
				else if (currentOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
				{
					// remove the source simpleTrans for this arc!
					currentOperator.getSimpleTransContainer()
							.removeAllSourceElements(arcToDelete.getTargetId());
				}
				currentOperator.getSimpleTransContainer()
						.removeSourceArcsFromElement(arcToDelete.getTargetId());
				// System.out.println("INNER ARC TO TARGET deleted");
			} else if (getElementContainer().getElementById(
					arcToDelete.getTargetId()).getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
			{
				OperatorTransitionModel currentOperator = (OperatorTransitionModel) getElementContainer()
						.getElementById(arcToDelete.getTargetId());
				// TARGET IS XOR-JOIN OPERATOR => delete inner Transition that
				// is Target to place ID more than 1 inner transition

				if (currentOperator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
				{
					if (currentOperator.getSimpleTransContainer()
							.getElementsByType(
									PetriNetModelElement.TRANS_SIMPLE_TYPE)
							.size() != 1)
					{
						currentOperator.getSimpleTransContainer()
								.removeAllTargetElements(
										arcToDelete.getSourceId());
						LoggerManager.debug(Constants.CORE_LOGGER,
								"INNER Target Elements deleted");
					}
				}
				/* IF TARGET IS XOR SPLITJOIN */
				else if (currentOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
				{
					// remove the target simpleTrans for this arc!
					currentOperator.getSimpleTransContainer()
							.removeAllTargetElements(arcToDelete.getSourceId());
				}
				currentOperator.getSimpleTransContainer()
						.removeTargetArcsFromElement(arcToDelete.getSourceId());
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
		String id;
		if (elementType == PetriNetModelElement.SUBP_TYPE)
		{
			id = "sub" + ++subprocessCounter;
			return getElementContainer().getElementById(id) != null ? getNewElementId(elementType)
					: id;
		} else if (elementType == PetriNetModelElement.PLACE_TYPE)
		{
			id = "p" + ++placeCouter;
			return getElementContainer().getElementById(id) != null ? getNewElementId(elementType)
					: id;
		} else if (elementType == PetriNetModelElement.TRANS_SIMPLE_TYPE
				|| elementType == PetriNetModelElement.TRANS_OPERATOR_TYPE)
		{
			id = "t" + ++transitionCounter;
			return getElementContainer().getElementById(id) != null ? getNewElementId(elementType)
					: id;
		} else if (elementType == PetriNetModelElement.SUBP_TYPE)
		{
			id = "t" + ++subprocessCounter;
			return getElementContainer().getElementById(id) != null ? getNewElementId(elementType)
					: id;
		} else
			return null;
	}

	public String getNexArcId()
	{
		String arcId = "a" + ++arcCounter;
		return getElementContainer().getArcById(arcId) != null ? getNexArcId()
				: arcId;
	}

	public void addResourceMapping(String resourceClass, String resourceId)
	{
		Vector resourcesVector;
		if (containsRole(resourceClass) != -1
				|| containsOrgunit(resourceClass) != -1)
		{
			if (containsResource(resourceId) != -1)
			{
				if (getResourceMapping().get(resourceClass) == null)
				{
					resourcesVector = new Vector();
					resourcesVector.add(resourceId);
					getResourceMapping().put(resourceClass, resourcesVector);
				} else
				{
					((Vector) getResourceMapping().get(resourceClass))
							.add(resourceId);
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
		Vector tempResourceVector;
		tempResourceVector = (Vector) getResourceMapping().get(resourceClass);
		tempResourceVector.remove(resourceId);
	}

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	public Vector getResourceClassesResourceIsAssignedTo(String resourceId)
	{
		Vector assignedVector = new Vector();
		String resourceClassIdTemp;
		for (Iterator iter = getResourceMapping().keySet().iterator(); iter
				.hasNext();)
		{
			resourceClassIdTemp = iter.next().toString();
			Vector resourceVector = (Vector) getResourceMapping().get(
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

	public Vector getUnknownToolSpecs()
	{
		return unknownToolSpecs;
	}

	public void setUnknownToolSpecs(Vector unknownToolSpecs)
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
	public Vector getOrganizationUnits()
	{
		if (organizationUnits == null)
		{
			organizationUnits = new Vector();
		}
		return organizationUnits;
	}

	/**
	 * @param organizationUnits
	 *            The organizationUnits to set.
	 */
	public void setOrganizationUnits(Vector organizationUnits)
	{
		this.organizationUnits = organizationUnits;
	}

	/**
	 * @return Returns the resourceMap.
	 */
	public HashMap getResourceMapping()
	{
		if (resourceMapping == null)
		{
			resourceMapping = new HashMap();
		}
		return resourceMapping;
	}

	/**
	 * @param resourceMap
	 *            The resourceMap to set.
	 */
	public void setResourceMapping(LinkedHashMap resourceMapping)
	{
		this.resourceMapping = resourceMapping;
	}

	/**
	 * @return Returns the roles.
	 */
	public Vector getRoles()
	{
		if (roles == null)
		{
			roles = new Vector();
		}
		return roles;
	}

	/**
	 * @param roles
	 *            The roles to set.
	 */
	public void setRoles(Vector roles)
	{
		this.roles = roles;
	}

	/**
	 * @return Returns the resources.
	 */
	public Vector getResources()
	{
		if (resources == null)
		{
			resources = new Vector();
		}
		return resources;
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

	public int containsResource(Vector resourceVector, String name)
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
		for (Iterator iter = getResourceMapping().keySet().iterator(); iter
				.hasNext();)
		{
			Vector resourcevalues = (Vector) getResourceMapping().get(
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

}