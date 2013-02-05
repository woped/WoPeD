package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.gui.translations.Messages;

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
						.getString(PREFIX + "WellStructurednessAnalysis"));
		numPTHandles = qualanalysisService.getPTHandles().size();

		numTPHandles = qualanalysisService.getTPHandles().size();

		if (numPTHandles != 0 || numTPHandles != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		if (numPTHandles != 0) {
			createEntry(PREFIX + "NumPTHandles", qualanalysisService
					.getPTHandles().iterator(), numPTHandles,
					PREFIX_HELP + "PTHandles",
					PREFIX_EXAMPLE + "PTHandles");
		}

		if (numTPHandles != 0) {
			createEntry(PREFIX + "NumTPHandles", qualanalysisService
					.getTPHandles().iterator(), numTPHandles,
					PREFIX_HELP + "TPHandles",
					PREFIX_EXAMPLE + "TPHandles");
		}
		createEmptyEntry();
	}
}
