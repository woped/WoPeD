package org.woped.metrics.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.metrics.metricsCalculation.MetricsCalculator;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.StringPair;
import org.woped.metrics.metricsCalculation.UITypes.UIThreshold;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.sidebar.assistant.components.ClickLabel;
import org.woped.translations.Messages;

public class SideBar extends JPanel {

	private static final long serialVersionUID = 3760354320626128100L;

	private IEditor editor = null;

	protected static final String PREFIX = "Metrics.";
	protected static final String PREFIX_QUALANALYSIS = "AnalysisSideBar.";
	protected static final String PREFIX_BUTTON = PREFIX_QUALANALYSIS
			+ "Beginner.Button.";

	protected static final Font HEADER_FONT = new Font(Font.DIALOG, Font.BOLD,
			14);
	protected static final Font ITEMS_FONT = new Font(Font.DIALOG, Font.PLAIN,
			12);

	protected static final String CORRECT_ICON = PREFIX_BUTTON + "Correct";
	protected static final String INCORRECT_ICON = PREFIX_BUTTON + "Incorrect";
	protected static final String WARNING_ICON = PREFIX_BUTTON + "Warning";

	protected static final int TRAFFICLIGHT_GREEN = 1;
	protected static final int TRAFFICLIGHT_ORGANE = 2;
	protected static final int TRAFFICLIGHT_RED = 3;

	protected static final String SUB_POINT = " - ";

	public SideBar(IEditor editor) {
		super(new BorderLayout());
		this.editor = editor;
		addComponents();
	}

	private void addComponents() {
		this.add(createMainPanel(), BorderLayout.CENTER);
	}

	private List<StringPair> results = null;
	private JPanel metrics = null;

	private MetricsUIRequestHandler uiHandler;

	private JPanel createMainPanel() {

		// panel which contains the content of the sidebar
		JPanel mainPanel = new JPanel(new GridBagLayout());

		JLabel metricsLabel = new JLabel(Messages.getString(PREFIX
				+ "SideBar.Header"));
		metricsLabel.setFont(HEADER_FONT);
		metricsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		uiHandler = new MetricsUIRequestHandler(editor);
		List<String> groups = uiHandler.getMetricGroups();

		metrics = new JPanel(new BorderLayout());
		final JComboBox metricGroups = new JComboBox(groups.toArray());

		metricGroups.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.DESELECTED)
					return;

