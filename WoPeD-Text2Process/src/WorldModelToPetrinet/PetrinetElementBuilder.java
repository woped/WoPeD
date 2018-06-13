package WorldModelToPetrinet;

public class PetrinetElementBuilder {

    private IDHandler transitionIDH, placeIDH, arcIDH;

    public PetrinetElementBuilder(){
        transitionIDH= new IDHandler(1);
        placeIDH = new IDHandler(1);
        arcIDH= new IDHandler(1);

    }

    public Place createPlace(boolean hasMarking, String originID){
        Place p = new Place(hasMarking, originID);
        return p;
    }

    public Transition createTransition(String text, boolean hasResource, boolean isGateway, String originID){
        Transition t = new Transition(text,hasResource,isGateway,originID);
        return t;
    }

    public Arc createArc(String source, String target, String originID){
        Arc a = new Arc(source, target, originID);
        return a;
    }

}
