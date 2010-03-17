package org.woped.qualanalysis.service.interfaces;

import java.util.HashSet;
import java.util.Set;

import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.structure.components.ClusterElement;

/**
 * interface for WellStructuredness-parts of qualanalysis servies.
 * all classes which implement the WellStructuredness-methods of a service must implement this interface
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface IWellStructuredness {
	
	/**
     * 
     * @return a set with all pt-handles
     */
	public Set<Set<AbstractElementModel>> getPTHandles();
	
	/**
     * 
     * @return a set with all tp-handles
     */
	public Set<Set<AbstractElementModel>> getTPHandles();
	
	/**
     * 
     * @return a set with ?? (what is this for?)
     */
	public HashSet<Set<ClusterElement>> getM_handleClusters();

}
