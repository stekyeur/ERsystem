<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acil Servis - Giriş</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --success-gradient: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            --danger-gradient: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
            --warning-gradient: linear-gradient(135deg, #fdbb2d 0%, #22c1c3 100%);
            --dark-bg: #0a0e1a;
            --glass-bg: rgba(255, 255, 255, 0.08);
            --text-primary: #ffffff;
            --text-secondary: #a0aec0;
            --border-color: rgba(255, 255, 255, 0.1);
            --input-bg: rgba(255, 255, 255, 0.05);
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
                    radial-gradient(circle at 20% 20%, rgba(102, 126, 234, 0.15) 0%, transparent 50%),
                    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.15) 0%, transparent 50%),
                    radial-gradient(circle at 40% 40%, rgba(17, 153, 142, 0.1) 0%, transparent 50%),
                    radial-gradient(circle at 60% 20%, rgba(253, 187, 45, 0.05) 0%, transparent 50%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            position: relative;
        }

        /* Animated background particles */
        .particles {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: 1;
        }

        .particle {
            position: absolute;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: float 6s ease-in-out infinite;
        }

        .particle:nth-child(1) {
            width: 4px;
            height: 4px;
            left: 10%;
            animation-delay: 0s;
            animation-duration: 8s;
        }

        .particle:nth-child(2) {
            width: 6px;
            height: 6px;
            left: 80%;
            animation-delay: 2s;
            animation-duration: 10s;
        }

        .particle:nth-child(3) {
            width: 3px;
            height: 3px;
            left: 60%;
            animation-delay: 4s;
            animation-duration: 6s;
        }

        .particle:nth-child(4) {
            width: 5px;
            height: 5px;
            left: 30%;
            animation-delay: 1s;
            animation-duration: 12s;
        }

        .particle:nth-child(5) {
            width: 4px;
            height: 4px;
            left: 90%;
            animation-delay: 3s;
            animation-duration: 9s;
        }

        @keyframes float {
            0%, 100% {
                transform: translateY(100vh) rotate(0deg);
                opacity: 0;
            }
            10%, 90% {
                opacity: 1;
            }
            50% {
                transform: translateY(-10vh) rotate(180deg);
            }
        }

        .login-container {
            position: relative;
            z-index: 10;
            width: 100%;
            max-width: 480px;
            padding: 20px;
        }

        .login-card {
            background: var(--glass-bg);
            backdrop-filter: blur(30px);
            border: 1px solid var(--border-color);
            border-radius: 32px;
            padding: 50px 40px;
            position: relative;
            overflow: hidden;
            box-shadow: var(--shadow);
            animation: slideInUp 0.8s ease-out;
        }

        .login-card::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: conic-gradient(
                    from 0deg,
                    transparent,
                    rgba(102, 126, 234, 0.1),
                    transparent,
                    rgba(118, 75, 162, 0.1),
                    transparent
            );
            animation: rotate 20s linear infinite;
            z-index: -1;
        }

        .login-card::after {
            content: '';
            position: absolute;
            inset: 1px;
            background: var(--dark-bg);
            border-radius: 31px;
            z-index: -1;
        }

        @keyframes rotate {
            to {
                transform: rotate(360deg);
            }
        }

        @keyframes slideInUp {
            from {
                opacity: 0;
                transform: translateY(50px) scale(0.9);
            }
            to {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
        }

        .login-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .login-icon {
            width: 80px;
            height: 80px;
            background: var(--primary-gradient);
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 32px;
            color: white;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.05);
            }
        }

        .login-title {
            font-size: 2rem;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 8px;
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .login-subtitle {
            color: var(--text-secondary);
            font-size: 0.95rem;
            font-weight: 400;
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-label {
            display: block;
            font-weight: 500;
            color: var(--text-primary);
            margin-bottom: 8px;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .input-container {
            position: relative;
        }

        .form-control {
            width: 100%;
            padding: 16px 20px 16px 50px;
            background: var(--input-bg);
            border: 1px solid var(--border-color);
            border-radius: 16px;
            color: var(--text-primary);
            font-size: 1rem;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            background: rgba(255, 255, 255, 0.08);
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            transform: translateY(-2px);
        }

        .form-control::placeholder {
            color: var(--text-secondary);
            opacity: 0.7;
        }

        .input-icon {
            position: absolute;
            left: 18px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
            font-size: 16px;
            transition: color 0.3s ease;
            z-index: 1;
        }

        .form-control:focus + .input-icon {
            color: #667eea;
        }

        .login-button {
            width: 100%;
            padding: 16px;
            background: var(--primary-gradient);
            border: none;
            border-radius: 16px;
            color: white;
            font-size: 1rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            margin-top: 10px;
        }

        .login-button::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s;
        }

        .login-button:hover::before {
            left: 100%;
        }

        .login-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }

        .login-button:active {
            transform: translateY(0);
        }

        .alert {
            padding: 16px 20px;
            border-radius: 16px;
            margin-bottom: 25px;
            border: none;
            font-weight: 500;
            animation: shake 0.5s ease-in-out;
            position: relative;
            overflow: hidden;
        }

        .alert-danger {
            background: rgba(255, 65, 108, 0.1);
            color: #ff6b8a;
            border: 1px solid rgba(255, 65, 108, 0.2);
            backdrop-filter: blur(10px);
        }

        .alert-danger::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 4px;
            background: var(--danger-gradient);
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }

        .password-toggle {
            position: absolute;
            right: 18px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
            cursor: pointer;
            font-size: 16px;
            transition: color 0.3s ease;
            z-index: 1;
        }

        .password-toggle:hover {
            color: var(--text-primary);
        }

        .loading-spinner {
            display: none;
            width: 20px;
            height: 20px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border-top-color: white;
            animation: spin 1s ease-in-out infinite;
            margin-right: 10px;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .footer-text {
            text-align: center;
            margin-top: 30px;
            color: var(--text-secondary);
            font-size: 0.85rem;
        }

        .medical-bg {
            position: absolute;
            top: 20px;
            right: 20px;
            font-size: 120px;
            color: rgba(102, 126, 234, 0.05);
            z-index: 1;
        }

        @media (max-width: 640px) {
            .login-card {
                padding: 40px 30px;
                margin: 20px;
                border-radius: 24px;
            }

            .login-title {
                font-size: 1.5rem;
            }

            .login-icon {
                width: 60px;
                height: 60px;
                font-size: 24px;
            }

            .form-control {
                padding: 14px 18px 14px 45px;
            }

            .login-button {
                padding: 14px;
                font-size: 0.9rem;
            }
        }

        /* Loading state */
        .loading .login-button {
            pointer-events: none;
            opacity: 0.8;
        }

        .loading .loading-spinner {
            display: inline-block;
        }
    </style>
</head>
<body>
<div class="particles">
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
</div>

<div class="medical-bg">
    <i class="fas fa-heartbeat"></i>
</div>

<div class="login-container">
    <div class="login-card">
        <div class="login-header">
            <div class="login-icon">
                <i class="fas fa-user-md"></i>
            </div>
            <h1 class="login-title">Hoş Geldiniz</h1>
            <p class="login-subtitle">Acil Servis İş Yükü Planlama Sistemi</p>
        </div>

        <% if (request.getAttribute("hata") != null) { %>
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-triangle"></i>
            <%= request.getAttribute("hata") %>
        </div>
        <% } %>

        <form action="login" method="post" id="loginForm">
            <div class="form-group">
                <label for="username" class="form-label">Kullanıcı Adı</label>
                <div class="input-container">
                    <input type="text" class="form-control" id="username" name="username"
                           placeholder="Kullanıcı adınızı girin" required>
                    <i class="fas fa-user input-icon"></i>
                </div>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Şifre</label>
                <div class="input-container">
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Şifrenizi girin" required>
                    <i class="fas fa-lock input-icon"></i>
                    <i class="fas fa-eye password-toggle" onclick="togglePassword()"></i>
                </div>
            </div>

            <button type="submit" class="login-button">
                <div class="loading-spinner"></div>
                <span class="button-text">Giriş Yap</span>
            </button>
        </form>

        <div class="footer-text">
            <i class="fas fa-shield-alt"></i>
            Güvenli bağlantı ile korunmaktadır
        </div>
    </div>
</div>

<script>
    function togglePassword() {
        const passwordField = document.getElementById('password');
        const toggleIcon = document.querySelector('.password-toggle');

        if (passwordField.type === 'password') {
            passwordField.type = 'text';
            toggleIcon.classList.remove('fa-eye');
            toggleIcon.classList.add('fa-eye-slash');
        } else {
            passwordField.type = 'password';
            toggleIcon.classList.remove('fa-eye-slash');
            toggleIcon.classList.add('fa-eye');
        }
    }


    document.getElementById('loginForm').addEventListener('submit', function(e) {
        const form = e.target;
        const button = form.querySelector('.login-button');
        const buttonText = button.querySelector('.button-text');

        document.body.classList.add('loading');
        buttonText.textContent = 'Giriş Yapılıyor...';
    });


    document.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'translateY(-2px)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'translateY(0)';
        });
    });


    document.querySelector('.login-button').addEventListener('click', function(e) {
        const button = e.target;
        const rect = button.getBoundingClientRect();
        const ripple = document.createElement('div');
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;

        ripple.style.cssText = `
                position: absolute;
                width: ${size}px;
                height: ${size}px;
                left: ${x}px;
                top: ${y}px;
                background: rgba(255, 255, 255, 0.3);
                border-radius: 50%;
                transform: scale(0);
                animation: ripple 0.6s linear;
                pointer-events: none;
            `;

        button.appendChild(ripple);

        setTimeout(() => {
            ripple.remove();
        }, 600);
    });


    const style = document.createElement('style');
    style.textContent = `
            @keyframes ripple {
                to {
                    transform: scale(2);
                    opacity: 0;
                }
            }
        `;
    document.head.appendChild(style);


    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('username').focus();
    });
</script>
</body>
</html>