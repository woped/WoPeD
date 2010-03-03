package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

public interface IWorkflowTest {

	/**
	 * 
	 * @return a set of all source places
	 */
	public Set<PlaceNode> getSourcePlaces();
	
	/**
	 * 
	 * @return a set of all sink places
	 */
	public Set<PlaceNode> getSinkPlaces();
	
	/**
	 * 
	 * @return a set of all transitions with empty preset
	 */
	public Set<TransitionNode> getSourceTransitions();
	
	/**
	 * 
	 * @return a set of all transitions with empty postset
	 */
	public Set<TransitionNode> getSinkTransitions();
	
	/**
	 * 
	 * @return a set of all nodes with empty preset and empty postset
	 */
	public Set<AbstractNode> getNotConnectedNodes();
	
	/**
	 * 
	 * @return a set of all nodes not being strongly connected
	 */
	public Set<AbstractNode> getNotStronglyConnected();
}
