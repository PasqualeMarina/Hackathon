package dao;

import model.Giudice;
import model.Hackathon;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative ai giudici.
 */
public interface GiudiceDAO {
    /**
     * Pubblica un problema per un evento Hackathon.
     *
     * @param evento   L'evento Hackathon per cui pubblicare il problema
     * @param problema Il testo del problema da pubblicare
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean pubblicaProblema(Hackathon evento, String problema);

    /**
     * Accetta l'invito a un evento Hackathon per un giudice.
     *
     * @param evento     L'evento Hackathon a cui accettare l'invito
     * @param giudiceSSN Il codice fiscale del giudice
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean accettaInvito(Hackathon evento, String giudiceSSN);

    /**
     * Rifiuta l'invito a un evento Hackathon per un giudice.
     *
     * @param evento     L'evento Hackathon a cui rifiutare l'invito
     * @param giudiceSSN Il codice fiscale del giudice
     */
    void rifiutaInvito(Hackathon evento, String giudiceSSN);

    /**
     * Aggiunge un invito per un giudice a un evento Hackathon.
     *
     * @param giudice Il giudice da invitare
     * @param evento  L'evento Hackathon a cui invitare il giudice
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean aggiungiInvito(Giudice giudice, Hackathon evento);

    /**
     * Recupera la lista degli inviti ricevuti da un giudice.
     *
     * @param giudice        Il giudice di cui recuperare gli inviti
     * @param eventiCaricati La lista degli eventi disponibili
     * @return La lista degli eventi Hackathon a cui il giudice è stato invitato
     */
    List<Hackathon> getInvitiRicevuti(Giudice giudice, List<Hackathon> eventiCaricati);

    /**
     * Recupera la lista dei giudici associati a un evento Hackathon.
     *
     * @param titoloEvento Il titolo dell'evento Hackathon
     * @return La lista dei giudici associati all'evento
     */
    List<Giudice> getGiudiciByHackathon(String titoloEvento);

    /**
     * Recupera un giudice dal database tramite il suo codice fiscale.
     *
     * @param ssn Il codice fiscale del giudice
     * @return Il giudice trovato
     * @throws SQLException Se si verifica un errore nell'accesso al database
     */
    Giudice getGiudiceBySsn(String ssn) throws SQLException;
}

