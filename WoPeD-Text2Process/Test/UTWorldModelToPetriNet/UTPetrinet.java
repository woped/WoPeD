package UTWorldModelToPetriNet;

import TextToWorldModel.transform.ElementsBuilder;
import WorldModelToPetrinet.*;
import org.junit.Test;

import java.util.Iterator;

public class UTPetrinet {

    /*Unit test for Class WorldModelToPetrinet.PetriNet*/

    @Test
    public void evaluatePetriNet() {
        PetriNet pn = new PetriNet();
        PetrinetElementBuilder elementBuilder = pn.getElementBuilder();

        Place p1 = elementBuilder.createPlace(false,"");
        Place p2 = elementBuilder.createPlace(false,"");
        Place p3 = elementBuilder.createPlace(false,"");
        Place p4 = elementBuilder.createPlace(false,"");

        pn.add(p1);
        pn.add(p2);
        pn.add(p3);
        pn.add(p4);
        pn.add(elementBuilder.createTransition("test",false,false,""));
        Place p5 = (Place) pn.getPetrinetElementByID("p1");
        Transition t5 =(Transition) pn.getPetrinetElementByID("t1");
        pn.add(elementBuilder.createArc(p1.getID(),t5.getID(),""));
        System.out.println(p5.getID());
        System.out.println(t5.getID());

        Iterator<Place> i = pn.getPlaceList().iterator();
        while(i.hasNext()){
            System.out.println(i.next().getID());
        }

        pn.removePetrinetElementByID("t1");
        pn.removePetrinetElementByID("a1");
        i = pn.getPlaceList().iterator();
        while(i.hasNext()){
            System.out.println(i.next().getID());
        }


    }
}
