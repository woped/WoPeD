package WorldModelToPetrinet;

public abstract class PetriNetElement {

    protected String originID;
    public PetriNetElement(String originID){
        this.originID=originID;
    }

    public String getOriginID() {
        return originID;
    }

    public void setOriginID(String originID) {
        this.originID = originID;
    }

}
