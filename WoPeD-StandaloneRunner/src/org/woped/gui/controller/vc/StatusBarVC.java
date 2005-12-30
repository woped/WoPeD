package org.woped.gui.controller.vc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewListener;
import org.woped.editor.utilities.Messages;
import org.woped.editor.utilities.SynchonizeTask;
import org.woped.gui.controller.DefaultApplicationMediator;

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
/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (silenco) Created on: 05.03.2005 Last Change on:
 *         05.03.2005
 */
public class StatusBarVC extends JPanel implements IViewController, IStatusBar
{
    private JLabel             m_EditorNumberLabel = null;

    private JProgressBar       m_progressBar       = null;

    private JLabel             m_statusLabel       = null;

    private String             id                  = null;

    private int                progressBarCount;

    public static final String ID_PREFIX           = "STATUSBAR_VC_";

    private Vector             viewListener        = new Vector(1, 3);

    SynchonizeTask             task;

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public StatusBarVC(String id)
    {
        initialize();
    }

    private void initialize()
    {
        this.setLayout(new GridBagLayout());
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.setPreferredSize(new Dimension(800, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = 1;

        c.gridx = 1;
        this.add(getEditorNumberLabel(), c);
        // c.gridx = 2;
        // this.add(new JLabel(), c);
        c.gridx = 2;
        getStatusLabel().setText("");
        this.add(getStatusLabel(), c);
        c.gridx = 3;
        this.add(getProgressBar(), c);
        progressBarCount = -1;
    }

    private JLabel getEditorNumberLabel()
    {
        if (m_EditorNumberLabel == null)
        {
            m_EditorNumberLabel = new JLabel(Messages.getString("Statusbar.Editors") + ": " + String.valueOf(0));
            m_EditorNumberLabel.setHorizontalAlignment(JLabel.LEFT);
        }
        return m_EditorNumberLabel;
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @return
     */
    public SynchonizeTask getSyncTask()
    {
        return task;
    }

    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
        return DefaultApplicationMediator.VIEWCONTROLLER_STATUSBAR;
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    public final void fireViewEvent(AbstractViewEvent viewevent)
    {
        if (viewevent == null) return;
        java.util.Vector vector;
        synchronized (viewListener)
        {
            vector = (java.util.Vector) viewListener.clone();
        }
        if (vector == null) return;
        int i = vector.size();
        for (int j = 0; !viewevent.isConsumed() && j < i; j++)
        {
            IViewListener viewlistener = (IViewListener) vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    public JLabel getStatusLabel()
    {
        if (m_statusLabel == null)
        {
            m_statusLabel = new JLabel();
            m_statusLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return m_statusLabel;
    }

    public JProgressBar getProgressBar()
    {
        if (m_progressBar == null)
        {
            m_progressBar = new JProgressBar();
        }
        return m_progressBar;
    }

    public boolean startProgress(String description, int maxValue)
    {
        if (isRunning()) return false;
        getProgressBar().setMaximum(maxValue);
        getStatusLabel().setText(description);
        progressBarCount = 0;
        return true;
    }

    public boolean nextStep()
    {
        if (!isRunning()) return false;
        progressBarCount++;
        getProgressBar().setValue(progressBarCount);
        if (progressBarCount == getProgressBar().getMaximum())
        {
            progressBarCount = -1;
            getProgressBar().setMaximum(0);
            getProgressBar().setValue(0);
            getStatusLabel().setText("");
        }
        return true;
    }

    public boolean isRunning()
    {
        if (progressBarCount == -1) return false;
        return true;
    }
}