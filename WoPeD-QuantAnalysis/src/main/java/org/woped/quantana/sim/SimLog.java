package org.woped.quantana.sim;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SimLog {
	private HashMap<Integer, SimLogTrace> traces;
	private Date baseDate;
	
	public SimLog() {
		this.traces = new HashMap<Integer, SimLogTrace>();
		this.baseDate = new Date();
	}
	
	private SimLogTrace getTrace(int trace) {
		if (traces.containsKey(trace)) 
			return traces.get(trace);
		else 
			return null;
	}
	
	private void addTrace(SimLogTrace trace) {
		if (!traces.containsKey(trace.getCaseID()))
			traces.put(trace.getCaseID(), trace);
	}
	
	public void addEvent(int caseID, SimLogEvent event) {
		SimLogTrace trace = getTrace(caseID);
		if (trace == null) {
			trace = new SimLogTrace(caseID);
			addTrace(trace);
		}
		trace.addEvent(event);
	}
	
	public void clear() {
		this.traces = new HashMap<Integer, SimLogTrace>();
		this.baseDate = new Date();
	}
	
	public void writeXES(PrintWriter p) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		p.print("<?xml version=\"1.0\"?>\n" + 
				"<log xmlns=\"http://www.xes-standard.org/\" xes.version=\"1.0\">\n" + 
				"	<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>\n" + 
				"	<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n" + 
				"	<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n" + 
				"	<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n" + 
				"	<global scope=\"trace\">\n" + 
				"		<string key=\"concept:name\" value=\"UNKNOWN\"/>\n" + 
				"	</global>\n" + 
				"	<global scope=\"event\">\n" + 
				"		<date key=\"time:timestamp\" value=\"1970-01-01T00:00:00.000+00:00\"/>\n" + 
				"		<string key=\"lifecycle:transition\" value=\"UNKNOWN\"/>\n" + 
				"		<string key=\"concept:name\" value=\"UNKNOWN\"/>\n" + 
				"		<string key=\"org:resource\" value=\"UNKNOWN\"/>\n" + 
				"	</global>\n" + 
				"	<classifier name=\"Activity classifier\" keys=\"concept:name lifecycle:transition\"/>\n" + 
				"	<classifier name=\"Resource classifier\" keys=\"org:resource\"/>\n");
		for (Integer traceID : this.traces.keySet()) {
			traces.get(traceID).writeXES(p, this.baseDate, dateFormat);
		}
		p.print("</log>\n");
	}

	public void writeCSV(PrintWriter p) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		p.print("Case ID, Start Timestamp, Complete Timestamp, Activity, Resource\n");
		for (Integer traceID : this.traces.keySet()) {
			traces.get(traceID).writeCSV(p, this.baseDate, dateFormat);
		}
	}

}
