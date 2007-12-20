package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;


public class XORSplitTransition extends Transition<XORSplitOperatorTransitionModel>
{
	public XORSplitTransition(XORSplitOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if(!XORSplitTransition.class.isInstance(e))return false;
		if(this.getData().getId() != ((XORSplitTransition)e).getData().getId()) return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

	public String toString()
	{
		return XORSplitTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}
}
