/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.gui.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.editor.controller.vc.ConfigVC;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         Every Panel for the <code>ConfUI</code> has to be extended from the
 *         <code>AbstractConfPanel</code>.
 * 
 *         Created on: 26.11.2004 Last Change on: 26.11.2004
 */
public abstract class AbstractConfPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private JPanel namePanel = null;

	/**
	 * Resets the configuration to the state of entry (e. g. after
	 * Cancel-Button).
	 * 
	 * @return
	 */
	public abstract boolean applyConfiguration();

	/**
	 * Reads the configuration. Reads the stored values from the configuration
	 * object into the displayed fields.
	 */
	public abstract void readConfiguration();

	/**
	 * Constructor for AbstractConfPanel.
	 * 
	 * @param name
	 */
	public AbstractConfPanel(String name) {
		this.name = name;
		this.setLayout(new BorderLayout());
	}

	/**
	 * 
	 * @param mainPanel
	 */
	protected void setMainPanel(JPanel mainPanel) {
		JScrollPane scrollPanel = new JScrollPane(mainPanel);
		scrollPanel.setPreferredSize(ConfigVC.SCROLL_DIM);
		this.add(scrollPanel, BorderLayout.CENTER);
	}

	/**
	 * @return Returns the namePanel.
	 */
	public JPanel getNamePanel() {
		if (namePanel == null) {
			namePanel = new JPanel();
			namePanel.setBackground(ConfigVC.BACK_COLOR);
			namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel nameLabel = new JLabel(getPanelName());
			nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
			nameLabel.setBackground(Color.WHITE);
			namePanel.add(nameLabel);
		}
		return namePanel;
	}

	/**
	 * Retruns the panel name.
	 * 
	 * @return PanelName
	 */
	public String getPanelName() {
		return name;
	}

	/**
	 * Retruns the Name of the Node-Panel.
	 * 
	 * @return PanelName
	 */
	public String toString() {
		return getPanelName();
	}

	public int getApromoreServerID() {
		return -1;
	}
	
	public boolean getNameAlreadyInUse() {
		return false;
	}

	public void changeApromoreServer(int ID) {
	}

	public boolean getChangeServer() {
		return false;
	}

	public void addApromoreServer(int ID) {
	}

	public boolean getAddServer() {
		return false;
	}

	public void disableServerSettings() {
	}
	
	public void setButtonsToDefault() {
	}
}