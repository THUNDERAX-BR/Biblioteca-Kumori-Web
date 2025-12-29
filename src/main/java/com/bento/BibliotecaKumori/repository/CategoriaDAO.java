package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.Categoria;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriaDAO {

    private final DataSource dataSource;

    public CategoriaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> listarNomesParaFiltro() {

        String sql = """
            SELECT DISTINCT nome
            FROM categorias
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
            throw new RuntimeException("Erro ao listar categorias para filtro", e);
        }
    }

    public List<Categoria> listarParaComboCadastroLivro() {

        String sql = """
            SELECT id, nome
            FROM categorias
            ORDER BY nome
        """;

        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias para cadastro de livro", e);
        }
    }

    public List<Categoria> listarCategoriasDoLivro(int idLivro) {

        String sql = """
            SELECT c.id, c.nome
            FROM categorias c
            INNER JOIN categorias_livros cl ON cl.categorias_id = c.id
            WHERE cl.livros_id = ?
            ORDER BY c.nome
        """;

        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivro);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias do livro", e);
        }
    }

    public Categoria buscarPorId(int id) {

        String sql = """
            SELECT id, nome
            FROM categorias
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria", e);
        }
    }

    public List<Categoria> listarGerenciar(String busca) {

        String sql = """
            SELECT id, nome
            FROM categorias
            WHERE 1=1
        """;

        if (busca != null && !busca.isBlank()) {
            sql += " AND (nome LIKE ? OR CAST(id AS CHAR) = ?)";
        }

        sql += " ORDER BY id";

        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            if (busca != null && !busca.isBlank()) {
                ps.setString(i++, "%" + busca + "%");
                ps.setString(i, busca);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias", e);
        }
    }

    public void cadastrar(String nome) {

        String sql = "INSERT INTO categorias (nome) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria", e);
        }
    }

    public void alterar(int id, String nome) {

        String sql = "UPDATE categorias SET nome = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar categoria", e);
        }
    }

    public void excluir(int id) {

        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria", e);
        }
    }
}