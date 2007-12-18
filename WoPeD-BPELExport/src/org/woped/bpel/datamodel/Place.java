package org.woped.bpel.datamodel;
import org.woped.core.model.petrinet.PlaceModel;

public class Place extends NonterminalElement<PlaceModel>
{

	public Place(PlaceModel data)
	{
		super(data);
	}

	@Override
	public boolean accept_post_object(AbstractElement e)
	{
		if(Transition.class.isInstance(e)||TerminalElement.class.isInstance(e))return true;
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		if(Transition.class.isInstance(e)||TerminalElement.class.isInstance(e))return true;
		return false;
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if(!Place.class.isInstance(e))return false;
		if(((Place)e).getData().getId() != this.getData().getId())return false;		
		return true;
	}

	@Override
	public String getBpelCode()
	{		
		return null;
	}

}
