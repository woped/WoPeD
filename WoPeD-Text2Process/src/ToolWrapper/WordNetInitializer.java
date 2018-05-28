package ToolWrapper;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.*;

import etc.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import processing.ProcessingUtils;
import text.T2PSentence;
import transform.ListUtils;
import worldModel.Action;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WordNetInitializer {

    //wordnet source directory
    private File wnDir = new File ("WoPeD-Text2Process/NLPTools/dict");

    //dictionary instance
    public final IRAMDictionary dict = new RAMDictionary (wnDir , ILoadPolicy.NO_LOAD );
    public IRAMDictionary getDict() {
        return dict;
    }
    public IRAMDictionary getInstance(){
        return dict;
    }

    //lists with static words created via wordnet
    private  ArrayList<String> f_acceptedAMODList = new ArrayList<>();
    private  ArrayList<String> acceptedForwardLinkList = new ArrayList<>();

    //actually opening and loading the dictionary
    public  void init () {

        try {
            // construct the dictionary object and open it
            dict.open();

            // tracking for test purposes only
            trek(dict);
            System.out.print("\n Loading Wordnet into memory ... ");
            long t = System.currentTimeMillis();

            //loading into memory
            dict.load(true);

            //tracking for test purposes
            System.out.printf(" done (%1d msec )\n", System.currentTimeMillis() - t);
            trek(dict);

        } catch (IOException ioe) {
            System.out.print("Exception occured while opening the dictionary: ");
            ioe.printStackTrace();
        } catch (InterruptedException ire) {
            System.out.print("Exception occured while loading the dictionary: ");
            ire.printStackTrace();
        }
        fillconstLists();
    }



    //tracking for test purposes!!!
    private void trek ( IDictionary dict ){
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

    //initialize the constant lists via wordnet
    private void fillconstLists(){

        try {
            for (String s : Constants.f_acceptedAMODforLoops) {
                IIndexWord _iw = dict.getIndexWord(s, POS.ADJECTIVE);
                if (_iw != null) {
                    addAllLemmas(_iw, f_acceptedAMODList);
                }
                _iw = dict.getIndexWord(s, POS.ADVERB);
                if (_iw != null) {
                    addAllLemmas(_iw, f_acceptedAMODList);
                }
            }

            for (String s : Constants.f_acceptedForForwardLink) {
                IIndexWord _iw = dict.getIndexWord(s, POS.ADJECTIVE);
                if (_iw != null) {
                    addAllLemmas(_iw, acceptedForwardLinkList);
                }
                _iw = dict.getIndexWord(s, POS.ADVERB);
                if (_iw != null) {
                    addAllLemmas(_iw, acceptedForwardLinkList);
                }
            }

        } catch (Exception e) {
            System.out.print("An exception occured while initializing the constant lists for module etc: ");
            e.printStackTrace();
        }
    }

    //helper methods for filling lists
    private void addAllLemmas(IIndexWord _iw, List<String> list) throws Exception {
        // get the synset
        List <IWordID> wordIDs = _iw.getWordIDs();
        for (IWordID wi : wordIDs){
            IWord word = dict.getWord(wi);
            ISynset syn = word.getSynset();
            addLemmas(syn, list);

            // get the antonyms
            List < ISynsetID > _pts = syn.getRelatedSynsets(Pointer.ANTONYM);
            for (ISynsetID p : _pts) {
                addLemmas(dict.getSynset(p),list);
            }

        }
    }
    private void addLemmas(ISynset syn, List<String> list) {
        for (IWord word : syn.getWords()) {
            String _str = word.getLemma();
            if (_str.indexOf('(') > 0) {
                _str = _str.substring(0, _str.indexOf('('));
            }
            _str = _str.replaceAll("_", " ");
            list.add(_str);
        }
    }
    public ArrayList<String> getAcceptedForwardLinkList() {
        return acceptedForwardLinkList;
    }
    public boolean isAMODAcceptedForLinking(String value) {
        return f_acceptedAMODList.contains(value);
    }


    //functionality

    //checks whether a noun is a animate_thing
    public boolean isAnimate(String noun) {
       try {
            IIndexWord _idw = dict.getIndexWord(noun, POS.NOUN);
            return checkHypernymTree(_idw, ListUtils.getList("animate_thing"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //checks whether a noun can be a group_action
    public boolean canBeGroupAction(String mainNoun) {
        try {
            IIndexWord _idw = dict.getIndexWord(mainNoun, POS.NOUN);
            return checkHypernymTree(_idw, ListUtils.getList("group_action"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //checks wether a thing is a person or a system
    public  boolean canBePersonOrSystem(String fullNoun, String mainNoun) {
        try {
            if (Constants.f_personCorrectorList.contains(fullNoun)) {
                return true;
            }
            if (ProcessingUtils.isPersonalPronoun(mainNoun)) {
                return true;
            }
            IIndexWord _idw = dict.getIndexWord(fullNoun, POS.NOUN);
            if (_idw == null || (!_idw.getLemma().contains(mainNoun)))
                _idw = dict.getIndexWord(mainNoun, POS.NOUN);
            return checkHypernymTree(_idw, Constants.f_realActorDeterminers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //checks wether a noun is a timeperiod
    public  boolean isTimePeriod(String mainNoun) {
        try {
            IIndexWord _idw = dict.getIndexWord(mainNoun, POS.NOUN);
            return checkHypernymTree(_idw, ListUtils.getList("time_period"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //checks wether verb is an interaction verb
    public  boolean isInteractionVerb(Action a) {
        String _verb = getBaseForm(a.getName());
        if (Constants.f_interactionVerbs.contains(_verb)) {
            return true;
        }
        if (!isWeakVerb(_verb)) {
            try {
                IIndexWord _idw = null;
                if (a.getMod() != null && ((a.getModPos() - a.getWordIndex()) < 2)) {
                    _idw = dict.getIndexWord(_verb + " " + a.getMod() , POS.VERB);
                }
                if (_idw == null) {
                    _idw = dict.getIndexWord(_verb + " " + a.getPrt() , POS.VERB);
                }
                if (_idw == null) {
                    _idw =  dict.getIndexWord(_verb , POS.VERB);
                }
                if (_idw != null) {
                    return checkHypernymTree(_idw, Constants.f_interactionVerbs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    //helpers
    private  boolean checkHypernymTree(IIndexWord idw, List<String> wordsToCHeck) {
        if (idw != null) {
            //System.out.println("checking senses of: "+ idw.getLemma());

            List<IWordID> wordIDs = idw.getWordIDs();
            for (IWordID wi : wordIDs) {
                IWord word = dict.getWord(wi);
                ISynset syn = word.getSynset();

                //ignore instances
                List<ISynsetID> _pts = syn.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
                if (!_pts.isEmpty()) {
                    continue;
                }
                try{
                    if (canBe(syn, wordsToCHeck, new LinkedList<>())) {
                        return true;
                    }
                }catch(Exception e){
                        e.printStackTrace();
                }

            }
        }
        return false;
    }
    private  boolean canBe(ISynset s, List<String> lookFor, LinkedList<ISynset> checked) throws Exception {

        if (!checked.contains(s)) {
            checked.add(s); // to avoid circles
            for (String lf : lookFor) {
                List<IWord> words = s.getWords();

                for (IWord w : words){
                    if (w.getLemma().equals(lf)) {
                        return true;
                    }
                }
            }

            List<ISynsetID> _pts = s.getRelatedSynsets(Pointer.HYPERNYM);
            for (ISynsetID p : _pts) {
                if (canBe(dict.getSynset(p), lookFor, checked)) {
                    return true;
                }
            }
        }
        return false;
    }

    public  String deriveVerb(String noun) {
        try {
            IIndexWord _idw = dict.getIndexWord(noun, POS.NOUN);
            if (_idw == null) {
                System.err.println("Could not find IndexWord for: " + noun);
                return null;
            }
            String _selected = null;
            int _distance = 0;

            for (IWordID wi : _idw.getWordIDs()) {
                IWord word = dict.getWord(wi);
                System.out.println( "dev:" + word.getRelatedWords(Pointer.DERIVATIONALLY_RELATED).toString());
                ISynset syn = word.getSynset();
                for(IWord w : syn.getWords()){
                    System.out.println( "word:" +  w.getRelatedWords(Pointer.DERIVATIONALLY_RELATED).toString());
                }
                syn.getWords();
                System.out.println(syn.toString());
                List<ISynsetID> synsets = syn.getRelatedSynsets(Pointer.DERIVATIONALLY_RELATED);
                System.out.println(synsets.toString());

                for (ISynsetID s : synsets) {
                    if (s.getPOS() == POS.VERB) {

                        ISynset synset = dict.getSynset(s);
                        for (IWord w : synset.getWords()) {
                            int _d = StringUtils.getLevenshteinDistance(w.getLemma(), noun);
                            if (_selected == null || _d < _distance) {
                                _selected = w.getLemma();
                                _distance = _d;
                            }
                        }


                    }

                }

            }
            return _selected;

        }catch (Exception e) {
                    e.printStackTrace();
                    return null;
        }
    }


    /* private static void print(PointerTargetTree _tt) {
        if (_tt.getRootNode() != null) {
            System.out.println(_tt.getRootNode());
            _tt.getRootNode().getChildTreeList().print();
        }
    }*/

    public  boolean isWeakAction(Action a) {
        if (isWeakVerb(a.getName())) {
            if (a.getXcomp() == null || isWeakVerb(a.getXcomp().getVerb())) {
                return true;
            }
        }
        return false;
    }
    public  boolean isWeakVerb(String v) {
        return Constants.f_weakVerbs.contains(getBaseForm(v));
    }
    public  String getBaseForm(String verb) {
        return getBaseForm(verb, true, POS.VERB);
    }
    public  String getBaseForm(String verb, boolean keepAuxiliaries, POS pos) {
        String[] _parts = verb.split(" "); // verb can contain auxiliary verbs
        // (to acquire)
        try {
            IIndexWord word = dict.getIndexWord(_parts[_parts.length - 1], pos);
            if (word != null) {
                _parts[_parts.length - 1] = word.getLemma();
                StringBuilder _b = new StringBuilder();
                for (int i = keepAuxiliaries ? 0 : _parts.length - 1; i < _parts.length; i++) {
                    String _s = _parts[i];
                    _b.append(_s);
                    _b.append(' ');
                }
                _b.deleteCharAt(_b.length() - 1);
                return _b.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verb;
    }

    public boolean isMetaActor(String fullNoun, String noun) {
        if (!Constants.f_personCorrectorList.contains(fullNoun)) {
            try {
                IIndexWord _idw = dict.getIndexWord(fullNoun, POS.NOUN);
                if (_idw == null || (!_idw.getLemma().contains(noun)))
                    _idw = dict.getIndexWord(noun, POS.NOUN);
                return checkHypernymTree(_idw, Constants.f_metaActorsDeterminers);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean isVerbOfType(String verb, String type) {
        try {
            IIndexWord _idw = dict.getIndexWord(verb, POS.VERB);
            return checkHypernymTree(_idw, ListUtils.getList(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean canBeDataObject(String fullNoun, String noun) {
        try {
            IIndexWord _idw = dict.getIndexWord(fullNoun, POS.NOUN);

            if (_idw == null || (!_idw.getLemma().contains(noun)))
                _idw = dict.getIndexWord(noun, POS.NOUN);
            return checkHypernymTree(_idw, Constants.f_dataObjectDeterminers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    //test method
    public static void main (String[] args) {
        WordNetInitializer wni = new WordNetInitializer();
        wni.init();
        System.out.println(wni.acceptedForwardLinkList);
        System.out.println(wni.f_acceptedAMODList);


        System.out.println("animate" + wni.isAnimate("woman")); //läuft
        System.out.println(wni.canBeGroupAction("exchange")); //läuft
        System.out.println(wni.canBePersonOrSystem("Lisa", "she")); //läuft, aber schlecht
        System.out.println("time: " + wni.isTimePeriod("day")); //läuft, mittel gut

        T2PSentence s = new T2PSentence(2);
        Action a = new Action(s,4 , "send");
        System.out.println(wni.isInteractionVerb(a)); //läuft, aber rulebased!


        //System.out.println(wni.deriveVerb("talking"));

    }

}
