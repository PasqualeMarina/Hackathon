package implementazionidao;

import dao.OrganizzatoreDAO;
import model.Giudice;
import model.Hackathon;
import model.Organizzatore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganizzatoreImplementazionePostgresDao implements OrganizzatoreDAO {

    private final Connection connection;

    public OrganizzatoreImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void creaEvento(Hackathon evento, Organizzatore organizzatore) {
        String sql = "INSERT INTO hackathon (titolo, sede, data_inizio, durata, descrizione_problema, organizzatore_ssn, num_max_iscritti, num_max_per_team) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ps.setString(2, evento.getSede());
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataInizio()));
            ps.setInt(4, evento.getDurata());
            ps.setString(5, evento.getDescrizioneProblema());
            ps.setString(6, organizzatore.getSsn());
            ps.setInt(7, evento.getNumMaxIscritti());
            ps.setInt(8, evento.getNumMaxPerTeam());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invitaGiudice(Giudice giudice, Hackathon evento) {
        String sql = "INSERT INTO giudice_hackathon (giudice_ssn, hackathon_titolo) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudice.getSsn());
            ps.setString(2, evento.getTitolo());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Organizzatore getOrganizzatoreByHackathon(String titoloHackathon) {
        String sql = "SELECT u.ssn, u.nome, u.cognome, u.email, u.password, u.ruolo " +
                "FROM utente u JOIN hackathon h ON u.ssn = h.organizzatore_ssn " +
                "WHERE h.titolo = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titoloHackathon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Organizzatore(
                            rs.getString("ssn"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("ruolo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Se non trovato
    }

}
