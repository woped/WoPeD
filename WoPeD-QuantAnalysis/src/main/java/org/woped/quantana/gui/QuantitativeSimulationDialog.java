package org.woped.quantana.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;
import org.woped.quantana.Constants;
import org.woped.quantana.dashboard.storage.SimRunnerDB;
import org.woped.quantana.dashboard.storage.StorageEngine;
import org.woped.quantana.dashboard.storage.StorageEngine.Table;
import org.woped.quantana.dashboard.webserver.ClientStarter;
import org.woped.quantana.dashboard.webserver.DashboardRunner;
import org.woped.quantana.graph.Key;
import org.woped.quantana.gui.CapacityAnalysisDialog.MyTableHeaderRenderer;
import org.woped.quantana.model.ResUtilTableModel;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.model.ServerTableModel;
import org.woped.quantana.model.TasksResourcesAllocation;
import org.woped.quantana.model.TimeModel;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceUtilization;
import org.woped.quantana.sim.SimArc;
import org.woped.quantana.sim.SimDistribution;
import org.woped.quantana.sim.SimGraph;
import org.woped.quantana.sim.SimNode;
import org.woped.quantana.sim.SimParameters;
import org.woped.quantana.sim.SimReportServerStats;
import org.woped.quantana.sim.SimRunStats;
import org.woped.quantana.sim.SimRunner;
import org.woped.quantana.sim.SimServer;

