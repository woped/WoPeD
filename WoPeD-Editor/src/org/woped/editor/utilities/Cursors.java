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

import javax.swing.ImageIcon;

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
    private static ImageIcon placeIcon = Messages.getImageIcon("ToolBar.DrawPlace");
    private static ImageIcon andSpliIcon = Messages.getImageIcon("ToolBar.DrawAndSplit");
    private static ImageIcon  andJoinIcon = Messages.getImageIcon("ToolBar.DrawAndJoin");
    private static ImageIcon andSplitJoinIcon = Messages.getImageIcon("ToolBar.DrawAndSplitJoin");
    private static ImageIcon xorSplitIcon = Messages.getImageIcon("ToolBar.DrawXorSplit");
    private static ImageIcon xorJoinIcon = Messages.getImageIcon("ToolBar.DrawXorJoin");
    private static ImageIcon xorSplitJoinIcon = Messages.getImageIcon("ToolBar.DrawXorSplitJoin");
    private static ImageIcon transitionIcon = Messages.getImageIcon("ToolBar.DrawTransition");
    private static ImageIcon subProcessIcon = Messages.getImageIcon("ToolBar.DrawSubProcess");
    
    public static Cursor placeCursor        = Toolkit.getDefaultToolkit().createCustomCursor(placeIcon.getImage(), new Point(placeIcon.getIconWidth()/2, placeIcon.getIconHeight()/2), "Cursor");
    public static Cursor andSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(andSpliIcon.getImage(), new Point(andSpliIcon.getIconWidth()/2, andSpliIcon.getIconHeight()/2), "Cursor");
    public static Cursor andJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(andJoinIcon.getImage(), new Point(andJoinIcon.getIconWidth()/2, andJoinIcon.getIconHeight()/2), "Cursor");
    public static Cursor andSplitJoinCursor = Toolkit.getDefaultToolkit().createCustomCursor(andSplitJoinIcon.getImage(), new Point(andSplitJoinIcon.getIconWidth()/2, andSplitJoinIcon.getIconHeight()/2), "Cursor");
    public static Cursor xorSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(xorSplitIcon.getImage(), new Point(xorSplitIcon.getIconWidth()/2, xorSplitIcon.getIconHeight()/2), "Cursor");
    public static Cursor xorJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(xorJoinIcon.getImage(), new Point(xorJoinIcon.getIconWidth()/2, xorJoinIcon.getIconHeight()/2), "Cursor");
    public static Cursor xorSplitJoinCursor = Toolkit.getDefaultToolkit().createCustomCursor(xorSplitJoinIcon.getImage(), new Point(xorSplitJoinIcon.getIconWidth()/2, xorSplitJoinIcon.getIconHeight()/2), "Cursor");
    public static Cursor transitionCursor   = Toolkit.getDefaultToolkit().createCustomCursor(transitionIcon.getImage(), new Point(transitionIcon.getIconWidth()/2, transitionIcon.getIconHeight()/2), "Cursor");
    public static Cursor subProcessCursor   = Toolkit.getDefaultToolkit().createCustomCursor(subProcessIcon.getImage(), new Point(subProcessIcon.getIconWidth()/2, subProcessIcon.getIconHeight()/2), "Cursor");

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
        case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
        	return andSplitJoinCursor;
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
