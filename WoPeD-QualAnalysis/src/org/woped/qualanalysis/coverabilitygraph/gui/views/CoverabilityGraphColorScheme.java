package org.woped.qualanalysis.coverabilitygraph.gui.views;

import java.awt.*;

/**
 * Defines the default color scheme for coverability graph nodes if not specific colors are set on the nodes.
 */
public class CoverabilityGraphColorScheme {

    private static CoverabilityGraphColorScheme grayScaledScheme;
    private static CoverabilityGraphColorScheme coloredScheme;

    /**
     * The default background color of the node
     */
    Color DefaultNodeBackgroundColor;

    /**
     * The default background color of the root node.
     */
    Color RootNodeBackgroundColor;

    /**
     * The default background color of a selected node.
     */
    Color SelectedNodeBackgroundColor;

    /**
     * A color scheme based on gray scaled colors.
     *
     * @return a gray scaled color scheme
     */
    public static CoverabilityGraphColorScheme GRAY_SCALED_SCHEME() {

        if (grayScaledScheme == null) {
            grayScaledScheme = new CoverabilityGraphColorScheme();
            grayScaledScheme.DefaultNodeBackgroundColor = Color.WHITE;
            grayScaledScheme.RootNodeBackgroundColor = new Color(204, 204, 204);
            grayScaledScheme.SelectedNodeBackgroundColor = new Color(188, 235, 253);
        }

        return grayScaledScheme;
    }

    /**
     * A color scheme based on different colors.
     *
     * @return a colored color scheme
     */
    public static CoverabilityGraphColorScheme COLORED_SCHEME() {

        if (coloredScheme == null) {
            coloredScheme = new CoverabilityGraphColorScheme();
            coloredScheme.DefaultNodeBackgroundColor = Color.orange;
            coloredScheme.RootNodeBackgroundColor = Color.green;
            coloredScheme.SelectedNodeBackgroundColor = new Color(188, 235, 253);
        }

        return coloredScheme;
    }
}
