/**
 * MIT License
 *
 * Copyright (c) 2023 Saul Medeiros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dao;

import factory.ConexaoFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Usuario;

public class UsuarioDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Usuario> getLista() throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        sql = "SELECT id, nome, login, senha, status, id_perfil " +
              "FROM usuario";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Usuario usuario = new Usuario();
            PerfilDAO perfilDAO = new PerfilDAO();
            
            usuario.setIdUsuario(resultSet.getInt("id"));
            usuario.setNome(resultSet.getString("nome"));
            usuario.setLogin(resultSet.getString("login"));
            usuario.setSenha(resultSet.getString("senha"));
            usuario.setStatus(resultSet.getInt("status"));
            
            // Associação entre perfil e usuário
            usuario.setPerfil(
                perfilDAO.getCarregarPorId(
                    resultSet.getInt("id_perfil")
                )
            );
            
            usuarios.add(usuario);
        }
        
        ConexaoFactory.close(connection);
        return usuarios;
    }
    
    public Usuario getCarregarPorId(int idUsuario) throws SQLException {
        Usuario usuario = new Usuario();
        
        sql = "SELECT id, nome, login, senha, status, id_perfil " +
              "FROM usuario " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUsuario);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            usuario.setIdUsuario(resultSet.getInt("id"));
            usuario.setNome(resultSet.getString("nome"));
            usuario.setLogin(resultSet.getString("login"));
            usuario.setSenha(resultSet.getString("senha"));
            usuario.setStatus(resultSet.getInt("status"));
            
            // Associação entre perfil e usuário
            PerfilDAO perfilDAO = new PerfilDAO();
            usuario.setPerfil(
                perfilDAO.getCarregarPorId(
                    resultSet.getInt("id_perfil")
                )
            );
        }
        
        ConexaoFactory.close(connection);
        return usuario;
    }
    
    public boolean gravar(Usuario usuario) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        if (usuario.getIdUsuario() == 0) {
            sql = "INSERT INTO usuario " +
                  "(nome, login, senha, status, id_perfil) " +
                  "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getLogin());
            preparedStatement.setString(3, usuario.getSenha());
            preparedStatement.setInt(4, usuario.getStatus());
            preparedStatement.setInt(5, usuario.getPerfil().getIdPerfil());
        } else {
            sql = "UPDATE usuario " +
                  "SET nome = ?, login = ?, senha = ?, status = ?, " +
                  "id_perfil = ? " +
                  "WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getLogin());
            preparedStatement.setString(3, usuario.getSenha());
            preparedStatement.setInt(4, usuario.getStatus());
            preparedStatement.setInt(5, usuario.getPerfil().getIdPerfil());
            preparedStatement.setInt(6, usuario.getIdUsuario());
        }
        
        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ativar(int idUsuario) throws SQLException {
        sql = "UPDATE usuario " +
              "SET status = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUsuario);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desativar(int idUsuario) throws SQLException {
        sql = "UPDATE usuario " +
              "SET status = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idUsuario);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    /**
     * Este método é usado pela interface para validação de usuário.
     * 
     * @param login
     * @return Usuario
     * @throws java.sql.SQLException
     */
    public Usuario recuperarUsuario(String login) throws SQLException {
        Usuario usuario = new Usuario();
        PerfilDAO perfilDAO = new PerfilDAO();
        
        sql = "SELECT id, nome, login, senha, status, id_perfil " +
              "FROM usuario " +
              "WHERE login = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, login);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            usuario.setIdUsuario(resultSet.getInt("id"));
            usuario.setNome(resultSet.getString("nome"));
            usuario.setLogin(resultSet.getString("login"));
            usuario.setSenha(resultSet.getString("senha"));
            usuario.setStatus(resultSet.getInt("status"));
            
            // Associação entre perfil e usuário
            usuario.setPerfil(
                perfilDAO.getCarregarPorId(
                    resultSet.getInt("id_perfil")
                )
            );
        }
        
        ConexaoFactory.close(connection);
        return usuario;
    }
}
