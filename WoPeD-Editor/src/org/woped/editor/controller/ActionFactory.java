package org.woped.editor.controller;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JInternalFrame;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.editor.action.WoPeDAction;

public class ActionFactory
{
    // Application Scope
    public final static String         ACTIONID_NEW                    = "Action.NewEditor";
    public final static String         ACTIONID_CLOSE                  = "Action.CloseEditor";
    public final static String         ACTIONID_SELECT                 = "Action.FocusEditor";
    public final static String         ACTIONID_SAVE                   = "Action.SaveEditor";
    public final static String         ACTIONID_SAVEAS                 = "ToolBar.SaveAs";
    public final static String         ACTIONID_EXPORT                 = "Action.Export";
    public final static String         ACTIONID_OPEN                   = "ToolBar.Open";
    public final static String         ACTIONID_OPENSAMPLE             = "Action.OpenSample";
    public final static String         ACTIONID_SHOWCONFIG             = "Action.ShowConfig";
    public final static String         ACTIONID_SHOWABOUT              = "Action.ShowAbout";
    public final static String         ACTIONID_SHOWBUGREPORT          = "Action.ShowBugReport";
    public final static String         ACTIONID_SHOWHELPINDEX          = "Menu.Help.Index";
    public final static String         ACTIONID_SHOWHELPCONTENTS       = "Menu.Help.Contents";
    public final static String         ACTIONID_EXIT                   = "Action.QuitApplication";
    public final static String         ACTIONID_WOFLAN                 = "ToolBar.Woflan";
    public final static String		   ACTIONID_WOPED				   = "ToolBar.Woped";
    public final static String         ACTIONID_TOGGLE_TOKENGAME       = "ToolBar.TokenGame";
    public final static String         ACTIONID_PRINT                  = "Action.PrintEditor";
    // utils
    public final static String         ACTIONID_UPDATENETS             = "Action.UpdateNets";
    //public final static String ACTIONID_SCREENSHOT =
    // "Menu.Edit.MkScreenshot";
    // Drawmode
    public final static String         ACTIONID_DRAWMODE_PLACE         = "ToolBar.DrawPlace";
    public final static String         ACTIONID_DRAWMODE_TRANSITION    = "ToolBar.DrawTransition";
    public final static String         ACTIONID_DRAWMODE_ANDSPLIT      = "ToolBar.DrawAndSplit";
    public final static String         ACTIONID_DRAWMODE_ANDJOIN       = "ToolBar.DrawAndJoin";
    public final static String		   ACTIONID_DRAWMODE_ANDSPLITJOIN  = "ToolBar.DrawAndSplitJoin";
    public final static String         ACTIONID_DRAWMODE_XORSPLIT      = "ToolBar.DrawXorSplit";
    public final static String         ACTIONID_DRAWMODE_XORJOIN       = "ToolBar.DrawXorJoin";
    public final static String         ACTIONID_DRAWMODE_XORSPLITJOIN  = "ToolBar.DrawXorSplitJoin";
    public final static String         ACTIONID_DRAWMODE_SUB           = "ToolBar.DrawSubProcess";
    // Edit
    public final static String         ACTIONID_REDO                   = "Action.Redo";
    public final static String         ACTIONID_UNDO                   = "Action.Undo";
    public final static String         ACTIONID_UNGROUP                = "Action.UngroupSelection";
    public final static String         ACTIONID_GROUP                  = "Action.GroupSelection";
    public final static String         ACTIONID_EDIT                   = "Action.EditSelection";
    public final static String         ACTIONID_CUT                    = "Action.CutSelection";
    public final static String         ACTIONID_COPY                   = "Action.CopySelection";
    public final static String         ACTIONID_PASTE                  = "Action.PasteElements";

