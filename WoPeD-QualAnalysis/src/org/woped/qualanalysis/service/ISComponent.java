package org.woped.qualanalysis.service;

import java.util.Iterator;
import java.util.List;

import org.woped.core.model.AbstractElementModel;

/**
 * interface for SComponent-parts of qualanalysis servies.
 * all classes which implement the SComponents-methods of a service must implement this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface ISComponent {

	/**
	 * 
	 * @return an iterator for all sComponents
	 */
	public Iterator<List<AbstractElementModel>> getSComponentsIterator();

	/**
	 * 
	 * @return an iterator of all not-SCovered
	 */
	public Iterator<AbstractElementModel> getNotSCoveredIterator();
}
