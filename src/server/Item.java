package server;

import java.io.Serializable;
import java.util.Set;

public abstract class Item implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected Attribute attribute;
	protected Object value;
	
	public Item(Attribute attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public Object getValue() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	abstract double distance(Object item);
	
	/**
	 * Modifica il parametro value, assegnandoli il valore di data.computePrototype(clusteredData, attribute);
	 * @param data: riferimento a Data;
	 * @param clusteredData: riferimento ad ArraySet
	 */
	public void update(Data data, Set<Integer> clusteredData) {
		value = data.computePrototype(clusteredData, attribute);
	}

}
