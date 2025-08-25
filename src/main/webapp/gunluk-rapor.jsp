<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hospital.dto.IsYukuOzeti" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Günlük Rapor - Acil Servis İş Yükü</title>
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
                    <h2><i class="fas fa-chart-line me-2"></i>Günlük İş Yükü Raporu</h2>
                    <p class="lead text-muted"><i class="fas fa-calendar-alt me-2"></i>Tarih: <%= request.getAttribute("raporTarihi") %></p>
                </div>

                <div class="table-responsive">
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
                            List<IsYukuOzeti> gunlukAnaliz = (List<IsYukuOzeti>) request.getAttribute("gunlukAnaliz");
                            if (gunlukAnaliz != null && !gunlukAnaliz.isEmpty()) {
                                for (IsYukuOzeti ozet : gunlukAnaliz) {
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
                            <td colspan="3" class="text-center text-muted">Bu tarihte veri bulunamadı.</td>
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
