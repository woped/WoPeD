package org.woped.quantanalysis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.graph.Arc;
import org.woped.graph.Key;
import org.woped.graph.Node;
import org.woped.graph.NodePair;
import org.woped.graph.WorkflowNetGraph;
import org.woped.resourcealloc.ResourceAllocation;
import org.woped.resourcealloc.ResourceClassTaskAllocationTable;
import org.woped.resourcealloc.ResourceUtilization;
import org.woped.simulation.ProbabilityDistribution;
import org.woped.simulation.Simulator;
import org.woped.simulation.output.SimOutputDialog;

public class QuantitativeAnalysis extends JDialog {

	private static final long serialVersionUID	= 1L;
	
	private static final String TIME_MINUTE		= "minute(s)";
	private static final String TIME_SECOND		= "second(s)";
	private static final String TIME_HOUR		= "hour(s)";
	private static final String TIME_DAY		= "day(s)";
	private static final String TIME_WEEK		= "week(s)";
	private static final String TIME_MONTH		= "month(s)";
	private static final String TIME_YEAR		= "year(s)";
	
	private EditorVC editor = null;
	private JPanel jContentPane = null;
	private JTabbedPane register = null;
	private JPanel capaPanel = new JPanel();
	private JPanel simuPanel = new JPanel();
	private JPanel genPanel = new JPanel();
	
	private JLabel lblTimeUnit = null;
	private JLabel lblPeriod = null;
	private JLabel lblCases = null;
	private JTextField txtTimeUnit = null;
	private JTextField txtPeriod = null;
	private JTextField txtCases = null;
	private JComboBox cboTimeUnit = null;
	private JComboBox cboPeriodUnit = null;
	private JLabel lbl = new JLabel();
	private JLabel lbl2 = new JLabel();
	
	private JLabel lblPrecision = null;
	private JTextField txtPrecision = null;
	private JLabel lbl3 = new JLabel();
	private JButton btnApply = null;
	
	private double timeUnit = 1.0;
	private String timeIntervall = "minute(s)";
	private double period = 480.0; //8.0;
	private String periodIntervall = "minute(s)"; //"hour(s)";
	private double lambda = 50.0;
	private double epsilon = 0.001;
	
	StructuralAnalysis sa;
	ModelElementContainer mec;
	WorkflowNetGraph graph;
	ResourceAllocation resAlloc;
	TimeModel tm;
	int numResCls;
	//ResourceUtilization resUtil;
	//double[][] data;
	Object[][] obj;
	String[] colNames = {"Task", "Runs per Case", "Cases per Period", "Servicetime per Task", "Servicetime per Case", "Servicetime per Period"};
	String[] colNames2 = {"Resource class", "Average number of minutes", "Number of resources (capacity 100%)"};
	int numTrans = 0;
	HashMap<Key, Node> unfoldedNet = new HashMap<Key, Node>();
	
	// Simulation Dialog
	JTextField txtRuns;
	ButtonGroup groupIAT;
	ButtonGroup groupST;
	ButtonGroup groupQD;
	JTextField txt_p2_11;
	JTextField txt_p2_12;
	JTextField txt_p2_21;
	JTextField txt_p2_22;
	JTextField txt_p2_31;
	JTextField txt_p2_32;
	JTextField txt_p3_11;
	JTextField txt_p3_12;
	JTextField txt_p3_21;
	JTextField txt_p3_22;
	JTextField txt_p3_31;
	JTextField txt_p3_32;
	JCheckBox stop1;
	JCheckBox stop2;
	

