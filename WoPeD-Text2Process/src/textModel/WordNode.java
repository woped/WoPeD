/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package textModel;

import epc.SequenceFlow;
import nodes.ProcessNode;


public class WordNode extends ProcessNode {
	
	

	/**
	 * 
	 */
	public WordNode(String word) {
		setText(word.replaceAll("\\/", "/"));
	}

	/**
	 * 
	 */
	public WordNode() {
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @return
	 */
	public String getWord() {
		return getText();
	}
	
	@Override
	public String toString() {
		return "WordNode ("+getText()+")";
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
