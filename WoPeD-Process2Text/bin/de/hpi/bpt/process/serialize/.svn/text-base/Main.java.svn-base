package de.hpi.bpt.process.serialize;

import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Gateway;
import de.hpi.bpt.process.GatewayType;
import de.hpi.bpt.process.Process;
import de.hpi.bpt.process.Task;

public class Main {

	public static void main(String[] args) throws SerializationException {
		Process p = new Process();
		
		Task ti = new Task("i");
		Task to = new Task("o");
		Gateway s = new Gateway(GatewayType.XOR);
		Gateway j = new Gateway(GatewayType.XOR);
		Task ta = new Task("a");
		Task tb = new Task("b");
		
		p.addControlFlow(ti, s);
		ControlFlow cf1 = p.addControlFlow(s, ta);
		ControlFlow cf2 = p.addControlFlow(s, tb);
		cf1.setLabel("x>5");
		cf2.setLabel("x<=5");
		p.addControlFlow(ta, j);
		p.addControlFlow(tb, j);
		p.addControlFlow(j, to);
		
		String json = Process2JSON.convert(p);
		System.out.println(json);
		
		Process p2 = JSON2Process.convert(json);
		System.out.println(p2.countVertices());
		System.out.println(p2.countEdges());
		
		System.out.println("======================");
		
		System.out.println(Process2DOT.convert(p2));
	}

}
