package org.woped.quantana.sim;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimLogEvent {
	private String activity;
	private String resource;
	private double startTime;
	private double duration;
	
	public SimLogEvent(String activity, String resource, double startTime, double duration) {
		this.activity = activity;
		this.resource = resource;
		this.startTime = startTime;
		this.duration = duration;
	}

	public void writeXES(PrintWriter p, Date baseDate, SimpleDateFormat dateFormat) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.SECOND, (int)(startTime*60));
		Date start = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.SECOND, (int)((startTime+duration)*60));
		Date completion = cal.getTime();

		p.print("  <event>\n" +
				"    <string key=\"lifecycle:transition\" value=\"START\"/>\n" +
				"    <string key=\"org:resource\" value=\"" + this.resource + "\"/>\n" +
				"    <string key=\"concept:name\" value=\"" + this.activity + "\"/>\n" +
				"    <date key=\"time:timestamp\" value=\"" + dateFormat.format(start) + "\"/>\n" +
				"  </event>\n" +
				"  <event>\n" +
				"    <string key=\"lifecycle:transition\" value=\"COMPLETE\"/>\n" +
				"    <string key=\"org:resource\" value=\"" + this.resource + "\"/>\n" +
				"    <string key=\"concept:name\" value=\"" + this.activity + "\"/>\n" +
				"    <date key=\"time:timestamp\" value=\"" + dateFormat.format(completion) + "\"/>\n" +
				"  </event>\n");
	}

	public void writeCSV(PrintWriter p, Date baseDate, SimpleDateFormat dateFormat, int caseID) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.SECOND, (int)(startTime*60));
		Date start = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.SECOND, (int)((startTime+duration)*60));
		Date completion = cal.getTime();

		p.println(String.join(", ", (new Integer(caseID)).toString(), dateFormat.format(start), dateFormat.format(completion), this.activity, this.resource));
	}

}
