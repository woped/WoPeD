package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.qualanalysis.sidebar.assistant.components.DetailsMouseListener;
import org.woped.translations.Messages;

/**
 * shows soundness overview
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class SoundnessPage extends BeginnerPanel {

	private boolean errorStatus = true;

	private boolean warningStatus = true;

	private DetailsMouseListener boundDetailsListener, liveDetailsListener,
			wellstructuredDetailsListener, freechoiceDetailsListener;

	private BeginnerPanel boundednessPage, livenessPage,
			wellstructurednessPage, freechoicePage;

	public SoundnessPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString("AnalysisSideBar.Beginner.SoundnessAnalysis"));

		boundednessPage = new BoundednessPage(this, sideBar);
		boundDetailsListener = new DetailsMouseListener(sideBar,
				boundednessPage);
		
		livenessPage = new LivenessPage(this, sideBar);
		liveDetailsListener = new DetailsMouseListener(sideBar, livenessPage);
		
		wellstructurednessPage = new WellstructurednessPage(this, sideBar);
		wellstructuredDetailsListener = new DetailsMouseListener(sideBar,
				wellstructurednessPage);
		
		freechoicePage = new FreechoicePage(this, sideBar);
		freechoiceDetailsListener = new DetailsMouseListener(sideBar,
				freechoicePage);

		if (!boundednessPage.getStatus() || !livenessPage.getStatus()
				|| !wellstructurednessPage.getStatus()
				|| !freechoicePage.getStatus()) {
			// set status values
			warningStatus = false;
			if (!boundednessPage.getStatus() || !livenessPage.getStatus())
				errorStatus = false;
		}

	}

	public boolean getErrorStatus() {
		return errorStatus;
	}

	public boolean getWarningStatus() {
		return warningStatus;
	}

	@Override
	public void addComponents() {
		// boundedness

		createAnalysisEntry("AnalysisSideBar.Beginner.BoundednessAnalysis",
				boundDetailsListener, boundednessPage.getStatus());

		// liveness

		createAnalysisEntry("AnalysisSideBar.Beginner.LivenessAnalysis",
				liveDetailsListener, livenessPage.getStatus());

		// wellstructuredness

		createAnalysisEntry(
				"AnalysisSideBar.Beginner.WellstructurednessAnalysis",
				wellstructuredDetailsListener, wellstructurednessPage
						.getStatus());

		// freechoice

		createAnalysisEntry("AnalysisSideBar.Beginner.FreeChoiceAnalysis",
				freechoiceDetailsListener, freechoicePage.getStatus());

		createEmptyEntry();
	}
}
