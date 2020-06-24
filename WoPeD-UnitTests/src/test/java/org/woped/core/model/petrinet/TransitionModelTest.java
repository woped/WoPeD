package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransitionModelTest {

    /**
     * The system under test in this test class.
     */
    private TransitionModel sut;

    @Before
    public void setUp() throws Exception {
        sut = new TransitionModel(CreationMap.createMap());
        sut.setId("sut");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getNumIncomingActivePlaces_noRootOwningContainer_returnsZero() throws Exception {

        int expected = 0;
        int actual = sut.getNumIncomingActivePlaces();

        assertEquals(expected, actual);
    }

    @Test
    public void getNumIncomingActivePlaces_3Places2Active_returns2() throws Exception {

        sut.addOwningContainer(createSimpleDemoData());

        int expected = 2;
        int actual = sut.getNumIncomingActivePlaces();

        assertEquals(expected, actual);
    }

    @Test
    public void getNumIncomingActivePlaces_arc1HasWeightGreaterThenTokens_returns1() throws Exception {

        ModelElementContainer container = createSimpleDemoData();
        sut.addOwningContainer(container);

        ArcModel a1 = container.findArc("p1", "sut");
        a1.setInscriptionValue(2);

        int expected = 1;
        int actual = sut.getNumIncomingActivePlaces();

        assertEquals(expected, actual);
    }

    @Test
    public void getNumIncomingPlaces_noRootOwningContainer_returnsZero() throws Exception {

        int expected = 0;
        int actual = sut.getNumInputPlaces();

        assertEquals(expected, actual);
    }

    @Test
    public void getNumIncomingPlaces_3Places_returns3() throws Exception {

        sut.addOwningContainer(createSimpleDemoData());

        int expected = 3;
        int actual = sut.getNumInputPlaces();

        assertEquals(expected, actual);
    }


    /**
     * Creates demo data with three incoming arcs, 2 active
     *
     * @return the demo data
     */
    private ModelElementContainer createSimpleDemoData() {

        ModelElementContainer container = new ModelElementContainer();
        container.addElement(sut);

        PlaceModel p1 = mock(PlaceModel.class);
        DefaultPort dp1 = mock(DefaultPort.class);
        when(dp1.getParent()).thenReturn(p1);
        when(p1.getPort()).thenReturn(dp1);
        when(p1.getVirtualTokenCount()).thenReturn(1);
        when(p1.getId()).thenReturn("p1");

        PlaceModel p2 = mock(PlaceModel.class);
        DefaultPort dp2 = mock(DefaultPort.class);
        when(dp2.getParent()).thenReturn(p2);
        when(p2.getPort()).thenReturn(dp2);
        when(p2.getVirtualTokenCount()).thenReturn(0);
        when(p2.getId()).thenReturn("p2");

        PlaceModel p3 = mock(PlaceModel.class);
        DefaultPort dp3 = mock(DefaultPort.class);
        when(dp3.getParent()).thenReturn(p3);
        when(p3.getPort()).thenReturn(dp3);
        when(p3.getVirtualTokenCount()).thenReturn(1);
        when(p3.getId()).thenReturn("p3");

        container.addElement(p1);
        container.addElement(p2);
        container.addElement(p3);

        DefaultPort sutPort = mock(DefaultPort.class);
        when(sutPort.getParent()).thenReturn(sut);

        ArcModel p1t1 = ModelElementFactory.createArcModel("p1t1", dp1, sutPort);
        ArcModel p2t1 = ModelElementFactory.createArcModel("p2t1", dp2, sutPort);
        ArcModel p3t1 = ModelElementFactory.createArcModel("p3t1", dp3, sutPort);

        container.addReference(p1t1);
        container.addReference(p2t1);
        container.addReference(p3t1);

        return container;
    }
}