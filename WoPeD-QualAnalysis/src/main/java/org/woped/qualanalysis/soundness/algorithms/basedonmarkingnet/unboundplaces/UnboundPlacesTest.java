package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

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
	 * @param markingNet MarkingNet the algorithm is based on
	 */
    public UnboundPlacesTest(IMarkingNet markingNet) {
        super(markingNet);
    }

    /**
     * indicates all unbounded places. costs: count of markings * count of places
     * @see IUnboundedPlacesTest#getUnboundedPlaces()
     */
    @Override
    public Set<PlaceNode> getUnboundedPlaces() {
        Set<PlaceNode> unboundedPlaces = new HashSet<>();
        boolean[] unboundedPlacesArr;

        for (IMarking marking : mNet.getMarkings()) {

            for(PlaceNode place: marking.getPlaces()){
                if(marking.isPlaceUnbound(place)) unboundedPlaces.add(place);
            }

//            unboundedPlacesArr = marking.getPlaceUnlimited();
//
//            for (int i = 0; i < unboundedPlacesArr.length; i++) {
//                if (unboundedPlacesArr[i]) {
//                    unboundedPlaces.add(mNet.getPlaces()[i]);
//                }
//            }
        }
        return unboundedPlaces;
    }

}
