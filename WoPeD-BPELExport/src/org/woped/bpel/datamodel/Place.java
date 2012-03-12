package org.woped.bpel.datamodel;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.woped.core.model.petrinet.PlaceModel;

public class Place extends NonterminalElement<PlaceModel>
{

	public Place(PlaceModel data)
	{
		super(data);
	}

	@Override
	public boolean accept_post_object(AbstractElement<?> e)
	{
		if(!Place.class.isInstance(e))return true;
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement<?> e)
	{
		if(!Place.class.isInstance(e))return true;
		return false;
	}

	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if(!Place.class.isInstance(e))return false;
		if(!((Place)e).getData().getId().equals(this.getData().getId())) return false;		
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{		
		return null;
	}

	public String toString()
	{
		return Place.class.getSimpleName() + " Stored element " + this.getData().getId();
	}
}
