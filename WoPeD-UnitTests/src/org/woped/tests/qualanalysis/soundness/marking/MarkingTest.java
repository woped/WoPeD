package org.woped.tests.qualanalysis.soundness.marking;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.Marking;

public class MarkingTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSmallerEquals() {
		PlaceNode places[] = new PlaceNode[3];
		places[0] = new PlaceNode(0, 0, "p1", "p1", "p1");
		places[1] = new PlaceNode(0, 0, "p2", "p2", "p2");
		places[2] = new PlaceNode(0, 0, "p3", "p3", "p3");

		boolean markingLimited[] = { false, false, false };

		{
			// Markings do not have a total order: Neither marking1 <= marking2
			// nor
			// marking2 <= marking1 should hold here.
			int marking1Tokens[] = { 1, 0, 0 };
			Marking marking1 = new Marking(marking1Tokens, places,
					markingLimited);
			int marking2Tokens[] = { 0, 2, 0 };
			Marking marking2 = new Marking(marking2Tokens, places,
					markingLimited);
			assertFalse(marking1.smallerEquals(marking2));
			assertFalse(marking2.smallerEquals(marking1));
		}
		
		{
			// Easy case of two comparable markings: marking1 <= marking2
			int marking1Tokens[] = { 1, 0, 0 };
			Marking marking1 = new Marking(marking1Tokens, places,
					markingLimited);
			int marking2Tokens[] = { 2, 0, 1 };
			Marking marking2 = new Marking(marking2Tokens, places,
					markingLimited);
			assertTrue(marking1.smallerEquals(marking2));
			assertFalse(marking2.smallerEquals(marking1));
		}
		{
			// Comparing markings with unlimited places: marking1 <= marking2
			int marking1Tokens[] = { 1, 0, 1 };
			Marking marking1 = new Marking(marking1Tokens, places,
					markingLimited);
			boolean markingUnlimited[] = { false, false, true };			
			int marking2Tokens[] = { 2, 0, 0 };
			Marking marking2 = new Marking(marking2Tokens, places,
					markingUnlimited);
			assertTrue(marking1.smallerEquals(marking2));
			assertFalse(marking2.smallerEquals(marking1));
		}		
		{
			// Comparing markings with unlimited places: Neither marking1 <= marking2
			// nor marking2 <= marking1 should hold here
			int marking1Tokens[] = { 3, 0, 0 };
			Marking marking1 = new Marking(marking1Tokens, places,
					markingLimited);
			boolean markingUnlimited[] = { false, false, true };			
			int marking2Tokens[] = { 2, 0, 0 };
			Marking marking2 = new Marking(marking2Tokens, places,
					markingUnlimited);
			assertFalse(marking1.smallerEquals(marking2));
			assertFalse(marking2.smallerEquals(marking1));
		}
		{
			// Comparing markings with unlimited places: marking1 <= marking2.
			// Place 3 is unlimited in both markings. The token could for this
			// place should be ignored.
			int marking1Tokens[] = { 1, 0, 1 };
			boolean markingUnlimited[] = { false, false, true };			
			Marking marking1 = new Marking(marking1Tokens, places,
					markingUnlimited);
			int marking2Tokens[] = { 2, 0, 0 };
			Marking marking2 = new Marking(marking2Tokens, places,
					markingUnlimited);
			assertTrue(marking1.smallerEquals(marking2));
			assertFalse(marking2.smallerEquals(marking1));
		}
	}

}
