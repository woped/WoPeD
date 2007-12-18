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
		HashMap<Integer, Marking> markings = dataSource.getMarkings();
		HashMap<Integer, TransitionObject> transactions = dataSource.getTransactions();
		//DefaultGraphCell[] cells = new DefaultGraphCell[markings.size() + transactions.size()];
		// Cells
		HashSet<DefaultGraphCell> cellsList = new HashSet<DefaultGraphCell>();
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName());
		
		
		// Markings
		/*Collection<Marking> markingsColl = markings.values();
		Iterator<Marking> iterMarkings = markingsColl.iterator();
		while(iterMarkings.hasNext()){
			Marking actualMarking = iterMarkings.next();
			cellsList.add(new DefaultGraphCell(actualMarking));
		}*/
		
		// Transactions
		Collection<TransitionObject> transactionsColl = transactions.values();
		Iterator<TransitionObject> iterTransactions = transactionsColl.iterator();
		//cellsList.iterator()
		while(iterTransactions.hasNext()){
			TransitionObject actualTransition = iterTransactions.next();
			
			DefaultEdge edge = new DefaultEdge(actualTransition);
			GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
			// (x,y,w,h)
			DefaultGraphCell src = getCell(cellsList, actualTransition.start);
			GraphConstants.setBounds(src.getAttributes(), new Rectangle2D.Double(140,140,80,20));
			GraphConstants.setGradientColor(src.getAttributes(), Color.red);
			GraphConstants.setOpaque(src.getAttributes(), true);
			
			DefaultGraphCell tar = getCell(cellsList, actualTransition.ende);
			GraphConstants.setBounds(tar.getAttributes(), new Rectangle2D.Double(140,140,80,20));
			GraphConstants.setGradientColor(tar.getAttributes(), Color.red);
			GraphConstants.setOpaque(tar.getAttributes(), true);
			
			edge.setSource(src);
			edge.setTarget(tar);
			cellsList.add(src);
			cellsList.add(tar);
			cellsList.add(edge);
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- simpleGraphBuilder() " + this.getClass().getName() + " Size " + cellsList.size());
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		return graph;
	}
	
	private DefaultGraphCell getCell(HashSet<DefaultGraphCell> cellsList, Marking marking){
		Iterator<DefaultGraphCell> iter = cellsList.iterator();
		while(iter.hasNext()){
			DefaultGraphCell comp = iter.next();
			if(comp.getUserObject() != null && comp.getUserObject().equals(marking)){
				return comp;
			}
		}
		return new DefaultGraphCell(marking);
	}

}
