package org.woped.editor.graphbeautifier;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.orientation.EditorSize;

/**
 * The Class <code>SGYGraph</code> represents a Graph, containing SGYElements.
 * This graph can be beautified and drawn.
 * 
 * @author Team: GraphBeautifier WWI08
 * @version 0.3
 */

public class SGYGraph {
	
	
	
	// *************************************************************
	// * *
	// *Instance Variables *
	// * *
	// *************************************************************
	
	/** Grid, containing all models. */
	private ArrayList<ArrayList<SGYElement>> graph;
	/** maximum hierarchy */
	private int maxHierarchy;
	/** lowest global crossing (used in cross-reduction) */
	private int lowest_global_crossings;
	/** how often a cycle-reduction sweep can fail (in sense of no improving) */
	private int forgivenessCounter = 100;
	/** how many crossings exist in the graph */
	private int global_crossings = 0;
	/** the UI-editor containing the relevant graph */
	private EditorVC editor;
	/** the arcs, leading to cycles */
	private HashSet<ArcModel> cycleArcs;
	/**
	 * lineType is required for the optimal distance; 0 = Standard; 1 = there
	 * are Triggers; 2 = there are Triggers and Ressources
	 */
	private int lineType = 0;
	/** graph's source */
	private SGYElement graphSource;
	/** Contains all the net's vertices (elements, transitions) */
	HashMap<String, SGYElement> vertices;
	Vector<Object> selectElements = new Vector<Object>();

	
	
	
	// *************************************************************
	// * *
	// *Public Methods *
	// * *
	// *************************************************************
	
	/**
	 * Instantiates a new <code>SGYGraph</code>.
	 * 
	 * @param editor
	 *            the editor, thats graph is going to be imported
	 */
	public SGYGraph(EditorVC editor) {
		maxHierarchy = 0;
		lowest_global_crossings = 2147483647; // max number <int> can handle
		cycleArcs = new HashSet<ArcModel>();
		vertices = new HashMap<String, SGYElement>();
		this.editor = editor;
			this.gettingElements(vertices);
			this.detectCycles(graphSource);
			this.turnCycleArcs();
			graphSource.hierarchy = 0;
			this.setChildrensHierarchies(graphSource);
			this.importGraphToArrayLists();
	}

	
	
	/**
	 * Beautifies the imported graph according to the sugiyama-algorithm
	 * 
	 * @param forgivenessCounter
	 *            number of iterations, use 0 for default value
	 */
	public void beautify(int forgivenessCounter) {
		this.removeArcPoints();
		this.addDummyVertices();
		if (forgivenessCounter > 0)
			this.forgivenessCounter = forgivenessCounter;
		this.crossingReduction();
		this.verticalCoordinateAssignment();
		this.destroyDummyVertices();
	}

	
	
	/**
	 * Draws the graph and updates the UI
	 * 
	 * @param x
	 *            horizontal space between two elements, use 0 for default value
	 * @param y
	 *            vertical space between two elements, use 0 for default value
	 */
	public void draw(int x, int y) {
		// Is necessary to switch off the undoManager
		if (((WoPeDUndoManager) editor.getGraph().getUndoManager()) != null) {
			((WoPeDUndoManager) editor.getGraph().getUndoManager())
					.setEnabled(false);
		}
		SGYElement modelToArrange;
		int ixIntervall;
		int iyIntervall;
		if (x > 0) {
			ixIntervall = x;
		} else {
			ixIntervall = 75;
		}
		if (y > 0) {
			iyIntervall = y;
		} else {
			if (this.lineType == 2) {
				iyIntervall = 100;
			} else if (this.lineType == 1) {
				iyIntervall = 80;
			} else {
				iyIntervall = 70;
			}
		}
		int xOffset = 50;
		int xPosition = 0;
		int yPosition = 0;

		for (int i = 0; i < graph.size(); i++) {
			for (int j = 0; j < graph.get(i).size(); j++) {
				modelToArrange = graph.get(i).get(j);
				if (modelToArrange.isDummyVertex == false) {
					xPosition = (int) (xOffset + modelToArrange.hierarchy
							* ixIntervall);
					yPosition = (int) (modelToArrange.yCoordinate * iyIntervall);
					modelToArrange.editorElement.setPositionGroup(xPosition,
							yPosition);
				} else {
					if (modelToArrange.dummyTarget.hierarchy
							- modelToArrange.dummySource.hierarchy > 2) {
						xPosition = (int) (xOffset + modelToArrange.hierarchy
								* ixIntervall);
						yPosition = (int) (modelToArrange.yCoordinate * iyIntervall);
						ArcModel arc = this.getArc(modelToArrange.dummySource,
								modelToArrange.dummyTarget);
						Point addArcPoint = new Point(xPosition + 20,
								yPosition + 20);
						arc.addPoint(addArcPoint, arc.getPoints().length - 1);
					}
				}
			}
		}
		this.turnCycleArcs();// back to cycles

		// Resize the EditorWindow
		EditorSize editorSize = new EditorSize(editor);
		editorSize.resize(false);

		editor.getGraph().drawNet(editor.getModelProcessor());
		editor.updateNet();
		editor.updateUI();

		// Is necessary to switch on the undoManager
		if (editor.getGraph().getUndoManager() != null) {
			((WoPeDUndoManager) editor.getGraph().getUndoManager())
					.setEnabled(true);
		}
	}

	
	
