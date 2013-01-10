package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.bpel.BaseActivity;

public class SimpleTransition extends Transition<TransitionModel>
{

	public SimpleTransition(TransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!SimpleTransition.class.isInstance(e))
			return false;
		if (this.getData().getId().equals(((SimpleTransition) e).getData().getId()))
			return false;
		return true;
	}
	
	@Override
	public TActivity getBpelCode()

	{
		BaseActivity<?> ba = (BaseActivity<?>) this.getData().getBpelData();
		// TActivity activity = null;
		if (ba != null)
		{
			return ba.getActivity();

		} else
		{
			TEmpty iEmpty = TEmpty.Factory.newInstance();
			iEmpty.setName(this.getData().getNameValue());
			return iEmpty;
		}
	}

	public String toString()
	{
		return SimpleTransition.class.getSimpleName() + " Stored element "
				+ this.getData().getId();
	}

}