public class QuantitativeSimulationDialog extends JDialog implements
		MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private IEditor editor = null;

	private JPanel iatPanel = null;

	private JPanel stPanel = null;

	private JPanel queuePanel = null;

	private JPanel termPanel = null;

	private JPanel generalPanel = null;

	private JPanel distPanel = null;

	private JPanel statsPanel = null;

	private JPanel utilPanel = null;

	private JPanel buttonPanel = null;

	private int resObjNum = 0;

	private double period = 60.0;

	private double lambda = 50.0;

	private SimGraph simgraph;

	private ResourceAllocation resAlloc;

	private JTextField txtRuns;

	private JTextField txtLambda;

	private JTextField txtPeriod;

	private JTextField txtIATInterval;

	private JTextField txtSTInterval;

	private JTextField txtIATStdDev;

	private JTextField txtSTStdDev;

	private JComboBox<?> cboTimeUnits;

	private int timeUnit = 1;

	private int periodIndex = 2;

	private ButtonGroup groupIAT;

	private ButtonGroup groupST;

	private ButtonGroup groupQD;

	private JCheckBox stop1;

	private JCheckBox stop2;

	private JCheckBox ptk;

	private JCheckBox startDashboard;

	private JTable tableServers;

	private JScrollPane serverTableScrollPane;

	private Object[][] serverTableMatrix;

	private ServerTableModel serverTableModel;

	private JTable tableResUtil;

	private JScrollPane resUtilTableScrollPane;

	private Object[][] resUtilTableMatrix;

	private ResUtilTableModel resUtilTableModel;

	private TasksResourcesAllocation tasksAndResources;

	private TimeModel tm = null;

	private double epsilon = 0.001;

	// -->CN
	// SimRunner sim = null;

	SimRunner sim = null;

	private StorageEngine storageengine = null;
	private static DashboardRunner dashboard;
	//private static int nextport = 2711;
	public int port = 2711; //= ConfigurationManager.getConfiguration().getBusinessDashboardServerPort();

	// <--CN

	private String[] colServers = {
			Messages.getString("QuantAna.Simulation.Column.Names"),
			Messages.getString("QuantAna.Simulation.Column.L"),
			Messages.getString("QuantAna.Simulation.Column.CalcLambda"),
			Messages.getString("QuantAna.Simulation.Column.W"),
			Messages.getString("QuantAna.Simulation.Column.Ws"),
			Messages.getString("QuantAna.Simulation.Column.Wq"),
			Messages.getString("QuantAna.Simulation.Column.Details") };

	private String[] ttipsServers = {
			Messages.getString("QuantAna.Simulation.ToolTip.Names"),
			Messages.getString("QuantAna.Simulation.ToolTip.L"),
			Messages.getString("QuantAna.Simulation.ToolTip.CalcLambda"),
			Messages.getString("QuantAna.Simulation.ToolTip.W"),
			Messages.getString("QuantAna.Simulation.ToolTip.Ws"),
			Messages.getString("QuantAna.Simulation.ToolTip.Wq"),
			Messages.getString("QuantAna.Simulation.TooTip.Details") };

	private String[] colResUtil = {
			Messages.getString("QuantAna.Simulation.Column.Object"),
			Messages.getString("QuantAna.Simulation.Column.Util") };

	private String[] ttipsResUtil = {
			Messages.getString("QuantAna.Simulation.ToolTip.Object"),
			Messages.getString("QuantAna.Simulation.ToolTip.Util") };

	private int numServers;

	private Dialog thisDialog = this;

	// private JButton btnProtocol;

	// private JButton btnExport;

	private JButton btnDiagram;

	private JButton btnExportCSV;

	private JButton btnExportHTML;

	private JButton btnColumn[];

	private JButton btnStart;

	private String[] servNames;

	private ArrayList<SimRunStats> simStatistics;

	private HashMap<Key, SimNode> unfoldedNet = new HashMap<Key, SimNode>();

	// private ExportStatistics export;

	// private JFileChooser fileChooser;

	// private File dir;

	// private final ExtensionFileFilter eff = new ExtensionFileFilter();

	private boolean errorDetected = false;

	private TmpProtocolDialog logDlg;

	JFrame owner;

	/**
	 * This is the default constructor
	 */
	public QuantitativeSimulationDialog(JFrame owner, IEditor editor) {
		super(owner, Messages.getTitle("QuantAna.Simulation"), true);
		this.editor = editor;
		this.owner = owner;
		tm = new TimeModel(1, 1.0);
		simgraph = new SimGraph(editor);
		servNames = simgraph.getTransitions();
		numServers = servNames.length;
		initResourceAlloc();
		for (SimNode n : simgraph.getNodes().values()) {
			if (n.gettime() != 0) {
				n.settime(tm.cv(n.gettimeunit(), n.gettime()));
				n.settimeunit(tm.getStdUnit());
				n.setUnitFactor(tm.getStdUnitMultiple());
			}
		}
		timeUnit = tm.getStdUnit();
		logDlg = new TmpProtocolDialog(this);
		initialize();
	}

	public void showdlg() {
		simgraph = new SimGraph(editor);
		servNames = simgraph.getTransitions();
		numServers = servNames.length;
		initResourceAlloc();
		for (SimNode n : simgraph.getNodes().values()) {
			if (n.gettime() != 0) {
				n.settime(tm.cv(n.gettimeunit(), n.gettime()));
				n.settimeunit(tm.getStdUnit());
				n.setUnitFactor(tm.getStdUnitMultiple());
			}
		}
		timeUnit = tm.getStdUnit();

		getContentPane().remove(getUtilPanel());
		getContentPane().remove(getStatsPanel());

		resUtilTableScrollPane = null;
		tableResUtil = null;
		serverTableScrollPane = null;
		tableServers = null;
		statsPanel = null;
		utilPanel = null;

		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(new GridBagLayout());
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0;

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(10, 10, 5, 10);
		getContentPane().add(getGeneralPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 10, 5, 10);
		getContentPane().add(getQueuePanel(), constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		getContentPane().add(getTermPanel(), constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.insets = new Insets(10, 0, 0, 20);
		getContentPane().add(getButtonPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.insets = new Insets(0, 10, 5, 10);
		getContentPane().add(getDistPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weighty = 1;
		getContentPane().add(getStatsPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(5, 10, 5, 10);
		getContentPane().add(getUtilPanel(), constraints);

		makeTasksAndResources();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width > 820 ? 820 : screenSize.width;
		int x = screenSize.width > width ? (screenSize.width - width) / 2 : 0;
		int height = screenSize.height > 740 ? 740 : screenSize.height;
		int y = screenSize.height > height ? (screenSize.height - height) / 2
				: 0;
		this.setBounds(x, y, width, height);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnStart.requestFocus();
			}
		});
		this.setVisible(true);
	}

	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			FocusAdapter fa = new FocusAdapter() {
				public void focusGained(final FocusEvent fe) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) fe.getSource()).selectAll();
						}
					});
				}
			};
			MouseAdapter ma = new MouseAdapter() {
				public void mouseReleased(final MouseEvent me) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) me.getSource()).selectAll();
						}
					});
				}
			};

			generalPanel = new JPanel();
			generalPanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("QuantAna.Simulation.GeneralProperties")),
							BorderFactory.createEmptyBorder(5, 5, 0, 5)));
			GridBagConstraints constraints = new GridBagConstraints();
			generalPanel.setLayout(new GridBagLayout());
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			JLabel lblLambda = new JLabel(
					Messages.getString("QuantAna.Simulation.Mean"));
			lblLambda.setMinimumSize(new Dimension(100, 20));
			lblLambda.setMaximumSize(new Dimension(100, 20));
			lblLambda.setPreferredSize(new Dimension(100, 20));
			lblLambda.setHorizontalAlignment(SwingConstants.LEFT);
			constraints.insets = new Insets(5, 10, 5, 5);
			constraints.gridx = 0;
			constraints.gridy = 0;
			generalPanel.add(lblLambda, constraints);

			txtLambda = new JTextField("50");
			txtLambda.setMinimumSize(new Dimension(100, 20));
			txtLambda.setMaximumSize(new Dimension(100, 20));
			txtLambda.setPreferredSize(new Dimension(100, 20));
			txtLambda.addKeyListener(this);
			txtLambda.addFocusListener(fa);
			txtLambda.addMouseListener(ma);

			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 5, 5, 5);
			generalPanel.add(txtLambda, constraints);

			JLabel lblPeriod = new JLabel(
					Messages.getString("QuantAna.Simulation.Period"));
			lblPeriod.setMinimumSize(new Dimension(100, 20));
			lblPeriod.setMaximumSize(new Dimension(100, 20));
			lblPeriod.setPreferredSize(new Dimension(100, 20));
			lblPeriod.setHorizontalAlignment(SwingConstants.RIGHT);
			constraints.gridx = 2;
			constraints.gridy = 0;
			generalPanel.add(lblPeriod, constraints);

			txtPeriod = new JTextField("8.0");
			txtPeriod.setMinimumSize(new Dimension(100, 20));
			txtPeriod.setMaximumSize(new Dimension(100, 20));
			txtPeriod.setPreferredSize(new Dimension(100, 20));
			txtPeriod.addKeyListener(this);
			txtPeriod.addFocusListener(fa);
			txtPeriod.addMouseListener(ma);
			constraints.gridx = 3;
			constraints.gridy = 0;
			generalPanel.add(txtPeriod, constraints);

			cboTimeUnits = new JComboBox(Constants.TIMEUNITS);
			cboTimeUnits.setMinimumSize(new Dimension(120, 20));
			cboTimeUnits.setMaximumSize(new Dimension(120, 20));
			cboTimeUnits.setPreferredSize(new Dimension(120, 20));
			cboTimeUnits.setSelectedIndex(periodIndex);
			constraints.gridx = 4;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 5, 5, 20);
			generalPanel.add(cboTimeUnits, constraints);
		}

		return generalPanel;
	}

	private JPanel getDistPanel() {
		if (distPanel == null) {
			distPanel = new JPanel();
			distPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			constraints.insets = new Insets(5, 0, 5, 20);
			constraints.gridx = 0;
			constraints.gridy = 0;
			distPanel.add(getIATPanel(), constraints);

			constraints.insets = new Insets(5, 0, 5, 0);
			constraints.gridx = 1;
			constraints.gridy = 0;
			distPanel.add(getSTPanel(), constraints);
		}

		return distPanel;
	}

	private JPanel getStatsPanel() {
		if (statsPanel == null) {
			statsPanel = new JPanel();
			statsPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.Simulation.ServerStats")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			statsPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.weightx = 0;
			constraints.weighty = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			statsPanel.add(getServerTableScrollPane(), constraints);
			statsPanel.setMinimumSize(new Dimension(780, 140));
		}

		return statsPanel;
	}

	private JScrollPane getServerTableScrollPane() {
		if (serverTableScrollPane == null) {
			serverTableScrollPane = new JScrollPane(getServerTable());
			serverTableScrollPane.setBorder(BorderFactory.createEmptyBorder());
			serverTableScrollPane.setWheelScrollingEnabled(true);
			serverTableScrollPane.setMinimumSize(new Dimension(780, 120));
		}
		return serverTableScrollPane;
	}

	private JTable getServerTable() {
		if (tableServers == null) {
			serverTableMatrix = new Object[numServers + 1][colServers.length];

			serverTableMatrix[0][0] = Messages
					.getString("QuantAna.Simulation.Process");

			for (int i = 1; i <= numServers; i++) {
				serverTableMatrix[i][0] = servNames[i - 1];
			}

			serverTableModel = new ServerTableModel(colServers,
					serverTableMatrix);
			serverTableModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
				}
			});

			tableServers = new JTable(serverTableModel) {
				private static final long serialVersionUID = 11L;

				// Implement table header tool tips.
				protected JTableHeader createDefaultTableHeader() {
					JTableHeader jt = new JTableHeader(columnModel) {
						private static final long serialVersionUID = 12L;

						public String getToolTipText(MouseEvent e) {
							Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index)
									.getModelIndex();
							return ttipsServers[realIndex];
						}
					};
					jt.setDefaultRenderer(new MyTableHeaderRenderer());
					return jt;
				}
			};

			tableServers.setFocusable(false);
			tableServers.setCellSelectionEnabled(false);

			btnColumn = new JButton[numServers + 1];
			for (int i = 0; i < btnColumn.length; i++) {
				btnColumn[i] = new JButton("...");
				btnColumn[i].setSize(10, 10);
				btnColumn[i].setEnabled(false);
				btnColumn[i].setActionCommand(new Integer(i).toString());
				btnColumn[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int row = Integer.parseInt(e.getActionCommand());
						String name = (String) serverTableModel.getValueAt(row,
								0);
						new DetailsDialog(thisDialog, name);
					}
				});

			}

			MyTableCellRenderer mt = new MyTableCellRenderer();
			tableServers.setDefaultRenderer(Object.class, mt);
			tableServers.setDefaultEditor(Object.class, mt);
		}

		return tableServers;
	}

	private JPanel getUtilPanel() {
		if (utilPanel == null) {
			utilPanel = new JPanel();
			utilPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.Simulation.ResUtil")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			utilPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.weightx = 0;
			constraints.weighty = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			utilPanel.add(getResUtilTableScrollPane(), constraints);
			utilPanel.setMinimumSize(new Dimension(780, 120));
		}
		return utilPanel;
	}

	private JScrollPane getResUtilTableScrollPane() {
		if (resUtilTableScrollPane == null) {
			resUtilTableScrollPane = new JScrollPane(getResUtilTable());
			resUtilTableScrollPane.setBorder(BorderFactory.createEmptyBorder());
			resUtilTableScrollPane.setWheelScrollingEnabled(true);
			resUtilTableScrollPane.setMinimumSize(new Dimension(780, 120));
		}
		return resUtilTableScrollPane;
	}

	private JTable getResUtilTable() {
		if (tableResUtil == null) {
			resUtilTableMatrix = new Object[resObjNum][colResUtil.length];
			Object[] resObjNames = resAlloc.getResources().values().toArray();

			for (int i = 0; i < resObjNum; i++) {
				resUtilTableMatrix[i][0] = ((Resource) resObjNames[i])
						.getName();
			}

			resUtilTableModel = new ResUtilTableModel(colResUtil,
					resUtilTableMatrix);
			resUtilTableModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
				}
			});

			tableResUtil = new JTable(resUtilTableModel) {
				private static final long serialVersionUID = 11L;

				// Implement table header tool tips.
				protected JTableHeader createDefaultTableHeader() {
					JTableHeader jt = new JTableHeader(columnModel) {
						private static final long serialVersionUID = 12L;

						public String getToolTipText(MouseEvent e) {
							Point p = e.getPoint();
							int index = columnModel.getColumnIndexAtX(p.x);
							int realIndex = columnModel.getColumn(index)
									.getModelIndex();
							return ttipsResUtil[realIndex];
						}
					};
					jt.setDefaultRenderer(new MyTableHeaderRenderer());
					return jt;
				}
			};

			tableResUtil.setDefaultRenderer(Object.class,
					new MyTableCellRenderer());
			tableResUtil.setFocusable(false);
			tableResUtil.setCellSelectionEnabled(false);

			tableResUtil.setEnabled(false);
		}

		return tableResUtil;
	}

	class MyTableCellRenderer extends DefaultTableCellRenderer implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			setBackground(DefaultStaticConfiguration.DEFAULT_CELL_BACKGROUND_COLOR);
			if (row == 0) {
//				setFont(DefaultStaticConfiguration.DEFAULT_TABLE_BOLDFONT);
			} else {
				setFont(DefaultStaticConfiguration.DEFAULT_TABLE_FONT);
			}

			if (column == 0) {
				setHorizontalAlignment(LEFT);
			} else if (column == 6) {
				setHorizontalAlignment(CENTER);

				return btnColumn[row];
			} else {
				setHorizontalAlignment(RIGHT);
			}
			return this;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			return btnColumn[row];
		}

		public String getCellEditorValue() {
			return "Edit...";
		}

		public void addCellEditorListener(CellEditorListener l) {
		}

		public void cancelCellEditing() {
		}

		public boolean isCellEditable(EventObject anEvent) {
			return true;
		}

		public void removeCellEditorListener(CellEditorListener l) {
		}

		public boolean shouldSelectCell(EventObject anEvent) {
			return false;
		}

		public boolean stopCellEditing() {
			return true;
		}
	}

	private JPanel getIATPanel() {
		if (iatPanel == null) {

			FocusAdapter fa = new FocusAdapter() {
				public void focusGained(final FocusEvent fe) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) fe.getSource()).selectAll();
						}
					});
				}
			};
			MouseAdapter ma = new MouseAdapter() {
				public void mouseReleased(final MouseEvent me) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) me.getSource()).selectAll();
						}
					});
				}
			};

			iatPanel = new JPanel();
			iatPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
							.createTitledBorder(Messages
									.getString("QuantAna.Simulation.ArrivalRateDistribution")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));
			GridBagConstraints constraints = new GridBagConstraints();
			iatPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			groupIAT = new ButtonGroup();
			JRadioButton optIATConstant = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Constant"), false);
			JRadioButton optIATPoisson = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Poisson"), true);
			JRadioButton optIATGaussian = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Gaussian"), false);
			optIATConstant.setHorizontalAlignment(SwingConstants.LEFT);
			optIATConstant.setActionCommand("IAT_UNIFORM");
			optIATConstant.setPreferredSize(new Dimension(100, 20));
			optIATConstant.setMinimumSize(new Dimension(100, 20));
			optIATConstant.setMaximumSize(new Dimension(100, 20));
			optIATConstant.addKeyListener(this);
			optIATPoisson.setHorizontalAlignment(SwingConstants.LEFT);
			optIATPoisson.setActionCommand("IAT_EXP");
			optIATPoisson.setPreferredSize(new Dimension(100, 20));
			optIATPoisson.setMinimumSize(new Dimension(100, 20));
			optIATPoisson.setMaximumSize(new Dimension(100, 20));
			optIATPoisson.addKeyListener(this);

			optIATGaussian.setHorizontalAlignment(SwingConstants.LEFT);
			optIATGaussian.setActionCommand("IAT_GAUSS");
			optIATGaussian.setPreferredSize(new Dimension(100, 20));
			optIATGaussian.setMinimumSize(new Dimension(100, 20));
			optIATGaussian.setMaximumSize(new Dimension(100, 20));
			optIATGaussian.addKeyListener(this);

			groupIAT.add(optIATConstant);
			groupIAT.add(optIATPoisson);
			groupIAT.add(optIATGaussian);

			txtIATInterval = new JTextField("0");
			txtIATInterval.setEnabled(false);
			txtIATInterval.setMinimumSize(new Dimension(40, 20));
			txtIATInterval.setMaximumSize(new Dimension(40, 20));
			txtIATInterval.setPreferredSize(new Dimension(40, 20));
			txtIATInterval.addKeyListener(this);
			txtIATInterval.addFocusListener(fa);
			txtIATInterval.addMouseListener(ma);

			txtIATStdDev = new JTextField("0.5");
			txtIATStdDev.setEnabled(false);
			txtIATStdDev.setMinimumSize(new Dimension(40, 20));
			txtIATStdDev.setMaximumSize(new Dimension(40, 20));
			txtIATStdDev.setPreferredSize(new Dimension(40, 20));
			txtIATStdDev.addKeyListener(this);
			txtIATStdDev.addFocusListener(fa);
			txtIATStdDev.addMouseListener(ma);

			JLabel lblInterval = new JLabel(
					Messages.getString("QuantAna.Simulation.Interval"));
			lblInterval.setMinimumSize(new Dimension(120, 20));
			lblInterval.setMaximumSize(new Dimension(120, 20));
			lblInterval.setPreferredSize(new Dimension(120, 20));
			lblInterval.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lblDeviation = new JLabel(
					Messages.getString("QuantAna.Simulation.Deviation"));
			lblDeviation.setMinimumSize(new Dimension(120, 20));
			lblDeviation.setMaximumSize(new Dimension(120, 20));
			lblDeviation.setPreferredSize(new Dimension(120, 20));
			lblDeviation.setHorizontalAlignment(SwingConstants.RIGHT);

			constraints.gridx = 0;
			constraints.gridy = 0;
			iatPanel.add(optIATConstant, constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			iatPanel.add(lblInterval, constraints);
			constraints.gridx = 2;
			constraints.gridy = 0;
			iatPanel.add(txtIATInterval, constraints);
			constraints.gridx = 3;
			constraints.gridy = 0;
			iatPanel.add(new JLabel("%"), constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			iatPanel.add(optIATPoisson, constraints);
			constraints.gridx = 0;
			constraints.gridy = 2;
			iatPanel.add(optIATGaussian, constraints);
			constraints.gridx = 1;
			constraints.gridy = 2;
			iatPanel.add(lblDeviation, constraints);
			constraints.gridx = 2;
			constraints.gridy = 2;
			iatPanel.add(txtIATStdDev, constraints);

			optIATConstant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtIATInterval.setEnabled(true);
					txtIATStdDev.setEnabled(false);
				}
			});

			optIATPoisson.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtIATInterval.setEnabled(false);
					txtIATStdDev.setEnabled(false);
				}
			});

			optIATGaussian.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtIATInterval.setEnabled(false);
					txtIATStdDev.setEnabled(true);
				}
			});
		}

		return iatPanel;
	}

	private JPanel getSTPanel() {
		if (stPanel == null) {
			FocusAdapter fa = new FocusAdapter() {
				public void focusGained(final FocusEvent fe) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) fe.getSource()).selectAll();
						}
					});
				}
			};
			MouseAdapter ma = new MouseAdapter() {
				public void mouseReleased(final MouseEvent me) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) me.getSource()).selectAll();
						}
					});
				}
			};

			stPanel = new JPanel();
			stPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
							.createTitledBorder(Messages
									.getString("QuantAna.Simulation.ServiceTimeDistribution")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			GridBagConstraints constraints = new GridBagConstraints();
			stPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 0, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			groupST = new ButtonGroup();
			JRadioButton optSTConstant = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Constant"), false);
			JRadioButton optSTPoisson = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Poisson"), true);
			JRadioButton optSTGaussian = new JRadioButton(
					Messages.getString("QuantAna.Simulation.Gaussian"), false);
			optSTConstant.setHorizontalAlignment(SwingConstants.LEFT);
			optSTConstant.setActionCommand("ST_UNIFORM");
			optSTConstant.setPreferredSize(new Dimension(100, 20));
			optSTConstant.setMinimumSize(new Dimension(100, 20));
			optSTConstant.setMaximumSize(new Dimension(100, 20));
			optSTConstant.addKeyListener(this);
			optSTPoisson.setHorizontalAlignment(SwingConstants.LEFT);
			optSTPoisson.setActionCommand("ST_EXP");
			optSTPoisson.setPreferredSize(new Dimension(100, 20));
			optSTPoisson.setMinimumSize(new Dimension(100, 20));
			optSTPoisson.setMaximumSize(new Dimension(100, 20));
			optSTPoisson.addKeyListener(this);
			optSTGaussian.setHorizontalAlignment(SwingConstants.LEFT);
			optSTGaussian.setActionCommand("ST_GAUSS");
			optSTGaussian.setPreferredSize(new Dimension(100, 20));
			optSTGaussian.setMinimumSize(new Dimension(100, 20));
			optSTGaussian.setMaximumSize(new Dimension(100, 20));
			optSTGaussian.addKeyListener(this);

			groupST.add(optSTConstant);
			groupST.add(optSTPoisson);
			groupST.add(optSTGaussian);

			txtSTInterval = new JTextField("0");
			txtSTInterval.setEnabled(false);
			txtSTInterval.setMinimumSize(new Dimension(40, 20));
			txtSTInterval.setMaximumSize(new Dimension(40, 20));
			txtSTInterval.setPreferredSize(new Dimension(40, 20));
			txtSTInterval.addKeyListener(this);
			txtSTInterval.addFocusListener(fa);
			txtSTInterval.addMouseListener(ma);

			txtSTStdDev = new JTextField("0.5");
			txtSTStdDev.setEnabled(false);
			txtSTStdDev.setMinimumSize(new Dimension(40, 20));
			txtSTStdDev.setMaximumSize(new Dimension(40, 20));
			txtSTStdDev.setPreferredSize(new Dimension(40, 20));
			txtSTStdDev.addKeyListener(this);
			txtSTStdDev.addFocusListener(fa);
			txtSTStdDev.addMouseListener(ma);

			JLabel lblInterval = new JLabel(
					Messages.getString("QuantAna.Simulation.Interval"));
			lblInterval.setMinimumSize(new Dimension(120, 20));
			lblInterval.setMaximumSize(new Dimension(120, 20));
			lblInterval.setPreferredSize(new Dimension(120, 20));
			lblInterval.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lblDeviation = new JLabel(
					Messages.getString("QuantAna.Simulation.Deviation"));
			lblDeviation.setMinimumSize(new Dimension(120, 20));
			lblDeviation.setMaximumSize(new Dimension(120, 20));
			lblDeviation.setPreferredSize(new Dimension(120, 20));
			lblDeviation.setHorizontalAlignment(SwingConstants.RIGHT);

			constraints.gridx = 0;
			constraints.gridy = 0;
			stPanel.add(optSTConstant, constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			stPanel.add(lblInterval, constraints);
			constraints.gridx = 2;
			constraints.gridy = 0;
			stPanel.add(txtSTInterval, constraints);
			constraints.gridx = 3;
			constraints.gridy = 0;
			stPanel.add(new JLabel("%"), constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			stPanel.add(optSTPoisson, constraints);
			constraints.gridx = 0;
			constraints.gridy = 2;
			stPanel.add(optSTGaussian, constraints);
			constraints.gridx = 1;
			constraints.gridy = 2;
			stPanel.add(lblDeviation, constraints);
			constraints.gridx = 2;
			constraints.gridy = 2;
			stPanel.add(txtSTStdDev, constraints);

			optSTConstant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtSTInterval.setEnabled(true);
					txtSTStdDev.setEnabled(false);
				}
			});

			optSTPoisson.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtSTInterval.setEnabled(false);
					txtSTStdDev.setEnabled(false);
				}
			});

			optSTGaussian.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtSTInterval.setEnabled(false);
					txtSTStdDev.setEnabled(true);
				}
			});
		}

		return stPanel;
	}

	private JPanel getQueuePanel() {
		if (queuePanel == null) {
			queuePanel = new JPanel();
			queuePanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("QuantAna.Simulation.QueueingDiscipline")),
							BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			GridBagConstraints constraints = new GridBagConstraints();
			queuePanel.setLayout(new GridBagLayout());
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.FIRST_LINE_START;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			groupQD = new ButtonGroup();

			JRadioButton opt_q_1 = new JRadioButton(
					Messages.getString("QuantAna.Simulation.QueueingFIFO"),
					true);
			opt_q_1.setPreferredSize(new Dimension(180, 20));
			opt_q_1.setMinimumSize(new Dimension(180, 20));
			opt_q_1.setMaximumSize(new Dimension(180, 20));
			opt_q_1.setActionCommand("QUEUE_FIFO");
			opt_q_1.addKeyListener(this);

			groupQD.add(opt_q_1);
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 10, 5, 50);
			queuePanel.add(opt_q_1, constraints);

			JRadioButton opt_q_2 = new JRadioButton(
					Messages.getString("QuantAna.Simulation.QueueingLIFO"),
					false);
			opt_q_2.setPreferredSize(new Dimension(180, 20));
			opt_q_2.setMinimumSize(new Dimension(180, 20));
			opt_q_2.setMaximumSize(new Dimension(180, 20));
			opt_q_2.setActionCommand("QUEUE_LIFO");
			opt_q_2.addKeyListener(this);
			groupQD.add(opt_q_2);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(5, 10, 40, 50);
			queuePanel.add(opt_q_2, constraints);
		}
		return queuePanel;
	}

	private JPanel getTermPanel() {
		if (termPanel == null) {
			FocusAdapter fa = new FocusAdapter() {
				public void focusGained(final FocusEvent fe) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) fe.getSource()).selectAll();
						}
					});
				}
			};
			MouseAdapter ma = new MouseAdapter() {
				public void mouseReleased(final MouseEvent me) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							((JTextField) me.getSource()).selectAll();
						}
					});
				}
			};

			termPanel = new JPanel();
			termPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("QuantAna.Simulation.TerminationRule")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			GridBagConstraints constraints = new GridBagConstraints();
			termPanel.setLayout(new GridBagLayout());
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			JLabel lblRuns = new JLabel(
					Messages.getString("QuantAna.Simulation.NumRuns"));
			lblRuns.setPreferredSize(new Dimension(80, 20));
			lblRuns.setMinimumSize(new Dimension(80, 20));
			lblRuns.setMaximumSize(new Dimension(80, 20));
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 15, 5, 5);
			termPanel.add(lblRuns, constraints);

			txtRuns = new JTextField("1");
			txtRuns.setPreferredSize(new Dimension(80, 20));
			txtRuns.setMinimumSize(new Dimension(80, 20));
			txtRuns.setMaximumSize(new Dimension(80, 20));
			txtRuns.setHorizontalAlignment(SwingConstants.RIGHT);
			txtRuns.addKeyListener(this);
			txtRuns.addFocusListener(fa);
			txtRuns.addMouseListener(ma);

			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.insets = new Insets(5, 10, 5, 20);
			termPanel.add(txtRuns, constraints);

			stop1 = new JCheckBox(
					Messages.getString("QuantAna.Simulation.CasesCompleted"));
			stop1.setSelected(true);
			stop1.addKeyListener(this);
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.insets = new Insets(5, 10, 5, 5);
			termPanel.add(stop1, constraints);

			stop2 = new JCheckBox(
					Messages.getString("QuantAna.Simulation.TimeElapsed"));
			stop2.setSelected(true);
			stop2.addKeyListener(this);
			constraints.gridx = 0;
			constraints.gridy = 2;
			termPanel.add(stop2, constraints);
		}
		return termPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 25, 5, 10);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			btnStart = new JButton();
			btnStart.setText(Messages.getTitle("QuantAna.Button.Start"));
			btnStart.setIcon(Messages.getImageIcon("QuantAna.Button.Start"));
			Dimension dimButton = new Dimension(120, 25);
			btnStart.setMinimumSize(dimButton);
			btnStart.setMaximumSize(dimButton);
			btnStart.setPreferredSize(dimButton);
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					checkParams();
					if (!errorDetected)
						startSimulation();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttonPanel.add(btnStart, constraints);

			JButton btnConf = new JButton();
			btnConf.setText(Messages.getTitle("QuantAna.Button.TimeModel"));
			btnConf.setIcon(Messages.getImageIcon("QuantAna.Button.TimeModel"));
			btnConf.setMinimumSize(dimButton);
			btnConf.setMaximumSize(dimButton);
			btnConf.setPreferredSize(dimButton);
			btnConf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					getTimeModelDialog();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 1;
			buttonPanel.add(btnConf, constraints);

			/*
			 * btnDistribution = new JButton();
			 * btnDistribution.setText(Messages.
			 * getTitle("QuantAna.Button.Interarrival"));
			 * btnDistribution.setToolTipText(btnDistribution.getText());
			 * 
			 * btnDistribution.setIcon(Messages.getImageIcon(
			 * "QuantAna.Button.Interarrival"));
			 * btnDistribution.setEnabled(false);
			 * btnDistribution.setMinimumSize(dimButton);
			 * btnDistribution.setMaximumSize(dimButton);
			 * btnDistribution.setPreferredSize(dimButton);
			 * btnDistribution.addActionListener(new ActionListener() { public
			 * void actionPerformed(ActionEvent event) { viewDistribution(); }
			 * });
			 */

			constraints.gridx = 0;
			constraints.gridy = 2;
			// buttonPanel.add(btnDistribution, constraints);
			/*
			 * btnExport = new JButton();
			 * btnExport.setText(Messages.getTitle("QuantAna.Button.Export"));
			 * btnExport
			 * .setIcon(Messages.getImageIcon("QuantAna.Button.Export"));
			 * btnExport.setEnabled(false); btnExport.setMinimumSize(dimButton);
			 * btnExport.setMaximumSize(dimButton);
			 * btnExport.setPreferredSize(dimButton);
			 * btnExport.addActionListener(new ActionListener() { public void
			 * actionPerformed(ActionEvent event) { export = new
			 * ExportStatistics((QuantitativeSimulationDialog)thisDialog);
			 * 
			 * dir = new
			 * File(ConfigurationManager.getConfiguration().getLogdir());
			 * 
			 * fileChooser = new JFileChooser(); getFileFilter();
			 * save(Integer.parseInt(txtRuns.getText())); } });
			 */
			constraints.gridx = 0;
			constraints.gridy = 3;
			// buttonPanel.add(btnExport, constraints);

			btnDiagram = new JButton();
			btnDiagram.setText(Messages.getTitle("QuantAna.Button.Diagram"));
			btnDiagram
					.setIcon(Messages.getImageIcon("QuantAna.Button.Diagram"));
			btnDiagram.setEnabled(false);
			btnDiagram.setMinimumSize(dimButton);
			btnDiagram.setMaximumSize(dimButton);
			btnDiagram.setPreferredSize(dimButton);
			btnDiagram.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new ServerStatisticsDialog(thisDialog);
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 4;
			buttonPanel.add(btnDiagram, constraints);

			btnExportCSV = new JButton();
			btnExportCSV.setText(Messages.getTitle("QuantAna.Button.ExportCSV"));
			btnExportCSV
					.setIcon(Messages.getImageIcon("QuantAna.Button.Export"));
			btnExportCSV.setEnabled(false);
			btnExportCSV.setMinimumSize(dimButton);
			btnExportCSV.setMaximumSize(dimButton);
			btnExportCSV.setPreferredSize(dimButton);
			btnExportCSV.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					exportCSV();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 5;
			buttonPanel.add(btnExportCSV, constraints);

			btnExportHTML = new JButton();
			btnExportHTML.setText(Messages.getTitle("QuantAna.Button.ExportHTML"));
			btnExportHTML
					.setIcon(Messages.getImageIcon("QuantAna.Button.Export"));
			btnExportHTML.setEnabled(false);
			btnExportHTML.setMinimumSize(dimButton);
			btnExportHTML.setMaximumSize(dimButton);
			btnExportHTML.setPreferredSize(dimButton);
			btnExportHTML.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					exportHTML();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 6;
			buttonPanel.add(btnExportHTML, constraints);


			JButton btnClose = new JButton();
			btnClose.setText(Messages.getTitle("QuantAna.Button.Close"));
			btnClose.setIcon(Messages.getImageIcon("QuantAna.Button.Close"));
			btnClose.setMinimumSize(dimButton);
			btnClose.setMaximumSize(dimButton);
			btnClose.setPreferredSize(dimButton);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					dispose();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 7;
			buttonPanel.add(btnClose, constraints);

			constraints.gridx = 0;
			constraints.gridy = 8;
			ptk = new JCheckBox(Messages.getTitle("QuantAna.Simulation.Ptk"));
			buttonPanel.add(ptk, constraints);

			constraints.gridx = 0;
			constraints.gridy = 9;
			startDashboard = new JCheckBox(
					Messages.getTitle("QuantAna.Simulation.StartDashboard"));
			if(ConfigurationManager.getConfiguration().getBusinessDashboardUseByDefault() == true){ 
					startDashboard.setSelected(true);
			} else 	startDashboard.setSelected(false);
			buttonPanel.add(startDashboard, constraints);
			

		}

		return buttonPanel;
	}

	private void getTimeModelDialog() {
		new TimeModelDialog(this, tm);
	}

	public TimeModel getTimeModel() {
		return tm;
	}

	public void updTimeModel() {
		lambda = Double.parseDouble(txtLambda.getText());
		periodIndex = cboTimeUnits.getSelectedIndex();
		period = tm.cv(periodIndex, Double.parseDouble(txtPeriod.getText()));

		if (serverTableModel.getValueAt(0, 2) != null) {
			ServerTableModel stm = serverTableModel;

			for (int i = 0; i < stm.getRowCount(); i++) {
				try {
					double val;
					for (int k = 2; k < 5; k++) {
						Object o = stm.getValueAt(i, k);
						if (o instanceof String)
							val = Double.parseDouble(((String) o));
						else
							val = ((Double) o).doubleValue();
						val = tm.cv(timeUnit, val);
						stm.setValueAt(val, i, k);
					}
				} catch (Exception e) {
				}
			}
		}

		timeUnit = tm.getStdUnit();
		for (SimNode n : simgraph.getNodes().values()) {
			if (n.gettime() != 0) {
				n.settime(tm.cv(n.gettimeunit(), n.gettime(), n.getUnitFactor()));
				n.settimeunit(tm.getStdUnit());
				n.setUnitFactor(tm.getStdUnitMultiple());
			}
		}

	}

	public void updContents() {

		ServerTableModel stm = serverTableModel;
		ResUtilTableModel rtm = resUtilTableModel;
		HashMap<String, SimServer> serv = sim.getServerList();
		HashMap<String, Resource> res = resAlloc.getResources();

		simStatistics = sim.getRunStats();
		SimRunStats rs = simStatistics.get(simStatistics.size() - 1);
		double l_ = lambda / period;

		stm.setValueAt(String.format("%.2f", rs.getProcCompTime() * l_), 0, 1);
		stm.setValueAt(String.format("%.1f", lambda), 0, 2);
		stm.setValueAt(String.format("%.2f", rs.getProcCompTime()), 0, 3);
		stm.setValueAt(
				String.format("%.2f",
						rs.getProcCompTime() - rs.getProcWaitTime()), 0, 4);
		stm.setValueAt(String.format("%.2f", rs.getProcWaitTime()), 0, 5);

		for (int i = 1; i <= numServers; i++) {
			String id = produceID((String) stm.getValueAt(i, 0));
			SimServer s = serv.get(id);
			try {
				SimReportServerStats sst = (SimReportServerStats) rs
						.getServStats().get(s);
				stm.setValueAt(
						String.format("%.2f",
								sst.getAvgQLength() + sst.getAvgResNumber()),
						i, 1);
				stm.setValueAt(
						String.format("%.1f", lambda
								* (getUnfoldedSum(id) * l_)), i, 2);
				stm.setValueAt(
						String.format("%.2f",
								sst.getAvgServTime() + sst.getAvgWaitTime()),
						i, 3);
				stm.setValueAt(String.format("%.2f", sst.getAvgServTime()), i,
						4);
				stm.setValueAt(String.format("%.2f", sst.getAvgWaitTime()), i,
						5);
			} catch (Exception e) {
				stm.setValueAt("", i, 1);
				stm.setValueAt("", i, 2);
				stm.setValueAt("", i, 3);
				stm.setValueAt("", i, 4);
				stm.setValueAt("", i, 5);
			}
		}

		for (int i = 0; i < resObjNum; i++) {
			String name = (String) rtm.getValueAt(i, 0);
			Resource r = res.get(name);
			ResourceStats rst = rs.getResStats().get(r);

			String util = String.format("%,.2f",
					rst.getUtilizationRatio() * 100);
			rtm.setValueAt(util, i, 1);
		}

		if (ptk.isSelected()) {
			// write the log into the text editor
			for (int i = 0; i < getSimulator().getLog().size(); i++) {
				logDlg.addLine(getSimulator().getLog().get(i));
			}
			logDlg.setXESLog(getSimulator().getXESLog());
			// clear the array with the log enties
			// getSimulator().getLog().clear();
			// getSimulator().getXESLog().clear();
			logDlg.configTxt();
			logDlg.setVisible(true);
		}
	}

	private double getUnfoldedSum(String id) {
		double res = 0.0;
		for (SimNode n : unfoldedNet.values()) {
			if (n.getid().equals(id)) {
				res += n.getTempRuns();
			}
		}
		return res;
	}

	private String produceID(String key) {
		if (key.equals("Protocol") || key.equals("Process"))
			return key;
		else
			return key.substring(key.indexOf("(") + 1, key.indexOf(")"));
	}

	public SimGraph getGraph() {
		return simgraph;
	}

	private void startSimulation() {
		LoggerManager.info(Constants.QUANTANA_LOGGER,
				Messages.getString("QuantAna.Started"));
		updTimeModel();
		logDlg.clear();
		SimParameters sp = new SimParameters(lambda, period);
		sp.setRuns(Integer.parseInt(txtRuns.getText()));

		String op1 = groupIAT.getSelection().getActionCommand();
		if (op1.equals("IAT_UNIFORM")) {
			sp.setDistCases(SimDistribution.CONSTANT);
			sp.setCParam(Double.parseDouble(txtIATInterval.getText()) / 100);
		} else if (op1.equals("IAT_GAUSS")) {
			sp.setDistCases(SimDistribution.EXPOTENTIONAL);
			sp.setCParam(Double.parseDouble(txtIATStdDev.getText()));
		} else {
			sp.setDistCases(SimDistribution.POISSON);
		}

		String op2 = groupST.getSelection().getActionCommand();
		if (op2.equals("ST_UNIFORM")) {
			sp.setDistServ(SimDistribution.CONSTANT);
			sp.setSParam(Double.parseDouble(txtSTInterval.getText()) / 100);
		} else if (op2.equals("ST_GAUSS")) {
			sp.setDistServ(SimDistribution.EXPOTENTIONAL);
			sp.setSParam(Double.parseDouble(txtSTStdDev.getText()));
		} else {
			sp.setDistServ(SimDistribution.POISSON);
		}

		String op3 = groupQD.getSelection().getActionCommand();
		if (op3.equals("QUEUE_LIFO")) {
			sp.setQueue(SimRunner.Q_LIFO);
		} else {
			sp.setQueue(SimRunner.Q_FIFO);
		}

		if (stop1.isSelected()) {
			if (stop2.isSelected())
				sp.setStop(SimRunner.STOP_BOTH);
			else
				sp.setStop(SimRunner.STOP_CASE);
		} else if (stop2.isSelected()) {
			sp.setStop(SimRunner.STOP_TIME);
		} else {
			sp.setStop(SimRunner.STOP_NONE);
		}

		sp.setWriteLog(ptk.isSelected());

		// if (groupRoleNum > 2 && resObjNum > 1) {
		if (resObjNum > 0) {
			sp.setResUse(SimRunner.RES_USED);
		} else {
			sp.setResUse(SimRunner.RES_NOT_USED);
		}

		try {
			unfoldNet(simgraph, sp.getPeriod() / sp.getLambda(), epsilon);
		} catch (java.lang.StackOverflowError e) {
			unfoldedNet = null;
			JOptionPane.showMessageDialog(null, Messages.getString("QuantAna.Simulation.unfoldingFailed"), "Unfolding Error", JOptionPane.WARNING_MESSAGE);
		}
		// sim = new SimRunner(simgraph, new ResourceUtilization(resAlloc), sp);

		// -->CN

		if (startDashboard.isSelected()) {
			try {
				

				storageengine = StorageEngine.getInstance();

				

					if (dashboard == null) {
						dashboard = new DashboardRunner(storageengine,port);
						if (dashboard != null) {
							QuantitativeSimulationDialog.dashboard.startServer();

						}
					}
				
				
				storageengine.setOwner(this);
				storageengine.setSimParameters(sp);
				storageengine.CreateTable(Table.SIM_VALUES);
				
				//try{
				//	EmbeddedBrowserView browser = new EmbeddedBrowserView(port,true);
				//	browser.setVisible(true);
				//}
				//catch(JFXUsageNotSupported e){
					ClientStarter cs = new ClientStarter();
					cs.startClient(true);
				//}
					//dashboard.add(null);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        
			sim = new SimRunnerDB(simgraph, new ResourceUtilization(resAlloc),
					sp, storageengine);
		} else {
			sim = new SimRunner(simgraph, new ResourceUtilization(resAlloc), sp);
		}
		// <--CN
		// the waitdialog starts and observs the simulation
		new WaitDialog(this, Messages.getString("QuantAna.Simulation.Wait"),
				sp.getPeriod() / sp.getLambda(), sim);
	}

	private void unfoldNet(SimGraph g, double l, double e) {
		unfoldedNet.clear();
		for (SimNode n : g.getNodes().values()) {
			n.setJoinReached(false);
			n.setNumOfRuns(0);
		}
		SimNode s = g.getSource();
		SimNode n = new SimNode(s.getid(), s.getname());
		n.settype(s.gettype());
		n.setTempRuns(l);
		s.incIteration();
		n.setIteration(s.getIteration());
		Key start = new Key(n.getid(), l);
		unfoldedNet.put(start, n);
		LinkedList<SimNodePair> queue = new LinkedList<SimNodePair>();
		queue.addLast(new SimNodePair(s, n));
		runThroughNet(queue);
	}

	private void runThroughNet(LinkedList<SimNodePair> q) {
		if (!(q.isEmpty())) {
			SimNodePair np = q.removeFirst();
			Iterator<SimArc> a = np.getFirst().getarcOut().iterator();
			while (a.hasNext()) {
				SimArc aa = a.next();
				SimNode m = aa.getTarget();
				if (!m.isJoinReached()) {
					double val = aa.getProbability()
							* np.getSecond().getTempRuns();
					if (!(val < epsilon)) {
						SimNode y = new SimNode(m.getid(), m.getname());
						y.settype(m.gettype());
						y.setTempRuns(val);
						m.incIteration();
						y.setIteration(m.getIteration());
						if (m.isAndJoin())
							m.setJoinReached(true);
						np.getSecond()
								.getarcOut()
								.add(new SimArc(np.getSecond(), y, aa
										.getProbability()));
						Key k = new Key(m.getid(), val);
						unfoldedNet.put(k, y);

						if (!(containsElement(q, m)))
							q.addLast(new SimNodePair(m, y));
					}
				}
			}

			runThroughNet(q);
		}
	}

	private boolean containsElement(LinkedList<SimNodePair> q, SimNode n) {
		boolean contains = false;
		Iterator<SimNodePair> i = q.iterator();
		while (i.hasNext()) {
			SimNodePair p = i.next();
			if (n.equals(p.getSecond())) {
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
		Vector<ResourceClassModel> rVec = pmp.getRoles();
		Vector<ResourceClassModel> gVec = pmp.getOrganizationUnits();

		// groupRoleNum = rVec.size() + gVec.size();

		for (int i = 0; i < rVec.size(); i++)
			roles.add(((ResourceClassModel) rVec.get(i)).getName());

		for (int i = 0; i < gVec.size(); i++)
			groups.add(((ResourceClassModel) gVec.get(i)).getName());

		Iterator<TransitionModel> iter = getTransModels().iterator();

		resAlloc = new ResourceAllocation(roles, groups, iter, pmp);

		resObjNum = resAlloc.getResources().size();
	}

	private HashMap<String, AbstractPetriNetElementModel> getAllTransModels(ModelElementContainer mec) {
		HashMap<String, AbstractPetriNetElementModel> lst = new HashMap<String, AbstractPetriNetElementModel>();
		lst.putAll(mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));
		lst.putAll(mec.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
		lst.putAll(mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE));
		for (AbstractPetriNetElementModel p : mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE).values()) {
			lst.putAll(getAllTransModels( ((SubProcessModel)p).getElementContainer() ));
		}
		return lst;
	}


	private LinkedList<TransitionModel> getTransModels() {
		HashMap<String, AbstractPetriNetElementModel> allTransitions = getAllTransModels(editor.getModelProcessor().getElementContainer());
		LinkedList<TransitionModel> lst = new LinkedList<TransitionModel>();
		ArrayList<String> ids = new ArrayList<String>();
		for (SimNode n : simgraph.getNodes().values()) {
			if (n.isTransition())
				ids.add(n.getid());
		}
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i);
			if (id.endsWith("_SubProcessExit") || id.endsWith("_SubProcessEntry"))
				id = id.substring(0, id.lastIndexOf("_SubProcess"));
			lst.add( (TransitionModel) allTransitions.get(id));
		}
		return lst;
	}

	public SimRunner getSimulator() {
		return sim;
	}

	public void activateDetails() {
		for (JButton b : btnColumn) {
			b.setEnabled(true);
			// b.repaint();
		}
		btnDiagram.setEnabled(true);
		btnExportCSV.setEnabled(true);
		btnExportHTML.setEnabled(true);
		/*
		 * btnProtocol.setEnabled(true); btnExport.setEnabled(true);
		 */
		// btnDistribution.setEnabled(true);
	}

	public void mouseMoved(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void mouseDragged(MouseEvent e) {
	}

	public ResUtilTableModel getResUtilTableModel() {
		return resUtilTableModel;
	}

	public ServerTableModel getServerTableModel() {
		return serverTableModel;
	}

	private void makeTasksAndResources() {
		String[] tasks = simgraph.getTransitionsGT0();
		tasksAndResources = new TasksResourcesAllocation();

		for (String s : tasks) {
			tasksAndResources.addTaskResourcesPair(s,
					resAlloc.getResourcesPerTask(s));
		}
	}

	public TasksResourcesAllocation getTaskAndResource() {
		return tasksAndResources;
	}

	/*
	 * private void save(int runs){ fileChooser.setCurrentDirectory(dir);
	 * fileChooser.setMultiSelectionEnabled(false);
	 * 
	 * int result = fileChooser.showSaveDialog(this); if (result ==
	 * JFileChooser.APPROVE_OPTION){ String fname =
	 * fileChooser.getSelectedFile().getAbsolutePath(); dir =
	 * fileChooser.getCurrentDirectory(); String ext = ""; int idx =
	 * fname.lastIndexOf("."); if (idx > -1){ ext = fname.substring(idx + 1);
	 * fname = fname.substring(0, idx); } else { ext = "csv"; }
	 * 
	 * String text = "";
	 * 
	 * if (ext.equals("")) ext = "csv";
	 * 
	 * for (int i = 0; i <= runs; i++){ if (i < runs) text =
	 * export.getStatsTable(i, false); else text = export.getStatsTable(i,
	 * true);
	 * 
	 * try { if (ext.equals("csv")) { FileWriter fw; if (i == runs) fw = new
	 * FileWriter(fname + "." + ext); else fw = new FileWriter(fname + "_" +
	 * (i+1) + "." + ext); fw.write(text); fw.close(); } } catch (IOException e)
	 * { e.printStackTrace(); } } } }
	 * 
	 * private void getFileFilter(){ eff.addExtension("csv");
	 * eff.setDescription(
	 * Messages.getString("QuantAna.Simulation.Export.FileFilter"));
	 * fileChooser.setFileFilter(eff); }
	 * 
	 * class ExtensionFileFilter extends FileFilter {
	 * 
	 * private ArrayList<String> extensions = new ArrayList<String>(); private
	 * String description = " ";
	 * 
	 * public void addExtension(String ext){ if (!ext.startsWith(".")) ext = "."
	 * + ext; extensions.add(ext.toLowerCase()); }
	 * 
	 * public boolean accept(File f){ if (f.isDirectory()) return true;
	 * 
	 * String name = f.getName().toLowerCase(); for (String ext : extensions) if
	 * (name.endsWith(ext)) return true; return false; }
	 * 
	 * public String getDescription() { return description; }
	 * 
	 * public void setDescription(String description) { this.description =
	 * description; } }
	 */

	private void checkParams() throws InvalidRunsException,
			InvalidIntervalException {
		int i;
		boolean isInteger = false;
		JTextField tf = null;

		try {
			tf = txtLambda;
			isInteger = false;
			Double.parseDouble(txtLambda.getText());

			tf = txtPeriod;
			isInteger = false;
			Double.parseDouble(txtPeriod.getText());

			tf = txtIATStdDev;
			if (tf.isEnabled()) {
				isInteger = false;
				if (Double.parseDouble(txtIATStdDev.getText()) == 0) {
					throw new InvalidDeviationException();
				}
			}

			tf = txtSTStdDev;
			if (tf.isEnabled()) {
				isInteger = false;
				if (Double.parseDouble(txtSTStdDev.getText()) == 0) {
					throw new InvalidDeviationException();
				}
			}

			tf = txtRuns;
			isInteger = true;
			i = Integer.parseInt(txtRuns.getText());
			if (i < 1)
				throw new InvalidRunsException();

			tf = txtIATInterval;
			if (tf.isEnabled()) {
				isInteger = true;
				i = Integer.parseInt(txtIATInterval.getText());
				if ((i < 0) || (i > 100))
					throw new InvalidIntervalException();
			}

			tf = txtSTInterval;
			if (tf.isEnabled()) {
				isInteger = true;
				i = Integer.parseInt(txtSTInterval.getText());
				if ((i < 0) || (i > 100))
					throw new InvalidIntervalException();
			}

			errorDetected = false;
		} catch (InvalidRunsException ire) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("QuantAna.Message.InvalidRuns"));
			tf.requestFocus();
			tf.selectAll();
			errorDetected = true;
		} catch (InvalidIntervalException iie) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("QuantAna.Message.InvalidInterval"));
			tf.requestFocus();
			tf.selectAll();
			errorDetected = true;
		} catch (InvalidDeviationException iie) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("QuantAna.Message.InvalidDeviation"));
			tf.requestFocus();
			tf.selectAll();
			errorDetected = true;
		} catch (NumberFormatException nfe) {
			if (isInteger)
				JOptionPane.showMessageDialog(null, Messages
						.getString("QuantAna.Message.NumberFormatErrorInt"));
			else
				JOptionPane.showMessageDialog(null, Messages
						.getString("QuantAna.Message.NumberFormatErrorDouble"));
			tf.requestFocus();
			tf.selectAll();
			errorDetected = true;
		}
	}

	class InvalidRunsException extends NumberFormatException {
		private static final long serialVersionUID = 1L;

		public InvalidRunsException() {
		}

		public InvalidRunsException(String msg) {
			super(msg);
		}
	}

	class InvalidDeviationException extends NumberFormatException {
		private static final long serialVersionUID = 1L;

		public InvalidDeviationException() {
		}

		public InvalidDeviationException(String msg) {
			super(msg);
		}
	}

	class InvalidIntervalException extends NumberFormatException {
		private static final long serialVersionUID = 1L;

		public InvalidIntervalException() {
		}

		public InvalidIntervalException(String msg) {
			super(msg);
		}
	}

	class SimNodePair {
		SimNode a;
		SimNode b;

		public SimNodePair(SimNode x, SimNode y) {
			a = x;
			b = y;
		}

		public SimNode getFirst() {
			return a;
		}

		public SimNode getSecond() {
			return b;
		}
	}

	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			btnStart.doClick();
		}
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
	
	public JGraph getEditorGraph(){
		return this.editor.getGraph();
	}

	public void exportCSV() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Spreadsheets", "csv");
		chooser.setFileFilter(filter);
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			try {
				PrintWriter p = new PrintWriter(f);

				ServerTableModel stm = serverTableModel;
				ResUtilTableModel rtm = resUtilTableModel;
				HashMap<String, SimServer> serv = sim.getServerList();
				HashMap<String, Resource> res = resAlloc.getResources();

				simStatistics = sim.getRunStats();
				SimRunStats rs = simStatistics.get(simStatistics.size() - 1);
				double l_ = lambda / period;

				p.println(Messages.getString("QuantAna.Simulation.Column.Names") + ", "
						+ Messages.getString("QuantAna.Simulation.Column.L") + ", "
						+ Messages.getString("QuantAna.Simulation.Column.CalcLambda") + ", "
						+ Messages.getString("QuantAna.Simulation.Column.W") + ", "
						+ Messages.getString("QuantAna.Simulation.Column.Ws") + ", "
						+ Messages.getString("QuantAna.Simulation.Column.Wq") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.ZeroDelays") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.NumCalls") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.NumAccess") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.NumDeparture") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.MaxQueueLength") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.MaxCasesParallel") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.MaxWaitTime") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.NumServedWhenStopped") + ", "
						+ Messages.getString("QuantAna.Simulation.Details.QLenWhenStopped"));

				p.println("Process, "
						+ String.format("%.2f", rs.getProcCompTime() * l_) + ", "
						+ String.format("%.1f", lambda) + ", "
						+ String.format("%.2f", rs.getProcCompTime()) + ", "
						+ String.format("%.2f", rs.getProcCompTime()-rs.getProcWaitTime()) + ", "
						+ String.format("%.2f", rs.getProcWaitTime()));

				for (int i = 1; i <= numServers; i++) {
					String id = produceID((String) stm.getValueAt(i, 0));
					SimServer s = serv.get(id);
					try {
						SimReportServerStats sst = (SimReportServerStats) rs.getServStats().get(s);

						p.println(stm.getValueAt(i, 0) + ", "
								+ String.format("%.2f", sst.getAvgQLength() + sst.getAvgResNumber()) + ", "
								+ String.format("%.1f", lambda * (getUnfoldedSum(id) * l_)) + ", "
								+ String.format("%.2f", sst.getAvgServTime() + sst.getAvgWaitTime()) + ", "
								+ String.format("%.2f", sst.getAvgServTime()) + ", "
								+ String.format("%.2f", sst.getAvgWaitTime()) + ", "
								+ String.format("%,.2f", sst.getAvgZeroDelays()) + ", "
								+ String.format("%,.2f", sst.getAvgCalls()) + ", "
								+ String.format("%,.2f", sst.getAvgAccesses()) + ", "
								+ String.format("%,.2f", sst.getAvgDepartures()) + ", "
								+ String.format("%,.2f", sst.getAvgMaxQLength()) + ", "
								+ String.format("%,.2f", sst.getAvgMaxResNumber()) + ", "
								+ String.format("%,.2f", sst.getMaxWaitTime()) + ", "
								+ String.format("%,.2f", sst.getAvgNumServedWhenStopped()) + ", "
								+ String.format("%,.2f", sst.getAvgQLengthWhenStopped()));
					} catch (Exception e) { p.println(); }
				}

				p.println();
				p.println(Messages.getString("QuantAna.Simulation.Column.Object") + ", " + Messages.getString("QuantAna.Simulation.Column.Util"));

				for (int i = 0; i < resObjNum; i++) {
					String name = (String) rtm.getValueAt(i, 0);
					Resource r = res.get(name);
					ResourceStats rst = rs.getResStats().get(r);
					String util = String.format("%,.2f", rst.getUtilizationRatio() * 100);
					p.println(r.getName() + ", " + util);
				}

				p.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, Messages.getString("QuantAna.Simulation.Log.csvError"), "", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void exportHTML() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML pages", "html", "htm");
		chooser.setFileFilter(filter);
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			try {
				PrintWriter p = new PrintWriter(f);

				ServerTableModel stm = serverTableModel;
				ResUtilTableModel rtm = resUtilTableModel;
				HashMap<String, SimServer> serv = sim.getServerList();
				HashMap<String, Resource> res = resAlloc.getResources();

				simStatistics = sim.getRunStats();
				SimRunStats rs = simStatistics.get(simStatistics.size() - 1);
				double l_ = lambda / period;

				p.println("<!DOCTYPE HTML>\n" +
						"<html>\n" +
						"<head>\n" +
						"<style>");

				BufferedReader br = new BufferedReader(new InputStreamReader(QuantitativeSimulationDialog.class.getResource("stylesheet.css").openStream()));
		        String line = br.readLine();
		        while (line != null) {
		        	p.print(line);
			        line = br.readLine();
				}
		        br.close();

		        p.println("</style>\n" +
		        		"<title>" + Messages.getString("QuantAna.Simulation.Title") + "</title>\n" +
		        		"</head>");

		        p.println("<h1>" + Messages.getString("QuantAna.Simulation.Title") + "</h1>");

		        p.println("<h2>" + Messages.getString("QuantAna.Simulation.GeneralProperties") + "</h2>");
		        p.println("<p>" + Messages.getString("QuantAna.Simulation.Mean") + " " + txtLambda.getText() + "</p>");
		        p.println("<p>" + Messages.getString("QuantAna.Simulation.Period") + " " + txtPeriod.getText() + " " + cboTimeUnits.getSelectedItem().toString() + "</p>");

				p.println("<h2>" + Messages.getString("QuantAna.Simulation.TerminationRule") + "</h2>");
				p.println("<p>" + Messages.getString("QuantAna.Simulation.NumRuns") + " " + txtRuns.getText() + "</p>");

				if (stop1.isSelected()) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.CasesCompleted") + "</p>");
				}
				if (stop2.isSelected()) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.TimeElapsed") + "</p>");
				}

		        p.println("<h2>" + Messages.getString("QuantAna.Simulation.QueueingDiscipline") + "</h2>");
				if (groupQD.getSelection().getActionCommand().equals("QUEUE_LIFO")) {
					p.println("<p>LIFO</p>");
				} else {
					p.println("<p>FIFO</p>");
				}

				p.println("<h2>" + Messages.getString("QuantAna.Simulation.ArrivalRateDistribution") + "</h2>");
				if (groupIAT.getSelection().getActionCommand().equals("IAT_UNIFORM")) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Constant") + ", " + Messages.getString("QuantAna.Simulation.Interval") + " " + txtIATInterval.getText() + "%</p>");
				} else if (groupIAT.getSelection().getActionCommand().equals("IAT_GAUSS")) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Gaussian") + ", " + Messages.getString("QuantAna.Simulation.Deviation") + " " + txtIATStdDev.getText() + "</p>");
				} else {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Poisson") + "</p>");
				}

				p.println("<h2>" + Messages.getString("QuantAna.Simulation.ServiceTimeDistribution") + "</h2>");
				if (groupST.getSelection().getActionCommand().equals("ST_UNIFORM")) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Constant") + ", " + Messages.getString("QuantAna.Simulation.Interval") + " " + txtSTInterval.getText() + "%</p>");
				} else if (groupST.getSelection().getActionCommand().equals("ST_GAUSS")) {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Gaussian") + ", " + Messages.getString("QuantAna.Simulation.Deviation") + " " + txtSTStdDev.getText() + "</p>");
				} else {
					p.println("<p>" + Messages.getString("QuantAna.Simulation.Poisson") + "</p>");
				}

				p.println("<h2>" + Messages.getString("QuantAna.Simulation.ServerStats") + "</h2>");
				p.println("<table>\n" +
						"  <thead>");
				p.println("    <tr><th>" + Messages.getString("QuantAna.Simulation.Column.Names") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Column.L") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Column.CalcLambda") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Column.W") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Column.Ws") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Column.Wq") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.ZeroDelays") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.NumCalls") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.NumAccess") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.NumDeparture") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.MaxQueueLength") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.MaxCasesParallel") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.MaxWaitTime") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.NumServedWhenStopped") + "</th><th>"
						+ Messages.getString("QuantAna.Simulation.Details.QLenWhenStopped") + "</th></tr>");
				p.println("  </thead>");

				p.println("    <tr><td>Process</td><td>"
						+ String.format("%.2f", rs.getProcCompTime() * l_) + "</td><td>"
						+ String.format("%.1f", lambda) + "</td><td>"
						+ String.format("%.2f", rs.getProcCompTime()) + "</td><td>"
						+ String.format("%.2f", rs.getProcCompTime()-rs.getProcWaitTime()) + "</td><td>"
						+ String.format("%.2f", rs.getProcWaitTime()) + "</td></tr>");

				for (int i = 1; i <= numServers; i++) {
					String id = produceID((String) stm.getValueAt(i, 0));
					SimServer s = serv.get(id);
					try {
						SimReportServerStats sst = (SimReportServerStats) rs.getServStats().get(s);

						p.println("    <tr><td>" + stm.getValueAt(i, 0) + "</td><td>"
								+ String.format("%.2f", sst.getAvgQLength() + sst.getAvgResNumber()) + "</td><td>"
								+ String.format("%.1f", lambda * (getUnfoldedSum(id) * l_)) + "</td><td>"
								+ String.format("%.2f", sst.getAvgServTime() + sst.getAvgWaitTime()) + "</td><td>"
								+ String.format("%.2f", sst.getAvgServTime()) + "</td><td>"
								+ String.format("%.2f", sst.getAvgWaitTime()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgZeroDelays()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgCalls()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgAccesses()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgDepartures()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgMaxQLength()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgMaxResNumber()) + "</td><td>"
								+ String.format("%,.2f", sst.getMaxWaitTime()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgNumServedWhenStopped()) + "</td><td>"
								+ String.format("%,.2f", sst.getAvgQLengthWhenStopped()) + "</td></tr>");
					} catch (Exception e) { p.println(); }
				}
				p.println("</table>");
				p.println("<h2>" + Messages.getString("QuantAna.Simulation.ResUtil") + "</h2>");
				p.println("<table>\n" +
						"  <thead>");
				p.println("    <tr><th>" + Messages.getString("QuantAna.Simulation.Column.Object") + "</th><th>" + Messages.getString("QuantAna.Simulation.Column.Util") + "</th></tr>");
				p.println("  </thead>");

				for (int i = 0; i < resObjNum; i++) {
					String name = (String) rtm.getValueAt(i, 0);
					Resource r = res.get(name);
					ResourceStats rst = rs.getResStats().get(r);
					String util = String.format("%,.2f", rst.getUtilizationRatio() * 100);
					p.println("    <tr><td>" + r.getName() + "</td><td>" + util + "</td></tr>");
				}
				p.println("</table>");
				p.println("</body>\n" +
						"</html>");
				p.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, Messages.getString("QuantAna.Simulation.Log.htmlError"), "", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, Messages.getString("QuantAna.Simulation.Log.htmlError"), "", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
