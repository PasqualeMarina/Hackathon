package model;

import java.util.ArrayList;

public class Documento {
    private String link;
    private Team team;
    private ArrayList<Commento> commenti = new ArrayList<>();

    public Documento(String link, Team team){
        this.link = link;
        this.team = team;
    }

    public void aggiungiCommento(Commento commento){
        commenti.add(commento);
    }

    public String getLink(){
        return link;
    }
}