	// *************************************************************
	// * *
	// *Private Methods:Importing *
	// * *
	// *************************************************************

	/**
	 * Gets all Elements, builds parent/child-relationships, assigns the
	 * hierarchies
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void gettingElements(HashMap<String, SGYElement> vertices) {
		HashMap<String, SGYElement> potentialSources;
		AbstractElementModel ele;
		ArcModel arc;
		String sSource, sTarget;
		ModelElementContainer mec = editor.getModelProcessor()
				.getElementContainer();

		// getting Elements and Arcs
		for (String elementId : mec.getIdMap().keySet()) {
			ele = mec.getElementById(elementId);
			selectElements.add(ele.getParent());
			if (lineType != 2) {
				if (ele instanceof TransitionModel) {
					TransitionModel transitionModel = (TransitionModel) ele;
					if (transitionModel.hasTrigger()) {
						lineType = 1;
					}
					if (transitionModel.hasResource()) {
						lineType = 2;
					}
				}
			}
			vertices.put(ele.getId(), new SGYElement(ele));
		}

		// building relationships, defines the source
		potentialSources = (HashMap<String, SGYElement>) vertices.clone();
		for (String arcId : mec.getArcMap().keySet()) {
			arc = mec.getArcById(arcId);
			selectElements.add(arc);
			sSource = null;
			sTarget = null;
			if (arc != null) {
				sSource = arc.getSourceId();
				sTarget = arc.getTargetId();
			}
			if (vertices.containsKey(sSource) && vertices.containsKey(sTarget)) {
				vertices.get(sSource).children.add(vertices.get(sTarget));
				vertices.get(sTarget).parents.add(vertices.get(sSource));
				vertices.get(sSource).arcsOut.add(arc);
				potentialSources.remove(sTarget);
			}
		}

		editor.getGraph().setSelectionCells(selectElements.toArray());
		editor.move(editor.getGraph().getSelectionCells(), 0, 0);
		graphSource = potentialSources.values().iterator().next();
	}

	
	
	/**
	 * Goes through the Graph for all model's children (and grandchildren) and
	 * sets their hierarchy
	 * 
	 * @param model
	 *            the model that's children's hierarchy shall be updated
	 */
	private void setChildrensHierarchies(SGYElement ele) {
		Iterator<SGYElement> childIter = ele.children.iterator();
		SGYElement child;
		while (childIter.hasNext()) {
			child = childIter.next();
			if (child.hierarchy < ele.hierarchy + 1)
				child.hierarchy = ele.hierarchy + 1;
			if (maxHierarchy < ele.hierarchy + 1)
				maxHierarchy = ele.hierarchy + 1;
			if (child.children.size() != 0)
				setChildrensHierarchies(child);
		}
	}

	
	
	/**
	 * Flips an arc, changes the relations of the relevant elements and changes
	 * the assignment of itself to an element
	 */
	private void turnArc(ArcModel arc) {
		Object sourceEdgeOld;
		SGYElement sourceSGYOld;
		SGYElement targetSGYOld;

		sourceSGYOld = vertices.get(arc.getSourceId());
		targetSGYOld = vertices.get(arc.getTargetId());

		// flip
		sourceEdgeOld = arc.getSource();
		arc.setSource(arc.getTarget());
		arc.setTarget(sourceEdgeOld);

		// flipping arcpoint-sequence
		if (arc.getPoints().length > 2) {
			Point2D temp;
			Point2D[] arcPointsCopy = arc.getPoints().clone();
			for (int i = 1; i < (arcPointsCopy.length - 1); i++) {
				temp = arcPointsCopy[(arcPointsCopy.length - 1 - i)];
				arc.removePoint(temp);
				arc.addPoint(temp, i);
			}

		}

		// deleting old relations
		sourceSGYOld.children.remove(targetSGYOld);
		targetSGYOld.parents.remove(sourceSGYOld);

		// setting new relations
		targetSGYOld.children.add(sourceSGYOld);
		sourceSGYOld.parents.add(targetSGYOld);

		// changing assignment
		sourceSGYOld.arcsOut.remove(arc);
		targetSGYOld.arcsOut.add(arc);

	}

	
	
