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

import dao.MenuDAO;
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
import model.Menu;

@WebServlet(name = "GerenciarMenu", urlPatterns = {"/gerenciarMenu"})
public class GerenciarMenu extends HttpServlet {
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
               idMenu = request.getParameter("idMenu"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idMenu: " + idMenu);
        
        Menu menu = new Menu();
        MenuDAO menuDAO = new MenuDAO();
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Menu> menus = new ArrayList<>();
                    menus = menuDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarMenus.jsp");
                    request.setAttribute("menus", menus);
                    dispatcher.forward(request, response);
                }
                case "alterar" -> {
                    menu = menuDAO.getCarregarPorId(
                        Integer.parseInt(idMenu));
                    
                    if (menu.getIdMenu() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher("/cadastrarMenu.jsp");
                        request.setAttribute("menu", menu);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Menu não encontrado na Base de Dados!";
                }
                case "ativar" -> {
                    if (menuDAO.ativar(Integer.parseInt(idMenu)))
                        mensagem = "Menu ativado com sucesso!";
                    else
                        mensagem = "Falha ao ativar o Menu!";
                }
                case "desativar" -> {
                    if (menuDAO.desativar(Integer.parseInt(idMenu)))
                        mensagem = "Menu desativado com sucesso!";
                    else
                        mensagem = "Falha ao desativar o Menu!";
                }
                case "exibir" -> {
                    if (menuDAO.exibir(Integer.parseInt(idMenu)))
                        mensagem = "Menu agora está visível!";
                    else
                        mensagem = "Falha ao exibir o Menu!";
                }
                case "ocultar" -> {
                    if (menuDAO.ocultar(Integer.parseInt(idMenu)))
                        mensagem = "Menu foi ocultado!";
                    else
                        mensagem = "Falha ao ocultar o Menu!";
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarMenu?acao=listar';" +
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
        String idMenu = request.getParameter("idMenu"),
               nome = request.getParameter("nome"),
               link = request.getParameter("link"),
               icone = request.getParameter("icone"),
               exibir = request.getParameter("exibir"),
               status = request.getParameter("status"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdMenu: " + idMenu);
        System.out.println("Nome: " + nome);
        System.out.println("Link: " + link);
        System.out.println("Ícone: " + icone);
        System.out.println("Exibir: " + exibir);
        System.out.println("Status: " + status);
        
        Menu menu = new Menu();
        MenuDAO menuDAO = new MenuDAO();
        
        HttpSession sessao = request.getSession();
        
        // idMenu
        if (!idMenu.isEmpty()) {
            try {
                menu.setIdMenu(Integer.parseInt(idMenu));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute("msg", "Informe o nome do Menu!");
            exibirMensagem(request, response);
        } else
            menu.setNome(nome);
        
        // link
        if (link.isEmpty() || link.equals("")) {
            sessao.setAttribute("msg", "Informe o link do Menu!");
            exibirMensagem(request, response);
        } else
            menu.setLink(link);
        
        // icone
        if (icone.isEmpty() || icone.equals("")) {
            sessao.setAttribute("msg", "Informe o ícone do Menu!");
            exibirMensagem(request, response);
        } else
            menu.setIcone(icone);
        
        // exibir
        if (exibir.isEmpty() || exibir.equals("")) {
            sessao.setAttribute(
                "msg", "Informe se o Menu será exibido ou não!");
            exibirMensagem(request, response);
        } else {
            try {
                menu.setExibir(Integer.parseInt(exibir));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // status
        if (status.isEmpty() || status.equals("")) {
            sessao.setAttribute("msg", "Informe o status do Menu!");
        } else {
            try {
                menu.setStatus(Integer.parseInt(status));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // Inserção no banco de dados
        try {
            if (menuDAO.gravar(menu))
                mensagem = "Menu salvo na base de dados!";
            else
                mensagem = "Falha ao salvar o Menu na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        // exibe a mensagem e redireciona a listagem de menus
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarMenu?acao=listar';" +
            "</script>"
        );
    }
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de menu (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarMenu.jsp");
        dispatcher.forward(request, response);
    }
}
