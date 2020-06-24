package org.woped.editor.controller.bpel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.woped.gui.translations.Messages;

/**
 * 
 * @author Frank Sch�ler, Alexander Ro�wog, Johannes H�ndel
 * 
 */
public class Wait extends BaseActivity<TWait> {

	public final static int _NOWAITCONDITIONTYPE = -1;
	public final static int _DEADLINE = 0;
	public final static int _DURATION = 1;

	private int Year;
	private int Month;
	private int Day;
	private int Hour;
	private int Minute;
	private int Second;

	private int _waitconditiontype = -1;

	/**
	 * 
	 * @param Name
	 */
	public Wait(String Name) {
		super(Name);
	}

	/**
	 * 
	 * @param Name
	 * @param Condition
	 * @param WaitConditionType
	 */
	public Wait(String Name, String Condition, int WaitConditionType) {
		this(Name);
		this.fillWait(Condition, WaitConditionType);
	}

	public Wait(String Name, int WaitConditionType, int Year, int Month,
			int Day, int Hour, int Minute, int Second) {
		this(Name);
		this._waitconditiontype = WaitConditionType;
		this.Year = Year;
		this.Month = Month;
		this.Day = Day;
		this.Hour = Hour;
		this.Minute = Minute;
		this.Second = Second;
	}

	public String generateDurationCondition() {
		return "P" + Year + "Y" + Month + "M" + Day + "DT" + Hour + "H"
				+ Minute + "M" + Second + "S";
	}

	public String generateDeadlineCondition() {
		return "" + Year + "-" + Month + "-" + Day + "T" + Hour + ":" + Minute
				+ ":" + Second + java.util.Calendar.getInstance().getTimeZone().getID();
	}

	/**
	 * 
	 * @param Condition
	 * @param WaitConditionType
	 */
	public final void fillWait(String Condition, int WaitConditionType) {
		if (this.hasCondition()) {
			this.genTActivity(this.getName());
		}
		this._waitconditiontype = WaitConditionType;

		if (WaitConditionType == Wait._DEADLINE) {
			this.getActivity().addNewUntil().setValue(
					this.generateDeadlineCondition());
		} else if (WaitConditionType == Wait._DURATION) {
			this.getActivity().addNewFor()
					.setValue(this.generateDurationCondition());
		} else {
			this._waitconditiontype = Wait._NOWAITCONDITIONTYPE;
		}
	}

	/**
	 * @param Name
	 */
	protected void genTActivity(String Name) {
		this.setActivity(TWait.Factory.newInstance());
		this.getActivity().setName(Name);
	}

	/**
	 * @param bip
	 */
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip) {
		if (!BPELwaitPanel.class.isInstance(bip))
			return this;
		BPELwaitPanel panel = (BPELwaitPanel) bip;

		if (panel.getSelectedRadioButton().equalsIgnoreCase(
				Messages.getString("Transition.Properties.BPEL.Wait.Duration"))) {
			this.fillWait(panel.getDuration(), Wait._DURATION);
		} else if (panel.getSelectedRadioButton().equalsIgnoreCase(
				Messages.getString("Transition.Properties.BPEL.Wait.Deadline"))) {
			this.fillWait(panel.getDeadline(), Wait._DEADLINE);
		}
		return this;
	}

	/**
	 * @param bip
	 */
	public void setInformationToPanel(BPELadditionalPanel bip) {
		if (!BPELwaitPanel.class.isInstance(bip))
			return;
		//BPELwaitPanel panel = (BPELwaitPanel) bip;
		if (this._waitconditiontype == Wait._DEADLINE) {
			
		} else if (this._waitconditiontype == Wait._DURATION) {
		}
	}

	/**
	 * 
	 * @return
	 */
	public final boolean hasDeadLine() {
		return this._waitconditiontype == Wait._DEADLINE;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean hasDuration() {
		return this._waitconditiontype == Wait._DURATION;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean hasNoWaitConditionType() {
		return this._waitconditiontype == Wait._NOWAITCONDITIONTYPE;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean hasCondition() {
		return this.hasDeadLine() | this.hasDuration();
	}

	/**
	 * 
	 * @return
	 */
	public final int getWaitConditionType() {
		return this._waitconditiontype;
	}

	/**
	 * 
	 * @return
	 */
	public final String getDeadLineCondition() {
		if (this.getActivity().isSetUntil()) {
			return this.getActivity().getUntil().getValue();
		}
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public final String getDurationCondition() {
		if (this.getActivity().isSetFor()) {
			return this.getActivity().getFor().getValue();
		}
		return "";
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getMonth() {
		return Month;
	}

	public void setMonth(int month) {
		Month = month;
	}

	public int getDay() {
		return Day;
	}

	public void setDay(int day) {
		Day = day;
	}

	public int getHour() {
		return Hour;
	}

	public void setHour(int hour) {
		Hour = hour;
	}

	public int getMinute() {
		return Minute;
	}

	public void setMinute(int minute) {
		Minute = minute;
	}

	public int getSecond() {
		return Second;
	}

	public void setSecond(int second) {
		Second = second;
	}
}
