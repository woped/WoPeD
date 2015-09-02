package contentDetermination.labelAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import contentDetermination.labelAnalysis.interfaces.LabelCategorizer;
import contentDetermination.labelAnalysis.structure.Activity;
import edu.stanford.nlp.util.ArrayUtils;

/**
 * The LabelAnalyzer provides methods for analyzing labels from Process Models.
 * This version does not rely on process model structures but uses lists as input.
 * @author Henrik Leopold
 * @version 1.1
 */

public class EnglishLabelCategorizer implements LabelCategorizer {
	
	private Dictionary wordnet;					// JWNL - WordNet
	private EnglishLabelHelper lHelper;
	private EnglishLabelDeriver lDeriver;
	private ArrayList<Activity> vosList;		// List for Verb-Object Labels
	private ArrayList<Activity> anList;			// List for Action-Noun Labels
	private ArrayList<Activity> desList;		// List for Action-Noun Labels
	private ArrayList<Activity> tempList;		// List for undecided Labels
	private ArrayList<Activity> irrStringList;	// List for irregular Labels
	private HashMap<String,String> decisionMap;	// Saves categorization decision 
	
	private HashMap <String, Integer> vosWords;		// Map for saving words indicating VO labels
	private HashMap <String, Integer> anWords;		// Map for saving words indicating AN labels
	private HashMap <String, Integer> irrStrings; 	// Map for saving reoccurring string which may indicate irregular labels	
	
	/**
	 * Constructor for LabelAnalyzer.
	 */
	public EnglishLabelCategorizer(Dictionary wordnet, EnglishLabelHelper lHelper, EnglishLabelDeriver lDeriver) {
		this.wordnet = wordnet;
		this.lHelper = lHelper;
		this.lDeriver = lDeriver;
		this.init();
	}
	
