package org.woped.qualanalysis.sidebar.expert.components;

import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings({"serial"})
public abstract class NodeGroupListNetInfo extends NetInfo {
	// ! Construct node group list net info object
	// ! @param displayString name of the tree node to be displayed
	// ! @param nodeGroupIterator specifies an iterator enumerating objects of
	// type Collection
	public NodeGroupListNetInfo(String displayString, Iterator<?> nodeGroupIterator) {
		super(displayString);
		int x = 0;
		while (nodeGroupIterator.hasNext()) {
			try {
				Collection<?> current = (Collection<?>) nodeGroupIterator.next();
				add(new NodeGroupNetInfo(getGroupDisplayString(x, current), current.iterator()));
				++x;
			} catch (ClassCastException e) {
				// Ignore all nodes that are not at least AbstractElementModel
			}
		}
	}

	// ! Must be implemented to display a string for each
	// ! node group
	// ! @param nGroupIndex specifies the index of the node group (counting from
	// zero)
	// ! @param nodeGroup specifies the node group itself
	public abstract String getGroupDisplayString(int nGroupIndex, Collection<?> nodeGroup);
}