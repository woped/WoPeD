package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;

public abstract class AbstractQualanalysisService implements IQualanalysisService {

	protected IEditor editor;
	private StructuralAnalysis sA;
	protected ISComponent sComponent;
	protected ISoundnessCheck soundnessCheck;

	protected int numPlaces;
	protected Set<AbstractElementModel> places;
	protected int numTransitions;
	protected Set<AbstractElementModel> transitions;
	protected int numOperators;
	protected Set<AbstractElementModel> operators;
	protected int numSubprocesses;
	protected Set<AbstractElementModel> subprocesses;
	protected int numArcs;
	protected int numFreeChoiceViolations;
	protected Set<Set<AbstractElementModel>> freeChoiceViolations;
	protected int numMisusedOperators;
	protected Set<AbstractElementModel> misusedOperators;
	protected int numNotConnectedNodes;
	protected Set<AbstractElementModel> notConnectedNodes;
	protected int numNotStronglyConnectedNodes;
	protected Set<AbstractElementModel> notStronglyConnectedNodes;
	protected int numSourcePlaces;
	protected Set<AbstractElementModel> sourcePlaces;
	protected int numSinkPlaces;
	protected Set<AbstractElementModel> sinkPlaces;
	protected int numSourceTransitions;
	protected Set<AbstractElementModel> sourceTransitions;
	protected int numSinkTransitions;
	protected Set<AbstractElementModel> sinkTransitions;
	protected int numPTHandles;
	protected Set<Set<AbstractElementModel>> pTHandles;
	protected int numTPHandles;
	protected Set<Set<AbstractElementModel>> tPHandles;
	protected int numSComponents;
	protected Set<List<AbstractElementModel>> sComponents;
	protected int numNotSCovered;
	protected Set<AbstractElementModel> notSCovered;
	protected int numUnboundedPlaces;
	protected Set<AbstractElementModel> unboundedPlaces;
	protected int numDeadTransitions;
	protected Set<AbstractElementModel> deadTransitions;
	protected int numNonLiveTransitions;
	protected Set<AbstractElementModel> nonLiveTransitions;

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

	public boolean isWorkflowNet() {
		Boolean isWorkflowNet = true;
		if (numSourcePlaces != 1)
			isWorkflowNet = false;
		if (numSinkPlaces != 1)
			isWorkflowNet = false;
		if (numSourceTransitions != 0)
			isWorkflowNet = false;
		if (numSinkTransitions != 0)
			isWorkflowNet = false;
		if (numNotConnectedNodes != 0)
			isWorkflowNet = false;
		if (numNotStronglyConnectedNodes != 0)
			isWorkflowNet = false;
		return isWorkflowNet;
	}

	/**
	 * 
	 * @param iter
	 *            iterator to create the set from
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
