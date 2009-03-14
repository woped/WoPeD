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
/*
 * Created on Oct 19, 2004
 */
package org.woped.editor.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicToolTipUI;

import org.woped.core.config.DefaultStaticConfiguration;

/**
 * @author Thomas Pohl TODO: DOCUMENTATION (xraven)
 */
public class EditorToolTipUI extends BasicToolTipUI
{

    CellRendererPane m_renderPane = new CellRendererPane();
    JTextArea        m_textPane;

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void installUI(JComponent arg0)
    {
        super.installUI(arg0);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void paint(Graphics g, JComponent c)
    {
        m_textPane.setBackground(c.getBackground());
        m_renderPane.paintComponent(g, m_textPane, c, 1, 1, c.getWidth() - 1, c.getHeight() - 1);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void uninstallUI(JComponent arg0)
    {
        super.uninstallUI(arg0);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public Dimension getMaximumSize(JComponent arg0)
    {
        return getPreferredSize(arg0);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public Dimension getMinimumSize(JComponent arg0)
    {
        return getPreferredSize(arg0);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public Dimension getPreferredSize(JComponent arg0)
    {
        if (arg0 instanceof EditorToolTip)
        {
            String text = ((EditorToolTip) arg0).getTipText();
            m_renderPane.removeAll();

            if (text == null || "".equals(text))
            {
                return null;
            }
            m_textPane = new JTextArea(text);
            m_textPane.setFont(DefaultStaticConfiguration.DEFAULT_TOOLTIP_FONT);
            m_textPane.setMargin(new Insets(3, 5, 3, 5));
            m_renderPane.add(m_textPane);

            Dimension dim = m_textPane.getPreferredSize();
            dim.height += 2;
            dim.width += 2;
            return dim;
        }
        return null;
    }
}