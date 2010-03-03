package org.woped.qualanalysis.service.interfaces;

import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;

/**
 * interface for Workflow-parts of qualanalysis servies. all classes which implement the Workflow-methods of a service
 * must implement this interface
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IWorkflowCheck {

	/**
	 * 
	 * @return an iterator for all source places
	 */
	public Iterator<AbstractElementModel> getSourcePlacesIterator();

	/**
	 * 
	 * @return an iterator for all sink places
	 */
	public Iterator<AbstractElementModel> getSinkPlacesIterator();

	/**
	 * 
	 * @return an iterator for all transitions with empty preset
	 */
	public Iterator<AbstractElementModel> getSourceTransitionsIterator();

	/**
	 * 
	 * @return an iterator for all transitions with empty postset
	 */
	public Iterator<AbstractElementModel> getSinkTransitionsIterator();

	/**
	 * 
	 * @return an iterator for all nodes with empty preset and empty postset
	 */
	public Iterator<AbstractElementModel> getNotConnectedNodes();
	
	/**
	 * 
	 * @return an iterator for all nodes not being strongly connected
	 */
	public Iterator<AbstractElementModel> getNotStronglyConnectedNodes();
}
