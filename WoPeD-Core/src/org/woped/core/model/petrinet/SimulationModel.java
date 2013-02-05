package org.woped.core.model.petrinet;

import java.util.Date;
import java.util.Vector;

public class SimulationModel
{
	private String id = null;
	private String name = null;
	private String fingerprint = null;
	private Vector<TransitionModel> occuredTransitions = null;
	private Date savedDate = null;

	public SimulationModel(String id, String name)
	{
		this.name = name;
		this.id = id;
		occuredTransitions = new Vector<TransitionModel>();
	}

	/*
	 * Constructor to hand over an Existing Vector from HistoryBox
	 */
	public SimulationModel(String id, String name, Vector<TransitionModel>HistoryVector, String fingerprint, Date creationDate)
	{
		this.name = name;
		this.id = id;
		this.savedDate = creationDate;
		this.fingerprint = fingerprint;
		occuredTransitions = HistoryVector;
	}

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the vector with the fired transitions
     */
    public Vector<TransitionModel> getOccuredTransitions()
    {
    	return occuredTransitions;
    }

    /**
     * This method is needed to make it possible that Simulations may be overwritten with other content
     * @param HistoryVector
     */
    public void setOccuredTransitions(Vector<TransitionModel> HistoryVector)
    {
    	occuredTransitions = HistoryVector;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }

    /**
     * @return Returns the fingerprint of the petrinet the simulation was created with.
     */
    public String getFingerprint()
    {
    	return fingerprint;
    }

    /**
     * @param fingerprint
     *            The fingerprint to set.
     */
    public void setFingerprint(String fingerprint)
    {
    	this.fingerprint = fingerprint;
    }

	public String getId()
	{
		return id;
	}
	
    /**
     * @return Returns the date of creation.
     */
    public Date getSavedDate()
    {
        return savedDate;
    }

    /**
     * @param creationDate
     *            The date of creation to set.
     */
    public void setSavedDate(Date savedDate)
    {
        this.savedDate = savedDate;
    }

}
