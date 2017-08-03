package bpmn;


public class ChoreographyTask extends ChoreographyActivity{
	
	/** The property for the name of the upper participant */
    public final static String PROP_UPPER_PARTICIPANT = "upper_participant";
    /** The property for the name of the lower participant */
    public final static String PROP_LOWER_PARTICIPANT = "lower_participant";
    /** The property for the active participant */
    public final static String PROP_ACTIVE_PARTICIPANT = "active_participant";
    /** The property for the loop type: "NONE, STANDARD, MULTIINSTANCE" */
    public static final String PROP_LOOP_TYPE = "loop_type";
    /** The property for upper multi instance participants (=FALSE,1=TRUE)*/
    public final static String PROP_UPPER_PARTICIPANT_MULTI = "upper_mi";
    /** The property for  lower multi instance participants (=FALSE,1=TRUE) */
    public final static String PROP_LOWER_PARTICIPANT_MULTI = "lower_mi";
    public static final String LOOP_NONE = "NONE";
    public static final String LOOP_STANDARD = "STANDARD";
    public static final String LOOP_MULTI_INSTANCE = "MULTIINSTANCE";

    public ChoreographyTask() {
        super();
        initializeProps();
    }

    public ChoreographyTask(String label) {
        super();
        initializeProps();
        setText(label);
    }
    
    private void initializeProps() {
        setProperty(PROP_UPPER_PARTICIPANT, "Participant A");
        setProperty(PROP_LOWER_PARTICIPANT, "Participant B");
        setProperty(PROP_ACTIVE_PARTICIPANT, "Participant A");
        setProperty(PROP_LOOP_TYPE, LOOP_NONE);
 //       String[] loop = {LOOP_NONE, LOOP_STANDARD, LOOP_MULTI_INSTANCE};

        setProperty(PROP_UPPER_PARTICIPANT_MULTI, "FALSE");

        setProperty(PROP_LOWER_PARTICIPANT_MULTI, "FALSE");
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
    }

    @Override
    public String toString() {
        return "BPMN Choreography Task";
    }

}
