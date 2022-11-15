package server;

import java.io.Serializable;

public class ContinuousAttribute extends Attribute implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private double max;
	private double min;
	
	public ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Restituisce il valore normalizzato del valore passato in input
	 * @return: valore normalizzato 
	 */
	public double getScaledValue(double v) {
		double norm = (v - min)/(max - min);
		return norm;
	}
	
	

}
