/*
 * Created on 01.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.view.petrinet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.model.petrinet.TransitionResourceModel;
import org.woped.view.AbstractElementView;

/**
 * @author waschtl
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TransitionResourceView extends AbstractElementView
{
    private TransitionResourceRenderer renderer = new TransitionResourceRenderer();

    /**
     * @param cell
     */
    public TransitionResourceView(Object cell)
    {
        super(cell);
        // TODO Auto-generated constructor stub
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    class TransitionResourceRenderer extends VertexRenderer
    {
        public void paint(Graphics g)
        {
            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            if (super.isOpaque())
            {
                g.setColor(super.getBackground());
                //g.fillRect(b - 1, b - 1, d.width - b, d.height - b);

            }
            if (bordercolor != null)
            {
                g.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(b));
            }
            if (selected)
            {
                g.setColor(graph.getHighlightColor());
            }
            g.drawLine(0,0,TransitionResourceModel.DEFAULT_WIDTH-1,0);
            g.drawLine(0,TransitionResourceModel.DEFAULT_HEIGHT/2,TransitionResourceModel.DEFAULT_WIDTH-1,TransitionResourceModel.DEFAULT_HEIGHT/2);
            g.drawLine(0,TransitionResourceModel.DEFAULT_HEIGHT-1,TransitionResourceModel.DEFAULT_WIDTH-1,TransitionResourceModel.DEFAULT_HEIGHT-1);
            g.drawLine(0,0,0,TransitionResourceModel.DEFAULT_HEIGHT-1);
            g.drawLine(TransitionResourceModel.DEFAULT_WIDTH-1,0,TransitionResourceModel.DEFAULT_WIDTH-1,TransitionResourceModel.DEFAULT_HEIGHT-1);
            g2.setFont(new Font("Verdana", Font.PLAIN, 10));
            g2.drawString(((TransitionResourceModel) TransitionResourceView.this.getCell()).getTransOrgUnitName(), 2, (TransitionResourceModel.DEFAULT_HEIGHT/2)-1);
            g2.drawString(((TransitionResourceModel) TransitionResourceView.this.getCell()).getTransRoleName(), 2, TransitionResourceModel.DEFAULT_HEIGHT-2);
            
            
        }

    }
}