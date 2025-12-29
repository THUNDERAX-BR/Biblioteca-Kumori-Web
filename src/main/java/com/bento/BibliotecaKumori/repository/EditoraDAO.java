package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.Editora;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EditoraDAO {

    private final DataSource dataSource;

    public EditoraDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Editora> listarGerenciar(String busca) {

        String sql = """
            SELECT id, nome
            FROM editoras
            WHERE 1=1
        """;

        if (busca != null && !busca.isBlank()) {
            sql += " AND (nome LIKE ? OR id = ?)";
        }

        sql += " ORDER BY id";

        List<Editora> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (busca != null && !busca.isBlank()) {
                ps.setString(1, "%" + busca + "%");
                ps.setString(2, busca);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Editora(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar editoras", e);
        }
    }

    public Editora buscarPorId(int id) {

        String sql = "SELECT id, nome FROM editoras WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Editora(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar editora", e);
        }
    }

    public void cadastrar(String nome) {

        String verificar = "SELECT id FROM editoras WHERE nome = ?";
        String inserir = "INSERT INTO editoras(nome) VALUES (?)";

        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(verificar)) {
                ps.setString(1, nome);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    throw new RuntimeException("Editora já existe");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(inserir)) {
                ps.setString(1, nome);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar editora", e);
        }
    }

    public void alterar(int id, String nome) {

        String verificar = "SELECT id FROM editoras WHERE nome = ? AND id <> ?";
        String atualizar = "UPDATE editoras SET nome = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(verificar)) {
                ps.setString(1, nome);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    throw new RuntimeException("Editora já existe");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(atualizar)) {
                ps.setString(1, nome);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar editora", e);
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM editoras WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir editora", e);
        }
    }

    public List<Editora> listarParaComboCadastroLivro(Integer idLivro) {

        List<Editora> lista = new ArrayList<>();
        Integer idAtual = null;

        try (Connection conn = dataSource.getConnection()) {

            if (idLivro != null) {
                String sqlAtual = """
                    SELECT e.id, e.nome
                    FROM editoras e
                    INNER JOIN livros l ON l.editoras_id = e.id
                    WHERE l.id = ?
                """;

                try (PreparedStatement ps = conn.prepareStatement(sqlAtual)) {
                    ps.setInt(1, idLivro);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        idAtual = rs.getInt("id");
                        lista.add(new Editora(idAtual, rs.getString("nome")));
                    }
                }
            }

            String sql = """
                SELECT id, nome
                FROM editoras
                WHERE (? IS NULL OR id <> ?)
                ORDER BY nome
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, idAtual);
                ps.setObject(2, idAtual);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    lista.add(new Editora(
                            rs.getInt("id"),
                            rs.getString("nome")
                    ));
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar editoras para combo", e);
        }
    }
}