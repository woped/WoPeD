package org.woped.quantana.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JFrame;

import org.woped.editor.utilities.Messages;
import org.woped.quantana.graph.Node;

public class GeneralPropertiesDialog extends JDialog {

	private static final long serialVersionUID	= 5L;
	
	public static final String TIME_SECOND	= Messages.getString("Transition.Properties.Seconds");
	public static final String TIME_MINUTE	= Messages.getString("Transition.Properties.Minutes");
	public static final String TIME_HOUR	= Messages.getString("Transition.Properties.Hours");
	public static final String TIME_DAY		= Messages.getString("Transition.Properties.Days");
	public static final String TIME_WEEK	= Messages.getString("Transition.Properties.Weeks");
	public static final String TIME_MONTH	= Messages.getString("Transition.Properties.Months");
	public static final String TIME_YEAR	= Messages.getString("Transition.Properties.Years");
	
//	public static final int CALLER_CAPA	= 6;
//	public static final int CALLER_SIM	= 7;

	private JPanel genPanel = new JPanel();
	private QuantitativeAnalysisDialog owner;
//	private int caller;

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
	private JLabel lblTimeModel = null;
	private JLabel lblHourToMin = null;
	private JLabel lblDayToHour = null;
	private JLabel lblMonToDay = null;
	private JLabel lblYearToMon = null;
	private JLabel lblWeekToDay = null;
	private JTextField txtHourToMin = null;
	private JTextField txtDayToHour = null;
	private JTextField txtMonToDay = null;
	private JTextField txtYearToMon = null;
	private JTextField txtWeekToDay = null;
	private JLabel lblMin = new JLabel(TIME_MINUTE);
	private JLabel lblHour = new JLabel(TIME_HOUR);
	private JLabel lblDay1 = new JLabel(TIME_DAY);
	private JLabel lblDay2 = new JLabel(TIME_DAY);
	private JLabel lblMon = new JLabel(TIME_MONTH);
	private JButton btnApply = null;
	
	private double timeUnit = 1.0;
	private String timeIntervall = TIME_MINUTE;
	private double period = 8.0;
	private String periodIntervall = TIME_HOUR;
	private double lambda = 50.0;
	private double epsilon = 0.001;

	private TimeModel tm;

	public GeneralPropertiesDialog(QuantitativeAnalysisDialog dlg, boolean modal) {
		super(new JFrame(), modal);
		
		owner = dlg;
		initialize();
	}

