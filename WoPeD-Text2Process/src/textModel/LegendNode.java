/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package textModel;

import epc.SequenceFlow;
import nodes.ProcessNode;


 class LegendNode extends ProcessNode {
	
	/**
	 * 
	 */
	public LegendNode() {
	}

	@Override
	public void setIncoming(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

	@Override
	public void setOutgoing(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

}
