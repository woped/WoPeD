package contentDetermination.labelAnalysis.interfaces;

import java.util.ArrayList;

import contentDetermination.labelAnalysis.structure.Activity;

public interface LabelDeriver {
	
	/**
	 * Investigates label and determines action and business object.
	 */
	public void processLabel(Activity label, String labelStyle); 

	/**
	 * Returns the computed action of the processed label.
	 */
	public ArrayList<String> returnActions();
	
	/**
	 * Returns the computed business object of the processed label.
	 */
	public ArrayList<String> returnBusinessObjects();
	
	/**
	 * Returns the computed addition.
	 */
	public String returnAddition();
	
}
