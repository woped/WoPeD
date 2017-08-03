/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import epc.SequenceFlow;

public class ManagerialRole extends OrgChartElement {

	/**
	 * 
	 */
	public ManagerialRole() {
		super();
		setHasLine(true);
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
