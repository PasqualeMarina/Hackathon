package dao;

import model.Team;
import model.Hackathon;
import model.Partecipante;
import model.Documento;
import model.Voto;

import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative ai team.
 * Fornisce i metodi per gestire i team, i loro componenti, documenti e voti.
 */
public interface TeamDAO {
    /**
     * Recupera un team dal suo nome all'interno di un evento Hackathon.
     *
     * @param nome   Il nome del team da cercare
     * @param evento L'evento Hackathon in cui cercare il team
     * @return Il team trovato
     */
    Team getTeamByNome(String nome, Hackathon evento);

    /**
     * Recupera tutti i team partecipanti a un evento Hackathon.
     *
     * @param evento L'evento Hackathon di cui recuperare i team
     * @return La lista dei team partecipanti all'evento
     */
    List<Team> getTeamsByHackathon(Hackathon evento);

    /**
     * Aggiunge un partecipante a un team.
     *
     * @param team         Il team a cui aggiungere il partecipante
     * @param partecipante Il partecipante da aggiungere al team
     */
    void aggiungiPartecipante(Team team, Partecipante partecipante);

    /**
     * Aggiunge un documento a un team.
     *
     * @param team      Il team a cui aggiungere il documento
     * @param documento Il documento da aggiungere al team
     */
    void aggiungiDocumento(Team team, Documento documento);

    /**
     * Recupera tutti i componenti di un team.
     *
     * @param team Il team di cui recuperare i componenti
     * @return La lista dei partecipanti del team
     */
    List<Partecipante> getComponenti(Team team);

    /**
     * Recupera tutti i documenti pubblicati da un team.
     *
     * @param team Il team di cui recuperare i documenti
     * @return La lista dei documenti pubblicati dal team
     */
    List<Documento> getDocumentiPubblicati(Team team);

    /**
     * Recupera tutti i voti ricevuti da un team.
     *
     * @param team Il team di cui recuperare i voti
     * @return La lista dei voti ricevuti dal team
     */
    List<Voto> getVoti(Team team);
}

