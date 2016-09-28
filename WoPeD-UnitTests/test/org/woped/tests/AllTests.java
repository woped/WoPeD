package org.woped.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.tests.soundness.SoundnessTests;
import org.woped.core.AllTestsCore;
import org.woped.file.AllTestsFile;
import org.woped.metrics.AllTestsMetrics;
import org.woped.qualanalysis.AllTestsQualAnalysis;


@RunWith(Suite.class)
@Suite.SuiteClasses({SoundnessTests.class,
        AllTestsCore.class,
        AllTestsFile.class,
        AllTestsMetrics.class,
        AllTestsQualAnalysis.class})

public class AllTests {
}