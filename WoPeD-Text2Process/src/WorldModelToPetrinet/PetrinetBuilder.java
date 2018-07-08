package WorldModelToPetrinet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import TextToWorldModel.Constants;
import TextToWorldModel.ProcessLabelGenerator;
import TextToWorldModel.transform.DummyAction;
import worldModel.*;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;

public class PetrinetBuilder {

    public static final String DUMMY_PREFIX="DUMMY";
    private WorldModel processWM;
    private PetrinetElementBuilder elementBuilder;
    private List<Flow> flowsFromWM = new ArrayList<Flow>();
    private Iterator<Flow> iteratorFlows;


    private PetriNet petriNet;

    public PetrinetBuilder(WorldModel processWM) {
        this.processWM = processWM;
        petriNet= new PetriNet();
        elementBuilder=petriNet.getElementBuilder();
    }

    public PetriNet getPetriNet() {
        return petriNet;
    }


    public String buildPNML() throws PetrinetGenerationException {

        buildPetriNetfromWM();
        String PNML= petriNet.getPNML();

        return PNML;
    }

    private void buildPetriNetfromWM() throws PetrinetGenerationException {
        flowsFromWM = processWM.getFlows();
        iteratorFlows = flowsFromWM.iterator();
        while (iteratorFlows.hasNext()) {
            Flow nextFlow = iteratorFlows.next();
            if(nextFlow.getMultipleObjects().size()==1){
                createSequence(nextFlow);
            }else{
                //Gateway
                if(nextFlow.getDirection().equals(FlowDirection.split)){
                    createSplit(nextFlow);
                }
                if(nextFlow.getDirection().equals(FlowDirection.join)){
                   createJoin(nextFlow);
                }
            }
        }
        removeDummyNodes();
        petriNet.transformToWorkflowNet();

    }


    private void createJoin(Flow f){

        List<Place> sources=  getSourcePlaceForJoinFlow(f);

        Place target=elementBuilder.createPlace(false,getOriginID(f.getSingleObject()));
        petriNet.add(target);
        Transition t = createTransition(f.getSingleObject(),false);
        petriNet.add(t);

        Place singletarget = elementBuilder.createPlace(false,getOriginID(f.getSingleObject()));
        petriNet.add(singletarget);
        petriNet.add(elementBuilder.createArc(target.getID(),t.getID(),getOriginID(f.getSingleObject())));
        petriNet.add(elementBuilder.createArc(t.getID(),singletarget.getID(),getOriginID(f.getSingleObject())));


        if(f.getType().equals(FlowType.concurrency)){
            ANDJoin aj = new ANDJoin("",false,"",elementBuilder);
            aj.addANDJoinToPetriNet(petriNet,sources,target);
        }else{
            XORJoin xj = new XORJoin("",sources.size(),"",elementBuilder);
            xj.addXORJoinToPetriNet(petriNet,sources,target);
        }
    }

    private boolean isDummyAction(Action a){
        return a.getName().equals("Dummy Node");
    }

    private String getOriginID(SpecifiedElement element){
        if(isDummyAction((Action) element)){
            DummyAction d= (DummyAction) element;
            return DUMMY_PREFIX+d.getDummyID();
        }else{
            int sentenceID = element.getOrigin().getID();
            int wordIndex = element.getWordIndex();
            String originID= ""+sentenceID+wordIndex;
            return originID;
        }

    }

    private boolean artifactsExist(Action a){
        Iterator<Place> i = petriNet.getPlaceList().iterator();
        while(i.hasNext()){
            if(i.next().getOriginID().equals(getOriginID(a))){
                return true;
            }
        }
        return false;
    }

    private Place getExistingPlace(Action a){
        Iterator<Place> i = petriNet.getPlaceList().iterator();
        while(i.hasNext()){
            Place p = i.next();
            if(p.getOriginID().equals(getOriginID(a))){
                return p;
            }
        }
        //if not found
        return null;
    }

    private Place getSourcePlaceForSplitFlow(Flow f){
            Place p;
            if(artifactsExist(f.getSingleObject())){
                p= getExistingPlace(f.getSingleObject());
            }else{
                p=elementBuilder.createPlace(false,getOriginID(f.getSingleObject()));
                petriNet.add(p);
            }
        return p;
    }

