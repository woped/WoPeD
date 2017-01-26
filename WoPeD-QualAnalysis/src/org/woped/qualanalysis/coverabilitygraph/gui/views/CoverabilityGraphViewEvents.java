package org.woped.qualanalysis.coverabilitygraph.gui.views;

/**
 * Defines the application wide events of the coverability graph
 */
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
     * Closes the assistant mode.
     */
    public static final int ASSISTANT_CLOSE = 10020;

    /**
     * Starts the assistant mode.
     */
    public static final int ASSISTANT_START = 10021;

    /**
     * Resets the assistant mode.
     */
    public static final int ASSISTANT_RESET = 10022;

    /**
     * This event tells the coverability graph to change its layout to the layout with the provided key
     * <p>
     * The data object has to of type {@link Integer} and represents the key of the layout
     */
    public static final int CHANGE_LAYOUT = 10010;
}
