package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

public class ReachabilityGraphCircle {

	public static JGraph layoutGraph(JGraph graph, Dimension dim){
		GraphModel model = graph.getModel();
		LinkedList<Point> coordinates = CircleCoordinates.getCircleCoordinates(dim.width,dim.height,ReachabilityGraphModel.verticeCount(graph));
		// Build Graph
		LinkedList<ReachabilityPlaceModel> places = new LinkedList<ReachabilityPlaceModel>();
		for(int i = 0; i < model.getRootCount(); i++){
			if(model.getRootAt(i) instanceof ReachabilityPlaceModel){
				places.add((ReachabilityPlaceModel) model.getRootAt(i));
			}
		}
		ReachabilityPlaceModel initialPlace = ReachabilityGraphModel.lookupInitialMarking(places);
		if(initialPlace != null){
			GraphConstants.setBackground(initialPlace.getAttributes(), Color.green);
			setPlacesOnCircle(coordinates, places);
		}
		graph.getGraphLayoutCache().reload();
		return graph;
	}
	
	private static void setPlacesOnCircle(LinkedList<Point> coordinates, LinkedList<ReachabilityPlaceModel> places){
		ReachabilityPlaceModel initial = ReachabilityGraphModel.lookupInitialMarking(places);
		Point2D position = coordinates.removeFirst();
		Rectangle2D bounds = GraphConstants.getBounds(initial.getAttributes());
		GraphConstants.setBounds(initial.getAttributes(), new Rectangle2D.Double(position.getX(),position.getY(),bounds.getWidth(),bounds.getHeight()));
		places.remove(initial);
		Iterator<ReachabilityPlaceModel> iterPlaces = places.iterator();
		while(iterPlaces.hasNext()){
			ReachabilityPlaceModel actual = iterPlaces.next();
			bounds = GraphConstants.getBounds(actual.getAttributes());
			position = coordinates.removeFirst();
			GraphConstants.setBounds(actual.getAttributes(), new Rectangle2D.Double(position.getX(),position.getY(),bounds.getWidth(),bounds.getHeight()));
		}
	}
}
