package org.woped.bpel.datamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;

/**
 * @author Frank Schüler
 * 
 * This class reprasents the model a petrinet.
 */
public class BpelParserModel
{

	private static long					MODELCOUNTER		= 0;

	private long						_id					= MODELCOUNTER++;
	private AbstractElement				_oneelement			= null;
	private HashSet<AbstractElement>	_regist_places		= new HashSet<AbstractElement>();
	private HashSet<AbstractElement>	_regist_transition	= new HashSet<AbstractElement>();
	private Stack<AbstractElement>		_reg_to_deregist	= new Stack<AbstractElement>();

	/**
	 * Constructor to generate an empty object.
	 */
	public BpelParserModel()
	{

	}

	/**
	 * This constructor generate an object with a petrinet model.
	 */
	public BpelParserModel(ModelElementContainer container)
	{
		this();
		this.createModel(container);
	}

	/**
	 * Retured a copy registet places list.
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement> get_copy_of_regist_places()
	{
		HashSet<AbstractElement> copy = new HashSet<AbstractElement>();
		Iterator<AbstractElement> list = this._regist_places.iterator();
		while (list.hasNext())
			copy.add(list.next());
		return copy;
	}

	/**
	 * Retured a copy registet transition list
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement> get_copy_of_regist_transition()
	{
		HashSet<AbstractElement> copy = new HashSet<AbstractElement>();
		Iterator<AbstractElement> list = this._regist_transition.iterator();
		while (list.hasNext())
			copy.add(list.next());
		return copy;
	}

	public String toString()
	{
		String erg = new String();
		Iterator<AbstractElement> plist = this._regist_places.iterator();
		Iterator<AbstractElement> tlist = this._regist_transition.iterator();
		while (plist.hasNext())
			erg = erg + "\n" + plist.next().getClass().getSimpleName();
		while (tlist.hasNext())
			erg = erg + "\n" + tlist.next().getClass().getSimpleName();
		return erg;
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
		// System.out.println("\n******************\n"
		// + "Working an PetrinetElement " + e.getId() + "\n"
		// + "**************\n");

		if (this.get_registrated_element(e) != null)
			return true;
		// System.out.println("createModel " + e.getId());
		AbstractElement element = this.createElement(e);
		if (element == null)
			return false;
		if (this._oneelement == null)
			this._oneelement = element;
		this.regist_element(element);

		// System.out.println("***\nPrelist from " + e.getId() + "\n***");
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

				if (abs == null)
					return false;
				if (!abs.add_post_object(element))
					return false;
				if (!element.add_pre_object(abs))
					return false;
			}
		}

		// System.out.println("***\nPostlist from " + e.getId() + "\n***");
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
		// System.out.println("i am regist the element!" + e);
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
		else
			this._regist_transition.remove(e);
	}

	/**
	 * This method deregist all elements who has a relation to this element and
	 * deregist this element.
	 * 
	 * @param e
	 *            AbstractElement
	 */
	private void deregist_submodel(AbstractElement e)
	{
		if (this.get_registrated_element(e) == null)
			return;
		this.deregist_element(e);
		Iterator<AbstractElement> pre = e.get_all_pre_objects().iterator();
		Iterator<AbstractElement> post = e.get_all_post_objects().iterator();

		while (pre.hasNext())
			this.deregist_submodel(pre.next());
		while (post.hasNext())
			this.deregist_submodel(post.next());
	}

	/**
	 * With this method you can regist elements to deregist.
	 * 
	 * @param e
	 *            AbstractElement
	 */
	private void regist_to_deregist(AbstractElement e)
	{
		this._reg_to_deregist.add(e);
		if (e.equals(this._oneelement))
			this._oneelement = this._regist_places.iterator().next();
	}

	/**
	 * Deregist the elements how was flaged as to deregist.
	 */
	private void deregist_all_flaged_elements()
	{
		while (!this._reg_to_deregist.isEmpty())
		{
			this.deregist_element(this._reg_to_deregist.pop());
		}
	}

