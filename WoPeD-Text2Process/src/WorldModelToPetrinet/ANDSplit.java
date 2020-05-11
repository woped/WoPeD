package WorldModelToPetrinet;

import java.util.Iterator;
import java.util.List;

public class ANDSplit extends PetrinetGateway {

    Transition split;
    String originID;
    public ANDSplit(String text, boolean hasResource, String originID,PetrinetElementBuilder elementBuilder){
        super(elementBuilder);
        this.originID=originID;
        split=elementBuilder.createTransition(text,hasResource,true,originID); //new Transition(text,hasResource,true,originID);
        split.setPartOfGateway(1,split.getID());
        split.setOperatorType(101); // AND-SPLIT in PNML transition type

    }
    public void addANDSplitToPetriNet(PetriNet petriNet, Place source, List<Place> targets){
        petriNet.add(elementBuilder.createArc(source.getID(),split.getID(),originID));
        Iterator<Place> i = targets.iterator();
        while(i.hasNext()){
            Place p = i.next();
            petriNet.add(elementBuilder.createArc(split.getID(),p.getID(),originID));
        }
        petriNet.add(split);
    }
}
