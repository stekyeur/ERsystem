package com.hospital.servlet;

import com.hospital.dao.HemsireDAO;
import com.hospital.model.Hemsire;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HemsireServlet extends HttpServlet {
    private HemsireDAO hemsireDAO = new HemsireDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("yeni".equals(action)) {
            // Yeni hemşire formu - Tecrübe seviyeleri artık sabit listeden seçilecek
            request.setAttribute("tecrubeSeviyeleri", new String[]{"Tecrübesiz", "Orta Tecrübeli", "Tecrübeli"});
            request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
            return;
        }

        // Hemşire listesi
        List<Hemsire> hemsireler = hemsireDAO.findAll();
        request.setAttribute("hemsireler", hemsireler);
        request.getRequestDispatcher("hemsire-liste.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kullanici") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String personelNo = request.getParameter("personelNo");
            String adSoyad = request.getParameter("adSoyad");
            String tecrubeSeviyesi = request.getParameter("tecrubeSeviyesi");
            String iseGirisTarihiStr = request.getParameter("iseGirisTarihi");

            // Hemsire nesnesini yeni constructor ile oluştur
            Hemsire hemsire = new Hemsire(personelNo, adSoyad, tecrubeSeviyesi);

            if (iseGirisTarihiStr != null && !iseGirisTarihiStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(iseGirisTarihiStr);
                hemsire.setIseGirisTarihi(utilDate);
            }

            boolean basarili = hemsireDAO.create(hemsire);

            if (basarili) {
                response.sendRedirect("hemsire?basari=1");
            } else {
                request.setAttribute("hata", "Hemşire kaydedilirken hata oluştu.");
                request.setAttribute("tecrubeSeviyeleri", new String[]{"Tecrübesiz", "Orta Tecrübeli", "Tecrübeli"});
                request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("hata", "Geçersiz veri girişi: " + e.getMessage());
            request.setAttribute("tecrubeSeviyeleri", new String[]{"Tecrübesiz", "Orta Tecrübeli", "Tecrübeli"});
            request.getRequestDispatcher("hemsire-form.jsp").forward(request, response);
        }
    }
}