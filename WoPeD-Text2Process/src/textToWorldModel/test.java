package textToWorldModel;

import edu.stanford.nlp.ling.Word;
import etc.TextToProcess;
import gui.Initiator;
import text.T2PSentence;
import text.Text;
import transform.AnalyzedSentence;
import transform.TextAnalyzer;
import worldModel.WorldModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class test {
    static String[] sent= {"the", "manager", "signs","the","document"};
    static ArrayList<Word> sentence;
    public static void main(String [] args){
        Initiator in = new Initiator();
        File f= new File("");
        in.convert("The manager finishes the documents. If he likes it, he sends it to the office. Otherwise he throws it in the bin.",false,f);
    }
}
