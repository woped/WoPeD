package org.woped.core.controller;

public interface IViewController
{

    public String getId();

    public void addViewListener(IViewListener listener);

    public void removeViewListener(IViewListener listenner);

    public int getViewControllerType();

    public void fireViewEvent(AbstractViewEvent viewevent);

}
