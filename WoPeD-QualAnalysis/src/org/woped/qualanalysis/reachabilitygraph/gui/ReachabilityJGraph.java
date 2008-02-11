package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;

public class ReachabilityJGraph extends JGraph {
	
	private static final long serialVersionUID = 5078494841225380858L;
	
	// AttributeMap of Graph
	private HashMap<String, String> graphAttributes = null;

	public ReachabilityJGraph(){
		super();
		initAttributeMap();
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	public ReachabilityJGraph(GraphModel model, GraphLayoutCache view){
		super(model, view);
		initAttributeMap();
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	public String getToolTipText(MouseEvent event) {
		  Object cell = getFirstCellForLocation(event.getX(), event.getY());
		  if (cell instanceof ReachabilityPlaceModel) {
		    return ((ReachabilityPlaceModel) cell).getToolTipText();
		  }
		  if (cell instanceof ReachabilityEdgeModel) {
			    return ((ReachabilityEdgeModel) cell).getToolTipText();
			  }
		  return null;
		}

	public Vector<Object> getAllCellsForLocation(double x, double y) {
		Object cell = getFirstCellForLocation(x, y);
		Object topMostCell = cell;
		Vector<Object> allCells = new Vector<Object>();
		allCells.add(cell);

		for (cell = getNextCellForLocation(cell, x, y); cell != topMostCell; cell = getNextCellForLocation(cell, x, y)) {
			allCells.add(cell);
		}
		return allCells;
	}
	
	private void initAttributeMap(){
		graphAttributes = new HashMap<String, String>();
		graphAttributes.put("reachabilityGraph.place.width","80");
		graphAttributes.put("reachabilityGraph.place.height","20");
		graphAttributes.put("reachabilityGraph.color","true");
		graphAttributes.put("reachabilityGraph.parallel","true");
		graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace","150");
		graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace","25");
	}
	
	public void setAttributeMap(HashMap<String, String> graphAttributes){
		this.graphAttributes = graphAttributes;
	}
	
	public HashMap<String, String> getAttributeMap(){
		return graphAttributes;
	}
}
