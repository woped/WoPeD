package nodes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import models.ProcessModel;

public abstract class ProcessNode extends ProcessObject {
	
	 public final static String TAG_NODE = "node";
	 
	 public final static String PROP_TEXT = "text";
	 public final static String PROP_LABEL = "label";
	 public final static String PROP_STEREOTYPE = "stereotype";
	 
	 public ProcessNode() {
	    	super();
	        setProperty(PROP_TEXT, "");
	        setProperty(PROP_TEXT, "");
	        setProperty(PROP_STEREOTYPE, "");
	    }
	
	 public String getText() {
	        return this.getProperty(PROP_TEXT);
	    }

	    public void setText(String text) {
	        this.setProperty(PROP_TEXT, text);
	    }

	    public String getStereotype() {
	        return getProperty(PROP_STEREOTYPE);
	    }

	    public void setStereotype(String stereotype) {
	        setProperty(PROP_STEREOTYPE, stereotype);
	    }
	    
	    @Override
	    protected String getXmlTag() {
	    	return TAG_NODE;
	    }
	    
	    public List<Class<? extends ProcessNode>> getVariants() {
	        return new LinkedList<Class<? extends ProcessNode>>();
	    }
	    
	    @Override
	    public String getName() {
	        return getText();
	    }


	           
	    public String toString() {
	        return "ProcessNode ("+getText()+")";
	    }
	    
	    @Override
	    public void setProperty(String key, String value) {
	        super.setProperty(key, value);
	    }
	    
	    /**
	     * Returns if this node is an instance of a subclass of Cluster
	     * @return
	     */
	    public boolean isCluster() {
	        return false;
	    }
	    
	    /*
	     * returns all clusters in which this node is contained
	     */
	    public Set<Cluster> getParentClusters() {
	        Set<Cluster> result = new HashSet<Cluster>(1);
	        Cluster c;
	        for (ProcessModel m: getContexts()) {
	            c = m.getClusterForNode(this);
	            if (c!=null) result.add(c);
	        }
	        return result;
	    }
}
