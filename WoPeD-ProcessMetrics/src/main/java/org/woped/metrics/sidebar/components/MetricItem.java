package org.woped.metrics.sidebar.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;
import org.woped.metrics.builder.MetricsBuilder;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.UITypes.UIThreshold;
import org.woped.metrics.sidebar.SideBar;

/**
 * @author Mathias Gruschinske
 * a metric item is a single entry in the metric group and shows some information about the metric and displays the result of them
 */
public class MetricItem extends JPanel {

	private static final long serialVersionUID = 1L;

	protected static final String PREFIX_QUALANALYSIS = "AnalysisSideBar.";
	protected static final String PREFIX_BUTTON = PREFIX_QUALANALYSIS
			+ "Beginner.Button.";
	protected static final String CORRECT_ICON = PREFIX_BUTTON + "Correct";
	protected static final String INCORRECT_ICON = PREFIX_BUTTON + "Incorrect";
	protected static final String WARNING_ICON = PREFIX_BUTTON + "Warning";

	private String key;
	private String value;
	private MetricsUIRequestHandler uiHandler;
	private IEditor editor;
	private JPanel content;
	private JPanel detailPanel;
	
	private SideBar parent;

	/**
	 * @param key String which identified a single metric
	 * @param value String who contains the result of the metric
	 * @param uiHandler Handler to request some more informations for a metric
	 * @param editor Instance of the editor to highlight the components of the metrics
	 * 
	 * sets the instance variable for the metric item
	 */
	public MetricItem(String key, String value,
			MetricsUIRequestHandler uiHandler, IEditor editor, SideBar parent) {

		super(new BorderLayout());

		this.parent = parent;
		this.key = key;
		this.value = value;
		this.uiHandler = uiHandler;
		this.editor = editor;

		content = new JPanel(new BorderLayout());
		
		// creates the panels, which displays the results and some informations for the metric

		createSummaryPanel();
		createDetailPanel();

		this.add(content, BorderLayout.CENTER);

	}

	private JLabel expandIcon;
	private JLabel keyLabel;

