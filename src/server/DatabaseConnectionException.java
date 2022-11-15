package server;

@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {
	
	public DatabaseConnectionException(String str) {
		super(str);
	}
	
	public String toString() {
		return "Errore nella connessione con il DataBase!";
	}

}
