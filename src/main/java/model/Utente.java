package model;

public class Utente {
    private String ssn;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo; //può essere "Partecipante", "Organizzatore" o "Giudice"

    public Utente(String ssn, String nome, String cognome, String email, String password, String ruolo){
        this.ssn = ssn;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getRuolo(){
        return ruolo;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

}
