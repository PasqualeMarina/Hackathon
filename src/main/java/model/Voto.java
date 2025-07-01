package model;

/**
 * Classe che rappresenta un voto assegnato da un giudice ad un team.
 * Contiene informazioni sul giudice che ha votato, il team valutato e il punteggio assegnato.
 */
public class Voto {
    /**
     * Il giudice che ha assegnato il voto
     */
    private Giudice giudice;
    /**
     * Il team che ha ricevuto il voto
     */
    private Team team;
    /**
     * Il punteggio assegnato
     */
    private int votazione;

    /**
     * Costruisce un nuovo voto.
     *
     * @param giudice il giudice che assegna il voto
     * @param team    il team che riceve il voto
     * @param voto    il punteggio assegnato
     */
    public Voto(Giudice giudice, Team team, int voto){
        this.giudice = giudice;
        this.team = team;
        this.votazione = voto;
    }

    /**
     * Restituisce il team che ha ricevuto il voto.
     *
     * @return il team votato
     */
    public Team getTeam(){
        return team;
    }

    /**
     * Imposta il giudice che ha assegnato il voto.
     *
     * @param giudice il giudice da impostare
     */
    public void setGiudice(Giudice giudice){
        this.giudice = giudice;
    }

    /**
     * Restituisce il punteggio assegnato.
     *
     * @return il punteggio del voto
     */
    public int getVotazione(){
        return votazione;
    }

    /**
     * Restituisce il giudice che ha assegnato il voto.
     *
     * @return il giudice che ha votato
     */
    public Giudice getGiudice() {
        return giudice;
    }

    @Override
    public String toString(){
        return giudice.getNome() + " " + giudice.getCognome() + " ha votato per il team " + team.getNome() + " con votazione " + votazione;
    }
}
