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
package controller;

import dao.CargoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cargo;
/**
 * Controlador acessado somente pelo administrador
 */
@WebServlet(name = "GerenciarCargo", urlPatterns = {"/gerenciarCargo"})
public class GerenciarCargo extends HttpServlet {
    RequestDispatcher dispatcher = null;
    PrintWriter out = null;
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out = response.getWriter();
        String acao = request.getParameter("acao"),
               idCargo = request.getParameter("idCargo"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idCargo: " + idCargo);
        
        CargoDAO cargoDAO = new CargoDAO();
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Cargo> cargos = new ArrayList<>();
                    cargos = cargoDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarCargos.jsp");
                    request.setAttribute("cargos", cargos);
                    dispatcher.forward(request, response);
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.print(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarCargo?acao=listar';" +
            "</script>"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out = response.getWriter();
        
        /* Neste caso, como não é possível alterar o registro, o id não 
         * é capturado, e a coluna "funcionarios_vinculados" é preenchida 
         * automaticamente pelo sgbd a cada nova inserção de cargo.
         */
        String nome = request.getParameter("nome"),
               mensagem = "";
        
        CargoDAO cargoDAO = new CargoDAO();
        
        HttpSession sessao = request.getSession();
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute("msg", "Informe o nome do Cargo!");
            exibirMensagem(request, response);
        } else {
            // Inserção no banco de dados
            try {
                if (cargoDAO.registrar(nome))
                    mensagem = "Cargo salvo na base de dados!";
                else
                    mensagem = "Falha ao salvar o cargo!";
            } catch (SQLException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
         
        // exibe a mensagem e redireciona a listagem de cargos
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarCargo?acao=listar';" +
            "</script>"
        );
    }
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de cargo (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarCargo.jsp");
        dispatcher.forward(request, response);
    }
}
