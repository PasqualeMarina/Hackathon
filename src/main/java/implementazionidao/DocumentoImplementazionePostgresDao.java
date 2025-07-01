package implementazionidao;

import dao.DocumentoDAO;
import model.Commento;
import model.Documento;
import model.Giudice;
import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoImplementazionePostgresDao implements DocumentoDAO {

    private final Connection connection;

    public DocumentoImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean salvaDocumento(Documento documento) {
        String sql = "INSERT INTO documento (id, titolo, contenuto, team_nome) VALUES (nextval('documento_id_seq'), ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Documento del team " + documento.getTeam().getNome());
            ps.setString(2, documento.getLink()); // Il link è usato come contenuto
            ps.setString(3, documento.getTeam().getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void aggiungiCommento(Commento commento) {
        String sql = "INSERT INTO commento (id, autore_ssn, documento_id, contenuto) " +
                "VALUES (nextval('commento_id_seq'), ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, commento.getGiudice().getSsn());
            int documentoId = getIdDocumento(commento.getDocumento());
            ps.setInt(2, documentoId);
            ps.setString(3, commento.getDidascalia());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commento> getCommentiByDocumento(Documento documento) {
        List<Commento> commenti = new ArrayList<>();
        String sql = "SELECT c.contenuto, u.ssn, u.nome, u.cognome, u.email, u.password, u.ruolo " +
                "FROM commento c JOIN utente u ON c.autore_ssn = u.ssn " +
                "WHERE c.documento_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, getIdDocumento(documento));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String didascalia = rs.getString("contenuto");
                String ssn = rs.getString("ssn");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String ruolo = rs.getString("ruolo");

                Giudice giudice = new Giudice(ssn, nome, cognome, email, password, ruolo);
                Commento commento = new Commento(giudice, documento, didascalia);
                commenti.add(commento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commenti;
    }

    // Metodo di supporto per recuperare l'ID del documento dato team_nome e contenuto
    private int getIdDocumento(Documento documento) throws SQLException {
        String sql = "SELECT id FROM documento WHERE contenuto = ? AND team_nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, documento.getLink());
            ps.setString(2, documento.getTeam().getNome());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Documento non trovato");
            }
        }
    }

    public List<Documento> getDocumentiByTeam(Team team) throws SQLException {
        List<Documento> documenti = new ArrayList<>();

        String sql = """
        SELECT id, contenuto
        FROM documento
        WHERE team_nome = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, team.getNome());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String link = rs.getString("contenuto");

                    // Costruisco il Documento con l'oggetto team che ricevo in input
                    Documento d = new Documento(
                            link,
                            team
                    );
                    documenti.add(d);
                }
            }
        }

        return documenti;
    }
}
