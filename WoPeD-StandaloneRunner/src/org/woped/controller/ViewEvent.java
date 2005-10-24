package org.woped.controller;

public class ViewEvent extends AbstractViewEvent
{
    public static final int VIEWEVENTTYPE_GUI = 3;

    public ViewEvent(Object source, int type, int order, Object data)
    {
        super(source, type, order, data);
    }

    public ViewEvent(Object source, int type, int order)
    {
        super(source, type, order);
    }
}
