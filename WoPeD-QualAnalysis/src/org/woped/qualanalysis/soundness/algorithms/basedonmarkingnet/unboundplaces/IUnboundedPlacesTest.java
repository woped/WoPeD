package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.PlaceNode;


/**
 * indicates all unbounded places in marking net.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IUnboundedPlacesTest {
    /**
     * 
     * @return set of unbounded places 
     * 
     */
	Set<PlaceNode> getUnboundedPlaces();
}
