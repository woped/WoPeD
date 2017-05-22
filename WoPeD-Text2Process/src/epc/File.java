package epc;

import java.util.LinkedList;

import nodes.Linkable;
import tools.ReferenceChooserRestriction;
import epc.Artifact;

public class File extends Artifact implements Linkable {

	/** Property if this Data Object is a collection (0=FALSE, 1=TRUE) */
    public final static String PROP_COLLECTION = "collection";
    /** Property if this Data Object is Input or Output) */
    public final static String PROP_DATA = "data";
    /** Property to hold the state of the DataObject */
    public final static String PROP_STATE = "state";
    /** DataObject type is not input/output */
    public final static String DATA_NONE = "";
    /** DataObject type is Input */
    public final static String DATA_INPUT = "INPUT";
    /** DataObject type is Output */
    public final static String DATA_OUTPUT = "OUTPUT";
    
    public File() {
        super();
        initializeProperties();
    }

    public File( String text) {
        super();
        setText(text);
        initializeProperties();
    }

    private void initializeProperties() {
        setProperty(PROP_COLLECTION, null);
        setProperty(PROP_DATA, DATA_NONE);

        setProperty(PROP_STATE, "");
    }

    public String getState() {
        return getProperty(PROP_STATE);
    }

    public void setState(String state) {
        setProperty(PROP_STATE, state);
    }

    @Override
    public String toString() {
        return "EPC File ("+getText()+")";
    }

	@Override
	public ReferenceChooserRestriction getReferenceRestrictions() {
		throw new IllegalArgumentException("not implemented yet!");
	}
	
}
