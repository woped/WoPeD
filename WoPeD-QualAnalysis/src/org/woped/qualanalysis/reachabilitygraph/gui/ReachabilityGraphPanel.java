package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private IEditor editor = null;

	// Panels
	private JSplitPane rgp_splitPane = null;
	private JPanel rgp_bottomPanel = null; // bottom SplitPane
	private JScrollPane rgp_topPanel = null; // top SplitPane
	
	
	// jGraph related
	private JGraph rgp_jgraph = null; // the jGraph
	
	// Labels
	private JLabel someText = null;
	
	
	public ReachabilityGraphPanel(IEditor editor) {
		super();
		this.editor = editor;
		init();
		setTestText(editor.getId() + " " + editor.getName());
	}
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setLayout(new GridLayout(1,1));
		someText = new JLabel();
		
		rgp_bottomPanel = new JPanel();
		rgp_bottomPanel.add(someText);
		
		rgp_jgraph = this.getDefaultGraph();
		rgp_topPanel = new JScrollPane(rgp_jgraph);
		
		rgp_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rgp_topPanel, rgp_bottomPanel);
		rgp_splitPane.setResizeWeight(0.95);
		rgp_splitPane.setOneTouchExpandable(true);

		this.add(rgp_splitPane);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
	
	public IEditor getEditor(){
		return editor;
	}
	
	
	private void setTestText(String text){
		someText.setText(text);
	}
	
	
	private JGraph getDefaultGraph(){
		GraphModel model = new DefaultGraphModel(); 
		GraphLayoutCache view = new GraphLayoutCache(model,	new	DefaultCellViewFactory()); 
		JGraph graph = new JGraph(model, view); 
		DefaultGraphCell[] cells = new DefaultGraphCell[3]; 
		cells[0] = new DefaultGraphCell(new String("Hello")); 
		GraphConstants.setBounds(cells[0].getAttributes(), new 
		Rectangle2D.Double(20,20,40,20)); 
		GraphConstants.setGradientColor(cells[0].getAttributes(), Color.orange); 
		GraphConstants.setOpaque(cells[0].getAttributes(), true); 
		DefaultPort port0 = new DefaultPort(); 
		cells[0].add(port0); 
		cells[1] = new DefaultGraphCell(new String("World")); 
		GraphConstants.setBounds(cells[1].getAttributes(), new 
		Rectangle2D.Double(140,140,40,20)); 
		GraphConstants.setGradientColor(cells[1].getAttributes(), Color.red); 
		GraphConstants.setOpaque(cells[1].getAttributes(), true); 
		DefaultPort port1 = new DefaultPort(); 
		cells[1].add(port1); 
		DefaultEdge edge = new DefaultEdge(); 
		edge.setSource(cells[0].getChildAt(0)); 
		edge.setTarget(cells[1].getChildAt(0)); 
		cells[2] = edge; 
		int arrow = GraphConstants.ARROW_CLASSIC; 
		GraphConstants.setLineEnd(edge.getAttributes(), arrow); GraphConstants.setEndFill(edge.getAttributes(), true);
		graph.getGraphLayoutCache().insert(cells);
		return graph;
	}
	
}
