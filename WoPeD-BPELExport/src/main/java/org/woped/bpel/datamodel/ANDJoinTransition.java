package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;

/**
 * @author Frank Schüler
 *
 * This class reprasents the And-Join-Transition at a petrinet.
 * 
 * status: at work
 * date: 23.12.2007
 */
public class ANDJoinTransition extends
		Transition<ANDJoinOperatorTransitionModel>
{

	/**
	 * Constructor to create a object. Need a object of petrinet model. 
	 * @param data ANDJoinOperatorTransitionModel
	 */
	public ANDJoinTransition(ANDJoinOperatorTransitionModel data)
	{
		super(data);
	}

	/**
	 * This method test is a other AbstractElement equals to this object.
	 */
	public boolean equals(AbstractElement<?> e)
	{
		if (!ANDJoinTransition.class.isInstance(e))return false;
		if (!this.getData().getId().equals(((ANDJoinTransition)e).getData().getId())) return false;
		return true;
	}

	/**
	 * Returned the Bpel code at this object
	 * 
	 * @return String
	 */
	public TActivity getBpelCode()
	{
		return null;
	}
	
	/**
	 * Translate this object to a String
	 * 
	 * @return String
	 */
	public String toString()
	{
		return ANDJoinTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}
}
