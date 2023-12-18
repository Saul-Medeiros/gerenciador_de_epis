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
import model.Alocacao;
import model.Epi;

public class AlocacaoDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Alocacao> getLista() throws SQLException {
        ArrayList<Alocacao> alocacoes = new ArrayList<>();
        
        sql = "SELECT id, id_usuario, data_entrega, data_devolucao, " +
              "id_funcionario " +
              "FROM alocacao";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Alocacao alocacao = new Alocacao();
            
            alocacao.setIdAlocacao(resultSet.getInt("id"));
            alocacao.setDataEntrega(
                resultSet.getDate("data_entrega"));
            alocacao.setDataDevolucao(
                resultSet.getDate("data_devolucao"));
            
            // associação entre funcionario e alocacao
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            alocacao.setFuncionario(
                funcionarioDAO.getCarregarPorId(
                    resultSet.getInt("id_funcionario")
                )
            );
            
            // associação entre usuario e alocacao
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            alocacao.setUsuario(
                usuarioDAO.getCarregarPorId(
                    resultSet.getInt("id_usuario")
                )
            );
            
            alocacoes.add(alocacao);
        }
        
        ConexaoFactory.close(connection);
        return alocacoes;
    }
    
    public Alocacao getCarregarPorId(int idAlocacao) throws SQLException {
        Alocacao alocacao = new Alocacao();
        
        sql = "SELECT id, id_usuario, data_entrega, data_devolucao, " +
              "id_funcionario " +
              "FROM alocacao " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idAlocacao);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            alocacao.setIdAlocacao(resultSet.getInt("id"));
            alocacao.setDataEntrega(
                resultSet.getDate("data_entrega"));
            alocacao.setDataDevolucao(
                resultSet.getDate("data_devolucao"));
            
            // associação entre funcionario e alocacao
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            alocacao.setFuncionario(
                funcionarioDAO.getCarregarPorId(
                    resultSet.getInt("id_funcionario")
                )
            );
            
            // associação entre usuario e alocacao
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            alocacao.setUsuario(
                usuarioDAO.getCarregarPorId(
                    resultSet.getInt("id_usuario")
                )
            );
            
            // epis alocadas
            alocacao.setEpisAlocadas(
                episVinculadosPorAlocacao(idAlocacao));
            
            // epis não alocadas
            alocacao.setEpisNaoAlocadas(
                episNaoVinculadosPorAlocacao(idAlocacao));
        }
        
        ConexaoFactory.close(connection);
        return alocacao;
    }
    
    public boolean registrar(Alocacao alocacao) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        sql = "INSERT INTO alocacao " +
            "(id_usuario, data_entrega, data_devolucao, " +
            "id_funcionario) " +
            "VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 
            alocacao.getUsuario().getIdUsuario());
        preparedStatement.setDate(2, 
            new Date(alocacao.getDataEntrega().getTime()));
        
        /* evita erro por NullPointerException, fazendo com que o 
         * registro se não existir, seja nulo.
         */
        try {
            preparedStatement.setDate(3, 
                new Date(alocacao.getDataDevolucao().getTime()));
        } catch (NullPointerException e) {
            preparedStatement.setDate(3, null);
        }
        
        preparedStatement.setInt(4, 
            alocacao.getFuncionario().getIdFuncionario());

        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public ArrayList<Epi> episVinculadosPorAlocacao(int idAlocacao)
            throws SQLException {
        ArrayList<Epi> epis = new ArrayList<>();
        
        sql = "SELECT e.id, e.nome, e.descricao, e.status " +
              "FROM epi e " +
              "INNER JOIN alocacao_epi ae " +
              "ON e.id = ae.id_epi " +
              "WHERE ae.id_alocacao = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idAlocacao);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Epi epi = new Epi();
            
            epi.setIdEpi(resultSet.getInt("e.id"));
            epi.setNome(resultSet.getString("e.nome"));
            epi.setDescricao(
                resultSet.getString("e.descricao"));
            epi.setStatus(resultSet.getInt("e.status"));
            
            epis.add(epi);
        }
        
        ConexaoFactory.close(connection);
        return epis;
    }
    
    public ArrayList<Epi> episNaoVinculadosPorAlocacao(int idAlocacao)
            throws SQLException {
        ArrayList<Epi> epis = new ArrayList<>();
        
        /* Seleciona as epis que estão vinculadas ao cargo do funcionário
         * correspondente a alocação, e também seleciona as epis ativas 
         * no sistema (possuem disponibilidade).
         */
        sql = "SELECT e.id, e.nome, e.descricao, e.status " +
              "FROM epi e " +
              "WHERE e.id NOT IN(" +
                  "SELECT ae.id_epi " +
                  "FROM alocacao_epi ae " +
                  "WHERE ae.id_alocacao = ?" +
              ")" +
              "AND e.id IN(" +
                  "SELECT ce.id_epi " +
                  "FROM cargo_epi ce " +
                  "INNER JOIN cargo c " +
                  "ON c.id = ce.id_cargo " +
                  "INNER JOIN funcionario f " +
                  "ON f.id_cargo = c.id " +
                  "INNER JOIN alocacao a " +
                  "ON a.id_funcionario = f.id " +
                  "WHERE a.id = ?" +
              ")" +
              "AND e.status = 1";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idAlocacao);
        preparedStatement.setInt(2, idAlocacao);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Epi epi = new Epi();
            
            epi.setIdEpi(resultSet.getInt("e.id"));
            epi.setNome(resultSet.getString("e.nome"));
            epi.setDescricao(
                resultSet.getString("e.descricao"));
            epi.setStatus(resultSet.getInt("e.status"));
            
            epis.add(epi);
        }
        
        ConexaoFactory.close(connection);
        return epis;
    }
    
    public boolean alocar(int idEpi, int idAlocacao) throws SQLException {
        sql = "INSERT INTO alocacao_epi (id_epi, id_alocacao) " +
              "VALUES (?, ?)";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.setInt(2, idAlocacao);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desalocar(int idEpi, int idAlocacao) throws SQLException {
        sql = "DELETE FROM alocacao_epi " +
              "WHERE id_epi = ? AND id_alocacao = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.setInt(2, idAlocacao);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
