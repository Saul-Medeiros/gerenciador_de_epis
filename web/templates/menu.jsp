<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.Usuario, controller.GerenciarLogin"%>

<%
    Usuario user = GerenciarLogin.verificarAcesso(request, response);
    request.setAttribute("user", user);
%>

<div class="header-box px-2 pt-3 pb-4 d-flex justify-content-between">
    <h1 class="fs-4">
        <img src="assets/Logo_SwP.png">
    </h1>
    <button class="btn d-md-none d-block close-btn px-1 py-0" style="background:none;">
        <img src="icons/bars-solid-white.svg" style="width:25px;">
    </button>
</div>

<hr class="h-color mx-2">
<ul class="list-unstyled px-2">
    <li class="nav-item" style="background: none;">
        <c:if test="${user != null}">
            <span class="text-white" style="padding:8px 16px;">
                Olá, ${user.nome}!
            </span>
        </c:if>
    </li>
</ul>
<hr class="h-color mx-2">

<ul class="list-unstyled px-2">
    <c:choose>
        <c:when test="${user != null && user.perfil != null}">
            <c:forEach var="menu" items="${user.perfil.menusVinculados}">
                <!-- Verifica se a visualização do menu está disponível para o usuário (geral) -->
                <c:if test="${menu.exibir == 1}">
                    <li class="nav-item">
                        <a class="px-3 py-2 d-block" href="${menu.link}" style="text-decoration:none;">
                            <i class="${menu.icone}"></i>
                                ${menu.nome}
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                // redireciona a página de login quando não indentificado o usuário no sistema
                window.location.href='index.jsp';
            </script>
        </c:otherwise>
    </c:choose>

    <li class="nav-item" style="background: none;">
        <c:if test="${user != null}">
            <p class="nav-link text-white cursor-pointer"
                id="logout" style="text-decoration:none;">
                <i class="bi bi-box-arrow-left"></i>
                Logout
            </p>
        </c:if>
    </li>

    <script type="text/javascript">
        let logout = document.getElementById('logout');

        logout.addEventListener('click', () => {
            if(confirm('Deseja fazer logoff do sistema?')) {
                location.href = 'gerenciarLogin?';
            }
        });
    </script>
</ul>

<hr class="h-color mx-2">