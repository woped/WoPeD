package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.controller.IEditor;

public class ReachabilityGraphCircle {

	public static JGraph layoutGraph(IEditor editor, Dimension dim){
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory());
		view.setSelectsAllInsertedCells(false);
		JGraph graph = new JGraph(model, view); 
		BuildReachability dataSource = new BuildReachability(editor);
		HashMap<String, TransitionObject> transactions = dataSource.getTransactions();
		// Initialize PlaceModelSet
		HashSet<DefaultGraphCell> cellsList = new HashSet<DefaultGraphCell>();
		// Transactions
		Collection<TransitionObject> transactionsColl = transactions.values();
		Iterator<TransitionObject> iterTransactions = transactionsColl.iterator();
		//Circles		
		LinkedList<Point> coordinates = CircleCoordinates.getCircleCoordinates(dim.width,dim.height,dataSource.getMarkings().size());
		// Build Graph
		while(iterTransactions.hasNext()){
			TransitionObject actualTransition = iterTransactions.next();
			ReachabilityEdgeModel edge = ReachabilityGraphCircle.getEdge(cellsList,actualTransition);

			ReachabilityPlaceModel src = getPlace(cellsList, actualTransition.start, coordinates);
			cellsList.add(src);
				
			DefaultGraphCell tar = getPlace(cellsList, actualTransition.ende, coordinates);
			cellsList.add(tar);
				
			edge.setSource(src.getChildAt(0));
			edge.setTarget(tar.getChildAt(0));
			cellsList.add(edge);	
		}
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		LinkedList<ReachabilityPlaceModel> places = new LinkedList<ReachabilityPlaceModel>();
		for(int i = 0; i < model.getRootCount(); i++){
			if(model.getRootAt(i) instanceof ReachabilityPlaceModel){
				places.add((ReachabilityPlaceModel) model.getRootAt(i));
			}
		}
		ReachabilityPlaceModel initialPlace = lookupInitialMarking(places);
		if(initialPlace != null){
			GraphConstants.setBackground(initialPlace.getAttributes(), Color.green);	
		}
		graph.getGraphLayoutCache().reload();
		return graph;
	}
	
	private static ReachabilityEdgeModel getEdge(HashSet<DefaultGraphCell> cellsList, TransitionObject to){
		Iterator<DefaultGraphCell> iterCellsList = cellsList.iterator();
		
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
					
					if(to.ende.equals(srcRplm.getUserObject())  && to.start.equals(tarRplm.getUserObject())){
						//TransitionObject actTo = (TransitionObject) edge.getUserObject();
						GraphConstants.setLineBegin(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
						return edge;
					}	
				}
			}
		}
		return new ReachabilityEdgeModel(to);
	}
	
	private static ReachabilityPlaceModel getPlace(HashSet<DefaultGraphCell> cellsList, Marking marking, LinkedList<Point> coordinates){
		Iterator<DefaultGraphCell> iter = cellsList.iterator();
		while(iter.hasNext()){
			DefaultGraphCell comp = iter.next();
			if(comp.getUserObject() != null && comp.getUserObject().equals(marking)){
				return (ReachabilityPlaceModel) comp;
			}
		}
		ReachabilityPlaceModel place = new ReachabilityPlaceModel(marking);
     	// (x,y,w,h)
		Point2D position = coordinates.removeFirst();
		GraphConstants.setBounds(place.getAttributes(), new Rectangle2D.Double(position.getX(),position.getY(),80,20));
		ReachabilityPortModel port = new ReachabilityPortModel();
		place.add(port);
		port.setParent(place);
		return place;
	}
		
	private static ReachabilityPlaceModel lookupInitialMarking(LinkedList<ReachabilityPlaceModel> markings){
			Iterator<ReachabilityPlaceModel> markingsIter = markings.iterator();
			while(markingsIter.hasNext()){
				ReachabilityPlaceModel actPlaceModel = markingsIter.next();
				if(((Marking) actPlaceModel.getUserObject()).isInitial()){
					return actPlaceModel;
				}
			}
			return null;
	}
}
