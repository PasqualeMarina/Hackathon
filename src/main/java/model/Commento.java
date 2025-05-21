package model;

public class Commento {
    private String didascalia;
    private Giudice giudice;
    private Documento documento;

    public Commento(Giudice giudice, Documento documento, String didascalia){
        this.giudice = giudice;
        this.documento = documento;
        this.didascalia = didascalia;
    }
}
