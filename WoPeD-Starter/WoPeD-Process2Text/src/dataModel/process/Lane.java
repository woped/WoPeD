package dataModel.process;


public class Lane {
	
	private int id;
	private String name;
	private Pool pool;
	
	public Lane(int id, String name, Pool pool) {
		this.id = id;
		this.name = name;
		this.pool = pool;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Pool getPool() {
		return pool;
	}
	
	

}
