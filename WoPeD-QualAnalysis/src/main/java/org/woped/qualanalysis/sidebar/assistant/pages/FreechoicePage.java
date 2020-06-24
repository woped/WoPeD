package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;

/**
 * shows freechoice details
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class FreechoicePage extends BeginnerPanel {

	private int numFreeChoiceViolations;

	public FreechoicePage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString(PREFIX + "NumFreeChoiceViolations"));

		numFreeChoiceViolations = qualanalysisService.getFreeChoiceViolations().size();

		if (numFreeChoiceViolations != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		createEntry(PREFIX + "NumFreeChoiceViolations",
				qualanalysisService.getFreeChoiceViolations().iterator(),
				numFreeChoiceViolations,
				PREFIX_HELP + "FreeChoiceViolations");
		createEmptyEntry();
	}
}