	/**
	 * cycle-detection: Fills the <@code arcIDsToTurn> with the
	 * arcs that lead to a cycle.
	 */
	private void detectCycles(SGYElement element) {
		SGYElement nextElement;
		element.cycleDetectionVisited = 1;
		for (ArcModel arc : element.arcsOut) {
			nextElement = vertices.get(arc.getTargetId());
			//checking if the following element was already visited. If this is the case, here is a cycle.
			if (nextElement.cycleDetectionVisited == 0) 
				detectCycles(nextElement);
			else if (nextElement.cycleDetectionVisited == 1)
				cycleArcs.add(arc);
		}
		element.cycleDetectionVisited = 3;
	}

	
	
	/**
	 * calls the <@code turnArc>-Method for the determinde cycle-arcs
	 */
	private void turnCycleArcs() {
		for (ArcModel arc : cycleArcs) {
			turnArc(arc);
		}
	}

	
	/**
	 * building an ArrayList representing the graph fpr upcoming tasks
	 */
	private void importGraphToArrayLists() {
		graph = new ArrayList<ArrayList<SGYElement>>();
		for (int i = 0; i <= maxHierarchy; i++) {
			graph.add(new ArrayList<SGYElement>());
		}
		for (SGYElement actElement : vertices.values()) {
			graph.get(actElement.hierarchy).add(actElement);
		}
	}

	
	
	// *************************************************************
	// * *
	// *Private Methods:Beautifying *
	// * *
	// *************************************************************
	
	/**
	 * Add dummy vertices to the graph. Fills the connection between two
	 * elements with Dummy Vertices if hierarchy difference is greater than 1
	 */
	private void addDummyVertices() {
		ArrayList<SGYElement> alldummyVertices = new ArrayList<SGYElement>();
		for (int i = 0; i < this.graph.size(); i++) {
			for (int j = 0; j < this.graph.get(i).size(); j++) {
				SGYElement model = this.graph.get(i).get(j);
				if (model.children.size() == 0) {
					continue;
				}
				ArrayList<SGYElement> removeChildren = new ArrayList<SGYElement>();
				ArrayList<SGYElement> firstDummyVertices = new ArrayList<SGYElement>();
				for (SGYElement child : model.children) {
					int iHierarchyDifference = child.hierarchy
							- model.hierarchy;
					if (iHierarchyDifference > 1) {
						removeChildren.add(child);
						SGYElement[] singleDummyVertices = new SGYElement[iHierarchyDifference - 1];
						SGYElement lastDummyVertex = null;
						for (int k = 0; k < singleDummyVertices.length; k++) {
							singleDummyVertices[k] = new SGYElement(null, true,
									model, child);
						}
						int arrayCounter = 0;
						for (int k = model.hierarchy + 1; k < child.hierarchy; k++) {
							singleDummyVertices[arrayCounter].hierarchy = k;
							if (singleDummyVertices.length > 1) {
								if (arrayCounter == 0) {
									singleDummyVertices[arrayCounter].parents
											.add(model);
									singleDummyVertices[arrayCounter].children
											.add(singleDummyVertices[arrayCounter + 1]);
									firstDummyVertices
											.add(singleDummyVertices[arrayCounter]);
								} else if (arrayCounter == singleDummyVertices.length - 1) {
									singleDummyVertices[arrayCounter].parents
											.add(singleDummyVertices[arrayCounter - 1]);
									singleDummyVertices[arrayCounter].children
											.add(child);
									lastDummyVertex = singleDummyVertices[arrayCounter];
								} else {
									singleDummyVertices[arrayCounter].parents
											.add(singleDummyVertices[arrayCounter - 1]);
									singleDummyVertices[arrayCounter].children
											.add(singleDummyVertices[arrayCounter + 1]);
								}
							} else {
								singleDummyVertices[arrayCounter].parents
										.add(model);
								singleDummyVertices[arrayCounter].children
										.add(child);
								firstDummyVertices
										.add(singleDummyVertices[arrayCounter]);
								lastDummyVertex = singleDummyVertices[arrayCounter];
							}
							arrayCounter = arrayCounter + 1;
						}
						for (SGYElement dummyVertex : singleDummyVertices) {
							alldummyVertices.add(dummyVertex);
						}
						child.parents.add(lastDummyVertex);
					}
				}
				if (removeChildren.size() > 0) {
					for (SGYElement child : removeChildren) {
						model.children.remove(child);
						child.parents.remove(model);
					}
					for (SGYElement newChild : firstDummyVertices) {
						model.children.add(newChild);
					}
				}
			}
		}
		if (alldummyVertices.size() > 0) {
			for (SGYElement dummyVertex : alldummyVertices) {
				graph.get(dummyVertex.hierarchy).add(dummyVertex);
			}
		}
	}

	
	
