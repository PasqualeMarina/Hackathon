package dao;

import model.Hackathon;
import model.Partecipante;
import model.Team;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati relative ai partecipanti.
 */
public interface PartecipanteDAO {
    /**
     * Effettua l'iscrizione di un partecipante a un evento Hackathon.
     *
     * @param partecipante Il partecipante da iscrivere
     * @param evento       L'evento Hackathon a cui iscriversi
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean effettuaIscrizione(Partecipante partecipante, Hackathon evento);

    /**
     * Crea un nuovo team per un evento Hackathon.
     *
     * @param nome         Il nome del team da creare
     * @param evento       L'evento Hackathon per cui creare il team
     * @param partecipante Il partecipante che crea il team
     * @throws SQLException Se si verifica un errore nell'accesso al database
     */
    void creaTeam(String nome, Hackathon evento, Partecipante partecipante) throws SQLException;

    /**
     * Invia un invito a un partecipante per unirsi a un team.
     *
     * @param destinatario Il partecipante a cui inviare l'invito
     * @param team         Il team che invia l'invito
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean inviaInvito(Partecipante destinatario, Team team);

    /**
     * Accetta l'invito a unirsi a un team.
     *
     * @param partecipante Il partecipante che accetta l'invito
     * @param team         Il team a cui unirsi
     * @return true se l'operazione ha successo, false altrimenti
     */
    boolean accettaInvito(Partecipante partecipante, Team team);

    /**
     * Recupera la lista degli eventi Hackathon a cui un partecipante è iscritto.
     *
     * @param partecipante Il partecipante di cui recuperare le iscrizioni
     * @return La lista degli eventi Hackathon a cui il partecipante è iscritto
     */
    List<Hackathon> getIscrizioni(Partecipante partecipante);

    /**
     * Recupera la lista degli inviti ricevuti da un partecipante.
     *
     * @param partecipante Il partecipante di cui recuperare gli inviti
     * @return La lista dei team che hanno invitato il partecipante
     */
    List<Team> getInviti(Partecipante partecipante);

    /**
     * Recupera il team di un partecipante per un specifico evento Hackathon.
     *
     * @param partecipante Il partecipante di cui recuperare il team
     * @param evento       L'evento Hackathon di riferimento
     * @return Il team del partecipante per l'evento specificato
     */
    Team getTeam(Partecipante partecipante, Hackathon evento);

    /**
     * Recupera tutti i team di cui fa parte un partecipante.
     *
     * @param partecipante Il partecipante di cui recuperare i team
     * @return La lista dei team del partecipante
     */
    List<Team> getTeams(Partecipante partecipante);

    /**
     * Recupera tutti i partecipanti iscritti a un evento Hackathon.
     *
     * @param titoloEvento Il titolo dell'evento Hackathon
     * @return La lista dei partecipanti iscritti all'evento
     */
    List<Partecipante> getPartecipantiByHackathon(String titoloEvento);

    /**
     * Recupera tutti i partecipanti di un team specifico.
     *
     * @param nomeTeam Il nome del team
     * @return La lista dei partecipanti del team
     */
    List<Partecipante> getPartecipantiByTeam(String nomeTeam);
}

