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

	int numInnerTokens, numEmptySourcePlaces;

	public TokenPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages.getString("AnalysisSideBar.Beginner.TokenAnalysis"));

		numEmptySourcePlaces = qualanalysisService.getNumEmptySourcePlaces();
		numInnerTokens = qualanalysisService.getNumInnerTokens();

		if (numInnerTokens != 0 || numEmptySourcePlaces != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		if(numEmptySourcePlaces != 0){
			createEntry("Analysis.Tree.NumEmptySourcePlaces",
					qualanalysisService.getEmptySourcePlacesIterator(),
					numEmptySourcePlaces,
					"AnalysisSideBar.Beginner.Help.EmptySourcePlaces",
					"AnalysisSideBar.Beginner.Example.EmptySourcePlaces");
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
