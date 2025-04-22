package org.example;
import java.time.LocalDate;
import java.util.ArrayList;

public class Organizzatore extends Utente {
    public Organizzatore(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public void creaEvento(String sede, String nome, LocalDate data, int durata, Organizzatore organizzatore, int max_Iscritti, int max_perTeam){
        Hackathon evento = new Hackathon(sede, nome, data, durata, this, max_Iscritti, max_perTeam);
        eventiOrganizzati.add(evento);
    }

    public void invitaGiudice(Giudice giudice, Hackathon evento){
        if(eventiOrganizzati.contains(evento))
            giudice.aggiungiInvito(evento);
    }

    private ArrayList<Hackathon> eventiOrganizzati;
}
