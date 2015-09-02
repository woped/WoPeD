package contentDetermination.labelAnalysis.structure;

import java.util.ArrayList;

public class ActivityRecord {

	/**
	 * Defines a record of connected activity components. 
	 * All relevant combinations are possible: 1 A: N BOs;
	 * 1 BO: N As; 1 A: 1 BO. Addition can be added if necessary.
	 */
	
	private ArrayList <String> actions;
	private ArrayList <String> businessObjects;
	private String addition;
	
	public ActivityRecord(ArrayList<String> actions, ArrayList<String> businessObjects, String addition) {
		this.actions = actions;
		this.businessObjects = businessObjects;
		this.addition = addition;
	}

	public ArrayList<String> getActions() {
		return actions;
	}

	public ArrayList<String> getBusinessObjects() {
		return businessObjects;
	}

	public String getAddition() {
		return addition;
	}
	
	public String toString() {
		return "A: " + actions + "; BO: " + businessObjects + "; ADD: " + addition;
	}
	
	
}
