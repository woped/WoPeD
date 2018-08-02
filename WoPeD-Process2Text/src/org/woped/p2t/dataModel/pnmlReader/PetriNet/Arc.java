package org.woped.p2t.dataModel.pnmlReader.PetriNet;

public class Arc {
    private final String id;
    private final String source;
    private final String target;

    public Arc(String id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}