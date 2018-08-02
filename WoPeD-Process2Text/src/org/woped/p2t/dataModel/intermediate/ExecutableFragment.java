package org.woped.p2t.dataModel.intermediate;

import java.util.ArrayList;

public class ExecutableFragment extends AbstractFragment {
    private final ArrayList<ExecutableFragment> sentenceList;

    public ExecutableFragment(String action, String bo, String role, String addition) {
        super(action, bo, role, addition);
        sentenceList = new ArrayList<>();
    }

    public ArrayList<ExecutableFragment> getSentencList() {
        return sentenceList;
    }

    public void addSentence(ExecutableFragment eFrag) {
        this.sentenceList.add(eFrag);
    }

    public int getListSize() {
        return sentenceList.size();
    }
}