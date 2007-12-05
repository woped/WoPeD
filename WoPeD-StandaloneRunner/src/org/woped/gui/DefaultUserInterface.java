package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.PetriNetResourceEditor;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.gui.controller.vc.MenuBarVC;
import org.woped.gui.controller.vc.StatusBarVC;
import org.woped.gui.controller.vc.ToolBarVC;
import org.woped.qualanalysis.simulation.TokengameBarVC;
import org.woped.translations.Messages;

@SuppressWarnings("serial")
public class DefaultUserInterface extends JFrame implements IUserInterface, InternalFrameListener
{
    private JDesktopPane          desktop                = null;
    public static final int       DEFAULT_FRAME_DISTANCE = 20;
    // Used VC
    private StatusBarVC           statusBar              = null;
    private TaskBarVC             taskBar                = null;
    private ToolBarVC             toolBar                = null;
    private MenuBarVC menuBar = null;

    private int                   m_numEditors           = 0;
    private List<IEditor>         editorList             = new ArrayList<IEditor>();

    //! Stores a list of internal frames that should stay in foreground
    private List<DefaultEditorFrame>  m_modalityStack = new ArrayList<DefaultEditorFrame>();
    
    public DefaultUserInterface(ToolBarVC toolBar, MenuBarVC menuBar, TaskBarVC taskBar, StatusBarVC statusBar)
    {
        super();
        this.toolBar = toolBar;
        this.statusBar = statusBar;
        this.taskBar = taskBar;
        this.menuBar = menuBar;

        desktop = new JDesktopPane();
        desktop.setBackground(DefaultStaticConfiguration.DEFAULT_UI_BACKGROUND_COLOR);
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(desktop);
        propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        setIconImage(Messages.getImageIcon("Application").getImage());
        setTitle("WoPeD Version " + Messages.getString("Application.Version"));
        setBounds(ConfigurationManager.getConfiguration().getWindowX(), ConfigurationManager.getConfiguration().getWindowY(), (int) ConfigurationManager.getConfiguration().getWindowSize().getWidth(),
                (int) ConfigurationManager.getConfiguration().getWindowSize().getHeight());
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
        JPanel toolPanel = new JPanel();
        JPanel p1 = new JPanel();
        p1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        p1.add(taskBar, c);
 
        toolPanel.setLayout(new BorderLayout());
        toolPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        toolPanel.add(p1, BorderLayout.WEST);
        toolPanel.add(statusBar, BorderLayout.EAST);          
        toolPanel.setPreferredSize(new Dimension(100, 25));
        getContentPane().add(toolPanel, BorderLayout.SOUTH);
       
        // addKeyListener(this);
        SplashWindow splash = new SplashWindow(this);
        
        //Tokengame Shortcut here
        //TokengameBarVC RCFenster = new TokengameBarVC();
        //desktop.add(RCFenster);
    
        setVisible(true);
        LoggerManager.info(Constants.GUI_LOGGER, "END  INIT Application");
    }

    public void addEditor(IEditor editor)
    {
        if (editor != null)
        {
            DefaultEditorFrame frame;
            if (editor.getModelProcessor().getProcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
            {
                frame = new DefaultEditorFrame((EditorVC) editor, new PetriNetResourceEditor((EditorVC) editor));
            } 
            else
            {
                frame = new DefaultEditorFrame((EditorVC) editor, null);
 
            }
            
            Point position = getNextEditorPosition();
            frame.setAlignmentX((float) position.getX());
            frame.setAlignmentY((float) position.getY());
            frame.addInternalFrameListener(this);
            frame.setLocation(position);
            desktop.add(frame, BorderLayout.CENTER);
            
            if (editor.isSubprocessEditor())
            {
            	// Make subprocess editor window stay in foreground
            	m_modalityStack.add(0,frame);
            }
            
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
            } catch (Exception e)
            {
                LoggerManager.error(Constants.GUI_LOGGER, "VetoException Could not Select Frame");
            }

        }
    }

    public void quit()
    {
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_EXIT);
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.EXIT));
        setExtendedState(NORMAL);
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

    public void selectEditor(IEditor editor)
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
    
    public void renameEditor(IEditor editor)
    {
    	// Nothing to do here
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

        Vector<JInternalFrame> allOpenFrames = new Vector<JInternalFrame>();

        for (int i = 0; i < allFrames.length; i++)
        {
            if (!allFrames[i].isIcon())
            {
                allOpenFrames.add(0, allFrames[i]);
                if (allFrames[i].isMaximum()) 
                {
                    try
                    {
                        allFrames[i].setMaximum(false);
                    } catch (PropertyVetoException e)
                    {
                        //nothing to do
                    }
                }
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

        Vector<JInternalFrame> allOpenFrames = new Vector<JInternalFrame>();

        for (int i = 0; i < allFrames.length; i++)
        {
            if (!allFrames[i].isIcon())
            {
                allOpenFrames.add(0, allFrames[i]);
                if (allFrames[i].isMaximum()) 
                {
                    try
                    {
                        allFrames[i].setMaximum(false);
                    } catch (PropertyVetoException e)
                    {
                        //nothing to do
                    }
                }
            }
            
        }
        if (allOpenFrames.size()!=0)
        {
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

    public List<IEditor> getAllEditors()
    {
        return editorList;
    }
    
    

    public void updateRecentMenu()
    {
        menuBar.updateRecentMenu();
        
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

    private void FixModality()
    {
    	DefaultEditorFrame modalFrame =
    		(m_modalityStack.size()>0)?m_modalityStack.get(0):null;
    	JInternalFrame frames[] = desktop.getAllFrames();
    	for (int i = 0;i<frames.length;++i)
    	{
    		if (frames[i] instanceof DefaultEditorFrame)
    		{
    			DefaultEditorFrame current = (DefaultEditorFrame)frames[i];
    			if (current!=modalFrame)
    				current.acceptMouseEvents(modalFrame == null);
    		}
    	}
    	// Always activate the first modal frame
    	if (modalFrame!=null)    	
    	{
    		modalFrame.acceptMouseEvents(true);
    		try {
    			modalFrame.setSelected(true);
    		}
    		catch (PropertyVetoException e)
    		{}
    	}
    		
    }
    
    public void internalFrameActivated(InternalFrameEvent e)
    {
    	FixModality();
		getEditorFocus().fireViewEvent(new ViewEvent(getEditorFocus(), AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
    }

    public void internalFrameClosed(InternalFrameEvent e)
    {
    	// Remove the frame from the modality stack
    	m_modalityStack.remove(e.getInternalFrame());
    	FixModality();
    }

    public void internalFrameClosing(InternalFrameEvent e)
    {    	
        EditorVC editor = ((DefaultEditorFrame) e.getSource()).getEditor();
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_CLOSE);
        action.actionPerformed(new ViewEvent(editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE));

    }

    public void internalFrameDeactivated(InternalFrameEvent e)
    {

    }

    public void internalFrameDeiconified(InternalFrameEvent e)
    {

    }

    public void internalFrameIconified(InternalFrameEvent e)
    {

    }

    public void internalFrameOpened(InternalFrameEvent e)
    {

    }

    public Component getPropertyChangeSupportBean()
    {
        return desktop;
    }
}
