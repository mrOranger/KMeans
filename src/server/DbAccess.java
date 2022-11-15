package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
	
	static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	private static final String DBMS = "jdbc:mysql";
	private static final String SERVER = "localhost";
	private static final String DATABASE = "MapDB";
	private static final int PORT = 3306;
	private static final String USER_ID = "MapUser";
	private static final String PASSWORD = "map";
	private static Connection conn;
	
	/**
	 * Impartisce al ClassLoader di caricare il driver mysql, 
	 * inizializza la connessione riferita da conn.
	 * @throws DatabaseConnectionException eccezione sollevata nel caso di fallimento
	 * @throws ClassNotFoundException eccezione sollevata nel caricamento del driver
	 * @throws SQLException eccezione sollevata nella connessione con la base di dati
	 */
	public static void initConnection() throws DatabaseConnectionException, ClassNotFoundException, SQLException{
		String str = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?useSSL=true";
		Class.forName(DRIVER_CLASS_NAME);
		conn = DriverManager.getConnection(str, USER_ID, PASSWORD);	
	}
	
	/**
	 * Restituisce un riferimento all' oggetto Connection
	 * @return riferimento a Connection
	 */
	public static Connection getConnection() {
		return conn;
	}
	
	/**
	 * Chiude una connessione con il DB
 	 * @throws SQLException eccezione sollevata nel tentativo di chiusura della connessione
	 */
	public static void closeConnection() throws SQLException {
		conn.close();
	}
	
	
	

}
