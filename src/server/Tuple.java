package server;

import java.io.Serializable;

public class Tuple implements Serializable {
	
	private static final long serialVersionUID = 1L;
	Item[] tuple;
	
	public Tuple(int size) {
		tuple = new Item[size];
	}
	
	/**
	 * Retituisce la lunghezza delle tuple
	 * @return lunghezza di tuple
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * Restituisce il valore della tupla all' indice index
	 * @param index: indice della tuple
	 * @return tupla all' indice index
	 */
	public Item getItem(int index) {
		return tuple[index];
	}
	
	/**
	 * Assegna il valore di item nella tupla all' indice index
	 * @param item: valore da memorizzare
	 * @param index: indice di tuple da scriver
	 */
	public void add(Item item, int index) {
		tuple[index] = item;
	}
	/**
	 * Determina la distanza tra la tupla riferita dal parametro e la tupla corrente
	 * @param obj: tupla da cui calcolare la distanza 
	 * @return distanza tra le tuple
	 */
	public double getDistance(Tuple obj) {
		int currentLength = 0;
		double distance = 0;
		if(obj.getLength() > this.getLength()) {
			currentLength = this.getLength();
		}else {
			currentLength = obj.getLength();
		}
		for(int i = 0; i < currentLength; i++) {
			distance += this.getItem(i).distance(obj.getItem(i).getValue());
		}
		return distance;
	}
	
	/**
	 * Restitusce la media delle distanze tra la tupla corrente e quelle ottenute dalle righe della matrice Data
	 * aventi indice in Clustered Data.
	 * @param data: riferimento all' oggetto Data
	 * @param clusteredData: vettore di interi
	 * @return media delle distanze
	 */
	public double avgDistance(Data data, int[] clusteredData) {
		double p = 0.0 ,sumD = 0.0;
		for(int i = 0; i < clusteredData.length; i++){
			double d = getDistance(data.getItemSet(clusteredData[i]));
			sumD += d;
		}
		p = sumD/clusteredData.length;
		return p;
	}
	
	/**
	 * Restituisce una striga contenente i valore della tupla
	 * @return String: riferimento ad oggetto String
	 */
	public String toString() {
		String str = "";
		for(int i = 0; i < getLength(); i++) {
			str += getItem(i).getValue() + " ";
		}
		return str;
	}
}
