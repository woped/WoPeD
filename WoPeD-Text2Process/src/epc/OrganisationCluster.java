package epc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nodes.Cluster;
import nodes.ProcessNode;

public class OrganisationCluster extends Cluster{
	
	public List<Organisation> f_orgs = new ArrayList<Organisation>();
	
	protected void init() {
		//do nothing
    }
    
    public void addOrganisation(Organisation org) {
        if (!f_orgs.contains(org)) {
            f_orgs.add(org);
            addProcessNode(org);
        }
    }

    public void removeOrganisation(Organisation org) {
        f_orgs.remove(org);
        removeProcessNode(org);
    }

    public List<Organisation> getOrganisatons() {
        return f_orgs;
    }

    public Set<Organisation> getOrganisationsRecursively() {
        Set<Organisation> orgs = new HashSet<Organisation>( this.f_orgs );

        for ( Organisation org : this.f_orgs )
            orgs.addAll( org.getOrganisationsRecursively() );

        return orgs;
    }
    
    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        // Check if key=PROP_CONTAINED_NODES
        if (key.equals(PROP_CONTAINED_NODES)) {
            // reestablishing connection with lanes
            ArrayList<ProcessNode> _nodes = new ArrayList<ProcessNode>(getProcessNodes());
            if (_nodes != null) {
                if (f_orgs != null) {
                    f_orgs.clear();
                }
                for (ProcessNode pn : _nodes) {
                    if (pn instanceof Organisation) {
                        if (!f_orgs.contains(pn)) {
                            ((Organisation) pn).setParent(this);
                            addOrganisation((Organisation) pn);
                        }
                    }else if(pn instanceof OrgCollection) {//this should not happen...
                    	this.removeProcessNode(pn);
                    }
                }
            }
        }
    }

	@Override
	public void setIncoming(SequenceFlow flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutgoing(SequenceFlow flow) {
		// TODO Auto-generated method stub
		
	}

}
