package server;

@SuppressWarnings("serial")
public class NoValueException extends Exception {
	
	public NoValueException(String str) {
		super(str);
	}
	
	public String toString() {
		return "Valore non presente nel result set!";
	}

}
