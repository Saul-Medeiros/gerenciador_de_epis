<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-md bg-body-tertiary">
    <div class="container-fluid">
        <div class="d-flex justify-content-between d-md-none d-block" style="gap:15px;">
            <button class="btn px-1 py-0 open-btn" style="background:none;">
                <img src="icons/bars-solid-black.svg" style="width:25px;">
            </button>
            <a class="navbar-brand fs-4" href="#"><img src="assets/swissportlogo2.png"></a>
        </div>
    
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
    
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto ml-2 ml-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="home.jsp" style="color: #111;">Home</a>
                </li>
            </ul>
        </div>
    </div>
</nav>