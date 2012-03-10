package org.woped.qualanalysis.soundness.datamodel;

import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;

public interface ILowLevelPetriNet extends INodeNet<AbstractNode> {

	public boolean addNode(AbstractNode node);
	
	public boolean addNodes(Set<AbstractNode> nodes);
	
	public PlaceNode getPlaceNode(PlaceNode node);
	
	public Set<PlaceNode> getPlaces();
	
	public TransitionNode getTransitionNode(TransitionNode node);
	
	public Set<TransitionNode> getTransitions();
	
}
