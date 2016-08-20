package org.woped.tests.allTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.model.ArcModelTest;
import org.woped.tests.soundness.SoundnessTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        SoundnessTests.class,
        ArcModelTest.class
})

public class AllTests {
}