package model;

/**
 * Classe che rappresenta un commento fatto da un giudice su un documento.
 * Contiene informazioni sulla didascalia, il giudice che ha fatto il commento
 * e il documento commentato.
 */
public class Commento {
    private String didascalia;
    private Giudice giudice;
    private Documento documento;

    /**
     * Costruisce un nuovo commento.
     *
     * @param giudice    il giudice che ha fatto il commento
     * @param documento  il documento su cui è stato fatto il commento
     * @param didascalia il testo del commento
     */
    public Commento(Giudice giudice, Documento documento, String didascalia){
        this.giudice = giudice;
        this.documento = documento;
        this.didascalia = didascalia;
    }

    /**
     * Restituisce la didascalia del commento.
     *
     * @return la didascalia del commento
     */
    public String getDidascalia(){
        return didascalia;
    }

    /**
     * Restituisce il giudice che ha fatto il commento.
     *
     * @return il giudice che ha fatto il commento
     */
    public Giudice getGiudice(){
        return giudice;
    }

    /**
     * Restituisce il documento su cui è stato fatto il commento.
     *
     * @return il documento su cui è stato fatto il commento
     */
    public Documento getDocumento(){
        return documento;
    }
}
