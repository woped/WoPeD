package org.woped.simulation.output;

import javax.swing.JPanel;

public class ServerPanel extends JPanel {
	private static final long serialVersionUID = 5L;
	private String name = "";
	
	public ServerPanel(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
