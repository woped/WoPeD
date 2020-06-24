package org.woped.core.model.bpel;

import javax.xml.namespace.QName;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

public class BpelVariable {

	private TVariable _data = null;
	public static String STANDARDXMLSCHEMALINK = "http://www.w3.org/2001/XMLSchema";

	/**
	 * 
	 */
	public BpelVariable() {
		this._data = this.genTVariable();
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public BpelVariable(String name, String type) {
		this();
		this._data.setName(name);
		this._data.setType(new QName(BpelVariable.STANDARDXMLSCHEMALINK, type));

	}

	/**
	 * 
	 * @param name
	 * @param namespace
	 * @param type
	 */
	public BpelVariable(String name, String namespace, String type) {
		this();
		this._data.setName(name);
		this._data.setMessageType(new QName(namespace, type));
	}

	/**
	 * 
	 * @param arg
	 */
	public BpelVariable(TVariable arg) {
		this();
		this._data.set(arg);
	}

	/**
	 * 
	 * @return
	 */
	private TVariable genTVariable() {
		return TVariable.Factory.newInstance();
	}

	/**
	 * Returns a array (type=String) of possible basictypes for BPEL-variables
	 * method is used to create drop-down list in GUI
	 * 
	 * @return
	 */
	public static String[] getTypes() {
		String[] list = { "String", "normalizedString", "token", "byte",
				"unsignedByte", "base64Binary", "hexBinary", "integer",
				"positiveInteger", "negativeInteger", "nonNegativeInteger",
				"nonPositiveInteger", "int", "unsignedInt", "long",
				"unsignedLong", "short", "unsignedShort", "decimal", "float",
				"double", "boolean", "time", "dateTime", "duration", "date",
				"gMonth", "gYear", "gYearMonth", "gDay", "gMonthDay", "Name",
				"QName", "NCName", "anyURI", "language" };
		return list;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this._data.getName();
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return this._data.getMessageType().getLocalPart();
	}

	/**
	 * 
	 * @return
	 */
	public String toString() {
		return this.getName();
	}

	/**
	 * 
	 * @return
	 */
	public boolean equals(Object obj) {
		if (!BpelVariable.class.isInstance(obj))
			return false;
		if (this.getName().compareToIgnoreCase(((BpelVariable) obj).getName()) != 0)
			return false;
		return true;
	}

	/**
	 * 
	 * @param Name
	 * @return
	 */
	public boolean equalByName(String Name) {
		if (this.getName().compareToIgnoreCase(Name) == 0)
			return true;
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public TVariable getBpelCode() {
		return this._data;
	}

}
