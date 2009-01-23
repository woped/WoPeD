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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.config.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.translations.Messages;
import org.woped.understandability.TransitionColoring;

/**
 * @author <a href="mailto:test@test.de">Matthias/Markus/Bernhard </a> <br>
 * <br>
 *         The <code>ConfColorLayoutPanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the coloring
 *         and the layout. Note: Created for test purposes only!<br>
 *         Created on: 26.11.2008 Last Change on: 10.12.2008
 */

@SuppressWarnings("serial")
public class ConfColorLayoutPanel extends AbstractConfPanel {
	// private Component guiObject;
	// Coloring settings
	private JPanel colorlayoutPanel = null;
	//private JLabel colorlayoutLabel = null;
	private JPanel colorBoxPanel = null;
	private colorLabelMouseListener cLabelMouseListener = null;
	private Color defaultcolors[] = new Color[16];
	private Color colors[] = new Color[16];
	private JButton colorResetButton = null;
	private JLabel colorLabelCollection[] = new JLabel[16];
	private JLabel algorithmModeLabel = null;
	private JComboBox algorithmModeComboBox = null;
	private String[] algorithmModes = null;

	/**
	 * Constructor for ConfToolsPanel.
	 */
	public ConfColorLayoutPanel(JDialog frame, String name) {
		super(name);
		initialize();
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
		// getColorLayoutCheckBox().setSelected(ConfigurationManager.getConfiguration().getColorOn());
		setUnderstandColors();
		ConfigurationManager.getConfiguration().setAlgorithmMode(
				getAlgorithmModeComboBox().getSelectedIndex());
		new TransitionColoring().update();

		return true;
	}

	/**
	 * @see AbstractConfPanel#readConfigruation()
	 */
	public void readConfiguration() {
		if (ConfigurationManager.getConfiguration().getColorOn()) {
			setColorActive(true);
		}
		for (int i = 0; i < 16; i++) {
			colorLabelCollection[i].setBackground(colors[i]);
		}
		getAlgorithmModeComboBox().setSelectedIndex(
				ConfigurationManager.getConfiguration().getAlgorithmMode());
	}

	private void initialize() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		cLabelMouseListener = new colorLabelMouseListener();
		defaultcolors = ConfigurationManager.getConfiguration().getDefaultUnderstandColors();
		colors = ConfigurationManager.getConfiguration().getUnderstandColors();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(getColorLayoutPanel(), c);

		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 2;
		contentPanel.add(new JPanel(), c);
		setMainPanel(contentPanel);
	}

	// ################## GUI COMPONENTS #################### */

//	private JCheckBox getColorLayoutCheckBox() {
//		if (colorlayoutCheckBox == null) {
//			colorlayoutCheckBox = new JCheckBox();
//			colorlayoutCheckBox.setMinimumSize(new Dimension(150, 20));
//			colorlayoutCheckBox
//					.setToolTipText("<html>"
//							+ Messages
//									.getString("Configuration.ColorLayout.Panel.CheckBox.ColoringActive.ToolTip")
//							+ "</html>");
//		}
//		return colorlayoutCheckBox;
//	}

	private JPanel getColorLayoutPanel() {
		if (colorlayoutPanel == null) {
			colorlayoutPanel = new JPanel();
			colorlayoutPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHWEST;

			colorlayoutPanel
					.setBorder(BorderFactory
							.createCompoundBorder(
									BorderFactory
											.createTitledBorder(Messages
													.getString("Configuration.ColorLayout.Panel.Title")),
									BorderFactory
											.createEmptyBorder(5, 5, 10, 5)));

			// JPanel colorCheckBox = new JPanel();
			// colorCheckBox.setSize(40, 34);
			// colorCheckBox.setLayout(new GridLayout(1, 2));
			// colorCheckBox.add(getColorLayoutCheckBox());
			// colorCheckBox.add(getColorLayoutLabel());
			// colorlayoutCheckBox.addActionListener(cbuttonEventHandler);

			// c.anchor = GridBagConstraints.WEST;
			// c.fill = 100;
			// c.weightx = 1;
			// c.gridx = 0;
			// c.gridy = 0;
			// colorlayoutPanel.add(colorCheckBox, c);

			if (colorBoxPanel == null) {
				colorBoxPanel = new JPanel();
				JPanel colorBoxes = new JPanel();
				colorBoxes.setLayout(new GridLayout(2, 8, 2, 2));
				colorBoxes.setSize(new Dimension(136, 34));

				for (int i = 0; i < 8; i++) {
					colorLabelCollection[i] = getColorLabel(i);
					colorBoxes.add(colorLabelCollection[i]);
				}
				for (int i = 8; i < 16; i++) {
					colorLabelCollection[i] = getColorLabel(i);
					colorBoxes.add(colorLabelCollection[i]);
				}
//				c.weightx = 1;
//				c.gridx = 0;
//				c.gridy = 0;
				colorBoxPanel.add(colorBoxes);
				colorBoxPanel.add(getResetButton());
			}
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			colorlayoutPanel.add(colorBoxPanel, c);
			//Panel for algorithm modes
			JPanel algoModePanel = new JPanel();
			algoModePanel.setLayout(new GridLayout(2,2,2,2));
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			algoModePanel.add(getAlgorithmModeLabel(),c);
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 1;
			algoModePanel.  add(getAlgorithmModeComboBox(),c);
			
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			colorlayoutPanel.add(algoModePanel, c);
			setColorActive(true); //MN: Wird das noch gebraucht?
		}
		return colorlayoutPanel;
	 }
	
	 private JButton getResetButton()
	    {
	        if (colorResetButton == null)
	        {
	        	colorResetButton = new JButton();
	        	//Configuration.ColorLayout.Panel.ResetButton
	        	colorResetButton.setIcon(Messages.getImageIcon("Button.ColorReset"));
	        	colorResetButton.setText(Messages.getString("Button.ColorReset.Title"));
	        	colorResetButton.addActionListener(new ActionListener()
	            {
	                public void actionPerformed(ActionEvent e)
	                {
	                	for (int i = 0; i < 16; i++) {
	                		colorLabelCollection[i].setBackground(defaultcolors[i]);
						}
	                }
	            });
	        }
	        return colorResetButton;
	    }

