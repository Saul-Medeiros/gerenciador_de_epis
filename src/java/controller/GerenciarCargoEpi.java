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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cargo;

@WebServlet(name = "GerenciarCargoEpi", urlPatterns = {"/gerenciarCargoEpi"})
public class GerenciarCargoEpi extends HttpServlet {
    PrintWriter out = null;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        out = response.getWriter();
        String acao = request.getParameter("acao"),
               idCargo = request.getParameter("idCargo"),
               mensagem = "";
        
        System.out.println("Ação: " + acao);
        System.out.println("IdCargo: " + idCargo);
        
        Cargo cargo = new Cargo();
        CargoDAO cargoDAO = new CargoDAO();
        
        try {
            switch (acao) {
                case "vincular" -> {
                    cargo = cargoDAO.getCarregarPorId(
                        Integer.parseInt(idCargo));
                    
                    if (cargo.getIdCargo() > 0) {
                        RequestDispatcher dispatcher = getServletContext()
                            .getRequestDispatcher(
                                "/cadastrarCargoEpi.jsp");
                        request.setAttribute("cargo", cargo);
                        dispatcher.forward(request, response);
                    } else
                        mensagem = "Cargo não encontrado na base de dados!";
                }
                case "desvincular" -> {
                    String idEpi = request.getParameter("idEpi");
                    
                    if (idEpi.isEmpty() || idEpi.equals(""))
                        mensagem = "O código do EPI deve ser selecionado!";
                    else {
                        if (cargoDAO.desvincular(
                                Integer.parseInt(idEpi),
                                Integer.parseInt(idCargo)))
                            mensagem = "Epi desvinculado com sucesso!";
                        else
                            mensagem = "Falha ao desvincular o epi!";
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
                "location.href='gerenciarCargoEpi?" +
                "acao=vincular&idCargo=" + idCargo + "';" +
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
               idCargo = request.getParameter("idCargo"),
               mensagem = "";
        
        System.out.println("IdEpi: " + idEpi);
        System.out.println("IdCargo: " + idCargo);
        
        if (idEpi.equals("") || idCargo.equals(""))
            mensagem = "Os campos obrigatórios devem ser preenchidos!";
        else {
            try {
                CargoDAO cargoDAO = new CargoDAO();
                
                if (cargoDAO.vincular(Integer.parseInt(idEpi), 
                                      Integer.parseInt(idCargo)))
                    mensagem = "Epi vinculado com sucesso!";
                else
                    mensagem = "Falha ao vincular o Epi!";
            } catch (SQLException e) {
                mensagem = "Erro: " + e.getMessage();
            }
        }

        out.println(
            "<script type='text/javascript'>" +
                "alert(\"" + mensagem + "\");" +
                "location.href='gerenciarCargoEpi?" +
                "acao=vincular&idCargo=" + idCargo + "';" +
            "</script>"
        );
    }
}
