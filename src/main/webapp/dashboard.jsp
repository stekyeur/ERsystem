<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Acil Servis</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --success-gradient: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            --warning-gradient: linear-gradient(135deg, #fdbb2d 0%, #22c1c3 100%);
            --info-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --danger-gradient: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
            --dark-bg: #0a0e1a;
            --card-bg: rgba(255, 255, 255, 0.05);
            --text-primary: #ffffff;
            --text-secondary: #a0aec0;
            --border-color: rgba(255, 255, 255, 0.1);
            --glass-bg: rgba(255, 255, 255, 0.08);
            --shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: var(--dark-bg);
            background-image:
                    radial-gradient(circle at 20% 20%, rgba(102, 126, 234, 0.1) 0%, transparent 50%),
                    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.1) 0%, transparent 50%),
                    radial-gradient(circle at 40% 40%, rgba(17, 153, 142, 0.05) 0%, transparent 50%);
            color: var(--text-primary);
            min-height: 100vh;
            overflow-x: hidden;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .dashboard-header {
            text-align: center;
            margin: 40px 0;
            position: relative;
        }

        .dashboard-title {
            font-size: 3rem;
            font-weight: 700;
            background: linear-gradient(45deg, #667eea, #764ba2, #11998e);
            background-size: 200% 200%;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            animation: gradientShift 4s ease-in-out infinite;
            margin-bottom: 10px;
        }

        .dashboard-subtitle {
            color: var(--text-secondary);
            font-size: 1.1rem;
            font-weight: 300;
        }

        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }

        .stat-card {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border-color);
            border-radius: 24px;
            padding: 30px;
            position: relative;
            overflow: hidden;
            transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
            cursor: pointer;
        }

        .stat-card:hover {
            transform: translateY(-8px) scale(1.02);
            box-shadow: var(--shadow), 0 0 40px rgba(102, 126, 234, 0.2);
            border-color: rgba(255, 255, 255, 0.2);
        }

        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            /* `var(--gradient)` yerine direkt bir renk veya gradient kullanılmalı */
        }

        .stat-card.primary::before { background: var(--primary-gradient); }
        .stat-card.success::before { background: var(--success-gradient); }
        .stat-card.warning::before { background: var(--warning-gradient); }
        .stat-card.info::before { background: var(--info-gradient); }

        .stat-card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .stat-icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            color: white;
            position: relative;
            overflow: hidden;
        }

        .stat-icon::before {
            content: '';
            position: absolute;
            inset: 0;
            /* `var(--gradient)` yerine uygun bir gradient kullanılmalı */
            opacity: 0.2;
            border-radius: 12px;
        }

        /* Bu kısımlar zaten spesifik class'lara gradient atıyor. */
        .stat-card.primary .stat-icon { background: var(--primary-gradient); }
        .stat-card.success .stat-icon { background: var(--success-gradient); }
        .stat-card.warning .stat-icon { background: var(--warning-gradient); }
        .stat-card.info .stat-icon { background: var(--info-gradient); }

        /* `stat-icon::before` için özel kurallar */
        .stat-card.primary .stat-icon::before { background: var(--primary-gradient); }
        .stat-card.success .stat-icon::before { background: var(--success-gradient); }
        .stat-card.warning .stat-icon::before { background: var(--warning-gradient); }
        .stat-card.info .stat-icon::before { background: var(--info-gradient); }

        .stat-title {
            font-size: 0.9rem;
            color: var(--text-secondary);
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .stat-value {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 10px;
            animation: countUp 0.8s ease-out;
        }

        @keyframes countUp {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .data-table-section {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border-color);
            border-radius: 24px;
            padding: 30px;
            position: relative;
            overflow: hidden;
        }

        .section-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid var(--border-color);
        }

        .section-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: var(--text-primary);
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .section-icon {
            width: 40px;
            height: 40px;
            background: var(--primary-gradient);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 18px;
        }

        .date-badge {
            background: rgba(102, 126, 234, 0.2);
            border: 1px solid rgba(102, 126, 234, 0.3);
            color: #667eea;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .modern-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            background: transparent;
        }

        .modern-table thead th {
            background: rgba(102, 126, 234, 0.1);
            color: var(--text-primary);
            font-weight: 600;
            padding: 15px;
            text-align: left;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            border: none;
            position: relative;
        }

        .modern-table thead th:first-child {
            border-radius: 12px 0 0 0;
        }

        .modern-table thead th:last-child {
            border-radius: 0 12px 0 0;
        }

        .modern-table tbody tr {
            transition: all 0.3s ease;
            border-bottom: 1px solid var(--border-color);
        }

        .modern-table tbody tr:hover {
            background: rgba(255, 255, 255, 0.03);
            transform: scale(1.01);
        }

        .modern-table tbody td {
            padding: 15px;
            color: var(--text-secondary);
            border: none;
            vertical-align: middle;
        }

        .modern-table tbody tr:last-child td:first-child {
            border-radius: 0 0 0 12px;
        }

        .modern-table tbody tr:last-child td:last-child {
            border-radius: 0 0 12px 0;
        }

        .modern-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            border: none;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .badge-primary {
            background: var(--primary-gradient);
            color: white;
        }

        .badge-secondary {
            background: rgba(160, 174, 192, 0.2);
            color: var(--text-secondary);
            border: 1px solid rgba(160, 174, 192, 0.3);
        }

        .badge-danger {
            background: var(--danger-gradient);
            color: white;
        }

        .badge-success {
            background: var(--success-gradient);
            color: white;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: var(--text-secondary);
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.3;
        }

        .empty-state h3 {
            font-size: 1.2rem;
            margin-bottom: 10px;
            color: var(--text-primary);
        }

        .loading-animation {
            display: inline-block;
            width: 20px;
            height: 20px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border-top-color: #667eea;
            animation: spin 1s ease-in-out infinite;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .pulse {
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.7; }
        }

        @media (max-width: 768px) {
            .container {
                padding: 0 15px;
            }

            .dashboard-title {
                font-size: 2rem;
            }

            .stats-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .stat-card {
                padding: 20px;
            }

            .section-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }

            .modern-table {
                font-size: 0.9rem;
            }

            .modern-table thead th,
            .modern-table tbody td {
                padding: 10px;
            }
        }

        .fade-in {
            animation: fadeIn 0.8s ease-out forwards;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .stagger-1 { animation-delay: 0.1s; }
        .stagger-2 { animation-delay: 0.2s; }
        .stagger-3 { animation-delay: 0.3s; }
        .stagger-4 { animation-delay: 0.4s; }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>


<div class="container">
    <div class="dashboard-header fade-in">
        <h1 class="dashboard-title">Acil Servis Dashboard</h1>
        <p class="dashboard-subtitle">Hemşire Performans ve İstatistik Yönetim Sistemi</p>
    </div>

    <div class="stats-grid">
        <div class="stat-card primary fade-in stagger-1">
            <div class="stat-card-header">
                <div class="stat-icon">
                    <i class="fas fa-user-nurse"></i>
                </div>
            </div>
            <div class="stat-title">Toplam Hemşire</div>
            <div class="stat-value"><%= request.getAttribute("toplamHemsire") %></div>
        </div>

        <div class="stat-card success fade-in stagger-2">
            <div class="stat-card-header">
                <div class="stat-icon">
                    <i class="fas fa-medal"></i>
                </div>
            </div>
            <div class="stat-title">Tecrübeli (T3)</div>
            <div class="stat-value"><%= request.getAttribute("tecrubeliSayisi") %></div>
        </div>

        <div class="stat-card warning fade-in stagger-3">
            <div class="stat-card-header">
                <div class="stat-icon">
                    <i class="fas fa-user-graduate"></i>
                </div>
            </div>
            <div class="stat-title">Orta Tecrübeli (T2)</div>
            <div class="stat-value"><%= request.getAttribute("ortaTecrubeliSayisi") %></div>
        </div>

        <div class="stat-card info fade-in stagger-4">
            <div class="stat-card-header">
                <div class="stat-icon">
                    <i class="fas fa-seedling"></i>
                </div>
            </div>
            <div class="stat-title">Tecrübesiz (T0)</div>
            <div class="stat-value"><%= request.getAttribute("tecrubesizSayisi") %></div>
        </div>
    </div>

    <div class="data-table-section fade-in">
        <div class="section-header">
            <div class="section-title">
                <div class="section-icon">
                    <i class="fas fa-chart-line"></i>
                </div>
                Bugünkü İstatistikler
            </div>
            <%
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date bugun = (java.util.Date) request.getAttribute("bugun");
            %>
            <div class="date-badge">
                <i class="fas fa-calendar-day"></i>
                <%= sdf.format(bugun) %>
            </div>
        </div>

        <%
            List<Object[]> istatistikler = (List<Object[]>) request.getAttribute("gunlukIstatistikler");
            if (istatistikler != null && !istatistikler.isEmpty()) {
        %>
        <div style="overflow-x: auto;">
            <table class="modern-table">
                <thead>
                <tr>
                    <th><i class="fas fa-user-tag"></i> Hemşire Kategorisi</th>
                    <th><i class="fas fa-tasks"></i> İşlem</th>
                    <th><i class="fas fa-tag"></i> Tip</th>
                    <th><i class="fas fa-exclamation-triangle"></i> Kritiklik</th>
                    <th><i class="fas fa-calculator"></i> Toplam İşlem</th>
                    <th><i class="fas fa-clock"></i> Ortalama Süre</th>
                </tr>
                </thead>
                <tbody>
                <% for (Object[] row : istatistikler) { %>
                <tr>
                    <td><strong><%= row[0] %></strong></td>
                    <td><%= row[1] %></td>
                    <td>
                            <span class="modern-badge <%= "DIRECT".equals(row[2]) ? "badge-primary" : "badge-secondary" %>">
                                <i class="fas fa-<%= "DIRECT".equals(row[2]) ? "arrow-right" : "layers" %>"></i>
                                <%= row[2] %>
                            </span>
                    </td>
                    <td>
                            <span class="modern-badge <%= "CRITICAL".equals(row[3]) ? "badge-danger" : "badge-success" %>">
                                <i class="fas fa-<%= "CRITICAL".equals(row[3]) ? "exclamation-circle" : "check-circle" %>"></i>
                                <%= row[3] %>
                            </span>
                    </td>
                    <td><strong><%= row[4] %></strong></td>
                    <td><%= String.format("%.1f", row[5]) %> dk</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
        <div class="empty-state">
            <i class="fas fa-chart-line"></i>
            <h3>Henüz Veri Yok</h3>
            <p>Bugün için henüz işlem kaydı bulunmuyor.</p>
        </div>
        <% } %>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {

        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });

        const tableRows = document.querySelectorAll('.modern-table tbody tr');
        tableRows.forEach(row => {
            row.addEventListener('mouseenter', function() {
                this.style.transform = 'scale(1.01)';
            });

            row.addEventListener('mouseleave', function() {
                this.style.transform = 'scale(1)';
            });
        });

        const statValues = document.querySelectorAll('.stat-value');
        statValues.forEach(stat => {
            const finalValue = parseInt(stat.textContent);
            if (!isNaN(finalValue)) {
                let currentValue = 0;
                const increment = Math.ceil(finalValue / 20);
                const timer = setInterval(() => {
                    currentValue += increment;
                    if (currentValue >= finalValue) {
                        currentValue = finalValue;
                        clearInterval(timer);
                    }
                    stat.textContent = currentValue;
                }, 50);
            }
        });

        setTimeout(() => {
            document.body.classList.add('loaded');
        }, 500);
    });
</script>

</body>
</html>