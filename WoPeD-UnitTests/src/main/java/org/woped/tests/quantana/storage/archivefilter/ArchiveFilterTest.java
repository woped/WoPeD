package org.woped.tests.quantana.storage.archivefilter;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.woped.quantana.dashboard.storage.ArchiveFilter;

public class ArchiveFilterTest {

	@Before
	public void setUp() throws Exception {
	}

	

	@Test
	public void test() {
		
		ArchiveFilter f = new ArchiveFilter();

		assertNull(f.Filter(0, 0));
		
		assertNull(f.Filter(-5, -5));

		
		assertArrayEquals(new int[]{0,9}, f.Filter(10, 2));
		
		assertArrayEquals(new int[]{0,1,2,3,4,5}, f.Filter(6, 6));
		
		assertArrayEquals(new int[]{0,3,6,9}, f.Filter(10, 4));
		
		assertArrayEquals(new int[]{0,4,9}, f.Filter(10, 3));
		
		assertArrayEquals(new int[]{0,3,6,9,14}, f.Filter(15, 5));
		
		assertArrayEquals(new int[]{0,4,8,12,19}, f.Filter(20, 5));
		
		assertArrayEquals(new int[]{0,2,4,6,8,10,12,14,16,19}, f.Filter(20, 10));
		
		assertArrayEquals(new int[]{0,9,19}, f.Filter(20, 3));
		
		assertArrayEquals(new int[]{0,6,12,19}, f.Filter(20, 4));
		

		
		assertArrayEquals(new int[]{0,1,2,3,4}, f.Filter(5, 10));
	}


}
