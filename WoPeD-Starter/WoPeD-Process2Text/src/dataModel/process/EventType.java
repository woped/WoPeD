package dataModel.process;

import java.util.HashMap;
import java.util.Map;

public class EventType {
	
	public static final int START_EVENT = 1;
	public static final int START_MSG = 2;
	
	public static final int END_EVENT = 11;
	public static final int END_ERROR = 12;
	public static final int END_SIGNAL = 13;
	
	public static final int INTM = 21;
	public static final int INTM_TIMER = 22;
	public static final int INTM_CANCEL = 23;
	public static final int INTM_CONDITIONAL = 24;
	public static final int INTM_ESCALATION = 25;
	public static final int INTM_ERROR = 26;
	
	public static final int INTM_ESCALATION_THR = 31;
	public static final int INTM_SIGNAL_THR = 32;
	public static final int INTM_MULTIPLE_THR = 33;
	public static final int INTM_LINK_THR = 34;
	public static final int INTM_MSG_THR = 35;
	
	public static final int INTM_ESCALATION_CAT = 41;
	public static final int INTM_MULTIPLE_CAT = 42;
	public static final int INTM_LINK_CAT = 43;
	public static final int INTM_MSG_CAT = 44;
	public static final int INTM_PMULT_CAT = 45;
	public static final int INTM_COMPENSATION_CAT = 46;
	
	public static boolean isEndEvent(int type) {
		return (type > 10 && type < 20);
	}
	
	public static boolean isStartEvent(int type) {
		return (type > 0 && type < 10);
	}
	
	
	public static final Map<String, Integer> TYPE_MAP = new HashMap<String ,Integer >(){
        {
            put("StartNoneEvent", 1);
            put("StartEvent", 1);
            put("StartMessageEvent", 2);
            
            put("EndNoneEvent", 11);
            put("EndEvent", 11);
            put("EndTerminateEvent",11);
            put("EndErrorEvent",12);
            put("EndSignalEvent",13);
            
            put("IntermediateEvent",21);
            put("IntermediateTimerEvent",22);
            put("IntermediateCancelEvent",23);
            put("IntermediateConditionalEvent",24);
            put("IntermediateEscalationEvent",25);
            put("IntermediateErrorEvent",26);
            
            put("IntermediateEscalationEventThrowing",31);
            put("IntermediateSignalEventThrowing",32);
            put("IntermediateMultipleEventThrowing",33);
            put("IntermediateLinkEventThrowing",34);
            put("IntermediateMessageEventThrowing",35);
            
            put("IntermediateSignalEventCatching",41);
            put("IntermediateMultipleEventCatching",42);
            put("IntermediateLinkEventCatching",43);
            put("IntermediateMessageEventCatching",44);
            put("IntermediateParallelMultipleEventCatching",45);
            put("IntermediateCompensationEventCatching",46);
        }
    };

}
