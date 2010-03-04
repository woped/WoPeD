package org.woped.qualanalysis.soundness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.interfaces.ISComponent;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent.ISComponentTest;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class SComponentImplement implements ISComponent {

	private IEditor editor = null;
	private ISComponentTest sComponentTest = null;

	/**
	 * 
	 * @param editor
	 *            source object
	 */
	public SComponentImplement(IEditor editor) {
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.service.ISComponent#getNotSCoveredIterator()
	 */
	@Override
	public Iterator<AbstractElementModel> getNotSCoveredIterator() {
		Set<AbstractElementModel> notSCovered = new HashSet<AbstractElementModel>();
		for (PlaceNode place : getSComponentTest().getNotSCovered()) {
			notSCovered.add(getAEM(place));
		}
		return notSCovered.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.service.ISComponent#getSComponentsIterator()
	 */
	@Override
	public Iterator<List<AbstractElementModel>> getSComponentsIterator() {
		Set<List<AbstractElementModel>> sComponentsSet = new HashSet<List<AbstractElementModel>>();
		List<AbstractElementModel> sComponent;
		for (Set<AbstractNode> set : getSComponentTest().getSComponents()) {
			sComponent = new ArrayList<AbstractElementModel>();
			for (AbstractNode node : set) {
				if (!sComponent.contains(getAEM(node)))
					sComponent.add(getAEM(node));
			}
			// remove t* if existing
			sComponent.remove(null);
			sComponentsSet.add(sComponent);
		}
		return sComponentsSet.iterator();
	}

	/**
	 * 
	 * @param node
	 *            the AbstractNode to get the referring AbstractElementModel from
	 * @return the referred AbstractElementModel
	 */
	private AbstractElementModel getAEM(AbstractNode node) {
		return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
	}

	/**
	 * 
	 * @return the SComponentTest (if not existing it will be instantiated)
	 */
	private ISComponentTest getSComponentTest() {
		if (sComponentTest == null) {
			sComponentTest = AlgorithmFactory.createSComponentTest(BuilderFactory
					.createLowLevelPetriNetWithTStarBuilder(editor).getLowLevelPetriNet());
		}
		return sComponentTest;
	}

}
