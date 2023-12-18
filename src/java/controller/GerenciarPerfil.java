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

import dao.PerfilDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Perfil;

@WebServlet(name = "GerenciarPerfil", urlPatterns = {"/gerenciarPerfil"})
public class GerenciarPerfil extends HttpServlet {
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
               idPerfil = request.getParameter("idPerfil"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idPerfil: " + idPerfil);
        
        Perfil perfil = new Perfil();
        PerfilDAO perfilDAO = new PerfilDAO();
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Perfil> perfis = new ArrayList<>();
                    perfis = perfilDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarPerfis.jsp");
                    request.setAttribute("perfis", perfis);
                    dispatcher.forward(request, response);
                }
                case "alterar" -> {
                    perfil = perfilDAO.getCarregarPorId(
                        Integer.parseInt(idPerfil));
                    
                    if (perfil.getIdPerfil() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher("/cadastrarPerfil.jsp");
                        request.setAttribute("perfil", perfil);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Perfil não encontrado na Base de Dados!";
                }
                case "ativar" -> {
                    if (perfilDAO.ativar(Integer.parseInt(idPerfil)))
                        mensagem = "Perfil ativado com sucesso!";
                    else
                        mensagem = "Falha ao ativar o Perfil!";
                }
                case "desativar" -> {
                    if (perfilDAO.desativar(
                            Integer.parseInt(idPerfil)))
                        mensagem = "Perfil desativado com sucesso!";
                    else
                        mensagem = "Falha ao desativar o Perfil!";
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarPerfil?acao=listar';" +
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
        String idPerfil = request.getParameter("idPerfil"),
               nome = request.getParameter("nome"),
               dataCadastro = request.getParameter("dataCadastro"),
               status = request.getParameter("status"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdPerfil: " + idPerfil);
        System.out.println("Nome: " + nome);
        System.out.println("dataCadastro: " + dataCadastro);
        System.out.println("Status: " + status);
        
        Perfil perfil = new Perfil();
        PerfilDAO perfilDAO = new PerfilDAO();
        
        HttpSession sessao = request.getSession();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        // idPerfil
        if (!idPerfil.isEmpty()) {
            try {
                perfil.setIdPerfil(Integer.parseInt(idPerfil));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute("msg", "Informe o nome do Perfil!");
            exibirMensagem(request, response);
        } else
            perfil.setNome(nome);
        
        // dataCadastro
        if (dataCadastro.isEmpty() || dataCadastro.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe a data de cadastro do Perfil!");
            exibirMensagem(request, response);
        } else {
            try {
                perfil.setDataCadastro(
                    df.parse(dataCadastro));
            } catch (ParseException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // status
        if (status.isEmpty() || status.equals("")) {
            sessao.setAttribute(
                "msg", "Informe o status do Perfil!");
            exibirMensagem(request, response);
        } else {
            try {
                perfil.setStatus(Integer.parseInt(status));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // Inserção no banco de dados
        try {
            if (perfilDAO.gravar(perfil))
                mensagem = "Perfil salvo na base de dados!";
            else
                mensagem = "Falha ao salvar o Perfil na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarPerfil?acao=listar';" +
            "</script>"
        );
    }
       
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de perfil (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarPerfil.jsp");
        dispatcher.forward(request, response);
    }
}
