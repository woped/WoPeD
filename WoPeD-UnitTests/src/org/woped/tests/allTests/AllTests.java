package org.woped.tests.allTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.tests.soundness.SoundnessTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({SoundnessTests.class, org.woped.core.AllTestsCore.class, org.woped.file.AllTestsFile.class
})

public class AllTests {
}