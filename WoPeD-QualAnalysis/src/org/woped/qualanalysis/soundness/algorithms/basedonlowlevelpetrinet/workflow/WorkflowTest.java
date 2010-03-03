package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.AbstractLowLevelPetriNetTest;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * @see IWorkflowTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public class WorkflowTest extends AbstractLowLevelPetriNetTest implements IWorkflowTest {
	
	/**
     * 
     * @param lolNetWithTStar LowLevelPetriNet (without t*) the algorithm is based on
     */  
    public WorkflowTest(LowLevelPetriNet lolNetWithoutTStar) {
        super(lolNetWithoutTStar);
    }





	@Override
	public Set<PlaceNode> getSourcePlaces() {
		PlaceNode[] places = lolNet.getPlaces();
		Set<PlaceNode> sourcePlaces = new HashSet<PlaceNode>();
		for(int i=0; i<places.length; i++){
			if(places[i].getPreNodes().length == 0)
				sourcePlaces.add(places[i]);
		}
		return sourcePlaces;
	}
	
	@Override
	public Set<PlaceNode> getSinkPlaces() {
		PlaceNode[] places = lolNet.getPlaces();
		Set<PlaceNode> sinkPlaces = new HashSet<PlaceNode>();
		for(int i=0; i<places.length; i++){
			if(places[i].getPostNodes().length == 0)
				sinkPlaces.add(places[i]);
		}
		return sinkPlaces;
	}

	@Override
	public Set<TransitionNode> getSourceTransitions() {
		TransitionNode[] transitions = lolNet.getTransitions();
		Set<TransitionNode> sourceTransitions = new HashSet<TransitionNode>();
		for(int i=0; i<transitions.length; i++){
			if(transitions[i].getPreNodes().length == 0)
				sourceTransitions.add(transitions[i]);
		}
		return sourceTransitions;
	}	
	
	@Override
	public Set<TransitionNode> getSinkTransitions() {
		TransitionNode[] transitions = lolNet.getTransitions();
		Set<TransitionNode> sinkTransitions = new HashSet<TransitionNode>();
		for(int i=0; i<transitions.length; i++){
			if(transitions[i].getPostNodes().length == 0)
				sinkTransitions.add(transitions[i]);
		}
		return sinkTransitions;
	}
	
	@Override
	public Set<AbstractNode> getNotConnectedNodes() {
		TransitionNode[] transitions = lolNet.getTransitions();
		PlaceNode[] places = lolNet.getPlaces();
		Set<AbstractNode> notConnectedNodes = new HashSet<AbstractNode>();
		for(int i=0; i<transitions.length; i++){
			if(transitions[i].getPostNodes().length == 0 && transitions[i].getPreNodes().length == 0)
				notConnectedNodes.add(transitions[i]);
		}
		for(int i=0; i<places.length; i++){
			if(places[i].getPostNodes().length == 0 && places[i].getPreNodes().length == 0)
				notConnectedNodes.add(places[i]);
		}
		return notConnectedNodes;
	}

	@Override
	public Set<AbstractNode> getNotStronglyConnected() {
		// TODO Auto-generated method stub
		return new HashSet<AbstractNode>();
	}
    
    
}
