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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         The <code>ConfLanguagePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the language. <br>
 *         Created on: 26.11.2004 Last Change on: 14.11.2005
 */
@SuppressWarnings("serial")
public class ConfBusinessDashboardPanel extends AbstractConfPanel {


	/**
	 * Constructor for ConfToolsPanel.
	 */
	public ConfBusinessDashboardPanel(String name) {
		super(name);
		initialize();
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
				return true;
	}

	/**
	 * @see AbstractConfPanel#readConfiguration()
	 */
	public void readConfiguration() {

	}

	private void initialize() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		

		setMainPanel(contentPanel);
	}

	// ################## GUI COMPONENTS #################### */
}