package WorldModelToPetrinet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XORJoin extends PetrinetGateway {
    String transID;
    private int sourceCount;
    private String originID;
    private ArrayList<Transition> joins= new ArrayList<Transition>();

    public XORJoin(String label,int sourceCount,String originID, PetrinetElementBuilder elementBuilder){
        super(elementBuilder);
        this.sourceCount=sourceCount;
        this.originID=originID;
        transID = elementBuilder.createTransition("",false,false,"").getID();
        for(int i=0;i<sourceCount;i++){
            Transition source = elementBuilder.createTransition(label,false,true,"");
            source.setPartOfGateway(i+1,transID);
            source.setOperatorType(105); // XOR-JOIN in PNML transition type
            source.setOrientationCode(3);
            joins.add(source);
        }
    }

    public void addXORJoinToPetriNet(PetriNet petriNet,List<Place> sources, Place target){

        Iterator<Place> i = sources.iterator();
        int j=0;
        while(i.hasNext()){
            Place p = i.next();
            petriNet.add(elementBuilder.createArc(p.getID(),joins.get(j).getID(),originID));
            petriNet.add(joins.get(j));
            petriNet.add(elementBuilder.createArc(joins.get(j).getID(),target.getID(),originID));
            j++;
        }

    }
}
