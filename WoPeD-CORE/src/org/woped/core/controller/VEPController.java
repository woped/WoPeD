package org.woped.core.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lai@kybeidos.de">Simon Landes</a>
 * 
 * The <code>VEPController</code> stores all
 * <code>AbstractEventProcessor</code>-objects and offers the access to each
 * processor with the special major-code.
 * 
 */
public class VEPController
{

    private Map veps;

    /**
     * Constructor for the <code>VEPController</code>.
     */
    public VEPController()
    {}

    /**
     * Registers a <code>ViewEventProcessor</code>.
     * 
     * @param major
     * @param vep
     */
    public void register(int vepId, AbstractEventProcessor vep)
    {
        getVeps().put(new Integer(vepId), vep);
    }

    /**
     * Unregisters a <code>ViewEventProcessor</code>.
     * 
     * @param major
     */
    public void unregister(int vepId)
    {
        getVeps().remove(new Integer(vepId));
    }

    /**
     * Searches a <code>ViewEventProcessor</code>.
     * 
     * @param major
     * @return
     */
    public AbstractEventProcessor lookup(int vepId)
    {
        return (AbstractEventProcessor) getVeps().get(new Integer(vepId));
    }

    private Map getVeps()
    {
        if (veps == null) veps = new HashMap();
        return veps;
    }

}
