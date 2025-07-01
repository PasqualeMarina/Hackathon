package implementazionidao;

import dao.PartecipanteDAO;
import model.Hackathon;
import model.Partecipante;
import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartecipanteImplementazionePostgresDao implements PartecipanteDAO {
    private final Connection connection;

    public PartecipanteImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean effettuaIscrizione(Partecipante partecipante, Hackathon evento) {
        String sql = "INSERT INTO partecipante_hackathon (partecipante_ssn, hackathon_titolo) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ps.setString(2, evento.getTitolo());
            ps.executeUpdate();
            return true;
        } catch (SQLException _) {
            // iscrizione già presente o violazione di vincolo
            return false;
        }
    }

    public void creaTeam(String nome, Hackathon h, Partecipante p) throws SQLException {

        try {
            connection.setAutoCommit(false);

            // 1. Inserisci il team
            String insertTeam = "INSERT INTO team(nome, evento_titolo) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertTeam)) {
                ps.setString(1, nome);
                ps.setString(2, h.getTitolo());
                ps.executeUpdate();
            }

            // 2. Inserisci il partecipante nel team
            String insertPartecipanteTeam = "INSERT INTO partecipante_team(partecipante_ssn, team_nome) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertPartecipanteTeam)) {
                ps.setString(1, p.getSsn());
                ps.setString(2, nome);
                ps.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }



    @Override
    public boolean inviaInvito(Partecipante destinatario, Team team) {
        // Simulazione invito: verifica se già invitato
        String sql = "SELECT partecipante_ssn, team_nome FROM partecipante_team WHERE partecipante_ssn = ? AND team_nome = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, destinatario.getSsn());
            ps.setString(2, team.getNome());

            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException _) {
            return false;
        }
    }

    @Override
    public boolean accettaInvito(Partecipante partecipante, Team team) {
        String sql = "INSERT INTO partecipante_team (partecipante_ssn, team_nome) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ps.setString(2, team.getNome());
            ps.executeUpdate();
            return true;
        } catch (SQLException _) {
            return false;
        }
    }

    @Override
    public List<Hackathon> getIscrizioni(Partecipante partecipante) {
        List<Hackathon> eventi = new ArrayList<>();
        String sql = "SELECT h.titolo, h.sede, h.data_inizio, h.durata, h.organizzatore_ssn, h.num_max_iscritti, h.num_max_per_team, h.descrizione_problema " +
                "FROM partecipante_hackathon ph JOIN hackathon h ON ph.hackathon_titolo = h.titolo WHERE ph.partecipante_ssn = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hackathon evento = new Hackathon(
                        rs.getString("sede"),
                        rs.getString("titolo"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getInt("durata"),
                        null,
                        rs.getInt("num_max_iscritti"),
                        rs.getInt("num_max_per_team")
                );
                evento.setProblema(rs.getString("descrizione_problema"));
                eventi.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // **AGGIORNA LA LISTA INTERNA DEL PARTECIPANTE**
        partecipante.getIscrizioniEventi().clear();
        partecipante.getIscrizioniEventi().addAll(eventi);

        return eventi;
    }

    @Override
    public List<Team> getInviti(Partecipante partecipante) {
        // Simulazione inviti: ritornare team dove *non* è ancora membro ma ha ricevuto invito in memoria
        return partecipante.getInvitiRicevuti(); // gestito a livello applicativo
    }

    @Override
    public Team getTeam(Partecipante partecipante, Hackathon evento) {
        String sql = "SELECT t.nome FROM partecipante_team pt JOIN team t ON pt.team_nome = t.nome " +
                "WHERE pt.partecipante_ssn = ? AND t.evento_titolo = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ps.setString(2, evento.getTitolo());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Team(rs.getString("nome"), partecipante, evento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Team> getTeams(Partecipante partecipante) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT t.nome, t.evento_titolo FROM partecipante_team pt JOIN team t ON pt.team_nome = t.nome WHERE pt.partecipante_ssn = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, partecipante.getSsn());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Team team = new Team(rs.getString("nome"), partecipante, new Hackathon(rs.getString("evento_titolo"), "", null, 0, null, 0, 0));
                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    @Override
    public List<Partecipante> getPartecipantiByHackathon(String titoloEvento) {
        List<Partecipante> partecipanti = new ArrayList<>();
        String sql = """
        SELECT u.ssn, u.nome, u.cognome, u.email, u.password, u.ruolo
        FROM partecipante_hackathon ph
        JOIN utente u ON ph.partecipante_ssn = u.ssn
        WHERE ph.hackathon_titolo = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titoloEvento);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partecipanti;
    }

    @Override
    public List<Partecipante> getPartecipantiByTeam(String nomeTeam) {
        List<Partecipante> partecipanti = new ArrayList<>();
        String sql = """
        SELECT u.ssn, u.nome, u.cognome, u.email, u.password, u.ruolo
        FROM partecipante_team pt
        JOIN utente u ON pt.partecipante_ssn = u.ssn
        WHERE pt.team_nome = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeTeam);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puoi sostituirlo con una gestione più adeguata
        }

        return partecipanti;
    }

}

