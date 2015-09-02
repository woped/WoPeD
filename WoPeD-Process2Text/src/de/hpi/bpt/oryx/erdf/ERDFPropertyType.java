package de.hpi.bpt.oryx.erdf;

/**
 * 
 * @author Artem Polyvyanyy
 *
 */
public enum ERDFPropertyType {
	ERDF_ORYX_TYPE("oryx-type");

	private final String name;
	
	/**
	 * Private constructor
	 * @param name Property name
	 */
	private ERDFPropertyType(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
