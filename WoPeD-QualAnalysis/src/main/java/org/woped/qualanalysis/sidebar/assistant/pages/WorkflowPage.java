package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;

/**
 * shows workflow details
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class WorkflowPage extends BeginnerPanel {

	private int numSourcePlaces, numSinkPlaces, numSourceTransitions, numSinkTransitions,
			numNotConnectedNodes, numNotStronglyConnectedNodes, numArcViolations;

	public WorkflowPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString(PREFIX + "WorkflowAnalysis"));
		numSourcePlaces = qualanalysisService.getSourcePlaces().size();

		numSinkPlaces = qualanalysisService.getSinkPlaces().size();

		numSourceTransitions = qualanalysisService.getSourceTransitions().size();

		numSinkTransitions = qualanalysisService.getSinkTransitions().size();

		numNotConnectedNodes = qualanalysisService.getNotConnectedNodes().size();

		numNotStronglyConnectedNodes = qualanalysisService.getNotStronglyConnectedNodes().size();

		numArcViolations = qualanalysisService.getArcWeightViolations().size();

		if (!qualanalysisService.isWorkflowNet()) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		// Number of Source Places
		if (numSourcePlaces != 1) {
			createEntry(PREFIX + "NumSourcePlaces", qualanalysisService
					.getSourcePlaces().iterator(), numSourcePlaces,
					PREFIX_HELP + "SourcePlaces",
					PREFIX_EXAMPLE + "SourcePlaces");
		}

		// Number of Sink Places
		if (numSinkPlaces != 1) {
			createEntry(PREFIX + "NumSinkPlaces", qualanalysisService
					.getSinkPlaces().iterator(), numSinkPlaces,
					PREFIX_HELP + "SinkPlaces",
					PREFIX_EXAMPLE + "SinkPlaces");
		}

		// Number of Source Transitions
		if (numSourceTransitions != 0) {
			createEntry(PREFIX + "NumSourceTransitions", qualanalysisService
					.getSourceTransitions().iterator(), numSourceTransitions,
					PREFIX_HELP + "SourceTransitions",
					PREFIX_EXAMPLE + "SourceTransitions");
		}

		// Number of Sink Transitions
		if (numSinkTransitions != 0) {
			createEntry(PREFIX + "NumSinkTransitions", qualanalysisService
					.getSinkTransitions().iterator(), numSinkTransitions,
					PREFIX_HELP + "SinkTransitions",
					PREFIX_EXAMPLE + "SinkTransitions");
		}

		// Number of Isolated nodes
		if (numNotConnectedNodes != 0) {
			createEntry(PREFIX + "NumUnconnectedNodes",
					qualanalysisService.getNotConnectedNodes().iterator(),
					numNotConnectedNodes,
					PREFIX_HELP + "UnconnectedNodes");
		}

		// Number of not strongly connected nodes
		if (numNotStronglyConnectedNodes != 0) {
			createEntry(PREFIX + "NumNotStronglyConnectedNodes",
					qualanalysisService.getNotStronglyConnectedNodes().iterator(),
					numNotStronglyConnectedNodes,
					PREFIX_HELP + "NotStronglyConnectedNodes");
		}

		if(numArcViolations != 0){
			createEntry(PREFIX + "NumArcWeightViolations", qualanalysisService.getArcWeightViolations()
					, PREFIX_HELP +"NumArcWeightViolations");
		}

		createEmptyEntry();
	}


}
