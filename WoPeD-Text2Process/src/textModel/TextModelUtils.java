/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package textModel;


import nodes.ProcessEdge;
import nodes.ProcessNode;
import processing.ProcessUtils;

public class TextModelUtils extends ProcessUtils {

	@Override
	public ProcessEdge createDefaultEdge(ProcessNode source, ProcessNode target) {
		return null;
	}

}
