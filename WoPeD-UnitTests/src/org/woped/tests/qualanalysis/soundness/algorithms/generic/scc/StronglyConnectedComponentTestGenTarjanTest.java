package org.woped.tests.qualanalysis.soundness.algorithms.generic.scc;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjan;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.MNetMock;

public class StronglyConnectedComponentTestGenTarjanTest {
	private MNetMock mNetMock;
	private StronglyConnectedComponentTestGenTarjan<Marking> stronglyConnectedComponentTestGenTarjan;

	@Before
	public void setUp() throws Exception {
		mNetMock = new MNetMock();
		stronglyConnectedComponentTestGenTarjan = new StronglyConnectedComponentTestGenTarjan<Marking>(mNetMock);
	}

	@Test
	public void testGetStronglyConnectedComponents() {
		Set<Set<Marking>> actual = stronglyConnectedComponentTestGenTarjan.getStronglyConnectedComponents();
		
		Assert.assertFalse(actual.isEmpty());
		}

	@Test
	public void testIsStronglyConnected() {
		Assert.assertTrue(stronglyConnectedComponentTestGenTarjan.isStronglyConnected());
	}

}
