package WorldModelToPetrinet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import worldModel.Action;
import worldModel.Flow;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;
import worldModel.WorldModel;

public class PetrinetBuilder {
    private WorldModel processWM;
    private List<Flow> flowsFromWM = new ArrayList<Flow>();
    private List<Action> actionsFromWM = new ArrayList<Action>();
    private List<Transition> transitionsFromWM = new ArrayList<Transition>();
    private List<Place> places = new ArrayList<Place>();
    private List<String> xml = new ArrayList<String>();
    private Iterator<Flow> iteratorFlows;
    private Iterator<Action> iteratorActions;
    private Iterator<Transition> iteratorTransitions;
    private Iterator<Place> iteratorPlaces;
    private Transition transition;
    private Place place;
    private Arc arc;

    public PetrinetBuilder(WorldModel processWM) {
        this.processWM = processWM;
    }

    public String buildPNML() {

        /*
         * Also kurz dazu, wie ich es mir gedacht habe: Diese Klasse baut das XML zusammen, indem zuerst die Transitions, dann die Places und dann die Arcs erzeugt werden.
         * Die Reihenfolge beim erzeugen: 1. Transitions, dann 2. Places und 3. Arcs ist auch wichtig, sonst geht es nicht, weil es aufeinander aufbaut.
         * Sowohl die Transitions als auch die Places werden in einer Liste zwischengespeichert.
         *
         * Die getArcs()-Methode geht jetzt so vor: Ich hol mir die 3 flows aus dem WorldModel. Dann pr¸fe ich f¸r die flows, ob sie einen oder mehrere Nachfolger besitzen.
         *
         * Wenn der flow einen Nachfolger besitzt, dann erstelle ich eine Sequenz, indem ich eine Stelle, dann eine Transition und dann nochmals eine Stelle erzeuge und
         * alles durch arcs verbinde. Ich merke mir am Ende, dass die Stelle das letzte Element ist, um mit der n‰chsten Transition daran anschlieﬂen zu kˆnnen.
         *
         * Wenn der flow mehrere Nachfolger besitzt, dann erstelle ich ein XOR-Split, indem ich zwei Transitions erzeuge und beide jeweils mit der vorherigen Stelle (die ich
         * mir gemerkt habe) verbinde. F¸r jede Transition erstelle ich auch eine nachfolgende Stelle und merke mir sie wieder f¸r die n‰chsten Transitions.
         * Alles wird wieder durch arcs verbunden.
         *
         * Was fehlt: Transitions nach dem XOR richtig an die 2 places, die ich mir gemerkt habe, anh‰ngen. Auﬂerdem sollten wieder nachfolgende places
         * erstellt + gemerkt werden und alles durch arcs verbunden werden.
         * */

        return getTransitionsFromWM() + getPlaces() + getArcs();
    }

    // Get XML String with all transitions
    private String getTransitionsFromWM() {

        flowsFromWM = processWM.getFlows();
        iteratorFlows = flowsFromWM.iterator();

        while (iteratorFlows.hasNext()) {

            Flow nextFlow = iteratorFlows.next();

            actionsFromWM = nextFlow.getMultipleObjects();
            iteratorActions = actionsFromWM.iterator();

            while (iteratorActions.hasNext()) {
                Action actionFromFlow = iteratorActions.next();

                // Gateway
                if (actionFromFlow.getMarker() != null) {

                    // Generate 2 times
                    transition = new Transition(actionFromFlow.getName(), false, true);
                    transition.setTriggerType(104);
                    transitionsFromWM.add(transition);
                    xml.add(transition.toString());
                    transition = new Transition(actionFromFlow.getName(), false, true);
                    transition.setTriggerType(104);

                }
                // Transition with RoleName
                else if (actionFromFlow.getActorFrom() != null) {

                    transition = new Transition(actionFromFlow.getName(), true, false);
                    if (actionFromFlow.getActorFrom().getReference() == null) {
                        transition.setRoleName(actionFromFlow.getActorFrom().getName());
                    } else {
                        transition.setRoleName(actionFromFlow.getActorFrom().getReference().getName());
                    }

                }
                // Transition without RoleName
                else {
                    transition = new Transition(actionFromFlow.getName(), false, false);
                }

                // XML String from Transition
                transitionsFromWM.add(transition);
                xml.add(transition.toString());

            }

        }

        return xml.toString();
    }

