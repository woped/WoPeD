package org.woped.bpel.gui.transitionproperties;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDeadlineExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDurationExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;


public class Wait extends BaseActivity
{

	public Wait()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void saveInformation(BPELwaitPanel bwp)
	{
		TProcess p = BaseActivity.genBpelProsses();
		TWait wait = p.addNewWait();
		wait.setName(""+bwp.transition.getNameValue());
		String bool = bwp.getSelectedRadioButton();
		if(bool.equalsIgnoreCase("Deadline"))
		{
			XmlCursor curs = wait.addNewUntil().newCursor();
			curs.setTextValue(bwp.getDeadline());
		}
		else
		{
			XmlCursor curs = wait.addNewFor().newCursor();
			curs.setTextValue(bwp.getDuration());
		}
		this.setActivity(wait);
	}
	
	public void setInformationToPanel(BPELwaitPanel bwp)
	{
		TWait wait = (TWait) this.getActivity();
		
		if(wait.getUntil() != null)
		{
			TDeadlineExpr tdee = wait.getUntil();
		}
		if(wait.getFor() != null)
		{
			TDurationExpr tdue = wait.getFor();
		}
	}
}
