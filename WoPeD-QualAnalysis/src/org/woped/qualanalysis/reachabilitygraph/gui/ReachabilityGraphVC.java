package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.qualanalysis.IReachabilityGraph;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.translations.Messages;

public class ReachabilityGraphVC extends JInternalFrame implements IReachabilityGraph {
	
	private HashSet<ReachabilityGraphPanel> panels = new HashSet<ReachabilityGraphPanel>();
	
	private static final long serialVersionUID = 1L;
	private static ReachabilityGraphVC myInstance = null;
	
	private JRadioButton circleButton = null;
	private JRadioButton hierarchicButton= null;
	private JLabel bottomInfo = null;
	private JLabel legendInfo = null;
	
	public static ReachabilityGraphVC getInstance(IEditor editor){
		if(ReachabilityGraphVC.myInstance == null){
			ReachabilityGraphVC.myInstance = new ReachabilityGraphVC();
			myInstance.addEditor(editor);
		} else {
			myInstance.addEditor(editor);
		}
		return myInstance;
	}
	
	private ReachabilityGraphVC() {
		super();
		init();
	}
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(320,240));
		this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(false);
        this.setLayout(new BorderLayout());
        // NORTH Components
        legendInfo = new JLabel();
        legendInfo.setText(Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()");
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        northPanel.add(legendInfo);
        JButton export = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.ExportAsButton")); // TODO: Add to Messages
        export.addActionListener(new ExportGraphButtonListener(this));
        northPanel.add(export);
        this.add(BorderLayout.NORTH, northPanel);
        // SOUTH Components
        JButton refreshButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.RefreshButton"));
        refreshButton.addActionListener(new RefreshGraphButtonListener(this));
        circleButton = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Circle") + " " +  
        									Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        hierarchicButton = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Hierarchic") + " " + 
											 Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        hierarchicButton.setActionCommand(ReachabilityGraphModel.HIERARCHIC + "");
        hierarchicButton.setSelected(true);
        circleButton.setActionCommand(ReachabilityGraphModel.CIRCLE + "");
        ButtonGroup layoutGroup = new ButtonGroup();
        layoutGroup.add(circleButton);
        layoutGroup.add(hierarchicButton);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        southPanel.add(refreshButton);
        southPanel.add(circleButton);
        southPanel.add(hierarchicButton);
        southPanel.add(bottomInfo = new JLabel(""));
        this.add(BorderLayout.SOUTH, southPanel);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
	
	private void addEditor(IEditor editor){
		boolean alreadyContainsPanel = false;
		for(ReachabilityGraphPanel rgp : panels){
			if(rgp.getEditor().equals(editor)){
				alreadyContainsPanel = true;
			}
		}
		if(!alreadyContainsPanel){
			ReachabilityGraphPanel rgp =  new ReachabilityGraphPanel(editor);
			this.panels.add(rgp);
			this.add(rgp);	
			this.add(BorderLayout.CENTER, rgp);
			editor.setReachabilityEnabled(true);
		}
		this.updatePanelsVisibility(editor);
	}
	
	public boolean hasEditor(IEditor editor){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor() == editor){
				return true;
			}
		}
		return false;
	}
	
	public JGraph getJGraph(IEditor editor){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor() == editor){
				return rgp.getGraph();
			}
		}
		return null;
	}
	
	public JGraph getActualJGraph(){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isVisible()){
				return rgp.getGraph();
			}
		}
		return null;
	}
	
	public void removePanel(IEditor editor){
		
		ReachabilityGraphPanel rememberRgp = null;
		
		// Lookup a panel with given editor
		for(ReachabilityGraphPanel rgp : panels){
			if(rgp.getEditor().equals(editor)){
				rememberRgp = rgp;
			}
		}
		
		// if found a panel, then remove it from internal frame and from panels HashSet
		if(rememberRgp != null){
			this.remove(rememberRgp);	
			this.repaint();
			this.panels.remove(rememberRgp);
		}	
		
		// if it was the last active editor, set internal frame invisible
		if(this.panels.size() == 0){
			this.setVisible(false);
		}
	}

	public void refreshGraph(int type){
		Iterator<JRadioButton> iterButtons = getRadioButtons().iterator();
		while(iterButtons.hasNext()){
			JRadioButton button = iterButtons.next();
			if(Integer.parseInt(button.getActionCommand()) == type){
				button.setSelected(true);
			}
		}
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isShowing()){
				rgp.refreshGraph(type);
				bottomInfo.setText(rgp.getGraphInfo());
				legendInfo.setText(rgp.getLegend());
			}
		}
		this.repaint();
	}
	
	public void updatePanelsVisibility(IEditor editor){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- updateVisibility " + this.getClass().getName() + " ItemsCount " + this.panels.size());
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor().equals(editor)){
				this.add(rgp);
				bottomInfo.setText(rgp.getGraphInfo());
				legendInfo.setText(rgp.getLegend());
				this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title")+ " - " + editor.getName());
			} else {
				this.remove(rgp);
			}
		}
		this.repaint();
	}
	
	protected LinkedList<JRadioButton> getRadioButtons(){
		LinkedList<JRadioButton> buttons = new LinkedList<JRadioButton>();
		buttons.add(circleButton);
		buttons.add(hierarchicButton);
		return buttons;
	}
}

class ExportGraphButtonListener implements ActionListener {
	ReachabilityGraphVC rgvc = null;
	
	public ExportGraphButtonListener(ReachabilityGraphVC rgvc){
		this.rgvc = rgvc;
	}

	public void actionPerformed(ActionEvent arg0) {
		rgvc.getActualJGraph();
		// open modal dialog !
	}
}

class RefreshGraphButtonListener implements ActionListener {

	ReachabilityGraphVC rgvc = null;
	
	public RefreshGraphButtonListener(ReachabilityGraphVC rgvc){
		this.rgvc = rgvc;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Iterator<JRadioButton> iterButtons = rgvc.getRadioButtons().iterator();
		while(iterButtons.hasNext()){
			JRadioButton button = iterButtons.next();
			if(button.isSelected()){
				rgvc.refreshGraph(Integer.parseInt(button.getActionCommand()));	
			}		
		}
	}
}
