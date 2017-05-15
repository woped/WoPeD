package org.woped.editor.controller;

import org.woped.editor.action.EditorViewEvent;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.vc.EditorVC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class ActionFactory
{
    // Application Scope
    public final static String         ACTIONID_NEW                    = "Action.NewEditor";
    public final static String         ACTIONID_CLOSE                  = "Action.CloseEditor";
    public final static String         ACTIONID_SELECT                 = "Action.FocusEditor";
    public final static String         ACTIONID_SAVE                   = "Action.SaveEditor";
    public final static String		   ACTIONID_SAVEWEBSERVICE		   = "Action.SaveWebServiceEditor";	
    public final static String         ACTIONID_SAVEAS                 = "ToolBar.SaveAs";
    public final static String 		   ACTIONID_IMPORTAPRO 			   = "Action.ImportApromore";
    public final static String 		   ACTIONID_EXPORTAPRO 			   = "Action.ExportApromore";
    public final static String         ACTIONID_EXPORT                 = "Action.Export";
    public final static String         ACTIONID_OPEN                   = "ToolBar.Open";
    public final static String		   ACTIONID_OPENWEBSERVICE		   = "Action.OpenWebServiceEditor";
    public final static String         ACTIONID_OPENSAMPLE             = "Action.OpenSample";
    public final static String         ACTIONID_PRINT 				   = "Action.PrintEditor";

    public final static String         ACTIONID_SHOWCONFIG             = "Action.ShowConfig";
    public final static String         ACTIONID_SHOWABOUT              = "Action.ShowAbout";
    public final static String         ACTIONID_SHOWBUGREPORT          = "Action.ShowBugReport";
    public final static String         ACTIONID_SHOWHELPINDEX          = "Menu.Help.Index";
    public final static String         ACTIONID_SHOWHELPCONTENTS       = "Menu.Help.Contents";

    public final static String         ACTIONID_EXIT                   = "Action.QuitApplication";
    public final static String         ACTIONID_WOFLAN                 = "ToolBar.Woflan";
    public final static String		   ACTIONID_SEMANTICAL_ANALYSIS	   = "ToolBar.SemanticalAnalysis";
    
    public final static String         ACTIONID_OPEN_TOKENGAME         = "ToolBar.TokenGame.Open";
    public final static String         ACTIONID_CLOSE_TOKENGAME        = "ToolBar.TokenGame.Close";
    public final static String         ACTIONID_TOKENGAME_BACKWARD     = "ToolBar.TokenGame.Backward";
    public final static String         ACTIONID_TOKENGAME_JUMPINTO     = "ToolBar.TokenGame.JumpInto";
    public final static String         ACTIONID_TOKENGAME_FORWARD      = "ToolBar.TokenGame.Forward";
    public final static String         ACTIONID_TOKENGAME_PAUSE  	   = "ToolBar.TokenGame.Pause";
    public final static String         ACTIONID_TOKENGAME_START  	   = "ToolBar.TokenGame.Start";
    public final static String         ACTIONID_TOKENGAME_STOP  	   = "ToolBar.TokenGame.Stop";
    public final static String         ACTIONID_TOKENGAME_LEAVE  	   = "ToolBar.TokenGame.Leave";
    public final static String         ACTIONID_TOKENGAME_STEP  	   = "ToolBar.TokenGame.Step";
    public final static String         ACTIONID_TOKENGAME_AUTO  	   = "ToolBar.TokenGame.Auto";
    
    public final static String		   ACTIONID_ROTATEVIEW			   = "ToolBar.RotateView";
    public final static String		   ACTIONID_ROTATE_TRANS_LEFT	   = "ToolBar.RotateTransLeft";
    public final static String		   ACTIONID_ROTATE_TRANS_RIGHT	   = "ToolBar.RotateTransRight";
    public final static String		   ACTIONID_GRAPHBEAUTIFIER		   = "ToolBar.GraphBeautifier";
    public final static String		   ACTIONID_GRAPHBEAUTIFIER_DEFAULT= "Menu.View.GraphBeautifier.Default";
    public final static String		   ACTIONID_GRAPHBEAUTIFIER_ADV	   = "Menu.View.GraphBeautifier.Advanced";
    public final static String		   ACTIONID_METRIC                 = "ToolBar.Metric";
    public final static String		   ACTIONID_MASSMETRICANALYSE      = "Menu.Metric.MassAnalyse";
    public final static String		   ACTIONID_METRICSBUILDER		   = "Menu.Metric.MetricsBuilder";
    public final static String         ACTIONID_UPDATENETS             = "Action.UpdateNets";
    
    public final static String		   ACTIONID_P2T					   = "ToolBar.P2T";	
    public final static String		   ACTIONID_T2P					   = "ToolBar.T2P";
    
    public final static String         ACTIONID_DRAWMODE_PLACE         = "ToolBar.DrawPlace";
    public final static String         ACTIONID_DRAWMODE_TRANSITION    = "ToolBar.DrawTransition";
    public final static String         ACTIONID_DRAWMODE_ANDSPLIT      = "ToolBar.DrawAndSplit";
    public final static String         ACTIONID_DRAWMODE_ANDJOIN       = "ToolBar.DrawAndJoin";
    public final static String		   ACTIONID_DRAWMODE_ANDSPLITJOIN  = "ToolBar.DrawAndSplitJoin";
    public final static String         ACTIONID_DRAWMODE_XORSPLIT      = "ToolBar.DrawXorSplit";
    public final static String         ACTIONID_DRAWMODE_XORJOIN       = "ToolBar.DrawXorJoin";
    public final static String         ACTIONID_DRAWMODE_XORSPLITJOIN  = "ToolBar.DrawXorSplitJoin";
    public final static String 	       ACTIONID_DRAWMODE_ANDJOINXORSPLIT = "ToolBar.DrawAndJoinXorSplit";
    public final static String 	       ACTIONID_DRAWMODE_XORJOINANDSPLIT = "ToolBar.DrawXorJoinAndSplit";
    public final static String         ACTIONID_DRAWMODE_SUB           = "ToolBar.DrawSubProcess";    
    public final static String         ACTIONID_REDO                   = "Action.Redo";

    public final static String         ACTIONID_UNDO                   = "Action.Undo";
    public final static String         ACTIONID_UNGROUP                = "Action.UngroupSelection";
    public final static String         ACTIONID_GROUP                  = "Action.GroupSelection";
    public final static String         ACTIONID_EDIT                   = "Action.EditSelection";
    public final static String         ACTIONID_CUT                    = "Action.CutSelection";
    public final static String         ACTIONID_COPY                   = "Action.CopySelection";
    public final static String         ACTIONID_PASTE                  = "Action.PasteElements";
    public final static String         ACTIONID_PASTE_AT               = "Action.PasteElementsAt";
    public final static String         ACTIONID_REMOVE                 = "Popup.Remove";
    public final static String         ACTIONID_RENAME                 = "Popup.Rename";
    public final static String         ACTIONID_ACTIVATE_ROUTING       = "Popup.ActivateRouting";
    public final static String         ACTIONID_DEACTIVATE_ROUTING     = "Popup.DeactivateRouting";
    public final static String         ACTIONID_ACTIVATE_ALL_ROUTING   = "Popup.Route";
    public final static String         ACTIONID_DEACTIVATE_ALL_ROUTING = "Popup.Unroute";
    public final static String         ACTIONID_ADD_POINT              = "Popup.AddPoint";
    public final static String         ACTIONID_REMOVE_POINT           = "Popup.RemovePoint";
    public final static String         ACTIONID_ARC_WEIGHT_INCREASE    = "Popup.ArcWeightIncrease";
    public final static String         ACTIONID_ARC_WEIGHT_DECREASE    = "Popup.ArcWeightDecrease";
    public final static String         ACTIONID_OPEN_PROPERTIES        = "Popup.Properties";
    public final static String         ACTIONID_ADD_RESOURCE_TRIGGER   = "Popup.Trigger.AddResource";
    public final static String         ACTIONID_ADD_TIME_TRIGGER       = "Popup.Trigger.AddTime";
    public final static String         ACTIONID_ADD_MESSAGE_TRIGGER    = "Popup.Trigger.AddExternal";
    public final static String         ACTIONID_REMOVE_TRIGGER         = "Popup.Trigger.Remove";

    public final static String         ACTIONID_ADD_PLACE              = "Popup.Add.Place";
    public final static String         ACTIONID_ADD_TRANSITION         = "Popup.Add.Transition";
    public final static String         ACTIONID_ADD_TOKEN              = "Popup.Token.Add";
    public final static String         ACTIONID_REMOVE_TOKEN           = "Popup.Token.Remove";
    public final static String         ACTIONID_OPEN_SUBPROCESS        = "Popup.Subprocess";
    public final static String         ACTIONID_ADD_ANDJOIN            = "Popup.Add.AndJoin";
    public final static String         ACTIONID_ADD_ANDSPLIT           = "Popup.Add.AndSplit";
    public final static String		   ACTIONID_ADD_ANDSPLITJOIN       = "Popup.Add.AndSplitJoin";
    public final static String         ACTIONID_ADD_XORSPLITJOIN       = "Popup.Add.XorSplitJoin";
    public final static String         ACTIONID_ADD_XORJOIN            = "Popup.Add.XorJoin";
    public final static String         ACTIONID_ADD_XORSPLIT           = "Popup.Add.XorSplit";
    public final static String         ACTIONID_ADD_SUBPROCESS         = "Popup.Add.Subprocess";
    public final static String         ACTIONID_ADD_ANDJOINXORSPLIT    = "Popup.Add.AndJoinXorSplit";
    public final static String         ACTIONID_ADD_XORJOINANDSPLIT    = "Popup.Add.XorJoinAndSplit";
  
    public final static String         ACTIONID_ZOOMIN                 = "Action.ZoomIn";
    public final static String         ACTIONID_ZOOMOUT                = "Action.ZoomOut";
    public final static String		   ACTIONID_COLORING			   = "Action.Coloring";
    
    public final static String         ACTIONID_CASCADE                = "Action.Frames.Cascade";
    public final static String         ACTIONID_ARRANGE                = "Action.Frames.Arrange";
    public final static String		   ACTIONID_SHOWOVERVIEW		   = "Action.Frames.ShowOverview";
    public final static String		   ACTIONID_SHOWTREEVIEW           = "Action.Frames.ShowElements";

    public final static String         ACTIONID_QUANTCAP               = "ToolBar.QuantCap";
    public final static String         ACTIONID_QUANTSIM               = "ToolBar.QuantSim";
    public final static String         ACTIONID_REACHGRAPH_START       = "ToolBar.ReachabilityGraph";  
    
    public final static String         ACTIONID_REGISTER			   = "Action.Register";   
    public final static String		   ACTIONID_FACEBOOK			   = "Action.Facebook";
    public final static String		   ACTIONID_GOOGLEPLUS			   = "Action.Googleplus";
    public final static String		   ACTIONID_TWITTER  			   = "Action.Twitter";
    public final static String		   ACTIONID_COMMUNITY  			   = "Action.Community";

    
    private static HashMap<String, WoPeDAction> STATIC_ACTION_MAP               = null;
    private static HashMap<IEditor, Action>     SELECT_EDITOR_SELECT_ACTION     = new HashMap<IEditor, Action>();
    private static ApplicationMediator 			AM                         		= null;

    public static HashMap<String, WoPeDAction> createStaticActions(ApplicationMediator am)
    {
        AM = am;
        STATIC_ACTION_MAP = new HashMap<String, WoPeDAction>();

        STATIC_ACTION_MAP.put(ACTIONID_NEW, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.NEW, null, ACTIONID_NEW));
         
        STATIC_ACTION_MAP.put(ACTIONID_CLOSE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE, null, ACTIONID_CLOSE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CLOSE), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_SELECT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
        
        STATIC_ACTION_MAP.put(ACTIONID_SAVE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.SAVE, null, ACTIONID_SAVE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SAVE), VisualController.SUBPROCESS_EDITOR, VisualController.SUBPROCESS_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_SAVEAS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.SAVEAS, null, ACTIONID_SAVEAS));
        //VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SAVEAS), VisualController.SUBPROCESS_EDITOR, VisualController.IGNORE, VisualController.IGNORE);
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SAVEAS), VisualController.SUBPROCESS_EDITOR, VisualController.SUBPROCESS_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_IMPORTAPRO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.IMPORTAPRO, null, ACTIONID_IMPORTAPRO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_IMPORTAPRO), VisualController.APROMORE_IMPORT, VisualController.APROMORE_IMPORT, VisualController.IGNORE);        
       
        STATIC_ACTION_MAP.put(ACTIONID_EXPORTAPRO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.EXPORTAPRO, null, ACTIONID_EXPORTAPRO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_EXPORTAPRO), VisualController.APROMORE_EXPORT, VisualController.APROMORE_EXPORT, VisualController.IGNORE);        
        
        STATIC_ACTION_MAP.put(ACTIONID_OPEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, null, ACTIONID_OPEN));
        
        STATIC_ACTION_MAP.put(ACTIONID_OPENWEBSERVICE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPENWEBSERVICE, null, ACTIONID_OPENWEBSERVICE));
        
        STATIC_ACTION_MAP.put(ACTIONID_SAVEWEBSERVICE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.SAVEWEBSERVICE, null, ACTIONID_SAVEWEBSERVICE));
        
        STATIC_ACTION_MAP.put(ACTIONID_EXPORT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.EXPORT, null, ACTIONID_EXPORT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_EXPORT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWCONFIG, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.CONFIG, null, ACTIONID_SHOWCONFIG));
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWABOUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.ABOUT, null, ACTIONID_SHOWABOUT));
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWBUGREPORT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.BUGREPORT, null, ACTIONID_SHOWBUGREPORT));
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWHELPINDEX, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.HELP, null, ACTIONID_SHOWHELPINDEX));
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWHELPCONTENTS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.HELP_CONTENTS, null, ACTIONID_SHOWHELPCONTENTS));
        
        STATIC_ACTION_MAP.put(ACTIONID_EXIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.EXIT, null, ACTIONID_EXIT));
        
        STATIC_ACTION_MAP.put(ACTIONID_PRINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.PRINT, null, ACTIONID_PRINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PRINT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_UPDATENETS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.UPDATE));

        STATIC_ACTION_MAP.put(ACTIONID_SEMANTICAL_ANALYSIS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_WOPED, null, ACTIONID_SEMANTICAL_ANALYSIS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SEMANTICAL_ANALYSIS), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_METRIC, new WoPeDAction(am,AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_METRIC, null, ACTIONID_METRIC));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_METRIC), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_MASSMETRICANALYSE, new WoPeDAction(am,AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_MASSMETRICANALYSE, null, ACTIONID_MASSMETRICANALYSE));
        
        STATIC_ACTION_MAP.put(ACTIONID_METRICSBUILDER, new WoPeDAction(am,AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.ANALYSIS_METRICSBUILDER, null, ACTIONID_METRICSBUILDER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_METRICSBUILDER), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
               
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_PLACE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_PLACE, null, ACTIONID_DRAWMODE_PLACE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_PLACE), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_PLACE);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_TRANSITION, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_TRANSITION, null, ACTIONID_DRAWMODE_TRANSITION));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_TRANSITION), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_TRANSITION);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDSPLIT, null, ACTIONID_DRAWMODE_ANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDSPLIT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_AND_SPLIT);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDJOIN, null, ACTIONID_DRAWMODE_ANDJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDJOIN), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_AND_JOIN);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDSPLITJOIN, null, ACTIONID_DRAWMODE_ANDSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDSPLITJOIN), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_AND_SPLITJOIN);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORSPLIT, null, ACTIONID_DRAWMODE_XORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORSPLIT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_XOR_SPLIT);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORJOIN, null, ACTIONID_DRAWMODE_XORJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORJOIN), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_XOR_JOIN);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORSPLITJOIN, null, ACTIONID_DRAWMODE_XORSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORSPLITJOIN), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_XOR_SPLITJOIN);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_ANDJOINXORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_ANDJOIN_XORSPLIT, null, ACTIONID_DRAWMODE_ANDJOINXORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_ANDJOINXORSPLIT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_ANDJOIN_XORSPLIT);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_XORJOINANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_XORJOIN_ANDSPLIT, null, ACTIONID_DRAWMODE_XORJOINANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_XORJOINANDSPLIT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_XORJOIN_ANDSPLIT);
        
        STATIC_ACTION_MAP.put(ACTIONID_DRAWMODE_SUB, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.DRAWMODE_SUB, null, ACTIONID_DRAWMODE_SUB));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DRAWMODE_SUB), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.DRAWMODE_SUBPROCESS);
     
        STATIC_ACTION_MAP.put(ACTIONID_QUANTCAP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.QUANTCAP, null, ACTIONID_QUANTCAP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_QUANTCAP), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);        
        
        STATIC_ACTION_MAP.put(ACTIONID_QUANTSIM, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.QUANTSIM, null, ACTIONID_QUANTSIM));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_QUANTSIM), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);   
        
        STATIC_ACTION_MAP.put(ACTIONID_REACHGRAPH_START, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, AbstractViewEvent.REACHGRAPH, null, ACTIONID_REACHGRAPH_START));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REACHGRAPH_START), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.REACH_GRAPH_START);
        
        STATIC_ACTION_MAP.put(ACTIONID_ZOOMIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ZOOM_IN, null, ACTIONID_ZOOMIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ZOOMIN), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ZOOMOUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ZOOM_OUT, null, ACTIONID_ZOOMOUT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ZOOMOUT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_COLORING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.COLORING, null, ACTIONID_COLORING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_COLORING), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.COLORING);
        
        STATIC_ACTION_MAP.put(ACTIONID_P2T, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.P2T, null, ACTIONID_P2T));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_P2T), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.P2T);
        
        STATIC_ACTION_MAP.put(ACTIONID_T2P, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.T2P, null, ACTIONID_T2P));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_T2P), VisualController.ALWAYS, VisualController.ALWAYS, VisualController.T2P);
        
        
        STATIC_ACTION_MAP.put(ACTIONID_ROTATEVIEW, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROTATEVIEW, null, ACTIONID_ROTATEVIEW));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ROTATEVIEW), VisualController.SUBPROCESS_EDITOR, VisualController.SUBPROCESS_EDITOR, VisualController.ROTATE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ROTATE_TRANS_LEFT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROTATETRANSLEFT, null, ACTIONID_ROTATE_TRANS_LEFT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ROTATE_TRANS_LEFT), VisualController.OPERATOR_SELECTION, VisualController.OPERATOR_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ROTATE_TRANS_RIGHT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROTATETRANSRIGHT, null, ACTIONID_ROTATE_TRANS_RIGHT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ROTATE_TRANS_RIGHT), VisualController.OPERATOR_SELECTION, VisualController.OPERATOR_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_GRAPHBEAUTIFIER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.GRAPHBEAUTIFIER, null, ACTIONID_GRAPHBEAUTIFIER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_GRAPHBEAUTIFIER), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.NEVER);
        
        STATIC_ACTION_MAP.put(ACTIONID_GRAPHBEAUTIFIER_DEFAULT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.GRAPHBEAUTIFIER, null, ACTIONID_GRAPHBEAUTIFIER_DEFAULT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_GRAPHBEAUTIFIER_DEFAULT), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.NEVER);
        
        STATIC_ACTION_MAP.put(ACTIONID_GRAPHBEAUTIFIER_ADV, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.GRAPHBEAUTIFIER_ADV, null, ACTIONID_GRAPHBEAUTIFIER_ADV));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_GRAPHBEAUTIFIER_ADV), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.NEVER);
        
        STATIC_ACTION_MAP.put(ACTIONID_OPEN_TOKENGAME, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.OPEN_TOKENGAME, null, ACTIONID_OPEN_TOKENGAME));
        //VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_TOKENGAME), VisualController.WITH_EDITOR, VisualController.IGNORE, VisualController.TOKENGAME);
        //VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_TOKENGAME), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.TOKENGAME);
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_TOKENGAME),
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((!status[VisualController.TOKENGAME] && status[VisualController.WITH_EDITOR]));
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_CLOSE_TOKENGAME, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.CLOSE_TOKENGAME, null, ACTIONID_CLOSE_TOKENGAME));
       // VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CLOSE_TOKENGAME), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CLOSE_TOKENGAME),
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((status[VisualController.TOKENGAME]));
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_STEP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_STEP, null, ACTIONID_TOKENGAME_STEP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_STEP), VisualController.TOKENGAME_AUTOPLAY_MODE, VisualController.WITH_EDITOR, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_BACKWARD, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_BACKWARD, null, ACTIONID_TOKENGAME_BACKWARD));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_BACKWARD),
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((!status[VisualController.TOKENGAME_AUTOPLAY_MODE]) &&
								status[VisualController.TOKENGAME_TRANSITION_HISTORY]);
					}
				});

        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_STOP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_STOP, null, ACTIONID_TOKENGAME_STOP));
        //VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_STOP), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_STOP),                
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return (status[VisualController.TOKENGAME] && status[VisualController.WITH_EDITOR]);
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_FORWARD, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_FORWARD, null, ACTIONID_TOKENGAME_FORWARD));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_FORWARD),
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((!status[VisualController.TOKENGAME_AUTOPLAY_MODE]) &&
								status[VisualController.TOKENGAME_TRANSITION_ACTIVE]);
					}
				});
                
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_JUMPINTO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_JUMPINTO, null, ACTIONID_TOKENGAME_JUMPINTO));        
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_JUMPINTO),
        		new VisualController.IVisibility() {
					
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((!status[VisualController.TOKENGAME_AUTOPLAY_MODE]) &&
								status[VisualController.TOKENGAME_SUBPROCESS_TRANSITION_ACTIVE]);
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_LEAVE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_LEAVE, null, ACTIONID_TOKENGAME_LEAVE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_LEAVE),
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return ((!status[VisualController.TOKENGAME_AUTOPLAY_MODE]) &&
								status[VisualController.TOKENGAME_IN_SUBPROCESS]);
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_AUTO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_AUTO, null, ACTIONID_TOKENGAME_AUTO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_AUTO),                
                		new VisualController.IVisibility() {
        			
        					public boolean getVisible(boolean[] status) {
        						return status[VisualController.WITH_EDITOR];
        					}
        					
        					public boolean getSelected(boolean[] status) {
        						return false;
        					}
        					
        					public boolean getEnabled(boolean[] status) {
        						return (status[VisualController.TOKENGAME] && (!status[VisualController.TOKENGAME_AUTOPLAY_MODE]) &&
        								status[VisualController.WITH_EDITOR]);
        					}
        				});

        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_PAUSE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_PAUSE, null, ACTIONID_TOKENGAME_PAUSE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_PAUSE), 
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return (status[VisualController.TOKENGAME_AUTOPLAY_MODE] &&
								status[VisualController.TOKENGAME_AUTOPLAY_PLAYING]);
					}
				});
        
        STATIC_ACTION_MAP.put(ACTIONID_TOKENGAME_START, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.TOKENGAME_START, null, ACTIONID_TOKENGAME_START));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_TOKENGAME_START), 
        		new VisualController.IVisibility() {
			
					public boolean getVisible(boolean[] status) {
						return status[VisualController.WITH_EDITOR];
					}
					
					public boolean getSelected(boolean[] status) {
						return false;
					}
					
					public boolean getEnabled(boolean[] status) {
						return (status[VisualController.TOKENGAME_AUTOPLAY_MODE] &&
								(!status[VisualController.TOKENGAME_AUTOPLAY_PLAYING]));
					}
				});
        		                        
       STATIC_ACTION_MAP.put(ACTIONID_REDO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REDO, null, ACTIONID_REDO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REDO), VisualController.CAN_REDO, VisualController.CAN_REDO, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_UNDO, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.UNDO, null, ACTIONID_UNDO));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_UNDO), VisualController.CAN_UNDO, VisualController.CAN_UNDO, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_UNGROUP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.UNGROUP, null, ACTIONID_UNGROUP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_UNGROUP), VisualController.GROUP_SELECTION, VisualController.GROUP_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_GROUP, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.GROUP, null, ACTIONID_GROUP));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_GROUP), VisualController.MULTIPLE_SELECTION, VisualController.MULTIPLE_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_EDIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.EDIT, null, ACTIONID_EDIT));
        
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE, null, ACTIONID_REMOVE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE), VisualController.ANY_SELECTION, VisualController.ANY_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_RENAME, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.RENAME, null, ACTIONID_RENAME));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_RENAME), VisualController.NODE_SELECTION, VisualController.NODE_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ACTIVATE_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ACTIVE, null, ACTIONID_ACTIVATE_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ACTIVATE_ROUTING), VisualController.ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_DEACTIVATE_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_DEACTIVE, null, ACTIONID_DEACTIVATE_ROUTING));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_DEACTIVATE_ROUTING), VisualController.ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ACTIVATE_ALL_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ALL_ACTIVE, null, ACTIONID_ACTIVATE_ALL_ROUTING));
        
        STATIC_ACTION_MAP.put(ACTIONID_DEACTIVATE_ALL_ROUTING, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ROUTING_ALL_DEACTIVE, null, ACTIONID_DEACTIVATE_ALL_ROUTING));
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_POINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_POINT, null, ACTIONID_ADD_POINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_POINT), VisualController.ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_POINT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_POINT, null, ACTIONID_REMOVE_POINT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_POINT), VisualController.ARCPOINT_SELECTION, VisualController.ARCPOINT_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ARC_WEIGHT_INCREASE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.INCREASE_ARC_WEIGHT, null, ACTIONID_ARC_WEIGHT_INCREASE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ARC_WEIGHT_INCREASE), VisualController.ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ARC_WEIGHT_DECREASE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.DECREASE_ARC_WEIGHT, null, ACTIONID_ARC_WEIGHT_DECREASE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ARC_WEIGHT_DECREASE), VisualController.CAN_DECREASE_ARC_WEIGHT, VisualController.CAN_DECREASE_ARC_WEIGHT, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_OPEN_PROPERTIES, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.OPEN_PROPERTIES, null, ACTIONID_OPEN_PROPERTIES));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_PROPERTIES), VisualController.NODE_OR_ARC_SELECTION, VisualController.NODE_OR_ARC_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_MESSAGE_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_EXT_TRIGGER, null, ACTIONID_ADD_MESSAGE_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_MESSAGE_TRIGGER), VisualController.MESSAGE_TRIGGER_SELECTION, VisualController.MESSAGE_TRIGGER_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_RESOURCE_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_RES_TRIGGER, null, ACTIONID_ADD_RESOURCE_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_RESOURCE_TRIGGER), VisualController.RESOURCE_TRIGGER_SELECTION, VisualController.RESOURCE_TRIGGER_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_TIME_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TIME_TRIGGER, null, ACTIONID_ADD_TIME_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TIME_TRIGGER), VisualController.TIME_TRIGGER_SELECTION, VisualController.TIME_TRIGGER_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_TRIGGER, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_TRIGGER, null, ACTIONID_REMOVE_TRIGGER));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_TRIGGER), VisualController.TRIGGERED_TRANSITION_SELECTION, VisualController.TRIGGERED_TRANSITION_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_PLACE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_PLACE, null, ACTIONID_ADD_PLACE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_PLACE), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_TRANSITION, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TRANSITION, null, ACTIONID_ADD_TRANSITION));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TRANSITION), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDJOIN, null, ACTIONID_ADD_ANDJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDJOIN), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDSPLIT, null, ACTIONID_ADD_ANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDSPLIT), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDSPLITJOIN, null, ACTIONID_ADD_ANDSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDSPLITJOIN), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORJOIN, null, ACTIONID_ADD_XORJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORJOIN), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORSPLIT, null, ACTIONID_ADD_XORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORSPLIT), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORSPLITJOIN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORSPLITJOIN, null, ACTIONID_ADD_XORSPLITJOIN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORSPLITJOIN), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_ANDJOINXORSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_ANDJOINXORSPLIT, null, ACTIONID_ADD_ANDJOINXORSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_ANDJOINXORSPLIT), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ADD_XORJOINANDSPLIT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_XORJOINANDSPLIT, null, ACTIONID_ADD_XORJOINANDSPLIT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_XORJOINANDSPLIT), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ADD_SUBPROCESS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_SUBPROCESS, null, ACTIONID_ADD_SUBPROCESS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_SUBPROCESS), VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);

		STATIC_ACTION_MAP.put(ACTIONID_OPEN_SUBPROCESS, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.OPEN_SUBPROCESS, null, ACTIONID_OPEN_SUBPROCESS));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_OPEN_SUBPROCESS), VisualController.SUBPROCESS_SELECTION, VisualController.SUBPROCESS_SELECTION, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_ADD_TOKEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ADD_TOKEN, null, ACTIONID_ADD_TOKEN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ADD_TOKEN), VisualController.PLACE_SELECTION, VisualController.PLACE_SELECTION, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_REMOVE_TOKEN, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.REMOVE_TOKEN, null, ACTIONID_REMOVE_TOKEN));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_REMOVE_TOKEN), VisualController.TOKEN_PLACE_SELECTION, VisualController.TOKEN_PLACE_SELECTION, VisualController.IGNORE);
 
        STATIC_ACTION_MAP.put(ACTIONID_COPY, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.COPY, null, ACTIONID_COPY));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_COPY), VisualController.CAN_CUTCOPY, VisualController.CAN_CUTCOPY, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_CUT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.CUT, null, ACTIONID_CUT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CUT), VisualController.CAN_CUTCOPY, VisualController.CAN_CUTCOPY, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_PASTE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.PASTE, null, ACTIONID_PASTE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PASTE), VisualController.CAN_PASTE, VisualController.CAN_PASTE, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_PASTE_AT, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.PASTE_AT, null, ACTIONID_PASTE_AT));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_PASTE_AT), VisualController.CAN_PASTE, VisualController.CAN_PASTE, VisualController.IGNORE);

        STATIC_ACTION_MAP.put(ACTIONID_CASCADE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.CASCADE, null, ACTIONID_CASCADE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_CASCADE), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_ARRANGE, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.ARRANGE, null, ACTIONID_ARRANGE));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_ARRANGE), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
        
        STATIC_ACTION_MAP.put(ACTIONID_SHOWOVERVIEW, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.SHOWOVERVIEW, null, ACTIONID_SHOWOVERVIEW));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SHOWOVERVIEW), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR,  VisualController.OVERVIEW_VISIBLE);

        STATIC_ACTION_MAP.put(ACTIONID_SHOWTREEVIEW, new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.SHOWTREEVIEW, null, ACTIONID_SHOWTREEVIEW));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_SHOWTREEVIEW), VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.TREEVIEW_VISIBLE);

        STATIC_ACTION_MAP.put(ACTIONID_REGISTER, 	new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.REGISTER, 	null, ACTIONID_REGISTER));

        STATIC_ACTION_MAP.put(ACTIONID_FACEBOOK, 		new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.FACEBOOK, 		null, ACTIONID_FACEBOOK));
        
        STATIC_ACTION_MAP.put(ACTIONID_GOOGLEPLUS, 		new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.GOOGLEPLUS, 		null, ACTIONID_GOOGLEPLUS));
        
        STATIC_ACTION_MAP.put(ACTIONID_TWITTER, 		new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.TWITTER, 		null, ACTIONID_TWITTER));
        
        STATIC_ACTION_MAP.put(ACTIONID_COMMUNITY, 		new WoPeDAction(am, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.COMMUNITY, 		null, ACTIONID_COMMUNITY));
        VisualController.getInstance().addElement(STATIC_ACTION_MAP.get(ACTIONID_COMMUNITY), VisualController.IS_REGISTERED, VisualController.IGNORE, VisualController.IGNORE);
        
        return STATIC_ACTION_MAP;
    }

    public static HashMap<String, WoPeDAction> getStaticActionMap()
    {
        if (STATIC_ACTION_MAP == null) {
        	STATIC_ACTION_MAP = new HashMap<String, WoPeDAction>();
        }
        return STATIC_ACTION_MAP;
    }

    public static void registerAction(String actionId, WoPeDAction action){
        STATIC_ACTION_MAP.put(actionId, action);
    }

    public static WoPeDAction getStaticAction(String id)
    {
        return getStaticActionMap().get(id);
    }

    public static void addTarget(AbstractApplicationMediator mediator, String id, JComponent target) 
    {
    	getStaticActionMap().get(id).addTarget(target);
    }
    
    public static Action getSelectEditorAction(IEditor editor)
    {
        if (!(SELECT_EDITOR_SELECT_ACTION.get(editor) instanceof Action))
        {
            Action result = new EditorSelectAction(editor);
            SELECT_EDITOR_SELECT_ACTION.put(editor, result);
        }
        return SELECT_EDITOR_SELECT_ACTION.get(editor);

    }

    @SuppressWarnings("serial")
    private static class EditorSelectAction extends WoPeDAction
    {
        private IEditor m_editor = null;

        public EditorSelectAction(IEditor editor)
        {
             super("Action.SelectEditor", new Object[] { editor });
            	m_editor = editor;
        }

        public void actionPerformed(ActionEvent e) {
            if (((EditorVC)m_editor).getEditorPanel().getContainer() instanceof JInternalFrame 
        	    && ((JInternalFrame) ((EditorVC)m_editor).getEditorPanel().getContainer()).isSelected() && ((EditorVC) m_editor).getEditorPanel().getContainer().isVisible() ) {
                AM.fireViewEvent(new EditorViewEvent(m_editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.INCONIFY_EDITOR));
            } else {
                AM.fireViewEvent(new EditorViewEvent(m_editor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.SELECT_EDITOR));
            }
        }
    }
}
