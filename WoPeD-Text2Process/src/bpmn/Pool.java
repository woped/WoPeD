package bpmn;

import epc.SequenceFlow;

public class Pool extends LaneableCluster {

	/** Property if the Pool is a Black Box Pool */
    public final static String PROP_BLACKBOX_POOL = "blackbox_pool";
	 /** Property if this Pool has multiple instances (0=FALSE, 1=TRUE) */
    public final static String PROP_MULTI_INSTANCE = "multi_instance";
   
    public Pool() {
        super();
        init();
        setText("Pool");
    }

    public Pool(int x, int y, String label) {
        super();
        init();
        setText(label);
    }

    @Override
    protected void init() {
    	super.init();
        setProperty(PROP_BLACKBOX_POOL, "FALSE");
        //setPropertyEditor(PROP_BLACKBOX_POOL, new BooleanPropertyEditor());
        setProperty(PROP_MULTI_INSTANCE, "FALSE");
        //setPropertyEditor(PROP_MULTI_INSTANCE, new BooleanPropertyEditor());
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
    }
    
    @Override
    public boolean isCollapsed() {
        return "TRUE".equalsIgnoreCase(getProperty(PROP_BLACKBOX_POOL));
    }

    public String toString() {
        return "BPMN Pool ("+getName()+")";
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