    // Get XML String with all places
    private String getPlaces() {

        int amountOfTransitions = transitionsFromWM.size();
        int i = 1;

        while (i <= (amountOfTransitions + 1)) {

            // hasMarking -> false
            place = new Place(false);
            places.add(place);

            // hasMarking -> true (missing)...

            i++;
        }
        return places.toString();
    }

    // Get XML String with all arcs
    private String getArcs() {

        flowsFromWM = processWM.getFlows();
        iteratorFlows = flowsFromWM.iterator();
        iteratorPlaces = places.iterator();
        iteratorTransitions = transitionsFromWM.iterator();
        List<String> finalXML = new ArrayList<String>();
        Transition currentTransition;
        Place currentPlace;
        List<Place> lastElement = new ArrayList<Place>();

        while (iteratorFlows.hasNext()) {

            // Get next flow
            Flow nextFlow = iteratorFlows.next();

            // check if flow has multiple Followers
            boolean hasMultipleFollowers = nextFlow.getMultipleObjects().size() > 1;

            // just one follower = Sequence
            if (hasMultipleFollowers == false) {


                System.out.println(nextFlow.getMultipleObjects());

                if(lastElement.isEmpty()) {
                    // get place from list
                    currentPlace = iteratorPlaces.next();
                    finalXML.add(currentPlace.toString());
                }else{
                    currentPlace = lastElement.get(0);
                }


                // get transition from list
                currentTransition = iteratorTransitions.next();
                finalXML.add(currentTransition.toString());

                // generate arc between place and transition
                arc = new Arc(currentPlace.placeID, currentTransition.getTransID());
                finalXML.add(arc.toString());

                // get place
                currentPlace = iteratorPlaces.next();
                finalXML.add(currentPlace.toString());

                // create arc between transition and place
                // note that last element is a place
                lastElement.add(currentPlace);
                arc = new Arc(currentTransition.getTransID(), currentPlace.placeID);
                finalXML.add(arc.toString() + "\n");

            } else { // flow has multiple followers

                System.out.println(nextFlow.getMultipleObjects());

                // check if last element is a place
                String last = lastElement.get(0).getPlaceID();
                if (last.contains("p")) {

                    // get transition from list
                    currentTransition = iteratorTransitions.next();
                    finalXML.add(currentTransition.toString());

                    // create arc between last element (place) and transition
                    arc = new Arc(last, currentTransition.getIdGateway());
                    finalXML.add(arc.toString());

                    // get place from list
                    currentPlace = iteratorPlaces.next();
                    finalXML.add(currentPlace.toString());

                    // create arc between transition and place
                    arc = new Arc(currentTransition.getIdGateway(), currentPlace.getPlaceID());
                    finalXML.add(arc.toString());

                    //note last element
                    lastElement.remove(0);
                    lastElement.add(currentPlace);

                    // 2 times because we need two transitions for the gateway
                    currentTransition = iteratorTransitions.next();
                    finalXML.add(currentTransition.toString());

                    arc = new Arc(last, currentTransition.getIdGateway());
                    finalXML.add(arc.toString());

                    currentPlace = iteratorPlaces.next();
                    finalXML.add(currentPlace.toString());

                    arc = new Arc(currentTransition.getIdGateway(), currentPlace.getPlaceID());
                    finalXML.add(arc.toString());

                    //note last element
                    lastElement.add(currentPlace);

                    //System.out.println(finalXML.toString());

                }

            }

        }
        return finalXML.toString();//"";// arcs.toString() + "\n" + transitionsFromWM.toString() + "\n" + places.toString();
    }
}
