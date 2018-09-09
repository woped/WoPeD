package org.woped.starter;



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.woped.bpel.gui.EditorData;
import org.woped.bpel.gui.EditorOperations;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.ViewEvent;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.PetriNetResourceEditor;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.SubprocessEditorVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.gui.images.svg.woped;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.simulation.ReferenceProvider;
import org.woped.starter.controller.vc.StatusBarVC;
import org.woped.starter.osxMenu.OSXFullscreen;

@SuppressWarnings("serial")
public class DefaultUserInterface extends MainFrame implements IUserInterface, InternalFrameListener {
    public static final int DEFAULT_FRAME_DISTANCE = 20;
    private JDesktopPane desktop = null;
    // Used VC
    private StatusBarVC statusBar = null;
    private DefaultEditorFrame frame = null;

    private List<IEditor> editorList = new ArrayList<IEditor>();

    //! Stores a list of internal frames that should stay in foreground
    private List<DefaultEditorFrame> m_modalityStack = new ArrayList<DefaultEditorFrame>();

    public DefaultUserInterface(TaskBarVC taskBar, StatusBarVC statusBar) {
        super();
        if ( Platform.isMac() ) {
            final Window currentWindow = (Window) SwingUtilities.getRoot(this);
            OSXFullscreen.enableOSXFullscreen(currentWindow);
        }
        // Adaption of constructor signature
        this.statusBar = statusBar;

        desktop = new JDesktopPane();
        desktop.setBackground(DefaultStaticConfiguration.DEFAULT_UI_BACKGROUND_COLOR);
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(desktop);
        propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        setTitle("WoPeD " + Messages.getString("Application.Version"));
        setApplicationIcon(new woped());
        setBounds(ConfigurationManager.getConfiguration().getWindowX(), ConfigurationManager.getConfiguration().getWindowY(), (int) ConfigurationManager.getConfiguration().getWindowSize().getWidth(), (int) ConfigurationManager.getConfiguration().getWindowSize().getHeight());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Maximize JFrame?
        if ( ConfigurationManager.getConfiguration().isMaximizeWindow() ) setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        if ( ConfigurationManager.getConfiguration().getHomedir() == null ) ConfigurationManager.getConfiguration().setHomedir("nets/");

        getContentPane().add(desktop, BorderLayout.CENTER);

        // Prepare Statusbar & Taskbar
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

        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "Registration", null, null));

        if ( !ConfigurationManager.getConfiguration().isRegistered() && ConfigurationManager.getConfiguration().isShowOnStartup() && (ConfigurationManager.getConfiguration().getLaunchCounter() < 10 | (ConfigurationManager.getConfiguration().getLaunchCounter() % 5) == 3) ) {
            new RegistrationUI(this, true);
        } else {
            new SplashWindow(this);
        }

        //Helper for adding Tokengame
        //see Java-Doc for explanation
        ReferenceProvider helper = new ReferenceProvider();
        helper.setDesktopReference(desktop);
        helper.setUIReference(this);

        setVisible(true);
        LoggerManager.info(Constants.GUI_LOGGER, "END  INIT Application");
    }

    public void addEditor(IEditor editor) {
        if ( editor != null ) {
        	 frame = new DefaultEditorFrame((EditorVC) editor, new EditorOperations(editor), new EditorData(), new PetriNetResourceEditor((EditorVC) editor));

             Point position = getNextEditorPosition();
             frame.setAlignmentX((float) position.getX());
             frame.setAlignmentY((float) position.getY());
             frame.addInternalFrameListener(this);
             frame.setLocation(position);
             desktop.add(frame, BorderLayout.CENTER);

             if ( editor instanceof SubprocessEditorVC ) {
                 // Make subprocess editor window stay in foreground
                 m_modalityStack.add(0, frame);
             }

             editorList.add(frame.getEditor());
             ((EditorVC) frame.getEditor()).getEditorPanel().setContainer(frame);
             frame.pack();
             frame.setVisible(true);

             // Notify MainFrame
             super.addEditor(frame.getEditor());

             try {
                  frame.setSelected(true);
             } catch (Exception e) {
                 LoggerManager.error(Constants.GUI_LOGGER, "VetoException Could not Select Frame");
             }
        }
    }

    public void quit() {
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_EXIT);
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.EXIT));
        setExtendedState(NORMAL);
    }

    public Component getComponent() {
        return this;
    }

    public void removeEditor(IEditor editor) {
        ((DefaultEditorFrame) ((EditorVC) editor).getEditorPanel().getContainer()).dispose();
        editorList.remove(editor);
        m_modalityStack.remove(((EditorVC) editor).getEditorPanel().getContainer());
        // try to Select a different Frame
        if ( desktop.getAllFrames().length > 0 ) {
            if ( desktop.getAllFrames()[0] != null ) {
                try {
                    desktop.getAllFrames()[0].setSelected(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                } catch (NullPointerException npe) {
                    LoggerManager.debug(Constants.GUI_LOGGER, "Closed - NullPointerException");
                }
            }
        }

        // Notify MainFrame
        super.removeEditor(editor);
    }

    public void selectEditor(IEditor editor) {
        try {
            JInternalFrame iframe = (JInternalFrame) ((EditorVC) editor).getEditorPanel().getContainer();
            if ( iframe.isIcon() ) {
                iframe.setIcon(false);
            }
            iframe.setSelected(true);

            // Notify MainFrame
            super.selectEditor(editor);
        } catch (PropertyVetoException e) {
            LoggerManager.debug(Constants.GUI_LOGGER, "Could not select Frame");
        }
    }

    public void renameEditor(IEditor editor) {
        // Nothing to do here
    }

    //! Return the last editor window that had the focus
    //! @return last editor window that had the focus for null if no editor window open
    public IEditor getEditorFocus() {
        JInternalFrame[] frames = desktop.getAllFrames();
        IEditor result = null;
        for ( int i = 0; (result == null) && (i < frames.length); ++i )
            if ( frames[i] instanceof DefaultEditorFrame ) {
                result = ((DefaultEditorFrame) frames[i]).getEditor();
            }
        return result;
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void cascadeFrames() {
        JInternalFrame[] allFrames = desktop.getAllFrames();

        Vector<JInternalFrame> allOpenFrames = new Vector<JInternalFrame>();

        for ( int i = 0; i < allFrames.length; i++ ) {
            if ( !allFrames[i].isIcon() ) {
                allOpenFrames.add(0, allFrames[i]);
                if ( allFrames[i].isMaximum() ) {
                    try {
                        allFrames[i].setMaximum(false);
                    } catch (PropertyVetoException e) {
                        //nothing to do
                    }
                }
            }
        }
        for ( int i = 0; i < allOpenFrames.size(); i++ ) {
            Rectangle bounds = allOpenFrames.get(i).getBounds();

            allOpenFrames.get(i).setBounds(i * DEFAULT_FRAME_DISTANCE, i * DEFAULT_FRAME_DISTANCE, (int) bounds.getWidth(), (int) bounds.getHeight());
            try {
                allOpenFrames.get(i).setSelected(true);
            } catch (PropertyVetoException e) {
            }
        }
    }


    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void arrangeFrames() {
        JInternalFrame[] allFrames = desktop.getAllFrames();

        Vector<JInternalFrame> allOpenFrames = new Vector<JInternalFrame>();

        for ( int i = 0; i < allFrames.length; i++ ) {
            if ( !allFrames[i].isIcon() ) {
                allOpenFrames.add(0, allFrames[i]);
                if ( allFrames[i].isMaximum() ) {
                    try {
                        allFrames[i].setMaximum(false);
                    } catch (PropertyVetoException e) {
                        //nothing to do
                    }
                }
            }

        }
        if ( allOpenFrames.size() != 0 ) {
            int xCount = (int) Math.ceil(Math.sqrt(allOpenFrames.size()));
            int yCount = (int) Math.ceil((double) allOpenFrames.size() / xCount);

            int frameWidth = desktop.getWidth() / xCount;
            int frameHeight = desktop.getHeight() / yCount;
            int lastRowWidth = desktop.getWidth() / (xCount - (xCount * yCount - allOpenFrames.size()));

            LoggerManager.debug(Constants.GUI_LOGGER, "Frames" + allOpenFrames.size() + ":" + xCount + "x" + yCount);

            for ( int i = 0; i < allOpenFrames.size(); i++ ) {
                JInternalFrame currentFrame = allOpenFrames.get(i);
                int row = i / xCount;
                int col = i % xCount;

                if ( (row + 1) == yCount ) {
                    // LastRow
                    currentFrame.setBounds(new Rectangle(col * lastRowWidth, row * frameHeight, lastRowWidth, frameHeight));
                } else {
                    currentFrame.setBounds(new Rectangle(col * frameWidth, row * frameHeight, frameWidth, frameHeight));
                }

            }
        }
    }

    public void refreshFocusOnFrames() {
        JInternalFrame[] allFrames = desktop.getAllFrames();
        boolean foundEditor = false;
        for ( int i = 0; i < allFrames.length && !foundEditor; i++ ) {
            if ( allFrames[i] instanceof org.woped.starter.DefaultEditorFrame ) {
                try {
                    allFrames[i].setSelected(true);
                    foundEditor = true;
                } catch (PropertyVetoException e) {
                    // This exception isn't critical, so no need for handling this exception. Just a focus thing. ;)
                }
            }
        }
    }

    /* ########################## GETTER & SETTER ######################### */

    public StatusBarVC getStatusBar() {
        return statusBar;
    }

    public List<IEditor> getAllEditors() {
        return editorList;
    }


    /**
     * This method provides the possibility to have a "started" TokenGame and the "ProcessTab" is viewed
     * when simulation is running
     */
    public void setFirstTransitionActive() {
        // activate the "ProcessTab" to view the TokenGame
        JInternalFrame frames[] = desktop.getAllFrames();
        for ( int i = 0; i < frames.length; ++i ) {
            if ( frames[i] instanceof DefaultEditorFrame ) {
                DefaultEditorFrame current = (DefaultEditorFrame) frames[i];
                if ( current == desktop.getSelectedFrame() ) {
                    if ( current.getProcessTab() != null ) {
                        current.getProcessTab().setSelectedIndex(0);
                    }
                }
            }
        }
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @return
     */
    public Point getNextEditorPosition() {
        JInternalFrame[] allFrames = desktop.getAllFrames();
        int x = 0;
        int y = 0;
        boolean xfinished = false;
        boolean yfinished = false;
        while ( !xfinished ) {
            xfinished = true;
            for ( int i = 0; i < allFrames.length; i++ ) {
                if ( Math.abs(allFrames[i].getX() - x) < DEFAULT_FRAME_DISTANCE ) xfinished = false;
            }
            if ( !xfinished ) x += DEFAULT_FRAME_DISTANCE;
        }

        while ( !yfinished ) {
            yfinished = true;
            for ( int i = 0; i < allFrames.length; i++ ) {
                if ( Math.abs(allFrames[i].getY() - y) < DEFAULT_FRAME_DISTANCE ) yfinished = false;
            }
            if ( !yfinished ) y += DEFAULT_FRAME_DISTANCE;
        }

        return new Point(x, y);
    }

    public void hideEditor(IEditor editor) {
        ((EditorVC) editor).getEditorPanel().getContainer().setVisible(false);
    }

    /*
     * ########################## interface methods ##########################
     */

    /**
     * Fix modality in case a frame gets activated due to an external event. This will make sure that if there is at least one modal frame,
     * this frame will remain in focus with the other ones staying disabled
     *
     * @return true if modality had to be fixed because there is a modal frame and it was not the one currently due to
     * an external event, false otherwise
     */
    private boolean FixModality() {
        IEditor activatedEditor = getEditorFocus();
        DefaultEditorFrame modalFrame = (m_modalityStack.size() > 0) ? m_modalityStack.get(0) : null;
        JInternalFrame frames[] = desktop.getAllFrames();
        for ( int i = 0; i < frames.length; ++i ) {
            if ( frames[i] instanceof DefaultEditorFrame ) {
                DefaultEditorFrame current = (DefaultEditorFrame) frames[i];
                if ( current != modalFrame ) current.acceptMouseEvents(modalFrame == null);
            }
        }
        // Always activate the first modal frame
        if ( modalFrame != null ) {
            modalFrame.acceptMouseEvents(true);
            try {
                modalFrame.setSelected(true);
            } catch (PropertyVetoException e) {
            }
        }
        return ((modalFrame != null) && (activatedEditor != modalFrame.getEditor()));
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        // First check whether we have any active modal dialogs.
        // If so, there is no point and even some danger in reacting on the activated message
        // because we will change back to the original frame in an instant
        boolean modalityFixed = FixModality();
        if ( !modalityFixed )
            getEditorFocus().fireViewEvent(new ViewEvent(getEditorFocus(), AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
    }

    public void internalFrameClosed(InternalFrameEvent e) {
        // Remove the frame from the modality stack
        m_modalityStack.remove(e.getInternalFrame());
        FixModality();
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        IEditor editor = ((DefaultEditorFrame) e.getSource()).getEditor();
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_CLOSE);
        action.actionPerformed(new ViewEvent(editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE));

    }

    public void internalFrameDeactivated(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameOpened(InternalFrameEvent e) {

    }

    public Component getPropertyChangeSupportBean() {
        return desktop;
    }

    public boolean isMaximized() {
        return this.getExtendedState() == MAXIMIZED_BOTH;
    }

    public DefaultEditorFrame getFrame() {
        // TODO Auto-generated method stub
        return frame;
    }

}