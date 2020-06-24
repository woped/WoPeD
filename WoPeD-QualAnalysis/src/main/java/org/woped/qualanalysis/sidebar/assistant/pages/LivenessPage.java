package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;

/**
 * class for the LivenessPage of the analysis sidebar
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 *
 */
@SuppressWarnings("serial")
public class LivenessPage extends BeginnerPanel {

	private int numDeadTransitions, numNonLiveTransitions;

	/**
	 * 
	 * @param previous - reference to the previous page for back button
	 * @param sideBar - reference to the sidebar
	 */
	public LivenessPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString(PREFIX + "LivenessAnalysis"));

		numDeadTransitions = qualanalysisService.getDeadTransitions().size();

		numNonLiveTransitions = qualanalysisService.getNonLiveTransitions().size();
		
		if (numDeadTransitions != 0 || numNonLiveTransitions != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		if (numDeadTransitions != 0) {
			createEntry(PREFIX + "NumDeadTransitions",
					qualanalysisService.getDeadTransitions().iterator(),
					numDeadTransitions,
					PREFIX_HELP + "DeadTransitions",
					PREFIX_EXAMPLE + "DeadTransitions");
		}
		
		if (numNonLiveTransitions != 0) {
			createEntry(PREFIX + "NumNonLiveTransitions",
					qualanalysisService.getNonLiveTransitions().iterator(),
					numNonLiveTransitions,
					PREFIX_HELP + "NonLiveTransitions");
		}
		createEmptyEntry();
	}
}
