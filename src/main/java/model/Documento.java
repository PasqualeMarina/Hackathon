package org.example;

import java.util.ArrayList;

public class Documento {
    public Documento(String link, Team team){
        this.link = link;
        this.team = team;
    }

    public void aggiungiCommento(Commento commento){
        commenti.add(commento);
    }

    private String link;
    private Team team;
    private ArrayList<Commento> commenti;
}
