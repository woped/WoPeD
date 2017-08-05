package contentDetermination.labelAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import contentDetermination.labelAnalysis.interfaces.LabelDeriver;
import contentDetermination.labelAnalysis.structure.Activity;

public class EnglishLabelDeriver implements LabelDeriver {
	
	private Dictionary wordnet;
	private EnglishLabelHelper lHelper;
	private ArrayList<String> actions;
	private ArrayList<String> businessObjects;
	private String addition;
	
	/**
	 * Constructor
	 */
	public EnglishLabelDeriver(EnglishLabelHelper lHelper) {
		this.wordnet = lHelper.getDictionary();
		this.lHelper = lHelper;
		this.actions = new ArrayList<String>();
		this.businessObjects = new ArrayList<String>();
		this.addition = "";
	}
	
	public void processLabel(Activity activity, String labelStyle) {
		actions.clear();
		businessObjects.clear();
		addition = "";
		try {
			EnglishLabelProperties props = new EnglishLabelProperties();
			props.initialize();
			String label = activity.getLabel().toLowerCase();
	
			// Remove content in brackets (*), [*]
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
			String[] labelSplit = label.split(" ");
			lHelper.checkForConjunction(label, labelSplit, props);
			lHelper.checkForPreposisitons(label, labelSplit, props);
			lHelper.checkForGerundStyle(label, labelSplit, props);
			if (label.contains(":") || label.contains(" - ")) {
				props.setIrregularStyle(true);
			}
			
			if (labelStyle.equals("AN")) {
				// Derive action/ business for GERUND STYLE labels
				if (props.isGerundStyle() == true && props.hasConjunction() == false && props.isIrregularStyle() == false) {
					deriveFromGerundStyle(label, labelSplit, props);
				}	
				
				// Derive action/ business object for PREPOSITION-'OF' labels
				if (props.hasPrepositionOf() == true && props.hasConjunction() == false && props.isGerundStyle() == false && props.isIrregularStyle() == false) {
					if (lHelper.beginsWithAdverb(labelSplit)) {
						deriveFromPrepositionOfADV(label, labelSplit, props);
					} else {
						deriveFromPrepositionOf(label, labelSplit, props);
					}
				}
				
				// Derive action/ business object for NOUN PHRASE labels WITH PREPOSITIONAL PHRASE
				if (props.hasPreposition() == true && props.hasConjunction() == false && props.hasPrepositionOf() == false && props.isGerundStyle() == false && props.isIrregularStyle() == false) {
					if (lHelper.beginsWithAdverb(labelSplit)) {
						deriveFromNounPhraseWithPrepPhraseADV(label, labelSplit, props);
					} else {
						deriveFromNounPhraseWithPrepPhrase(label, labelSplit, props);
					}
				}
				
				// Derive action/ business object for NOUN PHRASE 
				if (props.hasConjunction() == false && props.hasPreposition() == false && props.isGerundStyle() == false && props.isIrregularStyle() == false) {
					if (lHelper.beginsWithAdverb(labelSplit)) {
						deriveFromNounPhraseADV(label, labelSplit, props);
					} else {
						if (lHelper.hasPluralNounAtEnd(label, labelSplit) == true) {
							deriveFromPluralNounLabel(label, labelSplit, props);
						} else {
							deriveFromNounPhrase(label, labelSplit, props);
						}
					}
				}
				
				// Derive action/ business object for CONJUNCTION labels
				if (props.hasConjunction() == true && props.isIrregularStyle() == false) {
					deriveFromConjunctionLabels(label, labelSplit, props);
				}
				// Derive action/ business object for IRREGULAR labels
				if (props.isIrregularStyle() == true) {
					deriveFromIrregular(label, labelSplit, activity, props);
				}
			}
			if (labelStyle.equals("VO")) {
				deriveFromVOS(label, labelSplit, props);
			}
			if (labelStyle.equals("DES")) {
				deriveFromDES(label, labelSplit,activity, props);
			}
			if (props.getMultipleActions().size() > 0) {
				actions = props.getMultipleActions();
			} else {
				actions.add(props.getAction());
			}
			businessObjects.add(props.getBusinessObject());
			addition = props.getAdditionalInfo();
			
		} catch (JWNLException e) {
			e.printStackTrace();
		}	
	}
	

