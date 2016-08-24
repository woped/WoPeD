package org.woped.core.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.ILogger;
import org.woped.core.utilities.LoggerManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
}