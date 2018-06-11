package WorldModelToPetrinet;

public class IDHandler {
    private int  id;
    public IDHandler(int startID){
     id=startID;
    }

    private int getNext(){
        id++;
        return id;
    }

}
