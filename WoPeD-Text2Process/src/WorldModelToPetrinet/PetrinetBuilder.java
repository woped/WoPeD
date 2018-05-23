package WorldModelToPetrinet;

import worldModel.WorldModel;

public class PetrinetBuilder {
    private WorldModel processWM;

    public  PetrinetBuilder(WorldModel processWM){
        this.processWM=processWM;
    }

    public String buildPNML(){
        String processPNML="<PNML>";
        /*
        TODO Implementation: processWM -> processPNML
        */
        return processPNML;
    }
}
