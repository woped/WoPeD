package ToolWrapper;

import de.saar.coli.salsa.reiter.framenet.DatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.fncorpus.AnnotationCorpus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameNetInitializer {

    //framenet source directory
    private String f_frameNetHome = FrameNetInitializer.class.getResource("/fndata-1.5").getPath();
    //framenet initializer instance
    private static FrameNetInitializer fni;
    //framenet instance (dictionary)
    private FrameNet f_frameNet;
    //reduced annotation corpus
    private AnnotationCorpus f_corpus;
    // TODO: prüfen ob weiterhin benötigt:
    private boolean generateButton = false;

    private FrameNetInitializer (){
        f_frameNet = new FrameNet();
    }

    //getter
    public synchronized static FrameNetInitializer getInstance(){
        if(fni == null) {
            synchronized (FrameNetInitializer.class) {
                if(fni == null){
                    fni = new FrameNetInitializer();
                    fni.init();
                }
            }
        }
        return fni;
    }

    public synchronized static void resetInstance() {
        fni=null;
    }

    public synchronized FrameNet getFN() {
        return f_frameNet;
    }
    public synchronized AnnotationCorpus getCorpus() {
        return f_corpus;
    }
    public synchronized boolean getGenrateButton(){
        return generateButton;
    }

    public synchronized void init() {
        try {

            //start time for tracking tracking
            long _start = System.currentTimeMillis();
            System.out.print("Loading FrameNet into memory ...");

            // Reading FrameNet
            DatabaseReader reader = new FNDatabaseReader(new File(f_frameNetHome), false);
            f_frameNet.readData(reader);
            Logger _l = Logger.getLogger("this");
            _l.setLevel(Level.SEVERE);

            //logging loading time
             System.out.printf(" done (%1d ms)\n", System.currentTimeMillis() - _start);

            //reading valence patterns from reduced corpus
            _start = System.currentTimeMillis();
            System.out.print("Loading FrameNet annotations into memory ...");
            f_corpus = new AnnotationCorpus(f_frameNet,_l);
            f_corpus.setScanSubCorpuses(false);
            f_corpus.parse(new File(f_frameNetHome+"lu"));

            //logging loading time
            System.out.printf(" done (%1d ms)\n", System.currentTimeMillis() -_start);
            generateButton = true;

        } catch (Exception ex) {
            System.err.print("Could not initialize FrameNet wrapper: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
