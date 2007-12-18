package org.woped.bpel.datamodel;

//standard java objects
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author Frank Schüler
 * 
 * This class is the basic object for an element at the data model, for the easy
 * navigation between the elements.
 * 
 * status: at work date: 12.12.2007
 */
public abstract class AbstractElement<E>
{

	private static long					IdCounter	= 0L;

	private HashSet<AbstractElement>	_pre		= new HashSet<AbstractElement>();
	private HashSet<AbstractElement>	_post		= new HashSet<AbstractElement>();
	private long						_id;
	private E							_data;

	/**
	 * abstract constructor
	 */
	public AbstractElement(E data)
	{
		this._id = AbstractElement.IdCounter++;
		this._data = data;
	}

	/**
	 * 
	 */
	public E getData()
	{
		return this._data;
	}

	/**
	 * Returned the id form the object
	 * 
	 * @return long
	 */
	public long getID()
	{
		return this._id;
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
		if (!e.add_post_object(this))
			return false;
		this._pre.add(e);
		return true;
	}

	/**
	 * Find an Element at pre objects by ID.
	 * 
	 * @param id
	 *            long
	 * 
	 * @return AbstractElement, returned null if no matching
	 */
	public AbstractElement find_pre_object_by_id(long id)
	{
		Iterator<AbstractElement> list = this._pre.iterator();
		AbstractElement erg = null;
		while (list.hasNext())
		{
			if ((erg = list.next()).getID() == id)
				return erg;
		}
		return null;
	}

	/**
	 * 
	 */
	public AbstractElement find_pre_object_by_Source(AbstractElement e)
	{
		if (!this.accept_pre_object(e))
			return null;
		HashSet<AbstractElement> list = this.get_all_post_objects();
		Iterator<AbstractElement> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElement erg = iter.next();
			if (e.equals(erg))
				return erg;
		}
		return null;
	}

	/**
	 * This method remove all links from pre object to this object and from this
	 * object to the pre object.
	 * 
	 * @param pre
	 *            AbstractElement
	 */
	public void remove_pre_object_relationship(AbstractElement pre)
	{
		pre.remove_post_object(this);
		this.remove_pre_object(pre);
	}

	/**
	 * This is a method to remove an AbstractObject from the list of pre by this
	 * ID. objects.
	 * 
	 * @param id
	 *            long
	 * @return boolean
	 */
	public boolean remove_pre_object(long id)
	{
		return this._pre.remove(this.find_pre_object_by_id(id));
	}

	/**
	 * Removed all relations between this object and alle pre object.
	 */
	public void remove_all_pre_relationship()
	{
		Iterator<AbstractElement> list = this._pre.iterator();
		while (list.hasNext())
		{
			list.next().remove_post_object(this);
		}
		this._pre.clear();
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
	 * This method delete all links to pre objects.
	 */
	public void remove_all_pre_objects()
	{
		this._pre.clear();
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
		if (!e.accept_pre_object(this))
			return false;
		this._post.add(e);

		return true;
	}

	/**
	 * Find an Element at post objects by ID.
	 * 
	 * @param id
	 *            long
	 * 
	 * @return AbstractElement, returned null if no matching
	 */
	public AbstractElement find_post_object_by_id(long id)
	{
		Iterator<AbstractElement> list = this._post.iterator();
		AbstractElement erg = null;
		while (list.hasNext())
		{
			if ((erg = list.next()).getID() == id)
				return erg;
		}
		return null;
	}

	/**
	 * 
	 */
	public AbstractElement find_post_object_by_Source(AbstractElement e)
	{
		if (!this.accept_pre_object(e))
			return null;
		HashSet<AbstractElement> list = this.get_all_post_objects();
		Iterator<AbstractElement> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElement erg = iter.next();
			if (e.equals(erg))
				return erg;
		}
		return null;
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
	 * This is a method to remove an AbstractObject from the list of post by
	 * this ID. objects.
	 * 
	 * @param id
	 *            long
	 * @return boolean
	 */
	public boolean remove_post_object(long id)
	{
		return this._post.remove(this.find_post_object_by_id(id));
	}

	/**
	 * This method delete all links to post objects.
	 */
	public void remove_all_post_objects()
	{
		this._post.clear();
	}

	/**
	 * This method remove all links from post object to this object and from
	 * this object to the post object.
	 * 
	 * @param pre
	 *            AbstractElement
	 */
	public void remove_post_object_relationship(AbstractElement pre)
	{
		pre.remove_post_object(this);
		this.remove_pre_object(pre);
	}

	/**
	 * Removed all relations between this object and alle post object.
	 */
	public void remove_all_post_relationship()
	{
		Iterator<AbstractElement> list = this._post.iterator();
		while (list.hasNext())
		{
			list.next().remove_pre_object(this);
		}
		this._post.clear();
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
		if (((AbstractElement) e).getID() != this._id)
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

	abstract public String getBpelCode();

	/**
	 * dont use
	 */
	public AbstractElement findElement(AbstractElement e)
	{

		return this.findElement(this, e, new LinkedList<AbstractElement>());
	}

	/**
	 * dont use
	 */
	private AbstractElement findElement(AbstractElement begin,
			AbstractElement e, LinkedList<AbstractElement> usedNotes)
	{
		if (begin == null)
			return null;
		AbstractElement erg = null;

		erg = begin.find_post_object_by_Source(e);
		if (erg != null)
			return erg;
		erg = begin.find_pre_object_by_Source(e);
		if (erg != null)
			return erg;
		usedNotes.add(begin);

		HashSet<AbstractElement> prelist = begin.get_all_pre_objects();
		HashSet<AbstractElement> postlist = begin.get_all_post_objects();
		Iterator<AbstractElement> preiter = prelist.iterator();
		Iterator<AbstractElement> postiter = postlist.iterator();
		while (preiter.hasNext() || postiter.hasNext())
		{
			AbstractElement tmp = preiter.next();
			if (usedNotes.indexOf(tmp) == -1)
			{
				erg = this.findElement(tmp, e, usedNotes);
				if (erg != null)
					return erg;
			}
			tmp = postiter.next();
			if (usedNotes.indexOf(tmp) == -1)
			{
				erg = this.findElement(tmp, e, usedNotes);
				if (erg != null)
					return erg;
			}
		}
		return null;
	}

}
