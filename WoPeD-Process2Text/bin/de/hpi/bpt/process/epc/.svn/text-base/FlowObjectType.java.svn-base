package de.hpi.bpt.process.epc;

/**
 * EPC flow object types
 * 
 * @author Artem Polyvyanyy
 */
public enum FlowObjectType {
	FUNCTION,
	EVENT,
	CONNECTOR,
	PROCESS_INTERFACE,
	UNDEFINED;
	
	/**
	 * Get a flow object type
	 * @param obj Flow object
	 * @return Type of the object
	 */
	public static FlowObjectType getType(IFlowObject obj) {
		if (obj instanceof IFunction) {
			return FlowObjectType.FUNCTION;
		}
		else if (obj instanceof IEvent) {
			return FlowObjectType.EVENT;
		}
		else if (obj instanceof IConnector) {
			return FlowObjectType.CONNECTOR;
		}
		else if (obj instanceof IProcessInterface) {
			return FlowObjectType.PROCESS_INTERFACE;
		}
		
		return FlowObjectType.UNDEFINED;
	}
	
}
