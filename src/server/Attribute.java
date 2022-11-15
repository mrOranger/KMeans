package server;

import java.io.Serializable;

public abstract class Attribute implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected String name;
	protected int index;
	
	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	String getName() {
		return name;
	}
	
	int getIndex() {
		return index;
	}
	
	public String toString() {
		return new String(name);
	}

}
