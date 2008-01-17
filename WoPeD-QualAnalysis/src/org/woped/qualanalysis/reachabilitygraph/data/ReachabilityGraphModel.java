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
import java.util.List;
import java.util.Set;


import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphModel {

	private IEditor editor = null;
	
	public ReachabilityGraphModel(IEditor editor){
		this.editor = editor;
	}
	
	public JGraph hierarchicalGraphBuilder(){
		GraphModel model = new DefaultGraphModel(); 
		GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory()); 
		view.setSelectsAllInsertedCells(false);
		JGraph graph = new JGraph(model, view); 
		// Compute ReachabilityGraph
		BuildReachability dataSource = new BuildReachability(editor);
		HashMap<String, TransitionObject> transactions = dataSource.getTransactions();
		HashMap<String, Marking> markings = dataSource.getMarkings();
		// Cells
		HashSet<DefaultGraphCell> cellsList = new HashSet<DefaultGraphCell>();
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- hierarchicalGraphBuilder() " + this.getClass().getName());
		// Transactions
		Collection<TransitionObject> transactionsColl = transactions.values();
		Iterator<TransitionObject> iterTransactions = transactionsColl.iterator();
		while(iterTransactions.hasNext()){
			TransitionObject actualTransition = iterTransactions.next();
			ReachabilityEdgeModel edge = getHierarchicEdge(cellsList,actualTransition);

			ReachabilityPlaceModel src = getHierarchicPlace(cellsList, actualTransition.start);
			cellsList.add(src);
				
			DefaultGraphCell tar = getHierarchicPlace(cellsList, actualTransition.ende);
			cellsList.add(tar);
				
			edge.setSource(src.getChildAt(0));
			edge.setTarget(tar.getChildAt(0));
			cellsList.add(edge);	
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- hierarchicalGraphBuilder() " + this.getClass().getName());
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		applyHierarchicLayout(graph);	
		graph.getGraphLayoutCache().reload();
		return graph;
	}
	
	private void applyHierarchicLayout(JGraph graph){
		GraphModel model = graph.getModel();
		LinkedList<ReachabilityPlaceModel> markings = new LinkedList<ReachabilityPlaceModel>();
		for(int i = 0; i < model.getRootCount(); i++){
			if(model.getRootAt(i) instanceof ReachabilityPlaceModel){
				markings.add((ReachabilityPlaceModel) model.getRootAt(i));
			}
		}
		ReachabilityPlaceModel initialPlace = lookupInitialMarking(markings);
		if(initialPlace != null){
			GraphConstants.setGradientColor(initialPlace.getAttributes(), Color.green);
			Rectangle2D bounds = GraphConstants.getBounds(initialPlace.getAttributes());

			ReachabilityGraphModel.hierarcher(initialPlace, new Rectangle2D.Double(200, 0, bounds.getWidth(), bounds.getHeight()));	
		}
		System.out.println(markings.size());
	}
	
	private static void hierarcher(ReachabilityPlaceModel place, Rectangle2D bounds){
		if(!place.isSetRecursiveBounds()){
			GraphConstants.setBounds(place.getAttributes(), bounds);
			place.setIsSetRecursiveBounds(true);	
		}
		bounds = GraphConstants.getBounds(place.getAttributes());
		LinkedList<ReachabilityEdgeModel> edges = new LinkedList<ReachabilityEdgeModel>();
		List ports = place.getChildren();
		for(int portIndex = 0; portIndex < ports.size(); portIndex++){
			if(ports.get(portIndex) instanceof ReachabilityPortModel){
				ReachabilityPortModel port = (ReachabilityPortModel) ports.get(portIndex);
				Set<ReachabilityEdgeModel> edgeSet = port.getEdges();
		     	Iterator<ReachabilityEdgeModel> edgeIterator = edgeSet.iterator();
		     	while(edgeIterator.hasNext()){
		     		edges.add(edgeIterator.next());
		     	}
			}
		}
		Iterator<ReachabilityEdgeModel> edgesIter = edges.iterator();
		int edgeCount = 0;
		LinkedList<ReachabilityPlaceModel> childs = new LinkedList<ReachabilityPlaceModel>();
     	while(edgesIter.hasNext()){
     		ReachabilityEdgeModel edge = (ReachabilityEdgeModel) edgesIter.next();
     		ReachabilityPortModel otherPort = getOtherPort(place, edge);
     		// (x,y,w,h) y = hoch/runter x = links/rechts		
			ReachabilityPlaceModel childPlace = (ReachabilityPlaceModel) otherPort.getParent();
			if(!childPlace.isSetRecursiveBounds()){
				int puffer = 0;
				if(edgeCount != 0){
					puffer = 50;
				}
				GraphConstants.setBounds(childPlace.getAttributes(), new Rectangle2D.Double(bounds.getX() + puffer + (bounds.getWidth() * edgeCount++), bounds.getY() + bounds.getHeight() + 100, bounds.getWidth(), bounds.getHeight()));
				childPlace.setIsSetRecursiveBounds(true);
				childs.add(childPlace);
			}	
     	}
     	Iterator<ReachabilityPlaceModel> iterChilds = childs.iterator();
     	while(iterChilds.hasNext()){
     		ReachabilityPlaceModel actualChild = iterChilds.next(); 
     		Rectangle2D childBound = GraphConstants.getBounds(actualChild.getAttributes());
     		hierarcher(actualChild, childBound);
     	}
	}

	private static ReachabilityPortModel getOtherPort(ReachabilityPlaceModel place, ReachabilityEdgeModel edge){
		List ports = place.getChildren();
		for(int portIndex = 0; portIndex < ports.size(); portIndex++){
			if(edge.getSource() == place.getChildAt(portIndex)){
				return (ReachabilityPortModel) edge.getTarget();
			} else if(edge.getTarget() == place.getChildAt(portIndex)){
				return (ReachabilityPortModel) edge.getSource();
			}
		}
		return null;
	}
	
	private ReachabilityPlaceModel lookupInitialMarking(LinkedList<ReachabilityPlaceModel> markings){
		Iterator<ReachabilityPlaceModel> markingsIter = markings.iterator();
		while(markingsIter.hasNext()){
			ReachabilityPlaceModel actPlaceModel = markingsIter.next();
			if(((Marking) actPlaceModel.getUserObject()).isInitial()){
				return actPlaceModel;
			}
		}
		return null;
	}
	
	private ReachabilityPlaceModel getHierarchicPlace(HashSet<DefaultGraphCell> cellsList, Marking marking){
		Iterator<DefaultGraphCell> iter = cellsList.iterator();
		while(iter.hasNext()){
			DefaultGraphCell comp = iter.next();
			if(comp.getUserObject() != null && comp.getUserObject().equals(marking)){
				return (ReachabilityPlaceModel) comp;
			}
		}
		ReachabilityPlaceModel place = new ReachabilityPlaceModel(marking);
		ReachabilityPortModel port = new ReachabilityPortModel();
		place.add(port);
		port.setParent(place);
		return place;
	}
	
	private ReachabilityEdgeModel getHierarchicEdge(HashSet<DefaultGraphCell> cellsList, TransitionObject to){
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
				}
			}
		}
		return new ReachabilityEdgeModel(to);
	}
	
	public JGraph simpleGraphBuilder(Dimension dim){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName());
		
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
			ReachabilityEdgeModel edge = getEdge(cellsList,actualTransition);

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
			GraphConstants.setGradientColor(initialPlace.getAttributes(), Color.green);	
		}
		graph.getGraphLayoutCache().reload();
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName() + " Size " + cellsList.size());
		return graph;
	}
	
	private ReachabilityEdgeModel getEdge(HashSet<DefaultGraphCell> cellsList, TransitionObject to){
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
	
	private ReachabilityPlaceModel getPlace(HashSet<DefaultGraphCell> cellsList, Marking marking, LinkedList<Point> coordinates){
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

}
