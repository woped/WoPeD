package org.woped.tests.qualanalysis.soundness.algorithms.generic.scc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;


import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjan;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.MNetMock;

public class StronglyConnectedComponentTestGenTarjanTest {
	private MNetMock mNetMock;
	private StronglyConnectedComponentTestGenTarjan<IMarking> stronglyConnectedComponentTestGenTarjan;

	@Before
	public void setUp() throws Exception {
		mNetMock = new MNetMock();
		stronglyConnectedComponentTestGenTarjan = new StronglyConnectedComponentTestGenTarjan<>(mNetMock);
	}

	@Test
	public void testGetStronglyConnectedComponents() {
		Set<Set<IMarking>> actual = stronglyConnectedComponentTestGenTarjan.getStronglyConnectedComponents();

		assertFalse(actual.isEmpty());
	}

	@Test
	public void testIsStronglyConnected() {
		assertTrue(stronglyConnectedComponentTestGenTarjan.isStronglyConnected());
	}

}
