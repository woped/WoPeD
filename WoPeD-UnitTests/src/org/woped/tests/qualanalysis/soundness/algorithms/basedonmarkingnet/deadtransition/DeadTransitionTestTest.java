package org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.DeadTransitionTest;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.MNetMock;

public class DeadTransitionTestTest {
	private IMarkingNet mNet;
	private DeadTransitionTest deadTransition;

	@Before
	public void setUp() throws Exception {
		mNet = new MNetMock();
		deadTransition = new DeadTransitionTest(mNet);
	}
	
	@Test
	public void testGetSwitchableTransitions() {
		TransitionNode[] transitions = new TransitionNode[4];
		Set<TransitionNode> expected = new HashSet<TransitionNode>();
		
//		Create expected values
		transitions[0] = new TransitionNode("t3", "t3", "t3");
		transitions[1] = new TransitionNode("t2", "t2", "t2");
		transitions[2] = new TransitionNode("t1", "t1", "t1");
		transitions[3] = new TransitionNode("t*", "t*", "t*");
		for(int i = 0; i < transitions.length; i++)
			expected.add(transitions[i]);
		
//		Run test
		Set<TransitionNode> actual = deadTransition.getSwitchableTransitions();
		
		Assert.assertTrue(expected.equals(actual));
	}

	@Test
	public void testGetDeadTransitions() {
		Set<TransitionNode> actual = deadTransition.getDeadTransitions();
		
		Assert.assertTrue(actual.isEmpty());
	}
}
