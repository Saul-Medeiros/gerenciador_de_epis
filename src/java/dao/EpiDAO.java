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
import model.Epi;

public class EpiDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Epi> getLista() throws SQLException {
        ArrayList<Epi> epis = new ArrayList<>();
        
        sql = "SELECT id, nome, descricao, status " +
              "FROM epi";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Epi epi = new Epi();
            
            epi.setIdEpi(resultSet.getInt("id"));
            epi.setNome(resultSet.getString("nome"));
            epi.setDescricao(resultSet.getString("descricao"));
            epi.setStatus(resultSet.getInt("status"));
            
            epis.add(epi);
        }
        
        ConexaoFactory.close(connection);
        return epis;
    }
    
    public Epi getCarregarPorId(int idEpi) throws SQLException {
        Epi epi = new Epi();
        
        sql = "SELECT id, nome, descricao, status " +
              "FROM epi " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            epi.setIdEpi(resultSet.getInt("id"));
            epi.setNome(resultSet.getString("nome"));
            epi.setDescricao(resultSet.getString("descricao"));
            epi.setStatus(resultSet.getInt("status"));
        }
            
        ConexaoFactory.close(connection);
        return epi;
    }
    
    public boolean gravar(Epi epi) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        if (epi.getIdEpi() == 0) {
            sql = "INSERT INTO epi (nome, descricao, status) " +
                  "VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, epi.getNome());
            preparedStatement.setString(2, epi.getDescricao());
            preparedStatement.setInt(3, epi.getStatus());
        } else {
            sql = "UPDATE epi " +
                  "SET nome = ?, descricao = ?, status = ? " +
                  "WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, epi.getNome());
            preparedStatement.setString(2, epi.getDescricao());
            preparedStatement.setInt(3, epi.getStatus());
            preparedStatement.setInt(4, epi.getIdEpi());
        }
        
        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ativar(int idEpi) throws SQLException {
        sql = "UPDATE epi " +
              "SET status = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desativar(int idEpi) throws SQLException {
        sql = "UPDATE epi " +
              "SET status = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
