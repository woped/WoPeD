package org.woped.qualanalysis.service.interfaces;

import java.util.List;
import java.util.Set;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

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
	 * @return a set with all sComponents
	 */
	public Set<List<AbstractPetriNetElementModel>> getSComponents();

	/**
	 * 
	 * @return a set with all not-SCovered
	 */
	public Set<AbstractPetriNetElementModel> getNotSCovered();
}
