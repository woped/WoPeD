package org.woped.editor.orientation;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;

/**
 * 
 * @author Projectgroup09 handels the rotation of the whole view and the single
 *         elements
 * 
 *         created on 20091202
 */
public class Orientation {

	private boolean rotateSelected = false;
	
	private static final int TURN_LEFT = -1;
	private static final int TURN_RIGHT = 1;
	
	private static int triggerXOffset;
	private static int triggerYOffset;
	private static int resourceXOffset;
	private static int resourceYOffset;

	/**
	 * rotates the elements including names (caption), arcs, resources, triggers
	 * for the whole view
	 * 
	 * @param mec the ModelElementContainer to rotate all elements of
	 */
	public void rotateView(ModelElementContainer mec) {
		Map<String, AbstractElementModel> elements = new HashMap<String, AbstractElementModel>();
		Iterator<String> elementsIter = mec.getIdMap().keySet().iterator();
		AbstractElementModel element;
		int newX = 0, newY = 0, negX = 0, negY = 0;
		
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.PLACE_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.SUBP_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.TRIGGER_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.GROUP_TYPE));
		elements.putAll(mec.getElementsByType(AbstractPetriNetModelElement.RESOURCE_TYPE));

		while (elementsIter.hasNext()) {
			element = elements.get(elementsIter.next());
			if (element != null) {
				// rotate elements
				element.setPosition(element.getY(), element.getX());

				// rotate names
				element.getNameModel().setPosition(element.getNameModel().getY(), element.getNameModel().getX());

				// rotate triggers and resources
				if (element instanceof TransitionModel) {
					TransitionModel tm = (TransitionModel) element;

					if (!rotateSelected) {
						rotateTransitionIcon(tm, TURN_RIGHT);
						triggerXOffset = -5;
						triggerYOffset = 0;
						resourceXOffset = -20;
						resourceYOffset = -25;
					} else {
						rotateTransitionIcon(tm, TURN_LEFT);
						triggerXOffset = 0;
						triggerYOffset = 5;
						resourceXOffset = 25;
						resourceYOffset = 20;
					}
					if (tm.hasTrigger()) {
						newX = tm.getToolSpecific().getTrigger().getY() + triggerXOffset;
						newY = tm.getToolSpecific().getTrigger().getX() + triggerYOffset;
						tm.getToolSpecific().getTrigger().setPosition(newX, newY);
						if (newX < negX)
							negX = newX;
						if (newY < negY)
							negY = newY;
					}
					if (tm.hasResource()) {
						newX = tm.getToolSpecific().getTransResource().getY() + resourceXOffset;
						newY = tm.getToolSpecific().getTransResource().getX() + resourceYOffset;
						tm.getToolSpecific().getTransResource().setPosition(newX, newY);
						if (newX < negX)
							negX = newX;
						if (newY < negY)
							negY = newY;
					}
				}
			}
		}

		// rotate arcpoints
		Iterator<String> arcsIter = mec.getArcMap().keySet().iterator();
		ArcModel arc;
		while (arcsIter.hasNext()) {
			arc = mec.getArcById(arcsIter.next());
			if (arc != null) {
				for (Point2D p : arc.getPoints()) {
					p.setLocation(p.getY(), p.getX());
				}
			}
		}
		if (negX < 0 || negY < 0) {
			moveAllElements(-negX, -negY, mec);
		}
	}

	/**
	 * moves all elements of the given ModelElementContainer by the given x- and y-value
	 * @param moveX the value to move all elements right or left
	 * @param moveY the value to move all elements up or down
	 * @param mec the ModelElementContainer with all elements which have to been moved
	 */
	private void moveAllElements(int moveX, int moveY, ModelElementContainer mec) {
		AbstractElementModel element;
		ArcModel arc;
		for (String elementId : mec.getIdMap().keySet()) {
			element = mec.getElementById(elementId);
			if (element != null) {
				// move element
				element.setPosition(element.getX() + moveX, element.getY() + moveY);

				// move element´s name
				element.getNameModel().setPosition(element.getNameModel().getX() + moveX,
						element.getNameModel().getY() + moveY);

				// move triggers and resources (if existing)
				if (element instanceof TransitionModel) {
					TransitionModel tm = (TransitionModel) element;

					if (tm.hasTrigger()) {
						TriggerModel trigMod = tm.getToolSpecific().getTrigger();
						trigMod.setPosition(trigMod.getX() + moveX, trigMod.getY() + moveY);
					}
					if (tm.hasResource()) {
						TransitionResourceModel transRes = tm.getToolSpecific().getTransResource();
						transRes.setPosition(transRes.getX() + moveX, transRes.getY() + moveY);
					}
				}
			}
		}
		// move arcs
		for (String arcId : mec.getArcMap().keySet()) {
			arc = mec.getArcById(arcId);
			if (arc != null) {
				for (Point2D p : arc.getPoints()) {
					p.setLocation(p.getX() + moveX, p.getY() + moveY);
				}
			}
		}
	}

	/**
	 * 
	 * @param transition the TransitionModel to rotate right way
	 */
	public void rotateTransRight(TransitionModel transition) {
		flipTransitionAccessories(transition);
		rotateTransitionIcon(transition, TURN_RIGHT);
	}
	
	/**
	 * 
	 * @param transition the TransitionModel to rotate left way
	 */
	public void rotateTransLeft(TransitionModel transition) {
		flipTransitionAccessories(transition);
		rotateTransitionIcon(transition, TURN_LEFT);
	}
	
	/**
	 * 
	 * @param transition the TransitionModel to rotate icon of
	 * @param direction the direction to rotate the icon
	 */
	private void rotateTransitionIcon(TransitionModel transition, int direction){
		int newPosition;
		int currentPosition = transition.getToolSpecific().getOperatorPosition().ordinal();
		if(direction == TURN_LEFT)
		{
			newPosition = (currentPosition + (OperatorPosition.values().length-1)) % OperatorPosition.values().length;
		}
		else
		{
			newPosition = (currentPosition + 1) % OperatorPosition.values().length;
		}
		transition.getToolSpecific().setOperatorPosition(OperatorPosition.values()[newPosition]);
	}
	
	/**
	 * 
	 * @param transition the TransitionModel to flip accessories of
	 */
	private void flipTransitionAccessories(TransitionModel transition){
		int currentPosition = transition.getToolSpecific().getOperatorPosition().ordinal();
		// flip name
		if (currentPosition % 2 != 0) {
			transition.getNameModel().setPosition(
					transition.getNameModel().getX() + transition.getWidth(),
					transition.getNameModel().getY() - transition.getHeight());
		} else {
			transition.getNameModel().setPosition(
					transition.getNameModel().getX() - transition.getWidth(),
					transition.getNameModel().getY() + transition.getHeight());
		}

		// flip trigger
		if (transition.hasTrigger()) {
			if (currentPosition % 2 != 0) {
				transition.getToolSpecific().getTrigger().setPosition(
					(int) transition.getToolSpecific().getTrigger().getPosition().getX() - 35,
					(int) transition.getToolSpecific().getTrigger().getPosition().getY() + 30);
			} else {
				transition.getToolSpecific().getTrigger().setPosition(
					(int) transition.getToolSpecific().getTrigger().getPosition().getX() + 35,
					(int) transition.getToolSpecific().getTrigger().getPosition().getY() - 30);
			}
		}
		// flip resource
		if (transition.hasResource()) {
			if (currentPosition % 2 != 0) {
				transition.getToolSpecific().getTransResource().setPosition(
					(int) transition.getToolSpecific().getTransResource().getPosition().getX() - 75,
					(int) transition.getToolSpecific().getTransResource().getPosition().getY() + 30);
			} else {
				transition.getToolSpecific().getTransResource().setPosition(
					(int) transition.getToolSpecific().getTransResource().getPosition().getX() + 75,
					(int) transition.getToolSpecific().getTransResource().getPosition().getY() - 30);
			}
		}
	}
	

	/**
	 * @return the rotateSelected
	 */
	public boolean isRotateSelected() {
		return rotateSelected;
	}

	/**
	 * @param rotateSelected the rotateSelected to set
	 */
	public void setRotateSelected(boolean rotateSelected) {
		this.rotateSelected = rotateSelected;
	}
}
