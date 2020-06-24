package org.woped.quantana.dashboard.storage;

public class TableInfo {
	private String TABLENAME = "";
	private String TABLETYPE = "";
	private String TABLEID = "";
	private String DESCRIPTION = "";
	
	public TableInfo(String TABLENAME, String TABLETYPE, String TABLEID, String DESCRIPTION){
		
		this.TABLENAME = TABLENAME;
		this.TABLETYPE = TABLETYPE;
		this.TABLEID = TABLEID;
		this.DESCRIPTION = DESCRIPTION;
	}
	
	public String getTABLENAME() {
		return TABLENAME;
	}

	public String getTABLETYPE() {
		return TABLETYPE;
	}

	public String getTABLEID() {
		return TABLEID;
	}
	
	
}
