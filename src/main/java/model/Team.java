package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un team partecipante ad un hackathon.
 * Contiene informazioni sul nome del team, i suoi componenti, i documenti pubblicati
 * e i voti ricevuti.
 */
public class Team {
    private String nome;
    private Hackathon evento;
    private List<Partecipante> componenti = new ArrayList<>();
    private List<Documento> documentiPubblicati = new ArrayList<>();
    private List<Voto> voti = new ArrayList<>();

    /**
     * Costruisce un nuovo team.
     *
     * @param nome         il nome del team
     * @param partecipante il primo partecipante del team
     * @param evento       l'evento hackathon a cui partecipa il team
     */
    public Team(String nome, Partecipante partecipante, Hackathon evento){
        this.nome = nome;
        componenti.add(partecipante);
        this.evento = evento;
    }

    @Override
    public String toString(){
        return nome;
    }

    /**
     * Restituisce il nome del team.
     *
     * @return il nome del team
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce la lista dei documenti pubblicati dal team.
     *
     * @return la lista dei documenti pubblicati
     */
    public List<Documento> getDocumentiPubblicati() {
        return documentiPubblicati;
    }

    /**
     * Aggiunge un voto al team solo se il giudice non ha già votato.
     *
     * @param voto il voto da aggiungere
     * @return true se il voto è stato aggiunto, false se il giudice aveva già votato
     */
    public boolean aggiungiVoto(Voto voto) {
        for (Voto v : voti) {
            if (v.getGiudice().equals(voto.getGiudice()) &&
                    v.getTeam().equals(voto.getTeam())) {
                return false; // Il giudice ha già votato per questo team
            }
        }
        voti.add(voto);
        return true;
    }

    /**
     * Restituisce l'evento hackathon a cui partecipa il team.
     *
     * @return l'evento hackathon
     */
    public Hackathon getEvento() {
        return evento;
    }

    /**
     * Aggiunge un partecipante al team.
     *
     * @param partecipante il partecipante da aggiungere
     */
    public void aggiungiPartecipante(Partecipante partecipante) {
        componenti.add(partecipante);
    }

    /**
     * Restituisce la lista dei componenti del team.
     *
     * @return la lista dei componenti
     */
    public List<Partecipante> getComponenti() {
        return componenti;
    }

    /**
     * Restituisce la lista dei voti ricevuti dal team.
     *
     * @return la lista dei voti
     */
    public List<Voto> getVoti() {
        return voti;
    }

    /**
     * Aggiunge un documento alla lista dei documenti pubblicati.
     *
     * @param documento il documento da aggiungere
     */
    public void aggiungiDocumento(Documento documento) {
        documentiPubblicati.add(documento);
    }

    /**
     * Imposta la lista dei componenti del team.
     *
     * @param componenti la lista dei componenti da impostare
     */
    public void setComponenti(List<Partecipante> componenti) {
        this.componenti = componenti;
    }

    /**
     * Imposta la lista dei documenti pubblicati.
     *
     * @param documentiPubblicati la lista dei documenti da impostare
     */
    public void setDocumentiPubblicati(List<Documento> documentiPubblicati) {
        this.documentiPubblicati = documentiPubblicati;
    }

    /**
     * Imposta la lista dei voti ricevuti.
     *
     * @param voti la lista dei voti da impostare
     */
    public void setVoti(List<Voto> voti){
        this.voti = voti;
    }
}
