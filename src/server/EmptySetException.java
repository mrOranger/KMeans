package server;

@SuppressWarnings("serial")
public class EmptySetException extends Exception{
	
	public EmptySetException(String str) {
		super(str);
	}
	
	public String toString() {
		return "Result set vuoto!";
	}

}
