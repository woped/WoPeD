package org.woped.bpel.datamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;

/**
 * @author Frank Schueler
 * 
 * This class represents the model a petrinet.
 */
public class BpelParserModel
{

	private static long					MODELCOUNTER		= 0;

	private long						_id					= MODELCOUNTER++;
	private AbstractElement<?>			_oneelement			= null;
	private HashSet<AbstractElement<?>>	_regist_places		= new HashSet<AbstractElement<?>>();
	private HashSet<AbstractElement<?>>	_regist_transition	= new HashSet<AbstractElement<?>>();
	private Stack<AbstractElement<?>>	_reg_to_deregist	= new Stack<AbstractElement<?>>();

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
	 * Retuned the ID of the Bpel parser model.
	 * 
	 * @return long
	 */
	public long getID()
	{
		return this._id;
	}

	/**
	 * Retured a copy registet places list.
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement<?>> get_copy_of_regist_places()
	{
		HashSet<AbstractElement<?>> copy = new HashSet<AbstractElement<?>>();
		Iterator<AbstractElement<?>> list = this._regist_places.iterator();
		while (list.hasNext())
			copy.add(list.next());
		return copy;
	}

	/**
	 * Retured a copy registet transition list
	 * 
	 * @return HashSet<AbstractElement>
	 */
	public HashSet<AbstractElement<?>> get_copy_of_regist_transition()
	{
		HashSet<AbstractElement<?>> copy = new HashSet<AbstractElement<?>>();
		Iterator<AbstractElement<?>> list = this._regist_transition.iterator();
		while (list.hasNext())
			copy.add(list.next());
		return copy;
	}

