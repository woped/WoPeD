package dataModel.pnmlReader;

import dataModel.pnmlReader.PetriNet.Element;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.process.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PetriNetToProcessConverter {
    private HashMap<String, Integer> transformedElems;
    public HashMap<Integer, String> transformedElemsRev;

    private String loopSet[] = new String[100];
    private int xor_split;
    private int xor_join;
    private int and_join;
    private int and_split;
    private int transitions;
    private int places;
    String types[] = new String[100];

    public ProcessModel convertToProcess(PetriNet petriNet) {

        ProcessModel model = new ProcessModel();
        Pool pool = new Pool("");
        Lane lane = new Lane("");
        model.addPool("");
        model.addLane("");

        transformedElems = new HashMap<>();
        transformedElemsRev = new HashMap<>();

        Element startElem = petriNet.getElements().get(petriNet.getStartPlace());
        transformElem(startElem, -1, petriNet, model, pool, lane);
        return model;
    }


    private int x = 0;
    private static String xorTitle;

    private void transformElem(Element elem, int precElem, PetriNet petriNet, ProcessModel model, Pool pool, Lane lane) {
        // Id of current petri net element
        String elemId = elem.getId();
        String elemType = "";
        // If element not already ecists
        if (!transformedElems.keySet().contains(elemId)) {

            // Places ...
            if (elem.getClass().toString().endsWith("Place")) {
                places++;

                // Simple place with 1 or more incoming and no outgoing arc
                if (petriNet.getSuccessor(elemId).size() == 0 && petriNet.getPredecessor(elemId).size() == 1) {
                    loopSet[x] = elemId + ": simple place, no ougoing arc";
                    x++;
                    int newId = model.getNewId();
                    model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
                    transformedElems.put(elemId, newId);
                    transformedElemsRev.put(newId, elemId);
                    if (precElem != -1) {
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                    }
                }

                // Simple place with 1 incoming and one outgoing arc
                if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() <= 1) {
                    loopSet[x] = elemId + ": simple place, one incoming, one outgoing arc";
                    x++;
                    String suc = petriNet.getSuccessor(elemId).get(0);
                    transformElem(petriNet.getElements().get(suc), precElem, petriNet, model, pool, lane);
                }

                //  Place with multiple outgoing arcs (XOR-Join)
                if (petriNet.getSuccessor(elemId).size() >= 0 && petriNet.getPredecessor(elemId).size() > 1) {
                    xor_join++;
                    loopSet[x] = elemId + ": XOR Join";
                    x++;
                    // Create new element
                    int newId = model.getNewId();
                    model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
                    transformedElems.put(elemId, newId);
                    transformedElemsRev.put(newId, elemId);
                    if (precElem != -1) {
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                    }

                    //if it is 0, there is no successor ...
                    if (!(petriNet.getSuccessor(elemId).size() == 0)) {
                        // Recursively go through the model
                        String suc = petriNet.getSuccessor(elemId).get(0);
                        transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                    } else {
                        //PetriNet ends with a XOR-Join
                        int newId3 = model.getNewId();
                        model.addGateway(new Gateway(newId3, "", lane, pool, GatewayType.XOR));
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(newId), model.getElem(newId3)));

                    }
                }

                // Place with multiple incoming arcs (XOR-Split)
                if (petriNet.getSuccessor(elemId).size() > 1 && petriNet.getPredecessor(elemId).size() >= 0) {
                    xor_split++;
                    loopSet[x] = elemId + ": XOR Split";
                    x++;
                    //Check XOR-Type
                    boolean checkXOR = false;
                    ArrayList<String> pr = petriNet.getSuccessor(elemId);
                    for (String aPr : pr) {
                        if (aPr.contains("op")) {
                            checkXOR = true;
                        }
                    }
                    if (checkXOR) {
                        //WoPeD XOR!! --> WoPeD specific operator
                        //Get Label from Successor --> XOR Label
                        String sucId = pr.get(0);
                        HashMap<String, Element> elems = petriNet.getElements();
                        Element sucXOR = elems.get(sucId);
                        String label = sucXOR.getLabel();
                        xorTitle = label;
                        //Add Activity
                        int newId = model.getNewId();
                        model.addActivity(new Activity(newId, label, null, null, ActivityType.NONE));
                        if (precElem != -1) {
                            model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                        }
                        // Add Gateway
                        int newId2 = model.getNewId();
                        model.addGateway(new Gateway(newId2, "", lane, pool, GatewayType.XOR));
                        transformedElems.put(elemId, newId2);
                        transformedElemsRev.put(newId2, elemId);
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(newId), model.getElem(newId2)));

                        // Recursively go through the model
                        for (String suc : petriNet.getSuccessor(elemId)) {
                            transformElem(petriNet.getElements().get(suc), newId2, petriNet, model, pool, lane);
                        }

                    } else {
                        //Normal XOR --> without WoPeD XOR
                        // Create new element
                        int newId = model.getNewId();
                        model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
                        transformedElems.put(elemId, newId);
                        transformedElemsRev.put(newId, elemId);
                        if (precElem != -1) {
                            model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                        }
                        // Recursively go through the model
                        for (String suc : petriNet.getSuccessor(elemId)) {
                            transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                        }
                    }


                }

                // Transitions...
            } else {
                transitions++;
                // Simple transition with 1 incoming and one outgoing arc
                if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() == 1) {
                    loopSet[x] = elemId + ": simple transition, one incoming, one outgoing arc, Type: " + elem.getType() + " ,role: " + elem.getRole();
                    x++;

                    //Role in Text --> Role in Petrinet = Lane in BPMN
                    Pool roleAsPool = new Pool(elem.getRole());
                    Lane roleAsLane = new Lane(elem.getRole());

                    if (elem.getRole().equals("none")) {
                        roleAsPool = null;
                        roleAsLane = null;
                    }

                    if (elem.getType() != null && elem.getType().equals("104")) {
                        //XOR-Split Transition
                        //WoPeD Transition, created by splitting specific XOR-Operator in several transitions

                        //Get Label from Successor --> Place Label
                        ArrayList<String> pr = petriNet.getSuccessor(elemId);
                        String sucId = pr.get(0);
                        HashMap<String, Element> elems = petriNet.getElements();
                        Element sucXOR = elems.get(sucId);
                        String label2 = sucXOR.getLabel();

                        int newId = model.getNewId();
                        model.addActivity(new Activity(newId, xorTitle + ": " + label2, roleAsLane, roleAsPool, ActivityType.NONE));
                        transformedElems.put(elemId, newId);
                        transformedElemsRev.put(newId, elemId);
                        if (precElem != -1) {
                            model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                        }

                        // Recursively go through the model
                        for (String suc : petriNet.getSuccessor(elemId)) {
                            transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                        }
                    } else {
                        //Normal Transition
                        int newId = model.getNewId();
                        String label = elem.getLabel();
                        model.addActivity(new Activity(newId, label, roleAsLane, roleAsPool, ActivityType.NONE));
                        transformedElems.put(elemId, newId);
                        transformedElemsRev.put(newId, elemId);
                        if (precElem != -1) {
                            model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                        }

                        // Recursively go through the model
                        for (String suc : petriNet.getSuccessor(elemId)) {
                            transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                        }
                    }
                }

                //  Transition with multiple incoming arcs (AND-Join)
                if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() > 1) {
                    loopSet[x] = elemId + ": AND Join";
                    and_join++;
                    // Create new element
                    int newId = model.getNewId();
                    model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.AND));
                    transformedElems.put(elemId, newId);
                    transformedElemsRev.put(newId, elemId);
                    if (precElem != -1) {
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                    }

                    // Recursively go through the model
                    String suc = petriNet.getSuccessor(elemId).get(0);
                    transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                }

                //  Transition with multiple incoming arcs (AND-Split)
                if (petriNet.getSuccessor(elemId).size() > 1 && petriNet.getPredecessor(elemId).size() == 1) {
                    loopSet[x] = elemId + ": AND Split";
                    and_split++;
                    // Create new element
                    int newId = model.getNewId();
                    model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.AND));
                    transformedElems.put(elemId, newId);
                    transformedElemsRev.put(newId, elemId);
                    if (precElem != -1) {
                        model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
                    }

                    // Recursively go through the model
                    for (String suc : petriNet.getSuccessor(elemId)) {
                        transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
                    }
                }
            }
        } else {
            model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(transformedElems.get(elem.getId()))));
        }

    }

    public void printConversion() {
        for (int i = loopSet.length; i > 0; i--) {
            if (loopSet[i - 1] != null) {
                System.out.println(i + ". Element: " + loopSet[i - 1]);
            }
        }
        System.out.println("Places: " + places);
        System.out.println("Transition " + transitions);
        System.out.println("XOR-Splits: " + xor_split);
        System.out.println("XOR-Joins: " + xor_join);
        System.out.println("AND-Splits: " + and_split);
        System.out.println("AND-Joins: " + and_join);

    }
}