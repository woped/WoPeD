package org.woped.qualanalysis.sidebar.assistant;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.qualanalysis.sidebar.assistant.components.ClickLabel;
import org.woped.qualanalysis.sidebar.assistant.components.DetailsMouseListener;
import org.woped.qualanalysis.sidebar.assistant.components.SimpleGridBagLayout;
import org.woped.qualanalysis.sidebar.assistant.pages.SoundnessPage;
import org.woped.qualanalysis.sidebar.assistant.pages.WorkflowPage;
import org.woped.translations.Messages;

/**
 * shows the analysis startpage with the overview of soundness analysis, workflow analysis and the net statistics
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class StartPage extends BeginnerPanel {

	private static final String COLON = ":";

	public StartPage(SideBar sideBar) {
		super(null, sideBar, Messages.getString("AnalysisSideBar.Beginner.Startpage"));

		SimpleGridBagLayout sgbl = new SimpleGridBagLayout();

		JPanel analysisPanel = new JPanel(sgbl);
		analysisPanel.setBackground(Color.WHITE);

		JLabel correctness = null;

		/*
		 * Workflow
		 */

		// add workflow-label
		JLabel workflowLabel = new JLabel(Messages.getString("AnalysisSideBar.Beginner.WorkflowAnalysis"));
		workflowLabel.setFont(SUBHEADER_FONT);
		workflowLabel.setBorder(BOTTOM_BORDER);
		sgbl.addComponent(analysisPanel, workflowLabel, 0, 0, 1, 1, 1, 0);

		// add workflow info
		if (qualanalysisService.isWorkflowNet()) {
			// create "correct"-icon
			correctness = new JLabel(Messages.getImageIcon(CORRECT_ICON));
		} else {
			// create workflow-page with further information
			WorkflowPage workflowPage = new WorkflowPage(this, sideBar);
			status = false;
			// create "incorrect"-icon
			correctness = new JLabel(Messages.getImageIcon(INCORRECT_ICON));
			// add detail-button
			JLabel workflowDetails = new JLabel(Messages.getImageIcon(DETAILS_ICON));
			workflowDetails.addMouseListener(new DetailsMouseListener(sideBar, workflowPage));
			workflowDetails.setBorder(BOTTOM_BORDER);
			sgbl.addComponent(analysisPanel, workflowDetails, 2, 0, 1, 1, 0, 0);
		}
		// add icon
		correctness.setBorder(BOTTOM_RIGHT_BORDER);
		sgbl.addComponent(analysisPanel, correctness, 1, 0, 1, 1, 0, 0);

		/*
		 * Soundness
		 */

		// display only if workflow-property is given
		if (qualanalysisService.isWorkflowNet()) {

			// add soundness-label
			JLabel soundnessLabel = new JLabel(Messages.getString("AnalysisSideBar.Beginner.SoundnessAnalysis"));
			soundnessLabel.setFont(SUBHEADER_FONT);
			soundnessLabel.setBorder(BOTTOM_BORDER);
			sgbl.addComponent(analysisPanel, soundnessLabel, 0, 1, 1, 1, 1, 1);

			// add soundness info
			// create coundness-page with further information
			SoundnessPage soundnessPage = new SoundnessPage(this, sideBar);
			if (soundnessPage.getWarningStatus()) {
				// create "correct"-icon
				correctness = new JLabel(Messages.getImageIcon(CORRECT_ICON));
			} else {
				if (!soundnessPage.getErrorStatus()) {
					// create "incorrect"-icon
					correctness = new JLabel(Messages.getImageIcon(INCORRECT_ICON));
				} else {
					// create "warning"-icon
					correctness = new JLabel(Messages.getImageIcon(WARNING_ICON));
				}
				
				// add detail-button
				JLabel soundnessDetails = new JLabel(Messages.getImageIcon(DETAILS_ICON));
				soundnessDetails.addMouseListener(new DetailsMouseListener(sideBar, soundnessPage));
				soundnessDetails.setBorder(BOTTOM_BORDER);
				sgbl.addComponent(analysisPanel, soundnessDetails, 2, 1, 1, 1, 0, 1);
			}
			// add icon
			correctness.setBorder(BOTTOM_RIGHT_BORDER);
			sgbl.addComponent(analysisPanel, correctness, 1, 1, 1, 1, 0, 1);
		}

		/*
		 * no token in source-place
		 */

		// check only for workflow-net
		if(qualanalysisService.isWorkflowNet()){
			// display only if there is no token in the source place
			if (qualanalysisService.getNumEmptySourcePlaces() > 0) {
				// add emptySourcePlace-label
				JLabel emptySourcePlaceLabel = new JLabel(Messages.getString("Analysis.Tree.EmptySourcePlaces"));
				emptySourcePlaceLabel.setFont(SUBHEADER_FONT);
				emptySourcePlaceLabel.setBorder(BOTTOM_BORDER);
				sgbl.addComponent(analysisPanel, emptySourcePlaceLabel, 0, 2, 1, 1, 1, 0);
				// add emptySourcePlace-icon with toolTip
				JLabel emptySourcePlaceIcon = new JLabel(Messages.getImageIcon(WARNING_ICON));
				emptySourcePlaceIcon.setBorder(BOTTOM_RIGHT_BORDER);
				emptySourcePlaceIcon.setToolTipText(Messages.getString("Analysis.Tree.EmptySourcePlaces.Info"));
				sgbl.addComponent(analysisPanel, emptySourcePlaceIcon, 1, 2, 1, 1, 0, 0);
			}
		}

		addComponent(analysisPanel, 0, 0, 1, 1, 1, 0);

		// emptyLabel

		addComponent(new JLabel(), 0, 1, 1, 1, 1, 1);

		/*
		 * net info
		 */

		JPanel infoPanel = new JPanel();
		ClickLabel clickLabel = null;
		JLabel count = null;
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		infoPanel.setLayout(sgbl);

		// header
		JLabel netStatistic = new JLabel(Messages.getString("AnalysisSideBar.Beginner.Netstatistics"));
		netStatistic.setFont(HEADER_FONT);
		netStatistic.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		sgbl.addComponent(infoPanel, netStatistic, 0, 0, 1, 1, 1, 0);

		// places
		clickLabel = new ClickLabel(Messages.getString("Analysis.Tree.NumPlaces") + COLON, qualanalysisService
				.getPlacesIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 1, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumPlaces()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 1, 1, 1, 0, 0);

		// transitions
		clickLabel = new ClickLabel(Messages.getString("Analysis.Tree.NumTransitions") + COLON, qualanalysisService
				.getTransitionsIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 2, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumTransitions()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 2, 1, 1, 0, 0);

		// operators
		clickLabel = new ClickLabel(SUB_POINT + Messages.getString("Analysis.Tree.NumOperators") + COLON,
				qualanalysisService.getOperatorsIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 3, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumOperators()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 3, 1, 1, 0, 0);

		// subprocesses
		clickLabel = new ClickLabel(SUB_POINT + Messages.getString("Analysis.Tree.NumSubprocesses") + COLON,
				qualanalysisService.getSubprocessesIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 4, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumSubprocesses()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 4, 1, 1, 0, 0);

		// arcs
		JLabel arcLabel = new JLabel(Messages.getString("Analysis.Tree.NumArcs") + COLON);
		arcLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, arcLabel, 0, 5, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumArcs()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 5, 1, 1, 0, 0);

		addComponent(infoPanel, 0, 2, 1, 1, 1, 0);
	}

	@Override
	public void addComponents() {
	}
}