/* This class initializes wordnet via the jwi interface.*/

package ToolWrapper;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WordNetInitializer {

    //wordnet source directory
    private File wnDir;
    //wordnet initializer instance
    private static WordNetInitializer wni;
    //dictionary instance
    private IRAMDictionary dict;// = new RAMDictionary (wnDir , ILoadPolicy.NO_LOAD );

    private WordNetInitializer(){
        URL WordNetpath = WordNetInitializer.class.getResource("/dict");
        wnDir = new File(WordNetpath.getPath());
        dict = new RAMDictionary (wnDir , ILoadPolicy.NO_LOAD );
    }

    //getter
    public static synchronized WordNetInitializer getInstance(){
        if(wni == null){
            synchronized (FrameNetInitializer.class) {
                if(wni == null){
                    wni = new WordNetInitializer();
                    wni.init();
                }
            }
        }
        return wni;
    }

    public static synchronized void resetInstance(){
        wni=null;
    }

    public synchronized IRAMDictionary getDict() {
        return dict;
    }

    //actually opening and loading the dictionary
    private synchronized void init () {

        try {
            // construct the dictionary object and open it
            dict.open();

            //loading into memory
            System.out.print("\nLoading WordNet into memory ...");
            long t = System.currentTimeMillis();
            dict.load(true);
            System.out.printf(" done (%1d ms)\n", System.currentTimeMillis() - t);


        } catch (IOException ioe) {
            System.out.print("Exception occured while opening the dictionary: ");
            ioe.printStackTrace();
        } catch (InterruptedException ire) {
            System.out.print("Exception occured while loading the dictionary: ");
            ire.printStackTrace();
        }
    }
}
