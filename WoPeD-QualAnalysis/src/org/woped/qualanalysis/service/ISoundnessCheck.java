package org.woped.qualanalysis.service;

import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;

public interface ISoundnessCheck {

	/**
	 * 
	 * @return the number of dead transitions
	 */
	public int getNumDeadTransitions();
	
	/**
	 * 
	 * @return an iterator for all dead transitions
	 */
	public Iterator<AbstractElementModel> getDeadTransitionsIterator();
	
	/**
	 * 
	 * @return the number of nonLive transitions
	 */
	public int getNumNonLiveTransitions();
	
	/**
	 * 
	 * @return an iterator for all nonLive transitions
	 */
	public Iterator<AbstractElementModel> getNonLiveTransitionsIterator();	
	
	/**
	 * 
	 * @return the number of unbounded places
	 */
	public int getNumUnboundedPlaces();
	
	/**
	 * 
	 * @return an iterator for all unbounded places
	 */
	public Iterator<AbstractElementModel> getUnboundedPlacesIterator();	
		
}
