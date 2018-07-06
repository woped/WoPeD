package textPlanning.recordClasses;

import dataModel.dsynt.DSynTSentence;
import dataModel.intermediate.ConditionFragment;

import java.util.ArrayList;

public class ConverterRecord {

    public final ArrayList<DSynTSentence> preStatements;
    public final ArrayList<DSynTSentence> postStatements;
    public final ConditionFragment pre;
    public final ConditionFragment post;
    public ModifierRecord mod;

    public ConverterRecord(ConditionFragment pre, ConditionFragment post, ArrayList<DSynTSentence> preStatements, ArrayList<DSynTSentence> postStatements) {
        this.pre = pre;
        this.post = post;
        this.preStatements = preStatements;
        this.postStatements = postStatements;
    }

    public ConverterRecord(ConditionFragment pre, ConditionFragment post, ArrayList<DSynTSentence> preStatements, ArrayList<DSynTSentence> postStatements, ModifierRecord mod) {
        this.pre = pre;
        this.post = post;
        this.preStatements = preStatements;
        this.postStatements = postStatements;
        this.mod = mod;
    }

    public boolean hasPreStatements() {
        return preStatements != null;
    }

}
