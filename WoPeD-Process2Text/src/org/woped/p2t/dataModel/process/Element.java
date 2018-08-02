package org.woped.p2t.dataModel.process;

public abstract class Element {
    private final int id;
    private final String label;
    private final org.woped.p2t.dataModel.process.Lane lane;
    private final org.woped.p2t.dataModel.process.Pool pool;

    Element(int id, String label, org.woped.p2t.dataModel.process.Lane lane, org.woped.p2t.dataModel.process.Pool pool) {
        this.id = id;
        this.label = label;
        this.lane = lane;
        this.pool = pool;
    }

    public org.woped.p2t.dataModel.process.Pool getPool() {
        return pool;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public org.woped.p2t.dataModel.process.Lane getLane() {
        return lane;
    }
}