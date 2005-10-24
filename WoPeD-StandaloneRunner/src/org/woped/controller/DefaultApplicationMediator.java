package org.woped.controller;

import java.io.File;

import org.woped.config.IConfiguration;
import org.woped.controller.vc.ConfigVC;
import org.woped.controller.vc.MenuBarVC;
import org.woped.controller.vc.StatusBarVC;
import org.woped.controller.vc.TaskBarVC;
import org.woped.controller.vc.ToolBarVC;
import org.woped.controller.vep.FileEventProcessor;
import org.woped.controller.vep.GUIViewEventProcessor;
import org.woped.gui.DefaultUserInterface;
import org.woped.gui.IUserInterface;
import org.woped.gui.config.ConfEditorPanel;
import org.woped.gui.config.ConfFilePanel;
import org.woped.gui.config.ConfToolsPanel;

public class DefaultApplicationMediator extends ApplicationMediator
{
    public static final int VIEWCONTROLLER_TOOLBAR   = 10;
    public static final int VIEWCONTROLLER_MENU      = 11;
    public static final int VIEWCONTROLLER_STATUSBAR = 12;

    private int             toolbarCounter           = 0;
    private int             menuCounter              = 0;
    private int             statusCounter            = 0;

    public DefaultApplicationMediator(IUserInterface ui, IConfiguration conf, String[] args)
    {
        super(ui, conf);
        getVepController().register(ViewEvent.VIEWEVENTTYPE_GUI, new GUIViewEventProcessor(ViewEvent.VIEWEVENTTYPE_GUI, this));
        getVepController().register(ViewEvent.VIEWEVENTTYPE_FILE, new FileEventProcessor(ViewEvent.VIEWEVENTTYPE_FILE, this));
        if (ui == null)
        {
            ToolBarVC toolbar = (ToolBarVC) this.createViewController(VIEWCONTROLLER_TOOLBAR);
            addViewController(toolbar);
            MenuBarVC menubar = (MenuBarVC) this.createViewController(VIEWCONTROLLER_MENU);
            addViewController(menubar);
            TaskBarVC taskbar = (TaskBarVC) this.createViewController(VIEWCONTROLLER_TASKBAR);
            addViewController(taskbar);
            StatusBarVC statusbar = (StatusBarVC) this.createViewController(VIEWCONTROLLER_STATUSBAR);
            addViewController(statusbar);
            ConfigVC config = (ConfigVC) this.createViewController(ApplicationMediator.VIEWCONTROLLER_CONFIG);
            addViewController(config);
            // Add Conf Node-Panels
            // TODO: addConfNodePanel(null, new ConfGuiPanel("GUI"));
            config.addConfNodePanel(null, new ConfEditorPanel("Editor"));
            config.addConfNodePanel(null, new ConfFilePanel("Files"));
            config.addConfNodePanel(null, new ConfToolsPanel("Tools"));
            config.setSelectedPanel("Editor");

            ui = new DefaultUserInterface(toolbar, menubar, taskbar, statusbar);
            setUi(ui);
            if (args != null)
            {
                for (int i = 0; i < args.length; i++)
                {
                    File f = new File(args[i]);
                    fireViewEvent(new ViewEvent(null, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, f));
                }
            }
        }
    }

    public IViewController createViewController(int type)
    {

        IViewController vc = null;
        if (type == VIEWCONTROLLER_TOOLBAR)
        {
            vc = new ToolBarVC(ToolBarVC.ID_PREFIX + toolbarCounter);
            toolbarCounter++;
        } else if (type == VIEWCONTROLLER_MENU)
        {
            vc = new MenuBarVC(MenuBarVC.ID_PREFIX + menuCounter);
            menuCounter++;
        } else if (type == VIEWCONTROLLER_STATUSBAR)
        {
            vc = new StatusBarVC(StatusBarVC.ID_PREFIX + statusCounter);
        } else
        {
            vc = super.createViewController(type);
        }
        return vc;
    }

    public void addViewController(IViewController viewController)
    {
        super.addViewController(viewController);
    }
}
