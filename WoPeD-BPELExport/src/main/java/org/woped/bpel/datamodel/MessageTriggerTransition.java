package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.bpel.BaseActivity;

public class MessageTriggerTransition extends Transition<TransitionModel>
{

	public MessageTriggerTransition(TransitionModel data)
	{
		super(data);
		// TODO Auto-generated constructor stub
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

	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!MessageTriggerTransition.class.isInstance(e))
			return false;
		if (this.getData().getId().equals(((MessageTriggerTransition) e).getData().getId()))
			return false;
		return true;
	}

}
