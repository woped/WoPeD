package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.qualanalysis.service.interfaces.INetStatistics;
import org.woped.qualanalysis.service.interfaces.ISComponent;
import org.woped.qualanalysis.service.interfaces.ISoundnessCheck;
import org.woped.qualanalysis.service.interfaces.IWorkflowCheck;
import org.woped.qualanalysis.soundness.WorkflowCheckImplement;
import org.woped.qualanalysis.structure.StructuralAnalysis;
import org.woped.qualanalysis.structure.components.ClusterElement;

/**
 * abstract class for qualanalysis services contains all needed variables and getters
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractQualanalysisService implements IQualanalysisService {

    protected IEditor editor = null;
    private StructuralAnalysis sA = null;
    protected ISComponent sComponent = null;
    protected ISoundnessCheck soundnessCheck = null;
    protected IWorkflowCheck workflowCheck = null;
    private INetStatistics netStatistics = null;

    private Set<AbstractElementModel> places = null;
    private Set<AbstractElementModel> transitions = null;
    private Set<AbstractElementModel> operators = null;
    private Set<AbstractElementModel> subprocesses = null;
    private int numArcs = 0;
    private Set<Set<AbstractElementModel>> freeChoiceViolations = null;
    private Set<AbstractElementModel> wronglyUsedOperators = null;

    private Set<AbstractElementModel> notConnectedNodes = null;
    private Set<AbstractElementModel> notStronglyConnectedNodes = null;
    private Set<Set<AbstractElementModel>> stronglyConnectedComponents = null;
    private Set<Set<AbstractElementModel>> connectedComponents = null;
    private Set<AbstractElementModel> sourcePlaces = null;
    private Set<AbstractElementModel> sinkPlaces = null;
    private Set<AbstractElementModel> sourceTransitions = null;
    private Set<AbstractElementModel> sinkTransitions = null;
    private Set<Set<AbstractElementModel>> pTHandles = null;
    private Set<Set<AbstractElementModel>> tPHandles = null;
    private HashSet<Set<ClusterElement>> m_handleClusters = null;
    private Set<List<AbstractElementModel>> sComponents = null;
    private Set<AbstractElementModel> notSCovered = null;
    private Set<AbstractElementModel> wronglyMarkedPlaces = null;
    private Set<AbstractElementModel> unboundedPlaces = null;
    private Set<AbstractElementModel> deadTransitions = null;
    private Set<AbstractElementModel> nonLiveTransitions = null;

    public AbstractQualanalysisService(IEditor editor) {
        this.editor = editor;
        sA = new StructuralAnalysis(editor);
        workflowCheck = new WorkflowCheckImplement(editor);
        netStatistics = sA;
    }

    public int getNumPlaces() {
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        return places.size();
    }

    public Iterator<AbstractElementModel> getPlacesIterator() {
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        return places.iterator();
    }

    public int getNumTransitions() {
        if (transitions == null) {
            transitions = netStatistics.getTransitions();
        }
        return transitions.size();
    }

    public Iterator<AbstractElementModel> getTransitionsIterator() {
        if (transitions == null) {
            transitions = netStatistics.getTransitions();
        }
        return transitions.iterator();
    }

    public int getNumOperators() {
        if (operators == null) {
            operators = netStatistics.getOperators();
        }
        return operators.size();
    }

    public Iterator<AbstractElementModel> getOperatorsIterator() {
        if (operators == null) {
            operators = netStatistics.getOperators();
        }
        return operators.iterator();
    }

    public int getNumSubprocesses() {
        if (subprocesses == null) {
            subprocesses = netStatistics.getSubprocesses();
        }
        return subprocesses.size();
    }

    public Iterator<AbstractElementModel> getSubprocessesIterator() {
        if (subprocesses == null) {
            subprocesses = netStatistics.getSubprocesses();
        }
        return subprocesses.iterator();
    }

    public int getNumArcs() {
        if (numArcs == 0) {
            numArcs = netStatistics.getNumArcs();
        }
        return numArcs;
    }

    public int getNumFreeChoiceViolations() {
        if (freeChoiceViolations == null) {
            freeChoiceViolations = sA.getFreeChoiceViolations();
        }
        return freeChoiceViolations.size();
    }

    public Iterator<Set<AbstractElementModel>> getFreeChoiceViolationsIterator() {
        if (freeChoiceViolations == null) {
            freeChoiceViolations = sA.getFreeChoiceViolations();
        }
        return freeChoiceViolations.iterator();
    }

    public int getNumWronglyUsedOperators() {
        if (wronglyUsedOperators == null) {
            wronglyUsedOperators = sA.getMisusedOperators();
        }
        return wronglyUsedOperators.size();
    }

    public Iterator<AbstractElementModel> getWronglyUsedOperatorsIterator() {
        if (wronglyUsedOperators == null) {
            wronglyUsedOperators = sA.getMisusedOperators();
        }
        return wronglyUsedOperators.iterator();
    }

    public int getNumSourcePlaces() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        return sourcePlaces.size();
    }

    public Iterator<AbstractElementModel> getSourcePlacesIterator() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        return sourcePlaces.iterator();
    }

    public int getNumSinkPlaces() {
        if (sinkPlaces == null) {
            sinkPlaces = workflowCheck.getSinkPlaces();
        }
        return sinkPlaces.size();
    }

    public Iterator<AbstractElementModel> getSinkPlacesIterator() {
        if (sinkPlaces == null) {
            sinkPlaces = workflowCheck.getSinkPlaces();
        }
        return sinkPlaces.iterator();
    }

    public int getNumSourceTransitions() {
        if (sourceTransitions == null) {
            sourceTransitions = workflowCheck.getSourceTransitions();
        }
        return sourceTransitions.size();
    }

    public Iterator<AbstractElementModel> getSourceTransitionsIterator() {
        if (sourceTransitions == null) {
            sourceTransitions = workflowCheck.getSourceTransitions();
        }
        return sourceTransitions.iterator();
    }

    public int getNumSinkTransitions() {
        if (sinkTransitions == null) {
            sinkTransitions = workflowCheck.getSinkTransitions();
        }
        return sinkTransitions.size();
    }

    public Iterator<AbstractElementModel> getSinkTransitionsIterator() {
        if (sinkTransitions == null) {
            sinkTransitions = workflowCheck.getSinkTransitions();
        }
        return sinkTransitions.iterator();
    }

    public int getNumNotConnectedNodes() {
        if (notConnectedNodes == null) {
            notConnectedNodes = workflowCheck.getNotConnectedNodes();
        }
        return notConnectedNodes.size();
    }

    public Iterator<AbstractElementModel> getNotConnectedNodesIterator() {
        if (notConnectedNodes == null) {
            notConnectedNodes = workflowCheck.getNotConnectedNodes();
        }
        return notConnectedNodes.iterator();
    }

    public int getNumNotStronglyConnectedNodes() {
        if (notStronglyConnectedNodes == null) {
            notStronglyConnectedNodes = workflowCheck.getNotStronglyConnectedNodes();
        }
        return notStronglyConnectedNodes.size();
    }

    @Override
    public Iterator<Set<AbstractElementModel>> getConnectedComponentsIterator() {
        if (connectedComponents == null) {
            connectedComponents = workflowCheck.getConnectedComponents();
        }
        return connectedComponents.iterator();
    }

    @Override
    public int getNumConnectedComponents() {
        if (connectedComponents == null) {
            connectedComponents = workflowCheck.getConnectedComponents();
        }
        return connectedComponents.size();
    }

    public Iterator<AbstractElementModel> getNotStronglyConnectedNodesIterator() {
        if (notStronglyConnectedNodes == null) {
            notStronglyConnectedNodes = workflowCheck.getNotStronglyConnectedNodes();
        }
        return notStronglyConnectedNodes.iterator();
    }

    @Override
    public int getStronglyConnectedComponentNum() {
        if (stronglyConnectedComponents == null) {
            stronglyConnectedComponents = workflowCheck.getStronglyConnectedComponents();
        }
        return stronglyConnectedComponents.size();
    }

    @Override
    public Iterator<Set<AbstractElementModel>> getStronglyConnectedComponentIterator() {
        if (stronglyConnectedComponents == null) {
            stronglyConnectedComponents = workflowCheck.getStronglyConnectedComponents();
        }
        return stronglyConnectedComponents.iterator();
    }

    public int getNumPTHandles() {
        if (pTHandles == null) {
            pTHandles = sA.getPTHandles();
        }
        return pTHandles.size();
    }

    public Iterator<Set<AbstractElementModel>> getPTHandlesIterator() {
        if (pTHandles == null) {
            pTHandles = sA.getPTHandles();
        }
        return pTHandles.iterator();
    }

    public int getNumTPHandles() {
        if (tPHandles == null) {
            tPHandles = sA.getTPHandles();
        }
        return tPHandles.size();
    }

    public Iterator<Set<AbstractElementModel>> getTPHandlesIterator() {
        if (tPHandles == null) {
            tPHandles = sA.getTPHandles();
        }
        return tPHandles.iterator();
    }

    public HashSet<Set<ClusterElement>> getM_handleClusters() {
        if (m_handleClusters == null) {
            m_handleClusters = sA.getM_handleClusters();
        }
        return m_handleClusters;
    }

    public int getNumSComponents() {
        if (sComponents == null) {
            sComponents = sComponent.getSComponents();
        }
        return sComponents.size();
    }

    public Iterator<List<AbstractElementModel>> getSComponentsIterator() {
        if (sComponents == null) {
            sComponents = sComponent.getSComponents();
        }
        return sComponents.iterator();
    }

    public int getNumNotSCovered() {
        if (notSCovered == null) {
            notSCovered = sComponent.getNotSCovered();
        }
        return notSCovered.size();
    }

    public Iterator<AbstractElementModel> getNotSCoveredIterator() {
        if (notSCovered == null) {
            notSCovered = sComponent.getNotSCovered();
        }
        return notSCovered.iterator();
    }

    public int getNumWronglyMarkedPlaces() {
        if (wronglyMarkedPlaces == null) {
            wronglyMarkedPlaces = calcWronglyMarkedPlaces();
        }
        return wronglyMarkedPlaces.size();
    }

    public Iterator<AbstractElementModel> getWronglyMarkedPlacesIterator() {
        if (wronglyMarkedPlaces == null) {
            wronglyMarkedPlaces = calcWronglyMarkedPlaces();
        }
        return wronglyMarkedPlaces.iterator();
    }

    public int getNumUnboundedPlaces() {
        if (unboundedPlaces == null) {
            unboundedPlaces = soundnessCheck.getUnboundedPlaces();
        }
        return unboundedPlaces.size();
    }

    public Iterator<AbstractElementModel> getUnboundedPlacesIterator() {
        if (unboundedPlaces == null) {
            unboundedPlaces = soundnessCheck.getUnboundedPlaces();
        }
        return unboundedPlaces.iterator();
    }

    public int getNumDeadTransitions() {
        if (deadTransitions == null) {
            deadTransitions = soundnessCheck.getDeadTransitions();
        }
        return deadTransitions.size();
    }

    public Iterator<AbstractElementModel> getDeadTransitionsIterator() {
        if (deadTransitions == null) {
            deadTransitions = soundnessCheck.getDeadTransitions();
        }
        return deadTransitions.iterator();
    }

    public int getNumNonLiveTransitions() {
        if (nonLiveTransitions == null) {
            nonLiveTransitions = soundnessCheck.getNonLiveTransitions();
        }
        return nonLiveTransitions.size();
    }

    public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
        if (nonLiveTransitions == null) {
            nonLiveTransitions = soundnessCheck.getNonLiveTransitions();
        }
        return nonLiveTransitions.iterator();
    }

    /**
     * @see IQualanalysisService#isWorkflowNet()
     */
    @Override
    public boolean isWorkflowNet() {
        return workflowCheck.isWorkflowNet();
    }

    /**
     * @see IQualanalysisService#isSound()
     */
    @Override
    public boolean isSound() {
        if (!isWorkflowNet()) {
            return false;
        }
        if (getNumWronglyMarkedPlaces() != 0) {
            return false;
        }
        if (getNumUnboundedPlaces() != 0) {
            return false;
        }
        if (getNumDeadTransitions() != 0) {
            return false;
        }
        if (getNumNonLiveTransitions() != 0) {
            return false;
        }
        return true;
    }

    /**
     * method to find places with wrong token count in initial marking
     * 
     * @return a set of AbstractElementModels (= places) which have wrong token count
     */
    private Set<AbstractElementModel> calcWronglyMarkedPlaces() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        Set<AbstractElementModel> wronglyMarkedPlaces = new HashSet<AbstractElementModel>();
        Set<AbstractElementModel> placesNotSource = new HashSet<AbstractElementModel>(this.places);
        placesNotSource.removeAll(this.sourcePlaces);
        // check if source place has one token
        for (AbstractElementModel place : this.sourcePlaces) {
            if (((PlaceModel) place).getTokenCount() != 1) {
                wronglyMarkedPlaces.add(place);
            }
        }
        // check if any other place has a token
        for (AbstractElementModel place : placesNotSource) {
            if (((PlaceModel) place).getTokenCount() != 0) {
                wronglyMarkedPlaces.add(place);
            }
        }
        return wronglyMarkedPlaces;
    }

    /**
     * 
     * @param <T> generic type.
     * @param iter iterator to create the set from
     * @return the set created from the given iterator
     */
    protected <T> Set<T> getSet(Iterator<T> iter) {
        Set<T> set = new HashSet<T>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }
}
