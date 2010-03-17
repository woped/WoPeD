package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.woped.core.model.AbstractElementModel;
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
    public Set<AbstractElementModel> getPlaces();

    /**
     * 
     * @return a set of all transitions
     */
    public Set<AbstractElementModel> getTransitions();

    /**
     * 
     * @return a set of all operators
     */
    public Set<AbstractElementModel> getOperators();

    /**
     * 
     * @return a set of all subprocesses
     */
    public Set<AbstractElementModel> getSubprocesses();

    /**
     * 
     * @return the number of arcs
     */
    public int getNumArcs();

    /**
     * 
     * @return a set of all freeChoiceViolations (each violation is a set of elements)
     */
    public Set<Set<AbstractElementModel>> getFreeChoiceViolations();

    /**
     * 
     * @return a set of all wrongly used operators
     */
    public Set<AbstractElementModel> getWronglyUsedOperators();

    /**
     * 
     * @return a set of all not-connected nodes
     */
    public Set<AbstractElementModel> getNotConnectedNodes();

    /**
     * 
     * @return a set of all connected components (each component is a set of elements)
     */
    public Set<Set<AbstractElementModel>> getConnectedComponents();

    /**
     * 
     * @return a set of all not-strongly-connected nodes
     */
    public Set<AbstractElementModel> getNotStronglyConnectedNodes();

    /**
     * 
     * @return a set of strongly connected components (each component is a set of elements)
     */
    public Set<Set<AbstractElementModel>> getStronglyConnectedComponents();

    /**
     * 
     * @return a set of all sourcePlaces
     */
    public Set<AbstractElementModel> getSourcePlaces();

    /**
     * 
     * @return a set of all sinkPlaces
     */
    public Set<AbstractElementModel> getSinkPlaces();

    /**
     * 
     * @return a set of all sourceTransitions
     */
    public Set<AbstractElementModel> getSourceTransitions();

    /**
     * 
     * @return a set of all sinkTransitions
     */
    public Set<AbstractElementModel> getSinkTransitions();

    /**
     * 
     * @return a set of all PTHandles (each handle is a set of elements)
     */
    public Set<Set<AbstractElementModel>> getPTHandles();

    /**
     * 
     * @return a set of all TPHandles (each handle is a set of elements)
     */
    public Set<Set<AbstractElementModel>> getTPHandles();

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
    public Set<List<AbstractElementModel>> getSComponents();

    /**
     * 
     * @return a set of all not-SCovered
     */
    public Set<AbstractElementModel> getNotSCovered();

    /**
     * 
     * @return an set of places with wrong token count in initial marking
     */
    public Set<AbstractElementModel> getWronglyMarkedPlaces();

    /**
     * 
     * @return a set of all unbounded places
     */
    public Set<AbstractElementModel> getUnboundedPlaces();

    /**
     * 
     * @return a set of all dead transitions
     */
    public Set<AbstractElementModel> getDeadTransitions();

    /**
     * 
     * @return a set of all non-live transitions
     */
    public Set<AbstractElementModel> getNonLiveTransitions();

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
     * method to get rid of all stuff that is not needed any more <br />
     * e.g. temporary files etc.
     */
    public void cleanup();
}