    public final static String         ACTIONID_REMOVE                 = "Popup.Remove";
    public final static String         ACTIONID_RENAME                 = "Popup.Rename";
    public final static String         ACTIONID_ACTIVATE_ROUTING       = "Popup.ActivateRouting";
    public final static String         ACTIONID_DEACTIVATE_ROUTING     = "Popup.DeactivateRouting";
    public final static String         ACTIONID_ACTIVATE_ALL_ROUTING   = "Popup.Route";
    public final static String         ACTIONID_DEACTIVATE_ALL_ROUTING = "Popup.Unroute";
    public final static String         ACTIONID_ADD_POINT              = "Popup.AddPoint";
    public final static String         ACTIONID_REMOVE_POINT           = "Popup.RemovePoint";
    public final static String         ACTIONID_OPEN_PROPERTIES        = "Popup.Properties";
    public final static String         ACTIONID_ADD_RES_TRIGGER        = "Popup.Trigger.AddResource";
    public final static String         ACTIONID_ADD_TIME_TRIGGER       = "Popup.Trigger.AddTime";
    public final static String         ACTIONID_ADD_EXT_TRIGGER        = "Popup.Trigger.AddExternal";
    public final static String         ACTIONID_REMOVE_TIRGGER         = "Popup.Trigger.Remove";
    public final static String         ACTIONID_ADD_PLACE              = "Popup.Add.Place";
    public final static String         ACTIONID_ADD_TRANSITION         = "Popup.Add.Transition";
    public final static String         ACTIONID_ADD_TOKEN              = "Popup.Token.Add";
    public final static String         ACTIONID_REMOVE_TOKEN           = "Popup.Token.Remove";
    public final static String         ACTIONID_OPEN_SUBPROCESS        = "Popup.Subprocess";
    public final static String         ACTIONID_ADD_ANDJOIN            = "Popup.Add.AndJoin";
    public final static String         ACTIONID_ADD_ANDSPLIT           = "Popup.Add.AndSplit";
    public final static String		   ACTIONID_ADD_ANDSPLITJOIN       = "Popup.Add.AndSplitJoin";
    public final static String         ACTIONID_ADD_XORSPLITJOIN       = "Popup.Add.XorSplitJoin";
    // public final static String ACTIONID_ADD_ORSPLIT = "Popup.Add.OrSplit";
    public final static String         ACTIONID_ADD_XORJOIN            = "Popup.Add.XorJoin";
    public final static String         ACTIONID_ADD_XORSPLIT           = "Popup.Add.XorSplit";
    public final static String         ACTIONID_ADD_SUBPROCESS         = "Popup.Add.Subprocess";
    // UML 2.0
    public final static String         ACTIONID_ADD_ACTIVITY           = "Action.Add.Activity";
    public final static String         ACTIONID_ADD_START              = "Action.Add.Start";
    public final static String         ACTIONID_ADD_STOP               = "Action.Add.Stop";
    public final static String         ACTIONID_ADD_AND                = "Action.Add.And";
    public final static String         ACTIONID_ADD_XOR                = "Action.Add.Xor";

    // view
    public final static String         ACTIONID_STRETCH                = "Menu.View.Stretch";
    public final static String         ACTIONID_PRESS                  = "Menu.View.Press";
    public final static String         ACTIONID_ZOOMIN                 = "Action.ZoomIn";
    public final static String         ACTIONID_ZOOMOUT                = "Action.ZoomOut";

    // window
    public final static String         ACTIONID_CASCADE                = "Action.Frames.Cascade";
    public final static String         ACTIONID_ARRANGE                = "Menu.Window.Arrange";
    public final static String		   ACTIONID_SHOWSIDEBAR			   = "Menu.Window.ShowSideBar";

    private static HashMap             STATIC_ACTION_MAP               = null;

    private static HashMap             SELECT_EDITOR_SELECT_ACTION     = new HashMap();

    private static ApplicationMediator AM                              = null;

