package org.woped.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.ILogger;
import org.woped.core.utilities.LoggerManager;
import org.woped.pnml.ArcType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PNMLImportTest {

    private PNMLImport sut;
    private ILogger logger;

    @Before
    public void setUp() throws Exception {

        AbstractApplicationMediator mediator = mock(AbstractApplicationMediator.class);
        sut = new PNMLImport(mediator);

        ConfigurationManager.setConfiguration(ConfigurationManager.getStandardConfiguration());

        logger = mock(ILogger.class);
        LoggerManager.resetForTesting();
        LoggerManager.register(logger, Constants.FILE_LOGGER);
    }

    @After
    public void tearDown() throws Exception {
        ConfigurationManager.setConfiguration(ConfigurationManager.getStandardConfiguration());

        LoggerManager.resetForTesting();
    }

    @Test
    public void importArcs_notToolSpecificWithSimpleTransition_createsArc() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p1");
        arcs[0].setTarget("t1");
        arcs[0].addNewInscription().setText(2);

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(false);
        sut.importArcs(arcs, net.container);

        int expectedArcCount = 1;
        int actualArcCount = net.container.getArcMap().size();
        assertEquals(expectedArcCount, actualArcCount);

        ArcModel arc = net.container.getArcMap().get(arcs[0].getId());
        assertNotNull(arc);

        assertEquals(arcs[0].getId(), arc.getId());
        assertEquals(arcs[0].getSource(), arc.getSourceId());
        assertEquals(arcs[0].getTarget(), arc.getTargetId());
    }

    @Test
    public void importArcs_toolSpecificWithSimpleTransition_createsArc() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p1");
        arcs[0].setTarget("t1");
        arcs[0].addNewInscription().setText(2);

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        int expectedArcCount = 1;
        int actualArcCount = net.container.getArcMap().size();
        assertEquals(expectedArcCount, actualArcCount);

        ArcModel arc = net.container.getArcMap().get(arcs[0].getId());
        assertNotNull(arc);

        assertEquals(arcs[0].getId(), arc.getId());
        assertEquals(arcs[0].getSource(), arc.getSourceId());
        assertEquals(arcs[0].getTarget(), arc.getTargetId());
    }

    @Test
    public void importArcs_toolSpecificWithOperatorAsTarget_createsArc() throws Exception {
        ArcType[] arcs = new ArcType[1];

        int arcWeight = 2;
        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p1");
        arcs[0].setTarget("t2_op_1");
        arcs[0].addNewInscription().setText(arcWeight);

        DemoNet net = new DemoNet();

        CreationMap map = CreationMap.createMap();
        map.setId("t2");
        map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
        map.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionModel t2 = (TransitionModel) ModelElementFactory.createModelElement(map);
        net.container.addElement(t2);

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        int expectedArcCount = 1;
        int actualArcCount = net.container.getArcMap().size();
        assertEquals(expectedArcCount, actualArcCount);

        ArcModel arc = net.container.getArcMap().get(arcs[0].getId());
        assertNotNull(arc);

        // verify outer arc is valid
        assertEquals("Verify arc id", arcs[0].getId(), arc.getId());
        assertEquals("Verify source id", arcs[0].getSource(), arc.getSourceId());
        assertEquals("Verify target id", "t2", arc.getTargetId());
        assertEquals("Verify arc weight", arcWeight, arc.getInscriptionValue());
    }

    @Test
    public void importArcs_toolSpecificWithOperatorAsSource_createsArc() throws Exception {
        ArcType[] arcs = new ArcType[1];

        int arcWeight = 2;
        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("t2_op_1");
        arcs[0].setTarget("p1");
        arcs[0].addNewInscription().setText(arcWeight);

        DemoNet net = new DemoNet();

        CreationMap map = CreationMap.createMap();
        map.setId("t2");
        map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
        map.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionModel t2 = (TransitionModel) ModelElementFactory.createModelElement(map);
        net.container.addElement(t2);

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        int expectedArcCount = 1;
        int actualArcCount = net.container.getArcMap().size();
        assertEquals(expectedArcCount, actualArcCount);

        ArcModel arc = net.container.getArcMap().get(arcs[0].getId());
        assertNotNull(arc);

        // verify outer arc is valid
        assertEquals("Verify arc id", arcs[0].getId(), arc.getId());
        assertEquals("Verify source id", "t2", arc.getSourceId());
        assertEquals("Verify target id", arcs[0].getTarget(), arc.getTargetId());
        assertEquals("Verify arc weight", arcWeight, arc.getInscriptionValue());
    }

    @Test
    public void importArcs_notToolSpecificSourceDoesNotExists_logsWarning() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p12");
        arcs[0].setTarget("t1");

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(false);
        sut.importArcs(arcs, net.container);

        String expectedWarning = "- INVALID ARC (a1): Couldn't resolve source and/or target.";
        assertTrue(sut.warnings.contains(expectedWarning));
    }

    @Test
    public void importArcs_notToolSpecificTargetDoesNotExist_logsWarning() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p1");
        arcs[0].setTarget("t21");

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(false);
        sut.importArcs(arcs, net.container);

        String expectedWarning = "- INVALID ARC (a1): Couldn't resolve source and/or target.";
        assertTrue(sut.warnings.contains(expectedWarning));
    }

    @Test
    public void importArcs_toolSpecificSourceDoesNotExists_logsWarning() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p12#1");
        arcs[0].setTarget("t1");

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        String expectedWarning = "- INVALID ARC (a1): Source is not a valid operator.";
        assertTrue(sut.warnings.contains(expectedWarning));
    }

    @Test
    public void importArcs_toolSpecificTargetDoesNotExists_logsWarning() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p1");
        arcs[0].setTarget("t1#2");

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        String expectedWarning = "- INVALID ARC (a1): Target is not a valid operator.";
        assertTrue(sut.warnings.contains(expectedWarning));
    }

    @Test
    public void importArcs_toolSpecificSourceAndTargetDoesNotExists_logsWarning() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a1");
        arcs[0].setSource("p13");
        arcs[0].setTarget("t12");

        DemoNet net = new DemoNet();

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        String expectedWarning = "- ARC LOST INFORMATION (a1): Exception while importing lesser important " + "information.";
        assertFalse(sut.warnings.contains(expectedWarning));
    }

    @Test
    public void importArcs_toolSpecificSourceAndTargetAreInnerElements_skipsArc() throws Exception {
        ArcType[] arcs = new ArcType[1];

        arcs[0] = ArcType.Factory.newInstance();
        arcs[0].setId("a21");
        arcs[0].setSource("P_CENTER_t2");
        arcs[0].setTarget("t2_op_1");

        DemoNet net = new DemoNet();
        CreationMap map = CreationMap.createMap();
        map.setId("t2");
        map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
        map.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionModel t2 = (TransitionModel) ModelElementFactory.createModelElement(map);
        net.container.addElement(t2);

        ConfigurationManager.getConfiguration().setImportToolspecific(true);
        sut.importArcs(arcs, net.container);

        verify(logger).debug(" ... Arc skipped: (ID:" + arcs[0].getId() + ") " + arcs[0].getSource() + " -> " + arcs[0].getTarget() + " ");
    }

    @Test
    public void getParentId_containsPlaceSeparator_returnsTransitionId() throws Exception {
        String childId = OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE + "t3";
        String expected = "t3";
        String actual = sut.getParentId(childId);

        assertEquals(expected, actual);
    }

    @Test
    public void getParentId_containsTransitionSeparator_returnsTransitionId() throws Exception {
        String childId = "t1" + OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION + "1";
        String expected = "t1";
        String actual = sut.getParentId(childId);

        assertEquals(expected, actual);
    }

    @Test
    public void getParentId_containsOldSeparator_returnsTransitionId() throws Exception {
        String childId = "t1" + OperatorTransitionModel.INNERID_SEPERATOR_OLD + "1";
        String expected = "t1";
        String actual = sut.getParentId(childId);

        assertEquals(expected, actual);
    }

    @Test
    public void getParentId_containsNoSeparator_returnsNull() throws Exception {
        String childId = "t1";
        String actual = sut.getParentId(childId);

        assertNull(actual);
    }


    private class DemoNet {

        ModelElementContainer container;
        PlaceModel p1;
        TransitionModel t1;

        DemoNet() {
            container = new ModelElementContainer();

            CreationMap placeMap = CreationMap.createMap();
            placeMap.setId("p1");
            placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
            p1 = (PlaceModel) ModelElementFactory.createModelElement(placeMap);
            container.addElement(p1);

            CreationMap transitionMap = CreationMap.createMap();
            transitionMap.setId("t1");
            transitionMap.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
            t1 = (TransitionModel) ModelElementFactory.createModelElement(transitionMap);
            container.addElement(t1);
        }
    }

}