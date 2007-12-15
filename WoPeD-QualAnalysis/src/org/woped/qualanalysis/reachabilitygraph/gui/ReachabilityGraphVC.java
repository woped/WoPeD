package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Dimension;
import java.util.HashSet;
import javax.swing.JInternalFrame;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphVC extends JInternalFrame {
	
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
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setSize(new Dimension(640, 480));
		this.setTitle("REACHGRAPH");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
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

	public void updatePanelsVisibility(IEditor editor){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- updateVisibility " + this.getClass().getName() + " ItemsCount " + this.panels.size());
		for (ReachabilityGraphPanel rgp : panels) {
			rgp.updateVisibility(editor);
		}
	}
	
}
