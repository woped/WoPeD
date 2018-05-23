package TextToWorldModel;

import edu.stanford.nlp.ling.Word;
import etc.TextToProcess;
import gui.Initiator;
import processing.FrameNetWrapper;
import processing.WordNetWrapper;
import text.T2PSentence;
import text.Text;
import worldModel.Action;
import worldModel.Actor;
import worldModel.Resource;
import worldModel.WorldModel;

import java.util.ArrayList;

public class WorldModelBuilder {

    private String processText;

    public WorldModelBuilder(String processText){
        this.processText = processText;
    }
    public WorldModel buildWorldModel(boolean mockBuild){
        WorldModel processWM;
        if(mockBuild){
            //for testing purposes only
            return buildWorldModelMock();
        }
        else{
            return buildWorldModel();
        }
    }

    private WorldModel buildWorldModelMock(){
        //create text representation of the mock WorldModel
        String [][] mockText = {{"The", "manager", "finishes", "the", "documents","."},
                                {"If", "he", "likes", "it",",", "he", "sends", "it", "to", "the", "office","."},
                                {"Otherwise", "he", "throws", "it", "in", "the", "bin","."}};
        Text mockTextT2P= new Text();
        for(int i=0;i<mockText.length;i++){
            ArrayList<Word> WordList = new ArrayList<Word>();
            for(int j=0;j<mockText[i].length;j++){
                WordList.add(new Word(mockText[i][j]));
            }
            mockTextT2P.addSentence(new T2PSentence(WordList));
        }
        // create mock WorldModel
        WorldModel mockWM = new WorldModel();

        //Add Actors
        Actor [] a = new Actor [5];
        a[0] = new Actor(mockTextT2P.getSentence(0),2,"manager");
        a[0].setDeterminer("The");
        a[1] = new Actor(mockTextT2P.getSentence(1),2,"he");
        a[1].setReference(a[0]);
        a[2] = new Actor(mockTextT2P.getSentence(1),6,"he");
        a[2].setReference(a[0]);
        a[3] = new Actor(mockTextT2P.getSentence(1),11,"office");
        a[3].setDeterminer("the");
        a[4] = new Actor(mockTextT2P.getSentence(2),2,"he");
        a[4].setReference(a[0]);
        for (int i = 0;i<a.length;i++){
            mockWM.addActor(a[i]);
        }

        //Add Resources
        Resource [] r = new Resource [5];
        r[0] = new Resource(mockTextT2P.getSentence(0),5 ,"documents");
        r[0].setDeterminer("the");
        r[1] = new Resource(mockTextT2P.getSentence(1),4 ,"it");
        r[1].setReference(r[0]);
        r[1].setResolve(true);
        r[2] = new Resource(mockTextT2P.getSentence(1),8 ,"it");
        r[2].setReference(r[0]);
        r[2].setResolve(true);
        r[3] = new Resource(mockTextT2P.getSentence(2),4 ,"it");
        r[3].setReference(r[0]);
        r[3].setResolve(true);
        r[4] = new Resource(mockTextT2P.getSentence(2),7 ,"bin");
        r[4].setDeterminer("the");
        for (int i = 0;i<r.length;i++){
           mockWM.addResource(r[i]);
        }

        //add actions
        Action[] act = new Action [1];
        act[0] = new Action(mockTextT2P.getSentence(0),3 ,"finish");
        act[0].setActorFrom(a[0]);
        act[0].setObject(r[0]);

        for (int i = 0;i<act.length;i++){
            mockWM.addAction(act[i]);
        }
        return mockWM;
    }
    private WorldModel buildWorldModel(){
        WordNetWrapper.init();
        FrameNetWrapper.init();
        TextToProcess t2p= new TextToProcess();
        WorldModel processWM = t2p.getWorldModel(processText);
        return processWM;
    }
}
