package WorldModelToPetrinet;

import TextToWorldModel.WorldModelBuilder;
import worldModel.WorldModel;

public class PetrinetBuilderTest {
    /*
    For module testing during implementation phase
    TODO replace by JUnit representation
    */
    public static void main(String [] args){

        /**** Mock WorldModel ****/
        /*WorldModelBuilder WMBuilder = new WorldModelBuilder("The manager finishes the document. If he likes it, he sends it to the office. Otherwise he throws it in the bin.");
        PetrinetBuilder PNBuilder = new PetrinetBuilder(WMBuilder.buildWorldModel(true));*/

        /**** Build WorldModel ****/
        WorldModelBuilder WMBuilder = new WorldModelBuilder("The manager finishes the document. If he likes it, he sends it to the office. Otherwise he throws it in the bin.");
        PetrinetBuilder PNBuilder = new PetrinetBuilder(WMBuilder.buildWorldModel(false));

        String processPNML=PNBuilder.buildPNML();
        System.out.println("Build Results:");
        System.out.println(processPNML);
    }
}
