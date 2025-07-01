package controller;

import dao.*;
import implementazionidao.*;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per la gestione delle operazioni nelle interfacce di Partecipante, Organizzatore e Giudice
 * Crea e gestisce la connessione al db caricando i dati all'avvio
**/
public class Controller {
    // Parametri di connessione PostgreSQL
    private static final String URL      = "jdbc:postgresql://localhost:5432/Hackathon";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "password";

    private Connection connection;

    // DAO
    private UtenteDAO           utenteDAO;
    private HackathonDAO        hackathonDAO;
    private GiudiceDAO          giudiceDAO;
    private PartecipanteDAO     partecipanteDAO;
    private OrganizzatoreDAO    organizzatoreDAO;
    private TeamDAO             teamDAO;
    private DocumentoDAO        documentoDAO;
    private CommentoDAO         commentoDAO;
    private VotoDAO             votoDAO;

    // Cache in memoria
    private List<Utente>        utenti    = new ArrayList<>();
    private List<Hackathon>     eventi    = new ArrayList<>();

    // ===================== Costruttore =====================

    /**
     * Costruisce un oggetto controller, crea la connessione al db e carica i dati presenti in esso.
     */
    public Controller() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Inizializza i DAO con la Connection
            utenteDAO        = new UtenteImplementazionePostgresDao(connection);
            hackathonDAO     = new HackathonImplementazionePostgresDao(connection);
            giudiceDAO       = new GiudiceImplementazionePostgresDao(connection);
            partecipanteDAO  = new PartecipanteImplementazionePostgresDao(connection);
            organizzatoreDAO = new OrganizzatoreImplementazionePostgresDao(connection);
            teamDAO          = new TeamImplementazionePostgresDao(connection);
            documentoDAO     = new DocumentoImplementazionePostgresDao(connection);
            commentoDAO      = new CommentoImplementazionePostgresDao(connection);
            votoDAO          = new VotoImplementazionePostgresDao(connection);

            caricaDatiDaDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===================== Caricamento dati dal DB =====================

    /**
     * Metodo principale per il caricamento dei dati dal db.
     *
     * @throws SQLException
     */
    public void caricaDatiDaDB() throws SQLException {
        caricaUtenti();
        caricaEventiEHackathon();
        caricaInvitiGiudici();
    }

    /**
     * Carica tutti gli utenti presenti nel db nella cache.
     */
    private void caricaUtenti(){
        utenti.clear();
        utenti.addAll(utenteDAO.getTuttiUtenti());
    }

    /**
     * Carica tutti gli eventi presenti nel db.
     * Per ciascun evento chiama metodi per il caricamento di ulteriori informazioni
     *
     * @throws SQLException
     */
    private void caricaEventiEHackathon() throws SQLException {
        eventi.clear();
        eventi.addAll(hackathonDAO.getTuttiHackathon());

        for (Hackathon h : eventi) {
            caricaOrganizzatore(h);
            caricaGiudici(h);
            caricaPartecipanti(h);
            caricaTeam(h);
        }
    }

    /**
     * Carica l'organizzatore per l'evento passato come parametro.
     *
     * @param h evento per cui caricare l'organizzatore
     */
    private void caricaOrganizzatore(Hackathon h){
        Organizzatore org = organizzatoreDAO.getOrganizzatoreByHackathon(h.getTitolo());
        h.setOrganizzatore(org);
        if (org != null)
            org.creaEvento(h);
    }

    /**
     * Carica la lista di giudici per l'evento passato come parametro.
     *
     * @param h evento per cui caricare i giudici
     */
    private void caricaGiudici(Hackathon h){
        List<Giudice> giudici = giudiceDAO.getGiudiciByHackathon(h.getTitolo());
        for (Giudice g : giudici) {
            h.aggiungiGiudice(g);
            g.getEventiDaGiudice().add(h);
        }
    }

