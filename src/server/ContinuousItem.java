package server;

import java.io.Serializable;

public class ContinuousItem extends Item implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public ContinuousItem (Attribute attribute, Double value) {
		super(attribute, value);
	}

	/**
	 * Determina la distanza (in valore assoluto) tra il valore scalato memorizzato nell' item corrente
	 * e quello scalato associato al valore del parametri. Per ottenere i valori scalati si usa getScaledvalue(Object)
	 */
	@Override
	double distance(Object item) {
		double distance_1 = ((ContinuousAttribute)attribute).getScaledValue(Double.parseDouble(this.getValue().toString()));
		double distance_2 = ((ContinuousAttribute)attribute).getScaledValue(new Double(Double.parseDouble(item.toString())));
		return (Math.abs(distance_1 - distance_2));
	}
	
	

}
