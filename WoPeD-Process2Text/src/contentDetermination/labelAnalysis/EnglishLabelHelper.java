package contentDetermination.labelAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import contentDetermination.support.Distance;
import contentDetermination.support.Noun2VerbTransformer;
import contentDetermination.support.Stemmer;

public class EnglishLabelHelper {
	
	static String[] verbs = {"sing off", "logon", "deallocate", "pick up", "top up", "postprocess", "downselect", "don't", "hand-in", "re-sort", "rescore", "overview", "repost", "rollup", "overrule", "pre-configure", "upsell", "pickup", "wrap-up", "up-sell", "cross-sell", "inventorise", "recheck", "intake", "login", "reroute", "lookup", "handover", "setup", "hand-over", "goto", "feedback", "pick-up", "rollback"};
	private Dictionary wordnet;

	private static String WN_TEMP_FILE = "file_temp_properties.xml";
	private static String WN_INIT_File = "file_properties.xml";
	
	static List<String> suffixes;
	
	/**
	 * Constructor
	 * @param wordnet
	 */
	public EnglishLabelHelper(Dictionary wordnet) {
		this.wordnet = wordnet;
	}
	
	public String getTag(String s) {
		//return tagger.tagString(s);
		return null;
	}

	public EnglishLabelHelper(String contextPath) throws JWNLException, IOException, ClassNotFoundException, URISyntaxException {
		// Initialize WordNet (JWNL)
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("file_properties.xml");
	    JWNL.initialize(input, contextPath);
		this.wordnet = Dictionary.getInstance();

//		// Tagger
//		resource = classLoader.getResource("bidirectional-distsim-wsj-0-18.tagger");
//		tagger = new MaxentTagger(resource.toURI().toString().replace("file:", ""));
//		tagger = new MaxentTagger("stanford-postagger-full-2011-06-19/models/bidirectional-distsim-wsj-0-18.tagger");
	}

	
	/**
	 *  Function returning verb for given noun
	 * @param noun action given as a noun which has to be transformed in to a verb
	 * @return verb (infinitive) derived from given noun
	 */
	public String getVerbsFromNoun(String noun) {
		try {
			String ret	= Noun2VerbTransformer.toVerb(noun, wordnet);
			return ret;	
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Evaluates whether given word can be a verb
	 * @param potVerb
	 * @return true if the given word can be a verb
	 */
	public boolean isVerb(String potVerb) {
		potVerb = potVerb.toLowerCase();
		for (int i = 0; i<verbs.length;i++) {
			if (potVerb.equals(verbs[i])) {
				return true;
			}
		}
		IndexWord word = null;
		try {
			word = wordnet.lookupAllIndexWords(potVerb).getIndexWord(POS.VERB);
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		if (word != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Evaluates whether given word can be an adjective 
	 * @param potAdj
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
		if (word != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Evaluates whether given word can be an adjective 
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
		if (word != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAdverb(String potAdverb) {
		potAdverb = potAdverb.toLowerCase();
		
		IndexWord word = null;
		try {
			word = wordnet.lookupAllIndexWords(potAdverb).getIndexWord(POS.ADVERB);
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		if (word != null) {
			return true;
		} else {
			return false;
		}
	}
	
	

	/**
	 * Checks whether "inf" is the infinitive of "original"
	 * @param inf
	 * @param original
	 * @return true if "inf" is the infinitive of "original"
	 */
	public boolean isInfinitive (String inf, String original) {
		for (int i = 0; i<verbs.length;i++) {
			if (original.equals(verbs[i])) {
				return true;
			}
		}
		return inf.equals(original);
	}

	/**
	 *  Returns the infinitive of a potential Action (which might still be a noun)
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
		if (mapped == false) {
			// go through each word in order to determine words that may represent an action
			
			// check if word is actually a verb
			if (isVerb(action)) {
				IndexWord iw = null;
				try {
					iw = wordnet.lookupAllIndexWords(action).getIndexWord(POS.VERB);
				} catch (JWNLException e) {
				}
				if (iw == null) {
				} else {
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
	 * Returns adverb for given adjective
	 * @param adj
	 * @return
	 * @throws JWNLException
	 */
	public String getAdverb(String adj) throws JWNLException {
		String adv = "";
		
		if (adj.equals("overall")) {
			adv = "overall";
		}
		if (adj.equals("public")) {
			adv = "publicly";
		}
		if (adj.equals("automated")) {
			adv = "automatically";
		}
		if (adj.equals("generic")) {
			adv = "genericly";
		}
		if (adj.equals("detailed")) {
			adv = "in detail";
		}
		if (adj.equals("online")) {
			adv = "online";
		}
		if (adj.equals("online")) {
			adv = "online";
		}
		if (adj.equals("mobile")) {
			adv = "in mobile manner";
		}
		

		if (adv.equals("")) {
			// Ending: y, happy --> happily
			if (adj.endsWith("y")) {
				adv = adj.substring(0, adj.length()-2) + "ily";
			}
			// Ending: -able, -ible, or -le, probable --> probably
			if (adj.endsWith("able") || adj.endsWith("ible") || adj.endsWith("le")) {
				adv = adj.substring(0, adj.length()-2) + "y";
			}
			// Ending: ic, automatic --> autmatically
			if (adj.endsWith("ic")) {
				adv = adj + "ally";
			}
		}
		if (adv.equals("")) {
			adv = adj + "ly";
		}
		return adv;
	}
	
	/**
	 * Checks whether given label has a plural noun at the end. A positive results proofs the label to be no AN label.
	 */
	public boolean hasPluralNounAtEnd(String label, String[] labelSplit) throws JWNLException {
		
		// Check ending first
		if (label.endsWith("s")) {
			IndexWord iw = null;
			String singular = "";
			iw = wordnet.lookupAllIndexWords(labelSplit[labelSplit.length-1]).getIndexWord(POS.NOUN);
			if (iw == null) {
			} else {
				singular = iw.getLemma();
			}
			// if given word and singular doesn't match
			if (labelSplit[labelSplit.length-1].equals(singular) == false) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Cleans a given label, i.e. content in brackets, dots etc. are removed
	 * @param label
	 * @return
	 */
	public String cleanLabel(String label) {
		// Remove content in brackets (*), [*] and clean from numbers etc. 
		if (label.indexOf(")") > -1 && label.indexOf(" (") > -1 && label.indexOf(")") == (label.length()-1)) {
			label = label.substring(0,label.indexOf(" ("));
		}
		if (label.indexOf(" [") > -1) {
			label = label.substring(0,label.indexOf(" ["));
		}
		if (label.endsWith(".")) {
			label = label.substring(0,label.length()-1);
		}
		if (label.endsWith(".")) {
			label = label.substring(0,label.length()-1);
		}
		if (label.contains("\"")) {
			label = label.replace("\"", "");
		}
		return label;
	}
	

	
	
	
	public String getParticiple(String verb) {
		if (verb.equals("plan")) {
			return "planned";
		}
		if (verb.equals("pay")) {
			return "paid";
		}
		if (verb.equals("take")) {
			return "taken";
		}
		if (verb.equals("get")) {
			return "gotten";
		}
		if (verb.equals("make")) {
			return "made";
		}
		if (verb.equals("split")) {
			return "splitted";
		}
		if (verb.equals("lookup")) {
			return "looked up";
		}
		if (verb.equals("read")) {
			return "read";
		}
		if (verb.endsWith("e")) {
			return verb+"d";
		}
		return verb + "ed";
	}
	
	public String getVOSLabelWithoutAddition(EnglishLabelProperties props, String[] labelSplit, String label) {
		if (props.getIndexPrepSplit() > 0) {
			String BO = "";
			for (int j=1; j<=props.getIndexPrepSplit()-1; j++) {
				BO = BO + " " + labelSplit[j];
			}
			BO = BO.trim();
			return labelSplit[0] + " " + BO;
		} else {
			return label;
		}
		
	}
	
	public boolean beginsWithAdverb(String[] labelSplit) throws JWNLException {
		String adj = labelSplit[0];
		if (adj.equals("corporate")) {
			return false;
		}
		if (adj.equals("incident")) {
			return false;
		}
		if (adj.equals("fixed")) {
			return false;
		}
		if (adj.equals("1")) {
			return false;
		}
		
		
		IndexWord  adjI = wordnet.lookupAllIndexWords(adj).getIndexWord(POS.ADJECTIVE);
		if (!(adjI == null)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns singular for given noun
	 * @param noun noun which needs to be converted in a singular noun
	 * @return singular of given noun
	 * @throws Exception
	 */
	public String getSingularOfNoun(String noun) {
		String tempTag = "";
		tempTag = "";//tagger.tagString(noun);
		if (tempTag.contains("NNS") && noun.endsWith("s")) {
			String action = noun.substring(0,noun.length()-1);
			return action;
		} else {
			return noun;
		}
	}
	
	/**
	 * Evaluates whether a given label is a status label (event style)
	 * @param labelTag tagged label string
	 * @return true if label is a status label
	 */
	public boolean isStatusLabel(String labelTag) {
		String[] splitLabel = labelTag.split(" ");
		if  (labelTag.contains("VBD")) {
			if (isVerb(splitLabel[0]) == false || isInfinitive(getInfinitiveOfAction(splitLabel[0]), splitLabel[0])==false) {
				return true;	
			}
		}
		if (labelTag.contains("VBD")) {
			String[] splitTag = labelTag.split(" ");
			for (int i = 0; i < splitTag.length; i++) {
				if (splitTag[i].contains("VBD") == true) {
					if ((i+1) > splitTag.length && splitTag[i+1].contains("VBN") == true) {
						return true;
					}
				}
			}	
		}
		return false;
	}
	
	/**
	 * Evaluates whether a given label follows the descriptive Style
	 * @param labelTag tagged label string
	 * @return true if label follows descriptive style
	 */
	public boolean isDescriptiveLabel(String labelTag) {
		String[] splitLabel = labelTag.split(" ");
		if  (labelTag.contains("VBZ")) {
			if (isVerb(splitLabel[0]) == false || isInfinitive(getInfinitiveOfAction(splitLabel[0]), splitLabel[0])==false) {
				return true;	
			}
		}
		return false;
	}
	
	/**
	 * Transforms action in an infinitive (=imperative) and aligns multiple actions to a row of imperatives.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 * @throws Exception 
	 */
	public void transformActions(String label, String[] labelSplit, EnglishLabelProperties props) throws Exception {
		// If no multiple actions...
		if (props.getMultipleActions().size() == 0) {
			
			// Transform to infinitive
			props.setAction(getSingularOfNoun(props.getAction()));
			props.setAction(getInfinitiveOfAction(props.getAction()));
			
			// If WordNet returns infinitive (WordNet might return nothing due to incompleteness) ...
			if (props.getAction().equals("") == false) {
				
				// Assign infinitive to action
				props.setAction(props.getAction().trim());
				
				// Make first letter upper case
				props.setAction(props.getAction().substring(0,1).toUpperCase() + props.getAction().substring(1,props.getAction().length()));
				props.addToActions(props.getAction());
			}
		// If there are multiple actions...	
		} else {
			
			// Clear action variable
			props.setAction("");
			
			// For each of the identified actions ...
			for (int j=0; j < props.getMultipleActions().size(); j++) {
				
				// Transform to infinitive
				String tempAction = props.getMultipleActions().get(j);
				tempAction = getSingularOfNoun(tempAction);
				tempAction = getInfinitiveOfAction(tempAction);
				
				// Organize in a row of actions separated with a ',' and the last action is connected with an 'and'
				if (j<props.getMultipleActions().size()-2) {
					props.setAction(props.getAction() + tempAction + ", ");	
				} else if (j< props.getMultipleActions().size()-1) {
					props.setAction(props.getAction() + tempAction + " and ");	
				} else {
					props.setAction(props.getAction() + tempAction);
				}
				props.addToActions(props.getAction());
			}
		}
	}
	
	/**
	 * Checks given label for conjunction and stores results in props.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	public void checkForConjunction(String label, String[] labelSplit, EnglishLabelProperties props) {
		if (label.contains(" and ")) {
			props.setIndexConjunction(label.indexOf(" and "));
			props.setHasConjunction(true);
			for (int j = 0; j < labelSplit.length; j++) {
				if (labelSplit[j].equals("and")) {
					props.setIndexConjunctionSplit(j);
					break;
				}
			}
		}	
		if (label.contains(" / ") && label.indexOf(" / ") < props.getIndexPrep()) {
			props.setIndexConjunction(label.indexOf(" / "));
			props.setHasConjunction(true);
			for (int j = 0; j < labelSplit.length; j++) {
				if (labelSplit[j].equals("/")) {
					props.setIndexConjunctionSplit(j);
					break;
				}
			}
		}	
		if (label.contains(",")) {
			props.setIndexConjunction(label.indexOf(","));
			props.setHasConjunction(true);
			for (int j = 0; j < labelSplit.length; j++) {
				if (labelSplit[j].contains(",")) {
					props.setIndexConjunctionSplit(j);
					break;
				}
			}
		}	
	}
	
	/**
	 * Checks given label for gerund style and stores results in props.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	public void checkForGerundStyle(String label, String[] labelSplit, EnglishLabelProperties props) {
		
		// Check if first verb can be a verb
		props.setVerb(this.isVerb(labelSplit[0]));
		if (props.isVerb() == true) {

			// Derive stem
			Stemmer stemmer = new Stemmer();
			stemmer.add(labelSplit[0].toCharArray(), labelSplit[0].length());
			String stem = stemmer.getResultBuffer().toString();
			
			// If word ends with 'ing' and stem does not end with 'ing' (to not categorize e.g. 'sing' as a gerund)
			if (labelSplit[0].endsWith("ing") && (stem.endsWith("ing")==false)) {
				if ((labelSplit.length > 1) && (props.getPrepositions().contains(labelSplit[1]) == false)) {
					props.setHasSuffixING(true);
				}	
			}
			// to be improved (corpus check?!)
			if (props.hasSuffixING() == true ) {
				props.setGerundStyle(true);
			}
		}	
	}
	
	/**
	 * Checks given label for prepositions and stores results props.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	public void checkForPreposisitons(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException{
		// Check each word in the label whether it is a preposition
		for (int j = 1; j < labelSplit.length; j++) {
			if (props.getPrepositions().contains(labelSplit[j]) == true) {
				if (labelSplit[j].equals("of")) {
					props.setHasPrepositionOf(true);
					props.setIndexOf(j);
				}
				props.setIndexPrep(label.indexOf(" " + labelSplit[j]));
				props.setHasPreposition(true);
				props.setIndexPrepSplit(j);
				
				// if conjunction is positioned in addition, ignore conjunction
				if (props.getIndexPrep() < props.getIndexConjunction()) {
					props.setHasConjunction(false);
				}
				break;
			}
		}
			// Exclude phrases like 'Bill of Exchange'
			if (label.contains(" of ") && label.length() >=3) {
				IndexWord indexWord = wordnet.getIndexWord(POS.NOUN,labelSplit[0] + " " + labelSplit[1] + " " + labelSplit[2]);
				if (indexWord != null) {
					props.setHasPrepositionOf(false);
					props.setHasPreposition(false);
				}
			}
			if (props.getIndexOf() >= 2 && props.hasConjunction() == false) {
				IndexWord indexWord = wordnet.getIndexWord(POS.ADJECTIVE,labelSplit[0]);
				if (indexWord != null) {
				} else {
					props.setHasPrepositionOf(false);
				}
			}
	}
	
	public String getNoun(String verb) {

		try {
		Set<String> candidateNouns = new HashSet<String>();
		IndexWord iw = wordnet.getIndexWord(POS.VERB, verb);
		if (iw != null) {
			for (Synset synset : iw.getSenses()) {
				Pointer[] pointers = synset.getPointers(PointerType.NOMINALIZATION);
				for (Pointer pointer : pointers) {
					Synset derived = pointer.getTargetSynset();
					for (Word word : derived.getWords()) {
						if (word.getPOS() == POS.NOUN && word.getLemma().startsWith(verb.substring(0, 1)) == true && word.getLemma().length() >= verb.length()) {
							candidateNouns.add(word.getLemma());
						}
					}
				}
			}	
		}
		String noun = "";
		int minDist = Integer.MAX_VALUE;
		for (String candidate : candidateNouns) {
			int distance = Distance.getLD(verb,candidate);
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
	
	public Dictionary getDictionary(){
		return wordnet;
	}
}
