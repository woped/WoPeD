package org.woped.bpel.datamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;

/**
 * @author Frank Schüler
 * 
 * This class reprasents the model a petrinet.
 */
public class Parser
{

	private static long					MODELCONTER			= 0;

	private long						_id;
	private AbstractElement				_oneelement			= null;
	private HashSet<AbstractElement>	_regist_places		= new HashSet<AbstractElement>();
	private HashSet<AbstractElement>	_regist_transition	= new HashSet<AbstractElement>();

	/**
	 * Constructor to generate an empty object.
	 */
	public Parser()
	{
		this._id = Parser.MODELCONTER++;
	}
	
	/**
	 * This constructor generate an object with a petrinet model.
	 */
	public Parser(ModelElementContainer container)
	{
		this();
		this.createModel(container);
	}

	/**
	 * This method generate the model from a petrinet model.
	 * 
	 * @param container ModelElementContainer
	 * 
	 * @return boolean returned false by error
	 */
	public boolean createModel(ModelElementContainer container)
	{
		PetriNetModelElement e = (PetriNetModelElement) container
				.getRootElements().get(0);
		return this.createModel(e, container);
	}

	/**
	 * This is the internal recurrsiv algorithm to convert the petrinet model to 
	 * the used model in this object.
	 * 
	 * @param e PetriNetModelElement
	 * @param con ModelElementContainer
	 * 
	 * @return boolean returned false by error
	 */
	private boolean createModel(PetriNetModelElement e,
			ModelElementContainer con)
	{

		if (this.get_registrated_element(e) != null)
			return true;
		// System.out.println("createModel " + e.getId());
		AbstractElement element = this.createElement(e);
		if (element == null)
			return false;
		if (this._oneelement == null)
			this._oneelement = element;
		this.regist_element(element);

		// System.out.println("Prelist from " + e.getId());
		Collection<AbstractElementModel> list = con
				.getSourceElements(e.getId()).values();
		Iterator<AbstractElementModel> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElementModel tmp = iter.next();
			if (PetriNetModelElement.class.isInstance(tmp))
			{
				if (!this.createModel((PetriNetModelElement) tmp, con))
					return false;
				AbstractElement abs = this
						.get_registrated_element((PetriNetModelElement) tmp);

				if (!abs.add_post_object(element))
					return false;
				if (!element.add_pre_object(abs))
					return false;
			}
		}

		// System.out.println("postlist from " + e.getId());
		list = con.getTargetElements(e.getId()).values();
		iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractElementModel tmp = iter.next();
			if (PetriNetModelElement.class.isInstance(tmp))
			{
				if (!this.createModel((PetriNetModelElement) tmp, con))
					return false;
				AbstractElement abs = this
						.get_registrated_element((PetriNetModelElement) tmp);

				// System.out.println(abs.getClass().getSimpleName());
				if (!abs.add_pre_object(element))
					return false;
				if (!element.add_post_object(abs))
					return false;
			}
		}
		return true;
	}

	/**
	 * This method regist the elements in seperated lists
	 * 
	 * @param e AbstractElement
	 */
	private void regist_element(AbstractElement e)
	{
		if (Place.class.isInstance(e))
			this._regist_places.add(e);
		else if (Transition.class.isInstance(e))
			this._regist_transition.add(e);
	}

	/**
	 * With this method can deregist an element at model.
	 * 
	 * @param e AbstractElement
	 */
	private void deregist_element(AbstractElement e)
	{
		if (Place.class.isInstance(e))
			this._regist_places.remove(e);
		else if (Transition.class.isInstance(e))
			this._regist_transition.remove(e);
	}
	
	/**
	 * With this method you can find an element at the model.
	 * 
	 * @param e PetriNetModelElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement get_registrated_element(PetriNetModelElement e)
	{
		return this.get_registrated_element(this.createElement(e));
	}
	
	/**
	 * With this method you can find an element at the model.
	 * 
	 * @param e AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement get_registrated_element(AbstractElement e)
	{
		
		if(Place.class.isInstance(e))
		{
			Iterator<AbstractElement> iter = this._regist_places.iterator();
			while (iter.hasNext())
			{
				AbstractElement erg = iter.next();
				if (erg.equals(e))
				{
					// System.out.println("found regist object");
					return erg;
				}
			}
		}
		else
		{
			Iterator<AbstractElement> iter = this._regist_transition.iterator();
			while (iter.hasNext())
			{
				AbstractElement erg = iter.next();
				if (erg.equals(e))
				{
					// System.out.println("found regist object");
					return erg;
				}
			}
		}		
		return null;
	}

	/**
	 * This method create the right AbstractElement for a PetriNetModelElement.
	 * 
	 * @param e PetriNetModelElement
	 * 
	 * @return AbstractElement
	 */
	private AbstractElement createElement(PetriNetModelElement e)
	{
		System.out.println(e.getClass().getSimpleName());
		if (PlaceModel.class.isInstance(e))
			return new Place((PlaceModel) e);
		if (TransitionModel.class.isInstance(e))
			return new SimpleTransition((TransitionModel) e);
		if (SubProcessModel.class.isInstance(e))
			return new Subprocess((SubProcessModel) e);
		return null;
	}

	/**
	 * Returned the count of elements
	 */
	public int count_elements()
	{
		return this._regist_places.size()+ this._regist_transition.size();
	}

}
