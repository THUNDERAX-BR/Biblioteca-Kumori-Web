package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.model.Autor;
import com.bento.BibliotecaKumori.model.Movimento;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AutorDAO {

    private final DataSource dataSource;

    public AutorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Autor> listarBuscarAutores(String busca, String movimento) {
        String sql = """
            SELECT a.id, a.nome, m.nome AS movimento
            FROM autores a
            INNER JOIN movimentos m ON m.id = a.movimentos_id
            WHERE 1=1
        """;

        boolean buscaNumerica = false;
        Integer idBusca = null;

        if (busca != null && !busca.isBlank()) {
            try {
                idBusca = Integer.parseInt(busca);
                buscaNumerica = true;
            } catch (NumberFormatException ignored) {}

            sql += buscaNumerica
                    ? " AND (a.nome LIKE ? OR a.id = ?)"
                    : " AND a.nome LIKE ?";
        }

        if (movimento != null && !movimento.isBlank()) {
            sql += " AND m.nome = ?";
        }

        sql += " ORDER BY a.nome";

        List<Autor> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;

            if (busca != null && !busca.isBlank()) {
                ps.setString(i++, "%" + busca + "%");
                if (buscaNumerica) {
                    ps.setInt(i++, idBusca);
                }
            }

            if (movimento != null && !movimento.isBlank()) {
                ps.setString(i, movimento);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Movimento mov = new Movimento();
                mov.setNome(rs.getString("movimento"));

                Autor autor = new Autor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        mov
                );
                lista.add(autor);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar autores", e);
        }
    }

    public Autor exibirAutor(int id) {
        String sql = """
            SELECT a.id, a.nome, a.data_nascimento, a.data_falecimento,
                   m.nome AS movimento, a.biografia, a.foto
            FROM autores a
            INNER JOIN movimentos m ON m.id = a.movimentos_id
            WHERE a.id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Autor autor = new Autor();
            Movimento mov = new Movimento();
            mov.setNome(rs.getString("movimento"));

            autor.setId(rs.getInt("id"));
            autor.setMovimento(mov);
            autor.setNome(rs.getString("nome"));
            autor.setBiografia(rs.getString("biografia"));
            autor.setFoto(rs.getBytes("foto"));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Date nasc = rs.getDate("data_nascimento");
            if (nasc != null) {
                autor.setDataNascimento(nasc.toLocalDate().format(fmt));
            }

            Date fal = rs.getDate("data_falecimento");
            if (fal != null) {
                autor.setDataFalecimento(fal.toLocalDate().format(fmt));
            }

            return autor;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao exibir autor", e);
        }
    }

    public void cadastrar(Autor autor, int idMovimento) {
        String sql = """
            INSERT INTO autores
            (nome, data_nascimento, data_falecimento, movimentos_id, biografia, foto)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, autor.getNome());
            ps.setDate(2, Date.valueOf(LocalDate.parse(autor.getDataNascimento(), fmt)));

            if (autor.getDataFalecimento() != null && !autor.getDataFalecimento().isBlank()) {
                ps.setDate(3, Date.valueOf(LocalDate.parse(autor.getDataFalecimento(), fmt)));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setInt(4, idMovimento);
            ps.setString(5, autor.getBiografia());
            ps.setBytes(6, autor.getFoto());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar autor", e);
        }
    }

    public void alterar(int id, Autor autor, int idMovimento, boolean alterarFoto) {
        String sql = alterarFoto ?
            """
            UPDATE autores
            SET nome=?, data_nascimento=?, data_falecimento=?,
                movimentos_id=?, biografia=?, foto=?
            WHERE id=?
            """
            :
            """
            UPDATE autores
            SET nome=?, data_nascimento=?, data_falecimento=?,
                movimentos_id=?, biografia=?
            WHERE id=?
            """;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, autor.getNome());
            ps.setDate(2, Date.valueOf(LocalDate.parse(autor.getDataNascimento(), fmt)));

            if (autor.getDataFalecimento() != null && !autor.getDataFalecimento().isBlank()) {
                ps.setDate(3, Date.valueOf(LocalDate.parse(autor.getDataFalecimento(), fmt)));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setInt(4, idMovimento);
            ps.setString(5, autor.getBiografia());

            if (alterarFoto) {
                ps.setBytes(6, autor.getFoto());
                ps.setInt(7, id);
            } else {
                ps.setInt(6, id);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar autor", e);
        }
    }

    public void excluir(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM autores WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir autor", e);
        }
    }

    public List<Autor> listarParaComboCadastroLivro(Integer idLivro) {
        List<Autor> lista = new ArrayList<>();
        Integer idAutorAtual = null;

        try (Connection conn = dataSource.getConnection()) {

            if (idLivro != null) {
                String sqlAtual = """
                    SELECT a.id, a.nome
                    FROM autores a
                    INNER JOIN livros l ON l.autores_id = a.id
                    WHERE l.id = ?
                """;

                try (PreparedStatement ps = conn.prepareStatement(sqlAtual)) {
                    ps.setInt(1, idLivro);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        idAutorAtual = rs.getInt("id");

                        Autor atual = new Autor();
                        atual.setId(idAutorAtual);
                        atual.setNome(rs.getString("nome"));
                        lista.add(atual);
                    }
                }
            }

            String sql = """
                SELECT id, nome
                FROM autores
                WHERE (? IS NULL OR id <> ?)
                ORDER BY nome
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, idAutorAtual);
                ps.setObject(2, idAutorAtual);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNome(rs.getString("nome"));
                    lista.add(autor);
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar autores para combo de livro", e);
        }
    }
}
