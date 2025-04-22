package org.example;

public class Utente {
    public Utente(String ssn, String nome, String cognome, String email, String password, String ruolo){
        this.ssn = ssn;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    private String ssn;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo; //può essere "Partecipante", "Organizzatore" o "Giudice"
}
