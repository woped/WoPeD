package org.woped.qualanalysis.sidebar;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JPanel;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualanalysisServiceImplement;
import org.woped.qualanalysis.sidebar.assistant.StartPage;
import org.woped.qualanalysis.sidebar.components.CloseableTabbedPane;
import org.woped.qualanalysis.sidebar.expert.ExpertPage;
import org.woped.qualanalysis.woflan.QualanalysisServiceImplementWoflan;
import org.woped.translations.Messages;

/**
 * class of the Analysissidebar
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class SideBar extends CloseableTabbedPane {

    private IQualanalysisService qualanService = null;

    private IEditor editor = null;

    private JPanel beginnerContainer = null;

    private JPanel expertContainer = null;

    private AbstractApplicationMediator mediator = null;

    private boolean autoRefreshStatus = false;

    private boolean workflowStatus = true;

    /**
     * 
     * @param temporaryFile - for Woflan Analysis
     * @param editor - reference to the current editor to analyis the net in it
     * @param mediator - reference to the current mediator (required for expertpage)
     * @param autoRefreshStatus - if true analyissidebar refresh if you change something in the net
     */
    public SideBar(IEditor editor, AbstractApplicationMediator mediator, boolean autoRefreshStatus) {
        super();
        this.autoRefreshStatus = autoRefreshStatus;
        this.editor = editor;
        this.mediator = mediator;
        // creating Container for the contents of the assistant tab
        beginnerContainer = new JPanel(new BorderLayout());
        // creating Container for the contents of the expert tab
        expertContainer = new JPanel(new BorderLayout());
        // init qualanalysis service
        qualanService = getQualanalysisService();
        addComponents();
        // add the container to the sidebar
        this.addTab(Messages.getString("AnalysisSidebar.Header.Beginner"), beginnerContainer);
        this.addTab(Messages.getString("AnalysisSidebar.Header.Expert"), expertContainer);
        // add MouseLstener for close icon and refresh icon
        this.addMouseListener(this);
    }

    /**
     * create startpage and get the status of the startpage (required showing t-star)
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
     * method for refresh the sidebar called from autorefresh or if user click refresh icon
     */
    @Override
    public void refresh() {
        cleanUp();
        editor.updateNet();
        qualanService = getQualanalysisService();
        beginnerContainer.removeAll();
        expertContainer.removeAll();
        addComponents();
        beginnerContainer.revalidate();
        expertContainer.revalidate();
    }

    /**
     * method calls cleanup method of qualanalysis service and dehighlight all elements in the net
     */
    private void cleanUp() {
        qualanService.cleanup();
        qualanService = null;
        // clear the selection
        Iterator<AbstractElementModel> i = editor.getModelProcessor().getElementContainer().getRootElements()
                .iterator();
        while (i.hasNext()) {
            AbstractElementModel current = i.next();
            current.setHighlighted(false);
        }
    }

    /**
     * adding contents to the two containers
     */
    private void addComponents() {
        beginnerContainer.add(createBeginnerPanel(), BorderLayout.CENTER);
        expertContainer.add(createExpertPanel(), BorderLayout.CENTER);
    }

    /*
     * @return reference to current or if null a new Instance of Qualanalysis Service
     */
    public IQualanalysisService getQualanalysisService() {
        if (qualanService != null) {
            return qualanService;
        } else {
            if (editor.isUseWoflanDLL()) {
                return new QualanalysisServiceImplementWoflan(editor);
            } else {
                return new QualanalysisServiceImplement(editor);
            }
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

    public boolean getWorkflowStatus() {
        return workflowStatus;
    }
}