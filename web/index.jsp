<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1,shrink-to-fit=no">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap/bootstrap.min.css" type="text/css">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">

        <!-- favicon -->
        <link rel="shortcut icon" href="assets/favicon.ico" type="image/x-icon">
        
        <!-- styles -->
        <link rel="stylesheet" href="css/login.css" type="text/css">
        
        <title>Swissport</title>
       </head>
    <body>
        <div id="container">
            
            <div id="conteudo" class="bg-background">
                <div class="container">    
                    <div id="login">
                        <img src="assets/Logo_SwP_2.png">
                        <form class="card" action="gerenciarLogin" method="POST">
                            <div class="card-header">
                                <h2>Login</h2>
                            </div>

                            <%
                                if (request.getAttribute("msg") != null) {
                                    String msg = (String) request.getAttribute("msg");
                    
                                    if (msg != null) {
                            %>
                            <div class="alert alert-danger" role="alert">
                                <%= msg %>
                                <button type="button" class="close"
                                        data-dismiss="alert"
                                        aria-label="Close">
                                    &times;
                                </button>
                            </div>
                            <%
                                    }
                                }
                                request.removeAttribute("msg");
                            %>
                            <div class="card-content-area">
                                <label>Usuário</label>
                                <input type="text" name="login"
                                       value=""
                                       class="form-control">
                            </div>
                            <div class="card-content-area">
                                <label>Senha</label>
                                <input type="password" name="senha"
                                       value=""
                                       class="form-control">
                            </div>
                        
                            <div class="card-footer">
                                <button class="submit">
                                    <i class="bi bi-box-arrow-in-right"></i>
                                    Login
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- JQuery -->
        <script src="jquery/jquery-3.6.0.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="bootstrap/bootstrap.min.js"></script>
    </body>
</html>