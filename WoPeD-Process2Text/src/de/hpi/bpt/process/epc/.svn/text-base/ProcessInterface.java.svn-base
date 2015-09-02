package de.hpi.bpt.process.epc;


/**
 * EPC process interface implementation
 * @author Artem Polyvyanyy
 *
 */
public class ProcessInterface extends FlowObject implements IProcessInterface {
	
	@SuppressWarnings("unchecked")
	private IEPC epc = null;

	public ProcessInterface() {
		super();
	}

	public ProcessInterface(String name, String desc) {
		super(name, desc);
	}

	public ProcessInterface(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.flow.FlowObject#getType()
	 */
	@Override
	public FlowObjectType getType() {
		return FlowObjectType.PROCESS_INTERFACE;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.flow.IProcessInterface#getProcess()
	 */
	@SuppressWarnings("unchecked")
	public IEPC getProcess() {
		return epc;
	}

	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.process.epc.flow.IProcessInterface#setProcess(de.hpi.bpt.process.epc.IEPC)
	 */
	@SuppressWarnings("unchecked")
	public void setProcess(IEPC epc) {
		this.epc = epc;
	}
}
