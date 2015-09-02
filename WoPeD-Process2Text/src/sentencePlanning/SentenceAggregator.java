package sentencePlanning;

import java.util.ArrayList;

import contentDetermination.labelAnalysis.EnglishLabelHelper;
import dataModel.dsynt.DSynTMainSentence;
import dataModel.dsynt.DSynTSentence;
import dataModel.intermediate.ExecutableFragment;
import dataModel.process.ProcessModel;

public class SentenceAggregator {
	
	private EnglishLabelHelper lHelper;
	
	public SentenceAggregator(EnglishLabelHelper lHelper) {
		this.lHelper = lHelper;
	}
	
	public ArrayList<DSynTSentence> performRoleAggregation(ArrayList<DSynTSentence> textPlan, ProcessModel process) {
		
		ArrayList<Integer> toBeDeleted = new ArrayList<Integer>();
		
		String prevRole = null;
		ExecutableFragment prevFragment = null;
		DSynTSentence prevSentence = null;
		int deleteCount = 0;
		
		for (int i = 0; i < textPlan.size(); i++) {
			
			// Determine current role
			String currRole = textPlan.get(i).getExecutableFragment().getRole(); 
			ExecutableFragment currFragment = textPlan.get(i).getExecutableFragment();
			DSynTSentence currSentence = textPlan.get(i);
			
			if (i > 1 && prevRole != null && prevFragment != null && prevSentence != null) {
				
				if (currRole.equals(prevRole) && currRole.equals("") == false && currFragment.sen_hasBullet == false && currFragment.sen_level == prevFragment.sen_level &&
						prevSentence.getExecutableFragment().getListSize() == 0 && 
						currFragment.sen_hasConnective == false && prevFragment.sen_hasConnective == false &&
					    currSentence.getClass().toString().equals("class dataModel.dsynt.DSynTMainSentence") &&
						prevSentence.getClass().toString().equals("class dataModel.dsynt.DSynTMainSentence")) {
					
					// Create list with sentences which need to be aggregated with the current one
					ArrayList<DSynTMainSentence> coordSentences = new ArrayList<DSynTMainSentence>();
					coordSentences.add((DSynTMainSentence) currSentence);
					
					// Conduct role aggregation
					((DSynTMainSentence) prevSentence).addCoordSentences(coordSentences);
					toBeDeleted.add(i-deleteCount);
					deleteCount++;
					
					System.out.println("Aggregated: " + textPlan.get(i).getExecutableFragment().getAction() + " - " + textPlan.get(i).getExecutableFragment().getBo());
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
		for (int i: toBeDeleted) {
			textPlan.remove(i);
		}
		return textPlan;
	}
}
