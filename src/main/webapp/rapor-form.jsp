<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rapor Oluştur - Acil Servis İş Yükü</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #667eea;
            --secondary-color: #764ba2;
            --success-color: #38ef7d;
            --danger-color: #ff416c;
            --warning-color: #f093fb;
            --info-color: #4facfe;
            --dark-color: #2c3e50;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding-top: 100px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .report-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.18);
            overflow: hidden;
        }

        .page-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 2rem;
            margin: -2rem -2rem 2rem -2rem;
        }

        .page-header h2 {
            margin: 0;
            font-weight: 700;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        .page-header p {
            margin: 0.5rem 0 0 0;
            opacity: 0.9;
        }

        .form-section {
            background: rgba(255, 255, 255, 0.8);
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            border-left: 4px solid var(--primary-color);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        }

        .form-section h5 {
            color: var(--dark-color);
            font-weight: 600;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
        }

        .form-section h5 i {
            margin-right: 0.5rem;
            color: var(--primary-color);
        }

        .form-control, .form-select {
            border-radius: 10px;
            border: 2px solid #e9ecef;
            padding: 0.75rem 1rem;
            font-size: 0.95rem;
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            border: none;
            border-radius: 10px;
            padding: 0.75rem 2rem;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
            border: none;
            border-radius: 10px;
            padding: 0.75rem 2rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-outline-info {
            border: 2px solid var(--info-color);
            color: var(--info-color);
            border-radius: 10px;
            padding: 0.75rem 1.5rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-outline-info:hover {
            background: var(--info-color);
            border-color: var(--info-color);
        }

        .alert {
            border-radius: 12px;
            border: none;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .alert-success {
            background: linear-gradient(135deg, rgba(56, 239, 125, 0.1), rgba(56, 239, 125, 0.05));
            border-left: 4px solid var(--success-color);
            color: #155724;
        }

        .alert-danger {
            background: linear-gradient(135deg, rgba(255, 65, 108, 0.1), rgba(255, 65, 108, 0.05));
            border-left: 4px solid var(--danger-color);
            color: #721c24;
        }

        .alert-info {
            background: linear-gradient(135deg, rgba(79, 172, 254, 0.1), rgba(79, 172, 254, 0.05));
            border-left: 4px solid var(--info-color);
            color: #0c5460;
        }

        .report-type-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1rem;
            margin-bottom: 1.5rem;
        }

        .report-type-card {
            background: rgba(255, 255, 255, 0.9);
            border-radius: 12px;
            padding: 1.5rem;
            border: 2px solid #e9ecef;
            cursor: pointer;
            transition: all 0.3s ease;
            text-align: center;
        }

        .report-type-card:hover {
            border-color: var(--primary-color);
            transform: translateY(-3px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.2);
        }

        .report-type-card.selected {
            border-color: var(--primary-color);
            background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.05));
        }

        .report-type-card i {
            font-size: 2rem;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
        }

        .report-type-card h6 {
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: var(--dark-color);
        }

        .report-type-card p {
            font-size: 0.85rem;
            color: #6c757d;
            margin: 0;
        }

        .date-range-toggle {
            background: rgba(255, 255, 255, 0.8);
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 1rem;
            border: 1px solid #e9ecef;
        }

        .quick-dates {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
            margin-top: 1rem;
        }

        .quick-date-btn {
            padding: 0.5rem 1rem;
            border: 1px solid var(--primary-color);
            background: transparent;
            color: var(--primary-color);
            border-radius: 20px;
            font-size: 0.85rem;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .quick-date-btn:hover, .quick-date-btn.active {
            background: var(--primary-color);
            color: white;
        }

        .format-selection {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
        }

        .format-option {
            flex: 1;
            background: rgba(255, 255, 255, 0.9);
            border: 2px solid #e9ecef;
            border-radius: 12px;
            padding: 1rem;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .format-option:hover {
            border-color: var(--primary-color);
            transform: translateY(-2px);
        }

        .format-option.selected {
            border-color: var(--primary-color);
            background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.05));
        }

        .format-option i {
            font-size: 1.5rem;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
        }

        @media (max-width: 768px) {
            body {
                padding-top: 80px;
            }

            .report-container {
                margin: 1rem;
                border-radius: 15px;
            }

            .format-selection {
                flex-direction: column;
            }

            .quick-dates {
                justify-content: center;
            }
        }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <div class="report-container p-4">
                <div class="page-header">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2><i class="fas fa-chart-line me-3"></i>Rapor Oluştur</h2>
                            <p>Acil servis iş yükü ve hemşire ihtiyaç analizleri için detaylı raporlar oluşturun</p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/rapor?action=liste" class="btn btn-outline-info">
                                <i class="fas fa-folder-open me-2"></i>Raporlarım
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Başarı/Hata Mesajları -->
                <% if (request.getAttribute("success") != null) { %>
                <div class="alert alert-success">
                    <i class="fas fa-check-circle me-2"></i>
                    <%= request.getAttribute("success") %>
                </div>
                <% } %>

                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <%= request.getAttribute("error") %>
                </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/rapor" method="post" id="reportForm">
                    <!-- Rapor Tipi Seçimi -->
                    <div class="form-section">
                        <h5><i class="fas fa-clipboard-list"></i>Rapor Tipi</h5>
                        <div class="report-type-cards">
                            <div class="report-type-card" onclick="selectReportType('gunluk', this)">
                                <i class="fas fa-calendar-day"></i>
                                <h6>Günlük Rapor</h6>
                                <p>Günlük iş yükü analizi ve detayları</p>
                            </div>
                            <div class="report-type-card" onclick="selectReportType('haftalik', this)">
                                <i class="fas fa-calendar-week"></i>
                                <h6>Haftalık Rapor</h6>
                                <p>Haftalık hemşire ihtiyaç analizi</p>
                            </div>
                            <div class="report-type-card" onclick="selectReportType('aylik', this)">
                                <i class="fas fa-calendar-alt"></i>
                                <h6>Aylık Rapor</h6>
                                <p>Aylık kapsamlı analiz raporları</p>
                            </div>
                            <div class="report-type-card" onclick="selectReportType('ozel', this)">
                                <i class="fas fa-calendar-check"></i>
                                <h6>Özel Tarih Aralığı</h6>
                                <p>Belirlediğiniz tarih aralığı analizi</p>
                            </div>
                        </div>
                        <input type="hidden" name="raporTipi" id="raporTipi" required>
                    </div>

                    <!-- Tarih Seçimi -->
                    <div class="form-section">
                        <h5><i class="fas fa-calendar"></i>Tarih Aralığı</h5>

                        <!-- Hızlı Tarih Seçimi -->
                        <div class="date-range-toggle">
                            <label class="form-label">Hızlı Seçim:</label>
                            <div class="quick-dates">
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('today')">Bugün</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('yesterday')">Dün</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('thisWeek')">Bu Hafta</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('lastWeek')">Geçen Hafta</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('thisMonth')">Bu Ay</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('lastMonth')">Geçen Ay</button>
                                <button type="button" class="quick-date-btn" onclick="setQuickDate('last30Days')">Son 30 Gün</button>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label for="baslangic" class="form-label">Başlangıç Tarihi</label>
                                <input type="date" class="form-control" id="baslangic" name="baslangic" required>
                            </div>
                            <div class="col-md-6">
                                <label for="bitis" class="form-label">Bitiş Tarihi</label>
                                <input type="date" class="form-control" id="bitis" name="bitis" required>
                            </div>
                        </div>
                    </div>

                    <!-- Format Seçimi -->
                    <div class="form-section">
                        <h5><i class="fas fa-file-export"></i>Rapor Formatı</h5>
                        <div class="format-selection">
                            <div class="format-option" onclick="selectFormat('pdf', this)">
                                <i class="fas fa-file-pdf"></i>
                                <h6>PDF Format</h6>
                                <small class="text-muted">Yazdırma için optimize edilmiş</small>
                            </div>
                            <div class="format-option" onclick="selectFormat('excel', this)">
                                <i class="fas fa-file-excel"></i>
                                <h6>Excel Format</h6>
                                <small class="text-muted">Veri analizi için uygun</small>
                            </div>
                        </div>
                        <input type="hidden" name="format" id="format" required>
                    </div>

                    <!-- Rapor Özellikleri -->
                    <div class="form-section" id="reportOptions" style="display: none;">
                        <h5><i class="fas fa-cog"></i>Rapor Seçenekleri</h5>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="includeCharts" name="includeCharts" checked>
                                    <label class="form-check-label" for="includeCharts">
                                        Grafik ve Tablolar Dahil Et
                                    </label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="includeDetails" name="includeDetails" checked>
                                    <label class="form-check-label" for="includeDetails">
                                        Detaylı Analiz Dahil Et
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- İşlem Butonları -->
                    <div class="text-center mt-4">
                        <button type="submit" class="btn btn-primary me-3">
                            <i class="fas fa-file-plus me-2"></i>Raporu Oluştur
                        </button>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Dashboard'a Dön
                        </a>
                    </div>
                </form>

                <!-- Bilgi Kutusu -->
                <div class="alert alert-info mt-4">
                    <h6><i class="fas fa-info-circle me-2"></i>Rapor Hakkında Bilgiler:</h6>
                    <ul class="mb-0">
                        <li><strong>Günlük Rapor:</strong> Seçilen tarihteki detaylı iş yükü analizi</li>
                        <li><strong>Haftalık Rapor:</strong> 7 günlük hemşire ihtiyaç analizi</li>
                        <li><strong>Aylık Rapor:</strong> Aylık kapsamlı istatistik ve trendler</li>
                        <li><strong>Özel Rapor:</strong> Belirlediğiniz tarih aralığında özelleştirilebilir analiz</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Rapor tipi seçimi
    function selectReportType(type, element) {
        // Tüm kartlardan selected sınıfını kaldır
        document.querySelectorAll('.report-type-card').forEach(card => {
            card.classList.remove('selected');
        });

        // Seçilen karta selected sınıfını ekle
        element.classList.add('selected');

        // Hidden input'u güncelle
        document.getElementById('raporTipi').value = type;

        // Rapor seçeneklerini göster
        document.getElementById('reportOptions').style.display = 'block';

        // Tarih alanlarını otomatik ayarla
        adjustDatesForReportType(type);
    }

    // Format seçimi
    function selectFormat(format, element) {
        // Tüm format seçeneklerinden selected sınıfını kaldır
        document.querySelectorAll('.format-option').forEach(option => {
            option.classList.remove('selected');
        });

        // Seçilen formata selected sınıfını ekle
        element.classList.add('selected');

        // Hidden input'u güncelle
        document.getElementById('format').value = format;
    }

    // Hızlı tarih seçimi
    function setQuickDate(period) {
        const today = new Date();
        const baslangic = document.getElementById('baslangic');
        const bitis = document.getElementById('bitis');

        // Tüm butonlardan active sınıfını kaldır
        document.querySelectorAll('.quick-date-btn').forEach(btn => {
            btn.classList.remove('active');
        });

        // Tıklanan butona active sınıfını ekle
        event.target.classList.add('active');

        let startDate, endDate;

        switch(period) {
            case 'today':
                startDate = endDate = today;
                break;
            case 'yesterday':
                startDate = endDate = new Date(today.getTime() - 24 * 60 * 60 * 1000);
                break;
            case 'thisWeek':
                const startOfWeek = new Date(today.setDate(today.getDate() - today.getDay()));
                startDate = startOfWeek;
                endDate = new Date();
                break;
            case 'lastWeek':
                const lastWeekStart = new Date(today.setDate(today.getDate() - today.getDay() - 7));
                const lastWeekEnd = new Date(today.setDate(today.getDate() - today.getDay() - 1));
                startDate = lastWeekStart;
                endDate = lastWeekEnd;
                break;
            case 'thisMonth':
                startDate = new Date(today.getFullYear(), today.getMonth(), 1);
                endDate = new Date();
                break;
            case 'lastMonth':
                startDate = new Date(today.getFullYear(), today.getMonth() - 1, 1);
                endDate = new Date(today.getFullYear(), today.getMonth(), 0);
                break;
            case 'last30Days':
                startDate = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
                endDate = new Date();
                break;
        }

        baslangic.value = startDate.toISOString().split('T')[0];
        bitis.value = endDate.toISOString().split('T')[0];
    }

    // Rapor tipine göre tarihleri ayarla
    function adjustDatesForReportType(type) {
        const today = new Date();
        const baslangic = document.getElementById('baslangic');
        const bitis = document.getElementById('bitis');

        switch(type) {
            case 'gunluk':
                baslangic.value = today.toISOString().split('T')[0];
                bitis.value = today.toISOString().split('T')[0];
                break;
            case 'haftalik':
                const weekStart = new Date(today.setDate(today.getDate() - today.getDay()));
                baslangic.value = weekStart.toISOString().split('T')[0];
                bitis.value = new Date().toISOString().split('T')[0];
                break;
            case 'aylik':
                const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
                baslangic.value = monthStart.toISOString().split('T')[0];
                bitis.value = new Date().toISOString().split('T')[0];
                break;
            case 'ozel':
                const last30Days = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
                baslangic.value = last30Days.toISOString().split('T')[0];
                bitis.value = new Date().toISOString().split('T')[0];
                break;
        }
    }

    // Form validasyonu
    document.getElementById('reportForm').addEventListener('submit', function(e) {
        const raporTipi = document.getElementById('raporTipi').value;
        const format = document.getElementById('format').value;
        const baslangic = document.getElementById('baslangic').value;
        const bitis = document.getElementById('bitis').value;

        if (!raporTipi) {
            e.preventDefault();
            alert('Lütfen bir rapor tipi seçin.');
            return;
        }

        if (!format) {
            e.preventDefault();
            alert('Lütfen bir format seçin.');
            return;
        }

        if (!baslangic || !bitis) {
            e.preventDefault();
            alert('Lütfen tarih aralığını belirtin.');
            return;
        }

        if (new Date(baslangic) > new Date(bitis)) {
            e.preventDefault();
            alert('Başlangıç tarihi bitiş tarihinden büyük olamaz.');
            return;
        }

        // Form gönderilirken loading göster
        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Rapor Oluşturuluyor...';
        submitBtn.disabled = true;
    });

    // Sayfa yüklendiğinde varsayılan değerleri ayarla
    document.addEventListener('DOMContentLoaded', function() {
        // Varsayılan tarih aralığını son 7 gün olarak ayarla
        const today = new Date();
        const weekAgo = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);

        document.getElementById('baslangic').value = weekAgo.toISOString().split('T')[0];
        document.getElementById('bitis').value = today.toISOString().split('T')[0];
    });
</script>
</body>
</html>