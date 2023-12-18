<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1,shink-to-fit=no">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap/bootstrap.min.css" type="text/css">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">

        <!-- Datatables CSS -->
        <link rel="stylesheet" href="datatables/dataTables.bootstrap4.min.css" type="text/css">
        <link rel="stylesheet" href="datatables/jquery.dataTables.min.css" type="text/css">

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
                    <form action="gerenciarMenuPerfil" method="POST">
                        <h3 class="text-center mt-3">Cadastro de Menu e Perfil</h3>
                        <input type="hidden" name="idPerfil"
                               value="${perfil.idPerfil}">
                    
                        <div class="form-group row offset-md-3 mt-5">
                            <label class="col-md-2">Nome</label>
                            <div class="col-md-5">
                                <input type="text" name="nome"
                                    class="form-control" readonly
                                    value="${perfil.nome}">
                            </div>
                        </div>

                        <div class="form-group row offset-md-3 mt-3">
                            <label class="col-md-2">Menu</label>
                            <div class="col-md-5">
                                <select class="form-control" name="idMenu">
                                    <option value="">Escolha um menu</option>
                                    <c:forEach var="menu" items="${perfil.menusNaoVinculados}">
                                        <option value="${menu.idMenu}">${menu.nome}</option>
                                    </c:forEach>
                                </select>
                            </div>    
                        </div>

                        <div class="d-md-flex justify-content-md-end mr-3">
                            <button class="btn btn-primary btn-sm mr-4">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#ffffff" class="bi bi-floppy2-fill" viewBox="0 0 16 16">
                                    <path d="M12 2h-2v3h2V2Z"/>
                                    <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v13A1.5 1.5 0 0 0 1.5 16h13a1.5 1.5 0 0 0 1.5-1.5V2.914a1.5 1.5 0 0 0-.44-1.06L14.147.439A1.5 1.5 0 0 0 13.086 0H1.5ZM4 6a1 1 0 0 1-1-1V1h10v4a1 1 0 0 1-1 1H4ZM3 9h10a1 1 0 0 1 1 1v5H2v-5a1 1 0 0 1 1-1Z"/>
                                  </svg> Vincular
                            </button>
                            
                            <a href="gerenciarPerfil?acao=listar"
                            class="btn btn-info btn-sm"
                            role="button">
                                <i class="bi bi-person-video2"></i> Listar Perfis
                            </a>
                        </div>
                    </form>
                    
                    <div class="container mt-5">
                        <table class="table table-hover table-striped"
                               id="listarMenuPerfil">
                            <thead>
                                <tr>
                                    <th>Código</th>
                                    <th>Nome</th>
                                    <th>Link</th>
                                    <th>Ícone</th>
                                    <th>Exibir</th>
                                    <th>Ação</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${perfil.menusVinculados}">
                                <tr>
                                    <td>${m.idMenu}</td>
                                    <td>${m.nome}</td>
                                    <td>${m.link}</td>
                                    <td><i class="${m.icone}"></i></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.exibir == 1}">
                                                Sim
                                            </c:when>
                                            <c:otherwise>
                                                Não
                                            </c:otherwise>
                                        </c:choose> 
                                    </td>
                                    <td>
                                        <button class="btn btn-danger btn-sm"
                                                onclick="confirmDesvincular('${m.idMenu}',
                                                                            '${m.nome}',
                                                                            '${perfil.idPerfil}')">
                                            <i class="bi bi-x-lg"></i> Desvincular
                                        </button>
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>              
                    </div>
                </div>
            </div>
        </div>

        <!-- JQuery JS -->
        <script src="jquery/jquery-3.6.0.min.js"></script>
        <!-- JQuery Datatables -->
        <script src="datatables/jquery.dataTables.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="bootstrap/bootstrap.min.js"></script>
        <!-- Bootstrap Datatables -->
        <script src="datatables/dataTables.bootstrap4.min.js"></script>
        <!-- Ações do Menu -->
        <script src="js/menu.js"></script>

        <script type="text/javascript">
            function confirmDesvincular(idMenu, nome, idPerfil) {
                if(confirm("Deseja desvincular o menu " + nome + "?")) {
                    location.href="gerenciarMenuPerfil?acao=desvincular&idMenu=" + idMenu + "&idPerfil=" + idPerfil;
                }
            }
        </script>

        <script>
            $(document).ready(() => {
                $('#listarMenuPerfil').dataTable({
                    "bJQueryUI" : true,
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