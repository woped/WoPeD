package org.woped.tests.qualanalysis.soundness.algorithms.generic.cc;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.generic.cc.ConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.LowLevelPetriNetMock;

public class ConnectedComponentTestGenTest {
	private LowLevelPetriNetMock lowLevelPetriNetMock;
	private ConnectedComponentTestGen<AbstractNode> connectedComponentTestGen;

	@Before
	public void setUp() throws Exception {
		lowLevelPetriNetMock = new LowLevelPetriNetMock();
		connectedComponentTestGen = new ConnectedComponentTestGen<AbstractNode>(lowLevelPetriNetMock);
	}

	@Test
	public void testGetConnectedComponents() {
		Set<Set<AbstractNode>> actual = connectedComponentTestGen.getConnectedComponents();
		
		Assert.assertFalse(actual.isEmpty());
	}

}
