<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.hospital.model.Islem" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>İşlem Listele - Acil Servis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <h2>İşlem Listele</h2>

    <% if (request.getAttribute("hata") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("hata") %>
    </div>
    <% } %>

    <div class="card mb-4">
        <div class="card-body">
            <form action="islem-listele" method="post">
                <div class="row">
                    <div class="col-md-4">
                        <label for="baslangicTarihi" class="form-label">Başlangıç Tarihi</label>
                        <input type="date" class="form-control" id="baslangicTarihi" name="baslangicTarihi"
                               value="<%= request.getAttribute("baslangicTarihi") != null ? request.getAttribute("baslangicTarihi") : "" %>" required>
                    </div>
                    <div class="col-md-4">
                        <label for="bitisTarihi" class="form-label">Bitiş Tarihi</label>
                        <input type="date" class="form-control" id="bitisTarihi" name="bitisTarihi"
                               value="<%= request.getAttribute("bitisTarihi") != null ? request.getAttribute("bitisTarihi") : "" %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">&nbsp;</label>
                        <div>
                            <button type="submit" class="btn btn-primary">Listele</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <%
        List<Islem> islemler = (List<Islem>) request.getAttribute("islemler");
        if (islemler != null && !islemler.isEmpty()) {
    %>
    <div class="card">
        <div class="card-header">
            <h5>İşlem Kayıtları (<%= islemler.size() %> adet)</h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Tarih</th>
                        <th>Hemşire</th>
                        <th>Kategori</th>
                        <th>İşlem</th>
                        <th>Birim</th>
                        <th>Süre (dk)</th>
                        <th>Tip</th>
                        <th>Kritiklik</th>
                        <th>Notlar</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        for (Islem islem : islemler) {
                    %>
                    <tr>
                        <td><%= sdf.format(islem.getTarih()) %></td>
                        <td><%= islem.getHemsire() != null ? islem.getHemsire().getAdSoyad() : "" %></td>
                        <td>
                            <% if (islem.getHemsire() != null && islem.getHemsire().getTecrube() != null) { %>
                            <span class="badge bg-info">
                                        <%= islem.getTecrube().getKategoriKodu() %>
                                    </span>
                            <% } %>
                        </td>
                        <td><%= islem.getIslemTuru() != null ? islem.getIslemTuru().getIslemAdi() : "" %></td>
                        <td><%= islem.getBirim() != null ? islem.getBirim().getBirimAdi() : "" %></td>
                        <td><%= islem.getSureDakika() %></td>
                        <td>
                                    <span class="badge bg-<%= "DIRECT".equals(islem.getIslemTipi()) ? "primary" : "secondary" %>">
                                        <%= islem.getIslemTipi() %>
                                    </span>
                        </td>
                        <td>
                                    <span class="badge bg-<%= "CRITICAL".equals(islem.getKritiklik()) ? "danger" : "success" %>">
                                        <%= islem.getKritiklik() %>
                                    </span>
                        </td>
                        <td><%= islem.getNotlar() != null ? islem.getNotlar() : "" %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <% } else if (request.getAttribute("baslangicTarihi") != null) { %>
    <div class="alert alert-info">
        Belirtilen tarih aralığında işlem kaydı bulunamadı.
    </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Varsayılan tarih aralığını son 7 gün olarak set et
    if (!document.getElementById('baslangicTarihi').value) {
        const today = new Date();
        const weekAgo = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);

        document.getElementById('baslangicTarihi').valueAsDate = weekAgo;
        document.getElementById('bitisTarihi').valueAsDate = today;
    }
</script>
</body>
</html>
