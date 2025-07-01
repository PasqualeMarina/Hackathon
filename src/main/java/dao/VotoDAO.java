package dao;

import model.Voto;
import model.Team;
import model.Giudice;

import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative ai voti.
 * Fornisce i metodi per salvare e recuperare i voti dei giudici per i team.
 */
public interface VotoDAO {
    /**
     * Salva un nuovo voto nel sistema.
     *
     * @param voto il voto da salvare
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean salvaVoto(Voto voto);

    /**
     * Recupera tutti i voti associati a un team specifico.
     *
     * @param team il team di cui recuperare i voti
     * @return la lista dei voti ricevuti dal team
     */
    List<Voto> getVotiByTeam(Team team);

    /**
     * Recupera il voto dato da un giudice specifico a un team specifico.
     *
     * @param giudice il giudice che ha espresso il voto
     * @param team    il team che ha ricevuto il voto
     * @return il voto trovato
     */
    Voto getVotoByGiudiceAndTeam(Giudice giudice, Team team);
}

