package WorldModelToPetrinet;

public abstract class  PetrinetGateway {

    protected PetrinetElementBuilder elementBuilder;

    public PetrinetGateway(PetrinetElementBuilder elementbuilder){
        this.elementBuilder=elementbuilder;
    }
}
