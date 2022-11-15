package server;


import java.io.Serializable;

@SuppressWarnings("serial")
public class ClusterSet implements Serializable{
	
	Cluster C[]; //Insieme di cluster
	int i = 0;  //Posizione valida dove memorizzare
	
	public ClusterSet(int index) throws OutOfRangeSampleSize {
		C = new Cluster[index];
	}
	
	/**
	 * Assegna c all' insieme di cluster
	 * @param c: valore da assegnare
	 */
	void add(Cluster c) {
		C[i] = c;
		i++;
	}
	
	/**
	 * Ritorna il valore in posizione index
	 * @param index: posizione dell' insieme
	 * @return cluster alla posizone index
	 */
	Cluster get(int index) {
		return C[index];
	}
	
	/**
	 * Sceglie i centroidi e crea un cluster per ogni centroide
	 * @param data: valori possibili per un centroide
	 */
	void initializeCentroids(Data data) throws OutOfRangeSampleSize {
		int centroidIndexes[] = data.sampling(C.length);
		for(int i = 0; i < centroidIndexes.length; i++){
			Tuple centroidI=data.getItemSet(centroidIndexes[i]);
			add(new Cluster(centroidI));
		}
	}
	
	/**
	 * Calcola la distanza tra tupla riferita da "tupla" ed il centroide di ciascun cluster C,
	 * restituisce il più vicino
	 * @param tuple: riferimento ad una tupla
	 * @return Cluster più vicino alla tupla
	 */
	Cluster nearestCluster(Tuple tuple) {
		double currentDistance = tuple.getDistance(C[0].getCentroid());
		int indexCluster = 0;
		for(int j = 1; j < i; j++) {
			if(tuple.getDistance(C[j].getCentroid()) < currentDistance) {
				currentDistance = tuple.getDistance(C[j].getCentroid());
				indexCluster = j;
			}
		}
		return get(indexCluster);
	}
	
	/**
	 * Restitusce il cluster a cui appartiene una tupla della matrice di Data, identificata da id
	 * @param id: riga della matrice Data
	 * @return Cluster di appartenenza
	 */
	Cluster currentCluster(int id) {
		int index = 0;
		boolean found = false;
		for(int j = 0; j < i; j++) {
			if(get(j).contain(id)) {
				found = true;
				index = j;
				break;
			}
		}
		if(found == true) {
			return get(index);
		}else {
			return null;
		}
	}
	
	/**
	 * Calcola il nuovo centroide per ciascun Cluster
	 * @param data: riferimento a Data;
	 */
	void updateCentroids(Data data) {
		for(int j = 0; j < C.length; j++) {
			get(j).computeCentroid(data);
		}
	}
	
	/**
	 * Restituisce una stringa contenente i centroidi di ciascun cluster 
	 * @return String: stringa
	 */
	public String toString() {
		String str = "";
		for(int j = 0; j < C.length; j++) {
			if(C[j] != null)
				str += C[j].getCentroid().toString() + " " + '\n';
		}
		return str;
	}
	
	/**
	 * Restituisce una stringa che descrive lo stato di ciascun cluster
	 * @param data: riferimento ad oggetto Data
	 * @return riferimento ad oggetto String
	 */
	public String toString(Data data) {
		String str = "";
		for(int i = 0; i < C.length; i++){
			if (C[i] != null){
				str += i + ":" + C[i].toString(data) + "\n";		
			}
		}
		return str;		

	}
}
