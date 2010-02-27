package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;

public abstract class AbstractQualanalysisService implements IQualanalysisService {

    protected IEditor editor;
    private StructuralAnalysis sA;
    protected ISComponent sComponent;
    protected ISoundnessCheck soundnessCheck;

    private int numPlaces;
    private Set<AbstractElementModel> places;
    private int numTransitions;
    private Set<AbstractElementModel> transitions;
    private int numOperators;
    private Set<AbstractElementModel> operators;
    private int numSubprocesses;
    private Set<AbstractElementModel> subprocesses;
    private int numArcs;
    private int numFreeChoiceViolations;
    private Set<Set<AbstractElementModel>> freeChoiceViolations;
    private int numMisusedOperators;
    private Set<AbstractElementModel> misusedOperators;
    private int numNotConnectedNodes;
    private Set<AbstractElementModel> notConnectedNodes;
    private int numNotStronglyConnectedNodes;
    private Set<AbstractElementModel> notStronglyConnectedNodes;
    private int numSourcePlaces;
    private Set<AbstractElementModel> sourcePlaces;
    private int numSinkPlaces;
    private Set<AbstractElementModel> sinkPlaces;
    private int numSourceTransitions;
    private Set<AbstractElementModel> sourceTransitions;
    private int numSinkTransitions;
    private Set<AbstractElementModel> sinkTransitions;
    private int numPTHandles;
    private Set<Set<AbstractElementModel>> pTHandles;
    private int numTPHandles;
    private Set<Set<AbstractElementModel>> tPHandles;
    private int numSComponents;
    private Set<List<AbstractElementModel>> sComponents;
    private int numNotSCovered;
    private Set<AbstractElementModel> notSCovered;
    private int numUnboundedPlaces;
    private Set<AbstractElementModel> unboundedPlaces;
    private int numDeadTransitions;
    private Set<AbstractElementModel> deadTransitions;
    private int numNonLiveTransitions;
    private Set<AbstractElementModel> nonLiveTransitions;

    public AbstractQualanalysisService(IEditor editor) {
        this.editor = editor;
        sA = new StructuralAnalysis(editor);
    }

    @SuppressWarnings("unchecked")
    protected void init() {
        numPlaces = sA.getNumPlaces();
        places = getSet(sA.getPlacesIterator());
        numTransitions = sA.getNumTransitions();
        transitions = getSet(sA.getTransitionsIterator());
        numOperators = sA.getNumOperators();
        operators = getSet(sA.getOperatorsIterator());
        numSubprocesses = sA.getNumSubprocesses();
        subprocesses = getSet(sA.getSubprocessesIterator());
        numArcs = sA.getNumArcs();
        numFreeChoiceViolations = sA.getNumFreeChoiceViolations();
        freeChoiceViolations = getSet(sA.getFreeChoiceViolations());
        numMisusedOperators = sA.getNumMisusedOperators();
        misusedOperators = getSet(sA.getMisusedOperatorsIterator());
        numNotConnectedNodes = sA.getNumNotConnectedNodes();
        notConnectedNodes = getSet(sA.getNotConnectedNodes());
        numNotStronglyConnectedNodes = sA.getNumNotStronglyConnectedNodes();
        notStronglyConnectedNodes = getSet(sA.getNotStronglyConnectedNodes());
        numSourcePlaces = sA.getNumSourcePlaces();
        sourcePlaces = getSet(sA.getSourcePlacesIterator());
        numSinkPlaces = sA.getNumSinkPlaces();
        sinkPlaces = getSet(sA.getSinkPlacesIterator());
        numSourceTransitions = sA.getNumSourceTransitions();
        sourceTransitions = getSet(sA.getSourceTransitionsIterator());
        numSinkTransitions = sA.getNumSinkTransitions();
        sinkTransitions = getSet(sA.getSinkTransitionsIterator());
        numPTHandles = sA.getNumPTHandles();
        pTHandles = getSet(sA.getPTHandlesIterator());
        numTPHandles = sA.getNumTPHandles();
        tPHandles = getSet(sA.getTPHandlesIterator());
        // SComponents
        numSComponents = sComponent.getNumSComponents();
        sComponents = getSet(sComponent.getSComponentsIterator());
        numNotSCovered = sComponent.getNumNotSCovered();
        notSCovered = getSet(sComponent.getNotSCoveredIterator());
        // Soundness
        numUnboundedPlaces = soundnessCheck.getNumUnboundedPlaces();
        unboundedPlaces = getSet(soundnessCheck.getUnboundedPlacesIterator());
        numDeadTransitions = soundnessCheck.getNumDeadTransitions();
        deadTransitions = getSet(soundnessCheck.getDeadTransitionsIterator());
        numNonLiveTransitions = soundnessCheck.getNumNonLiveTransitions();
        nonLiveTransitions = getSet(soundnessCheck.getNonLiveTransitionsIterator());
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public Iterator<AbstractElementModel> getPlacesIterator() {
        return places.iterator();
    }

    public int getNumTransitions() {
        return numTransitions;
    }

    public Iterator<AbstractElementModel> getTransitionsIterator() {
        return transitions.iterator();
    }

    public int getNumOperators() {
        return numOperators;
    }

    public Iterator<AbstractElementModel> getOperatorsIterator() {
        return operators.iterator();
    }

    public int getNumSubprocesses() {
        return numSubprocesses;
    }

    public Iterator<AbstractElementModel> getSubprocessesIterator() {
        return subprocesses.iterator();
    }

    public int getNumArcs() {
        return numArcs;
    }

    public int getNumFreeChoiceViolations() {
        return numFreeChoiceViolations;
    }

    public Iterator<Set<AbstractElementModel>> getFreeChoiceViolationsIterator() {
        return freeChoiceViolations.iterator();
    }

    public int getNumMisusedOperators() {
        return numMisusedOperators;
    }

    public Iterator<AbstractElementModel> getMisusedOperatorsIterator() {
        return misusedOperators.iterator();
    }

    public int getNumNotConnectedNodes() {
        return numNotConnectedNodes;
    }

    public Iterator<AbstractElementModel> getNotConnectedNodesIterator() {
        return notConnectedNodes.iterator();
    }

    public int getNumNotStronglyConnectedNodes() {
        return numNotStronglyConnectedNodes;
    }

    public Iterator<AbstractElementModel> getNotStronglyConnectedNodesIterator() {
        return notStronglyConnectedNodes.iterator();
    }

    public int getNumSourcePlaces() {
        return numSourcePlaces;
    }

    public Iterator<AbstractElementModel> getSourcePlacesIterator() {
        return sourcePlaces.iterator();
    }

    public int getNumSinkPlaces() {
        return numSinkPlaces;
    }

    public Iterator<AbstractElementModel> getSinkPlacesIterator() {
        return sinkPlaces.iterator();
    }

    public int getNumSourceTransitions() {
        return numSourceTransitions;
    }

    public Iterator<AbstractElementModel> getSourceTransitionsIterator() {
        return sourceTransitions.iterator();
    }

    public int getNumSinkTransitions() {
        return numSinkTransitions;
    }

    public Iterator<AbstractElementModel> getSinkTransitionsIterator() {
        return sinkTransitions.iterator();
    }

    public int getNumPTHandles() {
        return numPTHandles;
    }

    public Iterator<Set<AbstractElementModel>> getPTHandlesIterator() {
        return pTHandles.iterator();
    }

    public int getNumTPHandles() {
        return numTPHandles;
    }

    public Iterator<Set<AbstractElementModel>> getTPHandlesIterator() {
        return tPHandles.iterator();
    }

    public int getNumSComponents() {
        return numSComponents;
    }

    public Iterator<List<AbstractElementModel>> getSComponentsIterator() {
        return sComponents.iterator();
    }

    public int getNumNotSCovered() {
        return numNotSCovered;
    }

    public Iterator<AbstractElementModel> getNotSCoveredIterator() {
        return notSCovered.iterator();
    }

    public int getNumUnboundedPlaces() {
        return numUnboundedPlaces;
    }

    public Iterator<AbstractElementModel> getUnboundedPlacesIterator() {
        return unboundedPlaces.iterator();
    }

    public int getNumDeadTransitions() {
        return numDeadTransitions;
    }

    public Iterator<AbstractElementModel> getDeadTransitionsIterator() {
        return deadTransitions.iterator();
    }

    public int getNumNonLiveTransitions() {
        return numNonLiveTransitions;
    }

    public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
        return nonLiveTransitions.iterator();
    }

