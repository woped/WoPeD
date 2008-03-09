package org.woped.layout.gui;

import javax.swing.JDialog;

import org.woped.core.Constants;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;



public class SettingsDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	

	private static final int WIDTH  = 640;
	private static final int HEIGHT	= 480;


	public SettingsDialog(AbstractGraph graph, ModelElementContainer mec) {

		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(graph.getName());
		//setTitle(Messages.getString("QuantAna.CapacityPlanning.Precision"));
		//	
		pack();
		setVisible(true);
		LoggerManager.info(Constants.CORE_LOGGER, "SettingsDialog");

	}
	
	public SettingsDialog(ModelElementContainer mec) {

		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("layout title");
		//setTitle(Messages.getString("QuantAna.CapacityPlanning.Precision"));
		//	
		pack();
		setVisible(true);
		LoggerManager.info(Constants.CORE_LOGGER, "SettingsDialog");

	}

	
	public SettingsDialog( String s ) {
		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(s);
		pack();
		setVisible(true);

	}


}
