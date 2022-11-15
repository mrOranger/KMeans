package server;

import java.util.ArrayList;
import java.util.List;



public class Example implements Comparable<Example>{

	/* Array di oggetti che rappresentano una sincola transazione */
	private List<Object> example = new ArrayList<Object>();
	
	/**
	 * Aggiunge o incoda ad examples un elemento di tipo Object
	 * @param obj
	 */
	public void add(Object obj) {
		this.example.add(obj);
	}
	
	/**
	 * Restituisce l' elemento alla posizione i-esima in example
	 * @param index: posizione da restituire
	 * @return riferimento all' oggetto Object
	 */
	public Object get(int index) {
		if(index >= 0 && index <= example.size()) {
			return example.get(index);
		}
		return null; 
	}
	
	/**
	 * Confronta due transazine e restituisce 0, 1 o -1 sulla base del confronto
	 * @param ex riferimento all' oggetto transazione
	 * @return risultato del confronto
	 */
	@Override
	public int compareTo(Example ex) {
		
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	/**
	 * Restituisce una stringa che rappresenta lo stato di example
	 * @return riferimento all' oggetto string
	 */
	public String toString() {
		String str = "";
		for(Object obj : example) {
			str += obj + " ";
		}
		return str;
	}
}