/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;

import nodes.ProcessNode;


public abstract class OrgChartElement extends ProcessNode {

	
	private boolean f_hasLine = false;
	
	/**
	 * @return the f_hasLine
	 */
	public boolean hasLine() {
		return f_hasLine;
	}

	/**
	 * @param line the f_hasLine to set
	 */
	public void setHasLine(boolean line) {
		f_hasLine = line;
	}

	
	/**
	 * 
	 */
	public OrgChartElement() {
	}

}
