package org.woped.p2t.dataModel.pnmlReader.PetriNet;

import java.util.ArrayList;
import java.util.HashMap;

public class PetriNet {
    private final HashMap<String, Element> elements;
    private final HashMap<String, Arc> arcs;

    public PetriNet() {
        elements = new HashMap<>();
        arcs = new HashMap<>();
    }

    public HashMap<String, Element> getElements() {
        return elements;
    }

    public void addElements(Element element) {
        this.elements.put(element.getId(), element);
    }

    public HashMap<String, Arc> getArcs() {
        return arcs;
    }

    public void addArc(Arc arc) {
        this.arcs.put(arc.getId(), arc);
    }

    public String getStartPlace() {
        for (Element elem : elements.values()) {
            if (getPredecessor(elem.getId()).size() == 0) {
                return elem.getId();
            }
        }
        return "";
    }

    public ArrayList<String> getPredecessor(String id) {
        ArrayList<String> pres = new ArrayList<>();
        for (Arc arc : arcs.values()) {
            if (arc.getTarget().equals(id)) {
                pres.add(arc.getSource());
            }
        }
        return pres;
    }

    public ArrayList<String> getSuccessor(String id) {
        ArrayList<String> sucs = new ArrayList<>();
        for (Arc arc : arcs.values()) {
            if (arc.getSource().equals(id)) {
                sucs.add(arc.getTarget());
            }
        }
        return sucs;
    }
}