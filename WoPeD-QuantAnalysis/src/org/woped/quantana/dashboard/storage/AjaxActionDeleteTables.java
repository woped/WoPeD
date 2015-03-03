package org.woped.quantana.dashboard.storage;

public class AjaxActionDeleteTables extends AjaxAction {
	
	//public String action = "";
	private Parameter[] parameter = null;
	
	
	public class Parameter{
		public String ID = null;
	}
	
	public Parameter[] getParameter() {
		return parameter;
	}

	public void setParameter(Parameter[] parameter) {
		this.parameter = parameter;
	} 
}
