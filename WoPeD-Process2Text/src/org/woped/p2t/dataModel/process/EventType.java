package org.woped.p2t.dataModel.process;

public class EventType {
    public static final int START_EVENT = 1;
    public static final int START_MSG = 2;

    public static final int END_EVENT = 11;
    public static final int END_ERROR = 12;
    public static final int END_SIGNAL = 13;

    public static final int INTM_TIMER = 22;
    public static final int INTM_ERROR = 26;

    public static final int INTM_ESCALATION_THR = 31;
    public static final int INTM_SIGNAL_THR = 32;
    public static final int INTM_MULTIPLE_THR = 33;
    public static final int INTM_LINK_THR = 34;
    public static final int INTM_MSG_THR = 35;

    public static final int INTM_ESCALATION_CAT = 41;
    public static final int INTM_MSG_CAT = 44;
}