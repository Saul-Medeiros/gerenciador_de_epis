<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="model.Usuario, controller.GerenciarLogin"%>

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