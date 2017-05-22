/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import epc.SequenceFlow;

public class Role extends OrgChartElement {

	@Override
	public void setIncoming(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

	@Override
	public void setOutgoing(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}


}
