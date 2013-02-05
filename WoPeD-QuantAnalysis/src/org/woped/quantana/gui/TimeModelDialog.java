package org.woped.quantana.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;

import org.woped.gui.translations.Messages;
import org.woped.quantana.graph.Node;
import org.woped.quantana.model.TimeModel;
import org.woped.quantana.Constants;

public class TimeModelDialog extends JDialog {

	private static final long serialVersionUID	= 5L;
	
	public static final String TIME_SECOND	= Messages.getString("Transition.Properties.Seconds");
	public static final String TIME_MINUTE	= Messages.getString("Transition.Properties.Minutes");
	public static final String TIME_HOUR	= Messages.getString("Transition.Properties.Hours");
	public static final String TIME_DAY		= Messages.getString("Transition.Properties.Days");
	public static final String TIME_WEEK	= Messages.getString("Transition.Properties.Weeks");
	public static final String TIME_MONTH	= Messages.getString("Transition.Properties.Months");
	public static final String TIME_YEAR	= Messages.getString("Transition.Properties.Years");
	
	private JPanel timeModelPanel = null;
	private JLabel lblTimeUnit = null;
	private JTextField txtTimeUnit = null;
	private JComboBox cboTimeUnit = null;
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

	private JButton 	btnApply = null;
	private JDialog 	dlg;
	private TimeModel 	tm;

	public TimeModelDialog(JDialog dlg, TimeModel tm) {
		super(new JFrame(), true);	
		this.dlg = dlg; 
		this.tm = tm;
		initialize();
	}

