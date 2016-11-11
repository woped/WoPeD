package org.woped.qualanalysis.soundness.datamodel;

import java.util.Observable;

/**
 * This class is in charge of updating registered observers if a new node has been added.
 */
public interface INotifyNodeAdded {

    /**
     * The observable object which notifies all registered listeners if a connection has been added.
     * <p>
     * The update method of the observer will receive an {@link NodeAddedArgs} object containing the information about the added connection.
     *
     * @return the observable object which notifies the listeners if a connection has been added.
     */
    Observable nodeAdded();
}
