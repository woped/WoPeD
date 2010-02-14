package org.woped.editor.controller.vc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.gui.IEditorAware;
import org.woped.core.utilities.SwingUtils;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.ApplicationMediator;

@SuppressWarnings("serial")
public class TaskBarVC extends JPanel implements IViewController, IEditorAware
{

    private String             id             = null;
    private Vector<IViewListener> viewListener = new Vector<IViewListener>(1, 3);
    public static final String ID_PREFIX      = "TASKBAR_VC";

    private IEditor            selectedEditor = null;
    private HashMap<IEditor, JToggleButton> actions = new HashMap<IEditor, JToggleButton>();

    public TaskBarVC(String id)
    {
        this.id = id;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    public void renameEditor(IEditor editor)
    {
    	JToggleButton taskButton = (JToggleButton) actions.get(editor);
    	if (taskButton != null) {
    		taskButton.setText(editor.getName());
    	}
    }

    public void selectEditor(IEditor editor)
    {
        if (actions.get(editor) != null)
        {
            selectedEditor = editor;
            for (Iterator<JToggleButton> iter = actions.values().iterator(); iter.hasNext();)
            {
                iter.next().setSelected(false);
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
        Vector<IViewListener> vector;
        synchronized (viewListener)
        {
            vector = (Vector<IViewListener>) viewListener.clone();
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
            SwingUtils.setPreferredWidth(this, 100);
        }


    }

    public IEditor getSelectedEditor()
    {
        return selectedEditor;
    }
}
