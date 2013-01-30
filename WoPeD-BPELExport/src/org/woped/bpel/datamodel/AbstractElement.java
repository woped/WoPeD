package org.woped.bpel.datamodel;

//standard java objects
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

/**
 * @author Frank Sch�ler
 * 
 * This class is the basic object for an element at the data model, for the easy
 * navigation between the elements.
 * 
 * status: at work 
 * date: 12.12.2007
 */
public abstract class AbstractElement<E>
{

	private static long					IdCounter	= 0L;

	private HashSet<AbstractElement<?>>	_pre		= new HashSet<AbstractElement<?>>();
	private HashSet<AbstractElement<?>>	_post		= new HashSet<AbstractElement<?>>();
	private long						_id			= IdCounter++;
	private E							_data;

	/**
	 * abstract constructor
	 */
	public AbstractElement()
	{
	}

	/**
	 * With this method, you can set the internal stored data.
	 * 
	 * @param data E
	 */
	protected void setData(E data)
	{
		this._data = data;
	}

	/**
	 * This returned the saved data.
	 * 
	 * @return E
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
	public boolean add_pre_object(AbstractElement<?> e)
	{
		if (!this.accept_pre_object(e))
			return false;
		if (!e.accept_post_object(this))
			return false;
		this._pre.add(e);
		return true;
	}

	/**
	 * This is the method to a new AbstractElement as pre object and add this
	 * object as post object to the new AbstractElement.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	public boolean add_pre_object_relationship(AbstractElement<?> e)
	{
		if (!this.accept_pre_object(e))
			return false;
		if (!e.add_post_object(this))
			return false;
		this._pre.add(e);
		return true;
	}

	/**
	 * This method add a list of pre objects to this object and add this object
	 * to the elements of the list as post object.
	 * 
	 * @param list
	 *            HashSet<AbstractElement>
	 * @return returned false, if one object not added
	 */
	public boolean add_pre_object_list(HashSet<AbstractElement<?>> list)
	{
		Iterator<AbstractElement<?>> iter = list.iterator();
		boolean erg = true;
		while (iter.hasNext())
			if (!this.add_pre_object(iter.next()))
				erg = false;
		return erg;
	}

	/**
	 * This method add a list of pre objects
	 * 
	 * @param list
	 *            HashSet<AbstractElement>
	 * @return returned false, if one object not added
	 */
	public boolean add_pre_object_list_relationship(
			HashSet<AbstractElement<?>> list)
	{
		Iterator<AbstractElement<?>> iter = list.iterator();
		boolean erg = true;
		while (iter.hasNext())
			if (!this.add_pre_object_relationship(iter.next()))
				erg = false;
		return erg;
	}

	/**
	 * Find an Element at pre objects by ID.
	 * 
	 * @param id
	 *            long
	 * 
	 * @return AbstractElement, returned null if no matching
	 */
	public AbstractElement<?> find_pre_object_by_id(long id)
	{
		Iterator<AbstractElement<?>> list = this._pre.iterator();
		AbstractElement<?> erg = null;
		while (list.hasNext())
		{
			if ((erg = list.next()).getID() == id)
				return erg;
		}
		return null;
	}

	/**
	 * Find a AbstractElement at the pre list by the saved data.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> find_pre_object_by_Source(AbstractElement<?> e)
	{
		if (!this.accept_pre_object(e))
			return null;
		HashSet<AbstractElement<?>> list = this.get_all_post_objects();
		Iterator<AbstractElement<?>> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElement<?> erg = iter.next();
			if (e.equals(erg))
				return erg;
		}
		return null;
	}

	/**
	 * This method retured the first element at the pre list.
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> get_first_pre_element()
	{
		return this._pre.iterator().next();
	}

	/**
	 * This method remove all links from pre object to this object and from this
	 * object to the pre object.
	 * 
	 * @param pre
	 *            AbstractElement
	 */
	public void remove_pre_object_relationship(AbstractElement<?> pre)
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
		Iterator<AbstractElement<?>> list = this._pre.iterator();
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
	public boolean remove_pre_object(AbstractElement<?> e)
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
	public HashSet<AbstractElement<?>> get_all_pre_objects()
	{
		return this._pre;
	}

	/**
	 * Retured the iterator of the pre object list.
	 * 
	 * @return Iterator<AbstractElement>
	 */
	public Iterator<AbstractElement<?>> get_pre_list_iterator()
	{
		return this._pre.iterator();
	}

