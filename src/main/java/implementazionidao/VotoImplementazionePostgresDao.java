package implementazionidao;

import dao.VotoDAO;
import model.Voto;
import model.Team;
import model.Giudice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VotoImplementazionePostgresDao implements VotoDAO {

    private final Connection connection;

    public VotoImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    // In DAO
    public boolean salvaVoto(Voto voto) {
        String sql = "INSERT INTO voto (giudice_ssn, team_nome, punteggio) VALUES (?, ?, ?) " +
                "ON CONFLICT (giudice_ssn, team_nome) DO UPDATE SET punteggio = EXCLUDED.punteggio";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, voto.getGiudice().getSsn());
            ps.setString(2, voto.getTeam().getNome());
            ps.setInt(3, voto.getVotazione());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    @Override
    public List<Voto> getVotiByTeam(Team team) {
        List<Voto> voti = new ArrayList<>();
        String sql = "SELECT voto.punteggio, utente.ssn, utente.nome, utente.cognome, utente.email, utente.password, utente.ruolo " +
                "FROM voto JOIN utente ON voto.giudice_ssn = utente.ssn WHERE voto.team_nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Giudice giudice = new Giudice(
                        rs.getString("ssn"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("ruolo")
                );
                int punteggio = rs.getInt("punteggio");
                Voto voto = new Voto(giudice, team, punteggio);
                voti.add(voto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voti;
    }

    @Override
    public Voto getVotoByGiudiceAndTeam(Giudice giudice, Team team) {
        String sql = "SELECT punteggio FROM voto WHERE giudice_ssn = ? AND team_nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudice.getSsn());
            ps.setString(2, team.getNome());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int punteggio = rs.getInt("punteggio");
                return new Voto(giudice, team, punteggio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
