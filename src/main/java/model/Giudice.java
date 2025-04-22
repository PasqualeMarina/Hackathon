package org.example;

import java.util.ArrayList;

public class Giudice extends Utente {
    public Giudice(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    public void pubblicaProblema(Hackathon evento, String Problema){
        evento.setProblema(Problema);
    }

    public void accettaInvito(Hackathon evento){
        if(invitiRicevuti.contains(evento)){
            invitiRicevuti.remove(evento);
            eventiDaGiudice.add(evento);
            evento.aggiungiGiudice(this);
        }
    }

    public void aggiungiInvito(Hackathon evento){
        invitiRicevuti.add(evento);
    }

    public void pubblicaCommento(String didascalia, Documento documento){
        Commento commento = new Commento(this, documento, didascalia);
        documento.aggiungiCommento(commento);
    }

    private ArrayList<Hackathon> eventiDaGiudice;
    private ArrayList<Hackathon> invitiRicevuti;
}
