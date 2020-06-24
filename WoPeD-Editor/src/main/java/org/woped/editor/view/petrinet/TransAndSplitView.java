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

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.Toolspecific;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class TransAndSplitView extends TransSimpleView
{
    private TransAndSplitRenderer renderer = null;

    /**
     * Constructor for OrSplitView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public TransAndSplitView(Object cell, IEditor editor)
    {
        super(cell, editor);
        renderer = new TransAndSplitRenderer(cell);
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    /**
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
     * 
     * this inner class contains the Renderer information of an transition
     * 
     * 28.03.2003
     */
    private class TransAndSplitRenderer extends PetriNetElementRenderer
    {
    	TransAndSplitRenderer(Object cell)
    	{
    		super(cell);
    	}

        public void paint(Graphics g)
        {

            /* Trigger hinzuf���gen */
            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            if (super.isOpaque())
            {
                g.setColor(getFillColor());
                g.fillRect(b - 1, b - 1, d.width - b, d.height - b);
            }
            if (bordercolor != null)
            {
                g.setColor(getFillColor());
                g.fillRect(b - 1, b - 1, d.width - b, d.height - b);        	
                g.setColor(getInnerDrawingsColor());
                g2.setStroke(new BasicStroke(b));
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            g.setColor(getInnerDrawingsColor());
            // AND Split Lines
            //this.drawOperatorArrow(g, true, false);
            Toolspecific t = ((TransitionModel) getCell()).getToolSpecific();
            drawOperatorArrow2(g, t.getOperatorPosition(), t.getOperatorDirection() );
            
            if (isActive())
            {
            	ImageIcon img = Messages.getImageIcon("TokenGame.Active");
                g2.drawImage(img.getImage(), 5, 20, 16, 16, img.getImageObserver());
            }
            
            drawTime(g2, d);
        }

        /**
         * @return
         */
        public boolean isActive()
        {
            return TransAndSplitView.this.isActivated();
        }
               
    }

    /**
     * @see org.woped.editor.core.view.AbstractElementView#paint()
     */
    public void paint()
    {}

    /**
     * @see org.woped.editor.core.view.AbstractElementView#refresh()
     */
    public void refresh()
    {}

}