package org.woped.editor.controller.vc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.EditorViewEvent;

public class TaskBarVC extends JPanel implements IViewController
{

    private String             id                 = null;
    private Vector             viewListener       = new Vector(1, 3);
    public static final String ID_PREFIX          = "TASKBAR_VC";

    private EditorVC           selectedEditor     = null;
    private EditorVC           lastSelectedEditor = null;
    private HashMap            actions            = new HashMap();

    public TaskBarVC(String id)
    {
        this.id = id;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(800, 25));
    }

    public void selectEditor(EditorVC editor)
    {
        if (actions.get(editor) != null)
        {
            lastSelectedEditor = selectedEditor;
            selectedEditor = editor;
            for (Iterator iter = actions.values().iterator(); iter.hasNext();)
            {
                ((TaskBarButton) iter.next()).setSelected(false);
            }
            ((TaskBarButton) actions.get(editor)).setSelected(true);
        }
    }

    public void addEditor(EditorVC editor)
    {
        TaskBarButton button = new TaskBarButton(editor);
        actions.put(editor, button);
        this.add(button);
        selectEditor(editor);
    }

    public void removeEditor(EditorVC editor)
    {
        this.remove((Component) actions.get(editor));
        actions.remove(editor);
        validate();
        selectedEditor = null;
        if (lastSelectedEditor != null && !actions.isEmpty())
        {
            selectEditor(lastSelectedEditor);
        } else
        {

        }
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
        return ApplicationMediator.VIEWCONTROLLER_TASKBAR;
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

    class TaskBarButton extends JToggleButton implements ActionListener
    {
        EditorVC editor;

        TaskBarButton(EditorVC editor)
        {
            super(editor.getFileName(), DefaultStaticConfiguration.DEFAULTEDITORFRAMEICON);
            this.editor = editor;
            this.setPreferredSize(new Dimension(100, 20));
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!editor.equals(getSelectedEditor()) || TaskBarButton.this.isSelected())
            {
                fireViewEvent(new EditorViewEvent(editor, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.SELECT_EDITOR));
            } else
            {//if (!((TaskBarButton)e.getSource()).isSelected()) {
                fireViewEvent(new EditorViewEvent(editor, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.INCONIFY_EDITOR));
            }
        }
    }

    public EditorVC getLastSelectedEditor()
    {
        return lastSelectedEditor;
    }

    public EditorVC getSelectedEditor()
    {
        return selectedEditor;
    }
}
