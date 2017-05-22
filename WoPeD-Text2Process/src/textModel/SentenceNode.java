/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package textModel;

import java.util.ArrayList;

import epc.SequenceFlow;
import nodes.Cluster;
import nodes.ProcessNode;


public class SentenceNode extends Cluster {
	
	private ArrayList<WordNode> f_wordNodes = new ArrayList<WordNode>();
	private int f_index = 0;

	/**
	 * 
	 */
	public SentenceNode() {

	}
	
	/**
	 * @param index 
	 * 
	 */
	public SentenceNode(int index) {
		f_index = index;		
	}
	
	public void addWord(WordNode word){
		f_wordNodes .add(word);
		super.addProcessNode(word);
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return f_index;
	}
	
	@Override
	public void removeProcessNode(ProcessNode n) {
		//not possible its a build only model
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
