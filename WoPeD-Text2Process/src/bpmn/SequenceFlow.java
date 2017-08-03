package bpmn;

import nodes.ProcessEdge;
import nodes.ProcessNode;

public class SequenceFlow extends ProcessEdge {   
    
    /** The direction of this SequenceFlow. Possible values are "STANDARD, DEFAULT, CONDITIONAL" */
    public final static String PROP_SEQUENCETYPE = "sequence_type";

    public final static String TYPE_STANDARD = "STANDARD";
    public final static String TYPE_DEFAULT = "DEFAULT";
    public final static String TYPE_CONDITIONAL = "CONDITIONAL";

    public SequenceFlow() {
        super();
        initializeProperties();
    }

    public SequenceFlow(ProcessNode source, ProcessNode target) {
        super();
        initializeProperties();
    }

    private void initializeProperties() {
        setProperty(PROP_SEQUENCETYPE, "TYPE_STANDARD");
  //      String[] type = { TYPE_STANDARD , TYPE_DEFAULT, TYPE_CONDITIONAL };
        //setPropertyEditor(PROP_SEQUENCETYPE, new ListSelectionPropertyEditor(type));

    }
    
   
    public boolean isOutlineSourceArrow() {
        String type = getProperty(PROP_SEQUENCETYPE).toLowerCase();
        if (type.equals(TYPE_CONDITIONAL)) {
            return true;
        }
        return false;
    }

    protected boolean isDockingSupported() {
        return true;
    }
}
