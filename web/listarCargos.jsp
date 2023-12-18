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

        <!-- Datatables CSS -->
        <link rel="stylesheet" href="datatables/dataTables.bootstrap4.min.css" type="text/css">
        <link rel="stylesheet" href="datatables/jquery.dataTables.min.css">

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

                <div class="container">
                    <h3 class="text-center">Listagem de Cargos</h3>
                    <a href="cadastrarCargo.jsp"
                       class="btn btn-primary btn-md mb-3"
                       role = "button">
                       <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#ffffff" class="bi bi-floppy2-fill" viewBox="0 0 16 16">
                        <path d="M12 2h-2v3h2V2Z"/>
                        <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0H1.5ZM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1H4ZM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1Z"/>
                      </svg> Cadastrar Cargo
                    </a>
                    <table class="table table-hover table-striped"
                           id="listarCargos">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Nome</th>
                                <th>Qtd. Funcionarios</th>
                                <th>Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items= "${cargos}" var="c">
                                <tr>
                                    <td>${c.idCargo}</td>
                                    <td>${c.nome}</td>
                                    <td>${c.funcionariosVinculados}</td>
                                    <td>
                                        <a href="gerenciarCargoEpi?acao=vincular&idCargo=${c.idCargo}"
                                        class="btn btn-success btn-sm"
                                        role="button">
                                            <i class="bi bi-box-arrow-in-down"></i> Vincular Epi
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- JQuery JS -->
        <script src="jquery/jquery-3.6.0.min.js"></script>
        <!-- JQuery Datatables -->
        <script src="datatables/jquery.dataTables.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="bootstrap/bootstrap.min.js"></script>
        <!-- Bootstrap DataTables -->
        <script src="datatables/dataTables.bootstrap4.min.js"></script>
        <!-- Ações do Menu -->
        <script src="js/menu.js"></script>
        
        <script>
            $(document).ready(() => {
                $('#listarCargos').dataTable({
                    "bJQueryUI": true,
                    "lengthMenu": [[5, 10, 20, 25, -1], [5, 10, 20, 25, "Todos"]],
                    "oLanguage": {
                        "sProcessing": "Processando",
                        "sLengthMenu": "Mostrar _MENU_ registros",
                        "sZeroRecords": "Não foram encontrados resultados",
                        "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
                        "sInfoEmpty": "Mostrando de 0 até 0 de 0 registros",
                        "sInfoFiltered": "",
                        "sInfoPostFix": "",
                        "sSearch": "Pesquisar",
                        "sUrl": "",
                        "oPaginate": {
                            "sFirst": "Primeiro",
                            "sPrevious": "Anterior",
                            "sNext": "Próximo",
                            "sLast": "Último"
                        } 
                    }
                });
            });
        </script>
    </body>
</html>