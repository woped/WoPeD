package org.woped.core.controller;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public abstract class AbstractViewEvent extends ActionEvent
{

    // TYPES
    public static final int VIEWEVENTTYPE_EDIT        = 0;
    public static final int VIEWEVENTTYPE_APPLICATION = 1;
    public static final int VIEWEVENTTYPE_FILE        = 2;
    public static final int VIEWEVENTTYPE_GUI         = 3;


    /**
     * Type for coverability graph related events
     * <p>
     * The events from this group can be found in {@code CoverabilityGraphViewEvents}.
     * They use the range from 10.000 to 10.100 for their events.
     */
    public static final int VIEWEVENTTYPE_COVERABILITY_GRAPH = 4;
    public static final int VIEWEVENTTYPE_IMPORTAPRO = 701;
    public static final int VIEWEVENTTYPE_EXPORTAPRO = 702;


    // General Orders
    public static final int VIEWEVENT_FIRST           = 0;
    public static final int OK                        = 0;
    public static final int DONE                      = 1;
    public static final int OPEN                      = 2;
    public static final int CLOSE                     = 3;
    public static final int CANCEL                    = 4;
    public static final int EXIT                      = 5;
    public static final int FILE                      = 6;
    public static final int SAVE                      = 7;
    public static final int SAVEAS                    = 8;
    public static final int IMPORTAPRO                = 703;
    public static final int EXPORTAPRO                = 704;
    public static final int ERROR                     = 9;
    public static final int WARNING                   = 10;
    public static final int RETURN                    = 11;
    public static final int LOAD                      = 12;
    public static final int NOTIFY                    = 13;
    public static final int NOTIFY2                   = 14;
    public static final int INFO                      = 15;
    public static final int SETUP                     = 16;
    public static final int PRINT                     = 17;
    public static final int ENABLE                    = 18;
    public static final int DISABLE                   = 19;
    public static final int OPENTO                    = 20;
    public static final int OPENWEBSERVICE			  = 21;
    public static final int SAVEWEBSERVICE			  = 22;
    public static final int LOGIN                     = 50;
    public static final int LOGON                     = 51;
    public static final int LOGOUT                    = 52;
    public static final int LOGOFF                    = 53;
    public static final int OVERRIDE                  = 54;
    public static final int SUPERVISOR                = 55;
    public static final int SKILL                     = 56;
    public static final int LEVEL                     = 57;
    public static final int TITLEMESSAGE              = 100;
    public static final int STATUSMESSAGE             = 101;
    public static final int ERRORMESSAGE              = 102;
    public static final int SUGGESTIONMESSAGE         = 103;
    public static final int NEXT                      = 200;
    public static final int PREVIOUS                  = 201;
    public static final int FIRST                     = 202;
    public static final int LAST                      = 203;
    public static final int START                     = 204;
    public static final int BEGIN                     = 205;
    public static final int END                       = 206;
    public static final int PAUSE                     = 207;
    public static final int STOP                      = 208;
    public static final int RESTART                   = 209;
    public static final int SUBMIT                    = 210;
    public static final int BACKSPACE                 = 211;
    public static final int INSERT                    = 212;
    public static final int HOME                      = 213;
    public static final int PGUP                      = 214;
    public static final int PGDN                      = 215;
    public static final int LEFT                      = 216;
    public static final int RIGHT                     = 217;
    public static final int UP                        = 218;
    public static final int DOWN                      = 219;
    public static final int GOTO                      = 220;
    public static final int FAST                      = 620;
    public static final int MEDIUM                    = 621;
    public static final int SLOW                      = 622;
    public static final int RUN                       = 623;
    public static final int DELAY                     = 624;
    public static final int WAIT                      = 625;
    public static final int TIMER                     = 626;
    public static final int ON                        = 627;
    public static final int OFF                       = 628;
    public static final int HIGH                      = 629;
    public static final int LOW                       = 630;
    public static final int LIST                      = 300;
    public static final int MORE                      = 301;
    public static final int ADD                       = 302;
    public static final int DELETE                    = 303;
    public static final int MODIFY                    = 304;
    public static final int NEW                       = 305;
    public static final int EDIT                      = 306;
    public static final int COPY                      = 307;
    public static final int CUT                       = 308;
    public static final int PASTE                     = 309;
    public static final int PASTE_AT                  = 328;
    public static final int UNDO                      = 310;
    public static final int REMOVE                    = 311;
    public static final int PLUS                      = 312;
    public static final int MINUS                     = 313;
    public static final int INCREMENT                 = 314;
    public static final int DECREMENT                 = 315;
    public static final int CHANGED                   = 316;
    public static final int FILL                      = 317;
    public static final int EMPTY                     = 318;
    public static final int READY                     = 319;
    public static final int VIEW                      = 320;
    public static final int DETAILS                   = 321;
    public static final int READ                      = 322;
    public static final int WRITE                     = 323;
    public static final int UPDATE                    = 324;
    public static final int REFRESH                   = 325;
    public static final int SELECT                    = 326;
    public static final int UNSELECT                  = 327;
    public static final int SEARCH                    = 400;
    public static final int FIND                      = 401;
    public static final int HELP                      = 402;
    public static final int HINT                      = 403;
    public static final int TRAIN                     = 404;
    public static final int TEACH                     = 405;
    public static final int SUGGEST                   = 406;
    public static final int RENAME                    = 407;
    public static final int A                         = 500;
    public static final int B                         = 501;
    public static final int C                         = 502;
    public static final int D                         = 503;
    public static final int E                         = 504;
    public static final int F                         = 505;
    public static final int G                         = 506;
    public static final int H                         = 507;
    public static final int I                         = 508;
    public static final int J                         = 509;
    public static final int K                         = 510;
    public static final int L                         = 511;
    public static final int M                         = 512;
    public static final int N                         = 513;
    public static final int O                         = 514;
    public static final int P                         = 515;
    public static final int Q                         = 516;
    public static final int R                         = 517;
    public static final int S                         = 518;
    public static final int T                         = 519;
    public static final int U                         = 520;
    public static final int V                         = 521;
    public static final int W                         = 522;
    public static final int X                         = 523;
    public static final int Y                         = 524;
    public static final int Z                         = 525;
    public static final int OPTION                    = 550;
    public static final int CHOOSE                    = 551;
    public static final int OPT1                      = 555;
    public static final int OPT2                      = 556;
    public static final int OPT3                      = 557;
    public static final int OPT4                      = 558;
    public static final int OPT5                      = 559;
    public static final int OPT6                      = 560;
    public static final int OPT7                      = 561;
    public static final int OPT8                      = 562;
    public static final int TRACE                     = 800;
    public static final int UNTRACE                   = 801;
    public static final int DEBUG                     = 802;
    public static final int UNDEBUG                   = 803;
    public static final int LOG                       = 804;
    public static final int UNLOG                     = 805;
    public static final int HOOK                      = 806;
    public static final int UNHOOK                    = 807;
    public static final int TEAM                      = 900;
    public static final int WIN                       = 901;
    public static final int EXECUTE                   = 902;
    public static final int ACTION_EVENT = 903;
    // WoPeD Orders
    public static final int SELECT_EDITOR             = 1000;
    public static final int OPEN_SAMPLE               = 1001;
    public static final int CONFIG                    = 1002;
    public static final int ABOUT                     = 1003;
    public static final int HELP_CONTENTS             = 1004;
    public static final int SCREENSHOT                = 1005;
    public static final int EXPORT                    = 1006;
    public static final int STRETCH                   = 1007;
    public static final int PRESS                     = 1008;
    public static final int INCONIFY_EDITOR           = 1009;
    public static final int ZOOM_ABSOLUTE             = 1010;
    public static final int ZOOM_IN                   = 1011;
    public static final int ZOOM_OUT                  = 1012;
    public static final int ZOOMED                    = 1013;
    public static final int BUGREPORT                 = 1014;
    public static final int DRAWMODE_PLACE            = 1019;
    public static final int DRAWMODE_TRANSITION       = 1020;
    public static final int DRAWMODE_ANDSPLIT         = 1021;
    public static final int DRAWMODE_ANDJOIN          = 1022;
    public static final int DRAWMODE_ANDSPLITJOIN     = 1023;
    public static final int DRAWMODE_XORSPLIT         = 1024;
    public static final int DRAWMODE_XORJOIN          = 1025;
    public static final int DRAWMODE_SUB              = 1026;
    public static final int DRAWMODE_XORSPLITJOIN     = 1027;
    public static final int CHECK_SELECTION           = 1028;
    public static final int ARC_SELECTED              = 1029;
    public static final int REDO                      = 1030;
    public static final int UNGROUP                   = 1031;
    public static final int GROUP                     = 1032;
    public static final int ROUTING_DEACTIVE          = 1033;
    public static final int ROUTING_ACTIVE            = 1034;
    public static final int ROUTING_ALL_DEACTIVE      = 1035;
    public static final int ROUTING_ALL_ACTIVE        = 1036;
    public static final int ADD_POINT                 = 1037;
    public static final int REMOVE_POINT              = 1038;
    public static final int CELL_SELECTED             = 1039;
    // Petrinet Elements
    public static final int ADD_EXT_TRIGGER           = 1040;
    public static final int ADD_RES_TRIGGER           = 1041;
    public static final int ADD_TIME_TRIGGER          = 1042;
    public static final int REMOVE_TRIGGER            = 1043;
    public static final int ADD_PLACE                 = 1044;
    public static final int ADD_TRANSITION            = 1045;
    public static final int ADD_ANDJOIN               = 1046;
    public static final int ADD_ANDSPLIT              = 1047;
    public static final int ADD_XORJOIN               = 1048;
    public static final int ADD_XORSPLIT              = 1049;
    public static final int ADD_SUBPROCESS            = 1050;
    public static final int OPEN_SUBPROCESS           = 1051;
    public static final int REMOVE_TOKEN              = 1052;
    public static final int ADD_TOKEN                 = 1053;
    public static final int ADD_XORSPLITJOIN          = 1054;
    public static final int ADD_ANDSPLITJOIN		  = 1055;
    public static final int ADD_ANDJOINXORSPLIT       = 1056;
    public static final int ADD_XORJOINANDSPLIT		  = 1057;
    public static final int OPEN_PROPERTIES           = 1060;
    public static final int ANALYSIS_WOPED			  = 1062;
    public static final int COLORING 		 		  = 1063;
    public static final int ROTATEVIEW				  = 1064;
    public static final int ROTATETRANSLEFT			  = 1065;
    public static final int ROTATETRANSRIGHT		  = 1066;
    public static final int ANALYSIS_METRIC           = 1067;
    public static final int ANALYSIS_MASSMETRICANALYSE= 1068;
    public static final int ANALYSIS_METRICSBUILDER   = 1069;
    public static final int OPEN_TOKENGAME            = 1070;
    public static final int CLOSE_TOKENGAME           = 1071;
    //
    public static final int CASCADE                   = 1072;
    public static final int ARRANGE                   = 1073;
    public static final int SHOWSIDEBAR				  = 1074;
    public static final int DRAWMODE_ANDJOIN_XORSPLIT = 1075;
    public static final int DRAWMODE_XORJOIN_ANDSPLIT = 1076;
    public static final int SHOWOVERVIEW              = 1077;
    public static final int SHOWTREEVIEW			  = 1078;
    public static final int TOKENGAME_BACKWARD		  = 1079;
    public static final int TOKENGAME_JUMPINTO		  = 1080;
    public static final int TOKENGAME_FORWARD		  = 1081;
    public static final int TOKENGAME_PAUSE			  = 1082;
    public static final int TOKENGAME_START			  = 1083;
    public static final int TOKENGAME_STOP			  = 1084;
    public static final int TOKENGAME_LEAVE			  = 1085;
    public static final int TOKENGAME_STEP			  = 1086;
    public static final int TOKENGAME_AUTO			  = 1087;
    public static final int QUANTCAP				  = 1088;
    public static final int QUANTSIM				  = 1089;
    public static final int REACHGRAPH = 1090;
    public static final int GRAPHBEAUTIFIER			  = 1091;
    public static final int IMPROVE_LAYOUT			  = 1092;
    public static final int GRAPHBEAUTIFIER_ADV		  = 1093;
    public static final int VIEWEVENT_LAST            = 1094;
    public static final int REGISTER				  = 1095;
    public static final int FACEBOOK     			  = 1096;
    public static final int GOOGLEPLUS  			  = 1097;
    public static final int TWITTER     			  = 1098;
    public static final int COMMUNITY     			  = 1099;
    public static final int P2T						  = 1100;
    public static final int T2P						  = 1103;
    public static final int INCREASE_ARC_WEIGHT = 1101;
    public static final int DECREASE_ARC_WEIGHT = 1102;

    // Keys for coverability graph events are in the range from 10.000 to 10.100;
    // The can be found at CoverabilityGraphViewEvents in the coverability graph module

    //variable to change toolbars and references
    public static final int CHANGEPANEL				  = 1999;
    private IViewListener viewListener = null;
    private Object data = null;
    private int type = -1;
    private int order = -1;
    
    public AbstractViewEvent(Object source, int type, int order, Object data)
    {
        super(source, -1, null);
        this.type = type;
        this.order = order;
        this.data = data;
    }

    public AbstractViewEvent(Object source, int type, int order)
    {
        this(source, type, order, null);
    }

    /**
     * Get the the current listener receiving the event
     */
    public IViewListener getViewListener()
    {
        return viewListener;
    }

    /**
     * Set the current listener receiving the event
     */
    public void setViewListener(IViewListener viewlistener)
    {
        viewListener = viewlistener;
    }

    public int getType()
    {
        return type;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public void consume()
    {
        super.consume();
    }

    public boolean isConsumed()
    {
        return super.isConsumed();
    }

    public Object getData()
    {
        return data;
    }

}
