import static org.junit.Assert.assertEquals;

import ToolWrapper.StanfordParserFunctionality;
import ToolWrapper.StanfordParserInitializer;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import org.junit.Test;
import text.Text;

public class UTStanfordParser {
    @Test
    public void evaluateStanfordParserInvocation() {
        StanfordParserInitializer spi= StanfordParserInitializer.getInstance();

        /***check Initialiaztion***/
        DocumentPreprocessor dpp = spi.getDpp();
        GrammaticalStructureFactory gsf = spi.getGsf();
        LexicalizedParser parser = spi.getParser();
        TreebankLanguagePack tlp = spi.getTlp();

        assertEquals("Stanford Parser Initialization Issue: Document Preprocessor not initialized",true, dpp!=null);
        assertEquals("Stanford Parser Initialization Issue: GrammaticalStructureFactory not initialized",true, gsf!=null);
        assertEquals("Stanford Parser Initialization Issue: Lexicalized Parser not initialized",true, parser!=null);
        assertEquals("Stanford Parser Initialization Issue: Treebank Language Pack not initialized",true, tlp!=null);

        /***check Functionality***/
        String testText = "It's always difficult to write a test sentence, that actually makes sense. The Thing is also, that I even need two of them.";
        StanfordParserFunctionality spf = new StanfordParserFunctionality();

        Text analyzedText = spf.createText(testText);

        assertEquals("Stanford Parser Analysis Issue: Sentences not analyzed correctly",true, 2==analyzedText.getSize());
        assertEquals("Stanford Parser Analysis Issue: Words not analyzed correctly",true, 15==analyzedText.getSentence(0).length());
        assertEquals("Stanford Parser Analysis Issue: Words not analyzed correctly",true, 13==analyzedText.getSentence(1).length());

        GrammaticalStructure grammar = analyzedText.getSentence(0).getGrammaticalStructure();
        assertEquals("Stanford Parser Analysis Issue: Grammar not analyzed correctly",true, 12==grammar.dependencies().size());
    }
}