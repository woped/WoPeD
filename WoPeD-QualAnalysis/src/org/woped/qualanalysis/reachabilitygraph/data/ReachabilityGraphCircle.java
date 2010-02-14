package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;

public class ReachabilityGraphCircle {

	private static Map<ReachabilityPlaceModel, AttributeMap> edit;
	
	public static ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, Dimension dim){
		edit = new HashMap<ReachabilityPlaceModel, AttributeMap>();
		GraphModel model = graph.getModel();
		LinkedList<Point> coordinates = CircleCoordinates.getCircleCoordinates(dim.width,dim.height,AbstractReachabilityGraphModel.verticeCount(graph));
		// Build Graph
		LinkedList<ReachabilityPlaceModel> places = new LinkedList<ReachabilityPlaceModel>();
		for(int i = 0; i < model.getRootCount(); i++){
			if(model.getRootAt(i) instanceof ReachabilityPlaceModel){
				places.add((ReachabilityPlaceModel) model.getRootAt(i));
			}
		}
		ReachabilityPlaceModel initialPlace = AbstractReachabilityGraphModel.lookupInitialMarking(places);
		HashMap<String, String> graphAttributes = graph.getAttributeMap();
		if(initialPlace != null){
			int width = 0;
			int height = 0;
			if(graphAttributes.containsKey("reachabilityGraph.place.width") && graphAttributes.containsKey("reachabilityGraph.place.width")){
				width = Integer.parseInt(graphAttributes.get("reachabilityGraph.place.width"));
				height = Integer.parseInt(graphAttributes.get("reachabilityGraph.place.height"));
			}
			Rectangle2D bounds = GraphConstants.getBounds(initialPlace.getAttributes());
			bounds = new Rectangle2D.Double(bounds.getX(),bounds.getY(),width,height);
			GraphConstants.setBounds(initialPlace.getAttributes(), bounds);
			setPlacesOnCircle(coordinates, places);
		}
		graph.getGraphLayoutCache().edit(edit);
		boolean colored = Boolean.parseBoolean(graph.getAttributeMap().get("reachabilityGraph.color"));
		AbstractReachabilityGraphModel.setGrayScale(graph, !colored);
		return graph;
	}
	
	private static void setPlacesOnCircle(LinkedList<Point> coordinates, LinkedList<ReachabilityPlaceModel> places){
		ReachabilityPlaceModel initial = AbstractReachabilityGraphModel.lookupInitialMarking(places);
		Rectangle2D bounds = GraphConstants.getBounds(initial.getAttributes());
		Point2D position = coordinates.removeFirst();
		bounds = new Rectangle2D.Double(position.getX(),position.getY(), bounds.getWidth(), bounds.getHeight());
		GraphConstants.setBounds(initial.getAttributes(), bounds);
		edit.put(initial, initial.getAttributes());
		places.remove(initial);
		Iterator<ReachabilityPlaceModel> iterPlaces = places.iterator();
		while(iterPlaces.hasNext()){
			ReachabilityPlaceModel actual = iterPlaces.next();
			bounds = GraphConstants.getBounds(initial.getAttributes());
			position = coordinates.removeFirst();
			GraphConstants.setBounds(actual.getAttributes(), new Rectangle2D.Double(position.getX(),position.getY(),bounds.getWidth(),bounds.getHeight()));
			edit.put(actual, actual.getAttributes());
		}
	}
}
