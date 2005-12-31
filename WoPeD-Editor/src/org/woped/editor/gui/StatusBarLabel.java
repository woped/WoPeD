package org.woped.editor.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.utilities.Messages;

public class StatusBarLabel extends JPanel implements Observer
{
    private ModelElementContainer mEC;
    private JLabel                m_CounterLabel = null;
    private JLabel                m_SaveIcon = null;
    private EditorVC              editor;

    public StatusBarLabel(IEditor e)
    {
        mEC = e.getModelProcessor().getElementContainer();
        editor = (EditorVC)e;
        setLayout(new BorderLayout());
        m_CounterLabel = new JLabel();
        add(m_CounterLabel, BorderLayout.WEST);
        m_SaveIcon = new JLabel(Messages.getImageIcon("Button.Ok"));
        add(m_SaveIcon, BorderLayout.EAST);
        updateStatus();
    }

    public void updateStatus()
    {
        int placeC = mEC.getElementsByType(OperatorTransitionModel.PLACE_TYPE).size();
        int subPC = mEC.getElementsByType(OperatorTransitionModel.SUBP_TYPE).size();
        int transOpC = mEC.getElementsByType(OperatorTransitionModel.TRANS_OPERATOR_TYPE).size();
        int transSimpleC = mEC.getElementsByType(OperatorTransitionModel.TRANS_SIMPLE_TYPE).size();
        
        m_CounterLabel.setText(
                Messages.getString("Statusbar.Places") + ": " + placeC + "  " +
                Messages.getString("Statusbar.Transitions") + ": " + (transSimpleC + transOpC) + "  " +
                Messages.getString("Statusbar.Subprocesses") + ": " + subPC + "     ");
        
        if (editor.isSaved())
        {
            m_SaveIcon.setText(Messages.getString("Button.Saved.Title"));
            m_SaveIcon.setIcon(Messages.getImageIcon("Button.Saved"));
        }
        else
        {
           m_SaveIcon.setText(Messages.getString("Button.NotSaved.Title"));
           m_SaveIcon.setIcon(Messages.getImageIcon("Button.NotSaved"));
        }
    }

    public void update(Observable arg0, Object arg1)
    {
        updateStatus();
    }
}
