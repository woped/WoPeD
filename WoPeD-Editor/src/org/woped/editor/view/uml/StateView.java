package org.woped.editor.view.uml;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.uml.StateModel;
import org.woped.core.view.AbstractElementView;

@SuppressWarnings("serial")
public class StateView extends AbstractElementView
{
    private StateRenderer renderer = new StateRenderer();

    public StateView(Object cell)
    {
        super(cell);
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    public int getStateType()
    {
        return ((StateModel) getCell()).getStateType();
    }

    public Point2D getPerimeterPoint(Point2D source, Point2D p)
    {

        // liefert die Größe und die Koordinaten der Stelle.
        Rectangle2D r = getBounds();
        // Berechnet den relative Mittelpunkt der Stelle.
        double a = (16 - 1) / 2;
        double b = (16 - 1) / 2;
        // Berechnet den Winkel von dem Punkt p zum Mittelpunkt der Stelle.
        double dx = p.getX() - r.getCenterX();
        double dy = p.getY() - r.getCenterY();
        // Winkelberechnung siehe Abb. 16. Tangens(aplha) = dy/dx.
        double aplha = Math.atan2(dy, dx);
        // Berechne Berührungspunkt mit Außenhülle der Stelle.
        double dockPointX = (r.getCenterX() + (a * Math.cos(aplha)));
        double dockPointY = (r.getCenterY() + (b * Math.sin(aplha)));

        return new Point2D.Double(dockPointX, dockPointY);

    }

    /**
     * 
     * this inner class contains the render information of a place
     *  
     */
    private class StateRenderer extends VertexRenderer
    {

        public void paint(Graphics g)
        {

            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();

            if (super.isOpaque())
            {
                g2.setColor(super.getBackground());
            }

            if (bordercolor != null)
            {
                g2.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
            }
            if (selected)
            {
                g2.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g2.drawRect(b, b, d.width - b - 2, d.height - b - 2);
            }
            if (StateView.this.getStateType() == StateModel.STATE_START_TYPE)
            {
                g2.setColor(Color.BLACK);
                g2.fillOval(b + 7, b + 7, 16 - b - 1, 16 - b - 1);
                // Polygon p = new Polygon();
                // p.addPoint(b + 2, b + 2);
                // p.addPoint(b + 2, d.height -2);
                // p.addPoint(d.width - b - 2, d.height / 2);
                // (b, b, d.width - b - 1, d.height - b - 1);
                // g2.fillPolygon(p);
            } else if (StateView.this.getStateType() == StateModel.STATE_STOP_TYPE)
            {
                g2.setColor(Color.RED);
                g2.fillOval(b + 10, b + 10, 16 - b - 6, 16 - b - 6);
                // g2.fillRect(b + 3, b + 3, d.width - b - 6, d.height - b - 6);
                g2.setColor(Color.BLACK);
                g2.drawOval(b + 7, b + 7, 16 - b - 1, 16 - b - 1);
            }
        }
    }

}
