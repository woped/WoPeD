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

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.woped.core.Constants;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lai
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */

@SuppressWarnings("serial")
public class SubProcessModel extends TransitionModel implements
		InnerElementContainer {

	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	private static final String SUBELEMENT_SEPERATOR = "_";
	private int subElementCounter = 0;
	private ModelElementContainer subElementContainer;
	private boolean direction = false;
	private CreationMap map = null;

	public SubProcessModel(CreationMap map) {
		super(map);
		this.map = map;
		setSize(WIDTH, HEIGHT);
		getToolSpecific().setSubprocess(true);

		// there is already a container -> make a copy
		if (map.getSubElementContainer() != null) {
			ModelElementContainer newContainer = copySubElementContainer(map
					.getSubElementContainer());

			newContainer.setOwningElement(this);
			subElementContainer = newContainer;
		} else {
			// The sub element container
			// is owned by the subprocess
			subElementContainer = new ModelElementContainer();
			subElementContainer.setOwningElement(this);
			// System.err.println(this.getToolTipText());
		}

	}

	private ModelElementContainer copySubElementContainer(
			ModelElementContainer container) {

		LoggerManager.info(Constants.CORE_LOGGER, "copySubElementContainer");
		ModelElementContainer newContainer = new ModelElementContainer();

		newContainer.setOwningElement(container.getOwningElement());

		PetriNetModelProcessor processor = new PetriNetModelProcessor();
		processor.setElementContainer(newContainer);

		// HashMap<String, String> idMapper = new HashMap<String, String>();

		// copy elements

		Map<?, ?> idMap = container.getIdMap();
		Iterator<?> keyIterator = idMap.keySet().iterator();
		CreationMap currentArcMapSub;
		AbstractPetriNetElementModel newElementSub = null;
		while (keyIterator.hasNext()) {

			AbstractPetriNetElementModel currentElementSub = container
					.getElementById(keyIterator.next());

			if (!currentElementSub.isReadOnly()) {
				CreationMap newMapSub = (CreationMap) currentElementSub
						.getCreationMap().clone();
				// newMap.setId("copyof_" + newMap.getId());
				String[] splitName = newMapSub.getId().split(
						SUBELEMENT_SEPERATOR);
				if (splitName.length > 1)
					newMapSub.setId(super.getId() + SUBELEMENT_SEPERATOR
							+ splitName[splitName.length - 1]);
				newMapSub.setName(newMapSub.getId());
				newElementSub = ModelElementFactory
						.createModelElement(newMapSub);
				
				if (currentElementSub instanceof TransitionModel) {
					TransitionModel newTransitionSub = (TransitionModel) newElementSub;
					newTransitionSub
							.setToolSpecific(((TransitionModel) currentElementSub)
									.getToolSpecific());

					newContainer.addElement(newTransitionSub);

				} else

					newContainer.addElement(newElementSub);
			}
		}
		/* insert arc source/target */
		Iterator<?> arcIterSub = container.getArcMap().keySet().iterator();
		CreationMap cMapSub = CreationMap.createMap();
		String originalElementId;
		String[] splitedSourceId, splitedTargetId;
		while (arcIterSub.hasNext()) {
			currentArcMapSub = container.getArcMap().get((arcIterSub.next()))
					.getCreationMap();
			originalElementId = currentArcMapSub.getName();
			splitedSourceId = currentArcMapSub.getArcSourceId().split(
					SUBELEMENT_SEPERATOR);
			splitedTargetId = currentArcMapSub.getArcTargetId().split(
					SUBELEMENT_SEPERATOR);

			String newSourceName ;
			String newTargetName ;
			
			if ((splitedSourceId.length > 1) && (splitedTargetId.length > 1)) {
				newTargetName =super.getId() + SUBELEMENT_SEPERATOR
				+ splitedTargetId[splitedTargetId.length - 1];
				newSourceName = super.getId() + SUBELEMENT_SEPERATOR
				+ splitedSourceId[splitedSourceId.length - 1];
				cMapSub.setArcSourceId(newSourceName);
				cMapSub.setArcTargetId(newTargetName);
				processor.createArc(newSourceName,
						newTargetName);
			}
			else if ((splitedSourceId.length <= 1) && (splitedTargetId.length > 1)) {
				newTargetName =super.getId() + SUBELEMENT_SEPERATOR
				+ splitedTargetId[splitedTargetId.length - 1];
				cMapSub.setArcTargetId(newTargetName);
				((TransitionModel) newContainer.getElementById(newTargetName)).setIncomingTarget(true);
				processor.createArc(originalElementId,
						cMapSub.getArcTargetId());
			}
			else if ((splitedSourceId.length > 1) && (splitedTargetId.length <= 1)) {
				newSourceName = super.getId() + SUBELEMENT_SEPERATOR
				+ splitedSourceId[splitedSourceId.length - 1];
				cMapSub.setArcSourceId(newSourceName);
				((TransitionModel) newContainer.getElementById(newSourceName)).setOutgoingSource(true);
			}
		}
		/* insert arcs */
		return newContainer;
	}

	public CreationMap getMap() {
		return map;
	}

	public String getToolTipText() {
		return "Subprocess\nID: " + getId() + "\nName: " + getNameValue();
	}

	public int getType() {
		return AbstractPetriNetElementModel.SUBP_TYPE;
	}

	public ModelElementContainer getSimpleTransContainer() {
		return subElementContainer;
	}

	public ModelElementContainer getElementContainer() {
		return this.subElementContainer;
	}

	public AbstractPetriNetElementModel addElement(AbstractPetriNetElementModel element) {
		return subElementContainer.addElement(element);
	}

	public void addReference(String arcId, DefaultPort sourceId,
			DefaultPort targetId) {
		subElementContainer.addReference(ModelElementFactory.createArcModel(
				arcId, sourceId, targetId));
	}

	public AbstractPetriNetElementModel getElement(Object elementId) {
		return subElementContainer.getElementById(elementId);
	}

	public String getNewElementId() {
		subElementCounter++;
		return getId() + SUBELEMENT_SEPERATOR + subElementCounter;
	}

	// ! Overwritten to allow new outgoing connections only
	// ! if there is not already an outgoing connection
	// ! (Sub-processes must have exactly one input and one output)
	public boolean getAllowOutgoingConnections() {
		boolean result = true;
		int nNumOutgoing = 0;
		for (Iterator<?> i = getPort().edges(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Edge) {
				Edge e = (Edge) o;
				if (e.getSource() == getPort())
					++nNumOutgoing;
			}
		}
		result = (nNumOutgoing == 0);
		return result;
	}

	public boolean getDirection() {
		return direction;
	}

	public void setDirection(boolean dir) {
		direction = dir;
	}

}