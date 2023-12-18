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

import dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Menu;
import model.Usuario;

@WebServlet(name = "GerenciarLogin", urlPatterns = {"/gerenciarLogin"})
public class GerenciarLogin extends HttpServlet {
    private static HttpServletResponse response;
    RequestDispatcher dispatcher = null;
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sessao = request.getSession();
        
        if (sessao.getAttribute("user") != null) {
            sessao.removeAttribute("user");
            sessao.invalidate();
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        // referencia ao atributo estático para ser usado nas páginas web
        GerenciarLogin.response = response;
        
        String login = request.getParameter("login"),
               senha = request.getParameter("senha"),
               mensagem = "";
        Usuario usuario = new Usuario();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        // login
        if (login.isEmpty() || login.equals("")) {
            request.setAttribute("msg", "Informe o login de Usuário!");
            exibirMensagem(request, response);
        }
        
        // senha
        if (senha.isEmpty() || senha.equals("")) {
            request.setAttribute("msg", "Informe a senha de Usuário!");
            exibirMensagem(request, response);
        }
        
        /* verifica no banco de dados se o usuário existe e se as informações 
         * de login e senha coincidem
         */
        try {
            usuario = usuarioDAO.recuperarUsuario(login);
            
            if ((usuario.getIdUsuario() > 0) &&
                    (usuario.getSenha().equals(senha))) {
                HttpSession sessao = request.getSession();
                sessao.setAttribute("user", usuario);
                response.sendRedirect("home.jsp");
            } else
                mensagem = "Login ou senha inválidos!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='index.jsp'" +
            "</script>"
        );
    }
    
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
    
    public static Usuario verificarAcesso(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        GerenciarLogin.response = response;
        Usuario usuario = null;
        
        try {
            HttpSession sessao = request.getSession();
            
            if (sessao.getAttribute("user") == null) {
                System.out.println("teste 1");
                request.setAttribute(
                        "msg", "Usuário não autenticado no sistema!");
                response.sendRedirect("index.jsp");
            } else {
                String uri = request.getRequestURI(),
                       queryString = request.getQueryString();
                
                if (queryString != null)
                    uri += "?" + queryString;
                
                usuario = (Usuario) request.getSession()
                    .getAttribute("user");
                
                if (usuario == null) {
                    System.out.println("teste 2");
                    request.setAttribute(
                        "msg", "Usuário não autenticado no sistema!");
                    
                    response.sendRedirect("index.jsp");
                } else {
                    boolean possuiAcesso = false;
                    
                    for (Menu menu : usuario.getPerfil().getMenusVinculados()) {
                        if (uri.contains(menu.getLink())) {
                            possuiAcesso = true;
                            break;
                        }
                    }
                    
                    if (!possuiAcesso) {
                        request.setAttribute(
                            "msg", "Usuário não autorizado!");
                        response.sendRedirect("index.jsp");
                    } else
                        response.sendRedirect("home.jsp");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        return usuario;
    }
}
