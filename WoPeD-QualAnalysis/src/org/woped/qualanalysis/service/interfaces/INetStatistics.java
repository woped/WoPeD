package org.woped.qualanalysis.service.interfaces;

import java.util.Set;

import org.woped.core.model.AbstractElementModel;

/**
 * interface for NetStatistics-parts of qualanalysis servies.
 * all classes which implement the NetStatistics-methods of a service must implement this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface INetStatistics {
	
	/**
	 * 
	 * @return a set with all places
	 */
	public Set<AbstractElementModel> getPlaces();
	
	/**
	 * 
	 * @return a set with all transitions
	 */
	public Set<AbstractElementModel> getTransitions();

	/**
	 * 
	 * @return a set with all operator-transitions
	 */
	public Set<AbstractElementModel> getOperators();

	/**
	 * 
	 * @return a set with all subprocesses
	 */
	public Set<AbstractElementModel> getSubprocesses();

	/**
	 * 
	 * @return the number of arcs
	 */
	public int getNumArcs();
}
