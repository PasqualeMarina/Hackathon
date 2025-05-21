package model;

import java.util.ArrayList;

public class Team {
    private String nome;
    private Hackathon evento;
    private ArrayList<Partecipante> componenti = new ArrayList<>();
    private ArrayList<Documento> documentiPubblicati = new ArrayList<>();
    private ArrayList<Voto> voti = new ArrayList<>();

    public Team(String nome, Partecipante partecipante, Hackathon evento){
        this.nome = nome;
        componenti.add(partecipante);
        this.evento = evento;
    }

    @Override
    public String toString(){
        return "Team: " + nome;
    }

    public String getNome(){
        return nome;
    }

    public ArrayList<Documento> getDocumentiPubblicati(){
        return documentiPubblicati;
    }

    //aggiunge un voto solo se il giudice non ha gia votato per questo team
    public boolean aggiungiVoto(Voto voto){
        boolean presente = false;
        for(Voto v : voti)
            if(v.getGiudice().equals(voto.getGiudice()))
                presente = true;

        if(!presente)
            voti.add(voto);
        else
            return false;

        return true;
    }

    public ArrayList<Partecipante> getComponenti(){
        return componenti;
    }
}
