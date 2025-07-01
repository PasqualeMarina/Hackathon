package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un giudice dell'hackathon.
 * Contiene informazioni sugli eventi in cui partecipa come giudice
 * e gli inviti ricevuti per altri eventi.
 */

public class Giudice extends Utente {
    private List<Hackathon> eventiDaGiudice = new ArrayList<>();
    private List<Hackathon> invitiRicevuti = new ArrayList<>();

    public Giudice(String ssn, String nome, String cognome, String email, String password, String ruolo) {
        super(ssn, nome, cognome, email, password, ruolo);
    }

    /**
     * Pubblica un problema per un evento hackathon.
     *
     * @param evento   l'evento hackathon per cui pubblicare il problema
     * @param problema il testo del problema da pubblicare
     * @return true se il problema è stato pubblicato con successo, false altrimenti
     */
    public boolean pubblicaProblema(Hackathon evento, String problema){
        if(evento.getListaGiudici().contains(this)){
            evento.setProblema(problema);
            return true;
        }

        return false;
    }

    /**
     * Accetta un invito a giudicare un evento hackathon.
     *
     * @param evento l'evento hackathon per cui accettare l'invito
     * @return true se l'invito è stato accettato con successo, false altrimenti
     */
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

    /**
     * Rifiuta un invito a giudicare un evento hackathon.
     *
     * @param evento l'evento hackathon per cui rifiutare l'invito
     */
    public void rifiutaInvito(Hackathon evento){
        if(invitiRicevuti.contains(evento))
            invitiRicevuti.remove(evento);
    }

    /**
     * Aggiunge un nuovo invito per giudicare un evento hackathon.
     *
     * @param evento l'evento hackathon per cui aggiungere l'invito
     * @return true se l'invito è stato aggiunto con successo, false altrimenti
     */
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

    /**
     * Pubblica un commento su un documento.
     *
     * @param didascalia il testo del commento
     * @param documento  il documento su cui pubblicare il commento
     */
    public void pubblicaCommento(String didascalia, Documento documento){
        Commento commento = new Commento(this, documento, didascalia);
        documento.aggiungiCommento(commento);
    }

    /**
     * Restituisce la lista degli inviti ricevuti.
     *
     * @return la lista degli eventi hackathon per cui si è ricevuto un invito
     */
    public List<Hackathon> getInvitiRicevuti(){
        return invitiRicevuti;
    }

    /**
     * Imposta la lista degli eventi in cui si partecipa come giudice.
     *
     * @param eventiDaGiudice la lista degli eventi da impostare
     */
    public void setEventiDaGiudice(List<Hackathon> eventiDaGiudice){
        this.eventiDaGiudice = eventiDaGiudice;
    }

    /**
     * Imposta la lista degli inviti ricevuti.
     *
     * @param invitiRicevuti la lista degli inviti da impostare
     */
    public void setInvitiRicevuti(List<Hackathon> invitiRicevuti) {
        this.invitiRicevuti = invitiRicevuti;
    }

    /**
     * Restituisce la lista degli eventi in cui si partecipa come giudice.
     *
     * @return la lista degli eventi hackathon in cui si è giudice
     */
    public List<Hackathon> getEventiDaGiudice(){
        return eventiDaGiudice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Giudice other = (Giudice) obj;
        return ssn.equals(other.ssn); // usa il campo SSN o altro identificatore unico
    }

    @Override
    public int hashCode() {
        return ssn.hashCode();
    }

}
