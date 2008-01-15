package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.bpel.*;
public class SimpleTransition extends Transition<TransitionModel>
{
	TActivity bpel = null;
	public SimpleTransition(TransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!SimpleTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((SimpleTransition) e).getData().getId())
			return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		if(this.bpel != null) return this.bpel;
		TAssign t = BPEL.genBpelProsses().addNewAssign();	
		t.setName("Test-Assign");
		this.bpel = t;
		return this.bpel;
	}
	
	public String toString()
	{
		return SimpleTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}

}