    /**
     * Carica la lista dei partecipanti per l'evento passato come parametro.
     *
     * @param h evento per cui caricare i partecipanti
     */
    private void caricaPartecipanti(Hackathon h){
        List<Partecipante> iscrittiDB = partecipanteDAO.getPartecipantiByHackathon(h.getTitolo());
        List<Partecipante> iscrittiConsolidati = new ArrayList<>();

        for (Partecipante pDB : iscrittiDB) {
            Partecipante pEsistente = null;
            for (Utente utente : utenti) {
                if (utente instanceof Partecipante partecipante && utente.getSsn().equals(pDB.getSsn())) {
                    pEsistente = partecipante;
                    break;
                }
            }

            if (pEsistente != null) {
                iscrittiConsolidati.add(pEsistente);
                pEsistente.effettuaIscrizione(h);
            } else {
                iscrittiConsolidati.add(pDB);
                pDB.effettuaIscrizione(h);
                utenti.add(pDB);
            }
        }

        h.setListaPartecipanti(iscrittiConsolidati);
    }

    /**
     * Carica la lista dei team per l'evento passato come parametro e
     * per ogni team carica i membri, i documenti e i voti.
     *
     * @param h evento per cui caricare i team
     * @throws SQLException
     */
    private void caricaTeam(Hackathon h) throws SQLException {
        List<Team> teams = teamDAO.getTeamsByHackathon(h);
        h.setListaTeam(teams);

        for (Team t : teams) {
            caricaMembriTeam(t);
            caricaDocumentiECommenti(t);
            caricaVoti(t);
        }
    }

    /**
     * Carica la lista dei partecipanti per il team passato come parametro.
     *
     * @param t team di cui caricare i membri
     */
    private void caricaMembriTeam(Team t){
        List<Partecipante> membriDB = partecipanteDAO.getPartecipantiByTeam(t.getNome());
        List<Partecipante> membriConsolidati = new ArrayList<>();

        for (Partecipante pDB : membriDB) {
            Partecipante pEsistente = null;
            for (Utente utente : utenti) {
                if (utente instanceof Partecipante partecipante && utente.getSsn().equals(pDB.getSsn())) {
                    pEsistente = partecipante;
                    break;
                }
            }

            if (pEsistente != null) {
                membriConsolidati.add(pEsistente);
                if (!pEsistente.getTeams().contains(t)) {
                    pEsistente.getTeams().add(t);
                }
            } else {
                membriConsolidati.add(pDB);
                pDB.getTeams().add(t);
                utenti.add(pDB);
            }
        }

        t.setComponenti(membriConsolidati);
    }

    /**
     * Carica la lista dei documenti per il team passato come parametro.
     *
     * @param t team di cui caricare i documenti
     */
    private void caricaDocumentiECommenti(Team t) throws SQLException {
        List<Documento> docs = documentoDAO.getDocumentiByTeam(t);
        for (Documento d : docs) {
            List<Commento> comm = commentoDAO.getCommentiByDocumento(d, this.giudiceDAO);
            d.setCommenti(comm);
        }
        t.setDocumentiPubblicati(docs);
    }

    /**
     * Carica la lista dei voti per il team passato come parametro.
     *
     * @param t team di cui caricare i voti
     */
    private void caricaVoti(Team t){
        List<Voto> voti = votoDAO.getVotiByTeam(t);
        t.setVoti(voti);
    }

    /**
     * Carica gli inviti inviati dagli organizzatori ai giudici.
     */
    private void caricaInvitiGiudici() {
        for (Utente utente : utenti) {
            if (utente instanceof Giudice giudice) {
                List<Hackathon> inviti = giudiceDAO.getInvitiRicevuti(giudice, eventi);
                giudice.setInvitiRicevuti(inviti);
            }
        }
    }

    // ===================== Autenticazione e gestione utenti =====================

    /**
     * Confronta email e password per ogni utente e restituisce
     * il riferimento all'utente se trova una corrispondenza.
     *
     * @param email l'email per l'accesso
     * @param password la password per l'accesso
     * @return l'utente corrispondente alle credenziali, oppure null se non trovato
     */
    public Utente verificaCredenziali(String email, String password) {
        for (Utente u : utenti) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null; // nessuna corrispondenza trovata
    }


    /**
     * Aggiunge un oggetto utente al db e alla memoria cache.
     *
     * @param utente utente da aggiungere alla lista
     * @return true se l'operazione va a buon fine, false altrimenti
     */
    public boolean aggiungiUtente(Utente utente) {
        if (utente == null || utente.getEmail() == null) return false;

        for (Utente u : utenti) {
            if (u.getEmail().equals(utente.getEmail())) {
                return false; // utente con questa email già presente
            }
        }

        boolean ok = utenteDAO.salvaUtente(utente);
        if (ok) {
            utenti.add(utente);
        }
        return ok;
    }

