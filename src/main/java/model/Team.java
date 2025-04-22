package org.example;

import java.util.ArrayList;

public class Team {
    public Team(String nome, Partecipante partecipante, Hackathon evento){
        this.nome = nome;
        componenti.add(partecipante);
        this.evento = evento;
    }

    private String nome;
    private Hackathon evento;
    private ArrayList<Partecipante> componenti;
    private ArrayList<Documento> documentiPubblicati;
}