	/**
	 * This is the default constructor
	 */
	public QuantitativeAnalysis(Frame owner, boolean modal, EditorVC editor) {
		super(owner, modal);
		this.editor = editor;
		
		sa = new StructuralAnalysis(editor);//, new File(ConfigurationManager.getConfiguration().getHomedir() + "temp"));
		mec = editor.getModelProcessor().getElementContainer();
		
		graph = new WorkflowNetGraph(sa, mec);
		numTrans = graph.getNumTransitions();
		
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocation(20, 20);
		this.setContentPane(getJContentPane());
		this.setTitle("Quantitative Analysis");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getRegister(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes register	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getRegister() {
		getRegisterTab1();
		getRegisterTab2();
		getRegisterTab3();
		
		if (register == null) {
			register = new JTabbedPane();
			
			register.addTab("General Information", genPanel);
			register.addTab("Capacity Plan", capaPanel);
			register.addTab("Simulation", simuPanel);
			

			register.setEnabledAt(register.indexOfTab("Capacity Plan"), false);
			register.setEnabledAt(register.indexOfTab("Simulation"), false);
		}
		return register;
	}
	
	private void getRegisterTab1()
	{
		lblTimeUnit = new JLabel();
		lblTimeUnit.setText("time unit:");
		lblTimeUnit.setAlignmentX(0);
		lblPeriod = new JLabel();
		lblPeriod.setText("period:");
		lblPeriod.setAlignmentX(0);
		lblCases = new JLabel();
		lblCases.setText("new cases per period (" + '\u03BB' + "):");
		lblCases.setAlignmentX(0);
		txtTimeUnit = new JTextField();
		txtTimeUnit.setText(Double.valueOf(timeUnit).toString());
		txtTimeUnit.setPreferredSize(new Dimension(100, 20));
		txtPeriod = new JTextField();
		txtPeriod.setText(Double.valueOf(period).toString());
		txtCases = new JTextField();
		txtCases.setText(Double.valueOf(lambda).toString());
		txtPrecision = new JTextField();
		txtPrecision.setText(Double.valueOf(epsilon).toString());
		lblPrecision = new JLabel("precision ("+ '\u03B5' + "): ");
		btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				applyProperties();
			}
		});
		
		//String[] units = {"minute(s)", "second(s)", "hour(s)", "day(s)", "month(s)", "year(s)"};
		String[] units = {TIME_SECOND, TIME_MINUTE, TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH, TIME_YEAR};
		cboTimeUnit = new JComboBox(units);
		cboTimeUnit.setSelectedItem(timeIntervall);
		cboTimeUnit.setPreferredSize(new Dimension(100, txtTimeUnit.getHeight()));
		cboPeriodUnit = new JComboBox(units);
		cboPeriodUnit.setSelectedItem(periodIntervall);
		
