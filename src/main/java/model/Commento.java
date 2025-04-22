package org.example;

public class Commento {
    public Commento(Giudice giudice, Documento documento, String didascalia){
        this.giudice = giudice;
        this.documento = documento;
        this.didascalia = didascalia;
    }

    private String didascalia;
    private Giudice giudice;
    private Documento documento;
}
