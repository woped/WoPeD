package org.woped.p2t.contentDetermination.labelAnalysis;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;
import org.woped.p2t.contentDetermination.support.Distance;
import org.woped.p2t.contentDetermination.support.Noun2VerbTransformer;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class EnglishLabelHelper {
    private static final String[] verbs = {"sign off", "logon", "deallocate", "pick up", "top up", "postprocess", "downselect", "don't", "hand-in", "re-sort", "rescore", "overview", "repost", "rollup", "overrule", "pre-configure", "upsell", "pickup", "wrap-up", "up-sell", "cross-sell", "inventorise", "recheck", "intake", "login", "reroute", "lookup", "handover", "setup", "hand-over", "goto", "feedback", "pick-up", "rollback"};
    private Dictionary wordnet;

    public EnglishLabelHelper(String contextPath) throws JWNLException {
        // Initialize WordNet (JWNL)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("file_properties.xml");
        JWNL.initialize(input, contextPath);
        this.wordnet = Dictionary.getInstance();
    }

    /**
     * Function returning verb for given noun
     *
     * @param noun action given as a noun which has to be transformed in to a verb
     * @return verb (infinitive) derived from given noun
     */
    private String getVerbsFromNoun(String noun) {
        try {
            return Noun2VerbTransformer.toVerb(noun, wordnet);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Evaluates whether given word can be a verb
     *
     * @return true if the given word can be a verb
     */
    public boolean isVerb(String potVerb) {
        potVerb = potVerb.toLowerCase();
        for (String verb : verbs) {
            if (potVerb.equals(verb)) {
                return true;
            }
        }
        IndexWord word = null;
        try {
            word = wordnet.lookupAllIndexWords(potVerb).getIndexWord(POS.VERB);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return word != null;
    }

    /**
     * Evaluates whether given word can be an adjective
     *
     * @return true if the given word can be an adjective
     */
    public boolean isAdjective(String potAdj) {
        potAdj = potAdj.toLowerCase();

        IndexWord word = null;
        try {
            word = wordnet.lookupAllIndexWords(potAdj).getIndexWord(POS.ADJECTIVE);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return word != null;
    }

    /**
     * Evaluates whether given word can be an adjective
     *
     * @return true if the given word can be an adjective
     */
    public boolean isNoun(String potNoun) {
        potNoun = potNoun.toLowerCase();

        IndexWord word = null;
        try {
            word = wordnet.lookupAllIndexWords(potNoun).getIndexWord(POS.NOUN);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return word != null;
    }

    /**
     * Returns the infinitive of a potential Action (which might still be a noun)
     *
     * @param action action of label
     * @return infinitive of action
     */
    public String getInfinitiveOfAction(String action) {
        String inf = "";
        boolean mapped = false;

        // Add mappings for non-covered words
        if (action.equals("overview")) {
            inf = "overview";
            mapped = true;
        }
        if (action.equals("deallocate")) {
            inf = "deallocate";
            mapped = true;
        }
        if (action.equals("logon")) {
            inf = "logon";
            mapped = true;
        }
        if (action.equals("top up")) {
            inf = "top up";
            mapped = true;
        }
        if (action.equals("reposting")) {
            inf = "repost";
            mapped = true;
        }
        if (action.equals("postprocessing")) {
            inf = "postprocess";
            mapped = true;
        }
        if (action.equals("rollup")) {
            inf = "rollup";
            mapped = true;
        }
        if (action.equals("interfacing")) {
            inf = "interface";
            mapped = true;
        }
        if (action.equals("conversation")) {
            inf = "talk about";
            mapped = true;
        }
        if (action.equals("diagnostics")) {
            inf = "diagnose";
            mapped = true;
        }
        if (action.equals("check")) {
            inf = "check";
            mapped = true;
        }
        if (action.equals("admin")) {
            inf = "administrate";
            mapped = true;
        }
        if (action.equals("login")) {
            inf = "log in";
            mapped = true;
        }
        if (action.equals("intake")) {
            inf = "intake";
            mapped = true;
        }
        if (action.equals("creation")) {
            inf = "create";
            mapped = true;
        }
        if (action.equals("rebooking")) {
            inf = "rebook";
            mapped = true;
        }
        if (action.equals("rollup")) {
            inf = "roll up";
            mapped = true;
        }
        if (action.equals("reposting")) {
            inf = "repost";
            mapped = true;
        }
        if (action.equals("carryforward")) {
            inf = "carry forward";
            mapped = true;
        }
        if (action.equals("notification")) {
            inf = "notify";
            mapped = true;
        }
        if (action.equals("sale")) {
            inf = "sell";
            mapped = true;
        }
        if (action.equals("rescore")) {
            inf = "rescore";
            mapped = true;
        }
        if (action.equals("re-sort")) {
            inf = "re-sort";
            mapped = true;
        }
        if (action.equals("hand-in")) {
            inf = "hand-in";
            mapped = true;
        }
        if (action.equals("don't")) {
            inf = "don't";
            mapped = true;
        }
        if (action.equals("downselect")) {
            inf = "downselect";
            mapped = true;
        }
        if (action.equals("pickup")) {
            inf = "pickup";
            mapped = true;
        }
        if (action.equals("pick-up")) {
            inf = "pick-up";
            mapped = true;
        }
        if (action.equals("wrap-up")) {
            inf = "wrap-up";
            mapped = true;
        }

        // Standard procedure
        if (!mapped) {
            // go through each word in order to determine words that may represent an action
            // check if word is actually a verb
            if (isVerb(action)) {
                IndexWord iw = null;
                try {
                    iw = wordnet.lookupAllIndexWords(action).getIndexWord(POS.VERB);
                } catch (JWNLException ignored) {
                }
                if (iw != null) {
                    inf = iw.getLemma();
                }
            }

            // if no infinitive has been found
            if (inf.equals("")) {
                // check if word is a noun and proceed accordingly
                if (this.isNoun(action)) {
                    // get all verbs and check which verb is directly derived from noun
                    inf = getVerbsFromNoun(action);
                }
            }
        }
        return inf;
    }

    /**
     * Returns singular for given noun
     *
     * @param noun noun which needs to be converted in a singular noun
     * @return singular of given noun
     */
    public String getSingularOfNoun(String noun) {
        String tempTag = "";
        if (tempTag.contains("NNS") && noun.endsWith("s")) {
            return noun.substring(0, noun.length() - 1);
        } else {
            return noun;
        }
    }

    public String getNoun(String verb) {
        try {
            Set<String> candidateNouns = new HashSet<>();
            IndexWord iw = wordnet.getIndexWord(POS.VERB, verb);
            if (iw != null) {
                for (Synset synset : iw.getSenses()) {
                    Pointer[] pointers = synset.getPointers(PointerType.NOMINALIZATION);
                    for (Pointer pointer : pointers) {
                        Synset derived = pointer.getTargetSynset();
                        for (Word word : derived.getWords()) {
                            if (word.getPOS() == POS.NOUN && word.getLemma().startsWith(verb.substring(0, 1)) && word.getLemma().length() >= verb.length()) {
                                candidateNouns.add(word.getLemma());
                            }
                        }
                    }
                }
            }
            String noun = "";
            int minDist = Integer.MAX_VALUE;
            for (String candidate : candidateNouns) {
                int distance = Distance.getLD(verb, candidate);
                if (candidate.endsWith("tion")) {
                    distance = 0;
                }
                if (distance < minDist) {
                    minDist = distance;
                    noun = candidate;
                }
            }
            return noun;
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Dictionary getDictionary() {
        return wordnet;
    }
}