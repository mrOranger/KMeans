package server;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable{
	
	private Set<String> value;
	
	public DiscreteAttribute(String name, int index, String value[]) {
		super(name, index);
		this.value = new TreeSet<String>();
		for(int i = 0; i < value.length; i++) {
			this.value.add(value[i]);
		}
	}
	
	/**
	 * Restituisce la lunghezza dell' insieme di valori dell' attributo discreto
	 * @return lunghezza del set di valori
	 */
	public int getNumberOfDistinctsValue() {
		return (value.size());
	}
	
	/**
	 * Determina il numero di volte che il valore "value" compare in corrispondenza dell' attributo corrente (indice di colonna)
	 * negli esempi memorizzati in Data ed indicizzate (per riga) da idList 
	 * @param data: oggetto Data
	 * @param idList: riferimento all' oggetto ArraySet
	 * @param value: valore discreto
	 * @return frequenza valore discreto
	 */
	public int frequency(Data data, Set<Integer> idList, String value) {
		int frequenza = 0; //Frequenza dell' attributo 
		/* Iterazione sul numero di righe di data */
		Iterator<Integer> iter = idList.iterator();
		while(iter.hasNext()) {
			int index = Integer.parseInt(iter.next().toString());
			for(int k = 0; k < data.getNumberOfExplanatoryAttribute(); k++) {
				if(data.getAttributeValue(index, k).equals(value)) {
					frequenza++;
				}
			}
		}
		return frequenza;
	}

	@Override
	/** Restituisce il riferimento all' iteratore sul set di valore dell' attributo
	 * @return riferimento all' oggetto iteratore dell' elemento set
	 */
	public Iterator<String> iterator() {
		return value.iterator();
	}

}
