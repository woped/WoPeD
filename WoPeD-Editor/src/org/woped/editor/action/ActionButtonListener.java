package org.woped.editor.action;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.vep.ViewEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonListener implements ActionListener {

    private int event_id;
    private WoPeDAction action;

    public ActionButtonListener(AbstractApplicationMediator mediator, String action_id, int event_id, JComponent target) {

        action = ActionFactory.getStaticAction(action_id);
        ActionFactory.addTarget(mediator, action_id, target);
        this.event_id = event_id;
        target.setName(action_id);
    }

    public void actionPerformed(ActionEvent e) {
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, event_id));
    }
}