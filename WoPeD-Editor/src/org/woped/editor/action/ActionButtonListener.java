package org.woped.editor.action;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.editor.controller.ActionFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonListener implements ActionListener {

    private int event_id;
    private WoPeDAction action;
    private Object data;

    public ActionButtonListener(AbstractApplicationMediator mediator, String action_id, int event_id, JComponent target) {
        this(mediator, action_id, event_id, target, null);
    }

    public ActionButtonListener(AbstractApplicationMediator mediator, String action_id, int event_id, JComponent target, Object data){
        action = ActionFactory.getStaticAction(action_id);
        ActionFactory.addTarget(mediator, action_id, target);
        this.event_id = event_id;
        target.setName(action_id);
        this.data = data;
    }

    public void actionPerformed(ActionEvent e) {
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, event_id, data));
    }
}