package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.ISoundnessCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

public class SoundnessCheckImplement implements ISoundnessCheck {

	private IEditor editor;
	private MarkingNet markingNetWithoutTStar;
	private MarkingNet markingNetWithTStar;

	public SoundnessCheckImplement(IEditor editor) {
		this.editor = editor;
		markingNetWithoutTStar = BuilderFactory.createMarkingNet(BuilderFactory
				.createLowLevelPetriNetWithoutTStarBuilder(editor).getLowLevelPetriNet());
		markingNetWithTStar = BuilderFactory.createMarkingNet(BuilderFactory.createLowLevelPetriNetWithTStarBuilder(
				editor).getLowLevelPetriNet());
	}

	public MarkingNet getMarkingNet() {
		return this.markingNetWithoutTStar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getNumNonLiveTransitions()
	 */
	@Override
	public int getNumNonLiveTransitions() {
		String tStartId = "t*";
		Set<String> unliveTransitions = new HashSet<String>();
		for (TransitionNode transition : AlgorithmFactory.createNonLiveTranstionTest(markingNetWithTStar)
				.getNonLiveTransitions()) {
			unliveTransitions.add(transition.getOriginId());
		}
		// remove tStar-element if existing
		if (unliveTransitions.contains(tStartId))
			unliveTransitions.remove(tStartId);
		return unliveTransitions.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.woped.qualanalysis.ISoundnessCheck#getNonLiveTransitionsIterator()
	 */
	@Override
	public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
		Set<AbstractElementModel> unliveTransitions = new HashSet<AbstractElementModel>();
		for (TransitionNode transition : AlgorithmFactory.createNonLiveTranstionTest(markingNetWithTStar)
				.getNonLiveTransitions()) {
			unliveTransitions.add(editor.getModelProcessor().getElementContainer().getElementById(
					transition.getOriginId()));
		}
		// remove tStar-element if existing
		if (unliveTransitions.contains(null))
			unliveTransitions.remove(null);
		return unliveTransitions.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getNumDeadTransitions()
	 */
	@Override
	public int getNumDeadTransitions() {
		Set<String> deadTransitions = new HashSet<String>();
		for (TransitionNode transition : AlgorithmFactory.createDeadTransitionTest(markingNetWithoutTStar)
				.getDeadTransitions()) {
			deadTransitions.add(transition.getOriginId());
		}
		return deadTransitions.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getDeadTransitionsIterator()
	 */
	@Override
	public Iterator<AbstractElementModel> getDeadTransitionsIterator() {
		Set<AbstractElementModel> deadTransitions = new HashSet<AbstractElementModel>();
		for (TransitionNode transition : AlgorithmFactory.createDeadTransitionTest(markingNetWithoutTStar)
				.getDeadTransitions()) {
			deadTransitions.add(editor.getModelProcessor().getElementContainer().getElementById(
					transition.getOriginId()));
		}
		return deadTransitions.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getNumUnboundedPlaces()
	 */
	@Override
	public int getNumUnboundedPlaces() {
		Set<String> unboundedPlaces = new HashSet<String>();
		for (PlaceNode place : AlgorithmFactory.createUnboundedPlacesTest(markingNetWithoutTStar).getUnboundedPlaces()) {
			unboundedPlaces.add(place.getOriginId());
		}
		return unboundedPlaces.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getUnboundedPlacesIterator()
	 */
	@Override
	public Iterator<AbstractElementModel> getUnboundedPlacesIterator() {
		Set<AbstractElementModel> unboundedPlaces = new HashSet<AbstractElementModel>();
		for (PlaceNode place : AlgorithmFactory.createUnboundedPlacesTest(markingNetWithoutTStar).getUnboundedPlaces()) {
			unboundedPlaces.add(editor.getModelProcessor().getElementContainer().getElementById(place.getOriginId()));
		}
		return unboundedPlaces.iterator();
	}
}
