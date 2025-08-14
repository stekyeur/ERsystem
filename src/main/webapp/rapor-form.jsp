<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rapor Oluştur - Acil Servis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <h2>Rapor Oluştur</h2>

    <% if (request.getAttribute("hata") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("hata") %>
    </div>
    <% } %>

    <div class="card">
        <div class="card-body">
            <form action="rapor" method="post">
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="raporAdi" class="form-label">Rapor Adı</label>
                            <input type="text" class="form-control" id="raporAdi" name="raporAdi" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="raporTipi" class="form-label">Rapor Tipi</label>
                            <select class="form-select" id="raporTipi" name="raporTipi" required>
                                <option value="">Seçiniz</option>
                                <option value="GUNLUK">Günlük</option>
                                <option value="AYLIK">Aylık</option>
                                <option value="YILLIK">Yıllık</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="format" class="form-label">Format</label>
                            <select class="form-select" id="format" name="format" required>
                                <option value="">Seçiniz</option>
                                <option value="PDF">PDF</option>
                                <option value="EXCEL">Excel</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="baslangicTarihi" class="form-label">Başlangıç Tarihi</label>
                            <input type="date" class="form-control" id="baslangicTarihi" name="baslangicTarihi" required>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="bitisTarihi" class="form-label">Bitiş Tarihi</label>
                            <input type="date" class="form-control" id="bitisTarihi" name="bitisTarihi" required>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Rapor Oluştur</button>
                <a href="rapor?action=liste" class="btn btn-secondary">Raporlarım</a>
            </form>
        </div>
    </div>

    <div class="mt-4">
        <div class="alert alert-info">
            <h6>Rapor Tipleri Hakkında:</h6>
            <ul class="mb-0">
                <li><strong>Günlük:</strong> Belirtilen tarih aralığındaki günlük istatistikler</li>
                <li><strong>Aylık:</strong> Aylık bazda toplam ve ortalama veriler</li>
                <li><strong>Yıllık:</strong> Yıllık bazda analiz raporları</li>
            </ul>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Varsayılan tarih aralığını son ay olarak set et
    const today = new Date();
    const monthAgo = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());

    document.getElementById('baslangicTarihi').valueAsDate = monthAgo;
    document.getElementById('bitisTarihi').valueAsDate = today;
</script>
</body>
</html>