		GridBagLayout genLayout = new GridBagLayout();
		genPanel.setLayout(genLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lbl2, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblTimeUnit, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblPeriod, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblCases, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblPrecision, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtTimeUnit, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtPeriod, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtCases, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtPrecision, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(cboTimeUnit, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(cboPeriodUnit, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lbl, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(btnApply, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lbl3, constraints);
	}
	
	private void getRegisterTab2()
	{
		startCalculationTab2();
		
		capaPanel.setLayout(new BorderLayout());
		
		JTable tableTasks = new JTable(createObjectMatrix(numTrans), colNames);
		tableTasks.setEnabled(false);
		tableTasks.setBackground(capaPanel.getBackground());
		tableTasks.setMinimumSize(new Dimension(780, 270));
//		tableTasks.setAutoscrolls(true);
		JTable tableRes = getResClassTable();
		tableRes.setEnabled(false);
		tableRes.setBackground(capaPanel.getBackground());
		tableRes.setMinimumSize(new Dimension(780, 270));
//		tableRes.setMaximumSize(new Dimension(780, 270));

		Font fnt = new Font("Arial Bold", Font.PLAIN, 12);
		
		JLabel lblUnfolding = new JLabel();
		lblUnfolding.setText("The unfolded net comprises " + unfoldedNet.size() + " nodes.");
		String dev = String.format("%6.1f", (1 - graph.getSinkPlace().getNumOfRuns() / lambda) * 100);
		JLabel lblDeviation = new JLabel();
		lblDeviation.setText("The relative deviation is (estimated): " + dev + "%");
		lblUnfolding.setFont(fnt);
		lblDeviation.setFont(fnt);
		JPanel messagePanel = new JPanel();
		messagePanel.add(lblUnfolding);
		messagePanel.add(lblDeviation);
		
		JLabel lblDummy = new JLabel();
		JLabel lblDummy2 = new JLabel();
		JLabel lblHeading1 = new JLabel("The capacity required per Task: ");
		lblHeading1.setFont(fnt);
		lblHeading1.setAlignmentX(0);
		JLabel lblHeading2 = new JLabel("The capacity requirement per resource class: ");
		lblHeading2.setFont(fnt);
		lblHeading2.setAlignmentX(0);
		
//		capaPanel.add(new JScrollPane().add(tableTasks), BorderLayout.NORTH);
		
		GridBagLayout genLayout = new GridBagLayout();
		capaPanel.setLayout(genLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(lblDummy, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(lblHeading1, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(tableTasks, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(lblHeading2, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(tableRes, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(messagePanel, constraints);
		
		String text = "";
		Node[] nodes = graph.getNodeArray();
		for (int i = 0; i < nodes.length; i++){
			text += "\n(" + nodes[i].getId() + ": " + nodes[i].getIteration() + ")";
		}
		JLabel iterations = new JLabel();
		iterations.setText(text);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(iterations, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(lblDummy2, constraints);
	}
	
	private void getRegisterTab3()
	{
		simuPanel.setLayout(new GridLayout(6,1));
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		
		// Panel 1
		JLabel lblRuns = new JLabel("Number of simulation runs: ");
		txtRuns = new JTextField("1");
		txtRuns.setPreferredSize(new Dimension(100, 20));
		p1.add(lblRuns);
		p1.add(txtRuns);
		
		Border borderRuns = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null);
		p1.setBorder(borderRuns);

		// Panel 2
		String title = "Probabilty Distribution of interarrival times";
		groupIAT = new ButtonGroup();
		p2.setLayout(new GridLayout(3,3));
		JRadioButton opt_p2_1 = new JRadioButton("Uniform distribution", false);
		JRadioButton opt_p2_2 = new JRadioButton("Exponential distribution", true);
		JRadioButton opt_p2_3 = new JRadioButton("Gaussian distribution", false);
		opt_p2_1.setActionCommand("IAT_UNIFORM");
		opt_p2_2.setActionCommand("IAT_EXP");
		opt_p2_3.setActionCommand("IAT_GAUSS");
		groupIAT.add(opt_p2_1);
		groupIAT.add(opt_p2_2);
		groupIAT.add(opt_p2_3);
		JPanel p2_11 = new JPanel();
		p2_11.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p2_12 = new JPanel();
		p2_12.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p2_21 = new JPanel();
		p2_21.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p2_22 = new JPanel();
		p2_22.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p2_31 = new JPanel();
		p2_31.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p2_32 = new JPanel();
		p2_32.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		txt_p2_11 = new JTextField("0");
		txt_p2_11.setPreferredSize(new Dimension(100, 20));
		txt_p2_12 = new JTextField(Double.toString(2/Double.valueOf(txtCases.getText())));
		txt_p2_12.setPreferredSize(new Dimension(100, 20));
		txt_p2_21 = new JTextField(Double.toString(1/Double.valueOf(txtCases.getText())));
		txt_p2_21.setPreferredSize(new Dimension(100, 20));
		txt_p2_22 = new JTextField("0");
		txt_p2_22.setEnabled(false);
		txt_p2_22.setPreferredSize(new Dimension(100, 20));
		txt_p2_31 = new JTextField("0");
		txt_p2_31.setPreferredSize(new Dimension(100, 20));
		txt_p2_32 = new JTextField("1");
		txt_p2_32.setPreferredSize(new Dimension(100, 20));
		
		JLabel lbl_p2_11 = new JLabel("From");
		lbl_p2_11.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p2_12 = new JLabel("To");
		lbl_p2_12.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p2_21 = new JLabel("Mean");
		lbl_p2_21.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p2_22 = new JLabel("Position");
		lbl_p2_22.setPreferredSize(new Dimension(100, 20));
		lbl_p2_22.setEnabled(false);
		JLabel lbl_p2_31 = new JLabel("Mean");
		lbl_p2_31.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p2_32 = new JLabel("Std. deviation");
		lbl_p2_32.setPreferredSize(new Dimension(100, 20));
		
		p2_11.add(lbl_p2_11);
		p2_11.add(txt_p2_11);
		p2_12.add(lbl_p2_12);
		p2_12.add(txt_p2_12);
		p2_21.add(lbl_p2_21);
		p2_21.add(txt_p2_21);
		p2_22.add(lbl_p2_22);
		p2_22.add(txt_p2_22);
		p2_31.add(lbl_p2_31);
		p2_31.add(txt_p2_31);
		p2_32.add(lbl_p2_32);
		p2_32.add(txt_p2_32);
		
		p2.add(opt_p2_1);
		p2.add(p2_11);
		p2.add(p2_12);
		p2.add(opt_p2_2);
		p2.add(p2_21);
		p2.add(p2_22);
		p2.add(opt_p2_3);
		p2.add(p2_31);
		p2.add(p2_32);
		
		Border border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		p2.setBorder(border);
		
		// Panel 3
		String title2 = "Probabilty Distribution of service times";
		groupST = new ButtonGroup();
		p3.setLayout(new GridLayout(3,3));
		JRadioButton opt_p3_1 = new JRadioButton("Uniform distribution", false);
		JRadioButton opt_p3_2 = new JRadioButton("Exponential distribution", true);
		JRadioButton opt_p3_3 = new JRadioButton("Gaussian distribution", false);
		opt_p3_1.setActionCommand("ST_UNIFORM");
		opt_p3_2.setActionCommand("ST_EXP");
		opt_p3_3.setActionCommand("ST_GAUSS");
		groupST.add(opt_p3_1);
		groupST.add(opt_p3_2);
		groupST.add(opt_p3_3);
		JPanel p3_11 = new JPanel();
		p3_11.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p3_12 = new JPanel();
		p3_12.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p3_21 = new JPanel();
		p3_21.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p3_22 = new JPanel();
		p3_22.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p3_31 = new JPanel();
		p3_31.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel p3_32 = new JPanel();
		p3_32.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		txt_p3_11 = new JTextField("0");
		txt_p3_11.setPreferredSize(new Dimension(100, 20));
		txt_p3_12 = new JTextField(Double.toString(2/Double.valueOf(txtCases.getText())));
		txt_p3_12.setPreferredSize(new Dimension(100, 20));
		txt_p3_21 = new JTextField(Double.toString(1/Double.valueOf(txtCases.getText())));
		txt_p3_21.setPreferredSize(new Dimension(100, 20));
		txt_p3_22 = new JTextField("0");
		txt_p3_22.setEnabled(false);
		txt_p3_22.setPreferredSize(new Dimension(100, 20));
		txt_p3_31 = new JTextField("0");
		txt_p3_31.setPreferredSize(new Dimension(100, 20));
		txt_p3_32 = new JTextField("1");
		txt_p3_32.setPreferredSize(new Dimension(100, 20));
		
		JLabel lbl_p3_11 = new JLabel("From");
		lbl_p3_11.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p3_12 = new JLabel("To");
		lbl_p3_12.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p3_21 = new JLabel("Mean");
		lbl_p3_21.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p3_22 = new JLabel("Position");
		lbl_p3_22.setPreferredSize(new Dimension(100, 20));
		lbl_p3_22.setEnabled(false);
		JLabel lbl_p3_31 = new JLabel("Mean");
		lbl_p3_31.setPreferredSize(new Dimension(100, 20));
		JLabel lbl_p3_32 = new JLabel("Std. deviation");
		lbl_p3_32.setPreferredSize(new Dimension(100, 20));
		
		p3_11.add(lbl_p3_11);
		p3_11.add(txt_p3_11);
		p3_12.add(lbl_p3_12);
		p3_12.add(txt_p3_12);
		p3_21.add(lbl_p3_21);
		p3_21.add(txt_p3_21);
		p3_22.add(lbl_p3_22);
		p3_22.add(txt_p3_22);
		p3_31.add(lbl_p3_31);
		p3_31.add(txt_p3_31);
		p3_32.add(lbl_p3_32);
		p3_32.add(txt_p3_32);
		
		p3.add(opt_p3_1);
		p3.add(p3_11);
		p3.add(p3_12);
		p3.add(opt_p3_2);
		p3.add(p3_21);
		p3.add(p3_22);
		p3.add(opt_p3_3);
		p3.add(p3_31);
		p3.add(p3_32);
		
		Border border2 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title2);
		p3.setBorder(border2);
		
		// Panel 4
		String title3 = "Queueing discipline";
		groupQD = new ButtonGroup();
		JRadioButton opt_q_1 = new JRadioButton("First in - First out (FIFO)", true);
		JRadioButton opt_q_2 = new JRadioButton("Last in - First out (LIFO)", false);
		opt_q_1.setActionCommand("QUEUE_FIFO");
		opt_q_2.setActionCommand("QUEUE_LIFO");
		groupQD.add(opt_q_1);
		groupQD.add(opt_q_2);
		p4.add(opt_q_1);
		p4.add(opt_q_2);
		
		Border border3 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title3);
		p4.setBorder(border3);
		
		// Panel 5
		String title4 = "Stopping rule";
		stop1 = new JCheckBox('\u03BB' + " cases have completed service");
		stop2 = new JCheckBox("Time period has expired");
		stop1.setSelected(true);
		p5.add(stop1);
		p5.add(stop2);
		
		Border border4 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title4);
		p5.setBorder(border4);
		
		// Button Panel
		JButton btnStart = new JButton("Start");
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				startSimulation();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnStart);
		
		// All together
		simuPanel.add(p1);
		simuPanel.add(p2);
		simuPanel.add(p3);
		simuPanel.add(p4);
		simuPanel.add(p5);
		simuPanel.add(buttonPanel);
		
//		startSimulation(); // <------
	}
	
	private void startCalculationTab2(){
		//initData();
		calculateNumOfRuns(graph);
		
		initResourceAlloc();
		numResCls = resAlloc.getNumOfResClasses();
		
		//classTaskAllocToString(); // <--- Meldung anzeigen
	}
	
	/*private void initData(){
		data = new double[numTrans][colNames.length - 1];
		for (int c = 0; c < colNames.length - 1; c++){
			switch (c){
			case 2:
				for (int r = 0; r < numTrans; r++)
					data[r][c] = 1.0;
				break;
			default:
				for (int r = 0; r < numTrans; r++)
					data[r][c] = 0.0;
			}
		}
	}*/
	
	private Object[][] createObjectMatrix(int rows){
		obj = new Object[rows + 1][7];
		String[] trans = graph.getTransitions();
		double[] times = graph.getTimes();
		double[] runs = graph.getRuns();
		double sumCase = 0.0;
		double sumPeriod = 0.0;
		
		for (int r = 0; r < rows; r++){
			double n = runs[r];
			double n1 = n / lambda;
			double t = times[r];
			
			sumCase += n1 * t;
			sumPeriod += n * t;
			
			obj[r][0] = trans[r];
			obj[r][1] = Double.valueOf(n1);
			obj[r][2] = Double.valueOf(n);
			obj[r][3] = Double.valueOf(t);
			obj[r][4] = Double.valueOf(n1 * t);
			obj[r][5] = Double.valueOf(n * t);
		}
		
		obj[rows][0] = "Sum";
		obj[rows][1] = "";
		obj[rows][2] = "";
		obj[rows][3] = "";
		obj[rows][4] = Double.valueOf(sumCase);
		obj[rows][5] = Double.valueOf(sumPeriod);
		
		return obj;
	}
	
	private void calculateNumOfRuns(WorkflowNetGraph g){
		/*double curProb = 0.0;
		LinkedList<Node> roots = new LinkedList<Node>();
		g.resetMarking();
		prepareGraph(g.getStartPlace(), curProb, roots);
		
		for (Node n : roots){
			g.resetMarking();
			if (!n.equals(findRoot(n)))
				roots.remove(n);
		}
		
		g.resetMarking();
		Node start = g.getStartPlace();
		start.setNumOfRuns(1.0);
		start.setMarkiert(Node.FINISHED);
		for (Arc a : start.getSuccessor())
			calcRuns(start, a.getTarget());
		
		checkRuns(roots);*/
		
		/*Node n = graph.getStartPlace();
		lambda = Double.parseDouble(txtCases.getText());
		epsilon = Double.parseDouble(txtPrecision.getText());
		n.setNumOfRuns(lambda);
		n.setTempSum(lambda);
		n.setIteration(0);
		
		runThroughNet(n);*/
		
		lambda = Double.parseDouble(txtCases.getText());
		epsilon = Double.parseDouble(txtPrecision.getText());
		//HashMap<Key, Node> netTemp = unfoldNet(graph, lambda, epsilon);
		unfoldNet(graph, lambda, epsilon);
		
		Node[] origNet = graph.getNodeArray();
		for (Key k : unfoldedNet.keySet()){
			String id = k.getId();
			Node n = origNet[graph.getNodeIdx(id)];
			n.setNumOfRuns(n.getNumOfRuns() + k.getRuns());
			n.setIteration(n.getIteration() + 1);
		}
	}
	
	/*private void runThroughNet(Node n){
		for (Arc a : n.getSuccessor()){
			double val = a.getProbability() * n.getNumOfRuns();
			if (!(val < epsilon)){
				Node m = a.getTarget();
				m.setNumOfRuns(val);
				m.setTempSum(m.getTempSum() + val * m.getMultiply());
				m.setIteration(m.getIteration() + 1);
				
				runThroughNet(m);
			}
		}
	}*/
	
	private void unfoldNet(WorkflowNetGraph g, double l, double e){
		//HashMap<Key, Node> net = new HashMap<Key, Node>();
		Node s = g.getStartPlace();
		Node n = new Node(s.getId(), s.getName());
		n.setTempRuns(lambda);
		Key start = new Key(n.getId(), lambda);
		unfoldedNet.put(start, n);
		
		LinkedList<NodePair> queue = new LinkedList<NodePair>();
		queue.add(new NodePair(s, n));
		runThroughNet(queue);
		
		//return net;
	}
	
	/*// rekursive Funktion, mit Tiefensuche
	private void runThroughNet(Node n, Node x){
		for (Arc a : n.getSuccessor()){
			double val = a.getProbability() * x.getTempRuns();
			if (!(val < epsilon)){
				Node m = a.getTarget();
				Node y = new Node(m.getId(), m.getName());
				y.setTempRuns(val);
				x.getSuccessor().add(new Arc(y, a.getProbability()));
				Key k = new Key(m.getId(), val);
				unfoldedNet.put(k, y);
				
				runThroughNet(m, y);
			}
		}
	}*/
	
	// rekursive Funktion mit Breitensuche
	private void runThroughNet(LinkedList<NodePair> q){
		if (!(q.isEmpty())){
			NodePair np = q.removeFirst();
			for (Arc a : np.getFirst().getSuccessor()){
				Node m = a.getTarget();
				if (!m.isJoinReached()){
					double val = a.getProbability() * np.getSecond().getTempRuns();
					if (!(val < epsilon)){
						Node y = new Node(m.getId(), m.getName());
						y.setTempRuns(val);
						if (m.isAndJoin()) m.setJoinReached(true);
						np.getSecond().getSuccessor().add(new Arc(y, a.getProbability()));
						Key k = new Key(m.getId(), val);
						unfoldedNet.put(k, y);
						
						if (!(containsElement(q, m))) q.add(new NodePair(m, y));
						runThroughNet(q);
					}
				}
			}
		}
	}
	
	private boolean containsElement(LinkedList<NodePair> q, Node n){
		boolean contains = false;
		Iterator<NodePair> i = q.iterator();
		while (i.hasNext()){
			NodePair p = i.next();
			if (n.equals(p.getFirst())){
				contains = true;
				break;
			}
		}
		
		return contains;
	}
	
	/*private void prepareGraph(Node n, double p, LinkedList<Node> l){
		n.setMarkiert(Node.FINISHED);
		for (Arc a : n.getSuccessor()){
			Node target = a.getTarget();
			if (target.getMarkiert() != Node.FINISHED){
				if (a.getProbability() != 1.0)
					p = a.getProbability();
				
				prepareGraph(target, p, l);
			} else {
				a.setColor(Arc.COLOR_RED);
				l.add(target);
				target.setFoundCycle(true);
				a.setMultiply(p);
				
				for (Arc b : target.getPredecessor()){
					if (b.getSource().equals(a.getSource())){
						b.setColor(Arc.COLOR_RED);
						b.setMultiply(p);
					}
				}
			}
		}
	}
	
	private Node findRoot(Node n){
		if (n.getPredecessor() != null){
			n.setMarkiert(Node.FINISHED);
			for (Arc a : n.getPredecessor())
				if (a.getSource().getMarkiert() != Node.FINISHED)
					findRoot(a.getSource());
		}
		
		return n;
	}
	
	private void calcRuns(Node pre, Node cur){
		double runs = 0.0;
		ArrayList<Arc> pres = cur.getPredecessor();
		int numPreNodes = pres.size();
		
		//cur.setMarkiert(Node.FINISHED);
		if (cur.getMarkiert() == Node.NOT_STARTED) {
			switch (numPreNodes){
			case 1:
				runs = pre.getNumOfRuns() * pres.get(0).getProbability();
				break;
			default:
				runs = pre.getNumOfRuns();// * pres.get(0).getProbability();
				for (Arc a : pre.getSuccessor())
					if (a.getTarget().equals(cur))
						runs *= a.getProbability();
	
				for (Arc a : cur.getPredecessor()){
					if (!(a.getSource().equals(pre))){
						double mult = a.getMultiply();
						if (mult != 1.0){
							runs /= (1 - mult);
						} else {
							cur.setUseSum(true);
							cur.getTempArcs().add(a);
						}
					}
				}
			}
		} else if (cur.getMarkiert() == Node.BUSY){
			runs = cur.getTempSum();
			
			for (Arc a : cur.getTempArcs()){
				runs += a.getSource().getNumOfRuns();
			}
			
			cur.setUseSum(false);
		}
		
		if (cur.isUseSum()){
			cur.setTempSum(runs);
			cur.setMarkiert(Node.BUSY);
		} else {
			cur.setNumOfRuns(runs);
			cur.setMarkiert(Node.FINISHED);
		}
		
		for (Arc a : cur.getSuccessor())
			if (a.getTarget().getMarkiert() != Node.FINISHED)
				calcRuns(cur, a.getTarget());
	}
	
	private void checkRuns(LinkedList<Node> l){
		for (Node n : l){
			double sum = 0.0;
			for (Arc s : n.getPredecessor())
				sum += s.getSource().getNumOfRuns();
			
			if (sum != n.getNumOfRuns()){
				n.setNumOfRuns(sum);
				
				Node succ = n.getSuccessor().get(0).getTarget();
				while (succ != null && !succ.isFoundCycle()){
					succ.setNumOfRuns(sum);
					if (succ.getSuccessor().size() > 0)
						succ = succ.getSuccessor().get(0).getTarget();
					else
						succ = null;
				}
			}
		}
	}*/
	
	private void initResourceAlloc(){
		PetriNetModelProcessor pmp = (PetriNetModelProcessor)editor.getModelProcessor();
		
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> groups = new ArrayList<String>();
		Vector rVec = (Vector)pmp.getRoles();
		Vector gVec = (Vector)pmp.getOrganizationUnits();
		for (int i = 0; i < rVec.size(); i++)
			roles.add(((ResourceClassModel)rVec.get(i)).getName());
		
		for (int i = 0; i < gVec.size(); i++)
			groups.add(((ResourceClassModel)gVec.get(i)).getName());
		
		Iterator iter = getTransModels().iterator();
		
		resAlloc = new ResourceAllocation(roles, groups, iter, pmp);
		
//		JOptionPane.showMessageDialog(null, resAlloc);
	}
	
	private LinkedList<TransitionModel> getTransModels(){
		LinkedList<TransitionModel> lst = new LinkedList<TransitionModel>();
		ArrayList<String> ids = new ArrayList<String>();
		Node[] nodes = graph.getNodeArray();
		
		for (int i = 0; i < nodes.length; i++)
			if (graph.isTransition(nodes[i].getId()))
				ids.add(nodes[i].getId());
				
		for (int i = 0; i < ids.size(); i++){
			lst.add((TransitionModel)mec.getElementById(ids.get(i)));
		}
		
		return lst;
	}
	
	/*private void classTaskAllocToString(){
		String text = "";
		ArrayList<ResourceClassTaskAllocation> r = resAlloc.getResClsTskAlloc().getTable();
		for (int i = 0; i < r.size(); i++){
			text += "\n" + r.get(i).getResClass() + ": " + r.get(i).getTasks();
		}
		
		JOptionPane.showMessageDialog(null, text);
	}*/
	
	private JTable getResClassTable(){
		Object[][] content = new Object[numResCls][3];
		ResourceClassTaskAllocationTable rcta = resAlloc.getResClsTskAlloc();
		
		for (int i = 0; i < numResCls; i++){
			content[i][0] = rcta.getTable().get(i).getResClass();

			double sum = 0.0;
			ArrayList<String> min = rcta.getTable().get(i).getTasks();
			for (int j = 0; j < min.size(); j++){
				int idx = -1;
				for (int k = 0; k < numTrans; k++){
					if (((String)obj[k][0]).equals(min.get(j))) idx = k;
				}
				
				if (idx != -1) sum += ((Double)obj[idx][5]).doubleValue();
			}
			
			content[i][1] = Double.valueOf(sum);
			
			content[i][2] = Double.valueOf(sum / period);
		}
		
		return new JTable(content, colNames2);
	}
	
	/*private void addRadioButton(String name, boolean status, ButtonGroup g, JPanel p){
		JRadioButton button = new JRadioButton(name, status);
		g.add(button);
		p.add(button);
	}*/
	
	private void startSimulation(){
		SimParameters sp = new SimParameters();
		
		sp.setLambda(Double.parseDouble(txtCases.getText()));
		sp.setTimeOfPeriod(Double.parseDouble(txtPeriod.getText()));
		
		sp.setRuns(Integer.parseInt(txtRuns.getText()));
		
		String op1 = groupIAT.getSelection().getActionCommand();
		if (op1.equals("IAT_UNIFORM")){
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_UNIFORM);
			sp.setCPara1(Double.parseDouble(txt_p2_11.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_12.getText()));
		} else if (op1.equals("IAT_GAUSS")){
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_GAUSS);
			sp.setCPara1(Double.parseDouble(txt_p2_31.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_32.getText()));
		} else {
			sp.setDistCases(ProbabilityDistribution.DIST_TYPE_EXP);
			sp.setCPara1(Double.parseDouble(txt_p2_21.getText()));
			sp.setCPara2(Double.parseDouble(txt_p2_22.getText()));
		}
		
		String op2 = groupST.getSelection().getActionCommand();
		if (op2.equals("ST_UNIFORM")){
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_UNIFORM);
			sp.setSPara1(Double.parseDouble(txt_p3_11.getText()));
			sp.setSPara2(Double.parseDouble(txt_p3_12.getText()));
		} else if (op2.equals("ST_GAUSS")){
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_GAUSS);
			sp.setSPara1(Double.parseDouble(txt_p3_31.getText()));
			sp.setSPara2(Double.parseDouble(txt_p3_32.getText()));
		} else {
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_EXP);
			sp.setSPara1(Double.parseDouble(txt_p3_21.getText()));
			sp.setSPara2(Double.parseDouble(txt_p3_22.getText()));
		}
		
