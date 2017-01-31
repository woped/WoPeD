package org.woped.qualanalysis.sidebar;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.sidebar.assistant.StartPage;
import org.woped.qualanalysis.sidebar.components.PermanentTabbedPane;
import org.woped.qualanalysis.sidebar.components.TStar;
import org.woped.qualanalysis.sidebar.expert.ExpertPage;
import org.woped.gui.translations.Messages;

/**
 * class of the Analysissidebar
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * @editor Martin Meitz
 */
@SuppressWarnings("serial")
public class SideBar extends PermanentTabbedPane {

	private IQualanalysisService qualanService = null;

	private IEditor editor = null;

	private JPanel beginnerContainer = null;

	private JPanel expertContainer = null;

	private AbstractApplicationMediator mediator = null;

	private boolean autoRefreshStatus = false;

	private boolean workflowStatus = true;

	private JCheckBox tStarCheckBox = null;

	private TStar tStar = null;

	/**
	 * 
	 * @param editor
	 *            - reference to the current editor to analyis the net in it
	 * @param mediator
	 *            - reference to the current mediator (required for expertpage)
	 * @param autoRefreshStatus
	 *            - if true analyissidebar refresh if you change something in
	 *            the net
	 * @param tStarCheckBox
	 */
	public SideBar(IEditor editor, AbstractApplicationMediator mediator,
			boolean autoRefreshStatus, JCheckBox tStarCheckBox) {
		super();
		this.autoRefreshStatus = autoRefreshStatus;
		this.editor = editor;
		this.mediator = mediator;
		this.tStarCheckBox = tStarCheckBox;
		// creating Container for the contents of the assistant tab
		beginnerContainer = new JPanel(new BorderLayout());
		// creating Container for the contents of the expert tab
		expertContainer = new JPanel(new BorderLayout());
		// init qualanalysis service
		qualanService = getQualanalysisService();
		addComponents();
		// add the container to the sidebar
		this.addTab(Messages.getString("AnalysisSidebar.Header.Beginner"),
				beginnerContainer);
		this.addTab(Messages.getString("AnalysisSidebar.Header.Expert"),
				expertContainer);
		// add MouseLstener for close icon and refresh icon
		this.addMouseListener(this);
	}

	/**
	 * create startpage and get the status of the startpage (required showing
	 * t-star)
	 * 
	 * @return startpage for the assistant
	 */
	private JPanel createBeginnerPanel() {
		StartPage startPage = new StartPage(this);
		workflowStatus = startPage.getStatus();
		return startPage;
	}

	/**
	 * 
	 * @return new Instance of the ExpertPage
	 */
	private JPanel createExpertPanel() {
		return new ExpertPage(editor, mediator, qualanService);
	}

	/**
	 * method is called if user click the close icon
	 */
	@Override
	protected void clean() {
		cleanUp();
		// hide the sidebar
		editor.hideAnalysisBar();
	}

	/**
	 * method for refresh the sidebar called from autorefresh or if user click
	 * refresh icon
	 */
	@Override
	public void refresh() {
		cleanUp();
		editor.updateNet();
		qualanService = getQualanalysisService();
		beginnerContainer.removeAll();
		expertContainer.removeAll();
		addComponents();
		setStatusOfTStarCheckbox();
		showTStarIfPossible();
		beginnerContainer.revalidate();
		expertContainer.revalidate();
	}

	/**
	 * method calls cleanup method of qualanalysis service and dehighlight all
	 * elements in the net
	 */
	private void cleanUp() {
		qualanService.cleanup();
		qualanService = null;
		editor.getModelProcessor().removeHighlighting();
	}

	/**
	 * adding contents to the two containers
	 */
	private void addComponents() {
		beginnerContainer.add(createBeginnerPanel(), BorderLayout.CENTER);
		setStatusOfTStarCheckbox();
		expertContainer.add(createExpertPanel(), BorderLayout.CENTER);
	}

	/*
	 * @return reference to current or if null a new Instance of Qualanalysis
	 * Service
	 */
	public IQualanalysisService getQualanalysisService() {
		if (qualanService != null) {
			return qualanService;
		} else {
			return QualAnalysisServiceFactory
					.createNewQualAnalysisService(editor);
		}
	}

	public JPanel getBeginnerContainer() {
		return beginnerContainer;
	}

	public IEditor getEditor() {
		return editor;
	}

	@Override
	public boolean getAutoRefreshStatus() {
		return autoRefreshStatus;
	}

	public void setAutoRefreshStatus(boolean status) {
		autoRefreshStatus = status;
	}

	public void setStatusOfTStarCheckbox() {
		if (workflowStatus)
			tStarCheckBox.setEnabled(true);
		else
			tStarCheckBox.setEnabled(false);
	}

	public void showTStarIfPossible() {
		if (tStarCheckBox.isSelected() && workflowStatus) {
			AbstractPetriNetElementModel source = getQualanalysisService()
					.getSourcePlaces().iterator().next();
			AbstractPetriNetElementModel sink = getQualanalysisService()
					.getSinkPlaces().iterator().next();
			if (tStar == null)
				tStar = new TStar(editor);
			tStar.updateTStar(source, sink);
		} else {
			if (tStar != null)
				tStar.updateTStar(null, null);
		}
	}

	public boolean getWorkflowStatus() {
		return workflowStatus;
	}
}