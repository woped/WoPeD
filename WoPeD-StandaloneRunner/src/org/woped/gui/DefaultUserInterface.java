package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.PetriNetPropertyEditor;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.utilities.Messages;
import org.woped.gui.controller.ViewEvent;
import org.woped.gui.controller.vc.MenuBarVC;
import org.woped.gui.controller.vc.StatusBarVC;
import org.woped.gui.controller.vc.ToolBarVC;

public class DefaultUserInterface extends JFrame implements IUserInterface, InternalFrameListener
{
    private JDesktopPane          desktop                = null;
    public static final int       DEFAULT_FRAME_DISTANCE = 20;
    // Used VC
    private StatusBarVC           statusBar              = null;
    private TaskBarVC             taskBar                = null;
    private ToolBarVC             toolBar                = null;

    private PropertyChangeSupport propertyChangeSupport  = null;
    private int                   m_numEditors           = 0;
    private List                  editorList             = new ArrayList();

    public DefaultUserInterface(ToolBarVC toolBar, MenuBarVC menuBar, TaskBarVC taskBar, StatusBarVC statusBar)
    {
        super();
        this.toolBar = toolBar;
        this.statusBar = statusBar;
        this.taskBar = taskBar;

        desktop = new JDesktopPane();
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(desktop);
        propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());

