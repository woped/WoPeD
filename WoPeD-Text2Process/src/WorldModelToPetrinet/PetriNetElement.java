package WorldModelToPetrinet;

public abstract class PetriNetElement {

    private String originID;
    protected String text;
    protected IDHandler idHandler;

    public PetriNetElement(String originID){//, IDHandler idHandler){
        //this.idHandler=idHandler;
        this.originID=originID;
    }

    public String getOriginID() {
        return originID;
    }

    public void setOriginID(String originID) {
        this.originID = originID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
