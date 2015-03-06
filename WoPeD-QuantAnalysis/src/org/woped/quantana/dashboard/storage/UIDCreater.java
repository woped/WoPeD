package org.woped.quantana.dashboard.storage;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.woped.config.general.WoPeDGeneralConfiguration;

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
	
	public static String CreateComment(){
		//String sdf = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		WoPeDGeneralConfiguration wgc = new WoPeDGeneralConfiguration();
		String strComment = "";
		Date d = new Date();

		wgc.readConfig();
		String strLocale = wgc.getLocaleLanguage();
		
		if(strLocale.equals("de") == true){
			strComment = 	"Simulation vom " + new SimpleDateFormat("dd").format(d) + "." 
					 + new SimpleDateFormat("MM").format(d) + " um " 
					 + new SimpleDateFormat("HH").format(d) + ":" 
					 + new SimpleDateFormat("mm").format(d) + ":" 
					 + new SimpleDateFormat("ss").format(d);
		}
		else{
			strComment = 	"Simulation of " + new SimpleDateFormat("dd").format(d) + "." 
					 + new SimpleDateFormat("MM").format(d) + " at " 
					 + new SimpleDateFormat("hh").format(d) + ":" 
					 + new SimpleDateFormat("mm").format(d) + ":" 
					 + new SimpleDateFormat("ss").format(d) +  " "
					 + new SimpleDateFormat("a").format(d);
					 
			
		}
		
	  	return strComment;  
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
