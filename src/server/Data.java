package server;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Data {
	
	private List<Example> data;
	private int numberOfExamples;
	private List<Attribute> explanatorySet;
	
	public Data(String tableName) throws SQLException, NumberFormatException, NoValueException {
		
		try {
			DbAccess db = new DbAccess();
			TableData tableData = new TableData(db);
			db.initConnection();
			data = tableData.getDistinctTransazioni(tableName);
			TableSchema schema = new TableSchema(db, tableName);
			numberOfExamples = data.size();
			explanatorySet = new LinkedList<Attribute>();
			
			List<Object[]> valori = new LinkedList<Object[]>();
			
			for(int i = 0; i < schema.getNumberOfAttributes(); i++) {
				if(!schema.getColumn(i).isNumber()) {
					Object[] currentValue = tableData.getDistinctColumnValues(tableName, schema.getColumn(i)).toArray();
					String[] discreteValue = new String[currentValue.length];
					for(int j = 0; j < currentValue.length; j++) {
						discreteValue[j] = currentValue[j].toString();
					}
					valori.add(tableData.getDistinctColumnValues(tableName, schema.getColumn(i)).toArray());
					explanatorySet.add(new DiscreteAttribute(schema.getColumn(i).getColumnName(), i+1, discreteValue));
				}else {
					/* Se i valori della colonna sono dei numeri, bisogna istanziare un attributo continuo */
					double min = Double.parseDouble(tableData.getAggregateColumnValue(tableName, schema.getColumn(i), QUERY_TYPE.MIN).toString());
					double max = Double.parseDouble(tableData.getAggregateColumnValue(tableName, schema.getColumn(i), QUERY_TYPE.MAX).toString());
					explanatorySet.add(new ContinuousAttribute(schema.getColumn(i).getColumnName(), i+1, min, max));
				}
			}
		} catch (DatabaseConnectionException e) {
			System.err.println("Errore nella creazione della connessione!");
			e.printStackTrace();
		} catch (EmptySetException e) {
			System.err.println("Nessuna transazione presente nel DB!");
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			System.err.println("Errore nel caricamento del Driver!");
		}
	}
	
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	
	public int getNumberOfExplanatoryAttribute() {
		return (explanatorySet.size());
	}
	
	public List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}
	
	public Object getAttributeValue(int riga, int colonna) {
		if(riga >= 0 && riga < 14) {
			if(colonna >= 0 && colonna < 5) {
				return data.get(riga).get(colonna);
			}else {
				System.err.println("Indice della colonna non presente!");
			}
		}else {
			System.err.println("Indice della riga non presente");
		}
		return data.get(riga).get(colonna);
	}

	/**
	 * Crea e restituisce un elemento di tipo Tuple che modella come sequenza di coppie Attributo - Valore,
	 * la i-esima riga di Data.
	 * @param index: i-esima riga della matrice
	 * @return oggetto Tupla costru
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(explanatorySet.size());
		for(int i = 0; i < explanatorySet.size(); i++)
			if(explanatorySet.get(i) instanceof DiscreteAttribute) {
				tuple.add(new DiscreteItem(explanatorySet.get(i), data.get(index).get(i).toString()),i);
			}else {
				tuple.add(new ContinuousItem(explanatorySet.get(i), (Double.parseDouble(data.get(index).get(i).toString()))),i);
			}
		return tuple;
	}
	
	
	/**
	 * Crea e restiuisce un array di k elementi, rappresentanti gli indici di riga indicanti come centroidi in Data
	 * @param k: numero di cluster da generare;
	 * @return: vettore di interi
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize{
		if(k <= 0 || k > data.size()) {
			throw new OutOfRangeSampleSize("E' stata sollevata un' eccezione!");
		}
		int centroidIndexes[] = new int[k];
		//choose k random different centroids in data.
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i = 0; i < k;i++){
			boolean found = false;
			int c;
			do{
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				// verify that centroid[c] is not equal to a centroide 				already stored in CentroidIndexes
				for(int j = 0; j < i;j++)
					if(compare(centroidIndexes[j],c)){
						found = true;
						break;
					}
			}while(found);		
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}
	
	/**
	 * Restituisce vero se le due righe hanno gli stessi valori, falso altrimenti
	 * @param i: numero di riga
	 * @param j: numero di riga
	 * @return valore di verità;
	 */
	private boolean compare(int i, int j) {
		boolean equals = true;
		for(int k = 0; k < getNumberOfExplanatoryAttribute(); k++ ) {
			if(!getAttributeValue(i,k).equals(getAttributeValue(j,k))) {
				equals = false;
				break;
			}
		}
		return equals;
	}
	
	/**
	 * Restituise computePrototype(idList, (DiscreteAttribute)attribute);
	 * Utilizza l' RTTI per distinguere le istanze dell' attributo
	 * @param idList: insieme degli indici di righe di Data;
	 * @param attribute: attributo rispetto al quale calcolare il prototipo
	 * @return valore centroide rispetto ad attribute
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if(attribute instanceof DiscreteAttribute) {
			return computePrototype(idList, (DiscreteAttribute)attribute);
		}else {
			return computePrototype(idList, (ContinuousAttribute)attribute);
		}
	}
	
	/**
	 * Determina il valore che occore più frequentemente  nel cluster individuato da idList
	 * @param idList: insieme delle righe che compongono il cluster;
	 * @param attribute: attributo per il quale calcolare il più frequente
	 * @return valore più frequente
	 */
	public String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		/* Passo base */
		Iterator<String> iter = attribute.iterator();
		String first = iter.next();
		int currentFrequency = attribute.frequency(this, idList, first);
		String nomeValore = first;
		/* Itero sul dominio dell' attributo */
		while(iter.hasNext()) {
			/* Aggiornamento */
			String currElem = iter.next();
			if(attribute.frequency(this, idList, currElem) > currentFrequency) {
				currentFrequency = attribute.frequency(this, idList, currElem);
				nomeValore = currElem;
			}
		}
		return nomeValore;
	}
	
	/**
	 * Determina il valore prototipo come media dei valori osservati per attribute nelle transizioni di Data
	 * aventi indici di riga per idList
	 * @param idList: lista degli indici di riga 
	 * @param attribute: valore per il quale calcolare 
	 * @return
	 */
	public Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		Iterator<Integer> iter = idList.iterator();
		double prototype = 0F;
		while(iter.hasNext()) {
			int currIndex = Integer.parseInt(iter.next().toString()); /* Valore corrente di riga */
			double currentValue = Double.parseDouble(data.get(currIndex).get(attribute.getIndex()-1).toString());
			prototype += currentValue;
		}
		return new Double(prototype/idList.size());
	}
	
	/** 
	 * Funzione che restituisce una stringa con tutte la transazione in data
	 * @return riferimento all' oggetto di tipo String
	 */
	public String toString() {
		String str = "";
		for(int i = 0; i < getNumberOfExplanatoryAttribute(); i++) {
			str += getAttributeSchema().get(i);
			if(i < getNumberOfExplanatoryAttribute() - 1) {
				str += ", ";
			}
		}
		str += "\n";
		for(int i = 0; i < getNumberOfExamples(); i++) {
			str = str + (i+1) + ": ";
			for(int j = 0; j < getNumberOfExplanatoryAttribute(); j++) {
				str += getAttributeValue(i, j);
				if(j < getNumberOfExplanatoryAttribute() - 1) {
					str += ", ";
				}
			}
			str += "\n";
		}
		return str;
	}
}
