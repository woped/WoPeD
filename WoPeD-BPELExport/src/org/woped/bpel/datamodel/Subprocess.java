package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.SubProcessModel;


public class Subprocess extends NonterminalElement<SubProcessModel>
{

	public Subprocess(SubProcessModel data)
	{
		super(data);
	}

	public boolean accept_post_object(AbstractElement e)
	{
		if(Place.class.isInstance(e))return true;
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		if(Place.class.isInstance(e))return true;
		return false;
	}

	/**
	 * noch nicht final
	 */
	public boolean equals(AbstractElement e)
	{
		if(!Subprocess.class.isInstance(e))return false;
		if(((Subprocess)e).getData().getId() != this.getData().getId())return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
