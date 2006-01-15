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
package org.woped.editor.controller;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import javax.swing.JFrame;

import org.woped.core.config.IConfiguration;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.controller.vep.ApplicationEventProcessor;
import org.woped.editor.controller.vep.EditorEventProcessor;

/**
 * This Class should be the Mediator for the Editor VC... It must be implemented
 * (inherit) by each GUI. The Mediator must register each VC... Actionperformig
 * is only run by messages. Each VC must implement the VC Interface in order to
 * perform actions. The Method is calles by the Mediator... The actions must be
 * an AbstractWopedAction, which get the VC in the Constructor!
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *  
 */
public class ApplicationMediator extends AbstractApplicationMediator
{

    public static final int  VIEWCONTROLLER_EDITOR  = 0;
    public static final int  VIEWCONTROLLER_TASKBAR = 1;
    public static final int  VIEWCONTROLLER_CONFIG  = 2;
    private int              editorCounter          = 0;
    private EditorClipboard  clipboard              = new EditorClipboard();
    private VisualController visualController       = null;
    private HashMap          actionMap              = null;

    public ApplicationMediator()
    {
        this(null, null);
    }

    public ApplicationMediator(IUserInterface ui, IConfiguration conf)
    {
        super(ui, conf);

        visualController = new VisualController(this);
        actionMap = ActionFactory.createStaticActions(this);

        getVepController().register(AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, new ApplicationEventProcessor(AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, this));
        getVepController().register(AbstractViewEvent.VIEWEVENTTYPE_EDIT, new EditorEventProcessor(AbstractViewEvent.VIEWEVENTTYPE_EDIT, this));
        clipboard.addClipboardListener(visualController);

    }

    public void addViewController(IViewController viewController)
    {
        if (viewController != null)
        {
            // viewController.init();
            // viewController.setVisible(true);
            // viewController.setEnabled(true);
            switch (viewController.getViewControllerType())
            {
            case VIEWCONTROLLER_EDITOR:
                editorCounter++;
                // if (getUi() != null) getUi().addEditor((EditorVC)
                // viewController);
                break;
            case VIEWCONTROLLER_TASKBAR:
                // only one
                break;
            default:
                break;
            }
            super.addViewController(viewController);
        }
    }

    public IEditor createEditor(int modelProcessorType, boolean undoSupport)
    {
        EditorVC editor = new EditorVC(EditorVC.ID_PREFIX + editorCounter, clipboard, modelProcessorType, undoSupport);
        addViewController(editor);
        return editor;
    }

    public IViewController createViewController(int type)
    {
        IViewController vc = null;
        switch (type)
        {
        case VIEWCONTROLLER_TASKBAR:
            vc = new TaskBarVC(TaskBarVC.ID_PREFIX);
            break;
        case VIEWCONTROLLER_CONFIG:
            if (getUi() != null && getUi().getComponent() instanceof JFrame)
            {
                vc = new ConfigVC((JFrame) getUi(), true, ConfigVC.ID_PREFIX);
            } else
            {
                vc = new ConfigVC(true, ConfigVC.ID_PREFIX);
            }
            break;
        default:
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No VC created.");
            break;
        }
        return vc;
    }

    public VisualController getVisualController()
    {
        return visualController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.core.controller.AbstractApplicationMediator#setUi(org.woped.core.gui.IUserInterface)
     */
    public void setUi(IUserInterface ui)
    {
        if (ui != null)
        {
            PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ui.getPropertyChangeSupportBean());
            propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        }
        super.setUi(ui);
    }
}
