package org.woped.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.controller.AllTestsCoreController;
import org.woped.core.model.AllTestsCoreModel;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsCoreController.class, AllTestsCoreModel.class})
public class AllTestsCore {
}
