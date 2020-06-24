package org.woped.tests.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.UnboundPlacesTest;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.tests.qualanalysis.soundness.algorithms.testing.MNetMock;

public class UnboundPlacesTestTest {
	private IMarkingNet mNet;
	private UnboundPlacesTest unboundPlaces;

	@Before
	public void setUp() throws Exception {
		mNet = new MNetMock();
		unboundPlaces = new UnboundPlacesTest(mNet);
	}

	@Test
	public void testGetUnboundedPlaces() {
		Set<PlaceNode> actual = new HashSet<PlaceNode>();
		actual = unboundPlaces.getUnboundedPlaces();
		
		Assert.assertTrue(actual.isEmpty());
	}

}
