package org.woped.editor.graphbeautifier;

import java.util.ArrayList;
import java.util.HashSet;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

/**
 * The Class <code>SGYElement</code> represents an Element/Transition in the
 * Graph. It consists of a reference to an <code>AbstractElementModel</code> and
 * additive attributes necessary to do the SugiyamaAlgortihm.
 * 
 * @author Team: GraphBeautifier WWI08
 * @version 0.4
 */
public class SGYElement {
	// *************************************************************
	// InstanceVariables *******************************************
	// *************************************************************
	/** HashSet containing the vertex's parents. */
	protected HashSet<SGYElement> parents = new HashSet<SGYElement>();
	/** HashSet containing the vertex's children. */
	protected HashSet<SGYElement> children = new HashSet<SGYElement>();
	/** Deepest hierarchy for this vertex's in the containing graph. */
	protected int hierarchy;
	/** True, if this element is a dummy-vertex; false if not. */
	protected boolean isDummyVertex = false;
	/** Reference to the source of a dummy*/
	protected SGYElement dummySource;
	/** Reference to the target of a dummy*/
	protected SGYElement dummyTarget;
	/**
	 * information about visit-status in cycle-detection. <br>
	 * <b>Status:</b><br>
	 * 0 = not visited<br>
	 * 1 = proceeding<br>
	 * 3 = visited
	 */
	protected int cycleDetectionVisited = 0;
	/** average of indices of parents or children */
	protected double barycenter = 0;
	/** index of the element in arraylist used in order to count crossings */
	protected int index = -1;
	/** The reference to the handled <code>AbstractElementModel</code> */
	protected AbstractPetriNetElementModel editorElement;
	/** containing the outgoing arcs */
	protected ArrayList<ArcModel> arcsOut = new ArrayList<ArcModel>();
	/** Coordinate for arranging in editor */
	protected int xCoordinate = 0, yCoordinate = 0;

	// *************************************************************
	// Other Methods ***********************************************
	// *************************************************************
	/**
	 * Instantiates a new <code>SGYElement</code>.
	 * 
	 * @param aem
	 *            the reference to the handled <code>AbstractElementModel</code>
	 */
	public SGYElement(AbstractPetriNetElementModel aem) {
		this.editorElement = aem;
	}

	/**
	 * Instantiates a new <code>SGYElement</code>.
	 * 
	 * @param aem
	 *            the reference to the handled <code>AbstractElementModel</code>
	 * @param isDummyVertex
	 *            <code>true</code>, if this element is a dummy-vertex;
	 *            <code>false</code>(standard) if not.
	 * @param source
	 *			  the reference to the source <code>SGYElement</code>
	 * @param target
	 * 			  the reference to the target <code>SGYElement</code>
	 */
	public SGYElement(AbstractPetriNetElementModel aem, boolean isDummyVertex, SGYElement source, SGYElement target) {
		this.editorElement = aem;
		this.isDummyVertex = isDummyVertex;
		this.dummySource = source;
		this.dummyTarget = target;
	}
}