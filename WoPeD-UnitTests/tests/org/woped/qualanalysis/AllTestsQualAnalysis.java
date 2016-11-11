package org.woped.qualanalysis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.qualanalysis.simulation.AllTestsQualAnalysisSimulation;
import org.woped.qualanalysis.soundness.AllTestsQualAnalysisSoundness;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsQualAnalysisSimulation.class, AllTestsQualAnalysisSoundness.class})
public class AllTestsQualAnalysis {
}
