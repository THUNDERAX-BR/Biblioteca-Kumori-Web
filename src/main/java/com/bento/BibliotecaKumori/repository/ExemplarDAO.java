package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.Editora;
import com.bento.BibliotecaKumori.model.Exemplar;
import com.bento.BibliotecaKumori.model.Livro;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExemplarDAO {

    private final DataSource dataSource;

    public ExemplarDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Exemplar> listarExemplaresLivro(int idLivro) {

        String sql = "SELECT id, localizacao FROM exemplares WHERE livros_id = ?";
        List<Exemplar> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivro);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exemplar exemplar = new Exemplar(
                        rs.getInt("id"),
                        rs.getString("localizacao")
                );
                lista.add(exemplar);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os exemplares", e);
        }
    }

    public List<Exemplar> listarGerenciar(String busca) {

        String sql = """
            SELECT e.id, l.titulo, ed.nome AS editora, e.localizacao
            FROM exemplares e
            INNER JOIN livros l ON e.livros_id = l.id
            INNER JOIN editoras ed ON l.editoras_id = ed.id
            WHERE 1=1
        """;

        if (busca != null && !busca.isBlank()) {
            sql += " AND (l.titulo LIKE ? OR e.id = ?)";
        }

        sql += " ORDER BY e.id";

        List<Exemplar> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (busca != null && !busca.isBlank()) {
                ps.setString(1, "%" + busca + "%");
                ps.setString(2, busca);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Editora editora = new Editora();
                editora.setNome(rs.getString("editora"));

                Livro livro = new Livro();
                livro.setTitulo(rs.getString("titulo"));
                livro.setEditora(editora);

                Exemplar exemplar = new Exemplar(
                        rs.getInt("id"),
                        livro,
                        rs.getString("localizacao")
                );

                lista.add(exemplar);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os exemplares", e);
        }
    }

    public Exemplar buscarPorId(int id) {

        String sql = "SELECT localizacao FROM exemplares WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Exemplar exemplar = new Exemplar();
            exemplar.setLocalizacao(rs.getString("localizacao"));
            return exemplar;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao exibir o exemplar", e);
        }
    }

    public int buscarIdLivro(int idExemplar) {

        String sql = "SELECT livros_id FROM exemplares WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idExemplar);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Exemplar não encontrado");
            }

            return rs.getInt("livros_id");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar o livro do exemplar", e);
        }
    }

    public void cadastrar(int idLivro, String localizacao) {

        String sql = "INSERT INTO exemplares (livros_id, localizacao) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivro);
            ps.setString(2, localizacao);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar o exemplar", e);
        }
    }

    public void alterar(int id, int idLivro, String localizacao) {

        String sql = """
            UPDATE exemplares
            SET livros_id = ?, localizacao = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivro);
            ps.setString(2, localizacao);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar o exemplar", e);
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM exemplares WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir exemplar", e);
        }
    }
}