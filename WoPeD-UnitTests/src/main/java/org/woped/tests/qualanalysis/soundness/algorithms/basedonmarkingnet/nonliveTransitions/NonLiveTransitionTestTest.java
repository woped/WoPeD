package org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.NonLiveTransitionTest;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.MNetMock;

public class NonLiveTransitionTestTest {
	private IMarkingNet mNet;
	private NonLiveTransitionTest nonLiveTransition;

	@Before
	public void setUp() throws Exception {
		mNet = new MNetMock();
		nonLiveTransition = new NonLiveTransitionTest(mNet);
	}

	@Test
	public void testGetNonLiveTransitions() {
		Set<TransitionNode> actual = new HashSet<TransitionNode>();
		actual = nonLiveTransition.getNonLiveTransitions();
		
		Assert.assertTrue(actual.isEmpty());
	}

}
