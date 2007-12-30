package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;

/**
 * @author Frank SChüler
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
	public boolean equals(AbstractElement e)
	{
		if(!ANDJoinTransition.class.isInstance(e))return false;
		if(this.getData().getId() != ((ANDJoinTransition)e).getData().getId()) return false;
		return true;
	}

	/**
	 * Returned the Bpel code at this object
	 * 
	 * @return String
	 */
	public String getBpelCode()
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
