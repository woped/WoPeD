package org.woped.core.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.model.petrinet.AllTestsCoreModelPetrinet;

@RunWith(Suite.class)
@Suite.SuiteClasses({ArcModelTest.class, ModelElementFactoryTest.class, ModelElementContainerTest.class,
        PetriNetModelProcessorTest.class, AllTestsCoreModelPetrinet.class})
public class AllTestsCoreModel {
}
