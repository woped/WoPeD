/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.view.petrinet;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.gui.translations.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         <p>
 *         <p>
 *         29.04.2003
 */

@SuppressWarnings("serial")
public class PlaceView extends PetriNetElementView {

    private static final int TOKEN_RADIUS = 5;

    private PlaceRenderer renderer = null;


    /**
     * Constructor for PlaceView.
     *
     * @param cell
     * @param editor
     */
    public PlaceView(Object cell, IEditor editor) {
        super(cell, editor);
        renderer = new PlaceRenderer(cell);
    }

    /**
     * Returns the token.
     *
     * @return boolean
     */
    public boolean hasTokens() {
        return ((PlaceModel) getCell()).hasTokens();
    }

    public int getTokenCount() {
        return ((PlaceModel) getCell()).getTokenCount();
    }

    public int getVirtualTokenCount() {
        return ((PlaceModel) getCell()).getVirtualTokenCount();
    }

    public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) {

        // liefert die Größe und die Koordinaten der Stelle.
        Rectangle2D r = getBounds();
        // Berechnet den relative Mittelpunkt der Stelle.
        double a = (r.getWidth() - 1) / 2;
        double b = (r.getHeight() - 1) / 2;
        // Berechnet den absoluten Mittelpunkt der Stelle.
        double absCenterX = r.getCenterX();
        double absCenterY = r.getCenterY();
        // Berechnet den Winkel von dem Punkt p zum Mittelpunkt der Stelle.
        double dx = p.getX() - absCenterX;
        double dy = p.getY() - absCenterY;
        // Winkelberechnung siehe Abb. 16. Tangens(aplha) = dy/dx.
        double aplha = Math.atan2(dy, dx);
        // Berechne Berührungspunkt mit Außenhülle der Stelle.
        double dockPointX = (absCenterX + (a * Math.cos(aplha)));
        double dockPointY = (absCenterY + (b * Math.sin(aplha)));

        return new Point2D.Double(dockPointX, dockPointY);
    }

    public void paint() {
    }

    public void refresh() {
    }

    public CellViewRenderer getRenderer() {
        return renderer;
    }

    /**
     * this inner class contains the render information of a place
     */
    private class PlaceRenderer extends PetriNetElementRenderer {
        public PlaceRenderer(Object cell) {
            super(cell);
        }

        public void paint(Graphics g) {

            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            if ( super.isOpaque() ) {
                g.setColor(getFillColor());
                g.fillOval(b - 1, b - 1, d.width - b, d.height - b);
            }
            if ( bordercolor != null ) {
                g.setColor(getFillColor());
                g.fillOval(b, b, d.width - b - 1, d.height - b - 1);
                g.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
                g.drawOval(b, b, d.width - b - 1, d.height - b - 1);
            }
            int relevantTokens = (PlaceView.this.getVirtualTokenCount() == PlaceView.this.getTokenCount()) ? PlaceView.this.getTokenCount() : PlaceView.this.getVirtualTokenCount();
            switch (relevantTokens) {
                case 1:
                    g.setColor(Color.BLACK);
                    g.fillOval(b + (int) (d.getWidth() / 2) - TOKEN_RADIUS, b + (int) (d.getHeight() / 2) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    break;
                case 2:
                    g.setColor(Color.BLACK);
                    g.fillOval(b + (int) (d.getWidth() / 3) - TOKEN_RADIUS, b + (int) (d.getHeight() / 2) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    g.fillOval(b + (int) (d.getWidth() / 3 * 2) - TOKEN_RADIUS, b + (int) (d.getHeight() / 2) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    break;
                case 3:
                    g.setColor(Color.BLACK);
                    g.fillOval(b + (int) (d.getWidth() / 2) - TOKEN_RADIUS, b + (int) (d.getHeight() / 3) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    g.fillOval(b + (int) (d.getWidth() / 3) - TOKEN_RADIUS, b + (int) (d.getHeight() / 3 * 2) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    g.fillOval(b + (int) (d.getWidth() / 3 * 2) - TOKEN_RADIUS, b + (int) (d.getHeight() / 3 * 2) - TOKEN_RADIUS, TOKEN_RADIUS * 2, TOKEN_RADIUS * 2);
                    break;
                default:
                    g.setColor(Color.BLACK);
                    g.setFont(DefaultStaticConfiguration.DEFAULT_TOKEN_FONT);
                    FontMetrics metrics = g.getFontMetrics();
                    String msg = "";

                    if ( relevantTokens > 3 && relevantTokens < 100 ) {
                        msg += relevantTokens;
                    } else if ( relevantTokens > 99 ) {
//                        msg += '\u221E';
                        g.setFont(DefaultStaticConfiguration.UNBOUND_TOKEN_FONT);
                        msg += '\u03c9';
                    }

                    int x = (int) (d.getWidth() - metrics.stringWidth(msg)) / 2;
                    int y = (int) (((d.getHeight() - metrics.getHeight()) / 2) + (metrics.getAscent()));
                    g.drawString(msg, x, y);
                    break;
            }

            if ( isActive() ) {
                // Activate return to parent process switch
                ImageIcon img = Messages.getImageIcon("TokenGame.Subprocess.StepReturn");
                if ( img != null ) g2.drawImage(img.getImage(), 0, 24, 16, 16, img.getImageObserver());
            }

        }

        /**
         * Returns the token.
         *
         * @return boolean
         */
        /*public boolean hasTokens()
        {
			return PlaceView.this.hasTokens();
		}*/

    }
}