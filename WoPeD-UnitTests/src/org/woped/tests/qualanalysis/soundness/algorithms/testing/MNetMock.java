package org.woped.tests.qualanalysis.soundness.algorithms.testing;

import java.util.HashSet;
import java.util.Set;

import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.Marking;

public class MNetMock implements IMarkingNet {
	private TransitionNode[] transitions = new TransitionNode[4];
	private Set<Marking> markings = new HashSet<Marking>();
	private PlaceNode[] places = new PlaceNode[3];
	
	public MNetMock() {
		// Constructor needed to create mock data, transitions and markings			
//		Create places
		places[0] =  new PlaceNode(0, 0, "p3", "p3", "p3");
		places[1] =  new PlaceNode(1, 1, "p1", "p1", "p1");
		places[2] =  new PlaceNode(0, 0, "p2", "p2", "p2");
		
//		Create transitions
		transitions[0] = new TransitionNode("t3", "t3", "t3", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		transitions[1] = new TransitionNode("t2", "t2", "t2", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		transitions[2] = new TransitionNode("t1", "t1", "t1", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		transitions[3] = new TransitionNode("t*", "t*", "t*", OperatorTransitionModel.TRANS_SIMPLE_TYPE);
		
//		Create post- and preNode entries
		places[2].addPostNode(transitions[1]);
		transitions[1].addPreNode(places[2]);
		transitions[1].addPostNode(places[0]);
		places[0].addPreNode(transitions[1]);
		places[1].addPostNode(transitions[2]);
		transitions[2].addPreNode(places[1]);
		transitions[2].addPostNode(places[2]);
		places[2].addPreNode(transitions[2]);
		places[2].addPostNode(transitions[0]);
		transitions[0].addPreNode(places[2]);
		transitions[0].addPostNode(places[0]);
		places[0].addPreNode(transitions[0]);
		places[2].addPostNode(transitions[1]);
		transitions[1].addPreNode(places[2]);
		transitions[1].addPostNode(places[0]);
		places[0].addPreNode(transitions[1]);
		places[1].addPostNode(transitions[2]);
		transitions[2].addPreNode(places[1]);
		transitions[2].addPostNode(places[2]);
		places[2].addPreNode(transitions[2]);
		places[2].addPostNode(transitions[0]);
		transitions[0].addPreNode(places[2]);
		transitions[0].addPostNode(places[0]);
		places[0].addPreNode(transitions[0]);
		transitions[3].addPreNode(places[0]);
		transitions[3].addPostNode(places[1]);
		places[0].addPostNode(transitions[3]);
		places[1].addPreNode(transitions[3]);
		
//		Create markings
		Integer[][] tokens = { {1, 0, 0}, {0, 0, 1}, {0, 1, 0} };
		Boolean[] placeUnlimited = {false, false, false};
		Marking[] marking = new Marking[3];
		
		marking[0] = new Marking(tokens[0], places, placeUnlimited);
		marking[1] = new Marking(tokens[1], places, placeUnlimited);
		marking[2] = new Marking(tokens[2], places, placeUnlimited);
	
		marking[0].setInitial(false);
		marking[1].setInitial(false);
		marking[2].setInitial(true);
		marking[2].setPredecessor(marking[1]);
		marking[1].setPredecessor(marking[0]);
		
		marking[2].addSuccessor(new Arc(marking[1], transitions[2]));
		marking[1].addSuccessor(new Arc(marking[0], transitions[0]));
		marking[1].addSuccessor(new Arc(marking[0], transitions[1]));
		marking[0].addSuccessor(new Arc(marking[2], transitions[3]));
		
		for(int i = 0; i < marking.length; i++)
			markings.add(marking[i]);
	}
	
    public TransitionNode[] getTransitions() {
        return transitions;
    }
    
    public Set<Marking> getMarkings() {
        return markings;
    }
    
	@Override
	public PlaceNode[] getPlaces() {
		return places;
	}

	@Override
	public TransitionNode[] getActivatedTransitions(Marking marking) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marking getInitialMarking() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String placesToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String placesToStringId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String placesToStringName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marking calculateSucceedingMarking(Marking parentMarking,
			TransitionNode transition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Marking> getAllContainedNodes() {
		return new HashSet<Marking>(markings);
	}
}
