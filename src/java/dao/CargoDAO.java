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
import model.Cargo;
import model.Epi;

/**
 * Esta classe foi criada apenas para servir como auxiliar da
 * classe de persistência "FuncionarioDAO"
 */
public class CargoDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public Cargo getCarregarPorId(int idCargo) throws SQLException {
        Cargo cargo = new Cargo();
        connection = ConexaoFactory.conectar();
        
        sql = "SELECT id, nome, funcionarios_vinculados " +
              "FROM cargo " +
              "WHERE id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idCargo);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            cargo.setIdCargo(resultSet.getInt("id"));
            cargo.setNome(resultSet.getString("nome"));
            cargo.setFuncionariosVinculados(
                resultSet.getInt(
                    "funcionarios_vinculados"));
            
            // epis vinculados
            cargo.setEpisVinculados(
                episVinculadosPorCargo(idCargo));
            
            // epis não vinculados
            cargo.setEpisNaoVinculados(
                episNaoVinculadosPorCargo(idCargo));
        }
        
        /* para caso o registro não existir, o usuário vai ser informado para 
         * seu superior (adminitrador) informar um novo cargo no banco de
         * dados, para assim o registro ser visível no select da página de
         * cadastro.
         */
        
        ConexaoFactory.close(connection);
        return cargo;
    }
    
    /** 
     * Este método é responsável por realizar a inserção no banco de dados e
     * logo em seguida, a consulta do mesmo, retornando o id e servindo também
     * como complemento do método "getCarregarPorId".O Administrador do sistema
     * também consegue inserir um cargo a parte, para o técnico poder usar para
     * cadastrar um novo funcionário.
     *
     * @param nomeCargo
     * @return true
     * @throws java.sql.SQLException
     */
    public boolean registrar(String nomeCargo) throws SQLException {
        connection = ConexaoFactory.conectar();
        
        sql = "INSERT INTO cargo (nome, funcionarios_vinculados) " +
              "VALUES (?, 0)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nomeCargo);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    /**
     * Este método será executado toda vez que for inserido um novo 
     * registro em funcionário, pois, dependendo do cargo em que ele 
     * for registrado, vai contabilizar +1 na coluna funcionários_vinculados,
     * referente ao cargo.
     * 
     * @param idCargo
     * @throws java.sql.SQLException
     */
    protected void adicionarFuncionarioVinculado(int idCargo)
            throws SQLException {
        connection = ConexaoFactory.conectar();
        
        System.out.println("adiciona primeira vinculacao");
        /* a condição imposta evita que a coluna "funcionarios_vinculados" 
         * tenha um valor maior que o total de funcionários existentes no 
         * sistema.
         */
        sql = "UPDATE cargo " +
              "SET funcionarios_vinculados = funcionarios_vinculados + 1 " +
              "WHERE id = ? AND funcionarios_vinculados < ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idCargo);
        preparedStatement.setInt(2, contabilizarFuncionarios());
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
    }
    
    /**
     * Este método contabilizará todos os funcionários existentes
     */
    private int contabilizarFuncionarios() throws SQLException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        return funcionarioDAO.getLista().size();
    }
    
    /**
     * Este método será executado toda vez que for feita uma atualização em um 
     * registro em funcionário, pois, dependendo do cargo em que ele 
     * for registrado, vai contabilizar -1 na coluna funcionários_vinculados,
     * referente ao cargo.
     * 
     * @param idCargo
     * @throws java.sql.SQLException
     */
    protected void removerFuncionarioVinculado(int idCargo)
            throws SQLException {
        connection = ConexaoFactory.conectar();
        
        /* a condição impede que a quantidade de funcionários 
         * vinculados fique negativo
         */
        sql = "UPDATE cargo " +
              "SET funcionarios_vinculados = funcionarios_vinculados - 1 " +
              "WHERE id = ? AND funcionarios_vinculados > 0";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idCargo);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
    }
    
    public ArrayList<Cargo> getLista() throws SQLException {
        ArrayList<Cargo> cargos = new ArrayList<>();
        
        sql = "SELECT id, nome, funcionarios_vinculados " +
              "FROM cargo";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Cargo cargo = new Cargo();
            
            cargo.setIdCargo(resultSet.getInt("id"));
            cargo.setNome(resultSet.getString("nome"));
            cargo.setFuncionariosVinculados(
                resultSet
                    .getInt("funcionarios_vinculados"));
            
            cargos.add(cargo);
        }
        
        ConexaoFactory.close(connection);
        return cargos;
    }
    
    public ArrayList<Epi> episVinculadosPorCargo(int idCargo)
            throws SQLException {
        ArrayList<Epi> epis = new ArrayList<>();
        
        sql = "SELECT e.id, e.nome, e.descricao, e.status " +
              "FROM epi e " +
              "INNER JOIN cargo_epi ce " +
              "ON e.id = ce.id_epi " +
              "WHERE ce.id_cargo = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idCargo);
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
    
    public ArrayList<Epi> episNaoVinculadosPorCargo(int idCargo)
            throws SQLException {
        ArrayList<Epi> epis = new ArrayList<>();
        
        sql = "SELECT e.id, e.nome, e.descricao, e.status " +
              "FROM epi e " +
              "WHERE e.id NOT IN(" +
                  "SELECT ce.id_epi " +
                  "FROM cargo_epi ce " +
                  "WHERE ce.id_cargo = ?" +
              ")";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idCargo);
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
    
    public boolean vincular(int idEpi, int idCargo) throws SQLException {
        sql = "INSERT INTO cargo_epi (id_epi, id_cargo) " +
              "VALUES (?, ?)";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.setInt(2, idCargo);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desvincular(int idEpi, int idCargo) throws SQLException {
        sql = "DELETE FROM cargo_epi " +
              "WHERE id_epi = ? AND id_cargo = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idEpi);
        preparedStatement.setInt(2, idCargo);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
