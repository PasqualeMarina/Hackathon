package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un organizzatore di eventi hackathon.
 * Contiene informazioni sugli eventi organizzati e metodi per la gestione degli stessi.
 */
public class Organizzatore extends Utente {
    private List<Hackathon> eventiOrganizzati = new ArrayList<>();

    /**
     * Costruisce un nuovo organizzatore.
     *
     * @param ssn      il codice fiscale dell'organizzatore
     * @param nome     il nome dell'organizzatore
     * @param cognome  il cognome dell'organizzatore
     * @param email    l'email dell'organizzatore
     * @param password la password dell'organizzatore
     * @param ruolo    il ruolo dell'organizzatore
     */
    public Organizzatore(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    /**
     * Crea un nuovo evento hackathon con i parametri specificati.
     *
     * @param sede          la sede dell'evento
     * @param nome          il nome dell'evento
     * @param data          la data di inizio dell'evento
     * @param durata        la durata dell'evento in giorni
     * @param organizzatore l'organizzatore dell'evento
     * @param maxIscritti   il numero massimo di iscritti
     * @param maxPerTeam    il numero massimo di membri per team
     */
    public void creaEvento(String sede, String nome, LocalDate data, int durata, Organizzatore organizzatore, int maxIscritti, int maxPerTeam) {
        Hackathon evento = new Hackathon(sede, nome, data, durata, organizzatore, maxIscritti, maxPerTeam);
        eventiOrganizzati.add(evento);
    }

    /**
     * Aggiunge un evento hackathon esistente alla lista degli eventi organizzati.
     *
     * @param evento l'evento hackathon da aggiungere
     */
    public void creaEvento(Hackathon evento){
        eventiOrganizzati.add(evento);
    }

    /**
     * Invia un invito ad un giudice per partecipare ad un evento hackathon.
     *
     * @param giudice il giudice da invitare
     * @param evento  l'evento hackathon per cui inviare l'invito
     */
    public void invitaGiudice(Giudice giudice, Hackathon evento){
        if(eventiOrganizzati.contains(evento))
            giudice.aggiungiInvito(evento);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organizzatore that = (Organizzatore) o;

        return ssn != null ? ssn.equals(that.ssn) : that.ssn == null;
    }

    @Override
    public int hashCode() {
        return ssn != null ? ssn.hashCode() : 0;
    }


}
