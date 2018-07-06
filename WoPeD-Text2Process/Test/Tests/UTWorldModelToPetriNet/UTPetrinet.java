package Tests.UTWorldModelToPetriNet;

import Tests.T2PUnitTest;
import WorldModelToPetrinet.*;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class UTPetrinet extends T2PUnitTest {

    /*Unit test for Class WorldModelToPetrinet.PetriNet*/

    @Test
    public void evaluatePetriNet() {
        assertEquals("Referencing arcs for Elements are not extracted correctly.",true,evluateGetReferencingArcs());
        assertEquals("Workflownet Transformation was applied incorrectly.",true,evaluateTransformToWorkflowNet());
    }

    private boolean evluateGetReferencingArcs(){
        PetriNet pn = new PetriNet();
        Place[] places = createAndAddPlaces(4,pn);
        Transition[] transitions = createAndAddTransitions(2,pn);
        pn.add(pn.getElementBuilder().createArc(places[0].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(places[1].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(places[2].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(transitions[0].getID(),places[3].getID(),""));


        boolean all = pn.getAllReferencingArcsForElement(transitions[0].getID(), PetriNet.REFERENCE_DIRECTION.all).size()==4;
        boolean ingoing = pn.getAllReferencingArcsForElement(transitions[0].getID(),PetriNet.REFERENCE_DIRECTION.ingoing).size()==3;
        boolean outgoing = pn.getAllReferencingArcsForElement(transitions[0].getID(),PetriNet.REFERENCE_DIRECTION.outgoing).size()==1;
        boolean none = pn.getAllReferencingArcsForElement(transitions[1].getID(),PetriNet.REFERENCE_DIRECTION.all).size()==0;
        return all && ingoing && outgoing;
    }

    private boolean evaluateTransformToWorkflowNet(){
        //Create a Petrinet containing multiple sources/sinks and dangling transitions
        PetriNet pn = new PetriNet();
        Place[] places = createAndAddPlaces(5,pn);
        Transition[] transitions = createAndAddTransitions(2,pn);
        pn.add(pn.getElementBuilder().createArc(places[0].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(places[1].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(places[2].getID(),transitions[0].getID(),""));
        pn.add(pn.getElementBuilder().createArc(transitions[0].getID(),places[3].getID(),""));
        pn.add(pn.getElementBuilder().createArc(transitions[0].getID(),places[4].getID(),""));
        pn.add(pn.getElementBuilder().createArc(places[4].getID(),transitions[1].getID(),""));
        pn.transformToWorkflowNet();

        //check absence of dangling Transitions
        boolean hasDanglingTransitions = false;
        Iterator<Transition> j = pn.getTransitionList().iterator();
        while(j.hasNext()){
            Transition t = j.next();
            if(pn.getAllReferencingArcsForElement(t.getID(), PetriNet.REFERENCE_DIRECTION.ingoing).size()==0
                    || pn.getAllReferencingArcsForElement(t.getID(), PetriNet.REFERENCE_DIRECTION.outgoing).size()==0 ){
                hasDanglingTransitions=true;
            }
        }

        //check Source-Sink Property
        Iterator<Place> i = pn.getPlaceList().iterator();
        int sourceCount=0;
        int sinkCount=0;
        while(i.hasNext()){
            Place p=i.next();
            if(pn.getAllReferencingArcsForElement(p.getID(), PetriNet.REFERENCE_DIRECTION.ingoing).size()==0){
                sourceCount++;
            }
            if(pn.getAllReferencingArcsForElement(p.getID(), PetriNet.REFERENCE_DIRECTION.outgoing).size()==0){
                sinkCount++;
            }
        }
        boolean sourceSinkNet= sinkCount==1 && sourceCount ==1;

        //Reapply the logic to Petrinet enhanced by Workflownet Properties, to see if it remains unchanged -> check via Metadata
        pn.transformToWorkflowNet();

        boolean placeCount= pn.getPlaceList().size()==8;
        boolean transitionCount= pn.getTransitionList().size()==5;
        boolean arcCount= pn.getArcList().size()==15;

        return sourceSinkNet && placeCount &&(!hasDanglingTransitions)&& transitionCount&&arcCount;
    }

    private Place [] createAndAddPlaces(int placeCount, PetriNet pn){
        Place [] places = new Place [placeCount];
        for(int i=0;i<placeCount;i++){
            places[i]= pn.getElementBuilder().createPlace(false,"");
            pn.add(places[i]);
        }
        return places;
    }

    private Transition [] createAndAddTransitions(int transitionCount, PetriNet pn){
        Transition [] transitions = new Transition [transitionCount];
        for(int i=0;i<transitionCount;i++){
            transitions[i]= pn.getElementBuilder().createTransition("",false,false,"");
            pn.add(transitions[i]);
        }
        return transitions;
    }

}
