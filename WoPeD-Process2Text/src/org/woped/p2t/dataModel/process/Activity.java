package org.woped.p2t.dataModel.process;

import java.util.ArrayList;

public class Activity extends Element {
    private int type;
    private ArrayList<Annotation> annotations;
    private ArrayList<Integer> attachedEvents;

    public Activity(int id, String label, Lane lane, Pool pool, int type) {
        super(id, label, lane, pool);
        this.type = type;
        annotations = new ArrayList<>();
        attachedEvents = new ArrayList<>();
    }

    public boolean hasAttachedEvents() {
        return attachedEvents.size() > 0;
    }

    public ArrayList<Integer> getAttachedEvents() {
        return attachedEvents;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public int getType() {
        return type;
    }

    void addAnnotation(Annotation a) {
        annotations.add(a);
    }
}