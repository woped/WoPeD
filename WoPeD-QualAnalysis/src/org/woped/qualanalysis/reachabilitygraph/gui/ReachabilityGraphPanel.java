package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.translations.Messages;

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private IEditor editor = null;

	// Panels
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
		
		rgp_jgraph = this.getDefaultGraph(ReachabilityGraphModel.HIERARCHIC);
		rgp_topPanel = new JScrollPane();
		this.add(rgp_topPanel);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
	
	public IEditor getEditor(){
		return editor;
	}
	
	
	private void setTestText(String text){
		someText.setText(text);
	}
	
	public void refreshGraph(int type){
		this.remove(rgp_topPanel);
		this.add(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(type)));
		this.validate();
	}
	
	private JGraph getDefaultGraph(int type){
		ReachabilityGraphModel builder = new ReachabilityGraphModel(editor);
		return builder.getGraph(type, this.getSize());
	}
	
	public String getGraphInfo(){
		Object[] roots = this.rgp_jgraph.getRoots();
		int vertices = 0;
		int edges = 0;
		for(int i = 0; i < roots.length; i++){
			if(roots[i] instanceof ReachabilityEdgeModel){
				edges++;
			}
			if(roots[i] instanceof ReachabilityPlaceModel){				
				vertices++;
			}
		}
		return Messages.getString("QuanlAna.ReachabilityGraph.Vertices")+ " " + vertices + " " + 
			Messages.getString("QuanlAna.ReachabilityGraph.Edges")+ " " + edges;
	}
}
