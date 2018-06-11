import TextToWorldModel.WorldModelBuilder;
import WorldModelToPetrinet.PetrinetBuilder;
import jdk.internal.org.xml.sax.SAXException;
import jdk.nashorn.internal.runtime.Source;
import org.junit.Test;
import worldModel.WorldModel;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static com.apple.eio.FileManager.getResource;
import static org.junit.Assert.assertEquals;

public class STWorldModelToPetriNet {
    private static String PetriNetPNML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +

            "<pnml>\n" +
            "  <net type=\"http://www.informatik.hu-berlin.de/top/pntd/ptNetb\" id=\"noID\">\n" +
            "    <place id=\"p1\">\n" +
            "      <name>\n" +
            "        <text>p1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"240\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"240\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "    </place>\n" +
            "    <place id=\"p2\">\n" +
            "      <name>\n" +
            "        <text>p2</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"400\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"400\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "    </place>\n" +
            "    <place id=\"p3\">\n" +
            "      <name>\n" +
            "        <text>p3</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"550\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"550\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "    </place>\n" +
            "    <transition id=\"t1\">\n" +
            "      <name>\n" +
            "        <text>t1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"310\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"310\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <time>0</time>\n" +
            "        <timeUnit>1</timeUnit>\n" +
            "        <orientation>1</orientation>\n" +
            "      </toolspecific>\n" +
            "    </transition>\n" +
            "    <transition id=\"t2\">\n" +
            "      <name>\n" +
            "        <text>t2</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"480\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"480\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <time>0</time>\n" +
            "        <timeUnit>1</timeUnit>\n" +
            "        <orientation>1</orientation>\n" +
            "      </toolspecific>\n" +
            "    </transition>\n" +
            "    <transition id=\"t3\">\n" +
            "      <name>\n" +
            "        <text>t3</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"690\" y=\"280\"/>\n" +
            "        </graphics>\n" +
            "      </name>\n" +
            "      <graphics>\n" +
            "        <position x=\"690\" y=\"240\"/>\n" +
            "        <dimension x=\"40\" y=\"40\"/>\n" +
            "      </graphics>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <time>0</time>\n" +
            "        <timeUnit>1</timeUnit>\n" +
            "        <orientation>1</orientation>\n" +
            "      </toolspecific>\n" +
            "    </transition>\n" +
            "    <arc id=\"a1\" source=\"p1\" target=\"t1\">\n" +
            "      <inscription>\n" +
            "        <text>1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"500.0\" y=\"-12.0\"/>\n" +
            "        </graphics>\n" +
            "      </inscription>\n" +
            "      <graphics/>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <probability>1.0</probability>\n" +
            "        <displayProbabilityOn>false</displayProbabilityOn>\n" +
            "        <displayProbabilityPosition x=\"500.0\" y=\"12.0\"/>\n" +
            "      </toolspecific>\n" +
            "    </arc>\n" +
            "    <arc id=\"a2\" source=\"t1\" target=\"p2\">\n" +
            "      <inscription>\n" +
            "        <text>1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"500.0\" y=\"-12.0\"/>\n" +
            "        </graphics>\n" +
            "      </inscription>\n" +
            "      <graphics/>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <probability>1.0</probability>\n" +
            "        <displayProbabilityOn>false</displayProbabilityOn>\n" +
            "        <displayProbabilityPosition x=\"500.0\" y=\"12.0\"/>\n" +
            "      </toolspecific>\n" +
            "    </arc>\n" +
            "    <arc id=\"a3\" source=\"p2\" target=\"t2\">\n" +
            "      <inscription>\n" +
            "        <text>1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"500.0\" y=\"-12.0\"/>\n" +
            "        </graphics>\n" +
            "      </inscription>\n" +
            "      <graphics/>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <probability>1.0</probability>\n" +
            "        <displayProbabilityOn>false</displayProbabilityOn>\n" +
            "        <displayProbabilityPosition x=\"500.0\" y=\"12.0\"/>\n" +
            "      </toolspecific>\n" +
            "    </arc>\n" +
            "    <arc id=\"a4\" source=\"t2\" target=\"p3\">\n" +
            "      <inscription>\n" +
            "        <text>1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"500.0\" y=\"-12.0\"/>\n" +
            "        </graphics>\n" +
            "      </inscription>\n" +
            "      <graphics/>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <probability>1.0</probability>\n" +
            "        <displayProbabilityOn>false</displayProbabilityOn>\n" +
            "        <displayProbabilityPosition x=\"500.0\" y=\"12.0\"/>\n" +
            "      </toolspecific>\n" +
            "    </arc>\n" +
            "    <arc id=\"a5\" source=\"p3\" target=\"t3\">\n" +
            "      <inscription>\n" +
            "        <text>1</text>\n" +
            "        <graphics>\n" +
            "          <offset x=\"500.0\" y=\"-12.0\"/>\n" +
            "        </graphics>\n" +
            "      </inscription>\n" +
            "      <graphics/>\n" +
            "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "        <probability>1.0</probability>\n" +
            "        <displayProbabilityOn>false</displayProbabilityOn>\n" +
            "        <displayProbabilityPosition x=\"500.0\" y=\"12.0\"/>\n" +
            "      </toolspecific>\n" +
            "    </arc>\n" +
            "    <toolspecific tool=\"WoPeD\" version=\"1.0\">\n" +
            "      <bounds>\n" +
            "        <position x=\"11\" y=\"33\"/>\n" +
            "        <dimension x=\"1395\" y=\"460\"/>\n" +
            "      </bounds>\n" +
            "      <scale>100</scale>\n" +
            "      <treeWidthRight>1086</treeWidthRight>\n" +
            "      <overviewPanelVisible>true</overviewPanelVisible>\n" +
            "      <treeHeightOverview>100</treeHeightOverview>\n" +
            "      <treePanelVisible>true</treePanelVisible>\n" +
            "      <verticalLayout>false</verticalLayout>\n" +
            "      <resources/>\n" +
            "      <simulations/>\n" +
            "      <partnerLinks/>\n" +
            "      <variables/>\n" +
            "    </toolspecific>\n" +
            "  </net>\n" +
            "</pnml>";
    private static String  filePath;

    @Test
    public void evaluatePetriNetBuild() {
        filePath= System.getProperty("user.dir");
        filePath=filePath+"/TestData/";
        WorldModelBuilder WMBuilder = new WorldModelBuilder("");
        WorldModel wm = WMBuilder.buildWorldModel(true);
        PetrinetBuilder PNBuilder = new PetrinetBuilder(wm);
        //PetriNetPNML=PNBuilder.buildPNML();

        System.out.println(PetriNetPNML);
        System.out.println("-----------------");
        assertEquals("Generated PNML is invalid.",true,validateXMLSchema(filePath+"pnml_wf.xsd"));

        }
    public static boolean validateXMLSchema(String xsdPath){
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(PetriNetPNML.getBytes(StandardCharsets.UTF_8))));
        } catch (IOException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        return true;
    }

}
