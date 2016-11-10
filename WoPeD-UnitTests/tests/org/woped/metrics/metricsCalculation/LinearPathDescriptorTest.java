package org.woped.metrics.metricsCalculation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;

import java.util.Set;

import static org.junit.Assert.*;
import static org.woped.core.model.petrinet.AbstractPetriNetElementModel.PLACE_TYPE;
import static org.woped.core.model.petrinet.AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE;

public class LinearPathDescriptorTest {

    private LinearPathDescriptor sut;

    @Before
    public void setUp() throws Exception {
        PetriNetModelProcessor net = createTestNet();
        sut = new LinearPathDescriptor(net.getElementContainer().getIdMap(), net.getElementContainer().getArcMap());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test() throws Exception {
        assertNotNull(sut);
    }

    @Test
    public void getHighlightedNodes_newNet_returnsEmptyList() throws Exception {
        Set<String> highlightedNodes = sut.getHighlightedNodes();
        assertTrue(highlightedNodes.isEmpty());
    }

    @Test
    public void getNumberOfCyclicNodes_testNet_returns9() throws Exception {

        int expected = 9;
        int actual = sut.getNumberOfCyclicNodes();

        assertEquals(expected, actual);
    }

    private PetriNetModelProcessor createTestNet() {

        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(PLACE_TYPE);
        for (int i = 1; i <= 5; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(TRANS_SIMPLE_TYPE);
        for (int i = 1; i <= 6; i++) {
            transitionMap.setId("t" + i);
            processor.createElement(transitionMap);
        }

        processor.createArc("p1", "t1");
        processor.createArc("t1", "p2");
        processor.createArc("p2", "t2");
        processor.createArc("t2", "p3");
        processor.createArc("p2", "t3");
        processor.createArc("t3", "p4");
        processor.createArc("p4", "t4");
        processor.createArc("t4", "p1");
        processor.createArc("p4", "t5");
        processor.createArc("t5", "p5");
        processor.createArc("p5", "t6");
        processor.createArc("t6", "p1");

        return processor;
    }
}