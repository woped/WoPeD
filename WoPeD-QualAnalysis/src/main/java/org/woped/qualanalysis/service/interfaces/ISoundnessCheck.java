package org.woped.qualanalysis.service.interfaces;

import java.util.Set;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

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
	 * @return a set with all dead transitions
	 */
	public Set<AbstractPetriNetElementModel> getDeadTransitions();
	
	/**
	 * 
	 * @return a set with all nonLive transitions
	 */
	public Set<AbstractPetriNetElementModel> getNonLiveTransitions();	
	
	/**
	 * 
	 * @return a set with for all unbounded places
	 */
	public Set<AbstractPetriNetElementModel> getUnboundedPlaces();	
		
}
