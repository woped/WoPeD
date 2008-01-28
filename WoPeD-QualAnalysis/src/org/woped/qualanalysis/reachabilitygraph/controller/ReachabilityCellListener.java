package org.woped.qualanalysis.reachabilitygraph.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPortModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;

public class ReachabilityCellListener implements MouseListener {

	
	private ReachabilityJGraph graph = null;
	private ReachabilityPlaceModel lastHighlighted = null;
	
	public ReachabilityCellListener(ReachabilityJGraph graph){
		this.graph = graph;
	}
	
	public void mouseClicked(MouseEvent e) {
	       DefaultGraphCell cell = (DefaultGraphCell) graph.getFirstCellForLocation(e.getPoint().x, e.getPoint().y);
	       
           if (cell != null && cell instanceof ReachabilityPlaceModel)
           {
        	   ReachabilityPlaceModel place = (ReachabilityPlaceModel)cell;
               System.out.print(place.getUserObject().toString() + " has ");
               deHighlightEdges();
               highlightEdges(place);
           } else if (cell != null && cell instanceof ReachabilityEdgeModel){
        	   ReachabilityEdgeModel edge = (ReachabilityEdgeModel)cell;
               System.out.println(edge.getUserObject().toString());
               deHighlightEdges();
           } else {
        	   deHighlightEdges();
           }
           e.consume();
	}
	
	public void mouseEntered(MouseEvent e)  {	}
	public void mouseExited(MouseEvent e)   {	}
	public void mousePressed(MouseEvent e)  {	}
	public void mouseReleased(MouseEvent e) {	}
    
	private void deHighlightEdges(){
		if(lastHighlighted != null){
			ReachabilityPortModel port = (ReachabilityPortModel) this.lastHighlighted.getChildAt(0);
			Set<ReachabilityEdgeModel> edges = (Set<ReachabilityEdgeModel>)port.getEdges();
			Iterator<ReachabilityEdgeModel> iterEdges = edges.iterator();
			Map editMap = new HashMap();
			while(iterEdges.hasNext()){
				ReachabilityEdgeModel edge = iterEdges.next();
				if(edge.getSource().equals(port) || edge.getTarget().equals(port)){
					GraphConstants.setLineColor(edge.getAttributes(), Color.black);
					editMap.put(edge, edge.getAttributes());
				}
			}
			this.lastHighlighted = null;
			if(editMap.size() > 0){
				graph.getGraphLayoutCache().edit(editMap, null, null, null);	
			}
		}
	}
	
	private void highlightEdges(ReachabilityPlaceModel place){
		ReachabilityPortModel port = (ReachabilityPortModel) place.getChildAt(0);
		Set<ReachabilityEdgeModel> edges = (Set<ReachabilityEdgeModel>)port.getEdges();
		int in = 0;
		int out = 0;
		Iterator<ReachabilityEdgeModel> iterEdges = edges.iterator();
		Map editMap = new HashMap();
		while(iterEdges.hasNext()){
			ReachabilityEdgeModel edge = iterEdges.next();
			if(edge.getSource().equals(port)){
				GraphConstants.setLineColor(edge.getAttributes(), Color.green);
				editMap.put(edge, edge.getAttributes());
				out++;
			} else if(edge.getTarget().equals(port)){
				GraphConstants.setLineColor(edge.getAttributes(), Color.red);
				editMap.put(edge, edge.getAttributes());
				in++;
			}
		}
		this.lastHighlighted = place;
		if(editMap.size() > 0){
			graph.getGraphLayoutCache().edit(editMap, null, null, null);	
		}
		System.out.println(in + " incoming edges and " + out + " outgoing edges.");
	}
	
}
