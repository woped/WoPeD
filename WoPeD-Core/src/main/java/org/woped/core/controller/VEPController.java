package org.woped.core.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.woped.core.Constants;
import org.woped.core.utilities.LoggerManager;

/**
 * The VEPController is a registry for {@link AbstractEventProcessor}s.
 * <p>
 * It offers the functionality to register/unregister event processors which are interested in view events of a
 * specific type.
 */
public class VEPController {

    private Map<Integer, Collection<AbstractEventProcessor>> processorMap;

    /**
     * Constructor for the <code>VEPController</code>.
     */
    public VEPController() {
        processorMap = new HashMap<>();
    }

    /**
     * Registers a <code>ViewEventProcessor</code> to receive view events of the specified type.
     *
     * @param viewEventType the type of view events
     * @param vep           the view event processor which wants to receive events of the specified type
     */
    public void register(int viewEventType, AbstractEventProcessor vep) {

        Collection<AbstractEventProcessor> processors = processorMap.get(viewEventType);
        if (processors == null) {
            processors = new LinkedList<>();
            processorMap.put(viewEventType, processors);
        }

        processors.add(vep);
    }

    /**
     * Unregisters a <code>ViewEventProcessor</code> for the specified view event type.
     * <p>
     * If the processor is not registered for this event, a warning will be logged.
     *
     * @param viewEventType the type of the view event
     * @param vep           the view event processor to unregister
     */
    public void unregister(int viewEventType, AbstractEventProcessor vep) {
        Collection<AbstractEventProcessor> processors = processorMap.get(viewEventType);

        if (processors == null || !processors.contains(vep)) {
            LoggerManager.warn(Constants.CORE_LOGGER, "Tried to unregister a AbstractViewEventProcessor which has not" +
                    " registered for this type.");
            return;
        }

        processors.remove(vep);
    }

    /**
     * Gets the ViewEventProcessors which are registered for the specified type.
     *
     * @param viewEventType the type of the view event
     * @return a collection of all registered view event processors for the specified type or an empty collection
     * if no processors are registered for the specified type
     */
    public Collection<AbstractEventProcessor> getViewEventProcessorsForType(int viewEventType) {
        LinkedList<AbstractEventProcessor> processors = new LinkedList<>();

        if(processorMap.containsKey(viewEventType)){
            processors.addAll(processorMap.get(viewEventType));
        }

        return processors;
    }
}
