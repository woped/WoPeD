package bpmn;

import java.util.LinkedList;
import java.util.List;

import epc.SequenceFlow;
import nodes.Cluster;
import nodes.ProcessNode;

public class SubProcess extends Cluster{
	
	/** Loop-Type: "NONE, STANDARD, SEQUENCE, PARALLEL" */
    public static final String PROP_LOOP_TYPE = "loop_type";
    /** Transaction: 0=false 1=true */
    public static final String PROP_TRANSACTION = "transaction";
    /** Event-SubProcess: 0=false 1=true */
    public static final String PROP_EVENT_SUBPROCESS = "triggered_by_event";
    /** The property for ad-hoc sub-processes (0=FALSE,1=TRUE) */
    public final static String PROP_AD_HOC = "adhoc";
    //** The property for compensation (0=FALSE;1=TRUE) */
    public final static String PROP_COMPENSATION = "compensation";

    public SubProcess() {
        super();
        initializeProperties();
    }

    public SubProcess(String label) {
        super();
        setText(label);
        initializeProperties();
    }

    @Override
        public void setProperty(String key, String value) {
        super.setProperty(key, value);
    }

    /**
     *
     */
    public void setAdHoc(){
        setProperty(PROP_AD_HOC, "TRUE");
    }

    /**
     *
      */
     public void setTransaction(){
        setProperty(PROP_TRANSACTION, "TRUE");
    }

    private void initializeProperties() {
        setProperty(PROP_LOOP_TYPE, Activity.LOOP_NONE);
//        String[] type = { Activity.LOOP_NONE, Activity.LOOP_STANDARD, Activity.LOOP_MULTI_SEQUENCE, Activity.LOOP_MULTI_PARALLEL };
        //setPropertyEditor(PROP_LOOP_TYPE, new ListSelectionPropertyEditor(type));
        
        setProperty(PROP_TRANSACTION, "FALSE");
        //setPropertyEditor(PROP_TRANSACTION, new BooleanPropertyEditor());

        setProperty(PROP_EVENT_SUBPROCESS, "FALSE");
        //setPropertyEditor(PROP_EVENT_SUBPROCESS, new BooleanPropertyEditor());

        setProperty(PROP_AD_HOC, "FALSE");
        //setPropertyEditor(PROP_AD_HOC, new BooleanPropertyEditor());

        setProperty(PROP_COMPENSATION, "FALSE");
        //setPropertyEditor(PROP_COMPENSATION, new BooleanPropertyEditor());
    }
    
    @Override
    public void addProcessNode(ProcessNode n) {
        super.addProcessNode(n);
        // Check if StartEvent, if so change non_interrupting to true
        /*if (((n instanceof MessageStartEvent) ||
                (n instanceof TimerStartEvent) ||
                (n instanceof EscalationStartEvent) ||
                (n instanceof ConditionalStartEvent) ||
                (n instanceof SignalStartEvent) ||
                (n instanceof MultipleStartEvent) ||
                (n instanceof ParallelMultipleStartEvent)) &&
                (getProperty(PROP_EVENT_SUBPROCESS).equals("TRUE"))){
            n.setProperty(StartEvent.PROP_NON_INTERRUPTING, "TRUE");
        }*/

    }

    @Override
    public void removeProcessNode(ProcessNode n) {
        if (this.isContained(n)) {
            super.removeProcessNode(n);
            // Check if StartEvent, if so change non_interrupting to false
            /*if (((n instanceof MessageStartEvent) ||
                    (n instanceof TimerStartEvent) ||
                    (n instanceof EscalationStartEvent) ||
                    (n instanceof ConditionalStartEvent) ||
                    (n instanceof SignalStartEvent) ||
                    (n instanceof MultipleStartEvent) ||
                    (n instanceof ParallelMultipleStartEvent)) &&
                    (getProperty(PROP_EVENT_SUBPROCESS).equals("TRUE"))){
                n.setProperty(StartEvent.PROP_NON_INTERRUPTING, FALSE);
            }*/
        }
    }

    @Override
    public boolean isCollapsed() {
        return getProperty(PROP_COLLAPSED).equals("TRUE");
    }



    /**
     * Code replicated from Activity, since SubProcess does not inherit Activity.
     * @return
     */
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
