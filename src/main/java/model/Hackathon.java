package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un evento hackathon.
 * Contiene informazioni sulla sede, il titolo, la data, la durata,
 * l'organizzatore, i giudici, i team partecipanti e altri dettagli dell'evento.
 */
public class Hackathon{
    private String sede;
    private String titolo;
    private LocalDate dataInizio;
    private int durata = 2;
    private Organizzatore organizzatore;
    private ArrayList<Giudice> listaGiudici = new ArrayList<>();
    private ArrayList<Team> listaTeam = new ArrayList<>();
    private ArrayList<Partecipante> listaPartecipanti = new ArrayList<>();
    private int numMaxIscritti;
    private int numMaxPerTeam;
    private String descrizioneProblema;

    /**
     * Costruisce un nuovo evento hackathon.
     *
     * @param sede          la sede dell'evento
     * @param nome          il titolo dell'evento
     * @param data          la data di inizio
     * @param durata        la durata in giorni
     * @param organizzatore l'organizzatore dell'evento
     * @param maxIscritti   il numero massimo di iscritti
     * @param maxPerTeam    il numero massimo di membri per team
     */
    public Hackathon(String sede, String nome, LocalDate data, int durata, Organizzatore organizzatore, int maxIscritti, int maxPerTeam){
        this.sede = sede;
        this.titolo = nome;
        this.dataInizio = data;
        this.durata = durata;
        this.organizzatore = organizzatore;
        this.numMaxIscritti = maxIscritti;
        this.numMaxPerTeam = maxPerTeam;
        descrizioneProblema = "";
    }

    /**
     * Imposta il problema dell'hackathon.
     *
     * @param problema la descrizione del problema da risolvere
     */
    public void setProblema(String problema){
        this.descrizioneProblema = problema;
    }

    /**
     * Aggiunge un giudice all'evento.
     *
     * @param giudice il giudice da aggiungere
     */
    public void aggiungiGiudice(Giudice giudice){
        listaGiudici.add(giudice);
    }

    /**
     * Restituisce l'organizzatore dell'evento.
     *
     * @return l'organizzatore dell'evento
     */
    public Organizzatore getOrganizzatore(){
        return organizzatore;
    }

    @Override
    public String toString(){
        return "Titolo: " + titolo;
    }

    /**
     * Restituisce una stringa con tutte le informazioni dell'evento.
     *
     * @return stringa con i dettagli dell'evento
     */
    public String getInfo(){
        return "Titolo: " + titolo + "\nSede: " + sede + "\nData inizio: " + dataInizio + "\nDurata: " + durata + "\nNumero massimo iscritti: " + numMaxIscritti + "\nNumero massimo per team: " + numMaxPerTeam + "\nDescrizione problema: " + descrizioneProblema + "\nNumero Giudici: " + getListaGiudici().size();
    }

    /**
     * Restituisce il titolo dell'evento.
     *
     * @return il titolo dell'evento
     */
    public String getTitolo(){
        return titolo;
    }

    /**
     * Restituisce la lista dei giudici dell'evento.
     *
     * @return lista dei giudici
     */
    public List<Giudice> getListaGiudici(){
        return listaGiudici;
    }

    /**
     * Restituisce la lista dei team partecipanti.
     *
     * @return lista dei team
     */
    public List<Team> getListaTeam(){
        return listaTeam;
    }

    /**
     * Aggiunge un team all'evento.
     *
     * @param team il team da aggiungere
     */
    public void aggiungiTeam(Team team){
        listaTeam.add(team);
    }

    public int getNumMaxIscritti(){
        return numMaxIscritti;
    }

    public String getSede(){
        return sede;
    }

    public LocalDate getDataInizio(){
        return dataInizio;
    }

    public int getDurata(){
        return durata;
    }

    public int getNumMaxPerTeam(){
        return numMaxPerTeam;
    }

    public String getDescrizioneProblema(){
        return descrizioneProblema;
    }

    public void setDescrizioneProblema(String descrizioneProblema){
        this.descrizioneProblema = descrizioneProblema;

    }

    public void setListaGiudici(List<Giudice> listaGiudici){
        this.listaGiudici = new ArrayList<>(listaGiudici);
    }

    public void setListaTeam(List<Team> listaTeam){
        this.listaTeam = new ArrayList<>(listaTeam);
    }

    public void setListaPartecipanti(List<Partecipante> listaPartecipanti){
        this.listaPartecipanti = new ArrayList<>(listaPartecipanti);
    }

    public void setNumMaxIscritti(int numMaxIscritti){
        this.numMaxIscritti = numMaxIscritti;
    }

    public void setNumMaxPerTeam(int numMaxPerTeam){
        this.numMaxPerTeam = numMaxPerTeam;
    }

    public void setSede(String sede){
        this.sede = sede;
    }

    public void setOrganizzatore(Organizzatore organizzatore){
        this.organizzatore = organizzatore;
    }

    /**
     * Restituisce la lista dei partecipanti.
     *
     * @return lista dei partecipanti
     */
    public List<Partecipante> getListaPartecipanti() {
        return listaPartecipanti;
    }
}
