package Tests;

import TextToWorldModel.WorldModelBuilder;
import org.junit.Test;
import org.w3c.dom.*;
import worldModel.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class STTextToWorldModel extends T2PScenarioTest {

    private static String [] TestExamples = {"ST_Ressource_Hotel_Service.xml", "ST_Resource_Lemon_Chicken_Recipe.xml", "ST_Resource_Bike_Manufacturing.xml","ST_Ressource_Computer_Repair.xml"};
    private final static double acceptanceThreshold=0.4;
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
            printInfo(i,TestExamples[i]);
            parseTestFile(TestExamples[i]);

            startPerformanceTrace();
            WMbuilder= new WorldModelBuilder(sanitizeText(getPlainTextDescription()));
            WorldModel wm = WMbuilder.buildWorldModel(false);
            double performance = endPerformanceTrace();

            System.out.println("WorldModel for Testexample "+(i+1)+" "+TestExamples[i]+" was generated in "+(int) performance+" milliseconds.");
            Double score=compareResults(wm,i);
            assertEquals("Generated WorldModel for Testexample "+(i+1)+" "+TestExamples[i]+" fails the Requirements based on its Metadata.",true,score>acceptanceThreshold);
        }
    }

    private void printComparison(List<? extends SpecifiedElement> elements, ArrayList<String> testElements ){
        //elements=getDisjointList(elements);
        Iterator i = elements.iterator();
       int listSize;
       if(elements.size()>testElements.size()){
            listSize= elements.size();
       }else{
           listSize= testElements.size();
       }
       String [][] x= new String[3][listSize+1];
       x[0][0]="";
       x[1][0]="Calculated";
       x[2][0]="Testcase";
       for(int j=0;j<listSize;j++){
           x[0][j+1]=(j+1)+".";

           //Balance the two possibly differently sized Lists
           if((j+1)<=elements.size()){
               x[1][j+1]=elements.get(j).getName();
           }else{
               x[1][j+1]="-";
           }

           if((j+1)<=testElements.size()){
               x[2][j+1]=testElements.get(j);
           }else{
               x[2][j+1]="-";
           }
       }
       prinComparisonTable(x);
    }

    private double compareResults(WorldModel wm, int currentTestcase){
        HashMap<String, Double> elementDeltaScores = new HashMap<String, Double>();

        elementDeltaScores.put("actionDelta",(double)calculateActionDelta(wm));
        System.out.println("\n--------- Actions ---------");
        printComparison(wm.getActions(),getWorldModelElementList(ELEMENT_TYPE_ACTIONS));
        printScore("Similarity Score for Actions ",elementDeltaScores.get("actionDelta"));

        System.out.println("\n--------- Actors ---------");
        printComparison(wm.getActors(),getWorldModelElementList(ELEMENT_TYPE_ACTORS));
        elementDeltaScores.put("actorDelta",(double)calculateActorDelta(wm));
        printScore("Similarity Score for Actors ",elementDeltaScores.get("actorDelta"));

        System.out.println("\n--------- Resources ---------");
        printComparison(wm.getResources(),getWorldModelElementList(ELEMENT_TYPE_RESOURCES));
        elementDeltaScores.put("ResourceDelta",(double)calculateResourceDelta(wm));
        printScore("Similarity Score for resources ",elementDeltaScores.get("ResourceDelta"));

        double deltasum = 0.0;
        for (double d : elementDeltaScores.values()) {
            deltasum += d;
        }
        double score = deltasum/elementDeltaScores.size();
        printScore("\n Overall Score for Test "+(currentTestcase+1)+" "+TestExamples[currentTestcase],score);
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

    private static double calculateResourceDelta(WorldModel wm){
        int resourceCount= wm.getResources().size();
        return calculateDeltaScore(resourceCount,getWorldModelElementTypeCount(ELEMENT_TYPE_RESOURCES));
    }

    /*
    XML Parsing Methods
    */

    private static int getWorldModelElementTypeCount(String [] elementName){
        int elementCount=0;
        NodeList n = doc.getElementsByTagName(elementName[0]).item(0).getChildNodes();
        for (int i=0;i<n.getLength();i++)
            if (n.item(i).getNodeName().equals(elementName[1])){
                elementCount++;
            }
        return elementCount;
    }

    public ArrayList<String> getWorldModelElementList(String [] elementType){
        ArrayList<String> elementList= new ArrayList<String>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        int elementCount = getWorldModelElementTypeCount(elementType);
        for(int i=0;i<elementCount;i++){
            try {
                String text = xp.evaluate("//"+elementType[0]+"/"+elementType[1]+"["+(i+1)+"]/Name", doc.getDocumentElement());
                text=sanitizeText(text);
                elementList.add(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return elementList;
    }

}
