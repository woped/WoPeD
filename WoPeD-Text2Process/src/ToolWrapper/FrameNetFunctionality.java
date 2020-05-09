package ToolWrapper;

import TextToWorldModel.Constants;
import de.saar.coli.salsa.reiter.framenet.*;
import de.saar.coli.salsa.reiter.framenet.fncorpus.*;
import edu.mit.jwi.item.POS;
import TextToWorldModel.transform.SearchUtils;
import worldModel.Action;
import worldModel.SpecifiedElement;
import worldModel.Specifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class FrameNetFunctionality {

    private static FrameNet f_frameNet;
    private static AnnotationCorpus f_corpus;
    private FrameNetInitializer fni;

    //TODO: Usages ersetzen
    public enum PhraseType{
        CORE,
        PERIPHERAL,
        EXTRA_THEMATIC,
        GENITIVE,
        UNKNOWN
    }

    public FrameNetFunctionality (){
        fni = FrameNetInitializer.getInstance();
        f_frameNet = fni.getFN();
        f_corpus = fni.getCorpus();
    }

    public static void printAllFrames() {
        // Iterate over all frames
        for (Frame frame : f_frameNet.getFrames()) {

            // Print out the name of each frame
            System.out.println(frame.getName());

            // Iterate over all frame elements of the frame
            for (FrameElement fe : frame.frameElements()) {

                // Print out the name and semantic types of the frame element
                System.out.println("  " + fe.getName() + " / " + Arrays.toString(fe.getSemanticTypes()));
            }
            System.out.println(" This frame uses the frames: ");

            // Iterate over all frames that are used by this frame
            for (Frame uses : frame.uses()) {

                // Print out their names
                System.out.println("  " + uses.getName());
            }
        }
    }

    public static void determineSpecifierFrameElement(SpecifiedElement element, Specifier spec) {
        if("of".equals(spec.getHeadWord()) && !(element instanceof Action)){
            spec.setPhraseType(PhraseType.GENITIVE);
        }else {
            HashMap<FrameElement,Integer> _countMap = new HashMap<FrameElement, Integer>();
            boolean _verb = element instanceof Action;
            WordNetFunctionality wnf = new WordNetFunctionality();
            String _baseForm = wnf.getBaseForm(element.getName(), false, _verb? POS.VERB:POS.NOUN);
            if(_verb && ((Action)element).getPrt()!=null) {
                _baseForm += " "+((Action)element).getPrt();
            }
            Collection<LexicalUnit> _units = f_frameNet.getLexicalUnits(_baseForm, (_verb?"v":"n"));
            int _testedLUs = _units.size();
            for(LexicalUnit lu:_units) {
                AnnotatedLexicalUnit _alu = f_corpus.getAnnotation(lu);
                if(_alu != null) {
                    for(FEGroupRealization fegr:_alu.getFERealizations()) {
                        for(ValencePattern pat:fegr.getPatterns()) {
                            for(ValenceUnit vu:pat.getUnits()) {
                                if(vu.getPhraseType().startsWith("PP") && vu.getPhraseType().contains("["+spec.getHeadWord()+"]")) {
                                    if(_countMap.containsKey(vu.getFrameElement())) {
                                        Integer _newVal = _countMap.get(vu.getFrameElement()) + pat.getTotalCount();
                                        _countMap.put(vu.getFrameElement(), _newVal);
                                    }else {
                                        _countMap.put(vu.getFrameElement(),pat.getTotalCount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            FrameElement _best = (FrameElement) SearchUtils.getMaxCountElement(_countMap);
            if(_best == null) {
                spec.setPhraseType(PhraseType.UNKNOWN);
            }else {
                spec.setPhraseType(toPT(_best));
                spec.setFrameElement(_best);
            }
            if(Constants.DEBUG_FRAME_ASSIGNMENT) {
                System.out.println("Valence Units for: "+_baseForm+" ("+_testedLUs+") - "+spec.getPhrase());
                for(FrameElement fe:_countMap.keySet()) {
                    System.out.println("\t\t "+_countMap.get(fe)+" "+fe+" "+fe.getCoreTypeString());
                }
                System.out.println("\t+ Best: "+_best);
            }

        }
    }

    private static PhraseType toPT(FrameElement fe) {
        switch(fe.getCoreType()) {
            case Core: return PhraseType.CORE;
            case Core_Unexpressed: return PhraseType.CORE;
            case Extra_Thematic: return PhraseType.EXTRA_THEMATIC;
            case Peripheral: return PhraseType.PERIPHERAL;
            default: return PhraseType.UNKNOWN;
        }
    }
}
