/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * 
 * This class was written by
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.reachabilitygraph.controller.ParallelRouter;
import org.woped.qualanalysis.reachabilitygraph.controller.ReachabilityCellListener;
import org.woped.qualanalysis.reachabilitygraph.controller.ReachabilityGraphViewFactory;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;

public class ReachabilityGraphModel {

	// Possible layouts
	public static final int HIERARCHIC = 0;
	public static final int CIRCLE = 1;
	
	private IEditor editor = null;
	
	public ReachabilityGraphModel(IEditor editor){
		this.editor = editor;
	}
	
	public ReachabilityJGraph getGraph(){
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,	new ReachabilityGraphViewFactory());
		view.setAutoSizeOnValueChange(true);
		view.setSelectsAllInsertedCells(false);
		ReachabilityJGraph graph = new ReachabilityJGraph(model, view);
		graph.addMouseListener(new ReachabilityCellListener(graph));
		// Compute ReachabilityGraph
		BuildReachability dataSource = new BuildReachability(editor);
		HashMap<String, TransitionObject> transactions = dataSource.getTransactions();
		// Cells
		HashSet<DefaultGraphCell> cellsList = new HashSet<DefaultGraphCell>();
		// Transactions
		Collection<TransitionObject> transactionsColl = transactions.values();
		Iterator<TransitionObject> iterTransactions = transactionsColl.iterator();
		while(iterTransactions.hasNext()){
			TransitionObject actualTransition = iterTransactions.next();
			ReachabilityEdgeModel edge = getEdge(cellsList,actualTransition);

			ReachabilityPlaceModel src = getPlace(cellsList, actualTransition.start);
			cellsList.add(src);
				
			DefaultGraphCell tar = getPlace(cellsList, actualTransition.ende);
			cellsList.add(tar);
			
			edge.setSource(src.getChildAt(0));
			edge.setTarget(tar.getChildAt(0));
	        GraphConstants.setRouting(edge.getAttributes(), ParallelRouter.getSharedInstance(view));
			cellsList.add(edge);	
		}
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		return graph;
	}
	
	public static ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, int type, Dimension dim){		
		switch(type){
			case HIERARCHIC:	return ReachabilityLayoutHierarchic.layoutGraph(graph);
			case CIRCLE:		return ReachabilityGraphCircle.layoutGraph(graph, dim);
			default: 			return null;
		}
	}
	
	private static ReachabilityPlaceModel getPlace(HashSet<DefaultGraphCell> cellsList, Marking marking){
		Iterator<DefaultGraphCell> iter = cellsList.iterator();
		while(iter.hasNext()){
			DefaultGraphCell comp = iter.next();
			if(comp.getUserObject() != null && comp.getUserObject().equals(marking)){
				return (ReachabilityPlaceModel) comp;
			}
		}
		ReachabilityPlaceModel place = new ReachabilityPlaceModel(marking);
     	// (x,y,w,h)
		GraphConstants.setBounds(place.getAttributes(), new Rectangle2D.Double(0,0,80,20));
		ReachabilityPortModel port = new ReachabilityPortModel();
		place.add(port);
		port.setParent(place);
		return place;
	}
	
	private static ReachabilityEdgeModel getEdge(HashSet<DefaultGraphCell> cellsList, TransitionObject to){
		Iterator<DefaultGraphCell> iterCellsList = cellsList.iterator();
		ReachabilityEdgeModel returnEdge = new ReachabilityEdgeModel(to);
		int differ = 1;
		if(cellsList != null && !cellsList.isEmpty()){
			while(iterCellsList.hasNext()){
				DefaultGraphCell cell = iterCellsList.next();
				if(cell instanceof ReachabilityEdgeModel){
					ReachabilityEdgeModel edge = (ReachabilityEdgeModel) cell;
					// Source
					ReachabilityPortModel srcRpom = (ReachabilityPortModel) edge.getSource();
					ReachabilityPlaceModel srcRplm = (ReachabilityPlaceModel) srcRpom.getParent();
					// Target
					ReachabilityPortModel tarRpom = (ReachabilityPortModel) edge.getTarget();
					ReachabilityPlaceModel tarRplm = (ReachabilityPlaceModel) tarRpom.getParent();
					if(to.start == srcRplm.getUserObject() && to.ende == tarRplm.getUserObject()){
						GraphConstants.setLabelPosition(edge.getAttributes(), new Point2D.Double(GraphConstants.PERMILLE*(6-(0.6*differ++))/8, -20));
					}
				}
			}
		}
		return returnEdge;
	}
	
	public static void setParallelRouting(ReachabilityJGraph graph, boolean enabled){
		Object[] nodes =  graph.getRoots();
		HashMap<ReachabilityEdgeModel, AttributeMap> edit = new HashMap<ReachabilityEdgeModel,AttributeMap>();
		for(int i = 0; i < nodes.length; i++){
			if(nodes[i] instanceof ReachabilityEdgeModel){
				ReachabilityEdgeModel edge = (ReachabilityEdgeModel) nodes[i];
				if(enabled){
					GraphConstants.setRouting(edge.getAttributes(), ParallelRouter.getSharedInstance(graph.getGraphLayoutCache()));
				} else {
					GraphConstants.setRouting(edge.getAttributes(), GraphConstants.ROUTING_DEFAULT);	
				}
				edit.put(edge, edge.getAttributes());
			}
		}
		graph.getGraphLayoutCache().edit(edit,null,null,null);
	}
	
	public static void setGrayScale(ReachabilityJGraph graph, boolean enabled){
		Object[] nodes =  graph.getRoots();
		LinkedList<ReachabilityPlaceModel> places = new LinkedList<ReachabilityPlaceModel>();
		HashMap<ReachabilityPlaceModel, AttributeMap> edit = new HashMap<ReachabilityPlaceModel,AttributeMap>();
		for(int i = 0; i < nodes.length; i++){
			if(nodes[i] instanceof ReachabilityPlaceModel){
				ReachabilityPlaceModel place = (ReachabilityPlaceModel) nodes[i];
				places.add(place);
				if(enabled){
					GraphConstants.setBackground(place.getAttributes(), Color.lightGray);
				} else {
					GraphConstants.setBackground(place.getAttributes(), Color.orange);	
				}
				edit.put(place, place.getAttributes());
			}
		}
		if(enabled){
			ReachabilityPlaceModel initial = lookupInitialMarking(places);
			if(initial != null){
				edit.remove(initial);
				GraphConstants.setBackground(initial.getAttributes(), Color.gray);
				edit.put(initial, initial.getAttributes());
			}
		} else {
			ReachabilityPlaceModel initial = lookupInitialMarking(places);
			if(initial != null){
				edit.remove(initial);
				GraphConstants.setBackground(initial.getAttributes(), Color.green);
				edit.put(initial, initial.getAttributes());	
			}
		}
		graph.getGraphLayoutCache().edit(edit,null,null,null);
	}
	
	public static ReachabilityPlaceModel lookupInitialMarking(LinkedList<ReachabilityPlaceModel> markings){
		Iterator<ReachabilityPlaceModel> markingsIter = markings.iterator();
		while(markingsIter.hasNext()){
			ReachabilityPlaceModel actPlaceModel = markingsIter.next();
			if(((Marking) actPlaceModel.getUserObject()).isInitial()){
				return actPlaceModel;
			}
		}
		return null;
	}
	
	public static int edgeCount(JGraph graph){
		Object[] roots = graph.getRoots();
		int edges = 0;
		for(int i = 0; i < roots.length; i++){
			if(roots[i] instanceof ReachabilityEdgeModel){				
				edges++;
			}
		}
		return edges;
	}
	
	public static int verticeCount(JGraph graph){
		Object[] roots = graph.getRoots();
		int vertices = 0;
		for(int i = 0; i < roots.length; i++){
			if(roots[i] instanceof ReachabilityPlaceModel){				
				vertices++;
			}
		}
		return vertices;
	}
}
