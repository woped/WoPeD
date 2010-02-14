package org.woped.qualanalysis.service;

import java.util.Iterator;
import java.util.List;

import org.woped.core.model.AbstractElementModel;

public interface ISComponent {
	/**
	 * 
	 * @return the number of sComponents
	 */
	public int getNumSComponents();

	/**
	 * 
	 * @return an iterator for all sComponents
	 */
	public Iterator<List<AbstractElementModel>> getSComponentsIterator();

	/**
	 * 
	 * @return the number of not-SCovered
	 */
	public int getNumNotSCovered();

	/**
	 * 
	 * @return an iterator of all not-SCovered
	 */
	public Iterator<AbstractElementModel> getNotSCoveredIterator();
}
