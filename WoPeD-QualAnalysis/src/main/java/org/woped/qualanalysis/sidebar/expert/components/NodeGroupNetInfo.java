package org.woped.qualanalysis.sidebar.expert.components;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

@SuppressWarnings({"serial"})
public class NodeGroupNetInfo extends NetInfo {
	public NodeGroupNetInfo(String displayString, Iterator<?> elementIterator) {
		// First of all, initialize the element itself
		// but not yet with the real name
		super(displayString);

		while (elementIterator.hasNext()) {
			try {
				AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) elementIterator.next();
				add(new NodeNetInfo(current, false));
			} catch (ClassCastException e) {
				// Ignore all nodes that are not at least AbstractElementModel
			}
		}
	}

	public Object[] getReferencedElements() {
		// This is a group item
		// Retrieve all subitems and get their referenced elements
		ArrayList<Object> collectedItems = new ArrayList<Object>();
		// Iterate through all children
		for (Enumeration<?> e = children(); e.hasMoreElements();) {
			Object current = e.nextElement();
			NetInfo myInfo = null;
			try {
				myInfo = (NetInfo) current;
			} catch (Exception exception) {
			}

			if (myInfo != null) {
				// Add the returned items to our collector
				Object[] references = myInfo.getReferencedElements();
				for (int i = 0; i < references.length; ++i)
					collectedItems.add(references[i]);
			}
		}
		return collectedItems.toArray();
	};
}
