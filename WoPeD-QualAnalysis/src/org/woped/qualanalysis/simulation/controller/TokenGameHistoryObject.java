package org.woped.qualanalysis.simulation.controller;

public class TokenGameHistoryObject
{

	private String[]  HistoryItems = null;
	private String    HistoryName  = null;
	
	public TokenGameHistoryObject()
	{
		
	}
	
	public TokenGameHistoryObject(String[] Items)
	{
		HistoryItems = Items;
	}
	
	public void setHistoryName(String Name)
	{
		HistoryName = Name;
	}
	
	public String[] getHistoryItems()
	{
		return HistoryItems;
	}
}
