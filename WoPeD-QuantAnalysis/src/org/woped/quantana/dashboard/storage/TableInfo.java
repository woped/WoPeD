package org.woped.quantana.dashboard.storage;

public class TableInfo {
	private String TABLENAME = "";
	private String TABLETYPE = "";
	private String TABLEID = "";
	
	public TableInfo(String TABLENAME, String TABLETYPE, String TABLEID){
		
		this.TABLENAME = TABLENAME;
		this.TABLETYPE = TABLETYPE;
		this.TABLEID = TABLEID;
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
