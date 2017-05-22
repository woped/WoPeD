package bpmn;

import nodes.FlowObject;

public abstract class Event extends FlowObject{
	
    public Event() {
        super();
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
    }
    public String toString() {
        return "BPMN Event (" + getText() + ")";
    }

}
