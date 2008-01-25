package org.woped.server.holder;

public class UserHolder {

	static private int userID = -1;
	static private int sampleID = 1;
	
	static public int getUserID() {
		return userID;
	}
	
	static public void setUserID(int userID) {
		UserHolder.userID = userID;
	}
	
	static public int getSampleID() {
		return sampleID;
	}
	
}
