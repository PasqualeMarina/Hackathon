package implementazionidao;

import dao.TeamDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamImplementazionePostgresDao implements TeamDAO {

    private final Connection connection;

    public TeamImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Team getTeamByNome(String nome, Hackathon evento) {
        String sql = "SELECT nome FROM team WHERE nome = ? AND evento_titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, evento.getTitolo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Team team = new Team(rs.getString("nome"), null, evento); // componente null, verrà caricato dopo
                team = caricaComponentiDocumentiEVoti(team);
                return team;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Team> getTeamsByHackathon(Hackathon evento) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT nome FROM team WHERE evento_titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, evento.getTitolo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Team team = new Team(rs.getString("nome"), null, evento);
                team = caricaComponentiDocumentiEVoti(team);
                teams.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public void aggiungiPartecipante(Team team, Partecipante partecipante) {
        String sql = "INSERT INTO partecipante_team (partecipante_ssn, team_nome) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ps.setString(2, team.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aggiungiDocumento(Team team, Documento documento) {
        String sql = "INSERT INTO documento (titolo, contenuto, team_nome) VALUES (nextval('documento_id_seq'), ?, ?)";
        // Nota: il campo "titolo" in DB rappresenta l'ID documento, che è sequenziale
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, documento.getLink());
            ps.setString(2, team.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Partecipante> getComponenti(Team team) {
        List<Partecipante> partecipanti = new ArrayList<>();
        String sql = "SELECT utente.ssn, utente.nome, utente.cognome, utente.email, utente.password, utente.ruolo " +
                "FROM partecipante_team " +
                "JOIN utente ON partecipante_team.partecipante_ssn = utente.ssn " +
                "WHERE partecipante_team.team_nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partecipante p = new Partecipante(
                        rs.getString("ssn"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("ruolo")
                );
                partecipanti.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partecipanti;
    }

    @Override
    public List<Documento> getDocumentiPubblicati(Team team) {
        List<Documento> documenti = new ArrayList<>();
        String sql = "SELECT id, contenuto FROM documento WHERE team_nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Documento documento = new Documento(rs.getString("contenuto"), team);
                // Per ora non carichiamo commenti (possibile estensione)
                documenti.add(documento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documenti;
    }

    @Override
    public List<Voto> getVoti(Team team) {
        List<Voto> voti = new ArrayList<>();
        String sql = "SELECT voto.punteggio, utente.ssn, utente.nome, utente.cognome, utente.email, utente.password, utente.ruolo " +
                "FROM voto " +
                "JOIN utente ON voto.giudice_ssn = utente.ssn " +
                "WHERE voto.team_nome = ?";
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

    private Team caricaComponentiDocumentiEVoti(Team team) {
        team.getComponenti().clear();
        team.getDocumentiPubblicati().clear();
        team.getComponenti().addAll(getComponenti(team));
        team.getDocumentiPubblicati().addAll(getDocumentiPubblicati(team));
        team.getVoti().clear();
        team.getVoti().addAll(getVoti(team));
        return team;
    }
}
