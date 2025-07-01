package dao;

import model.Hackathon;
import model.Giudice;
import model.Team;
import model.Partecipante;

import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative agli eventi Hackathon.
 */
public interface HackathonDAO {
    /**
     * Salva un nuovo evento Hackathon nel sistema.
     *
     * @param hackathon L'evento Hackathon da salvare
     */
    void salvaHackathon(Hackathon hackathon);

    /**
     * Recupera un evento Hackathon dal suo titolo.
     *
     * @param titolo Il titolo dell'evento da cercare
     * @return L'evento Hackathon trovato
     */
    Hackathon getHackathonByTitolo(String titolo);

    /**
     * Recupera tutti gli eventi Hackathon presenti nel sistema.
     *
     * @return La lista di tutti gli eventi Hackathon
     */
    List<Hackathon> getTuttiHackathon();

    /**
     * Aggiorna il problema associato a un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon da aggiornare
     * @param problema  Il nuovo testo del problema
     */
    void aggiornaProblema(Hackathon hackathon, String problema);

    /**
     * Aggiunge un giudice a un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon a cui aggiungere il giudice
     * @param giudice   Il giudice da aggiungere
     */
    void aggiungiGiudice(Hackathon hackathon, Giudice giudice);

    /**
     * Aggiunge un team a un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon a cui aggiungere il team
     * @param team      Il team da aggiungere
     */
    void aggiungiTeam(Hackathon hackathon, Team team);

    /**
     * Recupera tutti i giudici associati a un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon di cui recuperare i giudici
     * @return La lista dei giudici associati all'evento
     */
    List<Giudice> getGiudici(Hackathon hackathon);

    /**
     * Recupera tutti i team partecipanti a un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon di cui recuperare i team
     * @return La lista dei team partecipanti all'evento
     */
    List<Team> getTeams(Hackathon hackathon);

    /**
     * Recupera tutti i partecipanti di un evento Hackathon.
     *
     * @param hackathon L'evento Hackathon di cui recuperare i partecipanti
     * @return La lista dei partecipanti all'evento
     */
    List<Partecipante> getPartecipanti(Hackathon hackathon);
}

