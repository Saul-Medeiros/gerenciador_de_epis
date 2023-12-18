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

import dao.EpiDAO;
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
import model.Epi;

@WebServlet(name = "GerenciarEpi", urlPatterns = {"/gerenciarEpi"})
public class GerenciarEpi extends HttpServlet {
    RequestDispatcher dispatcher = null;
    PrintWriter out = null;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        out = response.getWriter();
        String acao = request.getParameter("acao"),
               idEpi = request.getParameter("idEpi"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idEpi: " + idEpi);
        
        Epi epi = new Epi();
        EpiDAO epiDAO = new EpiDAO();
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Epi> epis = new ArrayList<>();
                    epis = epiDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarEpis.jsp");
                    request.setAttribute("epis", epis);
                    dispatcher.forward(request, response);
                }
                case "alterar" -> {
                    epi = epiDAO.getCarregarPorId(
                        Integer.parseInt(idEpi));
                    
                    if (epi.getIdEpi() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher("/cadastrarEpi.jsp");
                        request.setAttribute("epi", epi);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Epi não encontrado na Base de Dados!";
                }
                case "ativar" -> {
                    if (epiDAO.ativar(Integer.parseInt(idEpi)))
                        mensagem = "Epi ativado com sucesso!";
                    else
                        mensagem = "Falha ao ativar o Epi!";
                }
                case "desativar" -> {
                    if (epiDAO.desativar(Integer.parseInt(idEpi)))
                        mensagem = "Epi desativado com sucesso!";
                    else
                        mensagem = "Falha ao desativar o Epi!";
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarEpi?acao=listar';" +
            "</script>"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        out = response.getWriter();
        String idEpi = request.getParameter("idEpi"),
               nome = request.getParameter("nome"),
               descricao = request.getParameter("descricao"),
               status = request.getParameter("status"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdEpi: " + idEpi);
        System.out.println("Nome: " + nome);
        System.out.println("Descrição: " + descricao);
        System.out.println("Status: " + status);
        
        Epi epi = new Epi();
        EpiDAO epiDAO = new EpiDAO();
        
        HttpSession sessao = request.getSession();
        
        // idEpi
        if (!idEpi.isEmpty()) {
            try {
                epi.setIdEpi(Integer.parseInt(idEpi));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute("msg", "Informe o nome do Epi!");
            exibirMensagem(request, response);
        } else
            epi.setNome(nome);
        
        // descricao
        if (!descricao.isEmpty())
            epi.setDescricao(descricao);
        
        // status
        if (status.isEmpty() || status.equals("")) {
            sessao.setAttribute("msg", "Informe o status do Epi!");
            exibirMensagem(request, response);
        } else {
            try {
                epi.setStatus(Integer.parseInt(status));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // Inserção no banco de dados
        try {
            if (epiDAO.gravar(epi))
                mensagem = "Epi salva na base de dados!";
            else
                mensagem = "Falha ao salvar o Epi na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        // exibe a mensagem e redireciona a listagem de epis
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarEpi?acao=listar';" +
            "</script>"
        );
    }
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de epi (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarEpi.jsp");
        dispatcher.forward(request, response);
    }
}