				String selectedItem = (String) metricGroups.getSelectedItem();
				results = uiHandler.calculateMetrics(selectedItem);
				refreshUI();
			}
		});

		String selectedItem = (String) metricGroups.getSelectedItem();
		results = uiHandler.calculateMetrics(selectedItem);
		refreshUI();

		metrics.add(metricGroups, BorderLayout.NORTH);

		JLabel configMetricsLabel = new JLabel("Metrics Configurator");
		configMetricsLabel.setHorizontalAlignment(JLabel.RIGHT);
		configMetricsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		configMetricsLabel.setForeground(Color.blue);

		// add content to sidebar

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		mainPanel.add(metricsLabel, c);

		c.gridx = 1;
		c.weightx = 1.0;
		c.gridy = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				uiHandler.exportMetricsResults(results);				
			}
		});
		mainPanel.add(exportButton, c);
		
		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 1;
		c.weighty = 1.0;
		c.gridwidth = 2;
		mainPanel.add(metrics, c);

		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 2;
		c.weighty = 0;
		c.gridwidth = 2;
		mainPanel.add(createNetStatistics(), c);

		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 3;
		c.weighty = 0;
		c.gridwidth = 2;
		mainPanel.add(configMetricsLabel, c);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		return mainPanel;
	}

	private JPanel resultsUI = null;
	private JScrollPane resultScrollPane = null;

	private void refreshUI() {

		if (results != null) {
			if (resultScrollPane != null) {
				metrics.remove(resultScrollPane);
			}

			resultsUI = new JPanel(new GridBagLayout());
			rowCount = 0;

			for (int i = 0; i < results.size(); i++)
				bulidMetricsPanel(i);

			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = rowCount;
			c.gridwidth = 3;
			c.weighty = 1.0;
			resultsUI.add(new JLabel(), c);

			resultScrollPane = new JScrollPane(resultsUI);
			metrics.add(resultScrollPane, BorderLayout.CENTER);
			setVisible(false);
			setVisible(true);
		}
	}

	private int rowCount = 0;

	private void bulidMetricsPanel(int index) {

		String key = results.get(index).getKey();
		String value = results.get(index).getValue();

		GridBagConstraints c = new GridBagConstraints();

		final JPanel details = new JPanel(new BorderLayout());
		details.setVisible(false);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = rowCount;
		c.ipady = 5;
		c.ipadx = 5;

		final JLabel expand = new JLabel(Messages
				.getImageIcon("Metrics.SideBar.Expand"));
		expand.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				details.setVisible(!details.isVisible());
				if (details.isVisible()) {
					expand.setIcon(Messages
							.getImageIcon("PetriNet.Resources.Expand"));
				} else {
					expand.setIcon(Messages
							.getImageIcon("Metrics.SideBar.Expand"));
				}

			}
		});
		resultsUI.add(expand, c);

		String tooltip = uiHandler.getDescription((String) key);

		JLabel keyLabel = new JLabel(" " + key + ":");
		keyLabel.setToolTipText(tooltip);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = rowCount;
		c.weightx = 1.0;
		resultsUI.add(keyLabel, c);

		JLabel valueLabel = new JLabel(value);
		valueLabel.setToolTipText(tooltip);
	
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = rowCount;
		c.weightx = 1.0;
		resultsUI.add(valueLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = rowCount;
		c.weightx = 0;

		resultsUI.add(getTrafficLight(uiHandler.getTreshholdState(key
				.toString())), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = ++rowCount;
		c.gridwidth = 3;
		c.insets = new Insets(0, 25, 0, 0);

		List<UIThreshold> thresholds = uiHandler
				.getUIThresholds(key.toString());

		JPanel thresholdsPanel = new JPanel(new GridBagLayout());

		int row = 0;
		for (UIThreshold uiThreshold : thresholds) {

			String tempStr = "-";

			GridBagConstraints cDetails = new GridBagConstraints();
			cDetails.fill = GridBagConstraints.BOTH;
			cDetails.insets = new Insets(5, 5, 5, 5);

			cDetails.gridx = 0;
			cDetails.weightx = 0;
			cDetails.gridy = row;
			cDetails.weighty = 0;
			cDetails.gridwidth = 1;
			thresholdsPanel.add(getTrafficLight(uiThreshold.getThreshold()),
					cDetails);

			double from = uiThreshold.getFrom();
			String fromStr;

			if (from == Integer.MIN_VALUE) {
				tempStr = "<";
				fromStr = "value";
			} else {
				fromStr = "" + from;
			}

			cDetails.gridx = 1;
			cDetails.weightx = 0;
			cDetails.gridy = row;
			cDetails.weighty = 0;
			cDetails.gridwidth = 1;
			thresholdsPanel.add(new JLabel(fromStr), cDetails);
			
			double to = uiThreshold.getTo();
			String toStr;

			if (to == Double.MAX_VALUE) {
				tempStr = ">";
				toStr = "value";
			} else {
				toStr = "" + to;
			}

			cDetails.gridx = 2;
			cDetails.weightx = 0;
			cDetails.gridy = row;
			cDetails.weighty = 0;
			cDetails.gridwidth = 1;
			thresholdsPanel.add(new JLabel(tempStr), cDetails);

			cDetails.gridx = 3;
			cDetails.weightx = 0;
			cDetails.gridy = row;
			cDetails.weighty = 0;
			cDetails.gridwidth = 1;
			thresholdsPanel.add(new JLabel(toStr), cDetails);

			cDetails.gridx = 4;
			cDetails.weightx = 1.0;
			cDetails.gridy = row;
			cDetails.weighty = 0;
			cDetails.gridwidth = 1;
			thresholdsPanel.add(new JLabel(), cDetails);

			row++;

		}

		JLabel description = new JLabel(tooltip);
		description.setFont(new Font(description.getFont().getFamily(), Font.BOLD, description.getFont().getSize()));
		description.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,5));
		
		details.add(description, BorderLayout.NORTH);
		details.add(thresholdsPanel, BorderLayout.CENTER);
		
		details.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		resultsUI.add(details, c);

		rowCount++;
	}

	private IQualanalysisService qualanalysisService = null;

	private IQualanalysisService getQualanalysisService() {
		if (qualanalysisService != null) {
			return qualanalysisService;
		} else {
			return QualAnalysisServiceFactory
					.createNewQualAnalysisService(editor);
		}
	}

	private JPanel createNetStatistics() {
		JPanel netStatistics = new JPanel(new BorderLayout());
		qualanalysisService = getQualanalysisService();
		MetricsCalculator request = new MetricsCalculator(editor);

		JPanel statistics = new JPanel(new GridLayout(0, 2));

		// header label

		JLabel netStatisticLabel = new JLabel(Messages
				.getString(PREFIX_QUALANALYSIS + "NetStatistics"));
		netStatisticLabel.setFont(HEADER_FONT);
		netStatisticLabel
				.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		netStatistics.add(netStatisticLabel, BorderLayout.NORTH);

		// places

		ClickLabel clickLabel = new ClickLabel(Messages
				.getString(PREFIX_QUALANALYSIS + "NumPlaces")
				+ ":", qualanalysisService.getPlaces().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		JLabel count = new JLabel(String.valueOf(qualanalysisService
				.getPlaces().size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// transitions

		clickLabel = new ClickLabel(Messages.getString(PREFIX_QUALANALYSIS
				+ "NumTransitions")
				+ ":", qualanalysisService.getTransitions().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel((int) request.calculateT() + "", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// operators

		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString(PREFIX_QUALANALYSIS + "NumOperators")
				+ ":", qualanalysisService.getOperators().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel(String.valueOf(qualanalysisService.getOperators()
				.size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// subprocesses

		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString(PREFIX_QUALANALYSIS + "NumSubprocesses")
				+ ":", qualanalysisService.getSubprocesses().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel(String.valueOf(qualanalysisService.getSubprocesses()
				.size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// arcs

		JLabel arcLabel = new JLabel(Messages.getString(PREFIX_QUALANALYSIS
				+ "NumArcs")
				+ ":");
		arcLabel.setFont(ITEMS_FONT);
		statistics.add(arcLabel);

		count = new JLabel((int) request.calculateA() + "", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		netStatistics.add(statistics, BorderLayout.CENTER);

		return netStatistics;
	}

	private JLabel getTrafficLight(
			IMetricsConfiguration.AlgorithmThresholdState color) {

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
			trafficLight = new JLabel(Messages
					.getImageIcon("Metrics.SideBar.Incorrect"));
			break;
		default:
			trafficLight = new JLabel();
			break;
		}

		return trafficLight;
	}
}
