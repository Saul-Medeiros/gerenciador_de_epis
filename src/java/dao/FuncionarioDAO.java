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
import model.Funcionario;

public class FuncionarioDAO {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql = "";
    
    public ArrayList<Funcionario> getLista() throws SQLException {
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        
        sql = "SELECT id, nome, id_cargo, setor, status " +
              "FROM funcionario";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            Funcionario funcionario = new Funcionario();
            
            funcionario.setIdFuncionario(
                resultSet.getInt("id"));
            funcionario.setNome(resultSet.getString("nome"));
            
            // Associação entre Cargo e Funcionário
            CargoDAO cargoDAO = new CargoDAO();
            funcionario.setCargo(
                cargoDAO.getCarregarPorId(
                    resultSet.getInt("id_cargo")));
            
            funcionario.setSetor(resultSet.getString("setor"));
            funcionario.setStatus(resultSet.getInt("status"));
            
            funcionarios.add(funcionario);
        }
        
        ConexaoFactory.close(connection);
        return funcionarios;
    }
    
    public Funcionario getCarregarPorId(int idFuncionario) throws SQLException {
        Funcionario funcionario = new Funcionario();
        
        sql = "SELECT id, nome, id_cargo, setor, status " +
              "FROM funcionario " +
              "WHERE id = ?";
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idFuncionario);
        resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            funcionario.setIdFuncionario(
                resultSet.getInt("id"));
            funcionario.setNome(resultSet.getString("nome"));
            
            // Associação entre Cargo e Funcionário
            CargoDAO cargoDAO = new CargoDAO();
            funcionario.setCargo(
                cargoDAO.getCarregarPorId(
                    resultSet.getInt("id_cargo")));
            
            funcionario.setSetor(resultSet.getString("setor"));
            funcionario.setStatus(resultSet.getInt("status"));
        }
        
        ConexaoFactory.close(connection);
        return funcionario;
    }
    
    public boolean gravar(Funcionario funcionario) throws SQLException {
        connection = ConexaoFactory.conectar();
        CargoDAO cargoDAO = new CargoDAO();
        
        if (funcionario.getIdFuncionario() == 0) {
            sql = "INSERT INTO funcionario (nome, id_cargo, setor, status) " +
                  "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, funcionario.getCargo()
                .getIdCargo());
            preparedStatement.setString(3, funcionario.getSetor());
            preparedStatement.setInt(4, funcionario.getStatus());
            
            // adiciona +1 ao cargo referente
            cargoDAO.adicionarFuncionarioVinculado(
                funcionario.getCargo().getIdCargo());
        } else {
            sql = "UPDATE funcionario " +
                  "SET nome = ?, id_cargo = ?, setor = ?, status = ? " +
                  "WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            
            /* aqui é feito uma atualização no registro de funcionario
             * vinculado ao cargo, onde, se o cargo for trocado, é feito 
             * uma subtração da contagem do antigo cargo, e uma soma ao
             * novo cargo (na coluna funcionarios_vinculados).
             */
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            int idAntigoCargo = funcionarioDAO.getCarregarPorId(
                funcionario.getIdFuncionario())
                .getCargo()
                .getIdCargo();
            
            int idNovoCargo = funcionario.getCargo().getIdCargo();
            boolean manteveCargo = idNovoCargo == idAntigoCargo;
            
            if (!manteveCargo) {
                System.out.println("altera vinculacao");
                cargoDAO.removerFuncionarioVinculado(idAntigoCargo);
                cargoDAO.adicionarFuncionarioVinculado(idNovoCargo);
            }
            
            preparedStatement.setString(1, funcionario.getNome());
            preparedStatement.setInt(2, 
                funcionario.getCargo().getIdCargo());
            preparedStatement.setString(3, funcionario.getSetor());
            preparedStatement.setInt(4, funcionario.getStatus());
            preparedStatement.setInt(5, funcionario.getIdFuncionario());
        }
        
        preparedStatement.executeUpdate();
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean ativar(int idFuncionario) throws SQLException {
        sql = "UPDATE funcionario SET status = 1 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idFuncionario);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
    
    public boolean desativar(int idFuncionario) throws SQLException {
        sql = "UPDATE funcionario SET status = 0 " +
              "WHERE id = ?";
        
        connection = ConexaoFactory.conectar();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idFuncionario);
        preparedStatement.executeUpdate();
        
        ConexaoFactory.close(connection);
        return true;
    }
}
