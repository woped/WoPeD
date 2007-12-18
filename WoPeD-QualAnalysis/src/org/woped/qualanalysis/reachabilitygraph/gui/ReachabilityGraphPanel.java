package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;

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
	
	public void refreshGraph(){
		rgp_splitPane.setLeftComponent(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph()));
	}
	
	private JGraph getDefaultGraph(){
		ReachabilityGraphModel builder = new ReachabilityGraphModel(editor);
		return builder.simpleGraphBuilder();
	}
}
