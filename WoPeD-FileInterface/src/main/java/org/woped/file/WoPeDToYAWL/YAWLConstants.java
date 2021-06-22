package org.woped.file.WoPeDToYAWL;

public class YAWLConstants {

    // YAWL Nodes
    public final static String SPECIFICATIONSET = "specificationSet";
    public final static String SPECIFICATION = "specification";
    public final static String DOCUMENTATION = "documentation";
    public final static String METADATA = "metaData";
    public final static String CREATOR = "creator";
    public final static String DESCRIPTION = "description";
    public final static String COVERAGE = "coverage";
    public final static String VERSION = "version";
    public final static String PERSISTENT = "persistent";
    public final static String IDENTIFIER = "identifier";
    public final static String DECOMPOSITION = "decomposition";
    public final static String PROCESSCONTROLELEMENTS = "processControlElements";
    public final static String INPUTCONDITION = "inputCondition";
    public final static String OUTPUTCONDITION = "outputCondition";
    public final static String FLOWSINTO = "flowsInto";
    public final static String NEXTELEMENTREF = "nextElementRef";
    public final static String TASK = "task";
    public final static String NAME = "name";
    public final static String JOIN = "join";
    public final static String SPLIT = "split";
    public final static String RESOURCING = "resourcing";
    public final static String OFFER = "offer";
    public final static String ALLOCATE = "allocate";
    public final static String START = "start";
    public final static String ISDEFAULTFLOW = "isDefaultFlow";
    public final static String PREDICATE = "predicate";
    public final static String DECOMPOSESTO = "decomposesTo";
    public final static String LAYOUT = "layout";
    public final static String CONDITION = "condition";

    // Operators
    public final static String AND = "and";
    public final static String XOR = "xor";

    // YAWL Attributes
    public final static String ID = "id";
    public final static String CODE = "code";
    public final static String INITIATOR = "initiator";
    public final static String ORDERING = "ordering";

    // Layout Nodes
    public final static String LOCALE = "locale";
    public final static String SIZE = "size";
    public final static String NET = "net";
    public final static String BOUNDS = "bounds";
    public final static String FRAME = "frame";
    public final static String VIEWPORT = "viewport";
    public final static String VERTEX = "vertex";
    public final static String ATTRIBUTES = "attributes";
    public final static String CONTAINER = "container";
    public final static String LABEL = "label";
    public final static String DECORATOR = "decorator";
    public final static String POSITION = "position";
    public final static String FLOW = "flow";
    public final static String PORTS = "ports";
    public final static String LINESTYLE = "lineStyle";

    // Layout Attributes
    public final static String LANGUAGE = "language";
    public final static String COUNTRY = "country";
    public final static String DEFAULTBGCOLOR = "defaultBgColor";
    public final static String W = "w";
    public final static String H = "h";
    public final static String X = "x";
    public final static String Y = "y";
    public final static String BGCOLOR = "bgColor";
    public final static String TYPE = "type";
    public final static String SOURCE = "source";
    public final static String TARGET = "target";
    public final static String IN = "in";
    public final static String OUT = "out";

    // Layout Offsets
    public final static int XOFFSET_LABEL = -32;
    public final static int YOFFSET_LABEL = 32;
    public final static int XOFFSET_SPLIT_DECORATOR = 31;
    public final static int XOFFSET_JOIN_DECORATOR = -10;

    // Resource Nodes
    public final static String ORGDATA = "orgdata";
    public final static String PARTICIPANTS = "participants";
    public final static String PARTICIPANT = "participant";
    public final static String USERID = "userid";
    public final static String PASSWORD = "password";
    public final static String FIRSTNAME = "firstname";
    public final static String LASTNAME = "lastname";
    public final static String NOTES = "notes";
    public final static String ISADMINISTRATOR = "isAdministrator";
    public final static String ROLES = "roles";
    public final static String POSITIONS = "positions";
    public final static String CAPABILITIES = "capabilities";
    public final static String PRIVILEGES = "privileges";
    public final static String ROLE = "role";
    public final static String ORGGROUPS = "orggroups";
    public final static String ORGGROUP = "orggroup";
    public final static String GROUPNAME = "groupName";
    public final static String GROUPTYPE = "groupType";
    public final static String NONHUMANRESOURCES = "nonhumanresources";
    public final static String NONHUMANCATEGORIES = "nonhumancategories";
    public final static String DISTRIBUTIONSET = "distributionSet";
    public final static String INITIALSET = "initialSet";

    // Default Password
    public final static String DEFAULTPASSWORD = "YAWL";
}
