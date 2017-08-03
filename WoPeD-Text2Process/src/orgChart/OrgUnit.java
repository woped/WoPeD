/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import epc.SequenceFlow;

public class OrgUnit extends OrgChartElement {

	/**
	 * 
	 */
	public OrgUnit() {
		super();
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
