package org.woped.tests.qualanalysis.soundness.algorithms.generic.scc;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjan;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.LowLevelPetriNetMock;

public class StronglyConnectedComponentTestGenTarjanTest {
	private LowLevelPetriNetMock lowLevelPetriNetMock;
	private StronglyConnectedComponentTestGenTarjan stronglyConnectedComponentTestGenTarjan;

	@Before
	public void setUp() throws Exception {
		lowLevelPetriNetMock = new LowLevelPetriNetMock();
		stronglyConnectedComponentTestGenTarjan = new StronglyConnectedComponentTestGenTarjan<AbstractNode>(lowLevelPetriNetMock);
	}

	@Test
	public void testGetStronglyConnectedComponents() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsStronglyConnected() {
		fail("Not yet implemented");
	}

}
