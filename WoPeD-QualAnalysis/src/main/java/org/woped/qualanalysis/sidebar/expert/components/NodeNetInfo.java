package org.woped.qualanalysis.sidebar.expert.components;

import java.util.Iterator;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.InnerElementContainer;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.structure.NetAlgorithms;

//! This class implements a tree node representing
//! one single node of a petri-net that is currently begin
//! analyzed

@SuppressWarnings("serial")
public class NodeNetInfo extends NetInfo {

	private int typeId = 0;
    // ! Store the node object this tree item represents
    private AbstractPetriNetElementModel m_nodeObject;
    // ! Sometimes it is better to reference the node owner
    // ! instead of the node itself (inner transitions and places
    // ! of a complex operator)
    private AbstractPetriNetElementModel m_nodeOwner = null;

	// ! Instantiate this class with an element model node and a flag
	// ! specifying whether children of that node (sub-processes,
	// ! inner transitions) should be added to the tree item
	public NodeNetInfo(AbstractPetriNetElementModel node, boolean addChildren) {
		// Initialize the display of this item
		super("");
		m_nodeObject = node;
		ModelElementContainer rootOwningContainer = m_nodeObject.getRootOwningContainer();
		m_nodeOwner = rootOwningContainer != null ? rootOwningContainer.getOwningElement() : null;
		setUserObject(getNodeString(m_nodeObject, m_nodeOwner));

		// set the type of the element
		typeId = node.getType();

		// if the element is a transition-operator
		if (node instanceof OperatorTransitionModel) {
			OperatorTransitionModel otm = (OperatorTransitionModel) node;
			typeId = otm.getOperatorType();
		}

		// Generic approach to detect whether this element has any children:
		// Elements with children implement InnerElementContainer
		if (addChildren && (node instanceof InnerElementContainer) && 
				(typeId == OperatorTransitionModel.XOR_SPLIT_TYPE
				|| typeId == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE
				|| typeId == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE || typeId == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE || typeId == OperatorTransitionModel.SUBP_TYPE)) {
			// Add sub-elements as tree items
			InnerElementContainer operator = (InnerElementContainer) node;
			ModelElementContainer simpleTransContainer = operator.getSimpleTransContainer();
			// Recursively call ourselves to add inner nodes
			Iterator<AbstractPetriNetElementModel> innerIterator = simpleTransContainer.getRootElements().iterator();
			while (innerIterator.hasNext()) {
				try {
                    AbstractPetriNetElementModel current = innerIterator.next();
                    AbstractPetriNetElementModel owningElement = current.getRootOwningContainer().getOwningElement();
					if ((owningElement != null)
							&& (operator.equals(current.getRootOwningContainer().getOwningElement())))
						add(new NodeNetInfo(current, true));
				} catch (ClassCastException e) {
					// Ignore all nodes that are not at least
					// AbstractElementModel
				}
			}
		}
	}

	// ! Get a string with the following layout: "(node, node, ...)"
	// ! if any nodes are found matching the specified criteria, "" otherwise
	// ! @param node Specifies the node whose connected nodes are to be
	// enumerated
	// ! @param connectionType specifies the type of connection as
	// ! NetAlgorithms.connectionTypeINBOUND,
	// ! NetAlgorithms.connectionTypeOUTBOUND or
	// ! NetAlgorithms..connectionTypeALL
	// ! @param superOnly if true, only connections going to a super-net are
	// going
	// ! to be listed
	private static String getConnectedNodesString(AbstractPetriNetElementModel node, int connectionType, boolean superOnly) {
		StringBuffer result = new StringBuffer();
		Iterator<AbstractPetriNetElementModel> relevantNodes = NetAlgorithms.getDirectlyConnectedNodes(node, connectionType).iterator();
		int nodeHierarchyLevel = node.getHierarchyLevel();
		boolean isFirst = true;
		while (relevantNodes.hasNext()) {
            AbstractPetriNetElementModel currentNode = relevantNodes.next();
            // If superOnly is specified
			// only nodes that are contained in the next lower level container
			// are relevant for us
			if ((!superOnly) || (currentNode.getHierarchyLevel() < nodeHierarchyLevel)) {
				if (isFirst)
					isFirst = false;
				else
					result.append(", ");
				String currentNodeName = currentNode.getNameValue();
				result.append((currentNodeName != null) ? currentNodeName : currentNode.getId());
			}
		}
		if (result.length() > 0)
			result = new StringBuffer("(" + result.toString() + ")");
		return result.toString();
	}

	private static String getNodeString(AbstractPetriNetElementModel node,
			AbstractPetriNetElementModel nodeOwner) {
		String predecessors = getConnectedNodesString(node,
				NetAlgorithms.connectionTypeINBOUND, true);
		String successors = getConnectedNodesString(node,
				NetAlgorithms.connectionTypeOUTBOUND, true);
		String result = "";
		int typeId;

		String nameValue = node.getNameValue();
		if (nodeOwner != null && nodeOwner.getType() == TransitionModel.TRANS_OPERATOR_TYPE) {
			typeId = ((OperatorTransitionModel) nodeOwner).getOperatorType();
			if (typeId == OperatorTransitionModel.XOR_JOIN_TYPE
					|| typeId == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE || typeId == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) {
                result = predecessors;
			}
		}

		result += ((nameValue != null) ? nameValue : node.getId());

		if (nodeOwner != null && nodeOwner.getType() == TransitionModel.TRANS_OPERATOR_TYPE) {
			typeId = ((OperatorTransitionModel) nodeOwner).getOperatorType();
			if (typeId == OperatorTransitionModel.XOR_SPLIT_TYPE
					|| typeId == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE || typeId == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) {
                result += successors;
			}
		}

		/*
		 * if (nodeOwner != null) { // result += ", " + nodeOwnerString + ": ("
		 * + // getNodeString(nodeOwner, null) + ")"; // result += " (in " +
		 * getNodeString(nodeOwner, null) + ")"; }
		 */
		return result;
	}

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

	public Object[] getReferencedElements() {
		Object[] result = new Object[1];
		// If the owner is a sub-process
		// the object can be displayed and thus will be returned as the item to
		// be selected
		result[0] = (((m_nodeOwner != null) && (m_nodeOwner.getType() != AbstractPetriNetElementModel.SUBP_TYPE)) ? m_nodeOwner
				: m_nodeObject);
		return result;
    }
}
