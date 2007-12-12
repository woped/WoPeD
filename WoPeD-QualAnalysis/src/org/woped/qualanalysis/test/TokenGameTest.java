package org.woped.qualanalysis.test;

import java.util.Random;

public class TokenGameTest {
   
	
	public static String[] createTestdata()
	{
		
		Random RandomNumbers = new Random();
		Random RandomRange   = new Random();
		
		int range = RandomRange.nextInt(10);
		int numbers = 0;
		String[] TestItems = new String[range];
		
		for(int i = 0; i < range; i++)
		{
			numbers = RandomNumbers.nextInt(2000);
			TestItems[i] = ""+numbers;	
		}
		
		return TestItems;
	}
}
