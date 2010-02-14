package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * indicates all unbounded places.
 * 
 * @see IUnboundedPlacesTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class UnboundPlacesTest extends AbstractMarkingNetTest implements IUnboundedPlacesTest {

	/**
	 * 
	 * @param mNet MarkingNet the algorithm is based on
	 */
    public UnboundPlacesTest(MarkingNet mNet) {
        super(mNet);
    }

    /**
     * indicates all unbounded places. costs: count of markings * count of places
     * @see IUnboundedPlacesTest#getUnboundedPlaces()
     */
    @Override
    public Set<PlaceNode> getUnboundedPlaces() {
        Set<PlaceNode> unlimitedPlaces = new HashSet<PlaceNode>();
        Boolean[] unlimitedPlacesArr;

        for (Marking marking : mNet.getMarkings()) {
            unlimitedPlacesArr = marking.getPlaceUnlimited();

            for (int i = 0; i < unlimitedPlacesArr.length; i++) {
                if (unlimitedPlacesArr[i]) {
                    unlimitedPlaces.add(mNet.getPlaces()[i]);
                }
            }
        }
        return unlimitedPlaces;
    }

}
