package org.woped.core.model.petrinet;

import java.util.Vector;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;

public class SimulationModel extends PetriNetModelElement 
{
	private String name = null;
	private Vector<TransitionModel> firedTransitions = null;
	
	public SimulationModel(String name)
	{
		super(null); //TODO: Check what the superconstructor needs as parameter "CreationMap"
		this.name = name;
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
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }

	@Override
	public int getType() {
		return AbstractPetriNetModelElement.SIMULATION_TYPE;
	}
	
	/*
	 * the following two Methods are inherited but not used for simulations, because they aren't
	 * shown as graphical Objects like e.g. tranisitions
	 * (non-Javadoc)
	 * @see org.woped.core.model.AbstractElementModel#getDefaultHeight()
	 */
	@Override
	public int getDefaultHeight() {
		return 0;
	}
	@Override
	public int getDefaultWidth() {
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.woped.core.model.AbstractElementModel#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "ID: "+getId()+"\nName: "+getName(); //TODO port to use Messages
	}
}