	private void initialize(){
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
		txtHourToMin.setMinimumSize(new Dimension(80, 20));
		txtHourToMin.setMaximumSize(new Dimension(80, 20));
		txtHourToMin.setPreferredSize(new Dimension(80, 20));
		txtDayToHour = new JTextField("24");
		txtDayToHour.setMinimumSize(new Dimension(80, 20));
		txtDayToHour.setMaximumSize(new Dimension(80, 20));
		txtDayToHour.setPreferredSize(new Dimension(80, 20));
		txtMonToDay = new JTextField("30");
		txtMonToDay.setMinimumSize(new Dimension(80, 20));
		txtMonToDay.setMaximumSize(new Dimension(80, 20));
		txtMonToDay.setPreferredSize(new Dimension(80, 20));
		txtYearToMon = new JTextField("12");
		txtYearToMon.setMinimumSize(new Dimension(80, 20));
		txtYearToMon.setMaximumSize(new Dimension(80, 20));
		txtYearToMon.setPreferredSize(new Dimension(80, 20));
		txtWeekToDay = new JTextField("7");
		txtWeekToDay.setMinimumSize(new Dimension(80, 20));
		txtWeekToDay.setMaximumSize(new Dimension(80, 20));
		txtWeekToDay.setPreferredSize(new Dimension(80, 20));
		lblMin.setAlignmentX(0);
		lblHour.setAlignmentX(0);
		lblDay1.setAlignmentX(0);
		lblDay2.setAlignmentX(0);
		lblMon.setAlignmentX(0);
		
		btnApply = new JButton();
		btnApply.setText(Messages.getTitle("QuantAna.Button.Apply"));
		btnApply.setIcon(Messages.getImageIcon("QuantAna.Button.Apply"));
		btnApply.setMinimumSize(new Dimension(80, 25));
		btnApply.setMaximumSize(new Dimension(80, 25));
		btnApply.setPreferredSize(new Dimension(80, 25));
		btnApply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				applyProperties();				
			}
		});
		
		lblTimeUnit = new JLabel();
		lblTimeUnit.setText(Messages.getString("QuantAna.Config.TimeUnit"));
		lblTimeUnit.setAlignmentX(0);
		lblTimeUnit.setMinimumSize(new Dimension(80, 20));
		lblTimeUnit.setMaximumSize(new Dimension(80, 20));
		lblTimeUnit.setPreferredSize(new Dimension(80, 20));
		txtTimeUnit = new JTextField();
		txtTimeUnit.setText(tm.getStdUnitMultiple()+"");
		cboTimeUnit = new JComboBox(Constants.TIMEUNITS);
		cboTimeUnit.setSelectedItem(Constants.TIMEUNITS[tm.getStdUnit()]);
		cboTimeUnit.setMinimumSize(new Dimension(110, txtTimeUnit.getHeight()));

		timeModelPanel = new JPanel();
		timeModelPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Messages
						.getString("QuantAna.TimeModel.Settings")),
				BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		timeModelPanel.setMinimumSize(new Dimension(250, 250));

		timeModelPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5,5,5,5);
		constraints.fill = GridBagConstraints.BOTH;

		constraints.gridx = 0;
		constraints.gridy = 0;
		timeModelPanel.add(lblTimeUnit, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		timeModelPanel.add(txtTimeUnit, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		timeModelPanel.add(cboTimeUnit, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		timeModelPanel.add(lblHourToMin, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		timeModelPanel.add(txtHourToMin, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		timeModelPanel.add(lblMin, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		timeModelPanel.add(lblDayToHour, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		timeModelPanel.add(txtDayToHour, constraints);

		constraints.gridx = 2;
		constraints.gridy = 2;
		timeModelPanel.add(lblHour, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		timeModelPanel.add(lblWeekToDay, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		timeModelPanel.add(txtWeekToDay, constraints);

		constraints.gridx = 2;
		constraints.gridy = 3;
		timeModelPanel.add(lblDay1, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		timeModelPanel.add(lblMonToDay, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		timeModelPanel.add(txtMonToDay, constraints);

		constraints.gridx = 2;
		constraints.gridy = 4;
		timeModelPanel.add(lblDay2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		timeModelPanel.add(lblYearToMon, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		timeModelPanel.add(txtYearToMon, constraints);

		constraints.gridx = 2;
		constraints.gridy = 5;
		timeModelPanel.add(lblMon, constraints);
		
		this.getContentPane().setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		this.getContentPane().add(timeModelPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(5,90,5,90);

		this.getContentPane().add(btnApply, constraints);
		this.getRootPane().setDefaultButton(btnApply);
		
		setTitle(Messages.getTitle("QuantAna.TimeModel"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 350;
		int x = screenSize.width > width ? (screenSize.width - width)/2 : 0;
		int height = 320;
		int y = screenSize.height > height  ? (screenSize.height - height)/2 : 0;
		setBounds(x, y, width, height);

		this.setVisible(true);
	}

	private void applyProperties(){
		createTimeModel();
		if (dlg instanceof CapacityAnalysisDialog)
			((CapacityAnalysisDialog)dlg).updContents();
		else
			((QuantitativeSimulationDialog)dlg).updTimeModel();
		this.dispose();
	}

	private void createTimeModel(){
		Node[] nodes = new Node[0];

		if (dlg instanceof CapacityAnalysisDialog)
			nodes = ((CapacityAnalysisDialog)dlg).getGraph().getNodeArray();
		//else
			// nodes = ((QuantitativeSimulationDialog)dlg).getGraph().getNodeArray();

		tm.setCvDayToHour(Double.parseDouble(txtDayToHour.getText()));
		tm.setCvHourToMin(Double.parseDouble(txtHourToMin.getText()));
		tm.setCvMonthToDay(Double.parseDouble(txtMonToDay.getText()));
		tm.setCvWeekToDay(Double.parseDouble(txtWeekToDay.getText()));
		tm.setCvYearToMonth(Double.parseDouble(txtYearToMon.getText()));
		tm.setStdUnit(cboTimeUnit.getSelectedIndex());
		tm.setStdUnitMultiple(Double.parseDouble(txtTimeUnit.getText()));

		for (int i = 0; i < nodes.length; i++){
			if (nodes[i].isTransition() || nodes[i].getType() == Node.TYPE_SUBP){
				nodes[i].setTime(tm.cv(nodes[i].getTimeUnit(), nodes[i].getTime()));
				nodes[i].setTimeUnit(tm.getStdUnit());
			}
		}
	}
}
