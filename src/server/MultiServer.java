package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	
	private int PORT = 8080;
	private ServerSocket server;
	private Socket socket;
	
	public MultiServer(int PORT) throws IOException {
		this.PORT = PORT;
		run();
	}
	
	/**
	 * Metodo che istanzia una comunicazione per un client e definisce ogni volta che c'è una richiesta, un oggetto client.
	 */
	private void run(){
		try {
			server = new ServerSocket(PORT);
			System.out.println("In attesa di una connessione...");
			while(true) {
				socket = server.accept();
				System.out.println("Connessione con un client riuscita!");
				new ServerOneClient(socket);
			}
		} catch (IOException e) {
			System.err.println("Errore nell' istanziazione della server socket!");
			try {
				socket.close();
				return;
			} catch (IOException e1) {
				System.err.println("Errore nella chiusura della connessione!");
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			try {
				socket.close();
				return;
			} catch (IOException e) {
				System.err.println("Errore nella chiusura della connessione!");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new MultiServer(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
