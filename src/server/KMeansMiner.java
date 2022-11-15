package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class KMeansMiner {
	
	ClusterSet C;
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectInput;
	
	/**
	 * Costruttore che permette di serializzare un oggetto su un file 'fileName'
	 * @param fileName: nome del file su cui serializzare l' oggetto
	 * @throws FileNotFoundException eccezzione sollevata nel caso in cui il file non esista
	 * @throws IOException eccezzione sollevata nel caso in cui ci siano problemi di I/O
	 * @throws ClassNotFoundException eccezzione sollevata in cui non si trovi la classe da serializzare
	 */
	public KMeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException{
		objectInput = new ObjectInputStream(new FileInputStream(fileName));
		C = (ClusterSet)objectInput.readObject();
		objectInput.close(); /* Chiusura del file! */
	}
	
	public KMeansMiner(int k) throws OutOfRangeSampleSize {
		C = new ClusterSet(k);
	}
	
	/**
	 * Ritorna l' oggetto ClusterSet
	 * @return riferimento ad oggetto ClusterSet
	 */
	public ClusterSet getCluster() {
		return C;
	}
	
	public void salva(String fileName) throws FileNotFoundException, IOException{
		objectOut = new ObjectOutputStream(new FileOutputStream(fileName));
		objectOut.writeObject(C);
		objectOut.close(); /* Chiudo il collegamento */
	}
	
	/**
	 * Esegue l' algoritmo k-means, eseguendo i passi:
	 * 1. Scelta casuale dei centroidi per l' algoritmo;
	 * 2. Assegnazione a ciascuna riga della matrice di Data al centroide più vicino
	 * 3. Calcolo dei nuovi centroidi
	 * 4. Ripetizione dei passi 2 e 3 finchè non restiuiscono centroidi uguali
	 * @param data: riferimento all' oggetto Data
	 * @return numero di iterazioni da eseguire
	 * @throws OutOfRangeSampleSize 
	 */
	public int kMeans(Data data) throws OutOfRangeSampleSize {
		int numberOfIterations = 0;
		//STEP 1
		C.initializeCentroids(data);
		boolean changedCluster = false;
		do{
			numberOfIterations++;
			//STEP 2
			changedCluster = false;
			for(int i = 0; i < data.getNumberOfExamples(); i++){
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster = C.currentCluster(i);
				boolean currentChange = nearestCluster.addData(i);
				if(currentChange)
					changedCluster = true;
				//rimuovo la tupla dal vecchio cluster
				if(currentChange && oldCluster != null)
					//il nodo va rimosso dal suo vecchio cluster
					oldCluster.removeTuple(i);
			}			
			//STEP 3
			C.updateCentroids(data);
		}
		while(changedCluster);
		return numberOfIterations+1;
	}
}
