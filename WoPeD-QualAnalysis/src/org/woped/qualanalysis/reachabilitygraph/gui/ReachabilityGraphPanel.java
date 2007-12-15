package org.woped.qualanalysis.reachabilitygraph.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private ReachabilityGraphVC reachGraphVC = null;
	private IEditor editor = null;
	
	private JLabel someText = null;
	
	public ReachabilityGraphPanel(IEditor editor) {
		super();
		this.editor = editor;
		init();
		setTestText(editor.getId() + " " + editor.getName());
	}
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		someText = new JLabel();
		this.add(someText);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}

	public void updateVisibility(IEditor editor){
		if(this.editor.equals(editor)){
			this.setVisible(true);			
		} else {
			this.setVisible(false);
		}
	}
	
	public IEditor getEditor(){
		return editor;
	}
	
	
	private void setTestText(String text){
		someText.setText(text);
	}
	
}
