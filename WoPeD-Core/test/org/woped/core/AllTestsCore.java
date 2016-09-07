package org.woped.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.model.AllTestsCoreModel;
import org.woped.core.model.petrinet.AllTestsCoreModelPetrinet;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsCoreModel.class, AllTestsCoreModelPetrinet.class})
public class AllTestsCore {
}
