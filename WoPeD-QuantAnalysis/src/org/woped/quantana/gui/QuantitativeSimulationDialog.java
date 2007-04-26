package org.woped.quantana.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.editor.utilities.Messages;
import org.woped.quantana.graph.Node;
import org.woped.quantana.graph.WorkflowNetGraph;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceUtilization;
import org.woped.quantana.simulation.ProbabilityDistribution;
import org.woped.quantana.simulation.Simulator;

public class QuantitativeSimulationDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private EditorVC editor = null;

	private JPanel simParamPanel = null;

	private JPanel simResultPanel = null;

	private JPanel arrivalRateDistributionPanel = null;

	private JPanel serviceTimeDistributionPanel = null;

	private JPanel queueingDisciplinePanel = null;

	private JPanel terminationConditionPanel = null;

	private JPanel buttonPanel = null;

	private int groupRoleNum = 0;

	private int resObjNum = 0;

	private double period = 8.0;

	private double lambda = 50.0;

	private StructuralAnalysis sa;

	private ModelElementContainer mec;

	private WorkflowNetGraph graph;

	private ResourceAllocation resAlloc;

	// Simulation Tab
	private JTextField txtRuns;

	private ButtonGroup groupIAT;

	private ButtonGroup groupST;

	private ButtonGroup groupQD;

	private JTextField txt_p2_11;

	private JTextField txt_p2_12;

	private JTextField txt_p2_21;

	private JTextField txt_p2_22;

	private JTextField txt_p2_31;

	private JTextField txt_p2_32;

	private JCheckBox stop1;

	private JCheckBox stop2;

	private TimeModel tm = null;

	/**
	 * This is the default constructor
	 */
	public QuantitativeSimulationDialog(JFrame owner, EditorVC editor) {
		super(owner, Messages.getTitle("QuantAna.Simulation"), true);
		this.editor = editor;
		sa = new StructuralAnalysis(editor);
		mec = editor.getModelProcessor().getElementContainer();
		graph = new WorkflowNetGraph(sa, mec);
		tm = new TimeModel(1, 1.0);
		initResourceAlloc();
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
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		getContentPane().add(getSimParamPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		getContentPane().add(getSimResultPanel(), constraints);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width > 800 ? 800 : screenSize.width;
		int x = screenSize.width > width ? (screenSize.width - width) / 2 : 0;
		int height = screenSize.height > 900 ? 900 : screenSize.height;
		int y = screenSize.height > height ? (screenSize.height - height) / 2
				: 0;
		this.setBounds(x, y, width, height);
		this.setVisible(true);
	}

	private JPanel getSimResultPanel() {
		if (simResultPanel == null) {
			simResultPanel = new JPanel();
			simResultPanel.setBorder(BorderFactory.createTitledBorder(
							BorderFactory.createEtchedBorder(),
							Messages
									.getString("QuantAna.Simulation.SimResults")));
			GridBagConstraints constraints = new GridBagConstraints();
			simResultPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 0, 5, 0);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			simResultPanel.add(new JLabel("Results go here"), constraints);
		}
		simResultPanel.setMinimumSize(new Dimension(790, 400));
		return simResultPanel;
	}

	private JPanel getSimParamPanel() {
		if (simParamPanel == null) {
			simParamPanel = new JPanel();
			GridBagConstraints constraints = new GridBagConstraints();
			simParamPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 0, 5, 0);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			simParamPanel.add(getArrivalRateDistributionPanel(), constraints);
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.gridy = 1;
			simParamPanel.add(getServiceTimeDistributionPanel(), constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			simParamPanel.add(getQueueingDisciplinePanel(), constraints);
			constraints.gridx = 2;
			constraints.gridy = 1;
			simParamPanel.add(getTerminationConditionPanel(), constraints);
		}
		return simParamPanel;
	}

	private JPanel getArrivalRateDistributionPanel() {
		if (arrivalRateDistributionPanel == null) {
			arrivalRateDistributionPanel = new JPanel();
			JPanel jp = new JPanel();
			jp.setBorder(BorderFactory
							.createTitledBorder(
									BorderFactory.createEtchedBorder(),
									Messages
											.getString("QuantAna.Simulation.ArrivalRateDistribution")));
			GridBagConstraints constraints = new GridBagConstraints();
			jp.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			groupIAT = new ButtonGroup();
			JRadioButton opt_p2_1 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Constant"), false);
			JRadioButton opt_p2_2 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Poisson"), true);
			JRadioButton opt_p2_3 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Gaussian"), false);
			opt_p2_1.setActionCommand("IAT_UNIFORM");
			opt_p2_2.setActionCommand("IAT_EXP");
			opt_p2_3.setActionCommand("IAT_GAUSS");
			groupIAT.add(opt_p2_1);
			groupIAT.add(opt_p2_2);
			groupIAT.add(opt_p2_3);

			txt_p2_11 = new JTextField("0");
			txt_p2_11.setPreferredSize(new Dimension(100, 20));
			txt_p2_11.setMinimumSize(new Dimension(100, 20));
			txt_p2_11.setMaximumSize(new Dimension(100, 20));
			txt_p2_11.setEnabled(false);
			txt_p2_12 = new JTextField(Double.toString(2 / lambda * period));
			txt_p2_12.setPreferredSize(new Dimension(100, 20));
			txt_p2_12.setMinimumSize(new Dimension(100, 20));
			txt_p2_12.setMaximumSize(new Dimension(100, 20));
			txt_p2_12.setEnabled(false);
			txt_p2_21 = new JTextField(Double.toString(1 / lambda * period));
			txt_p2_21.setPreferredSize(new Dimension(100, 20));
			txt_p2_21.setMinimumSize(new Dimension(100, 20));
			txt_p2_21.setMaximumSize(new Dimension(100, 20));
			txt_p2_21.setEnabled(true);
			txt_p2_22 = new JTextField("0");
			txt_p2_22.setEnabled(false);
			txt_p2_22.setPreferredSize(new Dimension(100, 20));
			txt_p2_22.setMinimumSize(new Dimension(100, 20));
			txt_p2_22.setMaximumSize(new Dimension(100, 20));
			txt_p2_31 = new JTextField("0");
			txt_p2_31.setPreferredSize(new Dimension(100, 20));
			txt_p2_31.setMinimumSize(new Dimension(100, 20));
			txt_p2_31.setMaximumSize(new Dimension(100, 20));
			txt_p2_31.setEnabled(false);
			txt_p2_32 = new JTextField("1");
			txt_p2_32.setPreferredSize(new Dimension(100, 20));
			txt_p2_32.setMinimumSize(new Dimension(100, 20));
			txt_p2_32.setMaximumSize(new Dimension(100, 20));
			txt_p2_32.setEnabled(false);

			JLabel lbl_p2_11 = new JLabel(Messages
					.getString("QuantAna.Simulation.From"));
			lbl_p2_11.setPreferredSize(new Dimension(120, 20));
			lbl_p2_11.setMinimumSize(new Dimension(120, 20));
			lbl_p2_11.setMaximumSize(new Dimension(120, 20));
			lbl_p2_11.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lbl_p2_12 = new JLabel(Messages
					.getString("QuantAna.Simulation.To"));
			lbl_p2_12.setPreferredSize(new Dimension(120, 20));
			lbl_p2_12.setMinimumSize(new Dimension(120, 20));
			lbl_p2_12.setMaximumSize(new Dimension(120, 20));
			lbl_p2_12.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lbl_p2_21 = new JLabel(Messages
					.getString("QuantAna.Simulation.MeanPoisson"));
			lbl_p2_21.setPreferredSize(new Dimension(120, 20));
			lbl_p2_21.setMinimumSize(new Dimension(120, 20));
			lbl_p2_21.setMaximumSize(new Dimension(120, 20));
			lbl_p2_21.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lbl_p2_22 = new JLabel(Messages
					.getString("QuantAna.Simulation.Position"));
			lbl_p2_22.setPreferredSize(new Dimension(120, 20));
			lbl_p2_22.setMinimumSize(new Dimension(120, 20));
			lbl_p2_22.setMaximumSize(new Dimension(120, 20));
			lbl_p2_22.setEnabled(false);
			lbl_p2_22.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lbl_p2_31 = new JLabel(Messages
					.getString("QuantAna.Simulation.Mean"));
			lbl_p2_31.setPreferredSize(new Dimension(120, 20));
			lbl_p2_31.setMinimumSize(new Dimension(120, 20));
			lbl_p2_31.setMaximumSize(new Dimension(120, 20));
			lbl_p2_31.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel lbl_p2_32 = new JLabel(Messages
					.getString("QuantAna.Simulation.Deviation"));
			lbl_p2_32.setPreferredSize(new Dimension(120, 20));
			lbl_p2_32.setMinimumSize(new Dimension(120, 20));
			lbl_p2_32.setMaximumSize(new Dimension(120, 20));
			lbl_p2_32.setHorizontalAlignment(SwingConstants.RIGHT);

			constraints.gridx = 0;
			constraints.gridy = 0;
			jp.add(opt_p2_1, constraints);
			constraints.gridx = 1;
			constraints.gridy = 0;
			jp.add(lbl_p2_11, constraints);
			constraints.gridx = 2;
			constraints.gridy = 0;
			jp.add(txt_p2_11, constraints);
			constraints.gridx = 3;
			constraints.gridy = 0;
			jp.add(lbl_p2_12, constraints);
			constraints.gridx = 4;
			constraints.gridy = 0;
			jp.add(txt_p2_12, constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			jp.add(opt_p2_2, constraints);
			constraints.gridx = 1;
			constraints.gridy = 1;
			jp.add(lbl_p2_21, constraints);
			constraints.gridx = 2;
			constraints.gridy = 1;
			jp.add(txt_p2_21, constraints);
/*			constraints.gridx = 3;
			constraints.gridy = 1;
			jp.add(lbl_p2_22, constraints);
			constraints.gridx = 4;
			constraints.gridy = 1;
			jp.add(txt_p2_22, constraints);*/
			constraints.gridx = 0;
			constraints.gridy = 2;
			jp.add(opt_p2_3, constraints);
			constraints.gridx = 1;
			constraints.gridy = 2;
			jp.add(lbl_p2_31, constraints);
			constraints.gridx = 2;
			constraints.gridy = 2;
			jp.add(txt_p2_31, constraints);
			constraints.gridx = 3;
			constraints.gridy = 2;
			jp.add(lbl_p2_32, constraints);
			constraints.gridx = 4;
			constraints.gridy = 2;
			jp.add(txt_p2_32, constraints);
						
			opt_p2_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txt_p2_11.setEnabled(true);
					txt_p2_12.setEnabled(true);
					txt_p2_21.setEnabled(false);
					txt_p2_31.setEnabled(false);
					txt_p2_32.setEnabled(false);
				}
			});

			opt_p2_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txt_p2_11.setEnabled(false);
					txt_p2_12.setEnabled(false);
					txt_p2_21.setEnabled(true);
					txt_p2_31.setEnabled(false);
					txt_p2_32.setEnabled(false);
				}
			});

			opt_p2_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txt_p2_11.setEnabled(false);
					txt_p2_12.setEnabled(false);
					txt_p2_21.setEnabled(false);
					txt_p2_31.setEnabled(true);
					txt_p2_32.setEnabled(true);
				}
			});
			jp.setMinimumSize(new Dimension(750, 115));
			arrivalRateDistributionPanel.add(jp);
			arrivalRateDistributionPanel.add(getButtonPanel());

		}
		return arrivalRateDistributionPanel;
	}

	private JPanel getServiceTimeDistributionPanel() {
		if (serviceTimeDistributionPanel == null) {
			serviceTimeDistributionPanel = new JPanel();
			serviceTimeDistributionPanel
					.setBorder(BorderFactory
							.createTitledBorder(
									BorderFactory.createEtchedBorder(),
									Messages
											.getString("QuantAna.Simulation.ServiceTimeDistribution")));
			GridBagConstraints constraints = new GridBagConstraints();
			serviceTimeDistributionPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;

			groupST = new ButtonGroup();
			JRadioButton opt_p3_1 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Constant"), false);
			JRadioButton opt_p3_2 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Poisson"), true);
			JRadioButton opt_p3_3 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.Gaussian"), false);
			opt_p3_1.setHorizontalAlignment(SwingConstants.LEFT);
			opt_p3_1.setActionCommand("ST_UNIFORM");
			opt_p3_1.setPreferredSize(new Dimension(180, 20));
			opt_p3_1.setMinimumSize(new Dimension(180, 20));
			opt_p3_1.setMaximumSize(new Dimension(180, 20));
			opt_p3_2.setHorizontalAlignment(SwingConstants.LEFT);
			opt_p3_2.setActionCommand("ST_EXP");
			opt_p3_2.setPreferredSize(new Dimension(180, 20));
			opt_p3_2.setMinimumSize(new Dimension(180, 20));
			opt_p3_2.setMaximumSize(new Dimension(180, 20));
			opt_p3_3.setHorizontalAlignment(SwingConstants.LEFT);
			opt_p3_3.setActionCommand("ST_GAUSS");
			opt_p3_3.setPreferredSize(new Dimension(180, 20));
			opt_p3_3.setMinimumSize(new Dimension(180, 20));
			opt_p3_3.setMaximumSize(new Dimension(180, 20));

			groupST.add(opt_p3_1);
			groupST.add(opt_p3_2);
			groupST.add(opt_p3_3);
			
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridx = 0;
			constraints.gridy = 0;
			serviceTimeDistributionPanel.add(opt_p3_1, constraints);

			constraints.gridx = 0;
			constraints.gridy = 1;
			serviceTimeDistributionPanel.add(opt_p3_2, constraints);
			
			constraints.gridx = 0;
			constraints.gridy = 2;
			serviceTimeDistributionPanel.add(opt_p3_3, constraints);
		}
		return serviceTimeDistributionPanel;
	}

	private JPanel getQueueingDisciplinePanel() {
		if (queueingDisciplinePanel == null) {
			queueingDisciplinePanel = new JPanel();
			queueingDisciplinePanel
					.setBorder(BorderFactory
							.createTitledBorder(
									BorderFactory.createEtchedBorder(),
									Messages
											.getString("QuantAna.Simulation.QueueingDiscipline")));

			GridBagConstraints constraints = new GridBagConstraints();
			queueingDisciplinePanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			groupQD = new ButtonGroup();

			JRadioButton opt_q_1 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.QueueingFIFO"), true);
			opt_q_1.setPreferredSize(new Dimension(180, 20));
			opt_q_1.setMinimumSize(new Dimension(180, 20));
			opt_q_1.setMaximumSize(new Dimension(180, 20));
			opt_q_1.setActionCommand("QUEUE_FIFO");
			groupQD.add(opt_q_1);

			JRadioButton opt_q_2 = new JRadioButton(Messages
					.getString("QuantAna.Simulation.QueueingLIFO"), false);
			opt_q_2.setPreferredSize(new Dimension(180, 20));
			opt_q_2.setMinimumSize(new Dimension(180, 20));
			opt_q_2.setMaximumSize(new Dimension(180, 20));
			opt_q_2.setActionCommand("QUEUE_LIFO");
			groupQD.add(opt_q_2);
			JLabel dummy = new JLabel();
			dummy.setPreferredSize(new Dimension(180, 20));
			dummy.setMinimumSize(new Dimension(180, 20));
			dummy.setMaximumSize(new Dimension(180, 20));
			
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridx = 0;
			constraints.gridy = 0;
			queueingDisciplinePanel.add(opt_q_1, constraints);

			constraints.gridx = 0;
			constraints.gridy = 1;
			queueingDisciplinePanel.add(opt_q_2, constraints);

			constraints.gridx = 0;
			constraints.gridy = 2;
			queueingDisciplinePanel.add(dummy, constraints);

		}
		return queueingDisciplinePanel;
	}

	private JPanel getTerminationConditionPanel() {
		if (terminationConditionPanel == null) {
			terminationConditionPanel = new JPanel();
			terminationConditionPanel
					.setBorder(BorderFactory.createTitledBorder(BorderFactory
							.createEtchedBorder(), Messages
							.getString("QuantAna.Simulation.TerminationRule")));

			GridBagConstraints constraints = new GridBagConstraints();
			terminationConditionPanel.setLayout(new GridBagLayout());
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			JLabel lblRuns = new JLabel(Messages
					.getString("QuantAna.Simulation.NumRuns"));
			lblRuns.setPreferredSize(new Dimension(80, 20));
			lblRuns.setMinimumSize(new Dimension(80, 20));
			lblRuns.setMaximumSize(new Dimension(80, 20));
			txtRuns = new JTextField("1");
			txtRuns.setPreferredSize(new Dimension(80, 20));
			txtRuns.setMinimumSize(new Dimension(80, 20));
			txtRuns.setMaximumSize(new Dimension(80, 20));
			txtRuns.setHorizontalAlignment(SwingConstants.RIGHT);
			stop1 = new JCheckBox(Messages
					.getString("QuantAna.Simulation.CasesCompleted"));
			stop2 = new JCheckBox(Messages
					.getString("QuantAna.Simulation.TimeElapsed"));
			stop1.setSelected(true);
			stop2.setSelected(true);
			constraints.insets = new Insets(5, 10, 5, 5);
			constraints.gridx = 0;
			constraints.gridy = 0;
			terminationConditionPanel.add(lblRuns, constraints);
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridx = 1;
			constraints.gridy = 0;
			terminationConditionPanel.add(txtRuns, constraints);
			constraints.gridx = 0;
			constraints.gridy = 1;
			terminationConditionPanel.add(stop1, constraints);
			constraints.gridx = 0;
			constraints.gridy = 2;
			terminationConditionPanel.add(stop2, constraints);
		}
		return terminationConditionPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 15, 5, 5);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.EAST;
			constraints.fill = GridBagConstraints.HORIZONTAL;

			JButton btnStart = new JButton();
			btnStart.setText(Messages.getTitle("QuantAna.Button.Start"));
			btnStart.setIcon(Messages.getImageIcon("QuantAna.Button.Start"));
			btnStart.setMinimumSize(new Dimension(120, 25));
			btnStart.setMaximumSize(new Dimension(120, 25));
			btnStart.setPreferredSize(new Dimension(120, 25));
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					startSimulation();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttonPanel.add(btnStart, constraints);

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

	private void getTimeModelDialog() {
		new TimeModelDialog(this, tm);
	}

	public void updContents() {
	}

	public WorkflowNetGraph getGraph() {
		return graph;
	}

	private void startSimulation() {
		SimParameters sp = new SimParameters();

		sp.setLambda(lambda);
		sp.setTimeOfPeriod(period);

		sp.setRuns(Integer.parseInt(txtRuns.getText()));

		String op1 = groupIAT.getSelection().getActionCommand();
		if (op1.equals("IAT_UNIFORM")) {
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_UNIFORM);
			sp.setCPara1(Double.parseDouble(txt_p2_11.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_12.getText()));
		} else if (op1.equals("IAT_GAUSS")) {
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_GAUSS);
			sp.setCPara1(Double.parseDouble(txt_p2_31.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_32.getText()));
		} else {
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_EXP);
			sp.setCPara1(Double.parseDouble(txt_p2_21.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_22.getText()));
		}

		String op2 = groupST.getSelection().getActionCommand();
		if (op2.equals("ST_UNIFORM")) {
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_UNIFORM);
		} else if (op2.equals("ST_GAUSS")) {
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_GAUSS);
		} else {
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_EXP);
		}

		String op3 = groupQD.getSelection().getActionCommand();
		if (op3.equals("QUEUE_LIFO")) {
			sp.setQueue(Simulator.QD_LIFO);
		} else {
			sp.setQueue(Simulator.QD_FIFO);
		}

		if (stop1.isSelected()) {
			if (stop2.isSelected())
				sp.setStop(Simulator.STOP_BOTH);
			else
				sp.setStop(Simulator.STOP_CASE_DRIVEN);
		} else if (stop2.isSelected()) {
			sp.setStop(Simulator.STOP_TIME_DRIVEN);
		} else {
			sp.setStop(Simulator.STOP_NONE);
		}

		if (groupRoleNum > 2 && resObjNum > 1) {
			sp.setResUse(Simulator.RES_USED);
		} else {
			sp.setResUse(Simulator.RES_NOT_USED);
		}

		Simulator sim = new Simulator(graph, new ResourceUtilization(resAlloc),
				sp);
		sim.start();
	}

	private void initResourceAlloc() {
		PetriNetModelProcessor pmp = (PetriNetModelProcessor) editor
				.getModelProcessor();

		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> groups = new ArrayList<String>();
		Vector rVec = (Vector) pmp.getRoles();
		Vector gVec = (Vector) pmp.getOrganizationUnits();

		groupRoleNum = rVec.size() + gVec.size();

		for (int i = 0; i < rVec.size(); i++)
			roles.add(((ResourceClassModel) rVec.get(i)).getName());

		for (int i = 0; i < gVec.size(); i++)
			groups.add(((ResourceClassModel) gVec.get(i)).getName());

		Iterator iter = getTransModels().iterator();

		resAlloc = new ResourceAllocation(roles, groups, iter, pmp);

		resObjNum = resAlloc.getResources().size();
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

} // @jve:decl-index=0:visual-constraint="4,4"