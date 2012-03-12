package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
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
	public boolean equals(AbstractElement<?> e)
	{
		if (!Subprocess.class.isInstance(e))
			return false;
		if (!((Subprocess) e).getData().getId().equals(this.getData().getId()))
			return false;
		return true;
	}

	@Override
	public TProcess getBpelCode()
	{		
		return new BpelParserModel(((SubProcessModel)this.getData()).getElementContainer()).generate_bpel();
	}
	
	public String toString()
	{
		return Subprocess.class.getSimpleName() + " Stored element " + this.getData().getId();
	}

}
