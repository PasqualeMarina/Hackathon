package org.example;
import java.util.ArrayList;

public class Partecipante extends Utente {
    public Partecipante(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public effettuaIscrizione(Hackathon evento){
        iscrizioniEventi.add(evento);
    }

    public creaTeam(String Nome, Hackathon evento){
        Team team = new Team(Nome, this, evento);
        this.team = team;
    }

    public inviaInvito(Partecipante partecipante){
        partecipante.invitiRicevuti.add(this.team);
    }

    public accettaInvito(Team team){
        if(this.team == null && this.invitiRicevuti.contains(team))
            this.team = team;
    }

    private ArrayList<Hackathon> iscrizioniEventi;
    private ArrayList<Team> invitiRicevuti;
    Team team;
}
