package org.woped.core.model.bpel;

/*
 * @author: Alexander Roßwog
 */

public class UddiVariable 
{
	private String name;
	private String URL;
	public UddiVariable(String name, String URL)
	{
		this.name = name;
		this.URL = URL;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getURL()
	{
		return URL;
	}
	
	public boolean equalByName(String name)
	{
		boolean equal = false;
		if(this.name.equalsIgnoreCase(name))
		{
			equal = true;
		}
		return equal;
	}
}
