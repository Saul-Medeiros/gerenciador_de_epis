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

import dao.FuncionarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import model.Cargo;
import model.Funcionario;

@WebServlet(name = "GerenciarFuncionario",
            urlPatterns = {"/gerenciarFuncionario"})
public class GerenciarFuncionario extends HttpServlet {
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
               idFuncionario = request.getParameter("idFuncionario"),
               mensagem = "";
        
        Funcionario funcionario = new Funcionario();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        
        System.out.println("Ação: " + acao);
        System.out.println("idFuncionario: " + idFuncionario);
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Funcionario> funcionarios = new ArrayList<>();
                    funcionarios = funcionarioDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarFuncionarios.jsp");
                    request.setAttribute("funcionarios", funcionarios);
                    dispatcher.forward(request, response);
                }
                case "alterar" -> {
                    funcionario = funcionarioDAO
                        .getCarregarPorId(Integer
                            .parseInt(idFuncionario));
                    
                    if (funcionario.getIdFuncionario() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher(
                                    "/cadastrarFuncionario.jsp");
                        request.setAttribute("funcionario", funcionario);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Funcionário não foi encontrado na " +
                                   "Base de Dados!";
                }
                case "ativar" -> {
                    if (funcionarioDAO.ativar(
                        Integer.parseInt(idFuncionario)))
                        mensagem = "Funcionário ativado com sucesso!";
                    else 
                        mensagem = "Falha ao ativar o Fucionário!";
                }
                case "desativar" -> {
                    if (funcionarioDAO.desativar(
                        Integer.parseInt(idFuncionario)))
                        mensagem = "Funcionário desativado com sucesso!";
                    else 
                        mensagem = "Falha ao desativar o Fucionário!";
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarFuncionario?acao=listar';" +
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
        String idFuncionario = request.getParameter("idFuncionario"),
               nome = request.getParameter("nome"),
               idCargo = request.getParameter("idCargo"),
               setor = request.getParameter("setor"),
               status = request.getParameter("status"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdFuncionario: " + idFuncionario);
        System.out.println("Nome: " + nome);
        System.out.println("IdCargo: " + idCargo);
        System.out.println("Setor: " + setor);
        System.out.println("Status: " + status);
        
        Funcionario funcionario = new Funcionario();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        
        HttpSession sessao = request.getSession();
        
        // idFuncionario
        if (!idFuncionario.isEmpty()) {
            try {
                funcionario.setIdFuncionario(
                        Integer.parseInt(idFuncionario));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe o nome do Funcionário!");
            exibirMensagem(request, response);
        } else 
            funcionario.setNome(nome);
        
        // cargo
        Cargo cargo = new Cargo();
        if (idCargo.isEmpty() || nome.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe o cargo do Funcionário!");
            exibirMensagem(request, response);
        } else {
            try {
                cargo.setIdCargo(Integer.parseInt(idCargo));
                funcionario.setCargo(cargo);
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // setor
        if (setor.isEmpty() || setor.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe o setor do Funcionário!");
            exibirMensagem(request, response);
        } else
            funcionario.setSetor(setor);
        
        // status
        if (status.isEmpty() || status.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe o status do Funcionário!");
            exibirMensagem(request, response);
        } else {
            try {
                funcionario.setStatus(Integer.parseInt(status));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // Inserção no banco de dados
        try {
            if (funcionarioDAO.gravar(funcionario))
                mensagem = "Funcionário salvo na base de dados!";
            else
                mensagem = "Falha ao salvar o Funcionário na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        // exibe a mensagem e redireciona a listagem de funcionários
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarFuncionario?acao=listar';" +
            "</script>"
        );
    }
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de funcionário (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarFuncionario.jsp");
        dispatcher.forward(request, response);
    }
}
