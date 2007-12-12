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
		ReachabilityGraphPanel rgp =  new ReachabilityGraphPanel(editor);
		this.panels.add(rgp);
		this.add(rgp);
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

	public void updatePanelsVisibility(IEditor editor){
		for (ReachabilityGraphPanel rgp : panels) {
			rgp.updateVisibility(editor);
		}
	}
	
}
