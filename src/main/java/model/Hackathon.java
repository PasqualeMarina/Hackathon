package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hackathon{
    public Hackathon(String sede, String nome, LocalDate data, int durata, Organizzatore organizzatore, int max_Iscritti, int max_perTeam){
        this.sede = sede;
        this.titolo = nome;
        this.dataInizio = data;
        this.durata = durata;
        this.organizzatore = organizzatore;
        this.num_MaxIscritti = max_Iscritti;
        this.num_MaxPerTeam = max_perTeam;
    }

    public void setProblema(String Problema){
        this.descrizioneProblema = Problema;
    }

    public void aggiungiGiudice(Giudice giudice){
        listaGiudici.add(giudice);
    }

    private String sede;
    private String titolo;
    private LocalDate dataInizio;
    private int durata = 2;
    private Organizzatore organizzatore;
    private ArrayList<Giudice> listaGiudici;
    private int num_MaxIscritti;
    private int num_MaxPerTeam;
    private String descrizioneProblema;
}
