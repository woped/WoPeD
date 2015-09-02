package de.hpi.bpt.process;


public class Gateway extends Node {	
	
	private GatewayType type;

	public Gateway(GatewayType type) {
		this.type = type;
	}
	
	public Gateway(GatewayType type, String name) {
		this.type = type;
		this.setName(name);
	}
	
	public GatewayType getGatewayType() {
		return type;
	}

	public void setGatewayType(GatewayType type) {
		this.type = type;
	}
	
	public boolean isXOR() {
		return this.type == GatewayType.XOR;
	}
	
	public boolean isAND() {
		return this.type == GatewayType.AND;
	}
	
	public boolean isOR() {
		return this.type == GatewayType.OR;
	}
}
