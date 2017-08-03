package nodes;

import java.util.HashSet;
import java.util.Set;

import models.ProcessModel;


public abstract class ProcessObject extends SerializableProcessObject {
	
	 /**
     * Serialization properties
     */
    public final static String PROP_ID = "#id";
    public final static String PROP_CLASS_TYPE = "#type";
    /** Flag to hold selection status */
    private boolean selected = false;
    /** Stores the list of contexts for this ProcessNode, will not be serialized */
    protected Set<ProcessModel> contexts = new HashSet<ProcessModel>();

    /**
     * Default constructor. Needs always to be called!
     */
    public ProcessObject() {
        super.setProperty(PROP_ID, "" + this.hashCode());
        super.setProperty(PROP_CLASS_TYPE, this.getClass().getName());
    }
    
    /**
     * Tests if two ProcessObjects are equal based on their id. If the
     * argument obj is not an instance of ProcessObject, the super
     * function is called.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProcessObject) {
            ProcessObject po = (ProcessObject) obj;
            return (po.getId().equals(this.getId()));
        }

        return super.equals(obj);
    }

    /**
     * Sets a property.
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
          // Get old value
        String oldValue = getProperty(key);
            // Check if value really changed
            if ( (value != null && !value.equals(oldValue)) || (oldValue != null && value == null) )  {
                // Update value
                super.setProperty( key, value );
            }
        // Mark all containing contexts as dirty
        for (ProcessModel context : getContexts()) {
            context.markAsDirty(true);
        }
    }
    
    /** Returns the Id of this ProcessObject */
    public void setId(String id) {
        setProperty(PROP_ID, id);
    }

    /** Returns the Id of this ProcessObject */
    public String getId() {
        return getProperty(PROP_ID);
    }

    /** Returns the Name of this ProcessObject */
    public String getName() {
        return getProperty(PROP_ID);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * Adds a context (ProcessModel) to this ProcessObject. This method
     * must be called each time this object is added to a ProcessModel.
     * @param context
     */
    public void addContext(ProcessModel context) {
        contexts.add(context);
    }

    /**
     * Removes a context (ProcessModel) from this ProcessObject. This method
     * must be called each time this object is removed from a ProcessModel.
     * @param context
     */
    public void removeContext(ProcessModel context) {
        contexts.remove(context);
    }

    /**
     * Returns the set of the current ProcessObject's contexts (ProcessModels).
     * @return
     */
    public Set<ProcessModel> getContexts() {
        return contexts;
    }
    
	

}
