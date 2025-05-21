package model;

import java.util.ArrayList;

public class Giudice extends Utente {
    private ArrayList<Hackathon> eventiDaGiudice = new ArrayList<>();
    private ArrayList<Hackathon> invitiRicevuti = new ArrayList<>();

    public Giudice(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public boolean pubblicaProblema(Hackathon evento, String Problema){
        if(evento.getListaGiudici().contains(this)){
            evento.setProblema(Problema);
            return true;
        }

        return false;
    }

    public boolean accettaInvito(Hackathon evento){
        if(invitiRicevuti.contains(evento)){
            invitiRicevuti.remove(evento);
            eventiDaGiudice.add(evento);
            evento.aggiungiGiudice(this);
            return true;
        }
        else
            return false;
    }

    public void rifiutaInvito(Hackathon evento){
        if(invitiRicevuti.contains(evento))
            invitiRicevuti.remove(evento);
    }

    public boolean aggiungiInvito(Hackathon evento){
        for(Hackathon e : eventiDaGiudice)
            if(e.equals(evento))
                return false;
        for(Hackathon e : invitiRicevuti)
            if(e.equals(evento))
                return false;

        invitiRicevuti.add(evento);
        return true;
    }

    public void pubblicaCommento(String didascalia, Documento documento){
        Commento commento = new Commento(this, documento, didascalia);
        documento.aggiungiCommento(commento);
    }

    public ArrayList<Hackathon> getInvitiRicevuti(){
        return invitiRicevuti;
    }
}
