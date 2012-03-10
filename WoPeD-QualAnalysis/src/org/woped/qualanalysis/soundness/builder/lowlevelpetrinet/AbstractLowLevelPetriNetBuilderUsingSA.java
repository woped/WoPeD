package org.woped.qualanalysis.soundness.builder.lowlevelpetrinet;

import java.util.Iterator;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.structure.NetAlgorithms;
import org.woped.qualanalysis.structure.StructuralAnalysis;

/**
 * creates an LowLevelPetriNet. uses logic from the {@link StructuralAnalysis}
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractLowLevelPetriNetBuilderUsingSA extends AbstractLowLevelPetriNetBuilder {

    protected StructuralAnalysis sa;
    protected Boolean isSubprocess;

    /**
     * 
     * @param editor source object
     */
    public AbstractLowLevelPetriNetBuilderUsingSA(IEditor editor) {
        super(editor);
        isSubprocess = editor.isSubprocessEditor();
        sa = new StructuralAnalysis(editor);
    }

    /**
     * creates an lowlevelPetriNet from the provided editor using the Structural Analysis implementation. have to call from each specification!
     * 
     * @param editor source.
     * @return new created lowlevelPetriNet
     */
    protected void createLowLevelPetriNet() {
        Iterator<AbstractPetriNetElementModel> iterTransition = sa.getTransitions().iterator();
        Iterator<AbstractPetriNetElementModel> iterPlace = sa.getPlaces().iterator();

        LowLevelPetriNet lNet = new LowLevelPetriNet();
        Set<AbstractPetriNetElementModel> successors;
        Set<AbstractPetriNetElementModel> predecessors;
        TransitionModel tm;
        PlaceModel pm;

        // loop over all places
        while (iterPlace.hasNext()) {
            pm = (PlaceModel) iterPlace.next();
            lNet.getPlaceNode(new PlaceNode(pm.getTokenCount(), pm.getVirtualTokenCount(), pm.getId(), pm
                    .getNameValue(), makeOriginId(pm.getId())));
        }

        // loops over all transitions and set predecessors and successors
        while (iterTransition.hasNext()) {
        	AbstractPetriNetElementModel trans = iterTransition.next();
        	AbstractPetriNetElementModel opTrans = null; 
        	tm = (TransitionModel) trans;
            successors = NetAlgorithms.getDirectlyConnectedNodes(tm, NetAlgorithms.connectionTypeOUTBOUND);
            predecessors = NetAlgorithms.getDirectlyConnectedNodes(tm, NetAlgorithms.connectionTypeINBOUND);
            ModelElementContainer mec;
            AbstractPetriNetElementModel elem = null;
            Iterator<ModelElementContainer> ownerIterator = trans.getOwningContainersIterator();
            while (ownerIterator.hasNext()) {
            	mec = ownerIterator.next();
            	elem = mec.getOwningElement();
            	if (elem != null && elem.getType() == TransitionModel.TRANS_OPERATOR_TYPE) {
            		opTrans = (TransitionModel)elem;
            		break;
            	}
            }
         
            // add current transition node
            TransitionNode tNode;
            if (opTrans != null && opTrans.getType() == TransitionModel.TRANS_OPERATOR_TYPE) {
            	tNode = lNet.getTransitionNode(new TransitionNode(tm.getId(), tm.getNameValue(),
                    makeOriginId(tm.getId()), ((OperatorTransitionModel)opTrans).getOperatorType()));
            }
            else {
            	tNode = lNet.getTransitionNode(new TransitionNode(tm.getId(), tm.getNameValue(),
                    makeOriginId(tm.getId()), OperatorTransitionModel.TRANS_SIMPLE_TYPE));
            }

            // add predecessor of current transition node
            for (AbstractPetriNetElementModel predecessor : predecessors) {
                lNet.getPlaceNode(
                        new PlaceNode(((PlaceModel) predecessor).getTokenCount(), ((PlaceModel) predecessor)
                                .getVirtualTokenCount(), predecessor.getId(), predecessor.getNameValue(),
                                makeOriginId(predecessor.getId()))).addPostNode(tNode);
            }

            // add successor of current transition node
            for (AbstractPetriNetElementModel successor : successors) {
                tNode.addPostNode(lNet.getPlaceNode(new PlaceNode(((PlaceModel) successor).getTokenCount(),
                        ((PlaceModel) successor).getVirtualTokenCount(), successor.getId(), successor.getNameValue(),
                        makeOriginId(successor.getId()))));
            }

        }
        // if editor is subprocess-editor, add a token to source-places
        if (isSubprocess) {
            Iterator<AbstractPetriNetElementModel> sourcePlacesIterator = sa.getSourcePlaces().iterator();
            String sourcePlaceId;
            while (sourcePlacesIterator.hasNext()) {
                sourcePlaceId = sourcePlacesIterator.next().getId();
                for (PlaceNode lNetPlace : lNet.getPlaces()) {
                    if (lNetPlace.getOriginId().equals(sourcePlaceId) && lNetPlace.getTokenCount() == 0) {
                        lNetPlace.setTokenCount(1);
                        break;
                    }
                }
            }
        }
        lowLevelPetriNet = lNet;
    }

    /**
     * 
     * @param full id.
     * @return origin id.
     */
    private String makeOriginId(String id) {
        String originId = id;
        Integer index;
        if ((index = id.indexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION)) > 0) {
            originId = id.substring(0, index);
        }
        if (!((index = id.indexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE)) < 0)) {
            originId = id.substring(index + OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE.length());
        }
        return originId;
    }

}
