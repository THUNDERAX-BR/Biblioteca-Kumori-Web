package com.bento.BibliotecaKumori.repository;

import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.security.LoginFactory;
import com.bento.BibliotecaKumori.util.Criptografador;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginDAO {

    private final DataSource dataSource;

    public LoginDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Login validarLogin(String login, String senha) {

        if (login.isBlank() || senha.isBlank()) {
            throw new RuntimeException("Os campos Login e Senha devem ser preenchidos.");
        }

        String sql = """
            SELECT id, login, acesso
            FROM logins
            WHERE login = ? AND senha = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, Criptografador.md5(senha));

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Login inválido ou senha incorreta.");
            }

            LoginFactory factory = new LoginFactory();
            Login retorno = factory.getLogin(rs.getString("acesso").charAt(0));

            retorno.setId(rs.getInt("id"));
            retorno.setLogin(rs.getString("login"));

            return retorno;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao validar login", e);
        }
    }

    public List<Login> listarGerenciar(String busca) {

        String sql = """
            SELECT id, login, acesso
            FROM logins
            WHERE 1=1
        """;

        if (busca != null && !busca.isBlank()) {
            sql += " AND (login LIKE ? OR id = ?)";
        }

        sql += " ORDER BY id";

        List<Login> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (busca != null && !busca.isBlank()) {
                ps.setString(1, "%" + busca + "%");
                ps.setString(2, busca);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LoginFactory factory = new LoginFactory();
                Login l = factory.getLogin(rs.getString("acesso").charAt(0));

                l.setId(rs.getInt("id"));
                l.setLogin(rs.getString("login"));
                lista.add(l);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logins", e);
        }
    }

    public void cadastrar(String login, String senha, String acesso) {

        if (login.isBlank() || senha.isBlank() || acesso == null) {
            throw new RuntimeException("Preencha todos os campos.");
        }

        if (senha.length() < 8) {
            throw new RuntimeException("A senha deve ter 8 ou mais caracteres.");
        }

        String verifica = "SELECT id FROM logins WHERE login = ?";
        String insert = "INSERT INTO logins (login, senha, acesso) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(verifica)) {
                ps.setString(1, login);
                if (ps.executeQuery().next()) {
                    throw new IllegalStateException("Login já existe.");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, login);
                ps.setString(2, Criptografador.md5(senha));
                ps.setString(3, acesso);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar login", e);
        }
    }

    public void excluir(int id) {
        if(id == 1){
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM logins WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir login", e);
        }
    }
}