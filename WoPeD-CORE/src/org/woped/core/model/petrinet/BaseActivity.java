package org.woped.core.model.petrinet;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class BaseActivity
{
	private TActivity Data = null;

	public TActivity getActivity()
	{
		return this.Data;
	}
	
	public BaseActivity()
	{
		// TODO Auto-generated constructor stub
	}

}
