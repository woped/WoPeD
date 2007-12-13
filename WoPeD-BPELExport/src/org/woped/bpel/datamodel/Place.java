package org.woped.bpel.datamodel;


public class Place<Place> extends NonterminalElements
{

	public Place(Place data)
	{
		super(data);
	}

	@Override
	public boolean accept_post_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		return false;
	}

}
