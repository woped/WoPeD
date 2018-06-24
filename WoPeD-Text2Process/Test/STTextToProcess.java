import T2PWebservice.InvalidInputException;
import T2PWebservice.T2PController;
import org.junit.Test;
import worldModel.WorldModel;

import static org.junit.Assert.assertEquals;

public class STTextToProcess extends T2PScenarioTest {

    private static String [] TestExamples = {"ST_Resource_Lemon_Chicken_Recipe.xml", "ST_Resource_Bike_Manufacturing.xml","ST_Ressource_Computer_Repair.xml"};

    @Test
    public void evaluateT2P() throws InterruptedException, InvalidInputException {
        filePath = System.getProperty("user.dir");
        filePath=filePath+"/TestData/";
/*
            T2PController controllerx = new T2PController("<script>");
            T2PController controller = new T2PController("The manager finishes the document. If he likes it, he sends it to the office. Otherwise he throws it in the bin.");*/


        /*TODO evaluate quality of generated Petrinet based on tagged samples*/

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