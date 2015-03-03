package org.woped.quantana.dashboard.storage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UIDCreater {

	public static String CreateUIDFromDateTime(){
		
	  	//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	  	return sdf;  
	  	/*		String.valueOf(date.getYear()) +
	  			String.valueOf(date.getMonth()) + 
	  			String.valueOf(date.getDay()) + "_"+
	  			String.valueOf(date.getHours()) + 
	  			String.valueOf(date.getMinutes()) +
	  			String.valueOf(date.getSeconds());
	  	*/
	}
	
	public static double round(double d) {
		
		/*
		if (d > 0){
			d -= 0.00005;
		}else if (d > 0){
			d+= 0.00005;
		}
		return ((double)((long)(d*10000.0))/10000);
		*/
		
		 int stellen = 4;
		return Math.round(d * Math.pow(10, stellen)) / Math.pow(10, stellen);
		
		
	}
}
