package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.translations.Messages;

/**
 * shows wellstructuredness details
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class WellstructurednessPage extends BeginnerPanel {

	private int numPTHandles, numTPHandles;

	public WellstructurednessPage(BeginnerPanel previous, SideBar sideBar) {
		super(
				previous,
				sideBar,
				Messages
						.getString("AnalysisSideBar.Beginner.WellstructurednessAnalysis"));
		numPTHandles = qualanalysisService.getNumPTHandles();

		numTPHandles = qualanalysisService.getNumTPHandles();

		if (numPTHandles != 0 || numTPHandles != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		if (numPTHandles != 0) {
			createEntry("Analysis.Tree.NumPTHandles", qualanalysisService
					.getPTHandlesIterator(), numPTHandles,
					"AnalysisSideBar.Beginner.Help.PTHandles",
					"AnalysisSideBar.Beginner.Example.PTHandles");
		}

		if (numTPHandles != 0) {
			createEntry("Analysis.Tree.NumTPHandles", qualanalysisService
					.getTPHandlesIterator(), numTPHandles,
					"AnalysisSideBar.Beginner.Help.TPHandles",
					"AnalysisSideBar.Beginner.Example.TPHandles");
		}
		createEmptyEntry();
	}
}