	/**
	 * This method deregist all elements who has a relation to this element and
	 * deregist this element.
	 * 
	 * @param e
	 *            AbstractElement
	 */
	private void reg_to_deregist_submodel(AbstractElement e)
	{
		if (this._reg_to_deregist.search(e) != -1)
			return;
		this.regist_to_deregist(e);
		// System.out.println("Deregist " + e.getClass().toString());
		Iterator<AbstractElement> pre = e.get_all_pre_objects().iterator();
		Iterator<AbstractElement> post = e.get_all_post_objects().iterator();

		while (pre.hasNext())
			this.reg_to_deregist_submodel(pre.next());
		while (post.hasNext())
			this.reg_to_deregist_submodel(post.next());
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
		// System.out.println("search for element " + e.getClass().toString());
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
		// System.out.println("search " + e.getClass().toString());
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
		// System.out.println(e.getClass().getSimpleName() + " " + e.getId());
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
		{
			System.out.println(e + " has Trigger "
					+ ((TransitionModel) e).hasTrigger());
			System.out.println(e + " has Resource "
					+ ((TransitionModel) e).hasResource());
			if (((TransitionModel) e).hasTrigger())
			{
				TriggerModel m = ((TransitionModel) e).getToolSpecific()
						.getTrigger();
				System.out.println("code-Nr: " + m.getTriggertype());
				if (m.getTriggertype() == TriggerModel.TRIGGER_TIME)
					return new TriggerTransition((TransitionModel) e);
				if (m.getTriggertype() == TriggerModel.TRIGGER_RESOURCE)
					return new ResourceTransition((TransitionModel) e);
				if (m.getTriggertype() == TriggerModel.TRIGGER_TIME)
					return new MessageTransition((TransitionModel) e);
			}
			return new SimpleTransition((TransitionModel) e);
		}
		// System.out
		// .println("cant find the right object to generate the correct
		// AbstractElement!");
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
	 * This method generate the complete Bpel code and reduced the model.
	 * 
	 * @return String
	 */
	public String generate_bpel()
	{
		int counter = this.count_elements();
		this.toString();
		while (this.count_elements() != 3 && counter > 0)
		{
			this.eliminate_all_picks();
			// this.eliminate_all_sequences();
			this.eliminate_all_flows();
			this.eliminate_all_ifs();
			counter--;
			System.out.println("Durchlauf " + counter + "\nAnzahl Elemente "
					+ this.count_elements());
		}
		System.out.println(this.toString());

		return this._regist_transition.iterator().next().getBpelCode();
	}

	/**
	 * This method test of sequens and return the endelement of sequence.
	 * 
	 * @param e
	 *            AbstractElement startelement of sequence
	 * @return AbstractElement
	 */
	public AbstractElement isSequense(AbstractElement e)
	{
		if (e == null)
			return null;
		if (Place.class.isInstance(e))
			return null;
		if (XORSplitTransition.class.isInstance(e)
				|| XORJoinTransition.class.isInstance(e)
				|| ANDSplitTransition.class.isInstance(e)
				|| ANDJoinTransition.class.isInstance(e))
			return null;
		if (e.count_post_objects() != 1)
			return null;
		AbstractElement tmp = e.get_first_post_element();
		if (!Place.class.isInstance(tmp))
			return null;
		if (tmp.count_post_objects() != 1)
			return null;
		if (tmp.count_pre_objects() != 1)
			return null;
		tmp = tmp.get_first_post_element();
		if (tmp.count_pre_objects() != 1)
			return null;
		if (XORSplitTransition.class.isInstance(tmp)
				|| XORJoinTransition.class.isInstance(tmp)
				|| ANDSplitTransition.class.isInstance(tmp)
				|| ANDJoinTransition.class.isInstance(tmp))
			return null;
		return tmp;
	}

	/**
	 * This method eliminate all sequences and replace all contained elements
	 * the sequence as the SequenceTransition element.
	 */
	public void eliminate_all_sequences()
	{
		Iterator<AbstractElement> list = this.get_copy_of_regist_transition()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = this.isSequense(begin);
			if (end != null)
			{
				System.out.println("<Sequense> \n" + "\tbegin = " + begin
						+ "\n" + "\tend = " + end + "\n</Sequence>");
				HashSet<AbstractElement> pre_list = begin.get_pre_list_copy();
				HashSet<AbstractElement> post_list = end.get_post_list_copy();

				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();

				AbstractElement e = new SequenceTransition(begin);
				this.regist_element(e);
				this.reg_to_deregist_submodel(begin);

				e.add_pre_object_list_relationship(pre_list);
				e.add_post_object_list_relationship(post_list);
			}
		}
		this.deregist_all_flaged_elements();
	}