        setTitle("WoPeD Version " + Messages.getWoPeDVersion(false));
        setBounds(ConfigurationManager.getConfiguration().getWindowX(), ConfigurationManager.getConfiguration().getWindowY(), (int) ConfigurationManager.getConfiguration().getWindowSize().getWidth(), (int) ConfigurationManager
                .getConfiguration().getWindowSize().getHeight());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                quit();
            }
        });

        if (ConfigurationManager.getConfiguration().getHomedir() == null) ConfigurationManager.getConfiguration().setHomedir("nets/");

        setJMenuBar(menuBar);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(desktop, BorderLayout.CENTER);

        // Prepare Status & Taskbar
        getContentPane().add(taskBar, BorderLayout.SOUTH);

        setVisible(true);
        LoggerManager.info(Constants.GUI_LOGGER, "END  INIT Application");

        // addKeyListener(this);
        SplashWindow splash = new SplashWindow(this);
        splash.kill();
    }

    public void addEditor(IEditor editor)
    {
        if (editor != null)
        {
            Point position = getNextEditorPosition();
            DefaultEditorFrame frame;
            if (editor.getModelProcessor().getProcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
            {
                frame = new DefaultEditorFrame((EditorVC) editor, new PetriNetPropertyEditor((PetriNetModelProcessor) editor.getModelProcessor()));
            } else
            {
                frame = new DefaultEditorFrame((EditorVC) editor, null);
            }
            frame.setAlignmentX((float) position.getX());
            frame.setAlignmentY((float) position.getY());
            frame.addInternalFrameListener(this);
            frame.setLocation(position);
            desktop.add(frame, BorderLayout.CENTER);
            editorList.add(frame.getEditor());
            frame.getEditor().setContainer(frame);
            // frame.setBounds((int) position.getX(), (int) position.getY(),
            // (int) frame.getBounds().getWidth(), (int)
            // frame.getBounds().getHeight());
            m_numEditors++;
            frame.setVisible(true);
            try
            {
                frame.setSelected(true);
            } catch (PropertyVetoException e)
            {
                LoggerManager.error(Constants.GUI_LOGGER, "VetoException Could not Select Frame");
            }

        }
    }

    public void quit()
    {
        boolean usercanceled = false;
        JInternalFrame frame = null;
        JInternalFrame oldFrame = null;
        for (int i = 0; i < desktop.getAllFrames().length && !usercanceled; i++)
        {
            frame = desktop.getAllFrames()[i];
            if (oldFrame == frame)
            {
                usercanceled = true;
            } else
            {
                oldFrame = frame;
                frame.dispose();
            }
        }
        if (!usercanceled)
        {
            setExtendedState(NORMAL);
            ConfigurationManager.getConfiguration().setWindowX(getX());
            ConfigurationManager.getConfiguration().setWindowY(getY());
            ConfigurationManager.getConfiguration().setWindowSize(getSize());
            ConfigurationManager.getConfiguration().saveConfig();
            try
            {
                LoggerManager.info(Constants.GUI_LOGGER, "EXIT WoPeD");
                System.exit(0);
            } catch (AccessControlException ace)
            {
                setVisible(false);
            }
        } else
        {
            LoggerManager.debug(Constants.GUI_LOGGER, "User has canceled quit-operation.");
        }
    }

    public Component getComponent()
    {
        return this;
    }

    public void removeEditor(IEditor editor)
    {
        // if (!checkSave(editor))
        {
            ((DefaultEditorFrame) editor.getContainer()).dispose();
            editorList.remove(editor);
            m_numEditors--;
            // try to Select a different Frame
            if (desktop.getAllFrames().length > 0)
            {
                if (desktop.getAllFrames()[0] != null)
                {
                    try
                    {
                        desktop.getAllFrames()[0].setSelected(true);
                    } catch (PropertyVetoException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void requestEditorFocus(IEditor editor)
    {
        try
        {
            JInternalFrame iframe = (JInternalFrame) editor.getContainer();
            if (iframe.isIcon())
            {
                iframe.setIcon(false);
            }
            iframe.setSelected(true);
        } catch (PropertyVetoException e)
        {
            LoggerManager.debug(Constants.GUI_LOGGER, "Could not select Frame");
        }
    }

    public IEditor getEditorFocus()
    {
        if (desktop.getSelectedFrame() != null)
        {
            return ((DefaultEditorFrame) desktop.getSelectedFrame()).getEditor();
        }
        return null;
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *  
     */
    public void cascadeFrames()
    {
        JInternalFrame[] allFrames = desktop.getAllFrames();

        Vector allOpenFrames = new Vector();

        for (int i = 0; i < allFrames.length; i++)
        {
            if (!allFrames[i].isIcon())
            {
                allOpenFrames.add(0, allFrames[i]);
            }
        }
        for (int i = 0; i < allOpenFrames.size(); i++)
        {
            Rectangle bounds = ((JInternalFrame) allOpenFrames.get(i)).getBounds();

            ((JInternalFrame) allOpenFrames.get(i)).setBounds(i * DEFAULT_FRAME_DISTANCE, i * DEFAULT_FRAME_DISTANCE, (int) bounds.getWidth(), (int) bounds.getHeight());
            try
            {
                ((JInternalFrame) allOpenFrames.get(i)).setSelected(true);
            } catch (PropertyVetoException e)
            {}
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *  
     */
    public void arrangeFrames()
    {
        JInternalFrame[] allFrames = desktop.getAllFrames();

        Vector allOpenFrames = new Vector();

        for (int i = 0; i < allFrames.length; i++)
        {
            if (!allFrames[i].isIcon())
            {
                allOpenFrames.add(0, allFrames[i]);
            }
        }

        int xCount = (int) Math.ceil(Math.sqrt(allOpenFrames.size()));
        int yCount = (int) Math.ceil((double) allOpenFrames.size() / xCount);

        int frameWidth = desktop.getWidth() / xCount;
        int frameHeight = desktop.getHeight() / yCount;
        int lastRowWidth = desktop.getWidth() / (xCount - (xCount * yCount - allOpenFrames.size()));

        LoggerManager.debug(Constants.GUI_LOGGER, "Frames" + allOpenFrames.size() + ":" + xCount + "x" + yCount);

        for (int i = 0; i < allOpenFrames.size(); i++)
        {
            JInternalFrame currentFrame = ((JInternalFrame) allOpenFrames.get(i));
            int row = i / xCount;
            int col = i % xCount;

            if ((row + 1) == yCount)
            {
                // LastRow
                currentFrame.setBounds(new Rectangle(col * lastRowWidth, row * frameHeight, lastRowWidth, frameHeight));
            } else
            {
                currentFrame.setBounds(new Rectangle(col * frameWidth, row * frameHeight, frameWidth, frameHeight));
            }

        }
    }

    /* ########################## GETTER & SETTER ######################### */

    public StatusBarVC getStatusBar()
    {
        return statusBar;
    }

    public TaskBarVC getTaskBar()
    {
        return taskBar;
    }

    public ToolBarVC getToolBar()
    {
        return toolBar;
    }

    public List getAllEditors()
    {
        return editorList;
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @return
     */
    public Point getNextEditorPosition()
    {
        JInternalFrame[] allFrames = desktop.getAllFrames();
        int x = 0;
        int y = 0;
        boolean xfinished = false;
        boolean yfinished = false;
        while (!xfinished)
        {
            xfinished = true;
            for (int i = 0; i < allFrames.length; i++)
            {
                if (Math.abs(allFrames[i].getX() - x) < DEFAULT_FRAME_DISTANCE) xfinished = false;
            }
            if (!xfinished) x += DEFAULT_FRAME_DISTANCE;
        }

        while (!yfinished)
        {
            yfinished = true;
            for (int i = 0; i < allFrames.length; i++)
            {
                if (Math.abs(allFrames[i].getY() - y) < DEFAULT_FRAME_DISTANCE) yfinished = false;
            }
            if (!yfinished) y += DEFAULT_FRAME_DISTANCE;
        }

        return new Point(x, y);
    }

    public void hideEditor(IEditor editor)
    {
        editor.getContainer().setVisible(false);
    }

    /*
     * ########################## interface methods ##########################
     */

    public void internalFrameActivated(InternalFrameEvent e)
    {
        getEditorFocus().fireViewEvent(new ViewEvent(getEditorFocus(), AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.SELECT_EDITOR));
    }

    public void internalFrameClosed(InternalFrameEvent e)
    {
    // TODO Auto-generated method stub

    }

    public void internalFrameClosing(InternalFrameEvent e)
    {
        EditorVC editor = ((DefaultEditorFrame) e.getSource()).getEditor();
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_CLOSE);
        if (action != null) action.actionPerformed(new ViewEvent(editor, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.CLOSE));

    }

    public void internalFrameDeactivated(InternalFrameEvent e)
    {
    // TODO Auto-generated method stub

    }

    public void internalFrameDeiconified(InternalFrameEvent e)
    {
    // TODO Auto-generated method stub

    }

    public void internalFrameIconified(InternalFrameEvent e)
    {
    // TODO Auto-generated method stub

    }

    public void internalFrameOpened(InternalFrameEvent e)
    {
    // TODO Auto-generated method stub

    }

    public Component getPropertyChangeSupportBean()
    {
        return desktop;
    }
}
