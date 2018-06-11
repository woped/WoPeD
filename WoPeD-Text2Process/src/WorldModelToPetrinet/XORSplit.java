package WorldModelToPetrinet;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XORSplit {

    String transID;
    private int choiceCount;
    private String originID;
    private ArrayList<Transition> choices= new ArrayList<Transition>();

    public XORSplit(int choiceCount,String originID){
        this.choiceCount=choiceCount;
        this.originID=originID;
        transID = new Transition("",false,false,"").getTransID();
        for(int i=0;i<choiceCount;i++){
            Transition choice = new Transition("choice",false,true,"");
            choice.setPartOfGateway(i+1,transID);
            choice.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
            choices.add(choice);
        }
    }

    public void addXORSplitToPetriNet(PetriNet petriNet, Place source, List<Place> targets){

        Iterator<Place> i = targets.iterator();
        int j=0;
        while(i.hasNext()){
            petriNet.add(new Arc(source.getPlaceID(),choices.get(j).getTransID(),originID));
            Place p = i.next();
            petriNet.add(new Arc(choices.get(j).getTransID(),p.getPlaceID(),originID));
            petriNet.add(choices.get(j));
            j++;
        }

    }

}
