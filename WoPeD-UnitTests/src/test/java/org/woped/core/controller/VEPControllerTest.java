package org.woped.core.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.Constants;
import org.woped.core.utilities.ILogger;
import org.woped.core.utilities.LoggerManager;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class VEPControllerTest {

    private final int TEST_VIEW_EVENT_TYPE = 99;
    private VEPController cut;

    @Before
    public void setUp() throws Exception {
        LoggerManager.resetForTesting();
        cut = new VEPController();
    }

    @After
    public void tearDown() throws Exception {
        LoggerManager.resetForTesting();
    }

    @Test
    public void getViewEventProcessorForType_NoneRegistered_ReturnsEmptyList() throws Exception {
        Collection<AbstractEventProcessor> eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertTrue(eventProcessors.isEmpty());
    }

    @Test
    public void getViewEventProcessorForType_OneRegistered_containsProcessor() throws Exception {
        AbstractEventProcessor fakeProcessor = mock(AbstractEventProcessor.class);
        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor);

        Collection<AbstractEventProcessor> eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertTrue(eventProcessors.contains(fakeProcessor));
    }

    @Test
    public void getViewEventProcessorForType_TwoRegisteredDifferentEventTypes_ReturnsOne() throws Exception {
        int otherViewEventType = TEST_VIEW_EVENT_TYPE + 1;
        AbstractEventProcessor fakeProcessor1 = mock(AbstractEventProcessor.class);
        AbstractEventProcessor fakeProcessor2 = mock(AbstractEventProcessor.class);
        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor1);
        cut.register(otherViewEventType, fakeProcessor2);

        Collection<AbstractEventProcessor> eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertEquals("There should be only one registered processor for this type", eventProcessors.size(), 1);
        assertTrue("The registered processor for this type is not contained", eventProcessors.contains(fakeProcessor1));
    }

    @Test
    public void getViewEventProcessorForType_TwoRegisteredSameEventType_ReturnsBoth() throws Exception {
        AbstractEventProcessor fakeProcessor1 = mock(AbstractEventProcessor.class);
        AbstractEventProcessor fakeProcessor2 = mock(AbstractEventProcessor.class);
        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor1);
        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor2);

        Collection<AbstractEventProcessor> eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertEquals("There should be two registered processors for this type", eventProcessors.size(), 2);
    }

    @Test
    public void getViewEventProcessorForType_oneRegisteredAndUnregistered_returnsEmptyList() throws Exception {
        AbstractEventProcessor fakeProcessor = mock(AbstractEventProcessor.class);

        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor);
        Collection<AbstractEventProcessor> eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertTrue(eventProcessors.contains(fakeProcessor));

        cut.unregister(TEST_VIEW_EVENT_TYPE, fakeProcessor);
        eventProcessors = cut.getViewEventProcessorsForType(TEST_VIEW_EVENT_TYPE);
        assertTrue(eventProcessors.isEmpty());
    }

    @Test
    public void unregister_processorNotRegistered_logsWarning() throws Exception {
        ILogger fakeLogger = mock(ILogger.class);
        LoggerManager.register(fakeLogger, Constants.CORE_LOGGER);

        AbstractEventProcessor fakeProcessor = mock(AbstractEventProcessor.class);
        cut.unregister(TEST_VIEW_EVENT_TYPE, fakeProcessor);

        verify(fakeLogger).warn("Tried to unregister a AbstractViewEventProcessor which has not registered for this type.");
    }

    @Test
    public void unregister_unregisterRegisteredProcessorTwice_logsWarning() throws Exception {
        ILogger fakeLogger = mock(ILogger.class);
        LoggerManager.register(fakeLogger, Constants.CORE_LOGGER);

        AbstractEventProcessor fakeProcessor = mock(AbstractEventProcessor.class);
        cut.register(TEST_VIEW_EVENT_TYPE, fakeProcessor);
        cut.unregister(TEST_VIEW_EVENT_TYPE, fakeProcessor);
        verify(fakeLogger, never()).warn(anyString());

        cut.unregister(TEST_VIEW_EVENT_TYPE, fakeProcessor);
        verify(fakeLogger).warn("Tried to unregister a AbstractViewEventProcessor which has not registered for " +
                "this type.");
    }
}