	/**
	 * Removes dummy vertices off the graph. Removes a dummy vertices if parent
	 * and child have the same y-coordinate as the dummy.
	 */
	private void destroyDummyVertices() {
		ArrayList<SGYElement> removeDummyVertices = new ArrayList<SGYElement>();
		for (int i = 0; i < this.graph.size(); i++) {
			for (int j = 0; j < this.graph.get(i).size(); j++) {
				SGYElement model = this.graph.get(i).get(j);
				SGYElement parent = null;
				SGYElement child = null;
				if (model.isDummyVertex) {
					Iterator<SGYElement> iterChildren = model.children
							.iterator();
					Iterator<SGYElement> iterParents = model.parents.iterator();
					while (iterChildren.hasNext()) {
						child = iterChildren.next();
					}
					while (iterParents.hasNext()) {
						parent = iterParents.next();
					}
					if (child != null && parent != null) {
						if (parent.yCoordinate == model.yCoordinate
								&& model.yCoordinate == child.yCoordinate) {
							parent.children.remove(model);
							parent.children.add(child);
							child.parents.remove(model);
							child.parents.add(parent);
							removeDummyVertices.add(model);
						}
					}
				}
			}
		}
		if (removeDummyVertices.size() > 0) {
			for (SGYElement dummyVertex : removeDummyVertices) {
				graph.get(dummyVertex.hierarchy).remove(dummyVertex);
			}
		}
	}

	
	
	/**
	 * sweeps through the graph in order to find solution with minimum of
	 * crossing arcs
	 */
	@SuppressWarnings("unchecked")
	private void crossingReduction() {
		int sumIndex = 0;
		boolean direction;
		SGYElement neighbour;
		// copy of 2D ArrayList graph
		ArrayList<ArrayList<SGYElement>> copyGraph = new ArrayList<ArrayList<SGYElement>>();
		for (int i = 0; i < this.graph.size(); i++) {
			copyGraph.add(i, (ArrayList<SGYElement>) this.graph.get(i).clone());
		}

		// layer-by-layer sweep
		while (true) {
			// breaking condition, because there is just one element
			if (copyGraph.size() == 1)
				break;
			// left-to-right sweep
			for (int i = 1; i < copyGraph.size(); i++) {
				// calculate barycenter
				for (int j = 0; j < copyGraph.get(i).size(); j++) {
					SGYElement model = copyGraph.get(i).get(j);
					Iterator<SGYElement> iterParents = model.parents.iterator();
					while (iterParents.hasNext()) {
						neighbour = iterParents.next();
						sumIndex = sumIndex
								+ copyGraph.get(i - 1).indexOf(neighbour);
					}
					if (copyGraph.get(i).get(j).parents.size() == 0) {
						copyGraph.get(i).get(j).barycenter = sumIndex;
					} else {
						copyGraph.get(i).get(j).barycenter = sumIndex
								/ copyGraph.get(i).get(j).parents.size();
						sumIndex = 0;
					}
				}
				newOrder(copyGraph, i);
				direction = true;
				ArrayList<SGYElement> layerN1 = (ArrayList<SGYElement>) copyGraph
						.get(i - 1).clone();
				ArrayList<SGYElement> layerN2 = (ArrayList<SGYElement>) copyGraph
						.get(i).clone();
				global_crossings = global_crossings
						+ (crossCount(layerN1, layerN2, direction));
			}
			if (breakCondition(copyGraph)) {
				break;
			}
			// right-to-left sweep
			for (int i = copyGraph.size() - 1; i >= 1; i--) {
				// calculate barycenter
				for (int j = 0; j < copyGraph.get(i - 1).size(); j++) {
					Iterator<SGYElement> iterChildren = copyGraph.get(i - 1)
							.get(j).children.iterator();
					while (iterChildren.hasNext()) {
						neighbour = iterChildren.next();
						sumIndex = sumIndex
								+ copyGraph.get(i).indexOf(neighbour);
					}
					if (copyGraph.get(i - 1).get(j).children.size() == 0) {
						copyGraph.get(i - 1).get(j).barycenter = sumIndex;
					} else {
						copyGraph.get(i - 1).get(j).barycenter = sumIndex
								/ copyGraph.get(i - 1).get(j).children.size();
						sumIndex = 0;
					}
				}
				newOrder(copyGraph, i - 1);
				direction = false;
				ArrayList<SGYElement> layerN1 = (ArrayList<SGYElement>) copyGraph
						.get(i).clone();
				ArrayList<SGYElement> layerN2 = (ArrayList<SGYElement>) copyGraph
						.get(i - 1).clone();
				global_crossings = global_crossings
						+ (crossCount(layerN1, layerN2, direction));
			}
			if (breakCondition(copyGraph) == true) {
				break;
			}
		}
	}

	
	
