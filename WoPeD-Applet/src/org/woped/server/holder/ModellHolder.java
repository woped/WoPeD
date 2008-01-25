package org.woped.server.holder;

import java.io.Serializable;

public class ModellHolder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034091876737276818L;
	
	
	private int modellID = -1;
	private String title = "";
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ModellHolder() {
		
	}
	
	public ModellHolder(int modellID, String title) {
		this.title = title;
		this.modellID = modellID;
	}

	public int getModellID() {
		return modellID;
	}

	public void setModellID(int modellID) {
		this.modellID = modellID;
	}
	
	public String toString() {
		return title;
	}
	
	
}
