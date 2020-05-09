package TextToWorldModel;

import ToolWrapper.*;
import worldModel.Text;
import TextToWorldModel.transform.TextAnalyzer;
import worldModel.*;

public class WorldModelBuilder {

    private String processText;
    private Text parsedText;
    private TextAnalyzer analyzer = new TextAnalyzer();
    private StanfordParserFunctionality stanford = StanfordParserFunctionality.getInstance();

    public WorldModelBuilder(String processText){
        this.processText = processText;
    }
    public WorldModel buildWorldModel(boolean mockBuild){
        WorldModel processWM;
        if(mockBuild){
            //for testing purposes only
            return buildWorldModelMock();
        }
        else{
            return buildWorldModel();
        }
    }

    public static synchronized void resetNLPTools(){
        StanfordParserInitializer.resetInstance();
        WordNetInitializer.resetInstance();
        StanfordParserInitializer.resetInstance();
        FrameNetInitializer.resetInstance();
    }

    public TextStatistics getTextStatistics() {
        TextStatistics _result = new TextStatistics();
        _result.setNumberOfSentences(parsedText.getSize());
        _result.setAvgSentenceLength(parsedText.getAvgSentenceLength());
        _result.setNumOfReferences(analyzer.getNumberOfReferences());
        _result.setNumOfLinks(analyzer.getNumberOfLinks());
        return _result;
    }

    private WorldModel buildWorldModel(){
        parsedText = stanford.createText(processText);
        analyzer.clear();
        analyzer.analyze(parsedText);
        WorldModel processWM= analyzer.getWorld();
        return processWM;
    }

    private WorldModel buildWorldModelMock(){
        return new MockWorldModel().getMockWM();
    }
}
