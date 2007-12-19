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
		if(XORSplitTransition.class.isInstance(e))return true;
		if(this.getData().getId() == ((XORSplitTransition)e).getData().getId()) return true;
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