	/**
	 * This method test of pick and search for the endelement.
	 * 
	 * @param e
	 *            AbstractElement begin of pick
	 * @return AbstractElement
	 */
	public AbstractElement isSimplePick(AbstractElement e)
	{
		if (e == null)
			return null;
		if (!Place.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;
		AbstractElement end = null;
		AbstractElement tmp;
		Iterator<AbstractElement> list = e.get_all_post_objects().iterator();
		boolean firstrun = true;
		boolean hastrigger = false;
		while (list.hasNext())
		{
			tmp = list.next();
			if (!TriggerTransition.class.isInstance(tmp)
					&& !ResourceTransition.class.isInstance(tmp)
					&& !MessageTransition.class.isInstance(tmp))
				return null;
			if (TriggerTransition.class.isInstance(tmp) && hastrigger)
				return null;
			else
				hastrigger = true;
			tmp = tmp.get_first_post_element();

			if (firstrun)
			{
				end = tmp;
				firstrun = false;
			} else if (!end.equals(tmp))
				return null;
		}
		return end;
	}

	/**
	 * This method test of pick and search for the endelement.
	 * 
	 * @param e
	 *            AbstractElement begin of pick
	 * @return AbstractElement
	 */
	public AbstractElement isPick(AbstractElement e)
	{
		if (e == null)
			return null;
		if (!Place.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;
		AbstractElement end = null;
		AbstractElement tmp;
		Iterator<AbstractElement> list = e.get_all_post_objects().iterator();
		boolean firstrun = true;
		boolean hastrigger = false;
		while (list.hasNext())
		{
			tmp = list.next();
			
			//test 1, element
			if (!TriggerTransition.class.isInstance(tmp)
					&& !ResourceTransition.class.isInstance(tmp)
					&& !MessageTransition.class.isInstance(tmp))
				return null;
			if (TriggerTransition.class.isInstance(tmp) && hastrigger)
				return null;
			else
				hastrigger = true;
			if (tmp.count_post_objects() != 1)
				return null;
			if (tmp.count_pre_objects() != 1)
				return null;
			//test 2. element, it is a place
			tmp = tmp.get_first_post_element();
			if (tmp.count_post_objects() != 1)
				return null;
			if (tmp.count_pre_objects() != 1)
				return null;
			// test 3. element
			tmp = tmp.get_first_post_element();
			if (TriggerTransition.class.isInstance(tmp)
					&& ResourceTransition.class.isInstance(tmp)
					&& MessageTransition.class.isInstance(tmp))
				return null;
			if (tmp.count_post_objects() != 1)
				return null;
			if (tmp.count_pre_objects() != 1)
				return null;
			//test endelement
			tmp = tmp.get_first_post_element();
			if (firstrun)
			{
				end = tmp;
				firstrun = false;
			} else if (!end.equals(tmp))
				return null;
		}
		return end;
	}

	/**
	 * This method eliminate all picks and replace all contained elements the
	 * picks as the PickTransition element.
	 */
	public void eliminate_all_picks()
	{
		Iterator<AbstractElement> list = this.get_copy_of_regist_places()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = null;
			if ((end = this.isSimplePick(begin)) == null)
				end = this.isPick(begin);
			if (end != null)
			{
				System.out.println("<Pick> \n" + "\tbegin = " + begin + "\n"
						+ "\tend = " + end + "\n</Pick>");

				AbstractElement e = new PickTransition(begin);
				this.regist_element(e);

				Iterator<AbstractElement> deregist = begin.get_post_list_copy()
						.iterator();
				begin.remove_all_post_relationship();
				begin.add_post_object(e);
				e.add_pre_object(begin);

				end.remove_all_pre_relationship();
				end.add_pre_object(e);
				e.add_post_object(end);

				while (deregist.hasNext())
					this.reg_to_deregist_submodel(deregist.next());
			}
		}
		this.deregist_all_flaged_elements();
	}

	/**
	 * This method test of flow and search for the endelement.
	 * 
	 * @param e
	 *            AbstractElement begin of flow
	 * @return AbstractElement
	 */
	public AbstractElement isFlow(AbstractElement e)
	{
		// Work not right
		if (e == null)
			return null;
		if (!ANDSplitTransition.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;
		AbstractElement end = null;
		AbstractElement tmp = null;
		Iterator<AbstractElement> list = e.get_all_post_objects().iterator();
		tmp = list.next();
		boolean firstrun = true;
		while (list.hasNext())
		{
			tmp = list.next();
			for (int i = 0; i < 3; i++)
			{
				if (tmp.count_post_objects() != 1)
					return null;
				if (tmp.count_pre_objects() != 1)
					return null;
				tmp = tmp.get_first_post_element();
			}
			if (firstrun)
			{
				if (!ANDJoinTransition.class.isInstance(tmp))
					return null;
				end = tmp;
				firstrun = false;
			} else
			{
				if (!end.equals(tmp))
					return null;
			}
		}
		return tmp;
	}

	/**
	 * This method eliminate all flows and replace all contained elements the
	 * flows as the FlowTransition element.
	 */
	public void eliminate_all_flows()
	{
		Iterator<AbstractElement> list = this.get_copy_of_regist_transition()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = this.isFlow(begin);
			if (end != null)
			{
				System.out.println("<Flow> \n" + "\tbegin = " + begin + "\n"
						+ "\tend = " + end + "\n</Flow>");
				Iterator<AbstractElement> pre_list = begin.get_pre_list_copy()
						.iterator();
				Iterator<AbstractElement> post_list = end.get_post_list_copy()
						.iterator();

				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();

				AbstractElement e = new FlowTransition(begin);
				this.regist_element(e);
				this.reg_to_deregist_submodel(begin);
				AbstractElement tmp;
				while (pre_list.hasNext())
				{
					tmp = pre_list.next();
					tmp.add_post_object(e);
					e.add_pre_object(tmp);
				}
				while (post_list.hasNext())
				{
					tmp = post_list.next();
					tmp.add_pre_object(e);
					e.add_post_object(tmp);
				}
			}
		}
		this.deregist_all_flaged_elements();
	}

	/**
	 * This method test of if and search for the endelement.
	 * 
	 * @param e
	 *            AbstractElement begin of if
	 * @return AbstractElement
	 */
	public AbstractElement isIf(AbstractElement e)
	{
		// Work not right
		if (e == null)
			return null;
		if (!XORSplitTransition.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;
		AbstractElement end = null;
		AbstractElement tmp = null;
		Iterator<AbstractElement> list = e.get_all_post_objects().iterator();
		tmp = list.next();
		boolean firstrun = true;
		while (list.hasNext())
		{
			tmp = list.next();
			for (int i = 0; i < 3; i++)
			{
				if (tmp.count_post_objects() != 1)
					return null;
				if (tmp.count_pre_objects() != 1)
					return null;
				tmp = tmp.get_first_post_element();
			}
			if (firstrun)
			{
				if (!XORJoinTransition.class.isInstance(tmp))
					return null;
				end = tmp;
				firstrun = false;
			} else
			{
				if (!end.equals(tmp))
					return null;
			}
		}
		return tmp;
	}

	/**
	 * This method eliminate all ifs and replace all contained elements the
	 * flows as the IfTransition element.
	 */
	public void eliminate_all_ifs()
	{
		Iterator<AbstractElement> list = this.get_copy_of_regist_transition()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement begin = list.next();
			AbstractElement end = this.isIf(begin);
			if (end != null)
			{
				System.out.println("<If> \n" + "\tbegin = " + begin + "\n"
						+ "\tend = " + end + "\n</If>");
				Iterator<AbstractElement> pre_list = begin.get_pre_list_copy()
						.iterator();
				Iterator<AbstractElement> post_list = end.get_post_list_copy()
						.iterator();

				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();

				AbstractElement e = new IfTransition(begin);
				this.regist_element(e);
				this.reg_to_deregist_submodel(begin);
				AbstractElement tmp;
				while (pre_list.hasNext())
				{
					tmp = pre_list.next();
					tmp.add_post_object(e);
					e.add_pre_object(tmp);
				}
				while (post_list.hasNext())
				{
					tmp = post_list.next();
					tmp.add_pre_object(e);
					e.add_post_object(tmp);
				}
			}
		}
		this.deregist_all_flaged_elements();
	}
}
