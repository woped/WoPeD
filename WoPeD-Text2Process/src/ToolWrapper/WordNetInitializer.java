/* This class initializes wordnet via the jwi interface.*/

package ToolWrapper;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.*;

import etc.Constants;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WordNetInitializer {

    //wordnet source directory
    private static File wnDir = new File ("WoPeD-Text2Process/NLPTools/dict");
    //wordnet initializer instance
    private static WordNetInitializer wni;
    //dictionary instance
    public static final IRAMDictionary dict = new RAMDictionary (wnDir , ILoadPolicy.NO_LOAD );

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
    public static void init () {

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
        //fillconstLists();
    }
    //tracking log
    private static void trek ( IDictionary dict ){
        int tickNext = 0;
        int tickSize = 20000;
        int seen = 0;

        System .out . print (" Treking across Wordnet ");
        long t = System.currentTimeMillis ();
        for(POS pos : POS.values ())
            for(Iterator<IIndexWord> i = dict.getIndexWordIterator (pos); i.hasNext (); )
                for(IWordID wid:i.next().getWordIDs()){
                    seen += dict.getWord(wid).getSynset().getWords().size();
                    if( seen > tickNext ){
                        System.out.print('.');
                        tickNext = seen + tickSize ;
                    }
                }
        System.out.printf (" done (%1d msec )\n", System . currentTimeMillis () -t);
        System.out.println ("In my trek I saw " + seen + " words ");

    }

    //überschreiben
    public static ArrayList<String> getAcceptedForwardLinkList() {
        return Constants.f_acceptedForForwardLink;
    }
    public static boolean isAMODAcceptedForLinking(String value) {
        return Constants.f_acceptedAMODforLoops.contains(value);
    }

}