    /**
     * Restituisce la lista di tutti gli utenti caricati nella cache.
     *
     * @return una lista di oggetti di tipo Utente
     */
    public List<Utente> getUtenti() {
        return utenti;
    }

    // ===================== Gestione Hackathon =====================

    /**
     * Restituisce la lista di eventi di tipo Hackathon caricati nella cache.
     *
     * @return una lista contenente gli oggetti Hackathon attualmente caricati
     */
    public List<Hackathon> getEventi(){
        return eventi;
    }

    /**
     * Salva un evento di tipo Hackathon nel database e lo aggiunge alla cache degli eventi.
     *
     * @param h evento di tipo Hackathon che si desidera salvare
     * @return true se l'evento è stato aggiunto con successo alla cache degli eventi, false altrimenti
     */
    public boolean salvaHackathon(Hackathon h) {
        hackathonDAO.salvaHackathon(h);
        return eventi.add(h);
    }

    /**
     * Verifica se un evento con il titolo specificato esiste già nella lista di eventi.
     *
     * @param titolo il titolo dell'evento da verificare
     * @return true se non esiste alcun evento con il titolo specificato, false altrimenti
     */
    public boolean controllaNome(String titolo) {
        for (Hackathon e : eventi) {
            if (e.getTitolo().equals(titolo)) {
                return false; // trovato un evento con lo stesso titolo
            }
        }
        return true; // nessun evento con questo titolo
    }

    /**
     * Recupera informazioni di un evento.
     *
     * @param h riferimento all'Hackathon di cui si vogliono ottenere le informazioni
     * @return una stringa contenente le informazioni
     */
    public String getInfo(Hackathon h) {
        return h.getInfo();
    }

    // ===================== Organizzatore =====================

    /**
     * Aggiunge un evento di tipo Hackathon al sistema associandolo all'organizzatore specificato.
     * L'evento viene creato nel database tramite l'oggetto organizzatoreDAO e successivamente
     * aggiunto alla cache degli eventi.
     *
     * @param evento l'oggetto Hackathon rappresentante l'evento da aggiungere
     * @param org l'organizzatore associato all'evento
     */
    public void aggiungiEvento(Hackathon evento, Organizzatore org) {
        organizzatoreDAO.creaEvento(evento, org);
        eventi.add(evento);
    }

    // ===================== Giudice =====================
    /**
     * Invita un giudice a partecipare a un evento di tipo Hackathon, registrando l'invito
     * nel database e aggiornando la lista di inviti ricevuti dal giudice.
     *
     * @param g il giudice che deve essere invitato
     * @param h l'evento di tipo Hackathon al quale il giudice è invitato
     * @return true se l'invito è stato registrato correttamente, false altrimenti
     */
    public boolean inviaInvito(Giudice g, Hackathon h) {
        boolean ok = giudiceDAO.aggiungiInvito(g, h);
        if (ok) g.getInvitiRicevuti().add(h);
        return ok;
    }

    /**
     * Pubblica un problema associato a un evento di tipo Hackathon e aggiorna
     * la descrizione del problema per l'evento specificato.
     *
     * @param h l'oggetto Hackathon a cui è associato il problema da pubblicare
     * @param prob la descrizione del problema da pubblicare
     * @return true se il problema è stato pubblicato correttamente, false altrimenti
     */
    public boolean pubblicaProblema(Hackathon h, String prob) {
        boolean ok = giudiceDAO.pubblicaProblema(h, prob);
        if (ok) h.setDescrizioneProblema(prob);
        return ok;
    }

    /**
     * Restituisce la lista degli eventi di tipo Hackathon per i quali un determinato giudice ha ricevuto un invito.
     *
     * @param g il giudice di cui si vogliono ottenere gli inviti ricevuti
     * @return una lista di oggetti Hackathon corrispondenti agli inviti ricevuti dal giudice
     */
    public List<Hackathon> getInvitiGiudice(Giudice g) {
        return g.getInvitiRicevuti();
    }

    /**
     * Accetta l'invito ricevuto da un giudice a partecipare a un evento di tipo Hackathon.
     *
     * @param g il giudice che accetta l'invito
     * @param h l'evento di tipo Hackathon per cui l'invito viene accettato
     * @return true se l'operazione di accettazione dell'invito è andata a buon fine, false altrimenti
     */
    public boolean accettaInvito(Giudice g, Hackathon h) {
        boolean ok = giudiceDAO.accettaInvito(h, g.getSsn());
        if (ok) ok = g.accettaInvito(h);
        return ok;
    }