    public static HashMap createStaticActions(ApplicationMediator am)
    {
        AM = am;
        STATIC_ACTION_MAP = new HashMap();

        STATIC_ACTION_MAP.put(ACTIONID_NEW, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.NEW, null, ACTIONID_NEW));
        // 
        STATIC_ACTION_MAP.put(ACTIONID_CLOSE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE, null, ACTIONID_CLOSE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CLOSE), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_SELECT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
        // 
        STATIC_ACTION_MAP.put(ACTIONID_SAVE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.SAVE, null, ACTIONID_SAVE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SAVE), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_SAVEAS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.SAVEAS, null, ACTIONID_SAVEAS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SAVEAS), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_OPEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, null, ACTIONID_OPEN));
        // 
        STATIC_ACTION_MAP.put(ACTIONID_EXPORT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.EXPORT, null, ACTIONID_EXPORT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_EXPORT), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_SHOWCONFIG, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.CONFIG, null, ACTIONID_SHOWCONFIG));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SHOWCONFIG), VisualController.IGNORE, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_SHOWABOUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.ABOUT, null, ACTIONID_SHOWABOUT));
        //
        STATIC_ACTION_MAP.put(ACTIONID_SHOWBUGREPORT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.BUGREPORT, null, ACTIONID_SHOWBUGREPORT));
        //
        STATIC_ACTION_MAP.put(ACTIONID_SHOWHELPINDEX, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.HELP, null, ACTIONID_SHOWHELPINDEX));
        //
        STATIC_ACTION_MAP.put(ACTIONID_SHOWHELPCONTENTS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.HELP_CONTENTS, null, ACTIONID_SHOWHELPCONTENTS));
        //
        STATIC_ACTION_MAP.put(ACTIONID_EXIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.EXIT, null, ACTIONID_EXIT));
        //
        STATIC_ACTION_MAP.put(ACTIONID_PRINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.PRINT, null, ACTIONID_PRINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PRINT), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        /* Utils */
        STATIC_ACTION_MAP.put(ACTIONID_UPDATENETS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.UPDATE));
        //
        //    STATIC_ACTION_MAP.put(ACTIONID_SCREENSHOT, new WoPeDAction(am,
        // AbstractViewEvent.VIEWEVENTTYPE_APPLICATION,
        // AbstractViewEvent.SCREENSHOT, null, ACTIONID_SCREENSHOT));
        //
        STATIC_ACTION_MAP.put(ACTIONID_WOFLAN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_WOFLAN, null, ACTIONID_WOFLAN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_WOFLAN), VisualController.WOFLAN, VisualController.IGNORE, VisualController.IGNORE);
        // Add local analysis using WoPeD internal dialog
        STATIC_ACTION_MAP.put(ACTIONID_WOPED, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_WOPED, null, ACTIONID_WOPED));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_WOPED), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        /* Drawmode Actions */
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_PLACE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_PLACE, null, ACTIONID_DRAWMODE_PLACE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_PLACE), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_PLACE);
        STATIC_ACTION_MAP
                .put(ACTIONID_DRAWMODE_TRANSITION, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_TRANSITION, null, ACTIONID_DRAWMODE_TRANSITION));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_TRANSITION), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_TRANSITION);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDSPLIT, null, ACTIONID_DRAWMODE_ANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDSPLIT), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_AND_SPLIT);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDJOIN, null, ACTIONID_DRAWMODE_ANDJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDJOIN), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_AND_JOIN);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDSPLITJOIN, null, ACTIONID_DRAWMODE_ANDSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDSPLITJOIN), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_AND_SPLITJOIN);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORSPLIT, null, ACTIONID_DRAWMODE_XORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORSPLIT), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_XOR_SPLIT);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORJOIN, null, ACTIONID_DRAWMODE_XORJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORJOIN), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_XOR_JOIN);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORSPLITJOIN, null,
                ACTIONID_DRAWMODE_XORSPLITJOIN));
        VisualController.getInstance()
                .addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORSPLITJOIN), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_XOR_SPLITJOIN);
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_SUB, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_SUB, null, ACTIONID_DRAWMODE_SUB));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_SUB), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.DRAWMODE_SUBPROCESS);

        /* VIEW */
        STATIC_ACTION_MAP.put(ACTIONID_STRETCH, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.STRETCH, null, ACTIONID_STRETCH));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_STRETCH), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_PRESS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.PRESS, null, ACTIONID_PRESS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PRESS), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ZOOMIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ZOOM_IN, null, ACTIONID_ZOOMIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ZOOMIN), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ZOOMOUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ZOOM_OUT, null, ACTIONID_ZOOMOUT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ZOOMOUT), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_TOGGLE_TOKENGAME, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOGGLE_TOKENGAME, null, ACTIONID_TOGGLE_TOKENGAME));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOGGLE_TOKENGAME), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.TOKENGAME);
        /* EDIT */
        STATIC_ACTION_MAP.put(ACTIONID_REDO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REDO, null, ACTIONID_REDO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REDO), VisualController.CAN_REDO, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_UNDO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.UNDO, null, ACTIONID_UNDO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_UNDO), VisualController.CAN_UNDO, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_UNGROUP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.UNGROUP, null, ACTIONID_UNGROUP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_UNGROUP), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_GROUP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.GROUP, null, ACTIONID_GROUP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_GROUP), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_EDIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.EDIT, null, ACTIONID_EDIT));
        //
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE, null, ACTIONID_REMOVE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_RENAME, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.RENAME, null, ACTIONID_RENAME));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_RENAME), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ACTIVATE_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ACTIVE, null, ACTIONID_ACTIVATE_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ACTIVATE_ROUTING), VisualController.ARC_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_DEACTIVATE_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_DEACTIVE, null, ACTIONID_DEACTIVATE_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DEACTIVATE_ROUTING), VisualController.ARC_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ACTIVATE_ALL_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ALL_ACTIVE, null, ACTIONID_ACTIVATE_ALL_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ACTIVATE_ALL_ROUTING), VisualController.IGNORE/*zurück*/, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP
                .put(ACTIONID_DEACTIVATE_ALL_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ALL_DEACTIVE, null, ACTIONID_DEACTIVATE_ALL_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DEACTIVATE_ALL_ROUTING), VisualController.ALWAYS, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_POINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_POINT, null, ACTIONID_ADD_POINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_POINT), VisualController.ARC_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_POINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_POINT, null, ACTIONID_REMOVE_POINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_POINT), VisualController.ARC_POINT, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_OPEN_PROPERTIES, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.OPEN_PROPERTIES, null, ACTIONID_OPEN_PROPERTIES));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_PROPERTIES), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_EXT_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_EXT_TRIGGER, null, ACTIONID_ADD_EXT_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_EXT_TRIGGER), VisualController.TRANSITION_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_RES_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_RES_TRIGGER, null, ACTIONID_ADD_RES_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_RES_TRIGGER), VisualController.TRANSITION_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_TIME_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TIME_TRIGGER, null, ACTIONID_ADD_TIME_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TIME_TRIGGER), VisualController.TRANSITION_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_TIRGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_TRIGGER, null, ACTIONID_REMOVE_TIRGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_TIRGGER), VisualController.TRIGGERED_TRANSITION_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_PLACE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_PLACE, null, ACTIONID_ADD_PLACE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_PLACE), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_TRANSITION, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TRANSITION, null, ACTIONID_ADD_TRANSITION));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TRANSITION), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDJOIN, null, ACTIONID_ADD_ANDJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDJOIN), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDSPLIT, null, ACTIONID_ADD_ANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDSPLITJOIN), VisualController.NO_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDSPLITJOIN, null, ACTIONID_ADD_ANDSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDSPLIT), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORJOIN, null, ACTIONID_ADD_XORJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORJOIN), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORSPLIT, null, ACTIONID_ADD_XORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORSPLIT), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORSPLITJOIN, null, ACTIONID_ADD_XORSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORSPLITJOIN), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ADD_SUBPROCESS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_SUBPROCESS, null, ACTIONID_ADD_SUBPROCESS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_SUBPROCESS), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_OPEN_SUBPROCESS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.OPEN_SUBPROCESS, null, ACTIONID_OPEN_SUBPROCESS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_SUBPROCESS), VisualController.TRANSITION_SELECTION, VisualController.IGNORE,
                VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ADD_TOKEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TOKEN, null, ACTIONID_ADD_TOKEN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TOKEN), VisualController.PLACE_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_TOKEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_TOKEN, null, ACTIONID_REMOVE_TOKEN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_TOKEN), VisualController.TOKEN_PLACE_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        // UML ELements
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ACTIVITY, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ACTIVITY, null, ACTIONID_ADD_ACTIVITY));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ACTIVITY), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_START, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_START, null, ACTIONID_ADD_START));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_START), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_STOP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_STOP, null, ACTIONID_ADD_STOP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_STOP), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_AND, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_AND, null, ACTIONID_ADD_AND));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_AND), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XOR, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XOR, null, ACTIONID_ADD_XOR));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XOR), VisualController.NO_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        //
        STATIC_ACTION_MAP.put(ACTIONID_COPY, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.COPY, null, ACTIONID_COPY));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_COPY), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_PASTE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.PASTE, null, ACTIONID_PASTE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PASTE), VisualController.CAN_PASTE, VisualController.IGNORE, VisualController.IGNORE);
        STATIC_ACTION_MAP.put(ACTIONID_CUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.CUT, null, ACTIONID_CUT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CUT), VisualController.ANY_SELECTION, VisualController.IGNORE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_CASCADE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.CASCADE, null, ACTIONID_CASCADE));
        STATIC_ACTION_MAP.put(ACTIONID_ARRANGE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.ARRANGE, null, ACTIONID_ARRANGE));
        STATIC_ACTION_MAP.put(ACTIONID_SHOWSIDEBAR, 
        		new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.SHOWSIDEBAR,  null, ACTIONID_SHOWSIDEBAR));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SHOWSIDEBAR), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.IGNORE);

        return STATIC_ACTION_MAP;
    }

    public static HashMap getStaticActionMap()
    {
        if (STATIC_ACTION_MAP == null) STATIC_ACTION_MAP = new HashMap();
        return STATIC_ACTION_MAP;
    }

    public static WoPeDAction getStaticAction(String id)
    {
        return (WoPeDAction) getStaticActionMap().get(id);
    }

    public static Action getSelectEditorAction(IEditor editor)
    {
        if (!(SELECT_EDITOR_SELECT_ACTION.get(editor) instanceof Action))
        {
            Action result = new EditorSelectAction(editor);
            SELECT_EDITOR_SELECT_ACTION.put(editor, result);
        }
        return (Action) SELECT_EDITOR_SELECT_ACTION.get(editor);

    }

    private static class EditorSelectAction extends WoPeDAction
    {

        private IEditor m_editor = null;

        public EditorSelectAction(IEditor editor)
        {
            super("Action.SelectEditor", new Object[] { editor });
            m_editor = editor;

        }

        public void actionPerformed(ActionEvent e)
        {
            if (m_editor.getContainer() instanceof JInternalFrame && ((JInternalFrame)m_editor.getContainer()).isSelected() && ((JInternalFrame)m_editor.getContainer()).isVisible())
            {
                AM.fireViewEvent(new EditorViewEvent(m_editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.INCONIFY_EDITOR));
            } else
            {
                AM.fireViewEvent(new EditorViewEvent(m_editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
            }
        }

    }
}
