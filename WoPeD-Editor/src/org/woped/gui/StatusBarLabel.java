package org.woped.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import org.woped.controller.IEditor;
import org.woped.model.ModelElementContainer;
import org.woped.model.petrinet.OperatorTransitionModel;

public class StatusBarLabel extends JLabel implements Observer
{
    private ModelElementContainer mEC;

    public StatusBarLabel(IEditor e)
    {
        //e.getModelProcessor().getElementContainer().addObserver(this);
        mEC = e.getModelProcessor().getElementContainer();
        setStatsLabel();
    }

    public void setStatsLabel()
    {

        int place = mEC.getElementsByType(OperatorTransitionModel.PLACE_TYPE).size();
        int subP = mEC.getElementsByType(OperatorTransitionModel.SUBP_TYPE).size();
        int transOp = mEC.getElementsByType(OperatorTransitionModel.TRANS_OPERATOR_TYPE).size();
        int transSimple = mEC.getElementsByType(OperatorTransitionModel.TRANS_SIMPLE_TYPE).size();
        int trigger = mEC.getElementsByType(OperatorTransitionModel.TRIGGER_TYPE).size();
        setText("Places: " + place + "  Transitions: " + (transSimple + transOp) + "  (Operators: " + transOp + ")");
    }

    public void update(Observable arg0, Object arg1)
    {
        //mEC = (ModelElementContainer) arg0;
        setStatsLabel();
    }

}
