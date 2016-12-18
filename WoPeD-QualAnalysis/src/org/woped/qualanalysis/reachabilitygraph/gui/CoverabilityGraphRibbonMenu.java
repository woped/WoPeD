package org.woped.qualanalysis.reachabilitygraph.gui;

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
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.action.ActionButtonListener;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.CoverabilityGraphActions;
import org.woped.qualanalysis.reachabilitygraph.controller.CoverabilityGraphViewEvents;
import org.woped.gui.images.svg.*;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.reachabilitygraph.data.IReachabilityGraphModel;

import javax.swing.*;
import java.awt.*;

import static org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority.TOP;
import static org.woped.qualanalysis.reachabilitygraph.controller.CoverabilityGraphActions.*;

/**
 * This class creates and operates the ribbon bar for the coverability graph
 */
public class CoverabilityGraphRibbonMenu {

    private static final int ICON_WIDTH = 80;
    private static final int ICON_HEIGHT = 80;

    private static ResizableIcon getResizableIcon(String resource) {

        ImageIcon icon = Messages.getImageIcon(resource);
        if(icon == null){
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Unable do load icon from resources: " + resource);
            return null;
        }

        return ImageWrapperResizableIcon.getIcon(icon.getImage(), new Dimension(ICON_WIDTH, ICON_HEIGHT));
    }

    private final AbstractApplicationMediator mediator;
    private final RibbonContextualTaskGroup contextGroup;
    private final RibbonTask graphTask;

    /**
     * Creates a new instance of the coverability graph ribbon menu
     *
     * @param mediator tha event mediator of the application
     */
    public CoverabilityGraphRibbonMenu(AbstractApplicationMediator mediator) {

        // Register module related view events
        this.mediator = mediator;
        CoverabilityGraphViewEvents.registerEvents(this.mediator);

        graphTask = createCoverabilityGraphTask();
        contextGroup = createContextualGroup();
    }

    /**
     * Gets the contextual group for the coverability graph
     *
     * @return the contextual group of the coverability graph
     */
    public RibbonContextualTaskGroup getContextGroup(){
        return contextGroup;
    }

    /**
     * Gets the default task of the coverability group.
     * <p>
     * This group should be activated, when the contextual group gets visible
     *
     * @return the default task of the coverability group.
     */
    public RibbonTask getDefaultTask(){
        return graphTask;
    }

    private RibbonContextualTaskGroup createContextualGroup(){
        return new RibbonContextualTaskGroup("", Color.orange, graphTask);
    }

    private RibbonTask createCoverabilityGraphTask(){
        return  new RibbonTask(Messages.getString("CoverabilityGraph.Ribbon.Task.Title"), createMainBand(), createViewBand());
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
        viewBand = new JRibbonBand(Messages.getString("View.textBandTitle"), null);
        viewBand.setResizePolicies(CoreRibbonResizePolicies.getCorePoliciesSimple(viewBand));

        viewBand.addCommandButton(getLayoutButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomInButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomOutButton(), RibbonElementPriority.MEDIUM);
        viewBand.addCommandButton(getZoomChooser(), RibbonElementPriority.MEDIUM);

        return viewBand;
    }

    private JCommandButton getCloseButton() {
        JCommandButton closeButton = createButton("CoverabilityGraph.Ribbon.CloseButton", new file_close());
        addClickHandler(closeButton, CoverabilityGraphActions.CLOSE, CoverabilityGraphViewEvents.CLOSE_FRAME);

        return closeButton;
    }

    private JCommandButton getRefreshButton() {
        JCommandButton refreshButton = createButton("CoverabilityGraph.Ribbon.RefreshButton", new refresh());
        addClickHandler(refreshButton, CoverabilityGraphActions.REFRESH, CoverabilityGraphViewEvents.REFRESH);

        return refreshButton;
    }

    private JCommandButton getUnselectButton() {
        JCommandButton unselectButton = createButton("CoverabilityGraph.Ribbon.UnselectButton", new editor_undo());
        addClickHandler(unselectButton, CoverabilityGraphActions.UNSELECT, CoverabilityGraphViewEvents.UNSELECT);

        return unselectButton;
    }

