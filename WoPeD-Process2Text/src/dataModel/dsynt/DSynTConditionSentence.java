package dataModel.dsynt;

import dataModel.intermediate.ConditionFragment;
import dataModel.intermediate.ExecutableFragment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import textPlanning.IntermediateToDSynTConverter;

public class DSynTConditionSentence extends DSynTSentence {
    // Private elements
    private final ConditionFragment cFrag;
    private Element cVerb;
    private Element cObject;
    private Element verb;

    public DSynTConditionSentence(ExecutableFragment eFrag, ConditionFragment cFrag) {
        this.eFrag = eFrag;
        this.cFrag = cFrag;
        this.createDSynTRepresentation();
    }

    private void createDSynTRepresentation() {
        // Create main sentence and get respective document containing the DSynT
        DSynTMainSentence dSynTMainSentence = new DSynTMainSentence(eFrag);
        doc = dSynTMainSentence.getDSynT();
        verb = dSynTMainSentence.getVerb();

        // Create verb (conditional sentence)
        cVerb = IntermediateToDSynTConverter.createVerb(doc, cFrag, IntermediateToDSynTConverter.VERB_TYPE_CONDITION);
        verb.appendChild(cVerb);

        // Create business object (conditional sentence)
        if (cFrag.hasBO()) {
            cObject = IntermediateToDSynTConverter.createBO(doc, cFrag);
            cVerb.appendChild(cObject);
        }

        // Create role (conditional sentence)
        if (!cFrag.getRole().equals("")) {
            Element cRole = IntermediateToDSynTConverter.createRole(doc, cFrag);
            cVerb.appendChild(cRole);
        }

        // Create addition
        if (!cFrag.getAddition().equals("")) {
            IntermediateToDSynTConverter.createAddition(doc, cVerb, cFrag);
        }

        if (cFrag.getAllMods() != null) {
            IntermediateToDSynTConverter.appendMods(doc, cFrag, cVerb, cObject);
        }

        // Create if Node
        Element condition = IntermediateToDSynTConverter.createConditionNode(doc, cFrag);
        cVerb.appendChild(condition);
    }

    /**
     * Adds supplementary conditions to condition fragment.
     */
    public void addCondition(ConditionFragment cFrag, boolean isAnd) {
        Element cObject2 = null;
        Element cRole2;

        // Create coordinating conjunction node
        Element add = doc.createElement("dsyntnode");
        add.setAttribute("rel", "COORD");
        if (isAnd) {
            add.setAttribute("lexeme", "AND");
        } else {
            add.setAttribute("lexeme", "OR");
        }
        cVerb.appendChild(add);

        // Create verb
        Element cVerb2 = IntermediateToDSynTConverter.createVerb(doc, cFrag, IntermediateToDSynTConverter.VERB_TYPE_SUBCONDITION);
        add.appendChild(cVerb2);

        // Create business object (conditional sentence)
        if (cFrag.hasBO()) {
            cObject2 = IntermediateToDSynTConverter.createBO(doc, cFrag);
            cVerb2.appendChild(cObject2);
        }

        // Create role (conditional sentence)
        if (!cFrag.getRole().equals("")) {
            cRole2 = IntermediateToDSynTConverter.createRole(doc, cFrag);
            cVerb2.appendChild(cRole2);
        }

        // Create addition
        if (!cFrag.getAddition().equals("")) {
            IntermediateToDSynTConverter.createAddition(doc, cVerb2, cFrag);
        }

        if (cFrag.getAllMods() != null) {
            IntermediateToDSynTConverter.appendMods(doc, cFrag, cVerb2, cObject2);
        }
    }

    public Element getVerb() {
        return verb;
    }

    public Document getDSynT() {
        return doc;
    }

    public ConditionFragment getConditionFragment() {
        return cFrag;
    }
}