//	private JLabel getColorLayoutLabel() {
//		if (colorlayoutLabel == null) {
//			colorlayoutLabel = new JLabel(
//					Messages
//							.getString("Configuration.ColorLayout.Panel.Label.ColoringActive"));
//			colorlayoutLabel.setHorizontalAlignment(JLabel.RIGHT);
//		}
//		return colorlayoutLabel;
//	}
	 
	 private JLabel getAlgorithmModeLabel(){
		 if (algorithmModeLabel == null){
		     algorithmModeLabel = new JLabel(Messages.getString("Configuration.ColorLayout.Panel.Label.AlgorithmMode"));
		 }
		 return algorithmModeLabel;
	 }
	 
	 private JComboBox getAlgorithmModeComboBox(){
		 if (algorithmModeComboBox == null) {	         
	         algorithmModeComboBox = new JComboBox(getAlgorithmModes());
	         algorithmModeComboBox.setMinimumSize(new Dimension(200, 20));
	     }
		 return algorithmModeComboBox;
	}
	 
	 private String[] getAlgorithmModes(){
		 if (algorithmModes == null){
			 algorithmModes = new String[3];
			//MN: Wenn Komponente realisiert werden soll, dann Strings aus messages holen
			 algorithmModes[0] = "Petri net";
			 algorithmModes[1] = "Petrinet w/o xor correction";
			 algorithmModes[2] = "Van der Aalst net";
		 }
		 return algorithmModes;
	 }

	private JLabel getColorLabel(int nummer) {
		JLabel colorLabel = new JLabel();

		colorLabel.setOpaque(true);
		colorLabel.setBackground(colors[nummer]);
		colorLabel.setPreferredSize(new Dimension(15, 15));
		colorLabel.setBorder(new BevelBorder(BevelBorder.RAISED,Color.lightGray ,Color.darkGray));
		colorLabel.addMouseListener(cLabelMouseListener);
		return colorLabel;

	}

	private void setColorActive(boolean value) {
		for (int i = 0; i < 16; i++) {
			colorLabelCollection[i].setEnabled(value);
			getResetButton().setEnabled(value);
		}
	}

	// Saves the colors from the color set to the configuration XML file
	private void setUnderstandColors() {
		// save to XML file
		ConfigurationManager.getConfiguration().setColor1(
				colorLabelCollection[0].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor2(
				colorLabelCollection[1].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor3(
				colorLabelCollection[2].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor4(
				colorLabelCollection[3].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor5(
				colorLabelCollection[4].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor6(
				colorLabelCollection[5].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor7(
				colorLabelCollection[6].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor8(
				colorLabelCollection[7].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor9(
				colorLabelCollection[8].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor10(
				colorLabelCollection[9].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor11(
				colorLabelCollection[10].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor12(
				colorLabelCollection[11].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor13(
				colorLabelCollection[12].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor14(
				colorLabelCollection[13].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor15(
				colorLabelCollection[14].getBackground().getRGB());
		ConfigurationManager.getConfiguration().setColor16(
				colorLabelCollection[15].getBackground().getRGB());

		// update color array
		for (int i = 0; i < 16; i++) {
			colors[i] = colorLabelCollection[i].getBackground();
		}
	}

	class colorLabelMouseListener implements MouseListener {

		public void actionPerformed(ActionEvent e) {

			((JButton) e.getSource()).setBackground(
					NewColorChooser.showDialog1(ConfColorLayoutPanel.this,"Select a color", Color.black));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			((JLabel) e.getSource()).setBackground(NewColorChooser.showDialog1(
					ConfColorLayoutPanel.this, "Select a color", Color.black));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	static class NewColorChooser extends JColorChooser {

		public NewColorChooser() {
			super();
		}

		public static Color showDialog1(Component component, String title,
				Color initialColor) throws HeadlessException {

			AbstractColorChooserPanel[] oldPanels;
			JColorChooser pane = new JColorChooser(
					initialColor != null ? initialColor : Color.white);

			oldPanels = pane.getChooserPanels();
			for (int i = 0; i < oldPanels.length; i++) {
				String clsName = oldPanels[i].getClass().getName();
				if (clsName
						.equals("javax.swing.colorchooser.DefaultRGBChooserPanel")) {
					// Remove rgb chooser if desired
					pane.removeChooserPanel(oldPanels[i]);
				} else if (clsName
						.equals("javax.swing.colorchooser.DefaultHSBChooserPanel")) {
					// Remove hsb chooser if desired
					pane.removeChooserPanel(oldPanels[i]);
				}
			}
			pane.setPreviewPanel(new JPanel());
			ColorTracker ok = new ColorTracker(pane);
			JDialog dialog = createDialog(component, title, true, pane, ok,
					null);

			dialog.show(); // blocks until user brings dialog down...

			return ok.getColor();
		}

	}

	public static class ColorTracker implements ActionListener, Serializable {
		JColorChooser chooser;
		Color color;

		public ColorTracker(JColorChooser c) {
			chooser = c;
		}

		public void actionPerformed(ActionEvent e) {
			color = chooser.getColor();
		}

		public Color getColor() {
			return color;
		}
	}
}