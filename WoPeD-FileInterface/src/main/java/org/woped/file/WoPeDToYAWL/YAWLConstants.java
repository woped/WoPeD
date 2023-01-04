package org.woped.file.WoPeDToYAWL;

public class YAWLConstants {

  // YAWL Nodes
  public static final String SPECIFICATIONSET = "specificationSet";
  public static final String SPECIFICATION = "specification";
  public static final String DOCUMENTATION = "documentation";
  public static final String METADATA = "metaData";
  public static final String CREATOR = "creator";
  public static final String DESCRIPTION = "description";
  public static final String COVERAGE = "coverage";
  public static final String VERSION = "version";
  public static final String PERSISTENT = "persistent";
  public static final String IDENTIFIER = "identifier";
  public static final String DECOMPOSITION = "decomposition";
  public static final String PROCESSCONTROLELEMENTS = "processControlElements";
  public static final String INPUTCONDITION = "inputCondition";
  public static final String OUTPUTCONDITION = "outputCondition";
  public static final String FLOWSINTO = "flowsInto";
  public static final String NEXTELEMENTREF = "nextElementRef";
  public static final String TASK = "task";
  public static final String NAME = "name";
  public static final String JOIN = "join";
  public static final String SPLIT = "split";
  public static final String RESOURCING = "resourcing";
  public static final String OFFER = "offer";
  public static final String ALLOCATE = "allocate";
  public static final String START = "start";
  public static final String ISDEFAULTFLOW = "isDefaultFlow";
  public static final String PREDICATE = "predicate";
  public static final String DECOMPOSESTO = "decomposesTo";
  public static final String LAYOUT = "layout";
  public static final String CONDITION = "condition";

  // Operators
  public static final String AND = "and";
  public static final String XOR = "xor";

  // YAWL Attributes
  public static final String ID = "id";
  public static final String CODE = "code";
  public static final String INITIATOR = "initiator";
  public static final String ORDERING = "ordering";

  // Layout Nodes
  public static final String LOCALE = "locale";
  public static final String SIZE = "size";
  public static final String NET = "net";
  public static final String BOUNDS = "bounds";
  public static final String FRAME = "frame";
  public static final String VIEWPORT = "viewport";
  public static final String VERTEX = "vertex";
  public static final String ATTRIBUTES = "attributes";
  public static final String CONTAINER = "container";
  public static final String LABEL = "label";
  public static final String DECORATOR = "decorator";
  public static final String POSITION = "position";
  public static final String FLOW = "flow";
  public static final String PORTS = "ports";
  public static final String LINESTYLE = "lineStyle";

  // Layout Attributes
  public static final String LANGUAGE = "language";
  public static final String COUNTRY = "country";
  public static final String DEFAULTBGCOLOR = "defaultBgColor";
  public static final String W = "w";
  public static final String H = "h";
  public static final String X = "x";
  public static final String Y = "y";
  public static final String BGCOLOR = "bgColor";
  public static final String TYPE = "type";
  public static final String SOURCE = "source";
  public static final String TARGET = "target";
  public static final String IN = "in";
  public static final String OUT = "out";

  // Layout Offsets
  public static final int XOFFSET_LABEL = -32;
  public static final int YOFFSET_LABEL = 32;
  public static final int XOFFSET_SPLIT_DECORATOR = 31;
  public static final int XOFFSET_JOIN_DECORATOR = -10;

  // Resource Nodes
  public static final String ORGDATA = "orgdata";
  public static final String PARTICIPANTS = "participants";
  public static final String PARTICIPANT = "participant";
  public static final String USERID = "userid";
  public static final String PASSWORD = "password";
  public static final String FIRSTNAME = "firstname";
  public static final String LASTNAME = "lastname";
  public static final String NOTES = "notes";
  public static final String ISADMINISTRATOR = "isAdministrator";
  public static final String ROLES = "roles";
  public static final String POSITIONS = "positions";
  public static final String CAPABILITIES = "capabilities";
  public static final String PRIVILEGES = "privileges";
  public static final String ROLE = "role";
  public static final String ORGGROUPS = "orggroups";
  public static final String ORGGROUP = "orggroup";
  public static final String GROUPNAME = "groupName";
  public static final String GROUPTYPE = "groupType";
  public static final String NONHUMANRESOURCES = "nonhumanresources";
  public static final String NONHUMANCATEGORIES = "nonhumancategories";
  public static final String DISTRIBUTIONSET = "distributionSet";
  public static final String INITIALSET = "initialSet";

  // Default Password
  public static final String DEFAULTPASSWORD = "YAWL";
}
