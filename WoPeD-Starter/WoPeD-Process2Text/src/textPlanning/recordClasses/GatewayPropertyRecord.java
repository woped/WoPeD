package textPlanning.recordClasses;

import java.util.ArrayList;

import textPlanning.PlanningHelper;
import dataModel.process.Arc;
import dataModel.process.ProcessModel;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.graph.algo.rpst.RPSTEdge;
import de.hpi.bpt.graph.algo.rpst.RPSTNode;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Node;

public class GatewayPropertyRecord {
	
	private int outgoingArcs = 0;
	private int maxPathDepth = 0;
	private int maxPathActivityNumber = 0;
	private boolean isGatewayLabeled = false;
	private boolean hasLabeledArcs = false;
	private boolean hasYNArcs = false;
	private RPSTNode<ControlFlow, Node> node;
	private RPST<ControlFlow,Node> rpst;
	private ProcessModel process;
	
	
	public GatewayPropertyRecord(RPSTNode<ControlFlow, Node> node,  RPST<ControlFlow,Node> rpst, ProcessModel process) {
		this.node = node;
		this.rpst = rpst;
		this.process = process;
		setGatewayPropertyRecord();
	}
	
	/**
	 * Evaluates and determines according values for Gateway. 
	 */
	private void setGatewayPropertyRecord() {
		
		// Outgoing arcs
		for (RPSTEdge<ControlFlow, Node> conn: rpst.getEdges()) {
			if (conn.getSource().getId() == node.getId()) {
				outgoingArcs++;
			}
		}
		
		// maxPathDepth / maxPathActivityNumber
		ArrayList<RPSTNode<ControlFlow, Node>> paths = (ArrayList<RPSTNode<ControlFlow, Node>>) rpst.getChildren(node);
		for (RPSTNode<ControlFlow, Node> pnode: paths) {
			int depth = PlanningHelper.getDepth(pnode, rpst);
			int number = rpst.getChildren(pnode).size()-1;
			if (depth > maxPathDepth) {
				maxPathDepth = depth;
			}
			if (number > maxPathActivityNumber) {
				maxPathActivityNumber = number;
			}
		}
		
		// Labeling
		isGatewayLabeled = node.getEntry().getName().equals("") == false;
		hasLabeledArcs = true;
		for (Arc arc: process.getArcs().values()) {
			if (arc.getSource().getId() == Integer.valueOf(node.getEntry().getId())) {
				if (arc.getLabel().equals("") == true) {
					hasLabeledArcs = false;
				}
			}
		}
		
		hasYNArcs = true;
		if (process.getArcs().values().size() == 2) {
			for (Arc arc: process.getArcs().values()) {
				if (arc.getLabel().toLowerCase().equals("yes") && arc.getLabel().toLowerCase().equals("no")) {
					hasLabeledArcs = false;
				}
			}
		}
	}
	
	public int getOutgoingArcs() {
		return outgoingArcs;
	}


	public int getMaxPathDepth() {
		return maxPathDepth;
	}


	public int getMaxPathActivityNumber() {
		return maxPathActivityNumber;
	}


	public boolean isGatewayLabeled() {
		return isGatewayLabeled;
	}


	public boolean hasLabeledArcs() {
		return hasLabeledArcs;
	}


	public boolean hasYNArcs() {
		return hasYNArcs;
	}

}
