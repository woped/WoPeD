package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.interfaces.ISoundnessCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class SoundnessCheckImplement implements ISoundnessCheck {

    private IEditor editor = null;
    private MarkingNet markingNetWithoutTStar = null;
    private MarkingNet markingNetWithTStar = null;

    public SoundnessCheckImplement(IEditor editor) {
        this.editor = editor;

    }

    /**
     * 
     * @return the MarkingNet basing on the LowLevelPetriNet without t* (if not existing it will be instantiated)
     */
    private MarkingNet getMarkingNetWithoutTStar() {
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
    private MarkingNet getMarkingNetWithTStar() {
        if (markingNetWithTStar == null) {
            markingNetWithTStar = BuilderFactory.createMarkingNet(BuilderFactory
                    .createLowLevelPetriNetWithTStarBuilder(editor).getLowLevelPetriNet());
        }
        return this.markingNetWithTStar;
    }

    /*
     * (non-Javadoc)
     * @see org.woped.qualanalysis.ISoundnessCheck#getNonLiveTransitionsIterator() runtime optimization. if no dead transitions found in the net and the marking
     * net is strongly connected, the petri net is live.
     */
    @Override
    public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
        Set<AbstractElementModel> nonLiveTransitions = new HashSet<AbstractElementModel>();

        if (getDeadTransitionsIterator().hasNext()
                || !AlgorithmFactory.createSccTest(getMarkingNetWithTStar()).isStronglyConnected()) {
            for (TransitionNode transition : AlgorithmFactory.createNonLiveTranstionTest(getMarkingNetWithTStar())
                    .getNonLiveTransitions()) {
                nonLiveTransitions.add(getAEM(transition));
            }
        }
        // remove t* if existing
        nonLiveTransitions.remove(null);
        return nonLiveTransitions.iterator();
    }

    /*
     * (non-Javadoc)
     * @see org.woped.qualanalysis.ISoundnessCheck#getDeadTransitionsIterator()
     */
    @Override
    public Iterator<AbstractElementModel> getDeadTransitionsIterator() {
        Set<AbstractElementModel> deadTransitions = new HashSet<AbstractElementModel>();
        for (TransitionNode transition : AlgorithmFactory.createDeadTransitionTest(getMarkingNetWithoutTStar())
                .getDeadTransitions()) {
            deadTransitions.add(getAEM(transition));
        }
        // remove t* if existing
        deadTransitions.remove(null);
        return deadTransitions.iterator();
    }

    /*
     * (non-Javadoc)
     * @see org.woped.qualanalysis.ISoundnessCheck#getUnboundedPlacesIterator()
     */
    @Override
    public Iterator<AbstractElementModel> getUnboundedPlacesIterator() {
        Set<AbstractElementModel> unboundedPlaces = new HashSet<AbstractElementModel>();
        for (PlaceNode place : AlgorithmFactory.createUnboundedPlacesTest(getMarkingNetWithTStar())
                .getUnboundedPlaces()) {
            unboundedPlaces.add(getAEM(place));
        }
        return unboundedPlaces.iterator();
    }

    /**
     * 
     * @param node the AbstractNode to get the referring AbstractElementModel from
     * @return the referred AbstractElementModel
     */
    private AbstractElementModel getAEM(AbstractNode node) {
        return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
    }

}
