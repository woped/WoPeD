package bpmn;

public class DataStore extends Artifact {
    
	
    public DataStore() {
        super();
    }

    public DataStore(int xPos, int yPos, String text) {
        super();
        setText(text);
    }

    public String toString() {
        return "BPMN data store";
    }

}

