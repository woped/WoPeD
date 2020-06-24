package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
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

    private Set<AbstractPetriNetElementModel> places = null;
    private Set<AbstractPetriNetElementModel> transitions = null;
    private Set<AbstractPetriNetElementModel> operators = null;
    private Set<AbstractPetriNetElementModel> subprocesses = null;
    private int numArcs = 0;
    private Set<Set<AbstractPetriNetElementModel>> freeChoiceViolations = null;
    private Set<AbstractPetriNetElementModel> wronglyUsedOperators = null;

    private Set<AbstractPetriNetElementModel> notConnectedNodes = null;
    private Set<AbstractPetriNetElementModel> notStronglyConnectedNodes = null;
    private Set<Set<AbstractPetriNetElementModel>> stronglyConnectedComponents = null;
    private Set<Set<AbstractPetriNetElementModel>> connectedComponents = null;
    private Set<AbstractPetriNetElementModel> sourcePlaces = null;
    private Set<AbstractPetriNetElementModel> sinkPlaces = null;
    private Set<AbstractPetriNetElementModel> sourceTransitions = null;
    private Set<AbstractPetriNetElementModel> sinkTransitions = null;
    private Set<Set<AbstractPetriNetElementModel>> pTHandles = null;
    private Set<Set<AbstractPetriNetElementModel>> tPHandles = null;
    private HashSet<Set<ClusterElement>> m_handleClusters = null;
    private Set<List<AbstractPetriNetElementModel>> sComponents = null;
    private Set<AbstractPetriNetElementModel> notSCovered = null;
    private Set<AbstractPetriNetElementModel> wronglyMarkedPlaces = null;
    private Set<AbstractPetriNetElementModel> unboundedPlaces = null;
    private Set<AbstractPetriNetElementModel> deadTransitions = null;
    private Set<AbstractPetriNetElementModel> nonLiveTransitions = null;
    private Set<ArcModel> arcWeightViolations;


    public AbstractQualanalysisService(IEditor editor) {
        this.editor = editor;
        sA = new StructuralAnalysis(editor);
        workflowCheck = new WorkflowCheckImplement(editor);
        netStatistics = sA;
        wellStructuredness = sA;
    }

    public Set<AbstractPetriNetElementModel> getPlaces() {
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        return places;
    }

    public Set<AbstractPetriNetElementModel> getTransitions() {
        if (transitions == null) {
            transitions = netStatistics.getTransitions();
        }
        return transitions;
    }

    public Set<AbstractPetriNetElementModel> getOperators() {
        if (operators == null) {
            operators = netStatistics.getOperators();
        }
        return operators;
    }

    public Set<AbstractPetriNetElementModel> getSubprocesses() {
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

    public Set<Set<AbstractPetriNetElementModel>> getFreeChoiceViolations() {
        if (freeChoiceViolations == null) {
            freeChoiceViolations = sA.getFreeChoiceViolations();
        }
        return freeChoiceViolations;
    }

    public Set<AbstractPetriNetElementModel> getWronglyUsedOperators() {
        if (wronglyUsedOperators == null) {
            wronglyUsedOperators = sA.getMisusedOperators();
        }
        return wronglyUsedOperators;
    }

    public Set<AbstractPetriNetElementModel> getSourcePlaces() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        return sourcePlaces;
    }

    public Set<AbstractPetriNetElementModel> getSinkPlaces() {
        if (sinkPlaces == null) {
            sinkPlaces = workflowCheck.getSinkPlaces();
        }
        return sinkPlaces;
    }

    public Set<AbstractPetriNetElementModel> getSourceTransitions() {
        if (sourceTransitions == null) {
            sourceTransitions = workflowCheck.getSourceTransitions();
        }
        return sourceTransitions;
    }

    public Set<AbstractPetriNetElementModel> getSinkTransitions() {
        if (sinkTransitions == null) {
            sinkTransitions = workflowCheck.getSinkTransitions();
        }
        return sinkTransitions;
    }

    public Set<AbstractPetriNetElementModel> getNotConnectedNodes() {
        if (notConnectedNodes == null) {
            notConnectedNodes = workflowCheck.getNotConnectedNodes();
        }
        return notConnectedNodes;
    }

    public Set<Set<AbstractPetriNetElementModel>> getConnectedComponents() {
        if (connectedComponents == null) {
            connectedComponents = workflowCheck.getConnectedComponents();
        }
        return connectedComponents;
    }

    public Set<AbstractPetriNetElementModel> getNotStronglyConnectedNodes() {
        if (notStronglyConnectedNodes == null) {
            notStronglyConnectedNodes = workflowCheck.getNotStronglyConnectedNodes();
        }
        return notStronglyConnectedNodes;
    }

    @Override
    public Set<ArcModel> getArcWeightViolations() {
        if(arcWeightViolations == null){
            arcWeightViolations = workflowCheck.getArcWeightViolations();
        }

        return arcWeightViolations;
    }

    public Set<Set<AbstractPetriNetElementModel>> getStronglyConnectedComponents() {
        if (stronglyConnectedComponents == null) {
            stronglyConnectedComponents = workflowCheck.getStronglyConnectedComponents();
        }
        return stronglyConnectedComponents;
    }

    public Set<Set<AbstractPetriNetElementModel>> getPTHandles() {
        if (pTHandles == null) {
            pTHandles = wellStructuredness.getPTHandles();
        }
        return pTHandles;
    }

    public Set<Set<AbstractPetriNetElementModel>> getTPHandles() {
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

    public Set<List<AbstractPetriNetElementModel>> getSComponents() {
        if (sComponents == null) {
            sComponents = sComponent.getSComponents();
        }
        return sComponents;
    }

    public Set<AbstractPetriNetElementModel> getNotSCovered() {
        if (notSCovered == null) {
            notSCovered = sComponent.getNotSCovered();
        }
        return notSCovered;
    }

    public Set<AbstractPetriNetElementModel> getWronglyMarkedPlaces() {
        if (wronglyMarkedPlaces == null) {
            wronglyMarkedPlaces = calcWronglyMarkedPlaces();
        }
        return wronglyMarkedPlaces;
    }

    public Set<AbstractPetriNetElementModel> getUnboundedPlaces() {
        if (unboundedPlaces == null) {
            unboundedPlaces = soundnessCheck.getUnboundedPlaces();
        }
        return unboundedPlaces;
    }

    public Set<AbstractPetriNetElementModel> getDeadTransitions() {
        if (deadTransitions == null) {
            deadTransitions = soundnessCheck.getDeadTransitions();
        }
        return deadTransitions;
    }

    public Set<AbstractPetriNetElementModel> getNonLiveTransitions() {
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
    private Set<AbstractPetriNetElementModel> calcWronglyMarkedPlaces() {
        if (sourcePlaces == null) {
            sourcePlaces = workflowCheck.getSourcePlaces();
        }
        if (places == null) {
            places = netStatistics.getPlaces();
        }
        Set<AbstractPetriNetElementModel> wronglyMarkedPlaces = new HashSet<AbstractPetriNetElementModel>();
        Set<AbstractPetriNetElementModel> placesNotSource = new HashSet<AbstractPetriNetElementModel>(this.places);
        placesNotSource.removeAll(this.sourcePlaces);
        // check if source place has one token
        for (AbstractPetriNetElementModel place : this.sourcePlaces) {
            if (((PlaceModel) place).getTokenCount() != 1) {
                wronglyMarkedPlaces.add(place);
            }
        }
        // check if any other place has a token
        for (AbstractPetriNetElementModel place : placesNotSource) {
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
