package org.woped.layout.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.awt.Point;

import org.jgraph.graph.GraphCell;

import org.woped.core.Constants;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.uml.AbstractUMLElementModel;
import org.woped.core.model.uml.ActivityModel;
import org.woped.core.utilities.LoggerManager;


public class Orientation {
	
	static double scale_x = 1.2;
	static double scale_y = 1.2;
	static double offset_x = 50;
	static double offset_y = 0;

	private static Point flip(Point p) {
		return new Point(p.y,p.x);
	}
	
	private static Point scale(Point p) {
		return new Point((int)((double)p.x * scale_x),(int)((double)p.y * scale_y));
	}

	private static Point move(Point p) {
		return new Point((int)((double)p.x + offset_x),(int)((double)p.y + offset_y));
	}


	public static void rotate(ModelElementContainer mec) {
		 Map<String, AbstractElementModel> elements = new HashMap<String, AbstractElementModel>();
		 Iterator elementsIter = mec.getIdMap().keySet().iterator();
		 AbstractElementModel element;
		 
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.PLACE_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.SUBP_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRIGGER_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.GROUP_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.RESOURCE_TYPE));
		 
		 while (elementsIter.hasNext()) {
			 element = elements.get(elementsIter.next());
			 if ( element != null ) {
				 // re-position object
				 element.setPosition(flip(element.getPosition()));
				 // re-position label
				 element.getNameModel().setPosition(flip(element.getNameModel().getPosition()));
				 
				 if (element instanceof TransitionModel) {
					 if (((TransitionModel)element).hasResource()) {
						 TransitionModel t = (TransitionModel) element;
						 // re-position resource
						 t.getToolSpecific().getTransResource().setPosition(flip(t.getResourcePosition()));
						 // re-position trigger
						 t.getToolSpecific().getTrigger().setPosition(flip(t.getTriggerPosition()));
					 }
				 }
			 }
			 LoggerManager.info(Constants.CORE_LOGGER,"element " + element.getNameModel().getNameValue() + " processed" );
		 }

	}
	
	public static void rotate2(ModelElementContainer mec) {
		 Map<String, AbstractElementModel> elements = new HashMap<String, AbstractElementModel>();
		 Iterator elementsIter = mec.getIdMap().keySet().iterator();
		 AbstractElementModel element;
		 
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.PLACE_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.SUBP_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRIGGER_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.GROUP_TYPE));
		 elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.RESOURCE_TYPE));
		 
		 int x;
		 int y;
		 
		 while (elementsIter.hasNext()) {
			 element = elements.get(elementsIter.next());
			 if ( element != null ) {
				 /*
				 if (element.getType() == AbstractUMLElementModel.ACTIVITY_TYPE
							&& map.getImageIcon() != null) {
						((ActivityModel) element).setIcon(map.getImageIcon());
						if (map.getSize() != null) {
						}
				}
				*/
				 //((PetriNetModelElement)element).get
				 // re-position object
				 Point elementPosition = flip(element.getPosition());
				 elementPosition.setLocation(scale(elementPosition));
				 elementPosition.setLocation(move(elementPosition));
				 element.setPosition(elementPosition);
				 // re-position label
				 // diff ?
				 Point elementLabelPosition = flip(element.getNameModel().getPosition());
				 elementLabelPosition.setLocation(scale(elementLabelPosition));
				 elementLabelPosition.setLocation(move(elementLabelPosition));
				 element.getNameModel().setPosition(elementLabelPosition);
				 
				 if (element instanceof TransitionModel) {
					 TransitionModel t = (TransitionModel) element;
					 if (t.hasResource()) {
						
						 // re-position resource
						 /*
						 Point resourcePosition = flip(t.getResourcePosition());
						 resourcePosition.setLocation(scale(resourcePosition));
						 resourcePosition.setLocation(move(resourcePosition));
						 */
						 x = elementPosition.x - 5 - t.getToolSpecific().getTransResource().getWidth();
						 y = elementPosition.y - (int) t.getToolSpecific().getTransResource().getHeight()/3;
						 // Point resourcePosition = new Point(x,y);
						 t.getToolSpecific().getTransResource().setPosition(new Point(x,y));
					 }
					 if (t.hasTrigger()) {
						 // re-position trigger
						 /*
						 Point triggerPosition = flip(t.getTriggerPosition());
						 triggerPosition.setLocation(scale(triggerPosition));
						 triggerPosition.setLocation(move(triggerPosition));
						 */
						 x = elementPosition.x -5 -t.getToolSpecific().getTrigger().getWidth();
						 y = elementPosition.y + (int)element.getHeight()/2;
						 // Point triggerPosition = new Point(x,y);
						 t.getToolSpecific().getTrigger().setPosition(new Point(x,y));
						 /*
						 System.out.println("Element  x=" +elementPosition.x + " y=" + elementPosition.y );
						 System.out.println("Resource x=" +resourcePosition.x + " y=" + resourcePosition.y );
						 System.out.println("Trigger  x=" +triggerPosition.x + " y=" + triggerPosition.y );
						 */
					 }
				 }
			 }
			 LoggerManager.info(Constants.CORE_LOGGER,"element " + element.getNameModel().getNameValue() + " processed" );
		 }

	}

}
