package org.woped.qualanalysis.reachabilitygraph.controller;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.CoverabilityGraphLayout;

public class CoverabilityGraphViewEvents {

    /**
     * This event tells the coverability graph to close itself
     */
    public static final int CLOSE_FRAME = 10000;

    /**
     * This event occurs when the coverability graph frame has been activated
     */
    public static final int FRAME_ACTIVATED = 10001;

    /**
     * This event occurs when the coverability graph frame has been deactivated
     */
    public static final int FRAME_DEACTIVATED = 10002;

    /**
     * This event tells the coverability graph to recompute and refresh its view
     */
    public static final int REFRESH = 10003;

    /**
     * This event tells the coverability graph to remove all highlighting from the graph and the related editor
     */
    public static final int UNSELECT = 10004;

    /**
     * This event opens the coverability graph settings dialog.
     */
    public static final int SHOW_SETTINGS = 10005;

    /**
     * This event opens the export image dialog for the coverability graph
     */
    public static final int EXPORT = 10006;

    /**
     * This event tells the coverability graph to set the zoom factor of the related view to the value provided as data object.
     * <p>
     * The data object has to be of typ {@link Double}
     */
    public static final int ZOOM_SET = 10007;

    /**
     * This event tells the coverability graph to zoom on step into the graph
     */
    public static final int ZOOM_IN = 10008;

    /**
     * This event tells the coverability graph to zoom on step out of the graph
     */
    public static final int ZOOM_OUT = 10009;

    /**
     * This event tells the coverability graph to change its layout to the layout with the provided key
     * <p>
     * The data object has to of type {@link Integer} and represents the key of the layout
     */
    public static final int CHANGE_LAYOUT = 10010;

    public static void registerEvents(AbstractApplicationMediator mediator) {
        createEvent(mediator, CoverabilityGraphActions.FRAME_ACTIVATED, CoverabilityGraphViewEvents.FRAME_ACTIVATED, true);
        createEvent(mediator, CoverabilityGraphActions.FRAME_DEACTIVATED, CoverabilityGraphViewEvents.FRAME_DEACTIVATED, true);
        createEvent(mediator, CoverabilityGraphActions.CLOSE, CoverabilityGraphViewEvents.CLOSE_FRAME, true);
        createEvent(mediator, CoverabilityGraphActions.REFRESH, CoverabilityGraphViewEvents.REFRESH, true);
        createEvent(mediator, CoverabilityGraphActions.UNSELECT, CoverabilityGraphViewEvents.UNSELECT, false);
        createEvent(mediator, CoverabilityGraphActions.SHOW_SETTINGS, CoverabilityGraphViewEvents.SHOW_SETTINGS, true);
        createEvent(mediator, CoverabilityGraphActions.EXPORT, CoverabilityGraphViewEvents.EXPORT, true);
        createEvent(mediator, CoverabilityGraphActions.LAYOUT, CoverabilityGraphViewEvents.CHANGE_LAYOUT, true, CoverabilityGraphLayout.HIERARCHIC);
        createEvent(mediator, CoverabilityGraphActions.ZOOM_SET, CoverabilityGraphViewEvents.ZOOM_SET, true, 1.0);
        createEvent(mediator, CoverabilityGraphActions.ZOOM_IN, CoverabilityGraphViewEvents.ZOOM_IN, true);
        createEvent(mediator, CoverabilityGraphActions.ZOOM_OUT, CoverabilityGraphViewEvents.ZOOM_OUT, true);
    }

    private static void createEvent(AbstractApplicationMediator mediator, String actionId, int eventId, boolean isEnabled) {
        createEvent(mediator, actionId, eventId, isEnabled, null);
    }

    private static void createEvent(AbstractApplicationMediator mediator, String actionId, int eventId, boolean isEnabled, Object data) {
        WoPeDAction action = new WoPeDAction(mediator, AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, eventId, data);
        ActionFactory.registerAction(actionId, action);

        int enabled = isEnabled ? VisualController.ALWAYS : VisualController.NEVER;
        int visible = VisualController.ALWAYS;
        int selected = VisualController.IGNORE;

        VisualController.getInstance().addElement(action, enabled, visible, selected);
    }
}
