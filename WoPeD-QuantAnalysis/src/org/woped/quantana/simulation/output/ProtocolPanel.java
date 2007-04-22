package org.woped.quantana.simulation.output;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("unused")
public class ProtocolPanel extends JPanel {
	
	private static final long serialVersionUID = 3L;
	
	private SimOutputDialog sod;
	
	public ProtocolPanel(SimOutputDialog sod){
		this.setPreferredSize(new Dimension(500,500));
		this.sod = sod;
	}
}
