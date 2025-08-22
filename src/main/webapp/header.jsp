<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    :root {
        --nav-bg: rgba(10, 14, 26, 0.95);
        --nav-glass-bg: rgba(255, 255, 255, 0.08);
        --nav-border: rgba(255, 255, 255, 0.1);
        --nav-text: #ffffff;
        --nav-text-secondary: #a0aec0;
        --nav-hover: rgba(102, 126, 234, 0.2);
        --nav-active: #667eea;
        --nav-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        --nav-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
        --nav-dropdown-bg: rgba(10, 14, 26, 0.98);
    }

    .modern-navbar {
        background: var(--nav-bg);
        backdrop-filter: blur(20px);
        border-bottom: 1px solid var(--nav-border);
        box-shadow: var(--nav-shadow);
        position: sticky;
        top: 0;
        z-index: 1000;
        transition: all 0.3s ease;
        padding: 0;
    }

    .modern-navbar.scrolled {
        background: rgba(10, 14, 26, 0.98);
        backdrop-filter: blur(30px);
        box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.4);
    }

    .nav-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 0 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        min-height: 70px;
    }

    .nav-brand {
        display: flex;
        align-items: center;
        text-decoration: none;
        color: var(--nav-text);
        font-weight: 700;
        font-size: 1.3rem;
        transition: all 0.3s ease;
        position: relative;
    }

    .nav-brand:hover {
        color: var(--nav-text);
        transform: scale(1.05);
    }

    .brand-icon {
        width: 40px;
        height: 40px;
        background: var(--nav-gradient);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        font-size: 18px;
        color: white;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        animation: pulse 2s infinite;
    }

    @keyframes pulse {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.05); }
    }

    .brand-text {
        background: linear-gradient(45deg, #667eea, #764ba2);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }

    .nav-menu {
        display: flex;
        align-items: center;
        list-style: none;
        margin: 0;
        padding: 0;
        gap: 8px;
    }

    .nav-item {
        position: relative;
    }

    .nav-link {
        display: flex;
        align-items: center;
        padding: 12px 20px;
        color: var(--nav-text-secondary);
        text-decoration: none;
        font-weight: 500;
        font-size: 0.95rem;
        border-radius: 12px;
        transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
        position: relative;
        overflow: hidden;
        white-space: nowrap;
    }

    .nav-link::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: var(--nav-hover);
        opacity: 0;
        transition: opacity 0.3s ease;
        z-index: -1;
    }

    .nav-link:hover {
        color: var(--nav-text);
        transform: translateY(-2px);
    }

    .nav-link:hover::before {
        opacity: 1;
    }

    .nav-link.active {
        color: var(--nav-active);
        background: var(--nav-hover);
    }

    .nav-link i {
        margin-right: 8px;
        font-size: 16px;
    }

    /* Dropdown Styles */
    .dropdown {
        position: relative;
    }

    .dropdown-toggle::after {
        content: '\f107';
        font-family: 'Font Awesome 6 Free';
        font-weight: 900;
        border: none;
        margin-left: 8px;
        transition: transform 0.3s ease;
    }

    .dropdown.show .dropdown-toggle::after {
        transform: rotate(180deg);
    }

    .dropdown-menu {
        position: absolute;
        top: 100%;
        left: 0;
        background: var(--nav-dropdown-bg);
        backdrop-filter: blur(30px);
        border: 1px solid var(--nav-border);
        border-radius: 16px;
        padding: 8px;
        min-width: 200px;
        box-shadow: var(--nav-shadow);
        opacity: 0;
        visibility: hidden;
        transform: translateY(-10px);
        transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
        z-index: 1000;
        list-style: none;
        margin: 8px 0 0 0;
    }

    .dropdown.show .dropdown-menu {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
    }

    .dropdown-item {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        color: var(--nav-text-secondary);
        text-decoration: none;
        border-radius: 10px;
        transition: all 0.3s ease;
        font-size: 0.9rem;
        margin-bottom: 2px;
    }

    .dropdown-item:hover {
        color: var(--nav-text);
        background: var(--nav-hover);
        transform: translateX(4px);
    }

    .dropdown-item i {
        margin-right: 10px;
        font-size: 14px;
        width: 16px;
        text-align: center;
    }

    /* User Menu */
    .user-menu {
        margin-left: 20px;
    }

    .user-avatar {
        width: 36px;
        height: 36px;
        background: var(--nav-gradient);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-weight: 600;
        margin-right: 10px;
        font-size: 14px;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
    }

    .user-info {
        display: flex;
        align-items: center;
        padding: 8px 16px;
        color: var(--nav-text);
        border-radius: 12px;
        transition: all 0.3s ease;
        cursor: pointer;
        border: 1px solid transparent;
    }

    .user-info:hover {
        background: var(--nav-hover);
        border-color: var(--nav-border);
    }

    .user-name {
        font-weight: 500;
        font-size: 0.9rem;
    }

    /* Mobile Menu */
    .mobile-menu-toggle {
        display: none;
        background: none;
        border: none;
        color: var(--nav-text);
        font-size: 20px;
        padding: 8px;
        border-radius: 8px;
        transition: all 0.3s ease;
        cursor: pointer;
    }

    .mobile-menu-toggle:hover {
        background: var(--nav-hover);
    }

    .mobile-menu {
        display: none;
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        background: var(--nav-dropdown-bg);
        backdrop-filter: blur(30px);
        border-bottom: 1px solid var(--nav-border);
        padding: 20px;
        box-shadow: var(--nav-shadow);
    }

    .mobile-menu.show {
        display: block;
        animation: slideDown 0.3s ease;
    }

    @keyframes slideDown {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .mobile-nav-item {
        margin-bottom: 4px;
    }

    .mobile-nav-link {
        display: block;
        padding: 12px 16px;
        color: var(--nav-text-secondary);
        text-decoration: none;
        border-radius: 10px;
        transition: all 0.3s ease;
        font-size: 0.95rem;
    }

    .mobile-nav-link:hover {
        color: var(--nav-text);
        background: var(--nav-hover);
    }

    .mobile-dropdown-items {
        padding-left: 20px;
        margin-top: 8px;
    }

    .divider {
        height: 1px;
        background: var(--nav-border);
        margin: 16px 0;
    }

    /* Status Indicator */
    .status-indicator {
        width: 8px;
        height: 8px;
        background: #38ef7d;
        border-radius: 50%;
        margin-left: 8px;
        animation: blink 2s infinite;
    }

    @keyframes blink {
        0%, 50% { opacity: 1; }
        51%, 100% { opacity: 0.3; }
    }

    /* Responsive Design */
    @media (max-width: 1024px) {
        .nav-menu {
            display: none;
        }

        .mobile-menu-toggle {
            display: block;
        }

        .nav-container {
            padding: 0 15px;
        }
    }

    @media (max-width: 640px) {
        .nav-container {
            min-height: 60px;
        }

        .nav-brand {
            font-size: 1.1rem;
        }

        .brand-icon {
            width: 36px;
            height: 36px;
            margin-right: 10px;
        }

        .user-avatar {
            width: 32px;
            height: 32px;
        }
    }

    /* Notification Badge */
    .notification-badge {
        position: absolute;
        top: -2px;
        right: -2px;
        background: #ff416c;
        color: white;
        border-radius: 50%;
        width: 18px;
        height: 18px;
        font-size: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 600;
        animation: bounce 2s infinite;
    }

    @keyframes bounce {
        0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
        40% { transform: translateY(-3px); }
        60% { transform: translateY(-2px); }
    }
</style>

<nav class="modern-navbar" id="mainNavbar">
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-brand">
            <div class="brand-icon">
                <i class="fas fa-heartbeat"></i>
            </div>
            <span class="brand-text">Acil Servis İş Yükü</span>
        </a>

        <!-- Desktop Menu -->
        <ul class="nav-menu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-link" onclick="setActive(this)">
                    <i class="fas fa-tachometer-alt"></i>
                    Dashboard
                </a>
            </li>

            <li class="nav-item dropdown" onclick="toggleDropdown(this)">
                <a href="#" class="nav-link dropdown-toggle">
                    <i class="fas fa-tasks"></i>
                    İşlemler
                </a>
                <ul class="dropdown-menu">
                    <li><a href="${pageContext.request.contextPath}/islem-kayit" class="dropdown-item">
                        <i class="fas fa-plus-circle"></i>İşlem Kayıt
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/islem-listele" class="dropdown-item">
                        <i class="fas fa-list"></i>İşlem Listele
                    </a></li>
                </ul>
            </li>

            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/hemsire" class="nav-link" onclick="setActive(this)">
                    <i class="fas fa-user-nurse"></i>
                    Hemşireler
                </a>
            </li>

            <li class="nav-item dropdown" onclick="toggleDropdown(this)">
                <a href="#" class="nav-link dropdown-toggle">
                    <i class="fas fa-chart-bar"></i>
                    Raporlar
                </a>
                <ul class="dropdown-menu">
                    <li><a href="${pageContext.request.contextPath}/rapor" class="dropdown-item">
                        <i class="fas fa-file-plus"></i>Rapor Oluştur
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/rapor?action=liste" class="dropdown-item">
                        <i class="fas fa-folder-open"></i>Raporlarım
                    </a></li>
                </ul>
            </li>
        </ul>

        <!-- User Menu -->
        <div class="user-menu dropdown" onclick="toggleDropdown(this)">
            <div class="user-info dropdown-toggle">
                <div class="user-avatar">
                    <%= session.getAttribute("kullaniciAdi") != null ?
                            session.getAttribute("kullaniciAdi").toString().substring(0,1).toUpperCase() : "U" %>
                </div>
                <span class="user-name"><%= session.getAttribute("kullaniciAdi") %></span>
                <div class="status-indicator"></div>
            </div>
            <ul class="dropdown-menu">
                <li><a href="logout" class="dropdown-item">
                    <i class="fas fa-sign-out-alt"></i>Çıkış Yap
                </a></li>
            </ul>
        </div>

        <!-- Mobile Menu Toggle -->
        <button class="mobile-menu-toggle" onclick="toggleMobileMenu()">
            <i class="fas fa-bars"></i>
        </button>
    </div>

    <!-- Mobile Menu -->
    <div class="mobile-menu" id="mobileMenu">
        <div class="mobile-nav-item">
            <a href="${pageContext.request.contextPath}/dashboard" class="mobile-nav-link">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
        </div>

        <div class="mobile-nav-item">
            <div class="mobile-nav-link" onclick="toggleMobileDropdown(this)">
                <i class="fas fa-tasks"></i> İşlemler <i class="fas fa-chevron-down" style="margin-left: auto;"></i>
            </div>
            <div class="mobile-dropdown-items">
                <a href="${pageContext.request.contextPath}/islem-kayit" class="mobile-nav-link">
                    <i class="fas fa-plus-circle"></i> İşlem Kayıt
                </a>
                <a href="${pageContext.request.contextPath}/islem-listele" class="mobile-nav-link">
                    <i class="fas fa-list"></i> İşlem Listele
                </a>
            </div>
        </div>

        <div class="mobile-nav-item">
            <a href="${pageContext.request.contextPath}/hemsire" class="mobile-nav-link">
                <i class="fas fa-user-nurse"></i> Hemşireler
            </a>
        </div>

        <div class="mobile-nav-item">
            <div class="mobile-nav-link" onclick="toggleMobileDropdown(this)">
                <i class="fas fa-chart-bar"></i> Raporlar <i class="fas fa-chevron-down" style="margin-left: auto;"></i>
            </div>
            <div class="mobile-dropdown-items">
                <a href="rapor" class="mobile-nav-link">
                    <i class="fas fa-file-plus"></i> Rapor Oluştur
                </a>
                <a href="rapor?action=liste" class="mobile-nav-link">
                    <i class="fas fa-folder-open"></i> Raporlarım
                </a>
            </div>
        </div>

        <div class="divider"></div>

        <div class="mobile-nav-item">
            <a href="${pageContext.request.contextPath}/logout" class="mobile-nav-link">
                <i class="fas fa-sign-out-alt"></i> Çıkış Yap
            </a>
        </div>
    </div>
</nav>

<script>

    function toggleDropdown(element) {
        event.stopPropagation();


        document.querySelectorAll('.dropdown.show').forEach(dropdown => {
            if (dropdown !== element) {
                dropdown.classList.remove('show');
            }
        });


        element.classList.toggle('show');
    }


    function toggleMobileMenu() {
        const mobileMenu = document.getElementById('mobileMenu');
        const toggleButton = document.querySelector('.mobile-menu-toggle i');

        mobileMenu.classList.toggle('show');

        if (mobileMenu.classList.contains('show')) {
            toggleButton.className = 'fas fa-times';
        } else {
            toggleButton.className = 'fas fa-bars';
        }
    }


    function toggleMobileDropdown(element) {
        const dropdownItems = element.nextElementSibling;
        const chevron = element.querySelector('.fa-chevron-down, .fa-chevron-up');

        if (dropdownItems.style.display === 'block') {
            dropdownItems.style.display = 'none';
            chevron.className = 'fas fa-chevron-down';
        } else {
            dropdownItems.style.display = 'block';
            chevron.className = 'fas fa-chevron-up';
        }
    }


    function setActive(element) {
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        element.classList.add('active');


        localStorage.setItem('activeNavItem', element.getAttribute('href'));
    }


    document.addEventListener('click', function(event) {
        if (!event.target.closest('.dropdown')) {
            document.querySelectorAll('.dropdown.show').forEach(dropdown => {
                dropdown.classList.remove('show');
            });
        }
    });


    window.addEventListener('scroll', function() {
        const navbar = document.getElementById('mainNavbar');
        if (window.scrollY > 10) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });


    document.addEventListener('DOMContentLoaded', function() {
        const activeHref = localStorage.getItem('activeNavItem');
        if (activeHref) {
            const activeLink = document.querySelector(`a[href="${activeHref}"]`);
            if (activeLink) {
                activeLink.classList.add('active');
            }
        }


        const currentPath = window.location.pathname;
        document.querySelectorAll('.nav-link').forEach(link => {
            if (link.getAttribute('href') && currentPath.includes(link.getAttribute('href'))) {
                link.classList.add('active');
            }
        });
    });


    document.querySelectorAll('.mobile-dropdown-items').forEach(items => {
        items.style.display = 'none';
        items.style.transition = 'all 0.3s ease';
    });

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            document.querySelectorAll('.dropdown.show').forEach(dropdown => {
                dropdown.classList.remove('show');
            });

            const mobileMenu = document.getElementById('mobileMenu');
            if (mobileMenu.classList.contains('show')) {
                toggleMobileMenu();
            }
        }
    });
</script>