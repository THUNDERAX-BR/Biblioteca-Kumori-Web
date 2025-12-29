package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.Movimento;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimentoDAO {

    private final DataSource dataSource;

    public MovimentoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> listarFiltroBuscarAutores() {

        String sql = """
            SELECT DISTINCT nome
            FROM movimentos
            ORDER BY nome
        """;

        List<String> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nome"));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentos para filtro", e);
        }
    }

    public List<Movimento> listarGerenciar(String busca) {

        String sql = """
            SELECT id, nome
            FROM movimentos
            WHERE (? IS NULL OR nome LIKE ? OR id = ?)
            ORDER BY id
        """;

        List<Movimento> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (busca == null || busca.isBlank()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setString(1, busca);
                ps.setString(2, "%" + busca + "%");
                ps.setString(3, busca);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Movimento(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentos", e);
        }
    }

    public Movimento buscarPorId(int id) {

        String sql = "SELECT nome FROM movimentos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Movimento movimento = new Movimento();
            movimento.setId(id);
            movimento.setNome(rs.getString("nome"));

            return movimento;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao exibir movimento", e);
        }
    }

    public void cadastrar(String nome) {

        String valida = "SELECT id FROM movimentos WHERE nome = ?";
        String insert = "INSERT INTO movimentos (nome) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psValida = conn.prepareStatement(valida)) {

            psValida.setString(1, nome);
            ResultSet rs = psValida.executeQuery();

            if (rs.next()) {
                throw new RuntimeException("Movimento já existe");
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insert)) {
                psInsert.setString(1, nome);
                psInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar movimento", e);
        }
    }

    public void alterar(int id, String nome) {

        String valida = "SELECT id FROM movimentos WHERE nome = ? AND id <> ?";
        String update = "UPDATE movimentos SET nome = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psValida = conn.prepareStatement(valida)) {

            psValida.setString(1, nome);
            psValida.setInt(2, id);
            ResultSet rs = psValida.executeQuery();

            if (rs.next()) {
                throw new RuntimeException("Movimento já existe");
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(update)) {
                psUpdate.setString(1, nome);
                psUpdate.setInt(2, id);
                psUpdate.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar movimento", e);
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM movimentos WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir movimento", e);
        }
    }

    public List<Movimento> listarParaComboCadastroAutor(Integer idAutor) {

        List<Movimento> lista = new ArrayList<>();
        Integer idAtual = null;

        try (Connection conn = dataSource.getConnection()) {

            if (idAutor != null) {
                String sqlAtual = """
                    SELECT m.id, m.nome
                    FROM movimentos m
                    INNER JOIN autores a ON a.movimentos_id = m.id
                    WHERE a.id = ?
                """;

                try (PreparedStatement ps = conn.prepareStatement(sqlAtual)) {
                    ps.setInt(1, idAutor);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        idAtual = rs.getInt("id");
                        lista.add(new Movimento(idAtual, rs.getString("nome")));
                    }
                }
            }

            String sql = """
                SELECT id, nome
                FROM movimentos
                WHERE (? IS NULL OR id <> ?)
                ORDER BY nome
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, idAtual);
                ps.setObject(2, idAtual);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    lista.add(new Movimento(
                            rs.getInt("id"),
                            rs.getString("nome")
                    ));
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar movimentos para combo", e);
        }
    }
}