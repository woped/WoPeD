package org.woped.qualanalysis.coverabilitygraph.gui;

import java.awt.geom.Point2D;

/**
 * Provides the functionality to adjust the scale of a {@link org.jgraph.JGraph}
 */
public interface ZoomController {

    /**
     * Zooms one step into the graph.
     */
    void zoomIn();

    /**
     * Zooms the provided steps into the graph on the provided point.
     *
     * @param steps  the amount of steps to zoom
     * @param center the center of the zooming
     */
    void zoomIn(int steps, Point2D center);

    /**
     * Zooms one step out of the graph.
     */
    void zoomOut();

    /**
     * Zooms the provided steps out ouf the graph on the provided point.
     *
     * @param steps  the amount of steps to zoom.
     * @param center the center of the zooming.
     */
    void zoomOut(int steps, Point2D center);

    /**
     * Sets the zooming of the graph to the provided factor.
     * <p>
     * If the factor is less/larger then the limit the zoom is set to the limit.
     *
     * @param factor the new zoom factor
     */
    void setZoom(double factor);

    /**
     * Sets the zooming of the graph to the provided factor.
     * <p>
     * If the factor is less/larger then the limit the zoom is set to the limit.
     *
     * @param factor the new zoom factor
     * @param center the center of the zooming
     */
    void setZoom(double factor, Point2D center);
}
