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

import dao.AlocacaoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import model.Alocacao;
import model.Funcionario;
import model.Usuario;

@WebServlet(name = "GerenciarAlocacao", urlPatterns = {"/gerenciarAlocacao"})
public class GerenciarAlocacao extends HttpServlet {
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
               idAlocacao = request.getParameter("idAlocacao"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("idAlocação: " + idAlocacao);
        
        AlocacaoDAO alocacaoDAO = new AlocacaoDAO();

        try {
            switch (acao) {
                case "listar" -> {
                    ArrayList<Alocacao> alocacoes = new ArrayList<>();
                    alocacoes = alocacaoDAO.getLista();
                    dispatcher = getServletContext()
                            .getRequestDispatcher("/listarAlocacoes.jsp");
                    request.setAttribute("alocacoes", alocacoes);
                    dispatcher.forward(request, response);
                }
                case "alterarDevolucao" -> {
                    Alocacao alocacao = new Alocacao();
                    alocacao = alocacaoDAO.getCarregarPorId(
                        Integer.parseInt(idAlocacao));
                    
                    if (alocacao.getIdAlocacao() > 0) {
                        dispatcher = getServletContext()
                            .getRequestDispatcher(
                                    "/cadastrarAlocacao.jsp");
                        request.setAttribute("alocacao", alocacao);
                        dispatcher.forward(request, response);
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
                "location.href='gerenciarAlocacao?acao=listar';" +
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
        
        String idAlocacao = request.getParameter("idAlocacao"),
               idUsuario = request.getParameter("idUsuario"),
               dataEntrega = request.getParameter("dataEntrega"),
               dataDevolucao = request.getParameter("dataDevolucao"),
               idFuncionario = request.getParameter("idFuncionario"),
               mensagem = "";
        
        /* Mostra os dados inseridos pelo usuário antes de 
         * registrar no banco de dados.
         */
        System.out.println("IdAlocacao: " + idAlocacao);
        System.out.println("IdUsuario: " + idUsuario);
        System.out.println("DataEntrega: " + dataEntrega);
        System.out.println("DataDevolucao: " + dataDevolucao);
        System.out.println("IdFuncionario: " + idFuncionario);
        
        Alocacao alocacao = new Alocacao();
        AlocacaoDAO alocacaoDAO = new AlocacaoDAO();
        
        HttpSession sessao = request.getSession();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        // idAlocacao
        if (!idAlocacao.isEmpty()) {
            try {
                alocacao.setIdAlocacao(
                        Integer.parseInt(idAlocacao));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        /* Esta condição provavelmente será registrada 
         * automaticamente pelo id do usuário logado
         * o input neste caso será do tipo "hidden"
         */
        // idUsuario
        Usuario usuario = new Usuario();
        if (idUsuario.isEmpty() || idUsuario.equals("")) {
            sessao.setAttribute(
                    "msg", 
                    "Informe o Usuário que registrou a Alocação!");
            exibirMensagem(request, response);
        } else {
            try {
                usuario.setIdUsuario(Integer.parseInt(idUsuario));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
            
            // Associação entre o objeto Usuario e o objeto Alocacao
            alocacao.setUsuario(usuario);
        }
        /* ------------------------------------------ */
        
        // dataEntrega
        if (dataEntrega.isEmpty() || dataEntrega.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe a data de entrega da Alocação!");
            exibirMensagem(request, response);
        } else {
            try {
                alocacao.setDataEntrega(
                        df.parse(dataEntrega));
            } catch (ParseException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // dataDevolucao
        if (!dataDevolucao.isEmpty()) {
            try {
                alocacao.setDataDevolucao(
                        df.parse(dataDevolucao));
            } catch (ParseException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }
        
        // idFuncionario
        Funcionario funcionario = new Funcionario();
        if (idFuncionario.isEmpty() || idFuncionario.equals("")) {
            sessao.setAttribute(
                    "msg", "Informe o Funcionário da Alocação!");
            exibirMensagem(request, response);
        } else {
            try {
                funcionario.setIdFuncionario(
                    Integer.parseInt(idFuncionario));
            } catch (NumberFormatException e) {
                mensagem = "Erro: " + e.getMessage();
            }
            
            // Associação entre o objeto Funcionario e o objeto Alocacao
            alocacao.setFuncionario(funcionario);
        }
        
        // Inserção no banco de dados
        try {
            if (alocacaoDAO.registrar(alocacao))
                mensagem = "Alocação salva na base de dados!";
            else
                mensagem = "Falha ao salvar a Alocação na base de dados!";
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
        }
        
        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarAlocacao?acao=listar';" +
            "</script>"
        );
    }
    
    
    /**
     * Responsável por exibir o alerta para o usuário na página de
     * cadastro de alocação (o alerta é estático na página html até
     * uma nova ação do usuário).
     */
    private void exibirMensagem(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        dispatcher = getServletContext()
            .getRequestDispatcher("/cadastrarAlocacao.jsp");
        dispatcher.forward(request, response);
    }
}
