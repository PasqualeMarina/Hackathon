package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un documento caricato da un team.
 * Contiene informazioni sul link del documento, il team proprietario
 * e i commenti fatti dai giudici.
 */
public class Documento {
    private String link;
    private Team team;
    private ArrayList<Commento> commenti = new ArrayList<>();

    /**
     * Costruisce un nuovo documento.
     *
     * @param link il link al documento
     * @param team il team proprietario del documento
     */
    public Documento(String link, Team team){
        this.link = link;
        this.team = team;
    }

    /**
     * Aggiunge un commento al documento.
     *
     * @param commento il commento da aggiungere
     */
    public void aggiungiCommento(Commento commento){
        commenti.add(commento);
    }

    /**
     * Restituisce il link al documento.
     *
     * @return il link al documento
     */
    public String getLink(){
        return link;
    }

    /**
     * Restituisce il team proprietario del documento.
     *
     * @return il team proprietario del documento
     */
    public Team getTeam(){
        return team;
    }

    /**
     * Imposta la lista dei commenti del documento.
     *
     * @param commenti la lista dei commenti da impostare
     */
    public void setCommenti(List<Commento> commenti){
        this.commenti = (ArrayList)commenti;
    }

    @Override
    public String toString(){
        return "Documento: " + link + " - Team: " + team.getNome();
    }
}
