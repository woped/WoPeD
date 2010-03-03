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
import org.woped.qualanalysis.structure.StructuralAnalysis;
import org.woped.qualanalysis.structure.components.ClusterElement;

/**
 * abstract class for qualanalysis services contains all needed variables and getters
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractQualanalysisService implements IQualanalysisService {

	protected IEditor editor;
	private StructuralAnalysis sA;
	protected ISComponent sComponent;
	protected ISoundnessCheck soundnessCheck;
	protected IWorkflowCheck workflowCheck;
	private INetStatistics netStatistics;

	private Set<AbstractElementModel> places;
	private Set<AbstractElementModel> transitions;
	private Set<AbstractElementModel> operators;
	private Set<AbstractElementModel> subprocesses;
	private int numArcs;
	private Set<Set<AbstractElementModel>> freeChoiceViolations;
	private Set<AbstractElementModel> wronglyUsedOperators;
	private Set<AbstractElementModel> notConnectedNodes;
	private Set<AbstractElementModel> notStronglyConnectedNodes;
	private Set<AbstractElementModel> sourcePlaces;
	private Set<AbstractElementModel> sinkPlaces;
	private Set<AbstractElementModel> sourceTransitions;
	private Set<AbstractElementModel> sinkTransitions;
	private Set<Set<AbstractElementModel>> pTHandles;
	private Set<Set<AbstractElementModel>> tPHandles;
	private HashSet<Set<ClusterElement>> m_handleClusters;
	private Set<List<AbstractElementModel>> sComponents;
	private Set<AbstractElementModel> notSCovered;
	private Set<AbstractElementModel> wronglyMarkedPlaces;
	private Set<AbstractElementModel> unboundedPlaces;
	private Set<AbstractElementModel> deadTransitions;
	private Set<AbstractElementModel> nonLiveTransitions;

	public AbstractQualanalysisService(IEditor editor) {
		this.editor = editor;
		sA = new StructuralAnalysis(editor);
		workflowCheck = sA;
		netStatistics = sA;
	}

	protected void init() {
		// Net-statistics
		places = getSet(netStatistics.getPlacesIterator());
		transitions = getSet(netStatistics.getTransitionsIterator());
		operators = getSet(netStatistics.getOperatorsIterator());
		subprocesses = getSet(netStatistics.getSubprocessesIterator());
		numArcs = netStatistics.getNumArcs();
		// Other stuff
		freeChoiceViolations = getSet(sA.getFreeChoiceViolations());
		wronglyUsedOperators = getSet(sA.getMisusedOperatorsIterator());
		// Workflow
		sourcePlaces = getSet(workflowCheck.getSourcePlacesIterator());
		sinkPlaces = getSet(workflowCheck.getSinkPlacesIterator());
		sourceTransitions = getSet(workflowCheck.getSourceTransitionsIterator());
		sinkTransitions = getSet(workflowCheck.getSinkTransitionsIterator());
		notConnectedNodes = getSet(workflowCheck.getNotConnectedNodes());
		notStronglyConnectedNodes = getSet(workflowCheck.getNotStronglyConnectedNodes());
		// Wellstructuredness
		pTHandles = getSet(sA.getPTHandlesIterator());
		tPHandles = getSet(sA.getTPHandlesIterator());
		m_handleClusters = sA.getM_handleClusters();
		// SComponents
		sComponents = getSet(sComponent.getSComponentsIterator());
		notSCovered = getSet(sComponent.getNotSCoveredIterator());
		// Soundness
		unboundedPlaces = getSet(soundnessCheck.getUnboundedPlacesIterator());
		deadTransitions = getSet(soundnessCheck.getDeadTransitionsIterator());
		nonLiveTransitions = getSet(soundnessCheck.getNonLiveTransitionsIterator());
		// Other stuff
		wronglyMarkedPlaces = calcWronglyMarkedPlaces();
	}

	public int getNumPlaces() {
		return places.size();
	}

	public Iterator<AbstractElementModel> getPlacesIterator() {
		return places.iterator();
	}

	public int getNumTransitions() {
		return transitions.size();
	}

	public Iterator<AbstractElementModel> getTransitionsIterator() {
		return transitions.iterator();
	}

	public int getNumOperators() {
		return operators.size();
	}

	public Iterator<AbstractElementModel> getOperatorsIterator() {
		return operators.iterator();
	}

	public int getNumSubprocesses() {
		return subprocesses.size();
	}

	public Iterator<AbstractElementModel> getSubprocessesIterator() {
		return subprocesses.iterator();
	}

	public int getNumArcs() {
		return numArcs;
	}

	public int getNumFreeChoiceViolations() {
		return freeChoiceViolations.size();
	}

	public Iterator<Set<AbstractElementModel>> getFreeChoiceViolationsIterator() {
		return freeChoiceViolations.iterator();
	}

	public int getNumWronglyUsedOperators() {
		return wronglyUsedOperators.size();
	}

	public Iterator<AbstractElementModel> getWronglyUsedOperatorsIterator() {
		return wronglyUsedOperators.iterator();
	}

	public int getNumNotConnectedNodes() {
		return notConnectedNodes.size();
	}

	public Iterator<AbstractElementModel> getNotConnectedNodesIterator() {
		return notConnectedNodes.iterator();
	}

	public int getNumNotStronglyConnectedNodes() {
		return notStronglyConnectedNodes.size();
	}

	public Iterator<AbstractElementModel> getNotStronglyConnectedNodesIterator() {
		return notStronglyConnectedNodes.iterator();
	}

	public int getNumSourcePlaces() {
		return sourcePlaces.size();
	}

	public Iterator<AbstractElementModel> getSourcePlacesIterator() {
		return sourcePlaces.iterator();
	}

	public int getNumSinkPlaces() {
		return sinkPlaces.size();
	}

	public Iterator<AbstractElementModel> getSinkPlacesIterator() {
		return sinkPlaces.iterator();
	}

	public int getNumSourceTransitions() {
		return sourceTransitions.size();
	}

	public Iterator<AbstractElementModel> getSourceTransitionsIterator() {
		return sourceTransitions.iterator();
	}

	public int getNumSinkTransitions() {
		return sinkTransitions.size();
	}

	public Iterator<AbstractElementModel> getSinkTransitionsIterator() {
		return sinkTransitions.iterator();
	}

	public int getNumPTHandles() {
		return pTHandles.size();
	}

	public Iterator<Set<AbstractElementModel>> getPTHandlesIterator() {
		return pTHandles.iterator();
	}

	public int getNumTPHandles() {
		return tPHandles.size();
	}

	public Iterator<Set<AbstractElementModel>> getTPHandlesIterator() {
		return tPHandles.iterator();
	}

	public HashSet<Set<ClusterElement>> getM_handleClusters() {
		return m_handleClusters;
	}

	public int getNumSComponents() {
		return sComponents.size();
	}

	public Iterator<List<AbstractElementModel>> getSComponentsIterator() {
		return sComponents.iterator();
	}

	public int getNumNotSCovered() {
		return notSCovered.size();
	}

	public Iterator<AbstractElementModel> getNotSCoveredIterator() {
		return notSCovered.iterator();
	}

	public int getNumWronglyMarkedPlaces() {
		return wronglyMarkedPlaces.size();
	}

	public Iterator<AbstractElementModel> getWronglyMarkedPlacesIterator() {
		return wronglyMarkedPlaces.iterator();
	}

	public int getNumUnboundedPlaces() {
		return unboundedPlaces.size();
	}

	public Iterator<AbstractElementModel> getUnboundedPlacesIterator() {
		return unboundedPlaces.iterator();
	}

	public int getNumDeadTransitions() {
		return deadTransitions.size();
	}

	public Iterator<AbstractElementModel> getDeadTransitionsIterator() {
		return deadTransitions.iterator();
	}

	public int getNumNonLiveTransitions() {
		return nonLiveTransitions.size();
	}

	public Iterator<AbstractElementModel> getNonLiveTransitionsIterator() {
		return nonLiveTransitions.iterator();
	}

	/**
	 * @see IQualanalysisService#isWorkflowNet()
	 */
	@Override
	public boolean isWorkflowNet() {
		if (getNumSourcePlaces() != 1) {
			return false;
		}
		if (getNumSinkPlaces() != 1) {
			return false;
		}
		if (getNumSourceTransitions() != 0) {
			return false;
		}
		if (getNumSinkTransitions() != 0) {
			return false;
		}
		if (getNumNotConnectedNodes() != 0) {
			return false;
		}
		if (getNumNotStronglyConnectedNodes() != 0) {
			return false;
		}
		return true;
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
	 * @param <T>
	 *            generic type.
	 * @param iter
	 *            iterator to create the set from
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
