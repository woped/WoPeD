package org.woped.metrics.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;
import org.woped.metrics.builder.MetricsBuilder;
import org.woped.metrics.formulaEnhancement.FormulaEnhancementList;
import org.woped.metrics.formulaEnhancement.FormulaEnhancementUI;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.UITypes.UIMetricsGroup;
import org.woped.metrics.sidebar.components.MetricGroup;

/**
 * @author Mathias Gruschinske
 * The sidebar class, which contains the metrics
 */
public class SideBar extends JPanel {

	private static final long serialVersionUID = 1L;

	public static IEditor editor = null;

	protected static final String PREFIX = "Metrics.";
	protected static final String PREFIX_QUALANALYSIS = "AnalysisSideBar.";
	protected static final String PREFIX_BUTTON = PREFIX_QUALANALYSIS + "Beginner.Button.";

	/**
	 * @param editor
	 * sets the instance variable and adds the components
	 */
	public SideBar(IEditor editor) {
		super(new BorderLayout());
		SideBar.editor = editor;
		addComponents();
	}

	/**
	 * create and add the main panel
	 */
	private void addComponents() {
		this.add(createMainPanel(), BorderLayout.CENTER);
	}

	// List, which contains the groups with calculation results
	private List<UIMetricsGroup> metricGroups;
	private JPanel metrics = null;

	// UI Request Handler which combine view with logic
	private static MetricsUIRequestHandler uiHandler;

	public JPanel createMainPanel() {

		// panel which contains the content of the sidebar
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.WHITE);

		// headline
		JLabel metricsLabel = new JLabel(Messages.getString(PREFIX
				+ "SideBar.Header"));
		metricsLabel.setFont(DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT);
		metricsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		// create an instance of the ui request handler
		setUiHandler(new MetricsUIRequestHandler(editor));
		// gets the metric categories
		List<String> groups = getUiHandler().getMetricGroups();

		metrics = new JPanel(new BorderLayout());
		// create a combobox to select a metric category
		final JComboBox metricCategories = new JComboBox(groups.toArray());

		metricGroupPanels = new ArrayList<MetricGroup>();