	/**
	 * Initializes WordNet and creates the required dictionary object. In addition some variables are initialized.
	 */
	private void init() {
		
        // Initialize variables
        vosList = new ArrayList<Activity>();
		anList = new ArrayList<Activity>();
		desList = new ArrayList<Activity>();
		tempList = new ArrayList<Activity>();
		irrStringList = new ArrayList<Activity>();
		irrStrings = new HashMap<String, Integer>();
		vosWords = new HashMap<String, Integer>();
		anWords = new HashMap<String, Integer>();
	}
	
	
	/**
	 * Main method for categorizing labels into VO and AN. Organizes the call of the different stages.
	 */
	public HashMap<String,String> getLabelStyle(ArrayList<ArrayList<Activity>> modelCollection) {
		
		HashMap<String,String> labelStyleAllocation = new HashMap<String,String>();
		decisionMap = new HashMap<String, String>();
		
		EnglishLabelProperties props =  new EnglishLabelProperties();			
		String label;											
		String output;
		String[] outputSplit;
		int outCount = 0;
		int count = 0;
		
		// Convert into flat list
		ArrayList<Activity> allActivites = new ArrayList<Activity>();
		for (ArrayList<Activity> model: modelCollection) {
			allActivites.addAll(model);
		}

		try {
		
			for (Activity activity: allActivites) {
				count++;											// Count labels
				props.initialize();									// Reset property record
				label = activity.getLabel().toLowerCase();			// Save activity label to variable
				label = lHelper.cleanLabel(label);				// Clean label (bracket content, numbers, etc.)
				String[] labelSplit = label.split(" ");				// Create split label
				
				if (labelSplit.length>0) {
					output = categorizeLabelStep1(activity, label, labelSplit, props, false);
					if (output.equals("") == false) {
						outputSplit = output.split(";");
						outCount++;	
						labelStyleAllocation.put(activity.getOLabel(), outputSplit[0]);
						decisionMap.put(activity.getOLabel(), outputSplit[1]);
					}	
				}	
			}	
			
			// Trigger step 2 (irregular labels)
			for (Activity tempActivity: irrStringList) {
				output = categorizeLabelStep2(tempActivity, props);
				if (output.equals("") == false) {
					outputSplit = output.split(";");
					outCount++;	
					labelStyleAllocation.put(tempActivity.getOLabel(), outputSplit[0]);
					decisionMap.put(tempActivity.getOLabel(), outputSplit[1]);
				} 
			}
			
			// Trigger step 3 (additional strategies)
			for (Activity tempActivity: tempList) {
				output = categorizeLabelStep3(tempActivity);
				if (output.equals("") == false) {
					outputSplit = output.split(";");
					outCount++;	
					labelStyleAllocation.put(tempActivity.getOLabel(), outputSplit[0]);
					decisionMap.put(tempActivity.getOLabel(), outputSplit[1]);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done ("+outCount+"/"+count+")");
		return labelStyleAllocation;
	}
	
	/**
	 * Expensive implementation of categorization algorithm for single label (to be improved).
	 * For better results, refer to the model collection categorization.
	 */
	public String getLabelStyle(Activity activity) {
		ArrayList<ArrayList<Activity>> modelCollection = new ArrayList<ArrayList<Activity>>();
		ArrayList<Activity> model = new ArrayList<Activity>();
		model.add(activity);
		modelCollection.add(model);
		HashMap<String,String> allocation = this.getLabelStyle(modelCollection);
		return allocation.get(activity.getOLabel());
	}

	/**
	 * Categorizes label by only consulting local information (Stage 1)
	 * @return String with decision and decision strategy; "" if undecided
	 */
	private String categorizeLabelStep1(Activity activity, String label, String[] labelSplit, EnglishLabelProperties props, boolean fromStep3) throws JWNLException, IOException {
		boolean isVerb = false;
		boolean isAdjective = false;
		boolean isNoun = false;
		boolean isAdverb = false;
		boolean assignedByRule = false;
		boolean isInfinitive = false;

		// Remove redundant characters from first word
		labelSplit[0] = labelSplit[0].replace("/", "");
		labelSplit[0] = labelSplit[0].replace(":", "");
		labelSplit[0] = labelSplit[0].replace(",", "");
		
		// Evaluate POS options for first word
		isVerb = lHelper.isVerb(labelSplit[0]);
		isAdjective = lHelper.isAdjective(labelSplit[0]);
		isNoun = lHelper.isNoun(labelSplit[0]);
		isAdverb = lHelper.isAdverb(labelSplit[0]);
		
		// Check whether first "word" is a number
		try {
			Integer.parseInt(labelSplit[0]);
			labelSplit = (String[]) ArrayUtils.removeAt(labelSplit, 0);
			if (labelSplit.length == 0) {
				return "Rest";
			}
			isVerb = lHelper.isVerb(labelSplit[0]);
		} catch (NumberFormatException e) {
		}
		
		// Remove first word if not in dictionary
		if (isAdjective == false && isVerb == false && isNoun == false && isAdverb == false) {
			if (labelSplit.length > 1) {
				labelSplit = (String[]) ArrayUtils.removeAt(labelSplit, 0);
			}
			isVerb = lHelper.isVerb(labelSplit[0]);
		}
		
		// Remove adjective in first position
		if (isAdjective == true && isVerb == false && labelSplit.length > 1) {
			if (wordnet.lookupIndexWord(POS.NOUN, labelSplit[1]) != null && lHelper.isVerb(labelSplit[1]) == false && lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[1]),labelSplit[1])==false) {
				anList.add(activity);
				return("AN;Adjective in first position");
			} else {
				labelSplit = (String[]) ArrayUtils.removeAt(labelSplit, 0);
				isVerb = lHelper.isVerb(labelSplit[0]);
			}
		}
		
		// Remove adverb in first position
		if (isAdverb == true && isVerb == false) {
			if (labelSplit.length > 1) {
				labelSplit = (String[]) ArrayUtils.removeAt(labelSplit, 0);
			}	
			isVerb = lHelper.isVerb(labelSplit[0]);
		}
		
		// Check for conjunction
		lHelper.checkForConjunction(label, labelSplit, props);
		
		if (label.contains(":") && fromStep3 == false) {
			String irrString = label.substring(0,label.indexOf(":"));
			activity.setEditedLabel(label);
			irrStringList.add(activity);
			if (irrStrings.containsKey(irrString) == false) {
				irrStrings.put(irrString, 1);
			} else {
				int temp = irrStrings.get(irrString) + 1;
				irrStrings.put(irrString, temp);
			}
			return "";
		}
		if (label.contains(" - ") && fromStep3 == false) {
			String irrString = label.substring(0,label.indexOf(" - "));
			activity.setEditedLabel(label);
			irrStringList.add(activity);
			if (irrStrings.containsKey(irrString) == false) {
				irrStrings.put(irrString, 1);
			} else {
				int temp = irrStrings.get(irrString) + 1;
				irrStrings.put(irrString, temp);
			}
			return "";
		}
		
		// ******** START OF DECISION PROCESS ********
		
		// Rule-based decisions (structure-based)
		if (label.startsWith("in case of")) {
			vosList.add(activity);
			return("VO;Rule");
		}
		if (labelSplit.length > 1) {
			if (labelSplit[1].equals("of")) {
				anList.add(activity);
				return("AN;Rule");
			}
		}
		if (labelSplit.length > 1) {
			if (labelSplit[1].equals("if")) {
				vosList.add(activity);
				updateVOSMap(vosWords, labelSplit[0]);
				return("VO;Rule");
			}
		}
		if (labelSplit.length > 2) {
			if (labelSplit[2].equals("if") && labelSplit[1].equals(",")) {
				updateVOSMap(vosWords, labelSplit[0]);
				vosList.add(activity);
				return("VO;Rule");
			}
		}
		if (labelSplit.length > 3 && (labelSplit[2].equals("/") || labelSplit[2].equals("and"))) {
			if (labelSplit[3].equals("of")) {
				anList.add(activity);
				return("AN;Rule");
			}
		}
		
		if (lHelper.isDescriptiveLabel(activity.getLabel_Tag()) == true) {
			if (isVerb==false || lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[0]), labelSplit[0])==false) {
				desList.add(activity);
				return("DES;Rule");
			}
		}
		if (lHelper.isStatusLabel(activity.getLabel_Tag()) == true) {
			return("STATUS;Rule");
		}