    /**
     * Rifiuta l'invito ricevuto da un giudice per un evento.
     *
     * @param g il giudice che rifiuta l'invito
     * @param h l'evento a cui il giudice era stato invitato
     */
    public void rifiutaInvito(Giudice g, Hackathon h) {
        giudiceDAO.rifiutaInvito(h, g.getSsn());
        g.getInvitiRicevuti().remove(h);
    }

    // ===================== Partecipante =====================

    /**
     * Iscrive un partecipante a un evento.
     *
     * @param p the participant to be registered
     * @param h the hackathon event to which the participant is registering
     * @return true if the registration is successfully completed, false otherwise
     */
    public boolean effettuaIscrizione(Partecipante p, Hackathon h) {
        boolean ok = partecipanteDAO.effettuaIscrizione(p, h);
        if (ok) p.getIscrizioniEventi().add(h);
        return ok;
    }

    /**
     * Restituisce una lista di hackathon a cui il partecipante indicato non è ancora iscritto
     * e che non hanno raggiunto il numero massimo di partecipanti consentito.
     *
     * @param p il partecipante per cui cercare gli hackathon disponibili
     * @return una lista di hackathon a cui il partecipante non è ancora iscritto e che non sono pieni
     */
    public List<Hackathon> getHackathonNonIscritti(Partecipante p) {
        List<Hackathon> non = new ArrayList<>();
        for (Hackathon h : eventi) {
            boolean iscr = p.getIscrizioniEventi().contains(h);
            boolean full = h.getListaPartecipanti().size() >= h.getNumMaxIscritti();
            if (!iscr && !full) non.add(h);
        }
        return non;
    }

