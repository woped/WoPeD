package WorldModelToPetrinet;

import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XORJoin {
    String transID;
    private int sourceCount;
    private String originID;
    private ArrayList<Transition> joins= new ArrayList<Transition>();

    public XORJoin(int sourceCount,String originID){
        this.sourceCount=sourceCount;
        this.originID=originID;
        transID = new Transition("",false,false,"").getTransID();
        for(int i=0;i<sourceCount;i++){
            Transition source = new Transition("",false,true,"");
            source.setPartOfGateway(i+1,transID);
            source.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
            source.setOrientationCode(3);
            joins.add(source);
        }
    }

    public void addXORJoinToPetriNet(PetriNet petriNet,List<Place> sources, Place target){

        Iterator<Place> i = sources.iterator();
        int j=0;
        while(i.hasNext()){
            Place p = i.next();
            petriNet.add(new Arc(p.getPlaceID(),joins.get(j).getTransID(),originID));
            petriNet.add(joins.get(j));
            petriNet.add(new Arc(joins.get(j).getTransID(),target.getPlaceID(),originID));
            j++;
        }

    }
}
