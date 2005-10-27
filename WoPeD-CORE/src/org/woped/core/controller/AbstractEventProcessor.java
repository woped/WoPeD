package org.woped.core.controller;



public abstract class AbstractEventProcessor
{
    AbstractApplicationMediator mediator = null;
    private int         vepId    = -1;

    /**
     * Constructor for the <code>AbstractVEP</code> stores the
     * <code>AtelosApplicationMediator</code>.
     * 
     * @param mediator
     */
    public AbstractEventProcessor(int vepID, AbstractApplicationMediator mediator)
    {
        this.mediator = mediator;
        this.vepId = vepID;
    }

    /**
     * @return
     */
    public AbstractApplicationMediator getMediator()
    {
        return mediator;
    }

    public abstract void processViewEvent(AbstractViewEvent event);

    /**
     * @return
     */
    public int getVepId()
    {
        return vepId;
    }

}
