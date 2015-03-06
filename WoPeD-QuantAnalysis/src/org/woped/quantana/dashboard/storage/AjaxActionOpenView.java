package org.woped.quantana.dashboard.storage;


public class AjaxActionOpenView extends AjaxAction {
	
	//public String action = "";
	private Parameter parameter = null;
	
	
	public class Parameter{
		public String UserAgent = null;
	}
	
	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	} 
}