    private List<Place> getSourcePlaceForJoinFlow(Flow f){
        ArrayList<Place> sources= new ArrayList<Place>();
        Iterator<Action> i = f.getMultipleObjects().iterator();
        while(i.hasNext()){
            Action a= i.next();
            Place p;

                if(artifactsExist(a)){
                    p= getExistingPlace(a);
                }else{
                    p= elementBuilder.createPlace(false,getOriginID(a));
                    petriNet.add(p);
            }
            sources.add(p);
        }

        return sources;
    }

    private Transition createTransition(Action a, boolean isGateway){
        boolean hasResource = a.getObject() != null;
        boolean hasRole = a.getActorFrom() != null;

        //Transition Text
        Transition t = elementBuilder.createTransition(a.getFinalLabel(),hasRole,isGateway, getOriginID(a));

        //Resource Text
        //TODO Refactor
        if(a.getObject()!=null)
            t.setResourceName(a.getObject().getName());

        //Org Text: auskommentiert, da nicht verlässlich
        //TODO Refactor
        if(a.getActorFrom()!= null){
            if(a.getActorFrom().getReference()!=null){
                if(a.getActorFrom().getReference().getSpecifiers(Specifier.SpecifierType.NN).size()>0){
                    //t.setOrganizationalUnitName(a.getActorFrom().getReference().getSpecifiers(Specifier.SpecifierType.NN).get(0).getName());
                }else{
                    //t.setOrganizationalUnitName(a.getActorFrom().getReference().getName());
                }
                t.setRoleName(a.getActorFrom().getReference().getName());
            }else if(a.getActorFrom()!=null){
                if(a.getActorFrom().getSpecifiers(Specifier.SpecifierType.NN).size()>0){
                    //t.setOrganizationalUnitName(a.getActorFrom().getSpecifiers(Specifier.SpecifierType.NN).get(0).getName());
                }else{
                    //t.setOrganizationalUnitName(a.getActorFrom().getName());
                }
                t.setRoleName(a.getActorFrom().getName());
            }
        }
        return t;
    }

    private void createSequence(Flow f){

        //check refers (apparently) to a bug during WorldModel Creation: Sequences among Equal Actions are created -> Inconsistency -> ignore
        if(!getOriginID(f.getMultipleObjects().get(0)).equals(getOriginID(f.getSingleObject()))){
            boolean initiallyFound = artifactsExist(f.getSingleObject());
            Place p1= getSourcePlaceForSplitFlow(f);
            if(!initiallyFound){
                Place p0=elementBuilder.createPlace(false,"");
                petriNet.add(p0);
                Transition t0= createTransition(f.getSingleObject(),false);
                petriNet.add(t0);
                petriNet.add(elementBuilder.createArc(p0.getID(),t0.getID(),""));
                petriNet.add(elementBuilder.createArc(t0.getID(),p1.getID(),getOriginID(f.getSingleObject())));
            }
            Transition t1= createTransition(f.getMultipleObjects().get(0),false);
            petriNet.add(t1);
            Place p2 =elementBuilder.createPlace(false,getOriginID(f.getMultipleObjects().get(0)));
            petriNet.add(p2);
            Arc a1 =elementBuilder.createArc(p1.getID(),t1.getID(),getOriginID(f.getMultipleObjects().get(0)));
            Arc a2 =elementBuilder.createArc(t1.getID(),p2.getID(),getOriginID(f.getMultipleObjects().get(0)));
            petriNet.add(a1);
            petriNet.add(a2);
        }
    }

    private boolean checkIfCanBeChoiceLabel(Action splitAction){
        if(splitAction.getMarker()!=null){
            if(splitAction.getMarker().equals("if")){
                return true;
            }
        }
        return false;
    }

