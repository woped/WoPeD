package org.woped.editor.gui.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.understandability.NetColorScheme;

/**
 * @authors Bernhard von Hasseln, Matthias Mruzek and Markus Noeltner<br>
 *         The ConfUnderstandabilityPanel is the configuration panel
 *         for WoPeD's understandability features.
 *         So far the configuration for the handle cluster coloring
 *         has been implemented.
 */


@SuppressWarnings("serial")
public class ConfUnderstandabilityPanel extends AbstractConfPanel {
	private JPanel colorPanel = null;
	private JPanel colorAlgoPanel = null;
	private JPanel colorBoxPanel = null;
	private ColorLabelMouseListener cLabelMouseListener = null;
	private Color defaultcolors[] = new Color[16];
	private Color colors[] = new Color[16];
	private JButton colorResetButton = null;
	private JLabel colorLabelCollection[] = new JLabel[16];
	private JLabel algorithmModeLabel = null;
	private JComboBox algorithmModeComboBox = null;
	private String[] algorithmModes = null;

    //! Constructor for the ConfUnderstandabilityPanel
	public ConfUnderstandabilityPanel(String name) {
		super(name);
		initialize();
	}
	
	public ConfUnderstandabilityPanel(JDialog frame, String name) {
		super(name);
		initialize();
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
		setUnderstandColors();
		ConfigurationManager.getConfiguration().setAlgorithmMode(
				getAlgorithmModeComboBox().getSelectedIndex());
		new NetColorScheme().update();

		return true;
	}

	/**
	 * @see AbstractConfPanel#readConfigruation()
	 */
	public void readConfiguration() {
		ConfigurationManager.getConfiguration().setColorOn(false);
		for (int i = 0; i < 16; i++) {
			colorLabelCollection[i].setBackground(colors[i]);
		}
		getAlgorithmModeComboBox().setSelectedIndex(
				ConfigurationManager.getConfiguration().getAlgorithmMode());
	}

	//! Initialize all components
	private void initialize() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		cLabelMouseListener = new ColorLabelMouseListener();
		defaultcolors = ConfigurationManager.getConfiguration().getDefaultUnderstandColors();
		colors = ConfigurationManager.getConfiguration().getUnderstandColors();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(getColorPanel(), c);
		
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(getcolorAlgoPanel(),c);

		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 2;
		contentPanel.add(new JPanel(), c);
		setMainPanel(contentPanel);
	}

	// ################## GUI COMPONENTS #################### */
	private JPanel getColorPanel() {
		if (colorPanel == null) {
			colorPanel = new JPanel();
			colorPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			colorPanel
					.setBorder(BorderFactory
							.createCompoundBorder(
									BorderFactory
											.createTitledBorder(Messages
													.getString("Configuration.ColorLayout.ColorPanel.Title")),
									BorderFactory
											.createEmptyBorder(5, 5, 10, 5)));
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
				colorBoxPanel.add(colorBoxes);	
			}
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			colorPanel.add(colorBoxPanel, c);
			
			c.insets = new Insets(0, 4, 0, 0);
			
			c.gridx = 0;
			c.gridy = 1;
			colorPanel.add(getResetButton(), c);							
		}
		return colorPanel;
	 }
	
	private JPanel getcolorAlgoPanel() {
		if (colorAlgoPanel == null) {
			colorAlgoPanel = new JPanel();
			colorAlgoPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			colorAlgoPanel
					.setBorder(BorderFactory
							.createCompoundBorder(
									BorderFactory
											.createTitledBorder(Messages
													.getString("Configuration.ColorLayout.ColorAlgoPanel.Title")),
									BorderFactory
											.createEmptyBorder(5, 5, 10, 5)));
			
			c.insets = new Insets(0, 4, 0, 0);
			
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			colorAlgoPanel.add(getAlgorithmModeLabel(),c);
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			colorAlgoPanel.add(getAlgorithmModeComboBox(),c);			
		}
		return colorAlgoPanel;
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
	 
	 private JLabel getAlgorithmModeLabel(){
		 if (algorithmModeLabel == null){
		     algorithmModeLabel = new JLabel(Messages.getString("Configuration.ColorLayout.ColorAlgoPanel.Label.AlgorithmMode"));
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
			 algorithmModes[0] = Messages.getString("Configuration.ColorLayout.ColorAlgoPanel.CombBox.AlgorithmMode.Op0");
			 algorithmModes[1] = Messages.getString("Configuration.ColorLayout.ColorAlgoPanel.CombBox.AlgorithmMode.Op1");
			 algorithmModes[2] = Messages.getString("Configuration.ColorLayout.ColorAlgoPanel.CombBox.AlgorithmMode.Op2");
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

	//! Saves the colors from the color set to the configuration XML file
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

		// update the color array with the saved values
		for (int i = 0; i < 16; i++) {
			colors[i] = colorLabelCollection[i].getBackground();
		}
	}

	//! MouseListener for the 16 color labels
	class ColorLabelMouseListener implements MouseListener {

		public void actionPerformed(ActionEvent e) {

			((JButton) e.getSource()).setBackground(
					NewColorChooser.showDialog1(ConfUnderstandabilityPanel.this,"Select a color", Color.black));
		}

		public void mouseClicked(MouseEvent e) {
			((JLabel) e.getSource()).setBackground(NewColorChooser.showDialog1(
					ConfUnderstandabilityPanel.this, "Select a color", Color.black));
		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

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

			dialog.setVisible(true);
			
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