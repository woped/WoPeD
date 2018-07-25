package dataModel.process;

import java.util.ArrayList;

public class Annotation {
    private final ArrayList<String> actions;
    private final ArrayList<String> businessObjects;
    private String addition;

    public Annotation() {
        actions = new ArrayList<>();
        businessObjects = new ArrayList<>();
        addition = "";
    }

    void addAction(String action) {
        actions.add(action);
    }

    void addBusinessObjects(String bo) {
        businessObjects.add(bo);
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public ArrayList<String> getBusinessObjects() {
        return businessObjects;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String add) {
        addition = add;
    }

    public String toString() {
        return actions.toString() + " " + businessObjects.toString() + " " + addition;
    }
}