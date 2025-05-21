package model;
import java.util.ArrayList;

public class Partecipante extends Utente {
    private ArrayList<Hackathon> iscrizioniEventi = new ArrayList<>();
    private ArrayList<Team> invitiRicevuti = new ArrayList<>();
    Team team;

    public Partecipante(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public boolean effettuaIscrizione(Hackathon evento){
        if(evento.getNumMaxIscritti() > evento.getPartecipanti().size()){
            iscrizioniEventi.add(evento);
            return true;
        }
        else
            return false;
    }

    public void creaTeam(String nome, Hackathon evento){
        Team team = new Team(nome, this, evento);
        this.team = team;
    }

    public boolean inviaInvito(Partecipante partecipante, Team team){
        if(partecipante.invitiRicevuti.contains(team))
            return false;
        else{
            partecipante.invitiRicevuti.add(team);
            return true;
        }
    }

    public void accettaInvito(Team team){
        for(Hackathon h : iscrizioniEventi)
            if(h.getListaTeam().contains(team)){
                if(!h.getPartecipanti().contains(this))
                    effettuaIscrizione(h);

                if(this.team == null && this.invitiRicevuti.contains(team)){
                    this.team = team;
                    invitiRicevuti.remove(team);
                }
            }
    }

    public Team getTeam(){
        return team;
    }

    public ArrayList<Hackathon> getIscrizioniEventi(){
        return iscrizioniEventi;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public ArrayList<Team> getInvitiRicevuti(){
        return invitiRicevuti;
    }
}