		// Check that in case of conjunction, both verbs are provided as infinitive
		if (props.getIndexConjunctionSplit()==1) {
			isVerb = lHelper.isVerb(labelSplit[0]) && lHelper.isVerb(labelSplit[2]);
		}
		
		// If first word can be a verb
		if (isVerb == true && assignedByRule == false) {
			
			// Check whether original verb equals infinitive and thus may be an imperative
			isInfinitive = lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[0]), labelSplit[0]);
			if (props.hasConjunction()== true && props.getIndexConjunctionSplit()==1) {
				isInfinitive = lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[0]), labelSplit[0]) &&
				lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[2]), labelSplit[2]);
			}
			
			if (isInfinitive == true) {
				
				// Check whether verb in imperative form also be used as a noun (zero-derivation)
				IndexWord word = wordnet.lookupIndexWord(POS.NOUN, labelSplit[0]);
				if (word == null) {
					vosList.add(activity);
					updateVOSMap(vosWords, labelSplit[0]);
					return("VO;No Zero-Derivation");
				
					// If yes, further investigations are necessary...	
				} else {
					
					// Correct label with prep "w/o"
					if (label.contains("w / o")) {
						label = label.replace("w / o", "w/o");
						labelSplit = label.split(" ");
					}
					
					// Identify potential verb assuming to consider an action-noun label
					lHelper.checkForConjunction(label, labelSplit, props);				// Check for CONJUNCTIONS
					lHelper.checkForPreposisitons(label, labelSplit, props);			// Check for PREPOSITIONS
					lHelper.checkForGerundStyle(label, labelSplit, props);				// Check for GERUND STYLE

					// Get potential action and business object from label
					lDeriver.deriveFromActionNounLabels(props, label, labelSplit);
					
					boolean onlyOneVerb = false;
					
					// Conjunction Case
					if (props.hasConjunction() == true) {
						if (props.getMultipleActions().size() == 0) {
							onlyOneVerb = true;
						} else {
							for (String potVerb : props.getMultipleActions()) {
								if (lHelper.isVerb(lHelper.getInfinitiveOfAction(potVerb)) == false || labelSplit.length==1) { 
									onlyOneVerb = true;
								} 
							}
						}
						
						// If multiple potential verbs are identified
						if (onlyOneVerb == false) {
							if (labelSplit[0].equals(props.getMultipleActions().get(0))==true) {
								vosList.add(activity);
								updateVOSMap(vosWords, labelSplit[0]);
								return("VO;Only one potential verb");
							} else {
								activity.setEditedLabelSplit(labelSplit);
								tempList.add(activity);
								return "";
							}
						} else {
							updateVOSMap(vosWords, labelSplit[0]);
							vosList.add(activity);
							return("VO;Only one potential verb");
						}
						
					// Non-Conjunction Case
					} else {
						
						// Check for article in the second word
						if (labelSplit.length > 2 && Arrays.asList(EnglishLabelProperties.articles).contains(labelSplit[1]))  {
							vosList.add(activity);
							updateVOSMap(vosWords, labelSplit[0]);
							return("VO;Article");
						}
						
						// Check for Gerund at the end
						if (props.getAction().endsWith("ing") && lHelper.isNoun(props.getAction())) {
							anList.add(activity);
							return("AN;Gerund (End)");
						}
						
						String potVerb = lHelper.getInfinitiveOfAction(props.getAction());
						if (lHelper.hasPluralNounAtEnd(label, labelSplit)) {
							if (lHelper.isInfinitive(lHelper.getInfinitiveOfAction(labelSplit[0]),labelSplit[0])) {
								vosList.add(activity);
								return("VO;Plural Noun");
							} else {
								activity.setEditedLabelSplit(labelSplit);
								tempList.add(activity);
								return "";
							}
						}
						if (lHelper.isVerb(potVerb) == false || labelSplit.length == 1) { 
							onlyOneVerb = true;
							String[] oSplit = activity.getOLabel().toLowerCase().split(" ");
							if (oSplit.length > 1 && labelSplit.length == 1 && oSplit[0] != labelSplit[0]) {
								anList.add(activity);
								return("AN;Only one potential verb");
							}
						}
						// Two potential verbs are identified
						if (onlyOneVerb == false) {
								
							if (potVerb.equals(labelSplit[0]) || Arrays.asList(EnglishLabelProperties.elements).contains(labelSplit[1]) || labelSplit[1].equals("if")) {
								vosList.add(activity);
								if (labelSplit.length>1) {
									updateVOSMap(vosWords, labelSplit[0]);
								}
								return("VO;Only one potential verb");
							} else {
								activity.setEditedLabelSplit(labelSplit);
								tempList.add(activity);
								return "";
							}
						} else {
							if (labelSplit.length>1) {
								updateVOSMap(vosWords, labelSplit[0]);
							}
							vosList.add(activity);
							return("VO;Only one potential verb");
						}
					}
				}
				
			// Original verb in label and infinitive are not equal
			} else {
				String des = lHelper.getInfinitiveOfAction(labelSplit[0])+"s";
				if (des.equals(labelSplit[0])==true) {
					desList.add(activity);
					return("DES;Infinitive mismatch");
				}
				anList.add(activity);
				updateANMap(anWords, labelSplit[0]);
				return("AN;Infinitive mismatch");
			}
		} 
		// Original verb in label cannot be a verb
		if (isVerb == false && assignedByRule == false) {
			if (labelSplit.length > 1 && lHelper.isVerb(labelSplit[1])== true && labelSplit[1].endsWith("s")) {
				anList.add(activity);
				return("DES;Can not be Verb");
			}
			updateANMap(anWords, labelSplit[0]);
			anList.add(activity);
			return("AN;Can not be Verb");
		}
		return "";
	}
	
	/**
	 * Checks potential irregular labels, removes reoccurring Strings and reapplies Stage 1 for shortened label.
	 */
	private String categorizeLabelStep2(Activity tempActivity, EnglishLabelProperties props) throws JWNLException, IOException {
		
		String irrString = "";
		String restLabel = ""; 
		String result = "";
		String label = tempActivity.getEditedLabel(); 
		props = new EnglishLabelProperties();
		
		if (label.contains(":")) {
			irrString = label.substring(0,label.indexOf(":"));
			restLabel = label.substring(label.indexOf(":")+1,label.length());
		} else if (label.contains(" - ")) {
			irrString = label.substring(0,label.indexOf(" - "));
			restLabel = label.substring(label.indexOf(" - ")+1,label.length());
		}
		int amount = irrStrings.get(irrString);
		if (amount >= 2) {
			if (irrString.length() >= restLabel.length()) {
				result = categorizeLabelStep1(tempActivity, irrString, irrString.split(" "), props, true);
			} else {
				result = categorizeLabelStep1(tempActivity, restLabel, restLabel.split(" "), props, true);
			}
			return(result);
		} else {
			String l = lHelper.cleanLabel(tempActivity.getLabel().toLowerCase());
			return categorizeLabelStep1(tempActivity, l, l.split(" "), props, true);
		}
	}
	
	/**
	 * Categorizes label by consulting local and global process information (Stage 2-3), 
	 * calls Stage 4 if undecided after Stage 3
	 * @return String with decision and decision strategy; "" if undecided
	 */
	private String categorizeLabelStep3(Activity tempActivity) throws IOException {
			
			// Assign by considering similar labels in same process model
			ArrayList <Activity> model = tempActivity.getModel();
				
			HashMap <String, Integer> localVosWords = new HashMap<String, Integer>();
			HashMap <String, Integer> localAnWords = new HashMap<String, Integer>();
			
			for (Activity activity : model) {
				String[] tempSplit = activity.getLabel().split(" "); 
				if (tempSplit.length >= 1) {
					String firstWord = tempSplit[0].toLowerCase();
					if (firstWord.equals(tempActivity.getEditedLabelSplit()[0]) && tempActivity.getOLabel().equals(activity.getOLabel())==false) {
						if (vosList.contains(activity) || anList.contains(activity)) {
							if (vosList.contains(activity)) {
								updateVOSMap(localVosWords, tempActivity.getEditedLabelSplit()[0]);
							}
							if (anList.contains(activity)) {
								updateVOSMap(localAnWords, tempActivity.getEditedLabelSplit()[0]);
							}
						}
					}
				}
			}
			
			if (localVosWords.get(tempActivity.getEditedLabelSplit()[0])== null && localAnWords.get(tempActivity.getEditedLabelSplit()[0])!= null) {
				anList.add(tempActivity);
				return("AN;Local Frequency");
			}
			if (localVosWords.get(tempActivity.getEditedLabelSplit()[0])!= null && localAnWords.get(tempActivity.getEditedLabelSplit()[0])== null) {
				vosList.add(tempActivity);
				return("VO;Local Frequency");
			}
			if (localVosWords.get(tempActivity.getEditedLabelSplit()[0])!= null && localAnWords.get(tempActivity.getEditedLabelSplit()[0])!= null) {
				if (localVosWords.get(tempActivity.getEditedLabelSplit()[0]) > localAnWords.get(tempActivity.getEditedLabelSplit()[0])) {
					vosList.add(tempActivity);
					return("VO;Local Frequency");
				} else {
					anList.add(tempActivity);
					return("AN;Local Frequency");
				}
			}
			
			// Use global frequencies
			if (vosWords.get(tempActivity.getEditedLabelSplit()[0])== null && anWords.get(tempActivity.getEditedLabelSplit()[0])!= null) {
				anList.add(tempActivity);
				return("AN;Global Frequency");
			}
			if (vosWords.get(tempActivity.getEditedLabelSplit()[0])!= null && anWords.get(tempActivity.getEditedLabelSplit()[0])== null) {
				vosList.add(tempActivity);
				return("VO;Global Frequency");
			}
			if (vosWords.get(tempActivity.getEditedLabelSplit()[0])!= null && anWords.get(tempActivity.getEditedLabelSplit()[0])!= null) {
				if (vosWords.get(tempActivity.getEditedLabelSplit()[0]) > anWords.get(tempActivity.getEditedLabelSplit()[0])) {
					vosList.add(tempActivity);
					return("VO;Global Frequency");
				} else {
					anList.add(tempActivity);
					return("AN;Global Frequency");
				}
			}
			
			// or by best POS
			String bestPos = getBestPOS(tempActivity.getEditedLabelSplit()[0],tempActivity.getEditedLabelSplit(), tempActivity.getLabel());
			if (bestPos.equals("v")) {
				vosList.add(tempActivity);
				return("VO;Best POS");
			}
			if (bestPos.equals("n")) {
				anList.add(tempActivity);
				return("AN;Best POS");
			}
			return "";
	}

	/**
	 * Evaluates best part of speech based on word frequency list (Stage 4)
	 * @return "v" for VOS; "n" for AN
	 */
	private String getBestPOS(String word, String[] labelSplit, String label) throws IOException {
		
		if (Arrays.asList(EnglishLabelHelper.verbs).contains(labelSplit[0])) {
			return "v";
		}
		
		BufferedReader br = new BufferedReader(new FileReader("wordlist.txt"));
		String line = "";
		  
		  int vCount = 0;
		  int nCount = 0;
		  while ((line = br.readLine()) != null) {
			  if (line.contains(word)) {
				  String[] lineSplit = line.split("\t");
				  if (lineSplit[1].startsWith(word) && lineSplit[2].startsWith("v")) {
					  vCount = vCount + Integer.parseInt(lineSplit[0]);
				  }
				  if ((lineSplit[1].equals(word) || lineSplit[1].equals(word+"s")) && lineSplit[2].startsWith("n")) {
					  nCount = nCount + Integer.parseInt(lineSplit[0]);
				  }
			  }
		  }
		  String result = "";
		  if (vCount < nCount) {
			  result = "n";
		  } else {
			  result = "v";
		  }
		  br.close();
		  return result;
	}
	

	
	/**
	 * Updates the vosWords HashMap capturing first words of Verb-Object Labels
	 * @param vosWords HashMap for storing the mapping
	 * @param word new element
	 */
	private void updateVOSMap(HashMap <String, Integer> vosWords, String word){
		if (vosWords.containsKey(word)) {
			Integer temp = vosWords.get(word)+1;
			vosWords.put(word, temp);
		} else {
			vosWords.put(word, 1);
		}
	}
	
	/**
	 * Updates the anWords HashMap capturing first words of Action-Noun Labels
	 * @param anWords HashMap for storing the mapping
	 * @param word new element
	 */
	private void updateANMap(HashMap <String, Integer> anWords, String word){
		if (anWords.containsKey(word)) {
			Integer temp = anWords.get(word)+1;
			anWords.put(word, temp);
		} else {
			anWords.put(word, 1);
		}
	}
	
	/**
	 * Getters
	 */
	public ArrayList<Activity> getVOSLabels() {
		return vosList;
	}	
	
	public ArrayList<Activity> getANLabels() {
		return anList;
	}
	
	public ArrayList<Activity> getDESLabels() {
		return desList;
	}
}


