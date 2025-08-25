<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hospital.dto.HemsireIhtiyaci" %>
<%@ page import="com.hospital.dto.IsYukuOzeti" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Özel Rapor</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
  <style>
    :root {
      --primary-color: #667eea;
      --secondary-color: #764ba2;
      --dark-color: #2c3e50;
    }

    body {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      min-height: 100vh;
      padding-top: 50px;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
    .report-container {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(20px);
      border-radius: 20px;
      box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.18);
      padding: 2rem;
    }
    .page-header {
      color: var(--dark-color);
      border-bottom: 2px solid var(--primary-color);
      padding-bottom: 1rem;
      margin-bottom: 2rem;
    }
    .page-header h2 {
      font-weight: 700;
    }
    .table-responsive {
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
    }
    .table thead {
      background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
      color: white;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="row justify-content-center">
    <div class="col-lg-10">
      <div class="report-container">
        <div class="page-header">
          <h2><i class="fas fa-file-invoice me-2"></i>Özel Rapor</h2>
          <p class="lead text-muted"><i class="fas fa-calendar-alt me-2"></i>Tarih Aralığı: <%= request.getAttribute("baslangic") %> - <%= request.getAttribute("bitis") %></p>
        </div>

        <h4 class="mb-3"><i class="fas fa-chart-bar me-2"></i>İş Yükü Özeti</h4>
        <div class="table-responsive mb-5">
          <table class="table table-striped table-hover">
            <thead>
            <tr>
              <th>Saat Aralığı</th>
              <th>Hasta Sayısı</th>
              <th>Ort. Bekleme Süresi</th>
            </tr>
            </thead>
            <tbody>
            <%
              List<IsYukuOzeti> isYukuAnalizi = (List<IsYukuOzeti>) request.getAttribute("isYukuAnalizi");
              if (isYukuAnalizi != null && !isYukuAnalizi.isEmpty()) {
                for (IsYukuOzeti ozet : isYukuAnalizi) {
            %>
            <tr>
              <td><%= ozet.getSaatAraligi() %></td>
              <td><%= ozet.getHastaSayisi() %></td>
              <td><%= ozet.getOrtalamaBeklemeSuresi() %> dk</td>
            </tr>
            <%
              }
            } else {
            %>
            <tr>
              <td colspan="3" class="text-center text-muted">Belirtilen tarihler için iş yükü verisi bulunamadı.</td>
            </tr>
            <%
              }
            %>
            </tbody>
          </table>
        </div>

        <h4 class="mb-3"><i class="fas fa-user-nurse me-2"></i>Hemşire İhtiyacı Analizi</h4>
        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead>
            <tr>
              <th>Tarih</th>
              <th>Gerekli Hemşire Sayısı</th>
              <th>Mevcut Hemşire Sayısı</th>
            </tr>
            </thead>
            <tbody>
            <%
              List<HemsireIhtiyaci> hemsireAnalizi = (List<HemsireIhtiyaci>) request.getAttribute("hemsireAnalizi");
              if (hemsireAnalizi != null && !hemsireAnalizi.isEmpty()) {
                for (HemsireIhtiyaci ihtiyac : hemsireAnalizi) {
            %>
            <tr>
              <td><%= ihtiyac.getTarih() %></td>
              <td><%= ihtiyac.getGerekliHemsire() %></td>
              <td><%= ihtiyac.getMevcutHemsire() %></td>
            </tr>
            <%
              }
            } else {
            %>
            <tr>
              <td colspan="3" class="text-center text-muted">Belirtilen tarihler için hemşire ihtiyacı verisi bulunamadı.</td>
            </tr>
            <%
              }
            %>
            </tbody>
          </table>
        </div>

        <div class="mt-4 text-center">
          <a href="${pageContext.request.contextPath}/rapor?action=liste" class="btn btn-outline-primary">
            <i class="fas fa-arrow-left me-2"></i>Rapor Listesine Dön
          </a>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
