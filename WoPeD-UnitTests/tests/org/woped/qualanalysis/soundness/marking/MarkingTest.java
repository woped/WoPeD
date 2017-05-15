package org.woped.qualanalysis.soundness.marking;

import org.junit.Test;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.tests.MarkingGenerator;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class MarkingTest {

    @Test
    public void asMultiSetString_NoPlaces_returnsEmptyBrackets() throws Exception {
        Marking cut = new Marking(new int[0], new PlaceNode[0], new boolean[0]);

        String expected = "()";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_twoPlacesWithoutTokens_returnsEmptyBrackets() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "waiting", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "done", "p2");
        Marking cut = new Marking(new int[]{0, 0}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "()";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_onePlaceOneToken_containsOnlyPlaceId() throws Exception {

        PlaceNode place = new PlaceNode(0, 0, "p1", "waiting", "p1");
        Marking cut = new Marking(new int[]{1}, new PlaceNode[]{place}, new boolean[]{false});

        String expected = "( p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_onePlaceTwoTokens_containsTokenCountAndPlaceId() throws Exception {

        PlaceNode place = new PlaceNode(0, 0, "p1", "waiting", "p1");
        Marking cut = new Marking(new int[]{2}, new PlaceNode[]{place}, new boolean[]{false});

        String expected = "( 2p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_twoPlacesWithTokens_returnsExpectedResult() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "waiting", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "done", "p2");
        Marking cut = new Marking(new int[]{2, 3}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "( 3p2 2p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_placesWithoutTokens_thosePlacesAreNotContained() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "waiting", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "done", "p2");
        Marking cut = new Marking(new int[]{2, 0}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "( 2p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_placesWithUnboundTokens_thosePlacesAreContainedWithOmegaTokens() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "waiting", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "done", "p2");
        Marking cut = new Marking(new int[]{2, 2}, new PlaceNode[]{place1, place2}, new boolean[]{false, true});

        String expected = "( \u03C9p2 2p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiSetString_placeWithZeroTokenButUnlimited_placeIsContained() throws Exception {
        PlaceNode place = new PlaceNode(0, 0, "p1", "waiting", "p1");
        Marking cut = new Marking(new int[]{0}, new PlaceNode[]{place}, new boolean[]{true});

        String expected = "( \u03c9p1 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asMultiString_placesWithDifferentIdLength_hasShortLexOrdering() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p10", "waiting", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "done", "p2");
        Marking cut = new Marking(new int[]{1, 1}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "( p2 p10 )";
        String actual = cut.asMultiSetString();

        assertEquals(expected, actual);
    }

    @Test
    public void asTokenVectorString_noPlaces_returnsEmptyBrackets() throws Exception {
        Marking cut = new Marking(new int[0], new PlaceNode[0], new boolean[0]);

        String expected = "()";
        String actual = cut.asTokenVectorString();

        assertEquals(expected, actual);
    }

    @Test
    public void asTokenVectorString_onePlace_containsOneElement() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "Test", "p1");
        Marking cut = new Marking(new int[]{0}, new PlaceNode[]{place1}, new boolean[]{false});

        String expected = "( 0 )";
        String actual = cut.asTokenVectorString();

        assertEquals(expected, actual);
    }

    @Test
    public void asTokenVectorString_twoPlaces_containsTwoElements() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "Test", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "Test2", "p2");
        Marking cut = new Marking(new int[]{1, 2}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "( 1 2 )";
        String actual = cut.asTokenVectorString();

        assertEquals(expected, actual);
    }

    @Test
    public void asTokenVectorString_twoPlaces_outputIsShortLexOrdered() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p10", "Test", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "Test2", "p2");
        Marking cut = new Marking(new int[]{1, 2}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        String expected = "( 1 2 )";
        String actual = cut.asTokenVectorString();

        assertEquals(expected, actual);
    }

    @Test
    public void asTokenVectorString_placeUnbound_outputContainsUnboundSign() throws Exception {

        PlaceNode place1 = new PlaceNode(0, 0, "p1", "Test", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "Test2", "p2");
        Marking cut = new Marking(new int[]{1, 0}, new PlaceNode[]{place1, place2}, new boolean[]{false, true});

        String expected = "( 1 " + Marking.UNBOUND_SIGN + " )";
        String actual = cut.asTokenVectorString();

        assertEquals(expected, actual);
    }

    @Test
    public void getMarking_unorderedPlaceArray_returnsShortLexOrderedMapByPlaceId() throws Exception {
        PlaceNode place1 = new PlaceNode(0, 0, "p10", "Test", "p1");
        PlaceNode place2 = new PlaceNode(0, 0, "p2", "Test2", "p2");
        Marking cut = new Marking(new int[]{1, 2}, new PlaceNode[]{place1, place2}, new boolean[]{false, false});

        TreeMap<String, Integer> marking = cut.getMarking();
        String actual = marking.keySet().iterator().next();
        String expected = "p2";

        assertEquals(expected, actual);
    }

    @Test
    public void lessOrEqual_markingsNotComparable_falseBothDirections() throws Exception {
        IMarking m1 = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
        IMarking m2 = createDemoMarking(new int[]{0, 1, 0}, new boolean[]{false, false, false});

        assertFalse(m1.lessOrEqual(m2));
        assertFalse(m2.lessOrEqual(m1));
    }

    @Test
    public void lessOrEqual_m1IsLess_returnsExpectedResults() throws Exception {
        IMarking m1 = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
        IMarking m2 = createDemoMarking(new int[]{2, 1, 0}, new boolean[]{false, false, false});

        assertTrue(m1.lessOrEqual(m2));
        assertFalse(m2.lessOrEqual(m1));
    }

    @Test
    public void lessOrEqual_m1IsLessButOneUnlimited_returnsExpectedResults() throws Exception {
        IMarking m1 = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{true, false, false});
        IMarking m2 = createDemoMarking(new int[]{2, 1, 0}, new boolean[]{false, false, false});

        assertFalse(m1.lessOrEqual(m2));
        assertFalse(m2.lessOrEqual(m1));
    }

    @Test
    public void lessOrEqual_m1IsLessButAllUnlimited_returnsExpectedResults() throws Exception {
        IMarking m1 = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{true, true, true});
        IMarking m2 = createDemoMarking(new int[]{2, 1, 0}, new boolean[]{false, false, false});

        assertFalse(m1.lessOrEqual(m2));
        assertTrue(m2.lessOrEqual(m1));
    }

    @Test
    public void less_otherIsLess_returnTrue() throws Exception {
        IMarking cut = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
        IMarking other = createDemoMarking(new int[]{2, 0, 0}, new boolean[]{false, false, false});

        assertTrue(cut.less(other));
    }

    @Test
    public void less_otherIsGreater_returnFalse() throws Exception {
        IMarking cut = createDemoMarking(new int[]{2, 0, 0}, new boolean[]{false, false, false});
        IMarking other = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});

        assertFalse(cut.less(other));
    }

    @Test
    public void less_otherIsNotComparable_returnFalse() throws Exception {
        IMarking cut = createDemoMarking(new int[]{0, 1, 0}, new boolean[]{false, false, false});
        IMarking other = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});

        assertFalse(cut.less(other));
    }

    @Test
    public void less_otherIsEqual_returnFalse() throws Exception {
        IMarking cut = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
        assertFalse(cut.less(cut));
    }

    @Test
    public void lessOrEqual_bothUnlimitedDifferentTokens_returnsTrueBothDirections() throws Exception {
        IMarking m1 = createDemoMarking(new int[]{1, 0, 0}, new boolean[]{true, true, true});
        IMarking m2 = createDemoMarking(new int[]{2, 1, 0}, new boolean[]{true, true, true});

        assertTrue(m1.lessOrEqual(m2));
        assertTrue(m2.lessOrEqual(m1));
    }

    private IMarking createDemoMarking(int[] tokens, boolean[] unbound) {
        return new MarkingGenerator().createDemoMarking(tokens, unbound);
    }
}