	private void initialize(){
		lblTimeUnit = new JLabel();
		lblTimeUnit.setText(Messages.getString("QuantAna.Config.TimeUnit"));
		lblTimeUnit.setAlignmentX(0);
		lblPeriod = new JLabel(Messages.getString("QuantAna.Config.ObservationPeriod"));
		lblPeriod.setAlignmentX(0);
		lblCases = new JLabel(Messages.getString("QuantAna.Config.ArrivalRate"));
		lblCases.setAlignmentX(0);
		txtTimeUnit = new JTextField();
		txtTimeUnit.setText(Double.valueOf(timeUnit).toString());
		txtPeriod = new JTextField();
		txtPeriod.setPreferredSize(new Dimension(100, 20));
		txtPeriod.setText(Double.valueOf(period).toString());
		txtCases = new JTextField();
		txtCases.setText(Double.valueOf(lambda).toString());
		txtPrecision = new JTextField();
		txtPrecision.setText(Double.valueOf(epsilon).toString());
		lblPrecision = new JLabel(Messages.getString("QuantAna.Config.TerminationThreshold"));

		lblTimeModel = new JLabel(Messages.getString("QuantAna.Config.TimeModel"));
		lblTimeModel.setAlignmentX(0);
		lblHourToMin = new JLabel(Messages.getString("QuantAna.Config.OneHourHas"));
		lblHourToMin.setAlignmentX(0);
		lblDayToHour = new JLabel(Messages.getString("QuantAna.Config.OneDayHas"));
		lblDayToHour.setAlignmentX(0);
		lblWeekToDay = new JLabel(Messages.getString("QuantAna.Config.OneWeekHas"));
		lblWeekToDay.setAlignmentX(0);
		lblMonToDay = new JLabel(Messages.getString("QuantAna.Config.OneMonthHas"));
		lblMonToDay.setAlignmentX(0);
		lblYearToMon = new JLabel(Messages.getString("QuantAna.Config.OneYearHas"));
		lblYearToMon.setAlignmentX(0);
		txtHourToMin = new JTextField("60");
		txtHourToMin.setPreferredSize(new Dimension(50, 20));
		txtDayToHour = new JTextField("24");
		txtDayToHour.setPreferredSize(new Dimension(50, 20));
		txtMonToDay = new JTextField("30");
		txtMonToDay.setPreferredSize(new Dimension(50, 20));
		txtYearToMon = new JTextField("12");
		txtYearToMon.setPreferredSize(new Dimension(50, 20));
		txtWeekToDay = new JTextField("7");
		txtWeekToDay.setPreferredSize(new Dimension(50, 20));
		lblMin.setAlignmentX(0);
		lblHour.setAlignmentX(0);
		lblDay1.setAlignmentX(0);
		lblDay2.setAlignmentX(0);
		lblMon.setAlignmentX(0);
		
		btnApply = new JButton(Messages.getString("QuantAna.Config.Apply"));
		btnApply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				applyProperties();
				
			}
		});
		
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	dispose();
            	owner.setVisible(false);
            	owner.dispose();    
            }
        });
		
		String[] units = {TIME_SECOND, TIME_MINUTE, TIME_HOUR, TIME_DAY, TIME_WEEK, TIME_MONTH, TIME_YEAR};
		cboTimeUnit = new JComboBox(units);
		cboTimeUnit.setSelectedItem(timeIntervall);
		cboTimeUnit.setMinimumSize(new Dimension(110, txtTimeUnit.getHeight()));
		cboPeriodUnit = new JComboBox(units);
		cboPeriodUnit.setMinimumSize(new Dimension(110, txtTimeUnit.getHeight()));
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
		genPanel.add(lblPeriod, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblTimeUnit, constraints);

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
		genPanel.add(txtPeriod, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtTimeUnit, constraints);

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
		genPanel.add(cboPeriodUnit, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(cboTimeUnit, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lbl, constraints);
		
		// <---
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblTimeModel, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblHourToMin, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtHourToMin, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 3;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblMin, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblDayToHour, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtDayToHour, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 3;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblHour, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblWeekToDay, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtWeekToDay, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 3;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblDay1, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblMonToDay, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtMonToDay, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 3;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblDay2, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblYearToMon, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(txtYearToMon, constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 3;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lblMon, constraints);
		// <---
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(new JLabel(), constraints);

		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 12;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		genPanel.add(btnApply, constraints);

		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 13;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		genPanel.add(lbl3, constraints);
		
		this.getContentPane().add(genPanel, BorderLayout.CENTER);
//		this.getRootPane().setDefaultButton(btnApply);
	}

	private void applyProperties(){
		timeUnit = Double.parseDouble(txtTimeUnit.getText());
		timeIntervall = (String)cboTimeUnit.getSelectedItem();

		int cvTime = cboTimeUnit.getSelectedIndex();

		createTimeModel(cvTime, timeUnit);

		period = Double.parseDouble(txtPeriod.getText());
		int cvPeriod = cboPeriodUnit.getSelectedIndex();
		period = tm.cv(cvPeriod, period);

		//periodIntervall = (String)cboPeriodUnit.getSelectedItem();

		lambda = Double.parseDouble(txtCases.getText());
		epsilon = Double.parseDouble(txtPrecision.getText());
		
		int stdTUnit = tm.getStdUnit();
		periodIntervall = convTIndexToString(stdTUnit);
		
		// update Dialog-Values
		owner.setLambda(lambda);
		owner.setEpsilon(epsilon);
		owner.setTimeIntervall(timeIntervall);
		owner.setTimeUnit(timeUnit);
		owner.setPeriod(period);
		owner.setPeriodIntervall(periodIntervall);
		
		owner.updateParams(Double.toString(2/lambda), Double.toString(1/lambda));
		
		// close operations
		JTabbedPane register = owner.getTabbedPane();
		register.setEnabledAt(register.indexOfTab(Messages.getTitle("QuantAna.CapacityPlanning")), true);
		register.setEnabledAt(register.indexOfTab(Messages.getTitle("QuantAna.Simulation")), true);
		this.dispose();
		owner.setVisible(true);
	}

	private void createTimeModel(int unit, double val){
		Node[] nodes = owner.getGraph().getNodeArray();

		tm = new TimeModel(unit, val);
		tm.setCvDayToHour(Double.parseDouble(txtDayToHour.getText()));
		tm.setCvHourToMin(Double.parseDouble(txtHourToMin.getText()));
		tm.setCvMonthToDay(Double.parseDouble(txtMonToDay.getText()));
		tm.setCvWeekToDay(Double.parseDouble(txtWeekToDay.getText()));
		tm.setCvYearToMonth(Double.parseDouble(txtYearToMon.getText()));

		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].getType() == Node.TYPE_TRANS){
				nodes[i].setTime(tm.cv(nodes[i].getTimeUnit(), nodes[i].getTime()));
				nodes[i].setTimeUnit(tm.getStdUnit());
			}
		}
	}
	
	private String convTIndexToString(int u){
		switch (u){
		case 0:
			return TIME_SECOND;
		case 1:
			return TIME_MINUTE;
		case 2:
			return TIME_HOUR;
		case 3:
			return TIME_DAY;
		case 4:
			return TIME_WEEK;
		case 5:
			return TIME_MONTH;
		case 6:
			return TIME_YEAR;
		default:
			return "ERROR";
		}
	}
}
