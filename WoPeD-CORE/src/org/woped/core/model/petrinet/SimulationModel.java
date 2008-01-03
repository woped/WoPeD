package org.woped.core.model.petrinet;

import java.util.Vector;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;

public class SimulationModel  
{
	private String id = null;
	private String name = null;
	private Vector<TransitionModel> firedTransitions = null;
	//private ??? nethash  TODO: an anderer Stelle einen Algorithmus für einen Hash über das (logische) Netz schreiben und hier einen passenden Datentyp/Methodenn dazu einfügen
	
	public SimulationModel(String id, String name)
	{
		this.name = name;
		this.id = id;
		firedTransitions = new Vector<TransitionModel>();
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
     * @return Returns the vector with the fired tranisitions
     */
    public Vector<TransitionModel> getFiredTransitions()
    {
    	return firedTransitions;
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

	/*
	public int getType() {
		return AbstractPetriNetModelElement.SIMULATION_TYPE;
	}
	

	public String getToolTipText() 
	{
		return "ID: "+getId()+"\nName: "+getName(); //TODO port to use Messages
	}*/
	
	public String getId()
	{
		return id;
	}
	
}
