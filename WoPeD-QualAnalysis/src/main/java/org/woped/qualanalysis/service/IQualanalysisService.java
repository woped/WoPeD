package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.structure.components.ClusterElement;

/**
 * interface for qualanalysis services. all services have to implemnt this interface
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IQualanalysisService {

    /**
     * 
     * @return a set of all places
     */
    public Set<AbstractPetriNetElementModel> getPlaces();

    /**
     * 
     * @return a set of all transitions
     */
    public Set<AbstractPetriNetElementModel> getTransitions();

    /**
     * 
     * @return a set of all operators
     */
    public Set<AbstractPetriNetElementModel> getOperators();

    /**
     * 
     * @return a set of all subprocesses
     */
    public Set<AbstractPetriNetElementModel> getSubprocesses();

    /**
     * 
     * @return the number of arcs
     */
    public int getNumArcs();

    /**
     * 
     * @return a set of all freeChoiceViolations (each violation is a set of elements)
     */
    public Set<Set<AbstractPetriNetElementModel>> getFreeChoiceViolations();

    /**
     * 
     * @return a set of all wrongly used operators
     */
    public Set<AbstractPetriNetElementModel> getWronglyUsedOperators();

    /**
     * 
     * @return a set of all not-connected nodes
     */
    public Set<AbstractPetriNetElementModel> getNotConnectedNodes();

    /**
     * 
     * @return a set of all connected components (each component is a set of elements)
     */
    public Set<Set<AbstractPetriNetElementModel>> getConnectedComponents();

    /**
     * 
     * @return a set of all not-strongly-connected nodes
     */
    public Set<AbstractPetriNetElementModel> getNotStronglyConnectedNodes();

    /**
     * Gets the arcs of the petri net whose weight is larger than 1.
     *
     * @return the arcs that violate the weight condition
     */
    Set<ArcModel> getArcWeightViolations();

    /**
     * 
     * @return a set of strongly connected components (each component is a set of elements)
     */
    public Set<Set<AbstractPetriNetElementModel>> getStronglyConnectedComponents();

    /**
     * 
     * @return a set of all sourcePlaces
     */
    public Set<AbstractPetriNetElementModel> getSourcePlaces();

    /**
     * 
     * @return a set of all sinkPlaces
     */
    public Set<AbstractPetriNetElementModel> getSinkPlaces();

    /**
     * 
     * @return a set of all sourceTransitions
     */
    public Set<AbstractPetriNetElementModel> getSourceTransitions();

    /**
     * 
     * @return a set of all sinkTransitions
     */
    public Set<AbstractPetriNetElementModel> getSinkTransitions();

    /**
     * 
     * @return a set of all PTHandles (each handle is a set of elements)
     */
    public Set<Set<AbstractPetriNetElementModel>> getPTHandles();

    /**
     * 
     * @return a set of all TPHandles (each handle is a set of elements)
     */
    public Set<Set<AbstractPetriNetElementModel>> getTPHandles();

    /**
     * TODO what is this method for?
     * 
     * @return ??
     */
    public HashSet<Set<ClusterElement>> getM_handleClusters();

    /**
     * 
     * @return a set of all SComponents (each SComponent is a list of elements)
     */
    public Set<List<AbstractPetriNetElementModel>> getSComponents();

    /**
     * 
     * @return a set of all not-SCovered
     */
    public Set<AbstractPetriNetElementModel> getNotSCovered();

    /**
     * 
     * @return an set of places with wrong token count in initial marking
     */
    public Set<AbstractPetriNetElementModel> getWronglyMarkedPlaces();

    /**
     * 
     * @return a set of all unbounded places
     */
    public Set<AbstractPetriNetElementModel> getUnboundedPlaces();

    /**
     * 
     * @return a set of all dead transitions
     */
    public Set<AbstractPetriNetElementModel> getDeadTransitions();

    /**
     * 
     * @return a set of all non-live transitions
     */
    public Set<AbstractPetriNetElementModel> getNonLiveTransitions();

    /**
     * method to check if the given petrinet is a workflownet or not
     * 
     * @return true if the petrinet is a workflownet otherwise false
     */
    public boolean isWorkflowNet();

    /**
     * method to check if the given petrinet is sound or not
     * 
     * @return true if the petrinet is sound otherwise false
     */
    public boolean isSound();

    /**
     * method to check if the given petrinet is sound or not
     *
     * @return true if the petrinet is sound otherwise false
     */

    public void cleanup();
}
