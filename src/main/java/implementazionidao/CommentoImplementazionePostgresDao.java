package implementazionidao;

import dao.CommentoDAO;
import dao.GiudiceDAO;
import model.Commento;
import model.Documento;
import model.Giudice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentoImplementazionePostgresDao implements CommentoDAO {

    private final Connection connection;

    public CommentoImplementazionePostgresDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean salvaCommento(Commento commento) {
        // Assumo che la tabella commento abbia le colonne:
        // id (serial PK), autore_ssn, documento_id, contenuto

        String sql = "INSERT INTO commento (autore_ssn, documento_id, contenuto) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, commento.getGiudice().getSsn());
            // Per documento_id serve un modo per recuperare l'id dal Documento,
            // supponiamo che Documento abbia un metodo getId() (se no serve un'altra query)
            int documentoId = recuperaIdDocumento(commento.getDocumento());

            ps.setInt(2, documentoId);
            ps.setString(3, commento.getDidascalia());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private int recuperaIdDocumento(model.Documento documento) throws SQLException {
        // Serve implementare questo metodo per recuperare l'id del documento dal DB
        // ad esempio tramite link e team_nome (unici o almeno identificativi)
        // oppure puoi passare direttamente l'id nel modello Documento

        String sql = "SELECT id FROM documento WHERE link = ? AND team_nome = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, documento.getLink());
            ps.setString(2, documento.getTeam().getNome());

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Documento non trovato");
                }
            }
        }
    }

    public List<Commento> getCommentiByDocumento(Documento documento, GiudiceDAO g) throws SQLException {
        List<Commento> commenti = new ArrayList<>();

        String sql = """
        SELECT c.autore_ssn, c.contenuto
        FROM commento c
        JOIN documento d ON c.documento_id = d.id
        WHERE d.contenuto = ?
        ORDER BY c.id
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // filtriamo per link del documento
            ps.setString(1, documento.getLink());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // recupera lo SSN del giudice autore
                    String autoreSsn = rs.getString("autore_ssn");
                    // ottieni l'istanza di Giudice (assicurati che giudiceDAO sia disponibile nel DAO)
                    Giudice giudice = g.getGiudiceBySsn(autoreSsn);
                    // testo del commento da usare come didascalia
                    String didascalia = rs.getString("contenuto");
                    // crea l'oggetto Commento con il costruttore corretto
                    Commento comm = new Commento(giudice, documento, didascalia);
                    commenti.add(comm);
                }
            }
        }

        return commenti;
    }



}

