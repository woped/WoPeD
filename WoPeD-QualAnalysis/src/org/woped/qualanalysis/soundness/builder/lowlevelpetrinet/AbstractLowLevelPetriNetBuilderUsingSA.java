package org.woped.qualanalysis.soundness.builder.lowlevelpetrinet;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.structure.NetAlgorithms;
import org.woped.qualanalysis.structure.StructuralAnalysis;

import java.util.Iterator;
import java.util.Set;

/**
 * creates an LowLevelPetriNet. uses logic from the {@link StructuralAnalysis}
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public abstract class AbstractLowLevelPetriNetBuilderUsingSA extends AbstractLowLevelPetriNetBuilder {

    protected StructuralAnalysis sa;
    protected Boolean isSubprocess;

    /**
     * @param editor source object
     */
    public AbstractLowLevelPetriNetBuilderUsingSA(IEditor editor) {
        super(editor);
        isSubprocess = editor.isSubprocessEditor();
        sa = new StructuralAnalysis(editor);
    }

    /**
     * Creates an lowlevelPetriNet from the provided editor using the Structural Analysis implementation. have to call from each specification!
     */
    protected void createLowLevelPetriNet() {

        lowLevelPetriNet = new LowLevelPetriNet();
        addPlaces();
        addTransitions();

        // if editor is subprocess-editor, ensure that source place contains a token
        if ( isSubprocess ) {
            verifyInitialMarking();
        }
    }

    private void addPlaces() {
        // loop over all places
        Iterator<AbstractPetriNetElementModel> iterPlace = sa.getPlaces().iterator();
        while ( iterPlace.hasNext() ) {
            PlaceModel placeModel = (PlaceModel) iterPlace.next();
            lowLevelPetriNet.getPlaceNode(new PlaceNode(placeModel.getTokenCount(), placeModel.getVirtualTokenCount(), placeModel.getId(), placeModel.getNameValue(), extractOriginId(placeModel.getId())));
        }
    }

    private void addTransitions() {
        // loops over all transitions and set predecessors and successors
        Iterator<AbstractPetriNetElementModel> iterTransition = sa.getTransitions().iterator();
        while ( iterTransition.hasNext() ) {
            OperatorTransitionModel opTrans = null;
            ModelElementContainer mec = null;
            TransitionModel transitionModel = (TransitionModel) iterTransition.next();

            Iterator<ModelElementContainer> ownerIterator = transitionModel.getOwningContainersIterator();
            AbstractPetriNetElementModel elem = null;
            while ( ownerIterator.hasNext() ) {
                mec = ownerIterator.next();
                elem = mec.getOwningElement();
                if ( elem != null && elem.getType() == TransitionModel.TRANS_OPERATOR_TYPE ) {
                    opTrans = (OperatorTransitionModel) elem;
                    break;
                }
            }

            // add current transition node
            TransitionNode transition;
            if ( opTrans != null && opTrans.getType() == TransitionModel.TRANS_OPERATOR_TYPE ) {
                transition = lowLevelPetriNet.getTransitionNode(new TransitionNode(transitionModel.getId(), transitionModel.getNameValue(), extractOriginId(transitionModel.getId()), opTrans.getOperatorType()));
            } else {
                transition = lowLevelPetriNet.getTransitionNode(new TransitionNode(transitionModel.getId(), transitionModel.getNameValue(), extractOriginId(transitionModel.getId()), OperatorTransitionModel.TRANS_SIMPLE_TYPE));
            }

            // add predecessor of current transition node
            Set<AbstractPetriNetElementModel> predecessors = NetAlgorithms.getDirectlyConnectedNodes(transitionModel, NetAlgorithms.connectionTypeINBOUND);
            for ( AbstractPetriNetElementModel predecessor : predecessors ) {
                PlaceNode place = lowLevelPetriNet.getPlaceNode(new PlaceNode(((PlaceModel) predecessor).getTokenCount(), ((PlaceModel) predecessor).getVirtualTokenCount(), predecessor.getId(), predecessor.getNameValue(), extractOriginId(predecessor.getId())));
                place.addSuccessorNode(transition, getWeight(mec, place, transition));
            }

            // add successor of current transition node
            Set<AbstractPetriNetElementModel> successors = NetAlgorithms.getDirectlyConnectedNodes(transitionModel, NetAlgorithms.connectionTypeOUTBOUND);
            for ( AbstractPetriNetElementModel successor : successors ) {
                PlaceNode place = lowLevelPetriNet.getPlaceNode(new PlaceNode(((PlaceModel) successor).getTokenCount(), ((PlaceModel) successor).getVirtualTokenCount(), successor.getId(), successor.getNameValue(), extractOriginId(successor.getId())));
                transition.addSuccessorNode(place, getWeight(mec, transition, place));
            }

        }
    }

    private void verifyInitialMarking() {
        Iterator<AbstractPetriNetElementModel> sourcePlacesIterator = sa.getSourcePlaces().iterator();
        String sourcePlaceId;
        while ( sourcePlacesIterator.hasNext() ) {
            sourcePlaceId = sourcePlacesIterator.next().getId();
            for ( PlaceNode lNetPlace : lowLevelPetriNet.getPlaces() ) {
                if ( lNetPlace.getOriginId().equals(sourcePlaceId) && lNetPlace.getTokenCount() == 0 ) {
                    lNetPlace.setTokenCount(1);
                    break;
                }
            }
        }
    }

    private Integer getWeight(ModelElementContainer mec, AbstractNode source, AbstractNode target) {

        return mec.findArc(source.getId(), target.getId()).getInscriptionValue();
    }

    /**
     * @param id the full id of the element.
     * @return the origin id of the element.
     */
    private String extractOriginId(String id) {
        String originId = id;
        Integer index;
        if ( (index = id.indexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION)) > 0 ) {
            originId = id.substring(0, index);
        }
        if ( !((index = id.indexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE)) < 0) ) {
            originId = id.substring(index + OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE.length());
        }
        return originId;
    }
}
