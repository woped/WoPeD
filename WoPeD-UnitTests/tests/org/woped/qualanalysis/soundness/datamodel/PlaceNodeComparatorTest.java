package org.woped.qualanalysis.soundness.datamodel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaceNodeComparatorTest {

    private PlaceNodeComparator cut;

    @Before
    public void setUp() throws Exception {
        this.cut = new PlaceNodeComparator();
    }

    @Test
    public void compare_firstNodeLess_returnsLess() throws Exception {

        PlaceNode node1 = new PlaceNode(0,0,"p1", "test1", "p1");
        PlaceNode node2 = new PlaceNode(0,0,"p2", "test2", "p2");

        int expected = -1;
        int actual = cut.compare(node1, node2);

        assertEquals(expected, actual);
    }

    @Test
    public void compare_firstNodeGreater_returnsGreater() throws Exception {

        PlaceNode node1 = new PlaceNode(0,0,"p1", "test1", "p1");
        PlaceNode node2 = new PlaceNode(0,0,"p2", "test2", "p2");

        int expected = 1;
        int actual = cut.compare(node2, node1);

        assertEquals(expected, actual);
    }


    @Test
    public void compare_nodeIdsDifferentLength_returnsShorterIsGreater() throws Exception {

        PlaceNode node1 = new PlaceNode(0,0,"p10", "test10", "p10");
        PlaceNode node2 = new PlaceNode(0,0,"p2", "test2", "p2");

        int expected = -1;
        int actual = cut.compare(node2, node1);

        assertEquals(expected, actual);
    }

    @Test
    public void compare_nodesWithSameName_returnsNodeWithLowerId()throws Exception{

        PlaceNode node1 = new PlaceNode(0,0,"p2", "test", "p2");
        PlaceNode node2 = new PlaceNode(0,0,"p1", "test", "p1");

        int expected = -1;
        int actual = cut.compare(node2, node1);

        assertEquals(expected, actual);
    }
}