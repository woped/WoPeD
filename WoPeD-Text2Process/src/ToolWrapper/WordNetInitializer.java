/* This class initializes wordnet via the jwi interface.*/

package ToolWrapper;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;

import java.io.File;
import java.io.IOException;

public class WordNetInitializer {

    //wordnet source directory
    private static final File wnDir = new File ("WoPeD-Text2Process/NLPTools/WordNet/dict");
    //wordnet initializer instance
    private static WordNetInitializer wni;
    //dictionary instance
    private static final IRAMDictionary dict = new RAMDictionary (wnDir , ILoadPolicy.NO_LOAD );

    private WordNetInitializer(){

    }

    //getter
    public static WordNetInitializer getInstance(){
        if(wni == null){
            wni = new WordNetInitializer();
            init();
        }
        return wni;
    }
    public IRAMDictionary getDict() {
        return dict;
    }

    //actually opening and loading the dictionary
    private static void init () {

        try {
            // construct the dictionary object and open it
            dict.open();

            //loading into memory
            System.out.print("\n Loading Wordnet into memory ... ");
            long t = System.currentTimeMillis();
            dict.load(true);
            System.out.printf(" done (%1d msec )\n", System.currentTimeMillis() - t);


        } catch (IOException ioe) {
            System.out.print("Exception occured while opening the dictionary: ");
            ioe.printStackTrace();
        } catch (InterruptedException ire) {
            System.out.print("Exception occured while loading the dictionary: ");
            ire.printStackTrace();
        }
    }
}
