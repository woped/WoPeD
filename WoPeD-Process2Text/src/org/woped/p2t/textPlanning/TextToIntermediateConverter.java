package org.woped.p2t.textPlanning;


import org.woped.p2t.contentDetermination.extraction.GatewayExtractor;
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
import org.woped.p2t.dataModel.process.ProcessModel;
import org.woped.p2t.textPlanning.recordClasses.ConverterRecord;
import org.woped.p2t.textPlanning.recordClasses.ModifierRecord;

import java.util.ArrayList;
import java.util.HashMap;

class TextToIntermediateConverter {
    private final RPST<ControlFlow, Node> rpst;
    private final org.woped.p2t.dataModel.process.ProcessModel process;
    private final org.woped.p2t.contentDetermination.labelAnalysis.EnglishLabelHelper lHelper;
    private final boolean imperative;
    private final String imperativeRole;

    TextToIntermediateConverter(RPST<ControlFlow, Node> rpst, ProcessModel process, EnglishLabelHelper lHelper, String imperativeRole, boolean imperative) {
        this.rpst = rpst;
        this.process = process;
        this.lHelper = lHelper;
        this.imperative = imperative;
        this.imperativeRole = imperativeRole;
    }

    //*********************************************************************************************
    //										OR - SPLIT
    //*********************************************************************************************

    // The following optional parallel paths are available.
    ConverterRecord convertORSimple(RPSTNode<ControlFlow, Node> node) {
        // Create sentence "The following optional paths are available."
        ExecutableFragment eFrag = new ExecutableFragment("execute", "paths", "", "");
        ModifierRecord modRecord2 = new ModifierRecord(ModifierRecord.TYPE_ADJ, ModifierRecord.TARGET_BO);
        modRecord2.addAttribute("adv-type", "sentential");
        eFrag.addMod("one or more of the", modRecord2);
        ModifierRecord modRecord3 = new ModifierRecord(ModifierRecord.TYPE_ADJ, ModifierRecord.TARGET_BO);
        eFrag.addMod("following", modRecord3);
        eFrag.bo_isSubject = true;
        eFrag.bo_hasArticle = false;
        eFrag.verb_IsPassive = true;
        eFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

        ArrayList<DSynTSentence> preStatements = new ArrayList<>();
        preStatements.add(new DSynTMainSentence(eFrag));
        return new ConverterRecord(null, null, preStatements, null);
    }

    //*********************************************************************************************
    //										XOR - SPLIT
    //*********************************************************************************************
    ArrayList<DSynTSentence> convertXORSimple(RPSTNode<ControlFlow, Node> node, GatewayExtractor gwExtractor) {
        ExecutableFragment eFragYes = null;
        ExecutableFragment eFragNo = null;
        String role = "";

        ArrayList<RPSTNode<ControlFlow, Node>> pNodeList = new ArrayList<>(rpst.getChildren(node));
        for (RPSTNode<ControlFlow, Node> pNode : pNodeList) {
            for (RPSTNode<ControlFlow, Node> tNode : rpst.getChildren(pNode)) {
                if (tNode.getEntry() == node.getEntry()) {
                    for (org.woped.p2t.dataModel.process.Arc arc : process.getArcs().values()) {
                        if (arc.getSource().getId() == Integer.valueOf(tNode.getEntry().getId()) && arc.getTarget().getId() == Integer.valueOf(tNode.getExit().getId())) {
                            if (arc.getLabel().toLowerCase().equals("yes")) {
                                org.woped.p2t.dataModel.process.Activity a = process.getActivity(Integer.valueOf(tNode.getExit().getId()));
                                org.woped.p2t.dataModel.process.Annotation anno = a.getAnnotations().get(0);
                                String action = anno.getActions().get(0);
                                String bo = anno.getBusinessObjects().get(0);
                                role = a.getLane().getName();

                                String addition = anno.getAddition();
                                eFragYes = new ExecutableFragment(action, bo, role, addition);
                                eFragYes.addAssociation(Integer.valueOf(node.getExit().getId()));
                            }
                            if (arc.getLabel().toLowerCase().equals("no")) {
                                org.woped.p2t.dataModel.process.Activity a = process.getActivity(Integer.valueOf(tNode.getExit().getId()));
                                org.woped.p2t.dataModel.process.Annotation anno = a.getAnnotations().get(0);
                                String action = anno.getActions().get(0);
                                String bo = anno.getBusinessObjects().get(0);

                                role = a.getLane().getName();

                                String addition = anno.getAddition();
                                eFragNo = new ExecutableFragment(action, bo, role, addition);

                                ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
                                modRecord.addAttribute("adv-type", "sentential");
                                eFragNo.addMod("otherwise", modRecord);
                                eFragNo.sen_hasConnective = true;
                                eFragNo.addAssociation(Integer.valueOf(node.getExit().getId()));
                            }
                        }
                    }
                }
            }
        }

        ConditionFragment cFrag = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_IF, gwExtractor.getModList());
        cFrag.bo_replaceWithPronoun = true;
        cFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

