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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

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
import org.woped.resourcealloc.ResourceClassTaskAllocation;
import org.woped.resourcealloc.ResourceClassTaskAllocationTable;
import org.woped.resourcealloc.ResourceUtilization;
import org.woped.simulation.ProbabilityDistribution;
import org.woped.simulation.Simulator;
import org.woped.simulation.output.SimOutputDialog;

public class QuantitativeAnalysisDialog extends JDialog {

	private static final long serialVersionUID	= 1L;
	
//	public static final int CALL_CAPA	= 1;
//	public static final int CALL_SIM	= 2;
	
	private EditorVC editor = null;
	private JPanel jContentPane = null;
	private JTabbedPane register = null;
	private JPanel capaPanel = new JPanel();
	private JPanel simuPanel = new JPanel();
	
	private double timeUnit = 1.0;
	private String timeIntervall = GeneralPropertiesDialog.TIME_MINUTE;
	private double period = 8.0;
	private String periodIntervall = GeneralPropertiesDialog.TIME_HOUR;
	private double lambda = 50.0;
	private double epsilon = 0.001;
	private double capaLevel = 0.8;
	
	StructuralAnalysis sa;
	ModelElementContainer mec;
	WorkflowNetGraph graph;
	ResourceAllocation resAlloc;
	GeneralPropertiesDialog props = null;
	boolean usedAlready = false;
	int numResCls;
	int numTrans = 0;
	HashMap<Key, Node> unfoldedNet = new HashMap<Key, Node>();
	
	// Capacity Plan Tab
	String[] colNames = {"Task", "Runs per Case", "Cases per Period", "Servicetime per Task", "Servicetime per Case", "Servicetime per Period"};
	String[] colNames2 = {"Resource class", "Average number of minutes", "Number of resources"};
	String[] colToolTips1 = {
			"Task: Name and ID of all tasks of the process",
			"Runs per Case: The number of runs for a single case through the regarding task",
			"Cases per Period: The number of runs through a task in a whole period",
			"Servicetime per Task: The average service time a task needs to get the work done",
			"Servicetime per Case: The average service time the task needs for a single case (including loops)",
			"Servicetime per Period: The average service time of the task in the whole period"
	};
	String[] colToolTips2 = {
			"Resource class: The name of the resource class",
			"Average number of minutes: The sum of the average service times of all tasks per period using this resource class",
			"Number of resources: The number of resource objects needed to perform all tasks per resource class"
	};
	Object[][] tableTasksMatrix;
	Object[][] tableResMatrix;
	double[][] tasksMatrix;
	double[][] resMatrix;
	TasksTableModel tmTasks;
	ResTableModel tmRes;
	JTable tableTasks;
	JTable tableRes;
	JScrollPane tableTasksPane;
	JScrollPane tableResPane;
	JLabel lblDummy;
	JLabel lblDummy2;
	JLabel lblHeading1;
	JLabel lblHeading2;
	JLabel lblUnfolding;
	JLabel lblDeviation;
	JPanel messagePanel;
	JPanel panelHeading1;
	JPanel panelHeading2;
	JLabel lblPrecision;
//	JSpinner jspPrecision;
	JSlider jslPrecision;
	
	JLabel lblCapaLevel;
	JLabel lblLevelDisplay;
	JSlider jslCapaLevel;
	
	// Simulation Tab
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
	JLabel lblDummy3;
	JLabel lblDummy4;

	/**
	 * This is the default constructor
	 */
	public QuantitativeAnalysisDialog(Frame owner, boolean modal, EditorVC editor) {
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
		
		if (!usedAlready){
			register.setEnabledAt(register.indexOfTab("Capacity Plan"), false);
			register.setEnabledAt(register.indexOfTab("Simulation"), false);
			getConfiguration();
		}
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
//		getRegisterTab1();
		getRegisterTab2();
		getRegisterTab3();
		
		if (register == null) {
			register = new JTabbedPane();
			
//			register.addTab("General Information", genPanel);
			register.addTab("Capacity Plan", capaPanel);
			register.addTab("Simulation", simuPanel);
		}
		return register;
	}
	
