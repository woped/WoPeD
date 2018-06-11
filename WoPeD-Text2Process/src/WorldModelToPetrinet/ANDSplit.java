package WorldModelToPetrinet;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.Iterator;
import java.util.List;

public class ANDSplit {

    Transition split;
    String originID;
    public ANDSplit(String text, boolean hasResource, String originID){
        this.originID=originID;
        split=new Transition(text,hasResource,true,originID);
        split.setPartOfGateway(1,split.getTransID());
        split.setOperatorType(OperatorTransitionModel.AND_SPLIT_TYPE);

    }
    public void addANDSplitToPetriNet(PetriNet petriNet, Place source, List<Place> targets){
        petriNet.add(new Arc(source.getPlaceID(),split.getTransID(),originID));
        Iterator<Place> i = targets.iterator();
        while(i.hasNext()){
            Place p = i.next();
            petriNet.add(new Arc(split.getTransID(),p.getPlaceID(),originID));
        }
        petriNet.add(split);
    }
}
