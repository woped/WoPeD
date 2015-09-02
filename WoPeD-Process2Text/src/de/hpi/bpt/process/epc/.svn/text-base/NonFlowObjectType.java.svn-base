package de.hpi.bpt.process.epc;

/**
 * EPC non flow object types
 * @author Artem Polyvyanyy
 *
 */
public enum NonFlowObjectType {
	SYSTEM,
	DOCUMENT,
	APPLICATION_SYSTEM,
	ORGANIZATION,
	ROLE,
	ORGANIZATION_TYPE,
	UNDEFINED;
	
	/**
	 * Get a non flow object type
	 * @param obj Non flow object
	 * @return Type of the object
	 */
	public static NonFlowObjectType getType(INonFlowObject obj) {
		if (obj instanceof ISystem) {
			return NonFlowObjectType.SYSTEM;
		}
		else if (obj instanceof IDocument) {
			return NonFlowObjectType.DOCUMENT;
		}
		else if (obj instanceof IApplicationSystem) {
			return NonFlowObjectType.APPLICATION_SYSTEM;
		}
		else if (obj instanceof IOrganization) {
			return NonFlowObjectType.ORGANIZATION;
		}
		else if (obj instanceof IRole) {
			return NonFlowObjectType.ROLE;
		}
		else if (obj instanceof IOrganizationType) {
			return NonFlowObjectType.ORGANIZATION_TYPE;
		}
		
		return NonFlowObjectType.UNDEFINED;
	}
}
