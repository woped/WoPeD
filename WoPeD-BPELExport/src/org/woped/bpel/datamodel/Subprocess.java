package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.SubProcessModel;

public class Subprocess extends Transition<SubProcessModel>
{

	public Subprocess(SubProcessModel data)
	{
		super(data);
	}

	/**
	 * noch nicht final
	 */
	public boolean equals(AbstractElement e)
	{
		if (!Subprocess.class.isInstance(e))
			return false;
		if (((Subprocess) e).getData().getId() != this.getData().getId())
			return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
