package T2PWebservice;

import TextToWorldModel.WorldModelBuilder;
import WorldModelToPetrinet.PetrinetBuilder;
import worldModel.WorldModel;

public class T2PController {
    public String generatePetrinetFromText(String text){
        WorldModelBuilder wmBuilder = new WorldModelBuilder(text);
        PetrinetBuilder pnBuilder = new PetrinetBuilder(wmBuilder.buildWorldModel(false));
        String PNML = pnBuilder.buildPNML();
        PNML=PNML.replaceAll("\n","");
        PNML=PNML.replaceAll("\t","");
        return PNML;
    }
}
