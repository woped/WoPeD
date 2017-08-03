package bpmn;

import nodes.ProcessEdge;
import nodes.ProcessNode;

public class ConversationLink extends ProcessEdge{
	
	 /** The fork status of this edge (0,1) */
    public final String PROP_FORK = "fork";
    
    public ConversationLink() {
        super();
        initializeProperties();
    }

    public ConversationLink(ProcessNode source, ProcessNode target) {
        super();
        initializeProperties();
    }

    private void initializeProperties() {
        setProperty(PROP_FORK, "FALSE");
    }

}
