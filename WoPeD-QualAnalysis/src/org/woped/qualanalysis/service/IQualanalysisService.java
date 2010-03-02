package org.woped.qualanalysis.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.model.AbstractElementModel;

/**
 * interface for qualanalysis services. all services have to implemnt this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface IQualanalysisService {
	
	/**
	 * 
	 * @return the number of places
	 */
	public int getNumPlaces();

	/**
	 * 
	 * @return an iterator of all places
	 */
	public Iterator<AbstractElementModel> getPlacesIterator();

	/**
	 * 
	 * @return the number of transitions
	 */
	public int getNumTransitions();

	/**
	 * 
	 * @return an iterator of all transitions
	 */
	public Iterator<AbstractElementModel> getTransitionsIterator();

	/**
	 * 
	 * @return the number of operators
	 */
	public int getNumOperators();

	/**
	 * 
	 * @return an iterator of all operators
	 */
	public Iterator<AbstractElementModel> getOperatorsIterator();

	/**
	 * 
	 * @return the number of subprocesses
	 */
	public int getNumSubprocesses();

	/**
	 * 
	 * @return an iterator of all subprocesses
	 */
	public Iterator<AbstractElementModel> getSubprocessesIterator();

	/**
	 * 
	 * @return the number of arcs
	 */
	public int getNumArcs();

	/**
	 * 
	 * @return the number of freeChoiceViolations
	 */
	public int getNumFreeChoiceViolations();

	/**
	 * 
	 * @return an iterator of all freeChoiceViolations
	 */
	public Iterator<Set<AbstractElementModel>> getFreeChoiceViolationsIterator();

	/**
	 * 
	 * @return the number of wrongly used operators
	 */
	public int getNumWronglyUsedOperators();

	/**
	 * 
	 * @return an iterator of all wrongly used operators
	 */
	public Iterator<AbstractElementModel> getWronglyUsedOperatorsIterator();

	/**
	 * 
	 * @return the number of not-connected nodes
	 */
	public int getNumNotConnectedNodes();

	/**
	 * 
	 * @return an iterator of all not-connected nodes
	 */
	public Iterator<AbstractElementModel> getNotConnectedNodesIterator();

	/**
	 * 
	 * @return the number of not-strongly-connected nodes
	 */
	public int getNumNotStronglyConnectedNodes();

	/**
	 * 
	 * @return an iterator of all not-strongly-connected nodes
	 */
	public Iterator<AbstractElementModel> getNotStronglyConnectedNodesIterator();

	/**
	 * 
	 * @return the number of sourcePlaces
	 */
	public int getNumSourcePlaces();

	/**
	 * 
	 * @return an iterator of all sourcePlaces
	 */
	public Iterator<AbstractElementModel> getSourcePlacesIterator();

	/**
	 * 
	 * @return the number of sinkPlaces
	 */
	public int getNumSinkPlaces();

	/**
	 * 
	 * @return an iterator of all sinkPlaces
	 */
	public Iterator<AbstractElementModel> getSinkPlacesIterator();

	/**
	 * 
	 * @return the number of sourceTransitions
	 */
	public int getNumSourceTransitions();

	/**
	 * 
	 * @return an iterator of all sourceTransitions
	 */
	public Iterator<AbstractElementModel> getSourceTransitionsIterator();

	/**
	 * 
	 * @return the number of sinkTransitions
	 */
	public int getNumSinkTransitions();

	/**
	 * 
	 * @return an iterator of all sinkTransitions
	 */
	public Iterator<AbstractElementModel> getSinkTransitionsIterator();

	/**
	 * 
	 * @return the number of PTHandles
	 */
	public int getNumPTHandles();

	/**
	 * 
	 * @return an iterator of all PTHandles
	 */
	public Iterator<Set<AbstractElementModel>> getPTHandlesIterator();

	/**
	 * 
	 * @return the number of TPHandles
	 */
	public int getNumTPHandles();

	/**
	 * 
	 * @return an iterator of all TPHandles
	 */
	public Iterator<Set<AbstractElementModel>> getTPHandlesIterator();

	/**
	 * 
	 * @return the number of SComponents
	 */
	public int getNumSComponents();

	/**
	 * 
	 * @return an iterator of all SComponents
	 */
	public Iterator<List<AbstractElementModel>> getSComponentsIterator();

	/**
	 * 
	 * @return the number of not-SCovered
	 */
	public int getNumNotSCovered();

	/**
	 * 
	 * @return an iterator of all not-SCovered
	 */
	public Iterator<AbstractElementModel> getNotSCoveredIterator();
	
	/**
	 * 
	 * @return the number places with wrong token count in initial marking
	 */
	public int getNumWronglyMarkedPlaces();

	/**
	 * 
	 * @return an iterator for places with wrong token count in initial marking
	 */
	public Iterator<AbstractElementModel> getWronglyMarkedPlacesIterator();
	
	/**
	 * 
	 * @return the number of unbounded places
	 */
	public int getNumUnboundedPlaces();

	/**
	 * 
	 * @return an iterator of all unbounded places
	 */
	public Iterator<AbstractElementModel> getUnboundedPlacesIterator();

	/**
	 * 
	 * @return the number of dead transitions
	 */
	public int getNumDeadTransitions();

	/**
	 * 
	 * @return an iterator of all dead transitions
	 */
	public Iterator<AbstractElementModel> getDeadTransitionsIterator();

	/**
	 * 
	 * @return the number of non-live transitions
	 */
	public int getNumNonLiveTransitions();

	/**
	 * 
	 * @return an iterator of all non-live transitions
	 */
	public Iterator<AbstractElementModel> getNonLiveTransitionsIterator();

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
