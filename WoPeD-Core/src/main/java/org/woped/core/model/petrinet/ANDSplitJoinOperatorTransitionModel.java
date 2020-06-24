package org.woped.core.model.petrinet;

import org.woped.core.model.CreationMap;

@SuppressWarnings("serial")
public class ANDSplitJoinOperatorTransitionModel extends
		CombiOperatorTransitionModel {

	public ANDSplitJoinOperatorTransitionModel(CreationMap map) {
		super(map, AND_SPLITJOIN_TYPE);
	}

}
