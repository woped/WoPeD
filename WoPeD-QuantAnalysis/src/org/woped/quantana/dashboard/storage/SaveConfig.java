package org.woped.quantana.dashboard.storage;

public class SaveConfig extends AjaxAction{
	
	private Parameter[] parameter = null;
	

	public Parameter[] getParameter() {
		return parameter;
	}

	public void setConfiguration(Parameter[] param) {
		this.parameter = param;
	}
	
	public class Parameter {
		public int row = 0;
		public int col = 0;
		public int size_x = 0;
		public int size_y = 0;
	}

}
