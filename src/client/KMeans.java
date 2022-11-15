package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class KMeans extends JApplet {

	private static final long serialVersionUID = 1L;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static final String SERVER = "127.0.0.1";
	private static final int PORT = 8080;
	private static InetAddress addr = null;
	private static Socket socket;
	private TabbedPane tab;
	
	/**
	 * Inizializza il contenitore JApplet
	 */
	public void init() {
		tab = new TabbedPane();
		getContentPane().setLayout(new GridLayout(1,1));
		getContentPane().add(tab);	
	}



	private class TabbedPane extends JPanel {

		private static final long serialVersionUID = 1L;
		private JPanelCluster panelDB;  
		private JPanelCluster panelFile;

		/**
		 * Cotruttore che inizializza il JPanel della JApplet
		 */
		@SuppressWarnings("deprecation")
		TabbedPane() {
			super(new GridLayout(1,1));
			JTabbedPane jp = new JTabbedPane();
			panelDB = new JPanelCluster("Mining", new DBActionListener());			
			panelFile = new JPanelCluster("Load from file", new FileActionListener());
			ImageIcon iconDB = new ImageIcon("iconDB.png");
			ImageIcon iconFile = new ImageIcon("iconFile.png");
			jp.addTab("Database", iconDB, panelDB, "Database");
			jp.addTab("File", iconFile, panelFile, "File");
			this.add(jp);			
			panelFile.kText.hide();
			panelFile.tableText.hide();
			panelFile.kLabel.hide();
			panelFile.tableLabel.hide();
			panelDB.fileLabel.setText("Salva in");
			panelFile.fileLabel.setText("Carica da");
			jp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}


		/**
		 * Acquisisce il nome della tabella (da panelFile.tableText) e il numero dei cluster (da panelFile.kText). 
		 * Invia al server il comando 3, il nome della tabella e il numero dei cluster e aspetta la risposta del server. 
		 * In caso di risposta diversa da "OK", visualizza un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo, 
		 * altrimenti visualizza, in una JOptionPane, un messaggio che confermi il successo della attività.
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void learningFromFileAction() throws SocketException, IOException, ClassNotFoundException {
			try {
				addr = InetAddress.getByName(SERVER);
				System.out.println("addr = " + addr);
				socket = new Socket(addr, PORT);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());			
			}catch(UnknownHostException ex) {
				System.err.println("Errore nella connessione con il server");
			} catch (IOException e) {
				System.err.println("Errore instanziazione degli stream");
			}
			out.writeInt(1);
			String fileName = "";
			fileName = panelFile.fileText.getText();
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome file di salvataggio non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			out.writeObject(fileName);
			String StringCluster = in.readObject().toString();
			System.out.println("Messaggio: " + StringCluster);
			if(StringCluster.equals("Error")) {
				JOptionPane.showMessageDialog(this, "Nome file non valido!", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			panelFile.fileText.setText("");
			panelFile.clusterOutput.setText(StringCluster);
			String OKmsg = in.readObject().toString();
			if(OKmsg.equals("OK")) {
				JOptionPane.showMessageDialog(this, "File caricato con successo!", "Avviso", JOptionPane.INFORMATION_MESSAGE);
			}
			socket.close();
		}
		
		/**
		 * Acquisisce  il numero di cluster  (panelDB.kText.getText()). 
		 * Qualora il valore acquisito non fosse un numero positivo visualizza un messaggio di errore all'interno di una JOptionPane (vedi esempio).
		 * Acquisisce in nome della tabella (da panelDB.tableText). 
		 * Trasmette al server il comando 0 e il nome della tabella. 
		 * Resta in attesa della risposta del server. In caso di risposta diversa da "OK", visualizza un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo.
		 * Altrimenti, invia il comando 1 e il numero di cluster da scoprire e aspetta la risposta del server. 
		 * In caso di risposta diversa da "OK", visualizza un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo.
		 * Altrimenti legge il numero di iterazioni e i cluster così come sono trasmessi da server e li visualizza in panelDB.clusterOutput. 
		 * Invia al server il comando 2 e aspetta la risposta del server. 
		 * In caso di risposta diversa da "OK", visualizza un messaggio di errore in una JOptionPane e termina l'esecuzione del metodo, altrimenti visualizza un messaggio che confermi il successo della attività in una JOptionPane.
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
			try {
				addr = InetAddress.getByName(SERVER);
				System.out.println("addr = " + addr);
				socket = new Socket(addr, PORT);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());			
			}catch(UnknownHostException ex) {
				System.err.println("Errore nella connessione con il server");
			} catch (IOException e) {
				System.err.println("Errore instanziazione degli stream");
			}
			out.writeInt(2);
			int k = 0;	
			try{
				k = new Integer(panelDB.kText.getText()).intValue();
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "Inserire un valore numerico valido", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String tableName = "";
			String result = "";
			String fileName = "";
			tableName = panelDB.tableText.getText();
			if(tableName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome tabella non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			fileName = panelDB.fileText.getText();
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome file di salvataggio non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			out.writeObject(k);
			out.writeObject(tableName);
			out.writeObject(fileName);
			result = in.readObject().toString();
			if((!result.equals(new String("Error")))){
				panelDB.clusterOutput.setText(result);
				panelDB.fileText.setText("");
				panelDB.kText.setText("");
				panelDB.tableText.setText("");
			}
			if(!result.equals(new String("Error SQL"))) {
				panelDB.clusterOutput.setText(result);
				panelDB.fileText.setText("");
				panelDB.kText.setText("");
				panelDB.tableText.setText("");
			}
			String OKmsg = in.readObject().toString();
			if(OKmsg.equals("OK")) {
				JOptionPane.showMessageDialog(this, "Clusterizzazzione e salvataggio terminati con successo!", "Avviso", JOptionPane.INFORMATION_MESSAGE);
			}else if(OKmsg.equals("Error SQL")) {
				JOptionPane.showMessageDialog(this, "Nome tabella non valido!", "Avviso", JOptionPane.ERROR_MESSAGE);
			}else if(OKmsg.equals("Error")) {
				JOptionPane.showMessageDialog(this, "Il valore inserito corrisponde ad un numero di centroidi non ammissibile", "Avviso", JOptionPane.ERROR_MESSAGE);
			}
			socket.close();
		}



		private class JPanelCluster extends JPanel {

			private static final long serialVersionUID = 1L;
			private JTextField tableText = new JTextField(20);
			private JTextField kText = new JTextField(10);
			private JTextField fileText = new JTextField(20);
			private JTextArea clusterOutput = new JTextArea();
			private JButton executeButton;

			private JPanel upPanel;
			private JPanel centralPanel;
			private JPanel downPanel;

			private JLabel tableLabel;
			private JLabel kLabel;
			private JLabel fileLabel;
			private JLabel OutputLabel;

			/**
			 * Costruttore del panel relativo al recupero dal Database o dal file
			 * @param buttonName nome bottone
			 * @param a Listener del bottone
			 */
			JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
				this.upPanel = new JPanel(new FlowLayout());
				this.centralPanel = new JPanel(new BorderLayout(1,1));
				this.downPanel = new JPanel(new FlowLayout());
				this.kLabel = new JLabel("N. cluster");
				this.tableLabel = new JLabel("Nome tabella");
				this.fileLabel = new JLabel();
				this.OutputLabel = new JLabel("Risultato");

				upPanel.add(tableLabel);
				upPanel.add(tableText);
				upPanel.add(kLabel);
				upPanel.add(kText);
				upPanel.add(fileLabel);
				upPanel.add(fileText);
				add(upPanel);

				clusterOutput.setEditable(false);
				JScrollPane scrollingArea = new JScrollPane(clusterOutput);
				centralPanel.add(OutputLabel, BorderLayout.NORTH);
				centralPanel.add(scrollingArea, BorderLayout.CENTER);
				add(centralPanel);
				this.executeButton = new JButton(buttonName);
				this.executeButton.addActionListener(a);
				downPanel.add(executeButton);
				add(downPanel);
			}


		}
		
		/**
		 * Classe privata che implementa il listener del bottone per il recupero da Database
		 * @author Windows
		 */
		private class DBActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					learningFromDBAction();
				} catch (SocketException e1) {
					System.err.println("Errore SocketException");
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					System.err.println("Errore ClassNotFoundException");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}
		
		/**
		 * Classe privata che implementa il listener per il bottone di recupero da file
		 * @author Windows
		 */
		private class FileActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					learningFromFileAction();
				} catch (SocketException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					System.err.println("Errore ClassNotFoundException");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}
