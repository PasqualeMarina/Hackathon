package implementazionidao;

import model.*;
import dao.HackathonDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HackathonImplementazionePostgresDao implements HackathonDAO {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String RUOLO = "ruolo";
    private static final String SSN = "ssn";
    private static final String NOME = "nome";
    private static final String COGNOME = "cognome";
    private Connection connection;

    public HackathonImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvaHackathon(Hackathon hackathon) {
        String sql = "INSERT INTO hackathon(titolo, sede, data_inizio, durata, organizzatore_ssn, num_max_iscritti, num_max_per_team, descrizione_problema) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hackathon.getTitolo());
            ps.setString(2, hackathon.getSede());
            ps.setDate(3, Date.valueOf(hackathon.getDataInizio()));
            ps.setInt(4, hackathon.getDurata());
            ps.setString(5, hackathon.getOrganizzatore().getSsn());
            ps.setInt(6, hackathon.getNumMaxIscritti());
            ps.setInt(7, hackathon.getNumMaxPerTeam());
            ps.setString(8, hackathon.getDescrizioneProblema());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Hackathon getHackathonByTitolo(String titolo) {
        String sql = "SELECT sede, titolo, data_inizio, durata, organizzatore_ssn, num_max_iscritti, num_max_per_team, descrizione_problema FROM hackathon WHERE titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Recupera l'organizzatore
                Organizzatore organizzatore = getOrganizzatoreBySSN(rs.getString("organizzatore_ssn"));

                Hackathon h = new Hackathon(
                        rs.getString("sede"),
                        rs.getString("titolo"),
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getInt("durata"),
                        organizzatore,
                        rs.getInt("num_max_iscritti"),
                        rs.getInt("num_max_per_team")
                );
                h.setProblema(rs.getString("descrizione_problema"));

                // Giudici
                for (Giudice g : getGiudici(h)) {
                    h.aggiungiGiudice(g);
                }

                // Team
                for (Team t : getTeams(h)) {
                    h.aggiungiTeam(t);
                }

                // Partecipanti
                for (Partecipante p : getPartecipanti(h)) {
                    h.getListaPartecipanti().add(p);
                }

                return h;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Organizzatore getOrganizzatoreBySSN(String ssn) {
        String sql = "SELECT ssn, nome, cognome, email, password, ruolo FROM utente WHERE ssn = ? AND ruolo = 'Organizzatore'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Organizzatore(
                        rs.getString(SSN),
                        rs.getString(NOME),
                        rs.getString(COGNOME),
                        rs.getString(EMAIL),
                        rs.getString(PASSWORD),
                        rs.getString(RUOLO)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Giudice getGiudiceBySSN(String ssn) {
        String sql = "SELECT ssn, nome, cognome, email, password, ruolo FROM utente WHERE ssn = ? AND ruolo = 'Giudice'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Giudice(
                        rs.getString(SSN),
                        rs.getString(NOME),
                        rs.getString(COGNOME),
                        rs.getString(EMAIL),
                        rs.getString(PASSWORD),
                        rs.getString(RUOLO)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Partecipante getPartecipanteBySSN(String ssn) {
        String sql = "SELECT ssn, nome, cognome, email, password, ruolo FROM utente WHERE ssn = ? AND ruolo = 'Partecipante'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Partecipante(
                        rs.getString(SSN),
                        rs.getString(NOME),
                        rs.getString(COGNOME),
                        rs.getString(EMAIL),
                        rs.getString(PASSWORD),
                        rs.getString(RUOLO)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hackathon> getTuttiHackathon() {
        List<Hackathon> lista = new ArrayList<>();
        String sql = "SELECT titolo FROM hackathon";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Hackathon h = getHackathonByTitolo(rs.getString("titolo"));
                if (h != null) lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void aggiornaProblema(Hackathon hackathon, String problema) {
        String sql = "UPDATE hackathon SET descrizione_problema = ? WHERE titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, problema);
            ps.setString(2, hackathon.getTitolo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aggiungiGiudice(Hackathon hackathon, Giudice giudice) {
        String sql = "INSERT INTO giudice_hackathon (giudice_ssn, hackathon_titolo) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giudice.getSsn());
            ps.setString(2, hackathon.getTitolo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aggiungiTeam(Hackathon hackathon, Team team) {
        String sql = "INSERT INTO team(nome, evento_titolo) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNome());
            ps.setString(2, hackathon.getTitolo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inserisci i partecipanti del team
        for (Partecipante p : team.getComponenti()) {
            String sql2 = "INSERT INTO partecipante_team(partecipante_ssn, team_nome) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                ps.setString(1, p.getSsn());
                ps.setString(2, team.getNome());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Giudice> getGiudici(Hackathon hackathon) {
        List<Giudice> lista = new ArrayList<>();
        String sql = "SELECT giudice_ssn FROM giudice_hackathon WHERE hackathon_titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hackathon.getTitolo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Giudice g = getGiudiceBySSN(rs.getString("giudice_ssn"));
                if (g != null) lista.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Team> getTeams(Hackathon hackathon) {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT nome FROM team WHERE evento_titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hackathon.getTitolo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String teamNome = rs.getString("nome");
                List<Partecipante> membri = new ArrayList<>();

                // Carica membri del team
                String sql2 = "SELECT partecipante_ssn FROM partecipante_team WHERE team_nome = ?";
                try (PreparedStatement ps2 = connection.prepareStatement(sql2)) {
                    ps2.setString(1, teamNome);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        Partecipante p = getPartecipanteBySSN(rs2.getString("partecipante_ssn"));
                        if (p != null) membri.add(p);
                    }
                }

                if (!membri.isEmpty()) {
                    Team team = new Team(teamNome, membri.get(0), hackathon);
                    for (int i = 1; i < membri.size(); i++) {
                        team.aggiungiPartecipante(membri.get(i));
                    }
                    lista.add(team);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Partecipante> getPartecipanti(Hackathon hackathon) {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT partecipante_ssn FROM partecipante_hackathon WHERE hackathon_titolo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hackathon.getTitolo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partecipante p = getPartecipanteBySSN(rs.getString("partecipante_ssn"));
                if (p != null) lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

