package UTWorldModelToPetriNet;

import WorldModelToPetrinet.Arc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/*Unit Test for class WorldModelToPetriNet Arc*/
public class UTArc {

    /*Unit test for Class WorldModelToPetrinet.Arc*/

    String exspectedPNML="<arc id=\"a1\" source=\"1\" target=\"1\">" +
            "<inscription><text>1</text><graphics><offset x=\"500.0\" y=\"-12.0\"/>" +
            "</graphics></inscription><toolspecific tool=\"WoPeD\" version=\"1.0\">" +
            "<probability>1.0</probability><displayProbabilityOn>false</displayProbabilityOn>" +
            "<displayProbabilityPosition x=\"500.0\" y=\"12.0\"/></toolspecific></arc>";

    @Test
    public void evaluateArc(){
        Arc.resetStaticContext();
        Arc a = new Arc("1","1","");
        assertEquals("Arc did not create exspected PNML.", true,a.toString().equals(exspectedPNML));
    }
}
