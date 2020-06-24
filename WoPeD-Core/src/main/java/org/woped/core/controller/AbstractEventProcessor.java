package org.woped.core.controller;

public abstract class AbstractEventProcessor {
    AbstractApplicationMediator mediator = null;

    /**
     * Constructor for the <code>AbstractVEP</code> stores the
     * <code>AtelosApplicationMediator</code>.
     *
     * @param mediator
     */
    public AbstractEventProcessor(AbstractApplicationMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * @return
     */
    public AbstractApplicationMediator getMediator() {
        return mediator;
    }

    public abstract void processViewEvent(AbstractViewEvent event);
}