    /**
     * returns true if the net is a workflow net. a workflow net has only one source place, one sink place, no dangling transition and all nodes are conntected.
     */
    public boolean isWorkflowNet() {
        boolean isWorkflowNet = true;

        if (numSourcePlaces != 1) {
            isWorkflowNet = false;
        }
        if (numSinkPlaces != 1) {
            isWorkflowNet = false;
        }
        if (numSourceTransitions != 0) {
            isWorkflowNet = false;
        }
        if (numSinkTransitions != 0) {
            isWorkflowNet = false;
        }
        if (numNotConnectedNodes != 0) {
            isWorkflowNet = false;
        }
        if (numNotStronglyConnectedNodes != 0) {
            isWorkflowNet = false;
        }
        return isWorkflowNet;
    }

    public boolean isOnlyOneTokenInSource() {
        return false;
    }

    @Override
    public boolean isSound() {

        // only workflow nets are sound.
        if (!isWorkflowNet()) {
            return false;
        }

        // only one token is allowed in the source place and nowhere else.
        if (getTokenInfo() != TOKEN_SOURCE) {
            return false;
        }

        // dead transitions aren't allowed.
        if (getNumDeadTransitions() == 0) {
            return false;
        }

        // unlive transitions aren't allowed,
        if (getNumNonLiveTransitions() == 0) {
            return false;
        }

        return true;
    }

    @Override
    public int getTokenInfo() {
        // check if workflow-net
        if (!isWorkflowNet()) {
            return TOKEN_WF;
        }
        // check if any other place than the sourcePlace has a token
        for (AbstractElementModel place : places) {
            if (((PlaceModel) place).getTokenCount() != 0 && place != getSourcePlacesIterator().next()) {
                return TOKEN_OTHER;
            }
        }
        // check if sourcePlace has token
        if (((PlaceModel) getSourcePlacesIterator().next()).getTokenCount() == 1) {
            return TOKEN_SOURCE;
        } else {
            return TOKEN_NONE;
        }
    }

    /**
     * 
     * @param iter iterator to create the set from
     * @return the set created from the given iterator
     */
    @SuppressWarnings("unchecked")
    protected Set getSet(Iterator iter) {
        Set set = new HashSet();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }
}
