package dataModel.process;

public abstract class Element {
    private final int id;
    private final String label;
    private final Lane lane;
    private final Pool pool;

    Element(int id, String label, Lane lane, Pool pool) {
        this.id = id;
        this.label = label;
        this.lane = lane;
        this.pool = pool;
    }

    public Pool getPool() {
        return pool;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Lane getLane() {
        return lane;
    }
}