	/*private void getRegisterTab1()
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
	}*/
	
	private void getRegisterTab2()
	{
		calculateNumOfRuns(graph);
		initResourceAlloc();
		numResCls = resAlloc.getNumOfResClasses();
		
		Font fnt = new Font("Arial Bold", Font.PLAIN, 12);
		
//		getTables();
		
		lblDummy = new JLabel();
		lblDummy2 = new JLabel();
		lblHeading1 = new JLabel("The capacity required per Task: ", JLabel.LEFT);
		lblHeading1.setFont(fnt);
		lblHeading1.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lblHeading1.setPreferredSize(new Dimension(400,20));
		lblPrecision = new JLabel("precision: ");
		lblPrecision.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		lblPrecision.setPreferredSize(new Dimension(90,20));
		/*jspPrecision = new JSpinner(new SpinnerNumberModel(2, 0, 5, 1));
		jspPrecision.setPreferredSize(new Dimension(50,20));
		jspPrecision.setToolTipText("Sets the number of decimal places");
		jspPrecision.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				String prec = ((Integer)jspPrecision.getValue()).toString();
				showPrecision(prec);
//				int i = ((Integer)(jspPrecision.getValue())).intValue();
//				JOptionPane.showMessageDialog(null, Integer.valueOf(i));
			}
		});*/
		jslPrecision = new JSlider(0, 5, 2);
		jslPrecision.setPaintLabels(true);
		jslPrecision.setPaintTicks(true);
		jslPrecision.setMajorTickSpacing(1);
		jslPrecision.setToolTipText("Sets the number of decimal places");
		jslPrecision.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mouseReleased(MouseEvent e){
				String p = Integer.valueOf(jslPrecision.getValue()).toString();
				showPrecision(p);
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		JLabel lblPrecDisplay = new JLabel();
		lblPrecDisplay.setPreferredSize(new Dimension(60, 20));
		panelHeading1 = new JPanel();
		panelHeading1.add(lblHeading1);
		panelHeading1.add(lblPrecision);
		panelHeading1.add(jslPrecision);
		panelHeading1.add(lblPrecDisplay);
		
		lblHeading2 = new JLabel("The capacity requirement per resource class: ", JLabel.LEFT);
		lblHeading2.setFont(fnt);
		lblHeading2.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lblHeading2.setPreferredSize(new Dimension(400,20));
		lblCapaLevel = new JLabel("capacity level: ");
		lblCapaLevel.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		lblCapaLevel.setPreferredSize(new Dimension(90,20));
		jslCapaLevel = new JSlider(10, 100, 80);
		jslCapaLevel.setPaintTicks(true);
		jslCapaLevel.setPaintLabels(true);
		jslCapaLevel.setMajorTickSpacing(10);
		jslCapaLevel.setMinorTickSpacing(5);
		jslCapaLevel.setToolTipText("Choose the percentage of average resource utilization");
//		jslCapaLevel.setPreferredSize(new Dimension(180, (int)jslCapaLevel.getSize().getHeight()));
		jslCapaLevel.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int val = ((JSlider)e.getSource()).getValue();
				lblLevelDisplay.setText(Integer.toString(val));
			}
		});
		jslCapaLevel.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mouseReleased(MouseEvent e){
				updateResAlloc();
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		capaLevel = jslCapaLevel.getValue() / 100.0;
		lblLevelDisplay = new JLabel(Integer.toString(jslCapaLevel.getValue()), JLabel.CENTER);
		lblLevelDisplay.setPreferredSize(new Dimension(60,20));
		lblLevelDisplay.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		lblLevelDisplay.setFont(fnt);
		lblLevelDisplay.setBorder(BorderFactory.createEtchedBorder());
		panelHeading2 = new JPanel();
		panelHeading2.add(lblHeading2);
		panelHeading2.add(lblCapaLevel);
		panelHeading2.add(jslCapaLevel);
		panelHeading2.add(lblLevelDisplay);
		
		lblUnfolding = new JLabel("");
		lblDeviation = new JLabel("");
		lblUnfolding.setFont(fnt);
		lblDeviation.setFont(fnt);
		messagePanel = new JPanel();
		messagePanel.add(lblUnfolding);
		messagePanel.add(lblDeviation);
		
		getTables();
		
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
		capaPanel.add(panelHeading1, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(tableTasksPane, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(panelHeading2, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(tableResPane, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(messagePanel, constraints);
		
		JButton btnConf = new JButton("Configure");
		btnConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
//				callee = CALL_CAPA;
				getConfiguration();
			}
		});
		JButton btnCalc = new JButton("Calculate");
		btnCalc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				calculateTables();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnCalc);
		buttonPanel.add(btnConf);
		getRootPane().setDefaultButton(btnCalc);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		capaPanel.add(buttonPanel, constraints);
		
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
//		simuPanel.setLayout(new GridLayout(6,1));
		
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
		txt_p2_11.setEnabled(false);
		txt_p2_12 = new JTextField(Double.toString(2/lambda));
		txt_p2_12.setPreferredSize(new Dimension(100, 20));
		txt_p2_12.setEnabled(false);
		txt_p2_21 = new JTextField(Double.toString(1/lambda));
		txt_p2_21.setPreferredSize(new Dimension(100, 20));
		txt_p2_21.setEnabled(true);
		txt_p2_22 = new JTextField("0");
		txt_p2_22.setEnabled(false);
		txt_p2_22.setPreferredSize(new Dimension(100, 20));
		txt_p2_31 = new JTextField("0");
		txt_p2_31.setPreferredSize(new Dimension(100, 20));
		txt_p2_31.setEnabled(false);
		txt_p2_32 = new JTextField("1");
		txt_p2_32.setPreferredSize(new Dimension(100, 20));
		txt_p2_32.setEnabled(false);
		
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
		
		opt_p2_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				txt_p2_11.setEnabled(true);
				txt_p2_12.setEnabled(true);
				txt_p2_21.setEnabled(false);
				txt_p2_31.setEnabled(false);
				txt_p2_32.setEnabled(false);
			}
		});
		
		opt_p2_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				txt_p2_11.setEnabled(false);
				txt_p2_12.setEnabled(false);
				txt_p2_21.setEnabled(true);
				txt_p2_31.setEnabled(false);
				txt_p2_32.setEnabled(false);
			}
		});
		
		opt_p2_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				txt_p2_11.setEnabled(false);
				txt_p2_12.setEnabled(false);
				txt_p2_21.setEnabled(false);
				txt_p2_31.setEnabled(true);
				txt_p2_32.setEnabled(true);
			}
		});
		
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
		p3.setLayout(new GridLayout(3,1));
		JRadioButton opt_p3_1 = new JRadioButton("Uniform distribution", false);
		JRadioButton opt_p3_2 = new JRadioButton("Exponential distribution", true);
		JRadioButton opt_p3_3 = new JRadioButton("Gaussian distribution", false);
		opt_p3_1.setActionCommand("ST_UNIFORM");
		opt_p3_2.setActionCommand("ST_EXP");
		opt_p3_3.setActionCommand("ST_GAUSS");
		groupST.add(opt_p3_1);
		groupST.add(opt_p3_2);
		groupST.add(opt_p3_3);
		
		p3.add(opt_p3_1);
		p3.add(opt_p3_2);
		p3.add(opt_p3_3);
		
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
		getRootPane().setDefaultButton(btnStart);
		
		JButton btnConf = new JButton("Configure");
		btnConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
