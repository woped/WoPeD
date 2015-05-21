package org.woped.file.apromore.worker.elements;

import java.util.List;

import org.apromore.model.FolderType;

public class ReturnElement {
	private String[][] data;
	private List<FolderType> folders;
	private boolean serverAviable;

	public ReturnElement(String[][] data, List<FolderType> folders,
			boolean serverAviable) {
		this.data = data;
		this.folders = folders;
		this.serverAviable = serverAviable;
	}

	public List<FolderType> getFolders() {
		return folders;
	}

	public String[][] getData() {
		return data;
	}

	public boolean getServerAviable() {
		return serverAviable;
	}
}