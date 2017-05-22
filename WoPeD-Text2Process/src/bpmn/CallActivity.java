package bpmn;

import java.util.LinkedList;
import nodes.Linkable;
import tools.ReferenceChooserRestriction;

public class CallActivity extends Activity implements Linkable {
	
	//** The property for the call type (TASK, PROCESS) */
    public final static String PROP_CALL_TYPE = "call_type";

    public final static String CALL_TASK = "TASK";
    public final static String CALL_PROCESS = "PROCESS";

    public static ReferenceChooserRestriction restrictions;

    public ReferenceChooserRestriction getReferenceRestrictions() {
        if (restrictions == null) {
            LinkedList<Class> classes = new LinkedList<Class>();
            classes.add(StartEvent.class);
            classes.add(Pool.class);
            classes.add(Task.class);
            restrictions = new ReferenceChooserRestriction(null, classes);
        }
        return restrictions;
    }

    public CallActivity() {
        super();
        initializeProperties();
    }
    
    public CallActivity(String label) {
        super();
        setText(label);
        initializeProperties();
    }
    
    protected void initializeProperties() {
        setProperty(PROP_CALL_TYPE, CALL_PROCESS);
  //      String[] type = { CALL_TASK, CALL_PROCESS };
        //setPropertyEditor( PROP_CALL_TYPE, new ListSelectionPropertyEditor(type));
        //setProperty(PROP_REF,"");
       // setPropertyEditor(PROP_REF, new ReferencePropertyEditor(getReferenceRestrictions()));
    }

}
