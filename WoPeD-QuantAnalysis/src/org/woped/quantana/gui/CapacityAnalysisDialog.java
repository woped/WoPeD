package org.woped.quantana.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.editor.utilities.Messages;
import org.woped.quantana.graph.Arc;
import org.woped.quantana.graph.Key;
import org.woped.quantana.graph.Node;
import org.woped.quantana.graph.NodePair;
import org.woped.quantana.graph.WorkflowNetGraph;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceClassTaskAllocation;
import org.woped.quantana.resourcealloc.ResourceClassTaskAllocationTable;

public class CapacityAnalysisDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private EditorVC editor = null;

	private JLabel lblPeriod = null;

	private JLabel lblCases = null;

	private JLabel lblThreshold = null;

	private JTextField txtPeriod = null;

	private JTextField txtCases = null;

	private JTextField txtThreshold = null;

	private JComboBox cboPeriodUnit = null;

	private double timeUnit = 1.0;

	private String timeIntervall = Constants.TIME_MINUTE;

	private double period = 8.0;

	private String periodIntervall = Constants.TIME_HOUR;

	private double lambda = 50.0;

	private double epsilon = 0.001;

	private StructuralAnalysis sa;

	private ModelElementContainer mec;

	private WorkflowNetGraph graph;

	private ResourceAllocation resAlloc;

	private int numResCls;

	private int numTrans = 0;

	private int currPrec = 2;

	private int currUtil = 80;
	
	private HashMap<Key, Node> unfoldedNet = new HashMap<Key, Node>();

	// Capacity Plan Tab
	private String[] colNames = {
			Messages.getString("QuantAna.CapacityPlanning.Column.Taskname"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Workitemspercase"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Workitemsperperiod"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Timeperworkitem"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Timepercase"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Timeperperiod") 
			};

	private String[] colNames2 = {
			Messages.getString("QuantAna.CapacityPlanning.Column.Resourceclass"),
			Messages.getString("QuantAna.CapacityPlanning.Column.AggregateTime"),
			Messages.getString("QuantAna.CapacityPlanning.Column.Numresources") 
			};

	private String[] colToolTips1 = {
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Taskname"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Workitemspercase"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Workitemsperperiod"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Timeperworkitem"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Timepercase"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Timeperperiod") 
			};
	private String[] colToolTips2 = {
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Resourceclass"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.AggregateTime"),
			Messages.getString("QuantAna.CapacityPlanning.Tooltip.Numresources") 
			};
	private Object[][] tableTasksMatrix;

	private Object[][] tableResMatrix;

	private double[][] tasksMatrix;

	private double[][] resMatrix;

	private TasksTableModel tmTasks = null;

	private ResTableModel tmRes = null;

	private JTable tasksTable = null;

	private JTable resourcesTable = null;

	private JScrollPane tasksTablePane = null;

	private JScrollPane resourcesTablePane = null;

	private JLabel lblUnfolding = null;

	private JLabel lblDeviation = null;

	private JPanel resourcesPanel = null;

	private JPanel precisionPanel = null;

	private JPanel resourceUtilPanel = null;

	private JPanel paramPanel = null;

	private JPanel valuePanel = null;

	private JPanel buttonPanel = null;

	private JPanel statisticPanel = null;

	private JPanel tasksPanel = null;

	private JLabel lblTimeUnit = null;

	private JLabel lblPrecision = null;

	private JTextField txtPrecision = null;

	private JLabel lblResourceUtil = null;

	private JTextField txtResourceUtil = null;

	private static TimeModel tm = null;

	/**
	 * This is the default constructor
	 */
	public CapacityAnalysisDialog(Frame owner, EditorVC editor) {
		super(owner, true);
		this.editor = editor;
		sa = new StructuralAnalysis(editor);
		mec = editor.getModelProcessor().getElementContainer();
		graph = new WorkflowNetGraph(sa, mec);
		numTrans = graph.getNumTransitions();
		initialize();
		LoggerManager.info(Constants.QUANTANA_LOGGER, Messages
				.getString("QuantAna.Started"));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints constraints = new GridBagConstraints();
		calculateNumOfRuns(graph);
		initResourceAlloc();
		numResCls = resAlloc.getNumOfResClasses();

		if (tm == null) {
			tm = new TimeModel(1, 1.0);
		}

		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(getParamPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(getTasksPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		add(getResourcesPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		add(getStatisticPanel(), constraints);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width > 800 ? 800 : screenSize.width;
		int x = screenSize.width > width ? (screenSize.width - width) / 2 : 0;
		int height = screenSize.height > 700 ? 700 : screenSize.height;
		int y = screenSize.height > height ? (screenSize.height - height) / 2
				: 0;
		this.setBounds(x, y, width, height);
		this.setTitle(Messages.getTitle("QuantAna.CapacityPlanning"));

		this.updTableContents();
		this.setVisible(true);
	}

	private JPanel getParamPanel() {
		if (paramPanel == null) {
			paramPanel = new JPanel();
			paramPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.CapacityPlanning.Heading0")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			paramPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			paramPanel.add(getValuePanel(), constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 195, 5, 5);
			paramPanel.add(getButtonPanel(), constraints);
			paramPanel.setMinimumSize(new Dimension(780, 115));
		}
		return paramPanel;
	}

	private JPanel getValuePanel() {
		if (valuePanel == null) {
			valuePanel = new JPanel();
			valuePanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.fill = GridBagConstraints.HORIZONTAL;

			lblPeriod = new JLabel(Messages
					.getString("QuantAna.Config.ObservationPeriod"));
			lblPeriod.setMinimumSize(new Dimension(200, 20));
			lblPeriod.setMaximumSize(new Dimension(200, 20));
			lblPeriod.setPreferredSize(new Dimension(200, 20));
			constraints.gridx = 0;
			constraints.gridy = 0;
			valuePanel.add(lblPeriod, constraints);

			lblCases = new JLabel(Messages
					.getString("QuantAna.Config.ArrivalRate"));
			lblCases.setMinimumSize(new Dimension(200, 20));
			lblCases.setMaximumSize(new Dimension(200, 20));
			lblCases.setPreferredSize(new Dimension(200, 20));
			constraints.gridx = 0;
			constraints.gridy = 1;
			valuePanel.add(lblCases, constraints);

			lblThreshold = new JLabel(Messages
					.getString("QuantAna.Config.TerminationThreshold"));
			lblThreshold.setMinimumSize(new Dimension(200, 20));
			lblThreshold.setMaximumSize(new Dimension(200, 20));
			lblThreshold.setPreferredSize(new Dimension(200, 20));
			constraints.gridx = 0;
			constraints.gridy = 2;
			valuePanel.add(lblThreshold, constraints);

			txtPeriod = new JTextField();
			txtPeriod.setMinimumSize(new Dimension(100, 20));
			txtPeriod.setMaximumSize(new Dimension(100, 20));
			txtPeriod.setPreferredSize(new Dimension(100, 20));
			txtPeriod.setText(Double.valueOf(period).toString());
			constraints.gridx = 1;
			constraints.gridy = 0;
			valuePanel.add(txtPeriod, constraints);

			txtCases = new JTextField();
			txtCases.setMinimumSize(new Dimension(100, 20));
			txtCases.setMaximumSize(new Dimension(100, 20));
			txtCases.setPreferredSize(new Dimension(100, 20));
			txtCases.setText(Double.valueOf(lambda).toString());
			constraints.gridx = 1;
			constraints.gridy = 1;
			valuePanel.add(txtCases, constraints);

			txtThreshold = new JTextField();
			txtThreshold.setMinimumSize(new Dimension(100, 20));
			txtThreshold.setMaximumSize(new Dimension(100, 20));
			txtThreshold.setPreferredSize(new Dimension(100, 20));
			txtThreshold.setText(Double.valueOf(epsilon).toString());
			constraints.gridx = 1;
			constraints.gridy = 2;
			valuePanel.add(txtThreshold, constraints);

			cboPeriodUnit = new JComboBox(Constants.TIMEUNITS);
			cboPeriodUnit.setMinimumSize(new Dimension(110, 20));
			cboPeriodUnit.setMaximumSize(new Dimension(110, 20));
			cboPeriodUnit.setPreferredSize(new Dimension(110, 20));
			cboPeriodUnit.setSelectedItem(periodIntervall);
			constraints.gridx = 2;
			constraints.gridy = 0;
			valuePanel.add(cboPeriodUnit, constraints);
		}
		return valuePanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.EAST;
			constraints.fill = GridBagConstraints.HORIZONTAL;

			JButton btnCalc = new JButton();
			btnCalc.setText(Messages.getTitle("QuantAna.Button.Compute"));
			btnCalc.setIcon(Messages.getImageIcon("QuantAna.Button.Compute"));
			btnCalc.setMinimumSize(new Dimension(120, 25));
			btnCalc.setMaximumSize(new Dimension(120, 25));
			btnCalc.setPreferredSize(new Dimension(120, 25));
			btnCalc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					updTableContents();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttonPanel.add(btnCalc, constraints);

			JButton btnConf = new JButton();
			btnConf.setText(Messages.getTitle("QuantAna.Button.TimeModel"));
			btnConf.setIcon(Messages.getImageIcon("QuantAna.Button.TimeModel"));
			btnConf.setMinimumSize(new Dimension(120, 25));
			btnConf.setMaximumSize(new Dimension(120, 25));
			btnConf.setPreferredSize(new Dimension(120, 25));
			btnConf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					getTimeModelDialog();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 1;
			buttonPanel.add(btnConf, constraints);
		}
		return buttonPanel;
	}

	private JPanel getTasksPanel() {
		if (tasksPanel == null) {
			tasksPanel = new JPanel();
			tasksPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.CapacityPlanning.Heading1")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			tasksPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.anchor = GridBagConstraints.EAST;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			tasksPanel.add(getPrecisionPanel(), constraints);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 1;
			tasksPanel.add(getTasksTablePane(), constraints);
			tasksPanel.setMinimumSize(new Dimension(780, 240));
		}
		return tasksPanel;
	}

	private JPanel getPrecisionPanel() {
		if (precisionPanel == null) {
			precisionPanel = new JPanel();
			precisionPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			constraints.insets = new Insets(5, 5, 5, 30);
			constraints.gridx = 0;
			constraints.gridy = 0;
			lblTimeUnit = new JLabel();
			precisionPanel.add(lblTimeUnit, constraints);

			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridx = 1;
			constraints.gridy = 0;
			lblPrecision = new JLabel(Messages.getString("QuantAna.CapacityPlanning.Precision"));
			precisionPanel.add(lblPrecision, constraints);

			constraints.insets = new Insets(5, 5, 5, 0);
			constraints.gridx = 2;
			constraints.gridy = 0;
			JButton leftButton = new JButton();
			leftButton.setIcon(Messages.getImageIcon("QuantAna.Button.Left"));
			leftButton.setContentAreaFilled(false);
			leftButton.setBorderPainted(false);
			leftButton.setFocusPainted(false);
			leftButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (currPrec > 0)
						currPrec--;
					txtPrecision.setText(String.format("%8." + currPrec + "f",
							1.0));
					showPrecision();
				}
			});
			precisionPanel.add(leftButton, constraints);

			constraints.insets = new Insets(5, 0, 5, 0);
			constraints.gridx = 3;
			constraints.gridy = 0;
			txtPrecision = new JTextField();
			txtPrecision.setEditable(false);
			txtPrecision.setMinimumSize(new Dimension(80, 20));
			txtPrecision.setMaximumSize(new Dimension(80, 20));
			txtPrecision.setPreferredSize(new Dimension(80, 20));
			txtPrecision.setHorizontalAlignment(JTextField.RIGHT);
			txtPrecision.setText(String.format("%8." + currPrec + "f", 1.0));
			precisionPanel.add(txtPrecision, constraints);

			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.gridx = 4;
			constraints.gridy = 0;
			JButton rightButton = new JButton();
			rightButton.setIcon(Messages.getImageIcon("QuantAna.Button.Right"));
			rightButton.setContentAreaFilled(false);
			rightButton.setBorderPainted(false);
			rightButton.setFocusPainted(false);
			rightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (currPrec < 6)
						currPrec++;
					txtPrecision.setText(String.format("%8." + currPrec + "f",
							1.0));
					showPrecision();
				}
			});
			precisionPanel.add(rightButton, constraints);
		}

		return precisionPanel;
	}

	private JScrollPane getTasksTablePane() {
		if (tasksTablePane == null) {
			tasksTablePane = new JScrollPane(getTasksTable());
			tasksTablePane.setBorder(BorderFactory.createEmptyBorder());
			tasksTablePane.setWheelScrollingEnabled(true);
			tasksTablePane.setMinimumSize(new Dimension(750, 180));

		}
		return tasksTablePane;
	}

	private JPanel getResourcesPanel() {
		if (resourcesPanel == null) {
			resourcesPanel = new JPanel();
			resourcesPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.CapacityPlanning.Heading2")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			resourcesPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 0, 0, 0);
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.anchor = GridBagConstraints.EAST;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			resourcesPanel.add(getResourceUtilPanel(), constraints);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 1;
			resourcesPanel.add(getResourcesTablePane(), constraints);
			resourcesPanel.setMinimumSize(new Dimension(780, 240));
		}
		return resourcesPanel;
	}

	private JPanel getResourceUtilPanel() {
		if (resourceUtilPanel == null) {
			resourceUtilPanel = new JPanel();
			resourceUtilPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			constraints.gridx = 0;
			constraints.gridy = 0;
			lblResourceUtil = new JLabel(Messages
					.getString("QuantAna.CapacityPlanning.Utilization"));
			resourceUtilPanel.add(lblResourceUtil, constraints);

			constraints.insets = new Insets(5, 5, 5, 0);
			constraints.gridx = 1;
			constraints.gridy = 0;
			JButton leftButton = new JButton();
			leftButton.setIcon(Messages.getImageIcon("QuantAna.Button.Left"));
			leftButton.setContentAreaFilled(false);
			leftButton.setBorderPainted(false);
			leftButton.setFocusPainted(false);
			leftButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (currUtil > 4)
						currUtil -= 5;
					txtResourceUtil.setText(currUtil + "");
					updResAlloc();
				}
			});
			resourceUtilPanel.add(leftButton, constraints);

			constraints.insets = new Insets(5, 0, 5, 0);
			constraints.gridx = 2;
			constraints.gridy = 0;
			txtResourceUtil = new JTextField();
			txtResourceUtil.setEditable(false);
			txtResourceUtil.setMinimumSize(new Dimension(80, 20));
			txtResourceUtil.setMaximumSize(new Dimension(80, 20));
			txtResourceUtil.setPreferredSize(new Dimension(80, 20));
			txtResourceUtil.setHorizontalAlignment(JTextField.RIGHT);
			txtResourceUtil.setText(currUtil + "");
			resourceUtilPanel.add(txtResourceUtil, constraints);

			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.gridx = 3;
			constraints.gridy = 0;
			JButton rightButton = new JButton();
			rightButton.setIcon(Messages.getImageIcon("QuantAna.Button.Right"));
			rightButton.setContentAreaFilled(false);
			rightButton.setBorderPainted(false);
			rightButton.setFocusPainted(false);
			rightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (currUtil < 96)
						currUtil += 5;
					txtResourceUtil.setText(currUtil + "");
					updResAlloc();
				}
			});
			resourceUtilPanel.add(rightButton, constraints);
		}

		return resourceUtilPanel;
	}

	/*
	 * private JPanel getResourceUtilPanel1() { if (resourceUtilPanel == null) {
	 * resourceUtilPanel = new JPanel(); resourceUtilPanel.setLayout(new
	 * FlowLayout(FlowLayout.RIGHT));
	 * 
	 * lblCapaLevel = new JLabel(Messages
	 * .getString("QuantAna.CapacityPlanning.Utilization"));
	 * lblCapaLevel.setAlignmentX(JLabel.RIGHT_ALIGNMENT); jslCapaLevel = new
	 * JSlider(10, 100, 80); jslCapaLevel.setPaintTicks(true);
	 * jslCapaLevel.setPaintLabels(true); jslCapaLevel.setMajorTickSpacing(10);
	 * jslCapaLevel.setMinorTickSpacing(5); jslCapaLevel.setPreferredSize(new
	 * Dimension(200, 40)); jslCapaLevel.addChangeListener(new ChangeListener() {
	 * public void stateChanged(ChangeEvent e) { int val = ((JSlider)
	 * e.getSource()).getValue(); lblLevelDisplay.setText(Integer.toString(val) + "
	 * %"); } });
	 * 
	 * jslCapaLevel.addMouseListener(new MouseListener() { public void
	 * mouseClicked(MouseEvent e) { }
	 * 
	 * public void mouseReleased(MouseEvent e) { updateResAlloc(); }
	 * 
	 * public void mouseExited(MouseEvent e) { }
	 * 
	 * public void mousePressed(MouseEvent e) { }
	 * 
	 * public void mouseEntered(MouseEvent e) { } });
	 * 
	 * capaLevel = jslCapaLevel.getValue() / 100.0; lblLevelDisplay = new
	 * JLabel(Integer.toString(jslCapaLevel .getValue()) + " %", JLabel.CENTER);
	 * lblLevelDisplay.setAlignmentX(JLabel.RIGHT_ALIGNMENT); lblLevelDisplay
	 * .setFont(DefaultStaticConfiguration.DEFAULT_TABLE_FONT);
	 * lblLevelDisplay.setBorder(BorderFactory.createEtchedBorder());
	 * lblLevelDisplay.setPreferredSize(new Dimension(50, 30));
	 * resourceUtilPanel.add(lblCapaLevel);
	 * resourceUtilPanel.add(lblLevelDisplay);
	 * resourceUtilPanel.add(jslCapaLevel); resourceUtilPanel.setMinimumSize(new
	 * Dimension(450, 45)); } return resourceUtilPanel; }
	 */
	private JScrollPane getResourcesTablePane() {
		if (resourcesTablePane == null) {
			resourcesTablePane = new JScrollPane(getResourcesTable());
			resourcesTablePane.setBorder(BorderFactory.createEmptyBorder());
			resourcesTablePane.setWheelScrollingEnabled(true);
			resourcesTablePane.setMinimumSize(new Dimension(750, 180));
		}
		return resourcesTablePane;
	}

	private JTable getTasksTable() {
		if (tasksTable == null) {
			tasksMatrix = new double[numTrans + 1][colNames.length - 1];
			tableTasksMatrix = new Object[numTrans + 1][colNames.length];
			String[] trans = graph.getTransitions();
			double[] times = graph.getTimes();
			double[] runs = graph.getRuns();
			int j = trans.length;

			for (int i = 0; i < j; i++) {
				tasksMatrix[i][1] = runs[i];
				tasksMatrix[i][2] = times[i];

				tableTasksMatrix[i][0] = trans[i];
				tableTasksMatrix[i][2] = String.format("%15." + currPrec + "f",
						runs[i]);
				tableTasksMatrix[i][3] = String.format("%15." + currPrec + "f",
						times[i]);
			}

			tasksMatrix[j][2] = 0.0;
			tasksMatrix[j][3] = 0.0;

			tableTasksMatrix[j][0] = Messages
					.getString("QuantAna.CapacityPlanning.Aggregate");

			tmTasks = new TasksTableModel(colNames, tableTasksMatrix);
			tmTasks.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
				}
			});
			tasksTable = new JTable(tmTasks) {
				private static final long serialVersionUID = 11L;

				// Implement table header tool tips.
				protected JTableHeader createDefaultTableHeader() {
					JTableHeader jt = new JTableHeader(columnModel) {
						private static final long serialVersionUID = 12L;

						public String getToolTipText(MouseEvent e) {
							java.awt.Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index)
									.getModelIndex();
							return colToolTips1[realIndex];
						}
					};
					jt.setDefaultRenderer(new MyTableHeaderRenderer());
					return jt;
				}
			};
			tasksTable.setDefaultRenderer(Object.class,
					new MyTableCellRenderer());
			tasksTable.setEnabled(false);
		}
		return tasksTable;
	}

	private JTable getResourcesTable() {
		if (resourcesTable == null) {
			resMatrix = new double[numResCls][colNames2.length - 1];
			tableResMatrix = new Object[numResCls][colNames2.length];
			ArrayList<ResourceClassTaskAllocation> resTab = resAlloc
					.getResClsTskAlloc().getTable();
			for (int i = 0; i < resTab.size(); i++) {
				tableResMatrix[i][0] = resTab.get(i).getResClass();
			}
			tmRes = new ResTableModel(colNames2, tableResMatrix);
			tmRes.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					// JOptionPane.showMessageDialog(null, e.toString());
				}
			});

			resourcesTable = new JTable(tmRes) {
				private static final long serialVersionUID = 21L;

				// Implement table header tool tips.
				protected JTableHeader createDefaultTableHeader() {
					JTableHeader jt = new JTableHeader(columnModel) {
						private static final long serialVersionUID = 22L;

						public String getToolTipText(MouseEvent e) {
							java.awt.Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index)
									.getModelIndex();
							return colToolTips2[realIndex];
						}
					};
					jt.setDefaultRenderer(new MyTableHeaderRenderer());
					return jt;
				}
			};
			resourcesTable.setDefaultRenderer(Object.class,
					new MyTableCellRenderer());
			resourcesTable.setEnabled(false);
		}
		return resourcesTable;
	}

	private JPanel getStatisticPanel() {
		if (statisticPanel == null) {
			lblUnfolding = new JLabel("");
			lblDeviation = new JLabel("");
			lblUnfolding.setFont(DefaultStaticConfiguration.DEFAULT_TABLE_FONT);
			lblDeviation.setFont(DefaultStaticConfiguration.DEFAULT_TABLE_FONT);
			statisticPanel = new JPanel();
			statisticPanel.add(lblUnfolding);
			statisticPanel.add(lblDeviation);
		}

		return statisticPanel;
	}

	private void calculateNumOfRuns(WorkflowNetGraph g) {
		unfoldNet(graph, lambda, epsilon);

		Node[] origNet = graph.getNodeArray();
		for (Key k : unfoldedNet.keySet()) {
			String id = k.getId();
			Node n = origNet[graph.getNodeIdx(id)];
			n.setNumOfRuns(n.getNumOfRuns() + k.getRuns());
			n.setIteration(n.getIteration() + 1);
		}
	}

	private void unfoldNet(WorkflowNetGraph g, double l, double e) {
		unfoldedNet.clear();
		for (Node n : g.getNodeArray()) {
			if (n.isJoinReached())
				n.setJoinReached(false);

			n.setNumOfRuns(0);
		}

		Node s = g.getStartPlace();
		Node n = new Node(s.getId(), s.getName());
		n.setTempRuns(lambda);
		Key start = new Key(n.getId(), lambda);
		unfoldedNet.put(start, n);

		LinkedList<NodePair> queue = new LinkedList<NodePair>();
		queue.add(new NodePair(s, n));
		runThroughNet(queue);
	}

	// rekursive Funktion mit Breitensuche
	private void runThroughNet(LinkedList<NodePair> q) {
		if (!(q.isEmpty())) {
			NodePair np = q.removeFirst();
			for (Arc a : np.getFirst().getSuccessor()) {
				Node m = a.getTarget();
				if (!m.isJoinReached()) {
					double val = a.getProbability()
							* np.getSecond().getTempRuns();
					if (!(val < epsilon)) {
						Node y = new Node(m.getId(), m.getName());
						y.setTempRuns(val);
						if (m.isAndJoin())
							m.setJoinReached(true);
						np.getSecond().getSuccessor().add(
								new Arc(y, a.getProbability()));
						Key k = new Key(m.getId(), val);
						unfoldedNet.put(k, y);

						if (!(containsElement(q, m)))
							q.add(new NodePair(m, y));
						runThroughNet(q);
					}
				}
			}
		}
	}

	private boolean containsElement(LinkedList<NodePair> q, Node n) {
		boolean contains = false;
		Iterator<NodePair> i = q.iterator();
		while (i.hasNext()) {
			NodePair p = i.next();
			if (n.equals(p.getFirst())) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	private void initResourceAlloc() {
		PetriNetModelProcessor pmp = (PetriNetModelProcessor) editor
				.getModelProcessor();

		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> groups = new ArrayList<String>();
		Vector rVec = (Vector) pmp.getRoles();
		Vector gVec = (Vector) pmp.getOrganizationUnits();

		for (int i = 0; i < rVec.size(); i++)
			roles.add(((ResourceClassModel) rVec.get(i)).getName());

		for (int i = 0; i < gVec.size(); i++)
			groups.add(((ResourceClassModel) gVec.get(i)).getName());

		Iterator iter = getTransModels().iterator();

		resAlloc = new ResourceAllocation(roles, groups, iter, pmp);
	}

	private LinkedList<TransitionModel> getTransModels() {
		LinkedList<TransitionModel> lst = new LinkedList<TransitionModel>();
		ArrayList<String> ids = new ArrayList<String>();
		Node[] nodes = graph.getNodeArray();

		for (int i = 0; i < nodes.length; i++)
			if (graph.isTransition(nodes[i].getId()))
				ids.add(nodes[i].getId());

		for (int i = 0; i < ids.size(); i++) {
			lst.add((TransitionModel) mec.getElementById(ids.get(i)));
		}

		return lst;
	}

	static class MyTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			if (column == 0)
				setHorizontalAlignment(LEFT);
			else
				setHorizontalAlignment(RIGHT);
			setFont(DefaultStaticConfiguration.DEFAULT_TABLE_FONT);
			setBackground(DefaultStaticConfiguration.DEFAULT_CELL_BACKGROUND_COLOR);
			return this;
		}
	}

	static class MyTableHeaderRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			if (column == 0)
				setHorizontalAlignment(LEFT);
			else
				setHorizontalAlignment(RIGHT);
			setFont(DefaultStaticConfiguration.DEFAULT_TABLE_BOLDFONT);
			setBackground(DefaultStaticConfiguration.DEFAULT_HEADER_BACKGROUND_COLOR);
			return this;
		}
	}

	private void getTimeModelDialog() {
		new TimeModelDialog(this, tm);
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public String getPeriodIntervall() {
		return periodIntervall;
	}

	public void setPeriodIntervall(String periodIntervall) {
		this.periodIntervall = periodIntervall;
	}

	public String getTimeIntervall() {
		return timeIntervall;
	}

	public void setTimeIntervall(String timeIntervall) {
		this.timeIntervall = timeIntervall;
	}

	public double getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(double timeUnit) {
		this.timeUnit = timeUnit;
	}

	public WorkflowNetGraph getGraph() {
		return graph;
	}

	public void setGraph(WorkflowNetGraph graph) {
		this.graph = graph;
	}

	public void updParameters() {
		setLambda(Double.parseDouble(txtCases.getText()));
		setEpsilon(Double.parseDouble(txtThreshold.getText()));
		setTimeIntervall(timeIntervall);
		setTimeUnit(timeUnit);
		setPeriod(tm.cv(cboPeriodUnit.getSelectedIndex(), Double
				.parseDouble(txtPeriod.getText())));
		setPeriodIntervall(convTIndexToString(tm.getStdUnit()));
		lblTimeUnit.setText(Messages.getString("QuantAna.CapacityPlanning.TimeUnit") +
				" " + tm.getStdUnitMultiple() + " " + 
				Constants.TIMEUNITS[tm.getStdUnit()]
				);
	}

	public void updTableContents() {
		Object[][] tb = tableTasksMatrix;
		Object[][] tr = tableResMatrix;
		double sumCase = 0.0;
		double sumPeriod = 0.0;

		updParameters();
		calculateNumOfRuns(graph);
		double[] runs = graph.getRuns();
		double[] times = graph.getTimes();

		for (int r = 0; r < numTrans; r++) {
			tasksMatrix[r][1] = runs[r];
			tasksMatrix[r][2] = times[r];
			tb[r][2] = String.format("%15." + currPrec + "f", runs[r]);
			;
			tmTasks.fireTableCellUpdated(r, 2);
			tb[r][3] = String.format("%15." + currPrec + "f", runs[r]);
			tmTasks.fireTableCellUpdated(r, 3);

			double n = tasksMatrix[r][1];
			double n1 = n / lambda;
			double t = tasksMatrix[r][2];

			sumCase += n1 * t;
			sumPeriod += n * t;

			tasksMatrix[r][0] = n1;
			tasksMatrix[r][3] = n1 * t;
			tasksMatrix[r][4] = n * t;

			tb[r][1] = String.format("%15." + currPrec + "f", n1);
			tmTasks.fireTableCellUpdated(r, 1);
			tb[r][4] = String.format("%15." + currPrec + "f", n1 * t);
			tmTasks.fireTableCellUpdated(r, 4);
			tb[r][5] = String.format("%15." + currPrec + "f", n * t);
			tmTasks.fireTableCellUpdated(r, 5);
		}

		tasksMatrix[numTrans][0] = 0.0;
		tasksMatrix[numTrans][3] = sumCase;
		tasksMatrix[numTrans][4] = sumPeriod;

		tb[numTrans][4] = String.format("%15." + currPrec + "f", sumCase);// Double.valueOf(sumCase);
		tmTasks.fireTableCellUpdated(numTrans, 4);
		tb[numTrans][5] = String.format("%15." + currPrec + "f", sumPeriod);// Double.valueOf(sumPeriod);
		tmTasks.fireTableCellUpdated(numTrans, 5);

		ResourceClassTaskAllocationTable rcta = resAlloc.getResClsTskAlloc();

		for (int i = 0; i < numResCls; i++) {
			double sum = 0.0;
			ArrayList<String> min = rcta.getTable().get(i).getTasks();
			for (int j = 0; j < min.size(); j++) {
				int idx = -1;
				for (int k = 0; k < numTrans; k++) {
					if (((String) tb[k][0]).equals(min.get(j)))
						idx = k;
				}

				if (idx != -1)
					sum += tasksMatrix[idx][4];
			}

			double numRes = (sum / period) / (currUtil / 100.0);
			resMatrix[i][0] = sum;
			resMatrix[i][1] = numRes;

			tr[i][1] = String.format("%15." + currPrec + "f", sum);
			tmRes.fireTableCellUpdated(i, 1);
			tr[i][2] = String.format("%15." + currPrec + "f", numRes);
			tmRes.fireTableCellUpdated(i, 2);
		}

		lblUnfolding.setText("The unfolded net has " + unfoldedNet.size()
				+ " nodes.");
		String dev = String.format("%+6.1f", (1 - graph.getSinkPlace()
				.getNumOfRuns()
				/ lambda) * 100);
		lblDeviation.setText("The relative deviation is (estimated): " + dev
				+ "%");
	}

	private void showPrecision() {
		int numCols1 = colNames.length;

		for (int i = 0; i < numTrans; i++) {
			for (int j = 0; j < numCols1 - 1; j++) {
				tmTasks.setValueAt(String.format("%15." + currPrec + "f",
						tasksMatrix[i][j]), i, j + 1);
			}
		}

		tmTasks.setValueAt(String.format("%15." + currPrec + "f",
				tasksMatrix[numTrans][numCols1 - 3]), numTrans, numCols1 - 2);
		tmTasks.setValueAt(String.format("%15." + currPrec + "f",
				tasksMatrix[numTrans][numCols1 - 2]), numTrans, numCols1 - 1);

		for (int i = 0; i < numResCls; i++) {
			tmRes.setValueAt(String.format("%15." + currPrec + "f",
					resMatrix[i][0]), i, 1);
			tmRes.setValueAt(String.format("%15." + currPrec + "f",
					resMatrix[i][1]), i, 2);
		}
	}

	private void updResAlloc() {
		ResourceClassTaskAllocationTable rcta = resAlloc.getResClsTskAlloc();
		Object[][] tb = tableTasksMatrix;
		Object[][] tr = tableResMatrix;

		for (int i = 0; i < numResCls; i++) {
			double sum = 0.0;
			ArrayList<String> min = rcta.getTable().get(i).getTasks();
			for (int j = 0; j < min.size(); j++) {
				int idx = -1;
				for (int k = 0; k < numTrans; k++) {
					if (((String) tb[k][0]).equals(min.get(j)))
						idx = k;
				}

				if (idx != -1)
					sum += tasksMatrix[idx][4];
			}

			double numRes = (sum / period) / (currUtil / 100.0);
			resMatrix[i][0] = sum;
			resMatrix[i][1] = numRes;

			tr[i][1] = String.format("%15." + currPrec + "f", sum);
			tmRes.fireTableCellUpdated(i, 1);
			tr[i][2] = String.format("%15." + currPrec + "f", numRes);
			tmRes.fireTableCellUpdated(i, 2);
		}
	}

	private String convTIndexToString(int u) {
		switch (u) {
		case 0:
			return Constants.TIME_SECOND;
		case 1:
			return Constants.TIME_MINUTE;
		case 2:
			return Constants.TIME_HOUR;
		case 3:
			return Constants.TIME_DAY;
		case 4:
			return Constants.TIME_WEEK;
		case 5:
			return Constants.TIME_MONTH;
		case 6:
			return Constants.TIME_YEAR;
		default:
			return "ERROR";
		}
	}
} // @jve:decl-index=0:visual-constraint="4,4"
