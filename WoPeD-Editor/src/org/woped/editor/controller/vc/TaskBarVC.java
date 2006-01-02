package org.woped.editor.controller.vc;

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
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.gui.IEditorAware;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.EditorViewEvent;

public class TaskBarVC extends JPanel implements IViewController, IEditorAware
{

    private String             id             = null;
    private Vector             viewListener   = new Vector(1, 3);
    public static final String ID_PREFIX      = "TASKBAR_VC";

    private IEditor            selectedEditor = null;
    private HashMap            actions        = new HashMap();

    public TaskBarVC(String id)
    {
        this.id = id;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(800, 22));
        setMinimumSize(new Dimension(400, 22));
        setMaximumSize(new Dimension(800, 22));

    }

    public void selectEditor(IEditor editor)
    {
        if (actions.get(editor) != null)
        {
            selectedEditor = editor;
            for (Iterator iter = actions.values().iterator(); iter.hasNext();)
            {
                ((JToggleButton) iter.next()).setSelected(false);
            }
            ((JToggleButton) actions.get(editor)).setSelected(true);
        }
    }

    public void addEditor(IEditor editor)
    {
        JToggleButton button = new JToggleButton(ActionFactory.getSelectEditorAction(editor));//new
                                                                                              // TaskBarButton(editor);
        actions.put(editor, button);
        this.add(button);
        selectEditor(editor);
    }

    public void removeEditor(IEditor editor)
    {
        if (actions.get(editor) != null) remove((JToggleButton) actions.get(editor));
        actions.remove(editor);
        validate();
        selectedEditor = null;
        repaint();
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

    class TaskBarButton extends JToggleButton
    {
        IEditor editor;

        TaskBarButton(IEditor editor)
        {
            super(editor.toString(), DefaultStaticConfiguration.DEFAULTEDITORFRAMEICON);
            this.editor = editor;
            this.setPreferredSize(new Dimension(100, 20));
        }


    }

    public IEditor getSelectedEditor()
    {
        return selectedEditor;
    }
}
