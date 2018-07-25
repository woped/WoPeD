package preprocessing;

import java.util.HashMap;

import dataModel.process.Arc;
import dataModel.process.Element;
import dataModel.process.ProcessModel;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Event;
import de.hpi.bpt.process.Gateway;
import de.hpi.bpt.process.GatewayType;
import de.hpi.bpt.process.Node;
import de.hpi.bpt.process.Process;
import de.hpi.bpt.process.Task;

public class FormatConverter {
	private HashMap<Integer, Element> converterMap;
	private int newElems;

	/**
	 * Reconstructs the ProcessModel format from HPI Process Model after Rigid Structuring
	 */
	public ProcessModel transformFromRigidFormat(Process p) {
		ProcessModel pm = new ProcessModel();

		HashMap<String,Integer> idMap = new HashMap<>();
		HashMap<Integer,Element> elemMap = new HashMap<>();
		newElems = 0;

		for (Task t: p.getTasks()) {
			int id = Integer.valueOf(t.getName());
			if (converterMap.containsKey(id)) {
				Element elem = converterMap.get(id);

				if (elem.getClass().toString().endsWith("Activity")) {
					dataModel.process.Activity a = (dataModel.process.Activity) elem;
					pm.addActivity(a);
					idMap.put(t.getId(), a.getId());
					elemMap.put(a.getId(), a);
				}
				if (elem.getClass().toString().endsWith("Event")) {
					dataModel.process.Event e = (dataModel.process.Event) elem;
					pm.addEvent(e);
					idMap.put(t.getId(), e.getId());
					elemMap.put(e.getId(), e);
				}
			} else {
				System.out.println("ERROR: Transformation Problem");
			}
		}

		for (Gateway g: p.getGateways()) {
			if (!g.getName().equals("") && converterMap.containsKey(Integer.valueOf(g.getName()))) {
				int id = Integer.valueOf(g.getName());
				dataModel.process.Gateway gw = (dataModel.process.Gateway) converterMap.get(id);
				pm.addGateway(gw);
			} else {
				if (g.getGatewayType() == GatewayType.XOR) {
					dataModel.process.Gateway gw = new dataModel.process.Gateway(getId(), "", null, null, dataModel.process.GatewayType.XOR);
					pm.addGateway(gw);
					idMap.put(g.getId(), gw.getId());
					elemMap.put(gw.getId(), gw);
				}
				if (g.getGatewayType() == GatewayType.OR) {
					dataModel.process.Gateway gw = new dataModel.process.Gateway(getId(), "", null, null, dataModel.process.GatewayType.OR);
					pm.addGateway(gw);
					idMap.put(g.getId(), gw.getId());
					elemMap.put(gw.getId(), gw);
				}
				if (g.getGatewayType() == GatewayType.AND) {
					dataModel.process.Gateway gw = new dataModel.process.Gateway(getId(), "", null, null, dataModel.process.GatewayType.AND);
					pm.addGateway(gw);
					idMap.put(g.getId(), gw.getId());
					elemMap.put(gw.getId(), gw);
				}
				if (g.getGatewayType() == GatewayType.EVENT) {
					dataModel.process.Gateway gw = new dataModel.process.Gateway(getId(), "", null, null, dataModel.process.GatewayType.EVENT);
					pm.addGateway(gw);
					idMap.put(g.getId(), gw.getId());
					elemMap.put(gw.getId(), gw);
				}
			}
		}

		for (ControlFlow f: p.getControlFlow()) {
			Element source = elemMap.get(idMap.get(f.getSource().getId()));
			Element target = elemMap.get(idMap.get(f.getTarget().getId()));
			dataModel.process.Arc arc = new Arc(getId(), f.getName(), source , target);
			pm.addArc(arc);
		}
		return pm;
	}

	/**
	 * Transforms ProcessModel format to HPI Process Format (writes IDs to labels in order to save the information)
	 */
	public Process transformToRigidFormat(dataModel.process.ProcessModel pm) {
		Process p = new Process();
		converterMap = new HashMap<>();
		HashMap <Integer, Node> elementMap = new HashMap<>();

		// Transform activities
		for (dataModel.process.Activity a: pm.getActivites().values()) {
			Task t = new Task(Integer.toString(a.getId()));
			elementMap.put(a.getId(), t);
			converterMap.put(a.getId(),a);
		}

		// Transform events
		for (dataModel.process.Event e: pm.getEvents().values()) {
			Task et = new Task(Integer.toString(e.getId()));
			elementMap.put(e.getId(), et);
			converterMap.put(e.getId(),e);
		}

		// Transform gateway
		for (dataModel.process.Gateway g: pm.getGateways().values()) {
			if (g.getType() == dataModel.process.GatewayType.XOR) {
				Gateway gt = new Gateway(GatewayType.XOR,Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);

			}
			if (g.getType() == dataModel.process.GatewayType.OR) {
				Gateway gt = new Gateway(GatewayType.OR,Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
			if (g.getType() == dataModel.process.GatewayType.AND) {
				Gateway gt = new Gateway(GatewayType.AND,Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
			if (g.getType() == dataModel.process.GatewayType.EVENT) {
				Gateway gt = new Gateway(GatewayType.EVENT,Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
			converterMap.put(g.getId(),g);
		}

		// Transform arcs
		for (dataModel.process.Arc arc: pm.getArcs().values()) {
			if (arc.getSource() != null) {
				p.addControlFlow(elementMap.get(arc.getSource().getId()), elementMap.get(arc.getTarget().getId()));
			}
		}
		return p;
	}

	/**
	 * Transforms given ProcessModel to HPI format
	 */
	public Process transformToRPSTFormat(dataModel.process.ProcessModel pm) {
		Process p = new Process();
		HashMap <Integer, Node> elementMap = new HashMap<>();

		// Transform activities
		for (dataModel.process.Activity a: pm.getActivites().values()) {
			Task t = new Task(a.getLabel());
			t.setId(Integer.toString(a.getId()));
			elementMap.put(a.getId(), t);
		}

		// Transform events
		for (dataModel.process.Event e: pm.getEvents().values()) {
			Event et = new Event(e.getLabel());
			et.setId(Integer.toString(e.getId()));
			elementMap.put(e.getId(), et);
		}

		// Transform gateway
		for (dataModel.process.Gateway g: pm.getGateways().values()) {
			if (g.getType() == dataModel.process.GatewayType.XOR) {
				Gateway gt = new Gateway(GatewayType.XOR, g.getLabel());
				gt.setId(Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);

			}
			if (g.getType() == dataModel.process.GatewayType.OR) {
				Gateway gt = new Gateway(GatewayType.OR, g.getLabel());
				gt.setId(Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
			if (g.getType() == dataModel.process.GatewayType.AND) {
				Gateway gt = new Gateway(GatewayType.AND, g.getLabel());
				gt.setId(Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
			if (g.getType() == dataModel.process.GatewayType.EVENT) {
				Gateway gt = new Gateway(GatewayType.EVENT, g.getLabel());
				gt.setId(Integer.toString(g.getId()));
				elementMap.put(g.getId(), gt);
			}
		}

		// Transform arcs
		for (dataModel.process.Arc arc: pm.getArcs().values()) {
			if (arc.getSource() != null) {
				p.addControlFlow(elementMap.get(arc.getSource().getId()), elementMap.get(arc.getTarget().getId()));
			}
		}
		return p;
	}

	/**
	 * Calculates new ID
	 */
	private int getId() {
		int max = -1;
		for (int i: converterMap.keySet()) {
			if (i>max) {
				max = i;
			}
		}
		newElems++;
		return max+newElems;
	}
}