package dataModel.jsonStructure;

public class LaneProperties {
	
	String name;
	String documentation;
	String auditing;
	String monitoring;
	String parentpool;
	String parentlane;
	boolean showcaption;
	String bgcolor;
	
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
	public String getParentpool() {
		return parentpool;
	}
	public void setParentpool(String parentpool) {
		this.parentpool = parentpool;
	}
	public String getParentlane() {
		return parentlane;
	}
	public void setParentlane(String parentlane) {
		this.parentlane = parentlane;
	}
	public boolean isShowcaption() {
		return showcaption;
	}
	public void setShowcaption(boolean showcaption) {
		this.showcaption = showcaption;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	
	
}