	/**
	 * Method to create the summary panel, which is displayed in a group.
	 * A click on the panel sets the detail panel to visible.
	 */
	private void createSummaryPanel() {

		// Expander
		expandIcon = new JLabel(Messages.getImageIcon("Metrics.SideBar.Expand"));
		// adds mouselistener to observe a click on the panel
		// on a click the details panel should be visible
		expandIcon.addMouseListener(new ExpanderListener());
		expandIcon.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		content.add(expandIcon, BorderLayout.WEST);

		JPanel metricPanel = new JPanel(new BorderLayout());

		// KeyLabel
		if (uiHandler.hasHighlight(key)) {
			keyLabel = new HighlightLink(uiHandler, editor, key);
			keyLabel.setToolTipText(uiHandler.getDescription(key));
			keyLabel.addMouseListener(new ExpanderListener());
		} else {
			keyLabel = new JLabel(" " + key + ":");
			keyLabel.setToolTipText(uiHandler.getDescription(key));
			keyLabel.addMouseListener(new ExpanderListener());
			keyLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					uiHandler.setHighlight(null);
					editor.repaint();
				}
			});
		}

		keyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		metricPanel.add(keyLabel, BorderLayout.WEST);

		// ValueLabel
		JLabel valueLabel = new JLabel(value);
		valueLabel.setToolTipText(uiHandler.getDescription(key));
		valueLabel.addMouseListener(new ExpanderListener());
		valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		metricPanel.add(valueLabel, BorderLayout.CENTER);
		content.add(metricPanel, BorderLayout.CENTER);

		// TreshholdState
		JLabel treshholdState = getTrafficLight(uiHandler
				.getTreshholdState(key));
		treshholdState.addMouseListener(new ExpanderListener());
		treshholdState.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		content.add(treshholdState, BorderLayout.EAST);

	}

	/**
	 * Method to create the detail panel, which is displayed under the summary panel.
	 * Contains a description, errors and the threshold states. Additional it contains two actions, which use the metrics builder.
	 */
	private void createDetailPanel() {

		// detail panel
		detailPanel = new JPanel(new BorderLayout());
		detailPanel.setVisible(false);
		detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// create a container to set a border
		JPanel container = new JPanel(new BorderLayout());
		container.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		// description

		JPanel captions = new JPanel(new BorderLayout());

		JLabel descriptionLabel = new JLabel(uiHandler.getDescription(key));
		descriptionLabel.setFont(new Font(descriptionLabel.getFont()
				.getFamily(), Font.BOLD, descriptionLabel.getFont().getSize()));
		descriptionLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
										
		captions.add(descriptionLabel, BorderLayout.NORTH);

		// error message
		if (uiHandler.getError(key) != null) {
			JLabel errorLabel = new JLabel(uiHandler.getError(key));
			errorLabel.setForeground(Color.RED);
			errorLabel.setFont(new Font(errorLabel.getFont().getFamily(),
					Font.BOLD, descriptionLabel.getFont().getSize()));
			errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			captions.add(errorLabel, BorderLayout.SOUTH);
		}

		container.add(captions, BorderLayout.NORTH);

		// thresholds
		JPanel contentContainer = new JPanel(new BorderLayout());
		List<UIThreshold> thresholds = MetricsUIRequestHandler
				.getUIThresholds(key.toString());
		JPanel thresholdsPanel = new JPanel(new GridBagLayout());

		int row = 0;

		// for all thresholds in the xml for a metric will generate one new row in the thresholds panel
		for (UIThreshold uiThreshold : thresholds) {
			String tempStr = "-";

			GridBagConstraints constraintsDetails = new GridBagConstraints();

			constraintsDetails.fill = GridBagConstraints.BOTH;
			constraintsDetails.insets = new Insets(5, 5, 5, 5);
			constraintsDetails.gridx = 0;
			constraintsDetails.weightx = 0;
			constraintsDetails.gridy = row;
			constraintsDetails.weighty = 0;
			constraintsDetails.gridwidth = 1;

			thresholdsPanel.add(getTrafficLight(uiThreshold.getThreshold()),
					constraintsDetails);

			double from = uiThreshold.getFrom();
			String fromStr;

			if (from == Integer.MIN_VALUE) {
				tempStr = "<=";
				fromStr = Messages.getString("Metrics.SideBar.Detail.Value");
			} else
				fromStr = "" + from;

			constraintsDetails.gridx = 1;
			constraintsDetails.weightx = 0;
			constraintsDetails.gridy = row;
			constraintsDetails.weighty = 0;
			constraintsDetails.gridwidth = 1;

			thresholdsPanel.add(new JLabel(fromStr), constraintsDetails);

			double to = uiThreshold.getTo();

			String toStr;

			if (to == Double.MAX_VALUE) {
				tempStr = "<=";
				toStr = Messages.getString("Metrics.SideBar.Detail.Value");;
			} else 
				toStr = "" + to;

			constraintsDetails.gridx = 2;
			constraintsDetails.weightx = 0;
			constraintsDetails.gridy = row;
			constraintsDetails.weighty = 0;
			constraintsDetails.gridwidth = 1;

			thresholdsPanel.add(new JLabel(tempStr), constraintsDetails);

			constraintsDetails.gridx = 3;
			constraintsDetails.weightx = 0;
			constraintsDetails.gridy = row;
			constraintsDetails.weighty = 0;
			constraintsDetails.gridwidth = 1;

			thresholdsPanel.add(new JLabel(toStr), constraintsDetails);

			constraintsDetails.gridx = 4;
			constraintsDetails.weightx = 1.0;
			constraintsDetails.gridy = row;
			constraintsDetails.weighty = 0;
			constraintsDetails.gridwidth = 1;

			thresholdsPanel.add(new JLabel(), constraintsDetails);

			row++;
		}
		
		// if more than 0 items in the panel,  it will be displayed
		if (thresholdsPanel.getComponentCount() != 0) {
			contentContainer.add(thresholdsPanel, BorderLayout.CENTER);
		}

		// metric action
		JPanel actionContainer = new JPanel(new BorderLayout());
		JPanel actions;
		// We need two slots if metric is editable, otherwise only one
		if(MetricsUIRequestHandler.canBeEdited(key))
			actions = new JPanel(new GridLayout(0, 2));
		else
			actions = new JPanel(new GridLayout(0, 1));
			
		JLabel editActionLabel;

		// can the metric edit (custom metrics), the config hyperlink will be displayed
		// else an info hyperlink will be shown
		if (MetricsUIRequestHandler.canBeEdited(key)) {
			editActionLabel = new ConfigLabel(
					MetricsUIRequestHandler.algorithmNameToID(key),
					Messages.getImageIcon("Action.ShowConfig"),
					Messages.getString("Metrics.SideBar.Item.Config"));
		} else {
			editActionLabel = new InfoLabel(
					MetricsUIRequestHandler.algorithmNameToID(key),
					Messages.getImageIcon("AnalysisSideBar.Expert.Info"),
					Messages.getString("Metrics.SideBar.Item.Info"));

		}
		actions.add(editActionLabel);
				
		// adds the config/info hyperlink and a delete hyperlink if we have a custom metric
		if(MetricsUIRequestHandler.canBeEdited(key)) {
			actions.add(new DeleteLabel(
				MetricsUIRequestHandler.algorithmNameToID(key),
				Messages.getImageIcon("PetriNet.Resources.Delete"), 
				Messages.getString("Metrics.SideBar.Item.Delete")));
		}

		actionContainer.add(actions, BorderLayout.SOUTH);
		contentContainer.add(actionContainer, BorderLayout.EAST);
		container.add(contentContainer, BorderLayout.CENTER);
		detailPanel.add(container, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.gridwidth = 4;

		content.add(detailPanel, BorderLayout.SOUTH);
	}

	/**
	 * @param color input as MetricThresholdState
	 * @return gets the icon for the trafficlights of the thresholds
	 */
	private JLabel getTrafficLight(

	IMetricsConfiguration.MetricThresholdState color) {

		JLabel trafficLight;

		switch (color) {

		case GREEN:

			trafficLight = new JLabel(Messages.getImageIcon(CORRECT_ICON));
			break;

		case YELLOW:

			trafficLight = new JLabel(Messages.getImageIcon(WARNING_ICON));
			break;

		case RED:

			trafficLight = new JLabel(Messages.getImageIcon(INCORRECT_ICON));
			break;

		case GRAY:

			trafficLight = new JLabel(
					Messages.getImageIcon("Metrics.SideBar.Incorrect"));
			break;

		default:

			trafficLight = new JLabel();
			break;

		}

		return trafficLight;

	}

	/**
	 * @author Mathias Gruschinske
	 * internal class, which contains the mouse listener for the expander button
	 */
	class ExpanderListener implements MouseListener {

		public void mouseReleased(MouseEvent arg0) {

		}

		public void mousePressed(MouseEvent arg0) {

		}

		public void mouseExited(MouseEvent arg0) {

			content.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		}

		public void mouseEntered(MouseEvent arg0) {

			content.setCursor(new Cursor(Cursor.HAND_CURSOR));

		}

		public void mouseClicked(MouseEvent arg0) {

			setDetailsVisiblity(!detailPanel.isVisible());
		}

	}

	/**
	 * @param isVisible
	 * sets the visibility of the detail panel and change the icon of the button
	 */
	public void setDetailsVisiblity(boolean isVisible) {

		detailPanel.setVisible(isVisible);

		if (detailPanel.isVisible()) {

			expandIcon.setIcon(Messages
					.getImageIcon("PetriNet.Resources.Expand"));

		} else {

			expandIcon.setIcon(Messages.getImageIcon("Metrics.SideBar.Expand"));

		}

	}

	public double getKeyLabelWidth() {

		if (keyLabel != null) {

			return keyLabel.getSize().getWidth();

		}

		return 0;

	}

	public void setKeyLabelWidth(double width) {

		keyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
				(int) (width - keyLabel.getSize().getWidth())));

	}

	/**
	 * @author Mathias Gruschinske
	 * A config label with mouse listener, which open the metrics builder and select the metric to edit (only custom metrics)
	 */
	class ConfigLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public ConfigLabel(String metricID, Icon icon, String toolTip) {

			super(icon);

			final String ID = metricID;

			this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			this.setToolTipText(toolTip);
			this.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {

				}

				public void mousePressed(MouseEvent e) {

				}

				public void mouseEntered(MouseEvent arg0) {

					setCursor(new Cursor(Cursor.HAND_CURSOR));

				}

				public void mouseExited(MouseEvent arg0) {

					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

				}

				public void mouseClicked(MouseEvent e) {

					new MetricsBuilder(editor, ID);

				}

			});

		}

	}

	/**
	 * @author Mathias Gruschinske
	 * A delete label with mouse listener, which delete the metric (only custom metrics)
	 */
	class DeleteLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public DeleteLabel(String metricID, Icon icon, String toolTip) {
			super(icon);
			
			final String ID = metricID;

			this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			this.setToolTipText(toolTip);
			this.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseEntered(MouseEvent arg0) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent arg0) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				public void mouseClicked(MouseEvent e) {
					IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
					metricsConfig.deleteAlgorithm(ID);
					parent.createMainPanel();
					//TODO: Sidebar refresh
				}
			});
		}
	}

	/**
	 * @author Mathias Gruschinske
	 * A info label with mouse listener, which open the metrics builder and select the metric
	 */
	class InfoLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public InfoLabel(String metricID, Icon icon, String toolTip) {
			super(icon);

			final String ID = metricID;

			this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			this.setToolTipText(toolTip);
			this.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {
				}

				public void mouseEntered(MouseEvent arg0) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent arg0) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				public void mouseClicked(MouseEvent e) {
					new MetricsBuilder(editor, ID);
				}
			});
		}
	}
}