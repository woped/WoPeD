package org.woped.core.model;

import org.jgraph.graph.DefaultPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.ILogger;
import org.woped.core.utilities.LoggerManager;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModelElementContainerTest {

    @Before
    public void setup(){
        LoggerManager.resetForTesting();
    }

    @After
    public void tearDown(){
        LoggerManager.resetForTesting();
    }

    @Test
    public void addReference_missingSource_logsWarning(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");

        TransitionModel target = new TransitionModel(new CreationMap());
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getSourceId()).thenReturn(source.getId());

        sut.addReference(arc);

        verify(logger).warn("Source (ID: fakeSource) doesn't exist");
    }

    @Test
    public void addReference_missingTarget_logsWarning(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");

        ArcModel arc = mock(ArcModel.class);
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);

        verify(logger).warn("Target (ID:fakeTarget) does not exist");
    }

    @Test
    public void addReference_insertValidArc_arcExists(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel expected = mock(ArcModel.class);
        when(expected.getId()).thenReturn("arc1");
        when(expected.getSourceId()).thenReturn(source.getId());
        when(expected.getTargetId()).thenReturn(target.getId());

        sut.addReference(expected);
        ArcModel actual = sut.getArcById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void addReference_insertValidArc_logsMessage(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);

        verify(logger).debug("Reference: " + arc.getId() + " (" + arc.getSourceId() + " -> " + arc.getTargetId() + ") added.");
    }

    @Test
    public void addReference_insertArcTwice_logsWarning(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        sut.addReference(arc);

        verify(logger).debug("Arc already exists!");
    }

    @Test
    public void hasReference_ReferenceExists_returnsTrue(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        assertTrue( sut.hasReference(source.getId(), target.getId()));
    }

    @Test
    public void hasReference_ReferenceNotExists_returnsFalse(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        assertFalse(sut.hasReference(source.getId(), target.getId()));
    }

    @Test
    public void hasReference_arcRemoved_returnsFalse(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        assertTrue(sut.hasReference(source.getId(), target.getId())); // reference must be present

        sut.removeArc(arc.getId());
        assertFalse(sut.hasReference(source.getId(), target.getId()));
    }

    @Test
    public void removeElement_HasReferences_ReferenceRemoved(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        sut.removeElement(target.getId());

        assertFalse(sut.hasReference(source.getId(), target.getId()));
    }

    @Test
    public void removeArc_arcNotExists_logsWarning(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");

        sut.removeArc(arc.getId());

        verify(logger).warn("Arc with ID: " + arc.getId() + " does not exists");
    }

    @Test
    public void removeArc_arcExists_logsMessage(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        sut.removeArc(arc.getId());

        verify(logger).debug("Reference (ID:" + arc.getId() + ") deleted");
}

    @Test
    public void removeArc_validArc_arcRemovedFromArcMap(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        assertTrue(sut.getArcMap().containsKey(arc.getId()));

        sut.removeArc(arc.getId());
        assertFalse(sut.getArcMap().containsKey(arc.getId()));
    }

    @Test
    public void removeArc_validArc_arcRemovedFromSourceReferenceMap(){

        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);
        assertTrue(sut.hasReference(arc.getSourceId(), arc.getTargetId()));

        sut.removeArc(arc.getId());
        assertFalse(sut.hasReference(arc.getSourceId(), arc.getTargetId()));
    }

    @Test
    public void getTargetElements_idNotExists_returnsNull(){
        ModelElementContainer sut = new ModelElementContainer();

        Map<String, AbstractPetriNetElementModel> actual = sut.getTargetElements("notExistingId");

        assertNull(actual);
    }

    @Test
    public void getTargetElements_has2Targets_returnsTargets(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target1 = new TransitionModel(new CreationMap());
        target1.setId("fakeTarget1");
        sut.addElement(target1);

        TransitionModel target2 = new TransitionModel(new CreationMap());
        target2.setId("fakeTarget2");
        sut.addElement(target2);

        TransitionModel target3 = new TransitionModel(new CreationMap());
        target3.setId("fakeTarget3");
        sut.addElement(target3);

        DefaultPort port1 = mock(DefaultPort.class);
        when(port1.getParent()).thenReturn(target1);

        ArcModel arc1 = mock(ArcModel.class);
        when(arc1.getId()).thenReturn("arc1");
        when(arc1.getSourceId()).thenReturn(source.getId());
        when(arc1.getTargetId()).thenReturn(target1.getId());
        when(arc1.getTarget()).thenReturn(port1);

        DefaultPort port2 = mock(DefaultPort.class);
        when(port2.getParent()).thenReturn(target2);

        ArcModel arc2 = mock(ArcModel.class);
        when(arc2.getId()).thenReturn("arc2");
        when(arc2.getSourceId()).thenReturn(source.getId());
        when(arc2.getTargetId()).thenReturn(target2.getId());
        when(arc2.getTarget()).thenReturn(port2);

        sut.addReference(arc1);
        sut.addReference(arc2);

        Map<String, AbstractPetriNetElementModel> result = sut.getTargetElements(source.getId());

        assertTrue(result.containsKey(target1.getId()));
        assertEquals(result.get(target1.getId()), target1);

        assertTrue(result.containsKey(target2.getId()));
        assertEquals(result.get(target2.getId()), target2);

        //noinspection SuspiciousMethodCalls
        assertFalse(result.containsKey(target3));
    }

    @Test
    public void getOutgoingArcs_noArcs_returnsEmptyMap(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        Map<String, Object> outgoingArcs = sut.getOutgoingArcs("notExistingId");

        assertTrue(outgoingArcs.isEmpty());
    }

    @Test
    public void getOutgoingArcs_validSource_returnArcs(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);

        Map<String, Object> outgoingArcs = sut.getOutgoingArcs(source.getId());

        int expected = 1;
        int actual = outgoingArcs.size();

        assertEquals(expected, actual);
    }

    @Test
    public void getOutgoingArcs_validSource_mapDoesNotContainSelfReference(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target = new TransitionModel(new CreationMap());
        target.setId("fakeTarget");
        sut.addElement(target);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getId()).thenReturn("arc1");
        when(arc.getSourceId()).thenReturn(source.getId());
        when(arc.getTargetId()).thenReturn(target.getId());

        sut.addReference(arc);

        Map<String, Object> outgoingArcs = sut.getOutgoingArcs(source.getId());

        assertFalse(outgoingArcs.containsKey(ModelElementContainer.SELF_ID));
    }

    @Test
    public void getIncomingArcs_noArcs_returnsEmptyMap(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        Map<String, ArcModel> incomingArcs = sut.getIncomingArcs("notExistingId");

        assertTrue(incomingArcs.isEmpty());
    }

    @Test
    public void getIncomingArcs_hasValidArc_mapContainsThisArc(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target1 = new TransitionModel(new CreationMap());
        target1.setId("fakeTarget1");
        sut.addElement(target1);

        TransitionModel target2 = new TransitionModel(new CreationMap());
        target2.setId("fakeTarget2");
        sut.addElement(target2);

        ArcModel arc1 = mock(ArcModel.class);
        when(arc1.getId()).thenReturn("arc1");
        when(arc1.getSourceId()).thenReturn(source.getId());
        when(arc1.getTargetId()).thenReturn(target1.getId());

        ArcModel arc2 = mock(ArcModel.class);
        when(arc2.getId()).thenReturn("arc2");
        when(arc2.getSourceId()).thenReturn(source.getId());
        when(arc2.getTargetId()).thenReturn(target2.getId());

        sut.addReference(arc1);
        sut.addReference(arc2);

        Map<String, ArcModel> incomingArcs = sut.getIncomingArcs(target1.getId());

        // Contains only one arc
        assertEquals(incomingArcs.size(), 1);

        // Contains the correct key
        assertTrue(incomingArcs.containsKey(arc1.getId()));

        // Contains the correct element
        assertTrue(incomingArcs.containsValue(arc1));
    }

    @Test
    public void findSourceElements_invalidId_mapIsEmpty(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        Map<String, AbstractPetriNetElementModel> sourceElements = sut.findSourceElements("notExistingId");

        assertTrue(sourceElements.isEmpty());
    }

    @Test
    public void findSourceElements_OneSource_MapContainsOnlyThatSource(){
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target1 = new TransitionModel(new CreationMap());
        target1.setId("fakeTarget1");
        sut.addElement(target1);

        TransitionModel target2 = new TransitionModel(new CreationMap());
        target2.setId("fakeTarget2");
        sut.addElement(target2);

        ArcModel arc1 = mock(ArcModel.class);
        when(arc1.getId()).thenReturn("arc1");
        when(arc1.getSourceId()).thenReturn(source.getId());
        when(arc1.getTargetId()).thenReturn(target1.getId());

        ArcModel arc2 = mock(ArcModel.class);
        when(arc2.getId()).thenReturn("arc2");
        when(arc2.getSourceId()).thenReturn(source.getId());
        when(arc2.getTargetId()).thenReturn(target2.getId());

        sut.addReference(arc1);
        sut.addReference(arc2);

        Map<String, AbstractPetriNetElementModel> sourceElements = sut.findSourceElements(target1.getId());

        // map has only one element
        assertEquals(sourceElements.size(), 1);

        // map contains the correct key
        assertTrue(sourceElements.containsKey(source.getId()));

        // map contains the correct value
        assertTrue(sourceElements.containsValue(source));
    }

    @Test
    public void getElementByType_wantsTransitions_returnCorrectNumber() {
        ModelElementContainer sut = new ModelElementContainer();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        PlaceModel source = new PlaceModel(new CreationMap());
        source.setId("fakeSource");
        sut.addElement(source);

        TransitionModel target1 = new TransitionModel(new CreationMap());
        target1.setId("fakeTarget1");
        sut.addElement(target1);

        TransitionModel target2 = new TransitionModel(new CreationMap());
        target2.setId("fakeTarget2");
        sut.addElement(target2);

        Map<String, AbstractPetriNetElementModel> transitions = sut.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);

        int expected = 2;
        int actual = transitions.size();

        assertEquals(expected, actual);
    }
}