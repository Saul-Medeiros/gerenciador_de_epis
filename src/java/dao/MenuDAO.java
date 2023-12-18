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
import model.Menu;

public class MenuDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Menu> getLista() throws SQLException {
        ArrayList<Menu> menus = new ArrayList<>();
        
        sql = "SELECT id, nome, link, icone, exibir, status " +
              "FROM menu";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Menu menu = new Menu();
            
            menu.setIdMenu(resultSet.getInt("id"));
            menu.setNome(resultSet.getString("nome"));
            menu.setLink(resultSet.getString("link"));
            menu.setIcone(resultSet.getString("icone"));
            menu.setExibir(resultSet.getInt("exibir"));
            menu.setStatus(resultSet.getInt("status"));
            
            menus.add(menu);
        }
        
        ConexaoFactory.close(connection);
        return menus;
    }
    
    public Menu getCarregarPorId(int idMenu) throws SQLException {
        Menu menu = new Menu();
        
        sql = "SELECT id, nome, link, icone, exibir, status " +
              "FROM menu " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            menu.setIdMenu(resultSet.getInt("id"));
            menu.setNome(resultSet.getString("nome"));
            menu.setLink(resultSet.getString("link"));
            menu.setIcone(resultSet.getString("icone"));
            menu.setExibir(resultSet.getInt("exibir"));
            menu.setStatus(resultSet.getInt("status"));
        }
        
        ConexaoFactory.close(connection);
        return menu;
    }
    
    public boolean gravar(Menu menu) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        if (menu.getIdMenu() == 0) {
            sql = "INSERT INTO menu (nome, link, icone, exibir, status) " +
                  "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, menu.getNome());
            preparedStatement.setString(2, menu.getLink());
            preparedStatement.setString(3, menu.getIcone());
            preparedStatement.setInt(4, menu.getExibir());
            preparedStatement.setInt(5, menu.getStatus());
        } else {
            sql = "UPDATE menu " +
                  "SET nome = ?, link = ?, icone = ?, exibir = ?, status = ? " +
                  "WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, menu.getNome());
            preparedStatement.setString(2, menu.getLink());
            preparedStatement.setString(3, menu.getIcone());
            preparedStatement.setInt(4, menu.getExibir());
            preparedStatement.setInt(5, menu.getStatus());
            preparedStatement.setInt(6, menu.getIdMenu());
        }
        
        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ativar(int idMenu) throws SQLException {
        sql = "UPDATE menu " +
              "SET status = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desativar(int idMenu) throws SQLException {
        sql = "UPDATE menu " +
              "SET status = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean exibir(int idMenu) throws SQLException {
        sql = "UPDATE menu " +
              "SET exibir = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ocultar(int idMenu) throws SQLException {
        sql = "UPDATE menu " +
              "SET exibir = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idMenu);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
