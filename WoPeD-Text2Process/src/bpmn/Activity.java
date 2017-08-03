package bpmn;

import java.util.LinkedList;
import java.util.List;

import epc.SequenceFlow;
import nodes.FlowObject;
import nodes.ProcessNode;

public class Activity extends FlowObject {

	 /** Loop-Type: "NONE, STANDARD, SEQUENCE, PARALLEL" */
    public static final String PROP_LOOP_TYPE = "loop_type";
    public static final String TYPE_SERVICE = "SERVICE";
    public static final String TYPE_SEND = "SEND";
    public static final String TYPE_RECEIVE = "RECEIVE";
    public static final String TYPE_MANUAL = "MANUAL";
    public static final String TYPE_SCRIPT = "SCRIPT";
    public static final String TYPE_RULE = "RULE";
    public static final String TYPE_USER = "USER";
    public static final String TYPE_REFERENCE = "Reference";
    /** Loop-Property: None */
    public static final String LOOP_NONE = "NONE";
    /** Loop-Property: Standard */
    public static final String LOOP_STANDARD = "STANDARD";
    /** Loop-Property: Multi Instance Sequence */
    public static final String LOOP_MULTI_SEQUENCE = "SEQUENCE";
    /** Loop-Property: Mutli Instance Parallel */
    public static final String LOOP_MULTI_PARALLEL = "PARALLEL";
    
    public Activity() {
        super();
    }
    
    @Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(Task.class);
        result.add(SubProcess.class);
        result.add(CallActivity.class);
        return result;
    }

	@Override
	public void setIncoming(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

	@Override
	public void setOutgoing(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}
	
}
