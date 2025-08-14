<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.hospital.model.Rapor" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Raporlarım - Acil Servis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Raporlarım</h2>
        <a href="rapor" class="btn btn-primary">Yeni Rapor Oluştur</a>
    </div>

    <% if (request.getParameter("basari") != null) { %>
    <div class="alert alert-success">
        Rapor oluşturma işlemi başlatıldı. Hazır olduğunda indirme bağlantısı aktif olacaktır.
    </div>
    <% } %>

    <%
        List<Rapor> raporlar = (List<Rapor>) request.getAttribute("raporlar");
        if (raporlar != null && !raporlar.isEmpty()) {
    %>
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Rapor Adı</th>
                        <th>Tip</th>
                        <th>Format</th>
                        <th>Tarih Aralığı</th>
                        <th>Oluşturma Tarihi</th>
                        <th>Durum</th>
                        <th>İşlemler</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        for (Rapor rapor : raporlar) {
                    %>
                    <tr>
                        <td><%= rapor.getRaporAdi() %></td>
                        <td>
                                    <span class="badge bg-secondary">
                                        <%= rapor.getRaporTipi() %>
                                    </span>
                        </td>
                        <td>
                                    <span class="badge bg-info">
                                        <%= rapor.getFormat() %>
                                    </span>
                        </td>
                        <td>
                            <%= sdf.format(rapor.getBaslangicTarihi()) %> -
                            <%= sdf.format(rapor.getBitisTarihi()) %>
                        </td>
                        <td><%= sdfTime.format(rapor.getOlusturmaTarihi()) %></td>
                        <td>
                            <%
                                String durumClass = "secondary";
                                if ("HAZIR".equals(rapor.getDurum())) durumClass = "success";
                                else if ("HATA".equals(rapor.getDurum())) durumClass = "danger";
                                else if ("OLUSTURULUYOR".equals(rapor.getDurum())) durumClass = "warning";
                            %>
                            <span class="badge bg-<%= durumClass %>">
                                        <%= rapor.getDurum() %>
                                    </span>
                        </td>
                        <td>
                            <% if ("HAZIR".equals(rapor.getDurum())) { %>
                            <a href="rapor?action=indir&id=<%= rapor.getId() %>" class="btn btn-sm btn-success">
                                İndir
                            </a>
                            <% } else if ("OLUSTURULUYOR".equals(rapor.getDurum())) { %>
                            <button class="btn btn-sm btn-warning" disabled>
                                Hazırlanıyor...
                            </button>
                            <% } else { %>
                            <span class="text-muted">-</span>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <% } else { %>
    <div class="alert alert-info">
        Henüz rapor oluşturmamışsınız.
    </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Sayfa yenileme için otomatik refresh (raporlar hazırlanırken)
    setTimeout(() => {
        if (document.querySelector('.badge.bg-warning')) {
            location.reload();
        }
    }, 30000); // 30 saniyede bir kontrol et
</script>
</body>
</html>
