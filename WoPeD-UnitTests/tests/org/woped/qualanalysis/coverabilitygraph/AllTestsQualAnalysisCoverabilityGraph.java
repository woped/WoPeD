package org.woped.qualanalysis.coverabilitygraph;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeTest;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeTextFormatterTest;
import org.woped.qualanalysis.coverabilitygraph.controller.EditorValidatorTest;
import org.woped.qualanalysis.coverabilitygraph.gui.AllTestsQualAnalysisCoverabilityGraphGui;
import org.woped.qualanalysis.coverabilitygraph.model.AllTestsQualAnalysisCoverabilityGraphModel;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AllTestsQualAnalysisCoverabilityGraphGui.class,
        AllTestsQualAnalysisCoverabilityGraphModel.class,
        MpNodeTextFormatterTest.class,
        MpNodeTest.class,
        EditorValidatorTest.class})
public class AllTestsQualAnalysisCoverabilityGraph {
}
