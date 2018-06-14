package WorldModelToPetrinet;

public class IDHandler {
    private int  id;
    public IDHandler(int startID){
     id=startID;
    }

    public int getNext(){
        return id++;
    }

}
