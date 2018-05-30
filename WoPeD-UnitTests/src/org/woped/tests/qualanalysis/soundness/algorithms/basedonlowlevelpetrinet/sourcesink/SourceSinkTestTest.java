package org.woped.tests.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.SourceSinkTest;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.LowLevelPetriNetMock;

public class SourceSinkTestTest {
	private ILowLevelPetriNet lowLevelPetriNet;
	private SourceSinkTest sourceSinkTest;

	@Before
	public void setUp() throws Exception {
		lowLevelPetriNet = new LowLevelPetriNetMock();
		sourceSinkTest = new SourceSinkTest(lowLevelPetriNet);
	}

	@Test
	public void testGetSourcePlaces() {
		Set<PlaceNode> expected = new HashSet<PlaceNode>();
		expected.add(new PlaceNode(1, 1, "p1", "p1", "p1"));
		Set<PlaceNode> actual = sourceSinkTest.getSourcePlaces();
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetSinkPlaces() {
		Set<PlaceNode> expected = new HashSet<PlaceNode>();
		expected.add(new PlaceNode(0, 0, "p3", "p3", "p3"));
		Set<PlaceNode> actual = sourceSinkTest.getSinkPlaces();
		
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetSourceTransitions() {
		Set<TransitionNode> actual = sourceSinkTest.getSourceTransitions();
		
		Assert.assertTrue(actual.isEmpty());
	}

	@Test
	public void testGetSinkTransitions() {
		Set<TransitionNode> actual = sourceSinkTest.getSinkTransitions();
		
		Assert.assertTrue(actual.isEmpty());
	}

}
