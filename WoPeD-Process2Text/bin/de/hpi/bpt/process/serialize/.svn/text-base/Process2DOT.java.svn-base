package de.hpi.bpt.process.serialize;

import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Gateway;
import de.hpi.bpt.process.GatewayType;
import de.hpi.bpt.process.Process;
import de.hpi.bpt.process.Task;

public class Process2DOT {
	
	/**
	 * Convert process to GraphViz DOT string
	 * @param process A process to serialize
	 * @return DOT serialization string of the process
	 */
	public static String convert(Process process) {
		String result = "";
		if (process == null) return result;
		
		result += "digraph G {\n";
		for (Task t : process.getTasks())
			result += String.format("  n%s[shape=box,label=\"%s\"];\n", t.getId().replace("-", ""), t.getName());
		result+="\n";
		for (Gateway g : process.getGateways(GatewayType.AND))
			result += String.format("  n%s[shape=diamond,label=\"%s\"];\n", g.getId().replace("-", ""), "AND");
		for (Gateway g : process.getGateways(GatewayType.XOR))
			result += String.format("  n%s[shape=diamond,label=\"%s\"];\n", g.getId().replace("-", ""), "XOR");
		for (Gateway g : process.getGateways(GatewayType.OR))
			result += String.format("  n%s[shape=diamond,label=\"%s\"];\n", g.getId().replace("-", ""), "OR");
		for (Gateway g : process.getGateways(GatewayType.UNDEFINED))
			result += String.format("  n%s[shape=diamond,label=\"%s\"];\n", g.getId().replace("-", ""), "?");
		result+="\n";
		for (ControlFlow cf: process.getControlFlow()) {
			if (cf.getLabel()!=null || cf.getLabel()!="")
				result += String.format("  n%s->n%s[label=\"%s\"];\n", cf.getSource().getId().replace("-", ""), cf.getTarget().getId().replace("-", ""), cf.getLabel());
			else
				result += String.format("  n%s->n%s;\n", cf.getSource().getId().replace("-", ""), cf.getTarget().getId().replace("-", ""));
		}
		result += "}";
		
		return result;
	}
}