	/**
	 * Derives action / business object from verb-object label.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
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
		if (props.hasPhrasalVerb() == false && props.hasConjunction() == true) {
			props.setAction(labelSplit[0] + " and " + labelSplit[props.getIndexConjunctionSplit()+1]);
			
			props.addToMultipleActions(labelSplit[0]);
			props.addToMultipleActions(labelSplit[props.getIndexConjunctionSplit()+1]);
			
			// First BO
			String temp = "";
			for (int j = 1; j <= props.getIndexConjunctionSplit()-1; j++) {
				temp = temp + " " + labelSplit[j];
			}
			temp = temp.trim();
			props.addBOs(temp);
			
			// Second BO
			temp = "";
			for (int j = props.getIndexConjunctionSplit()+2; j <= labelSplit.length - 1; j++) {
				temp = temp + " " + labelSplit[j];
			}
			temp = temp.trim();
			props.addBOs(temp);
			
			assigned = true;
		}

		// If label only contains a single action 
		if (assigned == false) {
			props.setHasConjunction(false);
		}

		if (props.hasPhrasalVerb() == true && props.hasConjunction() == false) {
			props.setAction(labelSplit[0] + " " + labelSplit[1]);
			for (int j = 2; j <= labelSplit.length - 1; j++) {
				props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
			}
		}
		if (props.hasPhrasalVerb() == false && props.hasConjunction() == false) {
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
			if (props.getPrepositions().contains(boSplit[j]) == true) {
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
//		System.out.println("A: " + props.getAction() + " - BO: " + props.getBusinessObject() + " - ADD: " + props.getAdditionalInfo());
	}

	/**
	 * Derives action / business object from labels using a conjunction.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	private void deriveFromConjunctionLabels(String label, String[] labelSplit,
			EnglishLabelProperties props) {
		// Check labels having a preposition + conjunction

		if (props.hasPreposition() == true) {
			props.setAction(getWordBeforeIndex(label, props.getIndexPrep()));
			// If there is an "and" before the first action, there will be
			// additional actions
			if (props.getIndexPrepSplit() - 2 >= 0
					&& labelSplit[props.getIndexPrepSplit() - 2].equals("and")) {
				for (int j = 0; j <= props.getIndexPrepSplit() - 3; j++) {
					props.addToMultipleActions(labelSplit[j].replace(",", ""));
				}
				props.addToMultipleActions(props.getAction());
				int temp = -1;
				// Create business object
				props.setBusinessObject(labelSplit[props.getIndexPrepSplit()]);
				for (int j = props.getIndexPrepSplit() + 1; j <= labelSplit.length - 1; j++) {
					if (props.getPrepositions().contains(labelSplit[j]) == true) {
						temp = j;
						break;
					}
					props.setBusinessObject(props.getBusinessObject() + " "
							+ labelSplit[j]);
				}
				props.setBusinessObject(props.getBusinessObject().trim());
				// Create additional info
				if (temp > -1) {
					for (int j = temp; j <= labelSplit.length - 1; j++) {
						props.setAdditionalInfo(props.getAdditionalInfo() + " "
								+ labelSplit[j]);
					}
					props.setAdditionalInfo(props.getAdditionalInfo().trim());
				}

				// If there is an "/" before the first action, there will be
				// additional actions
			} else if ((props.getIndexPrepSplit() - 2 >= 0 && (labelSplit[props
					.getIndexPrepSplit() - 2].equals(",") || labelSplit[props
					.getIndexPrepSplit() - 2].endsWith(",")))
					|| (props.getIndexPrepSplit() - 2 >= 0 && (labelSplit[props
							.getIndexPrepSplit() - 2].equals("/") || labelSplit[props
							.getIndexPrepSplit() - 2].endsWith("/")))) {
				for (int j = 0; j <= props.getIndexPrepSplit() - 2; j++) {
					if (labelSplit[j].contains("/") == true) {
						props.addToMultipleActions(labelSplit[j].replace(" / ",
								""));
					} else {
						props.addToMultipleActions(labelSplit[j].replace(" , ",
								""));
					}
				}
				props.addToMultipleActions(props.getAction());
				int temp = -1;
				for (int j = props.getIndexPrepSplit() + 1; j <= labelSplit.length - 1; j++) {
					if (props.getPrepositions().contains(labelSplit[j]) == true) {
						temp = j;
						break;
					}
					props.setBusinessObject(props.getBusinessObject() + " "
							+ labelSplit[j]);
				}
				props.setBusinessObject(props.getBusinessObject().trim());
				if (temp > -1) {
					for (int j = temp; j <= labelSplit.length - 1; j++) {
						props.setAdditionalInfo(props.getAdditionalInfo() + " "
								+ labelSplit[j]);
					}
					props.setAdditionalInfo(props.getAdditionalInfo().trim());
				}
				// If the word before the preposition is not an "and"
			} else {
				for (int j = 0; j <= props.getIndexPrepSplit() - 2; j++) {
					props.setBusinessObject(props.getBusinessObject() + " "
							+ labelSplit[j]);
				}
				props.setBusinessObject(props.getBusinessObject().trim());
				for (int j = props.getIndexPrepSplit(); j <= labelSplit.length - 1; j++) {
					props.setAdditionalInfo(props.getAdditionalInfo() + " "
							+ labelSplit[j]);
				}
				props.setAdditionalInfo(props.getAdditionalInfo().trim());
			}
		}

		// Check labels having no preposition but conjunction
		if (props.hasPreposition() == false) {

			// if conjunction element is not placed in the second last position
			if (props.getIndexConjunctionSplit() == labelSplit.length - 2) {
				// If conjunction is implemented using "x/ y"
				if (labelSplit[props.getIndexConjunctionSplit()].contains("/")
						&& label.contains(" /") == false) {
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit()].replace("/", ""));
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit() + 1]);
					for (int j = 0; j <= props.getIndexConjunctionSplit() - 1; j++) {
						props.setBusinessObject(props.getBusinessObject() + " "
								+ labelSplit[j]);
					}
					props.setBusinessObject(props.getBusinessObject().trim());

					// If conjunction is implemented using "x, y"
				} else if (labelSplit[props.getIndexConjunctionSplit()]
						.contains(",")
						&& label.contains(" ,") == false) {
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit()].replace(",", ""));
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit() + 1]);
					for (int j = 0; j <= props.getIndexConjunctionSplit() - 1; j++) {
						props.setBusinessObject(props.getBusinessObject() + " "
								+ labelSplit[j]);
					}
					props.setBusinessObject(props.getBusinessObject().trim());

					// If conjunction is implemented using "x , y" or "x / y" or
					// "x and y"
				} else {
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit() - 1]);
					props.addToMultipleActions(labelSplit[props
							.getIndexConjunctionSplit() + 1]);
					for (int j = 0; j <= props.getIndexConjunctionSplit() - 2; j++) {
						props.setBusinessObject(props.getBusinessObject() + " "
								+ labelSplit[j]);
					}
					props.setBusinessObject(props.getBusinessObject().trim());
				}

				// if conjunction element is not placed in the second last
				// position (indicating a different style)
			} else {
				if (props.getIndexConjunctionSplit() > 0) {

					// Only one conjunction
					if (getConjunctionCount(labelSplit) == 1) {

						// First word is isolated
						if (props.getIndexConjunctionSplit() == 1) {

							// First word can be a verb
							if (lHelper.isVerb(labelSplit[0]) == true) {
								props.addToMultipleActions(labelSplit[0]);

								// Second word is isolated as well
								if (labelSplit.length == 3) {
									props.addToMultipleActions(labelSplit[2]);

									// There is a word group after conjunction
									// index
								} else {
									// If last word is verb
									if (lHelper.isVerb(labelSplit[labelSplit.length - 1]) == true) {
										props
												.addToMultipleActions(labelSplit[labelSplit.length - 1]);
										for (int j = props
												.getIndexConjunctionSplit() + 1; j < labelSplit.length - 2; j++) {
											props.setBusinessObject(props
													.getBusinessObject()
													+ " " + labelSplit[j]);
										}
										// First word of group is assumed to be
										// a verb
									} else {
										props
												.addToMultipleActions(labelSplit[props
														.getIndexConjunctionSplit() + 1]);
										for (int j = props
												.getIndexConjunctionSplit() + 2; j < labelSplit.length - 1; j++) {
											props.setBusinessObject(props
													.getBusinessObject()
													+ " " + labelSplit[j]);
										}
									}
								}

								// First word is no verb --> multiple business
								// objects with verb in the end
							} else {
								props.setAction(labelSplit[props
										.getIndexConjunctionSplit() - 1]);
								for (int j = 0; j < labelSplit.length - 1; j++) {
									props.setBusinessObject(props
											.getBusinessObject()
											+ " " + labelSplit[j]);
								}
							}

							// Word group before conjunction index (multiple
							// actions and BOs)
						} else {
						}

						// Multiple conjunction
					} else {
						// for (int i = 0; i<labelSplit.length; i++) {
						// if (i+1 < labelSplit.length) {
						// if (labelSplit[i+1].equals(",") ||
						// labelSplit[i+1].equals("/") ||
						// labelSplit[i+1].equals("or") ||
						// labelSplit[i+1].equals("and")) {
						// labelSplit[i] = getInfinitiveOfAction(labelSplit[i]);
						// }
						// }
						// props.setBusinessObject(props.getBusinessObject() +
						// " " + labelSplit[i]);
						// }
					}
				}
			}
		}
	}

	public void deriveFromActionNounLabels(EnglishLabelProperties props, String label, String[] labelSplit) throws JWNLException {

		// Derive action/ business for GERUND STYLE labels
		if (props.isGerundStyle() == true && props.hasConjunction() == false
				&& props.isIrregularStyle() == false) {
			deriveFromGerundStyle(label, labelSplit, props);
		}

		// Derive action/ business object for PREPOSITION-'OF' labels
		if (props.hasPrepositionOf() == true && props.hasConjunction() == false
				&& props.isGerundStyle() == false
				&& props.isIrregularStyle() == false) {
			deriveFromPrepositionOf(label, labelSplit, props);
		}

		// Derive action/ business object for NOUN PHRASE labels WITH
		// PREPOSITIONAL PHRASE
		if (props.hasPreposition() == true && props.hasConjunction() == false
				&& props.hasPrepositionOf() == false
				&& props.isGerundStyle() == false
				&& props.isIrregularStyle() == false) {
			deriveFromNounPhraseWithPrepPhrase(label, labelSplit, props);
		}

		// Derive action/ business object for NOUN PHRASE
		if (props.hasConjunction() == false && props.hasPreposition() == false
				&& props.isGerundStyle() == false
				&& props.isIrregularStyle() == false) {
			// } else {
			if (lHelper.hasPluralNounAtEnd(label, labelSplit) == true) {
				deriveFromPluralNounLabel(label, labelSplit, props);
			} else {
				deriveFromNounPhrase(label, labelSplit, props);
			}
			// }
		}

		// Derive action/ business object for CONJUNCTION labels
		if (props.hasConjunction() == true && props.isIrregularStyle() == false) {
			deriveFromConjunctionLabels(label, labelSplit, props);
		}
	}

	/**
	 * Derives from plural action-noun labels.
	 * @param label input label from which is derived
	 * @param labelSplit according split for input label
	 * @param activity according function object
	 * @param props
	 */
	private void deriveFromPluralNounLabel(String label, String[] labelSplit, EnglishLabelProperties props) {
		props.setAction(labelSplit[0]);
		for (int j = 1; j <= labelSplit.length - 1; j++) {
			props.setBusinessObject(props.getBusinessObject() + " "
					+ labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject().trim());
	}

	/**
	 * Derives action and business object from irregular labels (surrounding events required)
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param activity considered function
	 * @param props property object
	 * @throws JWNLException
	 */
	private void deriveFromIrregular(String label, String[] labelSplit, Activity activity, EnglishLabelProperties props) throws JWNLException {
		if (getpossibleVerbs((activity.getLabel())).size() >= 1) {
			ArrayList<String> possibleVerbs = getpossibleVerbs(label);
			props.setAction(possibleVerbs.get(0));
		}
	}
	
	/**
	 * Derives action / business object from noun phrase style label with adjective in the beginning.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 * @throws JWNLException 
	 */
	private void deriveFromNounPhraseADV(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException {
		String adv = lHelper.getAdverb(wordnet.lookupAllIndexWords(labelSplit[0]).getIndexWord(POS.ADJECTIVE).getLemma());
		props.setAction(labelSplit[labelSplit.length-1]);
		for (int j=1; j<=labelSplit.length-2; j++) {
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		
		props.setBusinessObject((props.getBusinessObject() + " " + adv).trim());
	}
	
	/**
	 * Derives action / business object from noun phrase style label.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	private void deriveFromNounPhrase(String label, String[] labelSplit, EnglishLabelProperties props) {
		props.setAction(labelSplit[labelSplit.length-1]);
		for (int j=0; j<=labelSplit.length-2; j++) {
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject().trim());
	}
	
	/**
	 * Derives action / business object from noun phrase style label having a prepositional phrase.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 * @throws JWNLException 
	 */
	private void deriveFromNounPhraseWithPrepPhraseADV(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException {
		String adv = lHelper.getAdverb(wordnet.lookupAllIndexWords(labelSplit[0]).getIndexWord(POS.ADJECTIVE).getLemma());
		props.setAction(getWordBeforeIndex(label, props.getIndexPrep()));
		for (int j=1; j<=props.getIndexPrepSplit()-2; j++) {
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject() + " " + adv);
		props.setBusinessObject(props.getBusinessObject().trim());
		for (int j=props.getIndexPrepSplit(); j<=labelSplit.length-1; j++) {
			props.setAdditionalInfo(props.getAdditionalInfo() + " " + labelSplit[j]);
		}
		props.setAdditionalInfo(props.getAdditionalInfo().trim());
	}
	
	/**
	 * Derives action / business object from noun phrase style label having a prepositional phrase.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	private void deriveFromNounPhraseWithPrepPhrase(String label, String[] labelSplit, EnglishLabelProperties props) {
		props.setAction(getWordBeforeIndex(label, props.getIndexPrep()));
		for (int j=0; j<=props.getIndexPrepSplit()-2; j++) {
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject().trim());
		for (int j=props.getIndexPrepSplit(); j<=labelSplit.length-1; j++) {
			props.setAdditionalInfo(props.getAdditionalInfo() + " " + labelSplit[j]);
		}
		props.setAdditionalInfo(props.getAdditionalInfo().trim());
	}
	
	/**
	 * Derives action / business object from preposition-'of' style label with adverb
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 * @throws JWNLException 
	 */
	private void deriveFromPrepositionOfADV(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException {
		String adv = lHelper.getAdverb(wordnet.lookupAllIndexWords(labelSplit[0]).getIndexWord(POS.ADJECTIVE).getLemma());
		props.setAction(getWordBeforeIndex(label, props.getIndexPrep()));
		int temp = -1;
		for (int j=props.getIndexOf()+1; j<=labelSplit.length-1; j++) {
			if (props.getPrepositions().contains(labelSplit[j]) == true) {
				temp = j;
				break;
			}
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject().trim());
		if (temp > -1) {
			for (int j=temp; j<=labelSplit.length-1; j++) {
				props.setAdditionalInfo(props.getAdditionalInfo() + " " + labelSplit[j]);
			}
			props.setAdditionalInfo(props.getAdditionalInfo().trim() + " " + adv);
		}
	}
	
	/**
	 * Derives action / business object from gerund style label.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 * @throws JWNLException
	 */
	private void deriveFromGerundStyle(String label, String[] labelSplit, EnglishLabelProperties props) throws JWNLException {
			if (labelSplit.length > 1) {
				if (checkForPhrasalVerb(lHelper.getInfinitiveOfAction(labelSplit[0]) + " " + labelSplit[1])) {
					props.setHasPhrasalVerb(true);
				}
			}
			if (props.hasPhrasalVerb() == true) {
				props.setAction(labelSplit[0] + " " + labelSplit[1]);
				for (int j=2; j<=labelSplit.length-1; j++) {
					props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
				}
			} else {
				props.setAction(labelSplit[0]);
				for (int j=1; j<=labelSplit.length-1; j++) {
					props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
				}
			}
			props.setBusinessObject(props.getBusinessObject().trim());
	}
	
	
	/**
	 * Derives action / business object from preposition-'of' style label.
	 * @param label action-noun label
	 * @param labelSplit split action-noun label
	 * @param props property object
	 */
	private void deriveFromPrepositionOf(String label, String[] labelSplit, EnglishLabelProperties props) {
		props.setAction(getWordBeforeIndex(label, props.getIndexPrep()));
		int temp = -1;
		for (int j=props.getIndexOf()+1; j<=labelSplit.length-1; j++) {
			if (props.getPrepositions().contains(labelSplit[j]) == true) {
				temp = j;
				break;
			}
			props.setBusinessObject(props.getBusinessObject() + " " + labelSplit[j]);
		}
		props.setBusinessObject(props.getBusinessObject().trim());
		if (temp > -1) {
			for (int j=temp; j<=labelSplit.length-1; j++) {
				props.setAdditionalInfo(props.getAdditionalInfo() + " " + labelSplit[j]);
			}
			props.setAdditionalInfo(props.getAdditionalInfo().trim());
		}
	}
	
	public void deriveFromDES(String label, String[] labelSplit, Activity activity, EnglishLabelProperties props) {
		String[] taggedSplit = activity.getLabel_Tag().split(" ");
		
		if (taggedSplit[0].contains("VB") || activity.getLabel_Tag().contains("VBZ") == false) {
			props.setAction(labelSplit[0]);
			String BO = "";
			for (int j = 1; j < labelSplit.length; j++) {
				BO = BO + " " + labelSplit[j];
			}
			props.setBusinessObject(BO);
		} else {
			int temp = 0;
			for (int j = 0; j < labelSplit.length; j++) {
				if (taggedSplit[j].contains("VBZ") == true) {
					props.setAction(labelSplit[j]);
					temp = j;
					break;
				}
			}
			String BO = "";
			for (int j = temp+1; j < labelSplit.length; j++) {
				BO = BO + " " + labelSplit[j];
			}
			props.setBusinessObject(BO);
		}
	}

	/**
	 * Returns possible verbs in given label (based on WordNet evaluation)
	 * @param label action-noun label
	 * @return list of possible verbs in given action-noun label
	 * @throws JWNLException
	 */
	private ArrayList<String> getpossibleVerbs(String label)
			throws JWNLException {
		HashSet<String> pVerbs = new HashSet<String>();
		String[] words = label.split(" ");

		// go through each word in order to determine words that may represent
		// an action
		for (int i = 0; i < words.length; i++) {

			// check if word is actually a verb
			if (lHelper.isVerb(words[i])) {
				IndexWord iw = null;
				try {
					iw = wordnet.lookupAllIndexWords(label).getIndexWord(
							POS.VERB);
				} catch (JWNLException e) {
				}
				if (iw == null) {
				} else {
					pVerbs.add(iw.getLemma());
				}
			}

			// check if word is a noun and proceed accordingly
			if (lHelper.isNoun(words[i])) {
				// get all verbs and check which verb is directly derived from
				// noun
				String temp = lHelper.getVerbsFromNoun(words[i]);
				if (temp.equals(words[i]) == false) {
					pVerbs.add(temp);
				}
			}
		}
		return new ArrayList<String>(pVerbs);
	}

	private int getConjunctionCount(String[] labelSplit) {
		int count = 0;
		for (String part: labelSplit) {
			if (part.contains(",")) count++;
			if (part.contains("/")) count++; 
			if (part.equals("and")) count++;
			if (part.equals("or")) count++;
		}
		return count;
	}
	
	/**
	 *  Returns next word before given index
	 * @param label action-noun label
	 * @param index index in action-noun label
	 * @return
	 */
	private String getWordBeforeIndex(String label, int index) {
		int nextBlank = label.substring(0, index).lastIndexOf(" ");
		if (nextBlank < 0) {
			return label.substring(0,index);
		} else {
			return label.substring(nextBlank+1,index);
		}
	}
	
	/**
	 * Returns true if given word is a two word verb
	 * @param word considered verb which might contain 2 words
	 * @return true if given word is a phrasal verb
	 * @throws JWNLException
	 */
	private boolean checkForPhrasalVerb(String word) throws JWNLException {
		IndexWord indexWord = wordnet.getIndexWord(POS.VERB,word);
			if (indexWord != null) {
				if (Arrays.asList(EnglishLabelProperties.elements).contains(word.split(" ")[1]) == true) {
					return false;
				}
				return true;
			} else {
				return false;
		}
	}

	public ArrayList<String> returnActions() {
		return actions;
	}

	public ArrayList<String> returnBusinessObjects() {
		return businessObjects;
	}
	
	public String returnAddition() {
		return addition;
	}
}
