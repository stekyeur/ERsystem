package com.hospital.servlet;

import com.hospital.dao.HemsireDAO;
import com.hospital.dao.IslemDAO;
import com.hospital.model.Hemsire;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class DashboardServlet extends HttpServlet {
    private IslemDAO islemDAO = new IslemDAO();
    private HemsireDAO hemsireDAO = new HemsireDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Bugünün tarihi
        Calendar cal = Calendar.getInstance();
        Date bugun = new Date(cal.getTimeInMillis());

        // Dashboard için istatistikler
        List<Object[]> gunlukIstatistikler = islemDAO.getGunlukIstatistikler(bugun);
        List<Hemsire> hemsireler = hemsireDAO.findAll();

        request.setAttribute("gunlukIstatistikler", gunlukIstatistikler);
        request.setAttribute("toplamHemsire", hemsireler.size());
        request.setAttribute("bugun", bugun);

        // Hemşire kategorilerine göre sayım
        long tecrubesizSayisi = hemsireler.stream()
                .filter(h -> h.getTecrube() != null && "T0".equals(h.getTecrube().getKategoriKodu()))
                .count();
        long ortaTecrubeliSayisi = hemsireler.stream()
                .filter(h -> h.getTecrube() != null && "T2".equals(h.getTecrube().getKategoriKodu()))
                .count();
        long tecrubeliSayisi = hemsireler.stream()
                .filter(h -> h.getTecrube() != null && "T3".equals(h.getTecrube().getKategoriKodu()))
                .count();

        request.setAttribute("tecrubesizSayisi", tecrubesizSayisi);
        request.setAttribute("ortaTecrubeliSayisi", ortaTecrubeliSayisi);
        request.setAttribute("tecrubeliSayisi", tecrubeliSayisi);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