	public String toString()
	{
		String erg = new String();
		Iterator<AbstractElement<?>> plist = this._regist_places.iterator();
		Iterator<AbstractElement<?>> tlist = this._regist_transition.iterator();
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
		if (container.getRootElements().isEmpty())
			return false;
		AbstractPetriNetElementModel e = (AbstractPetriNetElementModel) container
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
	private boolean createModel(AbstractPetriNetElementModel e,
			ModelElementContainer con)
	{
		
		if (this.get_registrated_element(e) != null)
		{
			return true;
		}
		
		AbstractElement<?> element = BpelParserModel.createElement(e);
		if (element == null)
			return false;
		if (this._oneelement == null)
			this._oneelement = element;
		this.regist_element(element);

		Map<String, AbstractPetriNetElementModel> map = con
				.getSourceElements(e.getId());
		Collection<AbstractPetriNetElementModel> list = map.values();
		Iterator<AbstractPetriNetElementModel> iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractPetriNetElementModel tmp = iter.next();
			if (AbstractPetriNetElementModel.class.isInstance(tmp))
			{
				if (!this.createModel(tmp, con))
					return false;
				AbstractElement<?> abs = this
						.get_registrated_element(tmp);

				if (abs == null)
					return false;
				if (!abs.add_post_object(element))
					return false;
				if (!element.add_pre_object(abs))
					return false;
			}
		}

		list = con.getTargetElements(e.getId()).values();
		iter = list.iterator();
		while (iter.hasNext())
		{
			AbstractPetriNetElementModel tmp = iter.next();
			if (AbstractPetriNetElementModel.class.isInstance(tmp))
			{
				if (!this.createModel(tmp, con))
					return false;
				AbstractElement<?> abs = this
						.get_registrated_element(tmp);

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
	private void regist_element(AbstractElement<?> e)
	{
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
	private void deregist_element(AbstractElement<?> e)
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
	@SuppressWarnings("unused")
	private void deregist_submodel(AbstractElement<?> e)
	{
		if (this.get_registrated_element(e) == null)
			return;
		this.deregist_element(e);
		Iterator<AbstractElement<?>> pre = e.get_all_pre_objects().iterator();
		Iterator<AbstractElement<?>> post = e.get_all_post_objects().iterator();

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
	private void regist_to_deregist(AbstractElement<?> e)
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

	private void reg_to_deregist_submodel(AbstractElement<?> e)
	{
		if (this._reg_to_deregist.search(e) != -1)
			return;
		this.regist_to_deregist(e);
		Iterator<AbstractElement<?>> pre = e.get_all_pre_objects().iterator();
		Iterator<AbstractElement<?>> post = e.get_all_post_objects().iterator();

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
	public AbstractElement<?> get_registrated_element(AbstractPetriNetElementModel e)
	{
		AbstractElement<?> ae = BpelParserModel.createElement(e);
		AbstractElement<?> result = this.get_registrated_element(ae);
		return result;
	}

	/**
	 * With this method you can find an element at the model.
	 * 
	 * @param e
	 *            AbstractElement
	 * 
	 * @return AbstractElement
	 */
	public AbstractElement<?> get_registrated_element(AbstractElement<?> e)
	{
		if (Place.class.isInstance(e))
		{
			Iterator<AbstractElement<?>> iter = this._regist_places.iterator();
			while (iter.hasNext())
			{
				AbstractElement<?> erg = iter.next();
				String erg_id = ((PlaceModel)(erg.getData())).getId();
				String e_id = ((PlaceModel)(e.getData())).getId();
				
				if (erg_id.equals(e_id))
				{
					return erg;
				}
			}
		} else
		{
			Iterator<AbstractElement<?>> iter = this._regist_transition
					.iterator();
			while (iter.hasNext())
			{
				AbstractElement<?> erg = iter.next();
				String erg_id = ((TransitionModel)(erg.getData())).getId();
				String e_id = ((TransitionModel)(e.getData())).getId();
				
				if (erg_id.equals(e_id))
				{
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
	private static AbstractElement<?> createElement(AbstractPetriNetElementModel e)
	{
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
			if (((TransitionModel) e).hasTrigger())
			{
				TriggerModel m = ((TransitionModel) e).getToolSpecific()
						.getTrigger();
				if (m.getTriggertype() == TriggerModel.TRIGGER_TIME)
					return new TimeTriggerTransition((TransitionModel) e);
				// if (m.getTriggertype() == TriggerModel.TRIGGER_RESOURCE)
				// return new ResourceTriggerTransition((TransitionModel) e);
				if (m.getTriggertype() == TriggerModel.TRIGGER_MESSAGE)
					return new MessageTriggerTransition((TransitionModel) e);
			}
			return new SimpleTransition((TransitionModel) e);
		}
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
	public TProcess generate_bpel()
	{
		while (true)
		{
			int pre = this.count_elements();
			this.eliminate_all_while();
			this.eliminate_all_picks();
			this.eliminate_all_sequences();
			this.eliminate_all_flows();
			this.eliminate_all_ifs();
			if (pre == this.count_elements())
				break;
		}

		if (this._regist_transition.size() != 1)
			return null;

		TExtensibleElements test = this._regist_transition.iterator().next()
				.getBpelCode();
		TProcess p = TProcess.Factory.newInstance();
		if (TSequence.class.isInstance(test))
			p.addNewSequence().set(test);
		else if (TPick.class.isInstance(test))
			p.addNewPick().set(test);
		else if (TIf.class.isInstance(test))
			p.addNewIf().set(test);
		else if (TAssign.class.isInstance(test))
			p.addNewAssign().set(test);
		else if (TFlow.class.isInstance(test))
			p.addNewFlow().set(test);
		else if (TEmpty.class.isInstance(test))
			p.addNewEmpty().set(test);
		else if (TWait.class.isInstance(test))
			p.addNewWait().set(test);
		else if (TReceive.class.isInstance(test))
			p.addNewReceive().set(test);
		else if (TReply.class.isInstance(test))
			p.addNewReply().set(test);
		else if (TInvoke.class.isInstance(test))
			p.addNewInvoke().set(test);
		return p;
	}

	/**
	 * This method test of sequence and return the endelement of sequence.
	 * 
	 * @param e
	 *            AbstractElement startelement of sequence
	 * @return AbstractElement
	 */
	public AbstractElement<?> isSequence(AbstractElement<?> e)
	{
		if (e == null)
			return null;
		if (Place.class.isInstance(e))
			return null;
		if (XORSplitTransition.class.isInstance(e)
				|| XORJoinTransition.class.isInstance(e)
				|| ANDSplitTransition.class.isInstance(e)
				|| ANDJoinTransition.class.isInstance(e)
				|| TimeTriggerTransition.class.isInstance(e)
		// || MessageTriggerTransition.class.isInstance(e)
		// || ResourceTriggerTransition.class.isInstance(e)
		)
			return null;
		if (e.count_post_objects() != 1 && e.count_pre_objects() != 1)
			return null;
		AbstractElement<?> tmp = e.get_first_post_element();
		if (!Place.class.isInstance(tmp))
			return null;
		if (tmp.count_post_objects() != 1 || tmp.count_pre_objects() != 1)
			return null;
		tmp = tmp.get_first_post_element();
		if (tmp.count_pre_objects() != 1)
			return null;
		if (XORSplitTransition.class.isInstance(tmp)
				|| XORJoinTransition.class.isInstance(tmp)
				|| ANDSplitTransition.class.isInstance(tmp)
				|| ANDJoinTransition.class.isInstance(tmp)
				|| TimeTriggerTransition.class.isInstance(e)
		// || MessageTriggerTransition.class.isInstance(e)
		// || ResourceTriggerTransition.class.isInstance(e)
		)
			return null;
		return tmp;
	}

	/**
	 * This method eliminate all sequences and replace all contained elements
	 * the sequence as the SequenceTransition element.
	 */
	public void eliminate_all_sequences()
	{
		Iterator<AbstractElement<?>> list = this.get_copy_of_regist_places()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement<?> begin = list.next();
			if (begin.count_post_objects() == 1)
			{
				begin = begin.get_first_post_element();
				AbstractElement<?> end = this.isSequence(begin);
				if (end != null)
				{
					this.newSequence(begin, end);
					this.deregist_all_flaged_elements();
					break;
				}
			}
		}

	}

	public SequenceTransition newSequence(AbstractElement<?> begin,
			AbstractElement<?> end)
	{
		HashSet<AbstractElement<?>> pre_list = begin.get_pre_list_copy();
		HashSet<AbstractElement<?>> post_list = end.get_post_list_copy();

		begin.remove_all_pre_relationship();
		end.remove_all_post_relationship();

		SequenceTransition e = new SequenceTransition(begin);
		this.regist_element(e);
		this.reg_to_deregist_submodel(begin);

		e.add_pre_object_list_relationship(pre_list);
		e.add_post_object_list_relationship(post_list);
		return e;
	}

	/**
	 * This method test of pick and search for the endelement.
	 * 
	 * @param e
	 *            AbstractElement begin of pick
	 * @return AbstractElement
	 */
	public AbstractElement<?> isPick(AbstractElement<?> e)
	{
		// String test = "";
		if (e == null)
			return null;
		if (!Place.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;

		AbstractElement<?> end = null;
		AbstractElement<?> tmp = null;
		Iterator<AbstractElement<?>> list = e.get_all_post_objects().iterator();

		// test = test + "<pick" + e.getData() + ">\n";
		boolean timetrigger = false;
		while (list.hasNext())
		{
			tmp = list.next();
			// test = test + "<pick-Line>\n";
			// first element must be a trigger
			// if (!TimeTriggerTransition.class.isInstance(tmp)
			// && !ResourceTriggerTransition.class.isInstance(tmp)
			// && !MessageTriggerTransition.class.isInstance(tmp))
			// return null;

			if (TimeTriggerTransition.class.isInstance(tmp))
			{
				if (timetrigger)
					return null;
				else
					timetrigger = true;
			}

			tmp = this.testPickLine(tmp);

			if (tmp == null)
				return null;
			else if (end == null)
				end = tmp;
			else if (!end.equals(tmp))
				return null;
		}
		if (e.count_post_objects() != end.count_pre_objects())
			return null;
		return end;
	}

	/**
	 * 
	 * @param tmp
	 * @return
	 */
	public AbstractElement<?> testPickLine(AbstractElement<?> tmp)
	{
		if (tmp.count_post_objects() != 1)
			return null;
		if (tmp.count_pre_objects() != 1)
			return null;
		// test = test + "<Trigger " + tmp.getClass().getSimpleName() +
		// ">\n";

		// test 2. element, it is a place
		tmp = tmp.get_first_post_element();
		if (!Place.class.isInstance(tmp))
			return null;
		if (tmp.count_pre_objects() > 1)
			return tmp;// end of a pick line with only one transition
		if ((tmp.count_post_objects() != 1) || (tmp.count_pre_objects() != 1))
			return null;

		// test 3. element
		tmp = tmp.get_first_post_element();
		if ((tmp.count_post_objects() != 1) || (tmp.count_pre_objects() != 1))
			return null;
		// test = test + "<Transition " + tmp.getClass().getSimpleName() +
		// ">\n";

		// test endelement
		tmp = tmp.get_first_post_element();

		if (!Place.class.isInstance(tmp))
			return null;
		return tmp;
	}

	/**
	 * This method eliminate all picks and replace all contained elements the
	 * picks as the PickTransition element.
	 */
	public void eliminate_all_picks()
	{
		Iterator<AbstractElement<?>> list = this.get_copy_of_regist_places()
				.iterator();
		while (list.hasNext())
		{
			AbstractElement<?> begin = list.next();
			AbstractElement<?> end = null;
			/*
			 * if ((end = this.isSimplePick(begin)) == null) end =
			 * this.isPick(begin);
			 */
			end = this.isPick(begin);
			if (end != null)
			{
				AbstractElement<?> e = new PickTransition(begin
						.get_post_list_copy());
				this.regist_element(e);

				Iterator<AbstractElement<?>> deregist = begin
						.get_post_list_copy().iterator();
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
	public AbstractElement<?> isFlow(AbstractElement<?> e)
	{
		if (e == null)
			return null;
		if (!ANDSplitTransition.class.isInstance(e))
			return null;
		if (e.count_post_objects() < 2)
			return null;
		ANDJoinTransition end = null;
		ANDJoinTransition tmp = null;
		Iterator<AbstractElement<?>> list = e.get_all_post_objects().iterator();
		while (list.hasNext())
		{
			tmp = this.testFlowLine(list.next());

			// test end element
			if (tmp == null)
				return null;
			else if (end == null)
				end = tmp;
			else if (!end.equals(tmp))
				return null;
		}
		if (e.count_post_objects() != end.count_pre_objects())
			return null;
		return end;
	}

	/**
	 * 
	 * @param begin
	 * @return
	 */
	public ANDJoinTransition testFlowLine(AbstractElement<?> begin)
	{
		// test first element in flowline
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test second element in flowline
		begin = begin.get_first_post_element();
		if (ANDJoinTransition.class.isInstance(begin))
			return (ANDJoinTransition) begin;
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test 3. element of flowline
		begin = begin.get_first_post_element();
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test endelement of AndJoinTransition
		begin = begin.get_first_post_element();
		if (!ANDJoinTransition.class.isInstance(begin))
			return null;

		return (ANDJoinTransition) begin;
	}

	/**
	 * This method eliminate all flows and replace all contained elements the
	 * flows as the FlowTransition element.
	 */
	public void eliminate_all_flows()
	{
		Iterator<AbstractElement<?>> list = this
				.get_copy_of_regist_transition().iterator();
		while (list.hasNext())
		{
			AbstractElement<?> begin = list.next();
			AbstractElement<?> end = this.isFlow(begin);
			if (end != null)
			{
				Iterator<AbstractElement<?>> pre_list = begin
						.get_pre_list_copy().iterator();
				Iterator<AbstractElement<?>> post_list = end
						.get_post_list_copy().iterator();

				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();

				AbstractElement<?> e = new FlowTransition(begin);
				this.regist_element(e);
				this.reg_to_deregist_submodel(begin);
				AbstractElement<?> tmp;
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
	 * @param begin
	 *            AbstractElement begin of if
	 * @return AbstractElement
	 */
	public AbstractElement<?> isIf(AbstractElement<?> begin)
	{
		// Work not right
		if (begin == null)
			return null;
		if (!XORSplitTransition.class.isInstance(begin))
			return null;
		if (begin.count_post_objects() < 2)
			return null;
		XORJoinTransition end = null;
		XORJoinTransition tmp = null;
		Iterator<AbstractElement<?>> list = begin.get_all_post_objects()
				.iterator();
		while (list.hasNext())
		{
			tmp = this.testIFLine(list.next());

			if (tmp == null)
				return null;
			if (end == null)
				end = tmp;
			if (!end.equals(tmp))
				return null;
		}

		if (begin.count_post_objects() != end.count_pre_objects())
			return null;
		return end;
	}

	/**
	 * 
	 * @param begin
	 * @return
	 */
	public XORJoinTransition testIFLine(AbstractElement<?> begin)
	{
		// test first element in ifline
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test second element in ifline
		begin = begin.get_first_post_element();
		if (XORJoinTransition.class.isInstance(begin))
			return (XORJoinTransition) begin;
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test 3. element of ifline
		begin = begin.get_first_post_element();
		if (begin.count_post_objects() != 1 || begin.count_pre_objects() != 1)
			return null;

		// test endelement of XORJoinTransition
		begin = begin.get_first_post_element();
		if (!XORJoinTransition.class.isInstance(begin))
			return null;

		return (XORJoinTransition) begin;
	}

	/**
	 * This method eliminate all ifs and replace all contained elements the
	 * flows as the IfTransition element.
	 */
	public void eliminate_all_ifs()
	{
		Iterator<AbstractElement<?>> list = this
				.get_copy_of_regist_transition().iterator();
		while (list.hasNext())
		{
			AbstractElement<?> begin = list.next();
			AbstractElement<?> end = this.isIf(begin);
			if (end != null)
			{
				Iterator<AbstractElement<?>> pre_list = begin
						.get_pre_list_copy().iterator();
				Iterator<AbstractElement<?>> post_list = end
						.get_post_list_copy().iterator();

				begin.remove_all_pre_relationship();
				end.remove_all_post_relationship();

				AbstractElement<?> e = new IfTransition(begin);
				this.regist_element(e);
				this.reg_to_deregist_submodel(begin);
				AbstractElement<?> tmp;
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
	 * 
	 */
	public void eliminate_all_while()
	{
		Iterator<AbstractElement<?>> list = this
				.get_copy_of_regist_transition().iterator();
		while (list.hasNext())
		{
			AbstractElement<?> begin = list.next();
			AbstractElement<?> linebegin = this.isWhile(begin);
			if (linebegin != null)
			{
				AbstractElement<?> tempplace = linebegin
						.get_first_post_element().get_first_post_element();

				AbstractElement<?> temptrans = linebegin
						.get_first_post_element();
				temptrans.remove_all_post_relationship();
				tempplace.remove_pre_object(linebegin.get_first_post_element());
				tempplace.remove_all_post_relationship();

				HashSet<AbstractElement<?>> post = begin.get_post_list_copy();
				begin.remove_all_post_relationship();
				begin.remove_all_pre_relationship();
				begin.add_post_object(linebegin);

				post.remove(linebegin);
				WhileTransition trans = new WhileTransition(begin);
				tempplace.add_post_object(trans);
				trans.add_pre_object(tempplace);

				this.regist_element(trans);
				this.reg_to_deregist_submodel(begin);

				AbstractElement<?> tmp;
				Iterator<AbstractElement<?>> post_list = post.iterator();
				while (post_list.hasNext())
				{
					tmp = post_list.next();
					tmp.add_pre_object(trans);
					trans.add_post_object(tmp);
				}
			}
		}
		this.deregist_all_flaged_elements();
	}

	/**
	 * 
	 * @param begin
	 * @return
	 */
	public AbstractElement<?> isWhile(AbstractElement<?> begin)
	{
		if (!XORSplitTransition.class.isInstance(begin))
			return null;
		if (begin.count_pre_objects() != 1)
			return null;
		if (begin.count_post_objects() != 2)
			return null;
		XORSplitTransition whilehead = (XORSplitTransition) begin;
		Iterator<AbstractElement<?>> iter = whilehead.get_post_list_iterator();

		while (iter.hasNext())
		{
			AbstractElement<?> linebegin = iter.next();
			AbstractElement<?> end = null;
			end = this.testWhileLine(linebegin, whilehead);
			if (end != null)
				return linebegin;
		}
		return null;
	}

	/**
	 * 
	 * @param line
	 * @param whilehead
	 * @return
	 */
	public AbstractElement<?> testWhileLine(AbstractElement<?> line,
			XORSplitTransition whilehead)
	{
		// test first element in line
		if (line.count_post_objects() != 1 || line.count_pre_objects() != 1)
			return null;

		// test second element in line
		line = line.get_first_post_element();
		if (XORJoinTransition.class.isInstance(line))
			return null;
		if (line.count_post_objects() != 1 || line.count_pre_objects() != 1)
			return null;

		// test 3. element of line
		line = line.get_first_post_element();
		if (line.count_post_objects() != 1)
			return null;

		// test next element! it must be the whilehead
		if (!whilehead.equals(line.get_first_post_element()))
			return null;

		return line;
	}
}
