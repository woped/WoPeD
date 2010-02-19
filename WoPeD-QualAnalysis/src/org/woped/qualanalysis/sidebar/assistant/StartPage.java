package org.woped.qualanalysis.sidebar.assistant;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.qualanalysis.sidebar.assistant.components.ClickLabel;
import org.woped.qualanalysis.sidebar.assistant.components.DetailsMouseListener;
import org.woped.qualanalysis.sidebar.assistant.components.SimpleGridBagLayout;
import org.woped.qualanalysis.sidebar.assistant.pages.SoundnessPage;
import org.woped.qualanalysis.sidebar.assistant.pages.WorkflowPage;
import org.woped.translations.Messages;

/**
 * shows the analysis startpage with the overview of soundness analysis,
 * workflow analysis and the net statistics
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class StartPage extends BeginnerPanel {

	public StartPage(SideBar sideBar) {
		super(null, sideBar, Messages
				.getString("AnalysisSideBar.Beginner.Startpage"));

		IEditor editor = sideBar.getEditor();
		SimpleGridBagLayout sgbl = new SimpleGridBagLayout();

		JPanel analysisPanel = new JPanel(sgbl);
		analysisPanel.setBackground(Color.WHITE);

		JLabel correctness = null;

		// workflow

		WorkflowPage workflowPage = new WorkflowPage(this, sideBar);
		JLabel workflowLabel = new JLabel(Messages
				.getString("AnalysisSideBar.Beginner.WorkflowAnalysis"));
		workflowLabel.setFont(SUBHEADER_FONT);
		workflowLabel.setBorder(BOTTOM_BORDEr);
		sgbl.addComponent(analysisPanel, workflowLabel, 0, 0, 1, 1, 1, 0);
		// adding workflow infos
		if (workflowPage.getStatus()) {
			correctness = new JLabel(Messages.getImageIcon(CORRECT_ICON));
		} else {
			status = false;
			correctness = new JLabel(Messages.getImageIcon(INCORRECT_ICON));
			JLabel workflowDetails = new JLabel(Messages
					.getImageIcon(DETAILS_ICON));
			workflowDetails.addMouseListener(new DetailsMouseListener(sideBar,
					workflowPage));
			workflowDetails.setBorder(BOTTOM_BORDEr);
			sgbl.addComponent(analysisPanel, workflowDetails, 2, 0, 1, 1, 0, 0);
		}
		correctness.setBorder(BOTTOM_RIGHT_BORDER);
		sgbl.addComponent(analysisPanel, correctness, 1, 0, 1, 1, 0, 0);

		// soundness

		if (workflowPage.getStatus()) {
			SoundnessPage soundnessPage = new SoundnessPage(this, sideBar);

			// adding soundness infos only when workflow is correct
			JLabel soundnessLabel = new JLabel(Messages
					.getString("AnalysisSideBar.Beginner.SoundnessAnalysis"));
			soundnessLabel.setFont(SUBHEADER_FONT);
			soundnessLabel.setBorder(BOTTOM_BORDEr);
			sgbl.addComponent(analysisPanel, soundnessLabel, 0, 1, 1, 1, 1, 1);
			if (!soundnessPage.getWarningStatus()) {
				correctness = new JLabel(Messages.getImageIcon(WARNING_ICON));
				if (!soundnessPage.getErrorStatus()) {
					correctness = new JLabel(Messages
							.getImageIcon(INCORRECT_ICON));
				}
				JLabel soundnessDetails = new JLabel(Messages
						.getImageIcon(DETAILS_ICON));
				soundnessDetails.addMouseListener(new DetailsMouseListener(
						sideBar, soundnessPage));
				soundnessDetails.setBorder(BOTTOM_BORDEr);
				sgbl.addComponent(analysisPanel, soundnessDetails, 2, 1, 1, 1,
						0, 1);
			} else {
				correctness = new JLabel(Messages.getImageIcon(CORRECT_ICON));
			}
			correctness.setBorder(BOTTOM_RIGHT_BORDER);
			sgbl.addComponent(analysisPanel, correctness, 1, 1, 1, 1, 0, 1);
		}
		addComponent(analysisPanel, 0, 0, 1, 1, 1, 0);

		// emptyLabel

		addComponent(new JLabel(), 0, 1, 1, 1, 1, 1);

		// info

		JPanel infoPanel = new JPanel();
		ClickLabel clickLabel = null;
		JLabel count = null;
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		infoPanel.setLayout(sgbl);

		// header
		JLabel netStatistic = new JLabel(Messages
				.getString("AnalysisSideBar.Beginner.Netstatistics"));
		netStatistic.setFont(HEADER_FONT);
		netStatistic.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		sgbl.addComponent(infoPanel, netStatistic, 0, 0, 1, 1, 1, 0);

		// places
		clickLabel = new ClickLabel(Messages
				.getString("Analysis.Tree.NumPlaces")
				+ ":", qualanalysisService.getPlacesIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 1, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumPlaces()),
				JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 1, 1, 1, 0, 0);

		// transitions
		clickLabel = new ClickLabel(Messages
				.getString("Analysis.Tree.NumTransitions")
				+ ":", qualanalysisService.getTransitionsIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 2, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService
				.getNumTransitions()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 2, 1, 1, 0, 0);

		// operators
		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString("Analysis.Tree.NumOperators") + ":",
				qualanalysisService.getOperatorsIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 3, 1, 1, 1, 0);

		count = new JLabel(String
				.valueOf(qualanalysisService.getNumOperators()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 3, 1, 1, 0, 0);

		// subprocesses
		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString("Analysis.Tree.NumSubprocesses") + ":",
				qualanalysisService.getSubprocessesIterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 4, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService
				.getNumSubprocesses()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 4, 1, 1, 0, 0);

		// arcs
		JLabel arcLabel = new JLabel(Messages
				.getString("Analysis.Tree.NumArcs")
				+ ":");
		arcLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, arcLabel, 0, 5, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getNumArcs()),
				JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 5, 1, 1, 0, 0);

		addComponent(infoPanel, 0, 2, 1, 1, 1, 0);
	}

	@Override
	public void addComponents() {
	}
}