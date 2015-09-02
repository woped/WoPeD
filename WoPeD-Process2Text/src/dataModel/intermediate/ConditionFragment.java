package dataModel.intermediate;

import java.util.ArrayList;
import java.util.HashMap;

import textPlanning.recordClasses.ModifierRecord;



public class ConditionFragment extends AbstractFragment {

	public static final int TYPE_IF = 0;
	public static final int TYPE_AS_LONG_AS = 1;
	public static final int TYPE_ONCE = 2;
	public static final int TYPE_WHETHER = 3;
	public static final int TYPE_NONE = 4;
	public static final int TYPE_WHEN = 5;
	public static final int TYPE_IN_CASE = 6;

	public boolean sen_headPosition = true;
	public int type;
	
	private ArrayList<ConditionFragment> sentenceList = new ArrayList<ConditionFragment>();
	
	public ArrayList<ConditionFragment> getSentenceList() {
		return sentenceList;
	}
	
	public void addCondition(ConditionFragment c) {
		sentenceList.add(c);
	}
	
	public ConditionFragment(String action, String bo, String role, String addition, int type) {
		super(action, bo, role, addition);
		this.type = type;
	}
	
	public ConditionFragment(String action, String bo, String role, String addition, int type, HashMap<String, ModifierRecord> modList) {
		super(action, bo, role, addition, modList);
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
