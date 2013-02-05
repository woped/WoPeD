package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.gui.translations.Messages;

/**
 * shows token details
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
@SuppressWarnings("serial")
public class TokenPage extends BeginnerPanel {

	int numWronglyMarkedPlaces;

	public TokenPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages.getString(PREFIX + "InitialMarkingAnalysis"));

		numWronglyMarkedPlaces = qualanalysisService.getWronglyMarkedPlaces().size();

		if (numWronglyMarkedPlaces != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		createEntry(PREFIX + "NumWronglyMarkedPlaces",
				qualanalysisService.getWronglyMarkedPlaces().iterator(),
				numWronglyMarkedPlaces,
				PREFIX_HELP + "WronglyMarkedPlaces");
		createEmptyEntry();
	}

}
