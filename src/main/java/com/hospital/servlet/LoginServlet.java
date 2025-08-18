package com.hospital.servlet;
import com.hospital.dao.KullaniciDAO;
import com.hospital.model.Kullanici;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private KullaniciDAO kullaniciDAO = new KullaniciDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("kullanici") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("hata", "Kullanıcı adı ve şifre gereklidir.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Kullanici kullanici = kullaniciDAO.login(username.trim(), password);

        if (kullanici != null) {
            HttpSession session = request.getSession();
            session.setAttribute("kullanici", kullanici);
            session.setAttribute("kullaniciAdi", kullanici.getAdSoyad());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("hata", "Geçersiz kullanıcı adı veya şifre.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
