package client;

@SuppressWarnings("serial")
public class ServerException extends Exception {
	
	public ServerException() {
		super("E' stata sollevata un' eccezione!");
	}
	
	public String toString() {
		return "Errore nella comunicazione con il server!";
	}
	
}
