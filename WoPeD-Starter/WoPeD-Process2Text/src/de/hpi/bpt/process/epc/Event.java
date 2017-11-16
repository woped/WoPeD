package de.hpi.bpt.process.epc;

/**
 * EPC event implementation
 * 
 * @author Artem Polyvyanyy
 */
public class Event extends FlowObject implements IEvent {

	public Event() {
		super();
	}

	public Event(String name, String desc) {
		super(name, desc);
	}

	public Event(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.flow.FlowObject#getType()
	 */
	@Override
	public FlowObjectType getType() {
		return FlowObjectType.EVENT;
	}

}
