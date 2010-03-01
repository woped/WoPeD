package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.translations.Messages;

/**
 * shows token details
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
@SuppressWarnings("serial")
public class TokenPage extends BeginnerPanel {

	int numInnerTokens, numWrongSourcePlaceTokens;

	public TokenPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages.getString("AnalysisSideBar.Beginner.TokenAnalysis"));

		numWrongSourcePlaceTokens = qualanalysisService.getNumWrongSourcePlaceTokens();
		numInnerTokens = qualanalysisService.getNumInnerTokens();

		if (numInnerTokens != 0 || numWrongSourcePlaceTokens != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		if(numWrongSourcePlaceTokens != 0){
			createEntry("Analysis.Tree.NumWrongSourcePlaceTokens",
					qualanalysisService.getWrongSourcePlaceTokensIterator(),
					numWrongSourcePlaceTokens,
					"AnalysisSideBar.Beginner.Help.WrongSourcePlaceTokens",
					"AnalysisSideBar.Beginner.Example.WrongSourcePlaceTokens");
		}
		
		if(numInnerTokens != 0){
			createEntry("Analysis.Tree.NumInnerTokens",
					qualanalysisService.getInnerTokensIterator(),
					numInnerTokens,
					"AnalysisSideBar.Beginner.Help.InnerTokens",
					"AnalysisSideBar.Beginner.Example.InnerTokens");
		}
		createEmptyEntry();
	}

}
