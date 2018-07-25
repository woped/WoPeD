package contentDetermination.labelAnalysis;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

import java.util.Arrays;

public class EnglishLabelDeriver {
    private final Dictionary wordnet;
    private final EnglishLabelHelper lHelper;

    /**
     * Constructor
     */
    public EnglishLabelDeriver(EnglishLabelHelper lHelper) {
        this.wordnet = lHelper.getDictionary();
        this.lHelper = lHelper;
    }

    /**
     * Derives action / business object from verb-object label.
     *
     * @param label      action-noun label
     * @param labelSplit split action-noun label
     * @param props      property object
     */
    public void deriveFromVOS(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException {
        // Check for phrasal verb
        if (labelSplit.length > 1) {
            if (checkForPhrasalVerb(lHelper.getInfinitiveOfAction(labelSplit[0]) + " " + labelSplit[1])) {
                props.setHasPhrasalVerb(true);
            }
        }

        // Check for conjunction
        if (label.contains(" and ") || label.contains("/")) {
            for (int i = 1; i < labelSplit.length; i++) {
                if (labelSplit[i].equals("and") || labelSplit[i].equals("/")) {
                    props.setHasConjunction(true);
                    props.setIndexConjunctionSplit(i);
                }
            }
        }

        boolean assigned = false;

        // A1 <AND> A2 BO (A2 is also given as imperative verb)
        if (!props.hasPhrasalVerb() && props.hasConjunction()) {
            props.setAction(labelSplit[0] + " and " + labelSplit[props.getIndexConjunctionSplit() + 1]);

            props.addToMultipleActions(labelSplit[0]);
            props.addToMultipleActions(labelSplit[props.getIndexConjunctionSplit() + 1]);

            // First BO
            StringBuilder temp = new StringBuilder();
            for (int j = 1; j <= props.getIndexConjunctionSplit() - 1; j++) {
                temp.append(" ").append(labelSplit[j]);
            }
            temp = new StringBuilder(temp.toString().trim());
            props.addBOs(temp.toString());

            // Second BO
            temp = new StringBuilder();
            for (int j = props.getIndexConjunctionSplit() + 2; j <= labelSplit.length - 1; j++) {
                temp.append(" ").append(labelSplit[j]);
            }
            temp = new StringBuilder(temp.toString().trim());
            props.addBOs(temp.toString());

            assigned = true;
        }

        // If label only contains a single action
        if (!assigned) {
            props.setHasConjunction(false);
        }

        if (props.hasPhrasalVerb() && !props.hasConjunction()) {
            props.setAction(labelSplit[0] + " " + labelSplit[1]);
            for (int j = 2; j <= labelSplit.length - 1; j++) {
                props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
            }
        }
        if (!props.hasPhrasalVerb() && !props.hasConjunction()) {
            props.setAction(labelSplit[0]);
            for (int j = 1; j <= labelSplit.length - 1; j++) {
                props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
            }
        }

        props.setBusinessObject(props.getBusinessObject().trim());

        // Separate addition
        String[] boSplit = props.getBusinessObject().split(" ");
        props.setBusinessObject("");
        int temp = -1;
        for (int j = 0; j <= boSplit.length - 1; j++) {
            if (props.getPrepositions().contains(boSplit[j])) {
                temp = j;
                break;
            }
            props.setBusinessObject(props.getBusinessObject() + " " + boSplit[j]);
        }
        props.setBusinessObject(props.getBusinessObject().trim());

        if (temp > -1) {
            for (int j = temp; j <= boSplit.length - 1; j++) {
                props.setAdditionalInfo(props.getAdditionalInfo() + " " + boSplit[j]);
            }
            props.setAdditionalInfo(props.getAdditionalInfo().trim());
        }
    }

    /**
     * Returns true if given word is a two word verb
     *
     * @param word considered verb which might contain 2 words
     * @return true if given word is a phrasal verb
     */
    private boolean checkForPhrasalVerb(String word) throws JWNLException {
        IndexWord indexWord = wordnet.getIndexWord(POS.VERB, word);
        if (indexWord != null) {
            return !Arrays.asList(EnglishLabelProperties.elements).contains(word.split(" ")[1]);
        } else {
            return false;
        }
    }
}