	/**
	 * This method make a copy of the list of pre objects.
	 * 
	 * @return Vector<AbstractElement>
	 */
	public HashSet<AbstractElement<?>> get_pre_list_copy()
	{
		HashSet<AbstractElement<?>> list = new HashSet<AbstractElement<?>>();
		Iterator<AbstractElement<?>> iter = this._pre.iterator();
		while (iter.hasNext())
			list.add(iter.next());
		return list;
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
	public boolean add_post_object(AbstractElement<?> e)
	{
		if (!this.accept_post_object(e))
			return false;
		if (!e.accept_pre_object(this))
			return false;
		this._post.add(e);
		return true;
	}

	/**
	 * This is the method to a new AbstractElement as post object and add this
	 * object as pre object to the new AbstractElement.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	public boolean add_post_object_Relationship(AbstractElement<?> e)
	{
		if (!this.accept_post_object(e))
			return false;
		if (!e.add_pre_object(this))
			return false;
		this._post.add(e);
		return true;
	}

	/**
	 * This method add a list of post objects
	 * 
	 * @param list
	 *            HashSet<AbstractElement>
	 * @return returned false, if one object not added
	 */
	public boolean add_post_object_list(HashSet<AbstractElement<?>> list)
	{
		Iterator<AbstractElement<?>> iter = list.iterator();
		boolean erg = true;
		while (iter.hasNext())
			if (!this.add_post_object(iter.next()))
				erg = false;
		return erg;
	}

	/**
	 * This method add a list of post objects and add this object to the objects
	 * of the list as pre object.
	 * 
	 * @param list
	 *            HashSet<AbstractElement>
	 * @return returned false, if one object not added
	 */
	public boolean add_post_object_list_relationship(
			HashSet<AbstractElement<?>> list)
	{
		Iterator<AbstractElement<?>> iter = list.iterator();
		boolean erg = true;
		while (iter.hasNext())
			if (!this.add_post_object_Relationship(iter.next()))
				erg = false;
		return erg;
	}

	/**
	 * Find an Element at post objects by ID.
	 * 
	 * @param id
	 *            long
	 * 
	 * @return AbstractElement, returned null if no matching
	 */
	public AbstractElement<?> find_post_object_by_id(long id)
	{
		Iterator<AbstractElement<?>> list = this._post.iterator();
		AbstractElement<?> erg = null;
		while (list.hasNext())
		{
			if ((erg = list.next()).getID() == id)
				return erg;
		}
		return null;
	}

	/**
	 * Find a AbstractElement at the post list by the saved data.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> find_post_object_by_Source(AbstractElement<?> e)
	{
		if (!this.accept_pre_object(e))
			return null;
		HashSet<AbstractElement<?>> list = this.get_all_post_objects();
		Iterator<AbstractElement<?>> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElement<?> erg = iter.next();
			if (e.equals(erg))
				return erg;
		}
		return null;
	}

	/**
	 * This method retured the first element at the post list.
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> get_first_post_element()
	{
		try {
			return this._post.iterator().next();
			}
		catch (Exception e) { return null; }
	}

	/**
	 * This is a method to remove an AbstractObject from the list of post
	 * objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * @return boolean
	 */
	public boolean remove_post_object(AbstractElement<?> e)
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
	public void remove_post_object_relationship(AbstractElement<?> pre)
	{
		pre.remove_post_object(this);
		this.remove_pre_object(pre);
	}

	/**
	 * Removed all relations between this object and alle post object.
	 */
	public void remove_all_post_relationship()
	{
		Iterator<AbstractElement<?>> list = this._post.iterator();
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
	public HashSet<AbstractElement<?>> get_all_post_objects()
	{
		return this._post;
	}

	/**
	 * Returned the Iterator of the post object list.
	 * 
	 * @return Iterator<AbstractElement>
	 */
	public Iterator<AbstractElement<?>> get_post_list_iterator()
	{
		return this._post.iterator();
	}

	/**
	 * This method make a copy of the list of post objects.
	 * 
	 * @return Vector<AbstractElement>
	 */
	public HashSet<AbstractElement<?>> get_post_list_copy()
	{
		HashSet<AbstractElement<?>> list = new HashSet<AbstractElement<?>>();
		Iterator<AbstractElement<?>> iter = this._post.iterator();
		while (iter.hasNext())
			list.add(iter.next());
		return list;
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
	abstract public boolean accept_pre_object(AbstractElement<?> e);

	/**
	 * The method controls the rules of accepted post objects.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	abstract public boolean accept_post_object(AbstractElement<?> e);

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
		if (((AbstractElement<?>) e).getID() != this._id)
			return false;
		return this.equals((AbstractElement<?>) e);
	}

	/**
	 * This method is required equal objects
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return boolean
	 */
	abstract public boolean equals(AbstractElement<?> e);

	/**
	 * Returned the Bpel code of this element.
	 * 
	 * @return String
	 */
	abstract public TExtensibleElements getBpelCode();
	
	/**
	 * With this method, you can find an AbstractElement.
	 * Searched by source.
	 * 
	 * @param e AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> findElement(AbstractElement<?> e)
	{

		return this.findElement(this, e, new LinkedList<AbstractElement<?>>());
	}

	/**
	 * 
	 * This method is the reccuriv algorithm to find a element
	 * at the model of the AbstractElements.
	 * 
	 * @param begin
	 * @param e
	 * @param usedNotes
	 * @return
	 */
	private AbstractElement<?> findElement(AbstractElement<?> begin,
			AbstractElement<?> e, LinkedList<AbstractElement<?>> usedNotes)
	{
		if (begin == null)
			return null;
		AbstractElement<?> erg = null;

		erg = begin.find_post_object_by_Source(e);
		if (erg != null)
			return erg;
		erg = begin.find_pre_object_by_Source(e);
		if (erg != null)
			return erg;
		usedNotes.add(begin);

		Iterator<AbstractElement<?>> preiter = begin.get_all_pre_objects().iterator();
		Iterator<AbstractElement<?>> postiter = begin.get_all_post_objects().iterator();
		while (preiter.hasNext() || postiter.hasNext())
		{
			AbstractElement<?> tmp = preiter.next();
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
