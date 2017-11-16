package dataModel.process;


public abstract class Element {
	
	private int id;
	private String label;
	private Lane lane;
	private Pool pool;

	public Element(int id, String label, Lane lane, Pool pool) {
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
