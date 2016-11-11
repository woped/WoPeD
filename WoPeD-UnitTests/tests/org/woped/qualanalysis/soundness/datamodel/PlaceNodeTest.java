package org.woped.qualanalysis.soundness.datamodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.tests.LowLevelPetriNetGenerator;

import static junit.framework.Assert.assertEquals;

public class PlaceNodeTest {
    private PlaceNode cut;
    private LowLevelPetriNetGenerator netGenerator;

    @Before
    public void setUp() throws Exception {
        cut = new PlaceNode(0, 0, "cut", "Class under test", "cut");
        netGenerator = new LowLevelPetriNetGenerator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void toString_defaultPlace_returnsName() throws Exception {
        String expected = cut.getName();
        String actual = cut.toString();

        assertEquals(expected, actual);
    }


}