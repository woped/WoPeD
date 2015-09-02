package de.hpi.bpt.process.epc;

/**
 * EPC system implementation
 * @author Artem Polyvyanyy
 *
 */
public class System extends NonFlowObject implements ISystem {

	public System() {
		super();
	}

	public System(String name, String desc) {
		super(name, desc);
	}

	public System(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.meta.NonFlowObject#getType()
	 */
	@Override
	public NonFlowObjectType getType() {
		return NonFlowObjectType.SYSTEM;
	}
}