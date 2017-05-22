package epc;

import java.util.LinkedList;

import nodes.Linkable;
import tools.ReferenceChooserRestriction;

public class Organisation extends OrganisationCluster implements Linkable{

	private OrganisationCluster parent;

    /**
     * needed for deserialization
     */
    public Organisation() {
        this("", 100, null);
    }

    /**
     * @param string
     * @param integer
     */
    public Organisation(String name, Integer size, OrganisationCluster parent) {
    	init();
        setText(name);
        this.parent = parent;
    }

    @Override
    public void setProperty(String key, String value) {
    	super.setProperty(key, value);
    }
    
    public OrganisationCluster getParent() {
        return parent;
    }

    public OrgCollection getSurroundingOrgCollection() {
        if ( this.getParent() != null ) {
            if ( this.getParent() instanceof OrgCollection )
                return (OrgCollection) this.getParent();
            else if ( this.getParent() instanceof Organisation )
                return ((Organisation) this.getParent()).getSurroundingOrgCollection();
        }
        
        return null;
    }
    
    /**
     * switches the parent of this Lane.
     * The old and new parent will get notified of the change
     * @param parent
     */
    public void setParent(OrganisationCluster parent) {
        this.parent = parent;
    }

	@Override
	public ReferenceChooserRestriction getReferenceRestrictions() {
		throw new IllegalArgumentException("not implemented yet!");
	}
	
}
