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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Perfil;

@WebServlet(name = "GerenciarMenuPerfil", 
            urlPatterns = {"/gerenciarMenuPerfil"})
public class GerenciarMenuPerfil extends HttpServlet {
    PrintWriter out = null;
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        out = response.getWriter();
        String acao = request.getParameter("acao"),
               idPerfil = request.getParameter("idPerfil"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("IdPerfil: " + idPerfil);
        
        Perfil perfil = new Perfil();
        PerfilDAO perfilDAO = new PerfilDAO();
        
        try {
            switch (acao) {
                case "vincular" -> {
                    // redireciona para o "post"
                    perfil = perfilDAO.getCarregarPorId(
                        Integer.parseInt(idPerfil));

                    if (perfil.getIdPerfil() > 0) {
                        RequestDispatcher dispatcher = getServletContext()
                            .getRequestDispatcher(
                                "/cadastrarMenuPerfil.jsp");
                        request.setAttribute("perfil", perfil);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Perfil não encontrado na base de dados!";
                }
                case "desvincular" -> {
                    String idMenu = request.getParameter("idMenu");

                    if (idMenu.isEmpty() || idMenu.equals(""))
                        mensagem = "O código do menu deve ser selecionado!";
                    else {
                        if (perfilDAO.desvincular(
                                Integer.parseInt(idMenu), 
                                Integer.parseInt(idPerfil)))
                            mensagem = "Menu desvinculado com sucesso!";
                        else
                            mensagem = "Falha ao desvincular o Menu!";
                    }
                }
                default -> response.sendRedirect("home.jsp");
            } 
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarMenuPerfil?" +
                "acao=vincular&idPerfil=" + idPerfil + "';" +
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
        String idMenu = request.getParameter("idMenu"),
               idPerfil = request.getParameter("idPerfil"),
               mensagem = "";
        
        System.out.println("IdMenu: " + idMenu);
        System.out.println("IdPerfil: " + idPerfil);
        
        if (idMenu.equals("") || idPerfil.equals(""))
            mensagem = "Os campos obrigatórios devem ser preenchidos!";
        else {
            try {
                PerfilDAO perfilDAO = new PerfilDAO();
                
                if (perfilDAO.vincular(Integer.parseInt(idMenu),
                                       Integer.parseInt(idPerfil)))
                    mensagem = "Menu vinculado com sucesso!";
                else
                    mensagem = "Falha ao vincular o Menu!";
            } catch (SQLException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }

        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarMenuPerfil?acao=vincular&idPerfil=" +
                    idPerfil + "';" +
            "</script>"
        );
    }
}
