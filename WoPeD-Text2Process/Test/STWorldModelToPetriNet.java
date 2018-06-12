import TextToWorldModel.WorldModelBuilder;
import WorldModelToPetrinet.PetrinetBuilder;
import jdk.internal.org.xml.sax.SAXException;
import jdk.nashorn.internal.runtime.Source;
import org.junit.Test;
import org.woped.file.PNMLImport;
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
    private static String PetriNetPNML;




    private static String  filePath;

    @Test
    public void evaluatePetriNetBuild() {
        filePath= System.getProperty("user.dir");
        filePath=filePath+"/TestData/";
        WorldModelBuilder WMBuilder = new WorldModelBuilder("");
        WorldModel wm = WMBuilder.buildWorldModel(true);
        PetrinetBuilder PNBuilder = new PetrinetBuilder(wm);
        PetriNetPNML=PNBuilder.buildPNML();
        assertEquals("Generated PNML is invalid.",true,validateXMLSchema(filePath+"pnml_wf.xsd"));

        }
    public static boolean validateXMLSchema(String xsdPath){
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(PetriNetPNML.getBytes(StandardCharsets.UTF_8))));
        } catch (IOException e) {
            //System.out.println("Exception: "+e.getMessage());
            return false;
        } catch (org.xml.sax.SAXException e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

}
