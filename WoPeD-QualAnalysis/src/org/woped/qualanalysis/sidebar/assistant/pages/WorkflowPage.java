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

	private int numSourcePlaces, numSinkPlaces, numSourceTrans, numSinkTrans,
			numIsolatedNodes, numNotStronglyConnectedNodes;

	public WorkflowPage(BeginnerPanel previous, SideBar sideBar) {
		super(previous, sideBar, Messages
				.getString("AnalysisSideBar.Beginner.WorkflowAnalysis"));
		numSourcePlaces = qualanalysisService.getNumSourcePlaces();

		numSinkPlaces = qualanalysisService.getNumSinkPlaces();

		numSourceTrans = qualanalysisService.getNumSourceTransitions();

		numSinkTrans = qualanalysisService.getNumSinkTransitions();

		numIsolatedNodes = qualanalysisService.getNumNotConnectedNodes();

		numNotStronglyConnectedNodes = qualanalysisService
				.getNumNotStronglyConnectedNodes();

		if (numSourcePlaces != 1 || numSinkPlaces != 1 || numSourceTrans != 0
				|| numSinkTrans != 0 || numIsolatedNodes != 0
				|| numNotStronglyConnectedNodes != 0) {
			status = false;
		}
	}

	@Override
	public void addComponents() {
		// Number of Source Places
		if (numSourcePlaces != 1) {
			createEntry("Analysis.Tree.NumSourcePlaces", qualanalysisService
					.getSourcePlacesIterator(), numSourcePlaces,
					"AnalysisSideBar.Beginner.Help.Sources",
					"AnalysisSideBar.Beginner.Example.Source");
		}

		// Number of Sink Places
		if (numSinkPlaces != 1) {
			createEntry("Analysis.Tree.NumSinkPlaces", qualanalysisService
					.getSinkPlacesIterator(), numSinkPlaces,
					"AnalysisSideBar.Beginner.Help.Sinks",
					"AnalysisSideBar.Beginner.Example.Sink");
		}

		// Number of Source Transitions
		if (numSourceTrans != 0) {
			createEntry("Analysis.Tree.NumSourceTrans", qualanalysisService
					.getSourceTransitionsIterator(), numSourceTrans,
					"AnalysisSideBar.Beginner.Help.SourceTrans",
					"AnalysisSideBar.Beginner.Example.SourceTrans");
		}

		// Number of Sink Transitions
		if (numSinkTrans != 0) {
			createEntry("Analysis.Tree.NumSinkTrans", qualanalysisService
					.getSinkTransitionsIterator(), numSinkTrans,
					"AnalysisSideBar.Beginner.Help.SinkTrans",
					"AnalysisSideBar.Beginner.Example.SinkTrans");
		}

		// Number of Isolated nodes
		if (numIsolatedNodes != 0) {
			createEntry("Analysis.Tree.NumUnconnectedNodes",
					qualanalysisService.getNotConnectedNodesIterator(),
					numIsolatedNodes,
					"AnalysisSideBar.Beginner.Help.NotConnectedNodes");
		}

		// Number of not strongly connected nodes
		if (numNotStronglyConnectedNodes != 0) {
			createEntry("Analysis.Tree.NumNotStronglyConnectedNodes",
					qualanalysisService.getNotStronglyConnectedNodesIterator(),
					numNotStronglyConnectedNodes,
					"AnalysisSideBar.Beginner.Help.NotStronglyConnectedNodes");
		}

		createEmptyEntry();
	}
}
