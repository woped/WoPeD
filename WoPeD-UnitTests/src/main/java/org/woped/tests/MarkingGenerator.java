package org.woped.tests;

import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.Marking;

/**
 * Provides the funcionallity to generate markings for testing purposes.
 */
public class MarkingGenerator {

    /**
     * Creates the marking ( 1 0 0 ) for the net p1-p3.
     *
     * @return a demo marking used for testing
     */
    public IMarking createDemoMarking(){
        return createDemoMarking(new int[]{1,0,0}, new boolean[]{false, false, false});
    }


    /**
     * Creates a marking for a net with 3 places with the provided properties.
     * <p>
     * The places are p1 - p3.
     *
     * @param tokens the token count of the places (length 3)
     * @param unbound the unbound state of the places (length 3)
     * @return a marking with the provided properties
     */
    public IMarking createDemoMarking(int[] tokens, boolean[] unbound) {
        return new Marking(tokens, getDemoPlaces(), unbound);
    }

    /**
     * Creates 3 places (p1 - p3) for testing purposes
     *
     * @return an array of 3 places
     */
    private PlaceNode[] getDemoPlaces() {
        PlaceNode places[] = new PlaceNode[3];
        places[0] = new PlaceNode(0, 0, "p1", "p1", "p1");
        places[1] = new PlaceNode(0, 0, "p2", "p2", "p2");
        places[2] = new PlaceNode(0, 0, "p3", "p3", "p3");
        return places;
    }
}
