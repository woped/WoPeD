package WorldModelToPetrinet;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.Iterator;
import java.util.List;

public class ANDJoin {

    Transition join;
    String originID;
    public ANDJoin(String text, boolean hasResource, String originID){
        this.originID=originID;
        join=new Transition(text,hasResource,true,originID);
        join.setPartOfGateway(1,join.getTransID());
        join.setOperatorType(OperatorTransitionModel.AND_JOIN_TYPE);
        join.setOrientationCode(3);

    }
    public void addANDJoinToPetriNet(PetriNet petriNet, List<Place> sources, Place target){
        petriNet.add(new Arc(join.getTransID(), target.getPlaceID(),originID));
        Iterator<Place> i = sources.iterator();
        while(i.hasNext()){
            Place p = i.next();
            petriNet.add(new Arc(p.getPlaceID(),join.getTransID(),originID));
        }
        petriNet.add(join);
    }

}
