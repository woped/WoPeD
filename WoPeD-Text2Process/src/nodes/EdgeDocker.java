package nodes;

import epc.SequenceFlow;

public class EdgeDocker extends ProcessNode {

    public final static String PROP_DOCKED_EDGE = "#docked_edge";

    private ProcessEdge dockedEdge;

    public EdgeDocker() {
        initializeProperties();
    }

    public EdgeDocker(ProcessEdge edge) {
        initializeProperties();
        setDockedEdge(edge);
    }

    private void initializeProperties() {
        this.setProperty(PROP_DOCKED_EDGE, "null");
    }

    public void setDockedEdge(ProcessEdge edge) {
        dockedEdge = edge;
        if(dockedEdge != null)
        this.setProperty(PROP_DOCKED_EDGE, dockedEdge.getProperty(PROP_ID));
    }

    public ProcessEdge getDockedEdge() {
        return dockedEdge;
    }
    
    @Override
    public String toString() {
    	return "EdgeDocker ("+dockedEdge+")";
    }

	@Override
	public void setIncoming(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

	@Override
	public void setOutgoing(SequenceFlow flow) {
		throw new IllegalArgumentException("not implemented yet!");
	}

}
