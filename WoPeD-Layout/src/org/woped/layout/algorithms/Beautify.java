package org.woped.layout.algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;

public class Beautify {
	public static void findSources(ModelElementContainer mec) {

		System.out.println("findSources");
		Map<String, ArcModel> elements = mec.getArcMap();
		Iterator elementsIter = mec.getIdMap().keySet().iterator();
		ArcModel element;
		while (elementsIter.hasNext()) {
			System.out.println("A");
			element = elements.get(elementsIter.next());
			if (element != null) {
				System.out.println( "Edge " + element.getSourceId() + " - " + element.getTargetId());
			}
			
		}
	}

}
