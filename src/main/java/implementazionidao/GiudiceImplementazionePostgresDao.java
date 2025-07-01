package implementazionidao;

import dao.GiudiceDAO;
import model.Giudice;
import model.Hackathon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiudiceImplementazionePostgresDao implements GiudiceDAO {

    private final Connection connection;

    public GiudiceImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean pubblicaProblema(Hackathon evento, String problema) {
        String sql = "UPDATE hackathon SET descrizione_problema = ? WHERE titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, problema);
            ps.setString(2, evento.getTitolo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Aggiornato per segnare stato = 'ACCETTATO'
    public boolean accettaInvito(Hackathon evento, String giudiceSSN) {
        String sql = "UPDATE giudice_hackathon SET stato = 'ACCETTATO' " +
                "WHERE hackathon_titolo = ? AND giudice_ssn = ? AND stato = 'INVITATO'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ps.setString(2, giudiceSSN);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Cancella invito se rifiutato
    public void rifiutaInvito(Hackathon evento, String giudiceSSN) {
        String sql = "DELETE FROM giudice_hackathon WHERE hackathon_titolo = ? AND giudice_ssn = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ps.setString(2, giudiceSSN);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Invita un giudice con stato = 'INVITATO'
    public boolean aggiungiInvito(Giudice giudice, Hackathon evento) {
        String sql = "INSERT INTO giudice_hackathon (giudice_ssn, hackathon_titolo, stato) " +
                "VALUES (?, ?, 'INVITATO')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudice.getSsn());
            ps.setString(2, evento.getTitolo());
            ps.executeUpdate();
            return true;
        } catch (SQLException _) {
            return false; // es. già invitato
        }
    }

    // ✅ Ottiene solo inviti non ancora accettati
    public List<Hackathon> getInvitiRicevuti(Giudice giudice, List<Hackathon> eventiCaricati) {
        List<Hackathon> inviti = new ArrayList<>();
        String sql = """
            SELECT h.titolo
            FROM hackathon h
            JOIN giudice_hackathon gh ON h.titolo = gh.hackathon_titolo
            WHERE gh.giudice_ssn = ? AND gh.stato = 'INVITATO'
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudice.getSsn());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String titolo = rs.getString("titolo");
                for (Hackathon h : eventiCaricati) {
                    if (h.getTitolo().equals(titolo)) {
                        inviti.add(h);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inviti;
    }

    // ✅ Ottiene solo giudici che hanno accettato
    public List<Giudice> getGiudiciByHackathon(String titoloEvento) {
        List<Giudice> giudici = new ArrayList<>();
        String sql = """
            SELECT u.ssn, u.nome, u.cognome, u.email, u.password, u.ruolo
            FROM giudice_hackathon gh
            JOIN utente u ON gh.giudice_ssn = u.ssn
            WHERE gh.hackathon_titolo = ? AND gh.stato = 'ACCETTATO'
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titoloEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Giudice g = new Giudice(
                        rs.getString("ssn"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("ruolo")
                );
                giudici.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giudici;
    }

    @Override
    public Giudice getGiudiceBySsn(String ssn) throws SQLException {
        String sql = """
            SELECT ssn, nome, cognome, email, password, ruolo
            FROM utente
            WHERE ssn = ? AND ruolo = 'Giudice'
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Giudice(
                        rs.getString("ssn"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("ruolo")
                );
            }
        }
        return null;
    }
}
