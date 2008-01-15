package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.PetriNetModelElement;

public abstract class NonterminalElement<E extends PetriNetModelElement> extends AbstractElement<PetriNetModelElement>
{	
	public NonterminalElement(PetriNetModelElement data)
	{
		super();
		this.setData(data);
	}
	
	public PetriNetModelElement getData()
	{
		return super.getData();
	}
}