		// adds item listener to select a category and gets the metric groups with results
		metricCategories.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.DESELECTED)
					return;

				String selectedItem = (String) metricCategories
						.getSelectedItem();
				metricGroups = getUiHandler().calculateMetrics(selectedItem);
				refreshUI();
			}
		});

		// first selection of a item
		String selectedItem = (String) metricCategories.getSelectedItem();
		metricGroups = getUiHandler().calculateMetrics(selectedItem);
		refreshUI();

		metrics.add(metricCategories, BorderLayout.NORTH);

		// Button for formula enhancements
		JLabel formulaEnhancementLabel = new JLabel(
				"Metrics Formula Enhancements");
		formulaEnhancementLabel.setHorizontalAlignment(JLabel.RIGHT);
		formulaEnhancementLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		formulaEnhancementLabel.setForeground(Color.blue);

		formulaEnhancementLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				new FormulaEnhancementUI();
			}
		});

		// add content to sidebar
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 0;
		c.weighty = 0;
		c.gridwidth = 3;
		mainPanel.add(metricsLabel, c);

		c.gridx = 3;
		c.weightx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		mainPanel.add(new ExportLabel(Messages.getImageIcon("Action.Export"),
				Messages.getString("Metrics.SideBar.Export")), c);

		c.gridx = 4;
		c.weightx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		mainPanel.add(
				new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"),
						Messages.getString("Metrics.SideBar.Hide")), c);

		c.gridx = 0;
		c.weightx = 1.0;
		c.gridy = 1;
		c.weighty = 1.0;
		c.gridwidth = 5;
		mainPanel.add(metrics, c);

		c.gridx = 0;
		c.weightx = 0;
		c.gridy = 3;
		c.weighty = 0;
		c.gridwidth = 1;
		mainPanel.add(new RevertLabel(Messages.getImageIcon("Action.Undo"),
				Messages.getString("Metrics.SideBar.Revert")), c);

		c.gridx = 1;
		c.weightx = 0;
		c.gridy = 3;
		c.weighty = 0;
		c.gridwidth = 1;
		mainPanel.add(
				new ConfigLabel(Messages.getImageIcon("Action.ShowConfig"),
						Messages.getString("Metrics.SideBar.Config")), c);

		if (FormulaEnhancementList.getInstance().enhancementsAvailable()) {
			// If there are enhancements, this button will be displayed
			c.gridx = 2;
			c.weightx = 1.0;
			c.gridy = 3;
			c.weighty = 0;
			c.gridwidth = 3;
			mainPanel.add(formulaEnhancementLabel, c);
		}

		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return mainPanel;
	}

	/**
	 * resets highlighting and collapse all detain panels
	 */
	public void clean() {
		getUiHandler().removeHighlights();
		editor.getGraph().refreshNet();
		editor.getGraph().repaint();

		for (MetricGroup item : metricGroupPanels) {
			item.setItemsUnvisible();
		}
	}

	private JScrollPane resultScrollPane;
	private List<MetricGroup> metricGroupPanels;

	/**
	 * Clean the sidebar and add the components again
	 */
	private void refreshUI() {

		if (metricGroups != null) {

			// remove the scroll pane
			if (resultScrollPane != null) {
				metrics.remove(resultScrollPane);
			}

			// create a new panel, which contains the metric groups
			JPanel groupContainer = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			int rowCount = 0;

			// for all metric groups create an metric group item
			for (UIMetricsGroup item : metricGroups) {

				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = rowCount++;
				c.weightx = 1.0;

				MetricGroup metricGroup = new MetricGroup(getUiHandler(),
						editor, item, this);
				groupContainer.add(metricGroup, c);
				metricGroupPanels.add(metricGroup);
			}

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = rowCount;
			c.weighty = 1.0;

			groupContainer.add(new JLabel(), c);

			// adds the scroll pane again			
			resultScrollPane = new JScrollPane(groupContainer);
			metrics.add(resultScrollPane, BorderLayout.CENTER);

			// refresh the UI
			setVisible(false);
			setVisible(true);
		}
	}

	public void setKeyLabelWidth() {
		double width = 0;
		for (MetricGroup item : metricGroupPanels) {
			if (item.getMaxKeyLabelWidth() > width) {
				width = item.getMaxKeyLabelWidth();
			}
		}

		for (MetricGroup item : metricGroupPanels) {
			item.setMaxKeyLabelWidth(width);
		}
	}
	
	/**
	 * @param uiHandler
	 * sets the UI Request handler
	 */
	public void setUiHandler(MetricsUIRequestHandler uiHandler) {
		SideBar.uiHandler = uiHandler;
	}

	/**
	 * @return
	 * gets the UI Request Handler
	 */
	public static MetricsUIRequestHandler getUiHandler() {
		return uiHandler;
	}

	/**
	 * @author Mathias Gruschinske
	 * label with mouse listener and icon to hide the sidebar
	 */
	class HideLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public HideLabel(Icon icon, String toolTip) {
			super(icon);

			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
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
					editor.hideMetricsBar();
				}
			});
		}
	}

	/**
	 * @author Mathias Gruschinske
	 * label with mouse listener and icon to call the export function to export the results
	 */
	class ExportLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public ExportLabel(Icon icon, String toolTip) {
			super(icon);

			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
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
					getUiHandler().exportMetricsResults(metricGroups);
				}
			});
		}
	}

	/**
	 * @author Mathias Gruschinske
	 * label with mouse listener and icon to collapse all detail panels and revert the highlighting
	 */
	class RevertLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public RevertLabel(Icon icon, String toolTip) {
			super(icon);

			this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
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
					clean();
				}
			});
		}
	}

	/**
	 * @author Mathias Gruschinske
	 * label with mouse listener and icon to call the metrics builder
	 */
	class ConfigLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public ConfigLabel(Icon icon, String toolTip) {
			super(icon);

			this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
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
					new MetricsBuilder(editor, "");
				}
			});
		}
	}
}
