package server;

import java.io.Serializable;

public class DiscreteItem extends Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public DiscreteItem(Attribute attribute, Object value) {
		super(attribute, value);
	}
	
	/**
	 * Restituisce 0 se l' elemento è uguale a value della superclasse
	 * @param Object: riferimento ad un Oggetto 
	 */
	@Override
	double distance(Object item) {
		if(getValue().equals(item)) {
			return 0;
		}else {
			
			return 1;
		}
	}
}
