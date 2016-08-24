package org.woped.core.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.woped.core.Constants;
import org.woped.core.utilities.ILogger;
import org.woped.core.utilities.LoggerManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModelElementFactoryTest {
    @Test
    public void createArcModel_validId_createsInstance() throws Exception {

        ModelElementFactory sut = new ModelElementFactory();

        ArcModel arc1 = ModelElementFactory.createArcModel("arc1", null, null);

        assertNotNull(arc1);

    }

    @Test
    public void createArcModel_noId_returnsNull(){
        ModelElementFactory sut = new ModelElementFactory();


        ArcModel expected = null;
        ArcModel actual = ModelElementFactory.createArcModel(null, null, null);


        assertEquals(expected, actual);
    }

    @Test
    public void createArcModel_noId_logsError(){
        ModelElementFactory sut = new ModelElementFactory();

        ILogger logger = mock(ILogger.class);
        LoggerManager.register(logger, Constants.CORE_LOGGER);

        ModelElementFactory.createArcModel(null,null,null);

        verify(logger).error("ID must be set");
    }

    @Before
    public void setup(){
        LoggerManager.resetForTesting();
    }

    @After
    public void tearDown(){
        LoggerManager.resetForTesting();
    }

}