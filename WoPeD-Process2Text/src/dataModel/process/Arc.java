package dataModel.process;

public class Arc {
    private final int id;
    private final String label;
    private final Element source;
    private final Element target;

    public Arc(int id, String label, Element source, Element target) {
        this.id = id;
        this.label = label;
        this.source = source;
        this.target = target;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Element getSource() {
        return source;
    }

    public Element getTarget() {
        return target;
    }

}