    /**
     * Crea un team per un determinato hackathon con il nome e il partecipante specificati.
     *
     * @param nome il nome del team da creare
     * @param p    il partecipante che sta creando il team
     * @param h    l'hackathon per cui si sta creando il team
     * @return true se la creazione del team è avvenuta con successo, false altrimenti
     */
    public boolean creaTeam(String nome, Partecipante p, Hackathon h) {
        try {
            partecipanteDAO.creaTeam(nome, h, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p.creaTeam(nome, h);
    }

    /**
     * Invia un invito a un team al un partecipante con la mail passata come parametro.
     *
     * @param p Il partecipante che prova a invitare.
     * @param email L'email del partecipante che si vuole invitare.
     * @param team Il team a cui l'utente(se esiste) viene invitato.
     * @return vero se l'invito viene inviato correttamente, falso altrimenti.
     */
    public boolean inviaInvito(Partecipante p, String email, Team team) {
        for (Utente u : utenti) {
            if (u.getEmail().equals(email) && u instanceof Partecipante partecipante && !p.equals(u)) {
                partecipanteDAO.inviaInvito(partecipante, team);
                return p.inviaInvito(partecipante, team);
            }
        }
        return false;
    }

    
    /**
     * Gestisce l'accettazione di un invito ad unirsi a un team da parte di un partecipante.
     * Se il partecipante non è già iscritto all'hackathon associato al team, viene prima effettuata
     * l'iscrizione all'evento (se ci sono ancora posti disponibili). Successivamente, il partecipante
     * viene aggiunto come membro del team.
     *
     * @param p il partecipante che accetta l'invito
     * @param t il team a cui il partecipante vuole unirsi
     * @return true se l'accettazione dell'invito è avvenuta con successo, false in caso contrario
     * (ad esempio se l'hackathon è pieno o se ci sono problemi nell'aggiunta al team)
     */
    public boolean accettaInvito(Partecipante p, Team t) {
        Hackathon h = t.getEvento();
        if (!h.getListaPartecipanti().contains(p) && h.getListaPartecipanti().size() < h.getNumMaxIscritti()) {
            boolean ok = partecipanteDAO.effettuaIscrizione(p, h);
            if (ok) {
                h.getListaPartecipanti().add(p);
                p.effettuaIscrizione(h);
            }
        } else return false;
        boolean ok = partecipanteDAO.accettaInvito(p, t);
        if (ok) t.aggiungiPartecipante(p);
        return ok;
    }

    /**
     * Verifica se un partecipante fa parte di un team nell'hackathon specificato.
     *
     * @param partecipante il partecipante da verificare
     * @param evento       l'hackathon in cui cercare il partecipante
     * @return true se il partecipante è membro di un team, false altrimenti
     */
    public boolean parteDiTeam(Partecipante partecipante, Hackathon evento) {
        for (Team team : evento.getListaTeam()) {
            if (team.getComponenti().contains(partecipante)) return true;
        }
        return false;
    }

    /**
     * Restituisce la lista degli inviti a team ricevuti da un partecipante.
     *
     * @param p il partecipante di cui ottenere gli inviti
     * @return una lista di team a cui il partecipante è stato invitato
     */
    public List<Team> getInviti(Partecipante p) {
        return p.getInvitiRicevuti();
    }

    /**
     * Restituisce la lista degli hackathon a cui un partecipante è iscritto.
     *
     * @param p il partecipante di cui ottenere le iscrizioni
     * @return una lista di hackathon a cui il partecipante è iscritto
     */
    public List<Hackathon> getIscrizioni(Partecipante p) {
        return p.getIscrizioniEventi();
    }

    /**
     * Restituisce la lista dei team di cui fa parte un partecipante.
     *
     * @param p il partecipante di cui ottenere i team
     * @return una lista di team di cui il partecipante è membro
     */
    public List<Team> getTeamsPartecipante(Partecipante p) {
        return p.getTeams();
    }

    // ===================== Team =====================

    /**
     * Restituisce l'hackathon associato a un team.
     *
     * @param team il team di cui ottenere l'hackathon
     * @return l'hackathon in cui il team partecipa
     */
    public Hackathon getHackathon(Team team) {
        return team.getEvento();
    }

    /**
     * Restituisce la lista dei team partecipanti a un hackathon.
     *
     * @param h l'hackathon di cui ottenere i team
     * @return una lista dei team partecipanti all'hackathon
     */
    public List<Team> getTeams(Hackathon h) {
        return h.getListaTeam();
    }

    /**
     * Restituisce la lista dei partecipanti che compongono un team.
     *
     * @param t il team di cui ottenere i componenti
     * @return una lista dei partecipanti membri del team
     */
    public List<Partecipante> getComponentiTeam(Team t) {
        return t.getComponenti();
    }

    /**
     * Restituisce il nome di un team.
     *
     * @param t il team di cui ottenere il nome
     * @return il nome del team
     */
    public String getNomeTeam(Team t) {
        return t.getNome();
    }

    /**
     * Restituisce la lista dei documenti pubblicati da un team.
     *
     * @param t il team di cui ottenere i documenti
     * @return una lista dei documenti pubblicati dal team
     */
    public List<Documento> getDocumentiTeam(Team t) {
        return t.getDocumentiPubblicati();
    }

    /**
     * Aggiunge un nuovo documento alla lista dei documenti di un team.
     *
     * @param t il team a cui aggiungere il documento
     * @param d il documento da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiDocumento(Team t, Documento d) {
        boolean ok = documentoDAO.salvaDocumento(d);
        if(ok)
            t.aggiungiDocumento(d);
        return ok;
    }

    /**
     * Aggiunge un commento a un documento.
     *
     * @param c il commento da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiCommento(Commento c) {
        commentoDAO.salvaCommento(c);
        c.getDocumento().aggiungiCommento(c);
        return true;
    }

    // ===================== Voti =====================

    /**
     * Assegna un voto a un team da parte di un giudice.
     *
     * @param g         il giudice che assegna il voto
     * @param t         il team che riceve il voto
     * @param punteggio il punteggio assegnato
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean assegnaVoto(Giudice g, Team t, int punteggio) {
        Voto v = new Voto(g, t, punteggio);
        boolean saved = votoDAO.salvaVoto(v);
        if (!saved) return false;
        return t.aggiungiVoto(v);
    }

    /**
     * Restituisce la lista dei voti ricevuti da un team.
     *
     * @param team il team di cui ottenere i voti
     * @return una lista dei voti ricevuti dal team
     */
    public List<Voto> getVotiTeam(Team team) {
        return team.getVoti();
    }

    // ===================== Utility =====================

    /**
     * Chiude la connessione al database.
     * Questo metodo dovrebbe essere chiamato quando l'applicazione termina
     * per rilasciare le risorse del database.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}