package org.woped.qualanalysis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.qualanalysis.coverabilitygraph.AllTestsQualAnalysisCoverabilityGraph;
import org.woped.qualanalysis.simulation.AllTestsQualAnalysisSimulation;
import org.woped.qualanalysis.soundness.AllTestsQualAnalysisSoundness;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsQualAnalysisSimulation.class, AllTestsQualAnalysisSoundness.class, AllTestsQualAnalysisCoverabilityGraph.class})
public class AllTestsQualAnalysis {
}
