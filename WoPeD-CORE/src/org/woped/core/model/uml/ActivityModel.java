package org.woped.core.model.uml;

import javax.swing.ImageIcon;

import org.woped.core.model.CreationMap;

public class ActivityModel extends AbstractUMLElementModel
{
    public static final int WIDTH  = 60;
    public static final int HEIGHT = 40;

    private ImageIcon       icon   = null;

    public ActivityModel(CreationMap map)
    {
        super(map);
    }

    public CreationMap getCreationMap()
    {
        CreationMap creationMap = super.getCreationMap();
        creationMap.setType(AbstractUMLElementModel.ACTIVITY_TYPE);
        // TODO: creationMap.setImageIconPath(icon.;
        return creationMap;
    }

    public int getType()
    {
        return AbstractUMLElementModel.ACTIVITY_TYPE;
    }

    public int getDefaultHeight()
    {
        return ActivityModel.HEIGHT;
    }

    public int getDefaultWidth()
    {
        return ActivityModel.WIDTH;
    }

    public String getToolTipText()
    {
        return "Activity: " + getId();
    }

    public ImageIcon getIcon()
    {
        return icon;
    }

    public void setIcon(ImageIcon icon)
    {
        this.icon = icon;
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
        //        AttributeMap portmap = getPort().getAttributes();
        //        GraphConstants.setSize(portmap, new Dimension(icon.getIconWidth()+4,
        // icon.getIconHeight())+4);
        //        getPort().setAttributes(null);
    }

}
