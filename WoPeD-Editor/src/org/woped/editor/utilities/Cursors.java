/*
 * Created on 20.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.editor.utilities;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Cursors
{
    public static Cursor placeCursor        = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawPlace").getImage(), new Point(), "Cursor");
    public static Cursor andSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawAndSplit").getImage(), new Point(), "Cursor");
    public static Cursor andJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawAndJoin").getImage(), new Point(), "Cursor");
    public static Cursor xorSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawXorSplit").getImage(), new Point(), "Cursor");
    public static Cursor xorJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawXorJoin").getImage(), new Point(), "Cursor");
    public static Cursor xorSplitJoinCursor = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawXorSplitJoin").getImage(), new Point(), "Cursor");
    public static Cursor transitionCursor   = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawTransition").getImage(), new Point(), "Cursor");
    public static Cursor subProcessCursor   = Toolkit.getDefaultToolkit().createCustomCursor(Messages.getImageIcon("ToolBar.DrawSubProcess").getImage(), new Point(), "Cursor");

    public static Cursor getElementCreationCursor(int petrinetType)
    {
        switch (petrinetType)
        {
        case PetriNetModelElement.PLACE_TYPE:
            return placeCursor;
        case OperatorTransitionModel.AND_SPLIT_TYPE:
            return andSplitCursor;
        case OperatorTransitionModel.AND_JOIN_TYPE:
            return andJoinCursor;
        case OperatorTransitionModel.XOR_SPLIT_TYPE:
            return xorSplitCursor;
        case OperatorTransitionModel.XOR_JOIN_TYPE:
            return xorJoinCursor;
        case OperatorTransitionModel.XOR_SPLITJOIN_TYPE:
            return xorSplitJoinCursor;
        case PetriNetModelElement.TRANS_SIMPLE_TYPE:
            return transitionCursor;
        case OperatorTransitionModel.SUBP_TYPE:
            return subProcessCursor;
        default:
            return Cursor.getPredefinedCursor(Cursor.CUSTOM_CURSOR);
        }
    }

}
