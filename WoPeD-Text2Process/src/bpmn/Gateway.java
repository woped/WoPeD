package bpmn;

import java.util.LinkedList;
import java.util.List;

import epc.SequenceFlow;
import nodes.FlowObject;
import nodes.ProcessNode;

public class Gateway extends FlowObject {
	
	 public Gateway() {
	        super();
	    }

	 public Gateway(int x, int y, String text) {
	        super();
	        setText(text);
	    }

	 @Override
	    public void setProperty(String key, String value) {
	        super.setProperty(key, value);
	    }
	 
	 public String getType() {
	        return getProperty(PROP_CLASS_TYPE);
	    }

	    public void setType(String type) {
	        setProperty(PROP_CLASS_TYPE, type);
	    }
	    
	    @Override
	    public List<Class<? extends ProcessNode>> getVariants() {
	        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
	        result.add(Gateway.class);
	        result.add(ExclusiveGateway.class);
	        result.add(EventBasedGateway.class);
	        result.add(InclusiveGateway.class);
	        result.add(ComplexGateway.class);
	        result.add(ParallelGateway.class);
	        return result;
	    }

	    @Override
	    public String toString() {
	        return "BPMN Gateway ("+getText()+")";
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