	/**
	 * arranges the elements of a layer in a new order according to the
	 * barycenters
	 */
	private ArrayList<ArrayList<SGYElement>> newOrder(
			ArrayList<ArrayList<SGYElement>> copyGraph, int i) {
		boolean randomize = false;
		// check if there are same barycenters
		for (int j = 0; j < copyGraph.get(i).size() - 1; j++) {
			for (int k = j + 1; k < copyGraph.get(i).size(); k++) {
				if (copyGraph.get(i).get(j).barycenter == copyGraph.get(i).get(
						k).barycenter) {
					copyGraph.get(i).get(k).barycenter += Math.random();
					randomize = true;
				}
				if (randomize) {
					copyGraph.get(i).get(j).barycenter += Math.random();
				}
			}
		}
		// new order, insertion sort
		for (int j = 1; j < copyGraph.get(i).size(); j++) {
			SGYElement element = copyGraph.get(i).get(j);
			int k = j - 1;
			while (k >= 0
					&& copyGraph.get(i).get(k).barycenter > (element.barycenter)) {
				copyGraph.get(i).set(k + 1, copyGraph.get(i).get(k));
				k--;
			}
			copyGraph.get(i).set(k + 1, element);
		}
		return copyGraph;
	}

	
	
	/**
	 * counts the number of crossing arcs in two adjacent layers
	 */
	private int crossCount(ArrayList<SGYElement> layerN1,
			ArrayList<SGYElement> layerN2, boolean direction) {
		int countedCrosses = 0;
		ArrayList<SGYElement> nextVertex = new ArrayList<SGYElement>();
		// lexicographical list
		if (direction) {
			for (int i = 0; i < layerN1.size(); i++) {
				SGYElement model = layerN1.get(i);
				Iterator<SGYElement> iterChildren = model.children.iterator();
				while (iterChildren.hasNext()) {
					SGYElement children = (SGYElement) iterChildren.next();
					nextVertex.add(children);
				}
			}
		} else {
			for (int i = 0; i < layerN1.size(); i++) {
				SGYElement model = layerN1.get(i);
				Iterator<SGYElement> iterParents = model.parents.iterator();
				while (iterParents.hasNext()) {
					SGYElement parent = (SGYElement) iterParents.next();
					nextVertex.add(parent);
				}
			}
		}
		// get Index of children
		for (SGYElement element : nextVertex) {
			element.index = layerN2.indexOf(element);
		}
		// InsertionSort, counts how often elements have to be moved
		for (int i = 1; i < nextVertex.size(); i++) {
			SGYElement element = nextVertex.get(i);
			int k = i - 1;
			while (k >= 0 && nextVertex.get(k).index > element.index) {
				countedCrosses++;
				nextVertex.set(k + 1, nextVertex.get(k));
				k--;
			}
			nextVertex.set(k + 1, element);
		}
		return countedCrosses;
	}

	
	
