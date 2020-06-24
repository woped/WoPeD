package org.woped.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.controller.AllTestsCoreController;
import org.woped.core.model.AllTestsCoreModel;
import org.woped.core.utilities.AllTestsCoreUtilities;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsCoreController.class, AllTestsCoreModel.class, AllTestsCoreUtilities.class})
public class AllTestsCore {
}
