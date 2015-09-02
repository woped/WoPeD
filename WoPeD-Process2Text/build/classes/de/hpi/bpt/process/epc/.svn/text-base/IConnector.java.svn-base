package de.hpi.bpt.process.epc;


/**
 * The connector in the process model. Connector is the flow object.
 * 
 * @author Artem Polyvyanyy
 */
public interface IConnector extends IFlowObject{	
	/**
	 * Returns the type of this connector: <code>AND</code>, <code>OR</code>, or <code>XOR</code>.
	 */
	ConnectorType getConnectorType();
	
	/**
	 * Define the connector type: <code>AND</code>, <code>OR</code>, or <code>XOR</code>.
	 */
	void setConnectorType(ConnectorType type);
	
	/**
	 * Check if connector is of type XOR
	 * @return <code>true</code> if connector is of type XOR, <code>false</code> otherwise
	 */
	boolean isXOR();
	
	/**
	 * Check if connector is of type AND
	 * @return <code>true</code> if connector is of type AND, <code>false</code> otherwise
	 */
	boolean isAND();
	
	/**
	 * Check if connector is of type OR
	 * @return <code>true</code> if connector is of type OR, <code>false</code> otherwise
	 */
	boolean isOR();
}