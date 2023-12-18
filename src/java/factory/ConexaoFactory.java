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
package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {
    private static final String URL = 
        "jdbc:mysql://localhost:3306/gestao_epi" +
        "?useTimeZone=true" +
        "&serverTimeZone=UTC" +
        "&useSSL=false";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection conectar() throws SQLException {
        Connection conexao = null;        
        try {
            Class.forName(DRIVER);
            conexao = DriverManager
                .getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            System.out.println("Falha ao registrar o Driver: " +
                e.getMessage());
        }
        return conexao;
    }
    
    public static void close(Connection conexao) throws SQLException {
        if(conexao != null)
            conexao.close();
    }
}
