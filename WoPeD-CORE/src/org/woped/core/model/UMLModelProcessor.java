package org.woped.core.model;

import java.awt.geom.Point2D;
import java.io.Serializable;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.uml.AbstractUMLElementModel;

/**
 * This class provides the translation between a uml View and a Petrinet Model
 * 
 * @author landess
 *  
 */
public class UMLModelProcessor extends AbstractModelProcessor implements Serializable
{
    private int activityCounter = 0;
    private int stateCounter    = 0;
    private int operatorCounter = 0;
    private int arcCounter      = 0;

    public UMLModelProcessor()
    {
        super(AbstractModelProcessor.MODEL_PROCESSOR_UML);
    }

    public ArcModel createArc(Object sourceId, Object targetId)
    {
        return createArc(null, sourceId, targetId, new Point2D[0], true);
    }

    public ArcModel createArc(String id, Object sourceId, Object targetId, Point2D[] points, boolean thisHasNoEffect)
    {
        AbstractElementModel sourceModel = getElementContainer().getElementById(sourceId);
        AbstractElementModel targetModel = getElementContainer().getElementById(targetId);
        // falls aalst source oder target -> update Model
        ArcModel arc = ModelElementFactory.createArcModel(getNexArcId(), (DefaultPort) sourceModel.getChildAt(0), (DefaultPort) targetModel.getChildAt(0));
        arc.setPoints(points);
        getElementContainer().addReference(arc);
        return arc;
    }

    public AbstractElementModel createElement(CreationMap map, String idPreFix)
    {
        if (map.isValid())
        {
            if (map.getId() == null) map.setId(getNewElementId(map.getType()));
            AbstractElementModel anElement = (AbstractElementModel) ModelElementFactory.createModelElement(map);
            getElementContainer().addElement(anElement);

            return anElement;
        }
        return null;
    }

    public void removeArc(Object arcId)
    {
        getElementContainer().removeArc(arcId);
    }

    public void removeElement(AbstractElementModel element)
    {
        getElementContainer().removeElement(element.getId());
    }

    public void removeElement(Object id)
    {
        getElementContainer().removeElement(id);
    }

    public String getNewElementId(int elementType)
    {
        String id;
        if (elementType == AbstractUMLElementModel.ACTIVITY_TYPE)
        {
            id = "A" + ++activityCounter;
            return getElementContainer().getElementById(id) != null ? getNewElementId(elementType) : id;
        } else if (elementType == AbstractUMLElementModel.OPERATOR_TYPE)
        {
            id = "O" + ++operatorCounter;
            return getElementContainer().getElementById(id) != null ? getNewElementId(elementType) : id;
        } else if (elementType == AbstractUMLElementModel.STATE_TYPE)
        {
            id = "S" + ++stateCounter;
            return getElementContainer().getElementById(id) != null ? getNewElementId(elementType) : id;
        } else return null;
    }

    public String getNexArcId()
    {
        String arcId = "a" + ++arcCounter;
        return getElementContainer().getArcById(arcId) != null ? getNexArcId() : arcId;
    }
}
