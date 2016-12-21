package org.woped;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.core.AllTestsCore;
import org.woped.editor.AllTestsEditor;
import org.woped.file.AllTestsFile;
import org.woped.gui.AllTestsGui;
import org.woped.metrics.AllTestsMetrics;
import org.woped.qualanalysis.AllTestsQualAnalysis;
import org.woped.tests.soundness.SoundnessTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({SoundnessTests.class,
        AllTestsCore.class,
        AllTestsEditor.class,
        AllTestsFile.class,
        AllTestsGui.class,
        AllTestsMetrics.class,
        AllTestsQualAnalysis.class})

public class AllTests {}