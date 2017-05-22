/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import epc.SequenceFlow;

public class Substitute extends OrgChartElement {
	
	/**
	 * 
	 */
	public Substitute() {
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
