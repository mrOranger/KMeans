package server;

/**
 * Classe per la gestione di un' eccezzione personalizzata
 * @author Windows
 */
@SuppressWarnings("serial")
public class OutOfRangeSampleSize extends Exception {
	
	public OutOfRangeSampleSize() {
		super(Exception.class.getName());
	}

	public OutOfRangeSampleSize(String message){
		super(message);
	}
	
	public static void printError() {
		System.err.println("Range non valido per la clusterizzazione!");
	}
}
