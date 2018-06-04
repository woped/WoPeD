import TextToWorldModel.WorldModelBuilder;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import worldModel.Action;
import worldModel.Actor;
import worldModel.WorldModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class STTextToWorldModel {
    private static String [] TestExamples = {"ST_Resource_Lemon_Chicken_Recipe.xml"}; //, "ST_Resource_Bike_Manufacturing.xml","ST_Ressource_Computer_Repair.xml"};
    private static String filePath;
    private static Document doc;
    private final static double acceptanceThreshold=0.1;
    private static final String [] ELEMENT_TYPE_ACTIONS = {"Actions","Action"};
    private static final String [] ELEMENT_TYPE_ACTORS ={"Actors","Actor"};
    private static final String [] ELEMENT_TYPE_RESOURCES ={"Resources","Resource"};
    private static final String [] ELEMENT_TYPE_FLOWS ={"Flows","Flow"};

    @Test
    public void evaluateWorldModelBuild() {
        filePath = System.getProperty("user.dir");
        filePath=filePath+"/TestData/";
        WorldModelBuilder WMbuilder;
        for(int i=0;i<TestExamples.length;i++){
            printInfo(i);
            parseTestFile(TestExamples[i]);
            WMbuilder= new WorldModelBuilder(sanitizeText(getPlainTextDescription()));
            WorldModel wm = WMbuilder.buildWorldModel(false);
            Double score=calculateSimilarityScore(wm,i);
            assertEquals("Generated WorldModel for Testexample "+(i+1)+" "+TestExamples[i]+" fails the Requirements based on its Metadata.",true,score>acceptanceThreshold);
        }
    }

    private double calculateSimilarityScore(WorldModel wm, int currentTestcase){
        HashMap<String, Double> elementDeltaScores = new HashMap<String, Double>();

        elementDeltaScores.put("actionDelta",(double)calculateActionDelta(wm));
        elementDeltaScores.put("actorDelta",(double)calculateActorDelta(wm));
        elementDeltaScores.put("ResourceDelta",(double)calculateResourceDelta(wm));
        elementDeltaScores.put("FlowDelta",(double)calculateFlowDelta(wm));
        double deltasum = 0.0;
        for (double d : elementDeltaScores.values()) {
            deltasum += d;
        }
        double score = deltasum/elementDeltaScores.size();
        System.out.printf("\n\nScore for Test "+(currentTestcase+1)+" "+TestExamples[currentTestcase]+" in Percent: %.2f%%%n", score*100);
        return score;
    }

    private static double calculateActorDelta(WorldModel wm){
        int actorCount=0;
        List<Actor> actors = wm.getActors();
        Iterator<Actor> iter = actors.iterator();
        while (iter.hasNext()){
            Actor a = iter.next();
            if(a.getReference()==null && a.isMetaActor()==false) {
                actorCount++;
            }
        }
        return  calculateDeltaScore(actorCount,getWorldModelElementTypeCount(ELEMENT_TYPE_ACTORS));
    }

    private static double calculateActionDelta(WorldModel wm){
        int actionCount=0;
        List<Action> actions = wm.getActions();
        Iterator<Action> iter = actions.iterator();
        while (iter.hasNext()) {
            Action a = iter.next();
            if(!a.getName().equals("Dummy Node")) {
            actionCount++;
            }
        }
        return calculateDeltaScore(actionCount,getWorldModelElementTypeCount(ELEMENT_TYPE_ACTIONS));
    }

    private static double calculateFlowDelta(WorldModel wm){
        int flowCount= wm.getFlows().size();
        return calculateDeltaScore(flowCount,getWorldModelElementTypeCount(ELEMENT_TYPE_FLOWS));
    }

    private static double calculateResourceDelta(WorldModel wm){
        //TODO implement Deep Delta Calc
        int resourceCount= wm.getResources().size();
        return calculateDeltaScore(resourceCount,getWorldModelElementTypeCount(ELEMENT_TYPE_RESOURCES));
    }

    private static void parseTestFile(String fileName){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            File inputFile = new File(filePath+fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private static int getWorldModelElementTypeCount(String [] elementName){
        int elementCount=0;
        NodeList n = doc.getElementsByTagName(elementName[0]).item(0).getChildNodes();
        for (int i=0;i<n.getLength();i++)
            if (n.item(i).getNodeName().equals(elementName[1])){
                elementCount++;
            }
        return elementCount;
    }

    private static String getPlainTextDescription(){
        return doc.getElementsByTagName("PlainTextDescription").item(0).getChildNodes().item(0).getNodeValue().toString();
    }

    private static String sanitizeText(String text){
        //get rid of tabs and newlines
        text = text.replace("\t", "");
        text = text.replace("\n", "");
        //deal with x*space based tabs
        while (text.contains("  ")){
            text=text.replace("  "," ");
        }
        return text;
    }

    private static void printInfo(int currentTestcase){
        System.out.println("--------------------------------------------------------------");
        System.out.println("    Test "+(currentTestcase+1)+": "+TestExamples[currentTestcase]);
        System.out.println("--------------------------------------------------------------");
    }

    private static double calculateDeltaScore(int actualElementsCount, int exscpectedElementsCount){
        double actual = (double)actualElementsCount;
        double excspected= (double)exscpectedElementsCount;

        double delta = Math.abs( actual - excspected);
        double relativeDelta = delta/actual;
        return 1-relativeDelta;
    }

}
