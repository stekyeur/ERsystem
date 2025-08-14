<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="../dashboard.jsp">Acil Servis İş Yükü</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="../dashboard.jsp">Dashboard</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="islemDropdown" role="button" data-bs-toggle="dropdown">
                        İşlemler
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="islem-kayit">İşlem Kayıt</a></li>
                        <li><a class="dropdown-item" href="islem-listele">İşlem Listele</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="hemsire">Hemşireler</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="raporDropdown" role="button" data-bs-toggle="dropdown">
                        Raporlar
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="rapor">Rapor Oluştur</a></li>
                        <li><a class="dropdown-item" href="rapor?action=liste">Raporlarım</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <%= session.getAttribute("kullaniciAdi") %>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="logout">Çıkış Yap</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
