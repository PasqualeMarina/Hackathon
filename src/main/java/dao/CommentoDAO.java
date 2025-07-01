package dao;

import model.Commento;
import model.Documento;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per i commenti.
 * Fornisce i metodi per salvare e recuperare i commenti associati ai documenti.
 */
public interface CommentoDAO {
    /**
     * Salva un nuovo commento nel sistema.
     *
     * @param commento il commento da salvare
     */
    boolean salvaCommento(Commento commento);

    /**
     * Recupera tutti i commenti associati a un documento specifico.
     *
     * @param documento il documento di cui recuperare i commenti
     * @param g         il DAO del giudice utilizzato per recuperare informazioni aggiuntive
     * @return la lista dei commenti associati al documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    List<Commento> getCommentiByDocumento(Documento documento, GiudiceDAO g) throws SQLException;
}

