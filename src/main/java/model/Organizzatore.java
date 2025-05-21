package model;
import java.time.LocalDate;
import java.util.ArrayList;

public class Organizzatore extends Utente {
    private ArrayList<Hackathon> eventiOrganizzati = new ArrayList<>();

    public Organizzatore(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public void creaEvento(String sede, String nome, LocalDate data, int durata, Organizzatore organizzatore, int maxIscritti, int maxPerTeam){
        Hackathon evento = new Hackathon(sede, nome, data, durata, organizzatore, maxIscritti, maxPerTeam);
        eventiOrganizzati.add(evento);
    }

    public void creaEvento(Hackathon evento){
        eventiOrganizzati.add(evento);
    }

    public void invitaGiudice(Giudice giudice, Hackathon evento){
        if(eventiOrganizzati.contains(evento))
            giudice.aggiungiInvito(evento);
    }
}
