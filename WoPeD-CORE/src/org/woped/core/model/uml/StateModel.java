package org.woped.core.model.uml;

import org.woped.core.model.CreationMap;

public class StateModel extends AbstractUMLElementModel
{
    public static final int STATE_STOP_TYPE  = 0;
    public static final int STATE_START_TYPE = 1;
    public static final int WIDTH            = 30;
    public static final int HEIGHT           = 30;

    private int             stateType        = -1;
    private CreationMap     map              = null;

    public StateModel(CreationMap map)
    {
        super(map);
        stateType = map.getStateType();

    }

    public CreationMap getCreationMap()
    {
        CreationMap creationMap = super.getCreationMap();
        creationMap.setType(AbstractUMLElementModel.STATE_TYPE);
        creationMap.setStateType(stateType);
        return creationMap;
    }

    public int getType()
    {
        return AbstractUMLElementModel.STATE_TYPE;
    }

    public int getDefaultHeight()
    {
        return HEIGHT;
    }

    public int getDefaultWidth()
    {
        return WIDTH;
    }

    public String getToolTipText()
    {
        return "Activity " + getId();
    }

    public int getStateType()
    {
        return stateType;
    }

}
