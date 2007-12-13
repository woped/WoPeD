package org.woped.bpel.datamodel;


public abstract class NonterminalElements<E extends Object> extends AbstractElement
{

	private E _data;
	
	public NonterminalElements(E data)
	{
		super();
		this._data = data;
	}

}
