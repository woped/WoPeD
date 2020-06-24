package org.woped.editor.controller.bpel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.IntegerFilter;
import org.woped.core.utilities.JTextFieldEvalution;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.gui.PopUpDialog;
import org.woped.gui.translations.Messages;

import com.toedter.calendar.JCalendar;

/**
 * @author Kristian Kindler / Esther Landes / Frank Sch�ler
 * 
 * This is a panel in the transition properties, which enables the user to
 * maintain data for a "wait" BPEL activity.
 * 
 * Created on 16.12.2007
 */

public class BPELwaitPanel extends BPELadditionalPanel implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String PANELNAME = "wait";

	private ButtonGroup waitButtonGroup = null;

	private JPanel waitDurationEntry = null;

	private JRadioButton waitDurationRadioButton = null;

	private JRadioButton waitDeadlineRadioButton = null;

	private JPanel radioButtonPanel;

	private JPanel radioButtonSubPanel;

	private JPanel calendarPanel;

	private JPanel deadlinePanel;

	private JPanel deadlineTimePanel;

	private JPanel deadlineTimeSubPanel;

	private JCalendar calendar;

	private JPanel durationPanel;

	private JPanel durationSubPanel;

	private JFormattedTextField deadLineTextFieldHour;

	private JTextFieldEvalution deadLineHourFilter;

	private JFormattedTextField deadLineTextFieldMinute;

	private JTextFieldEvalution deadLineMinuteFilter;

	private JFormattedTextField deadLineTextFieldSecond;

	private JTextFieldEvalution deadLineSecondFilter;

	private JTextField durationTextFieldYear;

	private JTextField durationTextFieldMonth;

	private JTextField durationTextFieldDay;

	private JTextField durationTextFieldHour;

	private JTextField durationTextFieldMinute;

	private JTextField durationTextFieldSecond;

	private static final String WAIT_DURATION = Messages
			.getString("Transition.Properties.BPEL.Wait.Duration");

	private static final String WAIT_DEADLINE = Messages
			.getString("Transition.Properties.BPEL.Wait.Deadline");

	private GridBagConstraints c1;

	public BPELwaitPanel(TransitionPropertyEditor t_editor,
			TransitionModel transition) {

		super(t_editor, transition);

		setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();

		waitButtonGroup = new ButtonGroup();
		waitButtonGroup.add(getWaitDurationRadioButton());
		waitButtonGroup.add(getWaitDeadlineRadioButton());

		c1.weightx = 1;
		c1.weighty = 1;
		c1.anchor = GridBagConstraints.WEST;
		c1.fill = GridBagConstraints.HORIZONTAL;

		c1.gridx = 0;
		c1.gridy = 0;
		add(getRadioButtonPanel(), c1);

		c1.gridx = 0;
		c1.gridy = 1;
		c1.insets = new Insets(5, 0, 20, 0);
		add(new JSeparator(), c1);

		c1.gridx = 0;
		c1.gridy = 2;
		c1.insets = new Insets(0, 0, 10, 0);

		add(getDurationPanel(), c1);

	}

	private JPanel getDurationPanel() {
		if (durationPanel == null) {
			durationPanel = new JPanel();
			durationPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0, 10, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			durationPanel.add(getDurationSubPanel(), c);
		}

		return durationPanel;
	}

	private JPanel getDurationSubPanel() {
		if (durationSubPanel == null) {
			durationSubPanel = new JPanel();
			durationSubPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;

			c.insets = new Insets(0, 0, 0, 20);
			c.gridx = 0;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Years")), c);
			c.gridx = 1;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Months")), c);
			c.gridx = 2;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Days")), c);

			c.insets = new Insets(0, 0, 5, 20);
			c.gridx = 0;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldYear(), c);
			c.gridx = 1;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldMonth(), c);
			c.gridx = 2;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldDay(), c);

			c.insets = new Insets(0, 0, 0, 20);
			c.gridx = 0;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Hours")), c);
			c.gridx = 1;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Minutes")), c);
			c.gridx = 2;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Seconds")), c);

			c.gridx = 0;
			c.gridy = 3;
			c.insets = new Insets(0, 0, 0, 20);
			durationSubPanel.add(getDurationInputfieldHour(), c);
			c.gridx = 1;
			c.gridy = 3;
			durationSubPanel.add(getDurationInputfieldMinute(), c);
			c.gridx = 2;
			c.gridy = 3;
			durationSubPanel.add(getDurationInputfieldSecond(), c);
		}

		return durationSubPanel;
	}

	private JPanel getDeadlinePanel() {
		if (deadlinePanel == null) {
			deadlinePanel = new JPanel();
			deadlinePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0, 10, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			deadlinePanel.add(getCalendarPanel(), c);
			c.gridx = 0;
			c.gridy = 1;
			deadlinePanel.add(getDeadlineTimePanel(), c);
		}

		return deadlinePanel;
	}

	private JPanel getCalendarPanel() {
		if (calendarPanel == null) {
			calendarPanel = new JPanel();
			calendarPanel.setLayout(new BorderLayout());
			calendarPanel.add(getDeadlineCalendar(), BorderLayout.WEST);
		}

		return calendarPanel;
	}

	private JCalendar getDeadlineCalendar() {
		if (calendar == null) {
			calendar = new JCalendar();
			calendar.setLocale(Locale.ENGLISH);
		}
		return calendar;
	}

	private JPanel getDeadlineTimePanel() {
		if (deadlineTimePanel == null) {
			deadlineTimePanel = new JPanel();
			deadlineTimePanel.setLayout(new BorderLayout());
			deadlineTimePanel.add(getDeadlineTimeSubPanel(), BorderLayout.WEST);
		}

		return deadlineTimePanel;
	}

	private JPanel getDeadlineTimeSubPanel() {
		if (deadlineTimeSubPanel == null) {
			deadlineTimeSubPanel = new JPanel();
			deadlineTimeSubPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(10, 0, 0, 20);
			deadlineTimeSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Hours")), c);
			c.gridx = 1;
			c.gridy = 0;
			deadlineTimeSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Minutes")), c);
			c.gridx = 2;
			c.gridy = 0;
			deadlineTimeSubPanel.add(new JLabel(Messages
					.getString("Transition.Properties.BPEL.Wait.Seconds")), c);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 20);
			deadlineTimeSubPanel.add(getDeadlineInputfieldHour(), c);
			c.gridx = 1;
			c.gridy = 1;
			deadlineTimeSubPanel.add(getDeadlineInputfieldMinute(), c);
			c.gridx = 2;
			c.gridy = 1;
			deadlineTimeSubPanel.add(getDeadlineInputfieldSecond(), c);
		}

		return deadlineTimeSubPanel;
	}

	private JFormattedTextField getDeadlineInputfieldHour() {
		if (deadLineTextFieldHour == null) {
			// deadLineTextFieldHour = new JTextField("00", 10);
			deadLineTextFieldHour = new JFormattedTextField(new Integer(00));
			this.deadLineHourFilter = new JTextFieldEvalution(
					this.deadLineTextFieldHour, null,  new IntegerFilter(
							0, 23), null);
			deadLineTextFieldHour.addKeyListener(this.deadLineHourFilter);
			deadLineTextFieldHour.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldHour;
	}

	private JFormattedTextField getDeadlineInputfieldMinute() {
		if (deadLineTextFieldMinute == null) {
			deadLineTextFieldMinute = new JFormattedTextField(new Integer(00));
			this.deadLineMinuteFilter = new JTextFieldEvalution(
					this.deadLineTextFieldMinute, null,
					new IntegerFilter(0, 59), null);
			deadLineTextFieldMinute.addKeyListener(this.deadLineMinuteFilter);
			deadLineTextFieldMinute.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldMinute;
	}

	private JFormattedTextField getDeadlineInputfieldSecond() {
		if (deadLineTextFieldSecond == null) {
			deadLineTextFieldSecond = new JFormattedTextField(new Integer(00));
			this.deadLineSecondFilter = new JTextFieldEvalution(
					this.deadLineTextFieldSecond, null,
					new IntegerFilter(0, 59), null);
			deadLineTextFieldSecond.addKeyListener(this.deadLineSecondFilter);
			deadLineTextFieldSecond.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldSecond;
	}

	private JTextField getDurationInputfieldYear() {
		if (durationTextFieldYear == null) {
			durationTextFieldYear = new JTextField("0", 10);
			durationTextFieldYear.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldYear;
	}

	private JTextField getDurationInputfieldMonth() {
		if (durationTextFieldMonth == null) {
			durationTextFieldMonth = new JTextField("0", 10);
			durationTextFieldMonth.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldMonth;
	}

	private JTextField getDurationInputfieldDay() {
		if (durationTextFieldDay == null) {
			durationTextFieldDay = new JTextField("0", 10);
			durationTextFieldDay.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldDay;
	}

	private JTextField getDurationInputfieldHour() {
		if (durationTextFieldHour == null) {
			durationTextFieldHour = new JTextField("00", 10);
			durationTextFieldHour.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldHour;
	}

	private JTextField getDurationInputfieldMinute() {
		if (durationTextFieldMinute == null) {
			durationTextFieldMinute = new JTextField("00", 10);
			durationTextFieldMinute.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldMinute;
	}

	private JTextField getDurationInputfieldSecond() {
		if (durationTextFieldSecond == null) {
			durationTextFieldSecond = new JTextField("00", 10);
			durationTextFieldSecond.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldSecond;
	}

	private JPanel getRadioButtonPanel() {
		if (radioButtonPanel == null) {
			radioButtonPanel = new JPanel();
			radioButtonPanel.setLayout(new BorderLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			radioButtonPanel.add(getRadioButtonSubPanel(), BorderLayout.WEST);
		}

		return radioButtonPanel;
	}

	private JPanel getRadioButtonSubPanel() {
		if (radioButtonSubPanel == null) {
			radioButtonSubPanel = new JPanel();
			radioButtonSubPanel.setLayout(new FlowLayout());
			radioButtonSubPanel.add(getWaitDurationEntry());
			radioButtonSubPanel.add(getWaitDeadlineRadioButton());
		}

		return radioButtonSubPanel;
	}

	private JPanel getWaitDurationEntry() {
		if (waitDurationEntry == null) {
			waitDurationEntry = new JPanel();
			waitDurationEntry.setLayout(new BorderLayout());
			waitDurationEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			waitDurationEntry.add(getWaitDurationRadioButton(), c);
		}

		return waitDurationEntry;
	}

	private JRadioButton getWaitDurationRadioButton() {
		if (waitDurationRadioButton == null) {
			waitDurationRadioButton = new JRadioButton(WAIT_DURATION);
			waitDurationRadioButton.setSelected(true);
			waitDurationRadioButton.setActionCommand(WAIT_DURATION);
			waitDurationRadioButton.addActionListener(this);
		}
		return waitDurationRadioButton;
	}

	private JRadioButton getWaitDeadlineRadioButton() {
		if (waitDeadlineRadioButton == null) {
			waitDeadlineRadioButton = new JRadioButton(WAIT_DEADLINE);
			waitDeadlineRadioButton.setActionCommand(WAIT_DEADLINE);
			waitDeadlineRadioButton.addActionListener(this);
		}
		return waitDeadlineRadioButton;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(WAIT_DURATION)) {
			if (deadlinePanel != null) {
				remove(deadlinePanel);
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0, 0, 10, 0);
				add(getDurationPanel(), c1);
				t_editor.repaintTabPane();
			}
		}

		else if (e.getActionCommand().equals(WAIT_DEADLINE)) {
			if (durationPanel != null) {
				remove(durationPanel);
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0, 0, 10, 0);
				add(getDeadlinePanel(), c1);
				t_editor.repaintTabPane();
			}
		}

	}

	// ***************** content getter methods **************************

	public String getSelectedRadioButton() {
		if (waitDurationRadioButton.isSelected() == true) {
			return "Duration";
		}
		return "Deadline";
	}

	// ***** Deadline *****

	public String getDeadline() {
		return "" + getDeadLineYear() + "-" + getDeadLineMonth() + "-"
				+ getDeadLineDay() + "T" + getDeadLineHour() + ":"
				+ getDeadLineMinute() + ":" + getDeadLineSecond() + "+1:00";
	}

	public void setDeadline() {
		Wait wait = (Wait) this.transition.getBpelData();
		this.deadLineTextFieldHour.setText("" + wait.getHour());
		this.deadLineTextFieldMinute.setText("" + wait.getMinute());
		this.deadLineTextFieldSecond.setText("" + wait.getSecond());
		calendar.getCalendar().set(wait.getYear(), wait.getMonth(),
				wait.getDay());
		Calendar c = new JCalendar().getCalendar();
		c.set(wait.getYear(), wait.getMonth() - 1, wait.getDay());
		calendar.setDate(c.getTime());
	}

	public String getDuration() {
		return "P" + getDurationYear() + "Y" + getDurationMonth() + "M"
				+ getDurationDay() + "DT" + getDurationHour() + "H"
				+ getDurationMinute() + "M" + getDurationSecond() + "S";
	}

	public void setDuration() {
		Wait wait = (Wait) this.transition.getBpelData();
		this.durationTextFieldYear.setText("" + wait.getYear());
		this.durationTextFieldMonth.setText("" + wait.getMonth());
		this.durationTextFieldDay.setText("" + wait.getDay());
		this.durationTextFieldHour.setText("" + wait.getHour());
		this.durationTextFieldMinute.setText("" + wait.getMinute());
		this.durationTextFieldSecond.setText("" + wait.getSecond());

	}

	public String getDeadLineDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getDate());
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day / 10 > 0) {
			return "" + day;
		} else {
			return "0" + day;
		}
	}

	public String getDeadLineMonth() {
		if (deadLineTextFieldMinute.getText() == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getDate());
		int month = c.get(Calendar.MONTH) + 1;
		if (month / 10 > 0) {
			return "" + month;
		} else {
			return "0" + month;
		}
	}

	public String getDeadLineYear() {
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getDate());
		return "" + c.get(Calendar.YEAR);
	}

	public String getDeadLineHour() {
		if (deadLineTextFieldHour.getText() == null)
			return null;
		return deadLineTextFieldHour.getText();
	}

	public String getDeadLineMinute() {
		if (deadLineTextFieldMinute.getText() == null)
			return null;
		return deadLineTextFieldMinute.getText();
	}

	public String getDeadLineSecond() {
		if (deadLineTextFieldSecond.getText() == null)
			return null;
		return deadLineTextFieldSecond.getText();
	}

	// ***** Duration ******

	public String getDurationYear() {
		if (durationTextFieldYear.getText() == null)
			return null;
		return durationTextFieldYear.getText();
	}

	public String getDurationMonth() {
		if (durationTextFieldMonth.getText() == null)
			return null;
		return durationTextFieldMonth.getText();
	}

	public String getDurationDay() {
		if (durationTextFieldDay.getText() == null)
			return null;
		return durationTextFieldDay.getText();
	}

	public String getDurationHour() {
		if (durationTextFieldHour.getText() == null)
			return null;
		return durationTextFieldHour.getText();
	}

	public String getDurationMinute() {
		if (durationTextFieldMinute.getText() == null)
			return null;
		return durationTextFieldMinute.getText();
	}

	public String getDurationSecond() {
		if (durationTextFieldSecond.getText() == null)
			return null;
		return durationTextFieldSecond.getText();
	}

	public boolean allFieldsFilled() {
		if (waitDeadlineRadioButton.isSelected()) {
			if (this.deadLineHourFilter.isInputAccepted()
					&& this.deadLineMinuteFilter.isInputAccepted()
					&& this.deadLineSecondFilter.isInputAccepted())
				return true;
			else return false;
		} else {
			if (durationTextFieldYear.getText().equals("")
					|| durationTextFieldMonth.getText().equals("")
					|| durationTextFieldDay.getText().equals("")
					|| durationTextFieldHour.getText().equals("")
					|| durationTextFieldMinute.getText().equals("")
					|| durationTextFieldSecond.getText().equals("")) {
				return false;
			} else
				return true;
		}
	}

	@Override
	public void refresh() {
		if (Wait.class.isInstance(this.transition.getBpelData())) {
			Wait re = (Wait) this.transition.getBpelData();
			if (re.getWaitConditionType() == Wait._DEADLINE) {
				waitDeadlineRadioButton.setSelected(true);
				remove(getDurationPanel());
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0, 0, 10, 0);
				add(getDeadlinePanel(), c1);
				this.setDeadline();
			} else {
				waitDurationRadioButton.setSelected(true);
				remove(getDeadlinePanel());
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0, 0, 10, 0);
				add(getDurationPanel(), c1);
				this.setDuration();
			}
		}
		this.repaint();
	}

	@Override
	public void saveInfomation() {
		if (allFieldsFilled() == false) {
			new PopUpDialog(
					t_editor,
					true,
					Messages.getString("Transition.Properties.BPEL.Error"),
					Messages
							.getString("Transition.Properties.BPEL.ErrorDuringFieldCheck"))
					.setVisible(true);
		} else {

			// Values in TextField already checked
			try {
				if (waitDeadlineRadioButton.isSelected()) {

					this.transition.setBaseActivity(new Wait(this.transition
							.getNameValue(), Wait._DEADLINE, Integer
							.parseInt(getDeadLineYear()), Integer
							.parseInt(getDeadLineMonth()), Integer
							.parseInt(getDeadLineDay()), Integer
							.parseInt(getDeadLineHour()), Integer
							.parseInt(getDeadLineMinute()), Integer
							.parseInt(getDeadLineSecond()))
							.saveInformation(this));
				}
				if (waitDurationRadioButton.isSelected()) {
					this.transition.setBaseActivity(new Wait(this.transition
							.getNameValue(), Wait._DURATION, Integer
							.parseInt(getDurationYear()), Integer
							.parseInt(getDurationMonth()), Integer
							.parseInt(getDurationDay()), Integer
							.parseInt(getDurationHour()), Integer
							.parseInt(getDurationMinute()), Integer
							.parseInt(getDurationSecond()))
							.saveInformation(this));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return this.PANELNAME;
	}

	@Override
	public void showPanel(JPanel panel, GridBagConstraints c) {
		this.refresh();
		panel.add(this, c);
	}
}