        // If imperative mode
        if (imperative && imperativeRole.equals(role)) {
            eFragNo.setRole("");
            eFragNo.verb_isImperative = true;
            eFragYes.setRole("");
            eFragYes.verb_isImperative = true;
        }

        DSynTConditionSentence dsyntSentence1 = new DSynTConditionSentence(eFragYes, cFrag);
        DSynTMainSentence dsyntSentence2 = new DSynTMainSentence(eFragNo);
        ArrayList<DSynTSentence> sentences = new ArrayList<>();
        sentences.add(dsyntSentence1);
        sentences.add(dsyntSentence2);
        return sentences;
    }

    ConverterRecord convertXORGeneral(RPSTNode<ControlFlow, Node> node) {
        // One of the following branches is executed.  (And then use bullet points for structuring)
        ExecutableFragment eFrag = new ExecutableFragment("execute", "one of the following branches", "", "");
        eFrag.bo_isSubject = true;
        eFrag.verb_IsPassive = true;
        eFrag.bo_hasArticle = false;
        eFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

        ArrayList<DSynTSentence> preStatements = new ArrayList<>();
        preStatements.add(new DSynTMainSentence(eFrag));

        // Statement about negative case (process is finished)
        ConditionFragment post = new ConditionFragment("execute", "one of the alternative branches", "", "", ConditionFragment.TYPE_ONCE, new HashMap<>());
        post.verb_isPast = true;
        post.verb_IsPassive = true;
        post.bo_isSubject = true;
        post.bo_isPlural = false;
        post.bo_hasArticle = false;
        post.setFragmentType(AbstractFragment.TYPE_JOIN);
        post.addAssociation(Integer.valueOf(node.getEntry().getId()));

        return new ConverterRecord(null, post, preStatements, null, null);

    }


    //*********************************************************************************************
    //										LOOP - SPLIT
    //*********************************************************************************************

    /**
     * Converts a loop construct with labeled entry condition into two sentences.
     */
    ConverterRecord convertLoop(RPSTNode<ControlFlow, Node> node, RPSTNode<ControlFlow, Node> firstActivity) {
        // Labeled Case
        if (!node.getExit().getName().equals("")) {
            // Derive information from the gateway
            GatewayExtractor gwExtractor = new GatewayExtractor(node.getExit(), lHelper);

            // Generate general statement about loop
//			String  role = process.getGateways().get(Integer.valueOf(node.getEntry().getId())).getLane().getName();
            String role = getRole(node);

            ExecutableFragment eFrag = new ExecutableFragment("repeat", "step", role, "");
            eFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));
            ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADJ, ModifierRecord.TARGET_BO);
            eFrag.addMod("latter", modRecord);
            eFrag.bo_isPlural = true;

            ExecutableFragment eFrag2 = new ExecutableFragment("continue", "", "", "");
            eFrag2.addAssociation(Integer.valueOf(node.getEntry().getId()));
            eFrag.addSentence(eFrag2);
            if (role.equals("")) {
                eFrag.verb_IsPassive = true;
                eFrag.bo_isSubject = true;
                eFrag2.verb_IsPassive = true;
                eFrag2.setBo("it");
                eFrag2.bo_isSubject = true;
                eFrag2.bo_hasArticle = false;
            }

            org.woped.p2t.dataModel.process.Activity a = process.getActivity(Integer.valueOf(firstActivity.getExit().getId()));
            ExecutableFragment eFrag3 = new ExecutableFragment(a.getAnnotations().get(0).getActions().get(0), a.getAnnotations().get(0).getBusinessObjects().get(0), "", "");
            eFrag3.addAssociation(a.getId());
            eFrag3.sen_isCoord = false;
            eFrag3.verb_isParticiple = true;
            ModifierRecord modRecord2 = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
            modRecord2.addAttribute("adv-type", "sentential");
            eFrag3.addMod("with", modRecord2);
            eFrag2.addSentence(eFrag3);

            ConditionFragment cFrag = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_AS_LONG_AS, new HashMap<>(gwExtractor.getModList()));
            cFrag.verb_IsPassive = true;
            cFrag.bo_isSubject = true;
            cFrag.sen_headPosition = true;
            cFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

            // Determine postcondition
            gwExtractor.negateGatewayLabel();
            ConditionFragment post = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_ONCE, gwExtractor.getModList());
            post.verb_IsPassive = true;
            post.bo_isSubject = true;
            post.setFragmentType(AbstractFragment.TYPE_JOIN);
            post.addAssociation(Integer.valueOf(node.getEntry().getId()));

            // If imperative mode
            if (imperative && imperativeRole.equals(role)) {
                eFrag.setRole("");
                eFrag.verb_isImperative = true;
                eFrag2.verb_isImperative = true;
            }

            ArrayList<DSynTSentence> postStatements = new ArrayList<>();
            postStatements.add(new DSynTConditionSentence(eFrag, cFrag));
            return new ConverterRecord(null, post, null, postStatements);
        }
        // Unlabeled case
        else {
            ExecutableFragment eFrag = new ExecutableFragment("repeat", "step", "", "");
            ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADJ, ModifierRecord.TARGET_BO);
            eFrag.addMod("latter", modRecord);
            eFrag.bo_isPlural = true;
            eFrag.bo_isSubject = true;
            eFrag.verb_IsPassive = true;
            ExecutableFragment eFrag2 = new ExecutableFragment("continue", "", "", "");
            eFrag.addSentence(eFrag2);

            org.woped.p2t.dataModel.process.Activity a = process.getActivity(Integer.valueOf(firstActivity.getExit().getId()));

            // Loops require roles --> If no role is set, the catch block sets a default role 'Process'
            String role;
            try {
                role = a.getLane().getName();
                if (role.equals("")) {
                    role = a.getPool().getName();
                }
            }
            catch (NullPointerException e) {
                role = "process";
      //          System.out.println("Class TextToIntermediateConverter.java occured a: "+e+". Make sure to use roles when handeling loops. Default Role 'Process' used now.");
            }

            eFrag2.setRole(role);
            ExecutableFragment eFrag3 = new ExecutableFragment(a.getAnnotations().get(0).getActions().get(0), a.getAnnotations().get(0).getBusinessObjects().get(0), "", a.getAnnotations().get(0).getAddition());
            eFrag3.sen_isCoord = false;
            eFrag3.verb_isParticiple = true;
            ModifierRecord modRecord2 = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
            modRecord2.addAttribute("adv-type", "sentential");
            eFrag3.addMod("with", modRecord2);
            eFrag2.addSentence(eFrag3);

            ConditionFragment cFrag = new ConditionFragment("be", "dummy", "", "", ConditionFragment.TYPE_IF, new HashMap<>());
            cFrag.addMod("required", new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB));
            cFrag.bo_replaceWithPronoun = true;
            cFrag.bo_isSubject = true;
            cFrag.sen_headPosition = true;

            // Determine postcondition
            ConditionFragment post = new ConditionFragment("finish", "loop", "", "", ConditionFragment.TYPE_ONCE, new HashMap<>());
            post.verb_IsPassive = true;
            post.bo_isSubject = true;
            post.setFragmentType(AbstractFragment.TYPE_JOIN);

            // If imperative mode
            if (imperative && imperativeRole.equals(role)) {
                eFrag.setRole("");
                eFrag.verb_isImperative = true;
                eFrag2.verb_isImperative = true;
            }

            ArrayList<DSynTSentence> postStatements = new ArrayList<>();
            postStatements.add(new DSynTConditionSentence(eFrag, cFrag));
            return new ConverterRecord(null, post, null, postStatements);
        }
    }

    //*********************************************************************************************
    //										SKIP - SPLIT
    //*********************************************************************************************

    ConverterRecord convertSkipGeneralUnlabeled(RPSTNode<ControlFlow, Node> node) {
        ConditionFragment pre = new ConditionFragment("be", "dummy", "", "", ConditionFragment.TYPE_IF, new HashMap<>());
        ModifierRecord mod = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
        pre.addMod("necessary", mod);
        pre.bo_replaceWithPronoun = true;
        pre.sen_headPosition = true;
        pre.sen_isCoord = true;
        pre.sen_hasComma = true;
        pre.addAssociation(Integer.valueOf(node.getEntry().getId()));
        return new ConverterRecord(pre, null, null, null);
    }

    /**
     * Converts a standard skip construct with labeled condition gateway into two sentences.
     */
    ConverterRecord convertSkipGeneral(RPSTNode<ControlFlow, Node> node) {
        // Derive information from the gateway
        GatewayExtractor gwExtractor = new GatewayExtractor(node.getEntry(), lHelper);

        // Generate general statement about upcoming decision
        ConditionFragment pre = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_IN_CASE, gwExtractor.getModList());
        pre.verb_IsPassive = gwExtractor.hasVerb;
        pre.bo_isSubject = true;
        pre.sen_headPosition = true;
        pre.bo_isPlural = gwExtractor.bo_isPlural;
        pre.bo_hasArticle = gwExtractor.bo_hasArticle;
        pre.addAssociation(Integer.valueOf(node.getEntry().getId()));
        return new ConverterRecord(pre, null, null, null);
    }

    /**
     * Converts a standard skip construct with labeled condition gateway, leading to the end of the process, into two sentences.
     */
    ConverterRecord convertSkipToEnd(RPSTNode<ControlFlow, Node> node) {
        // Derive information from the gateway
        GatewayExtractor gwExtractor = new GatewayExtractor(node.getEntry(), lHelper);
        String role = getRole(node);

        // Generate general statement about upcoming decision
        ExecutableFragment eFrag = new ExecutableFragment("decide", "", role, "");
        ConditionFragment cFrag = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_WHETHER, gwExtractor.getModList());
        cFrag.verb_IsPassive = true;
        cFrag.bo_isSubject = true;
        cFrag.sen_headPosition = false;
        cFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));
        eFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

        if (role.equals("")) {
            eFrag.verb_IsPassive = true;
            eFrag.setBo("it");
            eFrag.bo_hasArticle = false;
            eFrag.bo_isSubject = true;
            cFrag.verb_IsPassive = true;
            cFrag.setBo("it");
            cFrag.bo_hasArticle = false;
            cFrag.bo_isSubject = true;
        }

        // Statement about negative case (process is finished)
        ExecutableFragment eFrag2 = new ExecutableFragment("finish", "process instance", "", "");
        eFrag2.verb_IsPassive = true;
        eFrag2.bo_isSubject = true;
        ConditionFragment cFrag2 = new ConditionFragment("be", "case", "this", "", ConditionFragment.TYPE_IF, new HashMap<>());
        cFrag2.verb_isNegated = true;

        // Determine precondition
        ConditionFragment pre = new ConditionFragment(gwExtractor.getVerb(), gwExtractor.getObject(), "", "", ConditionFragment.TYPE_IF, new HashMap<>());
        pre.verb_IsPassive = true;
        pre.sen_headPosition = true;
        pre.bo_isSubject = true;
        ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_PREP, ModifierRecord.TARGET_VERB);
        modRecord.addAttribute("adv-type", "sentential");
        pre.addMod("otherwise", modRecord);
        pre.sen_hasConnective = true;

        // If imperative mode
        if (imperative && imperativeRole.equals(role)) {
            eFrag.setRole("");
            eFrag.verb_isImperative = true;
        }

        ArrayList<DSynTSentence> preStatements = new ArrayList<>();
        preStatements.add(new DSynTConditionSentence(eFrag, cFrag));
        preStatements.add(new DSynTConditionSentence(eFrag2, cFrag2));

        return new ConverterRecord(pre, null, preStatements, null);
    }

    //*********************************************************************************************
    //										AND - SPLIT
    //*********************************************************************************************

    ConverterRecord convertANDGeneral(RPSTNode<ControlFlow, Node> node, int activities) {
        // The process is split into three parallel branches.  (And then use bullet points for structuring)
        ExecutableFragment eFrag = new ExecutableFragment("split", "process", "", "into " + activities + " parallel branches");
        eFrag.bo_isSubject = true;
        eFrag.verb_IsPassive = true;
        eFrag.add_hasArticle = false;
        eFrag.addAssociation(Integer.valueOf(node.getEntry().getId()));

        ArrayList<DSynTSentence> preStatements = new ArrayList<>();
        preStatements.add(new DSynTMainSentence(eFrag));

        // Statement about negative case (process is finished)
        ConditionFragment post = new ConditionFragment("execute", "all " + activities + " branch", "", "", ConditionFragment.TYPE_ONCE, new HashMap<>());
        post.verb_isPast = true;
        post.verb_IsPassive = true;
        post.bo_isSubject = true;
        post.bo_isPlural = true;
        post.bo_hasArticle = false;
        post.addAssociation(Integer.valueOf(node.getEntry().getId()));
        post.setFragmentType(AbstractFragment.TYPE_JOIN);

        return new ConverterRecord(null, post, preStatements, null, null);
    }

    /**
     * Converts a simple and construct.
     */
    ConverterRecord convertANDSimple(RPSTNode<ControlFlow, Node> node, int activities, ArrayList<Node> conditionNodes) {
        // get last element of both branches and combine them to a post condition
        // if one of them is a gateway, include gateway post condition in the and post condition
        ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
        modRecord.addAttribute("adv-type", "sentential");

        if (activities == 1) {
            modRecord.setLemma("In concurrency to the latter step,");
        } else {
            modRecord.setLemma("In concurrency to the latter " + activities + " steps,");
        }

        // Determine postcondition
        ConditionFragment post = null;
        String role = "";

        // Check whether postcondition should be passed
        int arcs = 0;
        for (org.woped.p2t.dataModel.process.Arc arc : process.getArcs().values()) {
            if (arc.getTarget().getId() == Integer.valueOf(node.getExit().getId())) {
                arcs++;
            }
        }

        // Only of no other arc flows into join gateway, join condition is passed
        if (arcs == 2) {
            if (conditionNodes.size() == 1) {
                org.woped.p2t.dataModel.process.Activity a = process.getActivity(Integer.valueOf(conditionNodes.get(0).getId()));
                String verb = a.getAnnotations().get(0).getActions().get(0);
                role = getRole(node);
                post = new ConditionFragment("finish", lHelper.getNoun(verb), role, "", ConditionFragment.TYPE_ONCE, new HashMap<>());
                post.sen_headPosition = true;
                post.verb_isPast = true;
                post.setFragmentType(AbstractFragment.TYPE_JOIN);
                post.addAssociation(Integer.valueOf(node.getEntry().getId()));
            } else {
                post = new ConditionFragment("finish", "both branch", "", "", ConditionFragment.TYPE_ONCE, new HashMap<>());
                post.bo_isPlural = true;
                post.sen_headPosition = true;
                post.bo_hasArticle = false;
                post.bo_isSubject = true;
                post.verb_isPast = true;
                post.verb_IsPassive = true;
                post.setFragmentType(AbstractFragment.TYPE_JOIN);
                post.addAssociation(Integer.valueOf(node.getEntry().getId()));
            }
        }

        // If imperative mode
        if (imperative && imperativeRole.equals(role)) {
            post.role_isImperative = true;
        }

        return new ConverterRecord(null, post, null, null, modRecord);
    }

    ConverterRecord convertEvent(org.woped.p2t.dataModel.process.Event event) {
        ConditionFragment cFrag;
        ExecutableFragment eFrag;
        ArrayList<DSynTSentence> preSentences;

        switch (event.getType()) {

            //***************************************************************
            // 				INTERMEDIATE (CATCHING) EVENTS
            //***************************************************************

            // ERROR EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_ERROR:
                String error = event.getLabel();

                if (error.equals("")) {
                    cFrag = new ConditionFragment("occur", "error", "", "", ConditionFragment.TYPE_IF, new HashMap<>());
                    cFrag.bo_hasIndefArticle = true;
                } else {
                    cFrag = new ConditionFragment("occur", "error '" + error + "'", "", "", ConditionFragment.TYPE_IF, new HashMap<>());
                    cFrag.bo_hasArticle = true;
                }
                cFrag.bo_isSubject = true;
                if (event.isAttached()) {
                    cFrag.setAddition("while latter task is executed,");
                }
                break;

            // TIMER EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_TIMER:
                String limit = event.getLabel();
                if (limit.equals("")) {
                    cFrag = new ConditionFragment("reach", "the time limit", "", "", ConditionFragment.TYPE_IF, new HashMap<>());
                } else {
                    cFrag = new ConditionFragment("reach", "the time limit of " + limit, "", "", ConditionFragment.TYPE_IF, new HashMap<>());
                }

                if (event.isAttached()) {
                    cFrag.setAddition("while latter task is executed,");
                }
                configureFragment(cFrag);
                break;

            // MESSAGE EVENT (CATCHING)
            case org.woped.p2t.dataModel.process.EventType.INTM_MSG_CAT:
                cFrag = new ConditionFragment("receive", "a message", "", "", ConditionFragment.TYPE_ONCE, new HashMap<>());
                configureFragment(cFrag);
                break;

            // ESCALATION EVENT (CATCHING)
            case org.woped.p2t.dataModel.process.EventType.INTM_ESCALATION_CAT:
                cFrag = new ConditionFragment("", "of an escalation", "", "", ConditionFragment.TYPE_IN_CASE, new HashMap<>());
                cFrag.bo_hasArticle = false;
                cFrag.bo_isSubject = true;
                break;

            //***************************************************************
            // 						START / END EVENTS
            //***************************************************************

            // END EVENT
            case org.woped.p2t.dataModel.process.EventType.END_EVENT:
                eFrag = new ExecutableFragment("finish", "process", "", "");
                eFrag.verb_IsPassive = true;
                eFrag.bo_isSubject = true;
                eFrag.bo_hasArticle = true;
                return getEventSentence(eFrag);

            // ERROR EVENT
            case org.woped.p2t.dataModel.process.EventType.END_ERROR:
                eFrag = new ExecutableFragment("end", "process", "", "with an error");
                eFrag.bo_isSubject = true;
                eFrag.bo_hasArticle = true;
                eFrag.add_hasArticle = false;
                return getEventSentence(eFrag);

            case org.woped.p2t.dataModel.process.EventType.END_SIGNAL:
                eFrag = new ExecutableFragment("end", "process", "", "with a signal.");
                eFrag.bo_isSubject = true;
                eFrag.bo_hasArticle = true;
                eFrag.add_hasArticle = false;
                return getEventSentence(eFrag);

            // START EVENT
            case org.woped.p2t.dataModel.process.EventType.START_MSG:
                cFrag = new ConditionFragment("receive", "message", "", "", ConditionFragment.TYPE_ONCE);
                cFrag.bo_isSubject = true;
                cFrag.verb_IsPassive = true;
                cFrag.bo_hasArticle = true;
                cFrag.bo_hasIndefArticle = true;
                eFrag = new ExecutableFragment("start", "process", "", "");
                eFrag.bo_isSubject = true;
                eFrag.bo_hasArticle = true;
                return getEventSentence(eFrag, cFrag);

            //***************************************************************
            // 						THROWING EVENTS
            //***************************************************************

            // MESSAGE EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_MSG_THR:
                eFrag = new ExecutableFragment("send", "message", event.getLane().getName(), "");
                eFrag.bo_hasIndefArticle = true;
                return getEventSentence(eFrag);

            // ESCALATION EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_ESCALATION_THR:
                eFrag = new ExecutableFragment("trigger", "escalation", event.getLane().getName(), "");
                eFrag.bo_hasIndefArticle = true;
                return getEventSentence(eFrag);

            // LINK EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_LINK_THR:
                eFrag = new ExecutableFragment("send", "signal", event.getLane().getName(), "");
                eFrag.bo_hasIndefArticle = true;
                return getEventSentence(eFrag);

            // MULTIPLE TRIGGER
            case org.woped.p2t.dataModel.process.EventType.INTM_MULTIPLE_THR:
                eFrag = new ExecutableFragment("cause", "multiple trigger", event.getLane().getName(), "");
                eFrag.bo_hasArticle = false;
                eFrag.bo_isPlural = true;
                return getEventSentence(eFrag);

            // SIGNAL EVENT
            case org.woped.p2t.dataModel.process.EventType.INTM_SIGNAL_THR:
                eFrag = new ExecutableFragment("send", "signal", event.getLane().getName(), "");
                eFrag.bo_hasArticle = true;
                eFrag.bo_hasIndefArticle = true;
                eFrag.bo_isPlural = true;
                return getEventSentence(eFrag);

            default:
                System.out.println("NON-COVERED EVENT " + event.getType());
                return null;

        }

        // Handling of intermediate Events (up until now only condition is provided)

        // Attached Event
        if (event.isAttached()) {
            preSentences = new ArrayList<>();
            preSentences.add(getAttachedEventSentence(event, cFrag));
            return new ConverterRecord(null, null, preSentences, null);

            // Non-attached Event
        } else {
            preSentences = new ArrayList<>();
            preSentences.add(getIntermediateEventSentence(cFrag));
            return new ConverterRecord(null, null, preSentences, null);
        }
    }

    /**
     * Returns Sentence for attached Event.
     */
    private DSynTConditionSentence getAttachedEventSentence(org.woped.p2t.dataModel.process.Event event, ConditionFragment cFrag) {
        ExecutableFragment eFrag = new ExecutableFragment("cancel", "it", "", "");
        eFrag.verb_IsPassive = true;
        eFrag.bo_isSubject = true;
        eFrag.bo_hasArticle = false;

        if (!event.isLeadsToEnd()) {
            ModifierRecord modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
            ExecutableFragment eFrag2 = new ExecutableFragment("continue", "process", "", "");
            modRecord.addAttribute("adv-type", "sent-final");
            modRecord.addAttribute("rheme", "+");
            eFrag2.addMod("as follows", modRecord);

            eFrag2.bo_isSubject = true;
            eFrag.addSentence(eFrag2);
        } else {
            ExecutableFragment eFrag2 = new ExecutableFragment("finish", "process", "", "");
            eFrag2.bo_isSubject = true;
            eFrag2.verb_IsPassive = true;
            eFrag.addSentence(eFrag2);
        }
        return new DSynTConditionSentence(eFrag, cFrag);
    }

    // For attached events only
    DSynTConditionSentence getAttachedEventPostStatement(org.woped.p2t.dataModel.process.Event event) {
        ModifierRecord modRecord;
        ModifierRecord modRecord2;
        ExecutableFragment eFrag;
        ConditionFragment cFrag;

        switch (event.getType()) {
            case org.woped.p2t.dataModel.process.EventType.INTM_TIMER:
                modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
                eFrag = new ExecutableFragment("continue", "process", "", "");
                eFrag.bo_isSubject = true;
                modRecord.addAttribute("adv-type", "sent-final");
                modRecord.addAttribute("rheme", "+");
                eFrag.addMod("normally", modRecord);

                cFrag = new ConditionFragment("complete", "the task", "", "within the time limit", ConditionFragment.TYPE_IF, new HashMap<>());
                cFrag.sen_hasConnective = true;
                cFrag.add_hasArticle = false;
                modRecord2 = new ModifierRecord(ModifierRecord.TYPE_PREP, ModifierRecord.TARGET_VERB);
                modRecord2.addAttribute("adv-type", "sentential");
                cFrag.addMod("otherwise", modRecord2);
                configureFragment(cFrag);
                return new DSynTConditionSentence(eFrag, cFrag);
            case org.woped.p2t.dataModel.process.EventType.INTM_ERROR:
                modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
                eFrag = new ExecutableFragment("continue", "process", "", "");
                eFrag.bo_isSubject = true;
                modRecord.addAttribute("adv-type", "sent-final");
                modRecord.addAttribute("rheme", "+");
                eFrag.addMod("normally", modRecord);

                cFrag = new ConditionFragment("complete", "the task", "", "without error", ConditionFragment.TYPE_IF, new HashMap<>());
                cFrag.sen_hasConnective = true;
                cFrag.add_hasArticle = false;
                modRecord2 = new ModifierRecord(ModifierRecord.TYPE_PREP, ModifierRecord.TARGET_VERB);
                modRecord2.addAttribute("adv-type", "sentential");
                cFrag.addMod("otherwise", modRecord2);
                configureFragment(cFrag);
                return new DSynTConditionSentence(eFrag, cFrag);
            case org.woped.p2t.dataModel.process.EventType.INTM_ESCALATION_CAT:
                modRecord = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
                eFrag = new ExecutableFragment("continue", "process", "", "");
                eFrag.bo_isSubject = true;
                modRecord.addAttribute("adv-type", "sent-final");
                modRecord.addAttribute("rheme", "+");
                eFrag.addMod("normally", modRecord);

                cFrag = new ConditionFragment("complete", "the task", "", "without escalation", ConditionFragment.TYPE_IF, new HashMap<>());
                cFrag.sen_hasConnective = true;
                cFrag.add_hasArticle = false;
                modRecord2 = new ModifierRecord(ModifierRecord.TYPE_PREP, ModifierRecord.TARGET_VERB);
                modRecord2.addAttribute("adv-type", "sentential");
                cFrag.addMod("otherwise", modRecord2);
                configureFragment(cFrag);
                return new DSynTConditionSentence(eFrag, cFrag);
            default:
                System.out.println("NON-COVERED EVENT " + event.getType());
                return null;
        }
    }

    /**
     * Returns record with sentence for throwing events.
     */
    private ConverterRecord getEventSentence(ExecutableFragment eFrag) {
        DSynTMainSentence msen = new DSynTMainSentence(eFrag);
        ArrayList<DSynTSentence> preSentences = new ArrayList<>();
        preSentences.add(msen);
        return new ConverterRecord(null, null, preSentences, null);
    }

    private ConverterRecord getEventSentence(ExecutableFragment eFrag, ConditionFragment cFrag) {
        DSynTConditionSentence msen = new DSynTConditionSentence(eFrag, cFrag);
        ArrayList<DSynTSentence> preSentences = new ArrayList<>();
        preSentences.add(msen);
        return new ConverterRecord(null, null, preSentences, null);
    }


    /**
     * Returns sentence for intermediate events.
     */
    private DSynTConditionSentence getIntermediateEventSentence(ConditionFragment cFrag) {
        ExecutableFragment eFrag = new ExecutableFragment("continue", "process", "", "");
        eFrag.bo_isSubject = true;
        return new DSynTConditionSentence(eFrag, cFrag);
    }

    /**
     * Configures condition fragment in a standard fashion.
     */
    private void configureFragment(ConditionFragment cFrag) {
        cFrag.verb_IsPassive = true;
        cFrag.bo_isSubject = true;
        cFrag.bo_hasArticle = false;
    }

    /**
     * Returns role executing current RPST node.
     */
    private String getRole(RPSTNode<ControlFlow, Node> node) {
        String role = process.getGateways().get(Integer.valueOf(node.getExit().getId())).getLane().getName();
        if (role.equals("")) {
            role = process.getGateways().get(Integer.valueOf(node.getExit().getId())).getPool().getName();
        }
        return role;
    }
}