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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Perfil;
import model.Menu;

public class PerfilDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Perfil> getLista() throws SQLException {
        ArrayList<Perfil> perfis = new ArrayList<>();
        
        sql = "SELECT id, nome, data_cadastro, status " +
              "FROM perfil";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Perfil perfil = new Perfil();
            
            perfil.setIdPerfil(resultSet.getInt("id"));
            perfil.setNome(resultSet.getString("nome"));
            perfil.setDataCadastro(
                resultSet.getDate("data_cadastro"));
            perfil.setStatus(resultSet.getInt("status"));
            
            perfis.add(perfil);
        }
        
        ConexaoFactory.close(connection);
        return perfis;
    }
    
    public Perfil getCarregarPorId(int idPerfil) throws SQLException {
        Perfil perfil = new Perfil();
        
        sql = "SELECT id, nome, data_cadastro, status " +
              "FROM perfil " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPerfil);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            perfil.setIdPerfil(resultSet.getInt("id"));
            perfil.setNome(resultSet.getString("nome"));
            perfil.setDataCadastro(
                resultSet.getDate("data_cadastro"));
            perfil.setStatus(resultSet.getInt("status"));
            
            // menus vinculados
            perfil.setMenusVinculados(
                menusVinculadosPorPerfil(idPerfil));
            
            // menus não vinculados
            perfil.setMenusNaoVinculados(
                menusNaoVinculadosPorPerfil(idPerfil));
        }
        
        ConexaoFactory.close(connection);
        return perfil;
    }
    
    public boolean gravar(Perfil perfil) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        if (perfil.getIdPerfil() == 0) {
            sql = "INSERT INTO perfil (nome, data_cadastro, status) " +
                  "VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, perfil.getNome());
            preparedStatement.setDate(2, 
                new Date(perfil.getDataCadastro().getTime()));
            preparedStatement.setInt(3, perfil.getStatus());
        } else {
            sql = "UPDATE perfil " +
                  "SET nome = ?, data_cadastro = ?, status = ? " +
                  "WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, perfil.getNome());
            preparedStatement.setDate(2, 
                new Date(perfil.getDataCadastro().getTime()));
            preparedStatement.setInt(3, perfil.getStatus());
            preparedStatement.setInt(4, perfil.getIdPerfil());
        }
        
        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ativar(int idPerfil) throws SQLException {
        sql = "UPDATE perfil " +
              "SET status = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPerfil);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desativar(int idPerfil) throws SQLException {
        sql = "UPDATE perfil " +
              "SET status = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPerfil);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public ArrayList<Menu> menusVinculadosPorPerfil(int idPerfil)
            throws SQLException {
        ArrayList<Menu> menus = new ArrayList<>();
        
        sql = "SELECT m.id, m.nome, m.link, m.icone, m.exibir, m.status " +
              "FROM menu_perfil mp " +
              "INNER JOIN menu m " +
              "ON mp.id_menu = m.id " +
              "WHERE mp.id_perfil = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPerfil);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Menu menu = new Menu();
            
            menu.setIdMenu(resultSet.getInt("m.id"));
            menu.setNome(resultSet.getString("m.nome"));
            menu.setLink(resultSet.getString("m.link"));
            menu.setIcone(resultSet.getString("m.icone"));
            menu.setExibir(resultSet.getInt("m.exibir"));
            menu.setStatus(resultSet.getInt("m.status"));
            
            menus.add(menu);
        }
        
        ConexaoFactory.close(connection);
        return menus;
    }
    
    public ArrayList<Menu> menusNaoVinculadosPorPerfil(int idPerfil)
            throws SQLException {
        ArrayList<Menu> menus = new ArrayList<>();
        
        sql = "SELECT m.id, m.nome, m.link, m.icone, m.exibir, m.status " +
              "FROM menu m " +
              "WHERE m.id NOT IN(" +
                  "SELECT mp.id_menu " +
                  "FROM menu_perfil mp " +
                  "WHERE mp.id_perfil = ?" +
              ")";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPerfil);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Menu menu = new Menu();
            
            menu.setIdMenu(resultSet.getInt("m.id"));
            menu.setNome(resultSet.getString("m.nome"));
            menu.setLink(resultSet.getString("m.link"));
            menu.setIcone(resultSet.getString("m.icone"));
            menu.setExibir(resultSet.getInt("m.exibir"));
            menu.setStatus(resultSet.getInt("m.status"));
            
            menus.add(menu);
        }
        
        ConexaoFactory.close(connection);
        return menus;
    }
    
    public boolean vincular(int idMenu, int idPerfil) throws SQLException {
        sql = "INSERT INTO menu_perfil (id_menu, id_perfil) " +
              "VALUES (?, ?)";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.setInt(2, idPerfil);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desvincular(int idMenu, int idPerfil) throws SQLException {
        sql = "DELETE FROM menu_perfil " +
              "WHERE id_menu = ? AND id_perfil = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.setInt(2, idPerfil);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
