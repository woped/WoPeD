package org.woped.bpel.datamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;

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
	 * @param container
	 *            ModelElementContainer
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
	 * @param e
	 *            PetriNetModelElement
	 * @param con
	 *            ModelElementContainer
	 * 
	 * @return boolean returned false by error
	 */
	private boolean createModel(PetriNetModelElement e,
			ModelElementContainer con)
	{
		//System.out.println("\n******************\n"
			//	+ "Working an PetrinetElement " + e.getId() + "\n"
			//	+ "**************\n");

		if (this.get_registrated_element(e) != null)return true;
		// System.out.println("createModel " + e.getId());
		AbstractElement element = this.createElement(e);
		if (element == null)
			return false;
		if (this._oneelement == null)
			this._oneelement = element;
		this.regist_element(element);

		//System.out.println("***\nPrelist from " + e.getId() + "\n***");
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

				if (abs == null)return false;
				if (!abs.add_post_object(element))return false;
				if (!element.add_pre_object(abs))return false;
			}
		}

		//System.out.println("***\nPostlist from " + e.getId() + "\n***");
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

				if (abs == null)
					return false;
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
	 * @param e
	 *            AbstractElement
	 */
	private void regist_element(AbstractElement e)
	{
		System.out.println("i am regist the element!" + e);
		if (Place.class.isInstance(e))
			this._regist_places.add(e);
		else
			this._regist_transition.add(e);
	}

	/**
	 * With this method can deregist an element at model.
	 * 
	 * @param e
	 *            AbstractElement
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
	 * @param e
	 *            PetriNetModelElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement get_registrated_element(PetriNetModelElement e)
	{
		//System.out.println("search for element " + e.getClass().toString());
		return this.get_registrated_element(this.createElement(e));
	}

	/**
	 * With this method you can find an element at the model.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement get_registrated_element(AbstractElement e)
	{
		//System.out.println("search " + e.getClass().toString());
		if (Place.class.isInstance(e))
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
		} else
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
	 * @param e
	 *            PetriNetModelElement
	 * 
	 * @return AbstractElement
	 */
	private static AbstractElement createElement(PetriNetModelElement e)
	{
		//System.out.println(e.getClass().getSimpleName() + " " + e.getId());
		if (PlaceModel.class.isInstance(e))
			return new Place((PlaceModel) e);
		if (ANDSplitOperatorTransitionModel.class.isInstance(e))
			return new ANDSplitTransition((ANDSplitOperatorTransitionModel) e);
		if (ANDJoinOperatorTransitionModel.class.isInstance(e))
			return new ANDJoinTransition((ANDJoinOperatorTransitionModel) e);
		if (XORJoinOperatorTransitionModel.class.isInstance(e))
			return new XORJoinTransition((XORJoinOperatorTransitionModel) e);
		if (XORSplitOperatorTransitionModel.class.isInstance(e))
			return new XORSplitTransition((XORSplitOperatorTransitionModel) e);
		if (SubProcessModel.class.isInstance(e))
			return new Subprocess((SubProcessModel) e);
		if (TransitionModel.class.isInstance(e))
			return new SimpleTransition((TransitionModel) e);

		//System.out
		//		.println("cant find the right object to generate the correct AbstractElement!");
		return null;
	}

	/**
	 * Returned the count of elements
	 */
	public int count_elements()
	{
		return this._regist_places.size() + this._regist_transition.size();
	}
	
	/**
	 * This method test of sequens and return the endelement of sequence.
	 * @param e AbstractElement startelement of sequence
	 * @return AbstractElement
	 */
	public AbstractElement isSequense(AbstractElement e)
	{
		if(e == null) return null;
		if(Place.class.isInstance(e)) return null;
		if(e.count_post_objects() != 1) return null;
		AbstractElement tmp = e.get_first_post_element();
		if(tmp.count_post_objects() != 1) return null;
		tmp = tmp.get_first_post_element();
		return tmp;
	}
	
	/**
	 * This method eliminate all sequences and replace all contained elements
	 * the sequence as the SequenceTransition element.
	 */
	public void eliminate_all_seqences()
	{
		Iterator<AbstractElement> list = this._regist_transition.iterator();
		while(list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = this.isSequense(begin);
			if(end != null)
			{
				System.out.println("<Sequense> \n" + 
						"\tbegin = " + begin + "\n" + 
						"\tend = " + end + "\n</Sequence>");
				Iterator<AbstractElement> pre_list = begin.get_all_pre_objects().iterator();
				Iterator<AbstractElement> post_list = end.get_all_post_objects().iterator();
				
				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();
				
				AbstractElement e = new SequenceTransition(begin);
				AbstractElement tmp;
				while(pre_list.hasNext())
				{
					tmp = pre_list.next();
					tmp.add_post_object(e);
					e.add_pre_object(tmp);
				}
				while(post_list.hasNext())
				{
					tmp = post_list.next();
					tmp.add_pre_object(e);
					e.add_post_object(tmp);
				}
				
			}
		}
	}
	
	/**
	 * This method test of pick and search for the endelement.
	 * @param e AbstractElement begin of pick
	 * @return AbstractElement
	 */
	public AbstractElement isPick(AbstractElement e)
	{
		if(!Place.class.isInstance(e)) return null;
		if(e.count_post_objects() < 2) return null; 
		AbstractElement end;
		AbstractElement tmp;
		Iterator<AbstractElement> list = e.get_all_post_objects().iterator();
		tmp = list.next();
		if(tmp.count_post_objects() != 1) return null;
		end = tmp.get_first_post_element();
		while(list.hasNext())
		{
			tmp = list.next();
			if(tmp.count_post_objects() != 1) return null;
			if(!end.equals(tmp.get_first_post_element())) return null;
		}
		
		return e;
	}
	
	/**
	 * This method eliminate all picks and replace all contained elements
	 * the picks as the PickTransition element.
	 */
	public void eliminate_all_picks()
	{
		Iterator<AbstractElement> list = this._regist_transition.iterator();
		while(list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = this.isPick(begin);
			if(end != null)
			{
				System.out.println("<Pick> \n" + 
						"\tbegin = " + begin + "\n" + 
						"\tend = " + end + "\n</Pick>");
				
				AbstractElement e = new SequenceTransition(begin);
				begin.remove_all_post_relationship();
				begin.add_post_object(e);
				e.add_pre_object(begin);
				
				end.remove_all_pre_relationship();
				end.add_pre_object(e);
				e.add_post_object(end);
				
			}
		}
	}

}
