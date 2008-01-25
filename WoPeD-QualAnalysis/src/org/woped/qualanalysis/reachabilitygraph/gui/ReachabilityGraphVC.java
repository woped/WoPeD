package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.qualanalysis.IReachabilityGraph;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.ImageExport;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.translations.Messages;

public class ReachabilityGraphVC extends JInternalFrame implements IReachabilityGraph {
	
	private HashSet<ReachabilityGraphPanel> panels = new HashSet<ReachabilityGraphPanel>();
	
	private static final long serialVersionUID = 1L;
	private static ReachabilityGraphVC myInstance = null;
	
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
		this.setFrameIcon(Messages.getImageIcon("ToolBar.ReachabilityGraph"));
		this.setSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(320,240));
		this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(false);
        this.setLayout(new GridLayout(1,1));
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
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isShowing()){
				rgp.refreshGraph(type);
			}
		}
		this.repaint();
	}
	
	public void updatePanelsVisibility(IEditor editor){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- updateVisibility " + this.getClass().getName() + " ItemsCount " + this.panels.size());
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor().equals(editor)){
				this.add(rgp);
				rgp.updateVisibility();
				this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title")+ " - " + editor.getName());
			} else {
				this.remove(rgp);
			}
		}
		this.repaint();
	}
}
