package de.hpi.bpt.hypergraph.abs;

/**
 * Basic graph object interface
 * 
 * @author Artem Polyvyanyy
 */
public interface IGObject extends Comparable<IGObject> {
	/**
	 * Get unique identifier
	 * @return
	 */
	public String getId();

	/**
	 * Set unique identifier
	 * @param id Unique identifier
	 */
	public void setId(String id);
	
	/**
	 * Get graph object associated tag object
	 * @return Tag object
	 */
	public Object getTag();
	
	/**
	 * Set graph object associated tag object
	 * @param tag Tag object to set
	 */
	public void setTag(Object tag);
	
	/**
	 * Get name
	 * @return Name string
	 */
	public String getName();
	
	/**
	 * Set name
	 * @param name Name to set
	 */
	public void setName(String name);

	/**
	 * Get description
	 * @return Description string
	 */
	public String getDescription();
	
	/**
	 * Set description
	 * @param desc Description to set
	 */
	public void setDescription(String desc);
}
