package dataModel.intermediate;

import java.util.ArrayList;
import java.util.HashMap;

import textPlanning.recordClasses.ModifierRecord;



public class ExecutableFragment extends AbstractFragment {
	
	private ArrayList<ExecutableFragment> sentenceList;
	
	public ExecutableFragment(String action, String bo, String role, String addition) {
		super(action, bo, role, addition);
		sentenceList = new ArrayList<ExecutableFragment>();
	}
	
	public ExecutableFragment(String action, String bo, String role, String addition, HashMap<String, ModifierRecord> modList) {
		super(action, bo, role, addition, modList);
		sentenceList = new ArrayList<ExecutableFragment>();
	}
	
	public ArrayList<ExecutableFragment>getSentencList() {
		return sentenceList;
	}
	
	public void addSentence(ExecutableFragment eFrag) {
		this.sentenceList.add(eFrag);
	}
	
	public int getListSize() {
		return sentenceList.size();
	}
}