		String op3 = groupQD.getSelection().getActionCommand();
		if (op3.equals("QUEUE_LIFO")){
			sp.setQueue(Simulator.QD_LIFO);
		} else {
			sp.setQueue(Simulator.QD_FIFO);
		}
		
		if (stop1.isSelected()){
			if (stop2.isSelected())
				sp.setStop(Simulator.STOP_BOTH);
			else
				sp.setStop(Simulator.STOP_CASE_DRIVEN);
		} else if (stop2.isSelected()){
			sp.setStop(Simulator.STOP_TIME_DRIVEN);
		} else {
			sp.setStop(Simulator.STOP_NONE);
		}
		
		Simulator sim = new Simulator(graph, new ResourceUtilization(resAlloc), sp);
//		sim.start();
		sim.generateServerList();
		SimOutputDialog sod = new SimOutputDialog(null, true, sim); // <---------
//		sod.setAlwaysOnTop(true); // <---------
		sod.setVisible(true); // <---------
	}
	
	private void applyProperties(){
		timeUnit = Double.parseDouble(txtTimeUnit.getText());
		timeIntervall = (String)cboTimeUnit.getSelectedItem();
		
		int cvTime = cboTimeUnit.getSelectedIndex();
		
		createTimeModel(cvTime, timeUnit);
		
		period = Double.parseDouble(txtPeriod.getText());
		int cvPeriod = cboPeriodUnit.getSelectedIndex();
		period = tm.cv(cvPeriod, period);
		
		periodIntervall = (String)cboPeriodUnit.getSelectedItem();
		
		lambda = Double.parseDouble(txtCases.getText());
		epsilon = Double.parseDouble(txtPrecision.getText());
		
		register.setEnabledAt(register.indexOfTab("Capacity Plan"), true);
		register.setEnabledAt(register.indexOfTab("Simulation"), true);

//		getRegisterTab2();
//		getRegisterTab3();
	}
	
	private void createTimeModel(int unit, double val){
		Node[] nodes = graph.getNodeArray();
		
		/*switch (unit){
		case 0:
			unit = 1;
			break;
		case 1:
			unit = 0;
			break;
		default:
			
		}*/
		
		tm = new TimeModel(unit, val);
		
		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getType() == Node.TYPE_TRANS){
				nodes[i].setTime(tm.cv(nodes[i].getTimeUnit(), nodes[i].getTime()));
				nodes[i].setTimeUnit(tm.getStdUnit());
			}
		}
	}

}  //  @jve:decl-index=0:visual-constraint="4,4"
