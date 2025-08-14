<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sunucu Hatası - 500</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; background-color: #fff3cd; }
        h1 { font-size: 60px; color: #856404; }
        p { font-size: 20px; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>500</h1>
<p>Sunucu tarafında beklenmeyen bir hata oluştu.</p>
<p><a href="<%= request.getContextPath() %>/">Ana Sayfaya Dön</a></p>
</body>
</html>

