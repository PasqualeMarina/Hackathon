package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton che gestisce la connessione al database PostgreSQL.
 * Fornisce un punto di accesso centralizzato per la connessione al database
 * utilizzando il pattern Singleton per garantire una singola istanza.
 */
public class ConnessioneDatabase {

    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    private Connection connection = null;
    private String nome = "postgres";
    private String password = "password";
    private String url = "jdbc:postgresql://localhost:5432/Hackathon";
    private String driver = "org.postgresql.Driver";

    // COSTRUTTORE
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Restituisce l'istanza unica della classe ConnessioneDatabase.
     * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova.
     *
     * @return l'istanza unica di ConnessioneDatabase
     * @throws SQLException se si verifica un errore durante la connessione al database
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed())
            instance = new ConnessioneDatabase();

        return instance;
    }
}