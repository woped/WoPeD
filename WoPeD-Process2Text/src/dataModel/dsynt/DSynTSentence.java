package dataModel.dsynt;

import dataModel.intermediate.ExecutableFragment;
import org.w3c.dom.Document;

public abstract class DSynTSentence {
    Document doc;
    ExecutableFragment eFrag;

    public Document getDSynT() {
        return doc;
    }

    public ExecutableFragment getExecutableFragment() {
        return eFrag;
    }
}