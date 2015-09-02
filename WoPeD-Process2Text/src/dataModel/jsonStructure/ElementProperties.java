package dataModel.jsonStructure;

public class ElementProperties {
	
	String name;
	String documentation;
	String auditing;
	String monitoring;
	String eventdefinitionref;
	String eventdefinitions;
	String dataoutputassociations;
	String dataoutput;
	String outputset;
	String bgcolor;
	String trigger;
	String frequency;
	String tasktype;
	
	public String getTasktype() {
		return tasktype;
	}
	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	boolean applyincalc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	public String getAuditing() {
		return auditing;
	}
	public void setAuditing(String auditing) {
		this.auditing = auditing;
	}
	public String getMonitoring() {
		return monitoring;
	}
	public void setMonitoring(String monitoring) {
		this.monitoring = monitoring;
	}
	public String getEventdefinitionref() {
		return eventdefinitionref;
	}
	public void setEventdefinitionref(String eventdefinitionref) {
		this.eventdefinitionref = eventdefinitionref;
	}
	public String getEventdefinitions() {
		return eventdefinitions;
	}
	public void setEventdefinitions(String eventdefinitions) {
		this.eventdefinitions = eventdefinitions;
	}
	public String getDataoutputassociations() {
		return dataoutputassociations;
	}
	public void setDataoutputassociations(String dataoutputassociations) {
		this.dataoutputassociations = dataoutputassociations;
	}
	public String getDataoutput() {
		return dataoutput;
	}
	public void setDataoutput(String dataoutput) {
		this.dataoutput = dataoutput;
	}
	public String getOutputset() {
		return outputset;
	}
	public void setOutputset(String outputset) {
		this.outputset = outputset;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public boolean isApplyincalc() {
		return applyincalc;
	}
	public void setApplyincalc(boolean applyincalc) {
		this.applyincalc = applyincalc;
	}

}
