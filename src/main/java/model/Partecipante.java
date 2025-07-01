package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un partecipante agli eventi hackathon.
 * Contiene informazioni sulle iscrizioni agli eventi, gli inviti ricevuti
 * e i team di cui fa parte.
 */
public class Partecipante extends Utente {
    private ArrayList<Hackathon> iscrizioniEventi = new ArrayList<>();
    private ArrayList<Team> invitiRicevuti = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();

    /**
     * Costruisce un nuovo partecipante.
     *
     * @param ssn      il codice fiscale del partecipante
     * @param nome     il nome del partecipante
     * @param cognome  il cognome del partecipante
     * @param email    l'email del partecipante
     * @param password la password del partecipante
     * @param ruolo    il ruolo del partecipante
     */
    public Partecipante(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    /**
     * Effettua l'iscrizione del partecipante ad un evento hackathon.
     *
     * @param evento l'evento hackathon a cui iscriversi
     * @return true se l'iscrizione è avvenuta con successo, false altrimenti
     */
    public boolean effettuaIscrizione(Hackathon evento){
        if(evento.getNumMaxIscritti() > evento.getListaPartecipanti().size()){
            iscrizioniEventi.add(evento);
            return true;
        }
        else
            return false;
    }

    /**
     * Crea un nuovo team per un evento hackathon.
     *
     * @param nome   il nome del team
     * @param evento l'evento hackathon per cui creare il team
     * @return true se la creazione è avvenuta con successo, false altrimenti
     */
    public boolean creaTeam(String nome, Hackathon evento){
        Team team = new Team(nome, this, evento);
        if(getTeam(evento) == null){
            teams.add(team);
            evento.aggiungiTeam(team);
            return true;
        }
        else
            return false;
    }

    /**
     * Invia un invito ad un altro partecipante per unirsi ad un team.
     *
     * @param partecipante il partecipante da invitare
     * @param team         il team per cui inviare l'invito
     * @return true se l'invito è stato inviato con successo, false altrimenti
     */
    public boolean inviaInvito(Partecipante partecipante, Team team){
        if(partecipante.invitiRicevuti.contains(team))
            return false;
        else{
            partecipante.invitiRicevuti.add(team);
            return true;
        }
    }

    /**
     * Accetta un invito per unirsi ad un team.
     *
     * @param team il team di cui accettare l'invito
     * @return true se l'accettazione è avvenuta con successo, false altrimenti
     */
    public boolean accettaInvito(Team team) {
        if (team == null || team.getEvento() == null) {
            return false; // Invito non valido
        }

        Hackathon evento = team.getEvento();

        // Se il partecipante ha già un team per questo evento, rifiutiamo
        if (getTeam(evento) != null) {
            return false;
        }

        // Se non è iscritto all'evento, iscrivilo
        if (!iscrizioniEventi.contains(evento)) {
            effettuaIscrizione(evento);
        }

        // Aggiungilo al team
        team.aggiungiPartecipante(this);

        // Aggiungilo alla lista dei team personali, evitando duplicati
        if (!teams.contains(team)) {
            teams.add(team);
        }

        invitiRicevuti.remove(team);

        return true;
    }

    /**
     * Restituisce la lista degli eventi a cui il partecipante è iscritto.
     *
     * @return la lista degli eventi a cui il partecipante è iscritto
     */
    public List<Hackathon> getIscrizioniEventi(){
        return iscrizioniEventi;
    }

    /**
     * Restituisce la lista degli inviti ricevuti dal partecipante.
     *
     * @return la lista degli inviti ricevuti
     */
    public List<Team> getInvitiRicevuti(){
        return invitiRicevuti;
    }

    /**
     * Restituisce il team del partecipante per un dato evento.
     *
     * @param evento l'evento hackathon di cui cercare il team
     * @return il team del partecipante per l'evento specificato, null se non esiste
     */
    public Team getTeam(Hackathon evento){
        for(Team t : evento.getListaTeam())
            if(t.getComponenti().contains(this))
                return t;
        return null;
    }

    /**
     * Restituisce la lista dei team di cui il partecipante fa parte.
     *
     * @return la lista dei team del partecipante
     */
    public List<Team> getTeams(){
        return teams;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Partecipante other = (Partecipante) obj;
        return ssn != null && ssn.equals(other.ssn);
    }

    @Override
    public int hashCode() {
        return ssn != null ? ssn.hashCode() : 0;
    }
}
