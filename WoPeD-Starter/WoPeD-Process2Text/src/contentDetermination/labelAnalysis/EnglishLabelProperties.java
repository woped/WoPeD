package contentDetermination.labelAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * LabelProperties captures a property record for a label.
 * @author Henrik Leopold
 *
 */

public class EnglishLabelProperties {
	
	public static final String[] elements = {"until", "after", "on", "of", "by", "for", "from", "to", "at", "with", "w/o", "without", "in", "that", "using", "via", "during"};
	public static final String[] articles = {"the", "a", "an"};
	
	private String action;							// String for Action
	private String additionalInfo;					// String for Addition
	private String businessObject;					// String for Business Object
	private int indexPrep;							// Index of preposition in label
	private int indexPrepSplit;						// Index of preposition in split label (index = 2 --> second word)
	private int indexOf;							// Index of preposition 'of'
	private int indexConjunctionSplit;				// Index of conjunction in split label (index = 2 --> second word) 
	private int indexConjunction;					// Index of conjunction in label
	private boolean isVerb;							// If first word is a verb (according Wordnet)
	private boolean hasPreposition;					// If label contains preposition
	private boolean hasPrepositionOf;				// If label contains preposition 'of'
	private boolean hasSuffixING;					// If first word in label has suffix 'ing'
	private boolean hasConjunction;					// If label contains conjunction e.g. 'and', ',', '&'
	private boolean hasPhrasalVerb; 				// If label contains a verb comprising two words
	private boolean isGerundStyle;					// If first word of label is a gerund and represents action in label
	private boolean isIrregularStyle;				// If label follows the irregular style
	private HashSet <String> actions;				// Set for action
	private ArrayList<String> multipleActions;		// List for multiple actions
	private ArrayList<String> multipleBOs;		// List for multiple actions
	private HashSet <String> prepositions;			// Set of prepositions
	
	public EnglishLabelProperties() {
		// Initialize variables
		actions = new HashSet <String>();
		multipleActions = new ArrayList<String>();
		multipleBOs = new ArrayList<String>();
		
		// Elements splitting Noun Phrase label and addition
		String[] elements = {"within", "into", "upon", "until", "on", "of", "for", "by", "from", "to", "at", "with", "w/o", "without", "in", "that", "using", "via", "during"};
		prepositions = new HashSet<String>(Arrays.asList(elements));
		
		this.initialize();
	}
	
	public void initialize() {
		actions.clear();
		multipleActions.clear();
		multipleBOs.clear();
		action = "";
		businessObject = "";
		additionalInfo = "";
		indexPrep = -1;
		indexPrepSplit = -1;
		indexOf = -1;
		indexConjunction = -1;
		indexConjunctionSplit = -1;
		isIrregularStyle = false;
		hasPreposition = false;
		hasPrepositionOf = false;
		hasSuffixING = false;
		hasConjunction = false;
		hasPhrasalVerb = false;
		isGerundStyle = false;	
	}
	
	public ArrayList<String> getMultipleBOs() {
		return multipleBOs;
	}

	public void addBOs(String bo) {
		this.multipleBOs.add(bo);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(String businessObject) {
		this.businessObject = businessObject;
	}

	public int getIndexPrep() {
		return indexPrep;
	}

	public void setIndexPrep(int indexPrep) {
		this.indexPrep = indexPrep;
	}

	public int getIndexPrepSplit() {
		return indexPrepSplit;
	}

	public void setIndexPrepSplit(int indexPrepSplit) {
		this.indexPrepSplit = indexPrepSplit;
	}

	public int getIndexOf() {
		return indexOf;
	}

	public void setIndexOf(int indexOf) {
		this.indexOf = indexOf;
	}

	public int getIndexConjunctionSplit() {
		return indexConjunctionSplit;
	}

	public void setIndexConjunctionSplit(int indexConjunctionSplit) {
		this.indexConjunctionSplit = indexConjunctionSplit;
	}

	public int getIndexConjunction() {
		return indexConjunction;
	}

	public void setIndexConjunction(int indexConjunction) {
		this.indexConjunction = indexConjunction;
	}

	public boolean isVerb() {
		return isVerb;
	}

	public void setVerb(boolean isVerb) {
		this.isVerb = isVerb;
	}

	public boolean hasPreposition() {
		return hasPreposition;
	}

	public void setHasPreposition(boolean hasPreposition) {
		this.hasPreposition = hasPreposition;
	}

	public boolean hasPrepositionOf() {
		return hasPrepositionOf;
	}

	public void setHasPrepositionOf(boolean hasPrepositionOf) {
		this.hasPrepositionOf = hasPrepositionOf;
	}

	public boolean hasSuffixING() {
		return hasSuffixING;
	}

	public void setHasSuffixING(boolean hasSuffixING) {
		this.hasSuffixING = hasSuffixING;
	}

	public boolean hasConjunction() {
		return hasConjunction;
	}

	public void setHasConjunction(boolean hasConjunction) {
		this.hasConjunction = hasConjunction;
	}

	public boolean hasPhrasalVerb() {
		return hasPhrasalVerb;
	}

	public void setHasPhrasalVerb(boolean hasPhrasalVerb) {
		this.hasPhrasalVerb = hasPhrasalVerb;
	}

	public boolean isGerundStyle() {
		return isGerundStyle;
	}

	public void setGerundStyle(boolean isGerundStyle) {
		this.isGerundStyle = isGerundStyle;
	}

	public boolean isIrregularStyle() {
		return isIrregularStyle;
	}

	public void setIrregularStyle(boolean isIrregularStyle) {
		this.isIrregularStyle = isIrregularStyle;
	}

	public HashSet<String> getActions() {
		return actions;
	}

	public void setActions(HashSet<String> actions) {
		this.actions = actions;
	}
	
	public void addToActions(String toBeAdded) {
		this.actions.add(toBeAdded);
	}

	public ArrayList<String> getMultipleActions() {
		return multipleActions;
	}

	public void setMultipleActions(ArrayList<String> multipleActions) {
		this.multipleActions = multipleActions;
	}
	
	public void addToMultipleActions(String toBeAdded) {
		this.multipleActions.add(toBeAdded);
	}
	
	public HashSet <String> getPrepositions() {
		return prepositions;
	}
}
