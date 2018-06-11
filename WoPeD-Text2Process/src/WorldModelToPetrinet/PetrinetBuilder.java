package WorldModelToPetrinet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import transform.DummyAction;
import worldModel.*;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;

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

    private PetriNet petriNet;

    public PetrinetBuilder(WorldModel processWM) {
        this.processWM = processWM;
        petriNet= new PetriNet();
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
        buildPetriNetfromWM();
        String PNML= petriNet.getPNML();

        return PNML;
    }

    private void buildPetriNetfromWM(){
        flowsFromWM = processWM.getFlows();
        iteratorFlows = flowsFromWM.iterator();
        while (iteratorFlows.hasNext()) {
            Flow nextFlow = iteratorFlows.next();
            if(nextFlow.getMultipleObjects().size()==1){
                createSequence(nextFlow);
            }else{
                //Gateway
                if(nextFlow.getDirection().equals(FlowDirection.split)){
                    createSplit(nextFlow);
                }
                if(nextFlow.getDirection().equals(FlowDirection.join)){
                   createJoin(nextFlow);
                }
            }
        }

        // Sanitize to Workflownet
        List<Place> sinks = petriNet.getAllSinks();
        if(sinks.size()>1){
            petriNet.unifySinks(sinks);
        }
        else{
            sinks.get(0).setText("end");
        }
        List<Place> sources = petriNet.getAllSources();
        if(sources.size()>1){
            petriNet.unifySources(sources);
        }else{
            sources.get(0).setText("start");
            sources.get(0).hasMarking=true;
        }


    }


    private void createJoin(Flow f){

        List<Place> sources=  getSourcePlaceForJoinFlow(f);

        Place target=new Place(false,getOriginID(f.getSingleObject()));
        petriNet.add(target);
        Transition t = createTransition(f.getSingleObject(),false);
        petriNet.add(t);

        Place singletarget = new Place(false,getOriginID(f.getSingleObject()));
        petriNet.add(singletarget);
        petriNet.add(new Arc(target.getPlaceID(),t.getTransID(),getOriginID(f.getSingleObject())));
        petriNet.add(new Arc(t.getTransID(),singletarget.getPlaceID(),getOriginID(f.getSingleObject())));


        if(f.getType().equals(FlowType.concurrency)){
            ANDJoin aj = new ANDJoin("",false,"");
            aj.addANDJoinToPetriNet(petriNet,sources,target);
        }else{
            XORJoin xj = new XORJoin(sources.size(),"");
            xj.addXORJoinToPetriNet(petriNet,sources,target);
        }
    }

    private boolean isDummyAction(Action a){
        return a.getName().equals("Dummy Node");
    }

    private String generateTransitionLabel(Action a) {
        String transitionlabel="";
        if(isDummyAction(a)){
            transitionlabel+="";
        }else {
            if (a.getMarker() != null)
                transitionlabel += a.getMarker() + " ";
            transitionlabel += a.getVerb() + " ";
            if (a.getCop() != null)
                transitionlabel += a.getCop();
        }
        return transitionlabel;
    }
    private String getOriginID(SpecifiedElement element){
        if(isDummyAction((Action) element)){
            DummyAction d= (DummyAction) element;
            return ""+d.getDummyID();
        }else{
            int sentenceID = element.getOrigin().getID();
            int wordIndex = element.getWordIndex();
            String originID= "Dummy"+sentenceID+wordIndex;
            return originID;
        }

    }

    private boolean artifactsExist(Action a){
        Iterator<Place> i = petriNet.getPlaceList().iterator();
        while(i.hasNext()){
            if(i.next().getOriginID().equals(getOriginID(a))){
                return true;
            }
        }
        return false;
    }

    private Place getExistingPlace(Action a){
        Iterator<Place> i = petriNet.getPlaceList().iterator();
        while(i.hasNext()){
            Place p = i.next();
            if(p.getOriginID().equals(getOriginID(a))){
                return p;
            }
        }
        //if not found
        return null;
    }

    private Place getSourcePlaceForSplitFlow(Flow f){
            Place p;
            if(artifactsExist(f.getSingleObject())){
                p= getExistingPlace(f.getSingleObject());
            }else{
                p=new Place(false,getOriginID(f.getSingleObject()));
                petriNet.add(p);
            }
      //  }
        return p;
    }

    private List<Place> getSourcePlaceForJoinFlow(Flow f){
        ArrayList<Place> sources= new ArrayList<Place>();
        Iterator<Action> i = f.getMultipleObjects().iterator();
        while(i.hasNext()){
            Action a= i.next();
            Place p;

                if(artifactsExist(a)){
                    p= getExistingPlace(a);
                }else{
                    p=new Place(false,getOriginID(a));
                    petriNet.add(p);
            }
            sources.add(p);
        }

        return sources;
    }

    private Transition createTransition(Action a, boolean isGateway){
        boolean hasResource =a.getObject() != null;
        Transition t = new Transition(generateTransitionLabel(a),hasResource,isGateway, getOriginID(a));
        if(a.getObject()!=null)
            t.setResourceName(a.getObject().getName());
        if(a.getActorFrom()!=null){
            if(a.getActorFrom().getReference()!=null){
                if(a.getActorFrom().getReference().getSpecifiers(Specifier.SpecifierType.NN).size()>0){
                    t.setOrganizationalUnitName(a.getActorFrom().getReference().getSpecifiers(Specifier.SpecifierType.NN).get(0).getName());
                }else{
                    t.setOrganizationalUnitName(a.getActorFrom().getReference().getName());
                }
                t.setRoleName(a.getActorFrom().getReference().getName());
            }else if(a.getActorFrom()!=null){
                if(a.getActorFrom().getSpecifiers(Specifier.SpecifierType.NN).size()>0){
                    t.setOrganizationalUnitName(a.getActorFrom().getSpecifiers(Specifier.SpecifierType.NN).get(0).getName());
                }else{
                    t.setOrganizationalUnitName(a.getActorFrom().getName());
                }

                t.setRoleName(a.getActorFrom().getName());
            }
        }


        return t;
    }

    private void createSequence(Flow f){

        //check refers (apparently) to a bug during WorldModel Creation: Sequences among Equal Actions are created -> Inconsistency
        if(!getOriginID(f.getMultipleObjects().get(0)).equals(getOriginID(f.getSingleObject()))){
            boolean initiallyFound = artifactsExist(f.getSingleObject());
            Place p1= getSourcePlaceForSplitFlow(f);
            if(!initiallyFound){
                Place p0= new Place(false,"");
                petriNet.add(p0);
                Transition t0= createTransition(f.getSingleObject(),false);
                petriNet.add(t0);
                petriNet.add(new Arc(p0.getPlaceID(),t0.getTransID(),""));
                petriNet.add(new Arc(t0.getTransID(),p1.getPlaceID(),getOriginID(f.getSingleObject())));
            }
            Transition t1= createTransition(f.getMultipleObjects().get(0),false);
            petriNet.add(t1);
            Place p2 = new Place(false,getOriginID(f.getMultipleObjects().get(0)));
            petriNet.add(p2);
            Arc a1 = new Arc(p1.getPlaceID(),t1.getTransID(),getOriginID(f.getMultipleObjects().get(0)));
            Arc a2 = new Arc(t1.getTransID(),p2.getPlaceID(),getOriginID(f.getMultipleObjects().get(0)));
            petriNet.add(a1);
            petriNet.add(a2);
        }
    }

    private void createSplit(Flow f){
        Place p1= getSourcePlaceForSplitFlow(f);
        List<Action> splitList = f.getMultipleObjects();
        ArrayList<Place> targetPlaces = new ArrayList<Place>();
        Iterator<Action> i = splitList.iterator();
        //Place targetPlace = new Place(false,getOriginID(f.getSingleObject()));
        while(i.hasNext()){

            Action a = i.next();
            Place targetPlace = new Place(false,"");
            targetPlaces.add(targetPlace);
            petriNet.add(targetPlace);

            Transition t = createTransition(a,false);
            petriNet.add(t);
            Place p2= new Place(false, getOriginID(a));
            petriNet.add(p2);
            petriNet.add(new Arc(targetPlace.getPlaceID(),t.getTransID(),getOriginID(a)));
            petriNet.add(new Arc(t.getTransID(),p2.getPlaceID(),getOriginID(a)));
        }
        if(f.getType().equals(FlowType.concurrency)){
            ANDSplit as=new ANDSplit("",false,"");
            as.addANDSplitToPetriNet(petriNet,p1,targetPlaces);
        }else if(f.getType().equals(FlowType.choice)||f.getType().equals(FlowType.multiChoice) ||f.getType().equals(FlowType.iteration)){
            XORSplit xs = new XORSplit(splitList.size(),"");
            xs.addXORSplitToPetriNet(petriNet,p1,targetPlaces);
        }

    }


    // Get XML String with all transitions
    /*private String getTransitionsFromWM() {

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

        return xml.toString() .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();
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
        return places.toString() .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();
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
        return finalXML.toString() .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();//"";// arcs.toString() + "\n" + transitionsFromWM.toString() + "\n" + places.toString();
    }*/
}