	/**
	 * includes the breaking condition; if number of crossings is zero or there
	 * are no further improvements (forgivenessCounter = 0), it will stop
	 * crossing reduction
	 */
	@SuppressWarnings("unchecked")
	private boolean breakCondition(ArrayList<ArrayList<SGYElement>> copyGraph) {
		boolean breaking = false;
		if (lowest_global_crossings > global_crossings) {
			lowest_global_crossings = global_crossings;
			global_crossings = 0;
			for (int i = 0; i < copyGraph.size(); i++) {
				this.graph.set(i, (ArrayList<SGYElement>) copyGraph.get(i)
						.clone());
			}
		} else {
			forgivenessCounter--;
			global_crossings = 0;
			if (forgivenessCounter == 0) {
				breaking = true;
			}
		}
		if (lowest_global_crossings == 0) {
			breaking = true;
		}
		return breaking;
	}

	
	
	/**
	 * assigns the horizontal coordinate to each element. The determined order is not changing.
	 */
	private void verticalCoordinateAssignment() {
		ArrayList<SGYElement> actLayer;
		SGYElement actElement;
		HashSet<Integer> takenPlacesInLayer;
		int planPos;
		for (int i = graph.size() - 1; i >= 0; i--) { //going through the graph from the last layers
			actLayer = graph.get(i);
			int maxLayerY = 0;
			takenPlacesInLayer = new HashSet<Integer>(); //containing the places that are already used by an element in the actual layer
			for (int j = 0; j < actLayer.size(); j++) {
				actElement = actLayer.get(j);
				//if there are no children the position relative to children is not important. 
				//It can be set right under the previous element in the order
				if (actElement.children.size() == 0) { 
					planPos = maxLayerY + 1;
				//if there are children the best position (plan pos) would be the children's median.
				} else {
					planPos = 0;
					for (SGYElement child : actElement.children) {
						planPos = planPos + child.yCoordinate;
					}
					planPos = planPos / actElement.children.size();
				}
				//it is possible that the planned best position is already in use. In this cas we try to place it in the next possible place after it.
				for (int k = 0; k < 1000000; k++) {
					if (!takenPlacesInLayer.contains(new Integer(planPos + k))) {
						actElement.yCoordinate = planPos + k;
						if (planPos + k > maxLayerY)
							maxLayerY = planPos + k;
						takenPlacesInLayer.add(new Integer(planPos + k));
						break;
					}
				}
			}
		}

		// last step: centering the sink(s) in addiction to it's parents
		takenPlacesInLayer = new HashSet<Integer>();
		actLayer = graph.get(graph.size() - 1);
		for (int j = 0; j < actLayer.size(); j++) {
			actElement = actLayer.get(j);
			planPos = 0;
			for (SGYElement parent : actElement.parents) {
				planPos = planPos + parent.yCoordinate;
			}
			planPos = planPos / actElement.parents.size();
			for (int k = 0; k < 10000; k++) {
				if (!takenPlacesInLayer.contains(new Integer(planPos + k))) {
					actElement.yCoordinate = planPos + k;
					takenPlacesInLayer.add(new Integer(planPos + k));
					break;
				}
			}
		}
	}


	
	/**
	 * Removes all inner Arcpoints
	 */
	private void removeArcPoints() {
		ArcModel arc;
		for (String arcId : this.editor.getModelProcessor()
				.getElementContainer().getArcMap().keySet()) {
			arc = this.editor.getModelProcessor().getElementContainer()
					.getArcById(arcId);
			if (arc != null) {
				Point2D[] arrayArcPoints = arc.getPoints();
				if (arrayArcPoints.length > 2) {
					for (int i = 1; i < arrayArcPoints.length - 1; i++) {
						Point2D singleArcPoint = arrayArcPoints[i];
						arc.removePoint(singleArcPoint);
					}
				}
			}
		}
	}

	
	
	/**
	 * Get the arc between two elements
	 * 
	 * @param sourceID
	 *            sourceID of the arc
	 * @param targetID
	 *            targetID of the arc
	 */
	private ArcModel getArc(SGYElement source, SGYElement target) {
		ArcModel arc = null;
		for (String arcId : this.editor.getModelProcessor()
				.getElementContainer().getArcMap().keySet()) {
			arc = this.editor.getModelProcessor().getElementContainer()
					.getArcById(arcId);
			if (arc != null) {
				if (arc.getSourceId().equals(source.editorElement.getId())
						&& arc.getTargetId().equals(
								target.editorElement.getId()))
					break;
			}
		}
		return arc;
	}
}