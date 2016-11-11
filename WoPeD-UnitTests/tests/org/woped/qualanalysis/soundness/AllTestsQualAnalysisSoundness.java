package org.woped.qualanalysis.soundness;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.qualanalysis.soundness.datamodel.AllTestsQualAnalysisSoundnessDatamodel;
import org.woped.qualanalysis.soundness.marking.AllTestsQualAnalysisSoundnessMarking;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsQualAnalysisSoundnessDatamodel.class, AllTestsQualAnalysisSoundnessMarking.class})
public class AllTestsQualAnalysisSoundness {
}
