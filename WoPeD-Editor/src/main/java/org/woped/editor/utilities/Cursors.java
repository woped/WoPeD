/*
 * Created on 20.10.2005
 */
package org.woped.editor.utilities;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.gui.translations.Messages;

/**
 * @author lai
 */
public class Cursors
{
    private static ImageIcon placeIcon = Messages.getCursorImageIcon("ToolBar.DrawPlace");
    public static Cursor placeCursor        = Toolkit.getDefaultToolkit().createCustomCursor(placeIcon.getImage(), new Point(placeIcon.getIconWidth()/2, placeIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon andSpliIcon = Messages.getCursorImageIcon("ToolBar.DrawAndSplit");
    public static Cursor andSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(andSpliIcon.getImage(), new Point(andSpliIcon.getIconWidth()/2, andSpliIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon andJoinIcon = Messages.getCursorImageIcon("ToolBar.DrawAndJoin");
    public static Cursor andJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(andJoinIcon.getImage(), new Point(andJoinIcon.getIconWidth()/2, andJoinIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon andSplitJoinIcon = Messages.getCursorImageIcon("ToolBar.DrawAndSplitJoin");
    public static Cursor andSplitJoinCursor = Toolkit.getDefaultToolkit().createCustomCursor(andSplitJoinIcon.getImage(), new Point(andSplitJoinIcon.getIconWidth()/2, andSplitJoinIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon xorSplitIcon = Messages.getCursorImageIcon("ToolBar.DrawXorSplit");
    public static Cursor xorSplitCursor     = Toolkit.getDefaultToolkit().createCustomCursor(xorSplitIcon.getImage(), new Point(xorSplitIcon.getIconWidth()/2, xorSplitIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon xorJoinIcon = Messages.getCursorImageIcon("ToolBar.DrawXorJoin");
    public static Cursor xorJoinCursor      = Toolkit.getDefaultToolkit().createCustomCursor(xorJoinIcon.getImage(), new Point(xorJoinIcon.getIconWidth()/2, xorJoinIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon xorSplitJoinIcon = Messages.getCursorImageIcon("ToolBar.DrawXorSplitJoin");
    public static Cursor xorSplitJoinCursor = Toolkit.getDefaultToolkit().createCustomCursor(xorSplitJoinIcon.getImage(), new Point(xorSplitJoinIcon.getIconWidth()/2, xorSplitJoinIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon transitionIcon = Messages.getCursorImageIcon("ToolBar.DrawTransition");
    public static Cursor transitionCursor   = Toolkit.getDefaultToolkit().createCustomCursor(transitionIcon.getImage(), new Point(transitionIcon.getIconWidth()/2, transitionIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon subProcessIcon = Messages.getCursorImageIcon("ToolBar.DrawSubProcess");
    public static Cursor subProcessCursor   = Toolkit.getDefaultToolkit().createCustomCursor(subProcessIcon.getImage(), new Point(subProcessIcon.getIconWidth()/2, subProcessIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon andJoinXorSplitIcon = Messages.getCursorImageIcon("ToolBar.DrawAndJoinXorSplit");
    public static Cursor andJoinXorSplitCursor = Toolkit.getDefaultToolkit().createCustomCursor(andJoinXorSplitIcon.getImage(), new Point(andJoinXorSplitIcon.getIconWidth()/2, andJoinXorSplitIcon.getIconHeight()/2), "Cursor");
    private static ImageIcon xorJoinAndSplitIcon = Messages.getCursorImageIcon("ToolBar.DrawXorJoinAndSplit");
    public static Cursor xorJoinAndSplitCursor = Toolkit.getDefaultToolkit().createCustomCursor(xorJoinAndSplitIcon.getImage(), new Point(xorJoinAndSplitIcon.getIconWidth()/2, xorJoinAndSplitIcon.getIconHeight()/2), "Cursor");
    

    public static Cursor getElementCreationCursor(int petrinetType)
    {
        switch (petrinetType)
        {
        case AbstractPetriNetElementModel.PLACE_TYPE:
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
            case OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE:
            return xorSplitJoinCursor;
        case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
        	return andJoinXorSplitCursor;
        case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
        	return xorJoinAndSplitCursor;        	
        case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
            return transitionCursor;
        case OperatorTransitionModel.SUBP_TYPE:
            return subProcessCursor;
        default:
            return Cursor.getPredefinedCursor(Cursor.CUSTOM_CURSOR);
        }
    }

}
