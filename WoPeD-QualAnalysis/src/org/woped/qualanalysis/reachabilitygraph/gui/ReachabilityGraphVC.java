package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewListener;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphVC extends JInternalFrame {
	
	private HashMap<String, IEditor> editors = new HashMap<String, IEditor>();
	
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
		this.editors.put(editor.getId(), editor);
		someText.setText(editor.getId());
	}
	
	
	private JLabel someText = null;
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setSize(new Dimension(640, 480));
		this.setTitle("REACHGRAPH");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        
        someText = new JLabel("");
        this.add(someText);
        
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
}
