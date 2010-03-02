package org.woped.qualanalysis.sidebar.assistant.pages;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.translations.Messages;

/**
 * shows workflow details
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class WorkflowPage extends BeginnerPanel {

	private int numSourcePlaces, numSinkPlaces, numSourceTransitions, numSinkTransitions,
			numNotConnectedNodes, numNotStronglyConnectedNodes;

	public WorkflowPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString(PREFIX + "WorkflowAnalysis"));
		numSourcePlaces = qualanalysisService.getNumSourcePlaces();

		numSinkPlaces = qualanalysisService.getNumSinkPlaces();

		numSourceTransitions = qualanalysisService.getNumSourceTransitions();

		numSinkTransitions = qualanalysisService.getNumSinkTransitions();

		numNotConnectedNodes = qualanalysisService.getNumNotConnectedNodes();

		numNotStronglyConnectedNodes = qualanalysisService
				.getNumNotStronglyConnectedNodes();

		if (!qualanalysisService.isWorkflowNet()) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		// Number of Source Places
		if (numSourcePlaces != 1) {
			createEntry(PREFIX + "NumSourcePlaces", qualanalysisService
					.getSourcePlacesIterator(), numSourcePlaces,
					PREFIX_HELP + "SourcePlaces",
					PREFIX_EXAMPLE + "SourcePlaces");
		}

		// Number of Sink Places
		if (numSinkPlaces != 1) {
			createEntry(PREFIX + "NumSinkPlaces", qualanalysisService
					.getSinkPlacesIterator(), numSinkPlaces,
					PREFIX_HELP + "SinkPlaces",
					PREFIX_EXAMPLE + "SinkPlaces");
		}

		// Number of Source Transitions
		if (numSourceTransitions != 0) {
			createEntry(PREFIX + "NumSourceTransitions", qualanalysisService
					.getSourceTransitionsIterator(), numSourceTransitions,
					PREFIX_HELP + "SourceTransitions",
					PREFIX_EXAMPLE + "SourceTransitions");
		}

		// Number of Sink Transitions
		if (numSinkTransitions != 0) {
			createEntry(PREFIX + "NumSinkTransitions", qualanalysisService
					.getSinkTransitionsIterator(), numSinkTransitions,
					PREFIX_HELP + "SinkTransitions",
					PREFIX_EXAMPLE + "SinkTransitions");
		}

		// Number of Isolated nodes
		if (numNotConnectedNodes != 0) {
			createEntry(PREFIX + "NumUnconnectedNodes",
					qualanalysisService.getNotConnectedNodesIterator(),
					numNotConnectedNodes,
					PREFIX_HELP + "UnconnectedNodes");
		}

		// Number of not strongly connected nodes
		if (numNotStronglyConnectedNodes != 0) {
			createEntry(PREFIX + "NumNotStronglyConnectedNodes",
					qualanalysisService.getNotStronglyConnectedNodesIterator(),
					numNotStronglyConnectedNodes,
					PREFIX_HELP + "NotStronglyConnectedNodes");
		}

		createEmptyEntry();
	}
}
