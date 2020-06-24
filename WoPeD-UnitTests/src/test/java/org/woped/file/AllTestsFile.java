package org.woped.file;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.file.controller.vep.AllTestsFileControllerVep;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsFileControllerVep.class, PNMLExportTest.class, PNMLImportTest.class})
public class AllTestsFile {
}
