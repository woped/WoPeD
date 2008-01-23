package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;


public class Empty extends BaseActivity
{

	public Empty()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void saveEmptyInformation()
	{
		TProcess p = BaseActivity.genBpelProsses();
		TEmpty empty = p.addNewEmpty();
		this.setActivity(empty);
	}

}
