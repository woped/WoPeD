package bpmn;

import java.util.LinkedList;
import java.util.List;

import epc.SequenceFlow;
import nodes.ProcessNode;

public class EndEvent extends Event {

    public EndEvent() {
        super();
    }
    
    public EndEvent(int x, int y, String label) {
        super();
        setText(label); 
    }



    @Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(EndEvent.class);
        //result.add(MessageEndEvent.class);
        //result.add(ErrorEndEvent.class);
        //result.add(EscalationEndEvent.class);
        //result.add(CancelEndEvent.class);
        //result.add(CompensationEndEvent.class);
        //result.add(SignalEndEvent.class);
        result.add(TerminateEndEvent.class);
        //result.add(MultipleEndEvent.class);
        return result;
    }
    
    @Override
    public String toString() {
        return "End Event ("+getText()+")";
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

