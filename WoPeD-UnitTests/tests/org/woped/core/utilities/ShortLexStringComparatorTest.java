package org.woped.core.utilities;

import org.junit.Before;
import org.junit.Test;
import org.woped.core.utilities.ShortLexStringComparator;

import static org.junit.Assert.assertEquals;

public class ShortLexStringComparatorTest {

    private ShortLexStringComparator cut;

    @Before
    public void setup() {
        cut = new ShortLexStringComparator();
    }

    @Test
    public void comppare_sameIds_returnEqual() throws Exception {
        String id1 = "p1";
        String id2 = "p1";

        int expected = 0;
        int actual = cut.compare(id1, id2);

        assertEquals(expected, actual);

    }

    @Test
    public void compare_firstIdIsLess_returnsLess() throws Exception {
        String id1 = "p1";
        String id2 = "p2";

        int expected = -1;
        int actual = cut.compare(id1, id2);

        assertEquals(expected, actual);
    }

    @Test
    public void compare_firstIdIsGreater_returnsGreater() throws Exception {
        String id1 = "p2";
        String id2 = "p1";

        int expected = 1;
        int actual = cut.compare(id1, id2);

        assertEquals(expected, actual);
    }

    @Test
    public void compare_firstIdIsLessDifferentLength_returnsLess() throws Exception {
        String id1 = "p2";
        String id2 = "p10";

        int expected = -1;
        int actual = cut.compare(id1, id2);

        assertEquals(expected, actual);

    }

    @Test
    public void compare_firstIdIsGreaterDifferentLength_returnsGreater() throws Exception {
        String id1 = "p10";
        String id2 = "p2";

        int expected = 1;
        int actual = cut.compare(id1, id2);

        assertEquals(expected, actual);

    }
}