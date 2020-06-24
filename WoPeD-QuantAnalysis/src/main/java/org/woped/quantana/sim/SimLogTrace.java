package org.woped.quantana.sim;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class SimLogTrace {
	private int caseID;
	
	private Vector<SimLogEvent> events;
	
	public SimLogTrace(int caseID) {
		this.caseID = caseID;
		events = new Vector<SimLogEvent>();
	}
	
	public void addEvent(SimLogEvent event) {
		events.add(event);
	}
	
	protected int getCaseID() {
		return this.caseID;
	}
	
	public void writeXES(PrintWriter p, Date baseDate, SimpleDateFormat dateFormat) {
		p.print("<trace>\n" +
				"  <string key=\"concept:name\" value=\"" + this.caseID + "\"/>\n");
		for (SimLogEvent event : events) {
			event.writeXES(p, baseDate, dateFormat);
		}
		p.print("</trace>\n");
	}

	public void writeCSV(PrintWriter p, Date baseDate, SimpleDateFormat dateFormat) {
		for (SimLogEvent event : events) {
			event.writeCSV(p, baseDate, dateFormat, this.caseID);
		}
	}

}
