package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.ISoundnessCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
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
     * @see org.woped.qualanalysis.ISoundnessCheck#getNonLiveTransitionsIterator() runtime optimization. if no dead transitions found in the net and the marking
     * net is strongly connected, the petri net is live.
     */
    @Override
    public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
        Set<AbstractElementModel> nonLiveTransitions = new HashSet<AbstractElementModel>();

        if (getDeadTransitionsIterator().hasNext()
                || !AlgorithmFactory.createStronglyConnectedComponentTest(markingNetWithTStar).isStronglyConnected()) {
            for (TransitionNode transition : AlgorithmFactory.createNonLiveTranstionTest(markingNetWithTStar)
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
        for (TransitionNode transition : AlgorithmFactory.createDeadTransitionTest(markingNetWithoutTStar)
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
        for (PlaceNode place : AlgorithmFactory.createUnboundedPlacesTest(markingNetWithTStar).getUnboundedPlaces()) {
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
