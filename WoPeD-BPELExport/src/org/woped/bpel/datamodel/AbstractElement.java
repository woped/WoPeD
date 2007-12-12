package org.woped.bpel.datamodel;

//standard java objects
import java.util.Vector;

/**
 * 
 */
public abstract class AbstractElement implements Comparable{

	
	private Vector<AbstractElement> _pre;
	private Vector<AbstractElement> _post;
	
	/**
	 * 
	 */
	public AbstractElement() {
		this._post = new Vector<AbstractElement>();
		this._pre = new Vector<AbstractElement>();
	}
	
	/**
	 * 
	 */
	public void add_pre_object(AbstractElement e)
	{
		this._pre.add(e);
	}
	
	/**
	 * 
	 */
	public boolean remove_pre_object(AbstractElement e)
	{
		return this._pre.remove(e);
	}
	
	/**
	 * 
	 */
	public AbstractElement remove_pre_object(int index)
	{
		return this._pre.remove(index);
	}
	
	/**
	 * 
	 */
	public Vector<AbstractElement> get_all_pre_objects()
	{
		return this._pre;
	}

}
