<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.hospital.model.Rapor" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Raporlarım - Acil Servis İş Yükü</title>
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

        .reports-container {
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

        .alert-info {
            background: linear-gradient(135deg, rgba(79, 172, 254, 0.1), rgba(79, 172, 254, 0.05));
            border-left: 4px solid var(--info-color);
            color: #0c5460;
        }

        .reports-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: rgba(255, 255, 255, 0.9);
            border-radius: 12px;
            padding: 1.5rem;
            text-align: center;
            border-left: 4px solid;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        }

        .stat-card.total { border-left-color: var(--info-color); }
        .stat-card.ready { border-left-color: var(--success-color); }
        .stat-card.processing { border-left-color: var(--warning-color); }
        .stat-card.error { border-left-color: var(--danger-color); }

        .stat-card i {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .stat-card.total i { color: var(--info-color); }
        .stat-card.ready i { color: var(--success-color); }
        .stat-card.processing i { color: var(--warning-color); }
        .stat-card.error i { color: var(--danger-color); }

        .stat-card h3 {
            font-size: 1.5rem;
            font-weight: 700;
            margin: 0;
            color: var(--dark-color);
        }

        .stat-card p {
            margin: 0;
            color: #6c757d;
            font-size: 0.9rem;
        }

        .filter-section {
            background: rgba(255, 255, 255, 0.8);
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            border-left: 4px solid var(--primary-color);
        }

        .table-container {
            background: rgba(255, 255, 255, 0.9);
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        }

        .table {
            margin: 0;
        }

        .table thead {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
        }

        .table thead th {
            border: none;
            font-weight: 600;
            padding: 1rem;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody td {
            padding: 1rem;
            border: none;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
            vertical-align: middle;
        }

        .table tbody tr:hover {
            background: rgba(102, 126, 234, 0.05);
        }

        .badge {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .badge.bg-success { background: var(--success-color) !important; }
        .badge.bg-warning {
            background: var(--warning-color) !important;
            color: var(--dark-color) !important;
        }
        .badge.bg-danger { background: var(--danger-color) !important; }
        .badge.bg-info { background: var(--info-color) !important; }
        .badge.bg-secondary { background: #6c757d !important; }

        .btn-sm {
            padding: 0.5rem 1rem;
            border-radius: 8px;
            font-size: 0.8rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-success {
            background: var(--success-color);
            border: none;
            color: white;
        }

        .btn-success:hover {
            background: #32d470;
            transform: translateY(-1px);
            box-shadow: 0 3px 10px rgba(56, 239, 125, 0.3);
        }

        .btn-warning {
            background: var(--warning-color);
            border: none;
            color: var(--dark-color);
        }

        .btn-danger {
            background: var(--danger-color);
            border: none;
            color: white;
        }

        .btn-outline-primary {
            border: 2px solid var(--primary-color);
            color: var(--primary-color);
            border-radius: 10px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-outline-primary:hover {
            background: var(--primary-color);
            border-color: var(--primary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }

        .btn-outline-info {
            border: 2px solid var(--info-color);
            color: var(--info-color);
            border-radius: 10px;
            padding: 0.5rem 1rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-outline-info:hover {
            background: var(--info-color);
            border-color: var(--info-color);
        }

        .empty-state {
            text-align: center;
            padding: 3rem;
            color: #6c757d;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 1rem;
            color: var(--primary-color);
            opacity: 0.5;
        }

        .empty-state h4 {
            color: var(--dark-color);
            margin-bottom: 1rem;
        }

        .progress {
            height: 6px;
            border-radius: 10px;
            background: rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .progress-bar {
            background: linear-gradient(90deg, var(--success-color), var(--info-color));
            transition: width 1s ease;
        }

        .report-actions {
            display: flex;
            gap: 0.5rem;
            justify-content: center;
        }

        .filter-form {
            display: flex;
            gap: 1rem;
            align-items: end;
            flex-wrap: wrap;
        }

        .filter-form .form-group {
            flex: 1;
            min-width: 150px;
        }

        .auto-refresh {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: var(--primary-color);
            color: white;
            border: none;
            padding: 0.75rem;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            font-size: 1.2rem;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
            transition: all 0.3s ease;
        }

        .auto-refresh:hover {
            transform: scale(1.1);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }

        .auto-refresh.active {
            animation: spin 2s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @media (max-width: 768px) {
            body {
                padding-top: 80px;
            }

            .reports-container {
                margin: 1rem;
                border-radius: 15px;
            }

            .table-responsive {
                font-size: 0.85rem;
            }

            .report-actions {
                flex-direction: column;
            }

            .filter-form {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-11">
            <div class="reports-container p-4">
                <div class="page-header">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2><i class="fas fa-folder-open me-3"></i>Raporlarım</h2>
                            <p>Oluşturduğunuz rapor geçmişini görüntüleyin, indirin ve yönetin</p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/rapor" class="btn btn-outline-primary">
                                <i class="fas fa-plus me-2"></i>Yeni Rapor
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Başarı Mesajı -->
                <% if (request.getParameter("basari") != null) { %>
                <div class="alert alert-success">
                    <i class="fas fa-check-circle me-2"></i>
                    Rapor oluşturma işlemi başlatıldı. Hazır olduğunda indirme bağlantısı aktif olacaktır.
                </div>
                <% } %>

                <%
                    List<Rapor> raporlar = (List<Rapor>) request.getAttribute("raporlar");
                    int totalReports = raporlar != null ? raporlar.size() : 0;
                    int readyReports = 0;
                    int processingReports = 0;
                    int errorReports = 0;

                    if (raporlar != null) {
                        for (Rapor rapor : raporlar) {
                            if ("HAZIR".equals(rapor.getDurum())) readyReports++;
                            else if ("OLUSTURULUYOR".equals(rapor.getDurum())) processingReports++;
                            else if ("HATA".equals(rapor.getDurum())) errorReports++;
                        }
                    }
                %>

                <!-- İstatistikler -->
                <div class="reports-stats">
                    <div class="stat-card total">
                        <i class="fas fa-file-alt"></i>
                        <h3><%= totalReports %></h3>
                        <p>Toplam Rapor</p>
                    </div>
                    <div class="stat-card ready">
                        <i class="fas fa-check-circle"></i>
                        <h3><%= readyReports %></h3>
                        <p>Hazır Rapor</p>
                    </div>
                    <div class="stat-card processing">
                        <i class="fas fa-clock"></i>
                        <h3><%= processingReports %></h3>
                        <p>İşleniyor</p>
                    </div>
                    <div class="stat-card error">
                        <i class="fas fa-exclamation-triangle"></i>
                        <h3><%= errorReports %></h3>
                        <p>Hatalı</p>
                    </div>
                </div>

                <!-- Filtreler -->
                <div class="filter-section">
                    <h6><i class="fas fa-filter me-2"></i>Filtreler</h6>
                    <form class="filter-form" method="get" action="${pageContext.request.contextPath}/rapor">
                        <input type="hidden" name="action" value="liste">
                        <div class="form-group">
                            <label class="form-label">Durum</label>
                            <select name="durum" class="form-select">
                                <option value="">Tümü</option>
                                <option value="HAZIR">Hazır</option>
                                <option value="OLUSTURULUYOR">Oluşturuluyor</option>
                                <option value="HATA">Hatalı</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Rapor Tipi</label>
                            <select name="tip" class="form-select">
                                <option value="">Tümü</option>
                                <option value="GUNLUK">Günlük</option>
                                <option value="HAFTALIK">Haftalık</option>
                                <option value="AYLIK">Aylık</option>
                                <option value="OZEL">Özel</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Format</label>
                            <select name="format" class="form-select">
                                <option value="">Tümü</option>
                                <option value="PDF">PDF</option>
                                <option value="EXCEL">Excel</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-outline-info">
                                <i class="fas fa-search me-1"></i>Filtrele
                            </button>
                        </div>
                    </form>
                </div>

                <% if (raporlar != null && !raporlar.isEmpty()) { %>
                <!-- Raporlar Tablosu -->
                <div class="table-container">
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th><i class="fas fa-file me-2"></i>Rapor Adı</th>
                                <th><i class="fas fa-tag me-2"></i>Tip</th>
                                <th><i class="fas fa-file-export me-2"></i>Format</th>
                                <th><i class="fas fa-calendar me-2"></i>Tarih Aralığı</th>
                                <th><i class="fas fa-clock me-2"></i>Oluşturma</th>
                                <th><i class="fas fa-info-circle me-2"></i>Durum</th>
                                <th><i class="fas fa-download me-2"></i>Boyut</th>
                                <th><i class="fas fa-cogs me-2"></i>İşlemler</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                                SimpleDateFormat sdfTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                for (Rapor rapor : raporlar) {
                            %>
                            <tr>
                                <td>
                                    <strong><%= rapor.getRaporAdi() != null ? rapor.getRaporAdi() : "Rapor #" + rapor.getId() %></strong>
                                    <% if (rapor.getAciklama() != null && !rapor.getAciklama().isEmpty()) { %>
                                    <br><small class="text-muted"><%= rapor.getAciklama() %></small>
                                    <% } %>
                                </td>
                                <td>
                                    <span class="badge bg-secondary">
                                        <%= rapor.getRaporTipiText() %>
                                    </span>
                                </td>
                                <td>
                                    <span class="badge bg-info">
                                        <i class="fas fa-file-<%= "PDF".equals(rapor.getFormat()) ? "pdf" : "excel" %> me-1"></i>
                                        <%= rapor.getFormatText() %>
                                    </span>
                                </td>
                                <td>
                                    <small>
                                        <%= sdf.format(rapor.getBaslangicTarihi()) %><br>
                                        <%= sdf.format(rapor.getBitisTarihi()) %>
                                    </small>
                                </td>
                                <td>
                                    <small><%= sdfTime.format(rapor.getOlusturmaTarihi()) %></small>
                                </td>
                                <td>
                                    <%
                                        String durumClass = "secondary";
                                        String durumIcon = "question-circle";
                                        if ("HAZIR".equals(rapor.getDurum())) {
                                            durumClass = "success";
                                            durumIcon = "check-circle";
                                        } else if ("HATA".equals(rapor.getDurum())) {
                                            durumClass = "danger";
                                            durumIcon = "exclamation-triangle";
                                        } else if ("OLUSTURULUYOR".equals(rapor.getDurum())) {
                                            durumClass = "warning";
                                            durumIcon = "clock";
                                        }
                                    %>
                                    <span class="badge bg-<%= durumClass %>">
                                        <i class="fas fa-<%= durumIcon %> me-1"></i>
                                        <%= rapor.getDurumText() %>
                                    </span>

                                    <% if ("OLUSTURULUYOR".equals(rapor.getDurum())) { %>
                                    <div class="progress mt-1">
                                        <div class="progress-bar progress-bar-animated" style="width: 60%"></div>
                                    </div>
                                    <% } %>
                                </td>
                                <td>
                                    <small class="text-muted">
                                        <%= rapor.getDosyaBoyutuText() %>
                                    </small>
                                </td>
                                <td>
                                    <div class="report-actions">
                                        <% if ("HAZIR".equals(rapor.getDurum())) { %>
                                        <a href="${pageContext.request.contextPath}/rapor?action=indir&id=<%= rapor.getId() %>"
                                           class="btn btn-sm btn-success" title="Raporu İndir">
                                            <i class="fas fa-download"></i>
                                        </a>
                                        <button class="btn btn-sm btn-outline-info"
                                                onclick="viewReport(<%= rapor.getId() %>)" title="Önizleme">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                        <% } else if ("OLUSTURULUYOR".equals(rapor.getDurum())) { %>
                                        <button class="btn btn-sm btn-warning" disabled title="Hazırlanıyor">
                                            <i class="fas fa-spinner fa-spin"></i>
                                        </button>
                                        <% } else if ("HATA".equals(rapor.getDurum())) { %>
                                        <button class="btn btn-sm btn-danger"
                                                onclick="retryReport(<%= rapor.getId() %>)" title="Tekrar Dene">
                                            <i class="fas fa-redo"></i>
                                        </button>
                                        <% } else { %>
                                        <span class="text-muted">-</span>
                                        <% } %>

                                        <button class="btn btn-sm btn-outline-danger"
                                                onclick="deleteReport(<%= rapor.getId() %>)" title="Sil">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
                <% } else { %>
                <!-- Boş Durum -->
                <div class="empty-state">
                    <i class="fas fa-file-plus"></i>
                    <h4>Henüz Rapor Yok</h4>
                    <p>İlk raporunuzu oluşturmak için aşağıdaki butonu kullanın.</p>
                    <a href="${pageContext.request.contextPath}/rapor" class="btn btn-outline-primary mt-3">
                        <i class="fas fa-plus me-2"></i>İlk Raporu Oluştur
                    </a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<!-- Otomatik Yenileme Butonu -->
<% if (processingReports > 0) { %>
<button class="auto-refresh" onclick="toggleAutoRefresh()" title="Otomatik Yenileme">
    <i class="fas fa-sync-alt"></i>
</button>
<% } %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    let autoRefreshInterval = null;
    let autoRefreshActive = false;

    // Rapor önizlemesi
    function viewReport(reportId) {
        // Modal veya yeni sekme ile rapor önizlemesi
        window.open(`${window.location.origin}/rapor?action=preview&id=${reportId}`, '_blank');
    }

    // Rapor yeniden oluşturma
    function retryReport(reportId) {
        if (confirm('Bu raporu yeniden oluşturmak istediğinizden emin misiniz?')) {
            fetch(`${window.location.origin}/rapor?action=retry&id=${reportId}`, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Rapor yeniden oluşturulurken bir hata oluştu.');
                }
            });
        }
    }

    // Rapor silme
    function deleteReport(reportId) {
        if (confirm('Bu raporu silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.')) {
            fetch(`${window.location.origin}/rapor?action=delete&id=${reportId}`, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Rapor silinirken bir hata oluştu.');
                }
            });
        }
    }

    // Otomatik yenileme
    function toggleAutoRefresh() {
        const button = document.querySelector('.auto-refresh');

        if (autoRefreshActive) {
            clearInterval(autoRefreshInterval);
            autoRefreshActive = false;
            button.classList.remove('active');
            button.title = 'Otomatik Yenileme';
        } else {
            autoRefreshInterval = setInterval(() => {
                location.reload();
            }, 30000); // 30 saniyede bir yenile
            autoRefreshActive = true;
            button.classList.add('active');
            button.title = 'Otomatik Yenileme Aktif (30s)';
        }
    }

    // Sayfa yüklendiğinde otomatik yenileme başlat (eğer işlenen rapor varsa)
    document.addEventListener('DOMContentLoaded', function() {
        <% if (processingReports > 0) { %>
        // Otomatik yenilemeyi başlat
        setTimeout(() => {
            if (document.querySelector('.badge.bg-warning')) {
                location.reload();
            }
        }, 30000);
        <% } %>

        // Progress bar animasyonu
        document.querySelectorAll('.progress-bar-animated').forEach(bar => {
            setInterval(() => {
                const currentWidth = parseInt(bar.style.width) || 60;
                const newWidth = currentWidth >= 90 ? 60 : currentWidth + 10;
                bar.style.width = newWidth + '%';
            }, 2000);
        });

        // Tooltip'leri aktifleştir
        const tooltipElements = document.querySelectorAll('[title]');
        tooltipElements.forEach(element => {
            new bootstrap.Tooltip(element);
        });
    });

    // Filtre formunu otomatik gönder
    document.querySelectorAll('.filter-form select').forEach(select => {
        select.addEventListener('change', function() {
            this.form.submit();
        });
    });

    // Tablo sıralama fonksiyonu
    function sortTable(columnIndex) {
        const table = document.querySelector('.table');
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));

        rows.sort((a, b) => {
            const aValue = a.cells[columnIndex].textContent.trim();
            const bValue = b.cells[columnIndex].textContent.trim();
            return aValue.localeCompare(bValue);
        });

        rows.forEach(row => tbody.appendChild(row));
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.key === 'r') {
            e.preventDefault();
            location.reload();
        }

        if (e.ctrlKey && e.key === 'n') {
            e.preventDefault();
            window.location.href = '${pageContext.request.contextPath}/rapor';
        }
    });

    // Responsive tablo için yatay scroll gölgesi
    const tableContainer = document.querySelector('.table-responsive');
    if (tableContainer) {
        tableContainer.addEventListener('scroll', function() {
            const scrollLeft = this.scrollLeft;
            const scrollWidth = this.scrollWidth;
            const clientWidth = this.clientWidth;

            if (scrollLeft > 0) {
                this.classList.add('scroll-shadow-left');
            } else {
                this.classList.remove('scroll-shadow-left');
            }

            if (scrollLeft < scrollWidth - clientWidth) {
                this.classList.add('scroll-shadow-right');
            } else {
                this.classList.remove('scroll-shadow-right');
            }
        });
    }
</script>

<style>
    .scroll-shadow-left::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 10px;
        height: 100%;
        background: linear-gradient(to right, rgba(0,0,0,0.1), transparent);
        pointer-events: none;
        z-index: 1;
    }

    .scroll-shadow-right::after {
        content: '';
        position: absolute;
        top: 0;
        right: 0;
        width: 10px;
        height: 100%;
        background: linear-gradient(to left, rgba(0,0,0,0.1), transparent);
        pointer-events: none;
        z-index: 1;
    }
</style>
</body>
</html>