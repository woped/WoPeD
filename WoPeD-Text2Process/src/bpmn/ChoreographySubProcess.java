package bpmn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import epc.SequenceFlow;
import nodes.Cluster;
import nodes.ProcessNode;

public class ChoreographySubProcess extends Cluster {

	/** The property for the name of the upper participant */
    public final static String PROP_UPPER_PARTICIPANTS = "upper_participants";
    /** The property for the name of the lower participant */
    public final static String PROP_LOWER_PARTICIPANTS = "lower_participants";
    /** The property for the active participant */
    public final static String PROP_ACTIVE_PARTICIPANTS = "active_participants";
    /** The property for the loop type: "NONE, STANDARD, MULTIINSTANCE" */
    public static final String PROP_LOOP_TYPE = "loop_type";
    /** The property for upper multi instance participants (=FALSE,1=TRUE)*/
    public final static String PROP_UPPER_PARTICIPANTS_MULTI = "upper_mi";
    /** The property for lower multi instance participants (=FALSE,1=TRUE) */
    public final static String PROP_LOWER_PARTICIPANTS_MULTI = "lower_mi";
    public static final String LOOP_NONE = "NONE";
    public static final String LOOP_STANDARD = "STANDARD";
    public static final String LOOP_MULTI_INSTANCE = "MULTIINSTANCE";

    public ChoreographySubProcess() {
        super();
        initializeProps();
    }

    public ChoreographySubProcess(int x, int y, String label) {
        super();
        initializeProps();
        setText(label);
    }

    private void initializeProps() {
        setProperty(PROP_UPPER_PARTICIPANTS, "Participant A");
        setProperty(PROP_LOWER_PARTICIPANTS, "Participant B");
        setProperty(PROP_ACTIVE_PARTICIPANTS, "Participant A");
        setProperty(PROP_LOOP_TYPE, LOOP_NONE);
 //       String[] loop = { LOOP_NONE , LOOP_STANDARD, LOOP_MULTI_INSTANCE };

        setProperty(PROP_UPPER_PARTICIPANTS_MULTI, "0");
        setProperty(PROP_LOWER_PARTICIPANTS_MULTI, "0");

    }
    
    public List<String> getUpperParticipants(){
    	return Arrays.asList(getProperty(PROP_UPPER_PARTICIPANTS).split(";"));
    }
    
    public List<String> getLowerParticipants(){
    	return Arrays.asList(getProperty(PROP_LOWER_PARTICIPANTS).split(";"));
    }
    
    /**
     * Code replicated from ChoreographyActivity, since ChoreographySubProcess
     * does not inherit ChoreographyActivity.
     * @return
     */
    @Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(ChoreographyTask.class);
        result.add(ChoreographySubProcess.class);
        return result;
    }

    @Override
    public boolean isCollapsed() {
        return getProperty(PROP_COLLAPSED).equalsIgnoreCase("TRUE");
    }

    @Override
    public String toString() {
        return "BPMN Choreography Sub Process";
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
