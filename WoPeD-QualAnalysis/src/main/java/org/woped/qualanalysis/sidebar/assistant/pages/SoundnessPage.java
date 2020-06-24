package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.qualanalysis.sidebar.assistant.components.DetailsMouseListener;

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

	private DetailsMouseListener tokenDetailsListener, boundDetailsListener, liveDetailsListener,
			wellstructuredDetailsListener, freechoiceDetailsListener;

	private BeginnerPanel tokenPage, boundednessPage, livenessPage,
			wellstructurednessPage, freechoicePage;

	public SoundnessPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString(PREFIX + "SoundnessAnalysis"));

		tokenPage = new TokenPage(this, sideBar);
		tokenDetailsListener = new DetailsMouseListener(sideBar,
				tokenPage);
		
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

		if (!qualanalysisService.isSound() || !wellstructurednessPage.getStatus() || !freechoicePage.getStatus()) {
			// set status values
			warningStatus = false;
			if (!qualanalysisService.isSound())
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
		// inner-tokens

		createAnalysisEntry(PREFIX + "InitialMarkingAnalysis",
				tokenDetailsListener, tokenPage.getStatus());
		
		// boundedness

		createAnalysisEntry(PREFIX + "BoundednessAnalysis",
				boundDetailsListener, boundednessPage.getStatus());

		// liveness

		createAnalysisEntry(PREFIX + "LivenessAnalysis",
				liveDetailsListener, livenessPage.getStatus());

		// wellstructuredness

		createAnalysisEntry(PREFIX + "WellStructurednessAnalysis",
				wellstructuredDetailsListener, wellstructurednessPage
						.getStatus());

		// freechoice

		createAnalysisEntry(PREFIX_BEGINNER + "FreeChoiceAnalysis",
				freechoiceDetailsListener, freechoicePage.getStatus());

		createEmptyEntry();
	}
}
