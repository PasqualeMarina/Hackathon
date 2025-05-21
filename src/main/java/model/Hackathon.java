package model;

import java.time.LocalDate;
import java.util.ArrayList;

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

    public void setProblema(String problema){
        this.descrizioneProblema = problema;
    }

    public void aggiungiGiudice(Giudice giudice){
        listaGiudici.add(giudice);
    }

    public Organizzatore getOrganizzatore(){
        return organizzatore;
    }

    @Override
    public String toString(){
        return "Titolo: " + titolo;
    }

    public String getInfo(){
        return "Titolo: " + titolo + "\nSede: " + sede + "\nData inizio: " + dataInizio + "\nDurata: " + durata + "\nNumero massimo iscritti: " + numMaxIscritti + "\nNumero massimo per team: " + numMaxPerTeam + "\nDescrizione problema: " + descrizioneProblema + "\nNumero Giudici: " + getListaGiudici().size();
    }

    public String getTitolo(){
        return titolo;
    }

    public ArrayList<Giudice> getListaGiudici(){
        return listaGiudici;
    }

    public ArrayList<Team> getListaTeam(){
        return listaTeam;
    }

    public void aggiungiTeam(Team team){
        listaTeam.add(team);
    }

    public ArrayList<Partecipante> getPartecipanti(){
        return listaPartecipanti;
    }

    public int getNumMaxIscritti(){
        return numMaxIscritti;
    }
}
