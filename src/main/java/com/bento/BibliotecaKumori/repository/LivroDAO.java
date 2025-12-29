package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroDAO {

    private final DataSource dataSource;

    public LivroDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Livro> listarBuscarLivros(String busca, String anoIni, String anoFim, String categoria) {

        String sql = """
            SELECT l.id, l.titulo,
                   a.nome AS autor,
                   e.nome AS editora
            FROM livros l
            INNER JOIN autores a ON a.id = l.autores_id
            INNER JOIN editoras e ON e.id = l.editoras_id
            LEFT JOIN categorias_livros cl ON cl.livros_id = l.id
            LEFT JOIN categorias c ON c.id = cl.categorias_id
            WHERE 1=1
        """;

        boolean buscaNumerica = false;
        Integer idBusca = null;

        if (busca != null && !busca.isBlank()) {
            try {
                idBusca = Integer.parseInt(busca);
                buscaNumerica = true;
            } catch (NumberFormatException ignored) {
            }

            sql += buscaNumerica
                    ? " AND (l.titulo LIKE ? OR l.id = ?)"
                    : " AND l.titulo LIKE ?";
        }

        if (anoIni != null && !anoIni.isBlank()) {
            sql += " AND l.ano >= ?";
        }

        if (anoFim != null && !anoFim.isBlank()) {
            sql += " AND l.ano <= ?";
        }

        if (categoria != null && !categoria.isBlank()) {
            sql += " AND c.nome = ?";
        }

        sql += " GROUP BY l.id ORDER BY l.titulo";

        List<Livro> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;

            if (busca != null && !busca.isBlank()) {
                ps.setString(i++, "%" + busca + "%");
                if (buscaNumerica) {
                    ps.setInt(i++, idBusca);
                }
            }

            if (anoIni != null && !anoIni.isBlank()) {
                ps.setString(i++, anoIni);
            }

            if (anoFim != null && !anoFim.isBlank()) {
                ps.setString(i++, anoFim);
            }

            if (categoria != null && !categoria.isBlank()) {
                ps.setString(i, categoria);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Autor autor = new Autor();
                autor.setNome(rs.getString("autor"));

                Editora editora = new Editora();
                editora.setNome(rs.getString("editora"));

                lista.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        autor,
                        editora
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros", e);
        }
    }

    public Livro exibirLivro(int id) {

        String sql = """
            SELECT l.titulo, l.ano, l.descricao,
                   a.id AS autor_id, a.nome AS autor,
                   e.id AS editora_id, e.nome AS editora,
                   l.capa
            FROM livros l
            INNER JOIN autores a ON a.id = l.autores_id
            INNER JOIN editoras e ON e.id = l.editoras_id
            WHERE l.id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Livro livro = new Livro();
            livro.setId(id);
            livro.setTitulo(rs.getString("titulo"));
            livro.setAno(rs.getInt("ano"));
            livro.setDescricao(rs.getString("descricao"));
            livro.setCapa(rs.getBytes("capa"));

            Autor autor = new Autor();
            autor.setId(rs.getInt("autor_id"));
            autor.setNome(rs.getString("autor"));

            Editora editora = new Editora();
            editora.setId(rs.getInt("editora_id"));
            editora.setNome(rs.getString("editora"));

            livro.setAutor(autor);
            livro.setEditora(editora);

            return livro;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao exibir livro", e);
        }
    }

    public void cadastrar(Livro livro, int idAutor, int idEditora) {

        String sql = """
            INSERT INTO livros
            (titulo, ano, descricao, autores_id, editoras_id, capa)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livro.getTitulo());
            ps.setInt(2, livro.getAno());
            ps.setString(3, livro.getDescricao());
            ps.setInt(4, idAutor);
            ps.setInt(5, idEditora);
            ps.setBytes(6, livro.getCapa());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int idLivro = rs.getInt(1);

            for (Categoria c : livro.getCategorias()) {
                try (PreparedStatement pcs =
                             conn.prepareStatement(
                                 "INSERT INTO categorias_livros(livros_id, categorias_id) VALUES (?, ?)")) {
                    pcs.setInt(1, idLivro);
                    pcs.setInt(2, c.getId());
                    pcs.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar livro", e);
        }
    }

    public void alterar(int id, Livro livro, int idAutor, int idEditora, boolean alterarCapa) {

        String sql = alterarCapa ?
            """
            UPDATE livros
            SET titulo=?, ano=?, descricao=?, autores_id=?, editoras_id=?, capa=?
            WHERE id=?
            """
            :
            """
            UPDATE livros
            SET titulo=?, ano=?, descricao=?, autores_id=?, editoras_id=?
            WHERE id=?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, livro.getTitulo());
            ps.setInt(2, livro.getAno());
            ps.setString(3, livro.getDescricao());
            ps.setInt(4, idAutor);
            ps.setInt(5, idEditora);

            if (alterarCapa) {
                ps.setBytes(6, livro.getCapa());
                ps.setInt(7, id);
            } else {
                ps.setInt(6, id);
            }

            ps.executeUpdate();

            try (PreparedStatement del =
                         conn.prepareStatement(
                             "DELETE FROM categorias_livros WHERE livros_id=?")) {
                del.setInt(1, id);
                del.executeUpdate();
            }

            for (Categoria c : livro.getCategorias()) {
                try (PreparedStatement ins =
                             conn.prepareStatement(
                                 "INSERT INTO categorias_livros(livros_id, categorias_id) VALUES (?, ?)")) {
                    ins.setInt(1, id);
                    ins.setInt(2, c.getId());
                    ins.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar livro", e);
        }
    }

    public void excluir(int id) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM livros WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir livro", e);
        }
    }
    
    public List<Livro> listarLivrosPorAutor(int idAutor) {

        String sql = """
            SELECT l.id, l.titulo,
                   e.nome AS editora
            FROM livros l
            INNER JOIN editoras e ON e.id = l.editoras_id
            WHERE l.autores_id = ?
            ORDER BY l.titulo
        """;

        List<Livro> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Editora editora = new Editora();
                editora.setNome(rs.getString("editora"));

                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        null,
                        editora
                );

                lista.add(livro);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros do autor", e);
        }
    }
}