package implementazionidao;

import dao.UtenteDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteImplementazionePostgresDao implements UtenteDAO {

    private final Connection connection;

    // Costruttore che prende la connection dall'esterno
    public UtenteImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean salvaUtente(Utente utente) {
        String sql = "INSERT INTO utente (ssn, nome, cognome, email, password, ruolo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, utente.getSsn());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setString(4, utente.getEmail());
            ps.setString(5, utente.getPassword());
            ps.setString(6, utente.getRuolo());

            int righe = ps.executeUpdate();
            return righe > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Utente> getTuttiUtenti() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT ssn, nome, cognome, email, password, ruolo FROM utente";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utente u = creaUtenteDaResultSet(rs);
                utenti.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utenti;
    }

    @Override
    public Utente getUtenteByEmail(String email) {
        String sql = "SELECT ssn, nome, cognome, email, password, ruolo FROM utente WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creaUtenteDaResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Utente creaUtenteDaResultSet(ResultSet rs) throws SQLException {
        String ssn = rs.getString("ssn");
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String ruolo = rs.getString("ruolo");

        switch (ruolo) {
            case "Giudice":
                return new Giudice(ssn, nome, cognome, email, password, ruolo);
            case "Partecipante":
                return new Partecipante(ssn, nome, cognome, email, password, ruolo);
            case "Organizzatore":
                return new Organizzatore(ssn, nome, cognome, email, password, ruolo);
            default:
                return new Utente(ssn, nome, cognome, email, password, ruolo);
        }
    }
}
