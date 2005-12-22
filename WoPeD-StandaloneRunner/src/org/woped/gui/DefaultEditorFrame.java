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
/*
 * Created on Oct 5, 2004
 */
package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.editor.controller.PetriNetResourceEditor;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.gui.StatusBarLabel;
import org.woped.editor.utilities.Messages;

/**
 * The EditorFrame is the container for each <code>Editor</code>.
 * 
 * @author Thomas Pohl
 */
public class DefaultEditorFrame extends JInternalFrame
{
    private EditorVC               m_editor                 = null;
    private PetriNetResourceEditor m_petriNetResourceEditor = null;
    private StatusBarLabel         m_statusBarInfo          = null;

    private static final int       DEFAULT_WIDTH            = 500;
    private static final int       DEFAULT_HEIGHT           = 500;

    public DefaultEditorFrame(EditorVC editor, PetriNetResourceEditor propEditor)
    {
        super(editor.getFileName(), true, true, true, true);
        this.setVisible(false);
        m_editor = editor;
        m_petriNetResourceEditor = propEditor;
        this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        this.setFrameIcon(DefaultStaticConfiguration.DEFAULTEDITORFRAMEICON);
        // Size
        editor.setPreferredSize(editor.getSavedSize());
        if (propEditor != null)
        {
            // TabbedPane
            JTabbedPane tabbedPane = new JTabbedPane();
            JScrollPane propScrollPane = new JScrollPane(getPetriNetResourceEditor());
            tabbedPane.addTab(Messages.getString("PetriNet.Process.Title"), getEditor());
            tabbedPane.addTab(Messages.getString("PetriNet.Resources.Title"), propScrollPane);
            this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            propScrollPane.addFocusListener(new FocusListener()
                    {
                        public void focusGained(FocusEvent e)
                        {
                            getPetriNetResourceEditor().reset();                         
                        }
                        public void focusLost(FocusEvent e)
                        {
                            ;                         
                        }
                    });
        } else
        {
            this.getContentPane().add(editor, BorderLayout.CENTER);
        }
        // Statusbar
        JPanel statusBar = new JPanel(new BorderLayout());
        m_statusBarInfo = new StatusBarLabel(m_editor);
        statusBar.add(m_statusBarInfo, BorderLayout.CENTER);
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        setTitle(editor.getFileName());

        this.pack();
        this.repaint();
        editor.setSaved(true);
        this.setVisible(true);
    }

    /**
     * 
     * @return returns the containing Editor.
     */

    public EditorVC getEditor()
    {
        return m_editor;
    }

    /**
     * @return Returns the m_petriNetResourceEditor.
     */
    public PetriNetResourceEditor getPetriNetResourceEditor()
    {
        return m_petriNetResourceEditor;
    }

    /**
     * 
     * @str Text to be displayed in status bar.
     */
    public void setStatusBarInfo(String str)
    {
        m_statusBarInfo.setText(str);
    }

}