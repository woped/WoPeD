package bpmn;

public class EventBasedGateway extends Gateway{
	
	 /** Defines if this Gateway is instantiating: NONE, EXCLUSIVE, PARALLEL */
    public final static String PROP_INSTANTIATE = "instantiate";

    public final static String TYPE_INSTANTIATE_NONE = "NONE";
    public final static String TYPE_INSTANTIATE_EXCLUSIVE = "EXCLUSIVE";
    public final static String TYPE_INSTANTIATE_PARALLEL = "PARALLEL";

    public EventBasedGateway() {
        super();
        initializeProperties();
    }

    public void initializeProperties() {
        setProperty(PROP_INSTANTIATE, TYPE_INSTANTIATE_NONE);
 //       String[] inst = { TYPE_INSTANTIATE_NONE , TYPE_INSTANTIATE_EXCLUSIVE, TYPE_INSTANTIATE_PARALLEL };
        //setPropertyEditor(PROP_INSTANTIATE, new ListSelectionPropertyEditor(inst));
    }

}
