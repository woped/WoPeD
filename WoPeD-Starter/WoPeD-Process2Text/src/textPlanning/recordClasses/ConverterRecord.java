package textPlanning.recordClasses;

import java.util.ArrayList;

import dataModel.dsynt.DSynTSentence;
import dataModel.intermediate.ConditionFragment;

public class ConverterRecord {
	
	public ArrayList<DSynTSentence> preStatements;
	public ArrayList<DSynTSentence> postStatements;
	public ConditionFragment pre;
	public ConditionFragment post;
	public ModifierRecord mod;
	
	public ConverterRecord(ConditionFragment pre, ConditionFragment post, ArrayList<DSynTSentence> preStatements, ArrayList<DSynTSentence> postStatements) {
		this.pre = pre;
		this.post = post;
		this.preStatements = preStatements;
		this.postStatements = postStatements;
	}
	
	public ConverterRecord(ConditionFragment pre, ConditionFragment post, ArrayList<DSynTSentence> preStatements, ArrayList<DSynTSentence> postStatements, ModifierRecord mod) {
		this.pre = pre;
		this.post = post;
		this.preStatements = preStatements;
		this.postStatements = postStatements;
		this.mod = mod;
	}
	
	public void addMod(ModifierRecord mod) {
		this.mod = mod;
	}
	
	public boolean hasPreStatements() {
		return preStatements != null;
	}
	
	public boolean hasPostStatements() {
		return postStatements != null;
	}
	
	public boolean hasPreFragment() {
		return pre != null;
	}
	
	public boolean hasPostFragment() {
		return post != null;
	}
	
	public boolean hasMod() {
		return mod != null;
	}
}
