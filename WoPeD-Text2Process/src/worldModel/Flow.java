/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import java.util.ArrayList;
import java.util.List;

import text.T2PSentence;

public class Flow extends OriginatedElement{
	
	private List<Action> f_multiples = new ArrayList<Action>();
	private Action f_single = null;
	
	private FlowDirection f_direction = FlowDirection.split;
	
	public enum FlowDirection{
		split,
		join
	};
	
	public enum FlowType {
		concurrency,
		sequence,
		iteration,
		choice,
		multiChoice,
		exception
	};
	
	private FlowType f_type;	
	
	/**
	 * 
	 */
	public Flow(T2PSentence sentence) {
		super(sentence);
		f_type = FlowType.sequence;
	}
	
	public FlowType getType() {
		return f_type;
	}
	
	public void setType(FlowType type) {
		f_type = type;
	}

	public void setSingleObject(Action f_single) {
		this.f_single = f_single;
	}

	public Action getSingleObject() {
		return f_single;
	}

	public void setMultipleObjects(List<Action> f_multiples) {
		this.f_multiples = f_multiples;
	}

	public List<Action> getMultipleObjects() {
		return f_multiples;
	}

	public void setDirection(FlowDirection f_direction) {
		this.f_direction = f_direction;
	}

	public FlowDirection getDirection() {
		return f_direction;
	}
	
	@Override
	public String toString() {
		if(f_direction == FlowDirection.split) {
			StringBuilder _b = new StringBuilder("Flow from "+f_single +" to ");
			for(Action a:getMultipleObjects()) {
				if(getMultipleObjects().size() > 1) {
					_b.append("\n- ");
				}
				_b.append(a);
			}
			return _b.toString();
		}
		StringBuilder _b = new StringBuilder("Flow from");
		for(Action a:getMultipleObjects()) {
			_b.append("\n- " + a);
		}
		_b.append(" to "+f_single);
		return _b.toString();
	}
	
	
	
}
