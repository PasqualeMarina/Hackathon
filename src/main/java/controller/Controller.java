package controller;

import model.*;

import java.util.ArrayList;

public class Controller {
    private ArrayList<Utente> utenti = new ArrayList<>();
    private ArrayList<Hackathon> eventi = new ArrayList<>();

    public Controller(){
        // Inizializzazione con alcuni utenti di esempio
        aggiungiUtente(new Organizzatore("1234567890","Donato","Mario","email","password","Organizzatore"));
        aggiungiUtente(new Giudice      ("1234567891","Piero","Mario","email2","password","Giudice"));
    }

    public Utente verificaCredenziali(String email, String password){
        for(Utente e : utenti)
            if(e.getEmail().equals(email) && e.getPassword().equals(password))
                return e;
        return null;
    }

    public boolean aggiungiUtente(Utente utente){
        if(utente == null || utente.getEmail() == null)
            return false;
        for(Utente u : utenti)
            if(u.getEmail() != null && u.getEmail().equals(utente.getEmail()))
                return false;

        utenti.add(utente);
        return true;
    }

    public void aggiungiEvento(Hackathon evento, Organizzatore organizzatore){
        eventi.add(evento);
        organizzatore.creaEvento(evento);
    }

    public boolean controllaNome(String titolo){
        for(Hackathon e : eventi)
            if(e.getTitolo().equals(titolo))
                return false;
        return true;
    }

    public ArrayList<Hackathon> getEventi(){
        return eventi;
    }

    public ArrayList<Utente> getUtenti(){
        return utenti;
    }

    // -------- Giudice --------
    public boolean inviaInvito(Giudice giudice, Hackathon evento){
        return giudice.aggiungiInvito(evento);
    }

    public boolean pubblicaProblema(Giudice giudice, Hackathon evento, String problema){
        return giudice.pubblicaProblema(evento, problema);
    }

    public String getInviti(Giudice giudice){
        String result = "";
        for(Hackathon e : giudice.getInvitiRicevuti()){
            result += e.getInfo() + "\n---------------------------------------";
        }
        return result;
    }

    public ArrayList<Hackathon> getInvitiGiudice(Giudice giudice){
        return giudice.getInvitiRicevuti();
    }

    public boolean accettaInvito(Giudice giudice, Hackathon evento){
        return giudice.accettaInvito(evento);
    }

    public void rifiutaInvito(Giudice giudice, Hackathon evento){
        giudice.rifiutaInvito(evento);
    }

    // -------- Partecipante --------
    public boolean inviaInvito(Partecipante p,String email){
        for(Utente u: this.getUtenti()){
            if (u.getEmail().equals(email) && u instanceof Partecipante){
                return p.inviaInvito((Partecipante)u, p.getTeam());
            }
        }
        return false;
    }

    public boolean effettuaIscrizione(Partecipante partecipante, Hackathon evento){
        return partecipante.effettuaIscrizione(evento);
    }

    public ArrayList<Team> getInviti(Partecipante partecipante) {
        return partecipante.getInvitiRicevuti();
    }

    public String getTeam(Partecipante partecipante) {
        Team team = partecipante.getTeam();
        return (team != null) ? team.getNome() : "";
    }

    public void accettaInvito(Partecipante partecipante, Team team) {
        partecipante.accettaInvito(team);
    }

    public ArrayList<Hackathon> getHackathonNonIscritti(ArrayList<Hackathon> listaH, Partecipante partecipante) {
        ArrayList<Hackathon> nonIscritti = new ArrayList<>();

        for (Hackathon h : listaH) {
            boolean isAlreadyIscritto = partecipante.getIscrizioniEventi().contains(h);
            boolean isCompleto = h.getPartecipanti().size() >= h.getNumMaxIscritti();

            if (!isAlreadyIscritto && !isCompleto) {
                nonIscritti.add(h);
            }
        }

        return nonIscritti;
    }

    public Team creaTeam(String nomeTeam, Hackathon evento, Partecipante partecipante) {
        Team team = new Team(nomeTeam, partecipante, evento);
        partecipante.setTeam(team);
        return team;
    }

    public ArrayList<Hackathon> getIscrizioni(Partecipante partecipante) {
        return partecipante.getIscrizioniEventi();
    }

    // -------- Team --------
    public ArrayList<Team> getTeams(Hackathon evento){
        return evento.getListaTeam();
    }

    public String getDocumenti(Team team){
        StringBuilder result = new StringBuilder();
        String nome = team.getNome();
        for(Documento d : team.getDocumentiPubblicati()){
            result.append(nome).append(": ").append(d.getLink()).append("\n");
        }
        return result.toString();
    }

    public boolean daiVoto(Giudice giudice, Team team, int voto){
        return team.aggiungiVoto(new Voto(giudice, team, voto));
    }

    // -------- Altro --------
    public String getInfo(Hackathon evento){
        return evento.getInfo();
    }
}
