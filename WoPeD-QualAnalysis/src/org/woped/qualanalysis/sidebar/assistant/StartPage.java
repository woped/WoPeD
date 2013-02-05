package org.woped.qualanalysis.sidebar.assistant;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.assistant.components.BeginnerPanel;
import org.woped.qualanalysis.sidebar.assistant.components.ClickLabel;
import org.woped.qualanalysis.sidebar.assistant.components.DetailsMouseListener;
import org.woped.qualanalysis.sidebar.assistant.components.SimpleGridBagLayout;
import org.woped.qualanalysis.sidebar.assistant.pages.SoundnessPage;
import org.woped.qualanalysis.sidebar.assistant.pages.WorkflowPage;
import org.woped.gui.translations.Messages;

/**
 * shows the analysis startpage with the overview of soundness analysis, workflow analysis and the net statistics
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class StartPage extends BeginnerPanel {

	public StartPage(SideBar sideBar) {
		super(null, sideBar, Messages.getString(PREFIX_BEGINNER + "Startpage"));

		SimpleGridBagLayout sgbl = new SimpleGridBagLayout();

		JPanel analysisPanel = new JPanel(sgbl);
		analysisPanel.setBackground(Color.WHITE);

		JLabel correctness = null;

		/*
		 * Workflow
		 */

		// add workflow-label
		JLabel workflowLabel = new JLabel(Messages.getString(PREFIX + "WorkflowAnalysis"));
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
			JLabel soundnessLabel = new JLabel(Messages.getString(PREFIX + "SoundnessAnalysis"));
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
		 * token-mistake in initial marking
		 */

		// check only for workflow-net
		if(qualanalysisService.isWorkflowNet()){
			// display only if there is a token mistake in initial marking
			if (qualanalysisService.getWronglyMarkedPlaces().size() > 0) {
				// add tokenMistake-label
				JLabel tokenMistakeLabel = new JLabel(Messages.getString(PREFIX_BEGINNER + "InitialMarkingMistake"));
				tokenMistakeLabel.setFont(SUBHEADER_FONT);
				tokenMistakeLabel.setBorder(BOTTOM_BORDER);
				sgbl.addComponent(analysisPanel, tokenMistakeLabel, 0, 2, 1, 1, 1, 0);
				// add tokenMistake-icon with toolTip
				JLabel tokenMistakeIcon = new JLabel(Messages.getImageIcon(WARNING_ICON));
				tokenMistakeIcon.setBorder(BOTTOM_RIGHT_BORDER);
				tokenMistakeIcon.setToolTipText(Messages.getString(PREFIX_BEGINNER + "InitialMarkingMistake.Info"));
				sgbl.addComponent(analysisPanel, tokenMistakeIcon, 1, 2, 1, 1, 0, 0);
			}
		}

		addComponent(analysisPanel, 0, 0, 1, 1, 1, 0);

		// emptyLabel

		addComponent(new JLabel(), 0, 1, 1, 1, 1, 1);

		/*
		 * net info
		 */
		
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();

		JPanel infoPanel = new JPanel();
		ClickLabel clickLabel = null;
		JLabel count = null; 
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
		infoPanel.setLayout(sgbl);

		// header
		JLabel netStatistic = new JLabel(Messages.getString(PREFIX + "NetStatistics"));
		netStatistic.setFont(HEADER_FONT);
		netStatistic.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		sgbl.addComponent(infoPanel, netStatistic, 0, 0, 1, 1, 1, 0);

		// places
		clickLabel = new ClickLabel(Messages.getString(PREFIX + "NumPlaces") + COLON, qualanalysisService
				.getPlaces().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 1, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getPlaces().size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 1, 1, 1, 0, 0);

		// transitions
		clickLabel = new ClickLabel(Messages.getString(PREFIX + "NumTransitions") + COLON, qualanalysisService
				.getTransitions().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 2, 1, 1, 1, 0);

		count = new JLabel((int)calculateT(mec)+"", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 2, 1, 1, 0, 0);

		// operators
		clickLabel = new ClickLabel(SUB_POINT + Messages.getString(PREFIX + "NumOperators") + COLON,
				qualanalysisService.getOperators().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 3, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getOperators().size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 3, 1, 1, 0, 0);

		// subprocesses
		clickLabel = new ClickLabel(SUB_POINT + Messages.getString(PREFIX + "NumSubprocesses") + COLON,
				qualanalysisService.getSubprocesses().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, clickLabel, 0, 4, 1, 1, 1, 0);

		count = new JLabel(String.valueOf(qualanalysisService.getSubprocesses().size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 4, 1, 1, 0, 0);

		// arcs
		JLabel arcLabel = new JLabel(Messages.getString(PREFIX + "NumArcs") + COLON);
		arcLabel.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, arcLabel, 0, 5, 1, 1, 1, 0);

		count = new JLabel((int)calculateA(mec)+"", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		sgbl.addComponent(infoPanel, count, 1, 5, 1, 1, 0, 0);

		addComponent(infoPanel, 0, 2, 1, 1, 1, 0);
	}
	
	private double calculateA(ModelElementContainer mec){
		return mec.getArcMap().size();
	}
	
	private double calculateT(ModelElementContainer mec){
		return getTransitions(mec).size();
	}
	
	private Map<String,AbstractPetriNetElementModel> getTransitions(ModelElementContainer mec){
		Map<String, AbstractPetriNetElementModel> transitions = new HashMap<String, AbstractPetriNetElementModel>();
		Map<String, AbstractPetriNetElementModel> partialTransitions;
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		return transitions;
	}

	@Override
	public void addComponents() {
	}
}