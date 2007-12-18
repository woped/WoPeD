package org.woped.bpel.datamodel;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;

public class Model
{

	private static long				MODELCONTER		= 0;

	private long					_id;
	private AbstractElement			_oneelement		= null;
	private Vector<AbstractElement>	_allelements	= new Vector<AbstractElement>();

	public Model()
	{
		this._id = Model.MODELCONTER++;
	}

	public boolean createModel(ModelElementContainer container)
	{
		PetriNetModelElement e = (PetriNetModelElement) container
				.getRootElements().get(0);
		return this.createModel(e, container);
	}

	private boolean createModel(PetriNetModelElement e,
			ModelElementContainer con)
	{

		if (this.get_registrated_element(e) != null)
			return true;
		//System.out.println("createModel " + e.getId());
		AbstractElement element = this.createElement(e);
		if (element == null)
			return false;
		if (this._oneelement == null)
			this._oneelement = element;
		this.regist_element(element);

		//System.out.println("Prelist from " + e.getId());
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

		//System.out.println("postlist from " + e.getId());
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

				//System.out.println(abs.getClass().getSimpleName());
				if (!abs.add_pre_object(element))
					return false;
				if (!element.add_post_object(abs))
					return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	private void regist_element(AbstractElement e)
	{
		this._allelements.add(e);
	}

	private AbstractElement createElement(PetriNetModelElement e)
	{

		//System.out.println(e.getClass().getSimpleName());
		if (PlaceModel.class.isInstance(e))
			return new Place((PlaceModel) e);
		if (TransitionModel.class.isInstance(e))
			return new Transition((TransitionModel) e);
		if (SubProcessModel.class.isInstance(e))
			return new Subprocess((SubProcessModel) e);
		return null;
	}

	/**
	 * 
	 */
	public AbstractElement get_registrated_element(PetriNetModelElement e)
	{
		return this.get_registrated_element(this.createElement(e));
	}

	/**
	 * 
	 */
	public AbstractElement get_registrated_element(AbstractElement e)
	{
		Iterator<AbstractElement> iter = this._allelements.iterator();
		while (iter.hasNext())
		{
			AbstractElement erg = iter.next();
			if (erg.equals(e))
			{
				//System.out.println("found regist object");
				return erg;
			}
		}
		return null;
	}

	public int count_elements()
	{
		return this._allelements.size();
	}

}
