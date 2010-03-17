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
import org.woped.qualanalysis.service.interfaces.IWellStructuredness;
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
    private IWorkflowCheck workflowCheck = null;
    private INetStatistics netStatistics = null;
    private IWellStructuredness wellStructuredness = null;

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
        wellStructuredness = sA;
    }

    public Set<AbstractElementModel> getPlaces() {
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        return places;
    }

    public Set<AbstractElementModel> getTransitions() {
        if (transitions == null) {
            transitions = netStatistics.getTransitions();
        }
        return transitions;
    }

    public Set<AbstractElementModel> getOperators() {
        if (operators == null) {
            operators = netStatistics.getOperators();
        }
        return operators;
    }

    public Set<AbstractElementModel> getSubprocesses() {
        if (subprocesses == null) {
            subprocesses = netStatistics.getSubprocesses();
        }
        return subprocesses;
    }

    public int getNumArcs() {
        if (numArcs == 0) {
            numArcs = netStatistics.getNumArcs();
        }
        return numArcs;
    }

    public Set<Set<AbstractElementModel>> getFreeChoiceViolations() {
        if (freeChoiceViolations == null) {
            freeChoiceViolations = sA.getFreeChoiceViolations();
        }
        return freeChoiceViolations;
    }

    public Set<AbstractElementModel> getWronglyUsedOperators() {
        if (wronglyUsedOperators == null) {
            wronglyUsedOperators = sA.getMisusedOperators();
        }
        return wronglyUsedOperators;
    }

    public Set<AbstractElementModel> getSourcePlaces() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        return sourcePlaces;
    }

    public Set<AbstractElementModel> getSinkPlaces() {
        if (sinkPlaces == null) {
            sinkPlaces = workflowCheck.getSinkPlaces();
        }
        return sinkPlaces;
    }

    public Set<AbstractElementModel> getSourceTransitions() {
        if (sourceTransitions == null) {
            sourceTransitions = workflowCheck.getSourceTransitions();
        }
        return sourceTransitions;
    }

    public Set<AbstractElementModel> getSinkTransitions() {
        if (sinkTransitions == null) {
            sinkTransitions = workflowCheck.getSinkTransitions();
        }
        return sinkTransitions;
    }

    public Set<AbstractElementModel> getNotConnectedNodes() {
        if (notConnectedNodes == null) {
            notConnectedNodes = workflowCheck.getNotConnectedNodes();
        }
        return notConnectedNodes;
    }

    public Set<Set<AbstractElementModel>> getConnectedComponents() {
        if (connectedComponents == null) {
            connectedComponents = workflowCheck.getConnectedComponents();
        }
        return connectedComponents;
    }

    public Set<AbstractElementModel> getNotStronglyConnectedNodes() {
        if (notStronglyConnectedNodes == null) {
            notStronglyConnectedNodes = workflowCheck.getNotStronglyConnectedNodes();
        }
        return notStronglyConnectedNodes;
    }

    public Set<Set<AbstractElementModel>> getStronglyConnectedComponents() {
        if (stronglyConnectedComponents == null) {
            stronglyConnectedComponents = workflowCheck.getStronglyConnectedComponents();
        }
        return stronglyConnectedComponents;
    }

    public Set<Set<AbstractElementModel>> getPTHandles() {
        if (pTHandles == null) {
            pTHandles = wellStructuredness.getPTHandles();
        }
        return pTHandles;
    }

    public Set<Set<AbstractElementModel>> getTPHandles() {
        if (tPHandles == null) {
            tPHandles = wellStructuredness.getTPHandles();
        }
        return tPHandles;
    }

    public HashSet<Set<ClusterElement>> getM_handleClusters() {
        if (m_handleClusters == null) {
            m_handleClusters = wellStructuredness.getM_handleClusters();
        }
        return m_handleClusters;
    }

    public Set<List<AbstractElementModel>> getSComponents() {
        if (sComponents == null) {
            sComponents = sComponent.getSComponents();
        }
        return sComponents;
    }

    public Set<AbstractElementModel> getNotSCovered() {
        if (notSCovered == null) {
            notSCovered = sComponent.getNotSCovered();
        }
        return notSCovered;
    }

    public Set<AbstractElementModel> getWronglyMarkedPlaces() {
        if (wronglyMarkedPlaces == null) {
            wronglyMarkedPlaces = calcWronglyMarkedPlaces();
        }
        return wronglyMarkedPlaces;
    }

    public Set<AbstractElementModel> getUnboundedPlaces() {
        if (unboundedPlaces == null) {
            unboundedPlaces = soundnessCheck.getUnboundedPlaces();
        }
        return unboundedPlaces;
    }

    public Set<AbstractElementModel> getDeadTransitions() {
        if (deadTransitions == null) {
            deadTransitions = soundnessCheck.getDeadTransitions();
        }
        return deadTransitions;
    }

    public Set<AbstractElementModel> getNonLiveTransitions() {
        if (nonLiveTransitions == null) {
            nonLiveTransitions = soundnessCheck.getNonLiveTransitions();
        }
        return nonLiveTransitions;
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
        if (getWronglyMarkedPlaces().size() != 0) {
            return false;
        }
        if (getUnboundedPlaces().size() != 0) {
            return false;
        }
        if (getDeadTransitions().size() != 0) {
            return false;
        }
        if (getNonLiveTransitions().size() != 0) {
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
