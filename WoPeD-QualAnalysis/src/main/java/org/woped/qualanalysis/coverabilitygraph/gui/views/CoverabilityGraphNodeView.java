package org.woped.qualanalysis.coverabilitygraph.gui.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.NodeFormatter;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

public class CoverabilityGraphNodeView extends VertexView {

    private static final long serialVersionUID = 3973287972414121220L;
    public static transient ReachabilityPlaceRenderer renderer = new ReachabilityPlaceRenderer();

    private NodeFormatter nodeFormatter;

    CoverabilityGraphNodeView(Object cell, NodeFormatter nodeFormatter) {
        super(cell);
        this.nodeFormatter = nodeFormatter;
    }

    @Override
    public CellViewRenderer getRenderer() {
        return renderer;
    }

    protected static class ReachabilityPlaceRenderer extends VertexRenderer implements CellViewRenderer {

        private static final long serialVersionUID = 1075415120773973965L;

        transient Stroke borderline;

        ReachabilityPlaceRenderer() {
            super();
        }

        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width += d.width / 8;
            d.height += d.height / 2;
            return d;
        }

        @Override
        protected void installAttributes(CellView view) {
            super.installAttributes(view);

            if(!(view instanceof CoverabilityGraphNodeView)) return;
            NodeFormatter nodeFormatter = ((CoverabilityGraphNodeView)view).nodeFormatter;

            Object cell = view.getCell();
            if(!(cell instanceof CoverabilityGraphNode)) return;
            CoverabilityGraphNode node = (CoverabilityGraphNode) cell;

            AttributeMap map = view.getAllAttributes();
            float[] dashPattern = GraphConstants.getDashPattern(map);
            borderWidth = (int) GraphConstants.getLineWidth(map);
            borderline = new BasicStroke(borderWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
            bordercolor = GraphConstants.getLineColor(map);

            Color background = decideBackgroundColor(node, nodeFormatter);
            setBackground((background != null) ? background : defaultBackground);

            setText(nodeFormatter.getNodeTextFormatter().getText(node));
        }

        private Color decideBackgroundColor(CoverabilityGraphNode node, NodeFormatter formatter){

            // If color is set, use color
            Color background = GraphConstants.getBackground(view.getAllAttributes());
            if(background != null) return background;

            // Otherwise use color scheme
            if(selected) return formatter.getColorScheme().SelectedNodeBackgroundColor;
            if(node.getMarking().isInitial()) return formatter.getColorScheme().RootNodeBackgroundColor;
            return formatter.getColorScheme().DefaultNodeBackgroundColor;
        }


        public void paint(Graphics g) {
            int b = borderWidth;
            Dimension d = getSize();
            Graphics2D g2 = (Graphics2D) g;
            boolean tmp = selected;

            if (super.isOpaque()) {
                g2.setColor(super.getBackground());
                g2.fillRoundRect(0, 0, d.width - 1, d.height - 1, 10, 10);
            }

            try {
                setBorder(null);
                setOpaque(false);
                selected = false;
                super.paint(g);
            } finally {
                selected = tmp;
            }
            if (bordercolor != null) {
                g2.setColor(bordercolor);
                g2.setStroke(borderline);
                g2.drawRoundRect(b-1, b-1, d.width - b, d.height - b, 10, 10);
            }
        }
    }
}
