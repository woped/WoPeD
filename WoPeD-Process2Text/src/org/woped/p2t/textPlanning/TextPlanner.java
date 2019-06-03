package org.woped.p2t.textPlanning;


import dataModel.process.ActivityType;
import org.woped.p2t.contentDetermination.extraction.GatewayExtractor;
import org.woped.p2t.contentDetermination.labelAnalysis.EnglishLabelDeriver;
import org.woped.p2t.contentDetermination.labelAnalysis.EnglishLabelHelper;
import org.woped.p2t.dataModel.dsynt.DSynTConditionSentence;
import org.woped.p2t.dataModel.dsynt.DSynTMainSentence;
import org.woped.p2t.dataModel.dsynt.DSynTSentence;
import org.woped.p2t.dataModel.intermediate.AbstractFragment;
import org.woped.p2t.dataModel.intermediate.ConditionFragment;
import org.woped.p2t.dataModel.intermediate.ExecutableFragment;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.graph.algo.rpst.RPSTNode;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Node;
import de.hpi.bpt.process.Process;
import net.didion.jwnl.JWNLException;
import org.woped.p2t.dataModel.process.ProcessModel;
import org.woped.p2t.contentDetermination.preprocessing.FormatConverter;
import org.woped.p2t.textPlanning.recordClasses.ConverterRecord;
import org.woped.p2t.textPlanning.recordClasses.GatewayPropertyRecord;
import org.woped.p2t.textPlanning.recordClasses.ModifierRecord;
import org.woped.p2t.utils.Pair;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TextPlanner {
    private static final String[] quantifiers = {"a", "the", "all", "any", "more", "most", "none", "some", "such", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
    private final RPST<ControlFlow, Node> rpst;
    private final org.woped.p2t.dataModel.process.ProcessModel process;
    private final TextToIntermediateConverter textToIMConverter;
    private final ArrayList<ConditionFragment> passedFragments;
    private final ArrayList<ModifierRecord> passedMods; // used for Skips
    private final boolean isAlternative;
    private final ArrayList<DSynTSentence> sentencePlan;
    private final ArrayList<Pair<Integer, DSynTSentence>> activitiySentenceMap;
    private final EnglishLabelHelper lHelper;
    private final EnglishLabelDeriver lDeriver;
    private final boolean imperative;
    private final String imperativeRole;
    private ModifierRecord passedMod = null; // used for AND-Splits
    private boolean tagWithBullet = false;
    private boolean start = true;

    public TextPlanner(RPST<ControlFlow, Node> rpst, org.woped.p2t.dataModel.process.ProcessModel process, EnglishLabelDeriver lDeriver, EnglishLabelHelper lHelper, String imperativeRole, boolean imperative, boolean isAlternative) {
        this.rpst = rpst;
        this.process = process;
        this.lHelper = lHelper;
        this.lDeriver = lDeriver;
        textToIMConverter = new TextToIntermediateConverter(rpst, process, lHelper, imperativeRole, imperative);
        passedFragments = new ArrayList<>();
        sentencePlan = new ArrayList<>();
        activitiySentenceMap = new ArrayList<>();
        passedMods = new ArrayList<>();
        this.imperative = imperative;
        this.imperativeRole = imperativeRole;
        this.isAlternative = isAlternative;

    }

    /**
     * Text Planning Main
     */
    public void convertToText(RPSTNode<ControlFlow, Node> root, int level) throws JWNLException, FileNotFoundException {
        if (root == null) {
            return;
        }

        // Order nodes of current level with respect to control flow
        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = PlanningHelper.sortTreeLevel(root, root.getEntry(), rpst);

        // For each node of current level
        for (RPSTNode<ControlFlow, Node> node : orderedTopNodes) {

            if (PlanningHelper.isEvent(node.getExit())) {
                orderedTopNodes.indexOf(node);
                orderedTopNodes.size();
            }
            int depth = PlanningHelper.getDepth(node, rpst);
            if (PlanningHelper.isBond(node)) {

                // Converter Record
                ConverterRecord convRecord = null;

                //**************************************  LOOP - SPLIT  **************************************
                if (PlanningHelper.isLoop(node, rpst)) {
                    convRecord = getLoopConverterRecord(node);
                }
                //**************************************  SKIP - SPLIT  **************************************
                if (PlanningHelper.isSkip(node, rpst)) {
                    convRecord = getSkipConverterRecord(orderedTopNodes, node);
                }
                //**************************************  XOR - SPLIT  **************************************
                if (PlanningHelper.isXORSplit(node, rpst)) {
                    convRecord = getXORConverterRecord(node);
                }
                //**************************************  EVENT BASED - SPLIT  **************************************
                if (PlanningHelper.isEventSplit(node, rpst)) {
                    convRecord = getXORConverterRecord(node);
                }
                //**************************************  OR - SPLIT  **************************************
                if (PlanningHelper.isORSplit(node, rpst)) {
                    convRecord = getORConverterRecord(node);
                }
                //**************************************  AND - SPLIT  **************************************
                if (PlanningHelper.isANDSplit(node, rpst)) {
                    convRecord = getANDConverterRecord(node);
                }

                // Add pre statements
                if (convRecord != null && convRecord.preStatements != null) {
                    for (DSynTSentence preStatement : convRecord.preStatements) {
                        if (passedFragments.size() > 0) {
                            DSynTConditionSentence dsyntSentence = new DSynTConditionSentence(preStatement.getExecutableFragment(), passedFragments.get(0));
                            if (passedFragments.size() > 1) {
                                for (int i = 1; i < passedFragments.size(); i++) {
                                    dsyntSentence.addCondition(passedFragments.get(i), true);
                                    dsyntSentence.getConditionFragment().addCondition(passedFragments.get(i));
                                }
                            }
                            passedFragments.clear();
                            sentencePlan.add(dsyntSentence);
                        } else {
                            preStatement.getExecutableFragment().sen_level = level;
                            if (passedMods.size() > 0) {
                                preStatement.getExecutableFragment().addMod(passedMods.get(0).getLemma(), passedMods.get(0));
                                preStatement.getExecutableFragment().sen_hasConnective = true;
                                passedMods.clear();
                            }
                            sentencePlan.add(new DSynTMainSentence(preStatement.getExecutableFragment()));
                        }
                    }
                }

                // Pass precondition
                if (convRecord != null && convRecord.pre != null) {
                    if (passedFragments.size() > 0) {
                        if (passedFragments.get(0).getFragmentType() == AbstractFragment.TYPE_JOIN) {
                            ExecutableFragment eFrag = new ExecutableFragment("continue", "process", "", "");
                            eFrag.bo_isSubject = true;
                            DSynTConditionSentence dsyntSentence = new DSynTConditionSentence(eFrag, passedFragments.get(0));
                            sentencePlan.add(dsyntSentence);
                            passedFragments.clear();
                        }
                    }
                    passedFragments.add(convRecord.pre);
                }

                // Convert to Text
                if (PlanningHelper.isLoop(node, rpst) || PlanningHelper.isSkip(node, rpst)) {
                    convertToText(node, level);
                }
                if (PlanningHelper.isXORSplit(node, rpst) || PlanningHelper.isORSplit(node, rpst) || PlanningHelper.isEventSplit(node, rpst)) {
                    ArrayList<RPSTNode<ControlFlow, Node>> paths = PlanningHelper.sortTreeLevel(node, node.getEntry(), rpst);
                    for (RPSTNode<ControlFlow, Node> path : paths) {
                        tagWithBullet = true;
                        convertToText(path, level + 1);
                    }
                }
                if (PlanningHelper.isANDSplit(node, rpst)) {

                    // Determine path count
                    ArrayList<RPSTNode<ControlFlow, Node>> andNodes = PlanningHelper.sortTreeLevel(node, node.getEntry(), rpst);

                    if (andNodes.size() == 2) {
                        ArrayList<RPSTNode<ControlFlow, Node>> topNodes = PlanningHelper.sortTreeLevel(node, node.getEntry(), rpst);
                        RPSTNode<ControlFlow, Node> path1 = topNodes.get(0);
                        RPSTNode<ControlFlow, Node> path2 = topNodes.get(1);

                        // Convert both paths
                        convertToText(path1, level);
                        passedMod = convRecord.mod;
                        convertToText(path2, level);
                    } else {
                        ArrayList<RPSTNode<ControlFlow, Node>> paths = PlanningHelper.sortTreeLevel(node, node.getEntry(), rpst);
                        for (RPSTNode<ControlFlow, Node> path : paths) {
                            tagWithBullet = true;
                            convertToText(path, level + 1);
                        }
                    }
                }

                // Add post statement to sentence plan
                if (convRecord != null && convRecord.postStatements != null) {
                    sentencePlan.addAll(convRecord.postStatements);
                }

                // Pass post fragment
                if (convRecord != null && convRecord.post != null) {
                    passedFragments.add(convRecord.post);
                }
                //**************************************  ACTIVITIES  **************************************
            } else if (PlanningHelper.isTask(node.getEntry())) {
                convertActivities(node, level, depth);

                // Handle End Event
                if (PlanningHelper.isEvent(node.getExit())) {
                    org.woped.p2t.dataModel.process.Event event = process.getEvents().get((Integer.valueOf(node.getExit().getId())));
                    if (event.getType() == org.woped.p2t.dataModel.process.EventType.END_EVENT && orderedTopNodes.indexOf(node) == orderedTopNodes.size() - 1) {
                        // Adjust level and add to sentence plan
                        DSynTSentence sen = textToIMConverter.convertEvent(event).preStatements.get(0);
                        sen.getExecutableFragment().sen_level = level;
                        sentencePlan.add(sen);
                    }
                }
            }
            //**************************************  EVENTS  **************************************
//			 else if (PlanningHelper.isEvent(node.getEntry()) && orderedTopNodes.indexOf(node) > 0) {
            else if (PlanningHelper.isEvent(node.getEntry())) {
                org.woped.p2t.dataModel.process.Event event = process.getEvents().get((Integer.valueOf(node.getEntry().getId())));
                int currentPosition = orderedTopNodes.indexOf(node);
                // Start Event
                if (currentPosition == 0) {

                    // Start event should be printed
                    if (start && !isAlternative) {

                        // Event is followed by gateway --> full sentence
                        if (event.getType() == org.woped.p2t.dataModel.process.EventType.START_EVENT && currentPosition < orderedTopNodes.size() - 1 && PlanningHelper.isBond(orderedTopNodes.get(currentPosition + 1))) {
                            start = false;
                            ExecutableFragment eFrag = new ExecutableFragment("start", "process", "", "with a decision");
                            eFrag.add_hasArticle = false;
                            eFrag.bo_isSubject = true;
                            sentencePlan.add(new DSynTMainSentence(eFrag));
                        }
                        if (event.getType() != org.woped.p2t.dataModel.process.EventType.START_EVENT) {
                            start = false;
                            ConverterRecord convRecord = textToIMConverter.convertEvent(event);
                            if (convRecord != null && convRecord.hasPreStatements()) {
                                sentencePlan.add(convRecord.preStatements.get(0));
                            }
                        }
                    }


                    // Intermediate Events
                } else {
                    ConverterRecord convRecord = textToIMConverter.convertEvent(event);

                    // Add fragments if applicable
                    if (convRecord != null && convRecord.pre != null) {
                        passedFragments.add(convRecord.pre);
                    }

                    // Adjust level and add to sentence plan (first sentence not indented)
                    if (convRecord != null && convRecord.hasPreStatements()) {
                        for (int i = 0; i < convRecord.preStatements.size(); i++) {

                            DSynTSentence sen = convRecord.preStatements.get(i);

                            // If only one sentence (e.g. "Intermediate" End Event)
                            if (convRecord.preStatements.size() == 1) {
                                sen.getExecutableFragment().sen_level = level;
                            }

                            if (tagWithBullet) {
                                sen.getExecutableFragment().sen_hasBullet = true;
                                sen.getExecutableFragment().sen_level = level;
                                tagWithBullet = false;
                            }

                            if (i > 0) {
                                sen.getExecutableFragment().sen_level = level;
                            }
                            sentencePlan.add(sen);
                        }
                    }
                }
            } else {
                if (depth > 0) {
                    convertToText(node, level);
                }
            }
        }
    }

    private void convertActivities(RPSTNode<ControlFlow, Node> node, int level, int depth) throws JWNLException, FileNotFoundException {

        boolean planned = false;

        org.woped.p2t.dataModel.process.Activity activity = process.getActivity(Integer.parseInt(node.getEntry().getId()));
        org.woped.p2t.dataModel.process.Annotation anno = activity.getAnnotations().get(0);

        ExecutableFragment eFrag;

        // Start of the process
        if (start && !isAlternative) {

            start = false;
            ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
            modRecord.addAttribute("starting_point", "+");

            String bo = anno.getBusinessObjects().get(0);
            eFrag = new ExecutableFragment(anno.getActions().get(0), bo, "", anno.getAddition());
            eFrag.addAssociation(activity.getId());
            eFrag.addMod("the process begins when", modRecord);

            String role = getRole(activity, eFrag);
            eFrag.setRole(role);
            if (anno.getActions().size() == 2) {
                ExecutableFragment eFrag2;
                if (anno.getBusinessObjects().size() == 2) {
                    eFrag2 = new ExecutableFragment(anno.getActions().get(1), anno.getBusinessObjects().get(1), "", "");
                    eFrag2.addAssociation(activity.getId());
                } else {
                    eFrag2 = new ExecutableFragment(anno.getActions().get(1), "", "", "");
                    eFrag2.addAssociation(activity.getId());
                }
                correctArticleSettings(eFrag2);
                eFrag.addSentence(eFrag2);
            }

            if (bo.endsWith("s") && lHelper.isNoun(bo.substring(0, bo.length() - 1))) {
                eFrag.bo_hasArticle = true;
            } else {
                eFrag.bo_hasIndefArticle = true;
            }

            // If imperative mode
            if (imperative && imperativeRole.equals(role)) {
                eFrag.verb_isImperative = true;
                eFrag.role_isImperative = true;
            }
            correctArticleSettings(eFrag);
            DSynTMainSentence dsyntSentence = new DSynTMainSentence(eFrag);
            sentencePlan.add(dsyntSentence);
            activitiySentenceMap.add(new Pair<>(Integer.valueOf(node.getEntry().getId()), dsyntSentence));

            planned = true;
        }

        // Standard case
        eFrag = new ExecutableFragment(anno.getActions().get(0), anno.getBusinessObjects().get(0), "", anno.getAddition());
        eFrag.addAssociation(activity.getId());
        if (activity.getType() == ActivityType.TYPE_MAP.get("Subprocess")) {
            eFrag.setAddition("in a subprocess");
        }
        String role = getRole(activity, eFrag);
        eFrag.setRole(role);
        if (anno.getActions().size() == 2) {
            ExecutableFragment eFrag2;
            if (anno.getBusinessObjects().size() == 2) {
                eFrag2 = new ExecutableFragment(anno.getActions().get(1), anno.getBusinessObjects().get(1), "", "");
                if (eFrag.verb_IsPassive) {
                    if (anno.getBusinessObjects().get(0).equals("")) {
                        eFrag2.verb_IsPassive = true;
                        eFrag.setBo(eFrag2.getBo());
                        eFrag2.setBo("");
                        eFrag.bo_hasArticle = true;
                    } else {
                        eFrag2.verb_IsPassive = true;
                        eFrag2.bo_isSubject = true;
                    }

                }
            } else {
                eFrag2 = new ExecutableFragment(anno.getActions().get(1), "", "", "");
                if (eFrag.verb_IsPassive) {
                    eFrag2.verb_IsPassive = true;
                }
            }

            correctArticleSettings(eFrag2);
            eFrag2.addAssociation(activity.getId());
            eFrag.addSentence(eFrag2);
        }

        eFrag.sen_level = level;
        if (imperative && imperativeRole.equals(role)) {
            correctArticleSettings(eFrag);
            eFrag.verb_isImperative = true;
            eFrag.setRole("");
        }

        // In case of passed modifications (NOT AND - Split)
        if (passedMods.size() > 0 && !planned) {
            correctArticleSettings(eFrag);
            eFrag.addMod(passedMods.get(0).getLemma(), passedMods.get(0));
            eFrag.sen_hasConnective = true;
            passedMods.clear();
        }

        // In case of passed modifications (e.g. AND - Split)
        if (passedMod != null && !planned) {
            correctArticleSettings(eFrag);
            eFrag.addMod(passedMod.getLemma(), passedMod);
            eFrag.sen_hasConnective = true;
            passedMod = null;
        }

        if (tagWithBullet) {
            eFrag.sen_hasBullet = true;
            tagWithBullet = false;
        }

        // In case of passed fragments (General handling)
        if (passedFragments.size() > 0 && !planned) {
            correctArticleSettings(eFrag);
            DSynTConditionSentence dsyntSentence = new DSynTConditionSentence(eFrag, passedFragments.get(0));
            if (passedFragments.size() > 1) {
                for (int i = 1; i < passedFragments.size(); i++) {
                    dsyntSentence.addCondition(passedFragments.get(i), true);
                    dsyntSentence.getConditionFragment().addCondition(passedFragments.get(i));
                }
            }
            sentencePlan.add(dsyntSentence);
            activitiySentenceMap.add(new Pair<>(Integer.valueOf(node.getEntry().getId()), dsyntSentence));
            passedFragments.clear();
            planned = true;
        }

        if (!planned) {
            correctArticleSettings(eFrag);
            DSynTMainSentence dsyntSentence = new DSynTMainSentence(eFrag);
            sentencePlan.add(dsyntSentence);
            activitiySentenceMap.add(new Pair<>(Integer.valueOf(node.getEntry().getId()), dsyntSentence));
        }


        // If activity has attached Events
        if (activity.hasAttachedEvents()) {
            ArrayList<Integer> attachedEvents = activity.getAttachedEvents();
            HashMap<Integer, org.woped.p2t.dataModel.process.ProcessModel> alternativePaths = process.getAlternativePaths();
            for (Integer attEvent : attachedEvents) {
                if (alternativePaths.keySet().contains(attEvent)) {
                    System.out.println("Incorporating Alternative " + attEvent);
                    // Transform alternative
                    ProcessModel alternative = alternativePaths.get(attEvent);
                    alternative.annotateModel(lDeriver, lHelper);

                    // Consider complexity of the process
                    if (alternative.getElemAmount() <= 3) {
                        alternative.getEvents().get(attEvent).setLeadsToEnd(true);
                    }

                    FormatConverter rpstConverter = new FormatConverter();
                    Process p = rpstConverter.transformToRPSTFormat(alternative);
                    RPST<ControlFlow, Node> rpst = new RPST<>(p);
                    TextPlanner converter = new TextPlanner(rpst, alternative, lDeriver, lHelper, imperativeRole, imperative, true);
                    PlanningHelper.printTree(rpst.getRoot(), 0, rpst);
                    converter.convertToText(rpst.getRoot(), level + 1);
                    ArrayList<DSynTSentence> subSentencePlan = converter.getSentencePlan();
                    for (int i = 0; i < subSentencePlan.size(); i++) {
                        DSynTSentence sen = subSentencePlan.get(i);
                        if (i == 0) {
                            sen.getExecutableFragment().sen_level = level;
                        }
                        if (i == 1) {
                            sen.getExecutableFragment().sen_hasBullet = true;
                        }
                        sentencePlan.add(sen);
                    }

                    // Print sentence for subsequent normal execution
                    sentencePlan.add(textToIMConverter.getAttachedEventPostStatement(alternative.getEvents().get(attEvent)));
                }
            }
        }

        if (depth > 0) {
            convertToText(node, level);
        }
    }


    /**
     * Get ConverterRecord for AND
     */
    private ConverterRecord getANDConverterRecord(RPSTNode<ControlFlow, Node> node) {
        // Determine path count
        ArrayList<RPSTNode<ControlFlow, Node>> andNodes = PlanningHelper.sortTreeLevel(node, node.getEntry(), rpst);

        if (andNodes.size() == 2) {

            // Determine last activities of the AND split paths
            ArrayList<Node> conditionNodes = new ArrayList<>();
            for (RPSTNode<ControlFlow, Node> n : andNodes) {
                ArrayList<RPSTNode<ControlFlow, Node>> pathNodes = PlanningHelper.sortTreeLevel(n, n.getEntry(), rpst);
                Node lastNode = pathNodes.get(pathNodes.size() - 1).getEntry();
                if (PlanningHelper.isTask(lastNode)) {
                    conditionNodes.add(lastNode);
                }
            }
            return textToIMConverter.convertANDSimple(node, PlanningHelper.getActivityCount(andNodes.get(0), rpst), conditionNodes);

            // General case (paths > 2)
        } else {
            return textToIMConverter.convertANDGeneral(node, andNodes.size());
        }

    }


    /**
     * Get ConverterRecord for OR
     */
    private ConverterRecord getORConverterRecord(RPSTNode<ControlFlow, Node> node) {
        GatewayPropertyRecord orPropRec = new GatewayPropertyRecord(node, rpst);

        // Labeled Case
        if (orPropRec.isGatewayLabeled()) {
            return null;

            // Unlabeled case
        } else {
            return textToIMConverter.convertORSimple(node);
        }
    }

    /**
     * Get ConverterRecord for XOR
     */
    private ConverterRecord getXORConverterRecord(RPSTNode<ControlFlow, Node> node) {
        GatewayPropertyRecord propRec = new GatewayPropertyRecord(node, rpst);

        // Labeled Case with Yes/No - arcs and Max. Depth of 1
        if (propRec.isGatewayLabeled() && propRec.hasYNArcs() && propRec.getMaxPathDepth() == 1) {
            GatewayExtractor gwExtractor = new GatewayExtractor(node.getEntry(), lHelper);

            // Add sentence
            sentencePlan.addAll(textToIMConverter.convertXORSimple(node, gwExtractor));
            return null;
            // General case
        } else {
            return textToIMConverter.convertXORGeneral(node);
        }
    }

    /**
     * Get ConverterRecord for Loop
     */
    private ConverterRecord getLoopConverterRecord(RPSTNode<ControlFlow, Node> node) {
        RPSTNode<ControlFlow, Node> firstNodeInLoop = PlanningHelper.getNextActivity(node, rpst);
        return textToIMConverter.convertLoop(node, firstNodeInLoop);
    }

    /**
     * Get ConverterRecord for Skip
     */
    private ConverterRecord getSkipConverterRecord(ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes, RPSTNode<ControlFlow, Node> node) {
        GatewayPropertyRecord propRec = new GatewayPropertyRecord(node, rpst);

        // Yes-No Case
        if (propRec.isGatewayLabeled() && propRec.hasYNArcs()) {

            // Yes-No Case which is directly leading to the end of the process
            if (isToEndSkip(orderedTopNodes, node)) {
                return textToIMConverter.convertSkipToEnd(node);

                // General Yes/No-Case
            } else {
                return textToIMConverter.convertSkipGeneral(node);
            }

            // General unlabeled Skip
        } else {
            return textToIMConverter.convertSkipGeneralUnlabeled(node);
        }
    }

    /**
     * Evaluate whether skip leads to an end
     */
    private boolean isToEndSkip(ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes, RPSTNode<ControlFlow, Node> node) {
        int currentPosition = orderedTopNodes.indexOf(node);
        if (currentPosition < orderedTopNodes.size() - 1) {
            Node potEndNode = orderedTopNodes.get(currentPosition + 1).getExit();
            return PlanningHelper.isEndEvent(potEndNode, process);
        }
        return false;
    }


    /**
     * Returns role of a fragment.
     */
    private String getRole(org.woped.p2t.dataModel.process.Activity a, AbstractFragment frag) {
        if (a.getLane() == null) {
            frag.verb_IsPassive = true;
            frag.bo_isSubject = true;
            if (frag.getBo().equals("")) {
                frag.setBo("it");
                frag.bo_hasArticle = false;
            }
            return "";
        }
        String role = a.getLane().getName();
        if (role.equals("")) {
            role = a.getPool().getName();
        }
        if (role.equals("")) {
            frag.verb_IsPassive = true;
            frag.bo_isSubject = true;
            if (frag.getBo().equals("")) {
                frag.setBo("it");
                frag.bo_hasArticle = false;
            }
        }
        return role;
    }

    /**
     * Checks and corrects the article settings.
     */
    private void correctArticleSettings(AbstractFragment frag) {
        String bo = frag.getBo();
        if (bo.endsWith("s") && !bo.endsWith("ss") && frag.bo_hasArticle && lHelper.isNoun(bo.substring(0, bo.length() - 1))) {
            bo = bo.substring(0, bo.length() - 1);
            frag.setBo(bo);
            frag.bo_isPlural = true;
        }
        if (bo.contains("&")) {
            frag.bo_isPlural = true;
        }
        if (frag.bo_hasArticle) {
            String[] boSplit = bo.split(" ");
            if (boSplit.length > 1) {
                if (Arrays.asList(quantifiers).contains(boSplit[0].toLowerCase())) {
                    frag.bo_hasArticle = false;
                }
            }
        }
        if (bo.equals("") && frag.bo_hasArticle) {
            frag.bo_hasArticle = false;
        }
        if (bo.startsWith("their") || bo.startsWith("a ") || bo.startsWith("for")) {
            frag.bo_hasArticle = false;
        }
        String[] splitAdd = frag.getAddition().split(" ");
        frag.add_hasArticle = splitAdd.length <= 3 || !lHelper.isVerb(splitAdd[1]) || splitAdd[0].equals("on");

    }

    public ArrayList<DSynTSentence> getSentencePlan() {
        return sentencePlan;
    }
}