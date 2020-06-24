package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

public abstract class NonterminalElement<E extends AbstractPetriNetElementModel> extends AbstractElement<E>
{	
	public NonterminalElement(E data)
	{
		super();
		this.setData(data);
	}
	
	public E getData()
	{
		return super.getData();
	}
}
