package T2PWebservice;

import TextToWorldModel.WorldModelBuilder;
import WorldModelToPetrinet.PetrinetBuilder;
import worldModel.WorldModel;

public class T2PController extends Thread {


    private String text;

    public WorldModel getWm() {
        return wm;
    }

    private WorldModel wm;

    public String getPNML() {
        return PNML;
    }

    private String PNML;

    public T2PController( String text){
        this.text=text;
    }

    public void run(){
       //PNML= generatePetrinetFromText(text);
        WorldModelBuilder wmBuilder = new WorldModelBuilder(text);
        PetrinetBuilder pnBuilder = new PetrinetBuilder(wmBuilder.buildWorldModel(false));
        PNML = pnBuilder.buildPNML();
        PNML=PNML.replaceAll("\n","");
        PNML=PNML.replaceAll("\t","");

    }

    public WorldModel getWorldModel(){
        WorldModelBuilder wmBuilder = new WorldModelBuilder(text);
        return wmBuilder.buildWorldModel(false);
    }

    public static String generatePetrinetFromText(String text){
        WorldModelBuilder wmBuilder = new WorldModelBuilder(text);
        PetrinetBuilder pnBuilder = new PetrinetBuilder(wmBuilder.buildWorldModel(false));
        String PNML = pnBuilder.buildPNML();
        PNML=PNML.replaceAll("\n","");
        PNML=PNML.replaceAll("\t","");
        return PNML;
    }
    public String getText() {
        return text;
    }

}
