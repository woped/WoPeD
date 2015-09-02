package sentencePlanning;

import java.util.ArrayList;
import java.util.Iterator;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import contentDetermination.labelAnalysis.EnglishLabelHelper;
import dataModel.dsynt.DSynTMainSentence;
import dataModel.dsynt.DSynTSentence;
import dataModel.intermediate.ExecutableFragment;
import dataModel.process.ProcessModel;

public class ReferringExpressionGenerator {
	
	private EnglishLabelHelper lHelper;
	
	
	public ReferringExpressionGenerator(EnglishLabelHelper lHelper) {
		this.lHelper = lHelper;
	}
	
	
	public ArrayList<DSynTSentence> insertReferringExpressions(ArrayList<DSynTSentence> textPlan, ProcessModel process, boolean male) {
		
		String prevRole = null;
		ExecutableFragment prevFragment = null;
		DSynTSentence prevSentence = null;
		
		for (int i = 0; i < textPlan.size(); i++) {
			// Determine current role
			String currRole = textPlan.get(i).getExecutableFragment().getRole(); 
			ExecutableFragment currFragment = textPlan.get(i).getExecutableFragment();
			DSynTSentence currSentence = textPlan.get(i);
			
			if (prevRole != null && prevFragment != null && prevSentence != null) {
				
				if (currRole.equals(prevRole) && 
						!currRole.equals("") && !currRole.equals("he") && !currRole.equals("she") && !currRole.equals("it") &&
						!currFragment.sen_hasBullet && currFragment.sen_level == prevFragment.sen_level &&
						prevSentence.getExecutableFragment().getListSize() == 0 && 
						!currFragment.sen_hasConnective && !prevFragment.sen_hasConnective &&
					    currSentence.getClass().toString().endsWith("DSynTMainSentence") &&
						prevSentence.getClass().toString().endsWith("DSynTMainSentence")) {
					
					// Insert referring expression
					if (isPerson(currRole)) {
						if (male) {
							textPlan.get(i).getExecutableFragment().setRole("he");
						} else {
							textPlan.get(i).getExecutableFragment().setRole("she");
						}
					} else {
						textPlan.get(i).getExecutableFragment().setRole("it");
					}
				
					((DSynTMainSentence) textPlan.get(i)).changeRole();
					System.out.println("Referring Expression inserted: " + textPlan.get(i).getExecutableFragment().getAction() + " - " + textPlan.get(i).getExecutableFragment().getBo());
					prevRole = null;
					prevFragment = null;
					prevSentence = null;
				}
			} else {
				prevRole = currRole;
				prevFragment = currFragment;
				prevSentence = currSentence;	
			}
		}
		return textPlan;
	}
	
	// Checks WordNet HypernymTree whether "role" is a person
	private boolean isPerson(String role) {
		
		try {
			IndexWord word= lHelper.getDictionary().getIndexWord(POS.NOUN, role.toLowerCase());
			if (word != null) {
				Synset[] senses = word.getSenses();
				for (Synset sense: senses) {
					PointerTargetTree relatedTree = PointerUtils.getInstance().getHypernymTree(sense);
					PointerTargetNodeList[] relatedLists = relatedTree.reverse();
					for (PointerTargetNodeList relatedList: relatedLists) {
						Iterator iterator = relatedList.iterator();
						while (iterator.hasNext()) {
							PointerTargetNode elem = (PointerTargetNode) iterator.next();
							Synset syns = elem.getSynset();
							for (int j = 0; j < syns.getWords().length; j++) {
								if (syns.getWord(j).getLemma().equals("person") == true) {
									return true;
								}
							}
						}
					}
				}
			}
		} catch (JWNLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
