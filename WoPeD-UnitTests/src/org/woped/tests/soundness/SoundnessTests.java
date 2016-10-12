package org.woped.tests.soundness;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.SourceSinkTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.DeadTransitionTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.NonLiveTransitionTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.UnboundPlacesTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.generic.cc.ConnectedComponentTestGenTest;
import org.woped.tests.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjanTest;
import org.woped.tests.qualanalysis.soundness.marking.MarkingTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SourceSinkTestTest.class, DeadTransitionTestTest.class,
		NonLiveTransitionTestTest.class, UnboundPlacesTestTest.class,
		ConnectedComponentTestGenTest.class,
		StronglyConnectedComponentTestGenTarjanTest.class, MarkingTest.class })

public class SoundnessTests {
}