    private JCommandButton getSettingsButton() {
        JCommandButton showSettingsButton = createButton("CoverabilityGraph.Ribbon.ShowSettingsButton", new help_configuration());
        addClickHandler(showSettingsButton, CoverabilityGraphActions.SHOW_SETTINGS, CoverabilityGraphViewEvents.SHOW_SETTINGS);

        return showSettingsButton;
    }

    private JCommandButton getExportButton() {
        JCommandButton exportButton = createButton("CoverabilityGraph.Ribbon.ExportButton", new file_exportas());
        addClickHandler(exportButton, CoverabilityGraphActions.EXPORT, CoverabilityGraphViewEvents.EXPORT);

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
                addClickHandler(optionHierarchic, LAYOUT, CoverabilityGraphViewEvents.CHANGE_LAYOUT, IReachabilityGraphModel.HIERARCHIC);

                resourceKey = "CoverabilityGraph.Ribbon.LayoutOption.Circle";
                JCommandMenuButton optionCircular = new JCommandMenuButton(Messages.getString(resourceKey), getResizableIcon(resourceKey));
                addClickHandler(optionCircular, LAYOUT, CoverabilityGraphViewEvents.CHANGE_LAYOUT, IReachabilityGraphModel.CIRCLE);

                JCommandPopupMenu layoutOption = new JCommandPopupMenu();
                layoutOption.addMenuButton(optionHierarchic);
                layoutOption.addMenuButton(optionCircular);
                return layoutOption;
            }
        });

        return layoutButton;
    }

    private JCommandButton getZoomInButton() {
        JCommandButton zoomInButton = createButton("CoverabilityGraph.Ribbon.ZoomInButton", new zoom_in());
        addClickHandler(zoomInButton, ZOOM_IN, CoverabilityGraphViewEvents.ZOOM_IN);

        return zoomInButton;
    }

    private JCommandButton getZoomOutButton() {

        JCommandButton zoomOutButton = createButton("CoverabilityGraph.Ribbon.ZoomOutButton", new zoom_out());
        addClickHandler(zoomOutButton, ZOOM_OUT, CoverabilityGraphViewEvents.ZOOM_OUT);

        return zoomOutButton;
    }

    private JCommandButton getZoomChooser() {
        JCommandButton zoomChooserButton = createButton("CoverabilityGraph.Ribbon.ZoomChooserButton", new zoom_chooser());

        zoomChooserButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
        zoomChooserButton.setPopupCallback(new PopupPanelCallback() {
            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton) {
                String actionId = ZOOM_SET;
                int eventId = CoverabilityGraphViewEvents.ZOOM_SET;

                JCommandMenuButton zoomButton200 = new JCommandMenuButton("200 %", null);
                addClickHandler(zoomButton200, actionId, eventId, 2.0);

                JCommandMenuButton zoomButton150 = new JCommandMenuButton("150 %", null);
                addClickHandler(zoomButton150, actionId, eventId, 1.5);

                JCommandMenuButton zoomButton100 = new JCommandMenuButton("100 %", null);
                addClickHandler(zoomButton100, actionId, eventId, 1.0);

                JCommandMenuButton zoomButton75 = new JCommandMenuButton("75 %", null);
                addClickHandler(zoomButton75, actionId, eventId, 0.75);

                JCommandMenuButton zoomButton50 = new JCommandMenuButton("50 %", null);
                addClickHandler(zoomButton50, actionId, eventId, 0.5);

                JCommandMenuButton zoomButton25 = new JCommandMenuButton("25 %", null);
                addClickHandler(zoomButton25, actionId, eventId, 0.25);

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

    private JCommandButton createButton(String prefix, ResizableIcon icon) {
        JCommandButton button = new JCommandButton(Messages.getTitle(prefix), icon);

        button.setActionRichTooltip(new RichTooltip(
                Messages.getString(prefix + ".Tooltip.Title"),
                Messages.getString(prefix + ".Tooltip.Description")));

        return button;
    }

    private void addClickHandler(JCommandButton button, String actionId, int eventId) {
        addClickHandler(button, actionId, eventId, null);
    }

    private void addClickHandler(JCommandButton button, String actionId, int eventId, Object data) {
        button.addActionListener(new ActionButtonListener(mediator, actionId, eventId, button, data));
    }
}
