package TextToWorldModel;

import TextToWorldModel.transform.DummyAction;
import ToolWrapper.FrameNetFunctionality;
import WorldModelToPetrinet.IDHandler;
import edu.stanford.nlp.ling.Word;
import worldModel.*;

import java.util.ArrayList;

public class MockWorldModel {

    /*This class solely serves test purposes*/

    private WorldModel mockWM;

    public MockWorldModel(){
        generateMockWM();
    }

    public WorldModel getMockWM() {
        return mockWM;
    }

    private void generateMockWM() {
        IDHandler idHandler = new IDHandler(1);
        //create text representation of the mock WorldModel
        String[][] mockText = {{"The", "manager", "finishes", "the", "documents", "."},
                {"If", "he", "likes", "it", ",", "he", "sends", "it", "to", "the", "office", "."},
                {"Otherwise", "he", "throws", "it", "in", "the", "bin", "."}};
        Text mockTextT2P = new Text();
        for (int i = 0; i < mockText.length; i++) {
            ArrayList<Word> WordList = new ArrayList<Word>();
            for (int j = 0; j < mockText[i].length; j++) {
                WordList.add(new Word(mockText[i][j]));
            }
            mockTextT2P.addSentence(new T2PSentence(WordList));
        }

        // create mock WorldModel
        mockWM = new WorldModel();

        //Add Actors
        Actor[] a = new Actor[5];
        a[0] = new Actor(mockTextT2P.getSentence(0), 2, "manager");
        a[0].setDeterminer("The");
        a[1] = new Actor(mockTextT2P.getSentence(1), 2, "he");
        a[1].setReference(a[0]);
        a[2] = new Actor(mockTextT2P.getSentence(1), 6, "he");
        a[2].setReference(a[0]);
        a[3] = new Actor(mockTextT2P.getSentence(1), 11, "office");
        a[3].setDeterminer("the");
        a[4] = new Actor(mockTextT2P.getSentence(2), 2, "he");
        a[4].setReference(a[0]);
        for (int i = 0; i < a.length; i++) {
            mockWM.addActor(a[i]);
        }

        //Add Resources
        Resource[] r = new Resource[5];
        r[0] = new Resource(mockTextT2P.getSentence(0), 5, "documents");
        r[0].setDeterminer("the");
        r[1] = new Resource(mockTextT2P.getSentence(1), 4, "it");
        r[1].setReference(r[0]);
        r[1].setResolve(true);
        r[2] = new Resource(mockTextT2P.getSentence(1), 8, "it");
        r[2].setReference(r[0]);
        r[2].setResolve(true);
        r[3] = new Resource(mockTextT2P.getSentence(2), 4, "it");
        r[3].setReference(r[0]);
        r[3].setResolve(true);
        r[4] = new Resource(mockTextT2P.getSentence(2), 7, "bin");
        r[4].setDeterminer("the");
        for (int i = 0; i < r.length; i++) {
            mockWM.addResource(r[i]);
        }

        //add actions
        Action[] act = new Action[5];
        act[0] = new Action(mockTextT2P.getSentence(0), 3, "finishes");
        act[0].setBaseForm("finish");
        act[0].setActorFrom(a[0]);
        act[0].setObject(r[0]);
        act[0].setFinalLabel("finishes the document");

        act[1] = new Action(mockTextT2P.getSentence(1), 3, "likes");
        act[1].setBaseForm("like");
        act[1].setActorFrom(a[1]);
        act[1].setObject(r[1]);
        act[1].setMarker("if");
        act[1].setFinalLabel("likes the document");

        act[2] = new Action(mockTextT2P.getSentence(1), 7, "sends");
        act[2].setBaseForm("send");
        act[2].setActorFrom(a[2]);
        act[2].setObject(r[2]);
        act[2].setFinalLabel("sends the document to the office");
        act[2].setPreAdvMod("then", 0);
        Specifier s1 = new Specifier(mockTextT2P.getSentence(1), 11, "to the office");
        s1.setSpecifierType(Specifier.SpecifierType.PP);
        s1.setPhraseType(FrameNetFunctionality.PhraseType.CORE);
        s1.setHeadWord("to");
        s1.setObject(a[3]);
        //s1.setFrameElement(new FrameElement()); //TODO add FrameElement
        act[2].addSpecifiers(s1);

        act[3] = new Action(mockTextT2P.getSentence(2), 3, "throws");
        act[3].setBaseForm("throw");
        act[3].setActorFrom(a[4]);
        act[3].setObject(r[3]);
        act[3].setFinalLabel("throws the document in the bin");
        act[3].setPreAdvMod("otherwise", 1);
        Specifier s2 = new Specifier(mockTextT2P.getSentence(2), 7, "in the bin");
        s2.setSpecifierType(Specifier.SpecifierType.PP);
        s2.setPhraseType(FrameNetFunctionality.PhraseType.CORE);
        s2.setHeadWord("in");
        s2.setObject(r[4]);
        //s1.setFrameElement(new FrameElement()); //TODO add FrameElement
        act[3].addSpecifiers(s2);
        act[4] = new DummyAction(idHandler);
        act[4].setFinalLabel("Dummy Node");
        for (int i = 0; i < act.length; i++) {
            mockWM.addAction(act[i]);
        }

        //add flows
        Flow[] f = new Flow[3];
        f[0] = new Flow(mockTextT2P.getSentence(0));
        f[0].setDirection(Flow.FlowDirection.split);
        f[0].setType(Flow.FlowType.sequence);
        f[0].setSingleObject(new DummyAction(idHandler));
        ArrayList<Action> mo1 = new ArrayList<Action>();
        mo1.add(act[0]);
        f[0].setMultipleObjects(mo1);
        f[1] = new Flow(mockTextT2P.getSentence(1));
        f[1].setDirection(Flow.FlowDirection.split);
        f[1].setType(Flow.FlowType.choice);
        f[1].setSingleObject(act[0]);
        ArrayList<Action> mo2 = new ArrayList<Action>();
        mo2.add(act[1]);
        mo2.add(act[3]);
        f[1].setMultipleObjects(mo2);
        f[2] = new Flow(mockTextT2P.getSentence(2));
        f[2].setDirection(Flow.FlowDirection.split);
        f[2].setType(Flow.FlowType.sequence);
        f[2].setSingleObject(act[1]);
        ArrayList<Action> mo3 = new ArrayList<Action>();
        mo3.add(act[2]);
        f[2
                ].setMultipleObjects(mo3);

        for (int i = 0; i < f.length; i++) {
            mockWM.addFlow(f[i]);
        }

    }

}
