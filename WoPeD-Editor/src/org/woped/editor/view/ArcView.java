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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.editor.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ArcModel;
import org.woped.editor.utilities.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class ArcView extends EdgeView
{

    private ArcViewRenderer arcViewRenderer = new ArcViewRenderer();

    /**
     * Constructor for ArcView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public ArcView(Object cell)
    {
        super(cell);
        if (cell instanceof ArcModel){
            setAttributes(((ArcModel)cell).getAttributes());
        }
        if (GraphConstants.getPoints(getAttributes()) == null ){
            List<Point2D> points = new ArrayList<Point2D>(2);
            points.add(getAttributes().createPoint(10, 10));
            points.add(getAttributes().createPoint(20, 20));
            GraphConstants.setPoints(getAttributes(), points);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.EdgeView#getEdgeRenderer()
     */
/*    
    EdgeRenderer getEdgeRenderer()
    {
        return arcViewRenderer;
    }

*/    
    
    
    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.AbstractCellView#getRenderer()
     */
    public CellViewRenderer getRenderer()
    {
        return arcViewRenderer;
    }

    public boolean isActivated()
    {
        return ((ArcModel) cell).isActivated();
    }

    /**
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
     * 
     * this inner class contains the Renderer information of an transition
     * 
     * 28.03.2003
     */
    private class ArcViewRenderer extends EdgeRenderer
    {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.Component#paint(java.awt.Graphics)
         */
        public void paint(Graphics g)
        {
            Shape edgeShape = view.getShape();
            // Sideeffect: beginShape, lineShape, endShape
            if (edgeShape != null)
            {
                // super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                int c = BasicStroke.CAP_BUTT;
                int j = BasicStroke.JOIN_MITER;
                g2.setStroke(new BasicStroke(lineWidth, c, j));
                setOpaque(false);
                translateGraphics(g);
                g.setColor(getForeground());
                if (getGradientColor() != null && !preview)
                {
                    g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), getGradientColor(), true));
                }
                if (view.beginShape != null)
                {
                    if (beginFill) g2.fill(view.beginShape);
                    g2.draw(view.beginShape);
                }
                if (view.endShape != null)
                {
                    if (endFill) g2.fill(view.endShape);
                    g2.draw(view.endShape);
                }
                if (lineDash != null) // Dash For Line Only
                g2.setStroke(new BasicStroke(lineWidth, c, j, 10.0f, lineDash, dashOffset));
                if (view.lineShape != null) g2.draw(view.lineShape);

                if (selected)
                {
                    g2.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                    if (view.beginShape != null) g2.draw(view.beginShape);
                    if (view.lineShape != null) g2.draw(view.lineShape);
                    if (view.endShape != null) g2.draw(view.endShape);
                }
                if (isActivated())
                {
                    g2.setColor(Color.GREEN);
                    g.setColor(Color.GREEN);
                    g2.setStroke(new BasicStroke(1));
                    if (view.beginShape != null) g2.draw(view.beginShape);
                    if (view.lineShape != null) g2.draw(view.lineShape);
                    if (view.endShape != null) g2.draw(view.endShape);
                    
                    // Draw 'play' button to indicate that the
                    // arc is activated and can be clicked
                    ImageIcon img = Messages.getImageIcon("TokenGame.Active");
                    Rectangle labelPos = this.getBounds();
                    int buttonX  = (int)(labelPos.x+labelPos.width/2);
                    int buttonY  = (int)(labelPos.y+labelPos.height/2);
                    g2.drawImage(img.getImage(), buttonX, buttonY , 16, 16, img.getImageObserver());

                }
            }
        }

        private boolean isActivated()
        {
            return ArcView.this.isActivated();
        }
    }

}