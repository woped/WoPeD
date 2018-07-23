package TextToWorldModel;

import TextToWorldModel.Constants;
import TextToWorldModel.processing.ProcessingUtils;
import TextToWorldModel.transform.SearchUtils;
import ToolWrapper.FrameNetFunctionality;
import ToolWrapper.StanfordParserFunctionality;
import ToolWrapper.WordNetFunctionality;
import worldModel.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProcessLabelGenerator {

        private StanfordParserFunctionality stanford = StanfordParserFunctionality.getInstance();
        private FrameNetFunctionality framenet = new FrameNetFunctionality();
        private WordNetFunctionality wordnet= new WordNetFunctionality();



        //Nodes
        private final boolean EVENTS_TO_LABELS = true;
        private final boolean REMOVE_LOW_ENTROPY_NODES = true;
        private final boolean HIGHLIGHT_LOW_ENTROPY = true;
        //Labeling
        private final boolean ADD_UNKNOWN_PHRASETYPES =true;
        private final int MAX_NAME_DEPTH = 3;


        private void put(HashMap<Action, List<String>> os, Action a, String dataObj) {
            if(!os.containsKey(a)) {
                LinkedList<String> _list = new LinkedList<String>();
                os.put(a, _list);
            }
            os.get(a).add(dataObj);
        }

        private Specifier containsFrameElement(List<Specifier> specifiers,
                                               List<String> list) {
            for(Specifier sp:specifiers) {
                if(sp.getFrameElement() != null) {
                    if(list.contains(sp.getFrameElement().getName())) {
                        return sp;
                    }
                }
            }
            return null;

        }

        private boolean canBeTransformed(Action a) {
            if(a.getObject() != null && !ProcessingUtils.isUnrealActor(a.getObject())
                    && !a.getObject().needsResolve() && hasHiddenAction(a.getObject())) {
                return true;
            }
            if(a.getActorFrom() != null && ProcessingUtils.isUnrealActor(a.getActorFrom())
                    && hasHiddenAction(a.getActorFrom())) {
                return true;
            }
            return false;
        }


        private boolean hasHiddenAction(ExtractedObject obj) {
            boolean _canBeGerund = false;
            for(Specifier spec:obj.getSpecifiers(Specifier.SpecifierType.PP)) {
                if(spec.getName().startsWith("of")) {
                    _canBeGerund = true;
                }
            }
            if(!_canBeGerund) {
                return false;
            }
            if(obj != null) {
                for(String s:obj.getName().split(" ")) {
                    if(wordnet.deriveVerb(s) != null) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String getEventText(Action a) {
            StringBuilder _b = new StringBuilder();
            boolean _actorPlural = false;
            if(a.getActorFrom() != null) {
                _b.append(getName(a.getActorFrom(),true));
                _b.append(' ');
                _actorPlural = a.getActorFrom().getName().endsWith("s");
            }
            if(!wordnet.isWeakVerb(a.getName()) || (a.getCop() != null ||
                    a.getObject() != null || a.getSpecifiers().size()>0 || a.isNegated())) {
                boolean _auxIsDo = (a.getAux() != null && wordnet.getBaseForm(a.getAux()).equals("do"));
                if(a.isNegated() && (!wordnet.isWeakVerb(a.getName())||_auxIsDo)) {
                    if(a.getAux() != null && !wordnet.getBaseForm(a.getAux()).equals("be")) {
                        _b.append(a.getAux());
                    }else {
                        _b.append(_actorPlural ? "do" : ProcessingUtils.get3rdPsing("do"));
                    }
                    _b.append(" not ");
                    _b.append(wordnet.getBaseForm(a.getName()));
                    _b.append(' ');
                }else {
                    if(a.getAux() != null) {
                        if(a.getActorFrom() != null && !a.getActorFrom().getPassive()) {
                            _b.append(a.getAux());
                            _b.append(' ');
                            _b.append(a.getName());
                        }else{
                            _b.append(ProcessingUtils.get3rdPsing(a.getName()));
                        }

                    }else {
                        _b.append(_actorPlural ? wordnet.getBaseForm(a.getName()) : ProcessingUtils.get3rdPsing(a.getName()));
                    }
                    if(a.isNegated()) {
                        _b.append(" not ");
                    }
                }
                _b.append(' ');
            }


            if(a.getCop() != null) {
                _b.append(a.getCop());
            }else {
                if(a.getObject() != null) {
                    _b.append(getName(a.getObject(),true));
                }else {
                    if(a.getSpecifiers().size() > 0) {
                        _b.append(a.getSpecifiers().get(0).getPhrase());
                    }
                }
            }
            return _b.toString();
        }


        public String createTaskText(Action a) {
            String base=wordnet.getBaseForm(a.getVerb());
            StringBuilder _b = new StringBuilder();
            if(a.isNegated()) {
                if(a.getAux() != null) {
                    _b.append(a.getAux());
                    _b.append(' ');
                }
                _b.append("not");
                _b.append(' ');
            }
            if(wordnet.isWeakAction(a) && canBeTransformed(a)) {
                if(a.getActorFrom() != null && a.getActorFrom().isUnreal() && hasHiddenAction(a.getActorFrom())) {
                    _b.append(transformToAction(a.getActorFrom()));
                }else if(a.getObject() != null && ((a.getObject() instanceof Resource) ||  !((Actor)a.getObject()).isUnreal())) {
                    _b.append(transformToAction(a.getObject()));
                }
            }else {
                boolean _weak = wordnet.isWeakVerb(a.getName());
                if(!_weak) {
                    _b.append(wordnet.getBaseForm(a.getName()));
                    if(a.getPrt()!= null) {
                        _b.append(' ');
                        _b.append(a.getPrt());
                    }
                    _b.append(' ');
                }else {
                    //a weak verb which cannot be transformed hmmm.....
                    if(REMOVE_LOW_ENTROPY_NODES && (a.getActorFrom() == null || a.getActorFrom().isMetaActor()) &&
                            a.getXcomp() == null) {
                        return "Dummy";
                    }else {
                        if(a.getXcomp() == null) { //hm we should add something here or the label is empty
                            _b.append(getEventText(a));
                            return _b.toString().replaceAll("  ", " ");
                        }
                    }
                }
                boolean _xCompAdded = false;
                boolean _modAdded = false;
                if(a.getObject() != null) {
                    if(a.getMod() != null && (a.getModPos() < a.getObject().getWordIndex())) {
                        addMod(a,_b);
                        _b.append(' ');
                        _modAdded = true;
                    }
                    if(a.getXcomp()!= null && (a.getXcomp().getWordIndex() < a.getObject().getWordIndex())) {
                        addXComp(a, _b,!_weak);
                        _b.append(' ');
                        _xCompAdded = true;
                    }
                    if(a.getSpecifiers(Specifier.SpecifierType.IOBJ).size() > 0) {
                        for(Specifier spec:a.getSpecifiers(Specifier.SpecifierType.IOBJ)) {
                            _b.append(spec.getPhrase());
                            _b.append(' ');
                        }
                    }
                    _b.append(getName(a.getObject(),true));
                    //
                    for(Specifier _dob : a.getSpecifiers(Specifier.SpecifierType.DOBJ)) {
                        _b.append(' ');
                        _b.append(getName(_dob.getObject(),true));
                    }

                }
                if(!_modAdded) {
                    addMod(a, _b);
                }
                if(!_xCompAdded && a.getXcomp() != null) {
                    addSpecifiers(a, _b,a.getXcomp().getWordIndex(),true);
                    addXComp(a, _b,!_weak || a.getObject() != null);
                }
                addSpecifiers(a, _b,getXCompPos(a.getXcomp()),false);
                if(!(a.getObject() == null)) { //otherwise addSpecifiers already did the work!
                    for(Specifier sp:a.getSpecifiers(Specifier.SpecifierType.PP)) {
                        if((sp.getName().startsWith("to") || sp.getName().startsWith("in") || sp.getName().startsWith("about"))
                                && !(SearchUtils.startsWithAny(sp.getPhrase(), Constants.f_conditionIndicators))) {
                            _b.append(' ');
                            if(sp.getObject() != null) {
                                _b.append(sp.getHeadWord());
                                _b.append(' ');
                                _b.append(getName(sp.getObject(),true));
                            }else {
                                _b.append(sp.getName());
                                break; // one is enough
                            }
                        }
                    }
                }
            }

            if(base.equals(_b) || a.getCop()!=null){
                    _b.append(a.getCop());
            }

            return _b.toString().replaceAll("  ", " ");
        }

        private int getXCompPos(Action xcomp) {
            if(xcomp == null) {
                return -1;
            }
            return xcomp.getWordIndex();
        }

        private void addMod(Action a, StringBuilder _b) {
            if(a.getMod() != null){
                _b.append(' ');
                _b.append(a.getMod());
            }
        }

        private void addSpecifiers(Action a, StringBuilder _b, int limit,boolean smaller) {
            if(a.getObject() == null) {
                List<Specifier> _specs = a.getSpecifiers(Specifier.SpecifierType.PP);
                if(a.getXcomp() == null) {
                    _specs.addAll(a.getSpecifiers(Specifier.SpecifierType.SBAR));
                }
                Collections.sort(_specs);
                boolean _foundSth = false;
                for(Specifier spec:_specs) {
                    if(spec.getType() == Specifier.SpecifierType.SBAR && _foundSth == true) {
                        break;
                    }
                    if(spec.getWordIndex() > a.getWordIndex()) {
                        boolean _smaller = spec.getWordIndex() < limit;
                        if(!(_smaller^smaller)) {
                            if(considerPhrase(spec)){
                                _foundSth = true;
                                _b.append(' ');
                                if(spec.getObject() != null) {
                                    _b.append(spec.getHeadWord());
                                    _b.append(' ');
                                    _b.append(getName(spec.getObject(),true));
                                }else {
                                    _b.append(spec.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean considerPhrase(Specifier spec) {
            if(spec.getPhraseType() == FrameNetFunctionality.PhraseType.PERIPHERAL || spec.getPhraseType() == FrameNetFunctionality.PhraseType.EXTRA_THEMATIC) {
                return false;
            }else {
                if(spec.getPhraseType() == FrameNetFunctionality.PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
                    return true;
                }
            }
            return true; //always accept core and genetive
        }

        private void addXComp(Action a, StringBuilder _b,boolean needsAux) {
            if(a.getXcomp() != null) {
                if(needsAux) {
                    if(a.getXcomp().getAux() != null) {
                        _b.append(' ');
                        _b.append(a.getXcomp().getAux());
                        _b.append(' ');
                    }else {
                        _b.append(" to ");
                    }
                }
                _b.append(createTaskText( a.getXcomp()));
            }
        }

        private String transformToAction(ExtractedObject obj) {
            StringBuilder _b = new StringBuilder();
            for(String s:obj.getName().split(" ")) {
                String _der = wordnet.deriveVerb(s);
                if(_der != null) {
                    _b.append(_der);
                    break;
                }
            }
            for(Specifier spec:obj.getSpecifiers(Specifier.SpecifierType.PP)) {
                if(spec.getPhrase().startsWith("of") && spec.getObject() != null) {
                    _b.append(' ');
                    _b.append(getName(spec.getObject(),true));
                }
            }
            return _b.toString();
        }

        private String getName(ExtractedObject a,boolean addDet) {
            return getName(a, addDet, 1);
        }

        private String getName(ExtractedObject a,boolean addDet,int level) {
            return getName(a, addDet, level, false);
        }

        private String getName(ExtractedObject a,boolean addDet,int level,boolean compact) {
            if(a == null) {
                return "null";
            }
            if(a.needsResolve() && a.getReference() instanceof ExtractedObject) {
                return getName((ExtractedObject)a.getReference(),addDet);
            }
            StringBuilder _b = new StringBuilder();
            if(addDet && Constants.f_wantedDeterminers.contains(a.getDeterminer())) {
                _b.append(a.getDeterminer());
                _b.append(' ');
            }
            for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.AMOD)) {
                _b.append(s.getName());
                _b.append(' ');
            }
            for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.NUM)) {
                _b.append(s.getName());
                _b.append(' ');
            }
            for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.NN)) {
                _b.append(s.getName());
                _b.append(' ');
            }
            _b.append(a.getName());
            for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.NNAFTER)) {
                _b.append(' ');
                _b.append(s.getName());
            }
            if(level <= MAX_NAME_DEPTH)
                for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.PP)) {
                    if(s.getPhraseType() == FrameNetFunctionality.PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
                        if(s.getName().startsWith("of") ||
                                (!compact && s.getName().startsWith("into")) ||
                                (!compact && s.getName().startsWith("under")) ||
                                (!compact && s.getName().startsWith("about"))) {
                            addSpecifier(level, _b, s,compact);
                        }
                    }else if(considerPhrase(s)) {
                        addSpecifier(level, _b, s,compact);
                    }

                }
            if(!compact) {
                for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.INFMOD)) {
                    _b.append(' ');
                    _b.append(s.getName());
                }for(Specifier s:a.getSpecifiers(Specifier.SpecifierType.PARTMOD)) {
                    _b.append(' ');
                    _b.append(s.getName());
                }
            }
            return _b.toString();
        }

        private void addSpecifier(int level, StringBuilder _b, Specifier s,boolean compact) {
            _b.append(' ');
            if(s.getObject() != null) {
                _b.append(s.getHeadWord());
                _b.append(' ');
                _b.append(getName(s.getObject(),true,level+1,compact));

            }else {
                _b.append(s.getName());
            }
        }

    }


