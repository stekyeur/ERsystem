<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sayfa Bulunamadı - 404</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; background-color: #f8f9fa; }
        h1 { font-size: 60px; color: #dc3545; }
        p { font-size: 20px; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>404</h1>
<p>Üzgünüz, aradığınız sayfa bulunamadı.</p>
<p><a href="<%= request.getContextPath() %>/">Ana Sayfaya Dön</a></p>
</body>
</html>

