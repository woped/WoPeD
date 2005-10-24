package org.woped.view.uml;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.config.ConfigurationManager;
import org.woped.model.uml.OperatorModel;
import org.woped.view.AbstractElementView;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OperatorView extends AbstractElementView
{
    private OperatorRenderer renderer = new OperatorRenderer();

    public OperatorView(Object cell)
    {
        super(cell);
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    public int getOperatorType()
    {
        return ((OperatorModel) getCell()).getOperatorType();
    }

    public Point2D getPerimeterPoint(Point2D source, Point2D p)
    {
        Rectangle2D r = getBounds();
        if (getOperatorType() == OperatorModel.XOR_TYPE)
        {
            return super.getPerimeterPoint(source, p);
        } else if (getOperatorType() == OperatorModel.AND_TYPE)
        {
            return new Point2D.Double(r.getCenterX(), r.getCenterY());
        } else
        {
            return super.getPerimeterPoint(source, p);
        }

        //            // liefert die Größe und die Koordinaten der Stelle.
        //            Rectangle2D r = getBounds();
        //            double x = r.getX();
        //            double y = r.getY();
        //            // Berechnet den relative Mittelpunkt der Stelle.
        //            double a = (r.getWidth() - 1) / 2;
        //            double b = (r.getHeight() - 1) / 2;
        //            // Berechnet den absoluten Mittelpunkt der Stelle.
        //            double absCenterX = r.getCenterX();
        //            double absCenterY = r.getCenterY();
        //            // Berechnet den Winkel von dem Punkt p zum Mittelpunkt der Stelle.
        //            double dx = p.getX() - absCenterX;
        //            double dy = p.getY() - absCenterY;
        //            // Winkelberechnung siehe Abb. 16. Tangens(aplha) = dy/dx.
        //            double aplha = Math.atan2(dy, dx);
        //            // Berechne Berührungspunkt mit Außenhülle der Stelle.
        //           = (absCenterX + (a * Math.cos(aplha)));
        //            double dockPointY = (absCenterY + (b * Math.sin(aplha)));

    }

    /**
     * this inner class contains the render information of an UML Operator
     */
    private class OperatorRenderer extends VertexRenderer
    {

        public void paint(Graphics g)
        {

            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            boolean tmp = selected;
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
                g2.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            int operatorType = getOperatorType();
            if (operatorType == OperatorModel.XOR_TYPE)
            {
                g2.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                Polygon p = new Polygon();
                p.addPoint(b, d.height / 2);
                p.addPoint(d.width / 2, b);
                p.addPoint(d.width - b, d.height / 2);
                p.addPoint(d.width / 2, d.height - b);
                g2.fillPolygon(p);
            } else if (operatorType == OperatorModel.AND_TYPE)
            {
                g2.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g2.fillRect((d.width / 2) - 3, b, 6, (d.height) - b);
            }
        }
    }

}