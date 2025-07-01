package model;

/**
 * Classe che rappresenta un utente generico del sistema.
 * Contiene informazioni personali e credenziali di accesso dell'utente.
 */
public class Utente {
    protected String ssn;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String password;
    protected String ruolo; //può essere "Partecipante", "Organizzatore" o "Giudice"

    /**
     * Costruisce un nuovo utente.
     *
     * @param ssn      il codice fiscale dell'utente
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @param ruolo    il ruolo dell'utente
     */
    public Utente(String ssn, String nome, String cognome, String email, String password, String ruolo){
        this.ssn = ssn;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return l'email dell'utente
     */
    public String getEmail(){
        return email;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password dell'utente
     */
    public String getPassword(){
        return password;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return il ruolo dell'utente
     */
    public String getRuolo(){
        return ruolo;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce il codice fiscale dell'utente.
     *
     * @return il codice fiscale dell'utente
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruolo il nuovo ruolo da impostare
     */
    public void setRuolo(String ruolo){
        this.ruolo = ruolo;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password la nuova password da impostare
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email la nuova email da impostare
     */
    public void setEmail(String email){
        this.email = email;
    }


}
