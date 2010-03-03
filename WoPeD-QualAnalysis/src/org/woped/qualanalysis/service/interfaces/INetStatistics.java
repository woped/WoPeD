package org.woped.qualanalysis.service.interfaces;

import java.util.Iterator;

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
	 * @return an iterator for all places
	 */
	public Iterator<AbstractElementModel> getPlacesIterator();
	
	/**
	 * 
	 * @return an iterator for all transitions
	 */
	public Iterator<AbstractElementModel> getTransitionsIterator();

	/**
	 * 
	 * @return an iterator for all operator-transitions
	 */
	public Iterator<AbstractElementModel> getOperatorsIterator();

	/**
	 * 
	 * @return an iterator for all subprocesses
	 */
	public Iterator<AbstractElementModel> getSubprocessesIterator();

	/**
	 * 
	 * @return the number of arcs
	 */
	public int getNumArcs();
}
