package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;
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
	public TExtensibleElements getBpelCode()
	{		
		return new BpelParserModel(((SubProcessModel)this.getData()).getElementContainer()).generate_bpel();
	}
	
	public String toString()
	{
		return Subprocess.class.getSimpleName() + " Stored element " + this.getData().getId();
	}

}
