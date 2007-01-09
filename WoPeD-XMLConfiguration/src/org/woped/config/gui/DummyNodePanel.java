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
package org.woped.config.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.editor.gui.config.AbstractConfPanel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The dummy NodePanel is an example for an <ocde>AbstractConfPanel</code>.
 * 
 * Created on: 02.12.2004 Last Change on: 02.12.2004
 */

@SuppressWarnings("serial")
public class DummyNodePanel extends AbstractConfPanel
{
    /**
     * Contstructor for the <code>DummyNodePanel</code>
     */
    public DummyNodePanel(String name)
    {
        super(name);
        JPanel panel = new JPanel();
        panel.add(new JLabel(name));
        setMainPanel(panel);
    }

    public boolean applyConfiguration()
    {
        return true;
    }

    public void readConfigruation()
    {

    }
}