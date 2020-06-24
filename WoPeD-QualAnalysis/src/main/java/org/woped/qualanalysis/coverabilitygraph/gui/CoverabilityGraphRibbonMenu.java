package org.woped.qualanalysis.coverabilitygraph.gui;

import static org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority.TOP;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.images.svg.editor_undo;
import org.woped.gui.images.svg.file_close;
import org.woped.gui.images.svg.file_exportas;
import org.woped.gui.images.svg.help_configuration;
import org.woped.gui.images.svg.layout;
import org.woped.gui.images.svg.refresh;
import org.woped.gui.images.svg.zoom_chooser;
import org.woped.gui.images.svg.zoom_in;
import org.woped.gui.images.svg.zoom_out;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphEventListener;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.circular.CircularLayout;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.hierarchic.HierarchicLayout;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.tree.TreeLayout;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewEvents;

/**
 * This class creates and operates the ribbon bar for the coverability graph
 */
public class CoverabilityGraphRibbonMenu {

    private static final int ICON_WIDTH = 80;
    private static final int ICON_HEIGHT = 80;
    private final AbstractApplicationMediator mediator;
    private final RibbonContextualTaskGroup contextGroup;
    private final RibbonTask graphTask;

    // buttons with enable/disable support
    private JCommandButton refreshButton;
    private JCommandButton unselectButton;

    private static ResizableIcon getResizableIcon(String resource) {

        ImageIcon icon = Messages.getImageIcon(resource);
        if (icon == null) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Unable do load icon from resources: " + resource);
            return null;
        }

