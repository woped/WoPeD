package org.woped.p2t.contentDetermination.labelAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * LabelProperties captures a property record for a label.
 *
 * @author Henrik Leopold
 */
public class EnglishLabelProperties {
    public static final String[] elements = {"until", "after", "on", "of", "by", "for", "from", "to", "at", "with", "w/o", "without", "in", "that", "using", "via", "during"};
    private final ArrayList<String> multipleActions;        // List for multiple actions
    private final ArrayList<String> multipleBOs;        // List for multiple actions
    private final HashSet<String> prepositions;            // Set of prepositions
    private String action;                            // String for Action
    private String additionalInfo;                    // String for Addition
    private String businessObject;                    // String for Business Object
    private int indexConjunctionSplit;                // Index of conjunction in split label (index = 2 --> second word)
    private boolean hasConjunction;                    // If label contains conjunction e.g. 'and', ',', '&'
    private boolean hasPhrasalVerb;                // If label contains a verb comprising two words
    private final HashSet<String> actions;                // Set for action

    public EnglishLabelProperties() {
        // Initialize variables
        actions = new HashSet<>();
        multipleActions = new ArrayList<>();
        multipleBOs = new ArrayList<>();

        // Elements splitting Noun Phrase label and addition
        String[] elements = {"within", "into", "upon", "until", "on", "of", "for", "by", "from", "to", "at", "with", "w/o", "without", "in", "that", "using", "via", "during"};
        prepositions = new HashSet<>(Arrays.asList(elements));

        this.initialize();
    }

    private void initialize() {
        actions.clear();
        multipleActions.clear();
        multipleBOs.clear();
        action = "";
        businessObject = "";
        additionalInfo = "";
        indexConjunctionSplit = -1;
        hasConjunction = false;
        hasPhrasalVerb = false;
    }

    public ArrayList<String> getMultipleBOs() {
        return multipleBOs;
    }

    void addBOs(String bo) {
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

    void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getBusinessObject() {
        return businessObject;
    }

    void setBusinessObject(String businessObject) {
        this.businessObject = businessObject;
    }

    int getIndexConjunctionSplit() {
        return indexConjunctionSplit;
    }

    void setIndexConjunctionSplit(int indexConjunctionSplit) {
        this.indexConjunctionSplit = indexConjunctionSplit;
    }

    public boolean hasConjunction() {
        return hasConjunction;
    }

    void setHasConjunction(boolean hasConjunction) {
        this.hasConjunction = hasConjunction;
    }

    boolean hasPhrasalVerb() {
        return hasPhrasalVerb;
    }

    void setHasPhrasalVerb(boolean hasPhrasalVerb) {
        this.hasPhrasalVerb = hasPhrasalVerb;
    }

    public ArrayList<String> getMultipleActions() {
        return multipleActions;
    }

    void addToMultipleActions(String toBeAdded) {
        this.multipleActions.add(toBeAdded);
    }

    HashSet<String> getPrepositions() {
        return prepositions;
    }
}