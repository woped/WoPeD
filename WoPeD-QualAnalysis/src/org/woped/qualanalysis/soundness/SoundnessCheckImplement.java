package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.service.interfaces.ISoundnessCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class SoundnessCheckImplement implements ISoundnessCheck {

	private IEditor editor = null;
	private IMarkingNet markingNetWithoutTStar = null;
	private IMarkingNet markingNetWithTStar = null;

	private Set<AbstractPetriNetElementModel> nonLiveTransitions = null;
	private Set<AbstractPetriNetElementModel> deadTransitions = null;
	private Set<AbstractPetriNetElementModel> unboundedPlaces = null;

	public SoundnessCheckImplement(IEditor editor) {
		this.editor = editor;

	}

	/**
	 * 
	 * @return the MarkingNet basing on the LowLevelPetriNet without t* (if not existing it will be instantiated)
	 */
	private IMarkingNet getMarkingNetWithoutTStar() {
		if (markingNetWithoutTStar == null) {
			markingNetWithoutTStar = BuilderFactory.createMarkingNet(BuilderFactory
					.createLowLevelPetriNetWithoutTStarBuilder(editor).getLowLevelPetriNet());
		}
		return this.markingNetWithoutTStar;
	}

	/**
	 * 
	 * @return the MarkingNet basing on the LowLevelPetriNet with t* (if not existing it will be instantiated)
	 */
	private IMarkingNet getMarkingNetWithTStar() {
		if (markingNetWithTStar == null) {
			markingNetWithTStar = BuilderFactory.createMarkingNet(BuilderFactory
					.createLowLevelPetriNetWithTStarBuilder(editor).getLowLevelPetriNet());
		}
		return this.markingNetWithTStar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getNonLiveTransitionsIterator() runtime optimization. if no dead
	 * transitions found in the net and the marking net is strongly connected, the petri net is live.
	 */
	@Override
	public Set<AbstractPetriNetElementModel> getNonLiveTransitions() {
		if (nonLiveTransitions == null) {
			nonLiveTransitions = new HashSet<AbstractPetriNetElementModel>();

			if (getDeadTransitions().size() != 0
					|| !AlgorithmFactory.createSccTest(getMarkingNetWithTStar()).isStronglyConnected()) {
				for (TransitionNode transition : AlgorithmFactory.createNonLiveTranstionTest(getMarkingNetWithTStar())
						.getNonLiveTransitions()) {
					nonLiveTransitions.add(getAEM(transition));
				}
			}
			// remove t* if existing
			nonLiveTransitions.remove(null);
		}
		return nonLiveTransitions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getDeadTransitionsIterator()
	 */
	@Override
	public Set<AbstractPetriNetElementModel> getDeadTransitions() {
		if (deadTransitions == null) {
			deadTransitions = new HashSet<AbstractPetriNetElementModel>();
			for (TransitionNode transition : AlgorithmFactory.createDeadTransitionTest(getMarkingNetWithoutTStar())
					.getDeadTransitions()) {
				deadTransitions.add(getAEM(transition));
			}
			// remove t* if existing
			deadTransitions.remove(null);
		}
		return deadTransitions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.qualanalysis.ISoundnessCheck#getUnboundedPlaces()
	 */
	@Override
	public Set<AbstractPetriNetElementModel> getUnboundedPlaces() {
		if (unboundedPlaces == null) {
			unboundedPlaces = new HashSet<AbstractPetriNetElementModel>();
			for (PlaceNode place : AlgorithmFactory.createUnboundedPlacesTest(getMarkingNetWithTStar())
					.getUnboundedPlaces()) {
				unboundedPlaces.add(getAEM(place));
			}
		}
		return unboundedPlaces;
	}

	/**
	 * 
	 * @param node
	 *            the AbstractNode to get the referring AbstractElementModel from
	 * @return the referred AbstractElementModel
	 */
	private AbstractPetriNetElementModel getAEM(AbstractNode node) {
		return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
	}

}
