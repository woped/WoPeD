package UTWorldModelToPetriNet;

import WorldModelToPetrinet.Place;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UTPlace {
    String exspectedPNML="<place id=\"p1\"><name><text>p1</text>" +
            "<graphics><offset x=\"0\" y=\"0\"/></graphics>" +
            "</name><graphics><position x=\"0\" y=\"0\"/>" +
            "<dimension x=\"40\" y=\"40\"/></graphics></place>";
    @Test
    public void evaluatePlace(){
        Place.resetStaticContext();
        Place p= new Place(false,"");
        assertEquals("Place did not create exspected PNML.", true,p.toString().equals(exspectedPNML));
    }
}
