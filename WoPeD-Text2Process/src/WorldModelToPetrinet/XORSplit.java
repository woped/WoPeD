package WorldModelToPetrinet;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XORSplit extends PetrinetGateway {

    String transID;
    private int choiceCount;
    private String originID;
    private ArrayList<Transition> choices= new ArrayList<Transition>();

    public XORSplit(String label,int choiceCount,String originID, PetrinetElementBuilder elementBuilder){
        super(elementBuilder);
        this.choiceCount=choiceCount;
        this.originID=originID;
        transID = elementBuilder.createTransition("",false,false,"").getID();
        for(int i=0;i<choiceCount;i++){
            Transition choice = elementBuilder.createTransition(label,false,true,"");
            choice.setPartOfGateway(i+1,transID);
            choice.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
            choices.add(choice);
        }
    }

    public void addXORSplitToPetriNet(PetriNet petriNet, Place source, List<Place> targets){

        Iterator<Place> i = targets.iterator();
        int j=0;
        while(i.hasNext()){
            petriNet.add(elementBuilder.createArc(source.getID(),choices.get(j).getID(),originID));
            Place p = i.next();
            petriNet.add(elementBuilder.createArc(choices.get(j).getID(),p.getID(),originID));
            petriNet.add(choices.get(j));
            j++;
        }

    }

}
