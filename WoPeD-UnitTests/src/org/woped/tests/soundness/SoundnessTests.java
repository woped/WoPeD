package org.woped.tests.soundness;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.tests.editor.TestEditorEventProcessor;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.SourceSinkTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.DeadTransitionTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.NonLiveTransitionTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.UnboundPlacesTestTest;
import org.woped.tests.qualanalysis.soundness.algorithms.generic.cc.ConnectedComponentTestGenTest;
import org.woped.tests.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjanTest;
import org.woped.tests.translationTests.TestMessages;


@RunWith(Suite.class)
@Suite.SuiteClasses( { SourceSinkTestTest.class, DeadTransitionTestTest.class, NonLiveTransitionTestTest.class, UnboundPlacesTestTest.class, ConnectedComponentTestGenTest.class, StronglyConnectedComponentTestGenTarjanTest.class })

public class SoundnessTests {

	public static Test suite () {
		TestSuite suite= new TestSuite("Test for the whole soundess stuff");
		//$JUnit-BEGIN$
		
		//$JUnit-END$
		return suite;
	}
}