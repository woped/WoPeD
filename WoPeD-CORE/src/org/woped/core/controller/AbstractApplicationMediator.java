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
package org.woped.core.controller;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IConfiguration;
import org.woped.core.gui.IEditorAware;
import org.woped.core.gui.IUserInterface;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *  
 */
public abstract class AbstractApplicationMediator implements IViewListener
{
    private HashMap<String, IViewController>        viewControllerMap = null;
    private VEPController  vepController     = null;
    private IUserInterface ui                = null;
    private LinkedList<Object> editorLists = new LinkedList<Object>();

    public AbstractApplicationMediator(IUserInterface ui, IConfiguration conf)
    {
        viewControllerMap = new HashMap<String, IViewController>();
        setUi(ui);
        LoggerManager.info(Constants.CORE_LOGGER, "START INIT Application");
        boolean confOK = true;
        if (conf != null)
        {
            // Init Configuration
            confOK = conf.initConfig();
        }
        // use system look and feel
        try
        {
            if (!confOK)
            {
                // This should NEVER happen!
                LoggerManager.fatal(Constants.CORE_LOGGER, "Could really not load any configuration.");
                LoggerManager.info(Constants.CORE_LOGGER, "EXIT WoPeD - LOGGING DEACTIVATED");
                System.exit(0);
            } else
            {
                ConfigurationManager.setConfiguration(conf);
                if (ConfigurationManager.getConfiguration().getLookAndFeel() != null)
                {
                    UIManager.setLookAndFeel(ConfigurationManager.getConfiguration().getLookAndFeel());
                } else
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    ConfigurationManager.getConfiguration().setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            }
            LoggerManager.debug(Constants.CORE_LOGGER, "Look-And-Feel set.");
        } catch (Exception e)
        {
            LoggerManager.error(Constants.CORE_LOGGER, "Could not set System Look-And-Feel" + ConfigurationManager.getConfiguration().getLookAndFeel());
        }
    }

    public abstract IEditor createEditor(int modelProcessorType, boolean undoSupport);
    
    //! Create a sub-process editor window for the specified sub-process
    //! @param modelProcessorType specifies the model processor type for the underlying model
    //! @param undoSupport specifies whether or not undo should be supported
    //! @param parentEditor specifies a pointer to the parent editor
    //! @param subProcess specifies the sub-process element whose content should be edited
    //! @return new editor object
    public abstract IEditor createSubprocessEditor(int modelProcessorType, boolean undoSupport, IEditor parentEditor,
    		SubProcessModel subProcess);

    public static IViewController createViewController(String className, String id)
    {
        Class[] argsClass = null;
        Object[] args = null;

        argsClass = new Class[] { String.class };
        args = new Object[] { id, };
        try
        {

            // Creating the ViewController via Reflection
            ClassLoader classLoader = AbstractApplicationMediator.class.getClassLoader();
            Class theClass = classLoader.loadClass(className);

            IViewController vc = null;
            Constructor constructor = null;
            if (argsClass != null)
            {
                constructor = theClass.getConstructor(argsClass);
                vc = (IViewController) constructor.newInstance(args);
            } else
            {
                vc = (IViewController) theClass.newInstance();
            }

            return vc;
        } catch (Exception e1)
        {
            LoggerManager.error(Constants.CORE_LOGGER, "Could not create the ViewController (ID:" + id + ")" + e1.getMessage());
            return null;
        }
    }

    /**
     * Returns the reference to the controller of ViewEventProcessors (VEP),
     * which holds a map of VEPs, where the key is the major code of the event
     * to be handled.
     * 
     * @return
     */
    public VEPController getVepController()
    {
        if (vepController == null)
        {
            vepController = new VEPController();
        }
        return vepController;
    }

    public void removeViewController(IViewController viewController)
    {
        viewControllerMap.remove(viewController.getId());
    }

    public void fireViewEvent(AbstractViewEvent viewevent)
    {
        viewEventPerformed(viewevent);
    }

    public final void viewEventPerformed(AbstractViewEvent viewevent)
    {
        processViewEvent(viewevent);
    }

    public void processViewEvent(AbstractViewEvent viewevent)
    {
        AbstractEventProcessor vep = null;
        if ((vep = (AbstractEventProcessor) getVepController().lookup(viewevent.getType())) != null) vep.processViewEvent(viewevent);
    }

    public void addViewController(IViewController viewController)
    {
        viewController.addViewListener(this);
        viewControllerMap.put(viewController.getId(), viewController);
        if (viewController instanceof IEditorAware)
        {
            editorLists.add(viewController);
        }
    }

    public IViewController getViewController(String id)
    {
        return (IViewController) viewControllerMap.get(id);
    }

    public Map<String, IViewController> getViewControllers()
    {
        return viewControllerMap;
    }

    public IUserInterface getUi()
    {
        return ui;
    }

    public void setUi(IUserInterface ui)
    {
        this.ui = ui;
        if (ui != null)
        {
            editorLists.add(ui);
        }
    }

    public IViewController[] findViewController(int type)
    {
        ArrayList<IViewController> aL = new ArrayList<IViewController>();
        switch (type)
        {
        case IStatusBar.TYPE:
            for (IViewController iViewC : viewControllerMap.values()) {
        	if (iViewC instanceof IStatusBar) {
                    aL.add(iViewC);
                }
            }
            
            break;

        case IEditor.TYPE:
            for (IViewController iViewC : viewControllerMap.values()) {
        	if (iViewC instanceof IEditor) {
                    aL.add(iViewC);
                }
            }
            
            break;
        }
        IViewController[] iwC = new IViewController[aL.size()];
        for (int i = 0; i < aL.size(); i++) {
            iwC[i] = (IViewController) aL.get(i);
        }

        return iwC;
    }

    public List getEditorAwareVCs()
    {

        return (List) editorLists.clone();
    }
}
