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
package org.woped.editor.view.petrinet;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jgraph.graph.CellViewRenderer;
import org.woped.core.config.ConfigurationManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class TransAndJoinXOrSplitView extends TransSimpleView
{

    private TransAndJoinXOrSplitRenderer renderer = null;

    /**
     * Constructor for TransAndJoinXOrSplitView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public TransAndJoinXOrSplitView(Object cell)
    {
        super(cell);
        renderer = new TransAndJoinXOrSplitRenderer(cell);
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
    private class TransAndJoinXOrSplitRenderer extends AbstractElementRenderer
    {
    	TransAndJoinXOrSplitRenderer(Object cell)
    	{
    		super(cell);
    	}
    	
        public void paint(Graphics g)
        {

            /* Trigger hinzufügen */
            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            
            if (super.isOpaque())
            {
                g.setColor(getFillColor());
                g.fillRect(b - 1, b - 1, d.width - b, d.height - b);
            }
            g.setColor(getFillColor());
            g.fillRect(b - 1, b - 1, d.width - b, d.height - b);        	
                        
            g2.setStroke(new BasicStroke(b));
            g.setColor(getInnerDrawingsColor());
            	
            // AND JOIN Lines
            drawOperatorArrow(g,false,true);
            // XOR Split Lines
            drawOperatorArrow(g,true,true);

            if (bordercolor != null)
            {
                g.setColor(bordercolor);
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            
        }
        /**
         * @return
         */
        public boolean isActive()
        {
            return TransAndJoinXOrSplitView.this.isActivated();
        }

        /**
         * @return
         */
        public boolean isFireing()
        {
            return TransAndJoinXOrSplitView.this.isFireing();
        }
    }

}