    private void createSplit(Flow f){
        Place p1= getSourcePlaceForSplitFlow(f);
        List<Action> splitList = f.getMultipleObjects();
        ArrayList<Place> targetPlaces = new ArrayList<Place>();
        Iterator<Action> i = splitList.iterator();
        String label="";
        while(i.hasNext()){
            Action a = i.next();

                if(checkIfCanBeChoiceLabel(a)){
                    Place targetPlace = elementBuilder.createPlace(false,getOriginID(a));
                    targetPlaces.add(targetPlace);
                    petriNet.add(targetPlace);
                    label="choice if "+a.getFinalLabel();
                }
                else{
                    Place targetPlace = elementBuilder.createPlace(false,"");
                    targetPlaces.add(targetPlace);
                    petriNet.add(targetPlace);

                    Transition t = createTransition(a,false);
                    petriNet.add(t);
                    Place p2= elementBuilder.createPlace(false, getOriginID(a));
                    petriNet.add(p2);
                    petriNet.add(elementBuilder.createArc(targetPlace.getID(),t.getID(),getOriginID(a)));
                    petriNet.add(elementBuilder.createArc(t.getID(),p2.getID(),getOriginID(a)));
                }


        }
        if(f.getType().equals(FlowType.concurrency)){
            ANDSplit as=new ANDSplit("Split concurrently",false,"",elementBuilder);
            as.addANDSplitToPetriNet(petriNet,p1,targetPlaces);
        }else if(f.getType().equals(FlowType.choice)||f.getType().equals(FlowType.multiChoice) ||f.getType().equals(FlowType.iteration)){
            if(label.equals(""))
                label= "choice";
            XORSplit xs = new XORSplit(label,splitList.size(),"",elementBuilder);
            xs.addXORSplitToPetriNet(petriNet,p1,targetPlaces);
        }

    }

    private List<Place> getAllDummyPlaces(){
        ArrayList<Place> dummyPlaces= new ArrayList<Place>();
        Iterator<Place> i = petriNet.getPlaceList().iterator();
        while(i.hasNext()){
            Place p = i.next();
            if(p.getOriginID().startsWith(DUMMY_PREFIX))
                dummyPlaces.add(p);
        }
        return dummyPlaces;
    }

    private void removeDummyNodes(){
        List<Place> dummyList = getAllDummyPlaces();
        Iterator<Place> i = dummyList.iterator();
        while(i.hasNext()){
            Place dummyPlace = i.next();
            List<Arc> ingoingArcs = petriNet.getAllReferencingArcsForElement(dummyPlace.getID(),PetriNet.REFERENCE_DIRECTION.ingoing);
            if(ingoingArcs.size()==1){
                Transition dummyTransition = (Transition) petriNet.getPetrinetElementByID(ingoingArcs.get(0).getSource());

                //remove Arc between Dummy Transition + Place
                petriNet.removePetrinetElementByID(ingoingArcs.get(0).getID());

                //handle n sources x m targets Situation
                ArrayList<Transition> newTargets = new ArrayList<Transition>();
                Iterator<Arc> ntI = petriNet.getAllReferencingArcsForElement(dummyPlace.getID(), PetriNet.REFERENCE_DIRECTION.outgoing).iterator();
                while(ntI.hasNext()){
                    Arc a = ntI.next();
                    newTargets.add((Transition) petriNet.getPetrinetElementByID(a.getTarget()));
                    petriNet.removePetrinetElementByID(a.getID());
                }

                Iterator<Arc> nsI = petriNet.getAllReferencingArcsForElement(dummyTransition.getID(), PetriNet.REFERENCE_DIRECTION.ingoing).iterator();
                while(nsI.hasNext()){
                    Arc a = nsI.next();
                    Iterator<Transition> newTargetIterator = newTargets.iterator();
                    while(newTargetIterator.hasNext()){
                        petriNet.add(elementBuilder.createArc(a.getSource(),newTargetIterator.next().getID(),""));
                    }
                    petriNet.removePetrinetElementByID(a.getID());
                }

                //remove DummyElements
                petriNet.removePetrinetElementByID(dummyPlace.getID());
                petriNet.removePetrinetElementByID(dummyTransition.getID());

            }else{
                /*do nothing, currently cant be handled*/
                //TODO check if occurence is possible
            }

        }
    }
}
