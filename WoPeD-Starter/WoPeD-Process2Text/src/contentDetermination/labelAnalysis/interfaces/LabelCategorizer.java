package contentDetermination.labelAnalysis.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import contentDetermination.labelAnalysis.structure.Activity;

public interface LabelCategorizer {
	
	/**
	 * Returns the label style of the given activity / model collection.
	 * Possible results (English models): 'AN', 'VO' 
	 * Possible results (Dutch models): 'AN', 'AN (first)', 'VO', 'VO (inf)', 'OI' 
	 */
	public String getLabelStyle(Activity activity);
	public HashMap<String,String> getLabelStyle(ArrayList<ArrayList<Activity>> modelCollection);

}
