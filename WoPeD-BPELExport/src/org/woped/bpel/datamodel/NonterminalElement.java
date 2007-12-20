package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.PetriNetModelElement;

public abstract class NonterminalElement<E extends PetriNetModelElement> extends AbstractElement<E>
{	
	public NonterminalElement(E data)
	{
		super();
		this.setData(data);
	}

}