//				callee = CALL_SIM;
				getConfiguration();
			}
		});
		buttonPanel.add(btnConf);
		
		/*// All together
		simuPanel.add(p1);
		simuPanel.add(p2);
		simuPanel.add(p3);
		simuPanel.add(p4);
		simuPanel.add(p5);
		simuPanel.add(buttonPanel);
		simuPanel.add(btnConf);*/
		lblDummy3 = new JLabel();
		lblDummy4 = new JLabel();
		
		GridBagLayout genLayout = new GridBagLayout();
		simuPanel.setLayout(genLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(lblDummy3, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(p1, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(p2, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(p3, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(p4, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(p5, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(buttonPanel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		simuPanel.add(lblDummy4, constraints);
	}
	
	private void calculateNumOfRuns(WorkflowNetGraph g){
		unfoldNet(graph, lambda, epsilon);
		
		Node[] origNet = graph.getNodeArray();
		for (Key k : unfoldedNet.keySet()){
			String id = k.getId();
			Node n = origNet[graph.getNodeIdx(id)];
			n.setNumOfRuns(n.getNumOfRuns() + k.getRuns());
			n.setIteration(n.getIteration() + 1);
		}
	}
	
	private void unfoldNet(WorkflowNetGraph g, double l, double e){
		unfoldedNet.clear();
		for (Node n : g.getNodeArray()){
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
	
	/*private JTable getResClassTable(){
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
	}*/
	
	private void getTables(){
		tasksMatrix = new double[numTrans + 1][colNames.length - 1];
		resMatrix = new double[numResCls][colNames2.length - 1];
		
		tableTasksMatrix = new Object[numTrans+1][colNames.length];
		tableResMatrix = new Object[numResCls][colNames2.length];
		
		String[] trans = graph.getTransitions();
		double[] times = graph.getTimes();
		double[] runs = graph.getRuns();
		int j = trans.length;
		String prec = Integer.valueOf(jslPrecision.getValue()).toString();
		for (int i = 0; i < j; i++){
			tasksMatrix[i][2] = runs[i];
			tasksMatrix[i][3] = times[i];
			
			tableTasksMatrix[i][0] = trans[i];
			tableTasksMatrix[i][2] = String.format("%12." + prec + "f", runs[i]);
			tableTasksMatrix[i][3] = String.format("%12." + prec + "f", times[i]);
		}
		
		tasksMatrix[j][2] = 0.0;
		tasksMatrix[j][3] = 0.0;
		
		tableTasksMatrix[j][0] = "Sum";
		
		ArrayList<ResourceClassTaskAllocation> resTab = resAlloc.getResClsTskAlloc().getTable();
		for (int i = 0; i < resTab.size(); i++){
			tableResMatrix[i][0] = resTab.get(i).getResClass();
		}
		
		Font fnt = new Font("Arial Bold", Font.PLAIN, 12);
		
		tmTasks = new TasksTableModel(colNames, tableTasksMatrix);
		tmTasks.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){
				//JOptionPane.showMessageDialog(null, e.toString());
			}
		});
		tableTasks = new JTable(tmTasks){
			private static final long serialVersionUID = 11L;
			
			// Implement table header tool tips. 
			protected JTableHeader createDefaultTableHeader() {
				return new JTableHeader(columnModel) {
					private static final long serialVersionUID = 12L;
					
					public String getToolTipText(MouseEvent e) {
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index).getModelIndex();
						return colToolTips1[realIndex];
					}
				};
			}
		};
		tableTasks.setEnabled(false);
		tableTasks.setBackground(capaPanel.getBackground());
		tableTasks.getTableHeader().setFont(fnt);
		
		tmRes = new ResTableModel(colNames2, tableResMatrix);
		tmRes.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){
				//JOptionPane.showMessageDialog(null, e.toString());
			}
		});
		tableRes = new JTable(tmRes){
			private static final long serialVersionUID = 21L;
			
			// Implement table header tool tips. 
			protected JTableHeader createDefaultTableHeader() {
				return new JTableHeader(columnModel) {
					private static final long serialVersionUID = 22L;
					
					public String getToolTipText(MouseEvent e) {
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index).getModelIndex();
						return colToolTips2[realIndex];
					}
				};
			}
		};
		tableRes.setEnabled(false);
		tableRes.setBackground(capaPanel.getBackground());
		tableRes.getTableHeader().setFont(fnt);
		
		tableTasksPane = new JScrollPane(tableTasks);
		tableTasksPane.setMinimumSize(new Dimension(780, 200));
		tableTasksPane.setBorder(BorderFactory.createEmptyBorder());
		tableTasksPane.setWheelScrollingEnabled(true);
		tableResPane = new JScrollPane(tableRes);
		tableResPane.setMinimumSize(new Dimension(780, 100));
		tableResPane.setBorder(BorderFactory.createEmptyBorder());
		tableResPane.setWheelScrollingEnabled(true);
	}
	
	private void startSimulation(){
		SimParameters sp = new SimParameters();
		
		sp.setLambda(lambda);
		sp.setTimeOfPeriod(period);
		
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
//			sp.setSPara1(Double.parseDouble(txt_p3_11.getText()));
//			sp.setSPara2(Double.parseDouble(txt_p3_12.getText()));
		} else if (op2.equals("ST_GAUSS")){
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_GAUSS);
//			sp.setSPara1(Double.parseDouble(txt_p3_31.getText()));
//			sp.setSPara2(Double.parseDouble(txt_p3_32.getText()));
		} else {
			sp.setDistServ(ProbabilityDistribution.DIST_TYPE_EXP);
//			sp.setSPara1(Double.parseDouble(txt_p3_21.getText()));
//			sp.setSPara2(Double.parseDouble(txt_p3_22.getText()));
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
	
	private void getConfiguration(){
		if (props == null){
			props = new GeneralPropertiesDialog(this, true);
			props.setTitle("General Properties");
			props.setBounds(300, 200, 600, 400);
		}
		
		props.setVisible(true);
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
	
	public JTabbedPane getTabbedPane(){
		return register;
	}

	public boolean isUsedAlready() {
		return usedAlready;
	}

	public void setUsedAlready(boolean usedAlready) {
		this.usedAlready = usedAlready;
	}
	
	private void calculateTables(){
		Object[][] tb = tableTasksMatrix;
		Object[][] tr = tableResMatrix;
		String prec = Integer.valueOf(jslPrecision.getValue()).toString();
		double sumCase = 0.0;
		double sumPeriod = 0.0;
		
		calculateNumOfRuns(graph);
		double[] runs = graph.getRuns();
		double[] times = graph.getTimes();
		
		for (int r = 0; r < numTrans; r++){
			tasksMatrix[r][1] = runs[r];
			tasksMatrix[r][2] = times[r];
			
			tb[r][2] = String.format("%12." + prec + "f", runs[r]);//Double.valueOf(runs[r]);
			tmTasks.fireTableCellUpdated(r, 2);
			tb[r][3] = String.format("%12." + prec + "f", times[r]);//Double.valueOf(times[r]);
			tmTasks.fireTableCellUpdated(r, 3);
			
			double n = tasksMatrix[r][1];//Double.valueOf((String)tb[r][2]);
			double n1 = n / lambda;
			double t = tasksMatrix[r][2];//Double.valueOf((String)tb[r][3]);
			
			sumCase += n1 * t;
			sumPeriod += n * t;
			
			tasksMatrix[r][0] = n1;
			tasksMatrix[r][3] = n1 * t;
			tasksMatrix[r][4] = n * t;
			
			tb[r][1] = String.format("%12." + prec + "f", n1);//Double.valueOf(n1);
			tmTasks.fireTableCellUpdated(r, 1);
			tb[r][4] = String.format("%12." + prec + "f", n1 * t);//Double.valueOf(n1 * t);
			tmTasks.fireTableCellUpdated(r, 4);
			tb[r][5] = String.format("%12." + prec + "f", n * t);//Double.valueOf(n * t);
			tmTasks.fireTableCellUpdated(r, 5);
		}
		
		tasksMatrix[numTrans][0] = 0.0;
		tasksMatrix[numTrans][3] = sumCase;
		tasksMatrix[numTrans][4] = sumPeriod;
		
		tb[numTrans][4] = String.format("%12." + prec + "f", sumCase);//Double.valueOf(sumCase);
		tmTasks.fireTableCellUpdated(numTrans, 4);
		tb[numTrans][5] = String.format("%12." + prec + "f", sumPeriod);//Double.valueOf(sumPeriod);
		tmTasks.fireTableCellUpdated(numTrans, 5);
		
		ResourceClassTaskAllocationTable rcta = resAlloc.getResClsTskAlloc();
		capaLevel = jslCapaLevel.getValue() / 100.0;

		for (int i = 0; i < numResCls; i++){
			double sum = 0.0;
			ArrayList<String> min = rcta.getTable().get(i).getTasks();
			for (int j = 0; j < min.size(); j++){
				int idx = -1;
				for (int k = 0; k < numTrans; k++){
					if (((String)tb[k][0]).equals(min.get(j))) idx = k;
				}

				if (idx != -1) sum += tasksMatrix[idx][4];//Double.valueOf((String)tb[idx][5]);
			}
			
			double numRes = (sum / period) / capaLevel;
			resMatrix[i][0] = sum;
			resMatrix[i][1] = numRes;

			tr[i][1] = String.format("%12." + prec + "f", sum);//Double.valueOf(sum);
			tmRes.fireTableCellUpdated(i, 1);
			tr[i][2] = String.format("%12." + prec + "f", numRes);//Double.valueOf((sum / period) / capaLevel);
			tmRes.fireTableCellUpdated(i, 2);
		}
		
		lblUnfolding.setText("The unfolded net comprises " + unfoldedNet.size() + " nodes.");
		String dev = String.format("%6.1f", (1 - graph.getSinkPlace().getNumOfRuns() / lambda) * 100);
		lblDeviation.setText("The relative deviation is (estimated): " + dev + "%");
	}
	
	private void showPrecision(String precision){
//		double p = Math.pow(10, precision);
		
		int numCols1 = colNames.length;
		
		for (int i = 0; i < numTrans; i++){
			for (int j = 0; j < numCols1 - 1; j++){
				tmTasks.setValueAt(String.format("%12." + precision + "f", tasksMatrix[i][j]), i, j + 1);
			}
		}
		
		tmTasks.setValueAt(String.format("%12." + precision + "f", tasksMatrix[numTrans][numCols1 - 3]), numTrans, numCols1 - 2);
		tmTasks.setValueAt(String.format("%12." + precision + "f", tasksMatrix[numTrans][numCols1 - 2]), numTrans, numCols1 - 1);
		
		for (int i = 0; i < numResCls; i++){
			tmRes.setValueAt(String.format("%12." + precision + "f", resMatrix[i][0]), i, 1);
			tmRes.setValueAt(String.format("%12." + precision + "f", resMatrix[i][1]), i, 2);
		}
	}
	
	private void updateResAlloc(){
		ResourceClassTaskAllocationTable rcta = resAlloc.getResClsTskAlloc();
		Object[][] tb = tableTasksMatrix;
		Object[][] tr = tableResMatrix;
		capaLevel = jslCapaLevel.getValue() / 100.0;
		String prec = Integer.valueOf(jslPrecision.getValue()).toString();
		
		/*for (int i = 0; i < numResCls; i++){
			tmRes.setValueAt(String.format("%12." + prec + "f", (resMatrix[i][1] * oldCapaLevel) / capaLevel), i, 2);
		}*/
		
		for (int i = 0; i < numResCls; i++){
			double sum = 0.0;
			ArrayList<String> min = rcta.getTable().get(i).getTasks();
			for (int j = 0; j < min.size(); j++){
				int idx = -1;
				for (int k = 0; k < numTrans; k++){
					if (((String)tb[k][0]).equals(min.get(j))) idx = k;
				}

				if (idx != -1) sum += tasksMatrix[idx][4];//Double.valueOf((String)tb[idx][5]);
			}
			
			double numRes = (sum / period) / capaLevel;
			resMatrix[i][0] = sum;
			resMatrix[i][1] = numRes;

			tr[i][1] = String.format("%12." + prec + "f", sum);//Double.valueOf(sum);
			tmRes.fireTableCellUpdated(i, 1);
			tr[i][2] = String.format("%12." + prec + "f", numRes);//Double.valueOf((sum / period) / capaLevel);
			tmRes.fireTableCellUpdated(i, 2);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="4,4"
