import T2PWebservice.InvalidInputException;
import T2PWebservice.T2PController;
import TextToWorldModel.WorldModelBuilder;
import ToolWrapper.FrameNetFunctionality;
import ToolWrapper.StanfordParserFunctionality;
import ToolWrapper.WordNetFunctionality;
import ToolWrapper.WordNetInitializer;
import WorldModelToPetrinet.*;
import org.apache.bcel.classfile.Constant;
import org.junit.Test;
import org.w3c.dom.NodeList;
import worldModel.Action;
import worldModel.Actor;
import worldModel.SpecifiedElement;
import worldModel.WorldModel;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class STTextToProcess extends T2PScenarioTest {

    private static String [] TestExamples = {"ST_Resource_Lemon_Chicken_Recipe.xml"}; //, "ST_Resource_Bike_Manufacturing.xml","ST_Ressource_Computer_Repair.xml"};

    private final static double acceptanceThreshold = 0.1;

    private static final String [] ELEMENT_TYPE_PLACE = {"Places","Place"};
    private static final String [] ELEMENT_TYPE_TRANSITION ={"Transitions","Transition"};
    private static final String [] ELEMENT_TYPE_ARC ={"Arcs","Arc"};


    @Test
    public void evaluateT2P() throws InterruptedException, InvalidInputException {
        filePath = System.getProperty("user.dir");
        filePath = filePath + "/TestData/";

        WorldModelBuilder WMbuilder;
        PetrinetBuilder PNbuilder;

        //Initialize Tools
        WordNetFunctionality wf = new WordNetFunctionality();
        FrameNetFunctionality ff = new FrameNetFunctionality();
        StanfordParserFunctionality sf = new StanfordParserFunctionality();


        for (int i = 0; i < TestExamples.length; i++) {
            printInfo(i, TestExamples[i]);
            parseTestFile(TestExamples[i]);

            startPerformanceTrace();

            WMbuilder = new WorldModelBuilder(sanitizeText(getPlainTextDescription()));
            WorldModel wm = WMbuilder.buildWorldModel(false);
            PNbuilder = new PetrinetBuilder(wm);
            PNbuilder.buildPNML();
            PetriNet petriNet = PNbuilder.getPetriNet();


            double performance = endPerformanceTrace();

            System.out.println("Petrinet for Testexample " + (i + 1) + " " + TestExamples[i] + " was generated in " + (int) performance + " milliseconds.");
            Double score = compareResults(petriNet, i);
            assertEquals("Generated Petrinet for Testexample " + (i + 1) + " " + TestExamples[i] + " fails the Requirements based on its Metadata.", true, score > acceptanceThreshold);

        }


        wf = null;
        ff = null;
        sf = null;

    }

    private double compareResults(PetriNet petriNet, int currentTestcase) {

        HashMap<String, Double> elementDeltaScores = new HashMap<String, Double>();


        elementDeltaScores.put("placeDelta", calculatePlaceDelta(petriNet));
        System.out.println("\n--------- Places ---------");
        printComparison(petriNet.getPlaceList(), getPetriNetElementList(ELEMENT_TYPE_PLACE));
        printScore("Similarity Score for Places ", elementDeltaScores.get("placeDelta"));

        elementDeltaScores.put("transitionDelta", calculateTransitionDelta(petriNet));
        System.out.println("\n--------- Transitions ---------");
        printComparison(petriNet.getTransitionList(), getPetriNetElementList(ELEMENT_TYPE_TRANSITION));
        printScore("Similarity Score for Transitions ", elementDeltaScores.get("transitionDelta"));

        elementDeltaScores.put("arcDelta", calculateArcDelta(petriNet));
        System.out.println("\n--------- Arc ---------");
        printComparison(petriNet.getTransitionList(), getPetriNetElementList(ELEMENT_TYPE_ARC));
        printScore("Similarity Score for Arc ", elementDeltaScores.get("arcDelta"));

        double deltasum = 0.0;
        for (double d : elementDeltaScores.values()) {
            deltasum += d;
        }
        double score = deltasum/elementDeltaScores.size();
        printScore("\n Overall Score for Test "+(currentTestcase+1)+" "+TestExamples[currentTestcase],score);
        return score;
    }

    private static double calculatePlaceDelta(PetriNet pn){
        int placeCount=0;
        List<Place> places = pn.getPlaceList();
        Iterator<Place> iter = places.iterator();
        while (iter.hasNext()) {
            Place p = iter.next();
            placeCount++;
        }
        return calculateDeltaScore(placeCount,getPetriNetElementTypeCount(ELEMENT_TYPE_PLACE));
    }

    private static double calculateTransitionDelta(PetriNet pn){
        int transitionCount=0;
        List<Transition> transitions = pn.getTransitionList();
        Iterator<Transition> iter = transitions.iterator();
        while (iter.hasNext()) {
            Transition t = iter.next();
            transitionCount++;
        }
        return calculateDeltaScore(transitionCount,getPetriNetElementTypeCount(ELEMENT_TYPE_TRANSITION));
    }

    private static double calculateArcDelta(PetriNet pn){
        int arcCount=0;
        List<Arc> arcs = pn.getArcList();
        Iterator<Arc> iter = arcs.iterator();
        while (iter.hasNext()) {
            Arc a = iter.next();
            arcCount++;
        }
        return calculateDeltaScore(arcCount,getPetriNetElementTypeCount(ELEMENT_TYPE_PLACE));
    }

    private void printComparison(List<? extends PetriNetElement> elements, ArrayList<String> testElements ){

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
                x[1][j+1]=elements.get(j).getText();
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

    private static double calculateDeltaScore(int actualElementsCount, int exscpectedElementsCount){
        double actual = (double)actualElementsCount;
        double excspected= (double)exscpectedElementsCount;
        double delta = Math.abs( actual - excspected);
        double relativeDelta = delta/actual;
        return 1-relativeDelta;
    }

    /*
    XML Parsing Methods
    */

    private static int getPetriNetElementTypeCount(String [] elementName){
        int elementCount=0;
        NodeList n = doc.getElementsByTagName(elementName[0]).item(0).getChildNodes();
        for (int i=0;i<n.getLength();i++)
            if (n.item(i).getNodeName().equals(elementName[1])){
                elementCount++;
            }
        return elementCount;
    }

    public ArrayList<String> getPetriNetElementList(String [] elementType){
        ArrayList<String> elementList= new ArrayList<String>();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        int elementCount = getPetriNetElementTypeCount(elementType);
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





    @Test
    public void evaluateT2PInputValidityCheck() {
        boolean invalidCharactersDetected=false;
        boolean invalidSizeDetected=false;
        try {
            T2PController controllerx = new T2PController("<xml>I am a homeless datastructure, that arrived @ the wrong webservice.</xml>");
        } catch (InvalidInputException e) {
            invalidCharactersDetected=true;
        }

        try {
            String oversizedInput = new String(new char[20000]).replace('\0', 'x');
            T2PController controllerx = new T2PController(oversizedInput);
        } catch (InvalidInputException e) {
            invalidSizeDetected=true;
        }

        assertEquals("Invalid input not detected",true,invalidCharactersDetected&&invalidSizeDetected);
    }

    @Test
    public void evaluateT2PMultiThread() throws InterruptedException, InvalidInputException {

        /* Evaluates the absence of undesired behaviour caused by parallel execution
        Concurrent Tool Initialization Phase is not specifically covered in this testcase as it only happens once per deployment*/

        filePath = System.getProperty("user.dir");
        filePath=filePath+"/TestData/";
        for (int i=0;i<TestExamples.length;i++){
            parseTestFile(TestExamples[i]);
            String text= getPlainTextDescription();
            String[] PNMLS = generateThreads((int) (2+Math.random()*6), text);

            T2PController single = new T2PController(text);
            String PNML=single.generatePetrinetFromText();

            //Evluate, if all concurrent executions of the same input deliver an equal result compared to a single execution.
            boolean equalResult = true;
            for(int j = 0; j<PNMLS.length;j++){
                equalResult=equalResult && PNML.equals(PNMLS[j]);
            }
            assertEquals("Concurrent Petrinet Generation failed.",true,equalResult);
        }
    }

    private String [] generateThreads(int threadCount, String text) throws InterruptedException, InvalidInputException {

        startPerformanceTrace();
        T2PController [] controllers = new T2PController [threadCount];
        String [] PNMLS = new String [threadCount];

        for(int i = 0; i<threadCount;i++){
            controllers[i] = new T2PController(text);
        }

        for(int i = 0; i<threadCount;i++){
            controllers[i].start();
            //add some randomness to simulate real situations
            controllers[i].sleep((int) (Math.random()*1000));
        }

        for(int i = 0; i<threadCount;i++){
            controllers[i].join();
            PNMLS[i] = controllers[i].getPNML();
        }
        long performanc = endPerformanceTrace();
        System.out.println(threadCount+" concurrent Threads finished in "+performanc+" ms");

        return PNMLS;
    }
}