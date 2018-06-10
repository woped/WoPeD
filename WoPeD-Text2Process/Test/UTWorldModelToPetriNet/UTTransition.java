package UTWorldModelToPetriNet;

import WorldModelToPetrinet.Transition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UTTransition {

    String exspectedPNML="<transition id=\"t1\"><name><text/><graphics><offset x=\"0\" y=\"0\"/></graphics>" +
            "</name><graphics><position x=\"0\" y=\"0\"/><dimension x=\"40\" y=\"40\"/></graphics><toolspecific tool=\"WoPeD\" version=\"1.0\">" +
            "<time>0</time><timeUnit>1</timeUnit><orientation>1</orientation></toolspecific></transition>";
    @Test
    public void evaluateTransition(){
        Transition.resetStaticContext();
        Transition t= new Transition("",false,false,"");
        assertEquals("Transition did not create exspected PNML.", true,t.toString().equals(exspectedPNML));
    }

}
