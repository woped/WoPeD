package WorldModelToPetrinet;

public abstract class PetriNetElement {

    private String originID;
    protected String text;
    protected IDHandler idHandler;
    protected int IDCounter;
    protected String ID;

    public PetriNetElement(String originID, IDHandler idHandler){
        this.idHandler=idHandler;
        this.originID=originID;
        IDCounter = this.idHandler.getNext();
    }

    public String getOriginID() {
        return originID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getID() {
        return ID;
    }

}
