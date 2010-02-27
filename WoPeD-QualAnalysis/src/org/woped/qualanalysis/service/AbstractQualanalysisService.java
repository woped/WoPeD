package org.woped.qualanalysis.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;

/**
 * abstract class for qualanalysis services
 * contains all needed variables and getters
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public abstract class AbstractQualanalysisService implements IQualanalysisService {

	protected IEditor editor;
	private StructuralAnalysis sA;
	protected ISComponent sComponent;
	protected ISoundnessCheck soundnessCheck;

	private Set<AbstractElementModel> places;
	private Set<AbstractElementModel> transitions;
	private Set<AbstractElementModel> operators;
	private Set<AbstractElementModel> subprocesses;
	private int numArcs;
	private Set<Set<AbstractElementModel>> freeChoiceViolations;
	private Set<AbstractElementModel> misusedOperators;
	private Set<AbstractElementModel> notConnectedNodes;
	private Set<AbstractElementModel> notStronglyConnectedNodes;
	private Set<AbstractElementModel> sourcePlaces;
	private Set<AbstractElementModel> sinkPlaces;
	private Set<AbstractElementModel> sourceTransitions;
	private Set<AbstractElementModel> sinkTransitions;
	private Set<Set<AbstractElementModel>> pTHandles;
	private Set<Set<AbstractElementModel>> tPHandles;
	private Set<List<AbstractElementModel>> sComponents;
	private Set<AbstractElementModel> notSCovered;
	private Set<AbstractElementModel> innerTokens;
	private Set<AbstractElementModel> unboundedPlaces;
	private Set<AbstractElementModel> deadTransitions;
	private Set<AbstractElementModel> nonLiveTransitions;

	public AbstractQualanalysisService(IEditor editor) {
		this.editor = editor;
		sA = new StructuralAnalysis(editor);
	}

	@SuppressWarnings("unchecked")
	protected void init() {
		places = getSet(sA.getPlacesIterator());
		transitions = getSet(sA.getTransitionsIterator());
		operators = getSet(sA.getOperatorsIterator());
		subprocesses = getSet(sA.getSubprocessesIterator());
		numArcs = sA.getNumArcs();
		freeChoiceViolations = getSet(sA.getFreeChoiceViolations());
		misusedOperators = getSet(sA.getMisusedOperatorsIterator());
		notConnectedNodes = getSet(sA.getNotConnectedNodes());
		notStronglyConnectedNodes = getSet(sA.getNotStronglyConnectedNodes());
		sourcePlaces = getSet(sA.getSourcePlacesIterator());
		sinkPlaces = getSet(sA.getSinkPlacesIterator());
		sourceTransitions = getSet(sA.getSourceTransitionsIterator());
		sinkTransitions = getSet(sA.getSinkTransitionsIterator());
		pTHandles = getSet(sA.getPTHandlesIterator());
		tPHandles = getSet(sA.getTPHandlesIterator());
		// SComponents
		sComponents = getSet(sComponent.getSComponentsIterator());
		notSCovered = getSet(sComponent.getNotSCoveredIterator());
		// Soundness
		innerTokens = calcInnerTokens();
		unboundedPlaces = getSet(soundnessCheck.getUnboundedPlacesIterator());
		deadTransitions = getSet(soundnessCheck.getDeadTransitionsIterator());
		nonLiveTransitions = getSet(soundnessCheck.getNonLiveTransitionsIterator());
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

	public int getNumMisusedOperators() {
		return misusedOperators.size();
	}

	public Iterator<AbstractElementModel> getMisusedOperatorsIterator() {
		return misusedOperators.iterator();
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

	public int getNumInnerTokens() {
		return innerTokens.size();
	}

	public Iterator<AbstractElementModel> getInnerTokensIterator() {
		return innerTokens.iterator();
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

	public boolean isWorkflowNet() {
		if (getNumSourcePlaces() != 1)
			return false;
		if (getNumSinkPlaces() != 1)
			return false;
		if (getNumSourceTransitions() != 0)
			return false;
		if (getNumSinkTransitions() != 0)
			return false;
		if (getNumNotConnectedNodes() != 0)
			return false;
		if (getNumNotStronglyConnectedNodes() != 0)
			return false;
		return true;
	}
	
	public boolean isSound(){
		if(!isWorkflowNet())
			return false;
		if(getNumInnerTokens() != 0)
			return false;
		if(getNumUnboundedPlaces() != 0)
			return false;
		if(getNumDeadTransitions() != 0)
			return false;
		if(getNumNonLiveTransitions() != 0)
			return false;
		return true;
	}
	
	public boolean isSourceToken(){
		if(((PlaceModel)getSourcePlacesIterator().next()).getTokenCount() > 0)
			return true;
		else
			return false;	
	}
	
	/**
	 * mehtod to find all inner places (= places not being source) which have one or more tokens
	 * 
	 * @return a set of AbstractElementModels (=places) which have wrong tokens
	 */
	public Set<AbstractElementModel> calcInnerTokens(){
		Set<AbstractElementModel> placesNotSource = new HashSet<AbstractElementModel>(this.places);
		placesNotSource.removeAll(this.sourcePlaces);
		for(AbstractElementModel place: this.places){
			if(((PlaceModel)place).getTokenCount() == 0)
				placesNotSource.remove(place);
		}
		return placesNotSource;
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
