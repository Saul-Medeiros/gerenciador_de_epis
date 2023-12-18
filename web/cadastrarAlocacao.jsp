<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap/bootstrap.min.css" type="text/css">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
        
        <!-- favicon -->
        <link rel="shortcut icon" href="assets/favicon.ico" type="image/x-icon">

        <!-- styles -->
        <link rel="stylesheet" href="css/styles.css">
        
        <title>Swissport</title>
    </head>
    <body>
        <div class="main-container d-flex">
            <div class="sidebar" id="menu">
                <jsp:include page="templates/menu.jsp"></jsp:include>
            </div>
            
            <div id="conteudo" class="bg-background">
                <jsp:include page="templates/navbar.jsp"></jsp:include>

                <%
                    HttpSession sessao = request.getSession();

                    if (sessao.getAttribute("msg") != null) {
                        String msg = (String) sessao.getAttribute("msg");
        
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
                    sessao.removeAttribute("msg");
                %>

                <div class="container">
                    <form action="gerenciarAlocacao" method="post">
                        <h3 class="text-center">Registro de Alocação</h3>
                        <input id="idAlocacao" type="hidden" name="idAlocacao"
                               value="${alocacao.idAlocacao}">

                        <!-- O usuário futuramente será preenchido automaticamete pelo usuario que está usando o sistema -->
                        <div class="form-group row mt-5 offset-md-2">
                            <label class="col-md-3">Usuário</label>
                            <div class="col-md-5">
                                <select class="form-control-sm" id="idUsuario" name="idUsuario" value="user.idUsuario" disabled>
                                    <option value="${user.idUsuario}"
                                        <c:if test="${user.idUsuario == alocacao.usuario.idUsuario}">
                                        selected</c:if>>
                                            ${user.nome}
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group row offset-md-2">
                            <label class="col-md-3">Data de Entrega</label>
                            <div class="col-md-5">
                                <input type="date" id="dataEntrega" name="dataEntrega" class="form-control" 
                                    value="${alocacao.dataEntrega}">
                            </div>
                        </div>

                        <div class="form-group row offset-md-2">
                            <label class="col-md-3">Data de Devolução</label>
                            <div class="col-md-5">
                                <input type="date" name="dataDevolucao" class="form-control" 
                                    value="${alocacao.dataDevolucao}">
                            </div>
                        </div>

                        <div class="form-group row mt-5 offset-md-2">
                            <label class="col-md-3">Funcionário</label>
                            <div class="col-md-5">
                                <select class="form-control-sm" id="idFuncionario" name="idFuncionario">
                                    <option value="">Escolha uma opção</option>
                                    <jsp:useBean class="dao.FuncionarioDAO" id="funcionarioDAO"/>
                                    <c:forEach items="${funcionarioDAO.lista}" var="f">
                                        <option value="${f.idFuncionario}"
                                        <c:if test="${f.idFuncionario == alocacao.funcionario.idFuncionario}">
                                        selected</c:if>>
                                            ${f.nome}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group row offset-md-2">
                            <div class="d-md-flex justify-content-md-end mt-5">
                                <button class="btn btn-primary btn-md mr-3" onclick="activeForm()">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#ffffff" class="bi bi-floppy2-fill" viewBox="0 0 16 16">
                                        <path d="M12 2h-2v3h2V2Z"/>
                                        <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0H1.5ZM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1H4ZM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1Z"/>
                                      </svg> Gravar
                                </button>
                                <a href="gerenciarAlocacao?acao=listar"
                                   class="btn btn-warning btn-md"
                                   role="button">
                                   <i class="bi bi-arrow-left-square"></i> Voltar
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- JQuery JS -->
        <script src="jquery/jquery-3.6.0.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="bootstrap/bootstrap.min.js"></script>
        <!-- Ações do Menu -->
        <script src="js/menu.js"></script>

        <script>
            const idFuncionario = document.getElementById("idFuncionario");
            const idUsuario = document.getElementById("idUsuario");
            const dataEntrega = document.getElementById("dataEntrega");

            // ao carregamento da página, esta ação vai ocorrer
            document.addEventListener("DOMContentLoaded", () => {
                const idAlocacao = document.getElementById("idAlocacao");
                /* confere se o registro é uma alteração, ou seja, alterando 
                 * um registro já existente pegando como referência o id
                 */
                if (idAlocacao.value > 0) {
                    /* configura o input "text" para apenas leitura
                     * e desativa a interação com os selects
                     */
                    dataEntrega.readOnly = true;
                    idFuncionario.disabled = true;
                    idUsuario.disabled = true;
                }
            });

            /**
             * Para evitar erros com o Java, o formulário que antes estava 
             * apenas habilitado para leitura, é, por poucos segundos, 
             * ativado para modificação e, com isso, o formulário é 
             * enviado para o servidor Java.
             */
            function activeForm() {    
                dataEntrega.readOnly = false;
                idFuncionario.disabled = false;
                idUsuario.disabled = false;
            }
        </script>
    </body>
</html>