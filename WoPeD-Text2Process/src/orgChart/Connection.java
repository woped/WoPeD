/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package orgChart;


import nodes.ProcessEdge;
import nodes.ProcessNode;

public class Connection extends ProcessEdge {

	
	/**
	 * 
	 */
	public Connection() {
		super();
	}
	
	/**
	 * @param source
	 * @param target
	 */
	public Connection(ProcessNode source, ProcessNode target) {
		super(source,target);
	}


}
