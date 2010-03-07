package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 *interface for source/sink tests.
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface ISourceSinkTest {

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
}
