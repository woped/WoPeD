package org.woped.bpel.datamodel;

//standard java objects
import java.util.HashSet;

/**
 * @author Frank Schüler
 * 
 * This class is the basic object for an element at the data model, for the easy navigation
 * between the elements.
 * 
 * status: finish
 * date: 12.12.2007
 */
public abstract class AbstractElement
{

	private HashSet<AbstractElement>	_pre	= new HashSet<AbstractElement>();
	private HashSet<AbstractElement>	_post	= new HashSet<AbstractElement>();

	/**
	 * abstract constructor
	 */
	public AbstractElement()
	{
	}

	/* methoden for the pre objects */

	/**
	 * This is the method to a new AbstractElement as pre object.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	public boolean add_pre_object(AbstractElement e)
	{
		if (!this.accept_pre_object(e))
			return false;
		if (!this._pre.add(e))
			return false;
		if (!e.add_post_object(this))
		{
			this.remove_pre_object(e);
			return false;
		}
		return true;
	}

	/**
	 * This is a method to remove an AbstractObject from the list of pre
	 * objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * @return boolean
	 */
	public boolean remove_pre_object(AbstractElement e)
	{
		return this._pre.remove(e);
	}

	/**
	 * With this method you can fetch all pre objects.
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement> get_all_pre_objects()
	{
		return this._pre;
	}

	/**
	 * Retured the nummer of pre objects.
	 * 
	 * @return int
	 */
	public int count_pre_objects()
	{
		return this._pre.size();
	}

	/* methoden for the post objects */

	/**
	 * This is the method to a new AbstractElement as post object.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	public boolean add_post_object(AbstractElement e)
	{
		if (!this.accept_post_object(e))
			return false;
		if (!this._post.add(e))
			return false;
		if (!e.accept_pre_object(this))
		{
			this.remove_post_object(e);
			return false;
		}
		return true;
	}

	/**
	 * This is a method to remove an AbstractObject from the list of post
	 * objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * @return boolean
	 */
	public boolean remove_post_object(AbstractElement e)
	{
		return this._post.remove(e);
	}

	/**
	 * With this method you can fetch all post objects.
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement> get_all_post_objects()
	{
		return this._post;
	}

	/**
	 * Retured the nummer of post objects.
	 * 
	 * @return int
	 */
	public int count_post_objects()
	{
		return this._post.size();
	}

	/* support methods */

	/**
	 * The method controls the rules of accepted pre objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	abstract public boolean accept_pre_object(AbstractElement e);

	/**
	 * The method controls the rules of accepted post objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	abstract public boolean accept_post_object(AbstractElement e);

	/**
	 * This method is required for the HashSet objects
	 * 
	 * @param e
	 *            Object
	 * 
	 * @return boolean
	 */
	public boolean equals(Object e)
	{
		if (!AbstractElement.class.isInstance(e))
			return false;
		return this.equals((AbstractElement) e);
	}

	/**
	 * This method is required equal objects
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	abstract public boolean equals(AbstractElement e);
}
