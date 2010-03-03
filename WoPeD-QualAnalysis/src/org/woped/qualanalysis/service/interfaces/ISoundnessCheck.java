package org.woped.qualanalysis.service.interfaces;

import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;

/**
 * interface for Soundness-parts of qualanalysis servies.
 * all classes which implement the Soundness-methods of a service must implement this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface ISoundnessCheck {
	
	/**
	 * 
	 * @return an iterator for all dead transitions
	 */
	public Iterator<AbstractElementModel> getDeadTransitionsIterator();
	
	/**
	 * 
	 * @return an iterator for all nonLive transitions
	 */
	public Iterator<AbstractElementModel> getNonLiveTransitionsIterator();	
	
	/**
	 * 
	 * @return an iterator for all unbounded places
	 */
	public Iterator<AbstractElementModel> getUnboundedPlacesIterator();	
		
}
