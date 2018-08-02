package org.woped.p2t.dataModel.process;

public class Gateway extends org.woped.p2t.dataModel.process.Element {
    private final int type;

    public Gateway(int id, String label, org.woped.p2t.dataModel.process.Lane lane, org.woped.p2t.dataModel.process.Pool pool, int type) {
        super(id, label, lane, pool);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}