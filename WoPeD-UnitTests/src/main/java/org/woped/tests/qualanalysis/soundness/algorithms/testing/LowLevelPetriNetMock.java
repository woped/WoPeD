package org.woped.tests.qualanalysis.soundness.algorithms.testing;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNodeComparator;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * LowLevelPetriNet without t*
 */

public class LowLevelPetriNetMock implements ILowLevelPetriNet {
	private Set<TransitionNode> transitions = new HashSet<TransitionNode>();
	private Set<PlaceNode> places = new HashSet<PlaceNode>();

	public LowLevelPetriNetMock() {
		// Constructor needed to create mock data, transitions and markings	
		TransitionNode[] transitions = new TransitionNode[3];
		PlaceNode[] places = new PlaceNode[3];
		
//		Create places
		places[0] =  new PlaceNode(0, 0, "p3", "p3", "p3");
		places[1] =  new PlaceNode(1, 1, "p1", "p1", "p1");
		places[2] =  new PlaceNode(0, 0, "p2", "p2", "p2");
		
//		Create transitions
		transitions[0] = new TransitionNode("t3", "t3", "t3", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		transitions[1] = new TransitionNode("t2", "t2", "t2", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		transitions[2] = new TransitionNode("t1", "t1", "t1", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		
//		Create post- and preNode entries
		places[2].addSuccessorNode(transitions[1]);
		transitions[1].addPredecessorNode(places[2]);
		transitions[1].addSuccessorNode(places[0]);
		places[0].addPredecessorNode(transitions[1]);
		places[1].addSuccessorNode(transitions[2]);
		transitions[2].addPredecessorNode(places[1]);
		transitions[2].addSuccessorNode(places[2]);
		places[2].addPredecessorNode(transitions[2]);
		places[2].addSuccessorNode(transitions[0]);
		transitions[0].addPredecessorNode(places[2]);
		transitions[0].addSuccessorNode(places[0]);
		places[0].addPredecessorNode(transitions[0]);
		places[2].addSuccessorNode(transitions[1]);
		transitions[1].addPredecessorNode(places[2]);
		transitions[1].addSuccessorNode(places[0]);
		places[0].addPredecessorNode(transitions[1]);
		places[1].addSuccessorNode(transitions[2]);
		transitions[2].addPredecessorNode(places[1]);
		transitions[2].addSuccessorNode(places[2]);
		places[2].addPredecessorNode(transitions[2]);
		places[2].addSuccessorNode(transitions[0]);
		transitions[0].addPredecessorNode(places[2]);
		transitions[0].addSuccessorNode(places[0]);
		places[0].addPredecessorNode(transitions[0]);
		
		for(int i = 0; i < transitions.length; i++)
			this.transitions.add(transitions[i]);
		
		for(int i = 0; i < places.length; i++)
			this.places.add(places[i]);
	}
	
	@Override
	public Set<AbstractNode> getAllContainedNodes() {
        Set<AbstractNode> set = new HashSet<AbstractNode>();
        for (TransitionNode node : transitions)
        	set.add(node);
        for (PlaceNode node : places)
        	set.add(node);
        return set;
	}
	
	@Override
	public Set<TransitionNode> getTransitions() {
		return this.transitions;
	}


	@Override
	public SortedSet<PlaceNode> getPlaces() {
		SortedSet<PlaceNode> nodes = new TreeSet<>(new PlaceNodeComparator());
        nodes.addAll(this.places);
		return nodes;
	}

	@Override
	public boolean addNode(AbstractNode node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNodes(Set<AbstractNode> nodes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PlaceNode getPlaceNode(PlaceNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransitionNode getTransitionNode(TransitionNode node) {
		// TODO Auto-generated method stub
		return null;
	}
}
