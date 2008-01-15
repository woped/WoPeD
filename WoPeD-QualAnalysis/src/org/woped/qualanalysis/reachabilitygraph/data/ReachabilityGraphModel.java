package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
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
	
	public JGraph simpleGraphBuilder(){
		GraphModel model = new DefaultGraphModel(); 
		GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory()); 
		JGraph graph = new JGraph(model, view); 
		BuildReachability dataSource = new BuildReachability(editor);
		HashMap<String, TransitionObject> transactions = dataSource.getTransactions();
		//DefaultGraphCell[] cells = new DefaultGraphCell[markings.size() + transactions.size()];
		// Cells
		HashSet<DefaultGraphCell> cellsList = new HashSet<DefaultGraphCell>();
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName());
		// Transactions
		Collection<TransitionObject> transactionsColl = transactions.values();
		Iterator<TransitionObject> iterTransactions = transactionsColl.iterator();
		while(iterTransactions.hasNext()){
			TransitionObject actualTransition = iterTransactions.next();
			ReachabilityEdgeModel edge = getEdge(cellsList,actualTransition);

			ReachabilityPlaceModel src = getCell(cellsList, actualTransition.start);
			cellsList.add(src);
				
			DefaultGraphCell tar = getCell(cellsList, actualTransition.ende);
			cellsList.add(tar);
				
			edge.setSource(src.getChildAt(0));
			edge.setTarget(tar.getChildAt(0));
			cellsList.add(edge);	
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName() + " Size " + cellsList.size());
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		return graph;
	}
	
	private boolean existsSameDirectionTransition(HashSet<DefaultGraphCell> cellsList, TransitionObject to){
		Iterator<DefaultGraphCell> iterCellsList = cellsList.iterator();
		boolean exists = false;
		if(cellsList != null && !cellsList.isEmpty()){
			while(iterCellsList.hasNext() && !exists){
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
						exists = true;
						LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- existsSameDirectionTransition() "  + this.getClass().getName() + " exists = true !!");
					}	
				}
			}
		}
		return exists;
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
	
	private ReachabilityPlaceModel getCell(HashSet<DefaultGraphCell> cellsList, Marking marking){
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

}
