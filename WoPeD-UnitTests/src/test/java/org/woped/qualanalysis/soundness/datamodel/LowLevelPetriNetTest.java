package org.woped.qualanalysis.soundness.datamodel;

import org.junit.Test;

import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("UnnecessaryLocalVariable")
public class LowLevelPetriNetTest {

    @Test
    public void getPlaces_placesAddedInDifferentOrder_sameResult() throws Exception {
        LowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(0,0,"p123", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0,0,"p2754", "p2", "p2");

        net.addNode(p1);
        net.addNode(p2);

        SortedSet<PlaceNode> places = net.getPlaces();
        PlaceNode actual = places.first();
        PlaceNode expected = p1;

        assertEquals(expected, actual);

        net = new LowLevelPetriNet();
        net.addNode(p2);
        net.addNode(p1);

        places = net.getPlaces();
        actual = places.first();

        assertEquals(expected, actual);
    }
}