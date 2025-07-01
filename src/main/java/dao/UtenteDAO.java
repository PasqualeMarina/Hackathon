package dao;

import model.Utente;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative agli utenti.
 */
public interface UtenteDAO {
    /**
     * Salva un nuovo utente nel sistema.
     *
     * @param utente L'utente da salvare
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean salvaUtente(Utente utente);

    /**
     * Recupera tutti gli utenti presenti nel sistema.
     *
     * @return La lista di tutti gli utenti
     */
    List<Utente> getTuttiUtenti();

    /**
     * Recupera un utente tramite il suo indirizzo email.
     *
     * @param email L'indirizzo email dell'utente da cercare
     * @return L'utente trovato
     */
    Utente getUtenteByEmail(String email);
}