        return ImageWrapperResizableIcon.getIcon(icon.getImage(), new Dimension(ICON_WIDTH, ICON_HEIGHT));
    }

    /**
     * Creates a new instance of the coverability graph ribbon menu
     *
     * @param mediator tha event mediator of the application
     */
    public CoverabilityGraphRibbonMenu(AbstractApplicationMediator mediator) {

        // Register module related view events
        this.mediator = mediator;

        CoverabilityGraphFrameController graphFrameController = CoverabilityGraphFrameController.getInstance(mediator.getUi());
        graphFrameController.addInternalFrameListener(new CoverabilityGraphFrameListener());
        GraphListener graphListener = new GraphListener();
        graphFrameController.addGraphEventListener(graphListener);

        graphTask = createCoverabilityGraphTask();
        contextGroup = createContextualGroup();
    }

    /**
     * Gets the contextual group for the coverability graph
     *
     * @return the contextual group of the coverability graph
     */
    public RibbonContextualTaskGroup getContextGroup() {
        return contextGroup;
    }

    /**
     * Gets the default task of the coverability group.
     * <p>
     * This group should be activated, when the contextual group gets visible
     *
     * @return the default task of the coverability group.
     */
    public RibbonTask getDefaultTask() {
        return graphTask;
    }

    private RibbonContextualTaskGroup createContextualGroup() {
        return new RibbonContextualTaskGroup("", Color.orange, graphTask);
    }

    private RibbonTask createCoverabilityGraphTask() {
        return new RibbonTask(Messages.getString("CoverabilityGraph.Ribbon.Task.Title"), createMainBand(), createViewBand(), createAssistantBand());
    }

    private JRibbonBand createMainBand() {
        JRibbonBand mainBand = new JRibbonBand(Messages.getString("CoverabilityGraph.Ribbon.MainBand.Title"), null);
        mainBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesSimple(mainBand));

        mainBand.addCommandButton(getCloseButton(), TOP);
        mainBand.addCommandButton(getRefreshButton(), RibbonElementPriority.MEDIUM);
        mainBand.addCommandButton(getUnselectButton(), RibbonElementPriority.MEDIUM);
        mainBand.addCommandButton(getSettingsButton(), RibbonElementPriority.MEDIUM);
        mainBand.addCommandButton(getExportButton(), RibbonElementPriority.MEDIUM);

        return mainBand;
    }

    private JRibbonBand createViewBand() {
        JRibbonBand viewBand;
        viewBand = new JRibbonBand(Messages.getString("CoverabilityGraph.Ribbon.ViewBand.Title"), null);
        viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesSimple(viewBand));

        viewBand.addCommandButton(getLayoutButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomInButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomOutButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomChooser(), RibbonElementPriority.MEDIUM);

        return viewBand;
    }

    private JRibbonBand createAssistantBand() {
        JRibbonBand assistantBand;
        assistantBand = new JRibbonBand(Messages.getString("CoverabilityGraph.Ribbon.AssistantBand.Title"), null);
        assistantBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesSimple(assistantBand));

        assistantBand.addCommandButton(getCloseAssistantButton(), RibbonElementPriority.MEDIUM);
        assistantBand.addCommandButton(getStartAssistantButton(), RibbonElementPriority.MEDIUM);
        assistantBand.addCommandButton(getResetAssistantButton(), RibbonElementPriority.MEDIUM);

        return assistantBand;
    }

    private JCommandButton getCloseButton() {
        JCommandButton closeButton = createButton("CoverabilityGraph.Ribbon.CloseButton", new file_close());
        addClickHandler(closeButton, CoverabilityGraphViewEvents.CLOSE_FRAME);

        return closeButton;
    }

    private JCommandButton getRefreshButton() {
        if (refreshButton == null) {
            refreshButton = createButton("CoverabilityGraph.Ribbon.RefreshButton", new refresh());
            addClickHandler(refreshButton, CoverabilityGraphViewEvents.REFRESH);
        }
        return refreshButton;
    }

    private JCommandButton getUnselectButton() {
        if (unselectButton == null) {
            unselectButton = createButton("CoverabilityGraph.Ribbon.UnselectButton", new editor_undo());
            addClickHandler(unselectButton, CoverabilityGraphViewEvents.UNSELECT);
        }
        return unselectButton;
    }

    private JCommandButton getSettingsButton() {
        JCommandButton showSettingsButton = createButton("CoverabilityGraph.Ribbon.ShowSettingsButton", new help_configuration());
        addClickHandler(showSettingsButton, CoverabilityGraphViewEvents.SHOW_SETTINGS);

        return showSettingsButton;
    }

    private JCommandButton getExportButton() {
        JCommandButton exportButton = createButton("CoverabilityGraph.Ribbon.ExportButton", new file_exportas());

        addClickHandler(exportButton, CoverabilityGraphViewEvents.EXPORT);

        return exportButton;
    }

    private JCommandButton getLayoutButton() {

        JCommandButton layoutButton = createButton("CoverabilityGraph.Ribbon.LayoutButton", new layout());

        layoutButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
        layoutButton.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                String resourceKey = "CoverabilityGraph.Ribbon.LayoutOption.Hierarchic";
                JCommandMenuButton optionHierarchic = new JCommandMenuButton(Messages.getString(resourceKey), getResizableIcon(resourceKey));
                addClickHandler(optionHierarchic, CoverabilityGraphViewEvents.CHANGE_LAYOUT, new HierarchicLayout());

                resourceKey = "CoverabilityGraph.Ribbon.LayoutOption.Circle";
                JCommandMenuButton optionCircular = new JCommandMenuButton(Messages.getString(resourceKey), getResizableIcon(resourceKey));
                addClickHandler(optionCircular, CoverabilityGraphViewEvents.CHANGE_LAYOUT, new CircularLayout());

                resourceKey = "CoverabilityGraph.Ribbon.LayoutOption.Tree";
                JCommandMenuButton optionTree = new JCommandMenuButton(Messages.getString(resourceKey), getResizableIcon(resourceKey));
                addClickHandler(optionTree, CoverabilityGraphViewEvents.CHANGE_LAYOUT, new TreeLayout());

                JCommandPopupMenu layoutOption = new JCommandPopupMenu();
                layoutOption.addMenuButton(optionHierarchic);
                layoutOption.addMenuButton(optionCircular);
                layoutOption.addMenuButton(optionTree);
                return layoutOption;
            }
        });

        return layoutButton;
    }

    private JCommandButton getZoomInButton() {
        JCommandButton zoomInButton = createButton("CoverabilityGraph.Ribbon.ZoomInButton", new zoom_in());
        addClickHandler(zoomInButton, CoverabilityGraphViewEvents.ZOOM_IN);

        return zoomInButton;
    }

    private JCommandButton getZoomOutButton() {

        JCommandButton zoomOutButton = createButton("CoverabilityGraph.Ribbon.ZoomOutButton", new zoom_out());
        addClickHandler(zoomOutButton, CoverabilityGraphViewEvents.ZOOM_OUT);

        return zoomOutButton;
    }

    private JCommandButton getZoomChooser() {
        JCommandButton zoomChooserButton = createButton("CoverabilityGraph.Ribbon.ZoomChooserButton", new zoom_chooser());

        zoomChooserButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
        zoomChooserButton.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                int eventId = CoverabilityGraphViewEvents.ZOOM_SET;

                JCommandMenuButton zoomButton200 = new JCommandMenuButton("200 %", null);
                addClickHandler(zoomButton200, eventId, 2.0);

                JCommandMenuButton zoomButton150 = new JCommandMenuButton("150 %", null);
                addClickHandler(zoomButton150, eventId, 1.5);

                JCommandMenuButton zoomButton100 = new JCommandMenuButton("100 %", null);
                addClickHandler(zoomButton100, eventId, 1.0);

                JCommandMenuButton zoomButton75 = new JCommandMenuButton("75 %", null);
                addClickHandler(zoomButton75, eventId, 0.75);

                JCommandMenuButton zoomButton50 = new JCommandMenuButton("50 %", null);
                addClickHandler(zoomButton50, eventId, 0.5);

                JCommandMenuButton zoomButton25 = new JCommandMenuButton("25 %", null);
                addClickHandler(zoomButton25, eventId, 0.25);

                JCommandPopupMenu zoomOptions = new JCommandPopupMenu();
                zoomOptions.addMenuButton(zoomButton200);
                zoomOptions.addMenuButton(zoomButton150);
                zoomOptions.addMenuButton(zoomButton100);
                zoomOptions.addMenuButton(zoomButton75);
                zoomOptions.addMenuButton(zoomButton50);
                zoomOptions.addMenuButton(zoomButton25);

                return zoomOptions;
            }
        });

        return zoomChooserButton;
    }

    private JCommandButton getCloseAssistantButton() {
        String prefix = "CoverabilityGraph.Ribbon.Assistant.Close";
        JCommandButton closeAssistantButton = createButton(prefix, getResizableIcon(prefix));
        addClickHandler(closeAssistantButton, CoverabilityGraphViewEvents.ASSISTANT_CLOSE);
        return closeAssistantButton;
    }

    private JCommandButton getStartAssistantButton() {
        String prefix = "CoverabilityGraph.Ribbon.Assistant.Start";
        JCommandButton startAssistantButton = createButton(prefix, getResizableIcon(prefix));
        addClickHandler(startAssistantButton, CoverabilityGraphViewEvents.ASSISTANT_START);
        return startAssistantButton;
    }

    private JCommandButton getResetAssistantButton() {
        String prefix = "CoverabilityGraph.Ribbon.Assistant.Reset";
        JCommandButton resetAssistantButton = createButton(prefix, getResizableIcon(prefix));
        addClickHandler(resetAssistantButton, CoverabilityGraphViewEvents.ASSISTANT_RESET);
        return resetAssistantButton;
    }

    private JCommandButton createButton(String prefix, ResizableIcon icon) {
        JCommandButton button = new JCommandButton(Messages.getTitle(prefix), icon);

        button.setActionRichTooltip(new RichTooltip(
                Messages.getString(prefix + ".Tooltip.Title"),
                Messages.getString(prefix + ".Tooltip.Description")));

        return button;
    }

    private void addClickHandler(JCommandButton button, int eventId) {
        addClickHandler(button, eventId, null);
    }

    private void addClickHandler(JCommandButton button, final int eventId, final Object data) {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                mediator.fireViewEvent(new ViewEvent(CoverabilityGraphRibbonMenu.this, AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, eventId, data));
            }
        });
    }

    private void showContextGroup() {
        mediator.fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, CoverabilityGraphViewEvents.FRAME_ACTIVATED));
    }

    private void hideContextGroup() {
        mediator.fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, CoverabilityGraphViewEvents.FRAME_DEACTIVATED));
    }

    private class CoverabilityGraphFrameListener extends InternalFrameAdapter {

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            hideContextGroup();
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            showContextGroup();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            hideContextGroup();
        }
    }

    private class GraphListener implements CoverabilityGraphEventListener {

        /**
         * Invoked if the graph and the underlying petri net are out of sync.
         */
        @Override
        public void editorSyncLost() {
            refreshButton.setEnabled(true);
        }

        /**
         * Invoked, when the synchronisation between the graph and the underlying petri net has been renewed.
         */
        @Override
        public void editorSyncEstablished() {
            refreshButton.setEnabled(false);
        }

        /**
         * Invoked when a highlighting has been added to the graph.
         */
        @Override
        public void graphHighlightingAdded() {
            unselectButton.setEnabled(true);
        }

        /**
         * Invoked when all highlighting has been removed from the graph.
         */
        @Override
        public void graphHighlightingRemoved() {
            unselectButton.setEnabled(false);
        }
    }
}
