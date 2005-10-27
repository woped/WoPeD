package org.woped.controller.vc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import org.woped.controller.DefaultApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.editor.utilities.SynchonizeTask;

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
public class StatusBarVC extends JPanel implements IViewController
{
    private JLabel             m_EditorNumberLabel = null;
    private JProgressBar       m_progressBar       = null;
    private JLabel             m_progressBarLabel  = null;

    private Timer              timer               = null;
    private String             id                  = null;
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
        this.add(getProgressBarLabel(), c);
        c.gridx = 3;
        this.add(getProgressBar(), c);

        stopProgressBar();
    }

    private JLabel getEditorNumberLabel()
    {
        if (m_EditorNumberLabel == null)
        {
            m_EditorNumberLabel = new JLabel("Editors: ".concat(String.valueOf(0)));
            m_EditorNumberLabel.setHorizontalAlignment(JLabel.LEFT);
        }
        return m_EditorNumberLabel;
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param i
     */
    public void updateEditosNumber(int i)
    {
        getEditorNumberLabel().setText("Editors: ".concat(String.valueOf(i)));
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param progressText
     */
    public void startProgressBar(String progressText, SynchonizeTask task)
    {
        m_progressBar.setVisible(true);
        this.task = task;
        timer.start();
        task.go(getProgressBar());
        getProgressBarLabel().setForeground(Color.BLACK);
        getProgressBarLabel().setText(progressText);
        m_progressBar.setStringPainted(true);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *  
     */
    public void stopProgressBar()
    {
        timer.stop();
        getProgressBarLabel().setForeground(Color.GRAY);
        getProgressBarLabel().setText("");
        m_progressBar.setStringPainted(false);
        m_progressBar.setVisible(false);
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

    private JLabel getProgressBarLabel()
    {
        if (m_progressBarLabel == null)
        {
            m_progressBarLabel = new JLabel();
            m_progressBarLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return m_progressBarLabel;
    }

    public JProgressBar getProgressBar()
    {
        if (m_progressBar == null)
        {
            m_progressBar = new JProgressBar();
            m_progressBar.setValue(0);
            m_progressBar.setPreferredSize(new Dimension(200, 12));
            timer = new Timer(100, new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    m_progressBar.setValue(getSyncTask().getCurrent());
                    if (task.isDone())
                    {
                        Toolkit.getDefaultToolkit().beep();
                        timer.stop();
                    }

                }
            });

        }
        return m_progressBar;
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
}