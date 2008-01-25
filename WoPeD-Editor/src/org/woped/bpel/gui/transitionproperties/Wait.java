package org.woped.bpel.gui.transitionproperties;

import org.apache.xmlbeans.XmlCursor;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.woped.translations.Messages;

/**
 * 
 * @author Frank Schüler
 * 
 */
public class Wait extends BaseActivity<TWait> {

	public final static int _NOWAITCONDITIONTYPE = -1;
	public final static int _DEADLINE = 0;
	public final static int _DURATION = 1;

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
			XmlCursor curs = this.getActivity().addNewUntil().newCursor();
			curs.setTextValue(Condition);
		} else if (WaitConditionType == Wait._DURATION) {
			XmlCursor curs = this.getActivity().addNewFor().newCursor();
			curs.setTextValue(Condition);
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
				Messages.getString("Transition.Properties.BPEL.Wait.Duration"))) {
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
		BPELwaitPanel panel = (BPELwaitPanel) bip;
		if (this._waitconditiontype == Wait._DEADLINE) {
			panel.setDeadline(this.getDeadLineCondition());
		} else if (this._waitconditiontype == Wait._DURATION) {
			panel.setDuration(this.getDurationCondition());
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
		if (this.getActivity().getUntil() != null) {
			XmlCursor curs = this.getActivity().getUntil().newCursor();
			return curs.getTextValue();
		}
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public final String getDurationCondition() {
		if (this.getActivity().getFor() != null) {
			XmlCursor curs = this.getActivity().getUntil().newCursor();
			return curs.getTextValue();
		}
		return "";
	}
}
