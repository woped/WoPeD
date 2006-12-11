package org.woped.core.model;

import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class AbstractModelProcessor implements Serializable
{
    private int                   processorType         = -1;
    private String                id                    = null;
    private String                name                  = null;
    private ModelProcessorContext modelProcessorContext = null;

    public AbstractModelProcessor(int processorType)
    {
        this.processorType = processorType;
        elementContainer = new ModelElementContainer();
        modelProcessorContext = new ModelProcessorContext();
    }

    private ModelElementContainer elementContainer         = null;
    public static int             MODEL_PROCESSOR_UML      = 1;
    public static int             MODEL_PROCESSOR_PETRINET = 0;

    abstract public AbstractElementModel createElement(CreationMap map, String idPreFix);

    abstract public void removeElement(Object id);

    abstract public void removeElement(AbstractElementModel element);

    abstract public ArcModel createArc(Object sourceId, Object targetId);

    abstract public ArcModel createArc(String id, Object sourceId, Object targetId, Point2D[] points, boolean nestedElementsTranslation);

    abstract public void removeArc(Object arcId);

    abstract public String getNewElementId(int elementType);

    abstract public String getNexArcId();

    public ModelElementContainer getElementContainer()
    {
        return elementContainer;
    }

    public void setElementContainer(ModelElementContainer elementContainer)
    {
        this.elementContainer = elementContainer;
    }

    public int getProcessorType()
    {
        return processorType;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public ModelProcessorContext getModelProcessorContext()
    {
        return modelProcessorContext;
    }

    public void setModelProcessorContext(ModelProcessorContext modelProcessorContext)
    {
        this.modelProcessorContext = modelProcessorContext;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
