package dao;

import model.Commento;
import model.Documento;
import model.Team;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per i documenti.
 * Fornisce i metodi per salvare e recuperare i documenti e i relativi commenti.
 */
public interface DocumentoDAO {
    /**
     * Salva un nuovo documento nel sistema.
     *
     * @param documento il documento da salvare
     * @return true se va a buon fine, false altrimenti
     */
    boolean salvaDocumento(Documento documento);

    /**
     * Aggiunge un nuovo commento a un documento.
     *
     * @param commento il commento da aggiungere
     */
    void aggiungiCommento(Commento commento);

    /**
     * Recupera tutti i commenti associati a un documento specifico.
     *
     * @param documento il documento di cui recuperare i commenti
     * @return la lista dei commenti associati al documento
     */
    List<Commento> getCommentiByDocumento(Documento documento);

    /**
     * Recupera tutti i documenti associati a un team specifico.
     *
     * @param team il team di cui recuperare i documenti
     * @return la lista dei documenti associati al team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    List<Documento> getDocumentiByTeam(Team team) throws SQLException;
}