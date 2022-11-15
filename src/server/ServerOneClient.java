package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ServerOneClient extends Thread{

	private Socket socket;
	private ObjectInputStream in; 
	private ObjectOutputStream out;
	private KMeansMiner kmeans;
	
	/**
	 * Inizializza il thread e la socket di comunicazione
	 * @param socket attraverso la quale avviene la comunicazione
	 * @throws IOException
	 */
	public ServerOneClient(Socket s) throws IOException {
		this.socket = s;
		out = new ObjectOutputStream(socket.getOutputStream());	
		in = new ObjectInputStream(socket.getInputStream()) ;		
		this.start();		
	}

	/**
	 * Metodo run che definisce la computazione del Thread 
	 */
	@Override
	public void run() {
		try {
			boolean notFound = false;
			int answerMenu = in.readInt();
			switch(answerMenu) {
			case 1:
				String fileName = in.readObject().toString();	
				try {
					this.kmeans = new KMeansMiner(fileName + ".dmp");
				}catch(FileNotFoundException e) {
					notFound = true;
				}
				if(notFound == false) {
					out.writeObject(kmeans.getCluster().toString());
					out.writeObject("OK");
				}else {
					out.writeObject("Error");
					out.writeObject("Error");
				}
				break;							
			case 2:
				int k = Integer.parseInt(in.readObject().toString());
				String tableName = in.readObject().toString();
				String file = in.readObject().toString();
				try{
					Data data = new Data(tableName);
					if(k > data.getNumberOfExamples()) {
						out.writeObject(new String("Error"));
						out.writeObject(new String("Error"));
					}else {
						this.kmeans = new KMeansMiner(k);
						kmeans.kMeans(data);
						kmeans.salva(file + ".dmp");
						out.writeObject(kmeans.getCluster().toString(data));
						out.writeObject("OK");
					}
				}catch(SQLException e) {
					System.err.println("Errore di sintassi!");
					out.writeObject(new String("Error SQL"));
					out.writeObject(new String("Error SQL"));
				}
				break;
			default:
				return;
			}		
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoValueException e) {
			e.printStackTrace();
		} catch (OutOfRangeSampleSize e) {
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				System.err.println("Socket non chiusa");
			}
		}

	}
}

