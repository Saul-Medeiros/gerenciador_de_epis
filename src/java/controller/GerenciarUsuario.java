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
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Perfil;
import model.Usuario;

@WebServlet(name = "GerenciarUsuario", urlPatterns = {"/gerenciarUsuario"})
public class GerenciarUsuario extends HttpServlet {
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
               idUsuario = request.getParameter("idUsuario"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idUsuario: " + idUsuario);
        
        Usuario usuario = new Usuario();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Usuario> usuarios = new ArrayList<>();
                    usuarios = usuarioDAO.getLista();
                    dispatcher = getServletContext()
                        .getRequestDispatcher("/listarUsuarios.jsp");
                    request.setAttribute("usuarios", usuarios);
                    dispatcher.forward(request, response);
                }
                case "alterar" -> {
                    usuario = usuarioDAO.getCarregarPorId(
                        Integer.parseInt(idUsuario));
                    
                    if (usuario.getIdUsuario() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher("/cadastrarUsuario.jsp");
                        request.setAttribute("usuario", usuario);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Usuário não encontrado na Base de Dados!";
                }
                case "ativar" -> {
                    if (usuarioDAO.ativar(
                            Integer.parseInt(idUsuario)))
                        mensagem = "Usuário ativado com sucesso!";
                    else
                        mensagem = "Falha ao ativar o Usuário!";
                }
                case "desativar" -> {
                    if (usuarioDAO.desativar(
                            Integer.parseInt(idUsuario)))
                        mensagem = "Usuário desativado com sucesso!";
                    else
                        mensagem = "Falha ao desativar o Usuário!";
                }
                default -> response.sendRedirect("home.jsp");
            }
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarUsuario?acao=listar';" +
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
        String idUsuario = request.getParameter("idUsuario"),
               nome = request.getParameter("nome"),
               login = request.getParameter("login"),
               senha = request.getParameter("senha"),
               status = request.getParameter("status"),
               idPerfil = request.getParameter("idPerfil"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdUsuário: " + idUsuario);
        System.out.println("Nome: " + nome);
        System.out.println("Login: " + login);
        System.out.println("Senha: " + senha);
        System.out.println("Status: " + status);
        System.out.println("IdPerfil: " + idPerfil);
        
        Usuario usuario = new Usuario();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        HttpSession sessao = request.getSession();
        
        // idUsuario
        if (!idUsuario.isEmpty()) {
            try {
                usuario.setIdUsuario(Integer.parseInt(idUsuario));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // nome
        if (nome.isEmpty() || nome.equals("")) {
            sessao.setAttribute("msg", "Informe o Nome de Usuário!");
            exibirMensagem(request, response);
        } else
            usuario.setNome(nome);
        
        // login
        if (login.isEmpty() || login.equals("")) {
            sessao.setAttribute(
                "msg", "Informe o Login de Usuário!");
            exibirMensagem(request, response);
        } else
            usuario.setLogin(login);
        
        // senha
        if (senha.isEmpty() || senha.equals("")) {
            sessao.setAttribute(
                "msg", "Informe a Senha de Usuário!");
            exibirMensagem(request, response);
        } else
            usuario.setSenha(senha);
        
        // status
        if (status.isEmpty() || status.equals("")) {
            sessao.setAttribute(
                "msg", "Informe o Status de Usuário!");
            exibirMensagem(request, response);
        } else {
            try {
                usuario.setStatus(Integer.parseInt(status));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // idPerfil
        Perfil perfil = new Perfil();
        if (idPerfil.isEmpty() || idPerfil.equals("")) {
            sessao.setAttribute(
                "msg", "Informe um Perfil válido para o Usuário!");
            exibirMensagem(request, response);
        } else {
            try {
                perfil.setIdPerfil(Integer.parseInt(idPerfil));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
            
            // Associação entre o objeto Perfil e o objeto Usuario
            usuario.setPerfil(perfil);
        }
        
        // Inserção no banco de dados
        try {
            if (usuarioDAO.gravar(usuario))
                mensagem = "Usuário salvo na base de dados!";
            else
                mensagem = "Falha ao salvar o Usuário na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        // exibe a mensagem e redireciona a listagem de usuarios
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarUsuario?acao=listar';" +
            "</script>"
        );
    }
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de usuario (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarUsuario.jsp");
        dispatcher.forward(request, response);
    }
}
