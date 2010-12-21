package org.woped.metrics.builder;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import org.woped.core.controller.IEditor;

public class MetricsBuilder extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final Font HEADER_FONT = new Font(Font.DIALOG, Font.BOLD,
			14);
	protected static final Font ITEMS_FONT = new Font(Font.DIALOG, Font.PLAIN,
			12);
	
	public MetricsBuilder(IEditor editor) {
		super(new BorderLayout());
		addComponents();
	}

	private void addComponents() {
		this.add(createMainPanel(), BorderLayout.CENTER);
	}
	


	private JPanel createMainPanel() {
		
		MetricsBuilderPanel mbp = new MetricsBuilderPanel();
		
		
		return mbp;
		
	}

}
