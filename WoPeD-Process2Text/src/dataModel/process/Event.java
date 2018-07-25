package dataModel.process;

public class Event extends Element {
    private final int type;
    private final boolean isAttached;
    private boolean leadsToEnd;

    public Event(int id, String label, Lane lane, Pool pool, int type) {
        super(id, label, lane, pool);
        this.type = type;
        leadsToEnd = false;
        isAttached = false;
    }

    public boolean isLeadsToEnd() {
        return leadsToEnd;
    }

    public void setLeadsToEnd(boolean leadsToEnd) {
        this.leadsToEnd = leadsToEnd;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public int getType() {
        return type;
    }
}