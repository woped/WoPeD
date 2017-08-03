package bpmn;

import java.util.Map;

import epc.SequenceFlow;
import nodes.AttachedNode;
import nodes.ProcessNode;
import nodes.ProcessObject;
import models.ProcessModel;

public class IntermediateEvent extends Event implements AttachedNode {

    /** The sub-type of the event. Possible values are "Catching" or "Throwing" **/
    public final static String PROP_EVENT_SUBTYPE = "event_subtype";
    /** Catching Intermediate Event */
    public final static String EVENT_SUBTYPE_CATCHING = "Catching";
    /** Throwing Intermediate Event */
    public final static String EVENT_SUBTYPE_THROWING = "Throwing";
    /** The interruption type of the event. Possible values are "0" or "Throwing" **/
    public final static String PROP_NON_INTERRUPTING = "non_interupting";
    public final static String EVENT_NON_INTERRUPTING_FALSE = "0";
    public final static String EVENT_NON_INTERRUPTING_TRUE = "1";
    /** The parent node */
    public final static String PROP_SOURCE_NODE = "#source";
    
    private boolean isThrowable = false;

    public IntermediateEvent() {
        super();
        initializeProperties();
    }

    public IntermediateEvent(String label) {
        super();
        setText(label);
        initializeProperties();
    }

    protected void initializeProperties() {
        setProperty(PROP_EVENT_SUBTYPE, EVENT_SUBTYPE_CATCHING);
//        String[] subtype = { EVENT_SUBTYPE_CATCHING , EVENT_SUBTYPE_THROWING };
        //setPropertyEditor(PROP_EVENT_SUBTYPE, new ListSelectionPropertyEditor(subtype));
        setProperty(PROP_NON_INTERRUPTING, EVENT_NON_INTERRUPTING_FALSE);
        //setPropertyEditor(PROP_NON_INTERRUPTING, new BooleanPropertyEditor());
        setProperty(PROP_SOURCE_NODE, "");
    }
    
    /**
     * tells you whether this intermediate event can be set to a throwing state or not
     * @return
     */
    public boolean isThrowable() {
    	return isThrowable;
    }

    public void setThrowable(boolean value) {
    	isThrowable = value;    	
    }
    
    @Override
    /**
     * overriding handling of throwable/catching
     */
    public void setProperty(String key, String value) {
    	if(key.equals(PROP_EVENT_SUBTYPE) && !isThrowable) {
    		super.setProperty(PROP_EVENT_SUBTYPE, EVENT_SUBTYPE_CATCHING);    		
    	}else {
    		super.setProperty(key, value);
    	}
    	
    }
    /*@Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(IntermediateEvent.class);
        result.add(MessageIntermediateEvent.class);
        result.add(TimerIntermediateEvent.class);
        result.add(ErrorIntermediateEvent.class);
        result.add(EscalationIntermediateEvent.class);
        result.add(CancelIntermediateEvent.class);
        result.add(CompensationIntermediateEvent.class);
        result.add(ConditionalIntermediateEvent.class);
        result.add(LinkIntermediateEvent.class);
        result.add(SignalIntermediateEvent.class);
        result.add(MultipleIntermediateEvent.class);
        result.add(ParallelMultipleIntermediateEvent.class);
        return result;
    }*/
    
    public ProcessNode getParentNode(ProcessModel model) {
        return model.getNodeById(getProperty(PROP_SOURCE_NODE));
    }

    protected void handleCloning(Map<String, String> localIdMap) {
    	setProperty(PROP_SOURCE_NODE, localIdMap.get(this.getProperty(PROP_SOURCE_NODE)));
    }
    
    public void setParentNode(ProcessNode node) {
        String id = "";
        if (node!=null) id = node.getProperty(ProcessObject.PROP_ID);
        setProperty(PROP_SOURCE_NODE, id==null?"":id);
    }

    public String getParentNodeId() {
        String result = getProperty(PROP_SOURCE_NODE);
        return result;
    }

    public String toString() {
        return "Intermediate Event ("+getText()+")";
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
