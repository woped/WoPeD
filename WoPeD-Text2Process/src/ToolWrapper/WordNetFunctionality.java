package ToolWrapper;

import TextToWorldModel.WorldModelBuilder;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.item.*;
import etc.Constants;
import org.apache.commons.lang3.StringUtils;
import processing.ProcessingUtils;
import transform.ListUtils;
import worldModel.Action;
import worldModel.WorldModel;

import java.util.LinkedList;
import java.util.List;

public class WordNetFunctionality {

    private IRAMDictionary dict;
    private WordNetInitializer wni;

    public WordNetFunctionality (){
        wni = WordNetInitializer.getInstance();
        dict = wni.getDict();
    }

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
    //derive verb from noun
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
                List<IWordID> related = word.getRelatedWords(Pointer.DERIVATIONALLY_RELATED);

                for(IWordID r : related){
                    if(r.getPOS() == POS.VERB) {

                        IWord w = dict.getWord(r);
                        ISynset synset = w.getSynset();
                        for (IWord x : synset.getWords()) {
                            int _d = StringUtils.getLevenshteinDistance(x.getLemma(), noun);
                            if (_selected == null || _d < _distance) {
                                _selected = x.getLemma();
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
    //???
    public  boolean isWeakAction(Action a) {
        if (isWeakVerb(a.getName())) {
            if (a.getXcomp() == null || isWeakVerb(a.getXcomp().getVerb())) {
                return true;
            }
        }
        return false;
    }
    //checks if verb is a weak verb
    public  boolean isWeakVerb(String v) {
        return Constants.f_weakVerbs.contains(getBaseForm(v));
    }
    //???
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
    //checks verbtype
    public boolean isVerbOfType(String verb, String type) {
        try {
            IIndexWord _idw = dict.getIndexWord(verb, POS.VERB);
            return checkHypernymTree(_idw, ListUtils.getList(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //checks data object
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
    //???
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



    public static void main (String[] args) {
        WordNetFunctionality wnf = new WordNetFunctionality();

        System.out.println("animate: " + wnf.isAnimate("woman"));
        System.out.println("group action: " + wnf.canBeGroupAction("exchange"));
        System.out.println("is person or system: " + wnf.canBePersonOrSystem("Lisa", "she"));
        System.out.println("time: " + wnf.isTimePeriod("day"));
        System.out.println(wnf.deriveVerb("speaker"));
        System.out.println(wnf.isWeakVerb("be"));
        System.out.println(wnf.isVerbOfType("run", "move"));
        System.out.println(wnf.canBeDataObject("email", "text"));

        /**** Mock WorldModel ****/
        WorldModelBuilder WMBuilder = new WorldModelBuilder("He begin and finishes the document. If he likes it, he sends it to the office. Otherwise he throws it in the bin.");
        WorldModel test = WMBuilder.buildWorldModel(true);
        List<Action> a = test.getActions();

        System.out.println(wnf.isWeakAction(a.get(0)));
        System.out.println(wnf.getBaseForm("does"));
        System.out.println(wnf.isMetaActor("step", "task"));
    }
}
