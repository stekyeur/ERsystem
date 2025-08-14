<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Acil Servis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <h2>Dashboard</h2>

    <div class="row">
        <div class="col-md-3">
            <div class="card text-white bg-primary mb-3">
                <div class="card-body">
                    <h5 class="card-title">Toplam Hemşire</h5>
                    <h3><%= request.getAttribute("toplamHemsire") %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-success mb-3">
                <div class="card-body">
                    <h5 class="card-title">Tecrübeli (T3)</h5>
                    <h3><%= request.getAttribute("tecrubeliSayisi") %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-warning mb-3">
                <div class="card-body">
                    <h5 class="card-title">Orta Tecrübeli (T2)</h5>
                    <h3><%= request.getAttribute("ortaTecrubeliSayisi") %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-info mb-3">
                <div class="card-body">
                    <h5 class="card-title">Tecrübesiz (T0)</h5>
                    <h3><%= request.getAttribute("tecrubesizSayisi") %></h3>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h5>Bugünkü İstatistikler
                        <%
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            java.sql.Date bugun = (java.sql.Date) request.getAttribute("bugun");
                        %>
                        (<%= sdf.format(bugun) %>)
                    </h5>
                </div>
                <div class="card-body">
                    <%
                        List<Object[]> istatistikler = (List<Object[]>) request.getAttribute("gunlukIstatistikler");
                        if (istatistikler != null && !istatistikler.isEmpty()) {
                    %>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Hemşire Kategorisi</th>
                            <th>İşlem</th>
                            <th>Tip</th>
                            <th>Kritiklik</th>
                            <th>Toplam İşlem</th>
                            <th>Ortalama Süre (dk)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Object[] row : istatistikler) { %>
                        <tr>
                            <td><%= row[0] %></td>
                            <td><%= row[1] %></td>
                            <td>
                                        <span class="badge bg-<%= "DIRECT".equals(row[2]) ? "primary" : "secondary" %>">
                                            <%= row[2] %>
                                        </span>
                            </td>
                            <td>
                                        <span class="badge bg-<%= "CRITICAL".equals(row[3]) ? "danger" : "success" %>">
                                            <%= row[3] %>
                                        </span>
                            </td>
                            <td><%= row[4] %></td>
                            <td><%= String.format("%.1f", row[5]) %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p class="text-muted">Bugün için henüz işlem kaydı bulunmuyor.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
