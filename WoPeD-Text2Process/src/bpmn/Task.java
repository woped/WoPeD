package bpmn;

public class Task extends Activity {
	
	//** The property for compensation (0=FALSE;1=TRUE) */
    public final static String PROP_COMPENSATION = "compensation";
    public final static String PROP_IMPLEMENTATION = "implementation";

    public Task() {
        super();
        initializeProperties();
    }

    public Task(String label) {
        super();
        setText(label);
        initializeProperties();
    }

    protected void initializeProperties() {
        setProperty(PROP_LOOP_TYPE, LOOP_NONE);
 //       String[] type = { Activity.LOOP_NONE, Activity.LOOP_STANDARD, Activity.LOOP_MULTI_SEQUENCE, Activity.LOOP_MULTI_PARALLEL };

 /*       String[] atype = { "", Activity.TYPE_SEND, Activity.TYPE_RECEIVE, Activity.TYPE_SERVICE,
            Activity.TYPE_USER, Activity.TYPE_MANUAL, Activity.TYPE_RULE, Activity.TYPE_SCRIPT
        };*/
        
        setProperty(PROP_COMPENSATION, "FALSE");
        
        setProperty(PROP_IMPLEMENTATION, "");
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
    }


}
